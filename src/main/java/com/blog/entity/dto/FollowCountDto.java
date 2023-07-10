package com.blog.entity.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class FollowCountDto  implements Serializable {
  private String infoId;
  private Integer value;

  public FollowCountDto(String infoId, Integer value) {
    this.infoId = infoId;
    this.value = value;
  }
}
