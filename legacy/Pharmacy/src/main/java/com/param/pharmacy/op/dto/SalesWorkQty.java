package com.param.pharmacy.op.dto;

public class SalesWorkQty 
{
	Long qty;
	
	Integer intQty;
	
	Long detailId;

	Integer itemId;
	
	Long saleId;
	
	Integer selectedUomTypeId;
	
	Integer selectedUomUnitId;

	public Integer getSelectedUomTypeId() {
		return selectedUomTypeId;
	}

	public void setSelectedUomTypeId(Integer selectedUomTypeId) {
		this.selectedUomTypeId = selectedUomTypeId;
	}

	public Integer getSelectedUomUnitId() {
		return selectedUomUnitId;
	}

	public void setSelectedUomUnitId(Integer selectedUomUnitId) {
		this.selectedUomUnitId = selectedUomUnitId;
	}

	public Integer getIntQty() {
		return intQty;
	}

	public void setIntQty(Integer intQty) {
		this.intQty = intQty;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	
	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public SalesWorkQty() {
		super();
		// TODO Auto-generated constructor stub
	}
}
