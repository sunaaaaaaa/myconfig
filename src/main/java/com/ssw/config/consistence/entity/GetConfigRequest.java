package com.ssw.config.consistence.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName GetConfigRequest
 * @Description 增加配置请求
 * @Author sun
 * @Date 2021/12/10 10:29
 **/
public class GetConfigRequest implements Serializable {
    private static final long serialVersionUID = -8228340132839377884L;
    private String namespaceId;
    private String propertiesId;
    private boolean isReadSafe;
    private List<String> propKeys;

    public boolean isReadSafe() {
        return isReadSafe;
    }

    public void setReadSafe(boolean readSafe) {
        isReadSafe = readSafe;
    }

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

    public List<String> getPropKeys() {
        return propKeys;
    }

    public void setPropKeys(List<String> propKeys) {
        this.propKeys = propKeys;
    }
}
