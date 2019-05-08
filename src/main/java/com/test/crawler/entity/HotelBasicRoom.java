package com.test.crawler.entity;

/**
 * @Author: LiuQi
 * @Date: 2019/5/5 17:51
 * @Description: TODO
 */
public class HotelBasicRoom {

    private String basicRoomId; //基本房型
    private String hotelId;     //酒店id
    private String cnName;      //酒店名称
    private String enName;
    private String roomArea;    //房间大小
    private Boolean addBed;     //是否可以加床
    private String facilities;  //房间设施
    private String intro;       //房间介绍

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
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

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getBasicRoomId() {
        return basicRoomId;
    }

    public void setBasicRoomId(String basicRoomId) {
        this.basicRoomId = basicRoomId;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

    public Boolean getAddBed() {
        return addBed;
    }

    public void setAddBed(Boolean addBed) {
        this.addBed = addBed;
    }
}
