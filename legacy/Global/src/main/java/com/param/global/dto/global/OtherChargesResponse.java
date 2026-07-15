package com.param.global.dto.global;

public class OtherChargesResponse {
	
    private Integer id;
	
    private String otherCharges;
    
    private Double amount;
    
   

	public OtherChargesResponse() {
		super();
	}

	public OtherChargesResponse(Integer id, String otherCharges) {
		super();
		this.id = id;
		this.otherCharges = otherCharges;
	}
	
	public OtherChargesResponse(Integer id, String otherCharges, Double amount) {
		super();
		this.id = id;
		this.otherCharges = otherCharges;
		this.amount = amount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
