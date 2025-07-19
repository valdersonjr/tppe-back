package com.valderson.shoppingcart.controller;

import com.valderson.shoppingcart.dto.request.AddToCartRequest;
import com.valderson.shoppingcart.dto.response.CartResponse;
import com.valderson.shoppingcart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID do usuário inválido");
            }
            
            CartResponse cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar carrinho: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<?> addItemToCart(@PathVariable Long userId,
                                                      @Valid @RequestBody AddToCartRequest request) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID do usuário inválido");
            }
            
            CartResponse cart = cartService.addItemToCart(userId, request);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao adicionar item ao carrinho: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long userId,
                                                           @PathVariable Long productId) {
        try {
            if (userId == null || userId <= 0 || productId == null || productId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID do usuário ou produto inválido");
            }
            
            CartResponse cart = cartService.removeItemFromCart(userId, productId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro ao remover item do carrinho: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<?> getCartTotal(@PathVariable Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID do usuário inválido");
            }
            
            BigDecimal total = cartService.getCartTotal(userId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao calcular total do carrinho: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID do usuário inválido");
            }
            
            cartService.clearCart(userId);
            return ResponseEntity.ok("Carrinho limpo com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao limpar carrinho: " + e.getMessage());
        }
    }
}