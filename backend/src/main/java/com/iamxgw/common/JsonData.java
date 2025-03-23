package com.iamxgw.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author IamXGW
 *@since 2024-07-09 22:32
 */
@Getter
@Setter
public class JsonData {

    private final static int SYSTEM_ERROR = 1;
    private boolean ret;

    private String msg;

    private Object data;

    private int code = 0;

    public JsonData(boolean ret) {
        this.ret = ret;
    }

    public static JsonData success() {
        return new JsonData(true);
    }

    public static JsonData success(Object data) {
        JsonData jsonData = new JsonData(true);
        jsonData.setData(data);
        return jsonData;
    }

    public static JsonData success(Object data, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.setData(data);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.setCode(SYSTEM_ERROR);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static JsonData fail(int code, String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.setCode(code);
        jsonData.setMsg(msg);
        return jsonData;
    }
}