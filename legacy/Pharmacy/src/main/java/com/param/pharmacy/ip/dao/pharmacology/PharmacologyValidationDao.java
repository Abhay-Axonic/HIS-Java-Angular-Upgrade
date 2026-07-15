package com.param.pharmacy.ip.dao.pharmacology;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.PrescriptionFrequencyDetailResp;
import com.param.pharmacy.nursing.discharge.dto.BatchDetail;
import com.param.service.global.SessionService;

@Repository("pharmacologyValidationDao")
public class PharmacologyValidationDao extends AbstractDao<Integer, PrescriptionDetail>
		implements IPharmacologyValidationDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(PharmacologyValidationDao.class);

	@Override
	public List<Prescription> pharmacologyValidationSearch(PharmacologyValidationSearch pharmacologyValidationSearch,
			Integer screenId) {
		try {
			String hql = "SELECT prescription FROM Prescription AS prescription "
					+ " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A'  "
					+ " INNER JOIN prescription.priority AS priority WITH priority.status = 'A'"
					+ " INNER JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
					+ " INNER JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
					+ " LEFT JOIN prescription.admission as admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient as patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster as wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster as bedMaster WITH bedMaster.status = 'A' "
					+ " WHERE prescription.id IN (  SELECT prescription1.id FROM Prescription AS prescription1 INNER JOIN prescription1.prescriptionDetailsList AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ "								INNER JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "							    INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
					/* + "						        WHERE  storeType.id != 6 " */
					+ "							  ) "
					+ " AND  prescription.reconciliationDone = true AND prescription.isActive = true AND prescription.isDeleted = false AND prescription.updatedDate BETWEEN  :fromDate AND :toDate "
					+ " AND ( :wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
					+ " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
					+ " AND visitType.visitTypeCode != 'OP'" + " AND ( status.id = :statusId OR :statusId = 0 )"
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY  :orderby ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
			query.setParameter("statusId",
					pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId()
							: 0);
			query.setParameter("visitTypeId",
					pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType()
							: 0);
			query.setParameter("patientId",
					pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId()
							: 0);
			query.setParameter("patientName",
					pharmacologyValidationSearch.getPatientName() != null
							? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%'
							: "");
			query.setParameter("doctorName",
					pharmacologyValidationSearch.getDoctorName() != null
							? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%'
							: "");
			query.setParameter("wardName",
					pharmacologyValidationSearch.getWardName() != null
							? pharmacologyValidationSearch.getWardName().toLowerCase() + '%'
							: "");
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrescriptionDetail> pharmacologyValidationDetails(Long id) {
		try {
			String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " WHERE (:id = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Prescription pharmacologyValidationSave(Prescription prescription) {
		try {
			getSession().update(prescription);
			return prescription;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Prescription updatePrescription(Prescription prescription) {
		try {
			getSession().update(prescription);
			return prescription;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public AssignPicker saveAssignPicker(AssignPicker assignPicker) {
		try {
			getSession().save(assignPicker);
			return assignPicker;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Issue saveIssue(Issue issue) {
		try {
			getSession().saveOrUpdate(issue);
			return issue;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrescriptionFrequencyDetail> getPrescriptionFrequencyDetails(Long id) {
		try {
			String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE (:id = prescriptionDetail.prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PrescriptionDetail> getPrescriptionFrequencyDetailsForWorklist(Long id) {
		try {
			String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
//					+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE (:id = prescriptionDetail.prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Prescription getPrescriptionById(Long id) {
		try {
			String hql = "SELECT prescription FROM Prescription AS prescription "
					+ " WHERE (:id = prescription.id) AND prescription.isActive = true AND prescription.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (Prescription) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PharmacologyValidationSearchResult> pharmacologyValidationSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId) {
		String hql = "SELECT prescription.id as id, visitType.visitTypeName as visitType, "
				+ " admission.createdDate as visitDate, prescription.prescriptionDate as prescriptionDate, "
				+ " patient.uhidNumber as uhid, "
				+ " prescription.addedByUser as addedByUser, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''))AS patientName, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') as ageString, "
				+ " gender.genderCode as genderCode, "
				+ " patient.gender.genderId as genderId, "
				+ " patient.imagePath as imagePath, "
				+ " patient.patientId as patientId, " 
				+ " prescription.medicationType as medicationType, " 
				+ " priority.priorityDesc as priority, "
				+ " status.id as statusId, status.status as status, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, "
				+ " CONCAT(doctor.firstName, ' ', doctor.middleName, ' ', doctor.lastName,' | ',specialityMaster.specialityName) as doctorDetail, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''), ' | ',patient.uhidNumber, ' | ', gender.genderCode  ) AS patientDetail, "
				+ " patient.dob as patientDob, doctor.id as doctorId, admission.id as admissionId "
				+ " FROM Prescription AS prescription "
				+ " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ " INNER JOIN prescription.visitType as visitType " + " INNER JOIN prescription.priority AS priority "
				+ " LEFT JOIN prescription.doctor AS doctor " 
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster " 
				+ " LEFT JOIN prescription.admission as admission "
				+ " LEFT JOIN admission.patient as patient " 
				+ " LEFT JOIN patient.prefix AS prefix "
				+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.unitId = :unitId"
				+ " LEFT JOIN admissionDetail.wardMaster as wardMaster "
				+ " LEFT JOIN admissionDetail.bedMaster as bedMaster " + " LEFT JOIN patient.gender as gender "
				+ " WHERE prescription.id IN (  SELECT prescription1.id FROM Prescription AS prescription1 INNER JOIN prescription1.prescriptionDetailsList AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ "								LEFT JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							    LEFT JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
				/* + "						        WHERE  storeType.id != 6 " */
				+ "							  ) "
				+ " AND  prescription.reconciliationDone = true AND prescription.isActive = true AND prescription.isDeleted = false AND prescription.updatedDate BETWEEN  :fromDate AND :toDate AND prescription.isNonFormulary = 'N'"
				+ " AND ( :wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
				+ " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
				+ " AND visitType.visitTypeCode != 'OP'" + " AND ( status.id = :statusId OR :statusId = 0 )"
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
				+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName )) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "	INNER JOIN screenStatus.status as status) AND prescription.isValidate = false "
				+ " ORDER BY  :orderby ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("statusId",
				pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId",
				pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType() : 0);
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
		query.setParameter("wardName",
				pharmacologyValidationSearch.getWardName() != null
						? pharmacologyValidationSearch.getWardName().toLowerCase() + '%'
						: "");
		query.setParameter("unitId", pharmacologyValidationSearch.getUnitId()!=null ? pharmacologyValidationSearch.getUnitId() : sessionService.getUnitId());
		query.setParameter("screenId", screenId);
		query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyValidationSearchResult.class));
		return query.list();
	}

	@Override
	public PharmacologyValidationDetails getPrescriptionByIdBP(Long id, Integer unitId) {
		String hql = "SELECT prescription.id as id, patient.id as patientId, " 
				+ " visitType.visitTypeId as visitType, "
				+ " status.id as statusId, status.status as status, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, "
				+ " CONCAT(doctor.firstName,' ', doctor.middleName, ' ', doctor.lastName,' | ',specialityMaster.specialityName) as doctorDetail, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''), ' | ', gender.genderCode) AS patientDetail, "
				+ " patient.dob as patientDob, patient.dob as dob, doctor.id as doctorId, admission.id as admissionId, "
				+ "  gender.genderId as genderId, patient.imagePath as imagePath, "
				+ " admissionDetail.doa as doa, admissionDetail.pdd as pdd, patient.uhidNumber as uhid "
				+ " FROM Prescription AS prescription " 
				+ " INNER JOIN prescription.status as status "
				+ " INNER JOIN prescription.visitType as visitType " 
				+ " LEFT JOIN prescription.doctor AS doctor "
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster "
				+ " LEFT JOIN prescription.admission as admission " 
				+ " LEFT JOIN admission.patient as patient "
				+ " LEFT JOIN patient.prefix AS prefix "
				+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.unitId = :unitId"
				+ " LEFT JOIN admissionDetail.wardMaster as wardMaster "
				+ " LEFT JOIN admissionDetail.bedMaster as bedMaster " 
				+ " LEFT JOIN patient.gender as gender "
				+ " WHERE (:id = prescription.id) AND prescription.isActive = true AND prescription.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyValidationDetails.class));
		return (PharmacologyValidationDetails) query.uniqueResult();
	}

	@Override
	public List<PharmacologyItem> pharmacologyValidationDetailsBP(Long id) {
		String hql = "SELECT prescriptionDetail.id as id, item.id as itemId, item.name as itemName,"
				+ " item.isNarcotic as isNarcotic ,prescriptionDetail.priorityId as priorityId, "
				+ " generic.name as genericName, generic.id as genericId, prescription.isIntervention as interventionFlag, "
				+ " prescriptionDetail.remark as interventionRemark, status.id as statusId, status.status as status "
				+ " FROM PrescriptionDetail AS prescriptionDetail "
				+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " LEFT JOIN prescriptionDetail.item as item " 
				+ " LEFT JOIN item.generic as generic "
				+ " INNER JOIN prescriptionDetail.status as status "
				+ " WHERE (:id = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " AND prescriptionDetail.isNonFormulary = 'N'"
				+ " AND prescriptionDetail.statFlag = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));
		return query.list();
	}

	@Override
	public List<PrescriptionFrequencyDetailResp> getPrescriptionFrequencyDetailsBP(List<Long> prescriptionDetailIds) {
		String hql = "SELECT frequency.description as freqDescription, frequency.description as frequency, prescriptionFrequencyDetail.dose as dose, "
				+ " prescriptionFrequencyDetail.startDate as startDate, prescriptionFrequencyDetail.stopDate as stopDate, "
				+ " prescriptionDetail.id as prescriptionDetaild, strengthUnit.units as unit "
				+ " FROM PrescriptionFrequencyDetail AS prescriptionFrequencyDetail "
				+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ " LEFT JOIN prescriptionFrequencyDetail.frequency as frequency "
				+ " LEFT JOIN prescriptionFrequencyDetail.strengthUnit as strengthUnit WITH strengthUnit.isActive = true and strengthUnit.isDeleted = false"
				+ " WHERE (prescriptionDetail.id in (:id)) AND prescriptionFrequencyDetail.isActive = true AND prescriptionFrequencyDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", prescriptionDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(PrescriptionFrequencyDetailResp.class));
		return query.list();
	}

	@Override
	public List<PharmacologyItem> pharmacologyValidationDetailsBP(Integer id) {
		String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
				+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " WHERE (:id = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public Prescription getPrescriptionByAdmissionId(Integer admissionId) {

		try {
			String hql = "SELECT prescription FROM PrescriptionDetail AS prescriptionDetail  "
					+ " INNER JOIN prescriptionDetail.prescription  AS prescription "
					+ " where  prescription.isActive = true "
					+ " AND prescription.isDeleted = false "
					+ " and prescriptionDetail.isDischarge=true  "
					+ " and  prescription.admission.admissionId =:admissionId   ";

			Query query = getSession().createQuery(hql);
			query.setParameter("admissionId", admissionId);

			return (Prescription) query.list().get(0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<PrescriptionDetail> pharmacologyValidationDetailsByAdmissionId(Long admissionId) {

		try {
			String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " WHERE  (:id = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false  and prescriptionDetail.isDischarge=true ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", admissionId);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public BatchDetail getBatchDetail(Long id) {

		try {
			//String hql = "select bth.batch_no as batcnno , bth.expiry_date  as expdate from pharmacy.t_prescription_detail predetail join pharmacy.t_issue_detail issueDetail on predetail.id=issueDetail.prescription_detail_id join inventory.t_batch bth on issueDetail.batch_id=bth.id where issueDetail.prescription_detail_id=:id  ";
            String hql=" select cast(bth.id as int) as \"batchId\" , bth.batch_no as \"batcnno\" , bth.expiry_date  as \"expdate\" , mapper.issue_quantity as \"issuequantity\"  , mapper.accepted_quantity as \"acceptedqty\" from pharmacy.t_prescription pre join  pharmacy.t_prescription_detail predetail on pre.id=predetail.prescription_id  join pharmacy.t_issue_detail issueDetail on predetail.id=issueDetail.prescription_detail_id  join pharmacy.t_issue_detail_batch_mapper mapper on issueDetail.id=mapper.issue_detail_id join inventory.t_batch bth on mapper.batch_id=bth.id where predetail.is_discharge_medication=true and issueDetail.prescription_detail_id=:id  ";
			Query query = getSession().createSQLQuery(hql).addScalar("batchId").addScalar("batcnno")
					.addScalar("expdate").addScalar("issuequantity").addScalar("acceptedqty");
			query.setParameter("id", id); 
			query.setResultTransformer(Transformers.aliasToBean(BatchDetail.class));
			return (BatchDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<PrescriptionDetail> getPrescriptionDetailsList(Long prescriptionId) {
		String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
				+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " WHERE  (:id = prescription.id) AND prescriptionDetail.isActive = true "
				+ " AND prescriptionDetail.isDeleted = false  "
				+ " AND prescriptionDetail.statFlag = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", prescriptionId);

		return query.list();
	}
}