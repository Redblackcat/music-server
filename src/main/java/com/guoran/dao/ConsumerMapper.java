package com.guoran.dao;

import com.guoran.domain.Consumer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface ConsumerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Consumer record);

    int insertSelective(Consumer record);

    Consumer selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Consumer record);

    List<Consumer> allUser();

    List<Consumer> userOfId(int id);

    int deleteUser(int id);

    int updateUser(Consumer consumer);

    int verityPassword(String userName, String password);

    int updatePassword(Consumer consumer);

    int updateUserAvator(Consumer consumer);

    int existUsername(String userName);

    List<Consumer> loginStatus(String userName);

    Consumer selectByUsername(String username);
}