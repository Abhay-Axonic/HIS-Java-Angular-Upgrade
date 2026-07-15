package com.param.billing.global.transaction.dto;

import java.util.List;

public class TDoctorShareSplitLogsReqDto {
	
	private List<TDoctorShareRespDto> listTDoctorShareRespDto;
	//private TDoctorShareSplitLogsDto tDoctorShareSplitLogsDto;
	private TDoctorShareRespDto tDoctorShareRespDto;
	private Integer orgUnitId;
	private Integer orgId;
	
	
	public List<TDoctorShareRespDto> getListTDoctorShareRespDto() {
		return listTDoctorShareRespDto;
	}
	public void setListTDoctorShareRespDto(List<TDoctorShareRespDto> listTDoctorShareRespDto) {
		this.listTDoctorShareRespDto = listTDoctorShareRespDto;
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
	public TDoctorShareRespDto gettDoctorShareRespDto() {
		return tDoctorShareRespDto;
	}
	public void settDoctorShareRespDto(TDoctorShareRespDto tDoctorShareRespDto) {
		this.tDoctorShareRespDto = tDoctorShareRespDto;
	}
	
	
	
	

	

}
