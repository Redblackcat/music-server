package com.guoran.runner;

import com.guoran.dao.SongListMapper;
import com.guoran.domain.SongList;
import com.guoran.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class ViewCountRunner implements CommandLineRunner {
    //CommandLineRunner 项目启动时，预先加载数据。
    @Autowired
    private SongListMapper songListMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询歌单信息 id viewCount
        List<SongList> list = songListMapper.allSongList();
        HashMap<String, Long> hashMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            hashMap.put(list.get(i).getId().toString(), list.get(i).getViewCount());
        }
        for (int i = 0; i < list.size(); i++) {
            redisCache.setCacheMap("songList:viewCount", hashMap);
        }
    }
}
