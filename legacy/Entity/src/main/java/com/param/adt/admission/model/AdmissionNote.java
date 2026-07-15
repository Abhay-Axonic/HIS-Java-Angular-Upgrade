package com.param.adt.admission.model;



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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.ReasonMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.billing.global.model.DepositMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.GenderMaster;
import com.param.global.model.PatientCategoryMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.PrefixMaster;
import com.param.global.model.SpecialityMaster;
import com.param.global.model.UnknownPatientRegistration;
import com.param.global.model.VisitTypeMaster;

@NamedQueries({
	@NamedQuery(name="GET_PATIENT_LIST_LIKE" , query= "SELECT patient.patientId as patientId, "
			+ "patient.firstName as firstName, "
			+ "patient.uhidNumber as uhidNumber, "
			+ "patient.lastName as lastName "
			+ "FROM PatientRegistration patient "
			+ "WHERE (LOWER(patient.firstName) LIKE :firstName OR LOWER(patient.uhidNumber) LIKE :firstName) "
			+ "AND patient.status='A' "
			//+ "AND patient.unitId=:unitId "
			+ "AND patient.organizationId=:organizationId "),
	
	@NamedQuery(name="GET_PATIENT_VIST_DETAILS_BY_ID",query="SELECT patient.patientId as patientId, "
			+ "patient.uhidNumber as uhidNumber, "
			+ "patient.prefixId as prefixId, "
			+ "prefix.prefixCode as prefixDesc, "
			+ "patient.firstName as firstName, "
			+ "patient.middleName as middleName, "
			+ "patient.lastName as lastName, "
			+ "patient.genderId as id, "
			+ "to_char(patient.dob,'dd/MM/yyyy') as dob, "
			+ "gender.code as code, "
			+ "patient.patientCategoryId as patientCategoryId, "
			+ "pc.patientCategory as patientCategory "
			+ "FROM PatientRegistration as patient "
			+ "INNER JOIN patient.prefixMaster as prefix "
			+ "INNER JOIN patient.genderMaster as gender "
			+ "INNER JOIN patient.patientCategoryMaster as pc "
			+ "WHERE patient.patientId=:patientId"),
	
	@NamedQuery(name="IS_ALREADY_REQUEST_EXIST",query="SELECT note.admissionNoteId as admissionNoteId "
			+ "FROM AdmissionPatientMapper mpr "
			+ "INNER JOIN mpr.admissionNote note "
			+ "WHERE note.unitId=:unitId "
			+ "AND note.patientId=:patientId "
			+ "AND note.requestToDoctorId=:docId "
			+ "AND mpr.doa=:doa "
			+ "AND mpr.status='A' "
			+ "AND mpr.activeStatus='P' ")

	

})

