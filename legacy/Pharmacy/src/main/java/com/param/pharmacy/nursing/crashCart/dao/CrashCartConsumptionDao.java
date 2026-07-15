package com.param.pharmacy.nursing.crashCart.dao;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.CrashCartRequest;
import com.param.entity.model.pharmacy.CrashCartRequestDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;

@Repository
public class CrashCartConsumptionDao extends AbstractDao<Integer, CrashCartRequest>
		implements ICrashCartConsumptionDao {

	private static final Logger logger = Logger.getLogger(CrashCartConsumptionDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<CrashCartRequest> crashCartSearch(com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest request,
			Integer screenId) {
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
					+ "	AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ "	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND ( visitType.visitTypeId = 1 or ( ward.wardId in ( :wardId ))) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo) OR lower(encounter.encounterNumber) LIKE :visitNo "
					+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "								INNER JOIN screenStatus.status as status) "
					+ " ORDER BY crashCartRequest.id DESC ";
			query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("crashCartNo",request.getCrashCartNo() != null ? '%'+request.getCrashCartNo().toLowerCase() + '%' : "");
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? '%'+request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid", request.getUhid() != null ? '%'+request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo", request.getVisitNo() != null ? '%'+request.getVisitNo().toLowerCase() + '%' : "");
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

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return query.list();
	}

	@Override
	public CrashCartRequest saveCrashCart(CrashCartRequest crashCartRequest) {

		getSession().save(crashCartRequest);
		return crashCartRequest;
	}

	@Override
	public CrashCartRequest getCrashCartRequest(Integer id,Integer unitId) {
		String hql = " SELECT crashCartRequest FROM CrashCartRequest AS crashCartRequest"
				+ " WHERE crashCartRequest.isActive = true AND crashCartRequest.isDeleted = false AND crashCartRequest.id = :id"
				+ " AND crashCartRequest.unit.unitId = :unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null ? unitId :sessionService.getUnitId());

		return (CrashCartRequest) query.uniqueResult();
	}

	@Override
	public List<CrashCartRequestDetail> getCrashCartRequestDetail(Integer id) {

		String hql = "SELECT crashCartRequestDetail FROM CrashCartRequestDetail AS crashCartRequestDetail \r\n"
				+ "		INNER JOIN crashCartRequestDetail.crashCartRequest AS crashCartRequest WITH crashCartRequest.isActive = true AND crashCartRequest.isDeleted = false \r\n"
				+ "WHERE crashCartRequest.id = :id AND crashCartRequestDetail.isActive = true AND crashCartRequestDetail.isDeleted = false \r\n"
				+ "ORDER BY crashCartRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public CrashCartRequest updateCrashCartRequest(CrashCartRequest crashCartRequest) {
		getSession().update(crashCartRequest);
		return crashCartRequest;
	}

	@Override
	public Integer getDoctorId(Integer id) {

		String hql = "  select doctor_id from pharmacy.t_crash_cart_request where id="+id;
		Query query = getSession().createSQLQuery(hql);
		
		return  (Integer) query.uniqueResult();
	
	}

}
