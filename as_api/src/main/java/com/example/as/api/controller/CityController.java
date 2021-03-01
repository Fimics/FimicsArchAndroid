package com.example.as.api.controller;

import com.example.as.api.entity.CityEntity;
import com.example.as.api.entity.ResponseEntity;
import com.example.as.api.service.CityService;
import com.example.as.api.util.DataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/")
@Api(tags = {"City"})
public class CityController {
    @Autowired
    private CityService mService;

    @ApiOperation(value = "获取城市列表")
    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public ResponseEntity getCities() {
        List<CityEntity> list = mService.getCities();
        return ResponseEntity.success(DataUtil.getPageData(list));
    }
}
