package com.somacode.celmybell.service;


import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.entity.Resource;
import com.somacode.celmybell.entity.ResourceCategory;
import com.somacode.celmybell.repository.ResourceRepository;
import com.somacode.celmybell.repository.criteria.ResourceCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ResourceService {

    @Autowired ResourceRepository resourceRepository;
    @Autowired ResourceCriteria resourceCriteria;
    @Autowired ResourceCategoryService resourceCategoryService;
    @Autowired DocumentService documentService;

    public void init() {
    }

    public Resource create(String title, String description, MultipartFile documentFile, Long categoryId) {
        if (title.isEmpty() && title.equals("")) throw new IllegalArgumentException();
        if (description.isEmpty() && description.equals("")) throw new IllegalArgumentException();
        if (documentFile == null) throw new IllegalArgumentException();
        if (categoryId == null) throw new IllegalArgumentException();

        Resource resource = new Resource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setDocument(
                documentService.create(documentFile, Document.Type.IMAGE, Resource.class.getSimpleName(), title)
        );
        resource.setResourceCategory(resourceCategoryService.findById(categoryId));

        return resourceRepository.save(resource);
    }

    public Resource update(Long id, String title, String description, MultipartFile documentFile, Long categoryId) {
        Resource resource = findById(id);

        if (title != null) resource.setTitle(title);
        if (description != null) resource.setTitle(description);
        if (documentFile != null) {
            Document oldDocument = resource.getDocument();
            resource.setDocument(
                    documentService.create(documentFile, Document.Type.IMAGE, Resource.class.getSimpleName(), resource.getTitle())
            );
            documentService.delete(oldDocument.getId());
        }
        resource.setResourceCategory(resourceCategoryService.findById(categoryId));

        return resourceRepository.save(resource);
    }

    public Resource findById(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new NotFoundException();
        }
        return resourceRepository.getOne(id);
    }

    public Page<Resource> findFilterPageable(Integer page, Integer size, String search) {
        return resourceCriteria.findFilterPageable(page, size, search);
    }

    public void delete(Long id) {
        if (resourceRepository.existsById(id)) {
            throw new NotFoundException();
        }
        Resource resource = resourceRepository.getOne(id);
        documentService.delete(resource.getDocument().getId());
        resourceRepository.deleteById(id);
    }

}
