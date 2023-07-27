package com.example.autopartsrest.service.impl;

import com.example.autopartscommon.entity.Orders;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.OrdersRepository;
import com.example.autopartsrest.service.CartService;
import com.example.autopartsrest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartService cartService;
    private final OrdersRepository ordersRepository;

    @Override
    public void saveOrder(int cartId, User currentUser) {
        List<Product> productsInCart = cartService.getProductsInCart(cartId);
        Orders orders = new Orders();
        orders.setUser(currentUser);
        orders.setDateTime(new Date());
        orders.setProductList(productsInCart);
        cartService.emptyCart(cartId);
        ordersRepository.save(orders);
    }
}
