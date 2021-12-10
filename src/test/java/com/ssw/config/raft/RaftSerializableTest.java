package com.ssw.config.raft;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.ssw.config.consistence.entity.BaseRequest;
import com.ssw.config.consistence.entity.CreateNameSpaceRequest;
import com.ssw.config.consistence.entity.CreatePropertiesRequest;

/**
 * @ClassName RaftSerializableTest
 * @Description
 * @Author sun
 * @Date 2021/12/10 16:18
 **/
public class RaftSerializableTest {

    public static void main(String[] args) throws CodecException {
        CreateNameSpaceRequest request = new CreateNameSpaceRequest();
        request.setNamespaceId("111");
        CreatePropertiesRequest request2 = new CreatePropertiesRequest();
        request2.setNamespaceId("222");
        request2.setPropertiesId("xxx");
        request2.setType("hh");
        byte[] serialize = SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(request2);

        Object deserialize = SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(serialize, BaseRequest.class.getName());
        CreatePropertiesRequest request1 = (CreatePropertiesRequest)deserialize;
        System.out.println(request1.getNamespaceId());
    }

}
