package com.ssw.config.consistence.entity;

import java.io.Serializable;

/**
 * @ClassName BaseResonse
 * @Description
 * @Author sun
 * @Date 2021/12/10 13:50
 **/
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 5648181137091778784L;

    private boolean  isSuccess;
    private String redirect;
    private String errorMsg;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
