package com.param.procurement.purchase.dto.itemRequest;

import java.util.Date;

public class ItemRequestSearchDto {
	
	 private Integer id;

     private String requestNo;
     
     private Date requestDate;
     
     private String assetType;
     
     private String aadedBy;
     
     private String status;
     
     private String approvalStatus;
     
     
     public ItemRequestSearchDto(Integer id, String requestNo, Date requestDate, String assetType, String aadedBy,
			String status, String approvalStatus) {
		super();
		this.id = id;
		this.requestNo = requestNo;
		this.requestDate = requestDate;
		this.assetType = assetType;
		this.aadedBy = aadedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAadedBy() {
		return aadedBy;
	}

	public void setAadedBy(String aadedBy) {
		this.aadedBy = aadedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

}