@NamedNativeQueries
({
		@NamedNativeQuery(name = "GET_PENDING_ADMISSION_REQUEST_DETAILS", query = "SELECT admissionNote.admission_note_id as \"admissionNoteId\" ,"
				+ " admissionNote.is_admission_request_from_coversheet as \"isAdmissionReqFromCoversheet\","
				+ " admissionNote.visit_type_id as \"visitTypeId\", "
				+ " admissionNote.source_visit_type_id as \"sourceVisitTypeId\", " 
				+ " admissionNote.patient_id as \"patientId\", "
				+ "admissionNote.t_patient_id as \"tPatientId\", " 
				+ "admissionNote.gender_id as \"id\", "
				+ "admissionNote.reason_id as \"reasonId\", "
				+ "admissionNote.request_to_doctor_id as \"requestToDoctorId\", "
				+ "admissionNote.doctor_speciality_id as \"doctorSpecialityId\", "
				+ "admissionNote.is_flexiable_date as \"isFlexiableDate\", "
				+ "admissionNote.treating_doctor_id as \"treatingDoctorId\", "
				+ "admissionNote.treating_doctor_speciality_id as \"treatingDoctorSpecialityId\", "
				+ "admissionNote.admission_type_id as \"admissionTypeId\", "
				+ "admissionNote.admission_note_number as \"admissionNoteNumber\", "
				+ "coalesce(admissionNote.deposit_amt,0) as \"depositAmount\", "
				+ "coalesce(admissionNote.deposit_pending,0) as \"depositPending\", "
				+ "coalesce(admissionNote.deposit_collected,0) as \"depositCollected\", "
				+ "coalesce(admissionNote.deposit_status,1) as \"depositStatus\", "
				+ "concat(doctor.first_name,' ', doctor.middle_name,' ',doctor.last_name) as \"dFirstName\", "
				+ "speciality.speciality_name as \"specialityName\", "
				+ "visitType.visit_type_code as \"visitTypeName\", " 
				+ "patient.uhid_number as \"UHIDNumber\", "
				+ "patient.image_path as \"imagePath\", "
				+ "concat(patient.first_name,' ',patient.middle_name,' ',patient.last_name) as \"pFirstName\", "
				+ "to_char(patient.dob,'MM/dd/yyyy') as \"dob\", " 
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') as \"age\", "
				+ "gender.gender_code as \"code\", "
				+ "reason.reason_desc as \"reasonDesc\", " 
				+ "mapper.bed_category_id as \"bedCategoryId\", "
				+ "bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "mapper.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "patient.patient_category_id as \"patientCategoryId\", "
				+ "to_char(mapper.doa,'MM/dd/yyyy') as \"doa\", " 
				+ "to_char(mapper.pdd,'MM/dd/yyyy') as \"pdd\", "
				+ "bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "billingbedCat.billing_bed_desc as \"billingBedDesc\", "
				+ "patCategoryMst.patient_category as \"patientCategory\", "
				+ "payEn.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "bcwl.bed_category_waiting_list_id as \"bedCategoryWaitingListId\", "
				+ "patient.registration_type_id as \"registrationTypeId\", "
				+ "patient.prefix_id as \"prefixId\", "
				+ "patient.first_name as \"firstName\", "
				+ "patient.middle_name as \"middleName\", "
				+ "patient.last_name as \"lastName\", "
				+ "patient.mobile_number as \"mobileNumber\", "
				+ "patient.email as \"email\","
				+ "patient.blood_group as \"bloodGroup\","
				+ "dtls.nursing_rejection_note as \"requestRejectionNote\", "
				+ "res.reason_desc as \"requestRejectionReason\" "
				+ " FROM adt.t_admission_note as admissionNote "
				+ " INNER JOIN doctor.m_doctor_master as doctor on admissionNote.request_to_doctor_id = doctor.doctor_id "
				+ " INNER JOIN public.m_visit_type_master as visitType on admissionNote.visit_type_id = visitType.visit_type_id "
				+ " INNER JOIN patient.t_patient_registration as patient on admissionNote.patient_id = patient.patient_id "
				+ " INNER JOIN public.m_gender_master as gender on admissionNote.gender_id = gender.gender_id "
				+ " INNER JOIN adt.m_reason_master as reason on admissionNote.reason_id  = reason.reason_master_id "
				+ " INNER JOIN doctor.m_speciality_master as speciality on admissionNote.doctor_speciality_id = speciality.speciality_id "
				+ " INNER JOIN adt.t_admission_patient_mapping as mapper on admissionNote.admission_note_id = mapper.admission_note_id "
				+ " LEFT JOIN public.m_patient_category_master patCategoryMst on patient.patient_category_id = patCategoryMst.patient_category_id "
				+ " INNER JOIN adt.m_bed_category_master bedCatMst on mapper.bed_category_id = bedCatMst.bed_category_id "
				+ " INNER JOIN adt.m_billing_bed_category_master billingbedCat on billingbedCat.billing_bed_category_id = bedCatMst.billing_bed_category_id "
				+ " INNER JOIN public.m_payment_entitlement_master payEn on mapper.payment_entitlement_id = payEn.payment_entitlement_id "
				+ " LEFT JOIN adt.t_bed_category_waiting_list bcwl on bcwl.admission_note_id = admissionNote.admission_note_id  AND bcwl.status='A' "
				+ " LEFT join adt.t_admission_details dtls on dtls.admission_id = admissionNote.admission_id and nursing_acceptance='R' "
				+ " left join adt.m_reason_master res on res.reason_master_id = dtls.nursing_rejection_reason_id "
				+ "WHERE admissionNote.admission_status='P' " 
				+ " AND mapper.active_status='P' "
				+ " AND mapper.status='A' " 
				+ " AND admissionNote.organization_id=:organizationId "
				+ " AND admissionNote.unit_id=:unitId "
				+ " AND admissionNote.status='A' AND mapper.status='A'"
				+ " order by admissionNote.admission_note_id desc " ),
		
		@NamedNativeQuery(name = "GET_OPD_ADMISSION_REQUEST_DETAILS", query =  "SELECT admissionNote.admission_note_id as \"admissionNoteId\" , "
				+ " admissionNote.visit_type_id as \"visitTypeId\", " 
				+ "admissionNote.patient_id as \"patientId\", "
				+ "admissionNote.is_flexiable_date as \"isFlexiableDate\", "
				+ "admissionNote.admission_type_id as \"admissionTypeId\", "
				+ "visitType.visit_type_code as \"visitTypeName\", " 
				+ "reason.reason_desc as \"reasonDesc\", " 
				+ "admissionNote.note as \"note\", " 
				+ "mapper.bed_category_id as \"bedCategoryId\", "
				+ " to_char(mapper.doa,'dd/MM/yyyy') as \"doa\", " 
				+ " to_char(mapper.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ "concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\","
				+ "spe.speciality_name as \"specialityName\","
				+ "admtypMst.admission_type_desc as \"admissionTypeDesc\", "
				+ " (case " 
				+ " when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
				+ " when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
				+ " else 'Admin' end) as \"userName\", "
				+ "bedCatMst.bed_category_desc as \"bedCategoryDesc\" "
				+ "FROM adt.t_admission_note as admissionNote "
				+ "INNER JOIN public.m_visit_type_master as visitType on admissionNote.visit_type_id = visitType.visit_type_id "
				+ "INNER JOIN adt.m_reason_master as reason on admissionNote.reason_id  = reason.reason_master_id "
				+ " INNER JOIN adt.t_admission_patient_mapping as mapper on admissionNote.admission_note_id = mapper.admission_note_id "
				+ "INNER JOIN adt.m_bed_category_master bedCatMst on mapper.bed_category_id = bedCatMst.bed_category_id "
				+ "LEFT JOIN adt.m_admission_type_master admtypMst on admissionNote.admission_type_id = admtypMst.admission_type_id "
				+ "left join doctor.m_doctor_master doc on admissionNote.treating_doctor_id = doc.doctor_id "
				+ "left join doctor.m_speciality_master spe on admissionNote.treating_doctor_speciality_id=spe.speciality_id "
				+ " left join public.m_user_master um on admissionNote.created_by = um.user_id " 
				+ " left join public.m_employee_master empmst on um.user_id = empmst.user_id " 
				+ " left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "
				+ " WHERE  admissionNote.organization_id=:organizationId "
				+ " AND admissionNote.unit_id=:unitId "
				+ " AND admissionNote.patient_id=:patientId "
				+ " AND admissionNote.encounter_id=:encounterId " 
				+ " AND admissionNote.status='A' "
				+ " and mapper.status='A' "
			),
		
		@NamedNativeQuery(name = "GET_DAY_CARE_ADMISSION_REQUEST_DETAILS", query =  "SELECT distinct(admissionNote.admission_note_id) as \"admissionNoteId\" , "
				+ " admissionNote.visit_type_id as \"visitTypeId\", " 
				+ " admissionNote.patient_id as \"patientId\", "
				+ " admissionNote.is_flexiable_date as \"isFlexiableDate\", "
				+ " admissionNote.admission_type_id as \"admissionTypeId\", "
				+ " visitType.visit_type_code as \"visitTypeName\", " 
				+ " reason.reason_desc as \"reasonDesc\", " 
				+ " admissionNote.note as \"note\", " 
				+ " mapper.bed_category_id as \"bedCategoryId\", "
				+ " workbench.result_id as \"resultId\", "
				+ " workbench.workbench_added_date as \"workbenchAddedDate\", "
				+ " workbench.patient_workbench_id as \"patientWorkbenchId\", "
				+ " workbench.remark as \"workbenchRemark\", "
				+ " to_char(mapper.doa,'dd/MM/yyyy') as \"doa\", "
				+ " to_char(mapper.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ " to_char(mapper.created_date,'DD/MM/YYYY') as \"createdDate\", "
				+ " admtypMst.admission_type_desc as \"admissionTypeDesc\", "
				+ "spe.speciality_name as \"specialityName\","
				+ "concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ " bedCatMst.bed_category_desc as \"bedCategoryDesc\" "
				+ " FROM adt.t_admission_note as admissionNote "
				+ " INNER JOIN public.m_visit_type_master as visitType on admissionNote.visit_type_id = visitType.visit_type_id "
				+ " INNER JOIN adt.m_reason_master as reason on admissionNote.reason_id  = reason.reason_master_id "
				+ " INNER JOIN adt.t_admission_patient_mapping as mapper on admissionNote.admission_note_id = mapper.admission_note_id "
				+ " INNER JOIN adt.m_bed_category_master bedCatMst on mapper.bed_category_id = bedCatMst.bed_category_id "
				+ " LEFT JOIN adt.m_admission_type_master admtypMst on admissionNote.admission_type_id = admtypMst.admission_type_id"
				+ " left join doctor.m_doctor_master doc on admissionNote.treating_doctor_id=doc.doctor_id "
				+ " left join doctor.m_speciality_master spe on admissionNote.treating_doctor_speciality_id=spe.speciality_id "
				+ " left join adt.t_admitted_patient_orders_workbench workbench on admissionNote.admission_note_id=workbench.record_id "
				+ " WHERE admissionNote.visit_type_id= :visitTypeId"
				+ " AND admissionNote.organization_id=:organizationId "
				+ " AND admissionNote.unit_id=:unitId "
				+ " AND admissionNote.patient_id=:patientId "
				+ " AND admissionNote.admission_id=:admissionId " 
				+ " AND admissionNote.status='A' "
				+ " AND admissionNote.is_admission_request_from_coversheet='Y' "
			),
		@NamedNativeQuery(name = "GET_ADMISSION_REQUEST_DETAILS_FOR_BOOK_VIEW", query =  "SELECT distinct(admissionNote.admission_note_id) as \"admissionNoteId\" , "
				+ " admissionNote.visit_type_id as \"visitTypeId\", " 
				+ " admissionNote.patient_id as \"patientId\", "
				+ " admissionNote.is_flexiable_date as \"isFlexiableDate\", "
				+ " admissionNote.admission_type_id as \"admissionTypeId\", "
				+ " visitType.visit_type_code as \"visitTypeName\", " 
				+ " reason.reason_desc as \"reasonDesc\", " 
				+ " admissionNote.note as \"note\", " 
				+ " mapper.bed_category_id as \"bedCategoryId\", "
				+ " to_char(mapper.doa,'dd/MM/yyyy') as \"doa\", " 
				+ " to_char(mapper.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ " admtypMst.admission_type_desc as \"admissionTypeDesc\", "
				+ "spe.speciality_name as \"specialityName\","
				+ "concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ " bedCatMst.bed_category_desc as \"bedCategoryDesc\" "
				+ " FROM adt.t_admission_note as admissionNote "
				+ " INNER JOIN public.m_visit_type_master as visitType on admissionNote.visit_type_id = visitType.visit_type_id "
				+ " INNER JOIN adt.m_reason_master as reason on admissionNote.reason_id  = reason.reason_master_id "
				+ " INNER JOIN adt.t_admission_patient_mapping as mapper on admissionNote.admission_note_id = mapper.admission_note_id "
				+ " INNER JOIN adt.m_bed_category_master bedCatMst on mapper.bed_category_id = bedCatMst.bed_category_id "
				+ " LEFT JOIN adt.m_admission_type_master admtypMst on admissionNote.admission_type_id = admtypMst.admission_type_id"
				+ " left join doctor.m_doctor_master doc on admissionNote.treating_doctor_id=doc.doctor_id "
				+ " left join doctor.m_speciality_master spe on admissionNote.treating_doctor_speciality_id=spe.speciality_id "
				+ " WHERE "
				+ " admissionNote.patient_id=:patientId "
				+ " AND admissionNote.status='A' "
				+ " AND admissionNote.is_admission_request_from_coversheet='Y' "
				+ " AND to_char(admissionNote.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
				+ " AND :toDate "
			),
		@NamedNativeQuery(name = "GET_PAST_ADMISSION_DETAILS", 
		query =  /*" select" + 
				"	adm.admission_id as \"admissionId\"," + 
				"	adm.visit_type_id as \"visitTypeId\"," + 
				"	vtm.visit_type_code as \"visitTypeName\"," + 
				"	to_char(adtl.doa, 'DD/MM/YYYY') as \"visitDate\"," + 
				"	adm.speciality_id as \"specialityId\"," + 
				"	dept.speciality_name as  \"specialityName\"," + 
				"	('Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)) as \"doctorName\" " + 
				"	" + 
				" from" + 
				"	adt.t_admission adm " + 
				" inner join adt.t_admission_details adtl on " + 
				"	adtl.admission_id = adm.admission_id " + 
				" left join doctor.m_doctor_master doc on " + 
				"	doc.doctor_id=adm.doctor_id" + 
				" left join doctor.m_speciality_master dept on " + 
				"	dept.speciality_id=adm.speciality_id	" + 
				" left join public.m_visit_type_master vtm on " + 
				"	vtm.visit_type_id=adm.visit_type_id	" + 
				" where" + 
				"	adm.admission_id !=:admissionId " + 
				"	and adm.patient_id=:patientId " + 
//				"	and adm.visit_type_id=:visitTypeId " + 
				"	and adtl.admission_details_id = (" + 
				"	select admission_details_id" + 
				"	from adt.t_admission_details" + 
				"	where admission_id = adm.admission_id" + 
				"	order by admission_id desc limit 1)" + 
				"	and adm.status='I'" + 
				"	and adm.patient_id=adm.patient_id" +
				"	and adtl.nursing_acceptance = 'Y'" +
				" order by adm.admission_id desc	"*/
		
		       "select"
		       + "	adm.admission_id as \"admissionId\"," + 
		       "	adm.visit_type_id as \"visitTypeId\"," + 
		       "	vtm.visit_type_code as \"visitTypeName\"," + 
		       "	to_char(" + 
		       "		adtl.doa," + 
		       "		'DD/MM/YYYY'" + 
		       "	) as \"visitDate\"," +
			   "	to_char(" +
			   "		adtl.created_date," +
			   "		'DD/MM/YYYY'" +
			   "	) as \"createdDate\"," +
					   "		adtl.doa as \"visitDateNew\"," +
		       "	adm.speciality_id as \"specialityId\"," + 
		       "	dept.speciality_name as \"specialityName\"," + 
		       "	(" + 
		       "		'Dr. ' || coalesce(doc.first_name)|| ' ' || coalesce(doc.middle_name)|| ' ' || coalesce(doc.last_name)" + 
		       "	) as \"doctorName\"" + 
		       " from adt.t_admission adm " + 
		       " left join public.m_visit_type_master vtm on" + 
		       "	vtm.visit_type_id = adm.visit_type_id" + 
		       " inner join adt.t_admission_details adtl on  " + 
		       "	adtl.admission_id = adm.admission_id  " + 
		       " left join doctor.m_doctor_master doc on  " + 
		       "	doc.doctor_id=adm.doctor_id " + 
		       " left join doctor.m_speciality_master dept on  " + 
		       "	dept.speciality_id=adm.speciality_id	 " + 
		       " left join adt.m_discharge_summary	dissumm on  " + 
		       "    dissumm.admission_id=adm.admission_id and dissumm.status='A' " + 
		       " left join adt.t_discharge	discharge on  " + 
		       "    discharge.admission_id=adm.admission_id " + 
		       " where  adm.patient_id=:patientId "+
		       "  and adm.admission_id !=:admissionId " + 
		       "   and adtl.nursing_acceptance = 'Y'" + 
		       "   and adm.status = 'I'  " + 
		       "   and adm.patient_id = adm.patient_id " + 
		       "	and adtl.admission_details_id = ( " + 
		       "	select admission_details_id " + 
		       "	from adt.t_admission_details " + 
		       "	where admission_id = adm.admission_id " + 
		       "	order by admission_details_id desc limit 1) "
			),
		
		@NamedNativeQuery(name = "GET_PATIENT_LIST_LIKE_FOR_ER_MAP_OF_BED", 
		query =  " 	SELECT distinct(patient.patient_id) as patientid, " + 
				" patient.first_name as firstName, " + 
				" patient.uhid_number as uhidNumber, " + 
				" patient.last_name as lastName " + 
				" FROM patient.t_patient_registration patient " + 
				" inner JOIN adt.t_admission adm on patient.patient_id=adm.patient_id " + 
				" WHERE (LOWER(patient.first_name) LIKE :firstName OR LOWER(patient.uhid_number) LIKE :firstName) " + 
				" AND patient.status='A' " + 
				" AND adm.visit_type_id= 4 " + 
				" AND patient.organization_id=:organizationId "
			)
		
})



