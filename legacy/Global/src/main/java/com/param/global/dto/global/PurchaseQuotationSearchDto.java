package com.param.global.dto.global;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PurchaseQuotationSearchDto extends AgainstQuotationRequest{
	
	private Long Id;
	
	private Date qrDate;
	
	private String storeName;
	
	private String requestBy;
	
	private String authorisedBy;
	
	List<PurchaseRequestDetailResponse> items;
	
	
	public PurchaseQuotationSearchDto(Long id, String qrNo, Date prDate, Integer storeId, String storeName,
			String requestBy, String authorisedBy) {
		super();
		Id = id;
		super.qrNo = qrNo;
		this.qrDate = prDate;
		super.storeId = storeId;
		this.storeName = storeName;
		this.requestBy = requestBy;
		this.authorisedBy = authorisedBy;
	}
	
	public PurchaseQuotationSearchDto(Long id, String qrNo,Date prDate) {
		super();
		Id = id;
		super.qrNo = qrNo;
		this.qrDate = prDate;
	}

	public PurchaseQuotationSearchDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}

	public String getAuthorisedBy() {
		return authorisedBy;
	}

	public void setAuthorisedBy(String authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public List<PurchaseRequestDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<PurchaseRequestDetailResponse> items) {
		this.items = items;
	}
	
	


}
