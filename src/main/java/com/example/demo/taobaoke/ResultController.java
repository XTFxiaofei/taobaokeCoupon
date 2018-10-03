package com.example.demo.taobaoke;

import com.example.demo.utils.GetGoodsId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/result")
public class ResultController {

    private static final Logger LOGGER=LoggerFactory.getLogger(ResultController.class);

    @RequestMapping(value="/coupon",method = RequestMethod.GET)
    public String getGoodsJson(String goods) {

        String string=null;
        String goodsId=GetGoodsId.getResult(goods);
        LOGGER.info("查询优惠券:"+PageUtils.httpGet("https://pub.alimama.com/common/code/getAuctionCode.json?auctionid="+goodsId+"&adzoneid=129200277&siteid=208910108&scenes=1"));
        // 如果不是长链接 则用浏览器打开短链接 再解析
        string=PageUtils.httpGet("https://pub.alimama.com/common/code/getAuctionCode.json?auctionid="+goodsId+"&adzoneid=129200277&siteid=208910108&scenes=1");
        return string;
    }
}