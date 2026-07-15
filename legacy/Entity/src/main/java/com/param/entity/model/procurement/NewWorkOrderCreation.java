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
	
	@NamedNativeQuery(name = "GET_WO_REQUEST_BY_REQUEST_ID", 
	query = " select " 
					 + " 	request.workorder_request_id as \"workorderRequestId\", " 
					 + " 	request.workorder_request_number as \"workorderRequestNumber\", " 
					 + " 	request.against_type_id as \"againstTypeId\", " 
					 + " 	request.bill_type_id as \"billTypeId\", " 
					 + " 	request.store_id as \"storeId\", " 
					 + " 	request.vendor_id as \"vendorId\", " 
					 + " 	to_char(request.delivery_date, 'dd/mm/yyyy') as \"deliveryDate\", " 
					 + " 	request.contract_type_id as \"contractTypeId\", " 
					 + " 	to_char(request.workorder_request_date , 'dd/mm/yyyy') as \"workorderRequestDate\", " 
					 + " 	request.total_amount as \"totalAmount\", " 
					 + " 	request.total_discount_amount as \"totalDiscountAmount\", " 
					 + " 	request.total_tax_amount as \"totalTaxAmount\", " 
					 + " 	request.other_charge_amount as \"otherChargeAmount\", " 
					 + " 	request.total_net_amount as \"totalNetAmount\", " 
					 + " 	request.round_off as \"roundoff\", "
					 + "	request.is_reverse_tax as\"isReverseTax\", " 
					 + " 	(case " 
					 + " 		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
					 + " 		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
					 + " 		else 'Admin' " 
					 + " 	end) as \"userName\", " 
					 + " 	(case " 
					 + " 		when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " 
					 + " 		when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) " 
					 + " 		else 'Admin' " 
					 + " 	end) as \"approvedName\", " 
					 + " 	request.workorder_request_status_id as \"workorderRequestStatusId\", " 
					 + " 	s.status as \"status\", " 
					 + " 	request.payment_terms as \"paymentTerms\", " 
					 + " 	request.guarantee_warranty as \"guaranteeWarranty\", " 
					 + " 	request.org_id as \"orgId\", " 
					 + " 	request.unit_id as \"unitId\", " 
					 + " 	request.created_by as \"createdBy\", " 
					 + " 	vendortab.name as \"vendorName\", " 
					 + " 	storetab.store_name as \"storeName\", " 
					 + " 	agaisttypeMst.type as \"againstTypeName\", " 
					 + " 	request.bill_id as \"billId\", " 
					 + " 	request.admission_id as \"admissionId\", " 
					 + " 	request.encounter_id as \"encounterId\" " 
					 + " from " 
					 + " 	procurement.t_new_work_order_creation request " 
					 + " left join public.m_vendor vendortab on " 
					 + " 	request.vendor_id = vendortab.id " 
					 + " left join public.m_store storetab on " 
					 + " 	request.store_id = storetab.store_id " 
					 + " left join public.m_request_type agaisttypeMst on " 
					 + " 	request.against_type_id = agaisttypeMst.id " 
					 + " left join PUBLIC.m_employee_master empmst on " 
					 + " 	request.created_by = empmst.user_id " 
					 + " left join doctor.m_doctor_master docmst on " 
					 + " 	request.created_by = docmst.user_id " 
					 + " left join PUBLIC.m_employee_master empmst1 on " 
					 + " 	request.approved_by = empmst1.user_id " 
					 + " left join doctor.m_doctor_master docmst1 on " 
					 + " 	request.approved_by = docmst1.user_id " 
					 + " left join public.m_status s on " 
					 + " 	request.workorder_request_status_id = s.id " 
					 + " where " 
					 + " 	request.workorder_request_id =:workorderRequestId " 
					 + " 	and request.status = 'A' "),
	
	
	@NamedNativeQuery(name = "GET_WO_REQUEST_DETAILS_BY_REQUEST_ID", 
	query = "select  "
			+ "	requestDetails.workorder_request_details_id as \"workorderRequestDetialsId\", "
			+ "	requestDetails.workorder_request_id as \"workorderRequestId\", "
			+ "	requestDetails.store_id as \"storeId\", " + "requestDetails.item_id as \"itemId\", "
			+ "	requestDetails.item_name as \"itemName\", " + "requestDetails.item_quantity as \"itemQuantity\", "
			+ "	requestDetails.item_rate as \"itemRate\", " + "requestDetails.item_mrp as \"itemMrp\", "
			+ "	requestDetails.item_disc_percentage as \"itemDiscPercentage\", "
			+ "	requestDetails.item_disc_amount as \"itemDiscAmount\", "
			+ "	requestDetails.item_tax_id as \"itemTaxId\", " + "requestDetails.item_tax as \"itemTax\", "
			+ "	requestDetails.item_tax_amount as \"itemTaxAmount\", "
			+ "	requestDetails.item_net_amount as \"itemNetAmount\", "
			+ "	requestDetails.item_remark as \"itemRemark\", "
			+ "	msm.service_standard_name as \"serviceName\", "
			+ "	tod.order_details_id as \"orderDetailsId\" "
			+ "	from procurement.t_new_work_order_creation_details  requestDetails  "
			+ " 	left join t_order_details tod on tod.order_details_id = requestDetails.order_details_id "
			+ " 	left join service.m_service_master msm on tod.service_id = 	msm.service_master_id "
			+ "		where requestDetails.workorder_request_id =:workorderRequestId and requestDetails.status='A'  "
  
		),
	@NamedNativeQuery(name = "GET_WO_REQUEST_OTHERCHARGE_DETAILS_BY_REQUEST_ID", 
	query = "select " + 
			"	otherchargeDetails.workorder_request_id as \"workorderRequestId\", " + 
			"	otherchargeDetails.othercharge_type_id as \"otherchargeTypeId\", " + 
			"	otherchargeDetails.othercharge_amount as \"otherChargeAmount\" " + 
			"   from " + 
			"	procurement.t_new_work_order_creation_othercharge_details otherchargeDetails " + 
			"   where " + 
			"	otherchargeDetails.workorder_request_id =:workorderRequestId " + 
			"	and otherchargeDetails.status = 'A'"  
		),
	
	
	@NamedNativeQuery(name = "GET_WO_REQUEST_DOCUMENT_DETAILS_BY_REQUEST_ID", 
	query ="select " + 
			"	documentDetails.workorder_request_id as \"workorderRequestId\", " + 
			"	documentDetails.document_type_id as \"documentTypeId\", " + 
			"	doctypemaster.document_type_name \"documentTypeName\", " + 
			"	documentDetails.document_path as \"documentPath\" " + 
			"   from " + 
			"	procurement.t_new_work_order_document_details documentDetails " + 
			"   left join public.m_document_type_master doctypemaster on " + 
			"	documentDetails.document_type_id = doctypemaster.document_type_id " + 
			"   where " + 
			"	documentDetails.status = 'A' " + 
			"	and documentDetails.workorder_request_id =:workorderRequestId"  
		),
		
	
	
	
})



