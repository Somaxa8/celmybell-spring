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

    @Autowired ResourceCategoryService resourceCategoryService;


    @PostMapping("/api/resource-category")
    public ResponseEntity<ResourceCategory> postResourceCategory(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceCategoryService.create(name, parentId));
    }

    @DeleteMapping("/api/resource-category/{id}")
    public ResponseEntity<?> deleteResourceCategory(@PathVariable Long id) {
        resourceCategoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/public/resource-category")
    public ResponseEntity<List<ResourceCategory>> getResourceCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(resourceCategoryService.findAll());
    }

    @GetMapping("/public/resource-category/{id}")
    public ResponseEntity<ResourceCategory> getResourceCategory(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceCategoryService.findById(id));
    }
}
