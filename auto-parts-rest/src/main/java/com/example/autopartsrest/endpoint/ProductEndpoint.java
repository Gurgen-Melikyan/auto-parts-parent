package com.example.autopartsrest.endpoint;

import com.example.autopartscommon.entity.*;
import com.example.autopartscommon.repository.CartRepository;
import com.example.autopartscommon.repository.CommentsRepository;
import com.example.autopartscommon.repository.CurrencyRepository;
import com.example.autopartscommon.repository.ProductRepository;
import com.example.autopartsrest.dto.*;
import com.example.autopartsrest.mapper.CommentMapper;
import com.example.autopartsrest.mapper.ProductMapper;
import com.example.autopartsrest.security.CurrentUser;
import com.example.autopartsrest.service.*;
import com.example.autopartsrest.util.RoundUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductEndpoint {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final ProductMapper productMapper;
    private final CartService cartService;
    private final CurrencyService currencyService;

    @Value("${upload.image.path}")
    private String uploadPath;
    @Value("${site.url}")
    private String siteUrl;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody CreateProductRequestDto createProductRequestDto,
                                             @AuthenticationPrincipal CurrentUser currentUser) {
        Category byId = categoryService.findById(createProductRequestDto.getCategoryId());
        if (byId == null) {
            return ResponseEntity.badRequest().build();
        }
        Product product = productMapper.map(createProductRequestDto);
        product.setCategory(byId);
        product.setUser(currentUser.getUser());
        productService.save(product);
        return ResponseEntity.ok(productMapper.mapToDto(product));
    }

    @PostMapping("/comment/add")
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentRequestDto createCommentRequestDto,
                                                    @AuthenticationPrincipal CurrentUser currentUser) {
        Product byId = productService.findById(createCommentRequestDto.getProductId());
        if (byId == null) {
            return ResponseEntity.badRequest().build();
        }
        Comments comment = commentMapper.map(createCommentRequestDto);
        comment.setUser(currentUser.getUser());
        comment.setProduct(byId);
        commentService.save(comment);
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @PutMapping("/cart/{productId}")
    public Cart assignProjectToEmployee(
            @PathVariable Integer productId,
            @AuthenticationPrincipal CurrentUser currentUser) {
        return cartService.addToCart(productId, currentUser.getUser());
    }


    @PostMapping("/{id}/image")
    public ResponseEntity<ProductDto> uploadImage(
            @PathVariable("id") int productId,
            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        Product productOptional = productService.findById(productId);

        if (!multipartFile.isEmpty() && productOptional != null) {
            String originalFilename = multipartFile.getOriginalFilename();
            String imgName = System.currentTimeMillis() + "_" + originalFilename;
            File file = new File(uploadPath + imgName);
            multipartFile.transferTo(file);
            Product product = productOptional;
            product.setImgName(imgName);
            productService.save(product);
            ProductDto productDto = productMapper.mapToDto(product);
            productDto.setImgUrl(siteUrl + "/product/getImage?imgName=" + imgName);
            return ResponseEntity.ok(productDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("imgName") String picName) throws IOException {
        File file = new File(uploadPath + picName);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            return IOUtils.toByteArray(fis);
        }
        return null;
    }


    @GetMapping
    private ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Product> result = productService.findAll(pageable);

        List<Product> all = result.getContent();
        if (all.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        List<ProductDto> productDtos = productMapper.mapToList(all);
        List<Currency> currencies = currencyService.findAll();
        if (currencies != null && !currencies.isEmpty()) {
            Currency currency = currencies.get(0);
            for (ProductDto productDto : productDtos) {
                double price = productDto.getPrice();
                productDto.setPriceRUB(RoundUtil.round(price / currency.getRub(), 1));
                productDto.setPriceUSD(RoundUtil.round(price / currency.getUsd(), 2));
            }
        }

        return ResponseEntity.ok(productDtos);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") int id,
                                             @RequestBody UpdateProductDto updateProductDto,
                                             @AuthenticationPrincipal CurrentUser currentUser) {
        Product byId = productService.findById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        Product productFromDb = byId;
        if (productFromDb.getUser().getId() != currentUser.getUser().getId()) {
            return ResponseEntity.notFound().build();
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
        product.setUser(currentUser.getUser());

        return ResponseEntity.ok(productMapper.mapToDto(productService.save(productFromDb)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> singleProduct(@PathVariable("id") int id) {
        Product byId = productService.findById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        Product product = byId;
        return ResponseEntity.ok(productMapper.mapToDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        if (productService.existsById(id)) {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> removeComment(@PathVariable("id") int id, @AuthenticationPrincipal CurrentUser currentUser) {
        Comments byId = commentService.findById(id);
        if (byId != null) {
            return ResponseEntity.notFound().build();
        }
        if (byId.getUser().getId() != currentUser.getUser().getId() && !currentUser.getUser().getRole().equals(Role.ADMIN)) {
            return ResponseEntity.badRequest().build();
        }
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}



