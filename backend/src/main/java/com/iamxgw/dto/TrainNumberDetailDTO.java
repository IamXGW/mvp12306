package com.iamxgw.dto;

import com.iamxgw.model.TrainNumberDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author IamXGW
 * @since 2024-07-13 14:34
 */

@ToString
public class TrainNumberDetailDTO extends TrainNumberDetail {

    @Getter
    @Setter
    private String fromStation;

    @Getter
    @Setter
    private String toStation;

    @Getter
    @Setter
    private String trainNumber;
}