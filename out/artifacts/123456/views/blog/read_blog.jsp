<%@ page import="com.example.btl_web.dto.BlogDto" %>
<%@ page import="com.example.btl_web.utils.CookieUtils" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@ page import="org.apache.http.entity.StringEntity" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="com.example.btl_web.constant.Constant.*" %>
<%@ page import="org.apache.http.client.methods.HttpPost" %>
<%@ page import="org.apache.http.client.methods.HttpPut" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="com.example.btl_web.service.BlogService" %>
<%@ page import="com.example.btl_web.configuration.ServiceConfiguration" %>
<%@ page import="com.example.btl_web.dto.CommentDto" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.example.btl_web.dto.UserDto" %>
<%@ page import="com.example.btl_web.constant.Constant" %>
<%@ page import="com.example.btl_web.utils.SessionUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.btl_web.paging.Pageable" %>
<%@ page import="com.example.btl_web.paging.PageRequest" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="api_url_like" value="/api-create-like" />
<c:set var="api_url_comment" value="/api-create-comment" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/assets/css/blog/blog1.css">
    <link rel="stylesheet" href="/assets/css/home.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <title>Đọc truyện ${blog.title}</title>
</head>
<body>
    <%
        UserDto user = (UserDto) SessionUtils.getInstance().getValue(request, Constant.USER_MODEL);

        //Gửi api để like hoặc bỏ like bài viết
        String likeMethod = request.getParameter("likeMethod");
        if(likeMethod != null)
        {
            String alert = "alert";
            if(user != null)
            {
                BlogDto editLike = new BlogDto();
                String blogIdStr = request.getParameter("blog-id");
                editLike.setBlogId(Long.parseLong(blogIdStr));

                String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();
                Gson gson = new Gson();
                String blogJson = gson.toJson(editLike);

                String url = "http://localhost:8080/api-create-like";
                HttpClient httpClient = HttpClientBuilder.create().build();
                StringEntity stringEntity = new StringEntity(blogJson, StandardCharsets.UTF_8);
                HttpResponse responseApi = null;

                if(likeMethod.equals(Request.POST_METHOD))
                {
                    HttpPost sendApi = new HttpPost(url);
                    sendApi.addHeader("content-type", "application/json");
                    sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
                    sendApi.setEntity(stringEntity);

                    responseApi = httpClient.execute(sendApi);
                }
                else if(likeMethod.equals(Request.DELETE_METHOD))
                {
                    HttpPut sendApi = new HttpPut(url);
                    sendApi.addHeader("content-type", "application/json");
                    sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
                    sendApi.setEntity(stringEntity);

                    responseApi = httpClient.execute(sendApi);
                }
                HttpEntity entity = responseApi.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                int statusCode = responseApi.getStatusLine().getStatusCode();
                if(statusCode == 200)
                {
                    alert = "notice";
                }
                responseString = responseString.replaceAll("[\\[\\]\"]", "");
                request.setAttribute("like_message", responseString);
            }
            else
            {
                request.setAttribute("like_message", "Bạn phải đăng nhập mới được thực hiện hành động này");
            }
            request.setAttribute("like_status", alert);
        }
    %>

    <%
        //Xử lý api để gửi hoặc xóa comment
        String commentMethod = request.getParameter("edit_comment");
        if(commentMethod != null )
        {
            String alert = "alert";
            if(user != null)
            {
                String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();

                CommentDto editComment = new CommentDto();
                String commentId = request.getParameter("comment_id");
                String blogId = request.getParameter("blog_id");
                String content = request.getParameter("comment_content");
                if(commentId != null && !commentId.isEmpty())
                    editComment.setCommentId(Long.parseLong(commentId));
                if(blogId != null && !blogId.isEmpty())
                    editComment.setBlogComment(Long.parseLong(blogId));
                if(content != null && !content.isEmpty())
                    editComment.setContent(content);

                Gson gson = new Gson();
                String commentJson = gson.toJson(editComment);

                String url = "http://localhost:8080/api-create-comment";
                HttpClient httpClient = HttpClientBuilder.create().build();
                StringEntity stringEntity = new StringEntity(commentJson, StandardCharsets.UTF_8);
                HttpResponse responseApi = null;

                if(commentMethod.equals(Request.POST_METHOD))
                {
                    HttpPost sendApi = new HttpPost(url);
                    sendApi.addHeader("content-type", "application/json");
                    sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
                    sendApi.setEntity(stringEntity);

                    responseApi = httpClient.execute(sendApi);
                }
                else if(commentMethod.equals(Request.DELETE_METHOD))
                {
                    HttpPut sendApi = new HttpPut(url);
                    sendApi.addHeader("content-type", "application/json");
                    sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
                    sendApi.setEntity(stringEntity);

                    responseApi = httpClient.execute(sendApi);
                }

                HttpEntity entity = responseApi.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                int statusCode = responseApi.getStatusLine().getStatusCode();
                if(statusCode == 200)
                {
                    alert = "notice";
                }

                responseString = (new ObjectMapper()).readValue(responseString, String[].class)[0];
                request.setAttribute("comment_message", responseString);
            }
            else
            {
                request.setAttribute("comment_message", "Bạn phải đăng nhập thì mới được comment");
            }
            request.setAttribute("comment_status", alert);
        }
    %>

    <%
        //Lấy thông tin của bài viết
        String idStr = request.getAttribute("blogId").toString();
        BlogService blogService = ServiceConfiguration.getBlogService();
        if(idStr!= null)
        {
            Long id = Long.parseLong(idStr);
            BlogDto blogDto = new BlogDto();
            blogDto.setBlogId(id);

            blogDto = blogService.getOne(blogDto);

            if(blogDto != null)
            {
                if(blogDto.getStatus() == 0)
                {
                    if(user == null || user.getRole() < Role.MOD && user.getUserId() != blogDto.getUser().getUserId())
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                request.setAttribute("blog",blogDto);

                //Tạo pageable lấy 5 truyện
                Pageable pageable = new PageRequest(request.getParameterMap(), 5L);

                //Lấy ra 5 truyện cùng thể loại
                BlogDto find5Blogs = new BlogDto();
                find5Blogs.addACategory(blogDto.getCategories().get(0));
                find5Blogs.setStatus(1);
                List<BlogDto> suggestBlogs = blogService.getAllBlogs(pageable, find5Blogs);
                request.setAttribute("suggestBlogs", suggestBlogs);

                //Lấy ra 5 truyện cùng tác giả
            }
            else
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    %>

    <jsp:include page="/views/common/header.jsp" />
    <div class="main">
        <div class="content">
            <h1 style="display: flex; justify-content: center;">${blog.title} </h1> <br> <br> <br>

            <p>Thể loại:</p>
            <c:forEach var="category" items="${blog.categories}" varStatus="loop" >
                <c:if test="${loop.index > 0}">
                    <span>-</span>
                </c:if>
                <span><a href="/?categorySearch=${category.categoryId}">${category.name}</a></span>
            </c:forEach>

            <c:if test="${not empty USER_MODEL and blog.status == 1}" >
                <form action="" method="post">
                    <input type="hidden" name="blog-id" value="${blog.blogId}">
                    <c:if test="${blog.checkUserLikedBlog(USER_MODEL.userId) == false}" >
                        <input type="hidden" name="likeMethod" value="POST">
                    </c:if>
                    <c:if test="${blog.checkUserLikedBlog(USER_MODEL.userId)}" >
                        <input type="hidden" name="likeMethod" value="DELETE">
                    </c:if>
                    <button id="like-button">
                        <c:if test="${blog.checkUserLikedBlog(USER_MODEL.userId) == false}" >Thích bài viết này</c:if>
                        <c:if test="${blog.checkUserLikedBlog(USER_MODEL.userId)}" >Bỏ thích bài viết này</c:if>
                    </button>
                </form>
            </c:if>

            <p class="${like_status}">${like_message}</p>

            <p>${blog.likedUsers.size()} lươt thích</p>

            <c:if test="${blog.user.userId == USER_MODEL.userId}">
                <a href="/update-blog/${blog.blogId}">Chỉnh sửa bài viết này</a>
            </c:if>
            
            <img src="${blog.imageTitle}">

            <p>${blog.content}</p>

            <h4 style="display: flex; justify-content: center;">---End---</h4> <br>

            <a class="float-right" style="font-weight: bold" href="/user/${blog.user.userId}">${blog.user.fullName}</a>
        </div>

        <div class="sidebar">
            <div class="sidebar-two">
                <p style="font-weight: bold">Có thể bạn sẽ thích</p>
                <c:forEach var="blog" items="${suggestBlogs}">
                    <a href="/blogs/${blog.blogId}" class="comment">
                        <p>${blog.title}</p>
                        <img src="${blog.imageTitle}">
                    </a>
                </c:forEach>
            </div>

            <div class="sidebar-two">
                <h1>${blog.comments.size()} bình luận</h1>
                <c:if test="${not empty USER_MODEL and blog.status == 1}">
                    <!-- Form để thêm bình luận -->
                    <div class="comment-form">
                        <h3>Thêm bình luận</h3>
                        <p class="${comment_status}">${comment_message}</p>
                        <form action="" method="post">
                            <input type="hidden" name="blog_id" value="${blog.blogId}">
                            <input type="hidden" name="edit_comment" value="POST">
                            <textarea id="comment" name="comment_content" required></textarea>
                            <button>Gửi</button>
                        </form>
                    </div>
                </c:if>
                <!-- Phần hiển thị bình luận -->
                <div class="comments">
                    <c:forEach var="comment" items="${blog.comments}" >
                        <div class="comment">
                            <div class="author"><a href="/user/${comment.userComment.userId}">${comment.userComment.fullName}</a></div>
                            <div class="text">${comment.content}</div>
                            <div>${comment.createdAt}</div>
                            <c:if test="${comment.userComment.userId == USER_MODEL.userId}">
                                <form action="" method="POST">
                                    <input type="hidden" name="edit_comment" value="DELETE">
                                    <input type="hidden" name="comment_id" value="${comment.commentId}">
                                    <button>Xóa comment này</button>
                                </form>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/views/common/footer.jsp" />
</body>
</html>