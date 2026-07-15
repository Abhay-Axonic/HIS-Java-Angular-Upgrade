package com.param.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorLog {

    private String exception;
    private String method;
    private String parameters;
    private String payLoad;
    private String className;
    private String methodName;
    private String url;
    private String projectId;
    private String summary;
    private String description;
    private String priorityId;
    private String issueTypeId;
    private String startDate;
    private String issueNo;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSummary() {
        return "Api Error in -> class: "+this.getClassName()+ "Method: "+this.getMethodName();
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return    "Url         -> "+this.getUrl()+"\\n"
                + "Method      -> "+this.getMethod()+"\\n"
                + "Payload     -> "+this.getPayLoad()+"\\n"
                + "Parameters  -> "+this.getParameters()+"\\n"
                + "ClassName   -> "+this.getClassName()+"\\n"
                + "ClassMethod -> "+this.getMethodName()+"\\n"
                + "Exception   ->"+this.getException();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getIssueTypeId() {
        return issueTypeId;
    }

    public void setIssueTypeId(String issueTypeId) {
        this.issueTypeId = issueTypeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String   getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }
}
