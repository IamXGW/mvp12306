package com.iamxgw.dto;

import com.iamxgw.model.TrainSeat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TrainSeatDTO
 *
 * @author IamXGW
 * @since 2025-03-26
 */
@ToString
public class TrainSeatDTO extends TrainSeat {
    @Getter
    @Setter
    private String trainNumber;

    @Getter
    @Setter
    private String fromStation;

    @Getter
    @Setter
    private String toStation;

    @Getter
    @Setter
    private String showStart;

    @Getter
    @Setter
    private String showEnd;

}