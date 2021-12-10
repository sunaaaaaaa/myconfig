package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ssw.config.consistence.entity.CreatePropertiesRequest;
import com.ssw.config.consistence.statemachine.ConfigCenterStateMachine;

/**
 * @ClassName CreatePropertiesProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 14:04
 **/
public class CreatePropertiesProcessor implements RpcProcessor<CreatePropertiesRequest> {

    private ConfigCenterStateMachine stateMachine;

    public CreatePropertiesProcessor(ConfigCenterStateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    @Override
    public void handleRequest(RpcContext rpcContext, CreatePropertiesRequest createPropertiesRequest) {
        ConfigCenterClosure closure = new ConfigCenterClosure() {
            @Override
            public void run(Status status) {
                rpcContext.sendResponse(getResponse());
            }
        };

        closure.setRequest(createPropertiesRequest);
        closure.setOperation(createPropertiesRequest.requestType());
        stateMachine.handleRequest(closure);
    }

    @Override
    public String interest() {
        return CreatePropertiesRequest.class.getName();
    }
}
