package com.param.billing.global.transaction.dto;

import java.util.List;

import com.param.billing.unit.dto.DoctorSlabVoucherDetailsDto;

public class TDoctorShareProcessReqDto {
	private Integer orgId;
	private Integer unitId;
	private Integer userId;
	private String remark;
	private List<TDoctorShareRespDto> listtDoctorShareRespDto;
	private TDocShareVoucherDto tDocShareVoucherDto;
	private List<TDocShareVoucherDtlsDto> listTDocShareVoucherDtlsDto;
	private List<TDocShareVoucherDto> listTDocShareVoucherDto;
	private List<DoctorSlabVoucherDetailsDto> doctorSlabVoucherDetailsDtosList;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<DoctorSlabVoucherDetailsDto> getDoctorSlabVoucherDetailsDtosList() {
		return doctorSlabVoucherDetailsDtosList;
	}
	public void setDoctorSlabVoucherDetailsDtosList(List<DoctorSlabVoucherDetailsDto> doctorSlabVoucherDetailsDtosList) {
		this.doctorSlabVoucherDetailsDtosList = doctorSlabVoucherDetailsDtosList;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	
	
	public List<TDocShareVoucherDtlsDto> getListTDocShareVoucherDtlsDto() {
		return listTDocShareVoucherDtlsDto;
	}
	public void setListTDocShareVoucherDtlsDto(List<TDocShareVoucherDtlsDto> listTDocShareVoucherDtlsDto) {
		this.listTDocShareVoucherDtlsDto = listTDocShareVoucherDtlsDto;
	}
	public TDocShareVoucherDto gettDocShareVoucherDto() {
		return tDocShareVoucherDto;
	}
	public void settDocShareVoucherDto(TDocShareVoucherDto tDocShareVoucherDto) {
		this.tDocShareVoucherDto = tDocShareVoucherDto;
	}
	public List<TDoctorShareRespDto> getListtDoctorShareRespDto() {
		return listtDoctorShareRespDto;
	}
	public void setListtDoctorShareRespDto(List<TDoctorShareRespDto> listtDoctorShareRespDto) {
		this.listtDoctorShareRespDto = listtDoctorShareRespDto;
	}
	public List<TDocShareVoucherDto> getListTDocShareVoucherDto() {
		return listTDocShareVoucherDto;
	}
	public void setListTDocShareVoucherDto(List<TDocShareVoucherDto> listTDocShareVoucherDto) {
		this.listTDocShareVoucherDto = listTDocShareVoucherDto;
	}
	
}
