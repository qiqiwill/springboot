package com.test.crawler.dao;


import com.test.crawler.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: LIUQI
 * @Date: 2019/4/29 16:38
 * @Description: todo
 */
@Repository
@Mapper
public interface OverseasHotelMapper {


    @Insert("<script> " +
            "  insert into hotel(hotel_id, cn_name,en_name,diamond,label,lng,lat,cn_state,cn_country,cn_city,cn_district,cn_address," +
            "  en_state,en_country,en_city,en_district,en_address,description,facilities,childpolicy,ordernum) values  " +
            "  <foreach collection='result' item='item' separator=','> " +
            "       (#{item.hotelId}, #{item.cnName}, #{item.enName}, #{item.diamond},#{item.label}, #{item.lng}, #{item.lat}, #{item.cnState}, #{item.cnCountry}, #{item.cnCity}, #{item.cnDistrict}, #{item.cnAddress}," +
            "       #{item.enState}, #{item.enCountry}, #{item.enCity}, #{item.enDistrict}, #{item.enAddress},#{item.description}, #{item.facilities}, #{item.childPolicy}, #{item.orderNum})" +
            "  </foreach> " +
            "</script>")
    @Transactional
    void insert(@Param(value = "result") List<Hotel> result);


    @Transactional
    @Insert("<script> " +
            "insert into hotel_image(hotel_id, image_path) values  " +
            "<foreach collection='result' item='item' separator=','> " +
            "     (#{item.hotelId}, #{item.imagePath})" +
            "</foreach>" +
            "</script>")
    void insertImages(@Param(value = "result") List<HotelImage> result);


    @Transactional
    @Insert("<script> " +
            "  insert into hotel_basic_room(basic_room_id, hotel_id, cn_name,en_name, room_area, addBed, facilities) values  " +
            "  <foreach collection='result' item='item' separator=','> " +
            "       (#{item.basicRoomId},  #{item.hotelId}, #{item.cnName}, #{item.enName}, #{item.roomArea}, #{item.addBed}, #{item.facilities})" +
            "  </foreach> " +
            "</script>")
    void insertBasicRoom(@Param(value = "result") List<HotelBasicRoom> result);


    @Transactional
    @Insert("<script> " +
            "  insert into hotel_sub_room(room_id, hotel_id, basic_room_id, cn_name, en_name, bed_type, max_person) values  " +
            "  <foreach collection='result' item='item' separator=','> " +
            "       (#{item.roomId}, #{item.hotelId}, #{item.basicRoomId}, #{item.cnName}, #{item.enName}, #{item.bedType}, #{item.maxPerson})" +
            "  </foreach> " +
            "</script>")
    void insertSubRoom(@Param(value = "result") List<HotelSubRoom> result);


    @Transactional
    @Insert("<script> " +
            "insert into hotel_basic_room_image(hotel_basic_room_id, image_path, hotel_id) values  " +
            "<foreach collection='result' item='item' separator=','> " +
            "     (#{item.basicRoomId}, #{item.imagePath}, #{item.hotelId})" +
            "</foreach>" +
            "</script>")
    void insertRoomImages(@Param(value = "result") List<HotelRoomImage> result);


    //根据酒店id删除 酒店
    @Delete("delete from hotel where hotel_id = #{id}")
    void deleteHotel(String id);
    @Delete("delete from hotel_image where hotel_id = #{id}")
    void deleteHotelImage(String id);
    @Delete("delete from hotel_basic_room where hotel_id = #{id}")
    void deleteBasicRoom(String id);
    @Delete("delete from hotel_sub_room where hotel_id = #{id}")
    void deleteSubRoom(String id);
    @Delete("delete from hotel_basic_room_image where hotel_id = #{id}")
    void deleteBasicRoomImage(String id);


    //根据酒店id删除 酒店
    //;truncate table hotel_image;truncate table hotel_basic_room;truncate table hotel_sub_room;truncate table hotel_basic_room_image;
    @Update("truncate  table hotel")
    void truncateHotel();
    @Update("truncate  table hotel_image")
    void truncateHotelImage();
    @Update("truncate  table hotel_basic_room")
    void truncateBasicRoom();
    @Update("truncate  table hotel_sub_room")
    void truncateSubRoom();
    @Update("truncate  table hotel_basic_room_image")
    void truncateBasicRoomImage();
}
