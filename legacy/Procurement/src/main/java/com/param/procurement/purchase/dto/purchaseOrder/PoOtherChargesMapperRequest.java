package com.param.procurement.purchase.dto.purchaseOrder;

public class PoOtherChargesMapperRequest {

	private Integer id;
	private Integer poId;
	
	private Integer otherChargesId;
	private String otherCharges;
	private double amount;
	private boolean isFromPo;
	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public String getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOtherChargesId() {
		return otherChargesId;
	}

	public void setOtherChargesId(Integer otherChargesId) {
		this.otherChargesId = otherChargesId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean getIsFromPo() {
		return isFromPo;
	}

	public void setIsFromPo(boolean isFromPo) {
		this.isFromPo = isFromPo;
	}
	
	
}
