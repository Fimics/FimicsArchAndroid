package com.example.as.api.entity;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryEntity {
    /**
     * 商品类别ID
     */
    public Integer categoryId;
    /**
     * 类别名
     */
    public String categoryName;
    /**
     * 创建时间
     */
    public String createTime;

}
