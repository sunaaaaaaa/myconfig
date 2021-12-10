package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ssw.config.consistence.entity.PutConfigRequest;
import com.ssw.config.consistence.statemachine.ConfigCenterStateMachine;

/**
 * @ClassName PutConfigProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:30
 **/
public class PutConfigProcessor implements RpcProcessor<PutConfigRequest> {

    private ConfigCenterStateMachine stateMachine;

    public PutConfigProcessor(ConfigCenterStateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    @Override
    public void handleRequest(RpcContext rpcContext, PutConfigRequest putConfigRequest) {
        ConfigCenterClosure closure = new ConfigCenterClosure() {
            @Override
            public void run(Status status) {
                rpcContext.sendResponse(getResponse());
            }
        };

        closure.setRequest(putConfigRequest);
        closure.setOperation(putConfigRequest.requestType());
        stateMachine.handleRequest(closure);
    }

    @Override
    public String interest() {
        return PutConfigRequest.class.getName();
    }
}
