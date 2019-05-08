package com.test.crawler.service;

import com.test.crawler.dao.OverseasHotelMapper;
import com.test.crawler.entity.Hotel;
import com.test.crawler.util.OverseasHotelCrawlerUtil;
import com.test.crawler.util.OverseasStateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: LIUQI
 * @Date: 2019/4/29 16:38
 * @Description: TODO
 */
@Service("overseasHotelService")
public class OverseasHotelService implements IHotelService{

    private final Logger log = LoggerFactory.getLogger(OverseasHotelService.class);
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

    @Override
    public void batchInsert()  {
        try {
            //获取海外酒店主页
            Document document = Jsoup.connect("https://hotels.ctrip.com/international/").userAgent(OverseasHotelCrawlerUtil.agent).get();
            Elements citys = document.select("a[data-city][rel=nofollow]");
            for (Element e : citys) {
                String cityUrlSuffix = e.attr("href");
                String url = "https://hotels.ctrip.com"+cityUrlSuffix;
                if("/international/92573".equals(cityUrlSuffix)
                    || "/international/bangkok359".equals(cityUrlSuffix)
                    || "/international/dubai220".equals(cityUrlSuffix)
                    || "/international/phuket725".equals(cityUrlSuffix)
                    || "/international/paris192".equals(cityUrlSuffix)
                    || "/international/kualalumpur315".equals(cityUrlSuffix)
                    || "/international/kohsamui1229".equals(cityUrlSuffix)
                    || "/international/chicago549".equals(cityUrlSuffix)
                    || "/international/monterey3800".equals(cityUrlSuffix)
                    || "/international/maldives3880".equals(cityUrlSuffix)
                    || "/international/page3804".equals(cityUrlSuffix)){
                }else{
                    System.out.println(url);
                    String cnCity = e.text();
                    String cnState = OverseasStateUtil.getState(e.text());
                    String enState = OverseasStateUtil.getEnStateByCn(cnState);
                    exec.submit(new RunThread( url, cnState, enState, cnCity));
                    //曼谷bangkok359
                    //迪拜dubai220
                    //普吉岛phuket725
                    //巴黎paris192
                    //吉隆坡kualalumpur315
                    //苏梅岛kohsamui1229
                    //芝加哥chicago549
                    //蒙特雷县monterey3800
                    //马尔代夫maldives3880
                    //佩吉page3804
                    //那霸92573
                }
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
                    Thread.sleep(90000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class RunThread implements Runnable{

        private String url;
        private String cnState;
        private String enState;
        private String cnCity;
        private String cityName;
        private Set<String> hotelIdSet = new HashSet<String>();

        public RunThread( String url, String cnState, String enState, String cnCity){
            this.url = url;
            this.cnState = cnState;this.enState = enState;
            this.cnCity = cnCity;
            this.cityName = url.substring(url.lastIndexOf("/")+1);
        }

        @Override
        public void run() {
            int order = 1;
            List<Hotel> hotels = new ArrayList<Hotel>();
            try {
                int pageSum = OverseasHotelCrawlerUtil.getPageSum(url);
                for (int pageNo=1; pageNo<= pageSum; pageNo++){//按海外地区分页获取
                    List<String> hotelIds = OverseasHotelCrawlerUtil.getHotelIds(url+"/p"+pageNo);
                    for (String hotelId : hotelIds) {
                        if(hotelIdSet.contains(hotelId)){
                            log.warn("url:"+url+"第"+pageNo+"页 有重複酒店id"+ hotelId);
                            continue;//携程頁面有重複的酒店過濾掉
                        }else{
                            hotelIdSet.add(hotelId);
                        }
                        // 通过id获取详情页面解析
                        hotels.add(OverseasHotelCrawlerUtil.getByHotelId(hotelId, cnState, enState, cnCity, order++));
                    }
                    if(hotels.size() >= 100){
                        try {
                            overseasHotelMapper.insert(hotels);
                        }catch (Exception e1){
                            log.warn("add hotel error.... city:"+cityName+" page:"+ pageNo +"                             "+e1.getMessage());
                        }finally {
                            hotels.clear();//持久化后清空隊列
                        }
                    }
                }
            } finally {
                if(hotels.size()>0){
                    overseasHotelMapper.insert(hotels);
                    hotels.clear();
                }
                hotelIdSet.clear();
            }
            System.out.println("Thread:" + Thread.currentThread().getName() + "执行完毕");
        }
    }

}
