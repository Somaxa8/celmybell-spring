package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.entity.Resource;
import com.somacode.celmybell.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ResourceController {

    @Autowired ResourceService resourceService;


    @PostMapping("/api/resource-category/{categoryId}/resource")
    public ResponseEntity<Resource> postResource(
            @PathVariable Long categoryId,
            @RequestParam MultipartFile documentFile,
            @RequestParam String description,
            @RequestParam String title
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                resourceService.create(title, description, documentFile, categoryId)
        );
    }

    @GetMapping("/public/resources")
    public ResponseEntity<List<Resource>> getResources() {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findAll());
    }

    @GetMapping("/public/resource/{id}")
    public ResponseEntity<Resource> getResource(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findById(id));
    }

    @PatchMapping("/api/resource-category/{categoryId}/resource/{id}")
    public ResponseEntity<Resource> patchResource(
            @PathVariable Long id,
            @PathVariable Long categoryId,
            @RequestParam MultipartFile documentFile,
            @RequestParam String description,
            @RequestParam String title
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                resourceService.update(id, title, description, documentFile, categoryId)
        );
    }

    @DeleteMapping("/api/resource/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}