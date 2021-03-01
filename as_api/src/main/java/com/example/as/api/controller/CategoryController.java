package com.example.as.api.controller;

import com.example.as.api.entity.CategoryEntity;
import com.example.as.api.entity.ResponseEntity;
import com.example.as.api.service.CategoryService;
import com.example.as.api.util.DataUtil;
import com.example.as.api.util.ResponseCode;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
@Api(tags = {"Category"})
public class CategoryController {
    @Autowired
    private CategoryService mService;

    @ApiOperation(value = "商品类别查询")
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity category(@RequestParam(value = "pageIndex", defaultValue = "1") @ApiParam("起始页码") int pageIndex
            , @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam("每页显示的数量") int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<CategoryEntity> list = mService.getCategoryList(pageIndex, pageSize);
        return ResponseEntity.success(DataUtil.getPageData(list));
    }

    @ApiOperation(value = "添加商品类别")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity addCategory(@RequestParam(value = "categoryName") @ApiParam("商品类别") String categoryName) {
        try {
            mService.addCategory(categoryName);
            return ResponseEntity.successMessage("操作成功");
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return ResponseEntity.of(ResponseCode.RC_ERROR).setMessage("商品类别重复");
        }
    }

    @ApiOperation(value = "删除商品类别")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity removeCategory(@ApiParam("商品类别ID") @PathVariable String id) {
        mService.removeCategory(id);
        return ResponseEntity.successMessage("删除成功");
    }

}
