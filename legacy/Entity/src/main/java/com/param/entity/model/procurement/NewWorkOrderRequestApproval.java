package com.param.entity.model.procurement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_WO_REQUEST_APPROVAl_BY_REQUEST_ID", 
			query = "   select " + 
					"   request.workorder_request_approval_id as \"workorderRequestApprovalId\", "+
					"	request.workorder_request_id as \"workorderRequestId\", " + 
					"	request.workorder_request_number as \"workorderRequestNumber\", " + 
					"	request.against_type_id as \"againstTypeId\", " +
					"	request.bill_type_id as \"billTypeId\", " + 
					"	request.store_id as \"storeId\", " + 
					"	request.vendor_id as \"vendorId\", " + 
					"	to_char(request.delivery_date, 'dd/mm/yyyy') as \"deliveryDate\", " + 
					"	request.contract_type_id as \"contractTypeId\", " + 
					"	to_char(request.workorder_request_date , 'dd/mm/yyyy') as \"workorderRequestDate\", " + 
					"	request.total_amount as \"totalAmount\", " + 
					"	request.total_discount_amount as \"totalDiscountAmount\", " + 
					"	request.total_tax_amount as \"totalTaxAmount\", " + 
					"	request.other_charge_amount as \"otherChargeAmount\", " + 
					"	request.total_net_amount as \"totalNetAmount\", " + 
					   " 	(case " 
						 + " 		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
						 + " 		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
						 + " 		else 'Admin' " 
						 + " 	end) as \"userName\", " 
					     + " 	(case " 
						 + " 		when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " 
						 + " 		when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) " 
						 + " 		else 'Admin' " 
						 + " 	end) as \"approvedName\", " +
					"	request.org_id as \"orgId\", " + 
					"	request.unit_id as \"unitId\", " + 
					"	request.created_by as \"createdBy\", " + 
					"	request.approval_remark as \"approvalRemark\", " + 
					"	worequest.workorder_request_status_id as \"workorderRequestStatusId\", " + 
					"	vendortab.name as \"vendorName\", " + 
					"	storetab.store_name as \"storeName\", " + 
					"   agaisttypeMst.type as \"againstTypeName\" , sapproval.status as \"approvalStatus\", "+
					"	worequest.is_reverse_tax as \"isReverseTax\", " +
					"	request.payment_status_id as \"paymentStatusId\" " +
					
					"   from " + 
					"	procurement.t_new_work_order_approval request " + 
					"   left join procurement.t_new_work_order_creation worequest on " + 
					"	request.workorder_request_id = worequest.workorder_request_id " + 
					"   left join public.m_vendor vendortab on " + 
					"	request.vendor_id = vendortab.id " + 
					"   left join public.m_store storetab on " + 
					"	request.store_id = storetab.store_id " + 
					"   left join  public.m_request_type agaisttypeMst on " +
					"   request.against_type_id = agaisttypeMst.id"+
					"   left join PUBLIC.m_employee_master empmst on "+ 
					"  	worequest.created_by = empmst.user_id " +
					"   left join doctor.m_doctor_master docmst on " +
					" 	worequest.created_by = docmst.user_id " +
					"   left join PUBLIC.m_employee_master empmst1 on " +
					" 	worequest.approved_by = empmst1.user_id " +
					"   left join doctor.m_doctor_master docmst1 on "+ 
					" 	worequest.approved_by = docmst1.user_id  left join public.m_status  sapproval on worequest.workorder_approval_status_id = sapproval.id " +
					"   where " + 
					"	request.workorder_request_id =:workorderRequestId " + 
					"	and request.status = 'A'    " 
				),
	
	
	@NamedNativeQuery(name = "GET_WO_REQUEST_APPROVAL_DETAILS_BY_REQUEST_ID", 
				query = " select " 
						 + " 	requestDetails.workorder_request_approval_details_id as \"workorderRequestApprovalDetialsId\", " 
						 + " 	requestDetails.workorder_request_details_id as \"workorderRequestDetialsId\", " 
						 + " 	requestDetails.workorder_request_id as \"workorderRequestId\", " 
						 + " 	requestDetails.store_id as \"storeId\", " 
						 + " 	requestDetails.item_id as \"itemId\", " 
						 + " 	requestDetails.item_name as \"itemName\", " 
						 + " 	requestDetails.item_quantity as \"itemQuantity\", " 
						 + " 	requestDetails.item_rate as \"itemRate\", " 
						 + " 	requestDetails.item_mrp as \"itemMrp\", " 
						 + " 	requestDetails.item_disc_percentage as \"itemDiscPercentage\", " 
						 + " 	requestDetails.item_disc_amount as \"itemDiscAmount\", " 
						 + " 	requestDetails.item_tax_id as \"itemTaxId\", " 
						 + " 	requestDetails.item_tax as \"itemTax\", " 
						 + " 	requestDetails.item_tax_amount as \"itemTaxAmount\", " 
						 + " 	requestDetails.item_net_amount as \"itemNetAmount\", " 
						 + " 	requestDetails.item_remark as \"itemRemark\", " 
						 + " 	requestDetails.approved_qty as \"approvedQty\", " 
						 + " 	requestDetails.rejected_qty as \"rejectedQty\", " 
						 + " 	requestDetails.approval_detail_remark as \"approvalDetailRemark\", " 
						 + " 	requestDetails.is_rejected_qty as \"isRejectedQty\", " 
						 + " 	requestDetails.is_selected as \"isSelected\", " 
						 + " 	requestDetails.work_order_completion as \"workOrderCompletion\", " 
						 + " 	msm.service_standard_name as \"serviceName\", " 
						 + " 	tod.order_details_id as \"orderDetailsId\" " 
						 + " from " 
						 + " 	procurement.t_new_work_order_approval_details requestDetails " 
						 + " left join procurement.t_new_work_order_creation_details tnwocd on " 
						 + " 	tnwocd.workorder_request_details_id = requestDetails.workorder_request_details_id " 
						 + " left join t_order_details tod on " 
						 + " 	tod.order_details_id = tnwocd.order_details_id " 
						 + " left join service.m_service_master msm on " 
						 + " 	tod.service_id = msm.service_master_id " 
						 + " where " 
						 + " 	requestDetails.workorder_request_id = :workorderRequestId " 
						 + " 	and requestDetails.status = 'A' and requestDetails.is_selected=true "),
	@NamedNativeQuery(name = "GET_WO_REQUEST_APPROVAL_DETAILS_BY_REQUEST_ID_EDIT", 
	query = " select " 
			 + " 	requestDetails.workorder_request_approval_details_id as \"workorderRequestApprovalDetialsId\", " 
			 + " 	requestDetails.workorder_request_details_id as \"workorderRequestDetialsId\", " 
			 + " 	requestDetails.workorder_request_id as \"workorderRequestId\", " 
			 + " 	requestDetails.store_id as \"storeId\", " 
			 + " 	requestDetails.item_id as \"itemId\", " 
			 + " 	requestDetails.item_name as \"itemName\", " 
			 + " 	requestDetails.item_quantity as \"itemQuantity\", " 
			 + " 	requestDetails.item_rate as \"itemRate\", " 
			 + " 	requestDetails.item_mrp as \"itemMrp\", " 
			 + " 	requestDetails.item_disc_percentage as \"itemDiscPercentage\", " 
			 + " 	requestDetails.item_disc_amount as \"itemDiscAmount\", " 
			 + " 	requestDetails.item_tax_id as \"itemTaxId\", " 
			 + " 	requestDetails.item_tax as \"itemTax\", " 
			 + " 	requestDetails.item_tax_amount as \"itemTaxAmount\", " 
			 + " 	requestDetails.item_net_amount as \"itemNetAmount\", " 
			 + " 	requestDetails.item_remark as \"itemRemark\", " 
			 + " 	requestDetails.approved_qty as \"approvedQty\", " 
			 + " 	requestDetails.rejected_qty as \"rejectedQty\", " 
			 + " 	requestDetails.approval_detail_remark as \"approvalDetailRemark\", " 
			 + " 	requestDetails.is_rejected_qty as \"isRejectedQty\", " 
			 + " 	requestDetails.is_selected as \"isSelected\", " 
			 + " 	requestDetails.work_order_completion as \"workOrderCompletion\", " 
			 + " 	completion.work_order_completion_id as \"workOrderCompletionId\", " 
			 + " 	completion.invioce_date as \"invoiceDate\", " 
			 + " 	completion.invioce_number as \"invoiceNumber\", " 
			 + " 	completion.is_reverse_tax as \"isReverseTax\", " 
			 + " 	msm.service_standard_name as \"serviceName\", " 
			 + " 	tod.order_details_id as \"orderDetailsId\", "
			 + " 	completion.due_date as \"dueDate\" "
			 + " from " 
			 + " 	procurement.t_new_work_order_approval_details requestDetails " 
			 + " left join procurement.t_new_work_order_creation_details tnwocd on " 
			 + " 	tnwocd.workorder_request_details_id = requestDetails.workorder_request_details_id " 
			 + " left join t_order_details tod on " 
			 + " 	tod.order_details_id = tnwocd.order_details_id " 
			 + " left join service.m_service_master msm on " 
			 + " 	tod.service_id = msm.service_master_id " 
			 + " inner join procurement.t_new_work_order_completion completion on " 
			 + " 	completion.workorder_request_id = requestDetails.workorder_request_id " 
			 + " where " 
			 + " 	requestDetails.workorder_request_id =:workorderRequestId "
//			 + " 	and completion.work_order_completion_id =:workOrderCompletionId "
			 + " 	and requestDetails.status = 'A' " 
			 + " 	and completion.is_settled = false and requestDetails.is_selected=true "
		),
	@NamedNativeQuery(name = "GET_WO_REQUEST_APPROVAL_DETAILS_BY_REQUEST_ID_SETTLEMENT", 
	query = " select " 
			 + " 	requestDetails.workorder_request_approval_details_id as \"workorderRequestApprovalDetialsId\", " 
			 + " 	requestDetails.workorder_request_details_id as \"workorderRequestDetialsId\", " 
			 + " 	requestDetails.workorder_request_id as \"workorderRequestId\", " 
			 + " 	requestDetails.store_id as \"storeId\", " 
			 + " 	requestDetails.item_id as \"itemId\", " 
			 + " 	requestDetails.item_name as \"itemName\", " 
			 + " 	requestDetails.item_quantity as \"itemQuantity\", " 
			 + " 	requestDetails.item_rate as \"itemRate\", " 
			 + " 	requestDetails.item_mrp as \"itemMrp\", " 
			 + " 	requestDetails.item_disc_percentage as \"itemDiscPercentage\", " 
			 + " 	requestDetails.item_disc_amount as \"itemDiscAmount\", " 
			 + " 	requestDetails.item_tax_id as \"itemTaxId\", " 
			 + " 	requestDetails.item_tax as \"itemTax\", " 
			 + " 	requestDetails.item_tax_amount as \"itemTaxAmount\", " 
			 + " 	requestDetails.item_net_amount as \"itemNetAmount\", " 
			 + " 	requestDetails.item_remark as \"itemRemark\", " 
			 + " 	requestDetails.approved_qty as \"approvedQty\", " 
			 + " 	requestDetails.rejected_qty as \"rejectedQty\", " 
			 + " 	requestDetails.approval_detail_remark as \"approvalDetailRemark\", " 
			 + " 	requestDetails.is_rejected_qty as \"isRejectedQty\", " 
			 + " 	requestDetails.is_selected as \"isSelected\", " 
			 + " 	requestDetails.work_order_completion as \"workOrderCompletion\", " 
			 + " 	msm.service_standard_name as \"serviceName\", " 
			 + " 	tod.order_details_id as \"orderDetailsId\" " 
			 + " from " 
			 + " 	procurement.t_new_work_order_completion_details requestDetails " 
			 + " left join procurement.t_new_work_order_creation_details tnwocd on " 
			 + " 	tnwocd.workorder_request_details_id = requestDetails.workorder_request_details_id " 
			 + " left join t_order_details tod on " 
			 + " 	tod.order_details_id = tnwocd.order_details_id " 
			 + " left join service.m_service_master msm on " 
			 + " 	tod.service_id = msm.service_master_id " 
			 + " where " 
			 + " 	requestDetails.workorder_request_id = :workorderRequestId "
			 + "    and requestDetails.work_order_completion_id=:workOrderCompletionId "
			 + " 	and requestDetails.status = 'A' "),
	
	
	
	
	
	
})


@Entity
@Table(name = "t_new_work_order_approval", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_approval_seq", sequenceName = "procurement.t_new_work_order_approval_seq", allocationSize = 1)

public class NewWorkOrderRequestApproval {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_approval_seq")
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

	public Integer getWorkorderRequestApprovalId() {
		return workorderRequestApprovalId;
	}

	public void setWorkorderRequestApprovalId(Integer workorderRequestApprovalId) {
		this.workorderRequestApprovalId = workorderRequestApprovalId;
	}

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

	
	
	
	
	

	

	
}