package com.example.autopartscommon.repository;


import com.example.autopartscommon.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUser_Id(int userId);



}
