package com.iamxgw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuguangwei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainCity {
    private Integer id;

    private String name;

}