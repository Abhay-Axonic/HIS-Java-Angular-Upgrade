package com.param.global.model;

import javax.persistence.*;
import java.util.Date;
@NamedNativeQueries({
        @NamedNativeQuery(name = "CHECK_BOX_EXISTS_OR_NOT",
                query = "SELECT EXISTS(select 1 from m_mrd_box_master  " +
                        "where (box_description like :boxDescription or box_code like :boxCode) " +
                        "and organization_id =:organizationId)"),

        @NamedNativeQuery(name = "GET_ACTIVE_MRD_BOX_LIST",
                query = "SELECT box_id as \"boxId\",box_code as \"boxCode\",box_description as \"boxDescription\",unit_id as \"unitId\"," +
                        "organization_id as \"organizationId\" FROM m_mrd_box_master " +
                        "WHERE status='A' AND organization_id =:orgId  AND unit_id =:unitId " +
                        "ORDER BY box_description ASC "),
        @NamedNativeQuery(name = "GET_MRD_BOX_BY_ID",
                query = "SELECT box_id as \"boxId\",box_code as \"boxCode\",box_description as \"boxDescription\"," +
                        "status as \"status\",unit_id as \"unitId\",organization_id as \"organizationId\" " +
                        "FROM m_mrd_box_master " +
                        "WHERE box_id =:boxId and organization_id =:orgId " +
                        "and unit_id =:unitId"),
        @NamedNativeQuery(name = "GET_MRD_BOX_LIST",
                query = "SELECT box_id as \"boxId\",box_code as \"boxCode\",box_description as \"boxDescription\"," +
                        "status as \"status\",unit_id as \"unitId\",organization_id as \"organizationId\" " +
                        "FROM m_mrd_box_master " +
                        "WHERE organization_id =:orgId " +
                        "and unit_id =:unitId ORDER BY box_id DESC " ),
//                        "OFFSET (:page * :size) " +
//                        "LIMIT :size"),
        @NamedNativeQuery(name = "GET_MRD_BOX_LIST_COUNT",
                query = "SELECT count(*) " +
                        "FROM m_mrd_box_master " +
                        "WHERE organization_id =:orgId " +
                        "and unit_id =:unitId"),
        @NamedNativeQuery(name = "CHECK_BOX_EXISTS_OR_NOT_AND_EXCLUDES_BOX_ID",
                query = "SELECT EXISTS(select 1 from m_mrd_box_master  " +
                        "where (box_description like :boxDescription or box_code like :boxCode) " +
                        "and organization_id =:organizationId and  box_id !=:boxId )")
})
@Entity
@Table(name = "m_mrd_box_master", schema = "public")
@SequenceGenerator(name = "mrd_box_master_seq", sequenceName = "public.mrd_box_master_seq", allocationSize = 1)
public class BoxMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mrd_box_master_seq")
    @Column(name = "box_id")
    private int boxId;

    @Column(name = "box_code")
    private String boxCode;

    @Column(name = "box_description")
    private String boxDescription;


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

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public String getBoxDescription() {
        return boxDescription;
    }

    public void setBoxDescription(String boxDescription) {
        this.boxDescription = boxDescription;
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
