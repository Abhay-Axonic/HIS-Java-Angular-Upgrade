package com.param.procurement.workOrder.dao.workOrderRequest;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.WorkOrderRequest;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;

@Repository
public class WorkOrderRequestDao extends AbstractDao<Integer, WorkOrderRequest> implements IWorkOrderRequestDao{
	@Autowired
	private SessionService sessionService;

	@Override
	public List<WorkOrderRequest> searchWorkOrderList(com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequest workOrderRequest,Integer screenId) {
		String hql = "SELECT workOrderRequest FROM WorkOrderRequest AS workOrderRequest "
				+ "	INNER JOIN workOrderRequest.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN workOrderRequest.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN workOrderRequest.status as status "
				+ "	LEFT JOIN workOrderRequest.contractType as contractType "
				+ " WHERE workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false AND workOrderRequest.unit.unitId = :unitId "
				+ " 	AND workOrderRequest.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + " 	AND (:contractTypeId = 0 OR contractType.id = :contractTypeId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY workOrderRequest.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderRequest.getToDate()));
		query.setParameter("storeId",workOrderRequest.getStoreId() != null ? workOrderRequest.getStoreId() : 0);
		query.setParameter("statusId", workOrderRequest.getStatusId() != null ? workOrderRequest.getStatusId() : 0);
		query.setParameter("contractTypeId", workOrderRequest.getContractTypeId() != null ? workOrderRequest.getContractTypeId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",workOrderRequest.getUnitId()!=null ? workOrderRequest.getUnitId() : sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public WorkOrderRequest getWorkOrderRequest(Integer id)
	{
		String hql = "SELECT workOrderRequest FROM WorkOrderRequest AS workOrderRequest "
            + "WHERE workOrderRequest.id = :id  AND workOrderRequest.isActive = true AND workOrderRequest.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WorkOrderRequest) query.uniqueResult();
	}
	
	@Override
	public WorkOrderRequest saveWorkOrderRequest(WorkOrderRequest workOrderRequest) {
		getSession().save(workOrderRequest);
		return workOrderRequest;
		
	}
	
	@Override
	public WorkOrderRequest updateWorkOrderRequest(WorkOrderRequest workOrderRequest) {
		getSession().update(workOrderRequest);
		return workOrderRequest;
	}

}
