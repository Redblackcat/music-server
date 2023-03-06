package com.guoran.controller;

import com.guoran.common.ErrorMessage;
import com.guoran.common.FatalMessage;
import com.guoran.common.ResponseResult;
import com.guoran.common.SuccessMessage;
import com.guoran.constant.Constants;
import com.guoran.domain.Song;
import com.guoran.service.SongService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Api
@RequestMapping("song")
@RestController
public class SongController {

    @Autowired
    private SongService songService;

    /**
     * 文件上传
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(20, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(20, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/songPic/**")
                    .addResourceLocations(Constants.SONG_PIC_PATH);
            registry.addResourceHandler("/song/**")
                    .addResourceLocations(Constants.SONG_PATH);
        }
    }

    /**
     * 添加歌曲
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addSong(HttpServletRequest req, @RequestParam("file") MultipartFile mpfile) {
        ResponseResult result = new ResponseResult();
        String singerId = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String pic = "/img/songPic/tubiao.jpg";
        String lyric = req.getParameter("lyric").trim();

        String fileName = mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/" + fileName;
        try {
            mpfile.transferTo(dest);
            Song song = new Song();
            song.setSingerId(Integer.parseInt(singerId));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            song.setPic(pic);
            song.setLyric(lyric);
            song.setUrl(storeUrlPath);
            boolean res = songService.addSong(song);
            if (res) {
                return result.success("上传成功", storeUrlPath);
            } else {
                return result.error("上传失败");
            }
        } catch (IOException e) {
            return result.error("上传失败");
        }
    }

    /**
     * 删除歌曲
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseResult deleteSong(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String id = req.getParameter("id");

        boolean res = songService.deleteSong(Integer.parseInt(id));
        if (res) {
            return result.success("删除成功");
        } else {
            return result.error("删除失败");
        }
    }

    /**
     * 返回所有歌曲
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseResult allSong() {
        ResponseResult result = new ResponseResult();
        List<Song> list = songService.allSong();
        return result.success(list);
    }

    /**
     * 返回指定歌曲id的歌曲
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseResult songOfId(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String id = req.getParameter("id");
        List<Song> list = songService.songOfId(Integer.parseInt(id));
        return result.success(list);
    }

    /**
     * 返回指定歌手ID的歌曲
     */
    @RequestMapping(value = "/singer/detail", method = RequestMethod.GET)
    public ResponseResult songOfSingerId(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String singerId = req.getParameter("singer_id");
        List<Song> list = songService.songOfSingerId(Integer.parseInt(singerId));
        return result.success(list);
    }

    /**
     * 返回指定歌手名的歌曲
     */
    @RequestMapping(value = "/singerName/detail", method = RequestMethod.GET)
    public ResponseResult songOfSingerName(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String name = req.getParameter("name");
        List<Song> list = songService.songOfSingerName('%' + name + '%');
        return result.success(list);
    }

    /**
     * 更新歌曲信息
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseResult updateSongMsg(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String id = req.getParameter("id").trim();
        String singerId = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String lyric = req.getParameter("lyric").trim();

        Song song = new Song();
        song.setId(Integer.parseInt(id));
        song.setSingerId(Integer.parseInt(singerId));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setUpdateTime(new Date());
        song.setLyric(lyric);

        boolean res = songService.updateSongMsg(song);
        if (res) {
            return result.success("修改成功");
        } else {
            return result.error("修改失败");
        }
    }

    /**
     * 更新歌曲图片
     */
    @ResponseBody
    @RequestMapping(value = "/img/update", method = RequestMethod.POST)
    public ResponseResult updateSongPic(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        ResponseResult result = new ResponseResult();
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "songPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/img/songPic/" + fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setPic(storeUrlPath);
            boolean res = songService.updateSongPic(song);
            if (res) {
                return result.success("上传成功", storeUrlPath);
            } else {
                return result.error("上传失败");
            }
        } catch (IOException e) {
            return result.error("上传失败");
        }
    }

    /**
     * 更新歌曲
     */
    @ResponseBody
    @RequestMapping(value = "/url/update", method = RequestMethod.POST)
    public ResponseResult updateSongUrl(@RequestParam("file") MultipartFile urlFile, @RequestParam("id") int id) {
        ResponseResult result = new ResponseResult();
        String fileName = urlFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/" + fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeUrlPath);
            boolean res = songService.updateSongUrl(song);
            if (res) {
                return result.success("更新成功", storeUrlPath);
            } else {
                return result.error("更新失败");
            }
        } catch (IOException e) {
            return result.error("更新失败");
        }
    }
}
