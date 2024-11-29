package com.jmo.devel.customers.repository;

import com.jmo.devel.customers.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Integer> {
}
