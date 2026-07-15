package com.param.adt.discharge.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.param.global.model.StateMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_FIELD_MASTER_LIST",
		query="SELECT dischargeSummaryFieldMaster.dischargeFieldId AS dischargeFieldId,dischargeSummaryFieldMaster.defaultField AS defaultField,  dischargeSummaryFieldMaster.dischargeFieldCode AS dischargeFieldCode, dischargeSummaryFieldMaster.dischargeFieldDesc AS dischargeFieldDesc, dischargeSummaryFieldMaster.dischargeFieldTemplateObj AS dischargeFieldTemplateObj,"
					+ " dischargeSummaryFieldMaster.summaryFieldDesc AS summaryFieldDesc, dischargeSummaryFieldMaster.organizationId AS organizationId, dischargeSummaryFieldMaster.unitId AS unitId, dischargeSummaryFieldMaster.status AS status"
					+ " FROM DischargeSummaryFieldMaster dischargeSummaryFieldMaster "
					
					+ " WHERE (dischargeSummaryFieldMaster.organizationId is null"
					+ " OR dischargeSummaryFieldMaster.organizationId= :organizationId)"
//					+ " AND dischargeSummaryFieldMaster.unitId= :unitId"
					),

	@NamedQuery(name="GET_DISCHARGE_SUMMARY_FIELD_MASTER_COUNT",
		query="SELECT COUNT(*) "
				    + " FROM DischargeSummaryFieldMaster dischargeSummaryFieldMaster "
					+ " WHERE (dischargeSummaryFieldMaster.organizationId is null"
					+ " OR dischargeSummaryFieldMaster.organizationId= :organizationId)"
	),

	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_ID",
		query="SELECT dischargeSummaryFieldMaster.dischargeFieldId AS dischargeFieldId, dischargeSummaryFieldMaster.defaultField AS defaultField, dischargeSummaryFieldMaster.dischargeFieldCode AS dischargeFieldCode, dischargeSummaryFieldMaster.dischargeFieldDesc AS dischargeFieldDesc,dischargeSummaryFieldMaster.dischargeFieldTemplateObj AS dischargeFieldTemplateObj,"
					+ " dischargeSummaryFieldMaster.summaryFieldDesc AS summaryFieldDesc, dischargeSummaryFieldMaster.organizationId AS organizationId, dischargeSummaryFieldMaster.unitId AS unitId, dischargeSummaryFieldMaster.status AS status"
					+ " FROM DischargeSummaryFieldMaster dischargeSummaryFieldMaster"
					+ " WHERE dischargeSummaryFieldMaster.dischargeFieldId=:dischargeFieldId"
//					+ " AND dischargeSummaryFieldMaster.organizationId= :organizationId"
//					+ " AND dischargeSummaryFieldMaster.unitId= :unitId"
					),
	
//	@NamedQuery(name="GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER",
//		query="SELECT dischargeSummaryFieldMaster.dischargeFieldId AS dischargeFieldId, dischargeSummaryFieldMaster.dischargeFieldCode AS dischargeFieldCode, dfuw.dischargeChangedName AS dischargeFieldDesc, dischargeSummaryFieldMaster.dischargeFieldTemplateObj AS dischargeFieldTemplateObj,"
//					+ " dischargeSummaryFieldMaster.summaryFieldDesc AS summaryFieldDesc, dischargeSummaryFieldMaster.organizationId AS organizationId, dischargeSummaryFieldMaster.unitId AS unitId, dischargeSummaryFieldMaster.status AS status"
//					+ " FROM DischargeSummaryFieldMaster dischargeSummaryFieldMaster "
//					+ " INNER JOIN dischargeSummaryFieldMaster.dischargeFieldUnitWise dfuw"
//					+ " WHERE dischargeSummaryFieldMaster.status= 'A' "
//					+ " AND dischargeSummaryFieldMaster.organizationId= :organizationId"
//					+ " ORDER BY dischargeSummaryFieldMaster.dischargeFieldId asc"
////					+ " AND dischargeSummaryFieldMaster.unitId= :unitId"
//
//					),

 	@NamedQuery(name="GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MAPPER_MASTER",
		query="SELECT dischargeSummaryFieldMaster.dischargeFieldId AS dischargeFieldId, dischargeSummaryFieldMaster.dischargeFieldCode AS dischargeFieldCode, dischargeSummaryFieldMaster.dischargeFieldDesc AS dischargeFieldDesc, dischargeSummaryFieldMaster.dischargeFieldTemplateObj AS dischargeFieldTemplateObj,"
					+ " dischargeSummaryFieldMaster.summaryFieldDesc AS summaryFieldDesc, dischargeSummaryFieldMaster.organizationId AS organizationId, dischargeSummaryFieldMaster.unitId AS unitId, dischargeSummaryFieldMaster.status AS status"
					+ " FROM DischargeSummaryFieldMaster dischargeSummaryFieldMaster "
//					+ " INNER JOIN dischargeSummaryFieldMaster.dischargeFieldUnitWise dfuw"
					+ " WHERE dischargeSummaryFieldMaster.status= 'A' "
    				+ " AND dischargeSummaryFieldMaster.organizationId= :organizationId"
    				+ " OR dischargeSummaryFieldMaster.organizationId is null"
					+ " ORDER BY dischargeSummaryFieldMaster.dischargeFieldDesc asc "
//					+ " AND dischargeSummaryFieldMaster.unitId= :unitId"

					),
	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_NAME",
		query="SELECT dischargeSummaryFieldMaster.dischargeFieldId AS dischargeFieldId "
					+ "	FROM DischargeSummaryFieldMaster dischargeSummaryFieldMaster "
					+ " WHERE LOWER(dischargeSummaryFieldMaster.dischargeFieldDesc)=:dischargeFieldDesc"
