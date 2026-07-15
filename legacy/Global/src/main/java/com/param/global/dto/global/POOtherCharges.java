package com.param.global.dto.global;

import java.util.List;

public class POOtherCharges {
	
	private Integer id;
	
	private Integer otherChragesId;
	
	//addded 
	private Integer otherChargesId;
    public Integer getOtherChargesId() {
		return otherChargesId;
	}
	public void setOtherChargesId(Integer otherChargesId) {
		this.otherChargesId = otherChargesId;
	}//end


	private String otherCharges;
    
    private Double amount;
    
    private Integer poId;
    
    private Double totalAppliedOtherCharges;
	
	


	
	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public POOtherCharges() {
		super();
	}
	
	public POOtherCharges(Integer id, Integer otherChragesId, String otherCharges, Double amount) {
		super();
		this.id = id;
		this.otherChragesId = otherChragesId;
		this.otherCharges = otherCharges;
		this.amount = amount;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOtherChragesId() {
		return otherChragesId;
	}

	public void setOtherChragesId(Integer otherChragesId) {
		this.otherChragesId = otherChragesId;
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


	public Double getTotalAppliedOtherCharges() {
		return totalAppliedOtherCharges;
	}


	public void setTotalAppliedOtherCharges(Double totalAppliedOtherCharges) {
		this.totalAppliedOtherCharges = totalAppliedOtherCharges;
	}
	
	

}
