package com.cnstock.entity;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2019/1/3.
 */
public class Worker {
    private String ip;
    private String url;
    private String id;
    private int hashCode;
    private String content;
    private String name;
    //错误计数
    private int errorCount;
    //包含的后缀名
    private String suffix;
    //包含的内链
    private String interiorChain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getInteriorChain() {
        return interiorChain;
    }

    public void setInteriorChain(String interiorChain) {
        this.interiorChain = interiorChain;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }


}
