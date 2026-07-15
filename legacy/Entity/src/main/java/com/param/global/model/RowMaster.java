package com.param.global.model;

import javax.persistence.*;
import java.util.Date;
@NamedNativeQueries({
        @NamedNativeQuery(name = "CHECK_ROW_EXISTS_OR_NOT",
                query = "SELECT EXISTS(select 1 from m_mrd_row_master  " +
                        "where (row_description like :rowDescription or row_code like :rowCode) " +
                        "and organization_id =:organizationId)"),

        @NamedNativeQuery(name = "GET_ACTIVE_MRD_ROW_LIST",
                query = "SELECT row_id as \"rowId\",row_code as \"rowCode\",row_description as \"rowDescription\",unit_id as \"unitId\"," +
                        "organization_id as \"organizationId\" FROM m_mrd_row_master " +
                        "WHERE status='A' AND organization_id =:orgId  AND unit_id =:unitId " +
                        "ORDER BY row_description ASC "),
        @NamedNativeQuery(name = "GET_MRD_ROW_BY_ID",
                query = "SELECT row_id as \"rowId\",row_code as \"rowCode\",row_description as \"rowDescription\"," +
                        "status as \"status\",unit_id as \"unitId\",organization_id as \"organizationId\" " +
                        "FROM m_mrd_row_master " +
                        "WHERE row_id =:rowId and organization_id =:orgId " +
                        "and unit_id =:unitId"),
        @NamedNativeQuery(name = "GET_MRD_ROW_LIST",
                query = "SELECT row_id as \"rowId\",row_code as \"rowCode\",row_description as \"rowDescription\"," +
                        "status as \"status\",unit_id as \"unitId\",organization_id as \"organizationId\" " +
                        "FROM m_mrd_row_master " +
                        "WHERE organization_id =:orgId " +
                        "and unit_id =:unitId ORDER BY row_id DESC " ),
//                        "OFFSET (:page * :size) " +
//                        "LIMIT :size"),
        @NamedNativeQuery(name = "GET_MRD_ROW_LIST_COUNT",
                query = "SELECT count(*) " +
                        "FROM m_mrd_row_master " +
                        "WHERE organization_id =:orgId " +
                        "and unit_id =:unitId"),
        @NamedNativeQuery(name = "CHECK_ROW_EXISTS_OR_NOT_AND_EXCLUDES_ROW_ID",
                query = "SELECT EXISTS(select 1 from m_mrd_row_master  " +
                        "where (row_description like :rowDescription or row_code like :rowCode) " +
                        "and organization_id =:organizationId and  row_id !=:rowId )")
})
@Entity
@Table(name = "m_mrd_row_master", schema = "public")
@SequenceGenerator(name = "mrd_row_master_seq", sequenceName = "public.mrd_row_master_seq", allocationSize = 1)
public class RowMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mrd_row_master_seq")
    @Column(name = "row_id")
    private int rowId;

    @Column(name = "row_code")
    private String rowCode;

    @Column(name = "row_description")
    private String rowDescription;


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
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getRowCode() {
        return rowCode;
    }

    public void setRowCode(String rowCode) {
        this.rowCode = rowCode;
    }

    public String getRowDescription() {
        return rowDescription;
    }

    public void setRowDescription(String rowDescription) {
        this.rowDescription = rowDescription;
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
