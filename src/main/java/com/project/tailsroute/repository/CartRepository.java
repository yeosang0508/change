package com.project.tailsroute.repository;

import com.project.tailsroute.vo.Carts;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartRepository {
    @Insert("INSERT INTO cart (regDate, updateDate, memberId, itemName, itemprice,itemlink) " +
            "VALUES (NOW(), NOW(), #{memberId}, #{itemName}, #{itemprice},#{itemlink})")
    public void addCarts(int memberId, String itemName, Integer itemprice,String itemlink);

    @Select("SELECT * FROM cart WHERE memberId = #{memberId}")
    public List<Carts> findByMemberId(int memberId);

    @Delete("DELETE FROM cart WHERE id = #{id}")
    public void deleteCart(int id);
}