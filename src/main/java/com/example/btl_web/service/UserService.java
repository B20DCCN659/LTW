package com.example.btl_web.service;

import com.example.btl_web.dto.UserDto;
import com.example.btl_web.paging.Pageable;

import java.util.List;

public interface UserService{
    List<UserDto> findAll(Pageable pageable, UserDto dto);
    List<UserDto> countBlog();

    List<UserDto> findAllInclude(Pageable pageable, UserDto dto);
    UserDto findOneById(Long userId);
    long countUsers(UserDto countDto);
    UserDto login(String userName, String passWord);
    Long saveUser(UserDto userDto);
    Long updateUser(UserDto dto);
    Long deleteUser(UserDto user);
    boolean changePassword(UserDto user, String[] message);
    boolean validateSignUp(UserDto user, String[] errors);
    boolean validUpdate(UserDto user, String[] errors);
    boolean updateLastAction(UserDto user);
    String checkLastAction(Long userId);
}