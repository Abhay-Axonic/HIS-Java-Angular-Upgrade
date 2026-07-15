package com.param.otims.model;

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

@NamedQueries({
	@NamedQuery(name="GET_SUBSEQUENT_DETAILS_LIST_BY_ID",
			query="SELECT"
					+ " subSequentDetails.subSequentDetailsId AS subSequentDetailsId, "
					+ " subSequentDetails.subSequentProcConfigId AS subSequentProcConfigId,"
					+ " subSequentDetails.sequenceId AS sequenceId,"
					+ " subSequentDetails.discPercentage AS discPercentage, "
					+ " subSequentDetails.status AS status, "
					+ " subSequentDetails.createdBy AS createdBy, "
					+ " subSequentDetails.updatedBy AS updatedBy, "
					+ " to_char(subSequentDetails.createdDate, 'dd-MM-yyyy HH:MI:SS') AS createdDate, "
					+ " to_char(subSequentDetails.updatedDate,'dd-MM-yyyy HH:MI:SS') AS updatedDate, "
					+ " subSequentDetails.orgId AS orgId, "
					+ " subSequentDetails.unitId AS unitId "
					+ " FROM SubSequentDetails subSequentDetails "
					+ " WHERE subSequentDetails.unitId=:unitId "
					+ " AND subSequentDetails.orgId=:orgId "
					+ " AND subSequentDetails.subSequentProcConfigId = :subSequentProcConfigId "
					+ " AND subSequentDetails.status='A' "
					+ " order by sequenceId asc "),
	
	@NamedQuery(name="GET_SUBSEQUENT_DETAILS_LIST_BY_SPECIALITY_ID",
	query="SELECT"
			+ " subSequentDetails.subSequentDetailsId AS subSequentDetailsId, "
			+ " subSequentDetails.subSequentProcConfigId AS subSequentProcConfigId,"
			+ " subSequentDetails.sequenceId AS sequenceId,"
			+ " subSequentDetails.discPercentage AS discPercentage, "
			+ " subSequentDetails.status AS status, "
			+ " subSequentDetails.createdBy AS createdBy, "
			+ " subSequentDetails.updatedBy AS updatedBy, "
			+ " to_char(subSequentDetails.createdDate, 'dd-MM-yyyy HH:MI:SS') AS createdDate, "
			+ " to_char(subSequentDetails.updatedDate,'dd-MM-yyyy HH:MI:SS') AS updatedDate, "
			+ " subSequentDetails.orgId AS orgId, "
			+ " subSequentDetails.unitId AS unitId "
			+ " FROM SubSequentDetails subSequentDetails "
			+ " INNER JOIN subSequentDetails.subSequentProcedureConfig subSequentProcedureConfig "
			+ " WHERE subSequentDetails.unitId=:unitId "
			+ " AND subSequentDetails.orgId=:orgId "
			+ " AND subSequentProcedureConfig.specialityId = :specialityId "
			+ " AND subSequentDetails.status='A' "),
})

@Entity
@Table(name = "m_sub_sequent_details" , schema = "otims")
@SequenceGenerator(name = "m_sub_sequent_details_seq" , sequenceName = "otims.m_sub_sequent_details_seq" , allocationSize = 1)
public class SubSequentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_sub_sequent_details_seq")
	@Column(name = "sub_sequent_details_id")
	private int subSequentDetailsId;
	
	@Column(name="sub_sequent_proc_config_id")
	private Integer subSequentProcConfigId;
	
	@Column(name="sequence_id")
	private Integer sequenceId;
	
	@Column(name="disc_percentage")
	private Integer discPercentage;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_sequent_proc_config_id", insertable = false, nullable = false, updatable = false)
	private SubSequentProcedureConfig subSequentProcedureConfig;
	
	public int getSubSequentDetailsId() {
		return subSequentDetailsId;
	}

	public void setSubSequentDetailsId(int subSequentDetailsId) {
		this.subSequentDetailsId = subSequentDetailsId;
	}

	public Integer getSubSequentProcConfigId() {
		return subSequentProcConfigId;
	}

	public void setSubSequentProcConfigId(Integer subSequentProcConfigId) {
		this.subSequentProcConfigId = subSequentProcConfigId;
	}

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Integer getDiscPercentage() {
		return discPercentage;
	}

	public void setDiscPercentage(Integer discPercentage) {
		this.discPercentage = discPercentage;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	
}
