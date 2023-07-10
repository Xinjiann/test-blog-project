package com.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_user")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  public User() {
    this.avatar = "";
    this.status = 0;
    this.created = new Date();
    this.lastLogin = null;
  }

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @NotNull(message = "Idname can not empty")
  private String username;

  private String avatar;

  @NotNull(message = "Email address can not empty")
  @Email(message = "Invalid email address")
  private String email;

  private String password;

  private Integer status;

  private Date created;

  private Date lastLogin;

  private Integer isAdmin;


}
