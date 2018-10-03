package com.example.demo.utils.api;

import lombok.Data;

/**
 * Http请求的响应结果
 *
 * @author valord577
 * @date 18-8-21 下午5:39
 */
@Data
public class HttpResult {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;

    public HttpResult() {
    }

    public HttpResult(int code) {
        this.code = code;
    }

    public HttpResult(String content) {
        this.content = content;
    }

    public HttpResult(int code, String content) {
        this.code = code;
        this.content = content;
    }

}
