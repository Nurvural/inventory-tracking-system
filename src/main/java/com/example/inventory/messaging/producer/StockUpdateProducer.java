package com.example.inventory.messaging.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.inventory.config.RabbitMQConfig;
import com.example.inventory.dto.message.StockUpdateMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockUpdateProducer {

	private final RabbitTemplate rabbitTemplate;

	public void sendStockUpdate(Long productId, int quantity) {
		StockUpdateMessage message = new StockUpdateMessage(productId, quantity);
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.STOCK_EXCHANGE,
				RabbitMQConfig.STOCK_ROUTING_KEY,
				message
		);
		log.info("📤 Gönderilen stok mesajı → ProductId: {}, Quantity: {}", productId, quantity);
	}

}