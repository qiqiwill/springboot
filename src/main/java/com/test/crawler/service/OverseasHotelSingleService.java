package com.test.crawler.service;

import com.test.crawler.dao.OverseasHotelMapper;
import com.test.crawler.entity.*;
import com.test.crawler.util.OverseasHotelCrawlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: LIUQI
 * @Date: 2019/4/29 16:38
 * @Description: TODO
 */
@Service("overseasHotelSingleService")
public class OverseasHotelSingleService implements IHotelSingleService{

    @Autowired
    private OverseasHotelMapper overseasHotelMapper;

    private ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    @Override
    public void singleInsert(String hotelId) {
        exec.submit(new SingleHotelHandle(hotelId));
    }

    public class SingleHotelHandle implements Runnable{

        private String hotelId;
        public SingleHotelHandle(String hotelId){
            this.hotelId = hotelId;
        }

        @Override
        public void run() {
            singeInsert(hotelId);
        }
    }

    /*单个爬取*/
    @Transactional
    public void singeInsert(String id){
        deleteByHotelId(id);
        Hotel hotel = OverseasHotelCrawlerUtil.getByHotelId(id,"","","",-1);
        ArrayList list = new ArrayList();
        list.add(hotel);
        //新增酒店基本数据
        overseasHotelMapper.insert(list);
        //新增酒店图片数据
        List<HotelImage> hotelImages = OverseasHotelCrawlerUtil.getHotelImageByHotelId(id);
        if(!hotelImages.isEmpty()){
            overseasHotelMapper.insertImages(hotelImages);
        }
        //新增酒店房间数据
        HashMap map = OverseasHotelCrawlerUtil.getRoomByHotelId(id, hotel.getEnCity());
        if(!map.isEmpty()){
            List<HotelBasicRoom> basicRooms = (List<HotelBasicRoom>)map.get("basicRooms");
            List<HotelSubRoom> subRooms = (List<HotelSubRoom>)map.get("subRooms");
            List<HotelRoomImage> roomImages = (List<HotelRoomImage>)map.get("roomImages");
            if(!basicRooms.isEmpty()){
                overseasHotelMapper.insertBasicRoom(basicRooms);
            }
            if(!subRooms.isEmpty()){
                overseasHotelMapper.insertSubRoom(subRooms);
            }
            if(!roomImages.isEmpty()){
                overseasHotelMapper.insertRoomImages(roomImages);
            }
        }
    }

    public void deleteByHotelId(String id){
        overseasHotelMapper.deleteHotel(id);
        overseasHotelMapper.deleteHotelImage(id);
        overseasHotelMapper.deleteBasicRoom(id);
        overseasHotelMapper.deleteSubRoom(id);
        overseasHotelMapper.deleteBasicRoomImage(id);
    }
}
