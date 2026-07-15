package com.param.global.otims.dto;

import java.util.List;

import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.PrescriptionDto;

public class PostOperativeCareDto {

	private Integer postOperativeCareId;
	
	private Integer proReqId;
	
	private String followUpNote;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private PrescriptionDto prescriptionDto;
	
	private List<OTCompletionNoteDto> oTCompletionNoteDtoList;
	
	private CoversheetInstructionDto coversheetInstructionDto;
	
	private List<OtMealDto> otMealList;

	public Integer getPostOperativeCareId() {
		return postOperativeCareId;
	}

	public void setPostOperativeCareId(Integer postOperativeCareId) {
		this.postOperativeCareId = postOperativeCareId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getFollowUpNote() {
		return followUpNote;
	}

	public void setFollowUpNote(String followUpNote) {
		this.followUpNote = followUpNote;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public PrescriptionDto getPrescriptionDto() {
		return prescriptionDto;
	}

	public void setPrescriptionDto(PrescriptionDto prescriptionDto) {
		this.prescriptionDto = prescriptionDto;
	}

	public List<OTCompletionNoteDto> getoTCompletionNoteDtoList() {
		return oTCompletionNoteDtoList;
	}

	public void setoTCompletionNoteDtoList(List<OTCompletionNoteDto> oTCompletionNoteDtoList) {
		this.oTCompletionNoteDtoList = oTCompletionNoteDtoList;
	}

	public CoversheetInstructionDto getCoversheetInstructionDto() {
		return coversheetInstructionDto;
	}

	public void setCoversheetInstructionDto(CoversheetInstructionDto coversheetInstructionDto) {
		this.coversheetInstructionDto = coversheetInstructionDto;
	}

	public List<OtMealDto> getOtMealList() {
		return otMealList;
	}

	public void setOtMealList(List<OtMealDto> otMealList) {
		this.otMealList = otMealList;
	}


}
