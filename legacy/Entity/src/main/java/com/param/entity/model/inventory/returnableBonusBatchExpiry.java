package com.param.entity.model.inventory;
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
@Entity(name = "returnableBonusBatchExpiry")
@Table(name = "t_returnable_bonus_batch_expiry", schema = "inventory")
public class returnableBonusBatchExpiry extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "returnable_bonus_batch_expiry_id")
    private Integer returnableBonusBatchExpiryId;

    @Column(name = "batch_no")
    private String batchNo;
    
    @Column(name = "grn_detail_id")
    private Integer grnDetailId;
    
    @Column(name = "expiry_date")
    private Date expiryDate;
    
    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "returned_quantity")
    private Integer returnedQuantity;
    
    @Column(name = "return_quantity")
    private Integer returnQuantity;
    
    @Column(name = "returnable_gatepass_detail_id")
    private Integer returnableGatepassDetailId;
    
    @Column(name = "bonus_quantity")
    private Integer bonusQuantity;
    
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

	public Integer getReturnableBonusBatchExpiryId() {
		return returnableBonusBatchExpiryId;
	}

	public void setReturnableBonusBatchExpiryId(Integer returnableBonusBatchExpiryId) {
		this.returnableBonusBatchExpiryId = returnableBonusBatchExpiryId;
	}

	public Integer getReturnableGatepassDetailId() {
		return returnableGatepassDetailId;
	}

	public void setReturnableGatepassDetailId(Integer returnableGatepassDetailId) {
		this.returnableGatepassDetailId = returnableGatepassDetailId;
	}

	public Integer getBonusQuantity() {
		return bonusQuantity;
	}

	public void setBonusQuantity(Integer bonusQuantity) {
		this.bonusQuantity = bonusQuantity;
	}


}