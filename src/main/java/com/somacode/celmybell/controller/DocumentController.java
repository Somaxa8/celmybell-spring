package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class DocumentController {

    @Autowired DocumentService documentService;


    @PostMapping("/api/documents")
    public ResponseEntity<Document> postDocument(
            @RequestParam MultipartFile file,
            @RequestParam String description,
            @RequestParam String title
            ) {
        Document document = documentService.create(file, Document.Type.IMAGE, description, title);
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @GetMapping("/public/documents")
    public ResponseEntity<List<Document>> getDocuments() {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.findAll());
    }

    @GetMapping("/public/documents/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.findById(id));
    }

    @DeleteMapping("/api/documents/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/api/documents/{id}/relate/{categoryId}")
    public ResponseEntity<?> relateDocumentCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        documentService.relateCategory(id, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/api/documents/unrelate/{id}")
    public ResponseEntity<?> relateDocumentCategory(@PathVariable Long id) {
        documentService.unrelateCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}