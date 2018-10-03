package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

/**
 * @author valor
 * @date 2018/9/9 21:15
 */
@Slf4j
public class URLs {

    private static final String QUERY = "?";

    /**
     * url 解析工具类
     * @param url url
     * @return query参数
     */
    public static MultiMap<String> urlParser(String url) {
        if (!StringUtils.contains(url, QUERY)) {
            log.error("\"" + url + "\"" + " => no query params.");
            return null;
//            throw new RuntimeException("\"" + url + "\"" + " => no query params.");
        }

        String[] split = StringUtils.split(url, QUERY);
        if (2 > split.length) {
            log.error("\"" + url + "\"" + " => no query params.");
            return null;
//            throw new RuntimeException("\"" + url + "\"" + " => no query params.");
        }

        MultiMap<String> values = new MultiMap<>();
        UrlEncoded.decodeTo(split[1], values, "UTF-8");
        return values;
    }
}
