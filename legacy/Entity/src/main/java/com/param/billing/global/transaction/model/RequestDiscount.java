package com.param.billing.global.transaction.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.Admission;
import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.PatientRegistration;
import com.param.opd.coversheet.model.EncounterMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_REQUEST_DETAILS",query="SELECT reqDisDtls.reqDiscDetailsId as reqDiscDetailsId, "
			+ "reqDisDtls.serviceId as serviceId, "
			+ "reqDisDtls.orderDetailsId as orderDetailsId, "
			+ "reqDisDtls.orderId as orderId, "
			+ "ser.serviceStandardName as serviceName, "
			+ "reqDisDtls.netAmount as netAmount, "
			+ "reqDisDtls.reqDisAmt as reqDisAmt, "
			+ "reqDisDtls.approvedDiscAmt as approvedDiscAmt "
			+ "FROM RequestDiscountDetails reqDisDtls "
			+ "INNER JOIN reqDisDtls.serviceMaster ser "
			+ "WHERE reqDisDtls.reqDiscId=:reqDiscId "),
	
	
		})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_DISC_REQ_DTLS_FOR_OP_BILLING",query="select "
			 + " 	requestdis.req_disc_id as \"reqDiscId\", "
			 + " 	requestdis.req_dis_amt as \"reqDisAmt\", "
			 + " 	requestdis.net_amount as \"netAmount\", "
			 + " 	requestdis.remark as \"remark\", "
			 + " 	requestdis.approved_disc_amt as \"approvedDiscAmt\", "
			 + " 	requestdis.req_status as \"reqStatus\", "
			 + " 	requestdis.request_sent_to as \"requestSentTo\", "
			 + " 	requestdis.disc_type as \"discType\", "
             + "     dt.category as \"discTypeDesc\", "
			 + " 	(case when employeema.first_name is not null then "
			 + " 	(employeema.first_name || ' ' || employeema.middle_name || ' ' || employeema.last_name)  "
			 + " 	else "
			 + " 	(concat('Dr. ',coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name))) end ) "
			 + " 	as \"requestSentToEmployee\" ,"
			+ " 	requestdis.payee_id as \"payeeId\", "
			+"       tbpd.bill_payee_details_id  as \"billPayeeDetailsId\""
			 + " from "
			 + " 	billing.t_request_discount requestdis "
			 + " inner join public.m_user_master usermaster on "
			 + " 	requestdis.request_sent_to = usermaster.user_id "
			 + " left join public.m_employee_master employeema on "
			 + " 	usermaster.user_id = employeema.user_id "
			 + " left join doctor.m_doctor_master doctor on "
			 + " 	usermaster.user_id = doctor.user_id	 "
			// + " left join public.m_discount_type dt on "
            + " left join public.m_discount_category dt on "
			 + " 	dt.id = requestdis.disc_type	 "
			+"  left join billing.t_bill_payee_details tbpd on tbpd.bill_id = requestdis.bill_id and tbpd.payee_id = requestdis.payee_id and tbpd.status ='A'"
			 + " where requestdis.request_type in (1,6,4) "),
	
	@NamedNativeQuery(name="GET_DISC_REQ_DTLS_FOR_BAD_DEBIT",query="select "
			 + " 	requestdis.req_disc_id as \"reqDiscId\", "
			 + " 	requestdis.req_dis_amt as \"reqDisAmt\", "
			 + " 	requestdis.net_amount as \"netAmount\", "
			 + " 	requestdis.remark as \"remark\", "
			 + " 	requestdis.approved_disc_amt as \"approvedDiscAmt\", "
			 + " 	requestdis.req_status as \"reqStatus\", "
			 + " 	requestdis.request_sent_to as \"requestSentTo\", "
			 + " 	requestdis.disc_type as \"discType\", "
			 + " 	(case when employeema.first_name is not null then "
			 + " 	(employeema.first_name || ' ' || employeema.middle_name || ' ' || employeema.last_name)  "
			 + " 	else "
			 + " 	(concat('Dr. ',coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name))) end ) "
			 + " 	as \"requestSentToEmployee\" "
			 + " from "
			 + " 	billing.t_request_discount requestdis "
			 + " inner join public.m_user_master usermaster on "
			 + " 	requestdis.request_sent_to = usermaster.user_id "
			 + " left join public.m_employee_master employeema on "
			 + " 	usermaster.user_id = employeema.user_id "
			 + " left join doctor.m_doctor_master doctor on "
			 + " 	usermaster.user_id = doctor.user_id	 "),
	
	
	
	@NamedNativeQuery(name="GETR_EQUESTED_DISCOUNT",
	query=   " select " 
			 + " 	requestdis0_.req_disc_id as \"reqDiscId\", " 
			 + " 	requestdis0_.bill_id as \"billId\", " 
			 + " 	to_char(requestdis0_.request_date, 'dd/MM/yyyy') as \"requestDate\", " 
			 + " 	requestdis0_.visit_type_id as \"visitTypeId\", " 
			 + " 	requestdis0_.encounter_id as \"encounterId\", " 
			 + " 	encounterm1_.encounter_number as \"encounterNumber\", " 
			 + " 	requestdis0_.admission_id as \"admissionId\", " 
			 + " 	admission2_.admission_number as \"admissionNumber\", " 
			 + " 	creditnote11_.credit_note_remark as \"creditNoteRemark\", " 
			 + " 	refund.remark as \"refundRemark\", "
			 + " 	expense.remark as \"expenseRemark\","
			 + " 	cre.remark as \"companyReciptRemark\","
			 + " 	reasonmaster12_.reason_desc as \"creditReason\", " 
			 + " 	requestdis0_.patient_id as \"patientId\", " 
			 + " 	patientreg3_.uhid_number as \"uhidNumber\", " 
			 + " 	ms.status as \"status\", " 
			 + " 	(fromprefixmast4_.prefix_code || ' ' || coalesce(patientregfrom.first_name)|| ' ' || coalesce(patientregfrom.middle_name)|| ' ' || coalesce(patientregfrom.last_name)) as \"frompatientName\" , " 
			 + " 	(toprefixmast4_.prefix_code || ' ' || coalesce(patientregto_.first_name)|| ' ' || coalesce(patientregto_.middle_name)|| ' ' || coalesce(patientregto_.last_name)) as \"topatientName\", " 
			 + " 	(prefixmast4_.prefix_code || ' ' || coalesce(patientreg3_.first_name)|| ' ' || coalesce(patientreg3_.middle_name)|| ' ' || coalesce(patientreg3_.last_name)) as \"patientName\", " 
			 + " 	requestdis0_.net_amount as \"netAmount\", " 
			 + " 	requestdis0_.req_dis_amt as \"reqDisAmt\", " 
			 + " 	requestdis0_.approved_disc_amt as \"approvedDiscAmt\", " 
			 + " 	requestdis0_.request_sent_by as \"requestSentBy\", " 
			 + " 	requestdis0_.request_sent_to as \"requestSentTo\", " 
			 + " 	(case " 
			 + " 		when employeema6_.employee_id is not null then (coalesce(employeema6_.first_name)|| ' ' || coalesce(employeema6_.middle_name)|| ' ' || coalesce(employeema6_.last_name)) " 
			 + " 		else ('Dr. ' || coalesce(doctormast7_.first_name)|| ' ' || coalesce(doctormast7_.middle_name)|| ' ' || coalesce(doctormast7_.last_name)) " 
			 + " 	end) as \"requestSentByEmployee\", " 
			 + " 	requestdis0_.req_status as \"reqStatus\", " 
			 + " 	requestdis0_.disc_type as \"discType\", " 
			 + " 	requestdis0_.request_number as \"requestNumber\", " 
			 + " 	requestdis0_.request_type as \"requestType\", "
			 + " 	requestdis0_.remark as \"remark\", "
			 + " 	requestdis0_.credit_note_id as \"creditNoteId\", " 
			 + " 	creditnote11_.credit_note_no as \"creditNoteNo\", " 
			 + " 	deposit.deposit_no as \"depositNo\", " 
			 + " 	deposit.deposit_master_id as \"depositId\", " 
			 + " 	refund.refund_amount as \"refundAmount\", " 
			 + " 	refund.t_refund_cn_id as \"tRefundCnId\", " 
			 + " 	requestdis0_.from_patient_id as \"fromPatientId\", " 
			 + " 	requestdis0_.to_patient_id as \"toPatientId\", " 
			 + " 	creditnote11_.cr_net_amount as \"crNetAmount\", " 
			 + " 	expense.bill_number as \"expenseBillNumber\", " 
			 + " 	cast(expense.amount as float8) as \"expenseAmount\", " 
			 + " 	requestdis0_.expense_id as \"expenseId\", " 
			 + " 	cre.bill_number as \"companyReceiptsExpenseBillNumber\", " 
			 + " 	cast(cre.amount as float8) as \"companyReceiptsExpenseAmount\", " 
			 + " 	requestdis0_.company_receipts_expense_id as \"companyReceiptsExpenseId\", "
			 + "    bm.net_amount as \"netBillAmount\"," 
			 + " 	(case " 
			 + " 		when bm.billing_master_id is not null then bm.bill_status_id " 
			 + " 	end ) as \"billStatusId\", " 
			 + " 	(case " 
			 + " 		when bm.billing_master_id is not null then bm.bill_number " 
			 + " 		else sm.bill_no " 
			 + " 	end ) as \"billNumber\" " 
			 + " from " 
			 + " 	billing.t_request_discount requestdis0_ " 
			 + " left outer join opd.t_encounter_master encounterm1_ on " 
			 + " 	requestdis0_.encounter_id = encounterm1_.encounter_id " 
			 + " left outer join adt.t_admission admission2_ on " 
			 + " 	requestdis0_.admission_id = admission2_.admission_id " 
			 + " left join patient.t_patient_registration patientreg3_ on " 
			 + " 	requestdis0_.patient_id = patientreg3_.patient_id " 
			 + " left join patient.t_patient_registration patientregfrom on " 
			 + " 	requestdis0_.from_patient_id = patientregfrom.patient_id " 
			 + " left join patient.t_patient_registration patientregto_ on " 
			 + " 	requestdis0_.to_patient_id = patientregto_.patient_id " 
			 + " left join public.m_prefix_master fromprefixmast4_ on " 
			 + " 	patientregfrom.prefix_id = fromprefixmast4_.prefix_id " 
			 + " left join public.m_prefix_master toprefixmast4_ on " 
			 + " 	patientregto_.prefix_id = toprefixmast4_.prefix_id " 
			 + " left join public.m_prefix_master prefixmast4_ on " 
			 + " 	patientreg3_.prefix_id = prefixmast4_.prefix_id " 
			 + " inner join public.m_user_master usermaster5_ on " 
			 + " 	requestdis0_.request_sent_by = usermaster5_.user_id " 
			 + " left outer join public.m_employee_master employeema6_ on " 
			 + " 	usermaster5_.user_id = employeema6_.user_id " 
			 + " left outer join doctor.m_doctor_master doctormast7_ on " 
			 + " 	usermaster5_.user_id = doctormast7_.user_id " 
			 + " inner join public.m_user_master usermaster8_ on " 
			 + " 	requestdis0_.request_sent_by = usermaster8_.user_id " 
			 + " left outer join public.m_employee_master employeema9_ on " 
			 + " 	usermaster8_.user_id = employeema9_.user_id " 
			 + " left outer join doctor.m_doctor_master doctormast10_ on " 
			 + " 	usermaster8_.user_id = doctormast10_.user_id " 
			 + " left outer join billing.m_credit_note_master creditnote11_ on " 
			 + " 	requestdis0_.credit_note_id = creditnote11_.credit_note_id " 
			 + " 	and creditnote11_.status = 'A' " 
			 + " left outer join billing.t_refund_cn refund on " 
			 + " 	requestdis0_.t_refund_cn_id = refund.t_refund_cn_id " 
			 + " left outer join billing.m_deposit_master deposit on " 
			 + " 	refund.deposit_id = deposit.deposit_master_id " 
			 + " left outer join adt.m_reason_master reasonmaster12_ on " 
			 + " 	reasonmaster12_.reason_master_id = creditnote11_.cr_reason_id " 
			 + " left join public.m_status ms on " 
			 + " 	requestdis0_.req_Status = ms.id " 
			 + " left outer join billing.t_miscellaneous_expense expense on " 
			 + " 	expense.expense_id = requestdis0_.expense_id " 
			 + " left outer join billing.m_billing_master bm on " 
			 + " 	requestdis0_.bill_id = bm.billing_master_id " 
			 + " 	and bm.bill_cancelled = 'N' " 
			 + " left outer join pharmacy.t_sale sm on " 
			 + " 	requestdis0_.sale_id = sm.id "
			 + "	and sm.is_active = true " 
			 + " left outer join billing.t_company_receipts_expense cre on " 
			 + " 	cre.company_receipts_expense_id = requestdis0_.company_receipts_expense_id " 
			 + " where " 
			 + " 	requestdis0_.organization_id =:orgId " 
			 + " 	and requestdis0_.unit_id =:unitId "  ),
	

	@NamedNativeQuery(name="GET_UHID_MERGE_REQUEST_APPROVAL",
	query="    select "
	        + " requestdis0_.req_disc_id as \"reqDiscId\" , "   
	        + " requestdis0_.from_patient_id as \"fromPatientId\", "
	        +  " requestdis0_.to_patient_id as \"toPatientId\", "
	        +  " requestdis0_.request_sent_by as \"requestSentBy\" , "
	        +  " requestdis0_.request_sent_to as \"requestSentTo\", " 
	        +  " requestdis0_.req_status as \"reqStatus\", "
	        +  " requestdis0_.request_type as \"requestType\" "
            +  " from "
	        +  " billing.t_request_discount requestdis0_ "
            +  " left join patient.t_patient_registration patientreg3_ on "
	        +   " requestdis0_.patient_id = patientreg3_.patient_id "
            +   " left join public.m_prefix_master prefixmast4_ on "
	        +   " patientreg3_.prefix_id = prefixmast4_.prefix_id "
            +   " where "
	        +  " requestdis0_.organization_id =:organizationId "
	        +  " and requestdis0_.unit_id =:unitId "
	        )
		
})




