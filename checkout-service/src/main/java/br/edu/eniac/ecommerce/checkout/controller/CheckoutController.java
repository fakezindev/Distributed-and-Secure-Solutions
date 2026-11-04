package br.edu.eniac.ecommerce.checkout.controller;

import br.edu.eniac.ecommerce.checkout.dto.OrderRequestDTO;
import br.edu.eniac.ecommerce.checkout.service.CheckoutService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity> createCheckout(@Valid @RequestBody OrderRequestDTO request) {
        String result = checkoutService.executeCheckout(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "orderId", request.orderId(),
                "status", "ACCEPTED",
                "message", result
        ));
    }
}