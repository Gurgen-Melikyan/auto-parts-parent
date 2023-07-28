package com.example.autopartsrest.service.impl;

import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Orders;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.OrdersDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.mapper.ProductMapper;
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
    private final ProductMapper productMapper;



    @Override
    public void saveOrder(int cartId, User currentUser) throws EntityNotFoundException {
        List<Product> productsInCart = cartService.getProductsInCart(cartId);
        if (productsInCart == null) {
            throw new EntityNotFoundException("product whit " + cartId + " not found");
        }
        Orders orders = new Orders();
        orders.setUser(currentUser);
        orders.setDateTime(new Date());
        orders.setProductList(productsInCart);
        cartService.emptyCart(cartId);

    }

    @Override
    public OrdersDto allOrders(User currentUser) throws EntityNotFoundException {
        double sum = 0;
        Cart cartByUserId = cartService.getCartByUserId(currentUser.getId());
        if (cartByUserId == null) {
            throw new EntityNotFoundException("cart not found");
        }
        OrdersDto ordersDto = new OrdersDto();
        List<Product> productsInCart = cartService.getProductsInCart(cartByUserId.getId());
        for (Product product : productsInCart) {
            sum += product.getPrice();
            ordersDto.setProductId(product.getId());
        }

        ordersDto.setSum(sum);
        ordersDto.setProductInCart(productMapper.mapToList(productsInCart));
        if (productsInCart.isEmpty()) {
            throw new EntityNotFoundException("cart not found");
        }
        return ordersDto;
    }

}

