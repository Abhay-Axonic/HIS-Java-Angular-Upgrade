package com.param.procurement.consignment.dao.implantIndent;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.UnitOfMeasurement;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.procurement.ImplantIndent;
import com.param.entity.model.procurement.ImplantIndentDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.service.global.SessionService;

@Repository
public class ImplantIndentDao extends AbstractDao<Integer, ImplantIndent> implements IImplantIndentDao {

	final static Logger logger = Logger.getLogger(ImplantIndentDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ImplantIndent> searchImplantIndent(ImplantIndentRequest request, Integer screenId) {
		try {
			String hql = " SELECT implantIndent FROM ImplantIndent AS implantIndent "
					+ " INNER JOIN implantIndent.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN implantIndent.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN implantIndent.doctor AS doctor WITH doctor.status = 'A' "
					//+ " LEFT JOIN implantIndent.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ " LEFT JOIN implantIndent.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN implantIndent.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN implantIndent.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN implantIndent.indentType AS indentType  "
					+ " INNER JOIN implantIndent.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN implantIndent.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
					+ " WHERE implantIndent.isActive = true AND implantIndent.isDeleted = false AND "
					+ " implantIndent.addedDate BETWEEN :fromDate AND :toDate AND implantIndent.unit.unitId = :unitId "
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR "
					+ " lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :store OR :store = 0 ) "
					+ " AND ( indentType.id = :indentType OR :indentType = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( :implantIndentNo = '' OR implantIndent.implantIndentNo LIKE :implantIndentNo ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo )"
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY implantIndent.id DESC ";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("indentType", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
			query.setParameter("store", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("implantIndentNo", request.getImplantIndentNo() != null ? request.getImplantIndentNo() + '%' : "");
		//	query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid", request.getUhid() != null ? request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo", request.getVisitNo() != null ? request.getVisitNo().toLowerCase() + '%' : "");
		//	query.setParameter("store", request.getStore() != null ? request.getStore() : 0);
			query.setParameter("doctorName",
					request.getDoctorName() != null ? request.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);

			return query.list();
		} catch (Exception e) {
			logger.error("searchImplantIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ImplantIndent saveImplantIndent(ImplantIndent implantIndent) {
		getSession().save(implantIndent);
		return implantIndent;
	}

	@Override
	public ImplantIndent implantIndentById(Integer id, Integer unitId) {
		try {
			String hql = " SELECT implantIndent FROM ImplantIndent AS implantIndent"
					+ " WHERE implantIndent.isActive = true AND implantIndent.isDeleted = false AND implantIndent.id = :id"
					+ " AND implantIndent.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!= null ? unitId:sessionService.getUnitId());

			return (ImplantIndent) query.uniqueResult();
		} catch (Exception e) {
			logger.error("implantIndentById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ImplantIndentDetail> implantIndentDetailById(Integer id) {
		try {
			String hql = " SELECT implantIndentDetail FROM ImplantIndentDetail AS implantIndentDetail "
					+ " INNER JOIN implantIndentDetail.implantIndent AS implantIndent WITH implantIndent.isActive = true AND implantIndent.isDeleted = false "
					+ " WHERE implantIndentDetail.isActive = true AND implantIndentDetail.isDeleted = false AND implantIndent.id = :id"
					+ " AND implantIndent.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("implantIndentDetailById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ImplantIndent updateImplantIndent(ImplantIndent implantIndent) {
		getSession().update(implantIndent);
		return implantIndent;
	}

	@Override
	public Issue pendingIssuesSave(Issue issue) {
		getSession().save(issue);
		return issue;
	}
	
	@Override
	public List<UnitOfMeasurement> getAllByItemId(Integer itemId) {
		String hql = "SELECT uom  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ " WHERE uom.item.id = :itemId AND uom.isDeleted = false AND uom.isActive = true"
				+ " ORDER BY uom.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("itemId",itemId);
		return query.list();
	}
}