//					+ " AND dischargeSummaryFieldMaster.organizationId= :organizationId"
//					+ " AND dischargeSummaryFieldMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_NAME_NOT_ID",
		query="SELECT dischargeSummaryFieldMaster.dischargeFieldId AS dischargeFieldId "
					+ " FROM DischargeSummaryFieldMaster dischargeSummaryFieldMaster "
					+ " WHERE LOWER(dischargeSummaryFieldMaster.dischargeFieldDesc)=:dischargeFieldDesc"
					+ " AND dischargeSummaryFieldMaster.dischargeFieldId !=:dischargeFieldId"
//					+ " AND dischargeSummaryFieldMaster.organizationId= :organizationId"
//					+ " AND dischargeSummaryFieldMaster.unitId= :unitId"
					),
	})

@NamedNativeQueries({

		@NamedNativeQuery(name="GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER",
				query="SELECT dischargeSummaryFieldMaster.discharge_field_id AS \"dischargeFieldId\",dischargeSummaryFieldMaster.default_field AS defaultField, dischargeSummaryFieldMaster.discharge_field_code AS \"dischargeFieldCode\", dfuw.discharge_changed_name AS \"dischargeFieldDesc\", dischargeSummaryFieldMaster.discharge_field_template_obj AS \"dischargeFieldTemplateObj\","
						+ " dischargeSummaryFieldMaster.summary_field_desc AS \"summaryFieldDesc\", dischargeSummaryFieldMaster.organization_id AS \"organizationId\", dischargeSummaryFieldMaster.unit_id AS \"unitId\", dischargeSummaryFieldMaster.status AS \"status\""
						+ " FROM adt.m_discharge_summary_field_master dischargeSummaryFieldMaster "
						+ " INNER JOIN public.m_discharge_field_unit_wise dfuw"
						+ " ON dischargeSummaryFieldMaster.discharge_field_id = dfuw.discharge_field_id"
						+ " WHERE dischargeSummaryFieldMaster.status= 'A'"
						+ " AND dischargeSummaryFieldMaster.organization_id= :organizationId"
						+ " ORDER BY dischargeSummaryFieldMaster.discharge_field_id asc"
//					+ " AND dischargeSummaryFieldMaster.unitId= :unitId"

		)

})

@Entity
@Table(name = "m_discharge_summary_field_master", schema = "adt")
@SequenceGenerator(name = "m_discharge_summary_field_master_seq", sequenceName = "adt.m_discharge_summary_field_master_seq", allocationSize = 1)
public class DischargeSummaryFieldMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_discharge_summary_field_master_seq")
	@Column(name = "discharge_field_id")
	private Integer dischargeFieldId;
	
	@Column(name = "discharge_field_code")
	private String dischargeFieldCode;
	
	@Column(name = "discharge_field_desc")
	private String dischargeFieldDesc;
	
	@Column(name = "summary_field_desc")
	private String summaryFieldDesc;
	
	@Column(name = "discharge_field_template_obj")
	private String dischargeFieldTemplateObj;
	
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
	
	@Column(name = "discharge_field_column_json")
	private String dischargeFieldColumnJson;
	
	@Column(name = "default_field")
	private Character defaultField;
	

	@OneToOne
	@JoinColumn(name="discharge_field_id")
	private DischargeFieldUnitWise dischargeFieldUnitWise;

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public String getDischargeFieldCode() {
		return dischargeFieldCode;
	}

	public void setDischargeFieldCode(String dischargeFieldCode) {
		this.dischargeFieldCode = dischargeFieldCode;
	}

	public String getDischargeFieldDesc() {
		return dischargeFieldDesc;
	}

	public void setDischargeFieldDesc(String dischargeFieldDesc) {
		this.dischargeFieldDesc = dischargeFieldDesc;
	}

	public String getSummaryFieldDesc() {
		return summaryFieldDesc;
	}

	public void setSummaryFieldDesc(String summaryFieldDesc) {
		this.summaryFieldDesc = summaryFieldDesc;
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

	public String getDischargeFieldTemplateObj() {
		return dischargeFieldTemplateObj;
	}

	public void setDischargeFieldTemplateObj(String dischargeFieldTemplateObj) {
		this.dischargeFieldTemplateObj = dischargeFieldTemplateObj;
	}

	public String getDischargeFieldColumnJson() {
		return dischargeFieldColumnJson;
	}

	public void setDischargeFieldColumnJson(String dischargeFieldColumnJson) {
		this.dischargeFieldColumnJson = dischargeFieldColumnJson;
	}

	public Character getDefaultField() {
		return defaultField;
	}

	public void setDefaultField(Character defaultField) {
		this.defaultField = defaultField;
	}
	
	
}
