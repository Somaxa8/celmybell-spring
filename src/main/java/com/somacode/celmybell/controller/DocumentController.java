package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}