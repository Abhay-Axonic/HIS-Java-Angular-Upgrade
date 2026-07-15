package com.param.entity.model.procurement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.param.entity.model.base.BaseEntity;
@Entity(name = "GrnBonusBatchExpiry")
@Table(name = "t_grn_bonus_batch_expiry", schema = "procurement")
public class GrnBonusBatchExpiry extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grn_bonus_batch_expiry_id")
	private Integer grnBonusBatchExpiryId;

	@Column(name = "batch_no")
	private String batchNo;
	
	@Column(name = "grn_detail_id")
	private Integer grnDetailId;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "bonus_quantity")
	private Integer bonusQuantity;
	
	@Column(name = "batch_id")
	private Long batchId;

	@Column(name = "returned_quantity")
	private Integer returnedQuantity;
	
	@Column(name = "return_quantity")
	private Integer returnQuantity;
	
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
	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}


}