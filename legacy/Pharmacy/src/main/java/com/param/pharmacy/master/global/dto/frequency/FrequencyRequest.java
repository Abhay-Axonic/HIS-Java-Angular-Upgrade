package com.param.pharmacy.master.global.dto.frequency;

import com.param.global.dto.global.TranslatedFrequencyMasterDto;
import com.param.global.dto.global.TranslatedInstructionMasterDto;

import java.sql.Timestamp;
import java.util.List;

public class FrequencyRequest extends FrequencyResponse {
	
//	protected Integer slot;

	protected Timestamp administrationTime;

	protected Timestamp preBufferAdministrationTime;

	protected Timestamp postBufferAdministrationTime;

	protected List<FrequencyRequestItem> items;

	protected List<TranslatedFrequencyMasterDto> addedLanguageList;

	protected Integer frequencyId;
	
	public FrequencyRequest() {
		super();
	}

	public FrequencyRequest(Integer id, String code,String description, Integer noOfAdministrationTime, Boolean status) {
		super(id, code, description, noOfAdministrationTime, status);
		
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

	public List<FrequencyRequestItem> getItems() {
		return items;
	}

	public void setItems(List<FrequencyRequestItem> items) {
		this.items = items;
	}

	public List<TranslatedFrequencyMasterDto> getAddedLanguageList() {
		return addedLanguageList;
	}

	public void setAddedLanguageList(List<TranslatedFrequencyMasterDto> addedLanguageList) {
		this.addedLanguageList = addedLanguageList;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}
}
