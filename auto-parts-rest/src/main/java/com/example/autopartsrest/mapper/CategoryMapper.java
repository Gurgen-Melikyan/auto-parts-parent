package com.example.autopartsrest.mapper;


import com.example.autopartscommon.entity.Category;
import com.example.autopartsrest.dto.CategoryDto;
import com.example.autopartsrest.dto.CreateCategoryRequestDto;
import com.example.autopartsrest.dto.CreateCategoryResponseDto;
import com.example.autopartsrest.dto.UpdateCategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category map(CreateCategoryRequestDto dto);

    Category mapUpdate(UpdateCategoryDto dto);


    CreateCategoryResponseDto map(Category category);

    CategoryDto mapToDto(Category entity);

}
