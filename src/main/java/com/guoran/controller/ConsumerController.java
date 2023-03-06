package com.guoran.controller;

import com.guoran.common.*;
import com.guoran.common.enums.AppHttpCodeEnum;
import com.guoran.constant.Constants;
import com.guoran.domain.Consumer;
import com.guoran.service.ConsumerService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.ReactiveRedisCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api
@RequestMapping("/user")
@RestController
public class ConsumerController {
    @Autowired
    ConsumerService consumerService;

    @Configuration
    public static class MyPicConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/img/avatorImages/**")
                    .addResourceLocations(Constants.AVATOR_IMAGES_PATH);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addUser(HttpServletRequest request) {
        ResponseResult result = new ResponseResult();
        String userName = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String sex = request.getParameter("sex").trim();
        String phoneNum = request.getParameter("phone_num").trim();
        String email = request.getParameter("email").trim();
        String birth = request.getParameter("birth").trim();
        String introduction = request.getParameter("introduction").trim();
        String location = request.getParameter("location").trim();
        String avator = "/img/avatorImages/user.jpg";

        if (consumerService.existUser(userName)) {
            return result.error(AppHttpCodeEnum.USERNAME_EXIST.getCode(), AppHttpCodeEnum.USERNAME_EXIST.getMsg());
        }

        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        consumer.setUsername(userName);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));

        if ("".equals(phoneNum)) {
            consumer.setPhoneNum(null);
        } else {
            consumer.setPhoneNum(phoneNum);
        }
        consumer.setBirth(myBirth);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setAvator(avator);
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());

        boolean res = consumerService.addUser(consumer);
        if (res) {
            return result.success("注册成功", consumer);
        } else {
            return result.error("注册失败");
        }
    }

    //TODO:登录状态判断
    @ResponseBody
    @RequestMapping(value = "/login/status", method = RequestMethod.POST)
    public ResponseResult loginStatus(HttpServletRequest req, HttpSession session) {
        ResponseResult result = new ResponseResult();
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        boolean res = consumerService.verityPassword(userName, password);
        if (res) {
            session.setAttribute("username", userName);
            List<Consumer> list = consumerService.loginStatus(userName);
            return result.success("登录成功", list);
        } else {
            return result.error("用户名或密码错误");
        }
    }

    /**
     * 返回用户列表
     */
     @RequestMapping(value = "", method = RequestMethod.GET)
     public ResponseResult allUser() {
         ResponseResult result = new ResponseResult();
         List<Consumer> list = consumerService.allUser();
         return result.success(list);
     }

    /**
     * 返回指定id的用户
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseResult userOfId(HttpServletRequest request) {
        ResponseResult result = new ResponseResult();
        String id = request.getParameter("id");
        List<Consumer> list = consumerService.userOfId(Integer.parseInt(id));
        return result.success(list);
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseResult deleteUser(HttpServletRequest request) {
        ResponseResult result = new ResponseResult();
        String id = request.getParameter("id");

        boolean res = consumerService.deleteUser(Integer.parseInt(id));
        if (res) {
            return result.success("删除成功");
        } else {
            return result.error("删除失败");
        }
    }

    /**
     * 更新用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseResult updateUser(HttpServletRequest request) {
        ResponseResult result = new ResponseResult();

        String id = request.getParameter("id").trim();
        String userName = request.getParameter("username").trim();
        String sex = request.getParameter("sex").trim();
        String phoneNum = request.getParameter("phone_num").trim();
        String email = request.getParameter("email").trim();
        String birth = request.getParameter("birth").trim();
        String introduction = request.getParameter("introduction").trim();
        String location = request.getParameter("location").trim();

        Consumer consumer = new Consumer();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myBirth = new Date();
        try {
            myBirth = dateFormat.parse(birth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        consumer.setId(Integer.parseInt(id));
        consumer.setUsername(userName);
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setUpdateTime(new Date());
        consumer.setBirth(myBirth);

        boolean res = consumerService.updateUser(consumer);
        if (res) {
            return result.success("修改成功", consumer);
        } else {
            return result.error("修改失败");
        }
    }

    /**
     * 更新用户密码
     */
    @ResponseBody
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ResponseResult updatePassWord(HttpServletRequest request) {
        ResponseResult result = new ResponseResult();

        String id = request.getParameter("id").trim();
        String userName = request.getParameter("username").trim();
        String oldPassword = request.getParameter("old_password").trim();
        String password = request.getParameter("password").trim();

        boolean res = consumerService.verityPassword(userName,oldPassword);
        if (!res) {
            return result.error("密码错误");
        }

        Consumer consumer = new Consumer();
        consumer.setId(Integer.parseInt(id));
        consumer.setPassword(password);

        boolean flag = consumerService.updatePassword(consumer);
        if (flag) {
            return result.success("密码修改成功", consumer);
        } else {
            return result.error("密码修改失败");
        }
    }

    /**
     * 更新用户头像
     */
    @ResponseBody
    @RequestMapping(value = "/user/avatar/update", method = RequestMethod.POST)
    public ResponseResult updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) throws IOException {
        ResponseResult result = new ResponseResult();
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = Constants.PROJECT_PATH + System.getProperty("file.separator") + "img" + System.getProperty("file.separator") + "avatorImages";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String imgPath = "/img/avatorImages/" + fileName;

        avatorFile.transferTo(dest);
        Consumer consumer = new Consumer();
        consumer.setId(id);
        consumer.setAvator(imgPath);
        boolean res = consumerService.updateUserAvator(consumer);
        if (res) {
            return result.success();
        } else {
            return result.error("上传失败");
        }
    }
}
