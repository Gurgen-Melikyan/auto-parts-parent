package com.example.autopartsrest.service;


import com.example.autopartscommon.entity.Orders;
import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.OrdersDto;
import com.example.autopartsrest.exception.EntityNotFoundException;

public interface OrderService {

   void saveOrder(int cartId,User currentUser) throws EntityNotFoundException;

   OrdersDto allOrders(User currentUser) throws EntityNotFoundException;
}
