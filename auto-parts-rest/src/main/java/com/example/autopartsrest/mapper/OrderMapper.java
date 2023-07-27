package com.example.autopartsrest.mapper;

import com.example.autopartscommon.entity.Orders;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.CreateUserRequestDto;
import com.example.autopartsrest.dto.OrdersDto;
import com.example.autopartsrest.dto.ProductDto;
import com.example.autopartsrest.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {



    List<OrdersDto> mapToDto(List<ProductDto> entity);
}
