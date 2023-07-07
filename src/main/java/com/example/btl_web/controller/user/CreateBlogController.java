package com.example.btl_web.controller.user;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.utils.SessionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {User.CREATE_BLOG_PAGE, User.UPDATE_BLOG_PAGE + "/*"})
@MultipartConfig(
                fileSizeThreshold   = 1024 * 1024 * 1,  // 1 MB
                maxFileSize         = 1024 * 1024 * 10, // 10 MB
                maxRequestSize      = 1024 * 1024 * 15  // 15 MB
)
public class CreateBlogController extends HttpServlet {
    private BlogService blogService = ServiceConfiguration.getBlogService();
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDto user = (UserDto) SessionUtils.getInstance().getValue(req, Constant.USER_MODEL);
        if(user == null)
        {
            resp.sendRedirect(User.LOGIN_PAGE+ "?" + User.ACTION + "=" + User.ACTION_NOT_LOGIN);
        }
        else
        {
            BlogDto blogDto = new BlogDto();
            String url = req.getRequestURI();
            //Kiểm tra nếu là chỉnh sửa truyện
            if(url.startsWith(User.UPDATE_BLOG_PAGE))
            {
                String blogIdStr = url.split("/")[2];
                blogDto.setBlogId(Long.parseLong(blogIdStr));
                if(checkUserOfThisBlog(blogDto.getBlogId(), user.getUserId()))
                {
                    if(blogDto == null)
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    blogDto = blogService.getOne(blogDto);
                    //Xoá các thẻ <p> ở mỗi dòng
                    blogService.removePTagContent(blogDto);
                }
                else
                {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }

            List<CategoryDto> categories = categoryService.findAll(null, null);

            req.setAttribute("blog", blogDto);
            req.setAttribute("categories", categories);
            RequestDispatcher rd = req.getRequestDispatcher(User.CREATE_BLOG_JSP);

            rd.forward(req, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        UserDto user = (UserDto) SessionUtils.getInstance().getValue(req, Constant.USER_MODEL);
        if(user == null)
        {
            resp.sendRedirect(User.LOGIN_PAGE+ "?" + User.ACTION + "=" + User.ACTION_NOT_LOGIN);
            return;
        }

        BlogDto createBlog = new BlogDto(req);

        //Nếu người dùng không phải chủ bài viết thì không thay đổi
        if(createBlog.getUser() != null && !checkUserOfThisBlog(createBlog.getBlogId(), user.getUserId()))
        {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String requestMethod = req.getParameter("_method");
        boolean valid = false;
        if(requestMethod.equals(Request.POST_METHOD))
        {
            createBlog.setUser(user);
            valid = blogService.validCreateBlog(req, createBlog);
        }
        else if(requestMethod.equals(Request.PUT_METHOD))
            valid = blogService.validUpdateBlog(req, createBlog, user.getUserId());

        if(valid)
        {
            String message = "";
            Long blogId = null;

            if(requestMethod.equals(Request.POST_METHOD))
            {
                blogId = blogService.save(createBlog);
                message = "Tạo bài viết thành công, vui lòng đợi quản trị viên xét duyệt!";
            }
            else if(requestMethod.equals(Request.PUT_METHOD))
            {
                createBlog.setStatus(0);
                blogId = blogService.update(createBlog);
                message = "Chỉnh sửa bài viết thành công, vui lòng đợi quản trị viên xét duyệt";
            }
            if(blogId != null)
            {
                req.setAttribute("message", message);
                req.setAttribute("status", "notice");
            }
        }
        else
        {
            req.setAttribute("status", "alert");
            req.setAttribute("blog", createBlog);
        }

        doGet(req, resp);
    }

    private boolean checkUserOfThisBlog(Long blogId, Long userId)
    {
        BlogDto checkUserOfBlog = new BlogDto();
        checkUserOfBlog.setBlogId(blogId);
        checkUserOfBlog = blogService.getOne(checkUserOfBlog);

        return checkUserOfBlog.getUser().getUserId() == userId;
    }
}
