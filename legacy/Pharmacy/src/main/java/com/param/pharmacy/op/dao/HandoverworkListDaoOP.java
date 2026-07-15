package com.param.pharmacy.op.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.doctor.DoctorSpecialityMapper;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

@Repository("handoverWorkListDaoOP")
public class HandoverworkListDaoOP extends AbstractDao<Integer, Sale> implements IHandoverworkListDaoOP {
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Sale> handoverworkListSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId)
	{

		String hql = " SELECT sale FROM Sale AS sale \r\n"
				+ "		INNER JOIN sale.saleType AS saleType WITH saleType.isActive = true AND saleType.isDeleted = false AND saleType.unit.unitId = :unitId \r\n"
				+ "		INNER JOIN sale.status AS status WITH status.isActive = true AND status.isDeleted = false \r\n"
				+ "		INNER JOIN sale.addedBy AS user WITH user.status = 'A' AND user.unitId = :unitId \r\n"
				+ "		INNER JOIN sale.visitType AS visitType WITH visitType.status = 'A' \r\n"
				+ "		LEFT JOIN sale.patient AS patient WITH patient.status = 'A' AND patient.unitId = :unitId \r\n"
				+ "	 	LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ "		LEFT JOIN sale.admission as admission WITH admission.status = 'A' AND admission.unit.unitId = :unitId \r\n"
				+ "		LEFT JOIN sale.encounter as encounter WITH encounter.status = 'A' AND encounter.unitId = :unitId "
				+ "     LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.unitId = :unitId \r\n"
				+ " 	LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' AND wardMaster.unitId = :unitId "
				+ " 	LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' AND bedMaster.unitId = :unitId "
				+ "     LEFT JOIN sale.doctor AS doctor WITH doctor.status = 'A' "
				+ "     LEFT JOIN sale.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
				+ "		WHERE sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId \r\n"
				+ "		AND sale.addedDate BETWEEN  :fromDate AND :toDate \r\n"
				+ " 	AND (:visitTypeId = 0 OR visitType.visitTypeId = :visitTypeId) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) \r\n"
				+ " 	AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid) \r\n"
				+ "     AND (:wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
				+ " 	AND ( :patientId = 0 OR patient.patientId = :patientId ) "
				+ "		AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
				+ " 	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "		OR lower(patient.lastName) LIKE :patientName )) "
				+ " 	AND ((:doctorName = ''  OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName "
				+ "  	OR lower(doctor.lastName) LIKE :doctorName)) "
				+ " 	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " 	INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "		INNER JOIN screenStatus.status as status) "
				+ "		ORDER BY sale.id DESC\r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("uhid", pharmacologyValidationSearch.getUhid() != null ? pharmacologyValidationSearch.getUhid().toLowerCase() + '%' : "");
		query.setParameter("wardName", pharmacologyValidationSearch.getWardName() != null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
		query.setParameter("patientId",pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
		query.setParameter("patientName", pharmacologyValidationSearch.getPatientName() != null ? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("doctorName", pharmacologyValidationSearch.getDoctorName() != null ? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%' : "");
		query.setParameter("statusId", pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId", pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	
	}
	
	@Override
	public Sale handoverworkListDetails(Long id)
	{
		String hql = "SELECT sale FROM Sale AS sale "
				+ "	INNER JOIN sale.saleDetailsList AS saleDetail "
				+ " WHERE saleDetail.id IN ( SELECT saleDetail FROM SaleDetail AS saleDetail INNER JOIN saleDetail.batch AS batch WITH batch.isActive = true AND "
				+ " batch.isDeleted = false AND batch.isPharmacy = false WHERE saleDetail.isActive = true AND saleDetail.isDeleted = false ) "
				+ " AND sale.id = :id AND sale.isActive = true AND sale.isDeleted = false "
				+ " AND sale.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", sessionService.getUnitId());

		return (Sale) query.uniqueResult();
	}
	
	@Override
	public List<SaleDetail> handoverworkListDetailList(Long id)
	{
		String hql = "SELECT saleDetail FROM SaleDetail AS saleDetail "
				+ " INNER JOIN saleDetail.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
				+ " INNER JOIN saleDetail.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false "
				+ " WHERE sale.id = :id AND saleDetail.isActive = true AND saleDetail.isDeleted = false "
				+ " AND sale.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	}
	
	@Override
	public Sale handoverworkListUpdate(Sale sale)
	{
		getSession().update(sale);
		return sale;
	}

	@Override
	public List<PharmacologyValidationSearchResult> handoverworkListSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId) {
		String hql = "SELECT DISTINCT sale.id as id, visitType.visitTypeCode as visitType, "
				+ " admission.createdDate as visitDate, "
				+ " patient.uhidNumber as uhid, "
				+ " status.id as statusId, status.status as status, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, "
				+ " CONCAT(doctor.firstName, ' ', doctor.middleName, ' ', doctor.lastName) as doctorDetail, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
				+ " patient.dob as patientDob, doctor.id as doctorId, admission.id as admissionId, "
				+ " handoverBy.userName as dispensedBy "
				+ " 	FROM Sale AS sale \r\n"
				+ "		INNER JOIN sale.saleType AS saleType WITH saleType.isActive = true AND saleType.isDeleted = false AND saleType.unit.unitId = :unitId \r\n"
				+ "		INNER JOIN sale.status AS status WITH status.isActive = true AND status.isDeleted = false \r\n"
				+ "		INNER JOIN sale.addedBy AS user WITH user.status = 'A' AND user.unitId = :unitId \r\n"
				+ "		INNER JOIN sale.visitType AS visitType WITH visitType.status = 'A' \r\n"
				+ "		LEFT JOIN sale.handoverBy as handoverBy "
				+ "		LEFT JOIN sale.patient AS patient \r\n"
				+ "	 	LEFT JOIN patient.prefix AS prefix "
				+ "		LEFT JOIN sale.admission as admission \r\n"
				+ "		LEFT JOIN sale.encounter as encounter "
				+ "     LEFT JOIN admission.admissionDetail AS admissionDetail WITH (:unitId = admissionDetail.unitId) AND admissionDetail.status = 'A'"
				+ " 	LEFT JOIN admissionDetail.wardMaster AS wardMaster "
				+ " 	LEFT JOIN admissionDetail.bedMaster AS bedMaster "
				+ "     LEFT JOIN sale.doctor AS doctor "
				+ "		LEFT JOIN patient.gender as gender "
				+ "		WHERE sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId \r\n"
				+ "		AND sale.addedDate BETWEEN  :fromDate AND :toDate \r\n"
				+ " 	AND (:visitTypeId = 0 OR visitType.visitTypeId = :visitTypeId) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) \r\n"
				+ " 	AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid) \r\n"
				+ "     AND (:wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
				+ " 	AND ( :patientId = 0 OR patient.patientId = :patientId ) "
				+ "		AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
				+ " 	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "		OR lower(patient.lastName) LIKE :patientName )) "
				+ " 	AND ((:doctorName = ''  OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName "
				+ "  	OR lower(doctor.lastName) LIKE :doctorName)) "
				+ " 	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " 	INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "		INNER JOIN screenStatus.status as status) "
				+ "		ORDER BY sale.id DESC\r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("uhid", pharmacologyValidationSearch.getUhid() != null ? pharmacologyValidationSearch.getUhid().toLowerCase() + '%' : "");
		query.setParameter("wardName", pharmacologyValidationSearch.getWardName() != null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
		query.setParameter("patientId",pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
		query.setParameter("patientName", pharmacologyValidationSearch.getPatientName() != null ? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("doctorName", pharmacologyValidationSearch.getDoctorName() != null ? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%' : "");
		query.setParameter("statusId", pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId", pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyValidationSearchResult.class));
		return query.list();
	}

	@Override
	public HandoverDetails handoverworkListDetailsBP(Long id) {

		String hql = "SELECT DISTINCT sale.id as id, patient.id as patientId, "
				+ " patient.dob as dob, "
				+ " patient.uhidNumber as uhid, "
				+ " status.id as statusId, status.status as status, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, "
				+ " CONCAT(doctor.firstName, ' ', doctor.middleName, ' ', doctor.lastName) as doctorDetail, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
				+ " patient.dob as patientDob, doctor.id as doctoreId, admission.id as admissionId, "
				+ " admissionDetail.doa as doa, admissionDetail.pdd as pdd, sale.receivedBy as receiverName "
				+ " FROM Sale AS sale "
				+ "	INNER JOIN sale.saleDetailsList AS saleDetail "
				+ "	INNER JOIN sale.status AS status "
				+ "	LEFT JOIN sale.patient AS patient "
				+ "	 	LEFT JOIN patient.prefix AS prefix "
				+ "		LEFT JOIN sale.admission as admission \r\n"
				+ "     LEFT JOIN admission.admissionDetail AS admissionDetail WITH (:unitId = admissionDetail.unitId) AND admissionDetail.status = 'A'"
				+ " 	LEFT JOIN admissionDetail.wardMaster AS wardMaster "
				+ " 	LEFT JOIN admissionDetail.bedMaster AS bedMaster "
				+ "     LEFT JOIN sale.doctor AS doctor "
				+ "		LEFT JOIN patient.gender as gender "
				+ " WHERE saleDetail.id IN "
				+ " ( SELECT saleDetail FROM SaleDetail AS saleDetail "
				+ " INNER JOIN saleDetail.batch AS batch WITH batch.isActive = true AND "
				+ " batch.isDeleted = false AND batch.isPharmacy = false WHERE saleDetail.isActive = true "
				+ " AND saleDetail.isDeleted = false ) "
				+ " AND sale.id = :id AND sale.isActive = true AND sale.isDeleted = false "
				+ " AND sale.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(HandoverDetails.class));
		return (HandoverDetails) query.uniqueResult();
	}
}
	