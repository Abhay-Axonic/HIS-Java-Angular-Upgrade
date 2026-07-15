package com.param.procurement.quotation.dto.physicalInspection;

import java.util.List;

import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;

public class ConsignmentStockConversationSave {
	
private Integer id;
	
	private Integer statusId;
	
	private String remark;
	
	private Integer storeId;
	
	private List<ConsignmentStockConversionSaveItem> items;
	
	private List<ConsignmentStockConversationBatch> batch;
	
	
	public List<ConsignmentStockConversationBatch> getBatch() {
		return batch;
	}

	public void setBatch(List<ConsignmentStockConversationBatch> batch) {
		this.batch = batch;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

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

	public List<ConsignmentStockConversionSaveItem> getItems() {
		return items;
	}

	public void setItems(List<ConsignmentStockConversionSaveItem> items) {
		this.items = items;
	}

}
