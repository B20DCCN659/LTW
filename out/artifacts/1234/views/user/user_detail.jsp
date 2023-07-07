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
    <div id="trangchinh">
        <!-- Content -->
        <section class="ndung ltren-nam">
            <div class="thung-dung">
                <div class="qli-ndung dem-ba">
                    <div class="dong-mot">
                        <div class="cot-bon">
                            <div class="avatar-wrapper ltren-bon">
                                <img src="https://tse3.mm.bing.net/th?id=OIP.2qeEPUZpvbhuqtsQ_XVRKwHaJN&pid=Api&P=0" alt="avatar" class="avatar">
                            </div>
                        </div>
                        <div class="cot-tam">
                            <div class="info-ndung ltren-nam ltrai-muoi">
                                <div class="info-ndung_muc lduoi-hai text-white ">
                                    <label for="username" class="info-ndung_nnhan">Cấp độ :</label>
                                    <span class="info-ndung-gtri ltrai-ba" id="role">
                                        <c:if test="${USER.role == 3}">Admin</c:if>
                                        <c:if test="${USER.role == 2}">Quản trị viên</c:if>
                                        <c:if test="${USER.role == 1}">Người dùng</c:if>
                                    </span>
                                </div>

                                <div class="info-ndung_muc lduoi-hai text-white">
                                    <label for="username" class="info-ndung_nnhan">Tên người dùng :</label>
                                    <span class="info-ndung-gtri ltrai-ba" id="username">${USER.fullName}</span>
                                </div>
                                <div class="info-ndung_muc lduoi-hai text-white ">
                                    <label for="email" class="info-ndung_nnhan">Email:</label>
                                    <span class="info-ndung-gtri ltrai-ba" id="email">${USER.email}</span>
                                </div>
                                <div class="info-ndung_muc lduoi-hai text-white">
                                    <label for="phone" class="info-ndung_nnhan">SĐT:</label>
                                    <span class="info-ndung-gtri ltrai-ba" id="phone">${USER.phone}</span>
                                </div>
                                <div class="info-ndung_muc lduoi-hai text-white">
                                    <label for="address" class="info-ndung_nnhan">Địa chỉ:</label>
                                    <span class="info-ndung-gtri ltrai-ba" id="address">${USER.address}</span>
                                </div>
                                <c:if test="${USER_MODEL.userId == USER.userId}">
                                    <a href="/change-password" class="nut nut-sdang">Chỉnh sửa thông tin cá nhân</a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="thung-dung ltren-nam">
                <div class="dong-mot">
                    <div class="cot-mot">
                        <div class="lduoi-nam">
                            <h3 class="qli-ndung-tieude text-center lduoi-bon"> Truyện đã đăng</h3>
                            <table class="bang bang-soc bang-dao text-white bang_qli-ndung">
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