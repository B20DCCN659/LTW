<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Blog Truyện - Đăng nhập</title>
    <link rel="stylesheet" href="/assets/css/registraion/registraion5.css">
</head>
<body>
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
            <div class="gtri-hopmau">
                <form action="" method="post">
                    <h2>Đăng nhập</h2>
                    <div class="message ${message_type} ${display_flex}">
                        <div class="message-title">
                            ${message}
                        </div>
                    </div>
                    <div class="o-nhaptt">
                        <input name="userName" type="text" required>
                        <label>Tên đăng nhập</label>
                    </div>
                    <div class="o-nhaptt">
                        <input name="passWord" type="password" required>
                        <label>Mật khẩu</label>
                    </div>
                    <button class="nut">Đăng nhập</button>
                    <div class="dki">
                        <p>Bạn chưa có tài khoản ? <a href="registration">Đăng kí ngay</a></p>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>
</body>
</html>
