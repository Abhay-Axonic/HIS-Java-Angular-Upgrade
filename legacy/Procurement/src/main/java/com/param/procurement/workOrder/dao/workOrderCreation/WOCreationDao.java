package com.param.procurement.workOrder.dao.workOrderCreation;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.Status;
import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderRequest;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationRequest;
import com.param.service.global.SessionService;

@Repository
public class WOCreationDao extends AbstractDao<Integer, WorkOrderCreation>  implements IWOCreationDao{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<WorkOrderCreation> searchWorkOrderCreationList(WOCreationRequest woCreationRequest,Integer screenId) {
		String hql = "SELECT woCreation FROM WorkOrderCreation AS woCreation "
				+ "	INNER JOIN woCreation.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN woCreation.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN woCreation.status as status "
				+ "	LEFT JOIN woCreation.contractType as contractType "
				+ " WHERE woCreation.isActive = true AND woCreation.isDeleted = false AND woCreation.unit.unitId = :unitId "
				+ " 	AND woCreation.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:requestTypeId = 0 OR  woCreation.requestType.id = :requestTypeId) "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + " 	AND (:contractTypeId = 0 OR contractType.id = :contractTypeId) "
			    + " 	AND (:vendorId = 0 OR woCreation.vendor.id = :vendorId) "
			    + " 	AND ((:workOrderNo = '')"
			    + "	          OR (woCreation.workOrderNo = upper(:workOrderNo)) "
			    + "			  OR (woCreation.workOrderNo = lower(:workOrderNo)) "
			    + " 		  OR (woCreation.workOrderNo like upper(:workOrderNoLike) ) "
			    + "			  OR  (woCreation.workOrderNo like lower(:workOrderNoLike) ) )"
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY woCreation.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(woCreationRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(woCreationRequest.getToDate()));
		query.setParameter("requestTypeId", woCreationRequest.getWoAgainstTypeId() != null ? woCreationRequest.getWoAgainstTypeId() : 0);
		query.setParameter("storeId",woCreationRequest.getStoreId() != null ? woCreationRequest.getStoreId() : 0);
		query.setParameter("vendorId",woCreationRequest.getVendorId() != null ? woCreationRequest.getVendorId() : 0);
		query.setParameter("workOrderNo",woCreationRequest.getWoNo() != null ? woCreationRequest.getWoNo()  : "");
		query.setParameter("workOrderNoLike",woCreationRequest.getWoNo() != null ? woCreationRequest.getWoNo()+"%"  : "");
		query.setParameter("statusId", woCreationRequest.getStatusId() != null ? woCreationRequest.getStatusId() : 0);
		query.setParameter("contractTypeId", woCreationRequest.getContractTypeId() != null ? woCreationRequest.getContractTypeId() : 0);
		query.setParameter("screenId", screenId );
		query.setParameter("unitId", woCreationRequest.getUnitId()!=null ? woCreationRequest.getUnitId() : sessionService.getUnitId());
		
		return query.list();
	}
	
	@Override
	public List<WorkOrderQuotationRequest> searchWorkOrderCreationListByQR(WOCreationRequest woCreationRequest,Integer screenId,Integer woAgainstQR) {
		
		
		String hql = "SELECT workOrderQR FROM WorkOrderQuotationRequest AS workOrderQR "
				+ "	INNER JOIN workOrderQR.workOrderQRDetailsList as workOrderQRDetailsList " 
				+ "	INNER JOIN workOrderQRDetailsList.woQuotationVendorDetail as woQuotationVendorDetail " 
				+ "	INNER JOIN woQuotationVendorDetail.woQuotationEntryDetailList as woQuotationEntryDetailList " 
				+ "	INNER JOIN workOrderQR.woQuotationVendor as woQuotationVendor " 
				+ "	INNER JOIN workOrderQR.store as store WITH store.unit.unitId = :unitId " 
				+ "	INNER JOIN workOrderQR.status as status "
				+ " WHERE workOrderQR.isActive = true AND workOrderQR.isDeleted = false AND workOrderQR.unit.unitId = :unitId "
				+ " 	AND workOrderQR.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			   
			  
			  
				+ " ORDER BY workOrderQR.id DESC";
		

		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(woCreationRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(woCreationRequest.getToDate()));
		
		query.setParameter("storeId",woCreationRequest.getStoreId() != null ? woCreationRequest.getStoreId() : 0);
		query.setParameter("statusId",Status.Completed.getStatus());
	
		query.setParameter("unitId", woCreationRequest.getUnitId()!=null ? woCreationRequest.getUnitId() : sessionService.getUnitId());
		
		
		return query.list();
	}
	
