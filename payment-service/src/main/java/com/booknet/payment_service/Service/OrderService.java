package com.booknet.payment_service.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.booknet.payment_service.Repository.OrderRepository;

import jakarta.transaction.Transactional;

import com.booknet.payment_service.Entity.Order;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
