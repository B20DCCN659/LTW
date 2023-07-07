package com.example.btl_web.service.impl;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dao.BlogDao;
import com.example.btl_web.dao.UserDao;
import com.example.btl_web.dao.impl.BlogDaoImpl;
import com.example.btl_web.dao.impl.UserDaoImpl;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.dto.CommentDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.model.Blog;
import com.example.btl_web.model.User;
import com.example.btl_web.paging.PageRequest;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.service.UserBlogService;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.ConvertUtils;
import com.example.btl_web.utils.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class BlogServiceImpl implements BlogService {
    private BlogDao blogDao = BlogDaoImpl.getInstance();
    private UserDao userDao = UserDaoImpl.getInstance();
    private UserBlogService userBlogService = ServiceConfiguration.getUserBlogService();
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();
    private UserService userService = ServiceConfiguration.getUserService();

    @Override
    public List<BlogDto> getAllBlogs(Pageable pageable, BlogDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * FROM BLOGS b");

        sql.append(addAndClause(pageable, dto));

        List<Blog> blogs = blogDao.findAll(sql.toString());
        List<BlogDto> dtos = new ArrayList<>();

        for(Blog blog: blogs)
        {
            dtos.add(ConvertUtils.convertEntityToDto(blog, BlogDto.class));
        }

        return dtos;
    }

    @Override
    public List<BlogDto> BlogsMaxLike(Pageable pageable, BlogDto dto) {
        StringBuilder sql = new StringBuilder("SELECT b.*, likes.num_likes\n" +
                "FROM blogs b\n" +
                "LEFT JOIN (\n" +
                "    SELECT blog_id, COUNT(user_id) AS num_likes\n" +
                "    FROM liked\n" +
                "    GROUP BY blog_id\n" +
                ") likes ON b.blog_id = likes.blog_id\n" +
                "WHERE likes.num_likes >= 1\n" +
                "ORDER BY likes.num_likes DESC\n" +
                "LIMIT 5;\n");

        List<Blog> blogs = blogDao.findAll(sql.toString());
        List<BlogDto> dtos = new ArrayList<>();

        for(Blog blog: blogs)
        {
            dtos.add(ConvertUtils.convertEntityToDto(blog, BlogDto.class));
        }

        return dtos;
    }

    @Override
    public List<BlogDto> BlogsMaxComment(Pageable pageable, BlogDto dto) {
        StringBuilder sql = new StringBuilder("SELECT b.*, comments.num_comments\n" +
                "FROM blogs b\n" +
                "LEFT JOIN (\n" +
                "    SELECT blog_id, COUNT(comment_id) AS num_comments\n" +
                "    FROM comments\n" +
                "    GROUP BY blog_id\n" +
                ") comments ON b.blog_id = comments.blog_id\n" +
                "WHERE comments.num_comments >= 1\n" +
                "ORDER BY comments.num_comments DESC LIMIT 5;\n");

        List<Blog> blogs = blogDao.findAll(sql.toString());
        List<BlogDto> dtos = new ArrayList<>();

        for(Blog blog: blogs)
        {
            dtos.add(ConvertUtils.convertEntityToDto(blog, BlogDto.class));
        }

        return dtos;
    }

    @Override
    public List<BlogDto> BlogsNew(Pageable pageable, BlogDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * \n" +
                "FROM blogs\n" +
                "WHERE STATUS = 1\n" +
                "ORDER BY created_at DESC\n" +
                "LIMIT 5;\n");

        List<Blog> blogs = blogDao.findAll(sql.toString());
        List<BlogDto> dtos = new ArrayList<>();

        for(Blog blog: blogs)
        {
            dtos.add(ConvertUtils.convertEntityToDto(blog, BlogDto.class));
        }

        return dtos;
    }

    @Override
    public BlogDto getOne(BlogDto searchBlog) {
        BlogDto result = new BlogDto();

        List<BlogDto> blogDtos = getAllBlogs(null, searchBlog);
        if(blogDtos.isEmpty())
            return null;
        result = blogDtos.get(0);
        result.setCategoriesList(categoryService.findAllCategoryOfBlog(result.getBlogId(), 1));
        result.setLikedUsers(peopleLikedBlog(result.getBlogId()));

        CommentDto commentDto = new CommentDto();
        commentDto.setBlogComment(result.getBlogId());
        Pageable pageable = new PageRequest(new HashMap<>(), 10L);
        List<CommentDto> commentsOfBlog = userBlogService.findAll(pageable, commentDto);
        result.setComments(commentsOfBlog);

        return result;
    }

    @Override
    public long countBlogs(BlogDto blogDto) {
        StringBuilder sql = new StringBuilder("SELECT count(b.blog_id) FROM BLOGS b");
        sql.append(addAndClause(null, blogDto));
        //SELECT count(b.blog_id) FROM BLOGS b where (1=1) and blogs.title like '%12345%'
        return blogDao.countBlogs(sql.toString());
    }

    @Override
    public Long save(BlogDto blog) {
        //Thêm các thẻ <p> và </p> vào mỗi dòng của truyện
        addPTagContent(blog);

        Date timeStamp = new Date();
        String sql = "INSERT INTO BLOGS (content, created_at, title, user_id, status) values (?, ?, ?, ?, 2)";

        Long saveBlog = blogDao.save(sql, blog.getContent(), timeStamp.getTime(), blog.getTitle(), blog.getUser().getUserId());
        if(saveBlog == null)
            return null;
        //Lưu các thể loại của truyện này
        Long saveCategories = categoryService.saveCategoriesOfBlog(saveBlog, blog.getCategories());
        if(saveCategories == null)
            return null;

        //Cập nhật link ảnh tiêu đề
        BlogDto saveImageUrl = new BlogDto();
        saveImageUrl.setBlogId(saveBlog);
        saveImageUrl.setImageTitleData(blog.getImageTitleData());
        update(saveImageUrl);

        //Lưu hoạt động gần đây nhất của User
        userService.updateLastAction(blog.getUser());
        return saveBlog;
    }

    @Override
    public Long update(BlogDto blog) {
        if(blog.getContent() != null)
            addPTagContent(blog);

        StringBuilder sql = new StringBuilder("UPDATE BLOGS SET blog_id = " + blog.getBlogId());
        sql.append(addUpdateClause(blog));

        Long updateStatus = blogDao.save(sql.toString());

        if(blog.getImageTitleData() != null)
        {
            BlogDto editImage = new BlogDto();
            editImage.setBlogId(blog.getBlogId());
            //Thay đổi ảnh
            editImage.setImageTitle( FileUtils.saveImageToServer(blog.getImageTitleData(), blog.getBlogId()));
            if(editImage.getImageTitle() != null)
                updateStatus = update(editImage);
        }

        return updateStatus;
    }

    @Override
    public Long delete(Long blodId) {
        //Xóa ảnh tiêu đề
        FileUtils.deleteImage(blodId);

        //Xóa các liên kết thể loại
        categoryService.deleteCategoryOfBlog(blodId, null);

        //Xóa các comment bài viết này
        if(!userBlogService.deleteComment(null, null, blodId))
            return null;

        //Xóa các like của bài viết này
        if(!userBlogService.removeLikeThisBlog(blodId, null))
            return null;

        //Xóa bài viết
        String sql = "Delete from blogs where blog_id = " + blodId;
        return blogDao.save(sql);
    }

    @Override
    public boolean validCreateBlog(HttpServletRequest req, BlogDto blog) {
        String timeValid = userService.checkLastAction(blog.getUser().getUserId());
        if(timeValid != null)
        {
            req.setAttribute("message", timeValid);
            return false;
        }

        boolean result = true;
        if(blog.getTitle() == null || blog.getTitle().isEmpty())
        {
            result = false;
            req.setAttribute("bug_1", "Tiêu đề không được để trống");
        }
        if(blog.getImageTitleData() == null)
        {
            result = false;
            req.setAttribute("bug_2" ,"Ảnh tiêu đề không được để trống");
        }
        if(blog.getCategories() == null || blog.getCategories().isEmpty())
        {
            result = false;
            req.setAttribute("bug_3" ,"Phải chọn ít nhất 1 thể loại");
        }

        if(blog.getContent() == null || blog.getContent().isEmpty())
        {
            result = false;
            req.setAttribute("bug_4" ,"Nội dung truyện không được để trống");
        }

        return result;
    }

    @Override
    public boolean validUpdateBlog(HttpServletRequest req, BlogDto blog, Long userId) {
        String validTime = userService.checkLastAction(userId);
        if(validTime != null)
        {
            req.setAttribute("message", validTime);
            return false;
        }

        BlogDto dto = new BlogDto();
        dto.setBlogId(blog.getBlogId());
        List<BlogDto> checkBlogExisted = getAllBlogs(null, dto);
        if(checkBlogExisted == null || checkBlogExisted.isEmpty())
        {
            req.setAttribute("message", "Bài viết chỉnh sửa không tồn tại");
            return false;
        }

        return true;
    }

    @Override
    public List<UserDto> peopleLikedBlog(Long blogId) {
        String sql = "Select u.user_id, u.username, u.full_name from Users u, liked l where l.user_id = u.user_id and l.blog_id = " + blogId;

        List<User> users = userDao.findAllUserInclude(sql);
        List<UserDto> result = new ArrayList<>();
        if(!users.isEmpty())
        {
            for(User user: users)
            {
                result.add(ConvertUtils.convertEntityToDto(user, UserDto.class));
            }
        }
        return result;
    }

    @Override
    public boolean checkUserLikedBlog(BlogDto blog, Long userId) {
        return false;
    }

    @Override
    public void addPTagContent(BlogDto blog) {
        String oldContent = blog.getContent();
        StringBuilder newContent = new StringBuilder("");
        String lines[] = oldContent.split("\\n");
        for(String line: lines)
        {
            newContent.append("<p>").append(line).append("</p>\n");
        }
        blog.setContent(newContent.toString());
    }

    @Override
    public void removePTagContent(BlogDto blog) {
        String oldContent = blog.getContent();
        oldContent = oldContent.replaceAll("<p>|</p>", "");
        blog.setContent(oldContent);
    }

    private StringBuilder addAndClause(Pageable pageable ,BlogDto dto)
    {
        StringBuilder sb = new StringBuilder(" WHERE (1 = 1)");
        StringBuilder fromClause = new StringBuilder();

        if(dto != null)
        {
            List<CategoryDto> categories = dto.getCategories();
            if(categories != null)
            {
                fromClause.append(", blogs_categories b_c");
                sb.append(" AND b_c.blog_id = b.blog_id");
                for(CategoryDto category: categories)
                {
                    sb.append(" AND b_c.category_id = " + category.getCategoryId());
                }
            }
            if(dto.getUser() != null)
            {
                sb.append(" AND user_id = " + dto.getUser().getUserId());
            }

            Long blogId = dto.getBlogId();
            String title = dto.getTitle();
            String content = dto.getContent();
            String imageTitle = dto.getImageTitle();
            String createAt = dto.getCreatedAt();
            Integer status = dto.getStatus();

            if (blogId != null)
                sb.append(" AND blog_id = " + blogId);
            if (title != null)
                sb.append(" AND title like '%" + title + "%'");
            if (content != null)
                sb.append(" AND content like '%" + content + "%'");
            if (imageTitle != null)
                sb.append(" AND image_title = " + imageTitle);
            if (createAt != null)
                sb.append(" AND created_at = " + createAt);
            if (status != null)
                sb.append(" AND status = " + status);
        }

        if(pageable != null)
            sb.append(pageable.addPagingation());

        return fromClause.append(sb);
    }
    private StringBuilder addUpdateClause(BlogDto blog)
    {
        Long blogId = blog.getBlogId();
        String title = blog.getTitle();
        String content = blog.getContent();
        String imageTitle = blog.getImageTitle();
        String createAtStr  = blog.getCreatedAt();
        Long createAt = null;
        if(createAtStr != null)
            createAt = ConvertUtils.convertStringDateToLong(createAtStr);
        Integer status = blog.getStatus();

        StringBuilder sb = new StringBuilder();
        if(title != null)
            sb.append(", title = '" + title + "'");
        if(content != null)
            sb.append(", content = '" + content + "'");
        if(imageTitle != null)
            sb.append(", image_title = '" + imageTitle + "'");
        if(createAt != null)
            sb.append(", created_at = " + createAt);
        if(status != null)
            sb.append(", status = " + status );
        sb.append(" WHERE blog_id = " + blogId);
        return sb;
    }
}
