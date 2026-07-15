package com.param.global.dto.global;

import java.util.List;

public class AgainstQuotationRequestResponse {
	
List<QuotationRequestSearchDto> quotationRequestList;
	
	List<OtherChargesResponse> otherChargesList;
	
	List<TaxDropDown> taxList;
	

	public List<QuotationRequestSearchDto> getQuotationRequestList() {
		return quotationRequestList;
	}

	public void setQuotationRequestList(List<QuotationRequestSearchDto> quotationRequestList) {
		this.quotationRequestList = quotationRequestList;
	}

	public List<OtherChargesResponse> getOtherChargesList() {
		return otherChargesList;
	}

	public void setOtherChargesList(List<OtherChargesResponse> otherChargesList) {
		this.otherChargesList = otherChargesList;
	}

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}
	

}
