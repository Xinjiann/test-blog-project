package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.common.lang.Result;
import com.blog.entity.Record;
import com.blog.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class RecordController {

  @Autowired
  RecordService recordService;

  @GetMapping("/records/{userId}")
  public Result list(@PathVariable(name = "userId") Long userId) {

    QueryWrapper<Record> wrapper = new QueryWrapper<>();
    wrapper.eq("user_id", userId);
    List<Record> records = recordService.list(wrapper);
    List<Map<String, String>> countList = new ArrayList<>();
    Map<String, Integer> countMap = new HashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    records.forEach(i -> {
      String date = sdf.format(i.getDate());
      if (!countMap.containsKey(date)) {
        countMap.put(date, 1);
      } else {
        countMap.put(date, countMap.get(date) + 1);
      }
    });
    countMap.forEach((k, v) -> {
      Map<String, String> newMap = new HashMap<>();
      newMap.put("date", k);
      newMap.put("count", v.toString());
      countList.add(newMap);
    });
    return Result.success(countList);
  }

  @GetMapping("/records/add/{userId}")
  public Result add(@PathVariable(name = "userId") Long userId, @RequestParam("type") String type) throws ParseException {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String simpleDate = sdf.format(date);
    Record temp = new Record();
    temp.setUserId(userId);
    temp.setType(type);
    temp.setDate(sdf.parse(simpleDate));
    recordService.save(temp);
    return Result.success(temp);

  }

}
