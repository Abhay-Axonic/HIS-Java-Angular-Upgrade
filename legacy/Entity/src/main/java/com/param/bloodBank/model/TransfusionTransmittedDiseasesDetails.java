package com.param.bloodBank.model;

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
	

	@NamedQuery(name="GET_TTD_LIST_BY_ID",
		query=" SELECT TTDD.transfusionTransmittedDiseasesDetailsId AS transfusionTransmittedDiseasesDetailsId, "
				+ "	TTDD.transfusionTransmittedDiseasesId AS transfusionTransmittedDiseasesId, "
				+ " TTDD.bloodGroupId AS bloodGroupId, "
				+ " TTDD.testId AS testId, "
				+ " TTDD.testResult AS testResult, "
				+ " bgm.bloodGroupDesc AS bloodGroupDesc, "
				+ " btm.bloodTestDesc AS bloodTestDesc "
				+ " FROM TransfusionTransmittedDiseasesDetails TTDD "
				+ " INNER JOIN TTDD.bloodGroupMaster bgm  "
				+ " INNER JOIN TTDD.bloodTestMaster btm  "
				+ " WHERE TTDD.organizationId= :organizationId "
				+ " AND TTDD.unitId= :unitId "
				+ " AND TTDD.transfusionTransmittedDiseasesId= :transfusionTransmittedDiseasesId "
				+ " AND TTDD.status= 'A'")
})
@Entity
@Table(name = "t_transfusion_transmitted_diseases_details", schema = "blood_bank")
@SequenceGenerator(name = "t_transfusion_transmitted_diseases_details_seq", sequenceName = "blood_bank.t_transfusion_transmitted_diseases_details_seq", allocationSize = 1)
public class TransfusionTransmittedDiseasesDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_transfusion_transmitted_diseases_details_seq")
	@Column(name = "transfusion_transmitted_diseases_details_id")
	private Integer transfusionTransmittedDiseasesDetailsId;
	
	@Column(name = "transfusion_transmitted_diseases_id")
	private Integer transfusionTransmittedDiseasesId;
	
	@Column(name = "blood_group_id")
	private Integer bloodGroupId;
	
	@Column(name = "test_id")
	private Integer testId;
	
	@Column(name = "test_result")
	private Boolean testResult;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", insertable = false, updatable = false)
	private BloodTestMaster bloodTestMaster;
	
	public Integer getTransfusionTransmittedDiseasesDetailsId() {
		return transfusionTransmittedDiseasesDetailsId;
	}

	public void setTransfusionTransmittedDiseasesDetailsId(Integer transfusionTransmittedDiseasesDetailsId) {
		this.transfusionTransmittedDiseasesDetailsId = transfusionTransmittedDiseasesDetailsId;
	}

	public Integer getTransfusionTransmittedDiseasesId() {
		return transfusionTransmittedDiseasesId;
	}

	public void setTransfusionTransmittedDiseasesId(Integer transfusionTransmittedDiseasesId) {
		this.transfusionTransmittedDiseasesId = transfusionTransmittedDiseasesId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
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

	public Boolean getTestResult() {
		return testResult;
	}

	public void setTestResult(Boolean testResult) {
		this.testResult = testResult;
	}
	
	

}
