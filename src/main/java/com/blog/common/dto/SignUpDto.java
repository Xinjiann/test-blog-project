package com.blog.common.dto;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpDto implements Serializable {
  @NotNull(message = "username empty")
  private String username;

  @NotNull(message = "password empty")
  private String password;

  @NotNull(message = "email empty")
  @Email(message = "email invalid")
  private String email;
}
