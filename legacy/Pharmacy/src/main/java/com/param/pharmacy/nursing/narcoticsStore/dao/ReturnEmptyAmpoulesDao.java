package com.param.pharmacy.nursing.narcoticsStore.dao;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoulesDetail;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonSearchRequest;
import com.param.global.utility.Util;

@Repository("returnEmptyAmpoulesDao")
public class ReturnEmptyAmpoulesDao extends AbstractDao<Integer, Issue> implements IReturnEmptyAmpoulesDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(ReturnEmptyAmpoulesDao.class);

	@Override
	public List<ReturnOfEmptyAmpoules> returnEmptyAmpoulesSearch(CommonSearchRequest request, Integer screenId) {
		try {
			String hql = " SELECT returnOfEmptyAmpoules FROM ReturnOfEmptyAmpoules AS returnOfEmptyAmpoules "
					+ " INNER JOIN returnOfEmptyAmpoules.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " INNER JOIN issue.prescription AS prescription  "
					+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A' "
					+ " INNER JOIN issue.sale AS sale  "
					+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
					// + " LEFT JOIN admission.admissionDetail AS admissionDetail WITH
					// admissionDetail.status = 'A' "
					+ " LEFT JOIN returnOfEmptyAmpoules.ward AS ward WITH ward.status = 'A' "
					+ " LEFT JOIN returnOfEmptyAmpoules.bed AS bed WITH bed.status = 'A' "
					// + " LEFT JOIN bedMaster.floor AS floor WITH floor.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN returnOfEmptyAmpoules.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
					+ " INNER JOIN returnOfEmptyAmpoules.status AS status WITH status.isActive = true AND status.isDeleted = false "
					
					+ " INNER JOIN returnOfEmptyAmpoules.priority AS priority WITH priority.status = 'A' "
					+ " WHERE returnOfEmptyAmpoules.isActive = true"
					+ " AND issue.isNarcotics = true "
					+ " AND returnOfEmptyAmpoules.isDeleted = false AND returnOfEmptyAmpoules.unit.unitId = :unitId AND returnOfEmptyAmpoules.updatedDate BETWEEN :fromDate AND :toDate "
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR "
					+ " lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
				    + "  AND store.isNarcotic = true  AND store.storeId =:storeId "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName "
					+ "OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ( store.isNarcotic=true ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid "
					+ " OR lower(patientEncounter.uhidNumber) LIKE :uhid) "
					+ " AND( ward.wardId in ( :wardId )) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY returnOfEmptyAmpoules.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",
					request.getDoctorName() != null ? request.getDoctorName().toLowerCase() + '%' : "");
			List<Integer> wardList = new ArrayList<>();
			if(request.getWardList()!=null && request.getWardList().size()>0) {
				wardList.addAll(request.getWardList());
			}else {
				wardList.add(0);
			}
			query.setParameterList("wardId", wardList);
//			query.setParameterList("wardId", request.getWardList());
			//query.setParameter("statusId", request.getDoctorName() != null ? request.getDoctorName() : 0);
			query.setParameter("uhid", request.getUhid() != null ? request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("storeId", request.getStoreId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public ReturnOfEmptyAmpoules returnEmptyAmpoulesById(Integer id) {
		try {
			String hql = "  SELECT returnOfEmptyAmpoules FROM ReturnOfEmptyAmpoules AS returnOfEmptyAmpoules "
					+ " WHERE returnOfEmptyAmpoules.isActive = true AND returnOfEmptyAmpoules.isDeleted = false "
					+ " AND returnOfEmptyAmpoules.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (ReturnOfEmptyAmpoules) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * @Override public List<IssueDetail> returnEmptyAmpoulesDetails(Integer
	 * issueId) { try { String hql =
	 * "  SELECT issueDetail FROM IssueDetail AS issueDetail " +
	 * " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
	 * +
	 * " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
	 * +
	 * " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
	 * + " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false " +
	 * " AND issue.id = :id ";
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("id",
	 * issueId);
	 * 
	 * return query.list(); } catch (Exception e) {  } return
	 * null; }
	 */

	@Override
	public List<ReturnOfEmptyAmpoulesDetail> returnEmptyAmpoulesDetails(Integer issueId) {
		try {
			String hql = "  SELECT returnOfEmptyAmpoulesDetail FROM ReturnOfEmptyAmpoulesDetail AS returnOfEmptyAmpoulesDetail "
					+ " INNER JOIN returnOfEmptyAmpoulesDetail.issueBatchMapper AS issueBatchMapper WITH issueBatchMapper.isActive = true AND issueBatchMapper.isDeleted = false "
					+ " INNER JOIN issueBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
					+ " INNER JOIN returnOfEmptyAmpoulesDetail.returnOfEmptyAmpoules AS returnOfEmptyAmpoules WITH returnOfEmptyAmpoules.isActive = true AND returnOfEmptyAmpoules.isDeleted = false "
					+ " WHERE returnOfEmptyAmpoulesDetail.isActive = true AND returnOfEmptyAmpoulesDetail.isDeleted = false "
					+ " AND returnOfEmptyAmpoules.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueId);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ReturnOfEmptyAmpoules returnEmptyAmpoulesSave(ReturnOfEmptyAmpoules returnOfEmptyAmpoules) {
		getSession().save(returnOfEmptyAmpoules);
		return returnOfEmptyAmpoules;
	}

	@Override
	public IssueDetail returnEmptyAmpoulesDetailById(Integer issueDetailId) {
		try {
			String hql = "  SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " AND issueDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueDetailId);

			return (IssueDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ReturnOfEmptyAmpoules updateReturnOfEmptyAmpoules(ReturnOfEmptyAmpoules returnOfEmptyAmpoules) {
		getSession().update(returnOfEmptyAmpoules);
		return returnOfEmptyAmpoules;
	}

}