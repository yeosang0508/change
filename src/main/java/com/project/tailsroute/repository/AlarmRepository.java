package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Alarms;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AlarmRepository {
    @Insert("INSERT INTO alarm (regDate, updateDate, memberId, alarm_date, message,site) " +
            "VALUES (NOW(), NOW(), #{memberId},  #{alarm_date}, #{message},#{site})")
    public void addAlarms(int memberId, String alarm_date, String message,String site);

    @Select("SELECT * FROM alarm WHERE memberId = #{memberId}")
    public List<Alarms> findByMemberId(int memberId);

    @Update("UPDATE alarm SET alarm_date = #{alarm_date}, message = #{message} WHERE id = #{id}")
    public void updateAlarms(String alarm_date ,String message , int id);

    @Delete("DELETE FROM alarm WHERE id = #{id}")
    public void deleteAlarms(int id);
}
