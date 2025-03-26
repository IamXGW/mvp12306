package com.iamxgw.seatDao;

import com.iamxgw.model.TrainSeat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuguangwei
 */
public interface TrainSeatMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TrainSeat record);

    int insertSelective(TrainSeat record);

    TrainSeat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TrainSeat record);

    int updateByPrimaryKey(TrainSeat record);

    void batchInsert(List<TrainSeat> seatList);

    List<TrainSeat> searchList(@Param("trainNumberId") int trainNumberId, @Param("ticket") String ticket,
                               @Param("status") Integer status, @Param("carriageNum") Integer carriageNum,
                               @Param("rowNum") Integer rowNum, @Param("seatNum") Integer seatNum,
                               @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    int countList(@Param("trainNumberId") int trainNumberId, @Param("ticket") String ticket,
                  @Param("status") Integer status, @Param("carriageNum") Integer carriageNum,
                  @Param("rowNum") Integer rowNum, @Param("seatNum") Integer seatNum);
}