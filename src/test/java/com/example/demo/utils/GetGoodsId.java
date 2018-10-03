package com.example.demo.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.MultiMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:fly
 * @Description: 获取优惠券的接口。
 * @Date:0:55 2018/10/1
 */
@Slf4j
@RestController
@RequestMapping(value="/result")
public class GetGoodsId {

    private final String tbDomain = "item.taobao";
    private final String tmDomain = "detail.tmall";

    @RequestMapping(value="/coupon",method = RequestMethod.GET)
    public String getResult(String goods) {
        try {
            //String o="【鸿星尔克板鞋男鞋学生鞋子男子2018新款滑板运动鞋休闲鞋小白鞋男】http://m.tb.cn/h.3hXLXKr 点击链接，再选择浏览器咑閞；或復·制这段描述￥Ij7bbfwfPDX￥后到淘♂寳♀";
            //log.info("客户分享的链接是:"+o);
            //goods="【沐浴露男士持久留香香体清爽古龙香水全身沫浴家庭装洗发水浴套装】http://m.tb.cn/h.36RL1H4 点击链接，再选择浏览器咑閞；或復·制这段描述￥hbV3bf406Qr￥后到淘寳";
            String id = "";

            log.info("用户输入 => " + goods);

            Pattern domain = Patterns.DOMAIN_NAME;

            // 查找用户输入文字中的url
            Pattern webUrl = Patterns.WEB_URL;
            Matcher matcher = webUrl.matcher(goods);
            if (matcher.find()) {
                String url = matcher.group(0);
                log.info("找到包含到链接 => " + url);
                // 判断域名是否为 item.taobao.com / detail.tmall.com
                matcher = domain.matcher(url);
                if (matcher.find()) {
                    if (StringUtils.contains(matcher.group(0), tbDomain) || StringUtils.contains(matcher.group(0), tmDomain)) {
                        // 直接进行url解析 若有id就不必使用selenium
                        MultiMap<String> params = URLs.urlParser(url);
                        if (null != params) {
                            List<String> values = params.get("id");
                            if (CollectionUtils.isNotEmpty(values)) {
                                id = values.get(0);
                                log.info("id => " + id);
                                return id;
                            }
                        }
                    }
                }

                if (StringUtils.isBlank(id)) {
                    log.info("flag: 用户输入的不包含长链接");
                    // 如果不是长链接 则用浏览器打开短链接 再解析
                    Chromes.openTabAndFocus(url);
                    if (Chromes.scanTbUrl()) {
                        url = Chromes.getCurrentUrl();
                        log.info("解析的长链接2 => " + url);
                        // 判断域名
                        matcher = domain.matcher(url);
                        if (matcher.find()) {
                            if (StringUtils.contains(matcher.group(0), tbDomain) || StringUtils.contains(matcher.group(0), tmDomain)) {
                                // 直接进行url解析
                                MultiMap<String> params = URLs.urlParser(url);
                                if (null != params) {
                                    List<String> values = params.get("id");
                                    if (CollectionUtils.isNotEmpty(values)) {
                                        id = values.get(0);
                                        log.info("id => " + id);
                                        return id;
                                    }
                                }
                            }
                        }
                    }
                    Chromes.close();
                }
            }

          /*  if (StringUtils.isBlank(id)) {
                log.info("flag: 用户输入的不包含任何链接");
                // 尝试截取淘口令
                String s = StringUtils.substringBetween(goods, "€");
                if (StringUtils.isNotBlank(s)) {
                    String taoToken = "€" + s + "€";
                    log.info("淘口令 => " + taoToken);
                    String url = Alimama.tklParser(taoToken);
                    log.info("淘口令转长链接 => " + url);

                    if (StringUtils.isNotBlank(url)) {
                        Chromes.openTabAndFocus(url);
                        if (Chromes.scanTbUrl()) {
                            url = Chromes.getCurrentUrl();
                            log.info("解析的长链接 => " + url);
                            // 判断域名
                            matcher = domain.matcher(url);
                            if (matcher.find()) {
                                if (StringUtils.contains(matcher.group(0), tbDomain) || StringUtils.contains(matcher.group(0), tmDomain)) {
                                    // 直接进行url解析
                                    MultiMap<String> params = URLs.urlParser(url);
                                    if (null != params) {
                                        List<String> values = params.get("id");
                                        if (CollectionUtils.isNotEmpty(values)) {
                                            id = values.get(0);
                                            log.info("id => " + id);
                                        }
                                    }
                                }
                            }
                        }
                        Chromes.close();
                    } else {
                        GeneralResult<String> data = new GeneralResult<>();
                        data.setSuccess(false);
                        data.setValue("请输入正确的淘口令");

                        DeferredResult<String> result = new DeferredResult<>();
                        result.setResult(JSON.toJSONString(data));
                        return result;
                    }
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            Chromes.close();
        }
        return null;
    }
}
