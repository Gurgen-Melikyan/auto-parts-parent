package com.example.autopartsweb.service;


import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
     List<Product> findAllProducts();

    Product findById(int id);
    Product findByUserId(int id);

    void save(Product product);

    Page<Product> findAll(Pageable pageable);

    void addProduct(User currentUser, MultipartFile multipartFile, Product product) throws IOException;
    void updateProduct(int id,User currentUser, MultipartFile multipartFile, Product product) throws IOException;
    void deleteById(int id);
}
