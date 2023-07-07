<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" href="/assets/css/home.css">

    <style>
        .title{
            font-weight: bold;
        }

        ul
        {
            margin-bottom: 10px;
        }

        div#editor {
            padding: 30px;
            width: 81%;
            margin: auto;
            margin-top: 100px;
            text-align: left;
            background-color: white;
        }

        #dang {
            width: 100%;
            height: 30px;
            display: flex;
            justify-content: flex-end;
        }
        #dangbai {
            width: 70px;
            height: 30px;
            border: 1px solid #4c1ae4;
            background-color: #4c1ae4;
            border-radius: 8px;

        }

        #dang button {
            background-color: transparent;
            border: none;
            color: white;
            display: flex;
            text-align: center;
            line-height: 30px;
            cursor: pointer;
        }

        #content {
            display: block;
            width: 100%;
            height: 200px;
            margin-bottom: 40px;
        }

        .alert
        {
            margin: 0;
        }

    </style>
</head>

<body>
    <div class="web">
    <jsp:include page="/views/common/header.jsp" />
    <div id="editor">
        <p id="time-remain" class="${status}">${message}</p>
        <form action="" enctype="multipart/form-data" method="post">
            <div style="display: flex; justify-content: space-between;">
                <c:if test="${not empty blog.blogId}">
                    <input type="hidden" name="_method" value="PUT">
                </c:if>
                <c:if test="${empty blog.blogId}">
                    <input type="hidden" name="_method" value="POST">
                </c:if>
                <ul>
                    <input type="hidden" id="blogId" name="blogId" value="${blog.blogId}">
                    <p class="alert">${bug_1}</p>
                    <label class="title">Tiêu đề</label>
                    <input type="text" id="blogTitle" name="title" value="${blog.title}"> <br>
                </ul>

                <ul>
                    <p class="alert">${bug_2}</p>
                    <label class="title">Chọn ảnh cho tiêu đề</label>
                    <img id="image" src="${blog.imageTitle}">
                    <input type="file" id="imageTitleFile" name="imageTitleData" onchange="chooseFile(this)"> <br>
                    <script>
                        //Thực hiện hiển thị ảnh khi người dùng đăng 1 ảnh lên
                        function chooseFile(fileInput) {
                            if (fileInput.files && fileInput.files[0]) {
                                var reader = new FileReader();
                                reader.onload = function (e) {
                                    var image = document.getElementById('image');
                                    image.setAttribute('src', e.target.result);
                                };
                                reader.readAsDataURL(fileInput.files[0]);
                            }
                        }
                    </script>
                </ul>
            </div>

            <ul>
                <p class="alert">${bug_3}</p>
                <label class="title">Chọn thể loại:</label> <br>
                <c:forEach var="category" items="${categories}" varStatus="loop">
                    <c:set var="isSelected" value=""/>
                    <c:if test="${blog.checkBlogHasCategory(category)}">
                        <c:set var="isSelected" value="checked"/>
                    </c:if>
                    <label>${category.name}</label>
                    <input type="checkbox" name="category" id="category-${loop.index}" value="${category.categoryId}" ${isSelected}>
                    <br>
                </c:forEach>
            </ul>

            <p class="alert">${bug_4}</p>
            <label class="title">Nội dung truyện</label>
            <textarea name="content" id="content">${blog.content}</textarea>

            <div id="dang">
                <div id="dangbai">
                    <button>
                        <c:if test="${blog.blogId == null}">Đăng bài</c:if>
                        <c:if test="${blog.blogId != null}">Cập nhật</c:if>
                    </button>
                </div>
            </div>

        </form>
    </div>

    <jsp:include page="/views/common/footer.jsp"/>
</body>
</html>