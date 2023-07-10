package com.blog.service;

import com.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {


  public void updateAvatar(Long id, String filePath);
}
