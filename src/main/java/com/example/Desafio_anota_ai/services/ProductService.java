package com.example.Desafio_anota_ai.services;

import com.example.Desafio_anota_ai.domain.category.Category;
import com.example.Desafio_anota_ai.domain.category.CategoryDTO;
import com.example.Desafio_anota_ai.domain.category.exceptions.CategoryNotFoundException;
import com.example.Desafio_anota_ai.domain.product.Product;
import com.example.Desafio_anota_ai.domain.product.ProductDTO;
import com.example.Desafio_anota_ai.domain.product.exceptions.ProductNotFoundException;
import com.example.Desafio_anota_ai.repositories.CategoryRepository;
import com.example.Desafio_anota_ai.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private CategoryService categoryService;

    private ProductRepository repository;

    public ProductService(CategoryService categoryService, ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.repository = productRepository;
    }

    public Product insert(ProductDTO productData) {
        Category category = this.categoryService.getById(productData.categoryId()).orElseThrow(CategoryNotFoundException::new);
        Product newProduct = new Product(productData);
        newProduct.setCategory(category);
        this.repository.save(newProduct);
        return newProduct;
    }

    public Product update(String id, ProductDTO productData) {
        Product product = this.repository.findById(id).orElseThrow(ProductNotFoundException::new);

        this.categoryService.getById(productData.categoryId()).ifPresent(product::setCategory);

        if(!productData.title().isEmpty()) product.setTitle(productData.title());
        if(!productData.description().isEmpty()) product.setDescription(productData.description());
        if(!(productData.price() == null)) product.setPrice(productData.price());

        this.repository.save(product);

        return product;
    }

    public void delete(String id) {
        Product product = this.repository.findById(id).orElseThrow(ProductNotFoundException::new);

        this.repository.delete(product);
    }

    public List<Product> getAll(){
        return this.repository.findAll();
    }
}
