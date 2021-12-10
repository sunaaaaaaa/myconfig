package com.ssw.config.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Properties;

/**
 * @ClassName ConfigReadUtil
 * @Description 读取application配置文件
 * @Author sun
 * @Date 2021/12/10 9:50
 **/
public class ConfigReadUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReadUtil.class);

    private static Properties properties;

    static {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream("application.properties"));
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("read application properties error! cannot find the file");
        } catch (IOException e) {
            logger.warn("read application properties error! error info:" + e.getMessage());
        }
    }

    public static String getProperties(String key){
        if (properties == null){
            return null;
        }
        return properties.getProperty(key);
    }

}
