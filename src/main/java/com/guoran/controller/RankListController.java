package com.guoran.controller;

import com.guoran.common.ResponseResult;
import com.guoran.domain.RankList;
import com.guoran.service.RankListService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api
@RequestMapping("rankList")
@RestController
public class RankListController {

    @Autowired
    private RankListService rankListService;

    // 提交评分
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addRank(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String songListId = req.getParameter("song_list_id").trim();
        String consumerId = req.getParameter("consumer_id").trim();
        String score = req.getParameter("score").trim();

        RankList rankList = new RankList();
        rankList.setSongListId(Long.parseLong(songListId));
        rankList.setConsumerId(Long.parseLong(consumerId));
        rankList.setScore(Integer.parseInt(score));

        boolean res = rankListService.addRank(rankList);
        if (res) {
            return result.success("评价成功", rankList);
        } else {
            return result.error("评价失败");
        }
    }

    // 获取指定歌单的评分
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseResult rankOfSongListId(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String songListId = req.getParameter("song_list_id");
        int score = rankListService.rankOfSongListId(Long.parseLong(songListId));
        return result.success(score);
    }
    
    // 获取指定用户的歌单评分
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseResult getUserRank(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String consumerId = req.getParameter("consumer_id");
        String songListId = req.getParameter("song_list_id");
        int score = rankListService.getUserRank(Long.parseLong(consumerId), Long.parseLong(songListId));
        return result.success(score);
    }
}
