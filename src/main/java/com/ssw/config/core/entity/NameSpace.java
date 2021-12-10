package com.ssw.config.core.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName NameSpace
 * @Description 命名空间
 * @Author sun
 * @Date 2021/12/10 10:53
 **/
public class NameSpace {

    private String id;
    private Map<String,MyProperties> propertiesMap = new ConcurrentHashMap<>();

    public NameSpace(String id){
        this.id = id;
    }

    public MyProperties getProperties(String proId){
        return propertiesMap.get(proId);
    }

    public void delProperties(String proId){
        propertiesMap.remove(proId);
    }

    public void putProperties(String proId,MyProperties properties){
        propertiesMap.put(proId,properties);
    }

    public String getId() {
        return id;
    }
}
