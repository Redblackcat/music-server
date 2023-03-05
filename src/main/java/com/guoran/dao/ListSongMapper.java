package com.guoran.dao;

import com.guoran.domain.ListSong;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ListSongMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ListSong record);

    int insertSelective(ListSong record);

    ListSong selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ListSong record);

    int updateByPrimaryKey(ListSong record);

    int updateListSongMsg(ListSong record);

    int deleteListSong(Integer songId);

    List<ListSong> allListSong();

    List<ListSong> listSongOfSongId(Integer songListId);
}