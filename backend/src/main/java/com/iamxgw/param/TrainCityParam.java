package com.iamxgw.param;

/**
 * 
 * @author IamXGW
 * @since 2024-07-14 13:23
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


@Getter
@Setter
@ToString
public class TrainCityParam {

    private Integer id;

    @NotBlank(message = "城市名称不可以为空")
    @Length(min = 2, max = 20, message = "城市名称需要在 2-20 个字之间")
    private String name;

}