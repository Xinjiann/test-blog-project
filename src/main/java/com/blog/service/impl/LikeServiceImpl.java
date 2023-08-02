package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.UserLikes;
import com.blog.mapper.LikeMapper;
import com.blog.service.LikeService;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, UserLikes> implements LikeService {

}
