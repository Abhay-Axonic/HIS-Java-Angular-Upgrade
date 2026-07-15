package com.param.adt.master.global.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GET_HL7_CONFIG",
                query = "SELECT "
                        + "  hl7_config_id AS \"hl7ConfigId\", "
                        + "  his_app_url AS \"hisAppUrl\", "
                        + "  is_pms AS \"isPms\", "
                        + "  hl7_tls AS \"hl7Tls\", "
                        + "  hl7_server_port AS \"hl7ServerPort\", "
                        + "  hl7_port AS \"hl7Port\", "
                        + "  hl7_host_ip AS \"hl7HostIp\", "
                        + "  org_id AS \"orgId\", "
                        + "  unit_id AS \"unitId\" "
                        + "FROM public.hl7_config "
                        + "WHERE org_id = :orgId "
                        + "AND unit_id = :unitId"
        )

        })


    @Entity
    @Table(name = "hl7_config", schema = "public")
    @SequenceGenerator(name = "hl7_config_seq", sequenceName = "public.hl7_config_seq", allocationSize = 1)
    public class hl7Config {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hl7_config_seq")
        @Column(name = "hl7_config_id")
        private Integer hl7ConfigId;

        @Column(name = "his_app_url")
        private String hisAppUrl;

        @Column(name = "is_pms")
        private String isPms;

        @Column(name = "unit_id")
        private Integer unitId;

        @Column(name = "org_id")
        private Integer orgId;

        @Column(name = "hl7_tls")
        private boolean hl7Tls;

        @Column(name = "hl7_server_port")
        private Integer hl7ServerPort;

        @Column(name = "hl7_port")
        private Integer hl7Port;

        @Column(name = "hl7_host_ip")
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

        public boolean isHl7Tls() {
            return hl7Tls;
        }

        public void setHl7Tls(boolean hl7Tls) {
            this.hl7Tls = hl7Tls;
        }

        public Integer getOrgId() {
            return orgId;
        }

        public void setOrgId(Integer orgId) {
            this.orgId = orgId;
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
