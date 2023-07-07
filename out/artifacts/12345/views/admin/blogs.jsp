<%@ page import="com.example.btl_web.constant.Constant.*" %>
<%@ page import="com.example.btl_web.dto.BlogDto" %>
<%@ page import="com.example.btl_web.paging.Pageable" %>
<%@ page import="com.example.btl_web.paging.PageRequest" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.btl_web.service.BlogService" %>
<%@ page import="com.example.btl_web.configuration.ServiceConfiguration" %>
<%@ page import="com.example.btl_web.utils.CookieUtils" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@ page import="org.apache.http.client.methods.HttpPut" %>
<%@ page import="org.apache.http.entity.StringEntity" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="api_url" value="/api-blog"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="/assets/css/admin/admin.css">
    <link rel="stylesheet" href="/assets/css/home.css">
    <title>Quản lý truyện</title>
</head>
<body>
    <%
        String editMethod = request.getParameter("editBlog");
        if(editMethod != null)
        {
            String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();

            String blogIdStr = request.getParameter("id");
            String statusStr = request.getParameter("status");

            BlogDto editBlog = new BlogDto();
            if(blogIdStr != null || statusStr != null)
            {
                editBlog.setBlogId(Long.parseLong(blogIdStr));
                editBlog.setStatus(Integer.parseInt(statusStr));
            }

            Gson gson = new Gson();
            String blogJson = gson.toJson(editBlog);

            String url = "http://localhost:8080/api-blog";
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPut sendApi = new HttpPut(url);
            StringEntity params = new StringEntity(blogJson);
            sendApi.addHeader("content-type", "application/json");
            sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
            sendApi.setEntity(params);

            //Nhận phản hồi
            HttpResponse apiResponse = httpClient.execute(sendApi);
            HttpEntity entity = apiResponse.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            String status = "alert";

            int statusCode = apiResponse.getStatusLine().getStatusCode();
            if(statusCode == 200)
            {
                status = "notice";
            }

            //Xoá bỏ "" ở đầu và cuối của Json
            responseString = responseString.replaceAll("[\\[\\]\"]", "");
            request.setAttribute("status", status);
            request.setAttribute("message", responseString);
        }
    %>

    <%
        BlogService blogService = ServiceConfiguration.getBlogService();

        StringBuilder pageUrl = new StringBuilder(Admin.BLOGS_PAGE + "?");

        BlogDto searchDto = null;
        String searchName = request.getParameter("keySearch");
        if(searchName != null)
        {
            searchDto = new BlogDto();
            searchDto.setTitle(searchName);
            pageUrl.append("keySearch=" + searchName + "&");
        }
        pageUrl.append("page=");

        long totalBlog = blogService.countBlogs(searchDto);
        Pageable pageable = new PageRequest(request.getParameterMap(), totalBlog);

        List<BlogDto> blogList = blogService.getAllBlogs(pageable, searchDto);

        //Tìm lượt thích cho mỗi bài viết
        for(BlogDto blog: blogList)
        {
            blog.setLikedUsers(blogService.peopleLikedBlog(blog.getBlogId()));
        }

        request.setAttribute("pageable", pageable);
        request.setAttribute("blogList", blogList);
        request.setAttribute("categories_page", Admin.CATEGORIES_PAGE);
        request.setAttribute("home", pageUrl.toString());
        request.setAttribute("users_page", Admin.USERS_PAGE);
        request.setAttribute("keySearch", searchName);
    %>
    <div id="Admin">
        <div class="navbar-main">
            <jsp:include page="/views/common/header.jsp" />
        </div>
        <div id="main">
            <!-- MAIN -->
            <div class="container">
                <div class="navbar">
                    <div class="icon--link">
                        <i class="icon fa-solid fa-book-open"></i>
                    </div>
                </div>
            </div>
            <p class="${status}">${message}</p>
            <!-- Content -->
            <section class="content">
                <div class="row mt-5">
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <div class="float-left">
                                    <form action="/admin/blogs" method="post">
                                        <div class="tim-kiem">
                                            <input type="text" placeholder="Tìm Kiếm" class="search" name="keySearch" value="${keySearch}">
                                            <button class="btn btn-search">Tìm kiếm</button>
                                        </div>
                                    </form>

                                    <c:if test="${not empty keySearch}" >
                                        <p>Tìm kiếm truyện theo từ khoá: ${keySearch}</p>
                                    </c:if>
                                </div>
                            </div>
                            <div class="card-body">
                                <table class="table" style="border: solid 1px #000;">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th>STT</th>
                                        <th>Tên</th>
                                        <th>Người đăng</th>
                                        <th>Thời gian đăng</th>
                                        <th>Lượt thích</th>
                                        <th>Trạng thái</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="blog" items="${blogList}" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td> <a href="/blogs/${blog.blogId}">${blog.title}</a> </td>
                                                <td><a href="/user/${blog.user.userId}"></a> ${blog.user.userId}</td>
                                                <td>${blog.createdAt}</td>
                                                <td>${blog.likedUsers.size()}</td>
                                                <td>
                                                    <c:if test="${blog.status == 0}"><p class="blog-status-${loop.index} action" onclick="showOption(${loop.index})">Đã bị ẩn</p></c:if>
                                                    <c:if test="${blog.status == 1}"><p class="blog-status-${loop.index} action" onclick="showOption(${loop.index})">Đã được đuyệt</p></c:if>
                                                    <c:if test="${blog.status == 2}"><p class="blog-status-${loop.index} action" onclick="showOption(${loop.index})">Đang chờ xét duyệt</p></c:if>

                                                    <div class="menu-option">
                                                        <ul class="menu-list"  id="menu-${loop.index}">
                                                            <li>
                                                                <form action="" method="post">
                                                                    <input type="hidden" name="editBlog" value="POST">
                                                                    <input type="hidden" name="id" value="${blog.blogId}">
                                                                    <input type="hidden" name="status" value="0">
                                                                    <button class="">Ẩn truyện này</button>
                                                                </form>
                                                            </li>
                                                            <li>
                                                                <form>
                                                                    <input type="hidden" name="editBlog" value="POST">
                                                                    <input type="hidden" name="id" value="${blog.blogId}">
                                                                    <input type="hidden" name="status" value="1">
                                                                    <button>Công khai truyện này</button>
                                                                </form>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="card-footer">
                                <ul class="pagination jc-center" id="pagination">
                                    <jsp:include page="/views/common/pagingation.jsp" />
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <jsp:include page="/views/common/footer.jsp" />
</body>
</html>