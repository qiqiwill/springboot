package com.test.crawler.util;

import java.util.HashMap;

/**
 * @Author: LIUQI
 * @Date: 2019/5/6 10:49
 * @Description: TODO
 */
public class OverseasCountryUtil {

    private static HashMap<String,String> map = new HashMap<String, String>();
    static {
        map.put("泰国","Thailand");
        map.put("美国","U.S.A");
        map.put("英国","Britain");
        map.put("日本","Japan");
        map.put("意大利","Italy");
        map.put("法国","France");
        map.put("西班牙","Spain");
        map.put("葡萄牙","Portugal");
        map.put("德国","Germany");
        map.put("丹麦","Denmark");
        map.put("荷兰","the Netherlands");
        map.put("澳大利亚","Australia");
        map.put("新西兰","New Zealand");
        map.put("新加坡","Singapore");
        map.put("印度","India");
        map.put("韩国","South Korea");
        map.put("马来西亚","Malaysia");
        map.put("菲律宾","the Philippines");
        map.put("印度尼西亚","Indonesia");
        map.put("阿联酋","The United Arab Emirates");
        map.put("马尔代夫","Maldives");
        map.put("毛里求斯","Mauritius");
        map.put("越南","Vietnam");
        map.put("柬埔寨","Cambodia");
    }

    //根据中文名查找中午名
    public static String getEnByCn(String key){
        return map.get(key);
    }


}
