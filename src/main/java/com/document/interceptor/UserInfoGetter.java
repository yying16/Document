package com.document.interceptor;

import com.document.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//获取登录用户信息的拦截器

@Component
public class UserInfoGetter implements HandlerInterceptor {
    private static User user;

    public User getUser() {
        return user;
    }

    public void UserLogout() {
        user = null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        User result = (User) session.getAttribute("user");//获得已填写的账号信息

        if(result != null) {
            if(user == null) {
                user = result;
            }
        }
        return true;
    }
}
