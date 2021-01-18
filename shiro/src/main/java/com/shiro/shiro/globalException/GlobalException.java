package com.shiro.shiro.globalException;

import com.shiro.shiro.returnData.ReturnData;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;

@ResponseBody
@ControllerAdvice
public class GlobalException {
    private Logger logger= LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(RuntimeException.class)
    public ReturnData doHandleRuntimeException(RuntimeException e) {
        e.printStackTrace();


        /**
         * 配错误输出文件里面的日志
         */
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String errMsg = sw.toString();
        logger.error(errMsg);



        ReturnData returnData = new ReturnData();
        if(e instanceof BadSqlGrammarException){
            returnData.setResultMessage("系统异常");
            returnData.setSuccess(false);
        }else if(e instanceof RuntimeException) {
            returnData.setResultMessage(e.getMessage());
            returnData.setSuccess(false);
        }
        return returnData;
    }



    /**
     * shiro框架的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ShiroException.class)
    public ReturnData doHandleShiroException(ShiroException e) {
        ReturnData returnData = new ReturnData();
        returnData.setSuccess(false);
        if(e instanceof UnknownAccountException) {
            returnData.setResultMessage("账户不存在");
        }else if(e instanceof IncorrectCredentialsException) {
            returnData.setResultMessage("密码不正确");
        }else if(e instanceof AuthorizationException) {
            returnData.setResultMessage("没有此操作权限");
        }
        return returnData;
    }
}
