package com.example.as.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityEntity {
    //id
    public String id;
    //父id
    public String pid;
    //城市的名字
    public String districtName;
    //城市的类型，0是国，1是省，2是市，3是区
    public String type;
    //地区所处的层级
    public String hierarchy;
    //层级序列
    public String districtSqe;

}
