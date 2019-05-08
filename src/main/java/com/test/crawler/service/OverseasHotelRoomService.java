package com.test.crawler.service;

import com.test.crawler.dao.OverseasHotelMapper;
import com.test.crawler.entity.HotelBasicRoom;
import com.test.crawler.entity.HotelRoomImage;
import com.test.crawler.entity.HotelSubRoom;
import com.test.crawler.util.OverseasHotelCrawlerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: LIUQI
 * @Date: 2019/4/29 16:38
 * @Description: TODO
 */
@Service("overseasHotelRoomService")
public class OverseasHotelRoomService implements IHotelService{

    private final Logger log = LoggerFactory.getLogger(OverseasHotelRoomService.class);
    private ExecutorService exec = Executors.newFixedThreadPool(1);

    @Autowired
    private OverseasHotelMapper overseasHotelMapper;

    @Override
    public Boolean isRunning() {
        if(exec.isShutdown() && !exec.isTerminated()){
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    @Override
    public void batchInsert()  {
        try {
            //获取海外酒店主页
            Document document = Jsoup.connect("https://hotels.ctrip.com/international/").userAgent(OverseasHotelCrawlerUtil.agent).get();
            Elements citys = document.select("a[data-city][rel=nofollow]");
            for (Element e : citys) {
                String url = "https://hotels.ctrip.com"+e.attr("href");
                System.out.println(url);
                exec.submit(new RunThread(url));
                break;
            }
//            exec.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
//            while(true){
//                if(exec.isTerminated()){
//                    System.out.println("所有的子线程都结束了！");
//                    break;
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public class RunThread implements Runnable{

        private String url;

        public RunThread(String url){
            this.url = url;
        }

        @Override
        public void run() {
            String cityPy = url.substring(url.lastIndexOf("/")+1).replaceAll("[^(0-9)]", "");
            List<HotelBasicRoom> hotelBasicRooms = new ArrayList<HotelBasicRoom>();
            List<HotelSubRoom> hotelSubRooms = new ArrayList<HotelSubRoom>();
            List<HotelRoomImage> hotelRoomImages = new ArrayList<HotelRoomImage>();
            //按海外地区获取
            int pageSum = OverseasHotelCrawlerUtil.getPageSum(url);
            for (int pageNo=1; pageNo<= pageSum; pageNo++){
                List<String> hotelIds = OverseasHotelCrawlerUtil.getHotelIds(url+"/p"+pageNo);

                if(hotelIds.isEmpty()){
                   continue;
                }
                for (String hotelId: hotelIds){
                    HashMap map = OverseasHotelCrawlerUtil.getRoomByHotelId(hotelId, cityPy);
                    if(map.isEmpty())continue;
                    List<HotelBasicRoom> basicRooms = (List<HotelBasicRoom>)map.get("basicRooms");
                    List<HotelSubRoom> subRooms = (List<HotelSubRoom>)map.get("subRooms");
                    List<HotelRoomImage> roomImages = (List<HotelRoomImage>)map.get("roomImages");
                    hotelBasicRooms.addAll(basicRooms);
                    hotelSubRooms.addAll(subRooms);
                    hotelRoomImages.addAll(roomImages);

                    basicRooms.clear();subRooms.clear();roomImages.clear();
                }
                if(hotelBasicRooms.size()>=100){
                    overseasHotelMapper.insertBasicRoom(hotelBasicRooms);
                }
                if(hotelSubRooms.size()>=100){
                    overseasHotelMapper.insertSubRoom(hotelSubRooms);
                }
                if(hotelRoomImages.size()>=100){
                    overseasHotelMapper.insertRoomImages(hotelRoomImages);
                }
            }
            System.out.println("Thread:" + Thread.currentThread().getName() + "执行完毕");
        }
    }


}
