<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="parameter" value="" />
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ BlogTruyen.vn</title>
    <link rel="stylesheet" href="/assets/css/home.css">
    <link rel="stylesheet" href="/assets/css/font/themify-icons-font/themify-icons/themify-icons.css">
</head>

<body>
    <div class="web">
        <jsp:include page="/views/common/header.jsp" />

        <!-- dieu huong -->
        <div id="dieuhuong">
            <ul class="dieuhuong__ctiet">
                <li>
                    <div>
                        Thể loại
                        <i class="ti-angle-down"></i>
                    </div>
                    <ul class="dieuhuong__ctiet--tloai">
                        <c:forEach var="category" items="${categories}">
                            <c:if test="${empty keyWord}" >
                                <li><a class="category-search-url" href="/?categorySearch=${category.categoryId}">${category.name}</a></li>
                            </c:if>
                            <c:if test="${not empty keyWord}" >
                                <li><a class="category-search-url" href="/?categorySearch=${category.categoryId}&sortName=${keyWord}">${category.name}</a></li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </li>
                <li><a href="">Tác giả</a></li>
            </ul>

            <form id="search-blog" action="/" method="post">
                <div class="dieuhuong__tkiem">
                    <input type="hidden" name="categorySearch" value="${category_search.categoryId}">
                    <input class="dieuhuong__tkiem--input" type="text" placeholder="Tìm kiếm truyện" name="sortName">
                    <button class="dieuhuong__tkiem--nut" type="submit">Tìm kiếm</button>
                </div>
            </form>
        </div>

        <!-- end dieu huong -->
        <!-- noi dung chinh cua truyen -->
        <div id="ndtruyen">
            <div class="ndtruyen__dsach">
                <c:if test="${not empty category_search}" >
                    <h1 class="ket_qua_tim_kiem">
                        <span class="tieu_de_tim_kiem">Danh sách truyện có thể loại: </span>
                        <span class="nd_tim_kiem">${category_search.name}</span>
                    </h1>
                </c:if>
                <c:if test="${not empty key}">
                    <h1 class="ket_qua_tim_kiem">
                        <span class="tieu_de_tim_kiem">Kết quả tìm kiếm: </span>
                        <span class="nd_tim_kiem">${key}</span>
                    </h1>
                </c:if>
                <c:forEach items="${listA}" var="o">
                    <div class="truyen1">
                        <div class="truyen1__ndung">
                            <a href="/blogs/${o.blogId}" class="noidung">
                                <div>
                                    <h1>${o.title}</h1>
                                    <h3>${o.user.fullName}</h3>
                                    <h3>${o.createdAt}</h3>
                                </div>
                            </a>
                        </div>

                        <div class="truyen1__anh">
                            <img src="${o.imageTitle}">
                        </div>
                    </div>
                </c:forEach>


                <!-- trang khac -->
                <div class="card-footer">
                    <nav class="Page navigation">
                        <ul class="pagination jc-center" id="pagination">
                            <jsp:include page="/views/common/pagingation.jsp"/>
                        </ul>
                    </nav>
                </div>
            </div>

            <!-- Danh sach truyen -->
            <div class="truyen__moinhat">
                <h2 class="truyen__tieuDe">
                    Truyện mới nhất
                </h2>
                <ul class="truyen__moinhat-ds">
                    <c:forEach items="${New}" var="o">
                        <li class="phanTu">
                            <a href="/blogs/${o.blogId}" class="truyenmoinhat__duongdan">
                                <p class="truyen__icon">
                                        ${o.title}
                                    <i class="ti-angle-right"></i>
                                </p>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <!-- end noi dung truyen -->
        <jsp:include page="../common/footer.jsp"></jsp:include>
    </div>
</body>

</html>