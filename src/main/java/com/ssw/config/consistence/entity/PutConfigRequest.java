package com.ssw.config.consistence.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName PutConfigRequest
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:29
 **/
public class PutConfigRequest implements Serializable {
    private static final long serialVersionUID = 4093937043915416949L;

    private String namespaceId;
    private String propertiesId;
    private Map<String,String> props;

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

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }
}
