package com.example.as.api.service;

import com.example.as.api.entity.UserEntity;
import com.example.as.api.mapper.UserMapper;
import com.example.as.api.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserService {
    @Autowired
    private UserMapper mUserMapper;

    public void addUser(String userName, String password, String imoocId, String orderId) {
        mUserMapper.addUser(userName, password, imoocId, orderId, DateUtil.currentDate());
    }

    public List<UserEntity> findUser(String userName) {
        return mUserMapper.findUser(userName);
    }

    public List<UserEntity> getUserList() {
        return mUserMapper.getUserList();
    }

    public void updateUser(String uid, String forbid) {
        mUserMapper.updateUser(uid, forbid);
    }
}
