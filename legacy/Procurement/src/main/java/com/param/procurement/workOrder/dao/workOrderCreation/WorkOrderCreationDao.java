package com.param.procurement.workOrder.dao.workOrderCreation;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationRequest;

@Repository
public class WorkOrderCreationDao extends AbstractDao<Integer, WorkOrderCreation> implements IWorkOrderCreationDao{
	@Autowired
	private SessionService sessionService;

	@Override
	public List<WorkOrderCreation> searchWorkOrderCreationList(WorkOrderCreationRequest woCreationRequest,Integer screenId) {
		String hql = "SELECT woCreation FROM WorkOrderCreation AS woCreation "
				+ "	INNER JOIN woCreation.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN woCreation.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN woCreation.status as status "
				+ " WHERE woCreation.isActive = true AND woCreation.isDeleted = false AND woCreation.unit.unitId = :unitId "
				+ " 	AND woCreation.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY workOrderQR.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(woCreationRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(woCreationRequest.getToDate()));
		query.setParameter("storeId",woCreationRequest.getStoreId() != null ? woCreationRequest.getStoreId() : 0);
		query.setParameter("statusId", woCreationRequest.getStatusId() != null ? woCreationRequest.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
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
	public WorkOrderCreation saveWorkOrderCreation(WorkOrderCreation woCreation) {
		getSession().save(woCreation);
		return woCreation;
		
	}
	
	@Override
	public WorkOrderCreation updateWorkOrderCreation(WorkOrderCreation woCreation) {
		getSession().update(woCreation);
		return woCreation;
	}
	
	/*@Override
	public List<WorkOrderCreation> getWorkOrderQuotationRequestSearchList(WorkOrderQR workOrderQuotationReq,List<Integer> statusIds) {
		String hql = "SELECT workOrderQR FROM WorkOrderQuotationRequest AS workOrderQR "
				+ "	INNER JOIN workOrderQR.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQR.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQR.status as status "
				+ "	LEFT JOIN workOrderQR.approvalStatus as approvalStatus "
				+ " WHERE workOrderQR.isActive = true AND workOrderQR.isDeleted = false AND workOrderQR.unit.unitId = :unitId "
				+ " 	AND workOrderQR.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "     AND (approvalStatus.id IN (:approvalStatusId)) " 
				+ " ORDER BY workOrderQR.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderQuotationReq.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderQuotationReq.getToDate()));
		query.setParameter("storeId",workOrderQuotationReq.getStoreId() != null ? workOrderQuotationReq.getStoreId() : 0);
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public List<com.param.entity.model.procurement.WorkOrderRequest> getWorkOrderList(WorkOrderQR workOrderQuotationReq,List<Integer> statusIds) {
		String hql = "SELECT workOrderRequest FROM com.param.entity.model.procurement.WorkOrderRequest AS workOrderRequest "
				+ "	INNER JOIN workOrderRequest.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN workOrderRequest.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN workOrderRequest.approvalStatus as approvalStatus "
				+ " WHERE workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false AND workOrderRequest.unit.unitId = :unitId "
				+ " 	AND workOrderRequest.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "     AND (approvalStatus.id IN (:approvalStatusId)) " 
				+ " ORDER BY workOrderRequest.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderQuotationReq.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderQuotationReq.getToDate()));
		query.setParameter("storeId",workOrderQuotationReq.getStoreId() != null ? workOrderQuotationReq.getStoreId() : 0);
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public com.param.entity.model.procurement.WorkOrderRequest getWorkOrder(Integer id){
	String hql ="SELECT workOrderRequest FROM com.param.entity.model.procurement.WorkOrderRequest AS workOrderRequest "
			+ " WHERE workOrderRequest.id = :id AND workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false "
			+ "    AND workOrderRequest.unit.unitId = :unitId "
			+ "    ORDER BY workOrderRequest.id DESC";
	
			Query query = getSession().createQuery(hql);		
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());
			return (com.param.entity.model.procurement.WorkOrderRequest)query.uniqueResult();
		}
*/

}
