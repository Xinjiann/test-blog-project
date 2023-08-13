package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.common.lang.Result;
import com.blog.entity.UserLikes;
import com.blog.service.LikeService;
import com.blog.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LikeController {

  @Autowired
  RedisTemplate redisTemplate;

  @Autowired
  LikeService likeService;

  @RequestMapping("like/count")
  public Object likeCount(@RequestParam("commentId") String targetId) {
    if (redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, targetId)) {
      String res = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, targetId)
          .toString();
      return Result.success(Integer.parseInt(res));
    } else {
      return Result.success(0);
    }
  }

  @RequestMapping("/like")
  public Object likeStatus(@RequestParam("targetId") String targetId, @RequestParam("likeUserId") String likeUserId) {
    if (redisTemplate.opsForHash().hasKey(
        RedisKeyUtils.MAP_KEY_USER_LIKED, RedisKeyUtils.getLikedKey(targetId, likeUserId))) {
      String o = redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED, RedisKeyUtils.getLikedKey(targetId, likeUserId)).toString();
      if ("1".equals(o)) {
        unLikes(targetId, likeUserId);
        return Result.success("unlike");
      }
      if ("0".equals(o)) {
        likes(targetId, likeUserId);
        return Result.success("like");
      }
    }
    UserLikes userLikes = likeService.getOne(new QueryWrapper<UserLikes>().eq("target_id", targetId).eq("like_user_id", likeUserId));
    if (userLikes == null) {
      UserLikes userLikes1 = new UserLikes();
      userLikes1.setTargetId(targetId);
      userLikes1.setLikeUserId(likeUserId);
      userLikes1.setCreateTime(new Date());
      userLikes1.setUpdateTime(new Date());
      userLikes1.setStatus(1);
      likeService.saveOrUpdate(userLikes1);
      likes(targetId, likeUserId);
      return Result.success("like");
    }
    if (userLikes.getStatus() == 1) {
      unLikes(targetId, likeUserId);
      return Result.success("unlike");
    }

    if (userLikes.getStatus() == 0) {
      likes(targetId, likeUserId);
      return Result.success("like");
    }
    return Result.fail("");
  }

  public void likes(String targetId, String likeUserId) {
    String likedKey = RedisKeyUtils.getLikedKey(targetId, likeUserId);
    redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, targetId, 1);
    redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey, "1");
  }

  public void unLikes(String targetId, String likeUserId) {
    String likedKey = RedisKeyUtils.getLikedKey(targetId, likeUserId);
    redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, targetId, -1);
//    redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey);
    redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, likedKey, "0");
  }

}
