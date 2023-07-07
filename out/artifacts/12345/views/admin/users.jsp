<%@ page import="com.example.btl_web.constant.Constant" %>
<%@ page import="com.example.btl_web.dto.UserDto" %>
<%@ page import="com.example.btl_web.paging.Pageable" %>
<%@ page import="com.example.btl_web.paging.PageRequest" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.btl_web.service.UserService" %>
<%@ page import="com.example.btl_web.configuration.ServiceConfiguration" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.example.btl_web.utils.CookieUtils" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@ page import="org.apache.http.client.methods.HttpPut" %>
<%@ page import="org.apache.http.entity.StringEntity" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.example.btl_web.dto.BlogDto" %>
<%@ page import="com.example.btl_web.service.BlogService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="api_url" value="/api-admin-user" />
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
    <title>Quản lý nguời dùng</title>
</head>
<body>
    <%
        String editMethod = request.getParameter("editMethod");
        if(editMethod != null)
        {
            String userIdStr = request.getParameter("id");
            String statsStr = request.getParameter("status");
            UserDto editUser = null;
            if(userIdStr != null && statsStr != null)
            {
                editUser = new UserDto();
                editUser.setUserId(Long.parseLong(userIdStr));
                editUser.setStatus(Integer.parseInt(statsStr));
            }

            Gson gson = new Gson();
            String userJson = gson.toJson(editUser);

            String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();

            String url = "http://localhost:8080/api-admin-user";
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPut sendApi = new HttpPut(url);
            StringEntity jsonEntity = new StringEntity(userJson);
            sendApi.addHeader("content-type", "application/json");
            sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
            sendApi.setEntity(jsonEntity);

            //Nhận phản hồi
            HttpResponse apiResponse = httpClient.execute(sendApi);
            HttpEntity entity = apiResponse.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            String []error = (new ObjectMapper()).readValue(responseString, String[].class);

            String status = "alert";
            //System.out.println(responseString);

            int statusCode = apiResponse.getStatusLine().getStatusCode();
            if(statusCode == 200)
            {
                status = "notice";
            }

            request.setAttribute("status", status);
            request.setAttribute("message", error[0]);
        }
    %>

    <%
        UserService userService = ServiceConfiguration.getUserService();

        StringBuilder pageUrl = new StringBuilder(Constant.Admin.USERS_PAGE + "?");

        UserDto searchDto = null;

        String searchName = request.getParameter("searchName");
        if(searchName != null)
        {
            searchDto = new UserDto();
            searchDto.setFullName(searchName);
            pageUrl.append("searchName=" + searchName + "&");
        }
        pageUrl.append("page=");

        long totalItem = userService.countUsers(searchDto);
        Pageable pageable = new PageRequest(request.getParameterMap(), totalItem);

        //Đếm mỗi người đã viết bao nhiêu truyện
        BlogService blogService = ServiceConfiguration.getBlogService();
        List<UserDto> dtos = userService.findAll(pageable, searchDto);
        for(UserDto user: dtos)
        {
            BlogDto countBlog = new BlogDto();
            countBlog.setUser(user);
            countBlog.setStatus(1);
            user.setUploadedBlog(blogService.countBlogs(countBlog));
        }

        request.setAttribute("users_list", dtos);
        request.setAttribute("pageable", pageable);
        request.setAttribute("categories_page", Constant.Admin.CATEGORIES_PAGE);
        request.setAttribute("blogs_page", Constant.Admin.BLOGS_PAGE);
        request.setAttribute("home", pageUrl.toString());
        request.setAttribute("searchName", searchName);
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
                                    <form action="" method="get">
                                        <div class="tim-kiem">
                                            <input type="text" placeholder="Tìm Kiếm" class="search" name="searchName" value="${searchName}">
                                            <button class="btn btn-search">Tìm kiếm</button>
                                        </div>
                                    </form>

                                    <c:if test="${not empty searchName}">
                                        <p>Tìm kiếm các tài khoản theo từ khoá: ${searchName}</p>
                                    </c:if>
                                </div>
                            </div>
                            <div class="card-body">
                                <table class="table" style="border: solid 1px #000;">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th>STT</th>
                                        <th>Id</th>
                                        <th>Tên</th>
                                        <th>Email</th>
                                        <th>Cấp bậc</th>
                                        <th>Truyện đã viết</th>
                                        <th>Thời gian tạo</th>
                                        <th>Trạng thái</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${users_list}" varStatus="loop">
                                            <tr>
                                                <td>${loop.index+1}</td>
                                                <td>${user.userId}</td>
                                                <td> <a href="/user/${user.userId}">${user.fullName}</a> </td>
                                                <td>${user.email}</td>
                                                <td>${user.role}</td>
                                                <td>${user.uploadedBlog}</td>
                                                <td>
                                                    <p>${user.registeredAt}</p>
                                                </td>
                                                <td>
                                                    <c:if test="${user.status == 1}" >
                                                        <p class="blog-status-${loop.index} action" onclick="showOption(${loop.index})">Đang hoạt động</p>
                                                    </c:if>
                                                    <c:if test="${user.status == 0}" >
                                                        <p class="blog-status-${loop.index} action" onclick="showOption(${loop.index})">Đã bị khóa</p>
                                                    </c:if>

                                                    <div class="menu-option">
                                                        <ul class="menu-list"  id="menu-${loop.index}">
                                                            <li>
                                                                <form action="" method="post">
                                                                    <input type="hidden" name="editMethod" value="POST">
                                                                    <input type="hidden" name="id" value="${user.userId}">
                                                                    <input type="hidden" name="status" value="0">
                                                                    <button>Khóa tài khoản này</button>
                                                                </form>
                                                            </li>
                                                            <li>
                                                                <form action="" method="post">
                                                                    <input type="hidden" name="editMethod" value="PUT">
                                                                    <input type="hidden" name="id" value="${user.userId}">
                                                                    <input type="hidden" name="status" value="1">
                                                                    <button>Mở khóa tài khoản này</button>
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
                                <nav class="Page navigation">
                                    <ul class="pagination jc-center" id="pagination">
                                        <jsp:include page="/views/common/pagingation.jsp" />
                                    </ul>
                                </nav>
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