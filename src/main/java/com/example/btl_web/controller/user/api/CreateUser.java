package com.example.btl_web.controller.user.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = User.USER_CREATE_API)
public class CreateUser extends HttpServlet {
    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        UserDto user = HttpUtils.of(req.getReader()).toModel(UserDto.class);

        String[] errors = new String[5];
        boolean valid = userService.validateSignUp(user, errors);
        ObjectMapper mapper = new ObjectMapper();
        if(!valid)
        {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), errors);
        }
        else
        {
            Long status = userService.saveUser(user);
            if(status != null)
            {
                errors[0] = "Đăng ký tài khoản thành công!";
                mapper.writeValue(resp.getOutputStream(), errors);
            }
        }
    }
}
