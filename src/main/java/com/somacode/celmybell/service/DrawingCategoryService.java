package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.DocumentCategory;
import com.somacode.celmybell.repository.DocumentCategoryRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DrawingCategoryService {
    @Autowired
    DocumentCategoryRepository documentCategoryRepository;

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

        val drawingCategory = new DocumentCategory();
        drawingCategory.setCategory(name);

        if (parentId != null) {
            drawingCategory.setParent(documentCategoryRepository.getOne(parentId));
        }

        return documentCategoryRepository.save(drawingCategory);
    }

    public DocumentCategory update(Long drawingCategoryId, Long parentId, String name) {
        if (!documentCategoryRepository.existsById(drawingCategoryId)) {
            throw new NotFoundException("productCategoryId does not exist");
        }

        if (parentId != null) {
            if (!documentCategoryRepository.existsById(parentId)) {
                throw new NotFoundException("parentId does not exist");
            } else if (parentId.longValue() == drawingCategoryId.longValue()) {
                throw new BadRequestException("drawingCategoryId cannot be its parent");
            }
        }

        val drawingCategory = documentCategoryRepository.getOne(drawingCategoryId);
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

    public List<DocumentCategory> findAll() {
        return documentCategoryRepository.findAll();
    }

    public boolean existsById(Long id) {
        return documentCategoryRepository.existsById(id);
    }
}
