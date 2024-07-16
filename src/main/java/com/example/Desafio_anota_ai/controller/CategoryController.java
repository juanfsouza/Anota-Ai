package com.example.Desafio_anota_ai.controller;

import com.example.Desafio_anota_ai.domain.category.Category;
import com.example.Desafio_anota_ai.domain.category.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @PostMapping
    public ResponseEntity<Category> insert(@RequestBody CategoryDTO categoryData){

    }
}
