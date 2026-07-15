package com.param.pharmacy.nursing.pharmacy.dao.billableConsumption;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.BillableConsumption;
import com.param.entity.model.pharmacy.BillableConsumptionDetail;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.PatientDetailRequest;
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;
import com.param.service.global.SessionService;

@Repository("billableConsumptionDao")
public class BillableConsumptionDao extends AbstractDao<Integer, PatientIndent> implements IBillableConsumptionDao {

	private static final Logger logger = Logger.getLogger(BillableConsumptionDao.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<BillableConsumption> billableConsumptionSearch(BillableConsumptionRequest request, Integer screenId)
	{
		try
		{
			String hql = " SELECT billableConsumption FROM BillableConsumption AS billableConsumption "
					+ " INNER JOIN billableConsumption.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN billableConsumption.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN billableConsumption.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN billableConsumption.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN billableConsumption.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN billableConsumption.ward AS ward WITH ward.status = 'A' "
					+ " LEFT JOIN billableConsumption.bed AS bed WITH bed.status = 'A' "
					+ " INNER JOIN billableConsumption.addedBy AS addedBy WITH addedBy.status = 'A' "
					+ " INNER JOIN billableConsumption.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN billableConsumption.approvedStatus AS approvedStatus WITH approvedStatus.isActive = true AND approvedStatus.isDeleted = false "
					+ " WHERE billableConsumption.isActive = true AND billableConsumption.isDeleted = false AND "
					+ " billableConsumption.addedDate BETWEEN :fromDate AND :toDate AND billableConsumption.unit.unitId = :unitId "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND ( visitType.visitTypeId =:opVisitType or ( ward.wardId in ( :wardId ))) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND(( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo) OR lower(encounter.encounterNumber) LIKE :visitNo )"
					+ " AND( :consumptionNo = '' OR lower(billableConsumption.consumableNumber) LIKE :consumptionNo ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY billableConsumption.id DESC ";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType",request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",request.getPatientName() != null ? '%'+request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",request.getUhid()!= null ? '%'+request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",request.getVisitNo()!= null ? '%'+request.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("consumptionNo",request.getConsumptionNo()!= null ? '%'+request.getConsumptionNo().toLowerCase() + '%' : "");
			List<Integer> wardList = new ArrayList<>();
			Integer opVisitType=1;
			if(request.getWardList()!=null && request.getWardList().size()>0) {
				wardList.addAll(request.getWardList());
			}else {
				wardList.add(0);
			}
			if(request.getWardList()!=null && request.getWardList().size()>1 && request.getVisitType() == null)
			{
				opVisitType=1;
			}
			else if(request.getVisitType()!=null &&  request.getVisitType()== 1)
			{
				opVisitType=request.getVisitType();
				wardList.add(0);
			}
			else
			{
				opVisitType=0;
			}
			query.setParameter("opVisitType", opVisitType);
			query.setParameterList("wardId", wardList);
//			query.setParameterList("wardId",request.getWardList());
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
	public BillableConsumption billableConsumptionById(Integer id,Integer unitId)
	{

		try
		{
			String hql = " SELECT billableConsumption FROM BillableConsumption AS billableConsumption"
					+ " WHERE billableConsumption.isActive = true AND billableConsumption.isDeleted = false AND billableConsumption.id = :id"
					+ " AND billableConsumption.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null?unitId:sessionService.getUnitId());
			
		return (BillableConsumption) query.uniqueResult();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}
	
	@Override
	public List<BillableConsumptionDetail> billableConsumptionDetailById(Integer id,Integer unitId)
	{
		try
		{
			String hql = " SELECT billableConsumptionDetail FROM BillableConsumptionDetail AS billableConsumptionDetail "
					+ " LEFT JOIN billableConsumptionDetail.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
					+ " INNER JOIN billableConsumptionDetail.billableConsumption AS billableConsumption WITH billableConsumption.isActive = true AND billableConsumption.isDeleted = false "
					+ " WHERE billableConsumptionDetail.isActive = true AND billableConsumptionDetail.isDeleted = false AND billableConsumption.id = :id"
					+ " AND billableConsumption.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public BillableConsumption saveBillableConsumption(BillableConsumption billableConsumption)
	{
		getSession().save(billableConsumption);
		return billableConsumption;
	}
	
	
	@Override
	public Sale saveBill(Sale sale)
	{
		getSession().save(sale);
		return sale;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillableConsumptionSave> getBillableConsumptionDetails(Long id) {
		try
		{
			String query = " select " + 
					"	tbcd.billable_consumption_id as \"billableConsumptionId\", " + 
					"	tbcd.id  as \"billableConsumptionDetailsId\", " + 
					"	tbcd.item_id as \"itemId\", " + 
					"	tbcd.consumable_quantity  as \"consumebleQty\" , " + 
					"	tbc.consumable_number as \"consumableNumber\" " + 
					"from " + 
					"	pharmacy.t_billable_consumption_detail tbcd  " + 
					"	inner join pharmacy.t_billable_consumption tbc on tbcd.billable_consumption_id  = tbc.id  " + 
					"where " + 
					"	tbcd.is_active = true " + 
					"	and tbcd.is_deleted = false " + 
					"	and tbc.id =:id";
			StringBuffer s1 = new StringBuffer(query);
			
			List<BillableConsumptionSave> list = sessionFactory.getCurrentSession().createSQLQuery(s1.toString())
					.setParameter("id",id.intValue())
					.setResultTransformer(Transformers.aliasToBean(BillableConsumptionSave.class)).list();
			
		return list;
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}


}