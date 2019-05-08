package com.test.crawler.entity;

/**
 * @Author: LIUQI
 * @Date: 2019/4/29 16:32
 * @Description: todo
 */
public class Hotel {

    private String hotelId;
    private String cnName;      //中文名
    private String enName;      //英文名
    private int diamond;        //星级等级
    private String label;       //标签
    private String lng;         //经度
    private String lat;         //纬度
    private String cnState;     //州
    private String cnCountry;   //国家
    private String cnCity;      //城市
    private String cnDistrict;  //地区
    private String cnAddress;   //地址
    private String enState;     //州
    private String enCountry;   //国家
    private String enCity;      //城市
    private String enDistrict;  //地区
    private String enAddress;   //地址
    private String description; //酒店基本介绍
    private String facilities;  //酒店基本设施
    private String childPolicy; //兒童政策
    private int orderNum;       //排序序号
//    private int batchNum;       //添加批次


    public String getChildPolicy() {
        return childPolicy;
    }

    public void setChildPolicy(String childPolicy) {
        this.childPolicy = childPolicy;
    }

    public String getCnDistrict() {
        return cnDistrict;
    }

    public void setCnDistrict(String cnDistrict) {
        this.cnDistrict = cnDistrict;
    }

    public String getEnDistrict() {
        return enDistrict;
    }

    public void setEnDistrict(String enDistrict) {
        this.enDistrict = enDistrict;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
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

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCnState() {
        return cnState;
    }

    public void setCnState(String cnState) {
        this.cnState = cnState;
    }

    public String getCnCountry() {
        return cnCountry;
    }

    public void setCnCountry(String cnCountry) {
        this.cnCountry = cnCountry;
    }

    public String getCnCity() {
        return cnCity;
    }

    public void setCnCity(String cnCity) {
        this.cnCity = cnCity;
    }

    public String getCnAddress() {
        return cnAddress;
    }

    public void setCnAddress(String cnAddress) {
        this.cnAddress = cnAddress;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getEnState() {
        return enState;
    }

    public void setEnState(String enState) {
        this.enState = enState;
    }

    public String getEnCountry() {
        return enCountry;
    }

    public void setEnCountry(String enCountry) {
        this.enCountry = enCountry;
    }

    public String getEnCity() {
        return enCity;
    }

    public void setEnCity(String enCity) {
        this.enCity = enCity;
    }

    public String getEnAddress() {
        return enAddress;
    }

    public void setEnAddress(String enAddress) {
        this.enAddress = enAddress;
    }
}
