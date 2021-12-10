package com.ssw.config.core.entity;

import java.util.Map;

/**
 * @ClassName MyProperties
 * @Description 表示一个配置文件
 * @Author sun
 * @Date 2021/12/10 10:53
 **/
public class MyProperties {
    private String id;
    private Map<String,String> properties;

    public MyProperties(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
