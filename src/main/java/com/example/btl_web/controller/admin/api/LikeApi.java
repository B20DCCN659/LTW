package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.service.UserBlogService;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.HttpUtils;
import com.example.btl_web.utils.SessionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = User.USER_LIKE_API)
public class LikeApi extends HttpServlet {
    private UserService userService = ServiceConfiguration.getUserService();
    private UserBlogService userBlogService = ServiceConfiguration.getUserBlogService();
    private BlogService blogService = ServiceConfiguration.getBlogService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        solveApi(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        solveApi(req, resp);
    }

    private void solveApi(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String method = req.getMethod();

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        UserDto user = (UserDto) SessionUtils.getInstance().getValue(req, Constant.USER_MODEL);
        BlogDto likedBlog = HttpUtils.of(req.getReader()).toModel(BlogDto.class);

        String[] errors = new String[1];
        ObjectMapper mapper = new ObjectMapper();

        BlogDto checkStatusBlog = new BlogDto();
        checkStatusBlog.setBlogId(likedBlog.getBlogId());
        checkStatusBlog.setStatus(1);
        checkStatusBlog = blogService.getOne(checkStatusBlog);
        if(checkStatusBlog == null)
        {
            errors[0] = "Bài viết này không thể bày tỏ cảm xúc";
        }
        else
        {
            String timevalid = userService.checkLastAction(user.getUserId());
            if(timevalid == null)
            {
                boolean statusLiked = false;
                if(method.equals(Request.POST_METHOD))
                {
                    statusLiked = userBlogService.likeThisBlog(likedBlog.getBlogId(), user.getUserId());
                    errors[0] = "Bạn đã thích bài viết này";
                }
                else if(method.equals(Request.PUT_METHOD))
                {
                    statusLiked = userBlogService.removeLikeThisBlog(likedBlog.getBlogId(), user.getUserId());
                    errors[0] = "Bạn đã bỏ thích bải viết này";
                }

                if(statusLiked)
                {
                    mapper.writeValue(resp.getOutputStream(), errors[0]);
                    return;
                }
                else
                {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    mapper.writeValue(resp.getOutputStream(), "Bạn phải đăng nhập thì mới có thể thích bài viết này");
                    return;
                }
            }
            errors[0] = timevalid;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        mapper.writeValue(resp.getOutputStream(), errors);
    }
}
