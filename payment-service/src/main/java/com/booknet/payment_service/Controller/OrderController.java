package com.booknet.payment_service.Controller;

import com.booknet.payment_service.DTO.OrderDTO;
import com.booknet.payment_service.Entity.Order;
import com.booknet.payment_service.Repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setName(order.getName());
            dto.setEmail(order.getEmail());
            dto.setBookName(order.getBookName());
            dto.setPrix(String.valueOf(order.getTotalAmount()));
            dto.setPaymentMethod(order.getPaymentMethod());
            return dto;
        }).collect(Collectors.toList());
    }
     // ✅ Endpoint pour récupérer les commandes par email
    @GetMapping("/user")
    public List<Order> getOrdersByUserEmail(@RequestParam("email") String email) {
        return orderRepository.findByEmail(email);
    }
}

