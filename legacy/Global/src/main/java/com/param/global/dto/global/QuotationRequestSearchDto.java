package com.param.global.dto.global;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class QuotationRequestSearchDto extends AgainstQuotationRequest{
	
	private Integer Id;
	
	private Date wrDate;
	
	private Date qrDate;
	
	private String storeName;
	
	private String requestBy;
	
	private String authorisedBy;
	
	private String wrNo;
	private String vqrNo;
	
	public String getVqrNo() {
		return vqrNo;
	}

	public void setVqrNo(String vqrNo) {
		this.vqrNo = vqrNo;
	}

	private String qrNo;
	
	private Integer storeId;
	
	List<QuotationRequestDetailResponse> items;
	

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	
	public List<QuotationRequestDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<QuotationRequestDetailResponse> items) {
		this.items = items;
	}

	

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getQrNo() {
		return qrNo;
	}

	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
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
		
	public Date getWrDate() {
		return wrDate;
	}

	public void setWrDate(Date wrDate) {
		this.wrDate = wrDate;
	}

	public String getWrNo() {
		return wrNo;
	}

	public void setWrNo(String wrNo) {
		this.wrNo = wrNo;
	}

	public QuotationRequestSearchDto()
	{
		super();
	}
	
	public QuotationRequestSearchDto(Integer id, String workOrderNo, Date addedDate, Integer storeId,
			String storeName, String addedByName, String ApprovedByName) {
		this.Id = id;
		this.wrNo = workOrderNo;
		this.wrDate = addedDate;
		this.qrNo = workOrderNo;
		this.qrDate = addedDate;
		this.storeId = storeId;
		this.storeName = storeName;
		this.requestBy = addedByName;
		this.authorisedBy = ApprovedByName;
	}

	/*
	 * public QuotationRequestSearchDto(Integer id, String qrNo, Date addedDate,
	 * Integer storeId, String storeName, String addedByName, String ApprovedByName)
	 * { this.Id = id; this.qrNo = qrNo; this.qrDate = addedDate; this.storeId =
	 * storeId; this.storeName = storeName; this.requestBy = addedByName;
	 * this.authorisedBy = ApprovedByName; }
	 */
	
	public QuotationRequestSearchDto(Integer id, String qrNo,Date qrDate) {
		super();
		Id = id;
		this.qrNo = qrNo;
		this.qrDate = qrDate;
	}
}
