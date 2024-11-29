package com.jmo.devel.orders.repository;

import com.jmo.devel.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer> {
}
