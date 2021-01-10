package com.shiro.shiro.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class ShiroSession extends DefaultWebSessionManager {
    public ShiroSession() {
        super();
        //设置 shiro session 失效时间，默认为30分钟，这里现在设置为60分钟
        //setGlobalSessionTimeout(MILLIS_PER_MINUTE * 60);
    }

    /**
     * 	获取sessionId，原本是根据sessionKey来获取一个sessionId
     * 	重写的部分多了一个把获取到的token设置到request的部分。这是因为app调用登陆接口的时候，是没有token的，登陆成功后，产生了token,我们把它放到request中，返回结
     * 	果给客户端的时候，把它从request中取出来，并且传递给客户端，客户端每次带着这个token过来，就相当于是浏览器的cookie的作用，也就能维护会话了
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头中的 AUTH_TOKEN 的值，如果请求头中有 AUTH_TOKEN 则其值为sessionId。shiro就是通过sessionId 来控制的
        String token = WebUtils.toHttp(request).getHeader("auth-token");
//        Enumeration<String> headers=WebUtils.toHttp(request).getHeaderNames();
//        while(headers.hasMoreElements()){
//            String element = headers.nextElement();
//            System.err.println(element+"::::::"+WebUtils.toHttp(request).getHeader(element));
//        }

        String requestSource = WebUtils.toHttp(request).getHeader("Request-Source");
        /*
         ValueOperations<String,String> operation=redisTemplate.opsForValue();
         */
        //只要返回null，就是没有登陆，走shirofilter中的onAccessDenied接口，但由于登录接口在权限中放开了，不走这里
        if (StringUtils.isEmpty(token)/*||StringUtils.isEmpty(requestSource)*/){
            //如果没有携带id参数则按照父类的方式在cookie进行获取sessionId
            //Serializable token=super.getSessionId(request, response);
            //System.err.println("token:::"+token);
            //如果请求头中没有token，则返回null，走shirofilter中的onAccessDenied接口，返回未登录提示
            return null;
        } else {
//        	if("app".equals(requestSource)) {
//        	}
            //String value=jedis.get(token);
            //去redis中查找有没有该token
        	/*
        	String value=operation.get(token);
        	System.err.println("value:::"+value);
        	if(StringUtils.isEmpty(value)) {
        		return null;
        	}
        	*/
            //jedis.close();
            //请求头中如果有 AUTH_TOKEN, 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "Stateless request");
            //sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);

            /*
            if("app".equals(requestSource)) {
            	//如果是APP，将app对应的token延长超时为30天
            	redisTemplate.expire(token, 30, TimeUnit.DAYS);
            }else {
            	//如果是pc端，将pc端对应的token延长时间1天
            	redisTemplate.expire(token, 1, TimeUnit.DAYS);
            }
            */
            //返回token，shiro根据token也就是sessionId找对应的session对象，如果没找到，同样走shirofilter中的onAccessDenied接口，返回未登录提示
            return token;
        }
    }
}
