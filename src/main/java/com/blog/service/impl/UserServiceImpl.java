package com.blog.service.impl;

import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Autowired
  UserService userService;

  @Override
  public void updateAvatar(Long id, String filePath) {
    User user = userService.getById(id);
    user.setAvatar(filePath);
    userService.saveOrUpdate(user);
  }
}
