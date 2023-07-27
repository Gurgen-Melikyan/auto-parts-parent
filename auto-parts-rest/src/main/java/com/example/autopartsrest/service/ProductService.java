package com.example.autopartsrest.service;

import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Optional<Product> findByName(String name);
    Product findById(int id);
    Page<Product> findAll(Pageable pageable);

    Product save(Product product);
    void deleteById(int id);

    boolean existsById(int id);
}
