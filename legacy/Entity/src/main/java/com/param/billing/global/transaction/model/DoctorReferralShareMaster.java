package com.param.billing.global.transaction.model;



import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.global.SampleTypeMediaMappingMaster;
import com.param.global.common.DateConverter;

@Entity
@Table(name="m_doctor_referral_share_master", schema="billing")
@SequenceGenerator(name = "m_seq_doctor_referral_share_master", sequenceName = "billing.m_seq_doctor_referral_share_master", allocationSize = 1)
public class DoctorReferralShareMaster {
	@Id
	@Column(name="doc_referral_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_doctor_referral_share_master")
	private Integer docReferralId;
	
	@Column(name = "doc_referral_name")
	private String docReferralName;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "doctorReferralShareMaster")
	private List<MDoctorReferralShare> listMDoctorReferralShare;

	public Integer getDocReferralId() {
		return docReferralId;
	}

	public void setDocReferralId(Integer docReferralId) {
		this.docReferralId = docReferralId;
	}

	public String getDocReferralName() {
		return docReferralName;
	}

	public void setDocReferralName(String docReferralName) {
		this.docReferralName = docReferralName;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<MDoctorReferralShare> getListMDoctorReferralShare() {
		return listMDoctorReferralShare;
	}

	public void setListMDoctorReferralShare(List<MDoctorReferralShare> listMDoctorReferralShare) {
		this.listMDoctorReferralShare = listMDoctorReferralShare;
	}
	
	
}
