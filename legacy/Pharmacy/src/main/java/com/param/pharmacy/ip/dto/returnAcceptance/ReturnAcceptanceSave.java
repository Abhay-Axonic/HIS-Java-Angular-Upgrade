package com.param.pharmacy.ip.dto.returnAcceptance;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ReturnAcceptanceSave extends CookieObjectDto
{
	private Integer id;
	
	private Integer statusId;
	private Integer storeId;
	private Integer wardId;
	private Integer issueId;
	private Boolean fromVirtualStore;
	private String returnDate;
	private boolean isDummyService;
	
	public boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	private Integer patientId;
	
	private List<ReturnAcceptanceSaveItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<ReturnAcceptanceSaveItem> getItems() {
		return items;
	}

	public void setItems(List<ReturnAcceptanceSaveItem> items) {
		this.items = items;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Boolean getFromVirtualStore() {
		return fromVirtualStore;
	}

	public void setFromVirtualStore(Boolean fromVirtualStore) {
		this.fromVirtualStore = fromVirtualStore;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	
}