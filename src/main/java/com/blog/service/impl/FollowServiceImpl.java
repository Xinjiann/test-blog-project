package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.UserFollow;
import com.blog.mapper.FollowMapper;
import com.blog.service.FollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, UserFollow> implements
    FollowService {

}
