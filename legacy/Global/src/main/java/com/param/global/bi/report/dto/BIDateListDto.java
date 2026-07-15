package com.param.global.bi.report.dto;

import java.util.List;

public class BIDateListDto {
	private String trandate;

	private List<KeyValuePairDto> parameterList;


	public String getTrandate() {
		return trandate;
	}

	public void setTrandate(String trandate) {
		this.trandate = trandate;
	}

	public List<KeyValuePairDto> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<KeyValuePairDto> parameterList) {
		this.parameterList = parameterList;
	}
	
}
