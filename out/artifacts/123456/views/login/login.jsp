<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Blog Truyện - Đăng nhập</title>
    <link rel="stylesheet" href="/assets/css/registraion/registraion5.css">
</head>
<body>
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
            <div class="form-value">
                <form action="" method="post">
                    <h2>Đăng nhập</h2>
                    <div class="message ${message_type} ${display_flex}">
                        <div class="message-title">
                            ${message}
                        </div>
                    </div>
                    <div class="inputbox">
                        <input name="userName" type="text" required>
                        <label>Tên đăng nhập</label>
                    </div>
                    <div class="inputbox">
                        <input name="passWord" type="password" required>
                        <label>Mật khẩu</label>
                    </div>
                    <button class="btn">Đăng nhập</button>
                    <div class="register">
                        <p>Bạn chưa có tài khoản ? <a href="registration">Đăng kí ngay</a></p>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>
</body>
</html>
