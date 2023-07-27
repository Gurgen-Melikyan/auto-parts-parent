package com.example.autopartsrest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductDto {
    private String title;
    private String description;
    private int categoryId;
    private double price;
    private String imgUrl;
}
