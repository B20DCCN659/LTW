<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/assets/css/font/themify-icons-font/themify-icons/themify-icons.css">
<!-- tieu de -->
<div id="tieude">
    <div class="tieude__link">
        <a class="" href="/">BlogTruyen.vn</a>
    </div>

    <div class="tieude__nut">
        <div class="tieude__nut1">
            <c:set var="role" value="${USER_MODEL.role}" />
            <c:if test="${role >= 2}" >
                <div>
                    <a href="/admin/categories">Quản lý thể loại</a>
                </div>
                <div>
                    <a href="/admin/blogs">Quản lý bài viết</a>
                </div>
                <div>
                    <a href="/admin/users">Quản lý người dùng</a>
                </div>
            </c:if>
            <div>
                <a class="tieude__nut--icon" href="/statistical">
                    <i class="search-icon ti-bar-chart-alt"></i>
                    Thống kê</a>
            </div>
            <div>
                <a class="tieude__nut--icon" href="">
                    <i class="search-icon ti-announcement"></i>
                    Thông báo</a>
            </div>
            <div>
                <a class="tieuDe__nut--icon" href="">
                    <i class="search-icon ti-help-alt"></i>
                    Trợ giúp</a>
            </div>
            <div>
                <a class="tieuDe__nut--icon" href="/create-blog">
                    <i class="search-icon ti-write"></i>
                    Viết truyện</a>
            </div>
            <c:if test="${empty USER_MODEL}" >
                <div>
                    <a class="tieuDe__nut--icon" href="/login">
                        <i class="ti-layout-grid2-alt"></i>
                        Đăng nhập</a>
                </div>
            </c:if>
        </div>

        <c:if test="${not empty USER_MODEL}" >
            <div class="avatar">
                <img src="https://tse3.mm.bing.net/th?id=OIP.2qeEPUZpvbhuqtsQ_XVRKwHaJN&pid=Api&P=0" alt="Avatar"
                     class="avatar_anh" onclick="showMenu()">
                <div class="avatar_ndung">
                    <ul class="avatar_menu" id="menu">
                        <li>
                            <a href="/user/${USER_MODEL.userId}">
                                <i class="avatar_icon ti-user"></i>
                                Cá nhân</a>
                        </li>
                        <li>
                            <a href="#">
                                <i class=" avatar_icon ti-settings"></i>
                                Cài đặt</a>
                        </li>
                        <li>
                            <a href="#">
                            <i class="avatar_icon ti-help-alt"></i>
                            Trợ giúp</a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="avatar_icon ti-info-alt"></i>
                                Phản hồi</a>
                        </li>
                        <li>
                            <a href="/login?action=logout">
                                <i class="avatar_icon ti-drupal"></i>
                                Đăng xuất</a>
                        </li>
                    </ul>

                </div>
            </div>
        </c:if>

    </div>
    <script>
        function showMenu() {
            var menu = document.getElementById("menu");
            if (menu.style.display === "block") {
                menu.style.display = "none";
            } else {
                menu.style.display = "block";
            }

            document.addEventListener("click", (event) => {
                    if (!event.target.closest(".avatar")) {
                        menu.style.display = "none";
                    }
                }
            );
        }

        function showOption(id) {
            var menu = document.getElementById("menu-" + id);
            if (menu.style.display === "block") {
                menu.style.display = "none";
            } else {
                menu.style.display = "block";
            }

            document.addEventListener("click", (event) => {
                    if (!event.target.closest('.blog-status-' + id)) {
                        menu.style.display = "none";
                    }
                }
            );
        }
    </script>
</div>
<!-- end  tieu de -->