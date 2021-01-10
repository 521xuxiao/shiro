package com.shiro.shiro.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 配置shiro的过时时间 (默认的,利用的是session)
     */
//    @Bean
//    public SessionManager sessionManager() {
//        DefaultWebSessionManager sManager =  new DefaultWebSessionManager();
//        sManager.setGlobalSessionTimeout(3*60*60*1000);  // 3个小时过期
//        return sManager;
//    }

    /**
     * 配置shiro的过期时间,用的是shiroSession类里面的时间(利用的是 token)
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        ShiroSession sessionManager=new ShiroSession();
        //ms,设置shiro的session时长为30天
        sessionManager.setGlobalSessionTimeout(1000*60*60*24*30);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 创建secrityManager对象
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm realm, SessionManager sessionManager) {
        DefaultWebSecurityManager sManager = new DefaultWebSecurityManager();
        sManager.setRealm(realm);
        sManager.setSessionManager(sessionManager);
        return sManager;
    }

    /**
     * 配置过滤器工厂,生产大量的过滤器,来对请求进行过滤
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean sBean = new ShiroFilterFactoryBean();
        sBean.setSecurityManager(securityManager);
//        sBean.setLoginUrl("/doLoginUI");    // 访问需要认证的就跳转到登录页面,让用户登录, 类似于路由重定向

        Map<String, String> map = new LinkedHashMap<>();
        // 直接访问的资源
        map.put("/browser_components/**","anon");
        map.put("/js/**","anon");
        map.put("/build/**", "anon");
        map.put("/myShiro/login", "anon");

        // 需要认证才能访问的资源
        map.put("/**", "authc");
        map.put("/**", "ShiroLoginFilter");

//        退出登录
//        map.put("/shiro/logout", "logout");
        sBean.setFilterChainDefinitionMap(map);


        //使用自定义的过滤器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("ShiroLoginFilter", new ShiroLoginFilter());
        sBean.setFilters(filterMap);

        return sBean;
    }








    /**
     * 授权(做权限配置)
     */
    //  第一步:配置bean对象的生命周期管理(SpringBoot可以不配置)
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    //  第二步: 通过如下配置要为目标业务对象创建代理对象（SpringBoot中可省略）
    @DependsOn("lifecycleBeanPostProcessor")
    @Bean
    public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
    //  第三步:配置advisor对象,shiro框架底层会通过此对象的matchs方法返回值(类似切入点)决定是否创建代理对象,进行权限控制
    @Bean
    public AuthorizationAttributeSourceAdvisor newAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
