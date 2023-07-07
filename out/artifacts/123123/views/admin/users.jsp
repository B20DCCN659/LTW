<%@ page import="com.example.btl_web.constant.Constant" %>
<%@ page import="com.example.btl_web.constant.Constant.*" %>
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
            String roleStr = request.getParameter("role");

            UserDto editUser = new UserDto();
            if(userIdStr != null)
            {
                editUser.setUserId(Long.parseLong(userIdStr));
            }
            if(statsStr != null)
            {
                editUser.setStatus(Integer.parseInt(statsStr));
            }
            if(roleStr != null)
            {
                editUser.setRole(Integer.parseInt(roleStr));
            }

            Gson gson = new Gson();
            String userJson = gson.toJson(editUser);

            String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();

            String url = Constant.DOMAIN;
            if(editMethod.equals(Request.DELETE_METHOD))
                url += Admin.DELETE_USER_API;
            else if(editMethod.equals(Request.POST_METHOD) || editMethod.equals(Request.PUT_METHOD))
                url += Admin.USER_API;

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
        <div class="dhuong-chinh">
            <jsp:include page="/views/common/header.jsp" />
        </div>
        <div id="trangchinh">
            <!-- MAIN -->
            <div class="thung-dung">
                <div class="dhuong">
                    <div class="icon--link">
                        <i class="icon fa-solid fa-book-open"></i>
                    </div>
                </div>
            </div>
            <p class="${status}">${message}</p>
            <!-- Content -->
            <section class="ndung">
                <div class="dong-mot ltren-nam">
                    <div class="cot-mot">
                        <div class="the">
                            <div class="the-dau">
                                <div class="float-left">
                                    <form action="" method="get">
                                        <div class="tim-kiem">
                                            <input type="text" placeholder="Tìm Kiếm" class="search" name="searchName" value="${searchName}">
                                            <button class="nut nut-tkiem">Tìm kiếm</button>
                                        </div>
                                    </form>

                                    <c:if test="${not empty searchName}">
                                        <p>Tìm kiếm các tài khoản theo từ khoá: ${searchName}</p>
                                    </c:if>
                                </div>
                            </div>
                            <div class="the-giua">
                                <table class="bang" style="border: solid 1px #000;">
                                    <thead class="bang-den">
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
                                                <td>
                                                    <c:if test="${user.role == 1}">User</c:if>
                                                    <c:if test="${user.role == 2}">Quản trị viên</c:if>
                                                    <c:if test="${user.role == 3}">Admin</c:if>
                                                </td>
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

                                                    <div class="cdat-menu">
                                                        <ul class="dsach-menu"  id="menu-${loop.index}">
                                                            <c:if test="${user.status == 1}">
                                                                <li>
                                                                    <form action="" method="post">
                                                                        <input type="hidden" name="editMethod" value="POST">
                                                                        <input type="hidden" name="id" value="${user.userId}">
                                                                        <input type="hidden" name="status" value="0">
                                                                        <button>Khóa tài khoản này</button>
                                                                    </form>
                                                                </li>
                                                            </c:if>
                                                            <c:if test="${user.status == 0}">
                                                                <li>
                                                                    <form action="" method="post">
                                                                        <input type="hidden" name="editMethod" value="PUT">
                                                                        <input type="hidden" name="id" value="${user.userId}">
                                                                        <input type="hidden" name="status" value="1">
                                                                        <button>Mở khóa tài khoản này</button>
                                                                    </form>
                                                                </li>
                                                            </c:if>
                                                            <c:if test="${USER_MODEL.role == 3}">
                                                                <c:if test="${user.role == 1}">
                                                                    <li>
                                                                        <form action="" method="post">
                                                                            <input type="hidden" name="editMethod" value="PUT">
                                                                            <input type="hidden" name="id" value="${user.userId}">
                                                                            <input type="hidden" name="role" value="2">
                                                                            <button>Chuyển cấp độ lên quản trị viên</button>
                                                                        </form>
                                                                    </li>
                                                                </c:if>
                                                                <c:if test="${user.role == 2}">
                                                                    <li>
                                                                        <form action="" method="post">
                                                                            <input type="hidden" name="editMethod" value="PUT">
                                                                            <input type="hidden" name="id" value="${user.userId}">
                                                                            <input type="hidden" name="role" value="1">
                                                                            <button>Chuyển cấp độ xuống người dùng</button>
                                                                        </form>
                                                                    </li>
                                                                </c:if>
                                                                <li>
                                                                    <form action="" method="post">
                                                                        <input type="hidden" name="editMethod" value="DELETE">
                                                                        <input type="hidden" name="id" value="${user.userId}">
                                                                        <button>Xóa tài khoản này</button>
                                                                    </form>
                                                                </li>
                                                            </c:if>
                                                        </ul>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="the-cuoi">
                                <nav class="Page navigation">
                                    <ul class="ptrang jc-giua" id="pagination">
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