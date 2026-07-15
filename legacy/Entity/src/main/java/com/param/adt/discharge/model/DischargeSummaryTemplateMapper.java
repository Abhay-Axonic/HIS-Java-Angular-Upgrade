package com.param.adt.discharge.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_DETAILED_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST",
     query="SELECT dischargeSummaryTemplateMaster.discharge_summary_temp_id AS \"dischargeSummaryTempId\",dischargeSummaryTemplateMaster.discharge_field_id AS \"dischargeFieldId\", "
    	    + " field1.discharge_changed_name AS \"dischargeFieldDesc\" "
    	    + " FROM adt.t_discharge_summary_template_mapper dischargeSummaryTemplateMaster"
			+ " inner join adt.m_discharge_summary_field_master field on dischargeSummaryTemplateMaster.discharge_field_id = field.discharge_field_id  "
			+ " inner join public.m_discharge_field_unit_wise field1 on dischargeSummaryTemplateMaster.discharge_field_id = field1.discharge_field_id  "
			+ " WHERE dischargeSummaryTemplateMaster.organization_id= :organizationId"
			+ " AND dischargeSummaryTemplateMaster.unit_id= :unitId"
			+ " AND dischargeSummaryTemplateMaster.discharge_summary_temp_id= :dischargeSummaryTempId"
			 + " and field1.organization_id =:organizationId"
			 + " and field1.unit_id  = :unitId "
			 + " and field.status ='A' "
			 + " and field1.status ='A' "
			+ " AND dischargeSummaryTemplateMaster.status='A' order by discharge_summary_temp_mppr_id asc "
			),
	@NamedNativeQuery(name="GET_DETAILED_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_DISCHARGE_FIELD_ID",
    query="SELECT dischargeSummaryTemplateMaster.discharge_summary_temp_mppr_id As \"dischargeSummaryTempMpprId\",dischargeSummaryTemplateMaster.discharge_summary_temp_id AS \"dischargeSummaryTempId\",dischargeSummaryTemplateMaster.discharge_field_id AS \"dischargeFieldId\", "
    		+ " dischargeSummaryTemplateMaster.status AS \"status\",dischargeSummaryTemplateMaster.created_by AS\"createdBy\","
    		+ " dischargeSummaryTemplateMaster.updated_by AS \"updatedBy\",dischargeSummaryTemplateMaster.created_date AS\"createdDate\","
    		+ " dischargeSummaryTemplateMaster.organization_id AS \"organizationId\",dischargeSummaryTemplateMaster.unit_id AS\"unitId\" "
    		+ " FROM adt.t_discharge_summary_template_mapper dischargeSummaryTemplateMaster"
			+ " WHERE dischargeSummaryTemplateMaster.discharge_summary_temp_id= :dischargeSummaryTempId"
			+ " AND dischargeSummaryTemplateMaster.discharge_field_id= :dischargeFieldId "
			+ " AND dischargeSummaryTemplateMaster.status='A' "
			),
	
	@NamedNativeQuery(name="GET_DETAILED_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_DEPARTMENT_ID",
    query="SELECT mppr.discharge_temp_dept_mppr_id as \"dischargeTempDeptMpprId\" , mppr.discharge_summary_temp_id AS \"dischargeSummaryTempId\",mppr.department_id AS \"departmentId\", "
    		+ " mppr.status AS \"status\",mppr.created_by AS\"createdBy\","
    		+ " mppr.updated_by AS \"updatedBy\",to_char(mppr.created_date, 'yyyy-mm-dd HH:mm:ss') AS\"createdDate\","
    		+ " mppr.organization_id AS \"organizationId\",mppr.unit_id AS\"unitId\" "
    		+ " FROM adt.t_discharge_template_department_mapper mppr"
			+ " WHERE mppr.discharge_summary_temp_id= :dischargeSummaryTempId"
			+ " AND mppr.department_id= :dischargeFieldId"
			)
	
})

@Entity
@Table(name = "t_discharge_summary_template_mapper", schema = "adt")
@SequenceGenerator(name = "t_discharge_summary_template_mapper_seq", sequenceName = "adt.t_discharge_summary_template_mapper_seq", allocationSize = 1)
public class DischargeSummaryTemplateMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_discharge_summary_template_mapper_seq")
	
	@Column(name = "discharge_summary_temp_mppr_id")
	private Integer dischargeSummaryTempMpprId;
	
	@Column(name = "discharge_summary_temp_id")
	private Integer dischargeSummaryTempId;

	@Column(name = "discharge_field_id")
	private Integer dischargeFieldId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	public Integer getDischargeSummaryTempMpprId() {
		return dischargeSummaryTempMpprId;
	}

	public void setDischargeSummaryTempMpprId(Integer dischargeSummaryTempMpprId) {
		this.dischargeSummaryTempMpprId = dischargeSummaryTempMpprId;
	}

	public Integer getDischargeSummaryTempId() {
		return dischargeSummaryTempId;
	}

	public void setDischargeSummaryTempId(Integer dischargeSummaryTempId) {
		this.dischargeSummaryTempId = dischargeSummaryTempId;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
