package com.shiro.shiro.controller;

import com.shiro.shiro.returnData.ReturnData;
import com.shiro.shiro.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("myShiro")
@ResponseBody
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 增加用户列表数据
     * @Params {"username":"","password":""}
     */
    @PostMapping("/addUser")
    public ReturnData addUser(@RequestBody Map<String, Object> params) {
        loginService.addUser(params);
        return new ReturnData("新增成功");
    }

    /**
     * 登录
     */
    @PostMapping("login")
    public ReturnData login(@RequestBody Map<String, Object> params) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(params.get("username").toString(), params.get("password").toString());
        subject.login(token);
        String tokens = (String) subject.getSession().getId();
        Map<String, Object> map = new HashMap<>();
        map.put("token", tokens);
        return new ReturnData(map);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public ReturnData logout() {
        Subject subject = SecurityUtils.getSubject();
        /*  Session session=subject.getSession();
		    session.removeAttribute("USER_ID");
		*/
        subject.logout();
        return new ReturnData("退出成功");
    }
}
