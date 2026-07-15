package com.param.adt.admission.model;

import java.sql.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;

@NamedQueries({ @NamedQuery(name = "GET_VISITORS_LIST", 
	query = "SELECT vap.visitorPatientId as visitorPatientId, "
		+ "vpml.visitorPatientMapperId as visitorPatientMapperId, " 
		+ "vap.passNumber as passNumber, "
		+ "vpml.visitorPassTypeId as visitorPassTypeId, "
		+ "to_char(vpml.expiryDate,'dd/MM/yyyy') as expiryDate, " 
		+ "to_char(vpml.expiryDate,'dd/MM/yyyy') as txtOldDate, "
		+ "vptm.visitorPassTypeDesc as visitorPassTypeDesc "
		+ "FROM VisitorAgainstPatient vap " 
		+ "INNER JOIN vap.visitorPatientMappersList vpml "
		+ "INNER JOIN vpml.visitorPassTypeMaster vptm " 
		+ "WHERE vap.status='A' " 
		+ "AND vpml.status='A' "
		+ "AND vap.organizationId=:organizationId " 
		+ "AND vap.unitId=:unitId " 
		+ "AND vap.admissionId=:admissionId"),	
	@NamedQuery(name = "GET_PASSNUMBER_ALREADY_ISSUE_OR_NOT", 
	query = "SELECT count(vap.passNumber) as passNumberCount "		
		+ "FROM VisitorAgainstPatient vap "	
		+ "INNER JOIN vap.visitorPatientMappersList vpml "
		+ "INNER JOIN vpml.visitorPassTypeMaster vptm "
		+ "WHERE vap.status='A' " //status A used
		+ "AND vpml.visitorPassStatus='I' " // visitor status I for used
		+ "AND vap.organizationId=:organizationId " 
		+ "AND vap.unitId=:unitId " 
		+ "AND vap.passNumber=:passNumber")
	
})
@NamedNativeQueries({	
	@NamedNativeQuery(name = "CHECK_NO_OF_PASS_ISSUE_AGAINST_ADMISSION", 
	query = " select "
			+" 	count(vap.pass_number) as \"noOfPassCount\",(SELECT mppr.no_of_pass  "
			+" 	from public.t_visitor_pass_mapper mppr  "
			+" 	where mppr.unit_id=:unitId and mppr.organisation_id=:organizationId and mppr.bed_category_id=:bedCategoryId and mppr.visitor_type_pass_id=:visitTypeId and mppr.pass_type_id=:visitorPassTypeId) as \"noOfPassAllowed\" "
			+" from "
			+" 	adt.t_visitor_against_patient vap "
			+" inner join adt.t_visitor_patient_mapper tvpm on "
			+" 	vap.visitor_patient_id = tvpm.visitor_patient_id "
			+" inner join adt.m_visitor_pass_type_master mvptm on "
			+" 	tvpm.visitor_pass_type_id = mvptm.visitor_pass_type_id "
			+" where "
			+" 	vap.status = 'A' "
			+" 	and tvpm.status = 'A' "
			+" 	and vap.organization_id =:organizationId "
			+" 	and vap.unit_id =:unitId "
			+" 	and vap.admission_id =:admissionId "
			+" 	and tvpm.bed_category_id =:bedCategoryId "
			+" 	and tvpm.visit_type_id =:visitTypeId "
			+" 	and tvpm.visitor_pass_type_id =:visitorPassTypeId "),
	
	@NamedNativeQuery(name="GET_VISITOR_PATIENT_LIST",
	query="select distinct(admission.admission_id) as \"admissionId\" ,"
			+"   admission.patient_id as \"patientId\", "
			+"   admission.t_patient_id as \"tPatientId\", "
			+"   admission.admission_number as \"admissionNumber\", "
			+"  pr.uhid_number as \"UHIDNumber\", "
			+"  concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\", "
			+"  pr.gender_id as \"genderId\", "
			+"  gm.gender_code as \"genderCode\", "
			+"  to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
			+"  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\" ,"
			+"  to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", "
			+"  to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\" ,"
			+"  bm.floor_id as \"floorId\", "
			+"   bm.bed_number as \"bedNumber\", "
			+"   bm.bed_id as \"bedId\", "
			+"   bm.ward_id as \"wardId\", "
			+"  admission.speciality_id as \"specialityId\", "
			+"  sp.speciality_name as \"specialityName\" ,"
			+"    bedCatMst.bed_category_desc as \"bedCategoryDesc\" "
			+" FROM  adt.t_admission admission  "
			+" INNER JOIN adt.t_visitor_against_patient vap on vap.admission_id=admission.admission_id"
			+" INNER JOIN adt.t_visitor_patient_mapper tvpm on vap.visitor_patient_id = tvpm.visitor_patient_id"
			+" INNER JOIN adt.m_visitor_pass_type_master mvptm on tvpm.visitor_pass_type_id = mvptm.visitor_pass_type_id"
			+" INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
			+" left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
			+" INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
			+" INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
			+" INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
			+" INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id  and adl.status='A' "
			+" INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
			+"  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
			+" WHERE vap.status='A' "
			+" AND tvpm.status='A' "
			+" AND vap.organization_id=:organizationId "
			+" AND vap.unit_id=:unitId ")
})
@Entity
@Table(name = "t_visitor_against_patient", schema = "adt")
@SequenceGenerator(name = "visitor_against_patient_seq", sequenceName = "adt.visitor_against_patient_seq", allocationSize = 1)
public class VisitorAgainstPatient {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visitor_against_patient_seq")
	@Column(name = "visitor_patient_id")
	private int visitorPatientId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "pass_number")
	private String passNumber;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, updatable = false)
	private Admission admission;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "visitorAgainstPatient", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VisitorPatientMapper> visitorPatientMappersList;

	public int getVisitorPatientId() {
		return visitorPatientId;
	}

	public void setVisitorPatientId(int visitorPatientId) {
		this.visitorPatientId = visitorPatientId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getPassNumber() {
		return passNumber;
	}

	public void setPassNumber(String passNumber) {
		this.passNumber = passNumber;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
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

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public List<VisitorPatientMapper> getVisitorPatientMappersList() {
		return visitorPatientMappersList;
	}

	public void setVisitorPatientMappersList(List<VisitorPatientMapper> visitorPatientMappersList) {
		this.visitorPatientMappersList = visitorPatientMappersList;
	}

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}

}
