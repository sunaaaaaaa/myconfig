package com.ssw.config.consistence.entity;

import com.ssw.config.core.entity.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DelConfigRequest
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:29
 **/
public class DelConfigRequest implements Serializable,BaseRequest {

    private static final long serialVersionUID = 8323945041051367291L;

    private String namespaceId;
    private String propertiesId;
    private List<String> propKeys;

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

    @Override
    public String requestType() {
        return Constant.OPERATION_DEL_CONFIG;
    }
}
