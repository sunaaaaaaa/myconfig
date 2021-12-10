package com.ssw.config.consistence.entity;

import com.ssw.config.core.entity.Constant;

import java.io.Serializable;

/**
 * @ClassName CreatePropertiesRequest
 * @Description
 * @Author sun
 * @Date 2021/12/10 13:59
 **/
public class CreatePropertiesRequest implements Serializable,BaseRequest {
    private static final long serialVersionUID = 1001180716312001359L;
    private String namespaceId;
    private String propertiesId;
    private String fileType = ".properties"; //默认暂时仅支持 .properties
    private String type;//dev,opt.....

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(String propertiesId) {
        this.propertiesId = propertiesId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String requestType() {
        return Constant.OPERATION_CREATE_PROPERTIES;
    }
}
