package com.example.autopartsweb.service.impl;


import com.example.autopartscommon.entity.Orders;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.OrdersRepository;
import com.example.autopartsweb.service.CartService;
import com.example.autopartsweb.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository ordersRepository;
    private final CartService cartService;

    @Override
    public Orders getOrderByUserId(int userId) {
        return ordersRepository.findByUser_Id(userId).orElseThrow(() -> new RuntimeException("order not found"));
    }

    @Override
    public void crateOrder(Orders order) {
        ordersRepository.save(order);
    }

    @Override
    public void emptyOrder(int orderId) {
        Optional<Orders> byId = ordersRepository.findById(orderId);
        byId.ifPresent(ordersRepository::delete);
    }

    @Override
    public List<Product> getProductsInOrder(int orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("order not found"));
        return orders.getProductList();
    }

    @Override
    public void saveOrder(int cartId, User currentUser) {
        List<Product> productsInCart = cartService.getProductsInCart(cartId);
        Orders orders = new Orders();
        orders.setUser(currentUser);
        orders.setDateTime(new Date());
        orders.setProductList(productsInCart);
        cartService.emptyCart(cartId);

    }


    @Override
    public void removeById(int id) {
        ordersRepository.deleteById(id);
    }
}
