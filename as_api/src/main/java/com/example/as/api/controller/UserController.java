package com.example.as.api.controller;

import com.example.as.api.config.NeedLogin;
import com.example.as.api.entity.ResponseEntity;
import com.example.as.api.entity.UserEntity;
import com.example.as.api.service.UserService;
import com.example.as.api.util.DataUtil;
import com.example.as.api.util.ResponseCode;
import com.example.as.api.util.UserRedisUtil;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 最终提供给前端使用的接口对应的方法
 */
@RestController
@RequestMapping(value = "/user")
@Api(tags = {"Account"})
public class UserController {
    @Autowired
    private UserService mUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam(value = "userName") @ApiParam("账号或手机号") String userName
            , @RequestParam(value = "password") @ApiParam("密码") String password, HttpServletRequest request) {
        List<UserEntity> list = mUserService.findUser(userName);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.of(ResponseCode.RC_ACCOUNT_INVALID);
        }
        UserEntity userEntity = null;
        for (UserEntity entity : list) {
            //判断密码是否正确
            if (bCryptPasswordEncoder.matches(password, entity.pwd)) {
                userEntity = entity;
                break;
            }
        }
        if (userEntity == null) {
            return ResponseEntity.of(ResponseCode.RC_PWD_INVALID);
        }
        if ("1".equals(userEntity.forbid)) {
            return ResponseEntity.of(ResponseCode.RC_USER_FORBID);
        }
        UserRedisUtil.addUser(redisTemplate, request.getSession(), userEntity);
        return ResponseEntity.success(UserRedisUtil.getKey(request.getSession())).setMessage("login success.");
    }

    @ApiOperation(value = "注册")
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registration(@RequestParam(value = "userName") @ApiParam("账号或手机号") String userName
            , @RequestParam(value = "password") @ApiParam("密码") String password
            , @RequestParam(value = "imoocId") @ApiParam("慕课网用户ID") String imoocId
            , @RequestParam(value = "orderId") @ApiParam("订单号") String orderId) {
        mUserService.addUser(userName, bCryptPasswordEncoder.encode(password), imoocId, orderId);
        return ResponseEntity.successMessage("registration success.");
    }

    @NeedLogin
    @ApiOperation(value = "登出")
    @RequestMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserRedisUtil.removeUser(redisTemplate, session);
        return ResponseEntity.successMessage("logout success.");
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户列表")
    public ResponseEntity getUserList(@RequestParam(value = "pageIndex", defaultValue = "1") @ApiParam("起始页码从1开始") int pageIndex
            , @RequestParam(value = "pageSize") @ApiParam("每页显示的数量") int pageSize
    ) {
        PageHelper.startPage(pageIndex, pageSize);
        List<UserEntity> list = mUserService.getUserList();
        return ResponseEntity.success(DataUtil.getPageData(list));
    }

    @RequestMapping(value = "/{uid}",method = RequestMethod.PUT)
    @ApiOperation("用户管理")
    public ResponseEntity updateUser(@ApiParam(name = "用户ID") @PathVariable String uid
            , @RequestParam(value = "forbid") @ApiParam(name = "是否禁止") String forbid) {
        mUserService.updateUser(uid,forbid);
        return ResponseEntity.successMessage("操作成功");

    }

}

