package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Dog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DogRepository {

    @Select("""
			SELECT id
            FROM dog
            ORDER BY id DESC
            LIMIT 1;		
            	""")
    Integer lastNumber();

    @Insert("""
            INSERT INTO dog
            SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{loginedMemberId},
            `name` = #{dogName},
            weight = #{dogWeight},
            `type` = #{dogType},
            photo = #{photoPath}
                    			""")
    void upload(int loginedMemberId, String dogName, String dogWeight, String dogType, String photoPath);

    @Select("""
            SELECT *
            FROM dog
            WHERE memberId = #{loginedMemberId} 
            LIMIT 0, 1;
             """)
    Dog getDogfile(int loginedMemberId);

    @Select("""
            SELECT *
            FROM dog
            WHERE id = #{dogId}
             """)
    Dog getDogfileId(int dogId);

    @Update("""
			UPDATE dog
			SET updateDate = NOW(),
            `name` = #{dogName},
            weight = #{dogWeight},
            `type` = #{dogType},
            photo = #{photoPath}
			WHERE id = #{dogId}
			""")
    void modify(int dogId, String dogName, String dogWeight, String dogType, String photoPath);
}
