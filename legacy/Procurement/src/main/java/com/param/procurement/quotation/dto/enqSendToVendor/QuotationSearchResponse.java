package com.param.procurement.quotation.dto.enqSendToVendor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuotationSearchResponse {
	
	private Integer id;

	private Date qrDate;
	
	private String qrNumber;
	
	private String storeName;
	
	private String requestedBy;
	
	private String authorisedBy;
	
	private List<QuotationDetailSearchResponse> items;

	public QuotationSearchResponse(Integer id, Date qrDate, String qrNumber, String storeName, String requestedBy,
			String authorisedBy) {
		super();
		this.id = id;
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

	public List<QuotationDetailSearchResponse> getItems() {
		return items;
	}

	public void setItems(List<QuotationDetailSearchResponse> items) {
		this.items = items;
	}
	
	public void addItems(QuotationDetailSearchResponse item) {
		if (this.items == null)
			this.items = new ArrayList<QuotationDetailSearchResponse>();
		
		this.items.add(item);
	}
}
