package net.newglobe.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import net.newglobe.app.model.vo.DataResult;
import net.newglobe.util.TokenUtil;


public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private DefaultWebSessionManager shiroSessionManager;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
    	
    	//如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String servletPath = httpServletRequest.getServletPath();

        if (servletPath.contains("/api/")) {
        	String token = httpServletRequest.getHeader("token");
            //走登录逻辑
//            if(servletPath.toLowerCase().contains("login") || servletPath.equals("/api/project/loadProjectList")){
//                return true;
//            }
            
            if (StringUtils.isEmpty(token)) {
            	httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                DataResult<String> result = new DataResult<String>();
                result.setMessage("登录信息过期请重新登录");
                result.setSuccess(false);
                httpServletResponse.getWriter().write(JSON.toJSONString(result));
                return false;
            }else {
                //测试使用test token即可
                if("test".equals(token)){
                    String username = "root";
                    String password = "123456";
                    Subject subject = SecurityUtils.getSubject();
                    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
                    subject.login(usernamePasswordToken);// 如果登录成功才会继续执行下面的操作
                    return true;
                }

                Subject existSubject = tokenUtil.getSubject(token);
                if (existSubject == null) {//没有
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    DataResult<String> result = new DataResult<String>();
                    result.setSuccess(false);
                    result.setMessage("登录信息过期请重新登录");
                    httpServletResponse.getWriter().write(JSON.toJSONString(result));
                    return false;
                } else {
                	//session已经失效
                	if((System.currentTimeMillis()-existSubject.getSession().getStartTimestamp().getTime())>=shiroSessionManager.getGlobalSessionTimeout()-1000) {
                		tokenUtil.removeToken(token);
                        DataResult<String> result = new DataResult<String>();
                        result.setMessage("登录信息过期请重新登录");
                        result.setSuccess(false);
                        httpServletResponse.getWriter().write(JSON.toJSONString(result));
                        return false;
                	}else {
                        ThreadContext.unbindSubject();
                        ThreadContext.bind(existSubject);
                        return true;
                	}
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}