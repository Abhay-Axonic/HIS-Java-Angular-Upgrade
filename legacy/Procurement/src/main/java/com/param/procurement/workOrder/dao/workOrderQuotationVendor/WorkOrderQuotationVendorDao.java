package com.param.procurement.workOrder.dao.workOrderQuotationVendor;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.quotation.dto.enqSendToVendor.MultipleVendors;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;

@Repository
public class WorkOrderQuotationVendorDao extends AbstractDao<Integer, WoQuotationVendor> implements IWorkOrderQuotationVendorDao{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<WoQuotationVendor> searchWorkOrderQVList(WorkOrderQVRequest workOrderQVRequest,Integer screenId) {
		String hql = "SELECT workOrderQv FROM WoQuotationVendor AS workOrderQv "
				+ "	INNER JOIN workOrderQv.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQv.status as status "
				+ " INNER JOIN workOrderQv.WorkOrderQR AS workOrderQR "
				+ " INNER JOIN workOrderQR.store AS store "
				+ " INNER JOIN workOrderQv.vendor AS vendor "
				+ " WHERE workOrderQv.isActive = true AND workOrderQv.isDeleted = false AND workOrderQv.unit.unitId = :unitId "
				+ " 	AND workOrderQv.addedDate BETWEEN :fromDate AND :toDate "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    +"      AND ( VI ) "
			    + " 	AND ((:workOrderQRNo = '')"
			    + "	          OR (workOrderQR.workOrderQRNo = upper(:workOrderQRNo)) "
			    + "			  OR (workOrderQR.workOrderQRNo = lower(:workOrderQRNo)) "
			    + " 		  OR (workOrderQR.workOrderQRNo like upper(:workOrderQRNoLike) ) "
			    + "			  OR  (workOrderQR.workOrderQRNo like lower(:workOrderQRNoLike) ) )"
			    + " 	AND ((:vendorWorkOrderQRNo = '')"
			    + "	          OR (workOrderQv.qvNo = upper(:vendorWorkOrderQRNo)) "
			    + "			  OR (workOrderQv.qvNo = lower(:vendorWorkOrderQRNo)) "
			    + " 		  OR (workOrderQv.qvNo like upper(:vendorWorkOrderQRNoLike) ) "
			    + "			  OR  (workOrderQv.qvNo like lower(:vendorWorkOrderQRNoLike) ) )"
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY workOrderQv.id DESC";
		
		 StringBuilder appQuery =  new StringBuilder(" qv.vendor.id in ( ");
	     /*for(MultipleVendors mv : workOrderQVRequest.getVenders()) {
	    	 
	    	 appQuery.append(mv.getVendorId() +", ");
	     }*/
		List<Integer> vendorIds = new ArrayList<>();

		if (vendorIds.isEmpty()) {
			appQuery.append(" 0 ) ");
		} else {
			for (MultipleVendors mv : workOrderQVRequest.getVenders()) {
				Integer vendorId = mv.getVendorId(); // Get vendorId from mv
				vendorIds.add(vendorId); // Add vendorId to the list
				appQuery.append(":vendorId").append(vendorId).append(", "); // Append named parameter
			}
			if (appQuery.length() > 0) {
				appQuery.setLength(appQuery.length() - 2); // Remove the last comma and space
			}
		}
		appQuery.append(")"); // Close the IN clause

