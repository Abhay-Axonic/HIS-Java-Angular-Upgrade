package com.param.procurement.purchase.dto.purchaseRequest;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class PostPurchaseRequest {
	
	
	 private Integer id;
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	    private Date fromDate;

	    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	    private Date toDate;
	    
	    @NotNull
	    private Integer assetTypeId;
	    @NotNull
	    private Integer prTypeId;
	    private String remark;
	    @NotNull
	    private Integer approvalStatusId;
	    
	    @NotNull
	    private Integer storeId;
	    @NotNull
	    private Integer statusId;
	    @NotNull
	    private Integer priorityId;
	    @NotNull
	    private Integer requestTypeId;
	    private Integer reasonId;
	    private String note;
	    
	    @NotNull
	    private List<ItemRequest> items;
	    private List<Integer> prId;
	    private List<Integer> indentId;


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

	    public Integer getPriorityId() {
	        return priorityId;
	    }

	    public void setPriorityId(Integer priorityId) {
	        this.priorityId = priorityId;
	    }

	    public Integer getAssetTypeId() {
	        return assetTypeId;
	    }

	    public void setAssetTypeId(Integer assetTypeId) {
	        this.assetTypeId = assetTypeId;
	    }

	    public Integer getPrTypeId() {
	        return prTypeId;
	    }

	    public void setPrTypeId(Integer prTypeId) {
	        this.prTypeId = prTypeId;
	    }

	    public String getRemark() {
	        return remark;
	    }

	    public void setRemark(String remark) {
	        this.remark = remark;
	    }

	    public Integer getStatusId() {
	        return statusId;
	    }

	    public void setStatusId(Integer statusId) {
	        this.statusId = statusId;
	    }

	    public Integer getApprovalStatusId() {
	        return approvalStatusId;
	    }

	    public void setApprovalStatusId(Integer approvalStatusId) {
	        this.approvalStatusId = approvalStatusId;
	    }

	    public List<ItemRequest> getItems() {
	        return items;
	    }

	    public void setItems(List<ItemRequest> items) {
	        this.items = items;
	    }

	    public Date getFromDate() {
	        return fromDate;
	    }

	    public void setFromDate(Date fromDate) {
	        this.fromDate = fromDate;
	    }

	    public Date getToDate() {
	        return toDate;
	    }

	    public void setToDate(Date toDate) {
	        this.toDate = toDate;
	    }

	    public Integer getRequestTypeId() {
	        return requestTypeId;
	    }

	    public void setRequestTypeId(Integer requestTypeId) {
	        this.requestTypeId = requestTypeId;
	    }

	    public Integer getReasonId() {
	        return reasonId;
	    }

	    public void setReasonId(Integer reasonId) {
	        this.reasonId = reasonId;
	    }

	    public String getNote() {
	        return note;
	    }

	    public void setNote(String note) {
	        this.note = note;
	    }

	    public List<Integer> getPrId() {
	        return prId;
	    }

	    public void setPrId(List<Integer> prId) {
	        this.prId = prId;
	    }

	    public List<Integer> getIndentId() {
	        return indentId;
	    }

	    public void setIndentId(List<Integer> indentId) {
	        this.indentId = indentId;
	    }

	    @Override
	    public String toString() {
	        return "PurchaseRequest{" +
	                "id=" + id +
	                ", fromDate=" + fromDate +
	                ", toDate=" + toDate +
	                ", assetTypeId=" + assetTypeId +
	                ", prTypeId=" + prTypeId +
	                ", remark='" + remark + '\'' +
	                ", approvalStatusId=" + approvalStatusId +
	                ", items=" + items +
	                ", storeId=" + storeId +
	                ", statusId=" + statusId +
	                ", priorityId=" + priorityId +
	                ", requestTypeId=" + requestTypeId +
	                ", reasonId=" + reasonId +
	                ", note='" + note + '\'' +
	                ", prId=" + prId +
	                ", indentId=" + indentId +
	                '}';
	    }


}
