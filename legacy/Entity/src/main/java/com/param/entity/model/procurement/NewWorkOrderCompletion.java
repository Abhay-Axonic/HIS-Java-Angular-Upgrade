package com.param.entity.model.procurement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

    @Entity(name = "NewWorkOrderCompletion")
	@Table(name = "t_new_work_order_completion", schema = "procurement")
	@SequenceGenerator(name = "t_new_work_order_completion_seq", sequenceName = "procurement.t_new_work_order_completion_seq", allocationSize = 1)

	public class NewWorkOrderCompletion {
		
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_completion_seq")
		@Column(name = "work_order_completion_id")
		private Integer workOrderCompletionId;
		
		@Column(name = "workorder_request_approval_id")
		private Integer workorderRequestApprovalId;
		
		@Column(name = "workorder_request_id")
		private Integer workorderRequestId;
		
		@Column(name="workorder_request_number")
		private String workorderRequestNumber;
		
		@Column(name="store_id")
		private Integer storeId;
		
		 @Column(name="vendor_id")
		 private Integer vendorId;
		 
		 @Column(name="delivery_date")
		 private Date  deliveryDate;
		 
		 @Column(name="bill_type_id")
		 private Integer billTypeId;
		 
		 @Column(name="contract_type_id")
		 private Integer  contractTypeId;
		 
		 @Column(name="against_type_id")
		 private Integer  againstTypeId;
		 
		 @Column(name="approved_by")
		 private Integer  approvedBy;
		 
		 @Column(name="workorder_request_date")
		 private Date workorderRequestDate;
		 
		 @Column(name="total_amount")
		 private Double totalAmount;
		 
		 @Column(name="total_discount_amount")
		 private Double  totalDiscountAmount;
		 
		 @Column(name="total_tax_amount")
		 private Double totalTaxAmount;
		 
		 @Column(name="other_charge_amount")
		 private Double otherChargeAmount;
		
		 @Column(name="total_net_amount")
		 private Double totalNetAmount;
		 
		 
		 @Column(name="paid_amount")
		 private Double paidAmount;
		 
		 @Column(name="balance_amount")
		 private Double balanceAmount;
		 
		 @Column(name="payment_status_id")
		 private Integer paymentStatusId;
		 
		@Column(name="approval_remark")
		private String approvalRemark;
		
		@Column(name = "org_id")
		private Integer orgId;
		
		@Column(name = "unit_id")
		private Integer unitId;
		
		@Column(name="created_date")
		private Date createdDate;
		
		@Column(name="updated_date")
		private Date updatedDate;
		
		@Column(name="created_by")
		private Integer createdBy;
		
		@Column(name="updated_by")
		private Integer updatedBy;
		
		@Column(name="status")
		private String status;
		
		@Column(name="invioce_number")
		private String invioceNumber;
		
		@Column(name="invioce_date")
		private Date invioceDate;
		
		@Column(name="is_reverse_tax")
		private boolean isReverseTax;
		
		@Column(name="due_date")
		private Date dueDate;

		public Integer getWorkorderRequestId() {
			return workorderRequestId;
		}

		public void setWorkorderRequestId(Integer workorderRequestId) {
			this.workorderRequestId = workorderRequestId;
		}

		public String getWorkorderRequestNumber() {
			return workorderRequestNumber;
		}

		public void setWorkorderRequestNumber(String workorderRequestNumber) {
			this.workorderRequestNumber = workorderRequestNumber;
		}

		public Integer getStoreId() {
			return storeId;
		}

		public void setStoreId(Integer storeId) {
			this.storeId = storeId;
		}

		public Integer getVendorId() {
			return vendorId;
		}

		public void setVendorId(Integer vendorId) {
			this.vendorId = vendorId;
		}

		public Date getDeliveryDate() {
			return deliveryDate;
		}

		public void setDeliveryDate(Date deliveryDate) {
			this.deliveryDate = deliveryDate;
		}

		public Integer getBillTypeId() {
			return billTypeId;
		}

		public void setBillTypeId(Integer billTypeId) {
			this.billTypeId = billTypeId;
		}

		public Integer getContractTypeId() {
			return contractTypeId;
		}

		public void setContractTypeId(Integer contractTypeId) {
			this.contractTypeId = contractTypeId;
		}

		public Integer getAgainstTypeId() {
			return againstTypeId;
		}

		public void setAgainstTypeId(Integer againstTypeId) {
			this.againstTypeId = againstTypeId;
		}

		public Integer getApprovedBy() {
			return approvedBy;
		}

		public void setApprovedBy(Integer approvedBy) {
			this.approvedBy = approvedBy;
		}

		public Date getWorkorderRequestDate() {
			return workorderRequestDate;
		}

		public void setWorkorderRequestDate(Date workorderRequestDate) {
			this.workorderRequestDate = workorderRequestDate;
		}

		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public Double getTotalDiscountAmount() {
			return totalDiscountAmount;
		}

		public void setTotalDiscountAmount(Double totalDiscountAmount) {
			this.totalDiscountAmount = totalDiscountAmount;
		}

		public Double getTotalTaxAmount() {
			return totalTaxAmount;
		}

		public void setTotalTaxAmount(Double totalTaxAmount) {
			this.totalTaxAmount = totalTaxAmount;
		}

		public Double getOtherChargeAmount() {
			return otherChargeAmount;
		}

		public void setOtherChargeAmount(Double otherChargeAmount) {
			this.otherChargeAmount = otherChargeAmount;
		}

		public Double getTotalNetAmount() {
			return totalNetAmount;
		}

		public void setTotalNetAmount(Double totalNetAmount) {
			this.totalNetAmount = totalNetAmount;
		}

		public String getApprovalRemark() {
			return approvalRemark;
		}

		public void setApprovalRemark(String approvalRemark) {
			this.approvalRemark = approvalRemark;
		}

		public Integer getOrgId() {
			return orgId;
		}

		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
		}

		public Integer getUnitId() {
			return unitId;
		}

		public void setUnitId(Integer unitId) {
			this.unitId = unitId;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public Date getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
		}

		public Integer getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}

		public Integer getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Double getPaidAmount() {
			return paidAmount;
		}

		public void setPaidAmount(Double paidAmount) {
			this.paidAmount = paidAmount;
		}

		public Double getBalanceAmount() {
			return balanceAmount;
		}

		public void setBalanceAmount(Double balanceAmount) {
			this.balanceAmount = balanceAmount;
		}

		public Integer getPaymentStatusId() {
			return paymentStatusId;
		}

		public void setPaymentStatusId(Integer paymentStatusId) {
			this.paymentStatusId = paymentStatusId;
		}

		public Integer getWorkorderRequestApprovalId() {
			return workorderRequestApprovalId;
		}

		public void setWorkorderRequestApprovalId(Integer workorderRequestApprovalId) {
			this.workorderRequestApprovalId = workorderRequestApprovalId;
		}

		public Integer getWorkOrderCompletionId() {
			return workOrderCompletionId;
		}

		public void setWorkOrderCompletionId(Integer workOrderCompletionId) {
			this.workOrderCompletionId = workOrderCompletionId;
		}

		public String getInvioceNumber() {
			return invioceNumber;
		}

		public void setInvioceNumber(String invioceNumber) {
			this.invioceNumber = invioceNumber;
		}

		public Date getInvioceDate() {
			return invioceDate;
		}

		public void setInvioceDate(Date invioceDate) {
			this.invioceDate = invioceDate;
		}

		public boolean getIsReverseTax() {
			return isReverseTax;
		}

		public void setIsReverseTax(boolean isReverseTax) {
			this.isReverseTax = isReverseTax;
		}

		public Date getDueDate() {
			return dueDate;
		}

		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}

}
