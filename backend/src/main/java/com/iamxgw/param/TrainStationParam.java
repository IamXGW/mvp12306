package com.iamxgw.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: IamXGW
 * @create: 2024-07-14 14:19
 */
@Getter
@Setter
@ToString
public class TrainStationParam {

    private Integer id;

    @NotBlank(message = "站点名称不可为空")
    @Length(min = 1, max = 20, message = "站点名称需要在 2-20 个字之间")
    private String name;

    @NotNull(message = "城市不可以为空")
    @Min(value = 1, message = "城市不合法")
    private Integer cityId;

}