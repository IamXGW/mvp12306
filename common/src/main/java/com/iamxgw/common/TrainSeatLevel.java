package com.iamxgw.common;

import lombok.Getter;

@Getter
public enum TrainSeatLevel {

    TOP_GRADE(0, "特等座，商务座，1 排 2 座"),
    GRAND_1(1, "一等座，1 排 4 座"),
    GRAND_2(2, "二等座，1 排 5 座");

    int level;
    String desc;

    TrainSeatLevel(int level, String desc) {
        this.level = level;
        this.desc = desc;
    }
}
