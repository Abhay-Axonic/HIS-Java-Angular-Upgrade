package com.param.pharmacy.nursing.discharge.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;

@Repository
public class MedicationReconciliationDao extends AbstractDao<Integer, Prescription> implements IMedicationReconciliationDao {

	private static final Logger logger = Logger.getLogger(MedicationReconciliationDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<PrescriptionDetail> medicationReconciliationSearch(MedicationReconciliationSearchRequest request, Integer screenId) {
		try
		{
			String hql = " SELECT distinct prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH   prescription.isActive = true AND prescription.isDeleted = false "
					+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A' "
					+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ " LEFT JOIN prescription.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN bedMaster.floor AS floor WITH floor.status = 'A' "
				//	+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
				//	+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				//	+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN prescription.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
					+ " left outer  JOIN prescription.reconciliationStatus AS reconciliationStatus WITH reconciliationStatus.isActive = true AND reconciliationStatus.isDeleted = false "
				//	+ " INNER JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				//	+ " INNER JOIN prescription.priority AS priority WITH priority.status = 'A' "
					+ " WHERE prescriptionDetail.isDischarge = true and prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false AND prescription.unit.unitId = :unitId AND prescription.updatedDate BETWEEN :fromDate AND :toDate "
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR "
					+ " lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)" 
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				//	+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName)) "
					//+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					//+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ( reconciliationStatus.id = :statusId OR :statusId = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND( wardMaster.floorId = :floorId OR :floorId = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND prescriptionDetail.isNonFormulary = 'N' "
					//+ " AND reconciliationStatus.id IN (SELECT status.id FROM ScreenStatus AS screenStatus INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId  INNER JOIN screenStatus.status as status)"
					+ " ORDER BY prescriptionDetail.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId",request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",request.getPatientName() != null ? request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",request.getDoctorName()!= null ? request.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("wardId",request.getWardId() != null ? request.getWardId() : 0);
			query.setParameter("floorId",request.getFloorId() != null ? request.getFloorId() : 0);
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("uhid",request.getUhid() != null ? request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			//query.setParameter("screenId", screenId);
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Prescription medicationReconciliationSave(Prescription prescription) {
		getSession().update(prescription);
		return prescription;
	}

}
