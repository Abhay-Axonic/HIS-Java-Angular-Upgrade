package com.param.procurement.quotation.dto.physicalInspection;

import java.util.Date;
import java.util.List;

import com.param.entity.model.inventory.Batch;

public class ConsignmentStockConversionDetails
{
	  private Integer id;  /*grnID*/
	  
	  private String grnNo;
	  
	  private Date grnDate;
	  
	  private String invoiceNo;
	  
	  private Date invoiceDate;
	  
	  private String dcNo;
	  
	  private Date dcDate;
	  
	  private Integer grnTypeId;
	  
	  private String grnType;
	  
	  private Integer storeId;
	  
	  private String storeName;
	  
	  private Integer vendorId;
	  
	  private String vendor;
	  
	  private Integer assetTypeId;
	  
	  private String assetType;
	  
	  private String addedBy;
	  
	  private Integer statusId;
	  
	  private String status;
	  
	  private List<ConsignmentStockConversionItem> items;
	  
	  private List<RackResponse> racks;
	  
	  private List<RejectReasonResponse> reject;
	  
	  private List<Batch> batch; 
	  private String remark;
	  public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Batch> getBatch() {
		return batch;
	}

	public void setBatch(List<Batch> batchLists) {
		this.batch = batchLists;
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
	
		public Date getGrnDate() {
			return grnDate;
		}
	
		public void setGrnDate(Date grnDate) {
			this.grnDate = grnDate;
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
	
		public Integer getGrnTypeId() {
			return grnTypeId;
		}
	
		public void setGrnTypeId(Integer grnTypeId) {
			this.grnTypeId = grnTypeId;
		}
	
		public String getGrnType() {
			return grnType;
		}
	
		public void setGrnType(String grnType) {
			this.grnType = grnType;
		}
	
		public Integer getStoreId() {
			return storeId;
		}
	
		public void setStoreId(Integer storeId) {
			this.storeId = storeId;
		}
	
		public String getStoreName() {
			return storeName;
		}
	
		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}
	
		public Integer getVendorId() {
			return vendorId;
		}
	
		public void setVendorId(Integer vendorId) {
			this.vendorId = vendorId;
		}
	
		public String getVendor() {
			return vendor;
		}
	
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
	
	
		public Integer getAssetTypeId() {
			return assetTypeId;
		}
	
	
		public void setAssetTypeId(Integer assetTypeId) {
			this.assetTypeId = assetTypeId;
		}
	
		public String getAssetType() {
			return assetType;
		}
	
		public void setAssetType(String assetType) {
			this.assetType = assetType;
		}
	
		public List<ConsignmentStockConversionItem> getItems() {
			return items;
		}
	
		public void setItems(List<ConsignmentStockConversionItem> items) {
			this.items = items;
		}
	
		public List<RackResponse> getRacks() {
			return racks;
		}

		public void setRacks(List<RackResponse> racks) {
			this.racks = racks;
		}

		public List<RejectReasonResponse> getReject() {
			return reject;
		}

		public void setReject(List<RejectReasonResponse> reject) {
			this.reject = reject;
		}

		public String getAddedBy() {
			return addedBy;
		}

		public void setAddedBy(String addedBy) {
			this.addedBy = addedBy;
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

		public ConsignmentStockConversionDetails()
		  {
			  super();
		  }

		public ConsignmentStockConversionDetails(Integer id, String grnNo, Date grnDate, String invoiceNo, Date invoiceDate,
				String dcNo, Date dcDate, Integer grnTypeId, String grnType, Integer storeId, String storeName,
				Integer vendorId, String vendor, Integer assetTypeId, String assetType,
				List<ConsignmentStockConversionItem> items, List<RackResponse> racks,List<RejectReasonResponse> reject, String addedBy) {
			super();
			this.id = id;
			this.grnNo = grnNo;
			this.grnDate = grnDate;
			this.invoiceNo = invoiceNo;
			this.invoiceDate = invoiceDate;
			this.dcNo = dcNo;
			this.dcDate = dcDate;
			this.grnTypeId = grnTypeId;
			this.grnType = grnType;
			this.storeId = storeId;
			this.storeName = storeName;
			this.vendorId = vendorId;
			this.vendor = vendor;
			this.assetTypeId = assetTypeId;
			this.assetType = assetType;
			this.items = items;
			this.racks = racks;
			this.reject = reject;
			this.addedBy = addedBy;
		}

//		Added By Atmiya 20191111
	private Boolean isConsignment;

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public ConsignmentStockConversionDetails(Integer id, String grnNo, Date grnDate, String invoiceNo, Date invoiceDate,
			String dcNo, Date dcDate, Integer grnTypeId, String grnType, Integer storeId, String storeName,
			Integer vendorId, String vendor, Integer assetTypeId, String assetType, List<ConsignmentStockConversionItem> items,
			List<RackResponse> racks, List<RejectReasonResponse> reject, String addedBy, Boolean isConsignment) {
		super();
		this.id = id;
		this.grnNo = grnNo;
		this.grnDate = grnDate;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
		this.grnTypeId = grnTypeId;
		this.grnType = grnType;
		this.storeId = storeId;
		this.storeName = storeName;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.items = items;
		this.racks = racks;
		this.reject = reject;
		this.addedBy = addedBy;
		this.isConsignment = isConsignment;
	}
//		End Added By Atmiya 20191111
	
	public ConsignmentStockConversionDetails(Integer id, String grnNo, Date grnDate, String invoiceNo, Date invoiceDate,
			String dcNo, Date dcDate, Integer grnTypeId, String grnType, Integer storeId, String storeName,
			Integer vendorId, String vendor, Integer assetTypeId, String assetType, List<ConsignmentStockConversionItem> items,
			List<RackResponse> racks, List<RejectReasonResponse> reject, String addedBy, Boolean isConsignment , List<Batch> batch) {
		super();
		this.id = id;
		this.grnNo = grnNo;
		this.grnDate = grnDate;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
		this.grnTypeId = grnTypeId;
		this.grnType = grnType;
		this.storeId = storeId;
		this.storeName = storeName;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.items = items;
		this.racks = racks;
		this.reject = reject;
		this.addedBy = addedBy;
		this.isConsignment = isConsignment;
		this.batch = batch;
	}
}