<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="parameter" value="" />
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="../../assets/css/statistical/statistical.css">
    <link rel="stylesheet" href="./themify-icons/themify-icons.css">
</head>

<body>

<div class="web">
    <jsp:include page="/views/common/header.jsp" />

    <!-- Start nội dung -->
    <div id="noidung">
        <div class="truyen__moinhat">
            <h2 class="truyen__tieuDe">
                Truyện nhiều like nhất
            </h2>
            <ul class="truyen__moinhat-ds">
                <li class="deMuc">
                    <h3>Tên truyện</h3>
                    <h3 class="soluong">Số lượng</h3>
                </li>
                <c:forEach items="${Like}" var = "o">
                    <li class="phanTu">
                        <a href="/blogs/${o.blogId}" class="truyenmoinhat__duongdan">
                            <p class="truyen__icon">
                                    ${o.title}
                                <i>${o.num_Like}</i>
                            </p>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <div class="truyen__moinhat">
            <h2 class="truyen__tieuDe">
                Truyện nhiều comment nhất
            </h2>
            <ul class="truyen__moinhat-ds">
                <li class="deMuc">
                    <h3>Tên truyện</h3>
                    <h3 class="soluong">Số lượng</h3>
                </li>
                <c:forEach items="${Cmt}" var="o">
                    <li class="phanTu">
                        <a href="/blogs/${o.blogId}" class="truyenmoinhat__duongdan">
                            <p class="truyen__icon">
                                    ${o.title}
                                <i>${o.num_Comment}</i>
                            </p>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <div class="truyen__moinhat">
            <h2 class="truyen__tieuDe">
                Thể loại nhiều truyện nhất
            </h2>
            <ul class="truyen__moinhat-ds">
                <li class="deMuc">
                    <h3>Tên thể loại</h3>
                    <h3 class="soluong">Số lượng truyện</h3>
                </li>
                <c:forEach items="${Cate}" var="o">
                    <li class="phanTu">
                        <a href="#" class="truyenmoinhat__duongdan">
                            <p class="truyen__icon">
                                    ${o.name}
                                <i>${o.num_blog}</i>
                            </p>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <div class="truyen__moinhat">
            <h2 class="truyen__tieuDe">
                Tác giả viết nhiều truyện nhất
            </h2>
            <ul class="truyen__moinhat-ds">
                <li class="deMuc">
                    <h3>Tên tác giả</h3>
                    <h3 class="soluong">Số lượng</h3>
                </li>
                <c:forEach items="${cntBlog}" var="o">
                    <li class="phanTu">
                        <a href="#" class="truyenmoinhat__duongdan">
                            <p class="truyen__icon">
                                    ${o.fullName}
                                <i>${o.countBlog}</i>
                            </p>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <%--  <jsp:include page="../common/footer.jsp"></jsp:include>--%>
    </div>
</body>