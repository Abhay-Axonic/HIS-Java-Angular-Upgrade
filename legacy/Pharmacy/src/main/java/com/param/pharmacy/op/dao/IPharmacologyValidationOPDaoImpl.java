package com.param.pharmacy.op.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.service.global.SessionService;

@Repository
public class IPharmacologyValidationOPDaoImpl extends AbstractDao<Integer, PrescriptionDetail>
		implements IPharmacologyValidationOPDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Prescription> pharmacologyValidationSearchOP(PharmacologyValidationSearch pharmacologyValidationSearch,
			Integer screenId) {

		String hql = "SELECT prescription FROM Prescription AS prescription "
				+ " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A'  "
				+ " LEFT JOIN prescription.priority AS priority WITH priority.status = 'A'"
				+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
				+ " LEFT JOIN prescription.encounter as encounter WITH encounter.status = 'A'"
				+ " LEFT JOIN encounter.patient as patient WITH patient.status = 'A'"
				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ "  WHERE  prescription.isActive = true AND prescription.isDeleted = false AND prescription.updatedDate BETWEEN  :fromDate AND :toDate "
				+ " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
				+ " AND visitType.visitTypeCode = 'OP'" + " AND ( status.id = :statusId OR :statusId = 0 )"
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
				+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName )) "
				+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid)  "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY  :orderby ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("statusId",
				pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId", 1);
		query.setParameter("patientId",
				pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
		query.setParameter("patientName",
				pharmacologyValidationSearch.getPatientName() != null
						? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%'
						: "");
		query.setParameter("doctorName",
				pharmacologyValidationSearch.getDoctorName() != null
						? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%'
						: "");
		query.setParameter("uhid", pharmacologyValidationSearch.getUhid() != null
				? pharmacologyValidationSearch.getUhid().toLowerCase() + '%'
				: "");
		// query.setParameter("wardName",pharmacologyValidationSearch.getWardName() !=
		// null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
		query.setParameter("unitId", pharmacologyValidationSearch.getUnitId());
		query.setParameter("screenId", screenId);
		query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");

		return query.list();
	}

	@Override
	public Prescription getPrescriptionById(Long id) {
		String hql = "SELECT prescription FROM Prescription AS prescription "
				+ " WHERE (:id = prescription.id) AND prescription.isActive = true AND prescription.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Prescription) query.uniqueResult();
	}

	@Override
	public List<PrescriptionDetail> pharmacologyValidationDetails(Long id) {
		String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
				+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " WHERE (:id = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<PrescriptionFrequencyDetail> getPrescriptionFrequencyDetails(Long id) {
		String hql = "SELECT prescriptionFrequencyDetail FROM PrescriptionFrequencyDetail AS prescriptionFrequencyDetail "
				+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " WHERE (:id = prescriptionDetail.id) AND prescriptionFrequencyDetail.isActive = true AND prescriptionFrequencyDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public Prescription updatePrescription(Prescription prescription) {
		getSession().update(prescription);
		return prescription;
	}

	@Override
	public AssignPicker saveAssignPicker(AssignPicker assignPicker) {
		getSession().save(assignPicker);
		return assignPicker;
	}

	@Override
	public Issue saveIssue(Issue issue) {
		getSession().save(issue);
		return issue;
	}

	@Override
	public List<Prescription> pharmacologyValidationSearchOP(PharmacologyValidationSearch pharmacologyValidationSearch,
			Integer screenId, List<Integer> deptIds) {
		String hql = "SELECT prescription FROM Prescription AS prescription "
				+ " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A'  "
				+ " LEFT JOIN prescription.priority AS priority WITH priority.status = 'A'"
				+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
				+ " LEFT JOIN prescription.encounter as encounter WITH encounter.status = 'A'"
				+ " LEFT JOIN encounter.patient as patient WITH patient.status = 'A'"
				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ " INNER JOIN prescription.specialityMaster dept "
				+ " WHERE prescription.id IN (  SELECT prescription1.id FROM Prescription AS prescription1 INNER JOIN prescription1.prescriptionDetailsList AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ "								INNER JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							    INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
				+ "							  ) "
				+ " AND prescription.isActive = true AND prescription.isDeleted = false AND prescription.updatedDate BETWEEN  :fromDate AND :toDate "
				+ " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
				+ " AND visitType.visitTypeCode = 'OP'" + " AND ( status.id = :statusId OR :statusId = 0 )"
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
				+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName )) "
				+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid)  "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "	INNER JOIN screenStatus.status as status) " + " AND dept.specialityId in (:deptId)"
				+ " ORDER BY  :orderby ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("statusId",
				pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId", 1);
		query.setParameter("patientId",
				pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
		query.setParameter("patientName",
				pharmacologyValidationSearch.getPatientName() != null
						? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%'
						: "");
		query.setParameter("doctorName",
				pharmacologyValidationSearch.getDoctorName() != null
						? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%'
						: "");
		query.setParameter("uhid", pharmacologyValidationSearch.getUhid() != null
				? pharmacologyValidationSearch.getUhid().toLowerCase() + '%'
				: "");
		// query.setParameter("wardName",pharmacologyValidationSearch.getWardName() !=
		// null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
		query.setParameter("unitId", pharmacologyValidationSearch.getUnitId()!=null ? pharmacologyValidationSearch.getUnitId() : sessionService.getUnitId());
		query.setParameter("screenId", screenId);
		query.setParameterList("deptId", !deptIds.isEmpty() ? deptIds : new ArrayList<>(Arrays.asList("")));
		query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");

		return query.list();
	}
}