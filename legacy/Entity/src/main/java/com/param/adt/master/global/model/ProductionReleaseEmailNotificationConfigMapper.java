package com.param.adt.master.global.model;

import javax.persistence.*;

@NamedQueries({

})

@Embeddable
@Entity
@Table(name = "production_release_email_notification_config_mapper", schema = "public")
@SequenceGenerator(name = "production_release_email_notification_config_mapper_seq", sequenceName = "public.production_release_email_notification_config_mapper_seq", allocationSize = 1)
public class ProductionReleaseEmailNotificationConfigMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_release_email_notification_config_mapper_seq")
    @Column(name = "production_release_email_notification_config_mapper_id")
    private Integer productionReleaseEmailNotificationConfigMapperId;

    @Column(name = "send_to")
    private String sendTo;

    @Column(name = "message")
    private String message;

    @Column(name = "instance_name")
    private String instanceName;

    @Column(name = "version_number")
    private String versionNumber;

    @Column(name = "is_email_send")
    private Character isEmailSend;

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Integer getProductionReleaseEmailNotificationConfigMapperId() {
        return productionReleaseEmailNotificationConfigMapperId;
    }

    public void setProductionReleaseEmailNotificationConfigMapperId(Integer productionReleaseEmailNotificationConfigMapperId) {
        this.productionReleaseEmailNotificationConfigMapperId = productionReleaseEmailNotificationConfigMapperId;
    }
}
