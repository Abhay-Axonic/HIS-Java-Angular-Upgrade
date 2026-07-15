package com.param.pharmacy.ip.dao.rejectedRejectionConsumable;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.common.ServiceException;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.rejectedRejectionConsumable.RejectedRejectionConsumableSearch;

@Repository("rejectedRejectionConsumableDao")
public class RejectedRejectionConsumableDao extends AbstractDao<Integer, RejectedRejection> implements IRejectedRejectionConsumableDao{

	private static final Logger logger = Logger.getLogger(RejectedRejectionConsumableDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<RejectedRejection> rejectedRejectionConsumableSearch(RejectedRejectionConsumableSearch rejectedRejectionSearch, Integer screenId)
	{

		try
		{
			String hql = "SELECT rejectedRejection FROM RejectedRejection AS rejectedRejection "
					+ " INNER JOIN rejectedRejection.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN rejectedRejection.reject AS reject WITH reject.isActive = true AND reject.isDeleted = false"
					+ " LEFT JOIN rejectedRejection.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " LEFT JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false " 
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A'"
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " LEFT JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN rejectedRejection.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE rejectedRejection.isActive = true AND rejectedRejection.isDeleted = false AND rejectedRejection.unit.unitId = :unitId AND rejectedRejection.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND( :rejectedNo = '' OR lower(rejectedRejection.rejectNo) LIKE :rejectedNo) "
					+ " AND (:visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
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
					+ " ORDER BY rejectedRejection.id DESC ";
				
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(rejectedRejectionSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(rejectedRejectionSearch.getToDate()));
			query.setParameter("patientId",rejectedRejectionSearch.getPatientId() != null ? rejectedRejectionSearch.getPatientId() : 0);
			query.setParameter("patientName",rejectedRejectionSearch.getPatientName()!= null ? '%'+rejectedRejectionSearch.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("rejectedNo",rejectedRejectionSearch.getRejectedNo() != null ? '%'+rejectedRejectionSearch.getRejectedNo().toLowerCase() + '%' : "");
			query.setParameter("visitType", rejectedRejectionSearch.getVisitType() != null ? rejectedRejectionSearch.getVisitType() : 0);
			query.setParameter("visitNo",rejectedRejectionSearch.getVisitNo() != null ? '%'+rejectedRejectionSearch.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("storeId", rejectedRejectionSearch.getStoreId() != null ? rejectedRejectionSearch.getStoreId() : 0);
			query.setParameter("uhid",rejectedRejectionSearch.getUhid() != null ? '%'+rejectedRejectionSearch.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", rejectedRejectionSearch.getUnitId()!=null ? rejectedRejectionSearch.getUnitId() : sessionService.getUnitId());
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
	public RejectedRejection rejectedRejectionMedicationById(Integer id, Integer unitId)
	{
		try
		{
			String hql = " SELECT rejectedRejection FROM RejectedRejection AS rejectedRejection "
					+ " WHERE rejectedRejection.isActive = true AND rejectedRejection.isDeleted = false AND rejectedRejection.unit.unitId = :unitId AND rejectedRejection.id = :id";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
			return (RejectedRejection) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<RejectedRejectionDetail> rejectedRejectionMedicationDetailById(Integer rejectId, Integer unitId)
	{
		try
		{
			String hql = " SELECT rejectedRejectionDetail FROM RejectedRejectionDetail AS rejectedRejectionDetail "
					+ " INNER JOIN rejectedRejectionDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
					+ " INNER JOIN rejectedRejectionDetail.rejectedRejection AS rejectedRejection WITH rejectedRejection.isActive = true AND rejectedRejection.isDeleted = false "
					+ " WHERE rejectedRejectionDetail.isActive = true AND rejectedRejectionDetail.isDeleted = false AND rejectedRejectionDetail.unit.unitId = :unitId AND rejectedRejection.id = :id";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", rejectId);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
			return query.list();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public RejectedRejectionDetail rejectedRejectionMedicationDetailByIdValue(Integer rejectDetailId, Integer unitId)
	{
		try
		{
			String hql = " SELECT rejectedRejectionDetail FROM RejectedRejectionDetail AS rejectedRejectionDetail "
					+ " INNER JOIN rejectedRejectionDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
					+ " WHERE rejectedRejectionDetail.isActive = true AND rejectedRejectionDetail.isDeleted = false AND rejectedRejectionDetail.unit.unitId = :unitId AND rejectedRejectionDetail.id = :id";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", rejectDetailId);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
			return (RejectedRejectionDetail) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override

	public RejectedRejection rejectedRejectionMedicationSave(RejectedRejection rejectedRejection)
	{
		getSession().update(rejectedRejection);
		return rejectedRejection;
		
	}

	@Override
	public void updatePatientIssueReturn(Integer id, Integer acceptQuantity) {
		Integer qty = (Integer) sessionFactory.getCurrentSession()
                .createSQLQuery(" update pharmacy.t_patient_issue_return_detail set return_qty=return_qty-:acceptQuantity "+
						"  where  is_active = true and is_deleted = false and id =:id ")
				.setParameter("acceptQuantity", acceptQuantity)
				.setParameter("id", id)
                .executeUpdate();
		
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