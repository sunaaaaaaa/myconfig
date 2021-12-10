package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.Closure;
import com.ssw.config.consistence.entity.BaseRequest;
import com.ssw.config.consistence.entity.BaseResponse;
import com.ssw.config.consistence.entity.GetRequestResponse;
import com.ssw.config.core.entity.Constant;

import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName ConfigCenterClosure
 * @Description 配置中心扩展回调
 * @Author sun
 * @Date 2021/12/10 14:29
 **/
public abstract class ConfigCenterClosure implements Closure {

    private BaseRequest request;
    private BaseResponse response;
    private String operation;//操作get\create\put\...

    public BaseRequest getRequest() {
        return request;
    }

    public void setRequest(BaseRequest request) {
        this.request = request;
    }

    public BaseResponse getResponse() {
        return response;
    }

    private void setResponse(BaseResponse response) {
        this.response = response;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void failure(String errMsg,String redirect){
        BaseResponse response = new BaseResponse();
        response.setSuccess(false);
        response.setErrorMsg(errMsg);
        response.setRedirect(redirect);
        setResponse(response);
    }

    public void success(String operation,Map<String,String> props){
        if (Constant.OPERATION_GET_CONFIG.equals(operation)){
            if (props == null){
                props = new HashMap<>();
            }
            GetRequestResponse requestResponse = new GetRequestResponse();
            requestResponse.setSuccess(true);
            requestResponse.setPropertiesValues(props);
            setResponse(requestResponse);
        }else{
            BaseResponse response = new BaseResponse();
            response.setSuccess(true);
            setResponse(response);
        }
    }
}
