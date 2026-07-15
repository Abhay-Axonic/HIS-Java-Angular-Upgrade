package com.param.pharmacy.nursing.crashCart.dao.crashCartApproval;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.CrashCartRequest;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;

@Repository
public class CrashCartApprovalDao extends AbstractDao<Integer, CrashCartRequest> implements ICrashCartApprovalDao {

	@Autowired
	private SessionService sessionService;

	private static final Logger logger = Logger.getLogger(CrashCartApprovalDao.class);

	@Override
	public List<CrashCartRequest> crashCartApprovalSearch(
			com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest request, Integer screenId) {

		Query query = null;
		try {
			String hql = "SELECT crashCartRequest FROM CrashCartRequest AS crashCartRequest "
					+ " INNER JOIN crashCartRequest.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN crashCartRequest.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN crashCartRequest.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN crashCartRequest.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN crashCartRequest.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN crashCartRequest.ward AS ward WITH ward.status = 'A' "
					+ " LEFT JOIN crashCartRequest.bed AS bed WITH bed.status = 'A' "
					+ " INNER JOIN crashCartRequest.addedBy AS addedBy WITH addedBy.status = 'A' "
					+ " INNER JOIN crashCartRequest.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " WHERE crashCartRequest.isActive = true AND crashCartRequest.isDeleted = false AND "
					+ " crashCartRequest.addedDate BETWEEN :fromDate AND :toDate AND crashCartRequest.unit.unitId = :unitId "
					+ " AND ( :crashCartNo = '' OR lower(crashCartRequest.crashCartNo) LIKE :crashCartNo )"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND ( visitType.visitTypeId = 1 or ( ward.wardId in ( :wardId ))) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND (:visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo) OR lower(encounter.encounterNumber) LIKE :visitNo "
					+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "								INNER JOIN screenStatus.status as status) "
					+ " ORDER BY crashCartRequest.id DESC ";
			query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? '%'+request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid", request.getUhid() != null ? '%'+request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo", request.getVisitNo() != null ? '%'+request.getVisitNo().trim().toLowerCase() + '%' : "");
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			List<Integer> wardList = new ArrayList<>();
			if(request.getWardList()!=null && request.getWardList().size()>0) {
				wardList.addAll(request.getWardList());
			}else {
				wardList.add(0);
			}
			query.setParameterList("wardId", wardList);
//			query.setParameterList("wardId", request.getWardList());
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("crashCartNo",request.getCrashCartNo() != null ? '%'+request.getCrashCartNo().trim().toLowerCase()+'%' : "");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return query.list();
	}

	@Override
	public CrashCartRequest saveCrashCartRequestApproval(CrashCartRequest crashCartRequest) {
		getSession().update(crashCartRequest);
		return crashCartRequest;
	}

}
