package com.test.crawler.entity;

/**
 * @Author: LiuQi
 * @Date: 2019/5/5 20:23
 * @Description: TODO
 */
public class HotelSubRoom {

    private String roomId;      //子房间id
    private String hotelId;     //酒店id
    private String basicRoomId; //基本房型id
    private String cnName;      //
    private String enName;
    private int bedType;        //床型
    private int maxPerson;      //最大入住人数

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getBasicRoomId() {
        return basicRoomId;
    }

    public void setBasicRoomId(String basicRoomId) {
        this.basicRoomId = basicRoomId;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public int getBedType() {
        return bedType;
    }

    public void setBedType(int bedType) {
        this.bedType = bedType;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }
}
