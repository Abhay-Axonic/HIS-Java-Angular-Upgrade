package com.param.pharmacy.ip.dao.patientIssueRejection;

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
import com.param.entity.model.doctor.DoctorSpecialityMapper;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.global.common.ServiceException;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearch;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

@Repository("patientIssuesRejectionMedicationDao")
public class PatientIssuesRejectionMedicationDao extends AbstractDao<Integer, Reject> implements IPatientIssuesRejectionMedicationDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	private static final Logger logger = Logger.getLogger(PatientIssuesRejectionMedicationDao.class);

	@Override
	public List<Reject> patientIssuesRejectionMedicationSearch(PatientIssueRejectionSearch patientIssueRejectionSearch, Integer screenId)
	{
		try
		{
			String hql = "SELECT reject FROM Reject AS reject "
					+ " INNER JOIN reject.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " INNER JOIN reject.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' " 
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN bedMaster.floor AS floor WITH floor.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' " 
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " WHERE reject.isActive = true AND reject.isDeleted = false AND reject.unit.unitId = :unitId "
					+ " AND reject.rejectedDate BETWEEN :fromDate AND :toDate "
					+ " AND (:issueRejectNo = '' OR lower(reject.rejectNo) LIKE :issueRejectNo) "
					+ " AND ( floor.floorId = :floor OR :floor = 0 )"
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName " 
					+ "	OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY reject.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(patientIssueRejectionSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueRejectionSearch.getToDate()));
			query.setParameter("patientId",patientIssueRejectionSearch.getPatientId() != null ? patientIssueRejectionSearch.getPatientId() : 0);
			query.setParameter("patientName",patientIssueRejectionSearch.getPatientName()!= null ? patientIssueRejectionSearch.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("visitType",patientIssueRejectionSearch.getVisitType() != null ? patientIssueRejectionSearch.getVisitType() : 0);
			query.setParameter("doctorName",patientIssueRejectionSearch.getDoctorName()!= null ? patientIssueRejectionSearch.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("issueRejectNo",patientIssueRejectionSearch.getIssueRejectNo() != null ? patientIssueRejectionSearch.getIssueRejectNo().toLowerCase() + '%' : "");
			query.setParameter("floor", patientIssueRejectionSearch.getFloor() != null ? patientIssueRejectionSearch.getFloor() : 0);
			query.setParameter("uhid",patientIssueRejectionSearch.getUhid() != null ? patientIssueRejectionSearch.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	
	@Override  
	public List<PatientIssueRejectionSearchResult> patientIssuesRejectionMedicationSearchPT(PatientIssueRejectionSearch patientIssueRejectionSearch, Integer screenId)
	{
		String hql = "SELECT doctor.doctorId as doctorId, "
				+ " CONCAT(doctor.firstName, ' ', doctor.middleName, ' ', doctor.lastName,' | ',specialityMaster.specialityName) as doctorDetail, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, "
				+ " patient.dob as admissionPatDob, patientEncounter.dob as patientEncounterDob, "
//				+ " reject.issue.prescription.medicationType as medicationType, "
				+ " patient.gender.genderId as genderId, "
				+ " patient.imagePath as imagePath, "
				+ " patient.patientId as patientId, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''))AS patientName, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' ) as ageStringDet, "
				+ " gender.genderCode as genderCode, "
				+ " CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ', patient.lastName, ' | ', gender.genderCode, ' | ', patient.uhidNumber) as admittedPatientDetails, "
				+ " CONCAT(prefixEncounter.prefixCode, ' ', patientEncounter.firstName, ' ', patientEncounter.middleName, ' ', patientEncounter.lastName, ' | ', encounterGender.genderCode, ' | ', patientEncounter.uhidNumber) as encounterPatientDetails, "
				+ " reject.id as id, issue.id as issueId, reject.rejectNo as issueRejectNo, reject.rejectedDate as issueRejectDate, "
				+ " issue.issueNo as issueNo, patient.uhidNumber as admittedPatientUhid, patientEncounter.uhidNumber as encounterPatientUhid, "
				+ " store.storeName as store, visitType.visitTypeCode as visitType, "
				+ " status.status as status, status.id as statusId, admission.admissionId as admissionId,"
				+ " encounter.encounterId as encounterId "
				+ " FROM Reject AS reject "
				+ " INNER JOIN reject.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
				+ " INNER JOIN reject.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " LEFT JOIN issue.admission AS admission  " //HIS-15728
				+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
				+ " LEFT JOIN patient.gender AS gender WITH gender.status = 'A' " 
				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
				+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
				+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
				+ " LEFT JOIN bedMaster.floor AS floor WITH floor.status = 'A' "
				+ " LEFT JOIN issue.encounter AS encounter   "      //HIS-15728
				+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
				+ " LEFT JOIN patientEncounter.gender AS encounterGender WITH encounterGender.status = 'A' "
				+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
				+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
				+ "  LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' " // HIS-10082 
				+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
				+ " WHERE reject.isActive = true AND reject.isDeleted = false AND reject.unit.unitId = :unitId  and reject.isConsumable=false "
				+ " AND reject.rejectedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:issueRejectNo = '' OR lower(reject.rejectNo) LIKE :issueRejectNo) "
				+ " AND ( floor.floorId = :floor OR :floor = 0 )"
				+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName " 
				+ "	OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
				+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
				+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
				+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " ORDER BY reject.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(patientIssueRejectionSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(patientIssueRejectionSearch.getToDate()));
		query.setParameter("patientId",patientIssueRejectionSearch.getPatientId() != null ? patientIssueRejectionSearch.getPatientId() : 0);
		query.setParameter("patientName",patientIssueRejectionSearch.getPatientName()!= null ? patientIssueRejectionSearch.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("visitType",patientIssueRejectionSearch.getVisitType() != null ? patientIssueRejectionSearch.getVisitType() : 0);
		query.setParameter("doctorName",patientIssueRejectionSearch.getDoctorName()!= null ? patientIssueRejectionSearch.getDoctorName().toLowerCase() + '%' : "");
		query.setParameter("issueRejectNo",patientIssueRejectionSearch.getIssueRejectNo() != null ? '%'+patientIssueRejectionSearch.getIssueRejectNo().toLowerCase() + '%' : "");
		query.setParameter("floor", patientIssueRejectionSearch.getFloor() != null ? patientIssueRejectionSearch.getFloor() : 0);
		query.setParameter("uhid",patientIssueRejectionSearch.getUhid() != null ? '%'+patientIssueRejectionSearch.getUhid().toLowerCase() + '%' : "");
		query.setParameter("unitId", patientIssueRejectionSearch.getUnitId()!=null ? patientIssueRejectionSearch.getUnitId() : sessionService.getUnitId());
		query.setParameter("screenId", screenId);
			
		query.setResultTransformer(Transformers.aliasToBean(PatientIssueRejectionSearchResult.class));
		
		return query.list();
	}
	
	@Override
	public Reject patientIssuesRejectionMedicationById(Integer id)
	{
		try
		{
			String hql = "  SELECT reject FROM Reject AS reject "
					+ " WHERE reject.isActive = true AND reject.isDeleted = false "
					+ " AND reject.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return (Reject) query.uniqueResult();	
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<RejectDetail> patientIssuesRejectionMedicationDetailById(Integer rejectId)
	{
		try
		{
			String hql = "  SELECT rejectDetail FROM RejectDetail AS rejectDetail "
					+ " INNER JOIN rejectDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
					+ " INNER JOIN rejectDetail.reject AS reject WITH reject.isActive = true AND reject.isDeleted = false "
					+ " WHERE rejectDetail.isActive = true AND rejectDetail.isDeleted = false "
					+ " AND reject.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", rejectId);
			
			return query.list();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public RejectDetail patientIssuesRejectionMedicationDetailByIdValue(Integer rejectDetailId)
	{
		try
		{
			String hql = "  SELECT rejectDetail FROM RejectDetail AS rejectDetail "
					+ " WHERE rejectDetail.isActive = true AND rejectDetail.isDeleted = false "
					+ " AND rejectDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", rejectDetailId);
			
			return (RejectDetail) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Reject patientIssuesRejectionMedicationSave(Reject reject)
	{
		getSession().update(reject);
		return reject;
	}
	
	@Override
	public RejectedRejection patientIssuesRejectedRejectionMedicationSave(RejectedRejection rejectedRejection)
	{
		getSession().save(rejectedRejection);
		return rejectedRejection;
	}


	@Override
	public List<PatientSponsorDetails> findByEncounterId(Integer encounterId) {
		String hql = "SELECT detail FROM PatientSponsorDetails AS detail "
				+ "	INNER JOIN detail.encounterMaster AS encounter "
				+ "	WHERE detail.status='A' and encounter.encounterId = :encounterId "
				+ "	ORDER BY detail.priorityId ";
		Query query = getSession().createQuery(hql);
		
		query.setParameter("encounterId", encounterId);

		return query.list();
	}


	@Override
	public List<PatientSponsorDetails> findByAdmissionId(Integer admissionId) {
		String hql = "SELECT detail FROM PatientSponsorDetails AS detail "
				+ "	INNER JOIN detail.admission AS admission WITH admission.admissionId = :admissionId "
				+ " WHERE detail.status='A' "
				+ "	ORDER BY detail.priorityId ";
		Query query = getSession().createQuery(hql);
		
		query.setParameter("admissionId", admissionId);

		return query.list();
	}
}