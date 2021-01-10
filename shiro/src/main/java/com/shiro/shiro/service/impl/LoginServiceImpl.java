package com.shiro.shiro.service.impl;

import com.shiro.shiro.dao.LoginDao;
import com.shiro.shiro.service.LoginService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;

    @Transactional
    @Override
    public void addUser(Map<String, Object> params) {
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        SimpleHash sh = new SimpleHash("MD5", params.get("password").toString(), salt, 1);
        String md5Password = sh.toHex();
        params.put("salt", salt);
        params.put("password", md5Password);
        int num = loginDao.addUser(params);
        if(num == 0)
            throw new RuntimeException("新增用户列表失败");
    }
}
