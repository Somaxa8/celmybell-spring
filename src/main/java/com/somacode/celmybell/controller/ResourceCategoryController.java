package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.ResourceCategory;
import com.somacode.celmybell.service.ResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResourceCategoryController {

    @Autowired
    ResourceCategoryService documentCategoryService;


    @PostMapping("/api/document-category")
    public ResponseEntity<ResourceCategory> postDocumentCategory(
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
    public ResponseEntity<List<ResourceCategory>> getDocumentCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(documentCategoryService.findAll());
    }

    @GetMapping("/public/document-category/{id}")
    public ResponseEntity<ResourceCategory> getDocumentCategory(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(documentCategoryService.findById(id));
    }
}
