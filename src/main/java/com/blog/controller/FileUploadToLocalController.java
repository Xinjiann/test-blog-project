package com.blog.controller;

import com.blog.common.lang.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUploadToLocalController {
  final static String PIC_PATH = "static/img/"; //图片存放的相对于项目的相对位置

  /**
   *上传图片
   */
  @PostMapping("/uploadToLocal")
  public Result uploadPic(MultipartHttpServletRequest multiRequest, HttpServletRequest request){
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //生成日期格式
    String datePrefix = dateFormat.format(new Date()); //生成当前日期作为前缀
    String savePath = "src/main/resources/" + PIC_PATH; // 存储路径

    File folder = new File(savePath+datePrefix); //生成带当前日期的文件路径

    if(!folder.isDirectory()){
      folder.mkdirs();
    }

    String randomName = multiRequest.getFile("image").getOriginalFilename(); //获取图片名
    //生成随机数确保唯一性，并加上图片后缀
    String saveName = UUID.randomUUID().toString() + randomName.substring(randomName.lastIndexOf("."),randomName.length());
    String absolutePath = folder.getAbsolutePath(); //转换成绝对路径

    try {
      File fileToSave = new File(absolutePath + File.separator + saveName);
      multiRequest.getFile("image").transferTo(fileToSave); //图片存储到服务端
      String returnPath = request.getScheme() + "://"
          + request.getServerName()+":"+request.getServerPort()
          + "/img/" + datePrefix +"/"+ saveName;

      return Result.success(200,"上传成功",returnPath);

    }catch (Exception e){
      e.printStackTrace();
    }
    return Result.fail(500,"上传失败",null);
  }


}
