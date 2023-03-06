package com.guoran.service.impl;

import com.guoran.common.SuccessMessage;
import com.guoran.domain.Consumer;
import com.guoran.domain.LoginUser;
import com.guoran.service.LoginService;
import com.guoran.utils.JwtUtil;
import com.guoran.utils.RedisCache;
import com.guoran.vo.UserInfoVo;
import com.guoran.vo.UserLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Object login(Consumer consumer) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(consumer.getUsername(), consumer.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户或密码错误");
        }
        //获取用户id 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getConsumer().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(userId, loginUser);
        //把token和用户信息封装返回
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(loginUser.getConsumer(), userInfoVo);
        UserLoginVo vo = new UserLoginVo(jwt, userInfoVo);
        return new SuccessMessage("登录成功", vo).getMessage();
    }
}
