package com.param.pharmacy.master.unit.dto.vendor;

public class AreaDto {

	protected Integer areaId;
	
	protected String areaName;

	public AreaDto(Integer areaId, String areaName) {
		super();
		this.areaId = areaId;
		this.areaName = areaName;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
}
