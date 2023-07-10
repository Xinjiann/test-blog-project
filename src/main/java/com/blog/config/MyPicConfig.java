package com.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Xinjian Li
 * @Create 2022-01-02 15:44
 * @Description 新增加一个类用来添加虚拟路径映射
 */
@Configuration
public class MyPicConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String path = System.getProperty("user.dir") + "src/main/resources/static/img/";
    registry.addResourceHandler("/img/**").addResourceLocations("file:" + path);
  }
}

