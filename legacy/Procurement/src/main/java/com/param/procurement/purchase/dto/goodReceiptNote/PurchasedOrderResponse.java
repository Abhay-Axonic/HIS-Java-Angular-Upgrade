package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchasedOrderResponse {
	
    private Integer id ;
	
	private Integer approvedQuantity;
	
	private String poNo;
	
	private Integer grnDetailId;
	private Date poDate;

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public PurchasedOrderResponse() {
	}

	public PurchasedOrderResponse(Integer id, Integer approvedQuantity, String poNo, Integer grnDetailId , Date poDate) {
		this.id = id;
		this.approvedQuantity = approvedQuantity;
		this.poNo = poNo;
		this.grnDetailId = grnDetailId;
		this.poDate=poDate;
	}
}
