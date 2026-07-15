package com.param.procurement.purchase.dto.purchaseOrder;



public class PoOtherChargesMapperDto {
	
	private Integer id;
	
	private Integer otherChargesId;
	
	private String otherCharges;
	
	private double amount;
	
	private Integer poId;
	
	private boolean isFromPo;
	
	public PoOtherChargesMapperDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public Integer getPoId() {
		return poId;
	}



	public void setPoId(Integer poId) {
		this.poId = poId;
	}



	public PoOtherChargesMapperDto(Integer id, Integer otherChargesId, String otherCharges, double amount) {
		this.id = id;
		this.otherChargesId = otherChargesId;
		this.otherCharges = otherCharges;
		this.amount = amount;
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

	public String getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}



	public boolean getIsFromPo() {
		return isFromPo;
	}



	public void setIsFromPo(boolean isFromPo) {
		this.isFromPo = isFromPo;
	}

		
	

}
