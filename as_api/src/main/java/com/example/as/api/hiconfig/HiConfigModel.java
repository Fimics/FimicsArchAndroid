package com.example.as.api.hiconfig;

import com.example.as.api.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(value = {"contentMap"})
public class HiConfigModel {
    public Map<String, String> contentMap;
    /**
     * 配置ID
     */
    @ApiModelProperty(value = "订单ID", example = "123")
    public int id;
    /**
     * 命名空间
     */
    public String namespace;
    /**
     * 版本号
     */
    public String version;
    /**
     * 创建时间
     */
    public String createTime;
    /**
     * 原始配置下载地址
     */
    public String originalUrl;
    /**
     * json类型数据下载地址
     */
    public String jsonUrl;
    public String content;

    public static HiConfigModel of(String namespace, String content) {
        HiConfigModel model = new HiConfigModel();
        model.namespace = namespace;
        model.createTime = DateUtil.currentDate();
        model.version = HiConfigUtil.genVersion();
        model.content = content;
        model.contentMap = parseContent(content);
        return model;
    }

    /**
     * 将xxx=xx格式的数据转换成Map
     *
     * @param content
     * @return
     */
    private static Map<String, String> parseContent(@NonNull String content) {
        String[] items = content.split("\n");
        Map<String, String> contentMap = new HashMap<>();
        for (String item : items) {
            String[] kvs = item.split("=");
            if (kvs.length > 1) {
                contentMap.put(kvs[0], kvs[1]);
            }
        }
        return contentMap;
    }
}
