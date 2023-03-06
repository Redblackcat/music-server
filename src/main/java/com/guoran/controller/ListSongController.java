package com.guoran.controller;

import com.guoran.common.ErrorMessage;
import com.guoran.common.ResponseResult;
import com.guoran.common.SuccessMessage;
import com.guoran.domain.ListSong;
import com.guoran.service.ListSongService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api
@RequestMapping("listSong")
@RestController
public class ListSongController {

    @Autowired
    private ListSongService listSongService;

    /**
     * 给歌单添加歌曲
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addListSong(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String songId = req.getParameter("song_id").trim();
        String songListId = req.getParameter("song_list_id").trim();

        ListSong listsong = new ListSong();
        listsong.setSongId(Integer.parseInt(songId));
        listsong.setSongListId(Integer.parseInt(songListId));

        boolean res = listSongService.addListSong(listsong);
        if (res) {
            return result.success("添加成功", listsong);
        } else {
            return result.error("添加失败");
        }
    }

    /**
     * 删除歌单里的歌曲
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseResult deleteListSong(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String songId = req.getParameter("songId");

        boolean res = listSongService.deleteListSong(Integer.parseInt(songId));
        if (res) {
            return result.success("删除成功");
        } else {
            return result.error("删除失败");
        }
    }

    /**
     * 返回歌单里指定歌单id的歌曲
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseResult listSongOfSongId(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String songListId = req.getParameter("songListId");
        List<ListSong> list = listSongService.listSongOfSongId(Integer.parseInt(songListId));
        return result.success(list);
    }

    /**
     * 更新歌单里面的歌曲信息
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseResult updateListSongMsg(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String id = req.getParameter("id").trim();
        String songId = req.getParameter("song_id").trim();
        String songListId = req.getParameter("song_list_id").trim();

        ListSong listsong = new ListSong();
        listsong.setId(Integer.parseInt(id));
        listsong.setSongId(Integer.parseInt(songId));
        listsong.setSongListId(Integer.parseInt(songListId));

        boolean res = listSongService.updateListSongMsg(listsong);
        if (res) {
            return result.success("更新成功", listsong);
        } else {
            return result.error("更新失败");
        }
    }
}
