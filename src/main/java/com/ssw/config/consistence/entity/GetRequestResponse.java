package com.ssw.config.consistence.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName GetRequestResponse
 * @Description
 * @Author sun
 * @Date 2021/12/10 13:46
 **/
public class GetRequestResponse extends BaseResponse implements Serializable{

    private static final long serialVersionUID = -1898364354768971860L;
    private Map<String,String> propertiesValues;

    public Map<String, String> getPropertiesValues() {
        return propertiesValues;
    }

    public void setPropertiesValues(Map<String, String> propertiesValues) {
        this.propertiesValues = propertiesValues;
    }

}
