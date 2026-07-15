package com.param.pharmacy.master.global.dto.equipment;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class EquipmentResponse extends CookieObjectDto{

	protected Integer id;
	
	protected Boolean status;
	
	protected String code;

	protected String description;
	
	protected List<EquipmentResponse> items;
	
	public EquipmentResponse() {
		super();
		
	}

	public EquipmentResponse(Integer id, Boolean status, String code, String description) {
		super();
		this.id = id;
		this.status = status;
		this.code = code;
		this.description = description;
	}

	
	public List<EquipmentResponse> getItems() {
		return items;
	}

	public void setItems(List<EquipmentResponse> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public String getIsActive() {
			return null;
	}
	
	public void addItems(EquipmentResponse item) {
		if (this.items == null)
			this.items = new ArrayList<EquipmentResponse>();
		
		this.items.add(item);
	}

}
