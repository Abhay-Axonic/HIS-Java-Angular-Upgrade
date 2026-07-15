package com.param.procurement.quotation.dto.quotationFill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorDetailRespDto;

public class QuotationVendorSearchResponse {

	private Integer id;

	private Date vqrDate;

	private String vqrNumber;
	
	private Integer qrId;

	private Date qrDate;

	private String qrNumber;

	private String storeName;

	private String requestedBy;

	private String authorisedBy;
	
	private List<QuotationVendorSearchDetailResponse> items;
	
	
	public QuotationVendorSearchResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public QuotationVendorSearchResponse(Integer id, Date vqrDate, String vqrNumber, Integer qrId, Date qrDate, String qrNumber,
			String storeName, String requestedBy, String authorisedBy) {
		super();
		this.id = id;
		this.vqrDate = vqrDate;
		this.vqrNumber = vqrNumber;
		this.qrId = qrId;
		this.qrDate = qrDate;
		this.qrNumber = qrNumber;
		this.storeName = storeName;
		this.requestedBy = requestedBy;
		this.authorisedBy = authorisedBy;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getVqrDate() {
		return vqrDate;
	}

	public void setVqrDate(Date vqrDate) {
		this.vqrDate = vqrDate;
	}

	public String getVqrNumber() {
		return vqrNumber;
	}

	public void setVqrNumber(String vqrNumber) {
		this.vqrNumber = vqrNumber;
	}

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getAuthorisedBy() {
		return authorisedBy;
	}

	public void setAuthorisedBy(String authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public List<QuotationVendorSearchDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<QuotationVendorSearchDetailResponse> items) {
		this.items = items;
	}
	
	public void addItems(QuotationVendorSearchDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<QuotationVendorSearchDetailResponse>();
		
		this.items.add(item);
	}
	
	
	

}
