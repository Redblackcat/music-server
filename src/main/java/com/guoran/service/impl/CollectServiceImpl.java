package com.guoran.service.impl;

import com.guoran.dao.CollectMapper;
import com.guoran.domain.Collect;
import com.guoran.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Override
    public boolean addCollection(Collect collect) {
        return collectMapper.insertSelective(collect) > 0;
    }

    @Override
    public boolean existSongId(Integer userId, Integer songId) {
        return collectMapper.existSongId(userId, songId) > 0;
    }

    @Override
    public boolean deleteCollect(Integer userId, Integer songId) {
        return collectMapper.deleteCollect(userId, songId) > 0;
    }

    @Override
    public List<Collect> collectionOfUser(Integer userId) {
        return collectMapper.collectionOfUser(userId);
    }
}
