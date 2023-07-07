package com.example.btl_web.service.impl;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dao.UserDao;
import com.example.btl_web.dao.impl.UserDaoImpl;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.model.User;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.*;
import com.example.btl_web.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class UserServiceimpl implements UserService {
    private UserDao userDao = UserDaoImpl.getInstance();
    private HashPasswordService hashPasswordService = ServiceConfiguration.getHashPasswordService();
    @Override
    public List<UserDto> findAll(Pageable pageable, UserDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ( 1 = 1)");
        sql.append(addAndClause(pageable, dto));

        List<User> users = userDao.findAll(sql.toString());
        List<UserDto> dtos = new ArrayList<>();

        for(User user: users)
        {
            dtos.add(ConvertUtils.convertEntityToDto(user, UserDto.class));
        }
        return dtos;
    }

    @Override
    public List<UserDto> countBlog() {
        StringBuilder sql = new StringBuilder("SELECT u.*, blogs.num_blogs\n" +
                "FROM users u\n" +
                "JOIN (\n" +
                "    SELECT user_id, COUNT(blog_id) AS num_blogs\n" +
                "    FROM blogs\n" +
                "    GROUP BY user_id\n" +
                ") blogs ON u.user_id = blogs.user_id\n" +
                "ORDER BY blogs.num_blogs DESC LIMIT 5;");

        List<User> users = userDao.findAll(sql.toString());
        List<UserDto> dtos = new ArrayList<>();

        for(User user: users)
        {
            dtos.add(ConvertUtils.convertEntityToDto(user, UserDto.class));
        }
        return dtos;
    }

    @Override
    public List<UserDto> findAllInclude(Pageable pageable, UserDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ( 1 = 1)");
        sql.append(addAndClause(pageable, dto));

        List<User> users = userDao.findAllUserInclude(sql.toString());
        List<UserDto> dtos = new ArrayList<>();

        for(User user: users)
        {
            dtos.add(ConvertUtils.convertEntityToDto(user, UserDto.class));
        }
        return dtos;
    }

    @Override
    public UserDto findOneById(Long userId) {
        UserDto findUser = new UserDto();
        findUser.setUserId(userId);
        List<UserDto> result = findAll(null, findUser);
        if(result == null || result.isEmpty())
            return null;
        return result.get(0);
    }


    @Override
    public UserDto login(String userName, String passWord) {
        String encryptPassword = hashPasswordService.encryptPassword(passWord); //Mã hoá password này rồi thực hiện tìm kiếm trong database

        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE username = ? and password = ?");

        List<User> users = userDao.getUserByCondition(sql.toString(), userName, encryptPassword);

        return users.isEmpty() ? null : ConvertUtils.convertEntityToDto(users.get(0), UserDto.class);
    }
    @Override
    public Long saveUser(UserDto userDto) {
        String encryptPassword = hashPasswordService.encryptPassword(userDto.getPassWord());
        userDto.setPassWord(encryptPassword); //Mã hoá password
        Date timeStamp = new Date();
        String sql = "INSERT INTO USERS (email, password, created_at, role, username, last_action, status, full_name) VALUES (?, ?, ?, ?, ?, ?, 1, ?)";
        return userDao.saveUser(sql,userDto.getEmail(), userDto.getPassWord(), timeStamp.getTime(), 1 ,userDto.getUserName(), timeStamp.getTime(), userDto.getFullName());
    }

    @Override
    public Long updateUser(UserDto dto) {
        StringBuilder sql = new StringBuilder("UPDATE USERS SET user_id = " + dto.getUserId()) ;
        sql.append(addUpdateClause(dto));
        return userDao.saveUser(sql.toString());
    }

    @Override
    public Long deleteUser(UserDto user) {
        UserBlogService userBlogService = ServiceConfiguration.getUserBlogService();
        BlogService blogService = ServiceConfiguration.getBlogService();
        CategoryService categoryService = ServiceConfiguration.getCategoryService();

        //Xóa like
        if(!userBlogService.removeLikeThisBlog(null, user.getUserId()))
            return null;

        //Xóa comment
        if(!userBlogService.deleteComment(null,user.getUserId(), null))
            return null;

        //Xóa các truyện của người dùng này
        BlogDto findBlogOfUser = new BlogDto();
        findBlogOfUser.setUser(user);
        List<BlogDto> blogsOfUser = blogService.getAllBlogs(null, findBlogOfUser);
        //Xóa từng truyện
        for(BlogDto blog : blogsOfUser)
        {
                blogService.delete(blog.getBlogId());
        }

        //Xóa các thể loại mà người này tùng viết
        if(!categoryService.delete(null, user.getUserId()))
            return null;

        //Xóa người dùng này
        String sql = "Delete from users where user_id = " + user.getUserId();

        return userDao.saveUser(sql);
    }

    @Override
    public boolean changePassword(UserDto user, String[] messages) {
        boolean check = true;

        //Ở đây lưu tạm giá trị nhập lại mật khẩu vào biến email
        if(user.getPassWord() == null || user.getPassWord().isEmpty())
        {
            check = false;
            messages[0] = "Mật khẩu không được để trống";
        }

        if(user.getRe_password() == null || user.getEmail() == null)
        {
            check = false;
            messages[1] = "Không được bỏ trống mật khẩu";
        }
        else if(!user.getRe_password().equals(user.getEmail()))
        {
            check = false;
            messages[1] = "Mật khẩu xác nhận không đúng";
        }

        UserDto findThisUser = login(user.getUserName(), user.getPassWord());
        if(findThisUser == null)
        {
            check = false;
            messages[0] = "Mật khẩu không chính xác";
        }

        if(check)
        {
            String hashNewPassword = hashPasswordService.encryptPassword(user.getRe_password());
            String sql = "update users set password = '" + hashNewPassword + "' where username = '" + user.getUserName() + "'";
            return userDao.saveUser(sql) != null;
        }
        return check;
    }

    @Override
    public boolean validateSignUp(UserDto user, String[] errors) {
        boolean check = true;

        if(user.getFullName() == null || user.getFullName().equals(""))
        {
            errors[0] = "Tên người dùng không được để trống!";
            check = false;
        }

        if(user.getUserName() == null || user.getUserName().equals(""))
        {
            errors[1] = "Tên đăng nhập không được bỏ trống!";
            check = false;
        }
        UserDto checkUserNameExisted = new UserDto();
        checkUserNameExisted.setUserName(user.getUserName());
        if(!findAll(null, checkUserNameExisted).isEmpty())
        {
            errors[1] = "Tên đăng nhập này đã tồn tại!";
            check = false;
        }

        if(user.getPassWord() == null || user.getPassWord().equals(""))
        {
            check = false;
            errors[2] = "Mật khẩu không được để trống!";
        }
        String passWord = user.getPassWord();
        if(user.getPassWord() != null && passWord.length() < 6)
        {
            check = false;
            errors[2] = "Mật khẩu phải có độ dài ít nhất 6 ký tự!";
        }

        if(user.getRe_password() == null || user.getRe_password().equals(""))
        {
            check = false;
            errors[3] = "Mật khẩu nhập lại không được để trống!";
        }
        if(!user.getPassWord().equals(user.getRe_password()))
        {
            check = false;
            errors[3] = "Mật khẩu nhập lại không khớp";
        }

        if(!checkEmailValid(user.getEmail()))
        {
            check = false;
            errors[4] = "Email không đúng định dạng";
        }
        UserDto checkEmailExisted = new UserDto();
        checkEmailExisted.setEmail(user.getEmail());
        if(!findAll(null, checkEmailExisted).isEmpty())
        {
            check = false;
            errors[4] = "Email này đã được đăng ký";
        }

        return check;
    }

    @Override
    public boolean validUpdate(UserDto user, String[] errors) {
        String timeValid = checkLastAction(user.getUserId());
        if(timeValid != null)
        {
            errors[0] = timeValid;
            return false;
        }

        UserDto dto = new UserDto();
        List<UserDto> existedUser = findAll(null, dto);
        if(existedUser == null || existedUser.isEmpty())
        {
            errors[0] = "Người dùng không tồn tại";
            return false;
        }
        return true;
    }

    @Override
    public boolean updateLastAction(UserDto user) {
        Date timeLastACtion = new Date();
        user.setLastAction(timeLastACtion.getTime());
        return updateUser(user) != null;
    }

    @Override
    public String checkLastAction(Long userId) {
        UserDto validUser = findOneById(userId);
        Long timenow = (new Date()).getTime();
        Long lastAction = validUser.getLastAction();
        Long validTime = (timenow - lastAction) / 1000;

        if(validTime < 10)
            return "Bạn thao tác quá nhanh, vui lòng thử lại sau " + (10 - validTime);
        else
            updateLastAction(validUser);
        return null;
    }
    @Override
    public long countUsers(UserDto countDto) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(user_id) FROM USERS where (1 = 1)");
        sql.append(addAndClause(null, countDto));

        return userDao.countItems(sql.toString());
    }
    private StringBuilder addAndClause(Pageable pageable,UserDto userDto)
    {
        StringBuilder sb = new StringBuilder();
        if(userDto != null)
        {
            Long userId = userDto.getUserId();
            Integer status = userDto.getStatus();
            String userName = userDto.getUserName();
            String passWord = userDto.getPassWord();
            String email = userDto.getEmail();
            Integer role = userDto.getRole();
            String address = userDto.getAddress();
            String phone = userDto.getPhone();
            String fullName = userDto.getFullName();
            String registeredAt = userDto.getRegisteredAt();

            if(userId != null)
                sb.append(" AND user_id = " + userId );
            if(status != null)
                sb.append(" AND status = " + status);
            if(userName != null)
                sb.append(" AND username = '" + userName + "'");
            if(passWord != null)
                sb.append(" AND password = '" + passWord + "'");
            if(email != null)
                sb.append(" AND email = '" + email + "'");
            if(role != null)
                sb.append(" AND role = " + role);
            if(address != null)
                sb.append(" AND address = '" + address + "'");
            if(phone != null)
                sb.append(" AND phone = '" + phone +  "'");
            if(fullName != null)
                sb.append(" AND lower(full_name) like lower('%" + fullName + "%')");
            if(registeredAt != null)
                sb.append(" AND registered_at = " + registeredAt);
        }

        if(pageable != null)
            sb.append(pageable.addPagingation());

        return sb;
    }
    private StringBuilder addUpdateClause(UserDto dto)
    {
        StringBuilder sb = new StringBuilder();
        String userName = dto.getUserName();
        String passWord = dto.getPassWord();
        String email = dto.getEmail();
        Integer role = dto.getRole();
        String address = dto.getAddress();
        String phone = dto.getPhone();
        String fullName = dto.getFullName();
        Long timeStamp = null;
        Long lastAcion = dto.getLastAction();

        if(dto.getRegisteredAt() != null)
            timeStamp = ConvertUtils.convertStringDateToLong(dto.getRegisteredAt());
        Integer status = dto.getStatus();

        if(userName != null)
            sb.append(", username = '" + userName + "'");
        if(passWord != null)
            sb.append(", password = '" + passWord + "'");
        if(email != null)
            sb.append(", email = '" + email + "'");
        if(role != null)
            sb.append(", role = '" + role + "'");
        if(address != null)
            sb.append(", address = '" + address + "'");
        if(phone != null)
            sb.append(", phone = '" + phone + "'");
        if(fullName != null)
            sb.append(", full_name = N'" + fullName + "'");
        if(timeStamp != null)
            sb.append(", created_at = " + timeStamp);
        if(status != null)
            sb.append(", status = " + status);
        if(lastAcion != null)
            sb.append(", last_action = " + lastAcion);

        sb.append(" WHERE user_id = " + dto.getUserId());
        return sb;
    }
    private boolean checkEmailValid(String email)
    {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}
