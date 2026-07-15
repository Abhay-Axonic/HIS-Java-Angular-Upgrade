package com.param.procurement.quotation.dto.consignmentStockReturn;

public class ConsignmentStockReturnDto
{
	private Integer id;
	
    private String grnNo;
    
    private String crNo;
    
    private String storeName;
    
    private String assetType;
       
    private String vendorName;
      
    private Integer statusId;
    
    private String status;
    
    private Integer storeId;
    
    private Boolean isConsignment;
    
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public String getCrNo() {
		return crNo;
	}

	public void setCrNo(String crNo) {
		this.crNo = crNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public ConsignmentStockReturnDto()
	{
		super();
	}
	
	public ConsignmentStockReturnDto(Integer id, String grnNo, String storeName, String assetType,
			String vendorName, String status, Integer statusId) {
		super();
		this.id = id;
		this.grnNo = grnNo;
		this.storeName = storeName;
		this.assetType = assetType;	
		this.vendorName = vendorName;
		this.status = status;
		this.statusId = statusId;
	}
	
	public ConsignmentStockReturnDto(Integer id, String grnNo,String crNo,String storeName, String assetType,
			String vendorName, String status, Integer statusId,Integer storeId) {
		super();
		this.id = id;
		this.grnNo = grnNo;
		this.crNo = crNo;
		this.storeName = storeName;
		this.assetType = assetType;
		this.vendorName = vendorName;
		this.status = status;
		this.statusId = statusId;
		this.storeId=storeId;		
	}

	public ConsignmentStockReturnDto(Integer id, String grnNo, String crNo,String storeName, String assetType,
			String vendorName, String status, Integer statusId, Integer storeId, Boolean isConsignment) {
		super();
		this.id = id;
		this.grnNo = grnNo;
		this.crNo = crNo;
		this.storeName = storeName;
		this.assetType = assetType;
		this.vendorName = vendorName;
		this.status = status;
		this.statusId = statusId;
		this.storeId = storeId;
		this.isConsignment = isConsignment;

	}
}