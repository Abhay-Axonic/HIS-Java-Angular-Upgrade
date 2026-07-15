package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class PatientIssueReturnAddItem extends CookieObjectDto
{
	private List<Integer> itemIds;
	
	private List<Integer> issueIds;
	
	private List<Integer> batchIds;
	
	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public List<Integer> getIssueIds() {
		return issueIds;
	}

	public void setIssueIds(List<Integer> issueIds) {
		this.issueIds = issueIds;
	}
	private Integer screenId;

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public List<Integer> getBatchIds() {
		return batchIds;
	}

	public void setBatchIds(List<Integer> batchIds) {
		this.batchIds = batchIds;
	}

}