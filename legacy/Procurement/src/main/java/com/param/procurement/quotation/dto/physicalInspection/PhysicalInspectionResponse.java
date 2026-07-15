package com.param.procurement.quotation.dto.physicalInspection;

import java.util.Date;

public class PhysicalInspectionResponse
{
	private Integer id;
	
    private Date grnDate;
    
    private String grnNo;
    
    private String storeName;
    
    private String assetType;
    
    private String invoiceNo;
    
    private Date invoiceDate;
    
    private String dcNo;
    
    private Date dcDate;
    
    private String vendorName;
    
    private String addedBy;
    
    private Integer statusId;
    
    private String status;
    
    private Integer storeId;
    

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

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
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

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDcNo() {
		return dcNo;
	}

	public void setDcNo(String dcNo) {
		this.dcNo = dcNo;
	}

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
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

	public PhysicalInspectionResponse()
	{
		super();
	}
	
	public PhysicalInspectionResponse(Integer id, Date grnDate, String grnNo, String storeName, String assetType,
			String invoiceNo, Date invoiceDate, String dcNo, Date dcDate, String vendorName, String addedBy,
			String status, Integer statusId) {
		super();
		this.id = id;
		this.grnDate = grnDate;
		this.grnNo = grnNo;
		this.storeName = storeName;
		this.assetType = assetType;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
		this.vendorName = vendorName;
		this.addedBy = addedBy;
		this.status = status;
		this.statusId = statusId;
	}
	public PhysicalInspectionResponse(Integer id, Date grnDate, String grnNo, String storeName, String assetType,
			String invoiceNo, Date invoiceDate, String dcNo, Date dcDate, String vendorName, String addedBy,
			String status, Integer statusId,Integer storeId) {
		super();
		this.id = id;
		this.grnDate = grnDate;
		this.grnNo = grnNo;
		this.storeName = storeName;
		this.assetType = assetType;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
		this.vendorName = vendorName;
		this.addedBy = addedBy;
		this.status = status;
		this.statusId = statusId;
		this.storeId=storeId;
		
	}

	/* Added By Atmiya 20191109 */
	private Boolean isConsignment;

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public PhysicalInspectionResponse(Integer id, Date grnDate, String grnNo, String storeName, String assetType,
			String invoiceNo, Date invoiceDate, String dcNo, Date dcDate, String vendorName, String addedBy,
			String status, Integer statusId, Integer storeId, Boolean isConsignment) {
		super();
		this.id = id;
		this.grnDate = grnDate;
		this.grnNo = grnNo;
		this.storeName = storeName;
		this.assetType = assetType;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
		this.vendorName = vendorName;
		this.addedBy = addedBy;
		this.status = status;
		this.statusId = statusId;
		this.storeId = storeId;
		this.isConsignment = isConsignment;

	}

	/* End Added By Atmiya 20191109 */

}