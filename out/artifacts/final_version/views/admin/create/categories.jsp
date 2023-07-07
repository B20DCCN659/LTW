<%@ page import="com.example.btl_web.dto.CategoryDto" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@ page import="org.apache.http.client.methods.HttpPost" %>
<%@ page import="org.apache.http.entity.StringEntity" %>
<%@ page import="com.example.btl_web.utils.CookieUtils" %>
<%@ page import="org.apache.http.client.methods.HttpPut" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="com.example.btl_web.constant.Constant.*" %>
<%@ page import="com.example.btl_web.service.CategoryService" %>
<%@ page import="com.example.btl_web.configuration.ServiceConfiguration" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.example.btl_web.constant.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="api_url" value="/api-admin-category"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="../../../assets/css/admin/admin_create1.css">
    <link rel="stylesheet" href="/assets/css/home.css">
    <title>Thêm thể loại</title>
</head>
<body>
    <%
        request.setCharacterEncoding("UTF-8");

        String method = request.getParameter("_method");
        if(method != null)
        {
            String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();

            String categoryIdStr = request.getParameter("categoryId");
            String name = request.getParameter("name");

            CategoryDto editCategory = new CategoryDto();
            if(categoryIdStr != null && !categoryIdStr.isEmpty())
                editCategory.setCategoryId(Long.parseLong(categoryIdStr));
            editCategory.setName(name);
            Gson gson = new Gson();
            String categoryJson = gson.toJson(editCategory);

            String url = Constant.DOMAIN + Admin.CATEGORY_API;
            HttpClient httpClient = HttpClientBuilder.create().build();

            StringEntity param = new StringEntity(categoryJson, StandardCharsets.UTF_8);

            HttpResponse apiResponse = null;
            if (method.equals("POST"))
            {
                HttpPost sendApi = new HttpPost(url);
                sendApi.addHeader("content-type", "application/json");
                sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
                sendApi.setEntity(param);
                //Nhận phản hồi
                apiResponse = httpClient.execute(sendApi);
            }
            else if (method.equals("PUT"))
             {
                HttpPut sendApi = new HttpPut(url);
                sendApi.addHeader("content-type", "application/json");
                sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
                sendApi.setEntity(param);
                //Nhận phản hồi
                apiResponse = httpClient.execute(sendApi);
            }

            String status = "red";
            HttpEntity entity = apiResponse.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            int statusCode = apiResponse.getStatusLine().getStatusCode();

            if(statusCode == 200)
            {
                status = "blue";
            }

            //Xoá bỏ "" ở đầu và cuối của Json
            responseString = responseString.replaceAll("[\\[\\]\"]", "");
            request.setAttribute("status", status);
            request.setAttribute("message", responseString);
        }
    %>

    <%
        CategoryService categoryService = ServiceConfiguration.getCategoryService();
        String categoryIdStr = request.getAttribute("path_infor").toString().split("/")[1];
        Long categoryId = Long.parseLong(categoryIdStr);

        CategoryDto categoryDto = new CategoryDto();
        if(categoryId != -1)
        {
            categoryDto.setCategoryId(categoryId);
            categoryDto = categoryService.findOneBy(categoryDto);
        }

        request.setAttribute("category", categoryDto);
        request.setAttribute("users", Admin.USERS_PAGE);
        request.setAttribute("blogs", Admin.BLOGS_PAGE);
        request.setAttribute("categories", Admin.CATEGORIES_PAGE);
    %>

    <div id="Admin">
        <div class="dhuong-chinh">
            <jsp:include page="/views/common/header.jsp" />
        </div>
        <div style="margin-top: 70px" id="trangchinh">
            <p style="color: ${status}">${message}</p>
            <!-- Content -->
            <section class="ndung">
                <div class="dong-mot ltren-nam">
                    <div class="cot-sau lgiua">
                        <form action="#" method="post">
                            <input type="hidden" name="_csrf" value="5bbb96ac-26ce-4f51-9d7c-3b71a2dae617">
                            <div class="the">
                                <div class="the-dau">
                                    <c:if test="${empty category.categoryId}">
                                        <h2>Thêm thể loại</h2>
                                    </c:if>
                                    <c:if test="${not empty category.categoryId}">
                                        <h2>Thay đổi thông tin thể loại</h2>
                                    </c:if>
                                </div>
                                <div class="the-giua">
                                    <form class="ksoat-nhom ltren-bon" id="formSubmit" action="" method="post">
                                        <c:if test="${empty category.categoryId}">
                                            <c:set var="method_value" value="POST"/>
                                        </c:if>
                                        <c:if test="${not empty category.categoryId}">
                                            <c:set var="method_value" value="PUT"/>
                                        </c:if>
                                        <input type="hidden" name="_method" value="${method_value}">
                                        <input type="hidden" id="categoryId" name="categoryId" value = ${category.categoryId}>
                                        <label>Thể loại</label>
                                        <p id="name-error"></p>
                                        <input type="text" class="ksoat-mau" name="name" id="name" placeholder="Tên thể loại" value="${category.name}">

                                        <button class="nut nut-tcong lphai-hai">
                                            <i class="fa-solid fa-floppy-disk "></i>
                                            Lưu
                                        </button>
                                    </form>
                                </div>
                                <div class="the-cuoi">
                                    <a href="/admin/categories" class="nut nut-sdang">
                                        <i class="fa-solid fa-list "></i>
                                        Danh sách thể loại
                                    </a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </section>
        </div>
    </div>
</body>
</html>