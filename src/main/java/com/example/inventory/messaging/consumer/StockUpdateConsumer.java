package com.example.inventory.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.config.RabbitMQConfig;
import com.example.inventory.dto.message.StockUpdateMessage;
import com.example.inventory.enums.Status;
import com.example.inventory.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockUpdateConsumer {

	private final ProductRepository productRepository;

	@Transactional
	@RabbitListener(queues = RabbitMQConfig.STOCK_QUEUE)
	public void consumeStockUpdate(StockUpdateMessage message) {

		log.info("📥 Alınan mesaj → ProductId: {}, Quantity: {}", message.getProductId(), message.getQuantity());

		productRepository.findById(message.getProductId()).ifPresent(product -> {

			int newStock = product.getStockQuantity() - message.getQuantity();
			product.setStockQuantity(Math.max(newStock, 0));
			product.setStatus(newStock > 0 ? Status.ACTIVE : Status.INACTIVE);
			productRepository.save(product);
			log.info("✅ Stok güncellendi → {} = {}", product.getName(), newStock);

		});

	}
}
