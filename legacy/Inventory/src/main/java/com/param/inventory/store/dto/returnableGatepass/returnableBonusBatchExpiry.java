package com.param.inventory.store.dto.returnableGatepass;
	import java.util.Date;

	import javax.persistence.Column;
	import javax.persistence.FetchType;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.JoinColumn;
	import javax.persistence.ManyToOne;

	public class returnableBonusBatchExpiry {

	    private Integer returnableBonusBatchExpiryId;

	    private String batchNo;
	    
	    private Integer grnDetailId;
	    
	    private Date expiryDate;
	    
	    private Integer bonusQuantity;
	    
	    private Integer returnQuantity;
	    
	    private Integer returnedQuantity;
	    
	    private Long batchId;
	    
	    private Integer returnableGatepassDetailId;
	    

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

		public Integer getReturnableBonusBatchExpiryId() {
			return returnableBonusBatchExpiryId;
		}

		public void setReturnableBonusBatchExpiryId(Integer returnableBonusBatchExpiryId) {
			this.returnableBonusBatchExpiryId = returnableBonusBatchExpiryId;
		}

		public Integer getReturnQuantity() {
			return returnQuantity;
		}

		public void setReturnQuantity(Integer returnQuantity) {
			this.returnQuantity = returnQuantity;
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

		public Integer getReturnableGatepassDetailId() {
			return returnableGatepassDetailId;
		}

		public void setReturnableGatepassDetailId(Integer returnableGatepassDetailId) {
			this.returnableGatepassDetailId = returnableGatepassDetailId;
		}

	}
