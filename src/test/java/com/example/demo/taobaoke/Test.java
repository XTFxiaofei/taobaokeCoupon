package com.example.demo.taobaoke;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Test {
    private static final Logger LOGGER=LoggerFactory.getLogger(ResultController.class);
    public static Gson gson = new Gson();

    public static void main(String[] args) {
        //阿里妈妈录页面
        String taobao = "https://login.taobao.com/member/login.jhtml?style=mini&newMini2=true&css_style=alimama_index&from=alimama&redirectURL=http://www.alimama.com&full_redirect=true&disableQuickLogin=true";
       //阿里妈妈主页面
        String loginUrl = "https://www.alimama.com/index.htm";
       //获取个人信息的接口
        String userUrl = "http://pub.alimama.com/common/getUnionPubContextInfo.json";
        try {
            PageUtils.loadLogin(taobao, loginUrl);
            PageUtils.keepCookies();
            String token = PageUtils.getToken();
           // System.out.println("token:" + token);
            String result = PageUtils.httpGet(userUrl);
            LoginInfo info = gson.fromJson(result, LoginInfo.class);
            //System.out.println("获取个人信息:" + info);
           //格式60_59.172.110.203_862_1517744052508
            String pvid = "60"
                    + "_" + info.getData().getIp()
                    + "_" + PageUtils.getRandom()
                    + "_" + PageUtils.getCirrentTime();
           //获取导购推广位
            String shopping = PageUtils.getShoppingGuide(pvid, PageUtils.getToken());
           // System.out.println("导购位信息:" + shopping);
            LOGGER.info("查询优惠券:"+PageUtils.httpGet("https://pub.alimama.com/common/code/getAuctionCode.json?auctionid=575345685215&adzoneid=129200277&siteid=208910108&scenes=1"));
            // 如果不是长链接 则用浏览器打开短链接 再解析
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}