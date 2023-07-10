package com.blog.entity.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserLikCountDTO implements Serializable {

  private String infoId;
  private Integer value;

  public UserLikCountDTO(String infoId, Integer value) {
    this.infoId = infoId;
    this.value = value;
  }
}
