package com.test.crawler.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.crawler.entity.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: LIUQI
 * @Date: 2019/5/7 13:54
 * @Description: todo
 */
@Component
public class OverseasHotelCrawlerUtil {

    private final static Logger log = LoggerFactory.getLogger(OverseasHotelCrawlerUtil.class);
    public static final String agent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36";
    private static final int timeout = 100000;

    private static String hotelImageRootDir;
    private static String hotelRoomImageRootDir;

    @Value("${hotel-image-root-dir}")
    public void setHotelImageRootDir(String hotelImageRootDir){
        this.hotelImageRootDir = hotelImageRootDir;
    }
    @Value("${hotelroom-image-root-dir}")
    public void setHotelRoomImageRootDir(String hotelRoomImageRootDir){
        this.hotelRoomImageRootDir = hotelRoomImageRootDir;
    }

    //有的城市获取 酒店 需要调用 https://hotels.ctrip.com/international/tool/AjaxHotelList.aspx
    private static int getPageSumByAjax(String url){
        String  cityNum= url.replaceAll("[^(0-9)]", "");
        String  cityPy= url.replaceAll("[^(A-Za-z)]", "");
        Map<String, String> map = new HashMap();
        map.put("cityId", cityNum);
        map.put("cityPY", cityPy);
        map.put("pageIndex", "1");
        map.put("destinationType", "1");
        map.put("IsSuperiorCity", "1");
        try {
            Document document = Jsoup.connect("https://hotels.ctrip.com/international/tool/AjaxHotelList.aspx").userAgent(agent)
                .data(map).post();
            String pageNo = document.select("#txtpage").attr("data-totalpage");
            return Integer.valueOf(pageNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  0 ;
    }

    private static List<String> getHotelId(String cityPyNum, String pageNo){
        String  cityNum = cityPyNum.replaceAll("[^(0-9)]", "");
        String  cityPy = cityPyNum.replaceAll("[^(A-Za-z)]", "");
        Map<String, String> map = new HashMap();
        map.put("cityId", cityNum);
        map.put("cityPY", cityPy);
        map.put("pageIndex", pageNo);
        map.put("destinationType", "1");
        map.put("IsSuperiorCity", "1");
        List<String> hotelIds = new ArrayList<String>();
        try {
            Document document = Jsoup.connect("https://hotels.ctrip.com/international/tool/AjaxHotelList.aspx").userAgent(agent).data(map).post();
            Elements hList = document.select(".hlist .hlist_item");
            for (Element e : hList){
                hotelIds.add(e.id());
            }
            return hotelIds;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  hotelIds ;
    }


//    public static boolean isGetHotelByAjaxHotelList(String cityPyNum){
//        if("92573".equals(cityPyNum)|| "bangkok359".equals(cityPyNum)
//                || "dubai220".equals(cityPyNum)|| "phuket725".equals(cityPyNum)
//                || "paris192".equals(cityPyNum)|| "kualalumpur315".equals(cityPyNum)
//                || "kohsamui1229".equals(cityPyNum)|| "chicago549".equals(cityPyNum)
//                || "monterey3800".equals(cityPyNum)|| "maldives3880".equals(cityPyNum)
//                || "page3804".equals(cityPyNum)){
//            return false;
//        }else{
//            return true;
//        }
//    }

    public static int getPageSum(String url){
        Document cityDocument = null;
        try {
            cityDocument = Jsoup.connect(url).userAgent(agent).timeout(timeout).get();
        } catch (IOException e) {
            log.warn("url not connection error:  url :"+url);
        }
        Elements elements = cityDocument.select(".c_page_list.layoutfix");
        if(elements == null || elements.size()==0){
            String cityPyNum = url.substring(url.lastIndexOf("/")+1);
            return getPageSumByAjax(cityPyNum);
        }else{
            return Integer.valueOf(elements.select("a").last().attr("data-value"));
        }
    }


    public static List<String> getHotelIds(String url){
        Document document = null;
        List<String> hotelIds = new ArrayList<String>();
        try {
             document = Jsoup.connect(url).userAgent(agent).timeout(timeout).maxBodySize(Integer.MAX_VALUE).header("upgrade-insecure-requests","1")
                    .header("accept-encoding","gzip, deflate, br").header("accept-language","zh-CN,zh;q=0.9").header("Connection","close").get();
        } catch (IOException e) {
            log.warn("url not connection error:  url :"+url);
        }
        Elements hList = document.select(".hlist .hlist_item");

        //如果没有酒店 就通过另一种方式获取
        if(hList == null || hList.size() ==0){
            String pageNo = url.substring(url.lastIndexOf("/"));
            String cityPyNum = url.substring(url.substring(0, url.lastIndexOf("/")).lastIndexOf("/") + 1);
            List<String> hotelIdList = getHotelId(cityPyNum, pageNo);
            System.out.println("url:"+url+ " 酒店个数："+hotelIdList.size());
            return hotelIdList;
        }

        System.out.println("url:"+url+ " 酒店个数："+hList.size());
        if(hList.size() < 25){
            log.warn("url:"+url+ " 酒店个数："+hList.size());
        }
        for (Element e : hList){
            hotelIds.add(e.id());
        }
        return hotelIds;
    }


    //获取酒店基本数据
    public static Hotel getByHotelId(String hotelId, String cnState, String enState, String cnCity, int orderNum){
        String url = "https://hotels.ctrip.com/international/" + hotelId+".html";
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        hotel.setOrderNum(orderNum);
        //酒店详情页面
        Document detailDocument = null;
        try {
            detailDocument = Jsoup.connect(url).userAgent(agent).timeout(timeout).maxBodySize(Integer.MAX_VALUE).get();
        } catch (IOException e) {
            log.warn("url not connection error:  url :"+url);
        }
        //酒店名称
        String cnName = detailDocument.select(".cont_in .htl_info .name").removeClass("ename").text();
        String enName = detailDocument.select(".cont_in .htl_info .name .ename").text();
        hotel.setCnName(cnName);
        hotel.setEnName(enName);
        //酒店星级
        try{
            Elements small = detailDocument.select(".cont_in .htl_info .name small span");
            String diamondClass = small.get(1).attr("class");
            String diamond = diamondClass.replaceAll("hotel_diamond0","");
            hotel.setDiamond(Integer.valueOf(diamond));
        }catch (Exception e1){
            hotel.setDiamond(0);//没有星级 设置为0
        }
        //酒店标签
        Elements labels = detailDocument.select(".cont_in .htl_info .htl_info_tags .label_onsale_blue");
        StringBuilder labelStr = new StringBuilder();
        for (Element label : labels) {
            labelStr.append(label.text()).append(",");
        }
        String label = labelStr.toString();
        if(!label.isEmpty()){
            hotel.setLabel(label.substring(0, label.length()-1));//酒店标签
        }

        //酒店经纬度
        hotel.setLat(detailDocument.select("meta[itemprop=latitude]").attr("content"));
        hotel.setLng(detailDocument.select("meta[itemprop=longitude]").attr("content"));
        //属于哪个州
        hotel.setCnState(cnState);hotel.setEnState(enState);
        //属于哪个国家
        Elements addressE = detailDocument.getElementsByClass("address_text");
        String addressS = addressE.text();
        hotel.setCnCountry(addressS.substring(addressS.lastIndexOf(",")+1));//国家
        hotel.setEnCountry(hotel.getCnCountry());
        //属于哪个城市
        try {
            hotel.setCnCity(cnCity);
            hotel.setEnCity(detailDocument.getElementById("cityPY").val());
        }catch (Exception e1){}
        //属于哪个区域
        try{
            hotel.setCnDistrict(detailDocument.select("#LocationSeoUrl").text());// en todo
        }catch (Exception ex){
            log.warn("id:" +hotelId+ "名称："+cnName +"   没有区域");
        }
        //酒店地址
        hotel.setEnAddress(addressS);
        //酒店基本信息
        Element htlDes = detailDocument.getElementById("htlDes");
        if(htlDes != null){
            hotel.setDescription(detailDocument.getElementById("htlDes").text());
        }
        //酒店设施
        Elements facilities = detailDocument.select("#J_htl_facilities tr[data-show]");
        List<Facilities> facilitiesList = new ArrayList<Facilities>();
        for (Element f : facilities) {
            String options = f.select("th").text();
            Elements lis = f.select("li");
            List<String> liList = new ArrayList<String>();
            for (Element l : lis) {
                liList.add(l.text());
            }
            Facilities o = new Facilities();
            o.setOptions(options);
            o.setValues(liList);
            facilitiesList.add(o);
        }
        hotel.setFacilities(JSONArray.toJSONString(facilitiesList));

        //酒店兒童政策
        Elements detail_con_3 = detailDocument.select(".htl_info_table.detail_con_3");
        try{
            Element etTr = detail_con_3.select("tr").get(1);
            hotel.setChildPolicy(etTr.text());
        }catch (Exception e1){}
        return hotel;
    }

    //获取酒店图片数据
    public static List<HotelImage> getHotelImageByHotelId(String hotelId){
        String url = "https://hotels.ctrip.com/international/" + hotelId+".html";
        Document detailDocument = null;
        try {
            detailDocument = Jsoup.connect(url).userAgent(agent).timeout(timeout).get();
        } catch (IOException e) {
            log.warn("url not connection error:  url :"+url);
        }
        String cityPy = detailDocument.select("#cityPY").val();
        Elements picList = detailDocument.getElementById("picList").select(".J_pic_list");

        List<HotelImage> res = new ArrayList<HotelImage>();
        for (Element pic : picList){
            String picPath = downImages(hotelImageRootDir+cityPy+File.separator+hotelId, "http:"+pic.attr("_src"));//酒店图片
            HotelImage image = new HotelImage();
            image.setHotelId(hotelId);
            image.setImagePath(picPath);
            res.add(image);
        }
        return res;
    }

    //获取酒店房间数据
    public static HashMap getRoomByHotelId(String hotelId, String cityPy){
        HashMap res = new HashMap();
        String url = "https://hotels.ctrip.com/international/Tool/AjaxHotelRoomInfoDetailPart.aspx?Hotel="+hotelId+"&StartDate=2019-06-18&DepDate=2019-06-19";
        //酒店详情页面
        Connection.Response detailDocument = null;
        try {
            detailDocument = Jsoup.connect(url).userAgent(agent).header("Accept", "*/*")
                    .header("Content-Type", "application/json;charset=utf-8").timeout(timeout).ignoreContentType(Boolean.TRUE).execute();
        } catch (IOException e) {
            log.warn("url not connection error:  url :"+url);
        }
        String json = detailDocument.body();
        JSONObject jsonObj = JSONObject.parseObject(json);
        //获取
        JSONObject rooms = (JSONObject)jsonObj.get("HotelRoomData");

        if(rooms == null){
            log.warn("hotel id:" + hotelId + "获取不到房间数据");
        }else{
            JSONArray roomList = (JSONArray)rooms.get("roomList");
            List<HotelBasicRoom> basicRooms = new ArrayList<HotelBasicRoom>();
            List<HotelRoomImage> roomImages = new ArrayList<HotelRoomImage>();
            for (Object o: roomList){
                JSONObject obj = (JSONObject)o;
                JSONObject detail = (JSONObject) obj.get("roomInfoDetails");
                HotelBasicRoom basicRoom = new HotelBasicRoom();
                basicRoom.setHotelId(hotelId);
                basicRoom.setBasicRoomId(obj.getString("id"));
                basicRoom.setCnName(obj.getString("name"));
                basicRoom.setAddBed((Boolean) detail.get("canAddRoom"));
                String roomArea = detail.getJSONObject("roomDetails").getString("roomArea");
                if(roomArea != null){
                    basicRoom.setRoomArea(roomArea.replaceAll("平方米",""));
                }
                try {
                    JSONArray facilityTypes = detail.getJSONObject("FacilitiesOutput").getJSONArray("FacilityTypes");
                    for (Object obj1 : facilityTypes){
                        JSONObject facilityType = (JSONObject)obj1;
                        String typeName = (String)facilityType.get("TypeName");
                        if(typeName.indexOf("房间介绍") >-1){
                            basicRoom.setIntro(facilityType.getJSONArray("FacilityName").toJSONString());
                            break;
                        }
                    }
                    basicRoom.setFacilities(detail.getJSONObject("FacilitiesOutput").getJSONArray("FacilityTypes").toJSONString());
                }catch (Exception e1){}
                basicRooms.add(basicRoom);

                //获取房间图片数据
                JSONArray bigImages = detail.getJSONArray("bigImages");
                for (Object im: bigImages){
                    String picPath = downImages(hotelRoomImageRootDir+cityPy+File.separator+hotelId, "http:"+(String)im);//酒店图片
                    HotelRoomImage roomImage = new HotelRoomImage();
                    roomImage.setBasicRoomId(basicRoom.getBasicRoomId());
                    roomImage.setImagePath(picPath);
                    roomImage.setHotelId(hotelId);
                    roomImages.add(roomImage);
                }
            }

            JSONArray subRoomList = (JSONArray)rooms.get("subRoomList");
            List<HotelSubRoom> subRooms = new ArrayList<HotelSubRoom>();
            for (Object o: subRoomList){
                JSONObject obj = (JSONObject)o;
                HotelSubRoom subRoom = new HotelSubRoom();
                subRoom.setBasicRoomId(obj.getString("baseRoomId"));
                subRoom.setBedType(obj.getIntValue("bedType"));
                String name = obj.getString("name");
                subRoom.setCnName(Jsoup.parse(name).body().text());
                subRoom.setMaxPerson(obj.getIntValue("maxPerson"));
                subRoom.setRoomId(obj.getString("id"));
                subRoom.setHotelId(hotelId);
                subRooms.add(subRoom);
            }
            res.put("basicRooms", basicRooms);
            res.put("subRooms", subRooms);
            res.put("roomImages", roomImages);
        }
        return res;
    }

    //获取房间图片数据
    private static String downImages(String filePath, String imgUrl) {
        // 若指定文件夹没有，则先创建
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            // 因此要将加号转化为UTF-8格式的%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        File file = new File(filePath + File.separator + fileName);
        try {
            // 获取图片URL
            URL url = new URL(imgUrl);
            // 获得连接
             URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            log.warn("FileNotFoundException    file："+filePath+"    URL:"+imgUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath().substring(9);
    }
}
