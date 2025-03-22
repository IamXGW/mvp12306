package com.iamxgw.dto;

import com.iamxgw.model.TrainNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description:
 * @author: IamXGW
 * @create: 2024-07-13 14:34
 */
@ToString
public class TrainNumberDTO extends TrainNumber {

    @Getter
    @Setter
    private String fromStation;

    @Getter
    @Setter
    private String toStation;
}