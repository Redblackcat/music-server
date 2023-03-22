package com.guoran.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guoran.dao.ConsumerMapper;
import com.guoran.domain.Consumer;
import com.guoran.domain.LoginUser;
import com.guoran.service.ConsumerService;
import com.guoran.utils.JwtUtil;
import com.guoran.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Override
    public Object login(Consumer consumer) {
        //AuthenticationManager 进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(consumer.getUsername(), consumer.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        log.info("222");
        //如果没有查到 抛出异常
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，用用户id生成一个jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getConsumer().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis
        redisCache.setCacheObject("login:" + userid, loginUser);
        return map;
    }

    @Override
    public boolean addUser(Consumer consumer) {
        return consumerMapper.insertSelective(consumer) > 0;
    }

    /**
     * 返回用户列表
     * @return
     */
    @Override
    public List<Consumer> allUser() {
        return consumerMapper.allUser();
    }

    /**
     * 返回指定id的用户
     */
    @Override
    public List<Consumer> userOfId(int id) {
        return consumerMapper.userOfId(id);
    }

    /**
     * 删除指定id的用户
     * @param id
     * @return
     */
    @Override
    public boolean deleteUser(int id) {
        return consumerMapper.deleteUser(id) > 0;
    }

    /**
     * 修改用户子信息
     * @param consumer
     * @return
     */
    @Override
    public boolean updateUser(Consumer consumer) {
        return consumerMapper.updateUser(consumer) > 0;
    }

    @Override
    public boolean verityPassword(String userName, String password) {
        return consumerMapper.verityPassword(userName, password) > 0;
    }

    @Override
    public boolean updatePassword(Consumer consumer) {
        return consumerMapper.updatePassword(consumer) > 0;
    }

    @Override
    public boolean updateUserAvator(Consumer consumer) {
        return consumerMapper.updateUserAvator(consumer) > 0;
    }

    @Override
    public boolean existUser(String userName) {
        return consumerMapper.existUsername(userName) > 0;
    }

    @Override
    public List<Consumer> loginStatus(String userName) {
        return consumerMapper.loginStatus(userName);
    }

    @Override
    public void logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        //LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        JSONObject loginUser = (JSONObject) authentication.getPrincipal();
        Integer id = (Integer) loginUser.get("id");
        //删除redis中的值
        redisCache.deleteObject("login:" + id);
    }


}
