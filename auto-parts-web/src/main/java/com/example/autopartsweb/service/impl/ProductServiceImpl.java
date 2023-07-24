package com.example.autopartsweb.service.impl;


import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.ProductRepository;
import com.example.autopartsweb.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Value("${autoPartsStore.upload.image.path}")
    private String imageUploadPath;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        return byId.get();
    }

    @Override
    public Product findByUserId(int id) {
        return productRepository.findByUser_Id(id);

    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public void addProduct(User currentUser, MultipartFile multipartFile, Product product) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = System.nanoTime() + "_" + multipartFile.getOriginalFilename();
            File file = new File(imageUploadPath + fileName);
            multipartFile.transferTo(file);
            product.setImgName(fileName);
        }
        product.setUser(currentUser);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(int id, User currentUser, MultipartFile multipartFile, Product product) throws IOException {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            return;
        }
        Product productFromDb = byId.get();
        if (productFromDb.getUser().getId() != currentUser.getId()) {
            return;
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = System.nanoTime() + "_" + multipartFile.getOriginalFilename();
            File file = new File(imageUploadPath + fileName);
            multipartFile.transferTo(file);
            productFromDb.setImgName(fileName);
        }

        product.setId(id);
        productFromDb.setCategory(product.getCategory());
        product.setUser(currentUser);
        if (product.getTitle() != null) {
            productFromDb.setTitle(product.getTitle());
        }
        productFromDb.setPrice(product.getPrice());
        productRepository.save(productFromDb);
    }


    @Override
    public void deleteById(int id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            return;
        }
        productRepository.deleteById(id);
    }
}
