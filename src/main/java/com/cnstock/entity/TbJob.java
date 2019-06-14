package com.cnstock.entity;

public class TbJob {
    private String jobId;

    private String jobName;

    private String jobUrl;

    private String jobModel;

    private String hashCode;

    private String content;

    private Integer errorCount;

    private String isEnable;

    private Integer status;

    private String droneId;

    private String jobOwner;

    private String include1;

    private String include2;

    private String include3;

    private String isInclude1;

    private String isInclude2;

    private String isInclude3;

    private String createTime;

    private String updateTime;

    private String errorType;

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl == null ? null : jobUrl.trim();
    }

    public String getJobModel() {
        return jobModel;
    }

    public void setJobModel(String jobModel) {
        this.jobModel = jobModel == null ? null : jobModel.trim();
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode == null ? null : hashCode.trim();
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable == null ? null : isEnable.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId == null ? null : droneId.trim();
    }

    public String getJobOwner() {
        return jobOwner;
    }

    public void setJobOwner(String jobOwner) {
        this.jobOwner = jobOwner == null ? null : jobOwner.trim();
    }

    public String getInclude1() {
        return include1;
    }

    public void setInclude1(String include1) {
        this.include1 = include1 == null ? null : include1.trim();
    }

    public String getInclude2() {
        return include2;
    }

    public void setInclude2(String include2) {
        this.include2 = include2 == null ? null : include2.trim();
    }

    public String getInclude3() {
        return include3;
    }

    public void setInclude3(String include3) {
        this.include3 = include3 == null ? null : include3.trim();
    }

    public String getIsInclude1() {
        return isInclude1;
    }

    public void setIsInclude1(String isInclude1) {
        this.isInclude1 = isInclude1 == null ? null : isInclude1.trim();
    }

    public String getIsInclude2() {
        return isInclude2;
    }

    public void setIsInclude2(String isInclude2) {
        this.isInclude2 = isInclude2 == null ? null : isInclude2.trim();
    }

    public String getIsInclude3() {
        return isInclude3;
    }

    public void setIsInclude3(String isInclude3) {
        this.isInclude3 = isInclude3 == null ? null : isInclude3.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }
}