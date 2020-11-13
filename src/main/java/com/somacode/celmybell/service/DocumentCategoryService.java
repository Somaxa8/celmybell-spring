package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.entity.DocumentCategory;
import com.somacode.celmybell.repository.DocumentCategoryRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DocumentCategoryService {

    @Autowired DocumentCategoryRepository documentCategoryRepository;
    @Autowired DocumentService documentService;


    public void init() {
        create("Dibujo", null);
        create("Furries", 1L);
    }

    public DocumentCategory create(String name, Long parentId) {
        if (parentId != null && !documentCategoryRepository.existsById(parentId)) {
            throw new NotFoundException("parentId does not exist");
        }
        if (name.isEmpty() && name == "") {
            throw new IllegalArgumentException();
        }

        DocumentCategory drawingCategory = new DocumentCategory();
        drawingCategory.setCategory(name);

        if (parentId != null) {
            drawingCategory.setParent(documentCategoryRepository.getOne(parentId));
        }

        return documentCategoryRepository.save(drawingCategory);
    }

    public DocumentCategory update(Long documentCategoryId, Long parentId, String name) {
        if (parentId != null) {
            if (!documentCategoryRepository.existsById(parentId)) {
                throw new NotFoundException("parentId does not exist");
            } else if (parentId.longValue() == documentCategoryId.longValue()) {
                throw new BadRequestException("documentCategoryId cannot be its parent");
            }
        }

        DocumentCategory drawingCategory = findById(documentCategoryId);
        if (parentId == null) {
            drawingCategory.setParent(null);
        } else {
            drawingCategory.setParent(documentCategoryRepository.getOne(parentId));
        }

        if (name != null) drawingCategory.setCategory(name);

        return documentCategoryRepository.save(drawingCategory);
    }

    public DocumentCategory findById(Long id) {
        if (!documentCategoryRepository.existsById(id)) {
            throw new NotFoundException();
        }
        DocumentCategory productCategory = documentCategoryRepository.getOne(id);
        productCategory.setIsParent(!productCategory.getChildren().isEmpty());
        return productCategory;
    }

    public void delete(Long id) {
        documentCategoryRepository.deleteById(id);
    }

    public List<DocumentCategory> findAll() {
        return documentCategoryRepository.findAll();
    }

    public boolean existsById(Long id) {
        return documentCategoryRepository.existsById(id);
    }
}
