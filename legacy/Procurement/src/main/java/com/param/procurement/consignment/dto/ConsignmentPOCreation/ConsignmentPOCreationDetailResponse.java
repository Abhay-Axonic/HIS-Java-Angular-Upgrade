package com.param.procurement.consignment.dto.ConsignmentPOCreation;

import java.util.Date;
import java.util.List;

import com.param.entity.model.master.Tax;

public class ConsignmentPOCreationDetailResponse {
	


	private Integer id;
	
	private Integer issueId;
	
	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	private String store;
	
	private String itemCode;
	
	private String itemName;
	
	private String batchName;
	
	private Date expiry;
	
	private Integer poQuantoty;
	
	private Long batchId;
	
	private Integer vendorId;
	
	private Integer storeId;
	
	private Integer itemId;
	
	private Double cop;
	
	private Integer taxId;
	
	private String taxCode;
	
	private Double taxPercentage;
	
	private Double taxAmount;
	
	private Double totalAmount;
	
	private Double netAmount;
	
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
	
	private String batchNo;
	
	private String taxName;
	
	private Integer uomInitId;
	
	
	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getUomInitId() {
		return uomInitId;
	}

	public void setUomInitId(Integer uomInitId) {
		this.uomInitId = uomInitId;
	}

	private List<PatientWisePOIssueList> issueList;
	
	public ConsignmentPOCreationDetailResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConsignmentPOCreationDetailResponse(Integer storeId, String storeName, Integer itemId, String itemCode,
			String itemName, Date expiryDate, Integer rejectedQuantity,Long batchId,String batchNo, Double cop,String taxCode, String taxName, Double taxPercentage,
			Double sellingPrice, Double detailTotalAmount, Double detailTaxAmount,
			Double detailNetAmount, List<PatientWisePOIssueList> issueList,Integer taxId,Integer uomTypeId,Integer uomUnitId,Integer id,String remark) {
		
		this.storeId=storeId;
		this.store=storeName;
		this.itemId=itemId;
		this.batchId=batchId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.expiry=expiryDate;
		this.quantity=rejectedQuantity;
		this.cop=cop;
		this.mrp=sellingPrice;
		this.taxCode=taxCode;
		this.taxPercentage=taxPercentage;
		this.taxAmount=detailTaxAmount;
		this.netAmount=detailNetAmount;
		this.issueList=issueList;
		this.taxName=taxName;	
		this.taxId=taxId;
		this.uomTypeId=uomTypeId;
		this.uomUnitId=uomUnitId;
		this.id=id;
		this.remark=remark;
		
	}
	
	public ConsignmentPOCreationDetailResponse(Integer storeId, String storeName, Integer itemId, String itemCode,
			String itemName, Date expiryDate, Integer rejectedQuantity,Long batchId,String batchNo, Double cop,String taxCode, String taxName, Double taxPercentage,
			Double sellingPrice, Double detailTotalAmount, Double detailTaxAmount,
			Double detailNetAmount, List<PatientWisePOIssueList> issueList,Integer taxId,Integer uomTypeId,Integer uomUnitId) {
		
		this.storeId=storeId;
		this.store=storeName;
		this.itemId=itemId;
		this.batchId=batchId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.expiry=expiryDate;
		this.quantity=rejectedQuantity;
		this.cop=cop;
		this.mrp=sellingPrice;
		this.taxCode=taxCode;
		this.taxPercentage=taxPercentage;
		this.taxAmount=detailTaxAmount;
		this.netAmount=detailNetAmount;
		this.issueList=issueList;
		this.taxName=taxName;	
		this.taxId=taxId;
		this.uomTypeId=uomTypeId;
		this.uomUnitId=uomUnitId;
		
		
	}
	

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
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

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<PatientWisePOIssueList> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<PatientWisePOIssueList> issueList) {
		this.issueList = issueList;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	

}
