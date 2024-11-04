package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Hospital;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HospitalRepository {

    @Insert("""
                INSERT INTO `hospital` (name, callNumber, jibunAddress, roadAddress)
                VALUES (#{name}, #{callNumber}, #{addressLocation}, #{addressStreet})
            """)
    void doInsertHospitalInfo(String callNumber, String addressLocation, String addressStreet, String name);

    @Select("SELECT * FROM hospital")
    List<Hospital> findAllHospitals();

    @Select("SELECT id, IF(roadAddress = '', NULL, roadAddress) AS roadAddress, " +
            "  IF(jibunAddress = '', NULL, jibunAddress) AS jibunAddress FROM hospital WHERE latitude IS NULL OR longitude IS NULL")
    List<Hospital> getHospitalsWithoutCoordinates();

    @Update("UPDATE hospital SET latitude = #{latitude}, longitude = #{longitude} WHERE id = #{id}")
    void updateHospitalCoordinates(@Param("id") int id, @Param("latitude") String latitude, @Param("longitude") String longitude);
}
