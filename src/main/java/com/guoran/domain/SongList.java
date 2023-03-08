package com.guoran.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongList {
    private Integer id;

    private String title;

    private String pic;

    private String style;

    private String introduction;

    private Long viewCount;

    public SongList(Integer id, long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }
}