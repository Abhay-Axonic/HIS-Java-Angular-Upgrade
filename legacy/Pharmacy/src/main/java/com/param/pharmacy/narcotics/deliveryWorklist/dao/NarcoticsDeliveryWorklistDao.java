package com.param.pharmacy.narcotics.deliveryWorklist.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;

@Repository("narcoticsDeliveryWorklistDao")
public class NarcoticsDeliveryWorklistDao extends AbstractDao<Integer, PrescriptionDetail> implements INarcoticsDeliveryWorklistDao {

	private static final Logger logger = Logger.getLogger(NarcoticsDeliveryWorklistDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<Sale> narcoticsDeliveryWorklistSearch(NarcoticsRequest narcoticsRequest, Integer screenId)
	{
		try
		{
			String hql = " SELECT sale FROM Sale AS sale "
					+ "		INNER JOIN sale.saleType AS saleType WITH saleType.isActive = true AND saleType.isDeleted = false   "
					+ "		INNER JOIN sale.issueList AS issueList WITH issueList.isActive = true AND issueList.isDeleted = false   "
					+ "		INNER JOIN issueList.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false   "
					+ "		INNER JOIN sale.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ "		INNER JOIN sale.addedBy AS user WITH user.status = 'A' AND user.unitId = :unitId "
					+ "		INNER JOIN sale.visitType AS visitType WITH visitType.status = 'A' "
					+ "		LEFT JOIN sale.patient AS patient WITH patient.status = 'A' AND patient.unitId = :unitId "
					+ " 	LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ "		LEFT JOIN sale.admission as admission WITH admission.status = 'A' AND admission.unit.unitId = :unitId "
					+ "		LEFT JOIN sale.encounter as encounter WITH encounter.status = 'A' AND encounter.unitId = :unitId "
					+ "     LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.unitId = :unitId "
					+ " 	LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' AND wardMaster.unitId = :unitId "
					+ " 	LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' AND bedMaster.unitId = :unitId "
					+ "     LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A' "
					+ "     LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ "		WHERE sale.id IN ( SELECT sale1.id FROM Sale AS sale1 "
					+ " 					    INNER JOIN sale1.saleDetailsList AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false " 
					+ "							INNER JOIN saleDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ "							INNER JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "							Left JOIN prescriptionDetail.item as item with item.isNarcotic = true  "
					+ "							INNER JOIN sale1.issueList as issueList with issueList.isActive = true AND issueList.isDeleted = false  "
					//+ "					        INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
					//+ "							WHERE storeType.id = 6 "
					+ "							) AND "
					+ "     sale.isActive = true AND sale.isDeleted = false AND sale.unit.unitId = :unitId "
					+ "		AND sale.addedDate BETWEEN  :fromDate AND :toDate "
					+ " AND issueList.isNarcotics = true "
					+ " 	AND (:visitType = 0 OR visitType.visitTypeId = :visitType) "
					+ " 	AND (:statusId = 0 OR status.id = :statusId) "
					+ " 	AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid) "
					+ "     AND (:wardId = 0 OR wardMaster.wardId = :wardId) "
					+ " 	AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "		AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " 	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "		OR lower(patient.lastName) LIKE :patientName )) "
					+ " 	AND ((:doctorName = ''  OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName "
					+ "  	OR lower(doctor.lastName) LIKE :doctorName)) "
					+ " 	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " 	INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "		INNER JOIN screenStatus.status as status) "
					+ "		ORDER BY sale.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(narcoticsRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(narcoticsRequest.getToDate()));
			query.setParameter("uhid", narcoticsRequest.getUhid() != null ? '%'+narcoticsRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("wardId", narcoticsRequest.getWardId() != null ? narcoticsRequest.getWardId() : 0);
			query.setParameter("statusId", narcoticsRequest.getStatusId() != null ? narcoticsRequest.getStatusId() : 0);
			query.setParameter("visitType",narcoticsRequest.getVisitType() != null ? narcoticsRequest.getVisitType() : 0);
			query.setParameter("patientId",narcoticsRequest.getPatientId() != null ? narcoticsRequest.getPatientId() : 0);
			query.setParameter("patientName",narcoticsRequest.getPatientName() != null ? '%'+narcoticsRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName", narcoticsRequest.getDoctorName() != null ? '%'+narcoticsRequest.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("unitId", narcoticsRequest.getUnitId()!=null ? narcoticsRequest.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public List<PrescriptionDetail> narcoticsIssueWorklistDetails(Long id)
	{
		try
		{
			String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " WHERE (:id = prescription.id) AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false ";
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return query.list();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public PrescriptionFrequencyDetail getPrescriptionFrequencyDetails(Long id)
	{
		try
		{
			String hql = "SELECT prescriptionFrequencyDetail FROM PrescriptionFrequencyDetail AS prescriptionFrequencyDetail "
					+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " WHERE (:id = prescriptionDetail.id) AND prescriptionFrequencyDetail.isActive = true AND prescriptionFrequencyDetail.isDeleted = false ";
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
		return (PrescriptionFrequencyDetail) query.uniqueResult();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Prescription getPrescriptionById(Long id)
	{
		try
		{
			String hql = "SELECT prescription FROM Prescription AS prescription "
					+ " WHERE (:id = prescription.id) AND prescription.isActive = true AND prescription.isDeleted = false ";
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
		return (Prescription) query.uniqueResult();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Issue getIssueBySaleId(Long id)
	{
		try
		{
			String hql = "SELECT issue FROM Issue AS issue "
					+ " INNER JOIN issue.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false "
					+ " WHERE (:id = sale.id) AND issue.isActive = true AND issue.isDeleted = false ";
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return (Issue) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}