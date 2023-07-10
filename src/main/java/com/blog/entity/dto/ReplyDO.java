package com.blog.entity.dto;

import lombok.Data;

@Data
public class ReplyDO {

  private Long commentId;
  private String content;
  private String createUser;
  private String userAvatar;
}
