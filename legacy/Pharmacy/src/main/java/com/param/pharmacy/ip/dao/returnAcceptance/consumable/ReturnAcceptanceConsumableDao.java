package com.param.pharmacy.ip.dao.returnAcceptance.consumable;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.Reject;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceRequest;

@Repository("returnAcceptanceConsumableDao")
public class ReturnAcceptanceConsumableDao extends AbstractDao<Integer, PatientIssueReturn> implements IReturnAcceptanceConsumableDao {

	private static final Logger logger = Logger.getLogger(ReturnAcceptanceConsumableDao.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<PatientIssueReturn> returnAcceptanceSearch(ReturnAcceptanceRequest patientIssueReturnRequest, Integer screenId)
	{
		try
		{
			String hql = " SELECT distinct patientIssueReturn FROM PatientIssueReturn AS patientIssueReturn "
					+ " INNER JOIN patientIssueReturn.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " INNER JOIN patientIssueReturn.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN patientIssueReturn.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN patientIssueReturn.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN patientIssueReturn.patientIssueReturnDetail AS patientIssueReturnDetail WITH patientIssueReturnDetail.isActive = true AND patientIssueReturnDetail.isDeleted = false "
					+ " INNER JOIN patientIssueReturnDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " WHERE patientIssueReturn.unit.unitId = :unitId AND patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false and patientIssueReturn.fromMedication=false "
					+ " AND patientIssueReturn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND (:visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid )"
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND (:statusId = 0 OR status.id = :statusId) "
					+ " AND( :returnNo = '' OR lower(patientIssueReturn.returnNo) LIKE :returnNo) "
					+ " AND( :bedNo = '' OR lower(bedMaster.bedNumber) LIKE :bedNo) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY patientIssueReturn.id DESC ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
			query.setParameter("visitType",patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
			query.setParameter("patientId",patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
			query.setParameter("patientName",patientIssueReturnRequest.getPatientName() != null ? '%'+patientIssueReturnRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",patientIssueReturnRequest.getUhid()!= null ? '%'+patientIssueReturnRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",patientIssueReturnRequest.getVisitNo()!= null ?'%'+ patientIssueReturnRequest.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("returnNo",patientIssueReturnRequest.getReturnNo()!= null ? '%'+patientIssueReturnRequest.getReturnNo().toLowerCase() + '%' : "");
			query.setParameter("wardId",patientIssueReturnRequest.getWardId() != null ? patientIssueReturnRequest.getWardId() : 0);
			query.setParameter("bedNo",patientIssueReturnRequest.getBedNo()!= null ? '%'+patientIssueReturnRequest.getBedNo().toLowerCase() + '%' : "");
			query.setParameter("screenId", screenId);
			query.setParameter("statusId",patientIssueReturnRequest.getStatusId()!=null ? patientIssueReturnRequest.getStatusId() : 0 );
			query.setParameter("unitId", patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId() : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public PatientIssueReturn updatePatientIssueReturn(PatientIssueReturn patientIssueReturn) {

		getSession().update(patientIssueReturn);
		return patientIssueReturn;
	
	}

	@Override
	public Reject saveRejectDetails(Reject reject) {

		getSession().save(reject);
		return reject;
	
	}
	
}