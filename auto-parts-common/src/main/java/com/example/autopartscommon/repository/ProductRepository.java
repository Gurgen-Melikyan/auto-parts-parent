package com.example.autopartscommon.repository;


import com.example.autopartscommon.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByUser_Id(int id);

}
