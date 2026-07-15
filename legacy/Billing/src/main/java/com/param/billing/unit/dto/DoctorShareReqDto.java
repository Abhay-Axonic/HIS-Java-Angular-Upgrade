package com.param.billing.unit.dto;

import java.util.List;


public class DoctorShareReqDto {

	private Integer orgId;
	private Integer unitId;
	private Integer gradeId;
	private Integer departmentId;
	
	
	private List<TGlobalDocShareGroupWiseDto> listTGlobalDocShareGroupWiseDto;
	
	private List<TGlobalDocShareServiceWiseDto> listTGlobalDocShareServiceWiseDto;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	private String specialitySerarchName;
	
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

	public List<TGlobalDocShareGroupWiseDto> getListTGlobalDocShareGroupWiseDto() {
		return listTGlobalDocShareGroupWiseDto;
	}

	public void setListTGlobalDocShareGroupWiseDto(List<TGlobalDocShareGroupWiseDto> listTGlobalDocShareGroupWiseDto) {
		this.listTGlobalDocShareGroupWiseDto = listTGlobalDocShareGroupWiseDto;
	}

	public List<TGlobalDocShareServiceWiseDto> getListTGlobalDocShareServiceWiseDto() {
		return listTGlobalDocShareServiceWiseDto;
	}

	public void setListTGlobalDocShareServiceWiseDto(
			List<TGlobalDocShareServiceWiseDto> listTGlobalDocShareServiceWiseDto) {
		this.listTGlobalDocShareServiceWiseDto = listTGlobalDocShareServiceWiseDto;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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


	

}
