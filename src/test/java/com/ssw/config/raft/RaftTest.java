package com.ssw.config.raft;


import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.error.RemotingException;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;
import com.ssw.config.config.RaftConfig;
import com.ssw.config.consistence.ConfigRaftServer;
import com.ssw.config.consistence.entity.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RaftTest
 * @Description
 * @Author sun
 * @Date 2021/12/10 19:41
 **/
public class RaftTest {


    public static void main(String[] args) throws TimeoutException, InterruptedException, RemotingException, IOException {

        System.out.println(args[3]);

        //先启动server
        RaftConfig raftConfig = new RaftConfig();
        raftConfig.setDataPath("server1");
        raftConfig.setServerId("127.0.0.1:8081");
        raftConfig.setPeersId("127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083");
        ConfigRaftServer server1 = new ConfigRaftServer();
        server1.init(raftConfig);

        RaftConfig raftConfig2 = new RaftConfig();
        raftConfig2.setDataPath("server2");
        raftConfig2.setServerId("127.0.0.1:8082");
        raftConfig2.setPeersId("127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083");
        ConfigRaftServer server2 = new ConfigRaftServer();
        server2.init(raftConfig2);

        RaftConfig raftConfig3 = new RaftConfig();
        raftConfig3.setDataPath("server3");
        raftConfig3.setServerId("127.0.0.1:8083");
        raftConfig3.setPeersId("127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083");
        ConfigRaftServer server3 = new ConfigRaftServer();
        server3.init(raftConfig3);


        Thread.sleep(10000);

        String groupId = "meshConfig";
        String conf = "127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083";

        Configuration configuration = new Configuration();
        configuration.parse(conf);

        RouteTable.getInstance().updateConfiguration(groupId,conf);

        //创建客户端
        CliClientServiceImpl cliClientService = new CliClientServiceImpl();
        cliClientService.init(new CliOptions());

        if(!RouteTable.getInstance().refreshLeader(cliClientService,groupId,1000).isOk()){
            throw new IllegalStateException("refresh leader failed");
        }

        PeerId leader = RouteTable.getInstance().selectLeader(groupId);

        for (int i = 0;i<3;i++){
            CreateNameSpaceRequest createNameSpaceRequest = new CreateNameSpaceRequest();
            createNameSpaceRequest.setNamespaceId(""+i);
            cliClientService.getRpcClient().invokeSync(leader.getEndpoint(),createNameSpaceRequest,5000);
        }

        for(int i = 0;i<7;i++){
            int namespaceId = i%3;
            CreatePropertiesRequest createPropertiesRequest = new CreatePropertiesRequest();
            createPropertiesRequest.setNamespaceId(""+namespaceId);
            createPropertiesRequest.setPropertiesId(""+i);
            cliClientService.getRpcClient().invokeSync(leader.getEndpoint(),createPropertiesRequest,5000);
        }
        leader = RouteTable.getInstance().selectLeader(groupId);
        for(int i = 0;i<50;i++){
            int propertiesId = i%7;
            int namespaceId = propertiesId%3;
            PutConfigRequest request = new PutConfigRequest();
            request.setNamespaceId(""+namespaceId);
            request.setPropertiesId(""+propertiesId);
            Map<String,String> props = new HashMap<>();
            props.put(""+i,""+i);
            props.put("hello"+i,"hello"+i);
            request.setProps(props);
            cliClientService.getRpcClient().invokeSync(leader.getEndpoint(),request,5000);
        }
        leader = RouteTable.getInstance().selectLeader(groupId);
        for(int i = 0;i<7;i++){
            int namespaceId = i%3;
            GetConfigRequest request = new GetConfigRequest();
            request.setGetAll(true);
            request.setNamespaceId(""+namespaceId);
            request.setReadSafe(false);
            request.setNamespaceId(""+namespaceId);
            request.setPropertiesId(""+i);
            if (i>3){
                request.setReadSafe(true);
            }
            Object result = cliClientService.getRpcClient().invokeSync(leader.getEndpoint(), request, 10000);
            BaseResponse response = (BaseResponse)result;
            System.out.println(response.getErrorMsg());
            GetRequestResponse requestResponse = (GetRequestResponse)response;
            for (Map.Entry<String, String> entry : requestResponse.getPropertiesValues().entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        }

        leader = RouteTable.getInstance().selectLeader(groupId);
        for(int i = 0;i<10;i++){
            int propertiesId = i%7;
            int namespaceId = propertiesId%3;
            DelConfigRequest request = new DelConfigRequest();
            request.setNamespaceId(""+namespaceId);
            request.setPropertiesId(""+propertiesId);
            List<String> keys = new LinkedList<>();
            keys.add(""+i);
            keys.add("hello"+i);
            request.setPropKeys(keys);
            cliClientService.getRpcClient().invokeSync(leader.getEndpoint(),request,5000);
        }


        for(int i = 0;i<7;i++){
            int namespaceId = i%3;
            GetConfigRequest request = new GetConfigRequest();
            request.setGetAll(true);
            request.setNamespaceId(""+namespaceId);
            request.setReadSafe(true);
            if (i>3){
                request.setReadSafe(false);
            }
            Object result = cliClientService.getRpcClient().invokeSync(leader.getEndpoint(), request, 5000);
            Map<String,String> props = (Map)result;
            for (Map.Entry<String, String> entry : props.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        }
    }


}
