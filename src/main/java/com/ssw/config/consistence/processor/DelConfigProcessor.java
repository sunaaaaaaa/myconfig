package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ssw.config.consistence.entity.DelConfigRequest;

/**
 * @ClassName DelConfigProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:30
 **/
public class DelConfigProcessor implements RpcProcessor<DelConfigRequest> {

    @Override
    public void handleRequest(RpcContext rpcContext, DelConfigRequest delConfigRequest) {

    }

    @Override
    public String interest() {
        return null;
    }
}
