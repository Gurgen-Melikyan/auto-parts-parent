package com.example.autopartsrest.service.impl;


import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Currency;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.CategoryRepository;
import com.example.autopartscommon.repository.CurrencyRepository;
import com.example.autopartscommon.repository.ProductRepository;
import com.example.autopartsrest.dto.CreateProductRequestDto;
import com.example.autopartsrest.dto.ProductDto;
import com.example.autopartsrest.dto.UpdateProductDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.mapper.ProductMapper;
import com.example.autopartsrest.service.CategoryService;
import com.example.autopartsrest.service.ProductService;
import com.example.autopartsrest.util.RoundUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final CurrencyRepository currencyRepository;

    @Value("${upload.image.path}")
    private String uploadPath;
    @Value("${site.url}")
    private String siteUrl;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public Product uploadProductImage(int productId, MultipartFile multipartFile) throws IOException, IOException, EntityNotFoundException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("product not found");
        }
        if (!multipartFile.isEmpty()) {
            String originalFilename = multipartFile.getOriginalFilename();
            String imgName = System.currentTimeMillis() + "_" + originalFilename;
            File file = new File(uploadPath + imgName);
            multipartFile.transferTo(file);
            Product product = productOptional.get();
            product.setImgName(imgName);
            productRepository.save(product);
            return product;
        }
        return null;
    }

    @Override
    public Optional<Product> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Product findById(int id) throws EntityNotFoundException {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Product with " + id + " id does not exists");
        }
        return byId.get();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product createProduct(CreateProductRequestDto createProductRequestDto, User currentUser) {
        Category byId = categoryService.findById(createProductRequestDto.getCategoryId());
        if (byId == null) {
            throw new IllegalArgumentException("Invalid category ID");
        }

        Product product = productMapper.map(createProductRequestDto);
        product.setCategory(byId);
        product.setUser(currentUser);
        productRepository.save(product);

        return product;
    }


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(int id) throws EntityNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> getAll(int page, int size) throws EntityNotFoundException {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Product> result = productRepository.findAll(pageable);

        List<Product> all = result.getContent();
        if (all.size() == 0) {
            throw new EntityNotFoundException("Product not found");
        }
        List<ProductDto> productDtos = productMapper.mapToList(all);
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies != null && !currencies.isEmpty()) {
            Currency currency = currencies.get(0);
            for (ProductDto productDto : productDtos) {
                double price = productDto.getPrice();
                productDto.setPriceRUB(RoundUtil.round(price / currency.getRub(), 1));
                productDto.setPriceUSD(RoundUtil.round(price / currency.getUsd(), 2));
            }
            return productDtos;
        }
        throw new EntityNotFoundException("currency not found");

    }


    @Override
    public boolean existsById(int id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public ProductDto update(int id, UpdateProductDto updateProductDto, User currentUser) throws EntityNotFoundException {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("product with " + id + " not found");
        }
        Product productFromDb = byId.get();
        if (productFromDb.getUser().getId() != currentUser.getId()) {
            throw new EntityNotFoundException("you can chane yours");

        }
        Product product = productMapper.mapUpdate(updateProductDto);
        if (product.getTitle() != null) {
            productFromDb.setTitle(product.getTitle());
        }
        if (product.getCategory() != null) {
            productFromDb.setCategory(product.getCategory());
        }
        if (product.getDescription() != null) {
            productFromDb.setDescription(product.getDescription());
        }
        if (product.getPrice() != 0) {
            productFromDb.setPrice(product.getPrice());
        }
        if (product.getImgName() != null) {
            productFromDb.setImgName(product.getImgName());
        }
        product.setUser(currentUser);
        return productMapper.mapToDto(productRepository.save(productFromDb));
    }
}
