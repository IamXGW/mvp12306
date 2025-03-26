package com.iamxgw.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * 分页
 *
 * @author IamXGW
 * @since 2025-03-26
 */
public class PageQuery {
    @Getter
    @Setter
    @Min(value = 1, message = "页码必须大于 0")
    private int pageNo;

    @Getter
    @Setter
    @Min(value = 1, message = "每页展示内容必须大于 0")
    private int pageSize;

    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}