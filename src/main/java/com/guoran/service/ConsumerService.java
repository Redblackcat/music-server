package com.guoran.service;

import com.guoran.domain.Consumer;

import java.util.List;

public interface ConsumerService {
    Object login(Consumer consumer);

    boolean addUser(Consumer consumer);

    List<Consumer> allUser();

    List<Consumer> userOfId(int parseInt);

    boolean deleteUser(int parseInt);

    boolean updateUser(Consumer consumer);

    boolean verityPassword(String userName, String password);
    boolean updatePassword(Consumer consumer);

    boolean updateUserAvator(Consumer consumer);

    boolean existUser(String userName);

    List<Consumer> loginStatus(String userName);

    void logout();
}
