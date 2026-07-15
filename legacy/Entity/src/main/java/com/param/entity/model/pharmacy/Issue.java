package com.param.entity.model.pharmacy;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.adt.Admission;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.procurement.ImplantIndent;
import com.param.global.model.SpecialityMaster;

@Entity(name = "Issue")
@Table(name = "t_issue", schema = "pharmacy")
public class Issue extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "issue_no")
	private String issueNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_id")
	private Sale sale;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prescription_id")
	private Prescription prescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", nullable = false)
	private VisitType visitType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id")
	private Admission admission;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id")
	private Encounter encounter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_spl_id")
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id")
	private Store toStore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_indent_id")
	private PatientIndent patientIndent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "implant_indent_id")
	private ImplantIndent implantIndent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carrier_id")
	private User carrier;

	@Column(name = "remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assign_picker_id")
	private AssignPicker assignPicker;

	@Column(name = "is_consignment")
	private Boolean isConsignment;
	
	@Column(name = "is_implant")
	private Boolean isImplant;

	@Column(name = "ongoing")
	private Boolean onGoing = false;

	@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<IssueDetail> issueDetail;
	
	private Double discount;

	@Column(name = "net_amount")
	private Double netAmount;
		
	@Column(name = "total_self_payable")
	private Double totalSelfPayable;
		
	@Column(name = "total_credit_payable")
	private Double totalCreditPayable;
		
	@Column(name = "outstanding_amount")
	private Double outstandingAmount;
		
	@Column(name = "paid_amount")
	private Double paidAmount;
		
	@Column(name = "round_off")
	private Double roundOff;
		
	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "total_discount_amount")
	private Double totalDiscountAmount;

	@Column(name = "is_narcotics")
	private Boolean isNarcotics;
	@Column(name = "is_return")
	private Boolean isReturn = false;
	
	@Column(name = "infusion_tolal_volume")
	private Integer infusionTolalVolume;

	public Boolean getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getNetAmount() {
		return netAmount;
	}



	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
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



	public Double getOutstandingAmount() {
		return outstandingAmount;
	}



	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}



	public Double getPaidAmount() {
		return paidAmount;
	}



	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}



	public Double getRoundOff() {
		return roundOff;
	}



	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
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



	public Issue() 
	{
		super();
	}



	public Issue(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

	public Doctor getDoctor() {
		// Return null if the doctor object is not null and its ID is 0
		if (doctor != null && doctor.getDoctorId() != null && doctor.getDoctorId() == 0) {
			return null;
		}
		return doctor; // Otherwise, return the actual doctor object
	}

	public void setDoctor(Doctor doctor) {
		// Check if the incoming doctor object is not null and its ID is 0
		if (doctor != null && doctor.getDoctorId() == null || doctor.getDoctorId() == 0) {
			this.doctor = null; // Set doctor to null if doctorId is 0
		} else {
			this.doctor = doctor; // Otherwise, set the doctor as it is
		}
	}



	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Store getToStore() {
		return toStore;
	}

	public void setToStore(Store toStore) {
		this.toStore = toStore;
	}

	public PatientIndent getPatientIndent() {
		return patientIndent;
	}

	public void setPatientIndent(PatientIndent patientIndent) {
		this.patientIndent = patientIndent;
	}

	public User getCarrier() {
		return carrier;
	}

	public void setCarrier(User carrier) {
		this.carrier = carrier;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public AssignPicker getAssignPicker() {
		return assignPicker;
	}

	public void setAssignPicker(AssignPicker assignPicker) {
		this.assignPicker = assignPicker;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}
	
	public List<IssueDetail> getIssueDetail() {
		return issueDetail;
	}

	public void setIssueDetail(List<IssueDetail> issueDetail) {
		this.issueDetail = issueDetail;
	}

	public ImplantIndent getImplantIndent() {
		return implantIndent;
	}

	public void setImplantIndent(ImplantIndent implantIndent) {
		this.implantIndent = implantIndent;
	}

	public Boolean getOnGoing() {
		return onGoing;
	}

	public void setOnGoing(Boolean onGoing) {
		this.onGoing = onGoing;
	}

	public IssueDetail addIssueDetail(IssueDetail issueDetail) {
		if (getIssueDetail() == null)
			this.issueDetail = new ArrayList<IssueDetail>();

		getIssueDetail().add(issueDetail);
		issueDetail.setIssue(this);

		return issueDetail;
	}

	public Issue( Sale sale,String issueNo, VisitType visitType, Admission admission, Doctor doctor, Store store,
				  Store toStore,Status status,Boolean isImplant) {
		super();
		this.sale = sale;
		this.issueNo = issueNo;
		this.visitType = visitType;
		this.admission = admission;
		this.doctor = doctor;
		this.store = store;
		this.toStore = toStore;
		this.status = status;
		this.isImplant=isImplant;
	}

	public Issue(Integer id, String issueNo, Sale sale, Prescription prescription, VisitType visitType,
			Admission admission, Encounter encounter, Doctor doctor, Store store, Store toStore) {
		super();
		this.id = id;
		this.issueNo = issueNo;
		this.sale = sale;
		this.prescription = prescription;
		this.visitType = visitType;
		this.admission = admission;
		this.encounter = encounter;
		this.doctor = doctor;
		this.store = store;
		this.toStore = toStore;
	}

	public Issue(String issueNo, VisitType visitType, Doctor doctor, Store store,Store toStore, PatientIndent patientIndent,
			User carrier, String remark, Status status) {
		super();
		this.issueNo = issueNo;
		this.visitType = visitType;
		this.doctor = doctor;
		this.store = store;
		this.toStore = toStore;
		this.patientIndent = patientIndent;
		this.carrier = carrier;
		this.remark = remark;
		this.status = status;
	}

	public Issue(String issueNo, Prescription prescription, VisitType visitType, Doctor doctor, Store store,
			Status status, AssignPicker assignPicker) {
		super();
		this.issueNo = issueNo;
		/* this.sale = sale; */
		this.prescription = prescription;
		this.visitType = visitType;
		this.doctor = doctor;
		this.store = store;
		/* this.remark = remark; */
		this.status = status;
		this.assignPicker = assignPicker;
	}

	public Issue(String issueNo, Prescription prescription, VisitType visitType, Doctor doctor, Status status) {
		super();
		this.issueNo = issueNo;
		this.prescription = prescription;
		this.visitType = visitType;
		this.doctor = doctor;
		this.status = status;
	}

	public Issue(String issueNo, Sale sale, Prescription prescription, VisitType visitType, Doctor doctor, Store store,
				 Store toStore, String remark, Status status) {
		super();
		this.issueNo = issueNo;
		this.sale = sale;
		this.prescription = prescription;
		this.visitType = visitType;
		this.doctor = doctor;
		this.store = store;
		this.toStore = toStore;
		this.remark = remark;
		this.status = status;
	}

	public Issue(String issueNo, VisitType visitType, Doctor doctor, Store store, Store toStore, PatientIndent patientIndent,
			Status status) {
		super();
		this.issueNo = issueNo;
		this.visitType = visitType;
		this.doctor = doctor;
		this.store = store;
		this.toStore = toStore;
		this.patientIndent = patientIndent;
		this.status = status;
	}
	
	public Issue(String issueNo, VisitType visitType, Store store, Store toStore, PatientIndent patientIndent,
			Status status) {
		super();
		this.issueNo = issueNo;
		this.visitType = visitType;
		//this.doctor = doctor;
		this.store = store;
		this.toStore = toStore;
		this.patientIndent = patientIndent;
		this.status = status;
	}
	public Issue(String issueNo, VisitType visitType, Doctor doctor, Store store,ImplantIndent implantIndent,
			Status status, Boolean isConsignment,Boolean isImplant) {
		super();
		this.issueNo = issueNo;
		this.visitType = visitType;
		this.doctor = doctor;
		this.store = store;
		this.implantIndent = implantIndent;
		this.status = status;
		this.isConsignment = isConsignment;
		this.isImplant = isImplant;
	}
	public Issue(String issueNo, Sale sale2, VisitType visitType2, Admission admission2, Doctor doctor2, Store store2,
				 Store toStore,Status status2) {
		super();
		this.issueNo = issueNo;
		this.sale = sale2;
		this.visitType = visitType2;
		this.admission = admission2;
		this.doctor = doctor2;
		this.store = store2;
		this.toStore = toStore;
		this.status = status2;
	}
	

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsNarcotics() {
		return isNarcotics;
	}

	public void setIsNarcotics(Boolean isNarcotics) {
		this.isNarcotics = isNarcotics;
	}

	public Integer getInfusionTolalVolume() {
		return infusionTolalVolume;
	}

	public void setInfusionTolalVolume(Integer infusionTolalVolume) {
		this.infusionTolalVolume = infusionTolalVolume;
	}

}