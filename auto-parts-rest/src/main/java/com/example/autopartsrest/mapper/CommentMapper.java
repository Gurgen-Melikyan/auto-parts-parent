package com.example.autopartsrest.mapper;


import com.example.autopartscommon.entity.Category;
import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.entity.Product;
import com.example.autopartsrest.dto.CommentDto;
import com.example.autopartsrest.dto.CreateCategoryResponseDto;
import com.example.autopartsrest.dto.CreateCommentRequestDto;
import com.example.autopartsrest.dto.UpdateProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {



    Comments map(CreateCommentRequestDto dto);

    Product mapUpdate(UpdateProductDto dto);

    CreateCategoryResponseDto map(Category category);

    @Mapping(target = "productDto", source = "product")
    @Mapping(target = "userDto", source = "user")
    CommentDto mapToDto(Comments entity);

}
