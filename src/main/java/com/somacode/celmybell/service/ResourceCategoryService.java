package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.ResourceCategory;
import com.somacode.celmybell.repository.ResourceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResourceCategoryService {

    @Autowired ResourceCategoryRepository resourceCategoryRepository;
    @Autowired DocumentService documentService;


    public void init() {
        if (resourceCategoryRepository.count() <= 0) {
            create("Dibujo", null);
            create("Furries", 1L);
        }
    }

    public ResourceCategory create(String name, Long parentId) {
        if (parentId != null && !resourceCategoryRepository.existsById(parentId)) {
            throw new NotFoundException("parentId does not exist");
        }
        if (name.isEmpty() && name == "") {
            throw new IllegalArgumentException();
        }

        ResourceCategory resourceCategory = new ResourceCategory();
        resourceCategory.setTitle(name);

        if (parentId != null) {
            resourceCategory.setParent(resourceCategoryRepository.getOne(parentId));
        }

        return resourceCategoryRepository.save(resourceCategory);
    }

    public ResourceCategory update(Long resourceCategoryId, Long parentId, String name) {
        if (parentId != null) {
            if (!resourceCategoryRepository.existsById(parentId)) {
                throw new NotFoundException("parentId does not exist");
            } else if (parentId.longValue() == resourceCategoryId.longValue()) {
                throw new BadRequestException("documentCategoryId cannot be its parent");
            }
        }

        ResourceCategory resourceCategory = findById(resourceCategoryId);
        if (parentId == null) {
            resourceCategory.setParent(null);
        } else {
            resourceCategory.setParent(resourceCategoryRepository.getOne(parentId));
        }

        if (name != null) resourceCategory.setTitle(name);

        return resourceCategoryRepository.save(resourceCategory);
    }

    public ResourceCategory findById(Long id) {
        if (!resourceCategoryRepository.existsById(id)) {
            throw new NotFoundException();
        }
        ResourceCategory resourceCategory = resourceCategoryRepository.getOne(id);
        resourceCategory.setIsParent(!resourceCategory.getChildren().isEmpty());
        return resourceCategory;
    }

    public void delete(Long id) {
        resourceCategoryRepository.deleteById(id);
    }

    public List<ResourceCategory> findAll() {
        return resourceCategoryRepository.findAll();
    }

    public boolean existsById(Long id) {
        return resourceCategoryRepository.existsById(id);
    }
}
