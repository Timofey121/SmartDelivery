package com.timofey.orderservice.service;

import com.timofey.orderservice.dto.NotificationRequest;
import com.timofey.orderservice.dto.OrderRequest;
import com.timofey.orderservice.entity.Order;
import com.timofey.orderservice.mapper.OrderMapper;
import com.timofey.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final OrderMapper orderMapper;

    public Order createOrder(OrderRequest orderRequest) {
        Order order = orderRepository.save(orderMapper.OrderRequestToOrder(orderRequest));

        NotificationRequest notification = new NotificationRequest(
                "New order created: " + order.getProduct(),
                order.getCustomerName()
        );

        // Отправляем уведомление в очередь RabbitMQ
        rabbitTemplate.convertAndSend("notification.queue", notification);

        return order;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

}
