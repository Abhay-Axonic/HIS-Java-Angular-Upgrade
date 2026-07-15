package com.param.pharmacy.ip.dao.patientIssueRejectionConsumable;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.patientIssueRejectionConsumable.PatientIssueRejectionConsumableSearch;

@Repository("patientIssuesRejectionConsumableDao")
public class PatientIssuesRejectionConsumableDao extends AbstractDao<Integer, Reject> implements IPatientIssuesRejectionConsumableDao {

	private static final Logger logger = Logger.getLogger(PatientIssuesRejectionConsumableDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Reject> patientIssuesRejectionConsumableSearch(PatientIssueRejectionConsumableSearch patientIssueRejectionSearch, Integer screenId)
	{
		try
		{
			String hql = "SELECT reject FROM Reject AS reject "
					+ " INNER JOIN reject.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " INNER JOIN reject.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false " 
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' " 
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " WHERE reject.isActive = true AND reject.isDeleted = false AND reject.unit.unitId = :unitId and reject.isConsumable=true "
					+ " AND reject.rejectedDate BETWEEN :fromDate AND :toDate "
					+ " AND (:visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND (:issueRejectNo = '' OR lower(reject.rejectNo) LIKE :issueRejectNo) "
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND ( bedMaster.bedNumber = :bedNo OR :bedNo = '' ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
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
			query.setParameter("visitNo",patientIssueRejectionSearch.getVisitNo()!= null ? patientIssueRejectionSearch.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("issueRejectNo",patientIssueRejectionSearch.getIssueRejectNo() != null ? '%'+patientIssueRejectionSearch.getIssueRejectNo().toLowerCase() + '%' : "");
			query.setParameter("wardId", patientIssueRejectionSearch.getWardId() != null ? '%'+patientIssueRejectionSearch.getWardId() : 0);
			query.setParameter("bedNo", patientIssueRejectionSearch.getBedNo() != null ? '%'+patientIssueRejectionSearch.getBedNo().toLowerCase() + '%' : "");
			query.setParameter("storeId", patientIssueRejectionSearch.getStoreId() != null ? patientIssueRejectionSearch.getStoreId() : 0);
			query.setParameter("uhid",patientIssueRejectionSearch.getUhid() != null ? '%'+patientIssueRejectionSearch.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId",patientIssueRejectionSearch.getUnitId()!=null ? patientIssueRejectionSearch.getUnitId() : sessionService.getUnitId());
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
					+ "	INNER JOIN rejectDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isPharmacy = false "
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
}