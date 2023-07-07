<%@ page import="com.example.btl_web.dto.UserDto" %>
<%@ page import="com.example.btl_web.utils.CookieUtils" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@ page import="org.apache.http.client.methods.HttpPost" %>
<%@ page import="org.apache.http.entity.StringEntity" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.example.btl_web.constant.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="api_url" value="/api-create-user" />
<!DOCTYPE html>
<html lang="en">
<head>
  <link rel="stylesheet" href="../../assets/css/registraion/registraion5.css">
  <title>Blog Truyện - Đăng kí</title>
</head>
<body>
    <%
        String method = request.getParameter("_method");
        if(method != null)
        {
            UserDto newUser = new UserDto();
            String fullName = request.getParameter("fullName");
            String userName = request.getParameter("userName");
            String passWord = request.getParameter("passWord");
            String rePassWord = request.getParameter("passWord-2");
            String email = request.getParameter("email");

            newUser.setFullName(fullName);
            newUser.setUserName(userName);
            newUser.setPassWord(passWord);
            newUser.setRe_password(rePassWord);
            newUser.setEmail(email);

            Gson gson = new Gson();
            String userJson = gson.toJson(newUser);

            String url = Constant.DOMAIN + "/registration";
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost sendApi = new HttpPost(url);
            StringEntity params = new StringEntity(userJson, StandardCharsets.UTF_8);
            sendApi.addHeader("content-type", "application/json");
            sendApi.setEntity(params);

            //Nhận phản hồi
            HttpResponse apiResponse = httpClient.execute(sendApi);
            HttpEntity entity = apiResponse.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            String[] messages = (new ObjectMapper()).readValue(responseString, String[].class);
            String status = "alert";

            int statusCode = apiResponse.getStatusLine().getStatusCode();
            if(statusCode == 200)
            {
                response.sendRedirect("/login?action=sign-up-success");
            }
            request.setAttribute("user", newUser);
            request.setAttribute("status", status);
            request.setAttribute("bug", messages);
        }
    %>

    <div class="main">
        <div class="header">
            <div class="header__navbar">
                <a href="#" class="header__navbar--main">
                    BlogTruyen.vn
                </a>
            </div>
        </div>
        <section>
            <div class="form-box">
                <form action="" method="post">
                    <input type="hidden" name="_method" value="post">
                    <div class="form-value">
                        <h2>Đăng kí</h2>
                        <div class="inputbox">
                            <input type="text" name="fullName" value="${user.fullName}" required>
                            <label>Tên người dùng</label>
                        </div>
                        <span class="bug" id="bug1">${bug[0]}</span>
                        <div class="inputbox">
                            <input type="text" name="userName" id="user-name" value="${user.userName}" required>
                            <label>Tên đăng nhập</label>
                        </div>
                        <span class="bug">${bug[1]}</span>
                        <div class="inputbox">
                            <input type="password" name="passWord" value="${user.passWord}" id="pass-word" required>
                            <label>Mật khẩu</label>
                        </div>
                        <span class="bug">${bug[2]}</span>
                        <div class="inputbox">
                            <input type="password" name="passWord-2" id="re-pass-word" value="${user.re_password}" required>
                            <label>Nhập lại mật khẩu</label>
                        </div>
                        <span class="bug">${bug[3]}</span>
                        <div class="inputbox">
                            <input type="mail" name="email" id="email" value="${user.email}" required>
                            <label>Email</label>
                        </div>
                        <span class="bug">${bug[4]}</span>
                        <button class="btn">Đăng ký</button>
                        <div class="register">
                            <p>Bạn đã có tài khoản rồi ? <a href="../../login">Đăng nhập</a></p>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</body>
</html>