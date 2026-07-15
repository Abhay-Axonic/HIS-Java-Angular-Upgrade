package com.param.pharmacy.inPatient.dao.patientConsumableIssueList;

import java.util.ArrayList;
import java.util.List;

import com.param.common.constants.Status;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsRequestForDrugRecall;
import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListRequest;

@Repository("patientConsumableIssueListDao")
public class PatientConsumableIssueListDao extends AbstractDao<Integer, Issue> implements IPatientConsumableIssueListDao {

	private static final Logger logger = Logger.getLogger(PatientConsumableIssueListDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Issue> patientConsumableIssueListSearch(PatientConsumableIssueListRequest request, Integer screenId)
	{
		try
		{
			// HIS-10262
			List<Integer> ids=new ArrayList<Integer>();
			ids.add(com.param.common.constants.Status.Completed.getStatus());
			ids.add(com.param.common.constants.Status.PartiallyCompleted.getStatus());
			ids.add(com.param.common.constants.Status.Closed.getStatus());
			
			String hql = " SELECT issue FROM Issue AS issue "
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN issue.toStore AS toStore WITH toStore.isActive = true AND toStore.isDeleted = false "
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false  AND issue.isImplant = false AND issue.unit.unitId = :unitId AND issue.addedDate BETWEEN :fromDate AND :toDate"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( toStore.storeId = :toStoreId OR :toStoreId = 0 ) "
					+ " AND ( status.id in ( :ids)  ) "
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND (:bedNo = '' OR lower(bedMaster.bedNumber) LIKE :bedNo )"
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) " 
					+ " ORDER BY issue.id DESC";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType",request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId",request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",request.getPatientName() != null ?'%'+ request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",request.getUhid()!= null ? '%'+request.getUhid().toLowerCase() + '%' : "");
            query.setParameter("issueNo", request.getIssueNo()!= null ? '%'+ request.getIssueNo().toLowerCase() + '%' : "");
			query.setParameter("visitNo",request.getVisitNo()!= null ? request.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
			query.setParameter("wardId", request.getWardId() != null ? request.getWardId() : 0);
			query.setParameter("bedNo", request.getBedNo() != null ? '%'+request.getBedNo().toLowerCase() + '%' : "");
			query.setParameterList("ids", ids);
		//	query.setParameter("screenId",screenId);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());

			return query.list();
	}
	catch(Exception e) {
		throw e;
	}
//		return null;
	}

	@Override
	public List<Issue> patientConsumableIssueListSearchForDrugRecall(PatientDetailsRequestForDrugRecall request, Integer screenId) {
		try
		{
			String hql = " SELECT issue FROM Issue AS issue "
					+ " LEFT JOIN issue.issueDetail AS issueDetail"
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN issueDetail.issueDetailBatchMapper batchMapper"
					+ " LEFT JOIN batchMapper.batch as batch"
					+ " LEFT JOIN batch.item as item"
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.isImplant = false AND issue.unit.unitId = :unitId"
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( batch.id = :batchId OR :batchId = 0 ) "
					+ " AND ( item.id = :itemId OR :itemId = 0 ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY issue.id DESC";
			Query query = getSession().createQuery(hql);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("batchId", request.getBatchId() != null ? Long.parseLong(request.getBatchId().toString()) : 0L);
			query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
			//query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("statusId", com.param.common.constants.Status.Completed.getStatus());
			query.setParameter("screenId",screenId);
			query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Issue patientIssueById(Integer id,Integer unitId)
	{
		try
		{
			String hql = " SELECT issue FROM Issue AS issue WHERE issue.id = :id AND "
					+ " issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId ";
				
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (Issue) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<IssueDetail> patientIssueDetailById(Integer id)
	{
		try
		{
			String hql = " SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isPharmacy = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issue.id = :id ";
		
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
}