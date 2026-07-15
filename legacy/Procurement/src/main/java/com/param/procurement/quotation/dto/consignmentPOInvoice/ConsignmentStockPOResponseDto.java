package com.param.procurement.quotation.dto.consignmentPOInvoice;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;
@JsonInclude(Include.NON_NULL)
public class ConsignmentStockPOResponseDto extends CookieObjectDto {
	
	private Integer id;	
	private Integer storeId;	
    private String storeName;    
    private Integer grnId;    
    private String grnNumber;    
    private String poNumber;
    private Date poDate;    
    private Integer assetTypeId;
    private String vendorName;
    private Integer poId;
    private Integer issueingStoreId;
    List<ConsignmentStockPODetailResponseDto> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public String getGrnNumber() {
		return grnNumber;
	}

	public void setGrnNumber(String grnNumber) {
		this.grnNumber = grnNumber;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<ConsignmentStockPODetailResponseDto> getItems() {
		return items;
	}

	public void setItems(List<ConsignmentStockPODetailResponseDto> items) {
		this.items = items;
	}
    
    
	/*start code for use as save request*/
    private Integer vendorId;
    private Integer statusId;
    private String status;
    private Integer requestingStoreId;
    private Integer issuingStoreId;
    private String invoiceNumber;
    private Date invoiceDate;

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public Integer getIssuingStoreId() {
		return issuingStoreId;
	}

	public void setIssuingStoreId(Integer issuingStoreId) {
		this.issuingStoreId = issuingStoreId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}    
    /*end code for use as save request*/
	
	
	/*start code for use as get consignment po invoice list*/
    private String requestingStore;
    private String issuingStore;    
    private Integer addedById;    
    private String addedBy;
	private Boolean isSelected;

	public String getRequestingStore() {
		return requestingStore;
	}

	public void setRequestingStore(String requestingStore) {
		this.requestingStore = requestingStore;
	}

	public String getIssuingStore() {
		return issuingStore;
	}

	public void setIssuingStore(String issuingStore) {
		this.issuingStore = issuingStore;
	}

	public Integer getAddedById() {
		return addedById;
	}

	public void setAddedById(Integer addedById) {
		this.addedById = addedById;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}  
    
	/*end code for use as get consignment po invoice list*/
	
	private String cancelNote;
	private Integer cancelReasonId;

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getIssueingStoreId() {
		return issueingStoreId;
	}

	public void setIssueingStoreId(Integer issueingStoreId) {
		this.issueingStoreId = issueingStoreId;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
}
