package com.example.autopartsweb.service;


import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;

import java.util.List;

public interface CartService {
    void addToCart(List<Integer> products, User currentUser);

    Cart getCartByUserId(int userId);

    void crateCart(Cart cart);

    List<Product> getProductsInCart(int cartId);

    void emptyCart(int cartId);

    void removeById(int id);
}
