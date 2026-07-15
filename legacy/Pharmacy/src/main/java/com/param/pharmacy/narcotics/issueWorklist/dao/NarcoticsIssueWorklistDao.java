package com.param.pharmacy.narcotics.issueWorklist.dao;

import java.util.Date;
import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;

@Repository("narcoticsIssueWorklistDao")
public class NarcoticsIssueWorklistDao extends AbstractDao<Integer, Issue> implements INarcoticsIssueWorklistDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(NarcoticsIssueWorklistDao.class);

	@Override
	public List<Issue> narcoticsIssueWorklistSearch(NarcoticsRequest narcoticsRequest, Integer screenId)
	{
		try
		{
			String hql = "SELECT issue FROM Issue AS issue "
					+ " INNER JOIN issue.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN issue.visitType as visitType WITH visitType.status = 'A'  "
					+ " INNER JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " LEFT JOIN prescription.priority AS priority WITH priority.status = 'A'"
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN issue.admission as admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient as patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster as wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster as bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					//(After discussed with Divyesh)+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					//+ " INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false AND storeType.id = 6 "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.addedDate BETWEEN  :fromDate AND :toDate"
					+ " AND issue.isNarcotics = true "
					//(After discussed with Divyesh)+ "  AND store.isNarcotic = true  "
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 )"
					+ " AND issue.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ( status.id = :statusId OR :statusId = 0 )"
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid) "
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)" 
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " AND  prescription.id in ( select prescriptionDetailsList.prescription.id from PrescriptionDetail as prescriptionDetailsList "
					+ "	INNER JOIN prescriptionDetailsList.item as item with item.isNarcotic = true )"
					+ " ORDER BY  :orderby ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(narcoticsRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(narcoticsRequest.getToDate()));
			query.setParameter("statusId", narcoticsRequest.getStatusId() != null ? narcoticsRequest.getStatusId() : 0);
			query.setParameter("visitType",narcoticsRequest.getVisitType() != null ? narcoticsRequest.getVisitType() : 0);
			query.setParameter("patientId",narcoticsRequest.getPatientId() != null ? narcoticsRequest.getPatientId() : 0);
			query.setParameter("patientName",narcoticsRequest.getPatientName() != null ? narcoticsRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",narcoticsRequest.getDoctorName()!= null ? narcoticsRequest.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("wardId",narcoticsRequest.getWardId() != null ? narcoticsRequest.getWardId() : 0);
			query.setParameter("uhid",narcoticsRequest.getUhid() != null ? narcoticsRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), issue.id DESC");
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<IssueDetail> narcoticsIssueWorklistDetails(Integer id)
	{
		try
		{
			String hql = "SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " LEFT JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE (:id = issue.id) AND issueDetail.isActive = true AND issueDetail.isDeleted = false and issueDetail.isSubstitute=false and issueDetail.prescriptionDetail.statFlag=false"
					;
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return query.list();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	/*@Override
	public PrescriptionFrequencyDetail getPrescriptionFrequencyDetails(Long id)
	{
		try
		{
			String hql = "SELECT prescriptionFrequencyDetail FROM PrescriptionFrequencyDetail AS prescriptionFrequencyDetail "
					+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE (:id = prescriptionDetail.id) AND prescriptionFrequencyDetail.isActive = true AND prescriptionFrequencyDetail.isDeleted = false ";
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
		return (PrescriptionFrequencyDetail) query.uniqueResult();
	}
	catch(Exception e)
	{
		
	}
		return null;
	}*/
	
	@Override
	public Issue narcoticsIssueWorklistById(Integer id)
	{
		try
		{
			String hql = "SELECT issue FROM Issue AS issue "
					+ " WHERE (:id = issue.id) AND issue.isActive = true AND issue.isDeleted = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
		return (Issue) query.uniqueResult();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	
	@Override
	public Long getIssuedQuantity(Long prescriptionDetailId) {
		String hql = "SELECT  COALESCE(SUM(issueDetailBatchMapper.issueQuantity), 0) AS issuedQuantity "
				+ " FROM IssueDetail AS issueDetail "
				+ "		INNER JOIN issueDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isDeleted = false AND prescriptionDetail.isActive = true "
				+ "		LEFT JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.isActive = true "
				+ "	WHERE issueDetail.isDeleted = false AND issueDetail.isActive = true "
				+ "     AND prescriptionDetail.id = :prescriptionDetailId "
				+ " GROUP BY prescriptionDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("prescriptionDetailId", prescriptionDetailId);

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0);
	}

	@Override
	public List<NarcoticsResponse> narcoticsIssueWorklistSearchPT(NarcoticsRequest narcoticsRequest, Integer screenId, Integer unitId) {
		
		String hql = "SELECT issue.id as id, visitType.visitTypeCode as visitType, "
				+ " admission.createdDate as admissionDate,admission.admissionId as admissionId, prescription.prescriptionDate as prescriptionDate, "
				+ " prescription.medicationType as medicationType, " 
				+ " prescription.addedByUser as addedByUser, " 
				+ " patient.uhidNumber as admissionUhid, priority.priorityDesc as priority, status.status as status, "
				+ " status.id as statusId, encounter.createdDate as encounterDate, specialityMaster.specialityName as specialityName, "
				+ " patientEncounter.uhidNumber as encounterUhid, "
				+ " (case when encounterGender.genderId is null then admissionGender.genderId else encounterGender.genderId end) as genderId ,"
				+ " (case when encounterGender.genderCode is null then admissionGender.genderCode else encounterGender.genderCode end) as genderCode ,"
				+ " (case when patient.imagePath is null then patientEncounter.imagePath else patient.imagePath end) as imagePath ,"
				+ " (case when "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patientEncounter.dob)) , 'Y ' ,"
				+ " EXTRACT(month from AGE(CURRENT_TIMESTAMP, patientEncounter.dob)),'M ') is null"
				+ " then concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' ,"
				+ " EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') else "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patientEncounter.dob)) , 'Y ' ,"
				+ " EXTRACT(month from AGE(CURRENT_TIMESTAMP, patientEncounter.dob)),'M ') end) as ageString ,"
				+ " (case when CONCAT(encounterPrefix.prefixCode, ' ', patientEncounter.firstName, ' ', patientEncounter.middleName, ' ',patientEncounter.lastName) is null then"
				+ " CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ',patient.lastName)"
				+ " else CONCAT(encounterPrefix.prefixCode, ' ', patientEncounter.firstName, ' ', patientEncounter.middleName, ' ',patientEncounter.lastName) end) as patientName ,"
				+ " CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ',patient.lastName, ' | ', admissionGender.genderCode, ' | ') AS admissionPatDtls, "
				+ " CONCAT(encounterPrefix.prefixCode, ' ', patientEncounter.firstName, ' ', patientEncounter.middleName, ' ',patientEncounter.lastName, ' | ', encounterGender.genderCode, ' | ') AS encounterPatDtls, "
				+ " CONCAT(doctor.firstName, ' | ', doctor.middleName, ' ', doctor.lastName, ' | ') as doctorDetail, "
				+ " patient.dob as admissionDob, patientEncounter.dob as encounterDob, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, doctor.doctorId as doctorId "
				+ " FROM Issue AS issue "
				+ " INNER JOIN issue.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ " INNER JOIN issue.visitType as visitType WITH visitType.status = 'A'  "
				+ " INNER JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " INNER JOIN prescription.priority AS priority WITH priority.status = 'A'"
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
				+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
				+ " LEFT JOIN issue.admission as admission WITH admission.status = 'A' "
				+ " LEFT JOIN admission.patient as patient WITH patient.status = 'A' "
				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' "
				+ " LEFT JOIN admissionDetail.wardMaster as wardMaster WITH wardMaster.status = 'A' "
				+ " LEFT JOIN admissionDetail.bedMaster as bedMaster WITH bedMaster.status = 'A' "
				+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
				+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
				+ " LEFT JOIN patientEncounter.prefix AS encounterPrefix WITH encounterPrefix.status = 'A' "
				+ " LEFT JOIN patientEncounter.gender AS encounterGender WITH encounterGender.status = 'A' "
				+ " LEFT JOIN patient.gender AS admissionGender WITH admissionGender.status = 'A' "
				+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
				+ "  "
				//(After discussed with Divyesh)+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
			    + " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
				//+ " INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false AND storeType.id = 6 "
				+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.addedDate BETWEEN  :fromDate AND :toDate "
				+ " AND issue.isNarcotics = true "
				//(After discussed with Divyesh)+ "  AND store.isNarcotic = true  "
//			    + "  AND store.isNarcotic = true  "
//			    + " AND store.storeId =:storeId"
				+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 )"
				+ " AND issue.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
				+ " AND ( status.id = :statusId OR :statusId = 0 )"
				+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid) "
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)" 
				+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
				+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
				+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " AND  prescription.id in ( select prescriptionDetailsList.prescription.id from PrescriptionDetail as prescriptionDetailsList "
				+ "	INNER JOIN prescriptionDetailsList.item as item with item.isNarcotic = true  where prescriptionDetailsList.statFlag= false)"
				+ " ORDER BY  :orderby ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(narcoticsRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(narcoticsRequest.getToDate()));
		query.setParameter("statusId", narcoticsRequest.getStatusId() != null ? narcoticsRequest.getStatusId() : 0);
		query.setParameter("visitType",narcoticsRequest.getVisitType() != null ? narcoticsRequest.getVisitType() : 0);
		query.setParameter("patientId",narcoticsRequest.getPatientId() != null ? narcoticsRequest.getPatientId() : 0);
		query.setParameter("patientName",narcoticsRequest.getPatientName() != null ? '%'+narcoticsRequest.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("doctorName",narcoticsRequest.getDoctorName()!= null ? '%'+narcoticsRequest.getDoctorName().toLowerCase() + '%' : "");
		query.setParameter("wardId",narcoticsRequest.getWardId() != null ? narcoticsRequest.getWardId() : 0);
		query.setParameter("uhid",narcoticsRequest.getUhid() != null ? '%'+narcoticsRequest.getUhid().toLowerCase() + '%' : "");
		query.setParameter("unitId", unitId !=null ? unitId : sessionService.getUnitId());
		query.setParameter("screenId", screenId);
		query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), issue.id DESC");
