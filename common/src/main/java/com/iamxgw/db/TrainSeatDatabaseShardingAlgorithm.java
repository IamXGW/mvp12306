package com.iamxgw.db;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * 
 * @author IamXGW
 * @since 2024-07-07 21:38
 */
public class TrainSeatDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {
    private final static String PREFIX = "trainSeatDB";

    private String determineDB(int val) {
        int db = val % 5;
        if (db == 0) {
            db = 5;
        }
        return PREFIX + db;
    }
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        String actualDBName = determineDB(preciseShardingValue.getValue());
        if (collection.contains(actualDBName)) {
            return actualDBName;
        }
        throw new IllegalArgumentException();
    }
}