package com.ssw.config.core.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @ClassName MyProperties
 * @Description 表示一个配置文件
 * @Author sun
 * @Date 2021/12/10 10:53
 **/
public class MyProperties {
    private String id;
    private String namespaceId;
    private Map<String,String> properties;


    public MyProperties(String namespaceId,String id){
        this.id = id;
        this.namespaceId = namespaceId;
        properties = new ConcurrentHashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void putConfig(String key,String value){
        properties.put(key,value);
    }

    public void delConfig(String key){
        properties.remove(key);
    }

    public String getConfig(String key){
        return properties.get(key);
    }

    public Map<String,String> getConfigs(List<String> keys){
        Map<String,String> result = new HashMap<>();
        for (String key : keys) {
            String value = properties.get(key);
            if (value != null){
                result.put(key,value);
            }
        }
        return result;
    }
}
