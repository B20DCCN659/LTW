package com.example.btl_web.filter;

import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.Role;
import com.example.btl_web.constant.Constant.User;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();

        UserDto user = (UserDto) SessionUtils.getInstance().getValue(request,Constant.USER_MODEL);
        request.setAttribute(Constant.USER_MODEL, user);
        request.setCharacterEncoding("UTF-8");

        if(url.contains("admin"))
        {
            if(user == null)
            {
                response.sendRedirect(request.getContextPath() + "/login?action=not_login");
            }
            else
            {
                if(user.getRole() >= Role.MOD)
                {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                else
                {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
        else if(url.startsWith("/api"))
        {
            if(url.equals(User.USER_CREATE_API) && user == null)
            {
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else
            {
                if(user == null)
                {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                else
                {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        }
        else
        {
            //Nếu người dùng truy cập đến trang thay đổi mật khẩu, nhưng mà chưa đăng nhập
            if(url.equals(User.CHANGE_PASSWORD_PAGE) && user == null)
                response.sendRedirect(User.LOGIN_PAGE);

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
