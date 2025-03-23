package com.iamxgw.db;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * 
 * @author IamXGW
 * @since 2024-07-07 21:43
 */
public class TrainSeatTableShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {
    private final static String PREFIX = "train_Seat_";

    private String determineTable(int val) {
        int table = val % 10;
        if (table == 0) {
            table = 10;
        }
        return PREFIX + table;
    }
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        String actualTableName = determineTable(preciseShardingValue.getValue());
        if (collection.contains(actualTableName)) {
            return actualTableName;
        }
        throw new IllegalArgumentException();
    }
}