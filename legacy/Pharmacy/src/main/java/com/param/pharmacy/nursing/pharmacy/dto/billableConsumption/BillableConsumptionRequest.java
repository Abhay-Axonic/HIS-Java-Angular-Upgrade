package com.param.pharmacy.nursing.pharmacy.dto.billableConsumption;

import java.util.List;

import com.param.global.dto.global.CommonSearchRequest;

public class BillableConsumptionRequest extends CommonSearchRequest
{
	private String visitNo;
	
	private Integer storeId;
	
	private String consumptionNo;
	
	private String returnNo;
	
	
	private List<Integer> ids;

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getConsumptionNo() {
		return consumptionNo;
	}

	public void setConsumptionNo(String consumptionNo) {
		this.consumptionNo = consumptionNo;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}