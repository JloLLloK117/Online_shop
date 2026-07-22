package com.example.onlinestore.controller;

import com.example.onlinestore.entity.Product;
import com.example.onlinestore.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class WebController {

    private final ProductRepository productRepository;

    public WebController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public String listProducts(Model model){
        model.addAttribute("products", productRepository.findAll());
        return "products";
    }

    @PostMapping("/products")
    public String saveProduct(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam(required = false) MultipartFile image,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description != null ? description : "");
            product.setPrice(price);
            product.setCategory(category);

            if (image != null && !image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" +
                        image.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
                Path path = Paths.get("src/main/resources/static/images/" + fileName);
                Files.createDirectories(path.getParent());
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl("/images/" + fileName);
            }

            productRepository.save(product);
            redirectAttributes.addFlashAttribute("message", "Товар успешно добавлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении товара: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productRepository.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/products/update")
    public String updateProduct(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam(required = false) MultipartFile image,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            product.setName(name);
            product.setDescription(description != null ? description : "");
            product.setPrice(price);
            product.setCategory(category);

            if (image != null && !image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" +
                        image.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
                Path path = Paths.get("src/main/resources/static/images/" + fileName);
                Files.createDirectories(path.getParent());
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl("/images/" + fileName);
            }

            productRepository.save(product);
            redirectAttributes.addFlashAttribute("message", "Товар обновлён!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }

        return "redirect:/products";
    }

}
