package com.param.pharmacy.ip.dao.returnAcceptance.medication;

import java.math.BigInteger;
import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.Reject;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceRequest;

@Repository("returnAcceptanceMedicationDao")
public class ReturnAcceptanceMedicationDao extends AbstractDao<Integer, PatientIssueReturn> implements IReturnAcceptanceMedicationDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(ReturnAcceptanceMedicationDao.class);

	@Override
	public List<PatientIssueReturn> returnAcceptanceSearch(ReturnAcceptanceRequest returnAcceptanceRequest, Integer screenId)
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
					+ " WHERE patientIssueReturn.unit.unitId = :unitId AND patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false  and patientIssueReturn.fromMedication=true "
					+ " AND patientIssueReturn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName " 
					+ "	OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid )"
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND (:statusId = 0 OR status.id = :statusId) "
					+ " AND( :returnNo = '' OR lower(patientIssueReturn.returnNo) LIKE :returnNo) "
					+ " AND  status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY patientIssueReturn.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(returnAcceptanceRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(returnAcceptanceRequest.getToDate()));
			query.setParameter("returnNo", returnAcceptanceRequest.getReturnNo() != null ? '%'+returnAcceptanceRequest.getReturnNo().toLowerCase() + '%' : "");
			query.setParameter("visitType",returnAcceptanceRequest.getVisitType() != null ? returnAcceptanceRequest.getVisitType() : 0);
			query.setParameter("patientId",returnAcceptanceRequest.getPatientId() != null ? returnAcceptanceRequest.getPatientId() : 0);
			query.setParameter("patientName",returnAcceptanceRequest.getPatientName() != null ? '%'+returnAcceptanceRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",returnAcceptanceRequest.getDoctorName()!= null ? '%'+returnAcceptanceRequest.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("wardId",returnAcceptanceRequest.getWardId() != null ? returnAcceptanceRequest.getWardId() : 0);
			query.setParameter("uhid",returnAcceptanceRequest.getUhid() != null ? '%'+returnAcceptanceRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", returnAcceptanceRequest.getUnitId()!=null ? returnAcceptanceRequest.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("statusId",returnAcceptanceRequest.getStatusId()!=null ? returnAcceptanceRequest.getStatusId() : 0 );
		return query.list();
	}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	
	}
	
	@Override
	public PatientIssueReturn updatePatientIssueReturn(PatientIssueReturn patientIssueReturn)
	{
		getSession().update(patientIssueReturn);
		return patientIssueReturn;
	}
	
	@Override
	public Reject saveRejectDetails(Reject reject)
	{
		getSession().save(reject);
		return reject;
	}

	@Override
	public StoreStock saveStoreStock(StoreStock storeStock) {

		getSession().save(storeStock);
		return storeStock;
	
		
	}

	@Override
	public BigInteger getSaleId(Integer id) {
		String sql=" select ts.id from pharmacy.t_patient_issue_return_detail tpird " + 
				" inner join pharmacy.t_issue_detail_batch_mapper tidbm on tpird.issue_batch_mapper_id =tidbm.id " + 
				" inner join pharmacy.t_issue_detail tid on tidbm.issue_detail_id = tid.id " + 
				" inner join pharmacy.t_sale_detail tsd on tsd.id=tid.sale_detail_id " + 
				" inner join pharmacy.t_sale ts on ts.id =tsd.sale_id where tpird.id =:Id ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("Id", id);
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public String getDoctorName(BigInteger saleId) {
		String sql =" select coalesce(concat(mdm.first_name, ' ', mdm.middle_name, ' ', mdm.last_name)) " + 
				" from  pharmacy.t_sale_detail tsd " + 
				" inner join pharmacy.t_sale ts on ts.id =tsd.sale_id " + 
				" inner join doctor.m_doctor_master mdm on mdm.doctor_id =ts.doctor_id where tsd.sale_id =:Id limit 1 ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("Id", saleId);
		return (String) query.uniqueResult();
	}
}