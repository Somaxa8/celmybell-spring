package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.entity.DocumentCategory;
import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.repository.DocumentRepository;
import com.somacode.celmybell.service.tool.StorageTool;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class DocumentService {

    @Autowired StorageTool storageTool;
    @Autowired DocumentRepository documentRepository;
    @Autowired DocumentCategoryService documentCategoryService;


    public Document create(MultipartFile file, Document.Type type, String description, String title) {
        Document document = createEntity(file, type, description, title);
        saveFile(file, document);
        return document;
    }

    private void saveFile(MultipartFile file, Document document) {
        storageTool.save(file, getFolderFromType(document.getType()), document.getName());
    }

    private Document createEntity(MultipartFile file, Document.Type type, String description, String title) {
        if (!DocumentService.isValidFile(file) || type == null) {
            throw new BadRequestException("Invalid file or type to create a document");
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String name = FilenameUtils.getBaseName(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);

        Document document = new Document();

        documentRepository.save(document);

        long millis = System.currentTimeMillis();
        document.setBaseName(name);
        document.setExtension(extension);
        String ext = extension != null && !extension.trim().isEmpty() ? "." + extension : "";
        document.setName(document.getId() + "D" + millis + ext);
        document.setDescription(description);
        document.setTitle(title);
        document.setTag(""); // task - template - stage,ASSIGNING - stage,CREATED
        document.setType(type);

        String folder = getFolderFromType(document.getType());
        document.setUrl(folder + File.separator + document.getName());

        LogService.out.debug("DOCUMENT: " + document);

        return document;
    }

    public void delete(Long id) {
        Document document = findById(id);

        storageTool.delete(getFolderFromType(document.getType()), document.getName());
        documentRepository.deleteById(id);
    }

    public ResponseEntity<Resource> findByIdAsResource(Long id) {
        Document document = findById(id);
        String extension = document.getExtension() != null && !document.getExtension().isEmpty() ? "." + document.getExtension() : "";
        String filename = document.getBaseName() + extension;
        String mediaType = "application/octet-stream";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        Resource resource = storageTool.load(getFolderFromType(document.getType()), document.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .contentType(MediaType.parseMediaType(mediaType))
                .body(resource);
    }

    public ResponseEntity<InputStreamResource> findByIdAsStream(Long id) {
        Document document = findById(id);
        Resource resource = storageTool.load(getFolderFromType(document.getType()), document.getName());

        try {
            return ResponseEntity.ok()
                    .contentType(getMimeType(document.getExtension()))
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (IOException e) {
            throw new BadRequestException("Document cannot be loaded");
        }
    }

    public Document findById(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new NotFoundException();
        }
        return documentRepository.getOne(id);
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document clone(Document document) {
        Document d = new Document();
        documentRepository.save(d);

        long millis = System.currentTimeMillis();
        d.setName(d.getId() + "D" + millis);

        d.setTag(document.getTag());
        d.setType(document.getType());
        d.setBaseName(document.getBaseName());
        d.setExtension(document.getExtension());
        d.setDescription(document.getDescription());
        documentRepository.save(d);

        Resource resource = storageTool.load(getFolderFromType(document.getType()), document.getName());
        storageTool.save(resource, getFolderFromType(d.getType()), d.getName());

        return d;
    }

    public boolean existsById(Long id) {
        return documentRepository.existsById(id);
    }

    public void relateCategory(Long id, Long documentCategoryId) {
        if (!documentCategoryService.existsById(documentCategoryId) || !existsById(id)) {
            throw new NotFoundException();
        }
        DocumentCategory documentCategory = documentCategoryService.findById(id);
        Document document = findById(id);

        if (document.getDocumentCategory() == null) {
            document.setDocumentCategory(documentCategory);
        }

        documentRepository.save(document);
    }

    public void unrelateCategory(Long id) {
        if (!existsById(id)) {
            throw new NotFoundException();
        }

        Document document = findById(id);

        document.setDocumentCategory(null);
        documentRepository.save(document);
    }

    public List<Document> findDocumentsByDocumentCategoryId(Long documentCategoryId) {
        if (!documentCategoryService.existsById(documentCategoryId)) {
            throw new NotFoundException();
        }
        return documentRepository.findDocumentsByDocumentCategory_Id(documentCategoryId);
    }

    private static MediaType getMimeType(String extension) {
        if (extension == null) {
            return MediaType.TEXT_PLAIN;
        } else if (extension.equals("png")) {
            return MediaType.IMAGE_PNG;
        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (extension.equals("gif")) {
            return MediaType.IMAGE_GIF;
        } else if (extension.equals("pdf")) {
            return MediaType.APPLICATION_PDF;
        } else if (extension.equals("xml")) {
            return MediaType.APPLICATION_XML;
        } else if (extension.equals("json")) {
            return MediaType.APPLICATION_JSON;
        } else if (extension.equals("html")) {
            return MediaType.TEXT_HTML;
        } else {
            return MediaType.TEXT_PLAIN;
        }
    }

    private static String getFolderFromType(Document.Type type) {
        if (type == null) return "";
        return "/" + type.toString().toLowerCase();
    }

    public static boolean isValidFile(MultipartFile file) {
        if (file == null || file.isEmpty() || StringUtils.cleanPath(file.getOriginalFilename()).contains("..")) {
            return false;
        }
        return true;
    }

}
