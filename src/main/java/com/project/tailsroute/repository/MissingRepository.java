package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Missing;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MissingRepository {

    @Select("""
            SELECT id
                     FROM missing
                     ORDER BY id DESC
                     LIMIT 0, 1;		
                     	""")
    Integer lastNumber();


    @Insert("""
            INSERT INTO missing
            SET memberId = #{loginedMemberId},
            `name` = #{name},
            reportDate = #{reportDate},
            missingLocation = #{missingLocation},
            breed = #{breed},
            color = #{color},
            gender = #{gender},
            age = #{age},
            RFID = COALESCE(#{rfid},"없음"),    
            photo = #{photoPath},
            trait = #{trait}
              			""")
    void write(int loginedMemberId, String name, String reportDate, String missingLocation, String breed, String color, String gender, String age, String rfid, String photoPath, String trait);

    @Select("""
  			SELECT COUNT(*)
    		FROM missing
    		WHERE #{str} = '전체' OR missingLocation LIKE CONCAT('%', #{str}, '%')
				""")
    int totalCnt(String str);

    @Select("""        
			SELECT S.*, M.name AS extra__ownerName, M.cellphoneNum AS extra__ownerCellphoneNum
        	FROM missing S
        	LEFT JOIN `member` M
        	ON M.id = S.memberId
        	WHERE (
                S.missingLocation LIKE CONCAT('%', #{str}, '%') 
                OR S.missingLocation LIKE CONCAT('%', #{str2}, '%')
            ) 
        	OR #{str} = '전체'
       	    ORDER BY S.id DESC
        	LIMIT #{limitFrom}, #{itemsInAPage}        
        		""")
    List<Missing> list(int limitFrom, int itemsInAPage, String str, String str2);

    @Select("""        
			SELECT S.*, M.name AS extra__ownerName, M.cellphoneNum AS extra__ownerCellphoneNum
        	FROM missing S
        	LEFT JOIN `member` M
        	ON M.id = S.memberId
            WHERE S.id = #{missingId}
        		""")
    Missing missingArticle(int missingId);

    @Delete("""
            DELETE FROM missing 
            WHERE id = #{missingId}
                """)
    void missingDelete(int missingId);

    @Update("""
			UPDATE missing
			SET name = #{name},
			reportDate = #{reportDate},
			missingLocation = #{missingLocation},
			breed = #{breed},
			color = #{color},
            gender = #{gender},
            age = #{age},
            RFID = COALESCE(#{rfid},"없음"),
            photo = #{photoPath},
            trait = #{trait}
			WHERE id = #{missingId}
			""")
    void modify(int missingId, String name, String reportDate, String missingLocation, String breed, String color, String gender, String age, String rfid, String photoPath, String trait);

    @Select("""        
        SELECT id
        FROM missing
        ORDER BY id DESC
        LIMIT 1
        """)
    int findMissingId();

}
