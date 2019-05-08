package com.test.crawler.service;

import com.test.crawler.dao.OverseasHotelMapper;
import com.test.crawler.entity.HotelImage;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: LIUQI
 * @Date: 2019/4/29 16:38
 * @Description: todo
 */
@Service("overseasHotelImageService")
public class OverseasHotelImageService implements IHotelService{

    private final Logger log = LoggerFactory.getLogger(OverseasHotelImageService.class);
    private ExecutorService exec = Executors.newFixedThreadPool(8);

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
            int i = 0;
            for (Element e : citys) {
                String url = "https://hotels.ctrip.com"+e.attr("href");
                System.out.println(url);
                exec.submit(new RunThread(url));
            }
            exec.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            while(true){
                if(exec.isTerminated()){
                    System.out.println("所有的子线程都结束了！");
                    break;
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class RunThread implements Runnable{
        private String url;

        public RunThread(String url){
            this.url = url;
        }

        @Override
        public void run() {
            List<HotelImage> hotelImages = new ArrayList<HotelImage>();
            int order = 1;
            //按海外地区获取
            int pageSum = OverseasHotelCrawlerUtil.getPageSum(url);
            for (int pageNo=1; pageNo<=pageSum; pageNo++){
                List<String> hotelIds = OverseasHotelCrawlerUtil.getHotelIds(url+"/p"+pageNo);
                for (String hotelId : hotelIds) {
                    hotelImages.addAll(OverseasHotelCrawlerUtil.getHotelImageByHotelId(hotelId));
                    if(hotelImages.size() >= 100){
                        overseasHotelMapper.insertImages(hotelImages);
                        hotelImages.clear();
                    }
                }
            }
            if(hotelImages.size()>0){
                overseasHotelMapper.insertImages(hotelImages);
            }
            System.out.println("Thread:" + Thread.currentThread().getName() + "执行完毕");
        }
    }

}
