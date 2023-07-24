package com.example.autopartsweb.service;
import com.example.autopartscommon.entity.Orders;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;

import java.util.List;

public interface OrderService {

    Orders getOrderByUserId(int userId);

    void crateOrder(Orders order);

    void emptyOrder(int orderId);

    List<Product> getProductsInOrder(int orderId);

    void saveOrder(int cartId, User currentUser);

    void removeById(int id);
}
