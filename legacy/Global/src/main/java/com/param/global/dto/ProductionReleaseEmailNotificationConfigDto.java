package com.param.global.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ProductionReleaseEmailNotificationConfigDto {

    private Integer productionReleaseEmailNotificationConfigId;

    private String sendTo;

    private String subject;

    private String message;

    private Integer productionReleaseEmailNotificationConfigMapperId;

    private String instanceName;

    private String versionNumber;

    private Character isEmailSend;

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getProductionReleaseEmailNotificationConfigMapperId() {
        return productionReleaseEmailNotificationConfigMapperId;
    }

    public void setProductionReleaseEmailNotificationConfigMapperId(Integer productionReleaseEmailNotificationConfigMapperId) {
        this.productionReleaseEmailNotificationConfigMapperId = productionReleaseEmailNotificationConfigMapperId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Character getIsEmailSend() {
        return isEmailSend;
    }

    public void setIsEmailSend(Character isEmailSend) {
        this.isEmailSend = isEmailSend;
    }

    public Integer getProductionReleaseEmailNotificationConfigId() {
        return productionReleaseEmailNotificationConfigId;
    }

    public void setProductionReleaseEmailNotificationConfigId(Integer productionReleaseEmailNotificationConfigId) {
        this.productionReleaseEmailNotificationConfigId = productionReleaseEmailNotificationConfigId;
    }
}
