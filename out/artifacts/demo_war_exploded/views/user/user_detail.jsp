<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="/assets/css/user/user_detail.css">
    <link rel="stylesheet" href="/assets/css/home.css">
    <title>Thông tin của ${USER.fullName}</title>
</head>
<body>
<jsp:include page="/views/common/header.jsp" />
<div id="Admin">
    <div id="main">
        <!-- Content -->
        <section class="content mt-5">
            <div class="container">
                <div class="user__manage p-3">
                    <div class="row">
                        <div class="col-4">
                            <div class="avatar-wrapper mt-4">
                                <img src="https://tse3.mm.bing.net/th?id=OIP.2qeEPUZpvbhuqtsQ_XVRKwHaJN&pid=Api&P=0" alt="avatar" class="avatar">
                            </div>
                        </div>
                        <div class="col-8">
                            <div class="user-info mt-5 ml-10">
                                <div class="user-info__item mb-2 text-white">
                                    <label for="username" class="user-info__label">Cấp độ :</label>
                                    <span class="user-info__value ml-3" id="role">
                                        <c:if test="${USER.role == 3}">Admin</c:if>
                                        <c:if test="${USER.role == 2}">Quản trị viên</c:if>
                                        <c:if test="${USER.role == 1}">Người dùng</c:if>
                                    </span>
                                </div>

                                <div class="user-info__item mb-2 text-white">
                                    <label for="username" class="user-info__label">Tên người dùng :</label>
                                    <span class="user-info__value ml-3" id="username">${USER.fullName}</span>
                                </div>
                                <div class="user-info__item mb-2 text-white">
                                    <label for="email" class="user-info__label">Email:</label>
                                    <span class="user-info__value ml-3" id="email">${USER.email}</span>
                                </div>
                                <div class="user-info__item mb-2 text-white">
                                    <label for="phone" class="user-info__label">SĐT:</label>
                                    <span class="user-info__value ml-3" id="phone">${USER.phone}</span>
                                </div>
                                <div class="user-info__item mb-2 text-white">
                                    <label for="address" class="user-info__label">Địa chỉ:</label>
                                    <span class="user-info__value ml-3" id="address">${USER.address}</span>
                                </div>
                                <c:if test="${USER_MODEL.userId == USER.userId}">
                                    <a href="/change-password" class="btn btn-primary">Chỉnh sửa thông tin cá nhân</a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container mt-5">
                <div class="row">
                    <div class="col">
                        <div class="mb-5">
                            <h3 class="user__manage-title text-center mb-4"> Truyện đã đăng</h3>
                            <table class="table table-striped table-inverse text-white user__manage-table">
                                <thead class="thead-inverse thead-dark">
                                <tr>
                                    <th>STT</th>
                                    <th>Tên truyện</th>
                                    <c:if test="${USER_MODEL.userId == USER.userId}">
                                        <th>Trạng thái</th>
                                    </c:if>
                                    <th>Số lượt thích</th>
                                </tr>
                                <c:forEach var="blog" items="${blogs}" varStatus="loop">
                                    <tr style="background-color: black">
                                        <td>${loop.index+1}</td>
                                        <td><a href="/blogs/${blog.blogId}">${blog.title}</a></td>
                                        <c:if test="${USER_MODEL.userId == USER.userId}">
                                            <td>
                                                <c:if test="${blog.status == 0}">Đã bị ẩn</c:if>
                                                <c:if test="${blog.status == 1}">Đã được duyệt</c:if>
                                                <c:if test="${blog.status == 2}">Đang chờ xét duyệt</c:if>
                                            </td>
                                        </c:if>
                                        <td>${blog.likedUsers.size()}</td>
                                    </tr>
                                </c:forEach>
                                </thead>

                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mt-4 mr-1 d-flex justify-content-center">
            </div>
        </section>
    </div>
    <jsp:include page="/views/common/footer.jsp" />
</div>