@Entity
@Table(name = "t_admission_note", schema = "adt")
@SequenceGenerator(name="admission_note_seq", sequenceName="adt.admission_note_seq", allocationSize=1)
public class AdmissionNote 
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admission_note_seq")
	@Column(name="admission_note_id")
	private int admissionNoteId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="request_by")
	private Integer requestBy;
	
	@Column(name="request_to_doctor_id")
	private Integer requestToDoctorId;
	
	@Column(name="doctor_speciality_id")
	private Integer doctorSpecialityId;
	
	@Column(name="reason_id")
	private Integer reasonId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="pre_visit_id")
	private Integer preVisitId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="is_flexiable_date")
	private char isFlexiableDate;
	
	@Column(name="admission_status")
	private char admissionStatus;
	
	@Column(name="is_cancel")
	private char isCancel;
	
	@Column(name="canceled_by")
	private int canceledBy;
	
	@Column(name="canceled_date")
	private Date canceledDate;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private int createdBy;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;

	@Column(name="prefix_id")
	private Integer prefixId;
	
	@Column(name="gender_id")
	private Integer id;
	
	@Column(name="patient_category_id")
	private Integer patientCategoryId;
	
	@Column(name="t_patient_id")
	private Integer tPatientId;
	
	@Column(name="note")
	private String note;
	
	@Column(name="deposit_amt")
	private double depositAmount;
	
	@Column(name="deposit_pending")
	private double depositPending;
	
	@Column(name="deposit_collected")
	private double depositCollected;
	
	@Column(name="deposit_status")
	private Integer depositStatus;
	
	@Column(name="admission_type_id")
	private Integer admissionTypeId;

	@Column(name="admission_note_number")
	private String admissionNoteNumber;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="treating_doctor_id")
	private Integer treatingDoctorId;
	
	@Column(name="treating_doctor_speciality_id")
	private Integer treatingDoctorSpecialityId;
	
   @Column(name="is_admission_request_from_coversheet") 
   private Character  isAdmissionReqFromCoversheet;
   
   @Column(name="source_visit_type_id") 
   private Integer  sourceVisitTypeId;
   

	public Integer getSourceVisitTypeId() {
		return sourceVisitTypeId;
	}

	public void setSourceVisitTypeId(Integer sourceVisitTypeId) {
		this.sourceVisitTypeId = sourceVisitTypeId;
	}

	public Integer getTreatingDoctorId() {
		return treatingDoctorId;
	}

	public void setTreatingDoctorId(Integer treatingDoctorId) {
		this.treatingDoctorId = treatingDoctorId;
	}

	

	public Integer getTreatingDoctorSpecialityId() {
		return treatingDoctorSpecialityId;
	}

	public void setTreatingDoctorSpecialityId(Integer treatingDoctorSpecialityId) {
		this.treatingDoctorSpecialityId = treatingDoctorSpecialityId;
	}



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_to_doctor_id", insertable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefix_id", insertable = false, updatable = false)
	private PrefixMaster prefixMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_category_id", insertable = false, updatable = false)
	private PatientCategoryMaster patientCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reason_id", insertable = false, updatable = false)
	private ReasonMaster reasonMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_patient_id", insertable = false, updatable = false)
	private UnknownPatientRegistration unknownPatientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, updatable = false)
	private Admission admission;	
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admissionNote", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdmissionPatientDocuments> admissionPatientDocumentsList;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admissionNote", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdmissionPatientMapper> admissionPatientMappersList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admissionNote", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UnreservedPatient> unreservedPatientsList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admissionNote", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DepositMaster> listDepositMaster;

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	
	public Integer getAdmissionTypeId() {
		return admissionTypeId;
	}

	public void setAdmissionTypeId(Integer admissionTypeId) {
		this.admissionTypeId = admissionTypeId;
	}

	public List<DepositMaster> getListDepositMaster() {
		return listDepositMaster;
	}

	public void setListDepositMaster(List<DepositMaster> listDepositMaster) {
		this.listDepositMaster = listDepositMaster;
	}

	public Integer gettPatientId() {
		return tPatientId;
	}

	public void settPatientId(Integer tPatientId) {
		this.tPatientId = tPatientId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getCanceledBy() {
		return canceledBy;
	}

	public void setCanceledBy(int canceledBy) {
		this.canceledBy = canceledBy;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}


	public ReasonMaster getReasonMaster() {
		return reasonMaster;
	}

	public void setReasonMaster(ReasonMaster reasonMaster) {
		this.reasonMaster = reasonMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}


	public List<AdmissionPatientDocuments> getAdmissionPatientDocumentsList() {
		return admissionPatientDocumentsList;
	}

	public void setAdmissionPatientDocumentsList(List<AdmissionPatientDocuments> admissionPatientDocumentsList) {
		this.admissionPatientDocumentsList = admissionPatientDocumentsList;
	}

	public List<AdmissionPatientMapper> getAdmissionPatientMappersList() {
		return admissionPatientMappersList;
	}

	public void setAdmissionPatientMappersList(List<AdmissionPatientMapper> admissionPatientMappersList) {
		this.admissionPatientMappersList = admissionPatientMappersList;
	}

	public int getAdmissionNoteId() {
		return admissionNoteId;
	}

	public void setAdmissionNoteId(int admissionNoteId) {
		this.admissionNoteId = admissionNoteId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(Integer requestBy) {
		this.requestBy = requestBy;
	}

	public Integer getRequestToDoctorId() {
		return requestToDoctorId;
	}

	public void setRequestToDoctorId(Integer requestToDoctorId) {
		this.requestToDoctorId = requestToDoctorId;
	}

	public String getAdmissionNoteNumber() {
		return admissionNoteNumber;
	}

	public void setAdmissionNoteNumber(String admissionNoteNumber) {
		this.admissionNoteNumber = admissionNoteNumber;
	}

	public Integer getDoctorSpecialityId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialityId(Integer doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	

	public Date getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}

	

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public PrefixMaster getPrefixMaster() {
		return prefixMaster;
	}

	public void setPrefixMaster(PrefixMaster prefixMaster) {
		this.prefixMaster = prefixMaster;
	}

	public GenderMaster getGenderMaster() {
		return genderMaster;
	}

	public void setGenderMaster(GenderMaster genderMaster) {
		this.genderMaster = genderMaster;
	}


	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getPreVisitId() {
		return preVisitId;
	}

	public void setPreVisitId(Integer preVisitId) {
		this.preVisitId = preVisitId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public char getIsFlexiableDate() {
		return isFlexiableDate;
	}

	public void setIsFlexiableDate(char isFlexiableDate) {
		this.isFlexiableDate = isFlexiableDate;
	}

	public char getAdmissionStatus() {
		return admissionStatus;
	}

	public void setAdmissionStatus(char admissionStatus) {
		this.admissionStatus = admissionStatus;
	}

	public char getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(char isCancel) {
		this.isCancel = isCancel;
	}

	public int getCancelBy() {
		return canceledBy;
	}

	public void setCancelBy(int cancelBy) {
		this.canceledBy = cancelBy;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public double getDepositPending() {
		return depositPending;
	}

	public void setDepositPending(double depositPending) {
		this.depositPending = depositPending;
	}

	public double getDepositCollected() {
		return depositCollected;
	}

	public void setDepositCollected(double depositCollected) {
		this.depositCollected = depositCollected;
	}

	public Integer getDepositStatus() {
		return depositStatus;
	}

	public void setDepositStatus(Integer depositStatus) {
		this.depositStatus = depositStatus;
	}

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Character getIsAdmissionReqFromCoversheet() {
		return isAdmissionReqFromCoversheet;
	}

	public void setIsAdmissionReqFromCoversheet(Character isAdmissionReqFromCoversheet) {
		this.isAdmissionReqFromCoversheet = isAdmissionReqFromCoversheet;
	}

	
	
}
