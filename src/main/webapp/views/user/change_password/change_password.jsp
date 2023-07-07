<%@ page import="com.example.btl_web.dto.UserDto" %>
<%@ page import="com.example.btl_web.utils.CookieUtils" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@ page import="org.apache.http.entity.StringEntity" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.example.btl_web.constant.Constant" %>
<%@ page import="com.example.btl_web.constant.Constant.*" %>
<%@ page import="org.apache.http.client.methods.HttpPut" %>
<%@ page import="com.example.btl_web.utils.SessionUtils" %>
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
        UserDto user = (UserDto) SessionUtils.getInstance().getValue(request, Constant.USER_MODEL);
        request.setAttribute("user", user);

        String method = request.getParameter("_method");
        if(method != null)
        {
            String cookieValue = CookieUtils.getInstance().getValue(request, "JSESSIONID", request.getSession().getId()).toString();

            String passWord = request.getParameter("pass-word");
            String newPassWord = request.getParameter("new-password");
            String re_newPassWord = request.getParameter("re-new-password");

            UserDto changePasswordUser = new UserDto();
            changePasswordUser.setPassWord(passWord);
            changePasswordUser.setRe_password(newPassWord);
            changePasswordUser.setEmail(re_newPassWord);

            Gson gson = new Gson();
            String userJson = gson.toJson(changePasswordUser);

            String url = Constant.DOMAIN + User.CHANGE_PASSWORD_API;
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPut sendApi = new HttpPut(url);
            StringEntity params = new StringEntity(userJson, StandardCharsets.UTF_8);
            sendApi.addHeader("content-type", "application/json");
            sendApi.setHeader("Cookie", "JSESSIONID=" + cookieValue);
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
                request.setAttribute("success", messages[0]);
                status = "notice";
            }
            else
                request.setAttribute("bug", messages);
            request.setAttribute("status", status);
        }
    %>

    <div class="pchinh">
        <div class="tieude">
            <div class="dhuong-tieude">
                <a href="#" class="dhuong-tieude--chinh">
                    BlogTruyen.vn
                </a>
            </div>
        </div>
        <section>
            <div class="hopmau">
                <form action="" method="post" >
                    <input type="hidden" name="_method" value="post">
                    <div class="gtri-hopmau">
                        <h2>Đổi mật khẩu</h2>
                        <p class="${status}">${success}</p>
                        <div class="o-nhaptt">
                            <input type="password" name="pass-word" required>
                            <label>Mật khẩu hiện tại</label>
                        </div>
                        <span class="bug" id="bug1">${bug[0]}</span>
                        <div class="o-nhaptt">
                            <input type="password" name="new-password" required>
                            <label>Mật khẩu mới</label>
                        </div>
                        <span class="bug">${bug[1]}</span>
                        <div class="o-nhaptt">
                            <input type="password" name="re-new-password" required>
                            <label>Xác nhận mật khẩu</label>
                        </div>
                        <span class="bug">${bug[1]}</span>
                        <button class="nut">Đổi mật khẩu</button>
                        <div class="dki">
                            <p><a href="/user/${user.userId}">Quay trở về trang thông tin người dùng</a></p>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</body>
</html>