package com.xzh.personalproject.commons.utils;

public class JsonResult<T> {
    private String msg;
    private boolean success;
    private T data;

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public JsonResult(String msg, boolean success, T data) {
        super();
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public JsonResult() {
        super();
    }
}
