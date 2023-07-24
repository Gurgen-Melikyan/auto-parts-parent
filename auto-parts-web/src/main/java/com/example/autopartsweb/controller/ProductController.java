package com.example.autopartsweb.controller;


import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.entity.Product;
import com.example.autopartsweb.security.CurrentUser;
import com.example.autopartsweb.service.CartService;
import com.example.autopartsweb.service.CategoryService;
import com.example.autopartsweb.service.CommentService;
import com.example.autopartsweb.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CommentService commentService;
    private final CartService cartService;


    @GetMapping()
    public String ProductsPage(@AuthenticationPrincipal CurrentUser currentUser,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               ModelMap modelMap) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Product> result = productService.findAll(pageable);
        int totalPages = result.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("products", result);
        modelMap.addAttribute("user", currentUser.getUser());
        return "products";
    }


    @GetMapping("/add")
    public String addProductPage(ModelMap modelMap) {
        List<Category> allCategories = categoryService.findAllCategories();
        modelMap.addAttribute("categories", allCategories);
        return "addProduct";
    }

    @PostMapping("/add")
    public String addCategory(@AuthenticationPrincipal CurrentUser currentUser,
                              @ModelAttribute Product product,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {
        productService.addProduct(currentUser.getUser(), multipartFile, product);
        return "redirect:/products";
    }

    @GetMapping("/remove")
    public String removeProduct(@RequestParam("id") int id) {
            productService.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") int id, ModelMap modelMap) {
        Product product = productService.findById(id);
        modelMap.addAttribute("product", product);
        modelMap.addAttribute("categories", categoryService.findAllCategories());
        return "updateProduct";
    }

    @PostMapping("/edit")
    public String updatePage(@RequestParam("id") int id, @ModelAttribute Product product,
                             @AuthenticationPrincipal CurrentUser currentUser,
                             @RequestParam("image") MultipartFile multipartFile) throws IOException {
        productService.updateProduct(id, currentUser.getUser(), multipartFile, product);
        return "redirect:/products";
    }


    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("prod") List<Integer> products
            , @AuthenticationPrincipal CurrentUser currentUser) {
        cartService.addToCart(products, currentUser.getUser());
        return "redirect:/products";
    }


    @GetMapping("/{id}")
    public String singleProductPage(@PathVariable("id") int id, ModelMap modelMap) {
        Product byId = productService.findById(id);
        if (byId != null) {
            List<Comments> commentsList = commentService.findAllByProduct_Id(byId.getId());
            modelMap.addAttribute("product", byId);
            modelMap.addAttribute("commentsList", commentsList);
            return "singleItem";
        } else {
            return "redirect:/products";
        }

    }

    @PostMapping("/comment/add")
    public String addComment(@ModelAttribute Comments comment,
                             @AuthenticationPrincipal CurrentUser currentUser) {
        commentService.save(currentUser.getUser(), comment);
        return "redirect:/products/" + comment.getProduct().getId();
    }

    @GetMapping("/comment/remove")
    public String removeComment(@RequestParam("id") int id) {
        Comments comment = commentService.findById(id);
        commentService.deleteById(id);
        return "redirect:/products/" + comment.getProduct().getId();
    }
}
