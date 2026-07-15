package com.param.global.pharmacy.dto;

import java.util.List;

import com.param.global.billing.dto.PharmacyBillCommonDto;

public class SaleDtoChangeOfPayee  extends PharmacyBillCommonDto{
	
	private Integer statusId;
	private boolean smartCard;
	private String receivedBy;
	private String receivedOn;
	private boolean isHandover;
	private Integer handoverBy;
	private Integer carrierId;
	private String runningNumber;
	private boolean smartCardSubmited;
	private boolean claimSend;
	private boolean isConsolidatedBill;
	private String addedDate;
	private List<SaleDetailsChangeOfPayee> saledDetails;
	private Character isRefReq;
	private Integer departmentId;

	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Character getIsRefReq() {
		return isRefReq;
	}
	public void setIsRefReq(Character isRefReq) {
		this.isRefReq = isRefReq;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public boolean isSmartCard() {
		return smartCard;
	}
	public void setSmartCard(boolean smartCard) {
		this.smartCard = smartCard;
	}
	public String getReceivedBy() {
		return receivedBy;
	}
	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}
	public String getReceivedOn() {
		return receivedOn;
	}
	public void setReceivedOn(String receivedOn) {
		this.receivedOn = receivedOn;
	}
	public boolean isHandover() {
		return isHandover;
	}
	public void setHandover(boolean isHandover) {
		this.isHandover = isHandover;
	}
	public Integer getHandoverBy() {
		return handoverBy;
	}
	public void setHandoverBy(Integer handoverBy) {
		this.handoverBy = handoverBy;
	}
	public Integer getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}
	public String getRunningNumber() {
		return runningNumber;
	}
	public void setRunningNumber(String runningNumber) {
		this.runningNumber = runningNumber;
	}
	public boolean isSmartCardSubmited() {
		return smartCardSubmited;
	}
	public void setSmartCardSubmited(boolean smartCardSubmited) {
		this.smartCardSubmited = smartCardSubmited;
	}
	public boolean isClaimSend() {
		return claimSend;
	}
	public void setClaimSend(boolean claimSend) {
		this.claimSend = claimSend;
	}
	public boolean isConsolidatedBill() {
		return isConsolidatedBill;
	}
	public void setConsolidatedBill(boolean isConsolidatedBill) {
		this.isConsolidatedBill = isConsolidatedBill;
	}
	public String getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}
	public List<SaleDetailsChangeOfPayee> getSaledDetails() {
		return saledDetails;
	}
	public void setSaledDetails(List<SaleDetailsChangeOfPayee> saledDetails) {
		this.saledDetails = saledDetails;
	}


}
