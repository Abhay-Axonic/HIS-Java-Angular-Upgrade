package com.param.pharmacy.master.global.dto.frequency;

import java.util.ArrayList;
import java.util.List;

public class GetFrequencyResponse {

	protected Integer id;

	protected String code;

	protected String description;

	protected Integer noOfAdministrationTime;

	protected Boolean status;

	protected List<GetFrequencyListResponse> items;
	
//	protected List<GetFrequencyListResponse> getparameter;

	public GetFrequencyResponse(Integer id, String code, String description, Integer noOfAdministrationTime,
			Boolean status) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
		this.noOfAdministrationTime = noOfAdministrationTime;
		this.status = status;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoOfAdministrationTime() {
		return noOfAdministrationTime;
	}

	public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
		this.noOfAdministrationTime = noOfAdministrationTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<GetFrequencyListResponse> getItems() {
		return items;
	}

	public void setItems(List<GetFrequencyListResponse> items) {
		this.items = items;
	}

	public void addItems(GetFrequencyListResponse item) {
		if (this.items == null)
			this.items = new ArrayList<GetFrequencyListResponse>();

		this.items.add(item);
	}
	
	/*public List<GetFrequencyListResponse> getGetparameter() {
		return getparameter;
	}

	public void setGetparameter(List<GetFrequencyListResponse> getparameter) {
		this.getparameter = getparameter;
	}

	public void addGetparameter(GetFrequencyListResponse getparameter) {
		if (this.getparameter == null)
			this.getparameter = new ArrayList<GetFrequencyListResponse>();

		this.getparameter.add(getparameter);
	}
	*/

}
