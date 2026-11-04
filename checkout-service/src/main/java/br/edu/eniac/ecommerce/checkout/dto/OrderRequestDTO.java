package br.edu.eniac.ecommerce.checkout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

public record OrderRequestDTO(
        @NotBlank(message = "O identificador do pedido é obrigatório")
        String orderId,

        @NotBlank(message = "O identificador do cliente é obrigatório")
        String customer,

        @NotNull(message = "O valor total do pedido é obrigatório")
        @Positive(message = "O total do pedido deve ser maior que zero")
        BigDecimal total
) implements Serializable {}