package com.cnstock;


import com.alibaba.fastjson.JSON;
import com.cnstock.utils.HttpUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class WorkerApplication {

	public static void main(String[] args) throws Exception{

		String projectPath = new File("").getAbsolutePath();
		String configPath = projectPath + "/config/application.properties";
		Properties properties = new Properties();
		InputStream inputStream = new BufferedInputStream(new FileInputStream(configPath));
		SpringApplication app = new SpringApplication(WorkerApplication.class);
		properties.load(inputStream);
		app.setDefaultProperties(properties);
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();
        String initUrl = properties.getProperty("com.cnstock.getTask");
        Map<String,Object> maps = new HashMap<>();
        maps.put("workerId",hostAddress);
        String param = JSON.toJSONString(maps);
        HttpUtil.postParams(initUrl,param);
		app.run(args);
	}

}

