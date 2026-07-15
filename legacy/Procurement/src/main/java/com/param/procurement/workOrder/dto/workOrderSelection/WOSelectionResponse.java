package com.param.procurement.workOrder.dto.workOrderSelection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationDetailResponse;
@JsonInclude(Include.NON_NULL)
public class WOSelectionResponse extends WOSelectionRequest{

	private String status;
	
	private String qrNo;
	
	private Date qrDate;
	
	private String storeName;
	
	private String contractType;
	
	private String qrAgainst;
	
	private String addedBy;
	
	private Integer id;
	
	private Integer statusId;
	
	
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	private List<WOSelectionDetailsResponse> detail;

	public WOSelectionResponse() {
		super();
	}
	
	public WOSelectionResponse(Integer id, Integer statusId, String status, String qrNo, Date qrDate, String storeName, String contractType,
			String qrAgainst, String addedBy) {
		
		this.id=id;
		this.statusId=statusId;
		this.status = status;
		this.qrNo = qrNo;
		this.qrDate = qrDate;
		this.storeName = storeName;
		this.contractType = contractType;
		this.qrAgainst = qrAgainst;
		this.addedBy = addedBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getQrNo() {
		return qrNo;
	}

	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
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

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getQrAgainst() {
		return qrAgainst;
	}

	public void setQrAgainst(String qrAgainst) {
		this.qrAgainst = qrAgainst;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	
	public List<WOSelectionDetailsResponse> getDetail() {
		return detail;
	}

	public void setDetail(List<WOSelectionDetailsResponse> detail) {
		this.detail = detail;
	}
	
	public void addItems(WOSelectionDetailsResponse item) {
		if (this.detail == null)
			this.detail = new ArrayList<WOSelectionDetailsResponse>();
		
		this.detail.add(item);
	}
	
}
