package com.param.global.dto.sync;

import javax.persistence.Column;

public class hl7ConfigListDto {

    private Integer hl7ConfigId;

    private String hisAppUrl;

    private String isPms;

    private Integer unitId;

    private Integer orgId;

    private boolean hl7Tls;

    private Integer hl7ServerPort;

    private Integer hl7Port;

    private String hl7HostIp;

    public Integer getHl7ConfigId() {
        return hl7ConfigId;
    }

    public void setHl7ConfigId(Integer hl7ConfigId) {
        this.hl7ConfigId = hl7ConfigId;
    }


    public String getHisAppUrl() {
        return hisAppUrl;
    }

    public void setHisAppUrl(String hisAppUrl) {
        this.hisAppUrl = hisAppUrl;
    }

    public String getIsPms() {
        return isPms;
    }

    public void setIsPms(String isPms) {
        this.isPms = isPms;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public boolean isHl7Tls() {
        return hl7Tls;
    }

    public void setHl7Tls(boolean hl7Tls) {
        this.hl7Tls = hl7Tls;
    }

    public Integer getHl7ServerPort() {
        return hl7ServerPort;
    }

    public void setHl7ServerPort(Integer hl7ServerPort) {
        this.hl7ServerPort = hl7ServerPort;
    }

    public Integer getHl7Port() {
        return hl7Port;
    }

    public void setHl7Port(Integer hl7Port) {
        this.hl7Port = hl7Port;
    }

    public String getHl7HostIp() {
        return hl7HostIp;
    }

    public void setHl7HostIp(String hl7HostIp) {
        this.hl7HostIp = hl7HostIp;
    }
}
