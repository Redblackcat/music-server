package com.guoran.controller;

import com.guoran.common.ResponseResult;
import com.guoran.constant.Constants;
import com.guoran.domain.SongList;
import com.guoran.service.SongListService;
import com.guoran.utils.RedisCache;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Api("歌单")
@RequestMapping("/songList")
@RestController
public class SongListController {

    @Autowired
    private SongListService songListService;

    @Autowired
    private RedisCache redisCache;

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/songListPic/**")
                    .addResourceLocations(Constants.SONGLIST_PIC_PATH);
        }
    }

    /**
     * 添加歌单
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addSongList(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String title = req.getParameter("title").trim();
        String introduction = req.getParameter("introduction").trim();
        String style = req.getParameter("style").trim();
        String pic = "/img/songListPic/123.jpg";

        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        songList.setPic(pic);
        songList.setViewCount(0L);

        boolean res = songListService.addSongList(songList);
        if (res) {
            return result.success("添加成功", songList);
        } else {
            return result.error("添加失败");
        }
    }

    /**
     * 删除歌单
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseResult deleteSongList(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String id = req.getParameter("id");

        boolean res = songListService.deleteSongList(Integer.parseInt(id));
        if (res) {
            return result.success("删除成功");
        } else {
            return result.error("删除失败");
        }
    }

    /**
     * 返回所有歌单
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseResult allSongList() {
        ResponseResult result = new ResponseResult<>();
        List<SongList> list = songListService.allSongList();
        //从redis中获取歌单浏览量
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Integer id = list.get(i).getId();
                Integer viewCount = redisCache.getCacheMapValue("songList:viewCount", id.toString());
                list.get(i).setViewCount(viewCount.longValue());
            }
        }
        return result.success(list);
    }

    /**
     * 返回标题包含输入文字的歌单
     */
    @RequestMapping(value = "/likeTitle/detail", method = RequestMethod.GET)
    public ResponseResult songListOfLikeTitle(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String title = req.getParameter("title").trim();
        List<SongList> list = songListService.likeTitle('%' + title + '%');
        if (list.isEmpty()) {
            return result.success("未找到指定歌单");
        }
        return result.success(list);
    }

    /**
     * 返回指定类型的歌单
     */
    @RequestMapping(value = "/style/detail", method = RequestMethod.GET)
    public ResponseResult songListOfStyle(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String style = req.getParameter("style").trim();
        List<SongList> list = songListService.likeStyle('%' + style + '%');
        if (list.isEmpty()) {
            return result.success("未找到指定歌单");
        }
        return result.success(list);
    }

    /**
     * 更新歌单信息
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseResult updateSongListMsg(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String id = req.getParameter("id").trim();
        String title = req.getParameter("title").trim();
        String introduction = req.getParameter("introduction").trim();
        String style = req.getParameter("style").trim();

        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);

        boolean res = songListService.updateSongListMsg(songList);
        if (res) {
            return result.success("修改成功", songList);
        } else {
            return result.error("修改失败");
        }
    }

    /**
     * 更新歌单图片
     */
    @ResponseBody
    @RequestMapping(value = "/img/update", method = RequestMethod.POST)
    public ResponseResult updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        ResponseResult result = new ResponseResult<>();
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songListPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/songListPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(imgPath);

            boolean res = songListService.updateSongListImg(songList);
            if (res) {
                return result.success("上传成功", imgPath);
            } else {
                return result.error("上传失败");
            }
        } catch (IOException e) {
            return result.error("上传失败");
        }
    }

    /**
     * 浏览量
     */
    @PutMapping("/updateViewCount")
    public ResponseResult updateViewCount(HttpServletRequest request) {
        ResponseResult result = new ResponseResult<>();
        String id =  request.getParameter("id");
        songListService.updateViewCount(id);

        return result.success();
    }
}
