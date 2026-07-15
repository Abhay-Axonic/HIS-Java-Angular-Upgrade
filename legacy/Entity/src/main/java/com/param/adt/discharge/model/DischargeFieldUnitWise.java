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
	
	@NamedNativeQuery(name="GET_DISCHARGE_FIELD_UNIT_WISE_LIST",
			query = "SELECT dischargeFieldUnitWise.discharge_field_unit_wise_id AS \"dischargeFieldUnitWiseId\", dischargeFieldUnitWise.discharge_field_id AS \"dischargeFieldId\", dischargeFieldUnitWise.discharge_changed_name AS \"dischargeChangedName\", dischargeSummaryFieldMaster.discharge_field_desc AS \"dischargeFieldDesc\", dischargeFieldUnitWise.organization_id \"organizationId\", "
			        + " dischargeFieldUnitWise.unit_id AS \"unitId\", dischargeFieldUnitWise.created_by AS \"createdBy\", to_char(dischargeFieldUnitWise.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDate\", dischargeFieldUnitWise.updated_by \"updatedBy\", to_char(dischargeFieldUnitWise.updated_date, 'DD/MM/YYYY HH24:MI:SS') \"updatedDate\", dischargeFieldUnitWise.status \"status\" "
					+ " FROM public.m_discharge_field_unit_wise dischargeFieldUnitWise "
					+ " inner JOIN adt.m_discharge_summary_field_master as dischargeSummaryFieldMaster "
					+ " ON dischargeFieldUnitWise.discharge_field_id = dischargeSummaryFieldMaster.discharge_field_id "
					+ " WHERE dischargeFieldUnitWise.organization_id = :organizationId "
					+ " AND dischargeFieldUnitWise.unit_id = :unitId "
					+ " AND dischargeSummaryFieldMaster.status ='A' "
					+ " AND dischargeFieldUnitWise.status ='A' "
					),

		@NamedNativeQuery(name="GET_DISCHARGE_FIELD_UNIT_WISE_COUNT",
				query = "SELECT count(*) "
						+ " FROM public.m_discharge_field_unit_wise dischargeFieldUnitWise "
						+ " inner JOIN adt.m_discharge_summary_field_master as dischargeSummaryFieldMaster "
						+ " ON dischargeFieldUnitWise.discharge_field_id = dischargeSummaryFieldMaster.discharge_field_id "
						+ " WHERE dischargeFieldUnitWise.organization_id = :organizationId "
						+ " AND dischargeFieldUnitWise.unit_id = :unitId "
						+ " AND dischargeSummaryFieldMaster.status ='A' "
						+ " AND dischargeFieldUnitWise.status ='A' "
		),
	
	@NamedNativeQuery(name="GET_ACTIVE_DISCHARGE_FIELD_UNIT_WISE_TEMPLATE",
	query = "SELECT dischargeFieldUnitWise.discharge_field_unit_wise_id AS \"dischargeFieldUnitWiseId\", dischargeFieldUnitWise.discharge_field_id AS \"dischargeFieldId\", dischargeFieldUnitWise.discharge_changed_name AS \"dischargeChangedName\", dischargeFieldUnitWise.discharge_changed_name AS \"dischargeFieldDesc\", dischargeFieldUnitWise.organization_id \"organizationId\", "
	        + "dischargeFieldUnitWise.unit_id AS \"unitId\", dischargeFieldUnitWise.created_by AS \"createdBy\", to_char(dischargeFieldUnitWise.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDate\", dischargeFieldUnitWise.updated_by \"updatedBy\", to_char(dischargeFieldUnitWise.updated_date, 'DD/MM/YYYY HH24:MI:SS') \"updatedDate\", dischargeFieldUnitWise.status \"status\" "
			+ "FROM public.m_discharge_field_unit_wise dischargeFieldUnitWise "
			+ "inner JOIN adt.m_discharge_summary_field_master as dischargeSummaryFieldMaster "
			+ "ON dischargeFieldUnitWise.discharge_field_id = dischargeSummaryFieldMaster.discharge_field_id "
			+ "WHERE dischargeFieldUnitWise.organization_id = :organizationId "
			+ "AND dischargeFieldUnitWise.unit_id = :unitId "
			+ " AND dischargeSummaryFieldMaster.status ='A' "
			+ " AND dischargeFieldUnitWise.status ='A' "
			+ " ORDER BY dischargeFieldUnitWise.discharge_changed_name ASC "
			),
	
	@NamedNativeQuery(name="GET_ACTIVE_DISCHARGE_FIELD_UNIT_WISE",
			query = "SELECT dischargeFieldUnitWise.discharge_field_unit_wise_id AS \"dischargeFieldUnitWiseId\", dischargeFieldUnitWise.discharge_field_id AS \"dischargeFieldId\", dischargeFieldUnitWise.discharge_changed_name AS \"dischargeChangedName\", dischargeSummaryFieldMaster.discharge_field_desc AS \"dischargeFieldDesc\", dischargeFieldUnitWise.organization_id \"organizationId\", "
					+ "dischargeFieldUnitWise.unit_id AS \"unitId\", dischargeFieldUnitWise.created_by AS \"createdBy\", to_char(dischargeFieldUnitWise.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"createdDate\", dischargeFieldUnitWise.updated_by \"updatedBy\", to_char(dischargeFieldUnitWise.updated_date, 'DD/MM/YYYY HH24:MI:SS') \"updatedDate\", dischargeFieldUnitWise.status \"status\" "
					+ "FROM public.m_discharge_field_unit_wise dischargeFieldUnitWise "
					+ "INNER JOIN adt.m_discharge_summary_field_master as dischargeSummaryFieldMaster "
					+ "ON dischargeFieldUnitWise.discharge_field_unit_wise_id = dischargeSummaryFieldMaster.discharge_field_id "
					+ "WHERE dischargeFieldUnitWise.status = 'A' "
					+ "AND dischargeFieldUnitWise.organization_id = :organizationId "
					+ "AND dischargeFieldUnitWise.unit_id = :unitId "
					+ "ORDER BY dischargeFieldUnitWise.discharge_field_unit_wise_id"
					)	,

		@NamedNativeQuery(name = "GET_DISCHARGE_FIELD_UNIT_WISE_BY_ID",
				query = "SELECT dfuw.discharge_field_unit_wise_id as \"dischargeFieldUnitWiseId\", dfuw.discharge_field_id as \"dischargeFieldId\" , dsfm.discharge_field_desc AS \"dischargeFieldDesc\", "
						+ "dfuw.discharge_changed_name as \"dischargeChangedName\" , dfuw.status as \"status\" , dfuw.unit_id as \"unitId\" , dfuw.organization_id as \"organizationId\"  "
						+ "FROM public.m_discharge_field_unit_wise dfuw "
						+ "INNER JOIN adt.m_discharge_summary_field_master dsfm "
						+ "ON dfuw.discharge_field_id = dsfm.discharge_field_id "
						+ "WHERE dfuw.discharge_field_unit_wise_id = :dischargeFieldUnitWiseId "
		)
	
})

