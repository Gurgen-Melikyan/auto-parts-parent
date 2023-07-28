package com.example.autopartsrest.service;

import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.CreateProductRequestDto;
import com.example.autopartsrest.dto.ProductDto;
import com.example.autopartsrest.dto.UpdateProductDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();


    Product uploadProductImage(int productId, MultipartFile multipartFile) throws IOException, EntityNotFoundException;

    Optional<Product> findByName(String name);

    Product findById(int id) throws EntityNotFoundException;

    Page<Product> findAll(Pageable pageable);

    Product createProduct(CreateProductRequestDto createProductRequestDto,
                          User currentUser);

    Product save(Product product);

    void deleteById(int id) throws EntityNotFoundException;
    List<ProductDto> getAll(int page, int size) throws EntityNotFoundException;
    boolean existsById(int id);
    ProductDto update(int id, UpdateProductDto updateProductDto,User currentUser) throws EntityNotFoundException;
}
