package com.param.pharmacy.nursing.pharmacy.dao.patientIndent;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.PatientIndentDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentRequest;

@Repository("patientIndentDao")
public class PatientIndentDao extends AbstractDao<Integer, PatientIndent> implements IPatientIndentDao {

	private static final Logger logger = Logger.getLogger(PatientIndentDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<PatientIndent> patientIndentSearch(PatientIndentRequest patientIndentReuest, Integer screenId) {
		try {
			String hql = " SELECT patientIndent FROM PatientIndent AS patientIndent "
					+ " INNER JOIN patientIndent.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "	LEFT JOIN patientIndent.toStore as toStore WITH toStore.isActive = true AND toStore.isDeleted = false  "
					+ " INNER JOIN patientIndent.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientIndent.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN patientIndent.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN patientIndent.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN patientIndent.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN patientIndent.ward AS ward WITH ward.status = 'A' "
					+ " INNER JOIN patientIndent.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN patientIndent.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
					+ " LEFT JOIN patientIndent.bed AS bed WITH bed.status = 'A' "
					+ " LEFT JOIN bed.floor AS floor WITH floor.status = 'A' "
					+ " WHERE patientIndent.isActive = true AND patientIndent.isDeleted = false AND "
					+ " patientIndent.addedDate BETWEEN :fromDate AND :toDate AND patientIndent.unit.unitId = :unitId "
					+ " AND ( :doctorId = 0 OR  doctor.doctorId =:doctorId ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) " + " AND ( floor.id = :floor OR :floor = 0 ) "
					+ " AND( visitType.visitTypeId =:opVisitType or ( ward.wardId in ( :ward ))) " + " AND ( status.id = :statusId OR :statusId = 0 ) "
					// + " AND ( store.storeId = :store OR :store = 0 ) "
					+ " 	AND (:fromStoreId = 0 OR store.storeId = :fromStoreId) "
					+ " 	AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId "
					+ "			AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
					+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
					+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo )"
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY patientIndent.id DESC ";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(patientIndentReuest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIndentReuest.getToDate()));
			query.setParameter("statusId",
					patientIndentReuest.getStatusId() != null ? patientIndentReuest.getStatusId() : 0);
			query.setParameter("fromStoreId",
					patientIndentReuest.getFromStoreId() != null ? patientIndentReuest.getFromStoreId() : 0);
			query.setParameter("toStoreId",
					patientIndentReuest.getToStoreId() != null ? patientIndentReuest.getToStoreId() : 0);
			query.setParameter("visitType",
					patientIndentReuest.getVisitType() != null ? patientIndentReuest.getVisitType() : 0);
			query.setParameter("patientId",
					patientIndentReuest.getPatientId() != null ? patientIndentReuest.getPatientId() : 0);
			query.setParameter("patientName",
					patientIndentReuest.getPatientName() != null
							? '%'+patientIndentReuest.getPatientName().toLowerCase() + '%'
							: "");
			query.setParameter("uhid",
					patientIndentReuest.getUhid() != null ? '%'+patientIndentReuest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",
					patientIndentReuest.getVisitNo() != null ? '%'+patientIndentReuest.getVisitNo().toLowerCase() + '%'
							: "");
			// query.setParameter("store", patientIndentReuest.getStore() != null ?
			// patientIndentReuest.getStore() : 0);
			query.setParameter("floor", patientIndentReuest.getFloor() != null ? patientIndentReuest.getFloor() : 0);
			query.setParameter("doctorId",
					patientIndentReuest.getDoctorId() != null ? patientIndentReuest.getDoctorId() : 0);
			List<Integer> wardList = new ArrayList<>();
            Integer opVisitType=1;
			if(patientIndentReuest.getWardList()!=null && patientIndentReuest.getWardList().size()>0) {
				wardList.addAll(patientIndentReuest.getWardList());
			}else {
				wardList.add(0);
			}
            if(patientIndentReuest.getWardList().size()>1 && patientIndentReuest.getVisitType() == null)
            {
                opVisitType=1;
            }
            else if(patientIndentReuest.getVisitType()!=null &&  patientIndentReuest.getVisitType()== 1)
            {
                opVisitType=patientIndentReuest.getVisitType();
                wardList.add(0);
            }
            else
            {
                opVisitType=0;
            }
            query.setParameter("opVisitType", opVisitType);
			query.setParameterList("ward", wardList);
//			query.setParameterList("ward", patientIndentReuest.getWardList());
			query.setParameter("unitId", patientIndentReuest.getUnitId() != null ? patientIndentReuest.getUnitId()
					: sessionService.getUnitId());
			query.setParameter("userId", patientIndentReuest.getUserId() != null ? patientIndentReuest.getUserId()
					: sessionService.getUserId());
			query.setParameter("screenId", screenId);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PatientIndent patientIndentById(Integer id, Integer unitId) {

		try {
			String hql = " SELECT patientIndent FROM PatientIndent AS patientIndent"
					+ " WHERE patientIndent.isActive = true AND patientIndent.isDeleted = false AND patientIndent.id = :id"
					+ " AND patientIndent.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

			return (PatientIndent) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<PatientIndentDetail> patientIndentDetailById(Integer id, Integer unitId) {
		try {
			String hql = " SELECT patientIndentDetail FROM PatientIndentDetail AS patientIndentDetail "
					+ " INNER JOIN patientIndentDetail.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " WHERE patientIndentDetail.isActive = true AND patientIndentDetail.isDeleted = false AND patientIndent.id = :id"
					+ " AND patientIndent.unit.unitId = :unitId AND patientIndentDetail.status.id >0 ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PatientIndent savePatientIndent(PatientIndent patientIndent) {
		getSession().save(patientIndent);
		return patientIndent;
	}

	@Override
	public PatientIndent updatePatientIndent(PatientIndent patientIndent) {
		getSession().update(patientIndent);
		return patientIndent;
	}

	@Override
	public List<PatientIndentDetail> patientPreviousIndentDetails(PatientIndentRequest patientIndentReuest) {
		try {
			String hql = "SELECT patientIndentDetail FROM PatientIndentDetail AS patientIndentDetail INNER JOIN patientIndentDetail.patientIndent AS patientIndent "
					+ " INNER JOIN patientIndent.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "	LEFT JOIN patientIndent.toStore as toStore WITH toStore.isActive = true AND toStore.isDeleted = false  "
					+ " INNER JOIN patientIndent.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientIndent.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN patientIndent.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN patientIndent.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN patientIndent.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN patientIndent.ward AS ward WITH ward.status = 'A' "
					+ " INNER JOIN patientIndent.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN patientIndent.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
					+ " LEFT JOIN patientIndent.bed AS bed WITH bed.status = 'A' "
					+ " LEFT JOIN bed.floor AS floor WITH floor.status = 'A' "
					+ " WHERE patientIndent.isActive = true AND patientIndent.isDeleted = false AND "
					+ "  patientIndent.unit.unitId = :unitId "
					+ " AND ( :doctorId = 0 OR  doctor.doctorId =:doctorId ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) " + " AND ( floor.id = :floor OR :floor = 0 ) "
					+ " AND ( ward.wardId = :ward OR :ward = 0 ) " + " AND ( status.id = :statusId OR :statusId = 0 ) "
					// + " AND ( store.storeId = :store OR :store = 0 ) "
					+ " 	AND (:fromStoreId = 0 OR store.storeId = :fromStoreId) "
					+ " 	AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId "
					+ "			AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
					+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
					+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo )"
					+ " ORDER BY patientIndent.id DESC ";
			Query query = getSession().createQuery(hql);
		//	query.setParameter("fromDate", Util.getDate(patientIndentReuest.getFromDate()));
		//	query.setParameter("toDate", Util.getDate(patientIndentReuest.getToDate()));
			query.setParameter("statusId",
					patientIndentReuest.getStatusId() != null ? patientIndentReuest.getStatusId() : 0);
			query.setParameter("fromStoreId",
					patientIndentReuest.getFromStoreId() != null ? patientIndentReuest.getFromStoreId() : 0);
			query.setParameter("toStoreId",
					patientIndentReuest.getToStoreId() != null ? patientIndentReuest.getToStoreId() : 0);
			query.setParameter("visitType",
					patientIndentReuest.getVisitType() != null ? patientIndentReuest.getVisitType() : 0);
			query.setParameter("patientId",
					patientIndentReuest.getPatientId() != null ? patientIndentReuest.getPatientId() : 0);
			query.setParameter("patientName",
					patientIndentReuest.getPatientName() != null
							? patientIndentReuest.getPatientName().toLowerCase() + '%'
							: "");
			query.setParameter("uhid",
					patientIndentReuest.getUhid() != null ? patientIndentReuest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",
					patientIndentReuest.getVisitNo() != null ? patientIndentReuest.getVisitNo().toLowerCase() + '%'
							: "");
			// query.setParameter("store", patientIndentReuest.getStore() != null ?
			// patientIndentReuest.getStore() : 0);
			query.setParameter("floor", patientIndentReuest.getFloor() != null ? patientIndentReuest.getFloor() : 0);
			query.setParameter("doctorId",
					patientIndentReuest.getDoctorId() != null ? patientIndentReuest.getDoctorId() : 0);
			query.setParameter("ward", patientIndentReuest.getWard() != null ? patientIndentReuest.getWard() : 0);
			query.setParameter("unitId", patientIndentReuest.getUnitId() != null ? patientIndentReuest.getUnitId()
					: sessionService.getUnitId());
			query.setParameter("userId", patientIndentReuest.getUserId() != null ? patientIndentReuest.getUserId()
					: sessionService.getUserId()); 

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PatientIndent> patientPreviousIndent(PatientIndentRequest patientIndentReuest) {

		try {
			String hql = " SELECT patientIndent FROM PatientIndent AS patientIndent "
					+ " INNER JOIN patientIndent.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "	LEFT JOIN patientIndent.toStore as toStore WITH toStore.isActive = true AND toStore.isDeleted = false  "
					+ " INNER JOIN patientIndent.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientIndent.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN patientIndent.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN patientIndent.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN patientIndent.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN patientIndent.ward AS ward WITH ward.status = 'A' "
					+ " INNER JOIN patientIndent.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN patientIndent.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
					+ " LEFT JOIN patientIndent.bed AS bed WITH bed.status = 'A' "
					+ " LEFT JOIN bed.floor AS floor WITH floor.status = 'A' "
					+ " WHERE patientIndent.isActive = true AND patientIndent.isDeleted = false AND "
					+ "   patientIndent.unit.unitId = :unitId "
					+ " AND ( :doctorId = 0 OR  doctor.doctorId =:doctorId ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) " + " AND ( floor.id = :floor OR :floor = 0 ) "
					+ " AND ( ward.wardId = :ward OR :ward = 0 ) " + " AND ( status.id = :statusId OR :statusId = 0 ) "
					// + " AND ( store.storeId = :store OR :store = 0 ) "
					+ " 	AND (:fromStoreId = 0 OR store.storeId = :fromStoreId) "
					+ " 	AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId "
					+ "			AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
					+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
					+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo )"
					+ " ORDER BY patientIndent.id DESC ";
			Query query = getSession().createQuery(hql);
			//query.setParameter("fromDate", Util.getDate(patientIndentReuest.getFromDate()));
		//	query.setParameter("toDate", Util.getDate(patientIndentReuest.getToDate()));
			query.setParameter("statusId",
					patientIndentReuest.getStatusId() != null ? patientIndentReuest.getStatusId() : 0);
			query.setParameter("fromStoreId",
					patientIndentReuest.getFromStoreId() != null ? patientIndentReuest.getFromStoreId() : 0);
			query.setParameter("toStoreId",
					patientIndentReuest.getToStoreId() != null ? patientIndentReuest.getToStoreId() : 0);
			query.setParameter("visitType",
					patientIndentReuest.getVisitType() != null ? patientIndentReuest.getVisitType() : 0);
			query.setParameter("patientId",
					patientIndentReuest.getPatientId() != null ? patientIndentReuest.getPatientId() : 0);
			query.setParameter("patientName",
					patientIndentReuest.getPatientName() != null
							? patientIndentReuest.getPatientName().toLowerCase() + '%'
							: "");
			query.setParameter("uhid",
					patientIndentReuest.getUhid() != null ? patientIndentReuest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",
					patientIndentReuest.getVisitNo() != null ? patientIndentReuest.getVisitNo().toLowerCase() + '%'
							: "");
			// query.setParameter("store", patientIndentReuest.getStore() != null ?
			// patientIndentReuest.getStore() : 0);
			query.setParameter("floor", patientIndentReuest.getFloor() != null ? patientIndentReuest.getFloor() : 0);
			query.setParameter("doctorId",
					patientIndentReuest.getDoctorId() != null ? patientIndentReuest.getDoctorId() : 0);
			query.setParameter("ward", patientIndentReuest.getWard() != null ? patientIndentReuest.getWard() : 0);
			query.setParameter("unitId", patientIndentReuest.getUnitId() != null ? patientIndentReuest.getUnitId()
					: sessionService.getUnitId());
			query.setParameter("userId", patientIndentReuest.getUserId() != null ? patientIndentReuest.getUserId()
					: sessionService.getUserId()); 

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}
}