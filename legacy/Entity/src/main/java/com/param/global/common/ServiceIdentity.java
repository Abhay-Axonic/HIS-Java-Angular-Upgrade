package com.param.global.common;

import com.param.entity.lis.common.LocalTimeConverter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "t_service_identity", schema = "public")
@SequenceGenerator(name = "service_identity_seq", sequenceName = "public.service_identity_seq", allocationSize = 1)
public class ServiceIdentity {

    @Id
    @Column(name = "service_identity_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_identity_seq")
    private Integer serviceIdentityId;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "last_heart_beat_date_time")
    @Convert(converter = LocalTimeConverter.class)
    private Long lastHeartBeatDateTime;

    @Column(name = "primary_host")
    private Boolean primaryHost;

    public Integer getServiceIdentityId() {
        return serviceIdentityId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Long getLastHeartBeatDateTime() {
        return lastHeartBeatDateTime;
    }

    public void setLastHeartBeatDateTime(Long lastHeartBeatDateTime) {
        this.lastHeartBeatDateTime = lastHeartBeatDateTime;
    }

    public Boolean getPrimaryHost() {
        return primaryHost;
    }

    public void setPrimaryHost(Boolean primaryHost) {
        this.primaryHost = primaryHost;
    }

    public void setServiceIdentityId(Integer serviceIdentityId) {
        this.serviceIdentityId = serviceIdentityId;
    }
}
