package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.global.model.SpecialityMaster;

@Entity(name = "CrashCartRequest")
@Table(name = "t_crash_cart_request", schema = "pharmacy")
public class CrashCartRequest extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id")
	private Admission admission;

	@Column(name = "code_blue_date_time")
	private Date codeBlueDateTime;

	@Column(name = "crash_cart_no")
	private String crashCartNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_spl_id")
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id")
	private Encounter encounter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	private PatientRegistration patient;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id")
	private VisitType visitType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id")
	private WardMasters ward;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_id")
	private BedMaster bed;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crashCartRequest", cascade = CascadeType.ALL)
	private List<CrashCartRequestDetail> crashCartRequestDetails;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sale_id")
	private Sale sale;
	
	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public CrashCartRequest() {
		super();
	}

	public CrashCartRequest(Integer id) {
		super();
		this.id = id;
	}

	public CrashCartRequest(String crashCartNo, Date codeBlueDateTime, Doctor doctor, PatientRegistration patient,
			Store store, VisitType visitType, String remark, Status status) {
		super();
		this.crashCartNo = crashCartNo;
		this.codeBlueDateTime = codeBlueDateTime;
		this.doctor = doctor;
		this.patient = patient;
		this.store = store;
		this.visitType = visitType;
		this.remark = remark;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public Date getCodeBlueDateTime() {
		return codeBlueDateTime;
	}

	public void setCodeBlueDateTime(Date codeBlueDateTime) {
		this.codeBlueDateTime = codeBlueDateTime;
	}

	public String getCrashCartNo() {
		return crashCartNo;
	}

	public void setCrashCartNo(String crashCartNo) {
		this.crashCartNo = crashCartNo;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

	public PatientRegistration getPatient() {
		return patient;
	}

	public void setPatient(PatientRegistration patient) {
		this.patient = patient;
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

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public WardMasters getWard() {
		return ward;
	}

	public void setWard(WardMasters ward) {
		this.ward = ward;
	}

	public BedMaster getBed() {
		return bed;
	}

	public void setBed(BedMaster bed) {
		this.bed = bed;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<CrashCartRequestDetail> getCrashCartRequestDetails() {
		return this.crashCartRequestDetails;
	}

	public void setCrashCartRequestDetails(List<CrashCartRequestDetail> crashCartRequestDetails) {
		this.crashCartRequestDetails = crashCartRequestDetails;
	}

	public CrashCartRequestDetail addCrashCartRequestDetail(CrashCartRequestDetail crashCartRequestDetail) {
		if (getCrashCartRequestDetails() == null)
			this.crashCartRequestDetails = new ArrayList<CrashCartRequestDetail>();

		getCrashCartRequestDetails().add(crashCartRequestDetail);
		crashCartRequestDetail.setCrashCartRequest(this);

		return crashCartRequestDetail;
	}

	public CrashCartRequestDetail removeCrashCartRequestDetail(CrashCartRequestDetail crashCartRequestDetail) {
		getCrashCartRequestDetails().remove(crashCartRequestDetail);
		crashCartRequestDetail.setCrashCartRequest(null);

		return crashCartRequestDetail;
	}

}