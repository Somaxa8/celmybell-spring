package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.Config;
import com.somacode.celmybell.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConfigController {

    @Autowired ConfigService configService;


    @PostMapping("/api/config")
    public ResponseEntity<Config> postConfig(@RequestBody Config config) {
        return ResponseEntity.status(HttpStatus.CREATED).body(configService.create(config));
    }

    @GetMapping("/public/config")
    public ResponseEntity<List<Config>> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(configService.findAll());
    }

    @GetMapping("/public/config/tag")
    public ResponseEntity<List<Config>> getConfigByTag(@RequestParam String tag) {
        return ResponseEntity.status(HttpStatus.OK).body(configService.findConfigByTag(tag));
    }

    @GetMapping("/public/config/key")
    public ResponseEntity<List<Config>> getConfigByKey(@RequestParam String key) {
        return ResponseEntity.status(HttpStatus.OK).body(configService.findConfigByKey(key));
    }

    @PatchMapping("/api/config/{id}")
    public ResponseEntity<Config> patchConfig(@PathVariable Long id, @RequestBody Config config) {
        return ResponseEntity.status(HttpStatus.OK).body(configService.update(id, config));
    }

    @DeleteMapping("/api/config/{id}")
    public ResponseEntity<?> patchConfig(@PathVariable Long id) {
        configService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
