package com.param.inventory.stockAdjustments.dto.computeStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.inventory.stockAdjustments.dto.varianceApproval.BatchResponse;
//import com.param.global.dto.items.BatchResponse;

public class ComputeStockItemResponse {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer ProductCategoryId;

	protected String productCategory;

	protected Integer uomTypeId;

	protected String uomType;

	protected Integer uomUnitId;

	protected String uomUnit;

	protected Long batchId;

	protected String batchNo;

	protected Date expiryDate;

	protected Double cop;

	protected Double mrp;

	protected Long availableQuantity;

	protected Integer uploadedQuantity;

	protected Integer statusId;

	protected String status;

		
		protected Date newExpiryDate;
		
		protected String newBatchNo;
			
		protected Double newMrp;
		
		protected String remark;
		
		protected Integer rejectedQuantity;	
		
		protected List<BatchResponse> batch;	
			
	public ComputeStockItemResponse() {
		super();
	}

	public ComputeStockItemResponse(Integer id, Integer itemId, String itemName, String itemCode,
			Integer productCategoryId, String productCategory, Integer uomTypeId, String uomType, Integer uomUnitId,
			String uomUnit, Long batchId, String batchNo, Date expiryDate, Integer approvedUploadQuantity, Integer statusId, String status,Date newExpiryDate,String newBatchNo,Double newMrp,String remark,Integer rejectedQuantity) { 

		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.ProductCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
//		this.cop = cop;
//		this.mrp = mrp;
//		this.availableQuantity = availableQuantity;
		this.uploadedQuantity = approvedUploadQuantity;
		this.statusId = statusId;
		
		
		this.newExpiryDate = newExpiryDate;
		this.newBatchNo = newBatchNo;
		this.newMrp = newMrp;	
		this.remark = remark;
		this.rejectedQuantity = rejectedQuantity;
		// this.batch = batch;
	}
	
	public ComputeStockItemResponse(Integer id, Integer itemId, String itemName, String itemCode,
			Integer productCategoryId, String productCategory, Integer uomTypeId, String uomType, Integer uomUnitId,
			String uomUnit, Long batchId, String batchNo, Date expiryDate, Double cop, Double mrp,
			Long availableQuantity, Integer approvedUploadQuantity, Integer statusId, String status,Date newExpiryDate,String newBatchNo,Double newMrp,String remark,Integer rejectedQuantity,List<BatchResponse> batch) { 

		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.ProductCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.mrp = mrp;
		this.availableQuantity = availableQuantity;
		this.uploadedQuantity = approvedUploadQuantity;
		this.statusId = statusId;
		
		
		this.newExpiryDate = newExpiryDate;
		this.newBatchNo = newBatchNo;
		this.newMrp = newMrp;	
		this.remark = remark;
		this.rejectedQuantity = rejectedQuantity;
		this.batch = batch;		
	}	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getProductCategoryId() {
		return ProductCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		ProductCategoryId = productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Integer getUploadedQuantity() {
		return uploadedQuantity;
	}

	public void setUploadedQuantity(Integer uploadedQuantity) {
		this.uploadedQuantity = uploadedQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
		public Date getNewExpiryDate() {
			return newExpiryDate;
		}

		public void setNewExpiryDate(Date newExpiryDate) {
			this.newExpiryDate = newExpiryDate;
		}
		
		public Double getNewMrp() {
			return newMrp;
		}

		public void setNewMrp(Double newMrp) {
			this.newMrp = newMrp;
		}
		
		public String getNewBatchNo() {
			return newBatchNo;
		}

		public void setNewBatchNo(String newBatchNo) {
			this.newBatchNo = newBatchNo;
		}
		
		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		public Integer getRejectQuantity() {
			return rejectedQuantity;
		}

		public void setRejectQuantity(Integer rejectedQuantity) {
			this.rejectedQuantity = rejectedQuantity;
		}
		
		public List<BatchResponse> getBatch() {
			return batch;
		}
		public void setBatch(List<BatchResponse> batch) {
			this.batch = batch;
		}

		public void addBatches(BatchResponse batch) {
			if (this.batch == null)
				this.batch = new ArrayList<BatchResponse>();

			this.batch.add(batch);
		}
		

}
