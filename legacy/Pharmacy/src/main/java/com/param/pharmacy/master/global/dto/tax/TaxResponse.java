package com.param.pharmacy.master.global.dto.tax;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class TaxResponse  extends CookieObjectDto{

	protected Integer id;

	protected Boolean status;
	
	protected String code;

	protected String name;	
	
	protected  Double taxPercentage;
	
	protected  Integer generalLedgerId;
	
	protected List<TaxRequestItem> items;
	
	public TaxResponse(Integer id,String code, String name, Double taxPercentage,Boolean status) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.taxPercentage = taxPercentage;
		this.status = status;
	}

	public TaxResponse() {
		super();
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	
	public List<TaxRequestItem> getItems() {
		return items;
	}

	public void setItems(List<TaxRequestItem> items) {
		this.items = items;
	}

	public void addItems(TaxRequestItem item) {
		if (this.items == null)
			this.items = new ArrayList<TaxRequestItem>();

		this.items.add(item);
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}
}
