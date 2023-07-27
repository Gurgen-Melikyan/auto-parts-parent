package com.example.autopartsrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private int id;
    private String title;
    private String description;
    private CategoryDto categoryDto;
    private String imgUrl;
    private double price;

}
