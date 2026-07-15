package com.param.pharmacy.op.dto.salesReturn;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Column;

import com.param.service.global.model.CookieObjectDto;

public class RequestSaleReturnSave extends CookieObjectDto {

	private Integer storeId;

	private Integer statusId;

	private Long saleId;

	private Integer patientId;
	
	private List<Long> saleIds;
	
	private List<RequestSaleReturnSave> reuquestSaleReturnList;	

	private List<RequestSaleReturnSaveItem> item;

	private Double totalRefundAmount;
	
	private String validationMessage;

	private Double totalreturnPaidAmount;
	
	private String receivedFrom;	

	private Double taxPercentage;
	
	private Double discount;

	private Integer selfPayeeId;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public List<RequestSaleReturnSaveItem> getItem() {
		return item;
	}

	public void setItem(List<RequestSaleReturnSaveItem> item) {
		this.item = item;
	}

	public Double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(Double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	
	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Boolean ValidateSave() {
		Boolean result = true;
		this.validationMessage = "Please fill below required fields. | ";
		if (this.item == null || this.item.isEmpty()) {
			result = false;
			this.validationMessage += "items |";
		} else if (this.item != null && !this.item.isEmpty()
				&& (this.item.stream().filter(i -> (Objects.equals(i.ValidateSave(), false)))
						.map(RequestSaleReturnSaveItem::getSaleDetailId).collect(Collectors.toList())).size() > 0) {
			result = false;

			this.validationMessage += this.item.stream().filter(i -> (Objects.equals(i.ValidateSave(), false)))
					.map(RequestSaleReturnSaveItem::getValidationMessage).collect(Collectors.toList());

		}
		return result;
	}
	
	public List<Long> getSaleIds() {
		return saleIds;
	}

	public void setSaleIds(List<Long> saleIds) {
		this.saleIds = saleIds;
	}
	
	public List<RequestSaleReturnSave> getReuquestSaleReturnList() {
		return reuquestSaleReturnList;
	}

	public void setReuquestSaleReturnList(List<RequestSaleReturnSave> reuquestSaleReturnList) {
		this.reuquestSaleReturnList = reuquestSaleReturnList;
	}
	
	public Double getTotalreturnPaidAmount() {
		return totalreturnPaidAmount;
	}

	public void setTotalreturnPaidAmount(Double totalreturnPaidAmount) {
		this.totalreturnPaidAmount = totalreturnPaidAmount;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}
}