package com.example.autopartsrest.mapper;


import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Product;
import com.example.autopartsrest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    @Value("${site.url}")
    String siteUrl;

    @Mapping(target = "category.id", source = "categoryId")
    public abstract Product map(CreateProductRequestDto dto);

    public abstract Product mapUpdate(UpdateProductDto dto);


    public abstract CreateCategoryResponseDto map(Category category);

    public abstract List<ProductDto> mapToList(List<Product> productList);

    @Mapping(target = "categoryDto", source = "category")
    @Mapping(target = "imgUrl", expression = "java(entity.getImgName() != null ? siteUrl + \"/product/getImage?picName=\" + entity.getImgName() : null)")
    public abstract ProductDto mapToDto(Product entity);

}
