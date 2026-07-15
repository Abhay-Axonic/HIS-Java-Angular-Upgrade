package com.param.adt.discharge.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.SpecialityMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST",
		query="SELECT dischargeSummaryTemplateMaster.dischargeSummaryTempId AS dischargeSummaryTempId,"
					+ " dischargeSummaryTemplateMaster.dischargeSummaryTempCode AS dischargeSummaryTempCode,"
					+ " dischargeSummaryTemplateMaster.dischargeSummaryTempDesc AS dischargeSummaryTempDesc,"
					+ " dischargeSummaryTemplateMaster.departmentId AS departmentId,"
					+ " dischargeSummaryTemplateMaster.organizationId AS organizationId,"
					+ " dischargeSummaryTemplateMaster.unitId AS unitId, dischargeSummaryTemplateMaster.status AS status ,"
					+ " dischargeSummaryTemplateMaster.isHtml as isHtml "
					//+ " specialityMaster.specialityName as specialityName "
					+ " FROM DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster"
					//+ " INNER JOIN dischargeSummaryTemplateMaster.specialityMaster specialityMaster"
					+ " WHERE dischargeSummaryTemplateMaster.organizationId= :organizationId"
					+ " AND dischargeSummaryTemplateMaster.unitId= :unitId"
					//+ " ORDER BY dischargeSummaryTemplateMaster.dischargeSummaryTempId asc,dischargeSummaryTemplateMaster.status asc"
//				+" ORDER BY dischargeSummaryTemplateMaster.status asc"
					),

	@NamedQuery(name="GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_COUNT",
		query="SELECT COUNT(*) "
				+ " FROM DischargeSummaryTemplateMaster dstm"
				+ " WHERE dstm.organizationId= :organizationId"
				+ " AND dstm.unitId= :unitId"
	),
	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_ID",
		query="SELECT dischargeSummaryTemplateMaster.dischargeSummaryTempId AS dischargeSummaryTempId,"
				+ " dischargeSummaryTemplateMaster.dischargeSummaryTempCode AS dischargeSummaryTempCode,"
				+ " dischargeSummaryTemplateMaster.dischargeSummaryTempDesc AS dischargeSummaryTempDesc,"
					+ " dischargeSummaryTemplateMaster.departmentId AS departmentId,"
					+ " dischargeSummaryTemplateMaster.organizationId AS organizationId,"
					+ " dischargeSummaryTemplateMaster.unitId AS unitId, dischargeSummaryTemplateMaster.status AS status, "
					//+ " specialityMaster.specialityName as specialityName "
					+ " dischargeSummaryTemplateMaster.isHtml as isHtml "
					+ " FROM DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster"
					//+ " INNER JOIN dischargeSummaryTemplateMaster.specialityMaster specialityMaster"
					+ " WHERE dischargeSummaryTemplateMaster.dischargeSummaryTempId= :dischargeSummaryTempId"
					+ " AND dischargeSummaryTemplateMaster.organizationId= :organizationId"
					+ " AND dischargeSummaryTemplateMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_ACTIVE_DISCHARGE_SUMMARY_TEMPLATE_MASTER",
		query="SELECT dischargeSummaryTemplateMaster.dischargeSummaryTempId AS dischargeSummaryTempId,"
				+ " dischargeSummaryTemplateMaster.dischargeSummaryTempCode AS dischargeSummaryTempCode,"
				+ " dischargeSummaryTemplateMaster.dischargeSummaryTempDesc AS dischargeSummaryTempDesc,"
					+ " dischargeSummaryTemplateMaster.departmentId AS departmentId, dischargeSummaryTemplateMaster.organizationId AS organizationId,"
					+ " dischargeSummaryTemplateMaster.unitId AS unitId, dischargeSummaryTemplateMaster.status AS status,"
					+ " dischargeSummaryTemplateMaster.isHtml as isHtml "
					+ " FROM DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster"
					+ " WHERE dischargeSummaryTemplateMaster.status= 'A'"
					+ " AND dischargeSummaryTemplateMaster.organizationId= :organizationId"
					+ " AND dischargeSummaryTemplateMaster.unitId= :unitId"
					+ " ORDER BY dischargeSummaryTemplateMaster.dischargeSummaryTempId asc"
					),
	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_NAME",
		query="SELECT dischargeSummaryTemplateMaster.dischargeSummaryTempId AS dischargeSummaryTempId "
					+ "	FROM DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster"
					+ " WHERE LOWER(dischargeSummaryTemplateMaster.dischargeSummaryTempDesc)= :dischargeSummaryTempDesc"
					+ " AND dischargeSummaryTemplateMaster.organizationId= :organizationId"
					+ " AND dischargeSummaryTemplateMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_NAME_NOT_ID",
		query="SELECT dischargeSummaryTemplateMaster.dischargeSummaryTempId AS dischargeSummaryTempId"
					+ " FROM DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster"
					+ " WHERE LOWER(dischargeSummaryTemplateMaster.dischargeSummaryTempDesc)= :dischargeSummaryTempDesc"
					+ " AND dischargeSummaryTemplateMaster.dischargeSummaryTempId != :dischargeSummaryTempId"
					+ " AND dischargeSummaryTemplateMaster.organizationId= :organizationId"
					+ " AND dischargeSummaryTemplateMaster.unitId= :unitId"
					),
	
	
	})

@Entity
@Table(name = "m_discharge_summary_template_master", schema = "adt")
@SequenceGenerator(name = "m_discharge_summary_template_master_seq", sequenceName = "adt.m_discharge_summary_template_master_seq", allocationSize = 1)
public class DischargeSummaryTemplateMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_discharge_summary_template_master_seq")
	@Column(name = "discharge_id")
	private Integer dischargeSummaryTempId;
	
	@Column(name = "discharge_code")
	private String dischargeSummaryTempCode;
	
	@Column(name = "discharge_field_id")
	private Integer dischargeFieldId;
	
	@Column(name = "discharge_desc")
	private String dischargeSummaryTempDesc;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
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
	
	@Column(name = "is_html")
	private Character isHtml;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="department_id", insertable=false, updatable=false)
	private SpecialityMaster specialityMaster;
	
//	@ManyToOne(fetch= FetchType.LAZY)
//	@JoinColumn(name="discharge_field_id", insertable=false, updatable=false)
//	private DischargeSummaryFieldMaster dischargeSummaryFieldMaster;
	
	public Integer getDischargeSummaryTempId() {
		return dischargeSummaryTempId;
	}

	public void setDischargeSummaryTempId(Integer dischargeSummaryTempId) {
		this.dischargeSummaryTempId = dischargeSummaryTempId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public String getDischargeSummaryTempCode() {
		return dischargeSummaryTempCode;
	}

	public void setDischargeSummaryTempCode(String dischargeSummaryTempCode) {
		this.dischargeSummaryTempCode = dischargeSummaryTempCode;
	}

	public String getDischargeSummaryTempDesc() {
		return dischargeSummaryTempDesc;
	}

	public void setDischargeSummaryTempDesc(String dischargeSummaryTempDesc) {
		this.dischargeSummaryTempDesc = dischargeSummaryTempDesc;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public Character getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(Character isHtml) {
		this.isHtml = isHtml;
	}

	
	
}