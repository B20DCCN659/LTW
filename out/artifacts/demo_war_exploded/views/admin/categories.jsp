<%@ page import="com.example.btl_web.constant.Constant" %>
<%@ page import="com.example.btl_web.constant.Constant.*" %>
<%@ page import="com.example.btl_web.dto.CategoryDto" %>
<%@ page import="com.example.btl_web.paging.Pageable" %>
<%@ page import="com.example.btl_web.paging.PageRequest" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.btl_web.service.CategoryService" %>
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
<%@ page import="com.example.btl_web.service.BlogService" %>
<%@ page import="com.example.btl_web.dto.BlogDto" %>
<%@ page import="com.example.btl_web.service.UserService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="api_url" value="/api-admin-category" />
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
    <title>Quản lý thể loại</title>
</head>
<body>
    <%
        String editMethod = request.getParameter("editMethod");
        if(editMethod != null)
        {
            String categoryIdStr = request.getParameter("id");
            String statsStr = request.getParameter("status");
            CategoryDto editCategory = new CategoryDto();
            if(categoryIdStr != null)
            {
                editCategory.setCategoryId(Long.parseLong(categoryIdStr));
            }
            if(statsStr != null)
            {
                editCategory.setStatus(Integer.parseInt(statsStr));
            }

            Gson gson = new Gson();
            String categoryJson = gson.toJson(editCategory);

            String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();

            String url = Constant.DOMAIN;
            if(editMethod.equals(Request.DELETE_METHOD))
                url += Admin.DELETE_CATEGORY_API;
            else if(editMethod.equals(Request.PUT_METHOD))
                url += Admin.CATEGORY_API;

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPut sendApi = new HttpPut(url);
            StringEntity jsonEntity = new StringEntity(categoryJson);
            sendApi.addHeader("content-type", "application/json");
            sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
            sendApi.setEntity(jsonEntity);

            //Nhận phản hồi
            HttpResponse apiResponse = httpClient.execute(sendApi);
            HttpEntity entity = apiResponse.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            String status = "alert";
            //System.out.println(responseString);

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
        CategoryService categoryService = ServiceConfiguration.getCategoryService();

        StringBuilder pageUrl = new StringBuilder(Constant.Admin.CATEGORIES_PAGE + "?");

        CategoryDto searchDto = null;
        String searchName = request.getParameter("search-category");
        if(searchName != null)
        {
            searchDto = new CategoryDto();
            searchDto.setName(searchName);
            pageUrl.append("search-category=" + searchName + "&");
        }
        pageUrl.append("page=");

        long totalCategories = categoryService.countCategories(searchDto);
        Pageable pageable = new PageRequest(request.getParameterMap(), totalCategories );
        request.setAttribute("pageable", pageable);
        List<CategoryDto> categoryDtos = categoryService.findAll(pageable, searchDto);
        //Đếm xem mỗi thể loại có bao nhiêu truyện
        BlogService blogService = ServiceConfiguration.getBlogService();
        UserService userService = ServiceConfiguration.getUserService();
        for(CategoryDto category: categoryDtos)
        {
            BlogDto countBlog = new BlogDto();
            countBlog.setStatus(1);
            countBlog.addACategory(category);

            category.setBlogsHaveCategory(blogService.countBlogs(countBlog));
            category.setUser(userService.findOneById(category.getUserId()));
        }

        request.setAttribute("list", categoryDtos);
        request.setAttribute("home", pageUrl.toString());
        request.setAttribute("blogs_page", Constant.Admin.BLOGS_PAGE);
        request.setAttribute("users_page", Constant.Admin.USERS_PAGE);
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
                                    <form action="" method="get">
                                        <div class="tim-kiem">
                                            <input type="text" placeholder="Tìm Kiếm" class="search" name="search-category" value="${keySearch}">
                                            <button class="btn btn-search">Tìm kiếm</button>
                                        </div>
                                    </form>
                                    <c:if test="${not empty keySearch}" >
                                        <p>Tìm kiếm thể loại theo từ khoá: ${keySearch}</p>
                                    </c:if>
                                </div>
                                <div class="float-right">
                                    <a href="/admin/categories/edit/-1" class="btn btn-outline-primary">Thêm thể loại</a>
                                </div>
                            </div>
                            <div class="card-body">
                                <table class="table" style="border: solid 1px #000;">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th>Stt</th>
                                        <th>Id</th>
                                        <th>Tên</th>
                                        <th>Thời gian tạo</th>
                                        <th>Số lượng Truyện</th>
                                        <th>Người thêm</th>
                                        <th>Trạng thái</th>
                                        <th>Chỉnh sửa</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="item" items="${list}" varStatus="loop">
                                        <tr>
                                            <td>${loop.index + 1}</td>
                                            <td>${item.categoryId}</td>
                                            <td> <a href="/?categorySearch=${item.categoryId}">${item.name}</a> </td>
                                            <td>${item.createdAt}</td>
                                            <td>${item.blogsHaveCategory}</td>
                                            <td> <a href="/user/${item.userId}">${item.user.fullName}</a> </td>
                                            <td>
                                                <c:if test="${item.status == 1}" >
                                                    <p class="blog-status-${loop.index} action" onclick="showOption(${loop.index})">Công khai</p>
                                                </c:if>
                                                <c:if test="${item.status == 0}" >
                                                    <p class="blog-status-${loop.index} action" onclick="showOption(${loop.index})">Đã ẩn</p>
                                                </c:if>

                                                <div class="menu-option">
                                                    <ul class="menu-list"  id="menu-${loop.index}">
                                                        <li>
                                                            <form action="" method="post">
                                                                <input type="hidden" name="editMethod" value="POST">
                                                                <input type="hidden" name="id" value="${item.categoryId}">
                                                                <input type="hidden" name="status" value="0">
                                                                <button>Ẩn thể loại này</button>
                                                            </form>
                                                        </li>
                                                        <li>
                                                            <form action="" method="post">
                                                                <input type="hidden" name="editMethod" value="PUT">
                                                                <input type="hidden" name="id" value="${item.categoryId}">
                                                                <input type="hidden" name="status" value="1">
                                                                <button>Công khai thể loại này</button>
                                                            </form>
                                                        </li>
                                                        <c:if test="${USER_MODEL.role == 3}">
                                                            <li>
                                                                <form action="" method="post">
                                                                    <input type="hidden" name="editMethod" value="DELETE">
                                                                    <input type="hidden" name="id" value="${item.categoryId}">
                                                                    <button>Xóa thể loại này</button>
                                                                </form>
                                                            </li>
                                                        </c:if>
                                                    </ul>
                                                </div>
                                            </td>
                                            <td>
                                                <a href="/admin/categories/edit/${item.categoryId}">Chỉnh sửa</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="card-footer">
                                <ul class="pagination jc-center" id="pagination">
                                    <jsp:include page="/views/common/pagingation.jsp"/>
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