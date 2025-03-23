package com.iamxgw.dto;

import com.iamxgw.model.TrainStation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author IamXGW
 *@since 2024-07-13 14:34
 */
@ToString
public class TrainStationDTO extends TrainStation {

    @Getter
    @Setter
    private String cityName;
}