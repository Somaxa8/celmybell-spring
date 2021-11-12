package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.entity.Resource;
import com.somacode.celmybell.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ResourceController {

    @Autowired ResourceService resourceService;


    @PostMapping("/api/resource")
    public ResponseEntity<Resource> postResource(
            @RequestParam MultipartFile documentFile,
            @RequestParam Document.Type documentType,
            @RequestParam String description,
            @RequestParam String title
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.create(title, description, documentType, documentFile));
    }

    @GetMapping("/public/resources")
    public ResponseEntity<List<Resource>> getResources(
            @RequestParam(required = false) String search,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        Page<Resource> result = resourceService.findFilterPageable(page, size, search);
        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Count", String.valueOf(result.getTotalElements()))
                .body(result.getContent());
    }

    @GetMapping("/public/resource/{id}")
    public ResponseEntity<Resource> getResource(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findById(id));
    }

    @PatchMapping("/api/resource/{id}")
    public ResponseEntity<Resource> patchResource(
            @PathVariable Long id,
            @RequestParam(required = false) MultipartFile documentFile,
            @RequestParam(required = false) Document.Type documentType,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String title
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                resourceService.update(id, title, description, documentType, documentFile)
        );
    }

    @DeleteMapping("/api/resource/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}