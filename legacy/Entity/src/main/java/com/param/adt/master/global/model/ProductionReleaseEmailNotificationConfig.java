package com.param.adt.master.global.model;

import javax.persistence.*;

@NamedQueries({

})

@Embeddable
@Entity
@Table(name = "production_release_email_notification_config", schema = "public")
@SequenceGenerator(name = "production_release_email_notification_config_seq", sequenceName = "public.production_release_email_notification_config_seq", allocationSize = 1)
public class ProductionReleaseEmailNotificationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_release_email_notification_config_seq")
    @Column(name = "production_release_email_notification_config_id")
    private Integer productionReleaseEmailNotificationConfigId;

    @Column(name = "send_to")
    private String sendTo;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message")
    private String message;

    public Integer getProductionReleaseEmailNotificationConfigId() {
        return productionReleaseEmailNotificationConfigId;
    }

    public void setProductionReleaseEmailNotificationConfigId(Integer productionReleaseEmailNotificationConfigId) {
        this.productionReleaseEmailNotificationConfigId = productionReleaseEmailNotificationConfigId;
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

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
}
