package org.shaman.example.biz.utils.web;

import java.util.Set;

/**
 * Created by fenglei on 2015/10/10.
 */
public class WebResponse<T> {

    protected int status = ResponseCode.WEB_STATUS_OK;
    protected T data;
    protected Set<Integer> errorCode;
    protected String errorMessage;
    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Set<Integer> getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Set<Integer> errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
