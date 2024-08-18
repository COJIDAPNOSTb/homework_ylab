package org.example.app.mapper;

import org.example.app.dto.CarDTO;
import org.example.app.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDTO toDTO(Car car);
    Car toEntity(CarDTO carDTO);
}
