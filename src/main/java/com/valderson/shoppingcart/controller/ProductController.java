package com.valderson.shoppingcart.controller;

import com.valderson.shoppingcart.dto.response.ProductResponse;
import com.valderson.shoppingcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductResponse> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar produtos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID do produto inválido");
            }
            
            ProductResponse product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado: " + e.getMessage());
        }
    }
}
