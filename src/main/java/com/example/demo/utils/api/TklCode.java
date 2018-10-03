package com.example.demo.utils.api;

import lombok.Data;

/**
 * @author valor
 * @date 2018/9/10 13:34
 */
@Data
public class TklCode {

    private int error_code;

    private String msg;

    private TklUrl data;

    @Data
    public static class TklUrl {

        private String content;

        private boolean suc;

        private String url;
    }
}
