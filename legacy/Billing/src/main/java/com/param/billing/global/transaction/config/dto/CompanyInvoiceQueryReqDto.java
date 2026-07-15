package com.param.billing.global.transaction.config.dto;

import java.util.List;

public class CompanyInvoiceQueryReqDto {
	
	
	private Integer orgId;
	private Integer unitId;
	private TCompanyInvoiceQueryDetailDto tCompanyInvoiceQueryDetailDto;
	private List<TCompanyInvoiceQueryDetailDto> listTCompanyInvoiceQueryDetailDto;
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
	
	public TCompanyInvoiceQueryDetailDto gettCompanyInvoiceQueryDetailDto() {
		return tCompanyInvoiceQueryDetailDto;
	}
	public void settCompanyInvoiceQueryDetailDto(TCompanyInvoiceQueryDetailDto tCompanyInvoiceQueryDetailDto) {
		this.tCompanyInvoiceQueryDetailDto = tCompanyInvoiceQueryDetailDto;
	}
	public List<TCompanyInvoiceQueryDetailDto> getListTCompanyInvoiceQueryDetailDto() {
		return listTCompanyInvoiceQueryDetailDto;
	}
	public void setListTCompanyInvoiceQueryDetailDto(
			List<TCompanyInvoiceQueryDetailDto> listTCompanyInvoiceQueryDetailDto) {
		this.listTCompanyInvoiceQueryDetailDto = listTCompanyInvoiceQueryDetailDto;
	}

}
