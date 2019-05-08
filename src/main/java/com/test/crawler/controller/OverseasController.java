package com.test.crawler.controller;

import com.test.crawler.entity.ResultMsg;
import com.test.crawler.service.IHotelService;
import com.test.crawler.service.IHotelSingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: LIUQI
 * @Date: 2019/4/29 16:32
 * @Description: todo
 */
@RestController
public class OverseasController {

    @Autowired
    private IHotelSingleService hotelSingleService;

    @Autowired
    @Qualifier("overseasHotelService")
    private IHotelService hotelService;

    @Autowired
    @Qualifier("overseasHotelImageService")
    private IHotelService hotelImageService;

    @Autowired
    @Qualifier("overseasHotelRoomService")
    private IHotelService hotelRoomService;

    @RequestMapping(method = RequestMethod.GET, value = "/hotels")
    public String add(){
        if(hotelService.isRunning()){
            return "正在执行中";
        }else{
            hotelService.batchInsert();
            return "执行完成";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/hotelImages")
    public String addHotelImage(){
        if(hotelImageService.isRunning()){
            return "正在执行中";
        }else{
            hotelImageService.batchInsert();
            return "执行完成";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rooms")
    public String addRoom(){
        if(hotelRoomService.isRunning()){
            return "正在执行中";
        }else{
            hotelRoomService.batchInsert();
            return "执行完成";
        }
    }

    //通过id新增酒店
    @RequestMapping(method = RequestMethod.GET, value = "/{hotelId}")
    public ResultMsg addGetById(@PathVariable String hotelId){
        hotelSingleService.singleInsert(hotelId);
        return ResultMsg.ok();
    }

}
