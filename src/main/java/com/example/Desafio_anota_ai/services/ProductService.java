package com.example.Desafio_anota_ai.services;

import com.example.Desafio_anota_ai.domain.category.Category;
import com.example.Desafio_anota_ai.domain.category.CategoryDTO;
import com.example.Desafio_anota_ai.domain.category.exceptions.CategoryNotFoundException;
import com.example.Desafio_anota_ai.domain.product.Product;
import com.example.Desafio_anota_ai.domain.product.ProductDTO;
import com.example.Desafio_anota_ai.domain.product.exceptions.ProductNotFoundException;
import com.example.Desafio_anota_ai.repositories.CategoryRepository;
import com.example.Desafio_anota_ai.repositories.ProductRepository;
import com.example.Desafio_anota_ai.services.aws.AwsSnsService;
import com.example.Desafio_anota_ai.services.aws.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final CategoryService categoryService;

    private final ProductRepository repository;

    private final AwsSnsService snsService;

    public ProductService(CategoryService categoryService, ProductRepository productRepository, AwsSnsService snsService) {
        this.categoryService = categoryService;
        this.repository = productRepository;
        this.snsService = snsService;
    }

    public Product insert(ProductDTO productData) {
        Category category = this.categoryService.getById(productData.categoryId()).orElseThrow(CategoryNotFoundException::new);
        Product newProduct = new Product(productData);
        newProduct.setCategory(category);

        this.snsService.publish(new MessageDTO(newProduct.getOwnerId()));

        this.repository.save(newProduct);
        return newProduct;
    }

    public Product update(String id, ProductDTO productData) {
        Product product = this.repository.findById(id).orElseThrow(ProductNotFoundException::new);

        if(productData.categoryId() != null) {
            this.categoryService.getById(productData.categoryId()).ifPresent(product::setCategory);

        }

        if(!productData.title().isEmpty()) product.setTitle(productData.title());
        if(!productData.description().isEmpty()) product.setDescription(productData.description());
        if(!(productData.price() == null)) product.setPrice(productData.price());

        this.repository.save(product);

        this.snsService.publish(new MessageDTO(product.getOwnerId()));

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
