package com.param.global.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "CHECK_RACK_EXISTS_OR_NOT",
        query = "SELECT EXISTS(select 1 from m_mrd_rack_master  " +
                "where (rack_description like :rackDescription or rack_code like :rackCode) " +
                "and organization_id =:organizationId)"),

        @NamedNativeQuery(name = "GET_ACTIVE_MRD_RACK_LIST",
        query = "SELECT rack_id as \"rackId\",rack_code as \"rackCode\",rack_description as \"rackDescription\",unit_id as \"unitId\"," +
                "organization_id as \"organizationId\" FROM m_mrd_rack_master " +
                "WHERE status='A' AND organization_id =:orgId AND unit_id =:unitId " +
                "ORDER BY rack_description ASC "),
        @NamedNativeQuery(name = "GET_MRD_RACK_BY_ID",
        query = "SELECT rack_id as \"rackId\",rack_code as \"rackCode\",rack_description as \"rackDescription\"," +
                "status as \"status\",unit_id as \"unitId\",organization_id as \"organizationId\" " +
                "FROM m_mrd_rack_master " +
                "WHERE rack_id =:rackId and organization_id =:orgId " +
                "and unit_id =:unitId"),
        @NamedNativeQuery(name = "GET_MRD_RACK_LIST",
        query = "SELECT rack_id as \"rackId\",rack_code as \"rackCode\",rack_description as \"rackDescription\"," +
                "status as \"status\",unit_id as \"unitId\",organization_id as \"organizationId\" " +
                "FROM m_mrd_rack_master " +
                "WHERE organization_id =:orgId " +
                "and unit_id =:unitId ORDER BY rack_id DESC " ),
//                "OFFSET (:page * :size) " +
//                "LIMIT :size"),
        @NamedNativeQuery(name = "GET_MRD_RACK_LIST_COUNT",
                query = "SELECT count(*) " +
                        "FROM m_mrd_rack_master " +
                        "WHERE organization_id =:orgId " +
                        "and unit_id =:unitId"),
        @NamedNativeQuery(name = "CHECK_RACK_EXISTS_OR_NOT_AND_EXCLUDES_RACK_ID",
                query = "SELECT EXISTS(select 1 from m_mrd_rack_master  " +
                        "where (rack_description like :rackDescription or rack_code like :rackCode) " +
                        "and organization_id =:organizationId and  rack_id !=:rackId )")
})
@Entity
@Table(name = "m_mrd_rack_master", schema = "public")
@SequenceGenerator(name = "mrd_rack_master_seq", sequenceName = "public.mrd_rack_master_seq", allocationSize = 1)
public class MrdRackMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mrd_rack_master_seq")
    @Column(name = "rack_id")
    private Integer rackId;

    @Column(name = "rack_code")
    private String rackCode;

    @Column(name = "rack_description")
    private String rackDescription;


    @Column(name = "status")
    private char status;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_by")
    private int updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "unit_id")
    private Integer unitId;
    public int getRackId() {
        return rackId;
    }

    public void setRackId(int rackId) {
        this.rackId = rackId;
    }

    public String getRackCode() {
        return rackCode;
    }

    public void setRackCode(String rackCode) {
        this.rackCode = rackCode;
    }

    public String getRackDescription() {
        return rackDescription;
    }

    public void setRackDescription(String rackDescription) {
        this.rackDescription = rackDescription;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }
}
