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
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ConsumerMapper consumerMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户信息
        Consumer consumer = consumerMapper.selectByUsername(username);
        if (Objects.isNull(consumer)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 查询对应的权限信息

        //把数据封装成UserDetails返回
        return new LoginUser(consumer);
    }
}
