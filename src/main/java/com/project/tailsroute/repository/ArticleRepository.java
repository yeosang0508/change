package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleRepository {

    @Insert("""
            INSERT INTO article
            SET regDate = NOW(),
            updateDate = NOW(),
            memberId = #{memberId},
            boardId = #{boardId},
            title = #{title},
            `body` = #{body}
            """)
    public void writeArticle(int memberId, String title, String body, String boardId);

    @Delete("""
            DELETE FROM article
            WHERE id = #{id}
            """)
    public void deleteArticle(int id);

    @Update("""
            UPDATE article
            SET updateDate = NOW(),
            title = #{title},
            `body` = #{body}
            WHERE id = #{id}
            """)
    public void modifyArticle(int id, String title, String body);

    @Select("""
            SELECT A.*, M.nickname AS extra__writer, D.photo AS extra__dogPoto
            FROM article AS A
            LEFT JOIN `member` AS M
            ON A.memberId = M.id
            LEFT JOIN dog AS D
            ON A.memberId = D.memberId
            WHERE A.id = #{id}
            	""")
    public Article getForPrintArticle(int id);

    @Select("""
            SELECT *
            FROM article
            WHERE id = #{id}
            	""")
    public Article getArticleById(int id);

    @Select("""
                SELECT A.*, M.nickname AS extra__writer, IFNULL(COUNT(R.id), 0) AS extra__repliesCount
                FROM article AS A
                LEFT JOIN `member` AS M
                ON A.memberId = M.id
                LEFT JOIN `reply` AS R
                ON A.id = R.relId
                WHERE 1=1
                AND (#{boardId} = 0 OR A.boardId = #{boardId})
                AND (
                    (#{searchKeyword} = '') OR (
                        (#{searchKeywordTypeCode} = '제목' AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')) OR
                        (#{searchKeywordTypeCode} = '내용' AND A.body LIKE CONCAT('%', #{searchKeyword}, '%')) OR
                        (#{searchKeywordTypeCode} = '작성자' AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')) OR
                        (#{searchKeywordTypeCode} = '전체' AND (
                            A.title LIKE CONCAT('%', #{searchKeyword}, '%') OR
                            A.body LIKE CONCAT('%', #{searchKeyword}, '%') OR
                            M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
                        ))
                    )
                )
                GROUP BY A.id
                ORDER BY A.id DESC
                LIMIT #{limitFrom}, #{limitTake}
            """)
    public List<Article> getForPrintArticles(int boardId, int limitFrom, int limitTake, String searchKeywordTypeCode, String searchKeyword);


    @Select("""
            SELECT A.* , M.nickname AS extra__writer
            FROM article AS A
            LEFT JOIN `member` AS M
            ON A.memberId = M.id
            ORDER BY A.id DESC
            """)
    public List<Article> getArticles();

    @Select("SELECT LAST_INSERT_ID();")
    public int getLastInsertId();

    @Select("""
            SELECT COUNT(*)
            FROM article AS A
            LEFT JOIN `member` AS M
            ON A.memberId = M.id
            WHERE 1=1
            AND (#{boardId} = 0 OR A.boardId = #{boardId})
                AND (
                    (#{searchKeyword} = '') OR (
                        (#{searchKeywordTypeCode} = '제목' AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')) OR
                        (#{searchKeywordTypeCode} = '내용' AND A.body LIKE CONCAT('%', #{searchKeyword}, '%')) OR
                        (#{searchKeywordTypeCode} = '작성자' AND M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')) OR
                        (#{searchKeywordTypeCode} = '전체' AND (
                            A.title LIKE CONCAT('%', #{searchKeyword}, '%') OR
                            A.body LIKE CONCAT('%', #{searchKeyword}, '%') OR
                            M.nickname LIKE CONCAT('%', #{searchKeyword}, '%')
                        ))
                    )
                )
                """)
    public int getArticleCount(int boardId, String searchKeywordTypeCode, String searchKeyword);


    @Select("""
            SELECT hitCount
            FROM article
            WHERE id = #{id}
            	""")
    public int getArticleHitCount(int id);

    @Update("""
            UPDATE article
            SET goodReactionPoint = goodReactionPoint + 1
            WHERE id = #{relId}
            """)
    public int increaseGoodReactionPoint(int relId);

    @Update("""
            UPDATE article
            SET goodReactionPoint = goodReactionPoint - 1
            WHERE id = #{relId}
            """)
    public int decreaseGoodReactionPoint(int relId);

    @Update("""
            UPDATE article
            SET badReactionPoint = badReactionPoint + 1
            WHERE id = #{relId}
            """)
    public int increaseBadReactionPoint(int relId);

    @Update("""
            UPDATE article
            SET badReactionPoint = badReactionPoint - 1
            WHERE id = #{relId}
            """)
    public int decreaseBadReactionPoint(int relId);

    @Update("""
            UPDATE article
            SET hitCount = hitCount + 1
            WHERE id = #{id}
            """)
    public int increaseHitCount(int id);

    @Select("""
            SELECT goodReactionPoint
            FROM article
            WHERE id = #{relId}
            """)
    public int getGoodRP(int relId);

    @Select("""
            SELECT badReactionPoint
            FROM article
            WHERE id = #{relId}
            """)
    public int getBadRP(int relId);

    @Select("""
            SELECT MAX(id) + 1
            FROM article
            """)
    public Integer getCurrentArticleId();

}
