package com.blog.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.lang.PageRes;
import com.blog.common.lang.Result;
import com.blog.entity.Comment;
import com.blog.entity.Reply;
import com.blog.entity.Reply.SingleReply;
import com.blog.entity.dto.CommentDO;
import com.blog.entity.dto.ReplyDO;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommentController {

  @Autowired
  CommentService commentService;

  @GetMapping("comment/{id}")
  public Result commentDetail(@PathVariable(name = "id") Long id, @RequestParam(defaultValue = "1") Integer currentPage){

    IPage<Comment> page = new Page<>(currentPage, 10);
    QueryWrapper<Comment> wrapper=new QueryWrapper<>();
    wrapper.eq("blog_id", id);
    wrapper.eq("status", 1);
    IPage<Comment> list= commentService.page(page,wrapper.orderByDesc("create_time"));
    List<CommentDO> records = list.getRecords().stream().map(CommentDO::new).collect(Collectors.toList());
    PageRes<CommentDO> res = new PageRes<>(records, list.getTotal(), list.getSize(),
        list.getCurrent());
    return Result.success(res);
  }


  @PostMapping("comment/comment")
  public Result comment(@Validated @RequestBody Comment comment){

    Comment temp = new Comment();
    BeanUtil.copyProperties(comment, temp);
    temp.setCreateTime(new Date());
    Reply reply = new Reply();
    reply.setReplies(Collections.emptyList());
    temp.setReply(reply);
    commentService.save(temp);
    return Result.success(null);
  }

  @GetMapping("comment/delete")
  public Result commentDel(@RequestParam("id") Integer id) {
    Comment comment = commentService.getById(id);
    comment.setStatus(0);
    commentService.saveOrUpdate(comment);
    return Result.success(null);
  }

  @PostMapping("comment/reply")
  public Result reply(@Validated @RequestBody ReplyDO replyDO){

    SingleReply temp = new SingleReply();
    BeanUtil.copyProperties(replyDO, temp);
    temp.setCreateTime(new Date());
    Comment comment = commentService.getById(replyDO.getCommentId());
    List<SingleReply> replyList = comment.getReply().getReplies();
    replyList.add(temp);
    comment.getReply().setReplies(replyList);
    String json= JSON.toJSONString(comment.getReply());
    UpdateWrapper updateWrapper = new UpdateWrapper();
    updateWrapper.eq("id", replyDO.getCommentId());
    updateWrapper.set("reply", json);
    commentService.update(comment, updateWrapper);
    return Result.success(null);
  }
}
