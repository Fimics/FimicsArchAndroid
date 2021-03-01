package com.example.as.api.mapper;

import com.example.as.api.entity.CityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityMapper {
    List<CityEntity> getCities();
}
