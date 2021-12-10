package com.ssw.config.consistence.processor;

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.ssw.config.consistence.entity.CreateNameSpaceRequest;
import com.ssw.config.consistence.statemachine.ConfigCenterStateMachine;

/**
 * @ClassName CreateNamespaceProcessor
 * @Description
 * @Author sun
 * @Date 2021/12/10 14:04
 **/
public class CreateNamespaceProcessor implements RpcProcessor<CreateNameSpaceRequest> {

    private ConfigCenterStateMachine stateMachine;

    public CreateNamespaceProcessor(ConfigCenterStateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    @Override
    public void handleRequest(RpcContext rpcContext, CreateNameSpaceRequest createNameSpaceRequest) {
         ConfigCenterClosure closure = new ConfigCenterClosure() {
             @Override
             public void run(Status status) {
                 rpcContext.sendResponse(getResponse());
             }
         };

         closure.setRequest(createNameSpaceRequest);
         closure.setOperation(createNameSpaceRequest.requestType());
         stateMachine.handleRequest(closure);
    }

    @Override
    public String interest() {
        return CreateNameSpaceRequest.class.getName();
    }
}