@Entity
@Table(name = "t_new_work_order_creation", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_creation_seq", sequenceName = "procurement.t_new_work_order_creation_seq", allocationSize = 1)

public class NewWorkOrderCreation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_creation_seq")
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
	 
	 @Column(name="round_off")
	 private Double roundoff;
	 
	 
	 @Column(name="workorder_request_status_id")
	 private Integer workorderRequestStatusId;
	 
	 @Column(name="workorder_approval_status_id")
	 private Integer workorderApprovalStatusId;
	
	 public Integer getWorkorderApprovalStatusId() {
		return workorderApprovalStatusId;
	}

	public void setWorkorderApprovalStatusId(Integer workorderApprovalStatusId) {
		this.workorderApprovalStatusId = workorderApprovalStatusId;
	}

	@Column(name="payment_terms")
	 private String paymentTerms;
	 
	 @Column(name="guarantee_warranty")
	 private String guaranteeWarranty;
	
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
	
	@Column(name="workorder_completion_status_id")
	private Integer workorderCompletionStatusId;

	@Column(name="is_reverse_tax")
	private boolean isReverseTax;
	
	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "bill_id")
	private Integer billId;
	
	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
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

	public Integer getWorkorderRequestStatusId() {
		return workorderRequestStatusId;
	}

	public void setWorkorderRequestStatusId(Integer workorderRequestStatusId) {
		this.workorderRequestStatusId = workorderRequestStatusId;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getGuaranteeWarranty() {
		return guaranteeWarranty;
	}

	public void setGuaranteeWarranty(String guaranteeWarranty) {
		this.guaranteeWarranty = guaranteeWarranty;
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

	public Double getRoundoff() {
		return roundoff;
	}

	public void setRoundoff(Double roundoff) {
		this.roundoff = roundoff;
	}

	public boolean getIsReverseTax() {
		return isReverseTax;
	}

	public void setIsReverseTax(boolean isReverseTax) {
		this.isReverseTax = isReverseTax;
	}

	public Integer getWorkorderCompletionStatusId() {
		return workorderCompletionStatusId;
	}

	public void setWorkorderCompletionStatusId(Integer workorderCompletionStatusId) {
		this.workorderCompletionStatusId = workorderCompletionStatusId;
	}


	
}
