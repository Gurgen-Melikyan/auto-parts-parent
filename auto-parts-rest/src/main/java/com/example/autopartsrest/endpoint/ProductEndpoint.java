package com.example.autopartsrest.endpoint;

import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.entity.Product;
import com.example.autopartsrest.dto.*;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import com.example.autopartsrest.mapper.CommentMapper;
import com.example.autopartsrest.mapper.ProductMapper;
import com.example.autopartsrest.security.CurrentUser;
import com.example.autopartsrest.service.CartService;
import com.example.autopartsrest.service.CommentService;
import com.example.autopartsrest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductEndpoint {

    private final ProductService productService;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final ProductMapper productMapper;
    private final CartService cartService;


    @Value("${upload.image.path}")
    private String uploadPath;
    @Value("${site.url}")
    private String siteUrl;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody CreateProductRequestDto createProductRequestDto,
                                             @AuthenticationPrincipal CurrentUser currentUser) {
        Product product = productService.createProduct(createProductRequestDto, currentUser.getUser());
        return ResponseEntity.ok(productMapper.mapToDto(product));
    }

    @PostMapping("/comment/add")
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentRequestDto createCommentRequestDto,
                                                    @AuthenticationPrincipal CurrentUser currentUser) {
        Comments comment = commentService.createComment(createCommentRequestDto, currentUser.getUser());
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @PutMapping("/cart/{productId}")
    public Cart assignProjectToEmployee(
            @PathVariable Integer productId,
            @AuthenticationPrincipal CurrentUser currentUser) throws EntityNotFoundException {
        return cartService.addToCart(productId, currentUser.getUser());
    }


    @PostMapping("/{id}/image")
    public ResponseEntity<ProductDto> uploadImage(
            @PathVariable("id") int productId,
            @RequestParam("image") MultipartFile multipartFile) throws IOException, EntityNotFoundException {
        Product product = productService.uploadProductImage(productId, multipartFile);
        if (product != null) {
            String originalFilename = multipartFile.getOriginalFilename();
            String imgName = System.currentTimeMillis() + "_" + originalFilename;
            ProductDto productDto = productMapper.mapToDto(product);
            productDto.setImgUrl(siteUrl + "/product/getImage?imgName=" + imgName);
            return ResponseEntity.ok(productDto);
        }
        throw new EntityNotFoundException("product not found");

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
            @RequestParam(value = "size", defaultValue = "20") int size) throws EntityNotFoundException {
        List<ProductDto> productDtos = productService.getAll(page, size);
        return ResponseEntity.ok(productDtos);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") int id,
                                             @RequestBody UpdateProductDto updateProductDto,
                                             @AuthenticationPrincipal CurrentUser currentUser) throws EntityNotFoundException {
        return ResponseEntity.ok(productService.update(id, updateProductDto, currentUser.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> singleProduct(@PathVariable("id") int id) throws EntityNotFoundException {
        return ResponseEntity.ok(productMapper.mapToDto(productService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) throws EntityNotFoundException {
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> removeComment(@PathVariable("id") int id, @AuthenticationPrincipal CurrentUser currentUser) throws EntityNotFoundException, UserUnauthorizedException {
        commentService.deleteById(id, currentUser.getUser());
        return ResponseEntity.ok().build();
    }
}



