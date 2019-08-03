package com.ls.lishuai.exception;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/31 20:59
 */
public class ApiStatus {

    public static final Integer OK = 200;

    private boolean result;
    private int statusCode;
    private String message;
    private Object info;

    public static ApiStatus newOkApi() {
        ApiStatus apiStatus = new ApiStatus();
        apiStatus.setResult(true);
        apiStatus.setStatusCode(OK);
        apiStatus.setMessage("ok");
        return apiStatus;
    }






    public static ApiStatus newOkApi(String message, Object info) {
        ApiStatus apiStatus = new ApiStatus();
        apiStatus.setResult(true);
        apiStatus.setStatusCode(OK);
        apiStatus.setMessage(message);
        apiStatus.setInfo(info);
        return apiStatus;
    }





    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }
}
