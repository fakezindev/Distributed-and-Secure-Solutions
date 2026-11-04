package br.edu.eniac.ecommerce.checkout.service;

import br.edu.eniac.ecommerce.checkout.config.RabbitMQConfig;
import br.edu.eniac.ecommerce.checkout.dto.OrderRequestDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);
    private final RabbitTemplate rabbitTemplate;

    public CheckoutService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @CircuitBreaker(name = "paymentBrokerCircuit", fallbackMethod = "processCheckoutFallback")
    public String executeCheckout(OrderRequestDTO order) {
        log.info("A encaminhar pedido {} para processamento assíncrono...", order.orderId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                order
        );

        return "Pedido registado com sucesso. Transação enviada para a fila de pagamentos.";
    }

    public String processCheckoutFallback(OrderRequestDTO order, Throwable throwable) {
        log.warn("Falha no broker ou sobrecarga detetada. Circuito aberto para o pedido {}. Causa: {}",
                order.orderId(), throwable.getMessage());
        return "Serviço de mensageria temporariamente indisponível. Pedido armazenado em buffer de contingência.";
    }
}
