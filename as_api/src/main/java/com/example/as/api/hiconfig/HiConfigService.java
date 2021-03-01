package com.example.as.api.hiconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class HiConfigService {
    @Autowired
    private HiConfigMapper mapper;

    public List<HiConfigModel> getConfig(String namespace) {
        List<HiConfigModel> models = mapper.getConfig(namespace);
        if (models != null && models.size() > 0) {
            CacheManager.getInstance().putMemoryCache(StringUtils.isEmpty(namespace)
                    ? CacheManager.KEY.CONFIG : namespace, models.get(0));
        }
        return models;
    }

    public List<HiConfigModel> getAllConfig() {
        List<HiConfigModel> models = mapper.getAllConfig();
        if (models == null) {
            return null;
        }
        for (HiConfigModel model : models) {
            CacheManager.getInstance().putMemoryCache(model.namespace, model);
        }
        CacheManager.getInstance().needRefreshConfig = false;
        return models;
    }

    public void saveConfig(HiConfigModel model) {
        mapper.saveConfig(model);
        CacheManager.getInstance().putMemoryCache(model.namespace, model);
    }
}
