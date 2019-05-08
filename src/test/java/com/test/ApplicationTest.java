package com.test;


import com.test.crawler.Application;
import com.test.crawler.dao.OverseasHotelMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: ligs
 * @Date: 2019/4/30 9:43
 * @Description: todo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

//    @Autowired
//    @Qualifier("hotelService")
//    IHotelService hotelService;

    @Autowired
    private OverseasHotelMapper overseasHotelMapper;



    @Test
    public void test(){
//        hotelService.singeInsert("4372579");
        overseasHotelMapper.truncateHotel();
        overseasHotelMapper.truncateHotelImage();
        overseasHotelMapper.truncateBasicRoom();
        overseasHotelMapper.truncateBasicRoomImage();
        overseasHotelMapper.truncateSubRoom();
    }

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }
}
