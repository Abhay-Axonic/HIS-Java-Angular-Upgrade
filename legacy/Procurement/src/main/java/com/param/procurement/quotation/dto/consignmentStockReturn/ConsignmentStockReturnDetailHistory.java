package com.param.procurement.quotation.dto.consignmentStockReturn;

import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class ConsignmentStockReturnDetailHistory extends CookieObjectDto{
	
	private Integer crId;
	private Integer crDetailId;
	private Integer grnDetailId;
	private String  crNumber;
	private Integer  returnedQuantity;
	private Date  crDate;
	
	public Integer getCrId() {
		return crId;
	}
	public void setCrId(Integer crId) {
		this.crId = crId;
	}
	public Integer getCrDetailId() {
		return crDetailId;
	}
	public void setCrDetailId(Integer crDetailId) {
		this.crDetailId = crDetailId;
	}
	public Integer getGrnDetailId() {
		return grnDetailId;
	}
	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}
	public String getCrNumber() {
		return crNumber;
	}
	public void setCrNumber(String crNumber) {
		this.crNumber = crNumber;
	}
	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}
	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}
	public Date getCrDate() {
		return crDate;
	}
	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}
	

}
