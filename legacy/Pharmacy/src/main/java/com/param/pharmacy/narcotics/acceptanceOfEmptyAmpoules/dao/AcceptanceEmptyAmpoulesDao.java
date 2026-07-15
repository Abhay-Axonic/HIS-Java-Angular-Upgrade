package com.param.pharmacy.narcotics.acceptanceOfEmptyAmpoules.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoulesDetail;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonSearchRequest;
import com.param.global.utility.Util;

@Repository("acceptanceEmptyAmpoulesDao")
public class AcceptanceEmptyAmpoulesDao extends AbstractDao<Integer, ReturnOfEmptyAmpoules> implements IAcceptanceEmptyAmpoulesDao {

	private static final Logger logger = Logger.getLogger(AcceptanceEmptyAmpoulesDao.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<ReturnOfEmptyAmpoules> acceptanceEmptyAmpoulesSearch(CommonSearchRequest request, Integer screenId)
	{
		try
		{
			String hql = " SELECT returnOfEmptyAmpoules FROM ReturnOfEmptyAmpoules AS returnOfEmptyAmpoules "
					+ " INNER JOIN returnOfEmptyAmpoules.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " LEFT JOIN returnOfEmptyAmpoules.doctor AS doctor WITH doctor.status = 'A'"
//					+ " LEFT JOIN sale.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
					+ " INNER JOIN issue.prescription AS prescription "
					+ " left JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
                    + " INNER JOIN issue.sale AS sale  "
					+ " INNER JOIN returnOfEmptyAmpoules.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					
					
					+ " LEFT JOIN returnOfEmptyAmpoules.ward AS ward WITH ward.status = 'A' "
					+ " LEFT JOIN returnOfEmptyAmpoules.bed AS bed WITH bed.status = 'A' "
					+ " INNER JOIN returnOfEmptyAmpoules.priority AS priority WITH priority.status = 'A' "
					+ " INNER JOIN returnOfEmptyAmpoules.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " WHERE returnOfEmptyAmpoules.isActive = true AND returnOfEmptyAmpoules.isDeleted = false AND returnOfEmptyAmpoules.unit.unitId = :unitId AND returnOfEmptyAmpoules.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR "
					+ " lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)" 
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					//+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( ward.wardId = :wardId OR :wardId = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " AND returnOfEmptyAmpoules.status.id in (2,3,8)"
					+ " ORDER BY issue.id DESC ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId",request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",request.getPatientName() != null ? '%'+request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",request.getDoctorName()!= null ? '%'+request.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("wardId",request.getWardId() != null ? request.getWardId() : 0);
			//query.setParameter("statusId", request.getDoctorName() != null ? request.getDoctorName() : 0);
			query.setParameter("uhid",request.getUhid() != null ?'%'+ request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
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
	public ReturnOfEmptyAmpoules acceptanceEmptyAmpoulesById(Integer id)
	{
		try
		{
			String hql = "  SELECT returnOfEmptyAmpoules FROM ReturnOfEmptyAmpoules AS returnOfEmptyAmpoules "
					+ " WHERE returnOfEmptyAmpoules.isActive = true AND returnOfEmptyAmpoules.isDeleted = false "
					+ " AND returnOfEmptyAmpoules.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return (ReturnOfEmptyAmpoules) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<ReturnOfEmptyAmpoulesDetail> acceptanceEmptyAmpoulesDetails(Integer id)
	{
		try
		{
			String hql = "  SELECT returnOfEmptyAmpoulesDetail FROM ReturnOfEmptyAmpoulesDetail AS returnOfEmptyAmpoulesDetail "
					+ " INNER JOIN returnOfEmptyAmpoulesDetail.issueBatchMapper AS issueBatchMapper WITH issueBatchMapper.isActive = true AND issueBatchMapper.isDeleted = false "
					+ " INNER JOIN issueBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
					+ " INNER JOIN returnOfEmptyAmpoulesDetail.returnOfEmptyAmpoules AS returnOfEmptyAmpoules WITH returnOfEmptyAmpoules.isActive = true AND returnOfEmptyAmpoules.isDeleted = false "
					+ " WHERE returnOfEmptyAmpoulesDetail.isActive = true AND returnOfEmptyAmpoulesDetail.isDeleted = false "
					+ " AND returnOfEmptyAmpoules.id = :id ";

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
	public ReturnOfEmptyAmpoules acceptanceEmptyAmpoulesSave(ReturnOfEmptyAmpoules returnOfEmptyAmpoules)
	{
		getSession().update(returnOfEmptyAmpoules);
		return returnOfEmptyAmpoules;
	}
	
	@Override
	public ReturnOfEmptyAmpoulesDetail acceptanceEmptyAmpoulesDetailsById(Integer id)
	{
		try
		{
			String hql = "  SELECT returnOfEmptyAmpoulesDetail FROM ReturnOfEmptyAmpoulesDetail AS returnOfEmptyAmpoulesDetail "
					+ " WHERE returnOfEmptyAmpoulesDetail.isActive = true AND returnOfEmptyAmpoulesDetail.isDeleted = false "
					+ " AND returnOfEmptyAmpoulesDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return (ReturnOfEmptyAmpoulesDetail) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}