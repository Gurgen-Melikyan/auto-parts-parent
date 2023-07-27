package com.example.autopartsrest.endpoint;

import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Product;
import com.example.autopartsrest.dto.OrdersDto;
import com.example.autopartsrest.mapper.OrderMapper;
import com.example.autopartsrest.mapper.ProductMapper;
import com.example.autopartsrest.security.CurrentUser;
import com.example.autopartsrest.service.CartService;
import com.example.autopartsrest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersEndpoint {
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrdersDto> allOrders(@AuthenticationPrincipal CurrentUser currentUser) {
        double sum = 0;
        Cart cartByUserId = cartService.getCartByUserId(currentUser.getUser().getId());
        if (cartByUserId != null) {
            List<Product> productsInCart = cartService.getProductsInCart(cartByUserId.getId());
            for (Product product : productsInCart) {
                sum += product.getPrice();
            }

            OrdersDto ordersDto = new OrdersDto();
            ordersDto.setSum(sum);
            ordersDto.setProductInCart(productMapper.mapToList(productsInCart));
            if (!productsInCart.isEmpty()) {
                return ResponseEntity.ok(ordersDto);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add/{cartId}")
    private ResponseEntity<?> addProduct(@PathVariable("cartId") int cartId,
                                         @AuthenticationPrincipal CurrentUser currentUser) {
        orderService.saveOrder(cartId, currentUser.getUser());
        return ResponseEntity.ok().build();
    }
}



