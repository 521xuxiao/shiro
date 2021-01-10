package com.shiro.shiro.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoginDao {


    int addUser(Map<String, Object> params);

    Map<String, Object> queryUsers(String username);

    List<String> queryPermissions(String userId);
}
