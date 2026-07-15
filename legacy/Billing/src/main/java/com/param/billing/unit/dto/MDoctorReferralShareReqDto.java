package com.param.billing.unit.dto;

import java.util.List;

import com.param.billing.global.transaction.dto.DoctorReferralShareMapperDto;


public class MDoctorReferralShareReqDto {

	private Integer orgId;
	private Integer unitId;
	
	private Integer referralShareId;
	private Integer docReferralId;
	private Integer visitTypeId;
	private Integer duration;
	private double revenue;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private String effectiveDate;
	private Integer referraledDocId;
	private Integer noOfCases;
	private Integer share;
	private Integer noOfReferralCount;
	private Integer encounterId;
	private String referralName;
	private String visitTypeName;
	private List<DoctorReferralShareMapperDto> listMDoctorReferralShareDto;
//	private List<MDoctorReferralShareDto> listMDoctorReferralShareDto;
	private Integer doctorReferalShareMapperId;
	private Character status;
	
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

	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}

	public Integer getDocReferralId() {
		return docReferralId;
	}

	public void setDocReferralId(Integer docReferralId) {
		this.docReferralId = docReferralId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
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

	public Integer getReferraledDocId() {
		return referraledDocId;
	}

	public void setReferraledDocId(Integer referraledDocId) {
		this.referraledDocId = referraledDocId;
	}

	public List<DoctorReferralShareMapperDto> getListMDoctorReferralShareDto() {
		return listMDoctorReferralShareDto;
	}

	public void setListMDoctorReferralShareDto(List<DoctorReferralShareMapperDto> listMDoctorReferralShareDto) {
		this.listMDoctorReferralShareDto = listMDoctorReferralShareDto;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getNoOfCases() {
		return noOfCases;
	}

	public void setNoOfCases(Integer noOfCases) {
		this.noOfCases = noOfCases;
	}

	public Integer getShare() {
		return share;
	}

	public void setShare(Integer share) {
		this.share = share;
	}

	public Integer getNoOfReferralCount() {
		return noOfReferralCount;
	}

	public void setNoOfReferralCount(Integer noOfReferralCount) {
		this.noOfReferralCount = noOfReferralCount;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public Integer getDoctorReferalShareMapperId() {
		return doctorReferalShareMapperId;
	}

	public void setDoctorReferalShareMapperId(Integer doctorReferalShareMapperId) {
		this.doctorReferalShareMapperId = doctorReferalShareMapperId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}
