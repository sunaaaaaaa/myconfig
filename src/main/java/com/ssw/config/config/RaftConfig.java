package com.ssw.config.config;

/**
 * @ClassName RaftConfig
 * @Description
 * @Author sun
 * @Date 2021/12/10 10:01
 **/
public class RaftConfig {
    //默认值,raft数据、日志等存储父路径
    private String dataPath = "./mesh-config/";
    //raft集群id
    private String groupId = "meshConfig";
    private String serverId;//本机raft节点的ip和port
    private String peersId;//集群其他节点的ip和port
    private int timeoutMills = 2000;//选举超时时间,默认2s
    private boolean disableCli = false;//默认关闭
    private int snapshotIntervalSeconds = 60;//默认60s产生一次快照

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPeersId() {
        return peersId;
    }

    public void setPeersId(String peersId) {
        this.peersId = peersId;
    }

    public int getTimeoutMills() {
        return timeoutMills;
    }

    public void setTimeoutMills(int timeoutMills) {
        this.timeoutMills = timeoutMills;
    }

    public boolean isDisableCli() {
        return disableCli;
    }

    public void setDisableCli(boolean disableCli) {
        this.disableCli = disableCli;
    }

    public int getSnapshotIntervalSeconds() {
        return snapshotIntervalSeconds;
    }

    public void setSnapshotIntervalSeconds(int snapshotIntervalSeconds) {
        this.snapshotIntervalSeconds = snapshotIntervalSeconds;
    }
}
