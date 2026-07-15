package com.param.procurement.purchase.dao.localPurchaseRequest;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;

@Repository("locPurchaseRequestDao")
public class LocalPurchaseRequestDao extends AbstractDao<Integer, PurchaseRequest> implements ILocalPurchaseRequestDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<PurchaseRequest> searchLocalPuchaseRequest(LocPurchaseRequestDto locPurchaseReqDto,Integer screenId,Integer localPurchaseReqId){
		  String hql = "SELECT purchaseRequest FROM PurchaseRequest AS purchaseRequest "
					//+ " 	INNER JOIN purchaseRequest.addedBy as user WITH user.unitId = :unitId "
					+ " 	LEFT JOIN purchaseRequest.store as store WITH store.unit.unitId = :unitId "
					+ " 	LEFT JOIN purchaseRequest.requestingStore as requestingStore WITH store.unit.unitId = :unitId "
					+ " 	LEFT JOIN purchaseRequest.status as status "
					+ " 	LEFT JOIN purchaseRequest.status as approvalStatus "
					+ " 	LEFT JOIN purchaseRequest.assetType as assetType "
					+ " 	LEFT JOIN purchaseRequest.requestType as requestType "
					+ " 	LEFT JOIN purchaseRequest.priority as priority "					
					+ " WHERE purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false AND purchaseRequest.unit.unitId = :unitId  AND purchaseRequest.screen.id = :localPurchaseReqId"
					+ "		AND purchaseRequest.addedDate BETWEEN :fromDate AND :toDate "
					+ " 	AND (:storeId = 0 OR store.id = :storeId) "
					+ " 	AND (:requestingStore = 0 OR requestingStore.storeId = :requestingStore) "
					//+ " 	AND (:purchaseNo = '' OR lower(purchaseRequest.purchaseNo) LIKE :purchaseNo) "
					+ " 	AND (:statusId = 0 OR status.id = :statusId) "
					+ " 	AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
					//+ " 	AND (:prTypeId = 0 OR requestType.id = :prTypeId) \r\n"
					+ " 	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " 					INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "						INNER JOIN screenStatus.status as status) "
					+ " ORDER BY purchaseRequest.id DESC ";			
					
					Query query = getSession().createQuery(hql);
					query.setParameter("fromDate", Util.getDate(locPurchaseReqDto.getFromDate()));
					query.setParameter("toDate", Util.getDate(locPurchaseReqDto.getToDate()));
					query.setParameter("storeId", locPurchaseReqDto.getStoreId() != null ? locPurchaseReqDto.getStoreId() : 0);
					query.setParameter("requestingStore", locPurchaseReqDto.getRequestingStoreId() != null ? locPurchaseReqDto.getRequestingStoreId() : 0);
					//query.setParameter("purchaseNo", locPurchaseReqDto.getPurchaseNo() != null ? locPurchaseReqDto.getPurchaseNo().toLowerCase() + '%' : "");
					query.setParameter("statusId", locPurchaseReqDto.getStatusId() != null ? locPurchaseReqDto.getStatusId() : 0);
					query.setParameter("priorityId", locPurchaseReqDto.getPriorityId() != null ? locPurchaseReqDto.getPriorityId() : 0);
					//query.setParameter("prTypeId", locPurchaseReqDto.getPrTypeId() != null ? locPurchaseReqDto.getPrTypeId() : 0);
					query.setParameter("screenId", screenId);
					query.setParameter("localPurchaseReqId", localPurchaseReqId);
					query.setParameter("unitId", locPurchaseReqDto.getUnitId()!=null ? locPurchaseReqDto.getUnitId() : sessionService.getUnitId());
                    return query.list();
	}

	@Override
	public PurchaseRequest getPurchaseRequest(Long id) {
		String hql = "SELECT purchaseRequest FROM PurchaseRequest AS purchaseRequest WHERE purchaseRequest.id = :id  AND purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (PurchaseRequest) query.uniqueResult();
	}

	@Override
	public List<PurchaseRequestDetail> getPurchaseRequestDetail(Long purchaseRequestId) {
		String hql = " SELECT purchaseRequestDetail FROM PurchaseRequestDetail AS purchaseRequestDetail \r\n"
				+ "		INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false \r\n"
				+ " WHERE purchaseRequest.id = :purchaseRequestId AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false \r\n"
				+ " ORDER BY purchaseRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("purchaseRequestId", purchaseRequestId);
		return query.list();
	}
	
	@Override
	public PurchaseRequest saveLocalPurchaseRequest(PurchaseRequest purchaseRequest) {
		  getSession().save(purchaseRequest);
			return purchaseRequest;
	}
	
	
	@Override
	public PurchaseRequest updateLocalPurchaseRequest(PurchaseRequest purchaseRequest) {
		getSession().update(purchaseRequest);
		return purchaseRequest;
	}

	@Override
	public List<StoreIndentDetail> getIndentIdList(Integer purchaseReqDetailsId) {
					
			String hql = "SELECT storeIndentDetail FROM StoreIndentDetail AS storeIndentDetail "
					   + "  INNER JOIN storeIndentDetail.purchaseRequestDetail AS purchaseRequestDetail WITH purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
					   + " WHERE purchaseRequestDetail.id = :purchaseReqDetailsId AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false "
					   + " ORDER BY storeIndentDetail.id ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("purchaseReqDetailsId", purchaseReqDetailsId);
			return query.list();
	}

	@Override
	public Integer updateStoreIndentDetailPrId(Integer indentId, Integer purchaseReqDetailsId) {
		
		String hql = "UPDATE StoreIndentDetail AS storeIndentDetail SET storeIndentDetail.purchaseRequestDetail.id = :purchaseReqDetailsId "
				+ " WHERE storeIndentDetail.id = :indentId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("purchaseReqDetailsId", purchaseReqDetailsId);
		query.setParameter("indentId", indentId);
		return query.executeUpdate();
		
			/*String sql = "UPDATE inventory.t_store_indent_detail" + " SET purchase_request_detail_id=" + purchaseReqDetailsId
					+ " WHERE id=" + indentId;
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			return query.executeUpdate();*/
	}
	
	@Override
	public Long getLeaseQunatity(Integer storeId, Integer itemId) {

		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id = :itemId AND purchaseRequestDetail.goodReceiptNoteDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		return  (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public PurchaseRequestDetail saveLocalPurchaseRequestDetail(PurchaseRequestDetail purchaseRequestDetail) {
		 getSession().save(purchaseRequestDetail);
			return purchaseRequestDetail;
	
	}
}