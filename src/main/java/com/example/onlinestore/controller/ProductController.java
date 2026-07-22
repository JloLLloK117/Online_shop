package com.example.onlinestore.controller;

import com.example.onlinestore.entity.Product;
import com.example.onlinestore.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product product) {
        if(product.getId() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID не должен указываться при создании");
        }
        return productRepository.save(product);
    }

    @PostMapping("/api/products/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        if(!productRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Товар с ID " + id + " не найден");
        }
        if(product.getId() != null && !product.getId().equals(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID в пути и теле не совпадают");
        }
        product.setId(id);
        return productRepository.save(product);
    }

    @DeleteMapping("/api/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        if(!productRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Товар с ID " + id + " не найден");
        }
        productRepository.deleteById(id);
    }

}
