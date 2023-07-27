package com.example.autopartsrest.dto;

import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto {
    private int productId;
    private List<ProductDto> productInCart;
    private double sum;

}
