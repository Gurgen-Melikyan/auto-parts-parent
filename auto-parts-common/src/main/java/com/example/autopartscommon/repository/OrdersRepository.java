package com.example.autopartscommon.repository;

import com.example.autopartscommon.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    Optional<Orders>findByUser_Id(int userId);


}
