package com.iamxgw.common;

import lombok.Getter;

/**
 * @author xuguangwei
 */

@Getter
public enum TrainType {

    CRH2(1220),
    CRH5(1244);

    final int count;

    TrainType(int count) {
        this.count = count;
    }
}
