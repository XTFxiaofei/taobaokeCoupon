package com.example.demo.utils.api;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author valor
 * @date 2018/9/9 23:24
 */
@Slf4j
public class Alimama {



    private static final String AUCTION_CODE = "https://pub.alimama.com/common/code/getAuctionCode.json";

    private static final String TKL_PARSER = "http://api.chaozhi.hk/tb/tklParse";


    /**
     * @param taoToken 淘口令
     * @return 将淘口令转换成长链接
     */
    public static String tklParser(String taoToken) {
        Map<String, String> map = new HashMap<>(4);
        map.put("tkl", taoToken);

        try {
            HttpResult result = Https.ofPost(TKL_PARSER, map);
            log.info("httpResult => " + result);
            if (200 == result.getCode() && null != result.getContent()) {
                TklCode tklCode = JSON.parseObject(result.getContent(), TklCode.class);
                if (0 == tklCode.getError_code() && null != tklCode.getData()) {
                    TklCode.TklUrl tklUrl = JSON.parseObject(JSON.toJSONString(tklCode.getData()), TklCode.TklUrl.class);
                    if (tklUrl.isSuc()) {
                        return tklUrl.getUrl();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 表示没有请求到数据
        return null;
    }

}
