package com.param.pharmacy.master.global.dto.tax;

import java.util.ArrayList;
import java.util.List;

public class TaxListRespone extends TaxResponse {

	protected Integer taxComponentId;

	protected Double taxPercentage;

	private List<TaxDetailsResponse> taxDetail;

	public TaxListRespone(Integer id, String code, String name, Double taxPercentage, Boolean status) {
		super(id, code, name, taxPercentage, status);
		this.taxPercentage = taxPercentage;
	}

	public Integer getTaxComponentId() {
		return taxComponentId;
	}

	public void setTaxComponentId(Integer taxComponentId) {
		this.taxComponentId = taxComponentId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public List<TaxDetailsResponse> getTaxDetail() {
		return taxDetail;
	}

	public void setTaxDetail(List<TaxDetailsResponse> taxDetail) {
		this.taxDetail = taxDetail;
	}

	public void addItems(TaxDetailsResponse item) {
		if (this.taxDetail == null)
			this.taxDetail = new ArrayList<TaxDetailsResponse>();
		if (!item.getStatus()) {
			this.taxDetail.add(item);
		}
	}

}
