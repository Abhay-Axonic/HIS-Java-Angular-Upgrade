package com.param.pharmacy.master.unit.dto.kit;

import java.util.ArrayList;
import java.util.List;

public class KitItemMasterResponse {
	
	protected Integer id;
	
	protected String code;
	
	protected String description;
	
	protected Integer kitTypeId;
	
	protected String kitType;
	
	protected Boolean status;
	
	protected List<KitMasterResponseList>items;
	
	public KitItemMasterResponse()
	{
		super();
	}
	public KitItemMasterResponse(Integer id, String code, String description, Integer kitTypeId,String kitType, Boolean status) 
	{
		super();
		this.id=id;
		this.code=code;
		this.description=description;
		this.kitTypeId=kitTypeId;
		this.kitType=kitType;
		this.status=status;
	}
	
	public void addItems(KitMasterResponseList item) {
		if (this.items == null)
			this.items = new ArrayList<KitMasterResponseList>();
		
		this.items.add(item);
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


	public Integer getKitTypeId() {
		return kitTypeId;
	}


	public void setKitTypeId(Integer kitTypeId) {
		this.kitTypeId = kitTypeId;
	}


	public String getKitType() {
		return kitType;
	}


	public void setKitType(String kitType) {
		this.kitType = kitType;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public List<KitMasterResponseList> getItems() {
		return items;
	}


	public void setItems(List<KitMasterResponseList> items) {
		this.items = items;
	}


	public void add(KitItemMasterResponse responseList) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
