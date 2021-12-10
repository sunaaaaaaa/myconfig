package com.ssw.config.consistence;


import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import com.ssw.config.config.RaftConfig;
import com.ssw.config.consistence.entity.BaseResponse;
import com.ssw.config.consistence.processor.*;
import com.ssw.config.consistence.statemachine.ConfigCenterStateMachine;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName ConfigRaftServer
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:24
 **/
public class ConfigRaftServer {

    private ConfigCenterStateMachine stateMachine;
    private Node raftNode;
    private volatile boolean isInit;
    private AtomicLong leaderTerm = new AtomicLong(-1);

    public void init(RaftConfig raftConfig) throws IOException {
        //创建存储数据、快照、日志的父目录
        String dataPath = raftConfig.getDataPath();
        FileUtils.forceMkdir(new File(dataPath));
        //xxx.xxx.xxx.xxx:port的形式
        String serverHost = raftConfig.getServerId();
        //xxx.xxx.xxx.xxx:port,xxx.xxx.xxx.xxx:port
        String peersHost = raftConfig.getPeersId();
        PeerId serverId = new PeerId();
        if (!serverId.parse(serverHost)){
            throw new IllegalArgumentException("failed to parse serverId:" + serverHost);
        }
        Configuration peersConf = new Configuration();
        if (peersConf.parse(peersHost)){
            throw new IllegalArgumentException("failed to parse peers host:" + peersHost);
        }

        //创建状态机
        stateMachine = new ConfigCenterStateMachine(this);
        //raft初始化参数
        NodeOptions nodeOptions = new NodeOptions();
        //设置集群其他成员
        nodeOptions.setInitialConf(peersConf);
        //设置超时
        nodeOptions.setElectionTimeoutMs(raftConfig.getTimeoutMills());
        nodeOptions.setDisableCli(raftConfig.isDisableCli());
        nodeOptions.setSnapshotIntervalSecs(raftConfig.getSnapshotIntervalSeconds());
        nodeOptions.setLogUri(dataPath + File.separator + "log");
        nodeOptions.setRaftMetaUri(dataPath + File.separator + "raft-meta");
        nodeOptions.setSnapshotUri(dataPath + File.separator + "snapshot");
        nodeOptions.setFsm(stateMachine);


        //raft rpc服务器,此处配置中心访问量应该不大，不再单独创建一个rpc服务器对外提供服务
        RpcServer raftRpcServer = RaftRpcServerFactory.createRaftRpcServer(serverId.getEndpoint());
        raftRpcServer.registerProcessor(new CreateNamespaceProcessor(stateMachine));
        raftRpcServer.registerProcessor(new CreatePropertiesProcessor(stateMachine));
        raftRpcServer.registerProcessor(new GetConfigProcessor(stateMachine));
        raftRpcServer.registerProcessor(new PutConfigProcessor(stateMachine));
        raftRpcServer.registerProcessor(new DelConfigProcessor(stateMachine));

        RaftGroupService groupService = new RaftGroupService(raftConfig.getGroupId(),serverId,nodeOptions,raftRpcServer);
        raftNode = groupService.start();
        isInit = true;
    }

    public ConfigCenterStateMachine getStateMachine() {
        return stateMachine;
    }

    public Node getRaftNode() {
        return raftNode;
    }

    public boolean isInit() {
        return isInit;
    }

    //判断当前节点是否为leader
    public boolean isLeader(){
        return leaderTerm.get() > 0;
    }

    public BaseResponse redirect(){
        BaseResponse response = new BaseResponse();
        response.setSuccess(false);
        if (raftNode != null){
            PeerId leaderId = raftNode.getLeaderId();
            if (leaderId != null){
                response.setRedirect(leaderId.toString());
            }else{
                response.setErrorMsg("raft cluster leader is missing");
            }
        }else{
            response.setErrorMsg("this raft node is not exist");
        }
        return response;
    }

    public AtomicLong getLeaderTerm(){
        return leaderTerm;
    }
}
