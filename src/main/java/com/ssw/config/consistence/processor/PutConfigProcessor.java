package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ssw.config.consistence.entity.PutConfigRequest;

/**
 * @ClassName PutConfigProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:30
 **/
public class PutConfigProcessor implements RpcProcessor<PutConfigRequest> {


    @Override
    public void handleRequest(RpcContext rpcContext, PutConfigRequest putConfigRequest) {

    }

    @Override
    public String interest() {
        return null;
    }
}
