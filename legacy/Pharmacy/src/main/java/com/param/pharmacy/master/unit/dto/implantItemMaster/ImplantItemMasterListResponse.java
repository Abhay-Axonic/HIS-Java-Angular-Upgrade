package com.param.pharmacy.master.unit.dto.implantItemMaster;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ImplantItemMasterListResponse
{
	@JsonInclude(Include.NON_NULL)
	
	protected Integer id;

	protected String specialityName;
	
	public ImplantItemMasterListResponse() 
	{
		super();
		
	}
	
	
	public ImplantItemMasterListResponse(Integer id) 
	{
		super();
		this.id = id;
	}
	
	public ImplantItemMasterListResponse(Integer id, String specialityName)
	{
		super();
		this.id = id;
		this.specialityName = specialityName;
		
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
