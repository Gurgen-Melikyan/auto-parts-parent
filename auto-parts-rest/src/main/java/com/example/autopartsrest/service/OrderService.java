package com.example.autopartsrest.service;


import com.example.autopartscommon.entity.User;

public interface OrderService {

   void saveOrder(int cartId,User currentUser);
}
