package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class GRNBonusBatchExpiry {

	private Integer grnBonusBatchExpiryId;

	private String batchNo;
	
	private Integer grnDetailId;
	
	private Date expiryDate;
	
	private Integer bonusQuantity;

	public Integer getGrnBonusBatchExpiryId() {
		return grnBonusBatchExpiryId;
	}

	public void setGrnBonusBatchExpiryId(Integer grnBonusBatchExpiryId) {
		this.grnBonusBatchExpiryId = grnBonusBatchExpiryId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getBonusQuantity() {
		return bonusQuantity;
	}

	public void setBonusQuantity(Integer bonusQuantity) {
		this.bonusQuantity = bonusQuantity;
	}

}
