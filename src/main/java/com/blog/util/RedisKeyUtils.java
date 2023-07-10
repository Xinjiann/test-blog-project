package com.blog.util;

public class RedisKeyUtils {

  /**
   *
   *
   * @date 2022/1/04 14:44
   */
  public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
  /**
   *
   *
   * @date 2022/1/04 14:44
   */
  public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

  public static final String MAP_KEY_USER_FOLLOWED = "MAP_KEY_USER_FOLLOWED";

  public static final String MAP_KEY_USER_FOLLOWER = "MAP_KEY_USER_FOLLOWER";

  public static final String MAP_KEY_USER_FOLLOWED_COUNT = "MAP_KEY_USER_FOLLOWED_COUNT";

  public static final String MAP_KEY_USER_FOLLOWER_COUNT = "MAP_KEY_USER_FOLLOWER_COUNT";

  public static final String FAVORITE_BLOGS = "FAVORITE_BLOGS";

  /**
   *
   *
   * @param likedUserId
   * @param likedPostId
   * @return
   */
  public static String getLikedKey(String likedUserId, String likedPostId) {
    return likedUserId +
        "::" +
        likedPostId;
  }
}

