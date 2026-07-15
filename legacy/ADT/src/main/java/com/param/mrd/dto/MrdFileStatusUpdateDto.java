package com.param.mrd.dto;

public class MrdFileStatusUpdateDto {
    private  Integer allocationId;
    private Integer updatedBy;

    private String fileStatus;

    private String requestedNo;

    private String issuedNo;

    private Integer requestedById;

    public String getRequestedNo() {
        return requestedNo;
    }

    public void setRequestedNo(String requestedNo) {
        this.requestedNo = requestedNo;
    }

    public Integer getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Integer allocationId) {
        this.allocationId = allocationId;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getIssuedNo() {
        return issuedNo;
    }

    public void setIssuedNo(String issuedNo) {
        this.issuedNo = issuedNo;
    }

    public Integer getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(Integer requestedById) {
        this.requestedById = requestedById;
    }
}
