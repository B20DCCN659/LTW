package com.example.btl_web.service.impl;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dao.CategoryDao;
import com.example.btl_web.dao.impl.CategoryDaoImpl;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.model.Category;
import com.example.btl_web.paging.Pageable;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = CategoryDaoImpl.getInstance();
    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    public List<CategoryDto> findAll(Pageable pageable, CategoryDto dto) {
        StringBuilder sql = new StringBuilder("SELECT * FROM CATEGORIES WHERE (1 = 1)");

        sql.append(addAndClause(pageable, dto));

        List<Category> categories = categoryDao.select(sql.toString());
        List<CategoryDto> dtos = new ArrayList<>();
        for(Category category: categories)
        {
            dtos.add(ConvertUtils.convertEntityToDto(category, CategoryDto.class));
        }

        return dtos;
    }

    @Override
    public List<CategoryDto> CategoryMaxBlogs(Pageable pageable, CategoryDto dto) {
        StringBuilder sql = new StringBuilder("SELECT c.*, blogs.num_blogs\n" +
                "FROM categories c\n" +
                "LEFT JOIN (\n" +
                "    SELECT category_id, COUNT(blog_id) AS num_blogs\n" +
                "    FROM blogs_categories\n" +
                "    GROUP BY category_id\n" +
                ") blogs ON c.category_id = blogs.category_id\n" +
                "WHERE blogs.num_blogs >= 1\n" +
                "ORDER BY blogs.num_blogs DESC\n" +
                "LIMIT 5;");

        sql.append(addAndClause(pageable, dto));

        List<Category> categories = categoryDao.select(sql.toString());
        List<CategoryDto> dtos = new ArrayList<>();
        for(Category category: categories)
        {
            dtos.add(ConvertUtils.convertEntityToDto(category, CategoryDto.class));
        }

        return dtos;
    }

    @Override
    public List<CategoryDto> findAllCategoryOfBlog(Long blogCategoryId, Integer status) {
        StringBuilder sql = new StringBuilder("Select c.category_id, c.name from categories c, blogs_categories b_c, blogs b where " +
                                              "b.blog_id = ? and " +
                                              "c.status = 1 and " +
                                              "b_c.category_id = c.category_id and " +
                                              "b_c.blog_id = b.blog_id");
        List<Category> categories = categoryDao.selectDisplay(sql.toString(), blogCategoryId);
        if(categories == null)
            return null;
        List<CategoryDto> result = new ArrayList<>();
        categories.forEach( category -> {result.add(ConvertUtils.convertEntityToDto(category, CategoryDto.class));});

        return result;
    }

    @Override
    public CategoryDto findOneBy(CategoryDto dto) {
        List<CategoryDto> dtos = findAll(null, dto);

        return dtos.isEmpty() ? null: dtos.get(0);
    }

    @Override
    public long countCategories(CategoryDto category) {
        StringBuilder sql = new StringBuilder("Select count(category_id) from categories where (1 = 1)");
        sql.append(addAndClause(null, category));
        return categoryDao.count(sql.toString());
    }

    @Override
    public Long save(CategoryDto categoryDto) {
        if(!checkValidCategory(categoryDto))
            return null;

        Date timeStamp = new Date();
        StringBuilder sql = new StringBuilder("INSERT INTO CATEGORIES (name, user_id, created_at, status) values (?, ?, ?, 1)");

        return categoryDao.update(sql.toString(),categoryDto.getName(), categoryDto.getUserId(), timeStamp.getTime());
    }

    @Override
    public Long update(CategoryDto categoryDto) {
        StringBuilder sql = new StringBuilder("UPDATE CATEGORIES SET category_id = ?");
        sql.append(addClauseUpdate(categoryDto));

        return categoryDao.update(sql.toString(), categoryDto.getCategoryId());
    }

    @Override
    public Long saveCategoriesOfBlog(Long blogId, List<CategoryDto> categories) {
        String sql = "INSERT INTO blogs_categories (blog_id, category_id) values (?, ?)";

        for(CategoryDto category: categories)
        {
            Long status = categoryDao.update(sql, blogId ,category.getCategoryId());
            if(status == null)
                return null;
        }
        return 1L;
    }

    @Override
    public boolean delete(Long categoryId, Long userId)
    {
        //Xóa các liên kết thể loại truyện này
        deleteCategoryOfBlog(null, categoryId);

        StringBuilder sql = new StringBuilder("DELETE FROM CATEGORIES WHERE (1=1)");
        if(categoryId != null)
            sql.append(" and category_id = " + categoryId);
        if(userId != null)
            sql.append(" and user_id = " + userId);

        return categoryDao.update(sql.toString()) != null;
    }

    @Override
    public boolean deleteCategoryOfBlog(Long blogId, Long categoryId)
    {
        if(blogId == null && categoryId == null)
            return true;

        StringBuilder sql = new StringBuilder("Delete from blogs_categories where (1=1)");
        if(blogId != null)
            sql.append(" and blog_id = " + blogId);
        if(categoryId != null)
            sql.append(" and category_id = " + categoryId);
        return categoryDao.update(sql.toString()) != null;
    }

    @Override
    public boolean validCategoryCreate(CategoryDto category, String errors[],  Long userId)
    {
        String userValid = userService.checkLastAction(userId);
        if(userValid != null)
        {
            errors[0] = userValid;
            return false;
        }
        if(category.getName().isEmpty())
        {
            errors[0] = "Tên thể loại không được để trống!";
            return false;
        }
        return true;
    }

    @Override
    public boolean validCategoryUpdate(CategoryDto category, String[] error, Long userId)
    {
        String userValid = userService.checkLastAction(userId);
        if(userValid != null)
        {
            error[0] = userValid;
            return false;
        }

        //Kiểm tra xem thể loại này có tồn tại không
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getCategoryId());
        List<CategoryDto> categories = findAll(null, dto);
        if(categories == null || categories.isEmpty())
            return false;
        return true;
    }

    private StringBuilder addAndClause(Pageable pageable,CategoryDto categoryDto)
    {
        StringBuilder sb = new StringBuilder();
        if(categoryDto != null)
        {
            if(categoryDto.getCategoryId() != null)
                sb.append(" AND category_id = " + categoryDto.getCategoryId());
            if(categoryDto.getName() != null)
                sb.append(" AND lower(name) like lower('%" + categoryDto.getName() + "%')");
            if(categoryDto.getUserId() != null)
                sb.append(" AND user_id = " + categoryDto.getUserId());
        }

        if(pageable != null)
            sb.append(pageable.addPagingation());

        return sb;
    }

    private StringBuilder addClauseUpdate(CategoryDto dto)
    {
        StringBuilder sb = new StringBuilder();
        Long id = dto.getCategoryId();
        String name = dto.getName();
        Long userId = dto.getUserId();

        String createAt = dto.getCreatedAt();

        Integer status = dto.getStatus();

        if(name != null)
            sb.append(", name = N'" + name + "'");
        if(userId != null)
            sb.append(", user_id = " + userId);
        if(status != null)
            sb.append(", status = " + status);

        sb.append(" WHERE category_id = " + id);

        return sb;
    }

    private boolean checkValidCategory(CategoryDto categoryDto)
    {
        String name = categoryDto.getName();

        if(name == null || name.equals(""))
            return false;
        return true;
    }
}
