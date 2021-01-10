package com.shiro.shiro.shiro;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 没有登录的情况下进来
 */
public class ShiroLoginFilter extends FormAuthenticationFilter {
    /**
     * 	访问controller前判断是否登录
     * @return true:继续往下执行，false:不继续执行其他过滤器
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse res = (HttpServletResponse) response;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            JSONObject result = new JSONObject();
            result.put("success", false);
            result.put("login", false);
            result.put("message", "登录过期或未登录");
            PrintWriter writer = res.getWriter();
            writer.append(result.toString());
            writer.close();
        }
        return false;
    }
}
