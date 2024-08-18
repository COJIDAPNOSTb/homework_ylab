package org.example.app.mapper;

import org.example.app.dto.OrderDTO;
import org.example.app.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO toDTO(Order order);
    Order toEntity(OrderDTO orderDTO);
}
