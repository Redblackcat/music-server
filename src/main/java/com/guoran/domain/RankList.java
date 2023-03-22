package com.guoran.domain;

import lombok.Data;

@Data
public class RankList {
    private Long id;

    private Long songListId;

    private Long consumerId;

    private Integer score;
}