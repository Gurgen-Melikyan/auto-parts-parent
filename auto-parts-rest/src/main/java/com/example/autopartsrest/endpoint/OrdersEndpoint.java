package com.example.autopartsrest.endpoint;

import com.example.autopartsrest.dto.OrdersDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.security.CurrentUser;
import com.example.autopartsrest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersEndpoint {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrdersDto> allOrders(@AuthenticationPrincipal CurrentUser currentUser) throws EntityNotFoundException {
        OrdersDto ordersDto = orderService.allOrders(currentUser.getUser());
        return ResponseEntity.ok(ordersDto);
    }

    @PostMapping("/add/{cartId}")
    private ResponseEntity<?> addProduct(@PathVariable("cartId") int cartId,
                                         @AuthenticationPrincipal CurrentUser currentUser) throws EntityNotFoundException {
        orderService.saveOrder(cartId, currentUser.getUser());
        return ResponseEntity.ok().build();
    }
}



