package com.example.as.api.hiconfig;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiConfigMapper {
    List<HiConfigModel> getConfig(String namespace);

    List<HiConfigModel> getAllConfig();

    void saveConfig(HiConfigModel model);
}
