package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.DocumentCategory;
import com.somacode.celmybell.service.DocumentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DocumentCategoryController {

    @Autowired DocumentCategoryService documentCategoryService;


    @PostMapping("/api/document-category")
    public ResponseEntity<DocumentCategory> createDocumentCategory(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentCategoryService.create(name, parentId));
    }

    @DeleteMapping("/api/document-category/{id}")
    public ResponseEntity<?> deleteDocumentCategory(@PathVariable Long id) {
        documentCategoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/public/document-category")
    public ResponseEntity<List<DocumentCategory>> getDocumentCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(documentCategoryService.findAll());
    }

    @GetMapping("/public/document-category/{id}")
    public ResponseEntity<DocumentCategory> getDocumentCategory(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(documentCategoryService.findById(id));
    }
}
