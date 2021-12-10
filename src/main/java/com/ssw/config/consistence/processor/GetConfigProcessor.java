package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ssw.config.consistence.entity.GetConfigRequest;
import com.ssw.config.consistence.statemachine.ConfigCenterStateMachine;

/**
 * @ClassName GetConfigProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:30
 **/
public class GetConfigProcessor implements RpcProcessor<GetConfigRequest> {

    private ConfigCenterStateMachine stateMachine;

    public GetConfigProcessor(ConfigCenterStateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    @Override
    public void handleRequest(RpcContext rpcContext, GetConfigRequest getConfigRequest) {
        ConfigCenterClosure closure = new ConfigCenterClosure() {
            @Override
            public void run(Status status) {
                rpcContext.sendResponse(getResponse());
            }
        };

        closure.setRequest(getConfigRequest);
        closure.setOperation(getConfigRequest.requestType());
        if (!getConfigRequest.isReadSafe()){
           stateMachine.unSafeGetProps(closure);
           return;
        }

        stateMachine.handleRequest(closure);
    }

    @Override
    public String interest() {
        return GetConfigRequest.class.getName();
    }
}
