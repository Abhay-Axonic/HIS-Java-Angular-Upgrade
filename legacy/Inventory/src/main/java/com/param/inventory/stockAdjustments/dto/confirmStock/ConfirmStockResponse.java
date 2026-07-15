package com.param.inventory.stockAdjustments.dto.confirmStock;

import java.util.Date;

import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;

public class ConfirmStockResponse extends UploadStockResponse {

	private String stockAdjustmentNo;

	private Date stockAdjustmentDate;

	private Integer computedById;

	private String computedBy;

	public String getStockAdjustmentNo() {
		return stockAdjustmentNo;
	}

	public void setStockAdjustmentNo(String stockAdjustmentNo) {
		this.stockAdjustmentNo = stockAdjustmentNo;
	}

	public Date getStockAdjustmentDate() {
		return stockAdjustmentDate;
	}

	public void setStockAdjustmentDate(Date stockAdjustmentDate) {
		this.stockAdjustmentDate = stockAdjustmentDate;
	}

	public Integer getComputedById() {
		return computedById;
	}

	public void setComputedById(Integer computedById) {
		this.computedById = computedById;
	}

	public String getComputedBy() {
		return computedBy;
	}

	public void setComputedBy(String computedBy) {
		this.computedBy = computedBy;
	}

}
