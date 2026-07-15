package com.param.procurement.consignment.dao.patientConsumption;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.Status;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.procurement.ImplantIndent;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionRequest;
import com.param.service.global.SessionService;

@Repository
public class PatientConsumptionDao extends AbstractDao<Integer, ImplantIndent> implements IPatientConsumptionDao {

	final static Logger logger = Logger.getLogger(PatientConsumptionDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Issue> patientConsumptionSearch(PatientConsumptionRequest request, Integer screenId) {
		try {
			String hql = " SELECT DISTINCT  issue FROM Issue AS issue "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN issue.implantIndent AS implantIndent WITH implantIndent.isActive = true AND implantIndent.isDeleted = false  "
					+ " LEFT JOIN implantIndent.implantIndentDetails AS implantIndentDetails "
					+ " LEFT JOIN implantIndentDetails.item AS item  "
					//+ " LEFT JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId AND   issue.isConsignment = true AND item.isSterilize=true AND issue.addedDate BETWEEN :fromDate AND :toDate"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName ))"
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ))"
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :store OR :store = 0 ) "
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND (:bedNo = '' OR lower(bedMaster.bedNumber) LIKE :bedNo )"
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo) "
					+ " 	AND ((:visitNo = '')"
				    + "	          OR (admission.admissionNumber = upper(:visitNo)) "
				    + "			  OR (admission.admissionNumber = lower(:visitNo)) "
				    + " 		  OR (admission.admissionNumber like upper(:visitNoLike) ) "
				    + "			  OR  (admission.admissionNumber like lower(:visitNoLike) ) )"
					+ " 	AND ((:indentNo = '')"
				    + "	          OR (implantIndent.implantIndentNo = upper(:indentNo)) "
				    + "			  OR (implantIndent.implantIndentNo = lower(:indentNo)) "
				    + " 		  OR (implantIndent.implantIndentNo like upper(:indentNoLike) ) "
				    + "			  OR  (implantIndent.implantIndentNo like lower(:indentNoLike) ) )"
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY issue.id DESC";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid", request.getUhid() != null ? request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("indentNo",request.getIndentNo()!= null ? request.getIndentNo() : "");
			query.setParameter("indentNoLike",request.getIndentNo()!= null ? request.getIndentNo() + '%' : "");
			query.setParameter("visitNo", request.getVisitNo() != null ? request.getVisitNo() : "");
			query.setParameter("visitNoLike", request.getVisitNo() != null ? request.getVisitNo() + '%' : "");
			query.setParameter("store", request.getStore() != null ? request.getStore() : 0);
			query.setParameter("wardId", request.getWardId() != null ? request.getWardId() : 0);
			query.setParameter("bedNo", request.getBedNo() != null ? request.getBedNo().toLowerCase() + '%' : "");
			query.setParameter("statusId", Status.Pending.getStatus());
			//query.setParameter("implantStatusId", Status.Pending.getStatus());
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("patientConsumptionSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	//For PAtient List Search

	@Override
	public List<Issue> patientConsumtionIssueListSearch(PatientConsumptionRequest request, Integer screenId)
	{
		try
		{
			String hql = " SELECT  issue FROM Issue AS issue "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " LEFT JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN issue.implantIndent AS implantIndent WITH implantIndent.isActive = true AND implantIndent.isDeleted = false "

					+ " LEFT JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.isImplant = true AND issue.unit.unitId = :unitId AND issue.updatedDate BETWEEN :fromDate AND :toDate"
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName)) LIKE :patientName "
					+ " OR prefix.prefixCode LIKE :patientName OR patient.firstName LIKE :patientName OR patient.middleName LIKE :patientName "
					+ "	OR patient.lastName LIKE :patientName OR prefixEncounter.prefixCode LIKE :patientName OR "
					+ "	patientEncounter.firstName LIKE :patientName OR patientEncounter.middleName LIKE :patientName "
					+ "	OR patientEncounter.lastName LIKE :patientName)) "
					
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ " AND ( status.id in(:statusClosed, :statusCompleted) ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND (:bedNo = '' OR bedMaster.bedNumber LIKE :bedNo )"
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					
					+ " 	AND ((:visitNo = '')"
				    + "	          OR (admission.admissionNumber = upper(:visitNo)) OR  (encounter.encounterNumber = upper(:visitNo)) "
				    + "			  OR (admission.admissionNumber = lower(:visitNo))  OR (encounter.encounterNumber = lower(:visitNo)) "
				    + " 		  OR (admission.admissionNumber like upper(:visitNoLike) )  OR (encounter.encounterNumber like upper(:visitNoLike) ) "
				    + "			  OR  (admission.admissionNumber like lower(:visitNoLike) )  OR  (encounter.encounterNumber like lower(:visitNoLike) ) )"
					+ " 	AND ((:issueNo = '')"
				    + "	          OR (issue.issueNo = upper(:issueNo)) "
				    + "			  OR (issue.issueNo = lower(:issueNo)) "
				    + " 		  OR (issue.issueNo like upper(:issueNoLike) ) "
				    + "			  OR  (issue.issueNo like lower(:issueNoLike) ) )"
				    + " 	AND ((:uhid = '')"
				    + "	          OR (patient.uhidNumber = upper(:uhid)) OR (patientEncounter.uhidNumber = upper(:uhid))"
				    + "			  OR (patient.uhidNumber = lower(:uhid)) OR (patientEncounter.uhidNumber = lower(:uhid)) "
				    + " 		  OR (patient.uhidNumber like upper(:uhidLike) )  OR (patientEncounter.uhidNumber like upper(:uhidLike) ) "
				    + "			  OR  (patient.uhidNumber like lower(:uhidLike) )  OR  (patientEncounter.uhidNumber like lower(:uhidLike) ) )"
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY issue.id DESC";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType",request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId",request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",request.getPatientName() != null ? request.getPatientName() + '%' : "");
			query.setParameter("uhidLike",request.getUhid()!= null ? request.getUhid() + '%' : "");
			query.setParameter("statusClosed",  Status.Closed.getStatus());
			query.setParameter("statusCompleted",  Status.Completed.getStatus());
			query.setParameter("uhid",request.getUhid()!= null ? request.getUhid() : "");
			query.setParameter("issueNo",request.getIssueNo()!= null ? request.getIssueNo() : "");
			query.setParameter("issueNoLike",request.getIssueNo()!= null ? request.getIssueNo() + '%' : "");
			query.setParameter("visitNo",request.getVisitNo()!= null ? request.getVisitNo() : "");
			query.setParameter("visitNoLike",request.getVisitNo()!= null ? request.getVisitNo()+ '%' : "");
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("wardId", request.getWardId() != null ? request.getWardId() : 0);
			query.setParameter("bedNo", request.getBedNo() != null ? request.getBedNo() + '%' : "");
			query.setParameter("screenId",screenId);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());

			return query.list();
	}
	catch(Exception e)
	{
		logger.error("patientConsumtionIssueListSearch failed", e);
		throw new ServiceException(e.getMessage());
	}
	}


	@Override
	public List<Issue> patientConsumtionConsignmentIssueListSearch(PatientConsumptionRequest request, Integer screenId)
	{
		try {
			String hql = " SELECT issue FROM Issue AS issue "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN issue.implantIndent AS implantIndent WITH implantIndent.isActive = true AND implantIndent.isDeleted = false "
					+ " LEFT JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId AND   issue.isConsignment = true AND issue.addedDate BETWEEN :fromDate AND :toDate"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName ))"
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ))"
					+ " AND ( status.id in(:statusClosed, :statusCompleted) ) "
					+ " AND ( store.storeId = :store OR :store = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY implantIndent.id DESC";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid", request.getUhid() != null ? request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo", request.getVisitNo() != null ? request.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("store", request.getStoreId() != null ? request.getStoreId() : 0);
			
			
			query.setParameter("statusClosed",  Status.Closed.getStatus());
			query.setParameter("statusCompleted",  Status.Completed.getStatus());
			
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", sessionService.getUnitId());
			
			

			return query.list();
		} catch (Exception e) {
			logger.error("patientConsumtionConsignmentIssueListSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	//for Issue List Detail first method
	
	@Override
	public Issue patientIssueById(Integer id, Integer unitId)
	{
		try
		{
			String hql = " SELECT issue FROM Issue AS issue WHERE issue.id = :id AND "
					+ " issue.isActive = true AND issue.isDeleted = false AND issue.isImplant = true AND issue.unit.unitId = :unitId ";
				
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId",unitId!=null ? unitId : sessionService.getUnitId());

			return (Issue) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error("patientIssueById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	//for Issue List Detail second method
	@Override
	public List<IssueDetail> patientIssueDetailById(Integer id,Integer unitId)
	{
		try
		{
			String hql = " SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " LEFT JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false  "
					+ " LEFT JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " LEFT JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isPharmacy = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issue.id = :id ";
		
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
	}
	catch(Exception e)
	{
		logger.error("patientIssueDetailById failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Issue patientConsumption(Integer id, Integer unitId) {
		try {
			String hql = " SELECT issue FROM Issue AS issue "
					+ "	WHERE issue.isActive = true AND issue.isDeleted = false "
					+ " AND issue.id = :id AND issue.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (Issue) query.uniqueResult();
		} catch (Exception e) {
			logger.error("patientConsumption failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<IssueDetail> getpatientConsumptionDetail(Integer id, Integer unitId) {
		try {
			String hql = " SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issue.id = :id"
					+ " AND issue.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("getpatientConsumptionDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public IssueDetail getpatientConsumptionDetails(Integer id, Integer unitId) {
		try {
			String hql = " SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issueDetail.id = :id"
					+ " AND issueDetail.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (IssueDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getpatientConsumptionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Issue updateIssue(Issue issue) {
		getSession().update(issue);
		return issue;
	}

	@Override
	public Sale saveSale(Sale sale) {
		getSession().save(sale);
		return sale;
	}


}
