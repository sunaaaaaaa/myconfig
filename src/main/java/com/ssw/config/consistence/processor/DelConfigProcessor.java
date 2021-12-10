package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ssw.config.consistence.entity.DelConfigRequest;
import com.ssw.config.consistence.statemachine.ConfigCenterStateMachine;

/**
 * @ClassName DelConfigProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:30
 **/
public class DelConfigProcessor implements RpcProcessor<DelConfigRequest> {

    private ConfigCenterStateMachine stateMachine;

    public DelConfigProcessor(ConfigCenterStateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    @Override
    public void handleRequest(RpcContext rpcContext, DelConfigRequest delConfigRequest) {

        ConfigCenterClosure closure = new ConfigCenterClosure() {
            @Override
            public void run(Status status) {
                rpcContext.sendResponse(getResponse());
            }
        };

        closure.setRequest(delConfigRequest);
        closure.setOperation(delConfigRequest.requestType());
        stateMachine.handleRequest(closure);

    }

    @Override
    public String interest() {
        return DelConfigRequest.class.getName();
    }
}