@Entity
@Table(name="t_request_discount",schema="billing")
@SequenceGenerator(name = "request_discount_seq", sequenceName = "billing.request_discount_seq", allocationSize = 1)
public class RequestDiscount {

	@Id
	@Column(name="req_disc_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="request_discount_seq")
	private Integer reqDiscId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="request_date")
	private Date requestDate;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="net_amount")
	private BigDecimal netAmount;
	
	@Column(name="req_dis_amt")
	private BigDecimal reqDisAmt;
	
	@Column(name="approved_disc_amt")
	private BigDecimal approvedDiscAmt;
	
	@Column(name="request_sent_by")
	private Integer requestSentBy;
	
	@Column(name="request_sent_to")
	private Integer requestSentTo;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="req_status")
	private Integer reqStatus;
	
	@Column(name="disc_type")
	private Integer discType;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="request_number")
	private String requestNumber;
	
	@Column(name="bill_id")
	private Integer billId;
	
	@Column(name="credit_note_id")
	private Integer creditNoteId;
	
	@Column(name="request_type")
	private Integer requestType;
	
	@Column(name="t_refund_cn_id")
	private Integer tRefundCnId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="sale_id")
	private Integer tSaleId;
	
	@Column(name="expense_id")
	private Integer expenseId;
	
	@Column(name="company_receipts_expense_id")
	private Integer companyReceiptsExpenseId;
	
	@Column(name="username")
	private String username;

	@Column(name="from_patient_id")
	private Integer fromPatientId;
	

	@Column(name="to_patient_id")
	private Integer toPatientId;
	
	@Column(name="request_from")
	private Integer requestFrom;
	
	@Column(name="request_to")
	private Integer requestTo;
	
	@Column(name="remark")
	private String remark;

	public Integer getRequestTo() {
		return requestTo;
	}

	public void setRequestTo(Integer requestTo) {
		this.requestTo = requestTo;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public Integer getFromPatientId() {
		return fromPatientId;
	}

	public void setFromPatientId(Integer fromPatientId) {
		this.fromPatientId = fromPatientId;
	}

	public Integer getToPatientId() {
		return toPatientId;
	}

	public void setToPatientId(Integer toPatientId) {
		this.toPatientId = toPatientId;
	}

	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounterMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private Admission admission;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_sent_by", insertable = false, nullable = false, updatable = false)
	private UserMaster requestSentByUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_sent_to", insertable = false, nullable = false, updatable = false)
	private UserMaster requestSentToUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "credit_note_id", insertable = false, nullable = false, updatable = false)
	private CreditNoteMaster creditNoteMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_refund_cn_id", insertable = false, nullable = false, updatable = false)
	private RefundCNMaster refundCNMaster;
	
	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getReqDiscId() {
		return reqDiscId;
	}

	public void setReqDiscId(Integer reqDiscId) {
		this.reqDiscId = reqDiscId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public BigDecimal getApprovedDiscAmt() {
		return approvedDiscAmt;
	}

	public Integer getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(Integer requestFrom) {
		this.requestFrom = requestFrom;
	}

	public void setApprovedDiscAmt(BigDecimal approvedDiscAmt) {
		this.approvedDiscAmt = approvedDiscAmt;
	}

	public Integer getRequestSentBy() {
		return requestSentBy;
	}

	public void setRequestSentBy(Integer requestSentBy) {
		this.requestSentBy = requestSentBy;
	}

	public Integer getRequestSentTo() {
		return requestSentTo;
	}

	public void setRequestSentTo(Integer requestSentTo) {
		this.requestSentTo = requestSentTo;
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

	public Integer getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(Integer reqStatus) {
		this.reqStatus = reqStatus;
	}

	public Integer getDiscType() {
		return discType;
	}

	public void setDiscType(Integer discType) {
		this.discType = discType;
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

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public CreditNoteMaster getCreditNoteMaster() {
		return creditNoteMaster;
	}

	public void setCreditNoteMaster(CreditNoteMaster creditNoteMaster) {
		this.creditNoteMaster = creditNoteMaster;
	}

	public Integer gettRefundCnId() {
		return tRefundCnId;
	}

	public void settRefundCnId(Integer tRefundCnId) {
		this.tRefundCnId = tRefundCnId;
	}

	public RefundCNMaster getRefundCNMaster() {
		return refundCNMaster;
	}

	public void setRefundCNMaster(RefundCNMaster refundCNMaster) {
		this.refundCNMaster = refundCNMaster;
	}

	public Integer gettSaleId() {
		return tSaleId;
	}

	public void settSaleId(Integer tSaleId) {
		this.tSaleId = tSaleId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	
	
}
