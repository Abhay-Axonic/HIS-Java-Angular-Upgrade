package com.param.billing.global.transaction.config.dto;

import java.util.List;


public class TCompanyInvoiceCreationReqDto 
{
	private Character isConsolidatedBill;
	private Integer orgId;
	private Integer unitId;
	private MCompanyInvoiceMasterDto mCompanyInvoiceMasterDto;
	private List<TCompanyInvoiceDetailsDto> listTCompanyInvoiceDetailsDto;
	
	public Character getIsConsolidatedBill() {
		return isConsolidatedBill;
	}
	public void setIsConsolidatedBill(Character isConsolidatedBill) {
		this.isConsolidatedBill = isConsolidatedBill;
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
	public List<TCompanyInvoiceDetailsDto> getListTCompanyInvoiceDetailsDto() {
		return listTCompanyInvoiceDetailsDto;
	}
	public void setListTCompanyInvoiceDetailsDto(List<TCompanyInvoiceDetailsDto> listTCompanyInvoiceDetailsDto) {
		this.listTCompanyInvoiceDetailsDto = listTCompanyInvoiceDetailsDto;
	}
	public MCompanyInvoiceMasterDto getmCompanyInvoiceMasterDto() {
		return mCompanyInvoiceMasterDto;
	}
	public void setmCompanyInvoiceMasterDto(MCompanyInvoiceMasterDto mCompanyInvoiceMasterDto) {
		this.mCompanyInvoiceMasterDto = mCompanyInvoiceMasterDto;
	}
	

	
}
