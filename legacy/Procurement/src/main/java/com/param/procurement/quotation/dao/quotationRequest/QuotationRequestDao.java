package com.param.procurement.quotation.dao.quotationRequest;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.quotation.dto.quotationRequest.PurchaseRequestDetailsResponse;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDto;
import com.param.service.global.SessionService;

@Repository
public class QuotationRequestDao extends AbstractDao<Integer, QuotationRequest> implements IQuotationRequestDao{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<QuotationRequest> searchQuotationRequestList(QuotationRequestDto quotationRequestDto,Integer screenId) {
		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
				//+ "	INNER JOIN quotationRequest.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN quotationRequest.store as store WITH store.unit.unitId = :unitId " 
				+ "	LEFT JOIN quotationRequest.status as status "
				+ " LEFT JOIN quotationRequest.assetType as assetType "
				+ " WHERE quotationRequest.isActive = true AND quotationRequest.isDeleted = false AND quotationRequest.unit.unitId = :unitId "
				+ " 	AND quotationRequest.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY quotationRequest.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(quotationRequestDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(quotationRequestDto.getToDate()));
		query.setParameter("storeId",quotationRequestDto.getStoreId() != null ? quotationRequestDto.getStoreId() : 0);
		query.setParameter("statusId", quotationRequestDto.getStatusId() != null ? quotationRequestDto.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", quotationRequestDto.getUnitId()!=null ? quotationRequestDto.getUnitId(): sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public QuotationRequest getQuotationRequest(Integer id)
	{
		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
            + "WHERE quotationRequest.id = :id  AND quotationRequest.isActive = true AND quotationRequest.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (QuotationRequest) query.uniqueResult();
	}
	
	@Override
	public QuotationRequestDetail getQuotationRequestPendingQuantity(Integer id , Integer itemId)
	{
		String hql = "SELECT quotationRequestDetail FROM QuotationRequestDetail AS quotationRequestDetail  JOIN quotationRequestDetail.quotationRequest as quotationRequest "
            + "WHERE  quotationRequest.id = :id  AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false and quotationRequestDetail.item.id = :itemId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("itemId", itemId);
		
         //return (QuotationRequestDetail) query.uniqueResult();
		return (QuotationRequestDetail) query.list().get(0);
	}
	
	@Override
	public List<QuotationRequestDetail> getQuotationRequestDetail(Integer id)
	{
		String hql = "SELECT quotationRequestDetail FROM QuotationRequestDetail AS quotationRequestDetail "
            + " WHERE quotationRequestDetail.quotationRequest.id = :id  AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
            + " ORDER BY quotationRequestDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return  query.list();
	}
	
	@Override
	public List<PurchaseRequestDetail> getPurchaseRequestDetails(List<Integer> purchaseRequestDetailIds) {
		String hql = "SELECT purchaseRequestDetail FROM PurchaseRequestDetail AS purchaseRequestDetail \r\n"
				+ " WHERE purchaseRequestDetail.id in :purchaseRequestDetailIds "
				+ " AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameterList("purchaseRequestDetailIds", purchaseRequestDetailIds);
		return query.list();
	}
	
	@Override
	public PurchaseRequestDetail getPurchaseRequestDetails(Integer purchaseRequestDetailId) {
		String hql = "SELECT purchaseRequestDetail FROM PurchaseRequestDetail AS purchaseRequestDetail \r\n"
				+ " WHERE purchaseRequestDetail.id = :purchaseRequestDetailId "
				+ " AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("purchaseRequestDetailId", purchaseRequestDetailId);
		return (PurchaseRequestDetail) query.uniqueResult();
	}
	
	@Override
	public List<PurchaseRequestDetailsResponse> getPurchaseRequestDetail(Integer qrId) {
		
		String hql = " SELECT purchaseRequest.id AS id, purchaseRequest.purchaseNo AS prNumber ,"
				+ " purchaseRequest.addedDate AS prDate "
				+ " FROM com.param.entity.model.procurement.PurchaseRequestDetail AS purchaseRequestDetail "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest "
				+ " WHERE purchaseRequestDetail.quotationRequestDetail.id = :qrId AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
				+ " ORDER BY purchaseRequestDetail.id ";		
		
			Query query = getSession().createQuery(hql);
			query.setParameter("qrId", qrId);				
			query.setResultTransformer(Transformers.aliasToBean(PurchaseRequestDetailsResponse.class));
			return query.list();
	}
	
	@Override
	public QuotationRequest saveQuotationRequest(QuotationRequest quotationRequest) {
		getSession().save(quotationRequest);
		return quotationRequest;
		
	}
	
	@Override
	public QuotationRequest updateQuotationRequest(QuotationRequest quotationRequest) {
		getSession().update(quotationRequest);
		return quotationRequest;
	}
	
	@Override
	public Long getLeaseQunatity(Integer storeId, Integer itemId) {
		
		String hql = "SELECT SUM (COALESCE ((quotationRequestDetail.leaseQuantity),0) )"
				+ " FROM QuotationRequestDetail AS quotationRequestDetail "
				+ "INNER JOIN quotationRequestDetail.quotationRequest AS quotationRequest WITH quotationRequest.isDeleted = false AND quotationRequest.isActive = true AND quotationRequest.unit.unitId = :unitId "
				+ "WHERE quotationRequest.store.storeId = :storeId AND quotationRequestDetail.item.id = :itemId "
				+ " AND quotationRequestDetail.isDeleted = false AND quotationRequestDetail.isActive = true AND quotationRequestDetail.unit.unitId = :unitId "
				+ " AND quotationRequestDetail.status.id != :cancelStatusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		return  (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

}
