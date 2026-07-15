package com.param.pharmacy.master.global.dto.frequency;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FrequencyListResponse {

//	protected Integer slot;

	protected Timestamp administrationTime;

	protected Timestamp preBufferAdministrationTime;

	protected Timestamp postBufferAdministrationTime;
	
	private List<FrequencyDetailsResponse> frequencyDetail;
	
	public FrequencyListResponse(Timestamp administrationTime, Timestamp preBufferAdministrationTime,
			Timestamp postBufferAdministrationTime) {
		super();
//		this.slot = slot;
		this.administrationTime = administrationTime;
		this.preBufferAdministrationTime = preBufferAdministrationTime;
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	/*public Integer getSlot() {
		return slot;
	}


	public void setSlot(Integer slot) {
		this.slot = slot;
	}*/


	public Timestamp getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(Timestamp administrationTime) {
		this.administrationTime = administrationTime;
	}

	public Timestamp getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(Timestamp preBufferAdministrationTime) {
		this.preBufferAdministrationTime = preBufferAdministrationTime;
	}

	public Timestamp getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(Timestamp postBufferAdministrationTime) {
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public List<FrequencyDetailsResponse> getFrequencyDetail() {
		return frequencyDetail;
	}

	public void setFrequencyDetail(List<FrequencyDetailsResponse> frequencyDetail) {
		this.frequencyDetail = frequencyDetail;
	}
	
	public void addItems(FrequencyDetailsResponse item) {
		if (this.frequencyDetail == null)
			this.frequencyDetail = new ArrayList<FrequencyDetailsResponse>();
		
		this.frequencyDetail.add(item);
	}
}
