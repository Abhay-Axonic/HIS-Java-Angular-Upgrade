package com.param.procurement.quotation.dto.physicalInspection;

public class ConsignmentStockReturnResponse
{
	private Integer id;
	
	private Integer grnId;
	
    private String grnNo;
    
    private String crNo;
    
    private String storeName;
    
    private String assetType;
    
    private Integer assetTypeId;
       
    private String vendorName;
    
    private Integer vendorId;
      
    private Integer statusId;
    private Integer grnStatusId;
    
    private String status;
    private String grnStatus;
    
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

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
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

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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
	
	public Integer getGrnStatusId() {
		return grnStatusId;
	}

	public void setGrnStatusId(Integer grnStatusId) {
		this.grnStatusId = grnStatusId;
	}

	public String getGrnStatus() {
		return grnStatus;
	}

	public void setGrnStatus(String grnStatus) {
		this.grnStatus = grnStatus;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public ConsignmentStockReturnResponse()
	{
		super();
	}
	
	public ConsignmentStockReturnResponse(Integer id, String grnNo, String storeName, String assetType,
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
	
	public ConsignmentStockReturnResponse(Integer id, String grnNo,String crNo,String storeName, String assetType,
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

	public ConsignmentStockReturnResponse(Integer id, String grnNo, String crNo,String storeName, String assetType,
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