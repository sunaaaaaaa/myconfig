package com.ssw.config.consistence.statemachine;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.error.RaftException;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.ssw.config.consistence.ConfigRaftServer;
import com.ssw.config.consistence.entity.*;
import com.ssw.config.consistence.processor.ConfigCenterClosure;
import com.ssw.config.core.entity.Constant;
import com.ssw.config.core.entity.MyProperties;
import com.ssw.config.core.entity.NameSpace;
import org.apache.commons.lang.StringUtils;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ConfigCenterStateMachine
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:28
 **/
public class ConfigCenterStateMachine extends StateMachineAdapter {

    private ConfigRaftServer raftServer;
    private Map<String, NameSpace> nameSpaceMap = new ConcurrentHashMap<>();

    public ConfigCenterStateMachine(ConfigRaftServer raftServer){
        this.raftServer = raftServer;
    }

    //raft保证线性执行
    @Override
    public void onApply(Iterator iterator) {
        while (iterator.hasNext()){
            ConfigCenterClosure closure = null;
            BaseRequest request = null;
            if (iterator.done()!=null){
                closure = (ConfigCenterClosure) iterator.done();
                request = closure.getRequest();
            }else{
                ByteBuffer data = iterator.getData();
                try{
                    request = SerializerManager.getSerializer(SerializerManager.Hessian2)
                            .deserialize(data.array(),BaseRequest.class.getName());
                } catch (CodecException e) {
                    e.printStackTrace();
                }
            }
            Map<String,String> result = new HashMap<>();
            boolean isSuccess = false;
            if (request!= null){
                switch (request.requestType()){
                    case Constant.OPERATION_CREATE_NAMESPACE:
                        isSuccess = handleCreateNameSpace(request);
                        break;
                    case Constant.OPERATION_CREATE_PROPERTIES:
                        isSuccess = handleCreateProperties(request);
                        break;
                    case Constant.OPERATION_GET_CONFIG:
                        result = handleSafeGet(request);
                        isSuccess = true;
                        break;
                    case Constant.OPERATION_PUT_CONFIG:
                        isSuccess = handlePut(request);
                        break;
                    case Constant.OPERATION_DEL_CONFIG:
                        isSuccess = handleDel(request);
                        break;
                }

                if (closure!=null){
                    if (isSuccess){
                        closure.success(request.requestType(),result);
                        closure.run(Status.OK());
                    }else{
                        closure.failure("error:" + request.requestType(),raftServer.redirect().getRedirect());
                    }
                }
            }
            iterator.next();
        }
    }


    @Override
    public void onSnapshotSave(SnapshotWriter writer, Closure done) {
        super.onSnapshotSave(writer, done);
    }

    @Override
    public boolean onSnapshotLoad(SnapshotReader reader) {
        return super.onSnapshotLoad(reader);
    }

    @Override
    public void onLeaderStart(long term) {
        raftServer.getLeaderTerm().set(term);
        super.onLeaderStart(term);
    }

    @Override
    public void onLeaderStop(Status status) {
        raftServer.getLeaderTerm().set(-1);
        super.onLeaderStop(status);
    }

    @Override
    public void onError(RaftException e) {
        super.onError(e);
    }

    public void handleRequest(ConfigCenterClosure closure){

        if (!raftServer.isLeader()){
            notLeaderError(closure);
            return;
        }
        try{
            //通过raft同步
            Task task = new Task();
            task.setData(ByteBuffer.wrap(SerializerManager.
                    getSerializer(SerializerManager.Hessian2).serialize(closure.getRequest())));
            task.setDone(closure);
            raftServer.getRaftNode().apply(task);
        } catch (CodecException e) {
            closure.failure(e.getMessage(), StringUtils.EMPTY);
            closure.run(new Status(RaftError.EINTERNAL, e.getMessage()));
        }
    }

    public void unSafeGetProps(ConfigCenterClosure closure){
        GetConfigRequest getConfigRequest = (GetConfigRequest) closure.getRequest();
        NameSpace nameSpace = nameSpaceMap.get(getConfigRequest.getNamespaceId());
        if (nameSpace == null){
            closure.failure("namespace is not exist",raftServer.redirect().getRedirect());
            closure.run(new Status(RaftError.EPERM,"namespace is not exist"));
            return;
        }
        MyProperties properties = nameSpace.getProperties(getConfigRequest.getPropertiesId());
        if (properties == null) {
            closure.failure("properties file is not exist",raftServer.redirect().getRedirect());
            closure.run(new Status(RaftError.EPERM,"properties file is not exist"));
            return;
        }

        if (getConfigRequest.isGetAll()){
            closure.success(getConfigRequest.requestType(),properties.getProperties());
            closure.run(Status.OK());
        }else{
            closure.success(getConfigRequest.requestType(),properties.getConfigs(getConfigRequest.getPropKeys()));
            closure.run(Status.OK());
        }
    }

    //创建命名空间
    private boolean handleCreateNameSpace(BaseRequest baseRequest){

        CreateNameSpaceRequest request = (CreateNameSpaceRequest)baseRequest;
        NameSpace nameSpace = new NameSpace(request.getNamespaceId());
        nameSpaceMap.put(request.getNamespaceId(),nameSpace);
        return true;
    }

    private boolean handleCreateProperties(BaseRequest baseRequest){
        CreatePropertiesRequest request = (CreatePropertiesRequest)baseRequest;
        if (nameSpaceMap.containsKey(request.getNamespaceId())){
            NameSpace nameSpace = nameSpaceMap.get(request.getNamespaceId());
            MyProperties properties = new MyProperties(request.getNamespaceId(),request.getPropertiesId());
            nameSpace.putProperties(request.getPropertiesId(),properties);
            return true;
        }
        return false;
    }

    private Map<String,String> handleSafeGet(BaseRequest baseRequest){
        GetConfigRequest request = (GetConfigRequest)baseRequest;
        String namespaceId = request.getNamespaceId();
        String propertiesId = request.getPropertiesId();
        if (nameSpaceMap.containsKey(namespaceId)){
            NameSpace nameSpace = nameSpaceMap.get(namespaceId);
            MyProperties properties = nameSpace.getProperties(propertiesId);
            if (properties!=null){
                if(request.isGetAll()){
                    return properties.getProperties();
                }else{
                    return properties.getConfigs(request.getPropKeys());
                }
            }
        }
        return new HashMap<>();
    }

    private boolean handlePut(BaseRequest baseRequest){
        PutConfigRequest request = (PutConfigRequest)baseRequest;
        String namespaceId = request.getNamespaceId();
        if (nameSpaceMap.containsKey(namespaceId)){
            NameSpace nameSpace = nameSpaceMap.get(namespaceId);
            MyProperties properties = nameSpace.getProperties(request.getPropertiesId());
            if (properties != null){
                for (Map.Entry<String, String> entry : request.getProps().entrySet()) {
                    properties.putConfig(entry.getKey(),entry.getValue());
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean handleDel(BaseRequest baseRequest){

       DelConfigRequest request = (DelConfigRequest)baseRequest;
        String namespaceId = request.getNamespaceId();
        if (nameSpaceMap.containsKey(namespaceId)){
            NameSpace nameSpace = nameSpaceMap.get(namespaceId);
            MyProperties properties = nameSpace.getProperties(request.getPropertiesId());
            if (properties!=null){
                for (String propKey : request.getPropKeys()) {
                    properties.delConfig(propKey);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void notLeaderError(ConfigCenterClosure closure){
        closure.failure("not leader",raftServer.redirect().getRedirect());
        closure.run(new Status(RaftError.EPERM,"not leader"));
    }
}
