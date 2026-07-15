package com.param.procurement.purchase.dto.purchaseRequestApproval;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequest {

    private Integer id;
    private Integer approvedQuantity;
    private Integer rejectQuantity;
    private Integer rejectReasonId;
    private String rejectNote;
    private Integer statusId;

    public ItemRequest() {
    }

    public ItemRequest(Integer id, Integer approvedQuantity, Integer rejectQuantity, Integer rejectReasonId, String rejectNote, Integer statusId) {
        this.id = id;
        this.approvedQuantity = approvedQuantity;
        this.rejectQuantity = rejectQuantity;
        this.rejectReasonId = rejectReasonId;
        this.rejectNote = rejectNote;
        this.statusId = statusId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApprovedQuantity() {
        return approvedQuantity;
    }

    public void setApprovedQuantity(Integer approvedQuantity) {
        this.approvedQuantity = approvedQuantity;
    }

    public Integer getRejectQuantity() {
        return rejectQuantity;
    }

    public void setRejectQuantity(Integer rejectQuantity) {
        this.rejectQuantity = rejectQuantity;
    }

    public Integer getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(Integer rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    @Override
    public String toString() {
        return "ItemRequest{" +
                "id=" + id +
                ", approvedQuantity=" + approvedQuantity +
                ", rejectQuantity=" + rejectQuantity +
                ", rejectReasonId=" + rejectReasonId +
                ", rejectNote='" + rejectNote + '\'' +
                ", statusId=" + statusId +
                '}';
    }
}
