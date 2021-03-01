package com.example.as.api.mapper;

import com.example.as.api.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 为查询数据库配置的映射关系类
 */
@Repository
public interface UserMapper {
    void addUser(String userName, String password, String imoocId, String orderId, String createTime);

    List<UserEntity> findUser(String userName);

    List<UserEntity> getUserList();

    void updateUser(String uid, String forbid);
}
