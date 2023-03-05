package com.guoran.service.impl;

import com.guoran.dao.ConsumerMapper;
import com.guoran.domain.Consumer;
import com.guoran.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private ConsumerMapper consumerMapper;
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


}
