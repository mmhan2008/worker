package com.cnstock.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2019/1/3.
 */
@ConfigurationProperties(prefix = "com.stock")
public class ConfigBean {
    private String name;
    private String want;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWant() {
        return want;
    }

    public void setWant(String want) {
        this.want = want;
    }
}
