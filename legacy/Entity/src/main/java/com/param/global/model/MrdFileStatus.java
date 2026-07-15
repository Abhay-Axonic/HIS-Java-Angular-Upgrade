package com.param.global.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({

        @NamedNativeQuery(name = "GET_FILE_STATUS_ID",
        query = "select file_status_id as \"statusId\" from adt.m_mrd_file_status where file_status_description =:status ")
})
@Entity
@Table(name="m_mrd_file_status",schema="adt")
public class MrdFileStatus {
    @Id
    @Column(name="file_status_id")
    private Integer fileStatusId;

    @Column(name="file_status_code")
    private String fileStatusCode;

    @Column(name="file_status_description")
    private String fileStatusDescription;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="created_date")
    private Date createdDate;

    @Column(name="updated_by")
    private Integer updatedBy;

    @Column(name="updated_date")
    private Date updatedDate;

    @Column(name="status")
    private char status;

    @Column(name="unit_id")
    private Integer unitId;

    @Column(name="organization_id")
    private Integer organizationId;

    public Integer getFileStatusId() {
        return fileStatusId;
    }

    public void setFileStatusId(Integer fileStatusId) {
        this.fileStatusId = fileStatusId;
    }

    public String getFileStatusCode() {
        return fileStatusCode;
    }

    public void setFileStatusCode(String fileStatusCode) {
        this.fileStatusCode = fileStatusCode;
    }

    public String getFileStatusDescription() {
        return fileStatusDescription;
    }

    public void setFileStatusDescription(String fileStatusDescription) {
        this.fileStatusDescription = fileStatusDescription;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
}
