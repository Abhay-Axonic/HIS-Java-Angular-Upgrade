package com.param.pharmacy.nursing.pharmacy.dao.returnBillableConsumption;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
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
import com.param.entity.model.pharmacy.ReturnBillableConsumption;
import com.param.entity.model.pharmacy.ReturnBillableConsumptionDetail;
import com.param.global.pharmacy.dto.BillableConsumptionItem;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;
import com.param.global.common.ServiceException;

@Repository("returnBillableConsumptionDao")
public class ReturnBillableConsumptionDao extends AbstractDao<Integer, PatientIndent> implements IReturnBillableConsumptionDao {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	private static final Logger logger = Logger.getLogger(ReturnBillableConsumptionDao.class);

	@Override
	public List<ReturnBillableConsumption> returnBillableConsumptionSearch(BillableConsumptionRequest request, Integer screenId)
	{
		try
		{
			String hql = " SELECT returnBillableConsumption FROM ReturnBillableConsumption AS returnBillableConsumption "
					+ " INNER JOIN returnBillableConsumption.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN returnBillableConsumption.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN returnBillableConsumption.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN returnBillableConsumption.encounter AS encounter WITH encounter.status = 'A' "
					+ " INNER JOIN returnBillableConsumption.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN returnBillableConsumption.ward AS ward WITH ward.status = 'A' "
					+ " LEFT JOIN returnBillableConsumption.bed AS bed WITH bed.status = 'A' "
					+ " INNER JOIN returnBillableConsumption.addedBy AS addedBy WITH addedBy.status = 'A' "
					+ " INNER JOIN returnBillableConsumption.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN returnBillableConsumption.approvedStatus AS approvedStatus WITH approvedStatus.isActive = true AND approvedStatus.isDeleted = false "
					+ " WHERE returnBillableConsumption.isActive = true AND returnBillableConsumption.isDeleted = false AND "
					+ " returnBillableConsumption.addedDate BETWEEN :fromDate AND :toDate AND returnBillableConsumption.unit.unitId = :unitId "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ " AND ( (:patientId != 0 AND :patientName != '') OR (:patientName = ''  OR lower(COALESCE(CONCAT(patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName)) "
					+ " AND ( visitType.visitTypeId =:opVisitType or ( ward.wardId in ( :wardId ))) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND(( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo) OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND( :returnNo = '' OR lower(returnBillableConsumption.returnNumber) LIKE :returnNo ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY returnBillableConsumption.id DESC ";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType",request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",request.getPatientName() != null ? '%'+request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",request.getUhid()!= null ? '%'+request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",request.getVisitNo()!= null ?'%'+ request.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("returnNo",request.getReturnNo()!= null ? '%'+request.getReturnNo().toLowerCase() + '%' : "");
			List<Integer> wardList = new ArrayList<>();
            Integer opVisitType=1;
			if(request.getWardList()!=null && request.getWardList().size()>0) {
				wardList.addAll(request.getWardList());
			}else {
				wardList.add(0);
			}
            if(request.getWardList().size()>1 && request.getVisitType() == null)
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
	public ReturnBillableConsumption returnBillableConsumptionById(Integer id, Integer unitId)
	{

		try
		{
			String hql = " SELECT returnBillableConsumption FROM ReturnBillableConsumption AS returnBillableConsumption"
					+ " WHERE returnBillableConsumption.isActive = true AND returnBillableConsumption.isDeleted = false AND returnBillableConsumption.id = :id"
					+ " AND returnBillableConsumption.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
		return (ReturnBillableConsumption) query.uniqueResult();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<ReturnBillableConsumptionDetail> returnBillableConsumptionDetailById(Integer id,Integer unitId)
	{
		try
		{
			String hql = " SELECT returnBillableConsumptionDetail FROM ReturnBillableConsumptionDetail AS returnBillableConsumptionDetail "
					+ " INNER JOIN returnBillableConsumptionDetail.billableConsumptionDetail AS billableConsumptionDetail WITH billableConsumptionDetail.isActive = true AND billableConsumptionDetail.isDeleted = false "
					+ " INNER JOIN billableConsumptionDetail.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isReturn = false "
					+ " INNER JOIN returnBillableConsumptionDetail.returnBillableConsumption AS returnBillableConsumption WITH returnBillableConsumption.isActive = true AND returnBillableConsumption.isDeleted = false "
					+ " WHERE returnBillableConsumptionDetail.isActive = true AND returnBillableConsumptionDetail.isDeleted = false AND returnBillableConsumption.id = :id"
					+ " AND returnBillableConsumption.unit.unitId = :unitId ";
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
	public ReturnBillableConsumption saveReturnBillableConsumption(ReturnBillableConsumption returnBillableConsumption)
	{
		getSession().save(returnBillableConsumption);
		return returnBillableConsumption;
	}
	
	@Override
	public List<BillableConsumption> returnBillableConsumptionSearchIssue(BillableConsumptionRequest request)
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
					+ " INNER JOIN billableConsumption.status AS status WITH status.isActive = true AND status.isDeleted = false AND status.id!=9  "
					+ " INNER JOIN billableConsumption.approvedStatus AS approvedStatus WITH approvedStatus.isActive = true AND approvedStatus.isDeleted = false "
					+ " WHERE billableConsumption.isActive = true AND billableConsumption.isDeleted = false AND "
					+ " billableConsumption.addedDate BETWEEN :fromDate AND :toDate AND billableConsumption.unit.unitId = :unitId "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND ( ward.wardId = :wardId OR :wardId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND(( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo) OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND( :consumptionNo = '' OR lower(billableConsumption.consumableNumber) LIKE :consumptionNo )  "
					+ " ORDER BY billableConsumption.id DESC ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType",request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",request.getPatientName() != null ? request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("visitNo",request.getVisitNo()!= null ? request.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("wardId",request.getWardId() != null ? request.getWardId() : 0);
			query.setParameter("consumptionNo",request.getConsumptionNo()!= null ? request.getConsumptionNo().toLowerCase() + '%' : "");
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		
			return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public List<BillableConsumptionDetail> returnBillableConsumptionAdd(Integer id, Integer unitId)
	{
		try
		{
			String hql = " SELECT billableConsumptionDetail FROM BillableConsumptionDetail AS billableConsumptionDetail "
					+ " INNER JOIN billableConsumptionDetail.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isReturn = false "
					+ " INNER JOIN billableConsumptionDetail.billableConsumption AS billableConsumption WITH billableConsumption.isActive = true AND billableConsumption.isDeleted = false "
					+ " WHERE billableConsumptionDetail.isActive = true AND billableConsumptionDetail.isDeleted = false "
					+ " AND billableConsumption.unit.unitId = :unitId AND billableConsumption.id = :ids ";
					
			Query query = getSession().createQuery(hql);
			query.setParameter("ids", id);
			query.setParameter("unitId", unitId!=null?unitId:sessionService.getUnitId());

		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public Long getReturnQuantity(Integer billableConsumptionDetailId, Integer unitId)
	{
		try
		{
			String hql = " SELECT COALESCE(SUM(returnBillableConsumptionDetail.returnQuantity), 0) FROM ReturnBillableConsumptionDetail AS returnBillableConsumptionDetail "
					+ " INNER JOIN returnBillableConsumptionDetail.billableConsumptionDetail AS billableConsumptionDetail WITH billableConsumptionDetail.isDeleted = false AND billableConsumptionDetail.isActive = true "
					+ " WHERE billableConsumptionDetail.isActive = true AND billableConsumptionDetail.isDeleted = false "
					+ " AND billableConsumptionDetail.unit.unitId = :unitId AND billableConsumptionDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", billableConsumptionDetailId);
			query.setParameter("unitId", unitId!=null ? unitId: sessionService.getUnitId());

			return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public PharmacySaleReturnDto getSaleDetailIdByByBillalbleComptionDetails(Integer detailsId) {
		try
		{
			 String hql = " SELECT saleDetail.id as saleDetailId,billableConsumptionDetail.batch.id as batchId,billableConsumptionDetail.item.id as itemId,saleDetail.sale.id as saleId,saleDetail.productCategoryId as productCategoryId ,billableConsumptionDetail.billableConsumption.id as billableConsumptionId FROM BillableConsumptionDetail AS billableConsumptionDetail  "
					 +"	  INNER JOIN billableConsumptionDetail.saleDetail AS saleDetail  "
	 				+"    WHERE  billableConsumptionDetail.id = :id ";
			 		Query query = getSession().createQuery(hql); query.setParameter("id", detailsId); 
			 		//query.setParameter("unitId", sessionService.getUnitId());
			return (PharmacySaleReturnDto) query.setResultTransformer(Transformers.aliasToBean(PharmacySaleReturnDto.class)).uniqueResult();



	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	/*start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/
	public Long getTotalConsumeQuantity(Integer billableConsumptionDetailId, Integer unitId)
	{
		try
		{
			String hql = " SELECT COALESCE(SUM(returnBillableConsumptionDetail.consumeQuantity), 0) FROM ReturnBillableConsumptionDetail AS returnBillableConsumptionDetail "
					+ " INNER JOIN returnBillableConsumptionDetail.billableConsumptionDetail AS billableConsumptionDetail WITH billableConsumptionDetail.isDeleted = false AND billableConsumptionDetail.isActive = true "
					+ " WHERE billableConsumptionDetail.isActive = true AND billableConsumptionDetail.isDeleted = false "
					+ " AND billableConsumptionDetail.unit.unitId = :unitId AND billableConsumptionDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", billableConsumptionDetailId);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	/*end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/

	@Override
	public Integer getReturnQuantityById(Integer id) {

		try
		{
			String sql = " select COALESCE (sum(return_quantity),0) from pharmacy.t_return_billable_consumption_detail where billable_consumption_detail_id =:id ";

			Query query = getSession().createSQLQuery(sql);
			query.setParameter("id", id);
			BigInteger qty=(BigInteger) query.uniqueResult();
			Integer intValue=qty!=null ? qty.intValue() : 0;
			return intValue;
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public List<PharmacySaleReturnDto> getSaleDetailIdByByBillalbleComptionDetailsAll(
			List<Integer> billableConsumptionDetailsIds) {
		String hql = " SELECT saleDetail.id as saleDetailId,billableConsumptionDetail.batch.id as batchId,billableConsumptionDetail.item.id as itemId,saleDetail.sale.id as saleId,"
				+ " saleDetail.productCategoryId as productCategoryId ,billableConsumptionDetail.billableConsumption.id as billableConsumptionId,billableConsumptionDetail.id AS billbleConsumptionDetailId  "
				+ " FROM BillableConsumptionDetail AS billableConsumptionDetail  "
				 +"	  INNER JOIN billableConsumptionDetail.saleDetail AS saleDetail  "
				+"    WHERE  billableConsumptionDetail.id in(:id) ";
		 		Query query = getSession().createQuery(hql); query.setParameterList("id", billableConsumptionDetailsIds); 
		 		//query.setParameter("unitId", sessionService.getUnitId());
		  query.setResultTransformer(Transformers.aliasToBean(PharmacySaleReturnDto.class));
		  return query.list();
	}
}