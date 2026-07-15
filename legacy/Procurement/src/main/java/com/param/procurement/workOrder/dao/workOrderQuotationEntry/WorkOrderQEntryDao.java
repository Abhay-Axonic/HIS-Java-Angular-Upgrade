package com.param.procurement.workOrder.dao.workOrderQuotationEntry;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.WoQuotationEntry;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WoQuotationVendorDetail;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.global.utility.Util;

import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuoatationEntryRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuotationEntryDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;

@Repository
public class WorkOrderQEntryDao extends AbstractDao<Integer, WoQuotationEntry> implements IWorkOrderQEntryDao{
	@Autowired
	private SessionService sessionService;

	@Override
	public List<WoQuotationEntry> searchWorkOrderQEntryList(WoQuoatationEntryRequest woQEntryRequest,Integer screenId) {
		String hql = "SELECT woQuotationEntry FROM WoQuotationEntry AS woQuotationEntry "
				+ "	INNER JOIN woQuotationEntry.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN woQuotationEntry.status as status "
				+ " INNER JOIN woQuotationEntry.vendorQuotation AS vendorQuotation "
				+ " LEFT JOIN vendorQuotation.WorkOrderQR AS WorkOrderQR "
				+ " LEFT JOIN WorkOrderQR.store AS store "
				+ " INNER JOIN vendorQuotation.vendor AS vendor "
				+ " WHERE woQuotationEntry.isActive = true AND woQuotationEntry.isDeleted = false AND woQuotationEntry.unit.unitId = :unitId "
				+ " 	AND woQuotationEntry.addedDate BETWEEN :fromDate AND :toDate "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    +"      AND (:vendorId = 0 OR vendor.id = :vendorId) "
			    + " 	AND ((:workOrderQRNo = '')"
			    + "	          OR (WorkOrderQR.workOrderQRNo = upper(:workOrderQRNo)) "
			    + "			  OR (WorkOrderQR.workOrderQRNo = lower(:workOrderQRNo)) "
			    + " 		  OR (WorkOrderQR.workOrderQRNo like upper(:workOrderQRNoLike) ) "
			    + "			  OR  (WorkOrderQR.workOrderQRNo like lower(:workOrderQRNoLike) ) )"
			    
 				+ " 	AND ((:workOrderVQRNo = '')"
 				+ "	          OR (vendorQuotation.qvNo = upper(:workOrderVQRNo)) "
 				+ "			  OR (vendorQuotation.qvNo = lower(:workOrderVQRNo)) "
 				+ " 		  OR (vendorQuotation.qvNo like upper(:workOrderVQRNoLike) ) "
 				+ "			  OR  (vendorQuotation.qvNo like lower(:workOrderVQRNoLike) ) )"
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY woQuotationEntry.id DESC";
		
			
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(woQEntryRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(woQEntryRequest.getToDate()));
		query.setParameter("storeId",woQEntryRequest.getStoreId() != null ? woQEntryRequest.getStoreId() : 0);
		query.setParameter("statusId", woQEntryRequest.getStatusId() != null ? woQEntryRequest.getStatusId() : 0);
		query.setParameter("vendorId", woQEntryRequest.getVendorId() != null ? woQEntryRequest.getVendorId() : 0);
		query.setParameter("workOrderQRNo", woQEntryRequest.getWorkOrderQRNo() != null ? woQEntryRequest.getWorkOrderQRNo() : "");
		query.setParameter("workOrderQRNoLike", woQEntryRequest.getWorkOrderQRNo() != null ? woQEntryRequest.getWorkOrderQRNo()+"%"  : "");
		query.setParameter("workOrderVQRNo", woQEntryRequest.getWorkOrderVQRNo() != null ? woQEntryRequest.getWorkOrderVQRNo() : "");
		query.setParameter("workOrderVQRNoLike", woQEntryRequest.getWorkOrderVQRNo() != null ? woQEntryRequest.getWorkOrderVQRNo()+"%"  : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", woQEntryRequest.getUnitId()!=null ? woQEntryRequest.getUnitId() : sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public WoQuotationEntry getWoQuotationEntry(Integer id)
	{
		String hql = "SELECT woQuotationEntry FROM WoQuotationEntry AS woQuotationEntry "
            + "WHERE woQuotationEntry.id = :id  AND woQuotationEntry.isActive = true AND woQuotationEntry.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WoQuotationEntry) query.uniqueResult();
	}
	
	@Override
	public WoQuotationEntry saveWoQuotationEntry(WoQuotationEntry woQuotationEntry) {
		getSession().save(woQuotationEntry);
		return woQuotationEntry;
		
	}
	
	@Override
	public WoQuotationEntry updateWoQuotationEntry(WoQuotationEntry woQuotationEntry) {
		getSession().update(woQuotationEntry);
		return woQuotationEntry;
	}
	
	@Override
	public WorkOrderQuotationRequest updateWoQuotationRequest(WorkOrderQuotationRequest WorkOrderQR) {
		getSession().update(WorkOrderQR);
		return WorkOrderQR;
	}
	
	/*@Override
	public WorkOrderQuotationRequest getWorkOrderQuotationRequest(Integer id)
	{
		String hql = "SELECT workOrderQr FROM WorkOrderQuotationRequest AS workOrderQr "
            + "WHERE workOrderQr.id = :id  AND workOrderQr.isActive = true AND workOrderQr.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WorkOrderQuotationRequest) query.uniqueResult();
	}*/
	
	
	@Override
	public List<WoQuotationVendor> getWoQuotationVendorSearchList(WorkOrderQVRequest workOrderQuotationVendor,List<Integer> statusIds) {
		List<WoQuotationVendor> woQuotationVendor=new ArrayList<>();
		try {
		String hql = "SELECT workOrderQv FROM WoQuotationVendor AS workOrderQv "
				+ "	INNER JOIN workOrderQv.addedBy as user "
//				+ "WITH user.unitId = :unitId " 
				+ "	INNER JOIN workOrderQv.WorkOrderQR as WorkOrderQR WITH WorkOrderQR.unit.unitId = :unitId AND WorkOrderQR.isActive = true AND WorkOrderQR.isDeleted = false " 
				+ "	LEFT JOIN WorkOrderQR.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN workOrderQv.approvalStatus as approvalStatus "
				+ "	LEFT JOIN WorkOrderQR.contractType as contractType "
				+ " WHERE workOrderQv.isActive = true AND workOrderQv.isDeleted = false AND workOrderQv.unit.unitId = :unitId "
				+ " 	AND workOrderQv.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "     AND (approvalStatus.id IN (:approvalStatusId)) " 
			    + " 	AND (:contractTypeId = 0 OR contractType.id = :contractTypeId) "
				+ " ORDER BY workOrderQv.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(workOrderQuotationVendor.getFromDate()));
		query.setParameter("toDate", Util.getDate(workOrderQuotationVendor.getToDate()));
		query.setParameter("storeId",workOrderQuotationVendor.getStoreId() != null ? workOrderQuotationVendor.getStoreId() : 0);
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("contractTypeId", workOrderQuotationVendor.getContractTypeId() != null ? workOrderQuotationVendor.getContractTypeId() : 0);
		query.setParameter("unitId", workOrderQuotationVendor.getUnitId()!=null ? workOrderQuotationVendor.getUnitId() : sessionService.getUnitId());
		woQuotationVendor= query.list();
		}catch(Exception e) {
			throw e;
		}
		return woQuotationVendor;
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
	public List<WoQuotationVendorDetail> getWoQuotationVendorDetails(List<Integer> ids)
	{
		String hql = "SELECT workOrderQvDetail FROM WoQuotationVendorDetail AS workOrderQvDetail "
            + "WHERE workOrderQvDetail.id IN (:ids)  AND workOrderQvDetail.isActive = true AND workOrderQvDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		//query.setParameter("id", id);
		query.setParameterList("ids", ids);
		return query.list();
	}

	@Override
	public  WoQuotationEntryDetailResponse getWouotationNegoAmount(Integer negoId, Integer venId,Integer detailId) {
	
		
		/*
		 * String hql
		 * ="select woQuotationEntryDetail from WoQuotationEntryDetail AS woQuotationEntryDetail  "
		 * +"INNER JOIN woQuotationEntryDetail.woQuotationEntry AS woQuotationEntry with  woQuotationEntry.isActive = true AND woQuotationEntry.isDeleted = false  "
		 * +"INNER JOIN woQuotationEntry.vendorQuotation AS vendorQuotation with vendorQuotation.isActive = true AND vendorQuotation.isDeleted = false  "
		 * 
		 * +"INNER JOIN WoQuotationNegotiation.vendorQuotation AS vendorQuotation with  vendorQuotation.isActive = true AND vendorQuotation.isDeleted = false  "
		 * +" WHERE WoQuotationNegotiation.id= :negoId AND woQuotationVendor.id= :venId  AND woQuotationEntryDetail.isActive = true AND woQuotationEntryDetail.isDeleted = false "
		 * +" ORDER BY woQuotationNegotiation.updatedDate ASC  ";
		 */			 	
		
		String hql="select procurement.t_wo_quotation_entry_detail.vendor_cop as vendorcopalias,procurement.t_wo_quotation_entry_detail.expected_inclusion as expectedinclusionalias ,"
			+ "procurement.t_wo_quotation_entry_detail.payment_term as paymenttermalias,  procurement.t_wo_quotation_entry_detail.expected_exclusion as expectedexclusionalias,procurement.t_wo_quotation_entry_detail.id as id , "
			+ " procurement.t_wo_quotation_entry_detail.remark as remark from procurement.t_wo_quotation_entry_detail  "
		 	+ "inner join procurement.t_wo_quotation_entry on procurement.t_wo_quotation_entry.id=procurement.t_wo_quotation_entry_detail.quotation_entry_id  "
		    + "inner join procurement.t_wo_quotation_send_to_vendor on procurement.t_wo_quotation_send_to_vendor.id=procurement.t_wo_quotation_entry.vendor_quotation_id  "
		    + "inner join procurement.t_work_order_quotation_request on procurement.t_work_order_quotation_request.id=procurement.t_wo_quotation_send_to_vendor.quotation_request_id  "
		    + " inner join procurement.t_work_order_quotation_request_detail on procurement.t_work_order_quotation_request_detail.work_order_quotation_request_id=procurement.t_work_order_quotation_request.id  "
			+ "inner join procurement.t_wo_quotation_negotiation on procurement.t_wo_quotation_negotiation.vendor_quotation_id = procurement.t_wo_quotation_send_to_vendor.id  "
		    + "where procurement.t_work_order_quotation_request_detail.id="+detailId+" and procurement.t_wo_quotation_negotiation.id="+negoId+" and procurement.t_wo_quotation_send_to_vendor.id="+venId+" "
		    + " order by procurement.t_wo_quotation_negotiation.updated_date asc limit 1  ";
		/*
		 * String
		 * hql="select procurement.t_wo_quotation_entry_detail.vendor_cop,procurement.t_wo_quotation_entry_detail.expected_inclusion as expectedExclusion, "
		 * +
		 * " procurement.t_wo_quotation_entry_detail.expected_exclusion as expectedInclusion,procurement.t_wo_quotation_entry_detail.id as id from procurement.t_wo_quotation_entry_detail "
		 * +" inner join procurement.t_wo_quotation_entry on procurement.t_wo_quotation_entry.id=procurement.t_wo_quotation_entry_detail.quotation_entry_id "
		 * +" inner join procurement.t_wo_quotation_send_to_vendor on procurement.t_wo_quotation_send_to_vendor.id=procurement.t_wo_quotation_entry.vendor_quotation_id "
		 * +" inner join procurement.t_wo_quotation_negotiation on procurement.t_wo_quotation_negotiation.vendor_quotation_id = procurement.t_wo_quotation_send_to_vendor.id "
		 * +" where procurement.t_wo_quotation_negotiation.id="
		 * +negoId+" and procurement.t_wo_quotation_send_to_vendor.id="+venId+" "
		 * +" order by procurement.t_wo_quotation_negotiation.updated_date asc limit 1";
		 */	
		Query query = getSession().createSQLQuery(hql);
		/*
		 * query.setParameter("negoId", negoId); query.setParameter("venId", venId);
		 * query.setMaxResults(1);
		 */
		 query.setResultTransformer(Transformers.aliasToBean(WoQuotationEntryDetailResponse.class));
		return (WoQuotationEntryDetailResponse) query.uniqueResult();
	}

	
	


}
