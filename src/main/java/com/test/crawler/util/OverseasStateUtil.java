package com.test.crawler.util;

import java.util.HashMap;

/**
 * @Author: LIUQI
 * @Date: 2019/5/5 10:35
 * @Description: todo
 */
public class OverseasStateUtil {

    private static final String[] asia = new String[]{"普吉岛","曼谷","新加坡","东京","大阪","清迈","巴厘岛","吉隆坡","芭提雅","哥打京那巴鲁","京都","那霸","苏梅岛","甲米","胡志明市","迪拜"};
    private static final String[] europe = new String[]{"巴黎","伦敦","米兰","法兰克福","罗马","巴塞罗那","慕尼黑","莫斯科","阿姆斯特丹","马德里","佛罗伦萨","威尼斯","布拉格","维也纳","柏林","苏黎世","雅典","爱丁堡"};
    private static final String[] america = new String[]{"洛杉矶","拉斯维加斯","纽约","旧金山","温哥华","塞班岛","芝加哥","多伦多","波士顿","圣地亚哥","奥兰多","华盛顿","西雅图","关岛","迈阿密","蒙特雷县","佩吉"};
    private static final String[] oceania = new String[]{"悉尼","墨尔本","奥克兰","黄金海岸","凯恩斯","布里斯班","皇后镇","基督城","阿德莱德","珀斯","特卡波湖","阿波罗湾","坎贝尔港","霍巴特","罗托鲁瓦","堪培拉","达尼丁","惠灵顿"};
    private static final String[] africa =  new String[]{"毛里求斯","开罗","约翰内斯堡","开普敦","内罗毕","马埃岛","普拉兰岛","亚的斯亚贝巴","卡萨布兰卡","卢克索","达累斯萨拉姆","马拉喀什","德班","蒙巴萨","阿克拉","阿斯旺","沙姆沙伊赫"};
    private static final String[] island = new String[]{"马尔代夫","冲绳","皮皮岛","长滩岛","兰卡威","民丹岛","象岛","热浪岛","龟岛","丽贝岛"};

    private static final HashMap map = new HashMap();

    private static final HashMap enmap = new HashMap();

    static {
          for (String s: asia){
              map.put(s,"亚洲");
          }
          for (String s: europe){
              map.put(s, "欧洲");
          }
          for (String s: america){
              map.put(s, "美洲");
          }
        for (String s: oceania){
            map.put(s, "大洋洲");
        }
        for (String s: africa){
            map.put(s, "非洲");
        }
        for (String s: island){
            map.put(s, "海岛");
        }

        enmap.put("亚洲", "asia");
        enmap.put("欧洲", "europe");
        enmap.put("欧洲", "america");
        enmap.put("大洋洲", "oceania");
        enmap.put("非洲", "africa");
        enmap.put("海岛", "island");
    }

    public static String getState(String key){
        return (String) map.get(key);
    }

    public static String getEnStateByCn(String key){
        return (String) enmap.get(key);
    }

}
