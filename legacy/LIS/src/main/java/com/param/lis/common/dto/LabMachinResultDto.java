package com.param.lis.common.dto;

public class LabMachinResultDto {
	
	private Integer parameterId;
	private String paramResult;
	private Integer labSampleDtlsId;
	
	public Integer getParameterId() {
		return parameterId;
	}
	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}
	public String getParamResult() {
		return paramResult;
	}
	public void setParamResult(String paramResult) {
		this.paramResult = paramResult;
	}
	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}
	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	

}
