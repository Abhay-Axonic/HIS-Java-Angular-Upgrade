package com.param.procurement.quotation.dto.vendorNegotiation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalDetailsResponse;

public class VendorNegotiationResponse {

	private Integer id;
	
	private Integer qrId;

	private String qrNumber;

	private Date qrDate;

	private String vqrNumber;

	private Date vqrDate;

	private Integer storeId;

	private String storeName;

	private String vendorName;

	private String refNumber;

	private Date refDate;

	private Integer statusId;

	private String status;
	
	private List<VendorNegotiationDetailsResponse> items;
	
	public VendorNegotiationResponse() {
		super();
	}

	public VendorNegotiationResponse(Integer id, Integer qrId,String qrNumber, Date qrDate, String vqrNumber, Date vqrDate,
			Integer storeId, String storeName, String vendorName, String refNumber, Date refDate, Integer statusId,
			String status) {
		super();
		this.id = id;
		this.qrId = qrId;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.vqrNumber = vqrNumber;
		this.vqrDate = vqrDate;
		this.storeId = storeId;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.refNumber = refNumber;
		this.refDate = refDate;
		this.statusId = statusId;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getVqrNumber() {
		return vqrNumber;
	}

	public void setVqrNumber(String vqrNumber) {
		this.vqrNumber = vqrNumber;
	}

	public Date getVqrDate() {
		return vqrDate;
	}

	public void setVqrDate(Date vqrDate) {
		this.vqrDate = vqrDate;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public Date getRefDate() {
		return refDate;
	}

	public void setRefDate(Date refDate) {
		this.refDate = refDate;
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
	
	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}

	public List<VendorNegotiationDetailsResponse> getItems() {
		return items;
	}

	public void setItems(List<VendorNegotiationDetailsResponse> items) {
		this.items = items;
	}
	
	
	public void addItems(VendorNegotiationDetailsResponse item) {
		if (this.items == null)
			this.items = new ArrayList<VendorNegotiationDetailsResponse>();
		
		this.items.add(item);
	}
}
