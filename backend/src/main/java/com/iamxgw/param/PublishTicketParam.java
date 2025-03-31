package com.iamxgw.param;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author IamXGW
 * @since 2025-03-31
 */
@Data
public class PublishTicketParam {

    @NotBlank(message = "车次不能为空")
    private String trainNumber;

    @NotBlank(message = "必须选中座位")
    private String trainSeatIds;

}