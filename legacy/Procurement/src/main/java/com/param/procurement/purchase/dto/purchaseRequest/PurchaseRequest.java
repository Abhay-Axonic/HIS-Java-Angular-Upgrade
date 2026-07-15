package com.param.procurement.purchase.dto.purchaseRequest;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dao.global.IGlobalDao;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseRequest extends CookieObjectDto {

	private Integer id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fromDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date toDate;
	private Integer assetTypeId;
	private Integer prTypeId;
	private String remark;
	private Integer approvalStatusId;
	private Integer storeId;
	private Integer requestingStoreId;
	private Integer statusId;
	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	private Integer priorityId;
	private Integer requestTypeId;
	private Integer reasonId;
	private String note;
	private List<Integer> prId;
	private List<Integer> indentId;
	private List<ItemRequest> items;
	private String validationMessage;

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

	public String getvalidationMessage() {
		return validationMessage;
	}

	public void setvalidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
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

	public String getNote() {
		return note;
	}

	@Override
	public String toString() {
		return "PurchaseRequest{" + "id=" + id + ", fromDate=" + fromDate + ", toDate=" + toDate + ", assetTypeId="
				+ assetTypeId + ", prTypeId=" + prTypeId + ", remark='" + remark + '\'' + ", approvalStatusId="
				+ approvalStatusId + ", items=" + items + ", storeId=" + storeId + ", statusId=" + statusId
				+ ", priorityId=" + priorityId + ", requestTypeId=" + requestTypeId + ", reasonId=" + reasonId
				+ ", note='" + note + '\'' + ", prId=" + prId + ", indentId=" + indentId + '}';
	}

	public Boolean ValidateSave(IGlobalDao GlobalDao) {
		Boolean result = true;
		this.validationMessage = "Please fill below required fields. | ";
		if (this.storeId == null || this.storeId < 1) {
			result = false;
			this.validationMessage += "storeId |";
		}

		if (this.priorityId == null || this.priorityId < 1) {
			result = false;
			this.validationMessage += "priorityId |";
		}

		if (this.assetTypeId == null || this.assetTypeId < 1) {
			result = false;
			this.validationMessage += "assetTypeId |";
		}

		if (this.requestTypeId == null || this.requestTypeId < 1) {
			result = false;
			this.validationMessage += "requestTypeId |";
		}

		if (this.statusId == null || this.statusId < 1) {
			result = false;
			this.validationMessage += "statusId |";
		}

		if (this.approvalStatusId == null || this.approvalStatusId < 1) {
			result = false;
			this.validationMessage += "approvalStatusId |";
		}
		if (this.items == null || this.items.isEmpty()) {
			result = false;
			this.validationMessage += "items |";
		} else if (this.items != null && !this.items.isEmpty() && (this.items.stream()
				.filter(i -> (Objects
						.equals(i.ValidateSave(this.assetTypeId, this.requestTypeId, this.storeId, GlobalDao), false)))
				.map(ItemRequest::getItemId).collect(Collectors.toList())).size() > 0) {

			result = false;
			// for(ItemRequest item : items) {
			// Boolean itemResult = item.ValidateSave(this.assetTypeId, this.requestTypeId);
			// if(!itemResult) {
			this.validationMessage += this.items.stream()
					.filter(i -> (Objects.equals(
							i.ValidateSave(this.assetTypeId, this.requestTypeId, this.storeId, GlobalDao), false)))
					.map(ItemRequest::getValidationMessage).collect(Collectors.toList());
			// }
			//
			// }
		}

		return result;
	}

	public Boolean ValidateUpdate(IGlobalDao GlobalDao) {
		Boolean result = true;
		this.validationMessage = "Please fill below required fields. | ";
		if (this.id == null) {
			result = false;
			this.validationMessage += "id |";
		}
		if (this.priorityId == null || this.priorityId < 1) {
			result = false;
			this.validationMessage += "priorityId |";
		}

		if (this.items == null || this.items.isEmpty()) {
			result = false;
			this.validationMessage += "items |";
		} else if (this.items != null && !this.items.isEmpty()) {
			for (ItemRequest item : items) {
				Boolean itemResult = item.validateUpdate(this.assetTypeId, this.requestTypeId, this.storeId, GlobalDao);
				if (!itemResult) {
					this.validationMessage += " |" + item.getValidationMessage();
				}
			}
		}

		return result;
	}
}
