package com.param.procurement.purchase.dto.purchaseRequestApproval;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.service.global.model.CookieObjectDto;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseRequestApproval extends CookieObjectDto{

    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fromDate;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date toDate;
    private Integer storeId;
    private Integer requestingStoreId;
    private Integer statusId;
    private Integer priorityId;
    private Integer reasonId;
    public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	private String note;
    private Integer approvalStatusId;
    private List<ItemRequest> items;
    private List<Integer> prId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
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

    public List<Integer> getPrId() {
        return prId;
    }

    public void setPrId(List<Integer> prId) {
        this.prId = prId;
    }

    @Override
	public String toString() {
		return "PurchaseRequestApproval [id=" + id + ", fromDate=" + fromDate + ", toDate=" + toDate + ", storeId="
				+ storeId + ", requestingStoreId=" + requestingStoreId + ", statusId=" + statusId + ", priorityId="
				+ priorityId + ", reasonId=" + reasonId + ", note=" + note + ", approvalStatusId=" + approvalStatusId
				+ ", items=" + items + ", prId=" + prId + "]";
	}
}
