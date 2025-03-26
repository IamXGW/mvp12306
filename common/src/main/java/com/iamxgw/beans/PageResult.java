package com.iamxgw.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询结果
 *
 * @author IamXGW
 * @since 2025-03-26
 */
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {
    List<T> data = new ArrayList<T>();
    int total = 0;
}