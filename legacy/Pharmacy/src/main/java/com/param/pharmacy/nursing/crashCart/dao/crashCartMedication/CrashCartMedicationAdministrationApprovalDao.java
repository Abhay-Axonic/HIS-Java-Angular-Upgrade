package com.param.pharmacy.nursing.crashCart.dao.crashCartMedication;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.CrashCartRequest;
import com.param.global.common.ServiceException;

@Repository
public class CrashCartMedicationAdministrationApprovalDao extends AbstractDao<Integer, CrashCartRequest>
		implements ICrashCartMedicationAdministrationApprovalDao {

	private static final Logger logger = Logger.getLogger(CrashCartMedicationAdministrationApprovalDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<CrashCartRequest> crashCartMedicationSearch(
			com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest request, Integer screenId) {
		Query query = null;
		try {
			String hql = "SELECT crashCartRequest FROM CrashCartRequest AS crashCartRequest "
					+ " INNER JOIN crashCartRequest.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN crashCartRequest.patient AS patient WITH patient.status = 'A' "
					+ " INNER JOIN crashCartRequest.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN crashCartRequest.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN crashCartRequest.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN crashCartRequest.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN crashCartRequest.ward AS ward WITH ward.status = 'A' "
					+ " LEFT JOIN crashCartRequest.bed AS bed WITH bed.status = 'A' "
					+ " INNER JOIN crashCartRequest.addedBy AS addedBy WITH addedBy.status = 'A' "
					+ " INNER JOIN crashCartRequest.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " WHERE crashCartRequest.isActive = true AND crashCartRequest.isDeleted = false "
					+ " AND doctor.doctorId = :doctorId "
					+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "								INNER JOIN screenStatus.status as status) "
					+ " ORDER BY crashCartRequest.id DESC ";
			query = getSession().createQuery(hql);
			/*query.setParameter("doctorName",
					request.getDoctorName() != null ? request.getDoctorName().toLowerCase() + '%' : "");*/
			query.setParameter("screenId", screenId);
			query.setParameter("doctorId", request.getDoctorId() != null ? request.getDoctorId() : 0);
			//query.setParameter("unitId", sessionService.getUnitId());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return query.list();
	}

	@Override
	public CrashCartRequest updateCrashCasrtRequest(CrashCartRequest crashCartRequest) {
		getSession().update(crashCartRequest);
		return crashCartRequest;
	}

}
