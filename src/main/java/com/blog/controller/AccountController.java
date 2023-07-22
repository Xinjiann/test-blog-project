package com.blog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.common.dto.LoginDto;
import com.blog.common.dto.ResetPasswordDto;
import com.blog.common.dto.SignUpDto;
import com.blog.common.lang.Result;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

  @Autowired
  UserService userService;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/login")
  public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
    User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
    Assert.notNull(user, "用户不存在");
    if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
      return Result.fail("密码不正确");
    }
    String jwt = jwtUtils.generateToken(user.getId());
    response.setHeader("Authorization", jwt);
    response.setHeader("Access-control-Expose-Headers", "Authorization");
    return Result.success(MapUtil.builder()
        .put("id", user.getId())
        .put("username", user.getUsername())
        .put("avatar", user.getAvatar())
        .put("isAdmin", user.getIsAdmin())
        .map()
    );
  }

  @PostMapping("/signUp")
  public Result signUp(@Validated @RequestBody SignUpDto signUpDto) {
    if (signUpDto.getUsername().equals("") || signUpDto.getUsername() == null) {
      return Result.fail("用户名不能为空");
    }
    if (signUpDto.getPassword().equals("") || signUpDto.getPassword() == null) {
      return Result.fail("密码不能为空");
    }
    if (signUpDto.getEmail().equals("") || signUpDto.getEmail() == null) {
      return Result.fail("邮箱不能为空");
    }
    User user = userService.getOne(
        new QueryWrapper<User>().eq("username", signUpDto.getUsername()));
    if (user != null) {
      return Result.fail("用户已存在");
    }
    //存用户
    User userToSave = new User();
    userToSave.setUsername(signUpDto.getUsername());
    userToSave.setPassword(SecureUtil.md5(signUpDto.getPassword()));
    userToSave.setEmail(signUpDto.getEmail());
    userService.save(userToSave);
    return Result.success("注册成功");
  }

  @PostMapping("/resetPassword")
  public Result resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
    User user = userService.getOne(new QueryWrapper<User>().eq("username", resetPasswordDto.getUsername()));
    Assert.notNull(user, "用户名不存在");
    if (!user.getPassword().equals(SecureUtil.md5(resetPasswordDto.getPassword()))) {
      return Result.fail("原密码不正确");
    }
    if (user.getPassword().equals(SecureUtil.md5(resetPasswordDto.getNewPassword()))) {
      return Result.fail("原密码与新密码相同");
    }
    user.setPassword(SecureUtil.md5(resetPasswordDto.getNewPassword()));
    userService.saveOrUpdate(user);
    return Result.success("修改成功");
  }

  @RequiresAuthentication
  @GetMapping("logout")
  public Result logOut() {
    SecurityUtils.getSubject().logout();
    return Result.success(null);
  }

}