		//appQuery.append(" 0 ) ");
	    hql = hql.replace("VI", appQuery.toString());	 // Replace placeholder with vendor IDs condition
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderQVRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderQVRequest.getToDate()));
		query.setParameter("storeId",workOrderQVRequest.getStoreId() != null ? workOrderQVRequest.getStoreId() : 0);
		query.setParameter("statusId", workOrderQVRequest.getStatusId() != null ? workOrderQVRequest.getStatusId() : 0);
		//query.setParameter("vendorId", workOrderQVRequest.getVendorId() != null ? workOrderQVRequest.getVendorId() : 0);
	//	query.setParameter("vendorId", workOrderQVRequest.getVenders() != null ? workOrderQVRequest.getVenders() : 0);

		query.setParameter("vendorWorkOrderQRNo",workOrderQVRequest.getVqrNumber() != null ? workOrderQVRequest.getVqrNumber()  : "");
		query.setParameter("vendorWorkOrderQRNoLike",workOrderQVRequest.getVqrNumber() != null ? workOrderQVRequest.getVqrNumber()+"%"  : "");
		query.setParameter("workOrderQRNo",workOrderQVRequest.getQrNumber() != null ? workOrderQVRequest.getQrNumber()  : "");
		query.setParameter("workOrderQRNoLike",workOrderQVRequest.getQrNumber() != null ? workOrderQVRequest.getQrNumber()+"%"  : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());

		for (int i = 0; i < vendorIds.size(); i++) {
			query.setParameter("vendorId" + vendorIds.get(i), vendorIds.get(i));
		}
		return query.list();
	}

	@Override
	public List<WoQuotationVendor> searchWorkOrderQVListNew(WorkOrderQVRequest workOrderQVRequest,Integer screenId) {
		String hql = "SELECT workOrderQv FROM WoQuotationVendor AS workOrderQv "
				+ "	INNER JOIN workOrderQv.addedBy as user WITH user.unitId = :unitId "
				+ "	LEFT JOIN workOrderQv.status as status "
				+ " INNER JOIN workOrderQv.WorkOrderQR AS workOrderQR "
				+ " INNER JOIN workOrderQR.store AS store "
				+ " INNER JOIN workOrderQv.vendor AS vendor "
				+ " WHERE workOrderQv.isActive = true AND workOrderQv.isDeleted = false AND workOrderQv.unit.unitId = :unitId "
				+ " 	AND workOrderQv.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+"      AND ( :vendorId = 0 OR vendor.id = :vendorId ) "
				+ " 	AND ((:workOrderQRNo = '')"
				+ "	          OR (workOrderQR.workOrderQRNo = upper(:workOrderQRNo)) "
				+ "			  OR (workOrderQR.workOrderQRNo = lower(:workOrderQRNo)) "
				+ " 		  OR (workOrderQR.workOrderQRNo like upper(:workOrderQRNoLike) ) "
				+ "			  OR  (workOrderQR.workOrderQRNo like lower(:workOrderQRNoLike) ) )"
				+ " 	AND ((:vendorWorkOrderQRNo = '')"
				+ "	          OR (workOrderQv.qvNo = upper(:vendorWorkOrderQRNo)) "
				+ "			  OR (workOrderQv.qvNo = lower(:vendorWorkOrderQRNo)) "
				+ " 		  OR (workOrderQv.qvNo like upper(:vendorWorkOrderQRNoLike) ) "
				+ "			  OR  (workOrderQv.qvNo like lower(:vendorWorkOrderQRNoLike) ) )"
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY workOrderQv.id DESC";

		/*StringBuilder appQuery =  new StringBuilder(" qv.vendor.id in ( ");
		for(MultipleVendors mv : workOrderQVRequest.getVenders()) {

			appQuery.append(mv.getVendorId() +", ");
		}
		appQuery.append(" 0 ) ");
		hql = hql.replace("VI", appQuery.toString());*/
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderQVRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderQVRequest.getToDate()));
		query.setParameter("storeId",workOrderQVRequest.getStoreId() != null ? workOrderQVRequest.getStoreId() : 0);
		query.setParameter("statusId", workOrderQVRequest.getStatusId() != null ? workOrderQVRequest.getStatusId() : 0);
		query.setParameter("vendorId", workOrderQVRequest.getVendorId() != null ? workOrderQVRequest.getVendorId() : 0);
		//	query.setParameter("vendorId", workOrderQVRequest.getVenders() != null ? workOrderQVRequest.getVenders() : 0);

		query.setParameter("vendorWorkOrderQRNo",workOrderQVRequest.getVqrNumber() != null ? workOrderQVRequest.getVqrNumber()  : "");
		query.setParameter("vendorWorkOrderQRNoLike",workOrderQVRequest.getVqrNumber() != null ? workOrderQVRequest.getVqrNumber()+"%"  : "");
		query.setParameter("workOrderQRNo",workOrderQVRequest.getQrNumber() != null ? workOrderQVRequest.getQrNumber()  : "");
		query.setParameter("workOrderQRNoLike",workOrderQVRequest.getQrNumber() != null ? workOrderQVRequest.getQrNumber()+"%"  : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", workOrderQVRequest.getUnitId()!=null ? workOrderQVRequest.getUnitId() : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public WoQuotationVendor getWoQuotationVendor(Integer id)
	{
		String hql = "SELECT workOrderQv FROM WoQuotationVendor AS workOrderQv "
            + "WHERE workOrderQv.id = :id  AND workOrderQv.isActive = true AND workOrderQv.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WoQuotationVendor) query.uniqueResult();
	}
	
	@Override
	public WoQuotationVendor saveWoQuotationVendor(WoQuotationVendor workOrderQv) {
		getSession().save(workOrderQv);
		return workOrderQv;
		
	}

	@Override
	public WoQuotationVendor updateWoQuotationVendor(WoQuotationVendor workOrderQv) {
		getSession().update(workOrderQv);
		return workOrderQv;
	}
	
	@Override
	public WorkOrderQuotationRequest getWorkOrderQuotationRequest(Integer id)
	{
		String hql = "SELECT workOrderQr FROM WorkOrderQuotationRequest AS workOrderQr "
            + "WHERE workOrderQr.id = :id  AND workOrderQr.isActive = true AND workOrderQr.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WorkOrderQuotationRequest) query.uniqueResult();
	}
	
	@Override
	public List<WorkOrderQuotationRequest> getWorkOrderQuotationRequestSearchList(WorkOrderQR workOrderQuotationReq,List<Integer> statusIds) {
		String hql = "SELECT workOrderQR FROM WorkOrderQuotationRequest AS workOrderQR "
				+ "	INNER JOIN workOrderQR.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQR.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQR.approvalStatus as approvalStatus "
				+ "	LEFT JOIN workOrderQR.contractType as contractType "
				+ " WHERE workOrderQR.isActive = true AND workOrderQR.isDeleted = false AND workOrderQR.unit.unitId = :unitId "
				+ " 	AND workOrderQR.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "     AND (approvalStatus.id IN (:approvalStatusId)) " 
			    + " 	AND (:contractTypeId = 0 OR contractType.id = :contractTypeId) "
				+ " ORDER BY workOrderQR.id DESC";
		
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
	public List<WorkOrderQuotationRequest> getByVendorAndId(Integer vendorId, Integer id) {
		
		String hql = "SELECT workOrderQr FROM WorkOrderQuotationRequest AS workOrderQr "
	            + " INNER JOIN workOrderQr.woQuotationVendor sendToVendor "
	            + " WHERE sendToVendor.WorkOrderQR.id = :id  "
	            + "  AND (sendToVendor.vendor.id != :vendorId OR sendToVendor.status.id = :cancelled)"
	            + " AND workOrderQr.isActive = true AND workOrderQr.isDeleted = false"
	            + " AND sendToVendor.isActive = true AND sendToVendor.isDeleted = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("vendorId", vendorId);
			query.setParameter("cancelled", com.param.common.constants.Status.Cancelled.getStatus());

			return query.list();
		
	}

}
