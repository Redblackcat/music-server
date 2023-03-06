package com.guoran.service.impl;

import com.guoran.dao.ConsumerMapper;
import com.guoran.domain.Consumer;
import com.guoran.domain.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private ConsumerMapper consumerMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        Consumer consumer = consumerMapper.selectByUsername(username);
        //判断是否查到用户 若没查到抛出异常
        if (Objects.isNull(consumer)) {
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //TODO 查询权限信息封装
        return new LoginUser(consumer);
    }
}
