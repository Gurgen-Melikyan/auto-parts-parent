package com.example.autopartsrest.dto;

import com.example.autopartscommon.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductResponseDto {
    private int id;
    private String title;
    private String description;
    private Category category;
    private double price;
}
