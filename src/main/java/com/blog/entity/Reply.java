package com.blog.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply implements Serializable {

  private List<SingleReply> replies;


  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class SingleReply {

    private String content;
    private Date createTime;
    private String createUser;
    private String userAvatar;
  }


}
