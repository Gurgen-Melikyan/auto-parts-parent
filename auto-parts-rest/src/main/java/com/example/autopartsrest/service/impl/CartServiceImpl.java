package com.example.autopartsrest.service.impl;

import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.CartRepository;
import com.example.autopartscommon.repository.ProductRepository;
import com.example.autopartscommon.repository.UserRepository;
import com.example.autopartsrest.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    @Override
    public Cart getCartByUserId(int userId) {
        return cartRepository.findByUser_Id(userId);
    }

    @Override
    public void crateCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public List<Product> getProductsInCart(int cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        return cart.getProductList();
    }

    @Override
    public void emptyCart(int cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }

    @Override
    public void removeById(int id) {
        cartRepository.deleteById(id);
    }

    @Override
    public Cart addToCart(Integer productId, User user) {
        List<Product> productList = new ArrayList<>();
        Cart cart = cartRepository.findByUser_Id(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }
        Optional<Product> byId = productRepository.findById(productId);
        if (byId.isEmpty()) {
            return null;
        }
        Product product = byId.get();
        productList = cart.getProductList();
        if (productList != null) {
            productList.add(product);
            cart.setProductList(productList);
        }

        return cartRepository.save(cart);
    }

}
