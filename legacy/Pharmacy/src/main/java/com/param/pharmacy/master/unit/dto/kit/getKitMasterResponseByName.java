package com.param.pharmacy.master.unit.dto.kit;

import java.util.ArrayList;
import java.util.List;

public class getKitMasterResponseByName {
	
	protected Integer id;
	
	protected String code;
	
	protected String description;
	
	protected Integer kitTypeId;
	
	protected String kitType;
	
	protected Boolean status;
	
protected List<GetKitMasterResponseListByName>items;
	
		public getKitMasterResponseByName()
	{
		super();
	}

	public getKitMasterResponseByName(Integer id, String code, String description, Integer kitTypeId, String kitType,
			Boolean status) 
	{
		super();
		this.id=id;
		this.code=code;
		this.description=description;
		this.kitTypeId=kitTypeId;
		this.kitType=kitType;
		this.status=status;
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

	public void add(GetKitMasterResponseListByName response1)
	{
		if (this.items == null)
			this.items = new ArrayList<GetKitMasterResponseListByName>();
		
		this.items.add(response1);
		
		
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<GetKitMasterResponseListByName> getItems() {
		return items;
	}

	public void setItems(List<GetKitMasterResponseListByName> items) {
		this.items = items;
	}

	
/*
	public void addItems(GetKitMasterResponseListByName getKitMasterResponseListByName) 
	{
		if (this.items == null)
			this.items = new ArrayList<GetKitMasterResponseListByName>();
		
		this.items.add(getKitMasterResponseListByName);
		
	}
*/

}
