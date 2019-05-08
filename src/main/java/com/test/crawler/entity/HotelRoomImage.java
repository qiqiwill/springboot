package com.test.crawler.entity;

/**
 * @Author: LIUQI
 * @Date: 2019/4/30 11:12
 * @Description: todo
 */
public class HotelRoomImage {

    private String basicRoomId;

    private String imagePath;

    private String hotelId;     //酒店id

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getBasicRoomId() {
        return basicRoomId;
    }

    public void setBasicRoomId(String basicRoomId) {
        this.basicRoomId = basicRoomId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
