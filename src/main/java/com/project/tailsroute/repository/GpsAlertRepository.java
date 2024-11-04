package com.project.tailsroute.repository;

import com.project.tailsroute.vo.GpsAlert;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GpsAlertRepository {

    @Select("""
		   SELECT G.*, D.comPortName extra__comPortName, D.name extra__dogName, M.cellphoneNum extra__cellphoneNum
           FROM gpsAlert G
           LEFT JOIN dog D
           ON G.dogId = D.id
           LEFT JOIN `member` M
           ON D.memberId = M.id;		
             """)
    List<GpsAlert> All();

    @Insert("""
            INSERT INTO gpsAlert
            SET 
            regDate = NOW(),
            updateDate = NOW(),
            dogId = #{dogId},
            latitude = #{latitude},
            longitude = #{longitude}
              			""")
    void saveLocation(int dogId, double latitude, double longitude);

    @Select("""
		   SELECT G.*, D.comPortName extra__comPortName, D.name extra__dogName, M.cellphoneNum extra__cellphoneNum
           FROM gpsAlert G
           LEFT JOIN dog D
           ON G.dogId = D.id
           LEFT JOIN `member` M
           ON D.memberId = M.id
           WHERE dogId = #{dogId}
             """)
    GpsAlert getGpsAlert(int dogId);

    @Update("""
			UPDATE gpsAlert
			SET updateDate = NOW(),
			latitude = #{latitude},
            longitude = #{longitude}
			WHERE dogId = #{dogId}
			""")
    void updateLocation(int dogId, double latitude, double longitude);

    @Delete("""
            DELETE FROM gpsAlert 
            WHERE dogId = #{dogId}
                """)
    void deleteLocation(int dogId);

    @Update("""
			UPDATE gpsAlert
			SET updateDate = NOW(),
			chack = #{chack}
			WHERE id = #{gpsId}
			""")
    void toggleChack(int chack, int gpsId);

    @Update("""
			UPDATE gpsAlert
			SET updateDate = NOW(),
			onOff = #{value}
			WHERE dogId = #{dogId}
			""")
    void toggleOnOff(int dogId, int value);
}
