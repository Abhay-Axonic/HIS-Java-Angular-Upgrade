package com.param.procurement.purchase.dto.goodReceiptNote;

public class GRNOtherChargesMapper {
	
    private Integer id;
	
	private Integer otherChargesId;
	
	private String otherCharges;
	
	private double amount;
	
//	private Integer grnDetailId;
	private Integer grnId;
	private boolean isFromGrn;
	public GRNOtherChargesMapper() {
		super();
	}
	
	public GRNOtherChargesMapper(Integer id, Integer otherChargesId, String otherCharges, double amount) {
		super();
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

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public boolean getIsFromGrn() {
		return isFromGrn;
	}

	public void setIsFromGrn(boolean isFromGrn) {
		this.isFromGrn = isFromGrn;
	}

	
	
	

}
