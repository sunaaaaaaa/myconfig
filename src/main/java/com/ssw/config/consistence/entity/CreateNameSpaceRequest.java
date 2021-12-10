package com.ssw.config.consistence.entity;

import com.ssw.config.core.entity.Constant;

import java.io.Serializable;

/**
 * @ClassName CreateNameSpaceRequest
 * @Description
 * @Author sun
 * @Date 2021/12/10 13:56
 **/
public class CreateNameSpaceRequest implements Serializable,BaseRequest {
    private static final long serialVersionUID = 6369783126834834501L;
    private String namespaceId;

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String requestType() {
        return Constant.OPERATION_CREATE_NAMESPACE;
    }
}
