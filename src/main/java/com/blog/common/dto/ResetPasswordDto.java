package com.blog.common.dto;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDto implements Serializable  {
  @NotNull(message = "username empty")
  private String username;

  @NotNull(message = "password empty")
  private String password;

  @NotNull(message = "newPassword empty")
  private String newPassword;

  private String newPassword2;

}
