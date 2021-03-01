package com.example.as.api.controller;

import com.example.as.api.entity.ResponseEntity;
import com.example.as.api.hiconfig.HiConfigFileUtil;
import com.example.as.api.hiconfig.HiConfigModel;
import com.example.as.api.hiconfig.HiConfigService;
import com.example.as.api.util.DataUtil;
import com.example.as.api.util.ResponseCode;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/config")
@Api(tags = {"HiConfig"})
public class HiConfigController {
    @Autowired
    private HiConfigService mService;

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestParam("namespace") String namespace
            , @RequestParam("config") String config) {
        if (StringUtils.isEmpty(namespace) || StringUtils.isEmpty(config)) {
            return ResponseEntity.of(ResponseCode.RC_CONFIG_INVALID);
        }
        HiConfigModel model = HiConfigModel.of(namespace, config);
        HiConfigFileUtil.saveContent(model);
        mService.saveConfig(model);
        return ResponseEntity.success(model);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getConfig(@RequestParam(value = "namespace", required = false) String namespace
            , @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex
            , @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<HiConfigModel> models;
        if (StringUtils.isEmpty(namespace)) {
            models = mService.getAllConfig();
        } else {
            models = mService.getConfig(namespace);
        }
        return ResponseEntity.success(DataUtil.getPageData(models));
    }

}
