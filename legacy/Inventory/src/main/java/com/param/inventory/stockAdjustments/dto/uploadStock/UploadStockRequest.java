package com.param.inventory.stockAdjustments.dto.uploadStock;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.param.service.global.model.CookieObjectDto;

public class UploadStockRequest extends CookieObjectDto {

	private Integer id;

	private List<Integer> stopTransactionsId;

	private Date fromDate;

	private Date toDate;

	private Integer storeId;

	private Integer stopTransactionId;

	private Integer statusId;

	private Integer approvalStatusId;

	private List<UploadStockRequestItem> items;

	private Integer cancelReasonId;

	private String cancelNote;
	
	protected List<Integer> uploadStockId;

	protected Integer computeStockId;

	protected Integer varianvceApprovalId;

	protected Long batchId;
	
	protected MultipartFile file;
	
	private Integer successCount;
	
	private Integer failureCount;
	
	private String failedRemark;
	
	private String remark;

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Integer> getStopTransactionsId() {
		return stopTransactionsId;
	}

	public void setStopTransactionsId(List<Integer> stopTransactionsId) {
		this.stopTransactionsId = stopTransactionsId;
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

	public Integer getStopTransactionId() {
		return stopTransactionId;
	}

	public void setStopTransactionId(Integer stopTransactionId) {
		this.stopTransactionId = stopTransactionId;
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

	public List<UploadStockRequestItem> getItems() {
		return items;
	}

	public void setItems(List<UploadStockRequestItem> items) {
		this.items = items;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<Integer> getUploadStockId() {
		return uploadStockId;
	}

	public void setUploadStockId(List<Integer> uploadStockId) {
		this.uploadStockId = uploadStockId;
	}

	public Integer getComputeStockId() {
		return computeStockId;
	}

	public void setComputeStockId(Integer computeStockId) {
		this.computeStockId = computeStockId;
	}

	public Integer getVarianvceApprovalId() {
		return varianvceApprovalId;
	}

	public void setVarianvceApprovalId(Integer varianvceApprovalId) {
		this.varianvceApprovalId = varianvceApprovalId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}

	public String getFailedRemark() {
		return failedRemark;
	}

	public void setFailedRemark(String failedRemark) {
		this.failedRemark = failedRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