@NamedQueries({

		@NamedQuery(name = "GET_DISCHARGE_FIELD_UNIT_WISE_BY_UNITWISEID",
				query = "SELECT dfuw.dischargeFieldUnitWiseId as dischargeFieldUnitWiseId "
						+ "FROM DischargeFieldUnitWise dfuw "
						+ "WHERE dfuw.dischargeFieldUnitWiseId = :dischargeFieldUnitWiseId "
//						+ "AND dfuw.dischargeFieldId =:dischargeFieldId"
						)

})

@Entity
@Table(name = "m_discharge_field_unit_wise", schema = "public")
@SequenceGenerator(name = "seq_m_discharge_field_unit_wise", sequenceName = "public.seq_m_discharge_field_unit_wise", allocationSize = 1)
public class DischargeFieldUnitWise {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_discharge_field_unit_wise")
	@Column(name = "discharge_field_unit_wise_id")
	private Integer dischargeFieldUnitWiseId;

	@Column(name = "discharge_field_id")
	private Integer dischargeFieldId;

	@Column(name = "discharge_changed_name")
	private String dischargeChangedName;

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

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getDischargeFieldUnitWiseId() {
		return dischargeFieldUnitWiseId;
	}

	public void setDischargeFieldUnitWiseId(Integer dischargeFieldUnitWiseId) {
		this.dischargeFieldUnitWiseId = dischargeFieldUnitWiseId;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public String getDischargeChangedName() {
		return dischargeChangedName;
	}

	public void setDischargeChangedName(String dischargeChangedName) {
		this.dischargeChangedName = dischargeChangedName;
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
