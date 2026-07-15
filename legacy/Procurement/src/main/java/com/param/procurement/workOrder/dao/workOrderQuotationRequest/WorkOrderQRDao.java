package com.param.procurement.workOrder.dao.workOrderQuotationRequest;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.service.global.SessionService;

@Repository
public class WorkOrderQRDao extends AbstractDao<Integer, WorkOrderQuotationRequest> implements IWorkOrderQRDao{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<WorkOrderQuotationRequest> searchWorkOrderQRList(WorkOrderQR workOrderQuotationReq,Integer screenId) {
		String hql = "SELECT workOrderQR FROM WorkOrderQuotationRequest AS workOrderQR "
				+ "	INNER JOIN workOrderQR.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQR.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQR.status as status "
				+ "	LEFT JOIN workOrderQR.contractType as contractType "
				+ " WHERE workOrderQR.isActive = true AND workOrderQR.isDeleted = false AND workOrderQR.unit.unitId = :unitId "
				+ " 	AND workOrderQR.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + " 	AND (:contractTypeId = 0 OR contractType.id = :contractTypeId) "
			  
			    + " 	AND ((:workOrderQRNo = '')"
			    + "	          OR (workOrderQR.workOrderQRNo = upper(:workOrderQRNo)) "
			    + "			  OR (workOrderQR.workOrderQRNo = lower(:workOrderQRNo)) "
			    + " 		  OR (workOrderQR.workOrderQRNo like upper(:workOrderQRNoLike) ) "
			    + "			  OR  (workOrderQR.workOrderQRNo like lower(:workOrderQRNoLike) ) )"
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY workOrderQR.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderQuotationReq.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderQuotationReq.getToDate()));
		query.setParameter("storeId",workOrderQuotationReq.getStoreId() != null ? workOrderQuotationReq.getStoreId() : 0);
		query.setParameter("statusId", workOrderQuotationReq.getStatusId() != null ? workOrderQuotationReq.getStatusId() : 0);
		query.setParameter("contractTypeId", workOrderQuotationReq.getContractTypeId() != null ? workOrderQuotationReq.getContractTypeId() : 0);
		query.setParameter("workOrderQRNo", workOrderQuotationReq.getWorkOrderQRNo() != null ? workOrderQuotationReq.getWorkOrderQRNo() : "");
		query.setParameter("workOrderQRNoLike", workOrderQuotationReq.getWorkOrderQRNo() != null ? workOrderQuotationReq.getWorkOrderQRNo()+"%" : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", workOrderQuotationReq.getUnitId()!=null ? workOrderQuotationReq.getUnitId() : sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public WorkOrderQuotationRequest getWorkOrderQuotationRequest(Integer id)
	{
		String hql = "SELECT workOrderQR FROM WorkOrderQuotationRequest AS workOrderQR "
            + "WHERE workOrderQR.id = :id  AND workOrderQR.isActive = true AND workOrderQR.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WorkOrderQuotationRequest) query.uniqueResult();
	}
	
	@Override
	public List<Integer> getVenderListOFQuotationReuest(Integer id)
	{
		String hql = "SELECT workOrderQv FROM WoQuotationVendor AS workOrderQv "
            + "WHERE workOrderQv.WorkOrderQR = :id  AND workOrderQv.isActive = true AND workOrderQv.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (List<Integer>) query.list();
	}
	
	@Override
	public WorkOrderQuotationRequest saveWorkOrderQuotationRequest(WorkOrderQuotationRequest workOrderQR) {
		getSession().save(workOrderQR);
		return workOrderQR;
		
	}
	
	@Override
	public WorkOrderQuotationRequest updateWorkOrderQuotationRequest(WorkOrderQuotationRequest workOrderQR) {
		getSession().update(workOrderQR);
		return workOrderQR;
	}
	
	@Override
	public List<WorkOrderQuotationRequest> getWorkOrderQuotationRequestSearchList(WorkOrderQR workOrderQuotationReq,List<Integer> statusIds) {
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
				+ "	LEFT JOIN workOrderRequest.contractType as contractType "
				+ " WHERE workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false AND workOrderRequest.unit.unitId = :unitId "
				+ " 	AND workOrderRequest.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "     AND (approvalStatus.id IN (:approvalStatusId)) " 
			    + " 	AND (:contractTypeId = 0 OR contractType.id = :contractTypeId) "
				+ " ORDER BY workOrderRequest.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderQuotationReq.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderQuotationReq.getToDate()));
		query.setParameter("storeId",workOrderQuotationReq.getStoreId() != null ? workOrderQuotationReq.getStoreId() : 0);
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("contractTypeId", workOrderQuotationReq.getContractTypeId() != null ? workOrderQuotationReq.getContractTypeId() : 0);
		query.setParameter("unitId", workOrderQuotationReq.getUnitId()!=null ? workOrderQuotationReq.getUnitId() : sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public com.param.entity.model.procurement.WorkOrderRequest getWorkOrder(Integer id,Integer unitId){
	String hql ="SELECT workOrderRequest FROM com.param.entity.model.procurement.WorkOrderRequest AS workOrderRequest "
			+ " WHERE workOrderRequest.id = :id AND workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false "
			+ "    AND workOrderRequest.unit.unitId = :unitId "
			+ "    ORDER BY workOrderRequest.id DESC";
	
			Query query = getSession().createQuery(hql);		
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			return (com.param.entity.model.procurement.WorkOrderRequest)query.uniqueResult();
		}

}
