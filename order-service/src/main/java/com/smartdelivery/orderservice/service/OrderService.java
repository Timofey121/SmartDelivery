package com.smartdelivery.orderservice.service;

import com.smartdelivery.orderservice.dto.NotificationRequest;
import com.smartdelivery.orderservice.dto.OrderRequest;
import com.smartdelivery.orderservice.entity.Order;
import com.smartdelivery.orderservice.mapper.OrderMapper;
import com.smartdelivery.orderservice.repository.OrderRepository;
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
        Order order = orderRepository.save(orderMapper.orderRequestToOrder(orderRequest));

        NotificationRequest notification = new NotificationRequest(
                "New order created: " + order.getProduct(),
                order.getCustomerName()
        );

        // Send notification to RabbitMQ queue
        rabbitTemplate.convertAndSend("notification.queue", notification);

        return order;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

}
