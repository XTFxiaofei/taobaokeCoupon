package com.example.demo.utils.api;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HttpClient封装工具类
 *
 * @author valord577
 * @date 18-8-21 下午5:22
 */
public class Https {

    /**
     * 编码格式 发送编码格式统一用 utf-8
     */
    private static final String ENCODING = "UTF-8";

    /**
     * 设置连接超时时间 单位 毫秒(ms)
     */
    private static final int CONNECT_TIMEOUT = 6000;

    /**
     * 请求获取数据的超时时间(即响应时间) 单位 毫秒(ms)
     */
    private static final int SOCKET_TIMEOUT = 6000;

    /**
     * x-www-form-urlencoded
     * Content-Type: application/x-www-form-urlencoded;charset=utf-8
     */
    private static final int X_WWW_FORM_URLENCODED = 0;

    /**
     * raw-json
     * Content-Type: application/json;charset=utf-8
     */
    private static final int RAW_JSON = 1;

    /**
     * form-data
     */
    private static final int FORM_DATA = 2;

    /**
     * get请求方法 不带任何
     *
     * @param url 请求地址
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * get请求方法 带请求参数
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofGet(String url, Map<String, String> headers) throws Exception {
        return doGet(url, headers, null);
    }

    /**
     * get请求方法 带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param object  请求参数(实体类)
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofGet(String url, Map<String, String> headers, Object object) throws Exception {
        Map<String, String> params = ToMaps.convertToMap(object);
        return doGet(url, headers, params);
    }

    /**
     * get请求方法 带请求头和请求参数 x-www-form-urlencoded
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  请求参数
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        return doGet(url, headers, params);
    }

    /**
     * get请求方法 带请求头和请求参数 具体实现
     *
     * @param url         请求地址
     * @param headers     请求头
     * @param queryParams 请求参数
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    private static HttpResult doGet(String url, Map<String, String> headers, Map<String, String> queryParams) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = packQueryParams(url, queryParams);

        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        // 设置超时
        RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(config);

        // 设置请求头
        packHeader(headers, httpGet);

        // 执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            // 执行请求并获得响应结果
            return getHttpResult(response);
        } finally {
            // 释放资源
            release(httpClient, response);
        }

    }

    /**
     * post请求方法 带请求参数 application/json
     *
     * @param url         请求地址
     * @param bodyString  请求参数
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofPost(String url, String bodyString) throws Exception {
        return doPost(url, null, null, null, bodyString, RAW_JSON);
    }

    /**
     * post请求方法 带请求头和请求参数 application/json
     *
     * @param url         请求地址
     * @param headers     请求头
     * @param queryParams 请求参数
     * @param bodyString  请求参数
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofPost(String url, Map<String, String> headers, Map<String, String> queryParams, String bodyString) throws Exception {
        return doPost(url, headers, queryParams, null, bodyString, RAW_JSON);
    }

    /**
     * post请求方法 带请求参数 x-www-form-urlencoded
     *
     * @param url         请求地址
     * @param bodyParams  请求参数
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofPost(String url, Map<String, String> bodyParams) throws Exception {
        return doPost(url, null, null, bodyParams, null, X_WWW_FORM_URLENCODED);
    }

    /**
     * post请求方法 带请求头和请求参数 x-www-form-urlencoded
     *
     * @param url         请求地址
     * @param headers     请求头
     * @param queryParams 请求参数
     * @param bodyParams  请求参数
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    public static HttpResult ofPost(String url, Map<String, String> headers, Map<String, String> queryParams, Map<String, String> bodyParams) throws Exception {
        return doPost(url, headers, queryParams, bodyParams, null, X_WWW_FORM_URLENCODED);
    }

    /**
     * post请求方法 带请求头和请求参数 具体实现
     *
     * @param url         请求地址
     * @param headers     请求头
     * @param queryParams 请求参数
     * @param bodyParams  请求参数
     * @param bodyString  请求参数
     * @param contentType 请求方式
     * @return 请求结果
     * @throws Exception 抛出异常
     */
    private static HttpResult doPost(String url, Map<String, String> headers, Map<String, String> queryParams, Map<String, String> bodyParams, String bodyString, int contentType) throws Exception {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = packQueryParams(url, queryParams);

        // 创建http对象
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        // 设置超时
        RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(config);

        // 设置请求头
        packHeader(headers, httpPost);

        // 封装请求参数
        if (X_WWW_FORM_URLENCODED == contentType) {
            packBodyParams(bodyParams, httpPost);
        } else if (RAW_JSON == contentType) {
            packBodyParams(bodyString, httpPost);
        }

        // 执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            // 执行请求并获得响应结果
            return getHttpResult(response);
        } finally {
            // 释放资源
            release(httpClient, response);
        }

    }

    /**
     * 设置请求头
     *
     * @param headers    请求头参数
     * @param httpMethod 需要设置请求头的http请求
     */
    private static void packHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
        // 设置请求头
        if (null != headers) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置请求头到 HttpRequestBase
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 创建访问的地址 拼接url
     *
     * @param url         基础url
     * @param queryParams query参数
     * @return URIBuilder
     * @throws Exception 抛出异常
     */
    private static URIBuilder packQueryParams(String url, Map<String, String> queryParams) throws Exception {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (null != queryParams) {
            Set<Map.Entry<String, String>> entrySet = queryParams.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return uriBuilder;
    }

    /**
     * 封装 x-www-form-urlencoded 请求参数
     *
     * @param bodyParams 请求参数
     * @param httpMethod 请求方法
     * @throws Exception 抛出异常
     */
    private static void packBodyParams(Map<String, String> bodyParams, HttpEntityEnclosingRequestBase httpMethod) throws Exception {
        if (null != bodyParams) {
            List<NameValuePair> nvps = new ArrayList<>();
            Set<Map.Entry<String, String>> entrySet = bodyParams.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置到请求的http对象中
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, ENCODING);
            entity.setContentType("application/x-www-form-urlencoded");
            httpMethod.setEntity(entity);
        }
    }

    /**
     * 封装 raw-json 请求参数
     *
     * @param bodyString 请求参数
     * @param httpMethod 请求方法
     * @throws Exception 抛出异常
     */
    private static void packBodyParams(String bodyString, HttpEntityEnclosingRequestBase httpMethod) throws Exception {
        if (null != bodyString) {
            StringEntity entity = new StringEntity(bodyString, ENCODING);
            entity.setContentType("application/json");

            //设置到请求的http对象中
            httpMethod.setEntity(entity);
        }
    }

    /**
     * 获取响应结果
     *
     * @param response CloseableHttpResponse
     * @return 返回响应结果
     * @throws Exception 抛出异常
     */
    private static HttpResult getHttpResult(CloseableHttpResponse response) throws Exception {
        // 获取返回结果
        if (null != response && null != response.getStatusLine()) {
            String content = "";
            if (null != response.getEntity()) {
                content = EntityUtils.toString(response.getEntity(), ENCODING);
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), content);
        }
        return new HttpResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * 释放资源
     *
     * @param httpClient CloseableHttpClient
     * @param response   CloseableHttpResponse
     * @throws IOException 抛出异常
     */
    private static void release(CloseableHttpClient httpClient, CloseableHttpResponse response) throws IOException {
        // 释放资源
        if (null != httpClient) {
            httpClient.close();
        }

        if (null != response) {
            response.close();
        }
    }
}