//		query.setParameter("storeId", narcoticsRequest.getStoreId());
		query.setResultTransformer(Transformers.aliasToBean(NarcoticsResponse.class));
		
		return query.list();
		/*
		String sql = "select" + 
				"	issue.id as \"id\"," + 
				"	visitType.visit_type_code as \"visitType\"," + 
				"	admission.created_date as \"admissionDate\"," + 
				"	prescription.prescription_date as \"prescriptionDate\"," + 
				"	patient.uhid_number as \"admissionUhid\"," + 
				"	priority.priority_desc as \"priority\"," + 
				"	status.status as \"status\"," + 
				"	status.id as \"statusId\"," + 
				"	encounter.created_date as \"encounterDate\"," + 
				"	patientEncounter.uhid_number as \"encounterUhid\"," + 
				"	(" + 
				"		prefix.prefix_code || ' ' || patient.first_name || ' ' || patient.middle_name || ' ' || patient.last_name || ' | ' || admissionGender.gender_code || ' | '" + 
				"	) as \"admissionPatDtls\"," + 
				"	(" + 
				"		encounterPrefix.prefix_code || ' ' || patientEncounter.first_name || ' ' || patientEncounter.middle_name || ' ' || patientEncounter.last_name || ' | ' || encounterGender.gender_code || ' | '" + 
				"	) as \"encounterPatDtls\"," + 
				"	(" + 
				"		doctor.first_name || ' | ' || doctor.middle_name || ' ' || doctor.last_name || ' | '" + 
				"	) as \"doctorDetail\"," + 
				"	patient.dob as \"admissionDob\"," + 
				"	patientEncounter.dob as \"encounterDob\"," + 
				"	wardMaster.ward_name as \"wardName\"," + 
				"	bedMaster.bed_number as \"bedNo\"," + 
				"	doctor.doctor_id as \"doctorId\"" + 
				" from" + 
				"	pharmacy.t_issue issue" + 
				" inner join public.m_status status on" + 
				"	issue.status_id = status.id" + 
				"	and(" + 
				"		status.is_active = true" + 
				"		and status.is_deleted = false" + 
				"	)" + 
				" inner join public.m_visit_type_master visitType on" + 
				"	issue.visit_type_id = visitType.visit_type_id" + 
				"	and(" + 
				"		visitType.status = 'A'" + 
				"	)" + 
				" inner join pharmacy.t_prescription prescription on" + 
				"	issue.prescription_id = prescription.id" + 
				"	and(" + 
				"		prescription.is_active = true" + 
				"		and prescription.is_deleted = false" + 
				"	)" + 
				" inner join adt.m_priority_master priority on" + 
				"	prescription.priority_id = priority.priority_id" + 
				"	and(" + 
				"		priority.status = 'A'" + 
				"	)" + 
				" inner join doctor.m_doctor_master doctor on" + 
				"	issue.doctor_id = doctor.doctor_id" + 
				"	and(" + 
				"		doctor.status = 'A'" + 
				"	)" + 
				" left outer join adt.t_admission admission on" + 
				"	issue.admission_id = admission.admission_id" + 
				"	and(" + 
				"		admission.status = 'A'" + 
				"	)" + 
				" left outer join patient.t_patient_registration patient on" + 
				"	admission.patient_id = patient.patient_id" + 
				"	and(" + 
				"		patient.status = 'A'" + 
				"	)" + 
				" left outer join public.m_prefix_master prefix on" + 
				"	patient.prefix_id = prefix.prefix_id" + 
				"	and(" + 
				"		prefix.status = 'A'" + 
				"	)" + 
				" left outer join public.m_gender_master admissionGender on" + 
				"	patient.gender_id = admissionGender.gender_id" + 
				"	and(" + 
				"		admissionGender.status = 'A'" + 
				"	)" + 
				" left outer join adt.t_admission_details admissionDetail on" + 
				"	admission.admission_id = admissionDetail.admission_id" + 
				"	and(" + 
				"		admissionDetail.status = 'A'" + 
				"	)" + 
				" left outer join adt.m_ward_master wardMaster on" + 
				"	admissionDetail.ward_id = wardMaster.ward_id" + 
				"	and(" + 
				"		wardMaster.status = 'A'" + 
				"	)" + 
				" left outer join adt.m_bed_master bedMaster on" + 
				"	admissionDetail.bed_id = bedMaster.bed_id" + 
				"	and(" + 
				"		bedMaster.status = 'A'" + 
				"	)" + 
				" left outer join opd.t_encounter_master encounter on" + 
				"	issue.encounter_id = encounter.encounter_id" + 
				"	and(" + 
				"		encounter.status = 'A'" + 
				"	)" + 
				" left outer join patient.t_patient_registration patientEncounter on" + 
				"	encounter.patient_id = patientEncounter.patient_id" + 
				"	and(" + 
				"		patientEncounter.status = 'A'" + 
				"	)" + 
				" left outer join public.m_prefix_master encounterPrefix on" + 
				"	patientEncounter.prefix_id = encounterPrefix.prefix_id" + 
				"	and(" + 
				"		encounterPrefix.status = 'A'" + 
				"	)" + 
				" left outer join public.m_gender_master encounterGender on" + 
				"	patientEncounter.gender_id = encounterGender.gender_id" + 
				"	and(" + 
				"		encounterGender.status = 'A'" + 
				"	)" + 
				" left outer join public.m_prefix_master prefixEncounter on" + 
				"	patientEncounter.prefix_id = prefixEncounter.prefix_id" + 
				"	and(" + 
				"		prefixEncounter.status = 'A'" + 
				"	)" + 
				" inner join public.m_store store on" + 
				"	issue.store_id = store.store_id" + 
				"	and(" + 
				"		store.is_active = true" + 
				"		and store.is_deleted = false" + 
				"	)" + 
				" where" + 
				"	issue.is_active = true" + 
				"	and issue.is_deleted = false" + 
				"	and to_char(issue.added_date,'YYYY-MM-DD')" + 
				"		BETWEEN  :fromDate AND :toDate" + 
				"	and store.is_narcotic = true" + 
				"	and store.store_id =:storeId" + 
				"	and(" + 
				"		wardMaster.ward_id =:wardId" + 
				"		or :wardId= 0" + 
				"	)" + 
				"	and issue.unit_id =:unitId" + 
				"	and(" + 
				"		visitType.visit_type_id =:visitType" + 
				"		or :visitType= 0" + 
				"	)" + 
				"	and(" + 
				"		status.id =:statusId" + 
				"		or :statusId= 0" + 
				"	)" + 
				"	and(" + 
				"		:uhid= ''" + 
				"		or lower( patient.uhid_number ) like :uhid" + 
				"		or lower( patientEncounter.uhid_number ) like :uhid" + 
				"	)" + 
				"	and(" + 
				"		:doctorName= ''" + 
				"		or lower( coalesce(( doctor.first_name || ' ' || doctor.middle_name || ' ' || doctor.last_name ))) like :doctorName" + 
				"		or lower( doctor.first_name ) like :doctorName" + 
				"		or :doctorName= ''" + 
				"		or lower( doctor.middle_name ) like :doctorName" + 
				"		or :doctorName= ''" + 
				"		or lower( doctor.last_name ) like :doctorName" + 
				"	)" + 
				"	and(" + 
				"		:patientId= 0" + 
				"		or patient.patient_id =:patientId" + 
				"		or patientEncounter.patient_id =:patientId" + 
				"	)" + 
				"	and(" + 
				"		:patientName!= 0" + 
				"		and :patientName!= ''" + 
				"		or :patientName= ''" + 
				"		or lower( coalesce(( prefix.prefix_code || ' ' || patient.first_name || ' ' || patient.middle_name || ' ' || patient.last_name ))) like :patientName" + 
				"		or lower( coalesce(( prefixEncounter.prefix_code || ' ' || patientEncounter.first_name || ' ' || patientEncounter.middle_name || ' ' || patientEncounter.last_name ))) like :patientName" + 
				"		or lower( prefix.prefix_code ) like :patientName" + 
				"		or lower( patient.first_name ) like :patientName" + 
				"		or lower( patient.middle_name ) like :patientName" + 
				"		or lower( patient.last_name ) like :patientName" + 
				"		or lower( prefixEncounter.prefix_code ) like :patientName" + 
				"		or lower( patientEncounter.first_name ) like :patientName" + 
				"		or lower( patientEncounter.middle_name ) like :patientName" + 
				"		or lower( patientEncounter.last_name ) like :patientName" + 
				"	)" + 
				"	and(" + 
				"		status.id in(" + 
				"			select" + 
				"				status.id" + 
				"			from" + 
				"				public.m_screen_status_mapper screenStatus" + 
				"			inner join public.m_screen screen on" + 
				"				screenStatus.screen_id = screen.id" + 
				"				and(" + 
				"					screen.id =:screenId" + 
				"				)" + 
				"			inner join public.m_status status on" + 
				"				screenStatus.status_id = status.id" + 
				"		)" + 
				"	)" + 
				"	and(" + 
				"		prescription.id in(" + 
				"			select" + 
				"				prescriptionDetailsList.prescription_id" + 
				"			from" + 
				"				pharmacy.t_prescription_detail prescriptionDetailsList" + 
				"			inner join public.m_item item on" + 
				"				prescriptionDetailsList.item_id = item.id" + 
				"				and(" + 
				"					item.is_narcotic = true" + 
				"				)" + 
				"		)" + 
				"	)" + 
				"order by" + 
				"	:orderby";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("fromDate", Util.getDate(narcoticsRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(narcoticsRequest.getToDate()));
		query.setParameter("statusId", narcoticsRequest.getStatusId() != null ? narcoticsRequest.getStatusId() : 0);
		query.setParameter("visitType",narcoticsRequest.getVisitType() != null ? narcoticsRequest.getVisitType() : 0);
		query.setParameter("patientId",narcoticsRequest.getPatientId() != null ? narcoticsRequest.getPatientId() : 0);
		query.setParameter("patientName",narcoticsRequest.getPatientName() != null ? narcoticsRequest.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("doctorName",narcoticsRequest.getDoctorName()!= null ? narcoticsRequest.getDoctorName().toLowerCase() + '%' : "");
		query.setParameter("wardId",narcoticsRequest.getWardId() != null ? narcoticsRequest.getWardId() : 0);
		query.setParameter("uhid",narcoticsRequest.getUhid() != null ? narcoticsRequest.getUhid().toLowerCase() + '%' : "");
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("screenId", screenId);
		query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), issue.id DESC");
		query.setParameter("storeId", narcoticsRequest.getStoreId());
		query.setResultTransformer(Transformers.aliasToBean(NarcoticsResponse.class));
		
		return query.list();*/
	}

	@Override
	public List<IssueDetail> addeIssueItems(Integer id) {
		try
		{
			String hql = "SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " WHERE (:id = issueDetail.id) AND issueDetail.isActive = true AND issueDetail.isDeleted = false and issueDetail.isSubstitute=true ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return query.list();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
}