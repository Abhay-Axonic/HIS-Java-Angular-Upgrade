package com.param.pharmacy.master.global.dto.frequency;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.TranslatedFrequencyMasterDto;
import com.param.service.global.model.CookieObjectDto;

public abstract class FrequencyResponse extends CookieObjectDto {

	protected Integer id;
	
	protected Boolean status;
	
	protected String code;
	
	protected String description;
	
	protected Integer noOfAdministrationTime;
	
	protected List<FrequencyRequestItem> items;

	protected List<TranslatedFrequencyMasterDto> addedLanguageList;
	
	
	public FrequencyResponse() {
		super();
	}
	
	public FrequencyResponse(Integer id, String code, String description,
			Integer noOfAdministrationTime,Boolean status) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
		this.noOfAdministrationTime = noOfAdministrationTime;
		this.status = status;
	}


	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<FrequencyRequestItem> getItems() {
		return items;
	}

	public void setItems(List<FrequencyRequestItem> items) {
		this.items = items;
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
	
	public void addItems(FrequencyRequestItem item) {
		if (this.items == null)
			this.items = new ArrayList<FrequencyRequestItem>();

		this.items.add(item);
	}
	

	public List<TranslatedFrequencyMasterDto> getAddedLanguageList() {
		return addedLanguageList;
	}

	public void setAddedLanguageList(List<TranslatedFrequencyMasterDto> addedLanguageList) {
		this.addedLanguageList = addedLanguageList;
	}
}
