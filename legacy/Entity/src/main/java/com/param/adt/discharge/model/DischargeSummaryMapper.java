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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.RouteOfAdministration;
@NamedQueries({
	@NamedQuery(name="GET_DISCHARGE_SUMMARY_DETAIL_BY_DISCHARGE_SUMMARY_ID_AND_FIELD_ID",
		query="SELECT dischargeSummary.dischargeSummaryDetailId AS dischargeSummaryDetailId,dischargeSummary.dischargeSummaryId AS dischargeSummaryId,"
					+ " dischargeSummary.dischargeFieldId AS dischargeFieldId,"
					+ " dischargeSummary.dischargeFieldDescription AS dischargeFieldDescription,"
					+ " dischargeSummary.status AS status,"
					+ " dischargeSummary.updatedBy AS updatedBy,dischargeSummary.createdBy AS createdBy,"
					+ " dischargeSummary.updatedDate AS updatedDate,dischargeSummary.createdDate AS createdDate "
					+ " FROM DischargeSummaryMapper dischargeSummary"
					+ " WHERE dischargeSummary.dischargeSummaryId= :dischargeSummaryId"
					+ " AND dischargeSummary.dischargeFieldId= :dischargeFieldId"
					+ " AND dischargeSummary.organizationId= :organizationId"
					+ " AND dischargeSummary.unitId= :unitId"
					)
	})
@Entity
@Table(name = "m_discharge_Summary_detail", schema = "adt")
@SequenceGenerator(name = "m_discharge_summary_detail_seq", sequenceName = "adt.m_discharge_Summary_detail_seq", allocationSize = 1)
public class DischargeSummaryMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_discharge_summary_detail_seq")
	@Column(name = "discharge_summary_detail_id")
	private Integer dischargeSummaryDetailId;
	
	@Column(name = "discharge_summary_id")
	private Integer dischargeSummaryId;
	
	@Column(name = "discharge_field_id")
	private Integer dischargeFieldId;
	
	@Column(name = "discharge_field_description")
	private String dischargeFieldDescription;
	
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
	
	@Column(name = "discharge_summary_data_view_option_id")
	private Integer dischargeSummaryDataViewOptionId;
	
	/*@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="discharge_summary_id", insertable=false, updatable=false)
	private DischargeSummary dischargeSummary;*/

	public Integer getDischargeSummaryDetailId() {
		return dischargeSummaryDetailId;
	}

	public void setDischargeSummaryDetailId(Integer dischargeSummaryDetailId) {
		this.dischargeSummaryDetailId = dischargeSummaryDetailId;
	}

	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}

	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public String getDischargeFieldDescription() {
		return dischargeFieldDescription;
	}

	public void setDischargeFieldDescription(String dischargeFieldDescription) {
		this.dischargeFieldDescription = dischargeFieldDescription;
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

	public Integer getDischargeSummaryDataViewOptionId() {
		return dischargeSummaryDataViewOptionId;
	}

	public void setDischargeSummaryDataViewOptionId(Integer dischargeSummaryDataViewOptionId) {
		this.dischargeSummaryDataViewOptionId = dischargeSummaryDataViewOptionId;
	}
	
	

}
