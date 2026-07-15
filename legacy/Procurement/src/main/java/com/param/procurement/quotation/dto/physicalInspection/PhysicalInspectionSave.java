package com.param.procurement.quotation.dto.physicalInspection;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class PhysicalInspectionSave extends CookieObjectDto
{
	private Integer id;
	
	private Integer statusId;
	
	private String remark;
	
	private Integer storeId;
	
	private Boolean autoPhysicalInspectionSave;
	
	
	
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	private List<PhysicalInspectionSaveItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PhysicalInspectionSaveItem> getItems() {
		return items;
	}

	public void setItems(List<PhysicalInspectionSaveItem> items) {
		this.items = items;
	}

	public Boolean getAutoPhysicalInspectionSave() {
		return autoPhysicalInspectionSave;
	}

	public void setAutoPhysicalInspectionSave(Boolean autoPhysicalInspectionSave) {
		this.autoPhysicalInspectionSave = autoPhysicalInspectionSave;
	}
	 
}