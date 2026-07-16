package com.param.entity.model.procurement;

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
import com.param.entity.model.adt.Priority;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;

@Entity(name = "ImplantIndent")
@Table(name = "t_implant_indent", schema = "theatre_management")
public class ImplantIndent extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id")
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "implant_indent_no")
	private String implantIndentNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_type_id")
	private IndentType indentType;

	@Column(name = "is_cancellable")
	private Boolean isCancellable;

	@Column(name = "is_consignment")
	private Boolean isConsignment;

	@Column(name = "is_implant")
	private Boolean isImplant;

	@Column(name = "is_kit")
	private Boolean isKit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	private PatientRegistration patient;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id")
	private Admission admission;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id")
	private Encounter encounter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id")
	private Priority priority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id")
	private VisitType visitType;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id")
	private Store fromStore;

	// bi-directional many-to-one association to ImplantIndentDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "implantIndent", cascade = CascadeType.ALL)
	private List<ImplantIndentDetail> implantIndentDetails;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "implantIndent", cascade = CascadeType.ALL)
	private List<Issue> issue;

	public ImplantIndent() {
		super();
	}

	public ImplantIndent(Integer id) {
		this.id = id;
	}

	public ImplantIndent(Status approvalStatus, String implantIndentNo, IndentType indentType, Boolean isCancellable,
			Boolean isConsignment, Boolean isImplant, Boolean isKit, PatientRegistration patient, Doctor doctor,
			Admission admission, Priority priority, VisitType visitType, String remark, Status status, Store store, Store fromStore) {
		super();
		this.approvalStatus = approvalStatus;
		this.implantIndentNo = implantIndentNo;
		this.indentType = indentType;
		this.isCancellable = isCancellable;
		this.isConsignment = isConsignment;
		this.isImplant = isImplant;
		this.isKit = isKit;
		this.patient = patient;
		this.doctor = doctor;
		this.admission = admission;
		this.priority = priority;
		this.visitType = visitType;
		this.remark = remark;
		this.status = status;
		this.store = store;
		this.fromStore = fromStore;
	}

	public ImplantIndent(Status approvalStatus, String implantIndentNo, IndentType indentType, Boolean isCancellable,
			Boolean isConsignment, Boolean isImplant, Boolean isKit, PatientRegistration patient, Doctor doctor,
			Encounter encounter, Priority priority, VisitType visitType, String remark, Status status, Store store, Store fromStore) {
		super();
		this.approvalStatus = approvalStatus;
		this.implantIndentNo = implantIndentNo;
		this.indentType = indentType;
		this.isCancellable = isCancellable;
		this.isConsignment = isConsignment;
		this.isImplant = isImplant;
		this.isKit = isKit;
		this.patient = patient;
		this.doctor = doctor;
		this.encounter = encounter;
		this.priority = priority;
		this.visitType = visitType;
		this.remark = remark;
		this.status = status;
		this.store = store;
		this.fromStore = fromStore;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getImplantIndentNo() {
		return implantIndentNo;
	}

	public void setImplantIndentNo(String implantIndentNo) {
		this.implantIndentNo = implantIndentNo;
	}

	public IndentType getIndentType() {
		return indentType;
	}

	public void setIndentType(IndentType indentType) {
		this.indentType = indentType;
	}

	public Boolean getIsCancellable() {
		return isCancellable;
	}

	public void setIsCancellable(Boolean isCancellable) {
		this.isCancellable = isCancellable;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsKit() {
		return isKit;
	}

	public void setIsKit(Boolean isKit) {
		this.isKit = isKit;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
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

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public PatientRegistration getPatient() {
		return patient;
	}

	public void setPatient(PatientRegistration patient) {
		this.patient = patient;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
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

	public List<ImplantIndentDetail> getImplantIndentDetails() {
		return implantIndentDetails;
	}

	public void setImplantIndentDetails(List<ImplantIndentDetail> implantIndentDetails) {
		this.implantIndentDetails = implantIndentDetails;
	}

	public ImplantIndentDetail addImplantIndentDetail(ImplantIndentDetail implantIndentDetail) {
		if (getImplantIndentDetails() == null)
			this.implantIndentDetails = new ArrayList<ImplantIndentDetail>();

		getImplantIndentDetails().add(implantIndentDetail);
		implantIndentDetail.setImplantIndent(this);

		return implantIndentDetail;
	}

	public ImplantIndentDetail removeImplantIndentDetail(ImplantIndentDetail implantIndentDetail) {
		getImplantIndentDetails().remove(implantIndentDetail);
		implantIndentDetail.setImplantIndent(null);

		return implantIndentDetail;
	}

	public void updateImplantIndent(Boolean isConsignment, Boolean isImplant, Boolean isKit, String remark) {
		this.isConsignment = isConsignment != null ? isConsignment : this.isConsignment;
		this.isImplant = isImplant != null ? isImplant : this.isImplant;
		this.isKit = isKit != null ? isKit : this.isKit;
		this.remark = remark != null ? remark : this.remark;
	}

	public List<Issue> getIssue() {
		return issue;
	}

	public void setIssue(List<Issue> issue) {
		this.issue = issue;
	}
	
	

}
