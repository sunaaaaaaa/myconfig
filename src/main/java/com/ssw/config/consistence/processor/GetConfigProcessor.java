package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;

/**
 * @ClassName GetConfigProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:30
 **/
public class GetConfigProcessor implements RpcProcessor<GetConfigProcessor> {

    @Override
    public void handleRequest(RpcContext rpcContext, GetConfigProcessor getConfigProcessor) {

    }

    @Override
    public String interest() {
        return null;
    }
}
