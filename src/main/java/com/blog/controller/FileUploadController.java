package com.blog.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.blog.common.lang.FileUploadResult;
import com.blog.common.lang.Result;
import com.blog.service.FileUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class FileUploadController {

  @Autowired
  private FileUpLoadService fileUploadService;

  /**
   * 文件上传到oss
   *
   * @param uploadFile
   * @return
   * @throws Exception
   */
  @RequestMapping("file/upload")
  @ResponseBody
  public FileUploadResult upload(@RequestParam("file") MultipartFile uploadFile,
      @RequestParam("id") Long id)
      throws Exception {
    return fileUploadService.upload(uploadFile, id);
  }

  /**
   * 根据文件名删除
   *
   * @param objectName
   * @return
   * @throws Exception
   */
  @RequestMapping("/file/delete")
  @ResponseBody
  public FileUploadResult delete(@RequestParam("fileName") String objectName)
      throws Exception {
    return fileUploadService.delete(objectName);
  }


  /**
   * 查看桶内所有文件
   *
   * @return
   * @throws Exception
   */
  @RequestMapping("file/list")
  @ResponseBody
  public List<OSSObjectSummary> list()
      throws Exception {
    return fileUploadService.list();
  }

  /**
   * 根据文件名进行下载
   *
   * @param objectName
   * @param response
   * @throws IOException
   */
  @RequestMapping("file/download")
  @ResponseBody
  public void download(@RequestParam("fileName") String objectName, HttpServletResponse response)
      throws IOException {
    //通知浏览器以附件形式下载
    response.setHeader("Content-Disposition",
        "attachment;filename=" + new String(objectName.getBytes(), "ISO-8859-1"));
    fileUploadService.exportOssFile(response.getOutputStream(), objectName);
  }

  @RequestMapping("file/getUrl/{fileName}")
  @ResponseBody
  public Result getUrl(@PathVariable(name = "fileName") String fileName) {
    String path = fileName.replaceAll("_", "/");
    return fileUploadService.getUrl(path);
  }


}