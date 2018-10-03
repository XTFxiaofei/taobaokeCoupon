package com.example.demo.taobaoke;

public class LoginInfo {
    private String invalidKey;
    private boolean ok;
    private Data data;
    private Info info;

    public String getInvalidKey() {
        return invalidKey;
    }

    public void setInvalidKey(String invalidKey) {
        this.invalidKey = invalidKey;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
