package com.param.entity.model.pharmacy;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.param.billing.global.transaction.model.BillingMaster;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.DiscountCategory;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.Order;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.global.model.SpecialityMaster;

@NamedNativeQueries({ @NamedNativeQuery(name = "GET_SALE_DETAILS_BY_SALE_ID", query = "select "
		+ "	cast(sale_details.id as double precision) as \"id\", " + "	batch.item_id as \"itemId\" , "
		+ "	sum(cast(spdm.payee_amount as double precision)) as \"creditPayable\" " + "from "
		+ "	pharmacy.t_sale_detail_payee_mapper spdm " + "inner join pharmacy.t_sale_detail sale_details on "
		+ "	sale_details.id = spdm.t_sale_details_id " + "inner join inventory.t_batch batch on "
		+ "	batch.id = sale_details.batch_id " + "	and batch.is_deleted = false " + "	and batch.is_active = true "
		+ "inner join public.m_item item on " + "	item.id = batch.item_id " + "where "
		+ "	sale_details.sale_id = :saleId " + "	and spdm.payee_id =:payeeId " + "group by " + "	sale_details.id, "
		+ "	batch.item_id") })

@Entity(name = "Sale")
@Table(name = "t_sale", schema = "pharmacy")
public class Sale extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id")
	private Admission admission;

	@Column(name = "bill_no")
	private String billNo;

	@Column(name = "counter_id")
	private Integer counterId;

	private Double discount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "discount_category_id")
	private DiscountCategory discountCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "discount_type_id")
	private DiscountType discountType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_spl_id")
	private SpecialityMaster specialityMaster;

	@Column(name = "net_amount")
	private Double netAmount;
	@Column(name = "remark")
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "total_self_payable")
	private Double totalSelfPayable;

	@Column(name = "total_credit_payable")
	private Double totalCreditPayable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "outstanding_amount")
	private Double outstandingAmount;

	@Column(name = "paid_amount")
	private Double paidAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	private PatientRegistration patient;

	@Column(name = "round_off")
	private Double roundOff;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "total_discount_amount")
	private Double totalDiscountAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id")
	private VisitType visitType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_type_id")
	private SaleType saleType;

	@Column(name = "received_by")
	private String receivedBy;

	@Column(name = "received_on")
	private Timestamp receivedOn;

	@Column(name = "is_handover")
	private boolean isHandover;

	@Column(name = "is_consolidated_bill")
	private boolean isConsolidatedBill;

	@Column(name = "bill_id")
	private Integer billId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "handover_by")
	private User handoverBy;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "temp_doctor_id")
	private TempDoctor tempDoctor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "temp_patient_id")
	private TempPatientRegistration tempPatient;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
	private List<SaleDetail> saleDetailsList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
	private List<PharamcyBillPayeeDetails> billPayeeList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id")
	private Encounter encounter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carrier_id")
	private User carrier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prescription_id")
	private Prescription prescription;

	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name = "concession")
	private Double concession;

	@Transient
	private boolean otCompletion;

	@Column(name = "disallowed_against_invoice_dtl_id")
	private Integer disallowedAgainstInvoiceDtlId;

	// for HIS-10498
	@Column(name = "is_from_billable_consumption")
	private boolean isFromBillableConsumption;


	@Column(name = "is_from_crash_cart")
	private boolean isFromCrashCart;
	
	@Column(name = "is_from_pending_issue")
	private boolean isFromPendingIssue;
	
	@Column(name = "is_from_narcotics")
	private boolean isFromNarcotics;
	
	@Column(name = "is_from_patient_indent")
	private boolean isFromPatientIndent;
	
	@Column(name = "search_patient")
	private boolean searchPatient;

	@Column(name = "screen_id",updatable = false)
	private Integer screenId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
	List<SalePaymentMode> salePaymentModes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sale", cascade = CascadeType.ALL)
	List<Issue> issueList;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "bill_id",insertable = false, updatable = false)
	private BillingMaster billingList;
	
	@Column(name = "cop")
	private boolean cop;
	
	@Column(name = "received_from")
	private String receivedFrom;
	
	@Column(name = "is_virtual_store")
	private Boolean isVirtualStore;
	
	@Column(name = "cancelled_reason_id")
	private Integer cancelledReasonId;
	
	@Column(name = "cancelled_remark")
	private String cancelledRemark;
	
	@Column(name = "issue_id")
	private Integer issueId;
	
	@Column(name = "cancelled_by_id")
	private Integer cancelledById;
	
	@Transient
	private Integer entitlementId;
	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	public boolean getIsFromBillableConsumption() {
		return isFromBillableConsumption;
	}

	public void setFromBillableConsumption(boolean isFromBillableConsumption) {
		this.isFromBillableConsumption = isFromBillableConsumption;
	}

	public List<SalePaymentMode> getSalePaymentModes() {
		return salePaymentModes;
	}

	public void setSalePaymentModes(List<SalePaymentMode> salePaymentModes) {
		this.salePaymentModes = salePaymentModes;
	}

	public Sale() {

	}

	public Sale(Long id) {
		super();
		this.id = id;
	}


	public Sale(Admission admission, String billNo, Double outstandingAmount, Double paidAmount, Status status,
			Store store, Double taxAmount, Double totalAmount, Double netAmount,Integer screenId) {
		super();
		this.admission = admission;
		this.billNo = billNo;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.status = status;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.screenId = screenId;
	}

	public Sale(Admission admission, String billNo, Double outstandingAmount, Double paidAmount, Status status,
			Store store, Double taxAmount, Double totalAmount, Double netAmount, Doctor doctor, Double totalSelfPayable,
			Double totalCreditPayable,Integer screenId) {
		super();
		this.admission = admission;
		this.billNo = billNo;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.status = status;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = netAmount;
		this.doctor = doctor;
		this.totalSelfPayable = totalSelfPayable;
		this.totalCreditPayable = totalCreditPayable;
		this.screenId = screenId;
	}

	public Sale(Encounter encounter, Admission admission, String billNo, Integer counterId, Double discount,
			DiscountCategory discountCategory, DiscountType discountType, Doctor doctor, Double netAmount, Order order,
			Double outstandingAmount, Double paidAmount, PatientRegistration patient, Double roundOff, Status status,
			Store store, Double taxAmount, Double totalAmount, Double totalDiscountAmount, VisitType visitType,
			SaleType saleType, TempDoctor tempDoctor, TempPatientRegistration tempPatient, Prescription prescription,Integer screenId) {
		super();
		this.encounter = encounter;
		this.admission = admission;
		this.billNo = billNo;
		this.counterId = counterId;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.order = order;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.status = status;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.tempDoctor = tempDoctor;
		this.tempPatient = tempPatient;
		this.prescription = prescription;
		this.screenId  =screenId;
	}

	// Ganesh
	public Sale(Encounter encounter, Admission admission, String billNo, Integer counterId, Double discount,
			DiscountCategory discountCategory, DiscountType discountType, Doctor doctor, Double netAmount, Order order,
			Double outstandingAmount, Double paidAmount, PatientRegistration patient, Double roundOff, Status status,
			Store store, Double taxAmount, Double totalAmount, Double totalDiscountAmount, VisitType visitType,
			SaleType saleType, TempDoctor tempDoctor, TempPatientRegistration tempPatient, Prescription prescription,
			Double totalSelfPayable, Double totalCreditPayable,Integer screenId) {
		super();
		this.encounter = encounter;
		this.admission = admission;
		this.billNo = billNo;
		this.counterId = counterId;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.order = order;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.status = status;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.tempDoctor = tempDoctor;
		this.tempPatient = tempPatient;
		this.prescription = prescription;
		this.totalSelfPayable = totalSelfPayable;
		this.totalCreditPayable = totalCreditPayable;
		this.screenId  =screenId;
	}

	public Sale(Encounter encounter, Admission admission, String billNo, Integer counterId, Double discount,
			DiscountCategory discountCategory, DiscountType discountType, Doctor doctor, Double netAmount, Order order,
			Double outstandingAmount, Double paidAmount, PatientRegistration patient, Double roundOff, Status status,
			Store store, Double taxAmount, Double totalAmount, Double totalDiscountAmount, VisitType visitType,
			SaleType saleType, TempDoctor tempDoctor, TempPatientRegistration tempPatient, Prescription prescription,
			Double totalSelfPayable, Double totalCreditPayable, String remark) {
		super();
		this.encounter = encounter;
		this.admission = admission;
		this.billNo = billNo;
		this.counterId = counterId;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.order = order;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.status = status;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.tempDoctor = tempDoctor;
		this.tempPatient = tempPatient;
		this.prescription = prescription;
		this.totalSelfPayable = totalSelfPayable;
		this.totalCreditPayable = totalCreditPayable;
		this.remark = remark;
	}
	public Sale(Encounter encounter, Admission admission, String billNo, Integer counterId, Double discount,
			DiscountCategory discountCategory, DiscountType discountType, Doctor doctor, Double netAmount, Order order,
			Double outstandingAmount, Double paidAmount, PatientRegistration patient, Double roundOff, Status status,
			Store store, Double taxAmount, Double totalAmount, Double totalDiscountAmount, VisitType visitType,
			SaleType saleType, TempPatientRegistration tempPatient, Prescription prescription,
			Double totalSelfPayable, Double totalCreditPayable, String remark) {
		super();
		this.encounter = encounter;
		this.admission = admission;
		this.billNo = billNo;
		this.counterId = counterId;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.order = order;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.status = status;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.tempPatient = tempPatient;
		this.prescription = prescription;
		this.totalSelfPayable = totalSelfPayable;
		this.totalCreditPayable = totalCreditPayable;
		this.remark = remark;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public Sale(Long id, String receivedBy) {
		super();
		this.id = id;
		this.receivedBy = receivedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public DiscountCategory getDiscountCategory() {
		return discountCategory;
	}

	public void setDiscountCategory(DiscountCategory discountCategory) {
		this.discountCategory = discountCategory;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Double getNetAmount() {
		return netAmount == null ? 0 : netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Double getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public Double getPaidAmount() {
		return paidAmount == null ? 0 : paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public PatientRegistration getPatient() {
		return patient;
	}

	public void setPatient(PatientRegistration patient) {
		this.patient = patient;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public SaleType getSaleType() {
		return saleType;
	}

	public void setSaleType(SaleType saleType) {
		this.saleType = saleType;
	}

	public TempDoctor getTempDoctor() {
		return tempDoctor;
	}

	public void setTempDoctor(TempDoctor tempDoctor) {
		this.tempDoctor = tempDoctor;
	}

	public TempPatientRegistration getTempPatient() {
		return tempPatient;
	}

	public void setTempPatient(TempPatientRegistration tempPatient) {
		this.tempPatient = tempPatient;
	}

	public List<SaleDetail> getSaleDetailsList() {
		return saleDetailsList;
	}

	public void setSaleDetailsList(List<SaleDetail> saleDetailsList) {
		this.saleDetailsList = saleDetailsList;
	}

	public SaleDetail addSaleDetailsList(SaleDetail saleDetail) {
		if (getSaleDetailsList() == null)
			this.saleDetailsList = new ArrayList<SaleDetail>();

		getSaleDetailsList().add(saleDetail);
		saleDetail.setSale(this);

		return saleDetail;
	}

	public SaleDetail removeSaleDetailsList(SaleDetail saleDetail) {
		getSaleDetailsList().remove(saleDetail);
		saleDetail.setSale(null);

		return saleDetail;
	}

	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	public Timestamp getReceivedOn() {
		return receivedOn;
	}

	public void setReceivedOn(Timestamp receivedOn) {
		this.receivedOn = receivedOn;
	}

	public boolean isHandover() {
		return isHandover;
	}

	public void setHandover(boolean isHandover) {
		this.isHandover = isHandover;
	}

	public User getHandoverBy() {
		return handoverBy;
	}

	public void setHandoverBy(User handoverBy) {
		this.handoverBy = handoverBy;
	}

	public User getCarrier() {
		return carrier;
	}

	public void setCarrier(User carrier) {
		this.carrier = carrier;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public Sale(String billNo, Order order, Double discount, DiscountCategory discountCategory,
			DiscountType discountType, Doctor doctor, Double netAmount, Double outstandingAmount, Double paidAmount,
			PatientRegistration patient, Double roundOff, Store store, Double taxAmount, Double totalAmount,
			Double totalDiscountAmount, VisitType visitType, SaleType saleType, Prescription prescription,Integer screenId) {
		super();
		this.billNo = billNo;
		this.order = order;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.prescription = prescription;
		this.screenId = screenId;

	}

	public Sale(Order order, Double discount, DiscountCategory discountCategory, DiscountType discountType,
			Doctor doctor, Double netAmount, Double outstandingAmount, Double paidAmount, PatientRegistration patient,
			Double roundOff, Store store, Double taxAmount, Double totalAmount, Double totalDiscountAmount,
			VisitType visitType, SaleType saleType, Prescription prescription, Double totalSelfPayable,
			Double totalCreditPayable,Integer screenId) {
		super();
		this.order = order;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.prescription = prescription;
		this.totalSelfPayable = totalSelfPayable;
		this.totalCreditPayable = totalCreditPayable;
		this.screenId = screenId;
	}

	public Sale(Order order, Double discount, DiscountCategory discountCategory, DiscountType discountType,
			Doctor doctor, Double netAmount, Double outstandingAmount, Double paidAmount, PatientRegistration patient,
			Double roundOff, Store store, Double taxAmount, Double totalAmount, Double totalDiscountAmount,
			VisitType visitType, SaleType saleType, Prescription prescription, Double totalSelfPayable,
			Double totalCreditPayable, String remark) {
		super();
		this.order = order;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.prescription = prescription;
		this.totalSelfPayable = totalSelfPayable;
		this.totalCreditPayable = totalCreditPayable;
		this.remark = remark;
	}

	public Sale(Long id, Double discount, DiscountCategory discountCategory, DiscountType discountType, Doctor doctor,
			Double netAmount, Double outstandingAmount, Double paidAmount, PatientRegistration patient, Double roundOff,
			Store store, Double taxAmount, Double totalAmount, Double totalDiscountAmount, VisitType visitType,
			SaleType saleType, Prescription prescription, Double totalSelfPayable, Double totalCreditPayable,
			Status status) {
		super();
		this.id = id;
		this.discount = discount;
		this.discountCategory = discountCategory;
		this.discountType = discountType;
		this.doctor = doctor;
		this.netAmount = netAmount;
		this.outstandingAmount = outstandingAmount;
		this.paidAmount = paidAmount;
		this.patient = patient;
		this.roundOff = roundOff;
		this.store = store;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.visitType = visitType;
		this.saleType = saleType;
		this.prescription = prescription;
		this.totalSelfPayable = totalSelfPayable;
		this.totalCreditPayable = totalCreditPayable;
		this.status = status;
	}

	public Double getTotalSelfPayable() {
		return totalSelfPayable;
	}

	public void setTotalSelfPayable(Double totalSelfPayable) {
		this.totalSelfPayable = totalSelfPayable;
	}

	public Double getTotalCreditPayable() {
		return totalCreditPayable;
	}

	public void setTotalCreditPayable(Double totalCreditPayable) {
		this.totalCreditPayable = totalCreditPayable;
	}

	public boolean isConsolidatedBill() {
		return isConsolidatedBill;
	}

	public void setConsolidatedBill(boolean isConsolidatedBill) {
		this.isConsolidatedBill = isConsolidatedBill;
	}

	public List<Issue> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Issue> issueList) {
		this.issueList = issueList;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getDisallowedAgainstInvoiceDtlId() {
		return disallowedAgainstInvoiceDtlId;
	}

	public void setDisallowedAgainstInvoiceDtlId(Integer disallowedAgainstInvoiceDtlId) {
		this.disallowedAgainstInvoiceDtlId = disallowedAgainstInvoiceDtlId;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public List<PharamcyBillPayeeDetails> getBillPayeeList() {
		return billPayeeList;
	}

	public void setBillPayeeList(List<PharamcyBillPayeeDetails> billPayeeList) {
		this.billPayeeList = billPayeeList;
	}

	public boolean isOtCompletion() {
		return otCompletion;
	}

	public void setOtCompletion(boolean otCompletion) {
		this.otCompletion = otCompletion;
	}

	public boolean isFromCrashCart() {
		return isFromCrashCart;
	}

	public void setFromCrashCart(boolean isFromCrashCart) {
		this.isFromCrashCart = isFromCrashCart;
	}

	public boolean isFromPendingIssue() {
		return isFromPendingIssue;
	}

	public void setFromPendingIssue(boolean isFromPendingIssue) {
		this.isFromPendingIssue = isFromPendingIssue;
	}

	public boolean isFromNarcotics() {
		return isFromNarcotics;
	}

	public void setFromNarcotics(boolean isFromNarcotics) {
		this.isFromNarcotics = isFromNarcotics;
	}

	public boolean isFromPatientIndent() {
		return isFromPatientIndent;
	}

	public void setFromPatientIndent(boolean isFromPatientIndent) {
		this.isFromPatientIndent = isFromPatientIndent;
	}

	public boolean isSearchPatient() {
		return searchPatient;
	}

	public void setSearchPatient(boolean searchPatient) {
		this.searchPatient = searchPatient;
	}
	public boolean getCop() {
		return cop;
	}

	public void setCop(boolean cop) {
		this.cop = cop;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public Boolean getIsVirtualStore() {
		return isVirtualStore;
	}

	public void setIsVirtualStore(Boolean isVirtualStore) {
		this.isVirtualStore = isVirtualStore;
	}

	public Integer getCancelledReasonId() {
		return cancelledReasonId;
	}

	public void setCancelledReasonId(Integer cancelledReasonId) {
		this.cancelledReasonId = cancelledReasonId;
	}

	public String getCancelledRemark() {
		return cancelledRemark;
	}

	public void setCancelledRemark(String cancelledRemark) {
		this.cancelledRemark = cancelledRemark;
	}

	public Integer getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(Integer entitlementId) {
		this.entitlementId = entitlementId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getCancelledById() {
		return cancelledById;
	}

	public void setCancelledById(Integer cancelledById) {
		this.cancelledById = cancelledById;
	}
}