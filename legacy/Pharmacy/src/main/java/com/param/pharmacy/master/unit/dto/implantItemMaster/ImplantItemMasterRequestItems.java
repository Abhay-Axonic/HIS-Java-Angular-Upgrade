package com.param.pharmacy.master.unit.dto.implantItemMaster;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ImplantItemMasterRequestItems {
	@JsonInclude(Include.NON_NULL)
	
	protected Integer id; 
	
	protected String specialityname;
	

	public ImplantItemMasterRequestItems() 
	{		super();
	}

	public ImplantItemMasterRequestItems(Integer id) 
	{
		super();
		this.id = id;
	}
	public ImplantItemMasterRequestItems(Integer id, String specialityname) 
	{
		super();
		this.id = id;
		this.specialityname=specialityname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpecialityname() {
		return specialityname;
	}

	public void setSpecialityname(String specialityname) {
		this.specialityname = specialityname;
	}
	

}
