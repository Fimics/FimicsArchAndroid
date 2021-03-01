package com.example.as.api.hiconfig;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 高速缓存
 */
public class CacheManager {
    public interface KEY {
        String CONFIG = "config_key";
    }

    private Map<String, Object> mCachedMap = new HashMap<>();
    private static CacheManager instance;
    public boolean needRefreshConfig = true;

    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    private CacheManager() {
    }

    /**
     * 设置全局的内存缓存
     *
     * @param key
     * @param value
     */
    public void putMemoryCache(String key, Object value) {
        if (key == null || value == null) return;
        mCachedMap.put(key, value);
    }

    /**
     * 获取内存中的缓存
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getCache(String key, @NonNull Class<T> tClass) {
        if (key == null) {
            return null;
        }
        T value = null;
        try {
            value = (T) mCachedMap.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
