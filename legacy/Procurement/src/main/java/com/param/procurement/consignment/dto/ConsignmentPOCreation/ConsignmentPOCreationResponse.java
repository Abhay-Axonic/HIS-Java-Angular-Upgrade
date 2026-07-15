package com.param.procurement.consignment.dto.ConsignmentPOCreation;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ConsignmentPOCreationResponse {
	
private Integer id;
	
	private String store;
	
	private String itemCode;
	
	private String itemName;
	
	private String batchName;
	
	private Date expiryDate;
	
	private Integer poQuantoty;
	
	private Integer vendorId;
	
	private Integer storeId;
	
	private Integer itemId;
	
	private Double cop;
	
	private Integer taxId;
	
	private Double taxPercentage;
	
	private Double mrp;
	
	private Integer uomUnitId;
	
	private Integer uomTypeId;
	
	private Boolean isConsignment;
	
	private Boolean isImplant;
	
	private Date purchaseDate;
	
	private String purchaseNo;
	
	private String approvalStatus;
	
	private String status;
	
	private String addedBy;
	
	private String vendorName;
	
	private String remark;
	
	private Date addedDate;
	
	private Integer quantity;
	
	private Double totalAmount;
	
	private Double taxAmount;
	

	
	private Double netAmount;
	
	private List<ConsignmentPOCreationDetailResponse> items;
	
	public List<ConsignmentPOCreationDetailResponse> getItems() {
		return items;
	}


	public void setItems(List<ConsignmentPOCreationDetailResponse> items) {
		this.items = items;
	}


	public ConsignmentPOCreationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Integer getQuantity() {
		return quantity;
	}


	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public Boolean getIsImplant() {
		return isImplant;
	}


	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}


	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	

	public Integer getPoQuantoty() {
		return poQuantoty;
	}


	public void setPoQuantoty(Integer poQuantoty) {
		this.poQuantoty = poQuantoty;
	}


	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	
	
	 public Date getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public String getPurchaseNo() {
		return purchaseNo;
	}


	public void setPurchaseNo(String purchaseNo) {
		this.purchaseNo = purchaseNo;
	}


	public String getApprovalStatus() {
		return approvalStatus;
	}


	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getAddedBy() {
		return addedBy;
	}


	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}


	public String getVendorName() {
		return vendorName;
	}


	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Date getAddedDate() {
		return addedDate;
	}


	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public Double getTaxAmount() {
		return taxAmount;
	}


	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}


	public Double getNetAmount() {
		return netAmount;
	}


	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}   
	
	

	public ConsignmentPOCreationResponse(Integer id, Date purchaseDate, String purchaseNo, String storeName, Integer storeId,
			 String approvalStatus, String status, String addedBy, String remark,
				String vendorName,Date addedDate) {
			super();
			this.id = id;
			this.purchaseDate = purchaseDate;
			this.purchaseNo = purchaseNo;
			this.store = storeName;
			this.storeId = storeId;
			this.approvalStatus = approvalStatus;
			this.status = status;
			this.addedBy = addedBy;
			this.remark = remark;
			this.vendorName = vendorName;
			this.addedDate = addedDate;
		}   
	
	public ConsignmentPOCreationResponse(Integer storeId, String storeName, Integer itemId,String itemCode,String itemName,
			Date expiryDate,Integer quantity) {
			super();
			
			this.store = storeName;
			this.storeId = storeId;
			this.itemId=itemId;
			this.itemCode=itemCode;
			this.itemName=itemName;
			this.expiryDate=expiryDate;
			this.quantity=quantity;
			this.items=items;
		}


	public ConsignmentPOCreationResponse(Integer id,Double totalAmount, Double taxAmount, Double netAmount,
			List<ConsignmentPOCreationDetailResponse> purchaseOrderSearchDtoList,Integer storeId,Integer vendorId,String vendorName,
			String remark,Date purchaseDate,String purchaseNo) {
		this.totalAmount=totalAmount;
		this.taxAmount=taxAmount;
		this.netAmount= netAmount;
		this.items=purchaseOrderSearchDtoList;
		this.storeId=storeId;
		this.vendorId=vendorId;
		this.id=id;
		this.vendorName=vendorName;
		this.remark=remark;
		this.purchaseDate=purchaseDate;
		this.purchaseNo=purchaseNo;
	}
	
	public ConsignmentPOCreationResponse(Double totalAmount, Double taxAmount, Double netAmount,
			List<ConsignmentPOCreationDetailResponse> purchaseOrderSearchDtoList,Integer storeId,Integer vendorId
			) {
		this.totalAmount=totalAmount;
		this.taxAmount=taxAmount;
		this.netAmount= netAmount;
		this.items=purchaseOrderSearchDtoList;
		this.storeId=storeId;
		this.vendorId=vendorId;
		
		
	}


	

}
