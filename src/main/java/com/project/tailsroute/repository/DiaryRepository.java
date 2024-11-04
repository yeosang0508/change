package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Diary;
import org.apache.ibatis.annotations.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface DiaryRepository {

	@Select("""
            SELECT D.*, M.nickname AS extra__writer
            FROM diary AS D
            INNER JOIN `member` AS M
            ON D.memberId = M.id
            WHERE D.id = #{id}
            """)
	Diary getForPrintDiary(int id);

	@Select("""
            SELECT *
            FROM diary
            WHERE id = #{id}
            """)
	Diary getDiaryById(int id);

	@Insert("""
            INSERT INTO diary
            SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{memberId},
            title = #{title},
            `body` = #{body},
            imagePath = #{imagePath},
            startDate = #{startDate},
            endDate = #{endDate},
            takingTime = #{takingTime},
            information = #{information}	
            """)
	void writeDiary(int memberId, String title, String body, String imagePath,
					LocalDate startDate, LocalDate endDate,
					LocalTime takingTime, String information);

	@Delete("""
            DELETE FROM diary
            WHERE id = #{id}
            """)
	void deleteDiary(int id);

	@Update("""
            UPDATE diary
            SET updateDate = NOW(),
            title = #{title},
            `body` = #{body},
            imagePath = #{imagePath},
            startDate = #{startDate},
            endDate = #{endDate},
            takingTime = #{takingTime},
            information = #{information}
            WHERE id = #{id}
            """)
	void modifyDiary(int id, String title, String body, String imagePath,
					 LocalDate startDate, LocalDate endDate,
					 LocalTime takingTime, String information);

	@Select("""
                        SELECT D.*, M.nickname AS extra__writer
                        FROM diary AS D
                        INNER JOIN `member` AS M
                        ON D.memberId = M.id
                        WHERE D.memberId = #{memberId}
                        ORDER BY D.id DESC
            LIMIT #{limit} OFFSET #{offset}
                        """)
	List<Diary> findAllByOrderByDateDesc(@Param("memberId") int memeberId,@Param("limit") int limit, @Param("offset") int offset);

	@Select("""
        SELECT D.*, M.nickname AS extra__writer
            FROM diary AS D
            INNER JOIN `member` AS M
            ON D.memberId = M.id
            WHERE D.memberId = #{memberId}
            ORDER BY D.id asc 		
         LIMIT #{limit} OFFSET #{offset}
""")
	List<Diary> findAllByOrderByDateAsc(@Param("memberId") int memeberId,@Param("limit") int limit, @Param("offset") int offset);


	@Select("""
                        SELECT D.*, M.nickname AS extra__writer
                        FROM diary AS D
                        INNER JOIN `member` AS M
                        ON D.memberId = M.id
                        WHERE D.memberId = #{memberId}
                        ORDER BY D.id DESC
           
                        """)
	List<Diary> findAllDiary(int memberId);



	@Select("""
    SELECT COUNT(*)
    FROM diary
     WHERE memberId = #{memberId}
""")
	int countDiaries(int memberId);

}