package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_admitted_patient_orders_workbench", schema = "adt")
@SequenceGenerator(name = "admitted_patient_orders_workbench_seq", sequenceName = "adt.admitted_patient_orders_workbench_seq", allocationSize = 1)
public class AdmittedPatientOrdersWorkbench {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admitted_patient_orders_workbench_seq")
    @Column(name = "patient_workbench_id")
    private Integer patientWorkbenchId;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "tab_id")
    private Integer tabId;

    @Column(name = "result_id")
    private Integer resultId;

    @Column(name = "workbench_added_date")
    private Date workbenchAddedDate;

    @Column(name = "workbench_ended_date")
    private Date workbenchEndDate;

    @Column(name = "remark")
    private String remark;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "created_date",updatable = false)
    private Date createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getWorkbenchAddedDate() {
        return workbenchAddedDate;
    }

    public void setWorkbenchAddedDate(Date workbenchAddedDate) {
        this.workbenchAddedDate = workbenchAddedDate;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public Integer getTabId() {
        return tabId;
    }

    public void setTabId(Integer tabId) {
        this.tabId = tabId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getPatientWorkbenchId() {
        return patientWorkbenchId;
    }

    public void setPatientWorkbenchId(Integer patientWorkbenchId) {
        this.patientWorkbenchId = patientWorkbenchId;
    }

    public Date getWorkbenchEndDate() {
        return workbenchEndDate;
    }

    public void setWorkbenchEndDate(Date workbenchEndDate) {
        this.workbenchEndDate = workbenchEndDate;
    }
}
