package com.example.demo;

import com.example.demo.taobaoke.PageUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
        try {
            PageUtils.loadLogin("https://login.taobao.com/member/login.jhtml?style=mini&newMini2=true&css_style=alimama_index&from=alimama&redirectURL=http://www.alimama.com&full_redirect=true&disableQuickLogin=true",
                    "https://www.alimama.com/index.htm");
            PageUtils.keepCookies();
            String token = PageUtils.getToken();
            String result = PageUtils.httpGet("http://pub.alimama.com/common/getUnionPubContextInfo.json");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
