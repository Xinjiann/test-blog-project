package com.blog.entity.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto implements Serializable {
  private String infoId;
  private String followerId;
  private Integer status;
}