	@Override
	public WorkOrderQuotationRequest searchWorkOrderCreationQRById(WOCreationRequest woCreationRequest) {
		
		
		String hql = "SELECT workOrderQR FROM WorkOrderQuotationRequest AS workOrderQR "
				+ "	INNER JOIN workOrderQR.workOrderQRDetailsList as workOrderQRDetailsList   " 
				+ "	INNER JOIN workOrderQRDetailsList.woQuotationVendorDetail as woQuotationVendorDetail " 
				+ "	INNER JOIN woQuotationVendorDetail.woQuotationEntryDetailList as woQuotationEntryDetailList " 
				+ "	INNER JOIN workOrderQR.woQuotationVendor as woQuotationVendor " 
				+ "	INNER JOIN workOrderQR.store as store WITH store.unit.unitId = :unitId " 
				+ "	INNER JOIN workOrderQR.status as status "
				+ " WHERE workOrderQR.isActive = true AND workOrderQR.isDeleted = false AND workOrderQR.unit.unitId = :unitId "
				+ " 	AND workOrderQR.id =:Id AND  workOrderQRDetailsList.id IN (:itemIds)";
				
			    
		Query query = getSession().createQuery(hql);
		query.setParameter("Id", woCreationRequest.getQrId());
		query.setParameterList("itemIds",woCreationRequest.getItemIds());
		query.setParameter("unitId", woCreationRequest.getUnitId()!=null ? woCreationRequest.getUnitId() : sessionService.getUnitId());
		
		
		return (WorkOrderQuotationRequest)query.uniqueResult();
	}
	
	
	@Override
	public List<WorkOrderRequest> searchWorkOrderCreationListByWR(WOCreationRequest woCreationRequest,Integer screenId,Integer woAgainstQR) {
		
		
		String hql = "SELECT workOrderRequest FROM WorkOrderRequest AS workOrderRequest "
				+ "	INNER JOIN workOrderRequest.status as status "
				+ "	INNER JOIN workOrderRequest.store as store WITH store.unit.unitId = :unitId " 
				+ " WHERE workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false AND workOrderRequest.unit.unitId = :unitId "
				+ " 	AND workOrderRequest.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND status.id= :statusId "
				 + "	AND (:storeId = 0 OR store.id = :storeId) "
				+ " ORDER BY workOrderRequest.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(woCreationRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(woCreationRequest.getToDate()));
		query.setParameter("statusId", Status.Pending.getStatus());
		query.setParameter("storeId",woCreationRequest.getStoreId() != null ? woCreationRequest.getStoreId() : 0);
		query.setParameter("unitId",woCreationRequest.getUnitId()!=null ? woCreationRequest.getUnitId() : sessionService.getUnitId());
		
		
		return query.list();
	}
	
	@Override
	public WorkOrderRequest searchWorkOrderCreationWRId(WOCreationRequest woCreationRequest) {
		
		
		String hql = "SELECT workOrderRequest FROM WorkOrderRequest AS workOrderRequest "
				+ "	INNER JOIN workOrderRequest.workOrderDetailList as workOrderDetailList   "
				+ "	INNER JOIN workOrderRequest.status as status "
				+ "	INNER JOIN workOrderRequest.store as store WITH store.unit.unitId = :unitId " 
				+ " WHERE workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false AND workOrderRequest.unit.unitId = :unitId "
				+ " 	AND workOrderRequest.id=:Id AND workOrderDetailList.id in (:itemIds)";
				
				
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("Id",woCreationRequest.getWrId());
		query.setParameterList("itemIds",woCreationRequest.getItemIds());
		query.setParameter("unitId", woCreationRequest.getUnitId()!=null ? woCreationRequest.getUnitId() : sessionService.getUnitId());
		
		
		return (WorkOrderRequest)query.uniqueResult();
	}
	
	@Override
	public WorkOrderCreation saveWorkOrderCreation(WorkOrderCreation woCreation) {
		getSession().save(woCreation);
		return woCreation;
		
	}
	
	@Override
	public WorkOrderCreation getWorkOrderCreation(Integer id)
	{
		String hql = "SELECT woCreation FROM WorkOrderCreation AS woCreation "
            + "WHERE woCreation.id = :id  AND woCreation.isActive = true AND woCreation.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WorkOrderCreation) query.uniqueResult();
	}
	
	@Override
	public WorkOrderCreation updateWorkOrderCreation(WorkOrderCreation woCreation) {
		getSession().update(woCreation);
		return woCreation;
	}
	

	
	
}
