package com.param.billing.unit.dto;

import java.util.List;


public class IndividualDoctorShareReqDto {

	private Integer orgId;
	private Integer unitId;
	private Integer doctorId;
	private Integer specialityId;
	private Integer referralId;
	private String status;
	private Integer updatedBy;
	private String updatedDate;
	
	private List<TIndividualDocShareGroupWiseDto> listTIndividualDocShareGroupWiseDto;
	private List<TIndividualDocShareServiceWiseDto> listTIndividualDocShareServiceWiseDto;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String specialitySerarchName;
	
	

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
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

	public List<TIndividualDocShareGroupWiseDto> getListTIndividualDocShareGroupWiseDto() {
		return listTIndividualDocShareGroupWiseDto;
	}

	public void setListTIndividualDocShareGroupWiseDto(
			List<TIndividualDocShareGroupWiseDto> listTIndividualDocShareGroupWiseDto) {
		this.listTIndividualDocShareGroupWiseDto = listTIndividualDocShareGroupWiseDto;
	}

	public List<TIndividualDocShareServiceWiseDto> getListTIndividualDocShareServiceWiseDto() {
		return listTIndividualDocShareServiceWiseDto;
	}

	public void setListTIndividualDocShareServiceWiseDto(
			List<TIndividualDocShareServiceWiseDto> listTIndividualDocShareServiceWiseDto) {
		this.listTIndividualDocShareServiceWiseDto = listTIndividualDocShareServiceWiseDto;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getSpecialitySerarchName() {
		return specialitySerarchName;
	}

	public void setSpecialitySerarchName(String specialitySerarchName) {
		this.specialitySerarchName = specialitySerarchName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	

}
