package com.param.procurement.purchase.dto.purchaseRequestApproval;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseRequestApprovalResponse {

    private Long id;
    private Date prDate;
    private String prNo;
    private String storeName;
    private String requestingStoreName;
    private String type;
    private String prType;
    private String priority;
    private String approvalStatus;
    private String status;
    private String requestedBy;
    private String remark;

    public PurchaseRequestApprovalResponse(Long id, Date prDate, String prNo, String storeName, String type,
                                           String prType, String priority, String approvalStatus, String status,
                                           String requestedBy, String remark , String requestingStoreName) {
        this.id = id;
        this.prDate = prDate;
        this.prNo = prNo;
        this.storeName = storeName;
        this.type = type;
        this.prType = prType;
        this.priority = priority;
        this.approvalStatus = approvalStatus;
        this.status = status;
        this.requestedBy = requestedBy;
        this.remark = remark;
        this.requestingStoreName = requestingStoreName;
    }

    public String getRequestingStoreName() {
		return requestingStoreName;
	}

	public void setRequestingStoreName(String requestingStoreName) {
		this.requestingStoreName = requestingStoreName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPrDate() {
        return prDate;
    }

    public void setPrDate(Date prDate) {
        this.prDate = prDate;
    }

    public String getPrNo() {
        return prNo;
    }

    public void setPrNo(String prNo) {
        this.prNo = prNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrType() {
        return prType;
    }

    public void setPrType(String prType) {
        this.prType = prType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "PurchaseRequestApprovalResponse{" +
                "id=" + id +
                ", prDate=" + prDate +
                ", prNo='" + prNo + '\'' +
                ", storeName='" + storeName + '\'' +
                ", type='" + type + '\'' +
                ", prType='" + prType + '\'' +
                ", priority='" + priority + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", status='" + status + '\'' +
                ", requestedBy='" + requestedBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
