package com.iamxgw.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: IamXGW
 * @create: 2024-07-24 20:41
 */
@Getter
@Setter
@ToString
public class GeneratorTicketParam {

    @NotNull(message = "车次不可以为空")
    private Integer trainNumberId;

    @NotBlank(message = "必须有发车时间")
    private String fromTime;

}