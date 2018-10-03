package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * 缓存一个浏览器
 * 用于获取淘宝pc端url
 *
 * @author valor
 * @date 2018/9/7 13:03
 */
public class Chromes {

    /**
     * 各种常量
     */
    private static final String[] TB_DOMAIN = {"item.taobao", "detail.tmall"};

    private static final String BLANK = "about:blank";


    private static final String DRIVER_PATH = "/idea/taobaoke/ChromeDriver/chromedriver.exe";
    private static final String HEADLESS = "--headless";
    private static final String NO_SANDBOX = "--no-sandbox";
    private static final String NO_EXTENSIONS = "--disable-extensions";
    private static final String NO_DEV_SHM_USAGE = "--disable-dev-shm-usage";

    /**
     * selenium驱动的chrome浏览器
     */
    private volatile static ChromeDriver browser = null;

    /**
     * selenium 定时扫描器
     */
    private static WebDriverWait wait = null;

    /**
     * 私有构造 外部不可new
     */
    private Chromes() {

    }

    private static ChromeDriver getBrowser() {
        if (null == browser) {
            synchronized (ChromeDriver.class) {
                if (null == browser) {
                    System.setProperty("webdriver.chrome.driver", DRIVER_PATH);

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments(HEADLESS);
                    options.addArguments(NO_SANDBOX);
                    options.addArguments(NO_EXTENSIONS);
                    options.addArguments(NO_DEV_SHM_USAGE);

                    browser = new ChromeDriver(options);
                }
            }
        }

        return browser;
    }

    /**
     * 在当前标签页打开网页
     */
    public static void get(String url) {
        getBrowser().get(url);
    }

    /**
     * @return 获取当前标签页的url
     */
    public static String getCurrentUrl() {
        return getBrowser().getCurrentUrl();
    }

    /**
     * 查找元素
     */
    public static WebElement findElementById(String using) {
        return getBrowser().findElementById(using);
    }

    public static WebElement findElementByXPath(String using) {
        return getBrowser().findElementByXPath(using);
    }

    /**
     * 获取动作链
     */
    public static Actions actions() {
        return new Actions(getBrowser());
    }

    /**
     * @return 获取当前页面的cookies
     */
    public static Set<Cookie> getCookies() {
        return getBrowser().manage().getCookies();
    }

    /**
     * 关闭标签页
     * 若为默认标签页 则不操作
     */
    public static void close() {
        if (!StringUtils.equals(getDefaultHandle(), getBrowser().getWindowHandle())) {
            getBrowser().close();
            focusDefaultTab();
        }
    }

    /**
     * @return 获取扫描器实例
     */
    public static WebDriverWait getWait() {
        if (null == wait) {
            wait = new WebDriverWait(getBrowser(), 10);
        }
        return wait;
    }

    /**
     * 新建标签页 并打开网站
     * @return 返回新标签页的句柄
     */
    public static String openTabAndFocus(String url) {
        String js = "window.open('" + BLANK + "', '_blank');";
        getBrowser().executeScript(js);
        String newHandle = getAllHandles()[1].toString();
        getBrowser().switchTo().window(newHandle);
        if (getBrowser().getCurrentUrl().startsWith(BLANK)) {
            getBrowser().get(url);
            return newHandle;
        }

        for (Object o : getAllHandles()) {
            getBrowser().switchTo().window(o.toString());
            if (getBrowser().getCurrentUrl().startsWith(BLANK)) {
                getBrowser().get(url);
                return o.toString();
            }
        }

        throw new RuntimeException("未找到新建的空白标签页..");
    }

    /**
     * 将浏览器聚焦到默认标签页
     */
    public static void focusDefaultTab() {
        getBrowser().switchTo().window(getDefaultHandle());
    }

    /**
     * @return 当前标签页的句柄
     */
    public static String getCurrentHandle() {
        return getBrowser().getWindowHandle();
    }

    /**
     * @return 默认标签页的句柄
     */
    public static String getDefaultHandle() {
        return getAllHandles()[0].toString();
    }

    /**
     * @return 获取浏览器所有句柄
     */
    public static Object[] getAllHandles() {
        return getBrowser().getWindowHandles().toArray();
    }

    /**
     * 获取淘宝扫描结果 默认10秒超时 每200ms扫描一次
     */
    public static boolean scanTbUrl() {
        return scanUrl(TB_DOMAIN, 10L, 200L);
    }

    /**
     * 获取自定义扫描结果 默认10秒超时 每200ms扫描一次
     */
    public static boolean scanUrl(String[] target) {
        return scanUrl(target, 60L, 200L);
    }

    /**
     * 获取自定义扫描结果
     */
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
}
