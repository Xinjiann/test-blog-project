package com.blog.common.lang;

import com.baomidou.mybatisplus.extension.api.R;
import java.io.Serializable;
import lombok.Data;

@Data
public class Result implements Serializable {

  private int code;
  private String msg;
  private Object data;

  public static Result success(Object data) {
    return success(200, "success", data);
  }


  public static Result success(int code, String msg, Object data) {
    Result r = new Result();
    r.setCode(code);
    r.setData(data);
    r.setMsg(msg);
    return r;
  }

  public static Result fail(String msg) {
    return fail(400, msg, null);
  }

  public static Result fail(String msg, Object data) {
    return fail(400, msg, data);
  }

  public static Result fail(int code, String msg, Object data) {
    Result r = new Result();
    r.setCode(code);
    r.setMsg(msg);
    r.setData(data);
    return r;
  }


}
