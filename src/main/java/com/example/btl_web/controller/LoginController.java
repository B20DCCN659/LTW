package com.example.btl_web.controller;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.SessionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(Constant.LOGIN_JSP);
        String messageType = "alert";

        String action = req.getParameter(User.ACTION);
        if(action != null)
        {
            if(action.equals(User.ACTION_NOT_LOGIN))
            {
                req.setAttribute("message", "Bạn chưa đăng nhập!");
            }
            else if (action.equals(User.ACTION_NOT_PERMISSION))
            {
                req.setAttribute("message", "Bạn không có quyền truy cập địa chỉ này!");
            }
            else if(action.equals(User.ACTION_LOG_OUT))
            {
                //Thực hiện xoá cookie của người dùng
                addCookie(req, resp, 0);

                req.setAttribute("message", "Đăng xuất thành công!");
                SessionUtils.getInstance().removeValue(req, Constant.USER_MODEL);
            }
            if(action.equals("sign-up-success"))
            {
                req.setAttribute("message", "Đăng ký thành công, vui lòng đăng nhập để tiếp tục");
                messageType = "notice";
            }
            req.setAttribute("display_flex", "display__flex");
            req.setAttribute("message_type", messageType);
        }

        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String passWord = req.getParameter("passWord");

        UserDto userDto = userService.login(userName, passWord);

        if(userDto != null )
        {
            if(userDto.getStatus() == 1)
            {
                SessionUtils session = SessionUtils.getInstance();
                session.putValue(req, Constant.USER_MODEL, userDto);

                //Thêm thông tin session của người dùng vào cookie
                addCookie(req, resp, 1);

                resp.sendRedirect("/");
                return;
            }
            else
            {
                req.setAttribute("message", "Tài khoản này đã bị khoá! Vui lòng liên hệ admin");
            }
        }
        else
        {
            req.setAttribute("message", "Tài khoản mật khẩu không chính xác!");
        }
        RequestDispatcher rd = req.getRequestDispatcher(Constant.LOGIN_JSP);
        req.setAttribute("display_flex", "display__flex");
        req.setAttribute("message_type", "alert");
        rd.forward(req, resp);
    }

    private void addCookie(HttpServletRequest req, HttpServletResponse resp, int status)
    {
        //Todo: chuyển hàm này sang CookieUtils
        HttpSession session = req.getSession();
        Cookie cookie = null;
        if(status == 1) //Login
            cookie = new Cookie("JSESSIONID", session.getId());
        else if (status == 0) //logout
            cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(-1); // Cookie tồn tại đến khi trình duyệt đóng
        resp.addCookie(cookie);
    }
}
