package com.guoran.controller;

import com.guoran.common.ErrorMessage;
import com.guoran.common.FatalMessage;
import com.guoran.common.ResponseResult;
import com.guoran.common.SuccessMessage;
import com.guoran.constant.Constants;
import com.guoran.domain.Singer;
import com.guoran.service.SingerService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api
@RequestMapping("singer")
@RestController
public class SingerController {
    @Autowired
    private SingerService singerService;

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/singerPic/**")
                    .addResourceLocations(Constants.SINGER_PIC_PATH);
        }
    }

    /**
     *添加歌手
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addSinger(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String name = req.getParameter("  name").trim();
        String sex = req.getParameter("sex").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();
        String pic = "/img/avatorImages/user.jpg";

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        singer.setPic(pic);

        boolean res = singerService.addSinger(singer);
        if (res) {
            return result.success("添加成功", singer);
        } else {
            return result.error("添加失败");
        }
    }

    /**
     * 删除歌手
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseResult deleteSinger(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String id = req.getParameter("id");

        boolean res = singerService.deleteSinger(Integer.parseInt(id));
        if (res) {
            return result.success("删除成功");
        } else {
            return result.error("删除失败");
        }
    }

    /**
     * 返回所有歌手信息
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseResult allSinger() {
        ResponseResult result = new ResponseResult();
        List<Singer> list = singerService.allSinger();
        return result.success(list);
    }

    /**
     * 根据歌手名字查找歌手
     */
    @RequestMapping(value = "/name/detail", method = RequestMethod.GET)
    public ResponseResult singerOfName(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String name = req.getParameter("name").trim();
        List<Singer> list = singerService.singerOfName(name);
        if (!list.isEmpty()) {
            return result.success("查找成功", list);
        } else {
            return result.success("未找到该歌手");
        }
    }

    /**
     * 根据歌手性别查找歌手
     */
    @RequestMapping(value = "/sex/detail", method = RequestMethod.GET)
    public ResponseResult singerOfSex(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String sex = req.getParameter("sex").trim();
        List<Singer> list = singerService.singerOfSex(Integer.parseInt(sex));
        return result.success("查找成功", list);
    }

    /**
     * 更新歌手信息
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseResult updateSingerMsg(HttpServletRequest req) {
        ResponseResult result = new ResponseResult();
        String id = req.getParameter("id").trim();
        String name = req.getParameter("name").trim();
        String sex = req.getParameter("sex").trim();
        String birth = req.getParameter("birth").trim();
        String location = req.getParameter("location").trim();
        String introduction = req.getParameter("introduction").trim();

        Singer singer = new Singer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(myBirth);
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean res = singerService.updateSingerMsg(singer);
        if (res) {
            return result.success("修改成功");
        } else {
            return result.error("修改失败");
        }
    }

    /**
     * 更新歌手头像
     */
    @ResponseBody
    @RequestMapping(value = "/avatar/update", method = RequestMethod.POST)
    public ResponseResult updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        ResponseResult result = new ResponseResult();
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "singerPic";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/singerPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(imgPath);

            boolean res = singerService.updateSingerPic(singer);
            if (res) {
                return result.success("上传成功", imgPath);
            } else {
                return result.error("上传失败");
            }
        } catch (IOException e) {
            return result.error("上传失败");
        }
    }
}
