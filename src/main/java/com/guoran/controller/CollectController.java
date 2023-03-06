package com.guoran.controller;

import com.guoran.common.ResponseResult;
import com.guoran.domain.Collect;
import com.guoran.service.CollectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Api
@RequestMapping("/collection")
@RestController
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * 添加收藏的歌曲
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addCollection(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String userId = req.getParameter("user_id");
        String type = req.getParameter("type");
        String songId = req.getParameter("song_id");
        String songListId = req.getParameter("song_list_id");

        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(new Byte(type));
        //type为0时收藏歌曲，type为1时收藏歌单
        if (new Byte(type) == 0) {
            collect.setSongId(Integer.parseInt(songId));
        } else if (new Byte(type) == 1) {
            collect.setSongListId(Integer.parseInt(songListId));
        }
        collect.setCreateTime(new Date());

        boolean res = collectService.addCollection(collect);
        if (res) {
            return result.success("添加收藏成功", collect);
        } else {
            return result.error("添加收藏失败");
        }
    }

    // 取消收藏的歌曲
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseResult deleteCollection(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String userId = req.getParameter("user_id").trim();
        String songId = req.getParameter("song_id").trim();

        boolean res = collectService.deleteCollect(Integer.parseInt(userId), Integer.parseInt(songId));
        if (res) {
            return result.success("取消收藏", "歌曲id: " + songId);

        } else {
            return result.error("取消收藏失败");
        }

    }

    // 是否收藏歌曲
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public ResponseResult isCollection(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String userId = req.getParameter("user_id").trim();
        String songId = req.getParameter("song_id").trim();

        boolean res = collectService.existSongId(Integer.parseInt(userId), Integer.parseInt(songId));
        if (res) {
            return result.success("已收藏");
        } else {
            return result.success("未收藏");
        }
    }

    // 返回的指定用户 ID 收藏的列表
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseResult collectionOfUser(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String userId = req.getParameter("user_id");
        List<Collect> list = collectService.collectionOfUser(Integer.parseInt(userId));
        if (!list.isEmpty()) {
            return result.success(list);
        } else {
            return result.success("该用户未收藏歌曲");
        }

    }
}
