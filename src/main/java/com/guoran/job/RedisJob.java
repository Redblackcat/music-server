package com.guoran.job;

import com.guoran.dao.SongListMapper;
import com.guoran.domain.SongList;
import com.guoran.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class RedisJob {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SongListMapper songListMapper;

    /*@Scheduled(cron = "0/5 * * * * ?")
    public void testJob() {
        System.out.println("定时任务执行");
    }*/

    /**
     * 定时任务：每隔10分钟向数据库中更新redis中的浏览量
     */
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void updateViewCount() {
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("songList:viewCount");

        List<SongList> songLists = viewCountMap.entrySet()
                .stream()
                .map(entry -> new SongList(Integer.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        //更新到数据库中
        for (int i = 0; i < songLists.size(); i++) {
            songListMapper.updateViewCount(songLists.get(i));
        }
    }
}
