package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GET_LAB_INSPECTION_SCORE_BY_DEPARTMENT_ID",
                query = "SELECT laboratoryInspectionScore.laboratory_inspection_id AS \"laboratoryInspectionId\", "
                        + " laboratoryInspectionScore.department_id AS \"departmentId\", "
                        + " laboratoryInspectionScore.lab_inspection_id AS \"labInspectionId\", "
                        + " laboratoryInspectionScore.lab_inspection_name AS \"labInspectionName\", "
                        + " laboratoryInspectionScore.lab_inspection_name_id AS \"labInspectionNameId\", "
                        + " laboratoryInspectionScore.lab_inspection_name_flag AS \"labInspectionNameFlag\", "
                        + " to_char(laboratoryInspectionScore.lab_inspection_from_date, 'dd/MM/yyyy') AS \"labInspectionFromDate\", "
                        + " to_char(laboratoryInspectionScore.lab_inspection_to_date, 'dd/MM/yyyy') AS \"labInspectionToDate\", "
                        + " laboratoryInspectionScore.save_as_draft AS \"saveAsDraft\", "
                        + " laboratoryInspectionScore.unit_id AS \"unitId\", "
                        + " laboratoryInspectionScore.organization_id AS \"organizationId\", "
                        + " laboratoryInspectionScore.status AS \"status\", "
                        + " to_char(laboratoryInspectionScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(laboratoryInspectionScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", "
                        + " laboratoryInspectionScore.created_by AS \"createdBy\", "
                        + " laboratoryInspectionScore.updated_by AS \"updatedBy\" "
                        + " FROM adt.t_laboratory_inspection laboratoryInspectionScore "
                        + " WHERE laboratoryInspectionScore.status = 'A' "
                        + " AND laboratoryInspectionScore.save_as_draft = 'Y' "
                        + " AND laboratoryInspectionScore.unit_id = :unitId "
                        + " AND laboratoryInspectionScore.organization_id = :organizationId "
                        + " AND laboratoryInspectionScore.department_id = :departmentId "
                        + " ORDER BY laboratoryInspectionScore.laboratory_inspection_id DESC"
        )
})

@Entity
@Table(name = "t_laboratory_inspection", schema = "adt")
@SequenceGenerator(name = "t_laboratory_inspection_seq", sequenceName = "adt.t_laboratory_inspection_seq", allocationSize = 1)
public class LaboratoryInspectionScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_laboratory_inspection_seq")
    @Column(name = "laboratory_inspection_id")
    private Integer laboratoryInspectionId;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "lab_inspection_id")
    private Integer labInspectionId;

    @Column(name = "lab_inspection_name")
    private String labInspectionName;

    @Column(name = "lab_inspection_name_id")
    private Integer labInspectionNameId;

    @Column(name = "lab_inspection_name_flag")
    private Integer labInspectionNameFlag;

    @Column(name = "lab_inspection_from_date")
    private Date labInspectionFromDate;

    @Column(name = "lab_inspection_to_date")
    private Date labInspectionToDate;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "status")
    private Character status;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Integer getLaboratoryInspectionId() {
        return laboratoryInspectionId;
    }

    public void setLaboratoryInspectionId(Integer laboratoryInspectionId) {
        this.laboratoryInspectionId = laboratoryInspectionId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getLabInspectionId() {
        return labInspectionId;
    }

    public void setLabInspectionId(Integer labInspectionId) {
        this.labInspectionId = labInspectionId;
    }

    public String getLabInspectionName() {
        return labInspectionName;
    }

    public void setLabInspectionName(String labInspectionName) {
        this.labInspectionName = labInspectionName;
    }

    public Integer getLabInspectionNameId() {
        return labInspectionNameId;
    }

    public void setLabInspectionNameId(Integer labInspectionNameId) {
        this.labInspectionNameId = labInspectionNameId;
    }

    public Integer getLabInspectionNameFlag() {
        return labInspectionNameFlag;
    }

    public void setLabInspectionNameFlag(Integer labInspectionNameFlag) {
        this.labInspectionNameFlag = labInspectionNameFlag;
    }

    public Date getLabInspectionFromDate() {
        return labInspectionFromDate;
    }

    public void setLabInspectionFromDate(Date labInspectionFromDate) {
        this.labInspectionFromDate = labInspectionFromDate;
    }

    public Date getLabInspectionToDate() {
        return labInspectionToDate;
    }

    public void setLabInspectionToDate(Date labInspectionToDate) {
        this.labInspectionToDate = labInspectionToDate;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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
}
