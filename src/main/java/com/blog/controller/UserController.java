package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.common.lang.Result;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.util.RedisKeyUtils;
import com.google.gson.Gson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  RedisTemplate redisTemplate;

  @RequiresAuthentication
  @GetMapping("/index")
  public Object index() {
    User user = userService.getById(1L);

    return Result.success(user);
  }

  @GetMapping("/getUser/{id}")
  public Object getUser(@PathVariable(name = "id") Long id) {
    User user = userService.getById(id);

    return Result.success(user);
  }

  @PostMapping("/getUsers")
  public Object getUsers(@RequestBody List<String> ids){
    List<User> users = userService.list(new QueryWrapper<User>().in("id", ids));
    return Result.success(users);
  }

  @PostMapping("/save")
  public Object save(@Validated @RequestBody User user) {

    return Result.success(user);
  }

  @GetMapping("/getUserByName/{username}")
  public Object getUserByName(@PathVariable(name = "username") String username) {

    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.eq("username", username);
    User user = userService.getOne(wrapper);
    return Result.success(user);
  }

  @GetMapping("/editUsername/{id}")
  public Object editUsername(@PathVariable(name = "id") Long id, @RequestParam(name = "username") String username){

    User user = userService.getOne(
        new QueryWrapper<User>().eq("username", username));
    if (user != null) {
      return Result.fail("用户名已存在");
    } else {
      User userToSave = userService.getById(id);
      userToSave.setUsername(username);
      userService.saveOrUpdate(userToSave);
    }
    return Result.success(null);
  }

  @GetMapping("/getFavorite/{id}")
  public Object getFavorite(@PathVariable(name = "id") Long id) {
    if (redisTemplate.opsForHash().hasKey(RedisKeyUtils.FAVORITE_BLOGS, id)) {
      Gson gson = new Gson();
      String json = redisTemplate.opsForHash().get(RedisKeyUtils.FAVORITE_BLOGS, id).toString();
      List<String> data = gson.fromJson(json, List.class);
      return Result.success(data);
    }
    return Result.success(new ArrayList<String>());
  }

}
