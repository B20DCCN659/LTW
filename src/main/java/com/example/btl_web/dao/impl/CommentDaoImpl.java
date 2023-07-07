package com.example.btl_web.dao.impl;

import com.example.btl_web.dao.CommentDao;
import com.example.btl_web.mapper.impl.CommentMapperImpl;
import com.example.btl_web.model.Comment;

import java.util.List;

public class CommentDaoImpl extends GeneralDaoImpl<Comment> implements CommentDao {
    private static CommentDaoImpl commentDao;
    public static CommentDaoImpl getInstance()
    {
        if(commentDao == null)
            commentDao = new CommentDaoImpl();
        return commentDao;
    }

    @Override
    public List<Comment> findAll(String sql, Object... parameters) {
        return selectSql(sql, new CommentMapperImpl(), parameters);
    }

    @Override
    public Long update(String sql, Object... parameters) {
        return updateSql(sql, parameters);
    }

    @Override
    public long count(String sql, Object... parameters) {
        return countSql(sql, parameters);
    }
}