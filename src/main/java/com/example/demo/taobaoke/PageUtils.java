package com.example.demo.taobaoke;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Random;
import java.util.Set;

public class PageUtils {
    private static final String BLANK = "about:blank";
    /**
     * 各种常量
     */
    private static final String[] TB_DOMAIN = {"item.taobao", "detail.tmall"};

    public static PhantomJSDriver driver = null;
    public static String PhantomPath = "D:/idea/taobaokeDemo/phantomjs/phantomjs.exe";
    //页面中的cookie
    public static Set<Cookie> cookies = null;
    //页面中的token
    public static String token = null;
    // 初始化phantom先关的操作
    static {
    // 设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
     // ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
    // 截屏支持
        dcaps.setCapability("takesScreenshot", true);
    // css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
    // js支持
        dcaps.setJavascriptEnabled(true);
    // 驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PhantomPath);
    // 创建无界面浏览器对象
        driver = new PhantomJSDriver(dcaps);
    }

    public static boolean loadLogin(String loginUrl, String mainUrl) throws Exception {
        try {
            driver.get(loginUrl);
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File file = new File("almamaLogin.png");
            FileUtils.copyFile(scrFile, file);
            System.out.println("二维码已经保存为:" + file.getAbsolutePath());
            System.out.println("请打开淘宝扫描该图片登录");
            String current = "";
            while (!current.equals(mainUrl)) {
                current = driver.getCurrentUrl();
                Thread.sleep(2000);
            }
            System.out.println("登录成功");
            return true;
        } catch (Exception e) {
     //当出现异常时关闭
     //driver.close();
    //driver.quit();
            return false;
        }
    }

    //开启一个线程，定时访问页面，防止页面太久没有操作导致session超时
    public static void keepCookies() { new Thread() {
            @Override
            public void run() {
   //淘宝联盟页面
                String url = "http://pub.alimama.com/myunion.htm";
                while (true) {
                    try {
                        driver.get(url);
                        cookies = driver.manage().getCookies();
                        Thread.sleep(8 * 1000);
                    } catch (InterruptedException e) {
                    }
                }
            };
        }.start();
    }

    //获取token
    public static String getToken() {
   //等待cookie中有数据
        while (cookies == null) {
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Cookie cook : cookies) {
            String name = cook.getName();
            if (name != null && name.equals("_tb_token_")) {
                return token = cook.getValue();
            }
        }
        return token;
    }

    /**
     * 封装get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        try {
            //获取cookie字符串
            String cookieStr = "";
            for (Cookie cookie : cookies) {
                cookieStr += cookie.getName() + "=" + cookie.getValue() + ";";
            }
           // 根据地址获取请求
            HttpGet request = new HttpGet(url);//这里发送get请求
            //添加请求头
            request.addHeader("Pragma", "no-cache");
            //设置浏览器类型
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/538.1 (KHTML, like Gecko) PhantomJS/2.1.1 Safari/538.1");
            request.addHeader("Accept-Language", "zh-CN,en,*");
            //设置cookie
            request.addHeader("Cookie", cookieStr);
            // 获取当前客户端对象
            HttpClient httpClient = HttpClients.custom().build();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 封装post请求
     *
     * @param url
     * @return
     */
    public static String httpPost(String url) {
        try {
   //获取cookie字符串
            String cookieStr = "";
            for (Cookie cookie : cookies) {
                cookieStr += cookie.getName() + "=" + cookie.getValue() + ";";
            }
    // 根据地址获取请求
            HttpPost request = new HttpPost(url);//这里发送get请求
            //添加请求头
            request.addHeader("Pragma", "no-cache");
            //设置浏览器类型
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/538.1 (KHTML, like Gecko) PhantomJS/2.1.1 Safari/538.1");
            request.addHeader("Accept-Language", "zh-CN,en,*");
            //设置cookie
            request.addHeader("Cookie", cookieStr);
            // 获取当前客户端对象
            HttpClient httpClient = HttpClients.custom().build();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取导购推广位
     * @param pvid
     * @param token
     * @return
     */
    public static String getShoppingGuide(String pvid, String token) {
        String url = "https://pub.alimama.com/common/adzone/adzoneManage.json?"
                + "spm=a219t.7900221/1.1998910419.dbb742793.5bcb8cdcYsmdCb&"
                + "tab=3&"
                + "toPage=1&"
                + "perPageSize=40&"
                + "gcid=8&"
                + "t=" + getCirrentTime() + "&"
                + "pvid=" + pvid + "&"
    //"pvid=60_59.172.110.203_862_1517744052508&"
                + "_tb_token_=" + token + "&"
                + "_input_charset=utf-8";
        return httpGet(url);
    }

    //获取当前的时间戳
    public static long getCirrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取1000以内的随机数
     *
     * @return
     */
    public static int getRandom() {
        Random random = new Random();
        int result = (int) (random.nextFloat() * 1000);
        return result;
    }


   /* *//**
     * 新建标签页 并打开网站
     * @return 返回新标签页的句柄
     *//*
    public static String openTabAndFocus(String url) {
        String js = "window.open('" + BLANK + "', '_blank');";
        driver.executeScript(js);
        String newHandle = getAllHandles()[1].toString();
        driver.switchTo().window(newHandle);
        if (driver.getCurrentUrl().startsWith(BLANK)) {
            driver.get(url);
            return newHandle;
        }

        for (Object o : getAllHandles()) {
            driver.switchTo().window(o.toString());
            if (driver.getCurrentUrl().startsWith(BLANK)) {
                driver.get(url);
                return o.toString();
            }
        }
        throw new RuntimeException("未找到新建的空白标签页..");
    }
    *//**
     * @return 获取浏览器所有句柄
     *//*
    public static Object[] getAllHandles() {
        return driver.getWindowHandles().toArray();
    }

    *//**
     * 获取淘宝扫描结果 默认10秒超时 每200ms扫描一次
     *//*
    public static boolean scanTbUrl() {
        return scanUrl(TB_DOMAIN, 10L, 200L);
    }

    *//**
     * 获取自定义扫描结果 默认10秒超时 每200ms扫描一次
     *//*
    public static boolean scanUrl(String[] target) {
        return scanUrl(target, 60L, 200L);
    }

    *//**
     * 获取自定义扫描结果
     *//*
    public static boolean scanUrl(String[] target, long timeOutInSeconds, long sleepTimeOut) {
        long p = timeOutInSeconds * 10000 / sleepTimeOut;
        for (int i = 0; i < p + 1; i++) {
            if (p == i) {
                throw new RuntimeException("等待url转跳时 超时...");
            }
            for (int q = 0; q < target.length; q++) {
                if (StringUtils.contains(getCurrentUrl(), target[q])) {
                    return true;
                }
            }
            try {
                Thread.sleep(sleepTimeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    *//**
     * @return 获取当前标签页的url
     *//*
    public static String getCurrentUrl() {
        return driver.getCurrentUrl();
    }*/
}
