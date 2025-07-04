package com.booknet.payment_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.booknet.payment_service.Entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
     List<Order> findByEmail(String email);
}
