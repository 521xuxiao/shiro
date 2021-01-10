package com.shiro.shiro.shiro;

import com.shiro.shiro.dao.LoginDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
    @Autowired
    private LoginDao loginDao;
    /**
     * 设置MD5加密方式
     * @return
     */
    @Override
    public CredentialsMatcher getCredentialsMatcher() {
        HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
        cMatcher.setHashAlgorithmName("MD5");
        cMatcher.setHashIterations(1);
        return cMatcher;
    }


    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1.获取用户输入的信息
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username =  upToken.getUsername();
        // 2.基于用户输入的用户名查询用户信息
        Map<String, Object> queryUser = loginDao.queryUsers(username);
        // 3.对用户信息基本校验
        if(queryUser == null){
            throw new UnknownAccountException("用户不存在");
        }
        // 4.封装用户信息并返回
        ByteSource salt = ByteSource.Util.bytes(queryUser.get("salt").toString().getBytes());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
            queryUser,   // 存到session里面的当前登录人的用户信息
            queryUser.get("password").toString(),  // 数据库里面加密之后的密码
            salt,                      // 盐值
            this.getName()             // 当前类的getName
        );
        return info;
    }



    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取当前登录人的userId
        Map<String, Object> user = (Map<String, Object>) SecurityUtils.getSubject().getPrincipal();
        String userId = user.get("id").toString();
        // 根据userId查询当前登录人的perMission集合(set集合)
        List<String> listPermissions = loginDao.queryPermissions(userId);
        if(listPermissions == null || listPermissions.size() == 0) {
            throw new AuthorizationException();
        }
        // 封账数据并且返回到secrityManager 进行授权
        Set<String> setList = new HashSet<>();
        for (String item: listPermissions) {   // 将ArrayList转成set集合, 封装到 SimpleAuthorizationInfo 对象中
            setList.add(item);
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(setList);
        //setList 的数据结构就是这样的 [building:delete, building:update, building:query, building:add], 此集合的每哥元素都是业务层里面对应的 @RequiresPermissions("building:delete")
        System.err.println(setList);   // setList 为当前登录人的权限
        return info;
    }

}
