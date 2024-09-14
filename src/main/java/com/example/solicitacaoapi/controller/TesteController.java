package com.example.solicitacaoapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teste")
public class TesteController {

    @GetMapping("/saudacao")
    public ResponseEntity<String> saudacao() {
        return ResponseEntity.ok("Olá do Swagger!");
    }
}
