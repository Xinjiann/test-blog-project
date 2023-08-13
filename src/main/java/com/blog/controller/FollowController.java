package com.blog.controller;

import com.blog.common.lang.Result;
import com.blog.service.FollowService;
import com.blog.util.RedisKeyUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FollowController {

  @Autowired
  RedisTemplate redisTemplate;

  @Autowired
  FollowService followService;

  @RequestMapping("/getTargetFollowerCount")
  public Object likeCount(@RequestParam("targetId") String targetId) {
    Gson gson = new Gson();
    if (redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId)) {
      String json = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId).toString();
      List<String> follower = gson.fromJson(json, List.class);

      return Result.success(follower);
    } else {
      return Result.success(new ArrayList<>());
    }
  }

  @RequestMapping("/getTargetFollowedCount")
  public Object followerCount(@RequestParam("targetId") String targetId) {
    Gson gson = new Gson();
    if (redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, targetId)) {
      String json = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, targetId).toString();
      List<String> followed = gson.fromJson(json, List.class);

      return Result.success(followed);
    } else {
      return Result.success(new ArrayList<>());
    }
  }

  @RequestMapping("/follow")
  public Object likeStatus(@RequestParam("targetId") String targetId, @RequestParam("followerId") String followerId) {

    Gson gson = new Gson();
    if(redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId)) {

      String json1 = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId)
          .toString();
      List<String> follower = gson.fromJson(json1, List.class);
      if (follower.contains(followerId)) {
        return Result.fail("重复关注");
      }
      follower.add(followerId);
      redisTemplate.opsForHash()
          .put(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId, gson.toJson(follower));
    } else {
      List<String> follower = new ArrayList<>();
      follower.add(followerId);
      redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId, gson.toJson(follower));
    }

    if (redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, followerId)) {
      String json2 = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, followerId).toString();
      List<String> followed = gson.fromJson(json2, List.class);
      followed.add(targetId);
      redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, followerId, gson.toJson(followed));
    } else {
      List<String> followed = new ArrayList<>();
      followed.add(targetId);
      redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, followerId, gson.toJson(followed));
    }
    return Result.success(null);

  }

  @RequestMapping("/unFollow")
  public Object unFollow(@RequestParam("targetId") String targetId, @RequestParam("followerId") String followerId) {

    Gson gson = new Gson();
    if (redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId)) {
      String json1 = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId)
          .toString();
      String json2 = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, followerId)
          .toString();
      List<String> follower = gson.fromJson(json1, List.class);
      List<String> followed = gson.fromJson(json2, List.class);
      if (!follower.contains(followerId)) {
        return Result.fail("未关注");
      }
      follower.remove(followerId);
      followed.remove(targetId);

      redisTemplate.opsForHash()
          .put(RedisKeyUtils.MAP_KEY_USER_FOLLOWER, targetId, gson.toJson(follower));

      redisTemplate.opsForHash()
          .put(RedisKeyUtils.MAP_KEY_USER_FOLLOWED, followerId, gson.toJson(followed));

      return Result.success(null);
    }
    return Result.fail("错误");
  }

}

