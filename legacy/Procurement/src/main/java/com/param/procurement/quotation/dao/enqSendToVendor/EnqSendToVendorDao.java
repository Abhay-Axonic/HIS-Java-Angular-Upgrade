package com.param.procurement.quotation.dao.enqSendToVendor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.QuotationVendorDetail;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultipleVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.MultipleVendors;
import com.param.procurement.quotation.dto.quotationRequest.PurchaseRequestDetailsResponse;

@Repository
public class EnqSendToVendorDao  extends AbstractDao<Integer, QuotationVendor> implements IEnqSendToVendorDao{
	@Autowired
	private SessionService sessionService;

	@Override
	public List<QuotationVendor> getQuotationVendorSearchList(EnqSendToVendorReqDto enqSendToVendorReqDto,Integer screenId) {
		String hql = "SELECT quotationVendor FROM QuotationVendor AS quotationVendor "
				+ "	INNER JOIN quotationVendor.addedBy as user  " 
				+ "	LEFT JOIN quotationVendor.store as store  " 
				+ "	LEFT JOIN quotationVendor.status as status "
				+ " WHERE quotationVendor.isActive = true AND quotationVendor.isDeleted = false AND quotationVendor.unit.unitId = :unitId "
				+ " 	AND quotationVendor.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " 	AND (:vendorId = 0 OR quotationVendor.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(quotationVendor.vendor.name) LIKE :vendorName ) "
				+ " 	AND (:qrNumber = '' OR lower(quotationVendor.quotationRequest.qrNo) LIKE :qrNumber ) "
				+ " 	AND (:vqrNumber = '' OR lower(quotationVendor.vqNumber) LIKE :vqrNumber ) "
				+ " ORDER BY quotationVendor.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(enqSendToVendorReqDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(enqSendToVendorReqDto.getToDate()));
		query.setParameter("storeId",enqSendToVendorReqDto.getStoreId() != null ? enqSendToVendorReqDto.getStoreId() : 0);
		query.setParameter("statusId", enqSendToVendorReqDto.getStatusId() != null ? enqSendToVendorReqDto.getStatusId() : 0);
		if(enqSendToVendorReqDto.getVendorId()!=null && enqSendToVendorReqDto.getVendorId()>0) {
			query.setParameter("vendorId",enqSendToVendorReqDto.getVendorId() !=null ? enqSendToVendorReqDto.getVendorId() : 0);
			query.setParameter("vendorName", "");
		}else if(enqSendToVendorReqDto.getVendorName()!=null){
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",enqSendToVendorReqDto.getVendorName() !=null ? enqSendToVendorReqDto.getVendorName().toLowerCase() + '%' : "");
		}else
		{
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}
		
		query.setParameter("qrNumber",enqSendToVendorReqDto.getQrNumber() !=null ? '%'+enqSendToVendorReqDto.getQrNumber().toLowerCase() + '%' : "");
		query.setParameter("vqrNumber",enqSendToVendorReqDto.getVqrNumber() !=null ? '%'+enqSendToVendorReqDto.getVqrNumber().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", enqSendToVendorReqDto.getUnitId()!=null ? enqSendToVendorReqDto.getUnitId(): sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public QuotationVendor getQuotationVendor(Integer id)
	{
		String hql = "SELECT quotationVendor FROM QuotationVendor AS quotationVendor "
            + "WHERE quotationVendor.id = :id  AND quotationVendor.isActive = true AND quotationVendor.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (QuotationVendor) query.uniqueResult();
	}
	
	@Override
	public List<QuotationVendorDetail> getQuotationVendorDetail(Integer id)
	{
		String hql = "SELECT quotationVendorDetail FROM QuotationVendorDetail AS quotationVendorDetail "
            + " WHERE quotationVendorDetail.quotationVendor.id = :id  AND quotationVendorDetail.isActive = true AND quotationVendorDetail.isDeleted = false "
            + " ORDER BY quotationVendorDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return  query.list();
	}
	
	@Override
	public List<PurchaseRequestDetailsResponse> getPurchaseRequestDetail(Integer qrDetailId) {
		
		String hql = " SELECT purchaseRequest.id AS id, purchaseRequest.purchaseNo AS prNumber ,"
				+ " purchaseRequest.addedDate AS prDate "
				+ " FROM com.param.entity.model.procurement.PurchaseRequestDetail AS purchaseRequestDetail "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest "
				+ " WHERE purchaseRequestDetail.quotationRequestDetail.id = :qrDetailId AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
				+ " ORDER BY purchaseRequestDetail.id ";		
		
			Query query = getSession().createQuery(hql);
			query.setParameter("qrDetailId", qrDetailId);				
			query.setResultTransformer(Transformers.aliasToBean(PurchaseRequestDetailsResponse.class));
			return query.list();
	}
	
	
	@Override
	public QuotationVendor saveQuotationVendor(QuotationVendor quotationVendor) {
		getSession().save(quotationVendor);
		return quotationVendor;
		
	}
	
	@Override
	public QuotationVendor updateQuotationVendor(QuotationVendor quotationVendor) {
		getSession().update(quotationVendor);
		return quotationVendor;
	}
	
	@Override
	public QuotationRequest getQuotationRequestList(Integer qrId){
		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
				+ " WHERE quotationRequest.id = :qrId AND quotationRequest.isActive = true AND quotationRequest.isDeleted = false "
				+ " ORDER BY quotationRequest.id ";	
		
		Query query = getSession().createQuery(hql);
		query.setParameter("qrId", qrId);				
		return (QuotationRequest) query.uniqueResult();
	}
	
	@Override
	public QuotationRequestDetail getQuotationRequestDetailsList(Integer itemId,Integer qrId){
		String hql = "SELECT quotationRequestDetail FROM QuotationRequestDetail AS quotationRequestDetail "
				+ " WHERE quotationRequestDetail.item.id = :itemId AND quotationRequestDetail.quotationRequest.id = :qrId "
				+ " AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false "
				+ " ORDER BY quotationRequestDetail.id ";	
		
		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);		
		query.setParameter("qrId", qrId);
		return (QuotationRequestDetail) query.uniqueResult();
	}
	
	@Override
	public List<QuotationRequest> getQuotationRequestSearchList(EnqSendToVendorReqDto enqSendToVendorReqDto,List<Integer> statusIds) {
		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
				+ "	INNER JOIN quotationRequest.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN quotationRequest.store as store WITH store.unit.unitId = :unitId "
				+ "	LEFT JOIN quotationRequest.assetType as assetType WITH assetType.unit.unitId = :unitId "
				+ " INNER JOIN quotationRequest.approvalStatus as approvalStatus "
				+ " WHERE quotationRequest.isActive = true AND quotationRequest.isDeleted = false AND quotationRequest.unit.unitId = :unitId "
				+ " 	AND quotationRequest.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "		AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
			    + " 	AND (:qrNumber = '' OR lower(quotationRequest.qrNo) LIKE :qrNumber) "
			    + "      AND ( (quotationRequest.id NOT IN (SELECT qv.quotationRequest.id FROM QuotationVendor AS qv "  
			    + "			      					    WHERE qv.vendor.id=:vendorId ) )"
			    + "				OR (quotationRequest.id  IN (SELECT qv.quotationRequest.id FROM QuotationVendor AS qv "  + 
			    "			  	    WHERE qv.status.id = :CancelledStatusId and  qv.vendor.id=:vendorId ) ) )"
			    + "     AND (approvalStatus.id IN (:approvalStatusId))"
			    + " ORDER BY quotationRequest.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(enqSendToVendorReqDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(enqSendToVendorReqDto.getToDate()));
		query.setParameter("storeId",enqSendToVendorReqDto.getStoreId() != null ? enqSendToVendorReqDto.getStoreId() : 0);
		query.setParameter("assetTypeId",enqSendToVendorReqDto.getAssetTypeId() != null ? enqSendToVendorReqDto.getAssetTypeId() : 0);
		query.setParameter("vendorId",enqSendToVendorReqDto.getVendorId() !=null ? enqSendToVendorReqDto.getVendorId() : 0);
		query.setParameter("qrNumber",enqSendToVendorReqDto.getQrNumber() !=null ? enqSendToVendorReqDto.getQrNumber().toLowerCase() + '%' : "");
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("unitId", enqSendToVendorReqDto.getUnitId()!=null ? enqSendToVendorReqDto.getUnitId(): sessionService.getUnitId());
		query.setParameter("CancelledStatusId", com.param.common.constants.Status.Cancelled.getStatus());

		return query.list();
	}

	@Override
	public List<QuotationRequest> getQuotationRequestMultipleSearchList(
			EnqSendToMultipleVendorReqDto enqSendToVendorReqDto, List<Integer> statusIds) {

		String hql = "SELECT quotationRequest FROM QuotationRequest AS quotationRequest "
				//+ "	INNER JOIN quotationRequest.addedBy as user WITH user.unitId = :unitId " 
				//+ "	LEFT JOIN quotationRequest.store as store WITH store.unit.unitId = :unitId "
				+ "	LEFT JOIN quotationRequest.store as store "
				//+ "	LEFT JOIN quotationRequest.assetType as assetType WITH assetType.unit.unitId = :unitId "
				+ "	LEFT JOIN quotationRequest.assetType as assetType "
				+ " INNER JOIN quotationRequest.approvalStatus as approvalStatus "
				+ " WHERE quotationRequest.isActive = true AND quotationRequest.isDeleted = false AND quotationRequest.unit.unitId = :unitId "
				+ " 	AND quotationRequest.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "		AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
			    + " 	AND (:qrNumber = '' OR lower(quotationRequest.qrNo) LIKE :qrNumber) "
			    + "      AND ( (quotationRequest.id NOT IN (SELECT qv.quotationRequest.id FROM QuotationVendor AS qv "  
			    + "			      					    WHERE VENDOR ) )"
			    + "				OR (quotationRequest.id  IN (SELECT qv.quotationRequest.id FROM QuotationVendor AS qv "  + 
			    "			  	    WHERE qv.status.id = :CancelledStatusId and  VENDOR ) ) )"
			    + "     AND (approvalStatus.id IN (:approvalStatusId))"
			    + " ORDER BY quotationRequest.id DESC";

		List<Integer> venderIds = new ArrayList<>();
		for(MultipleVendors mv : enqSendToVendorReqDto.getVenders()) {
			venderIds.add(mv.getVendorId());
		}

		if(venderIds != null && !venderIds.isEmpty()){
			StringBuilder appQuery =  new StringBuilder(" qv.vendor.id in ( :vendorId) ");
			hql = hql.replace("VENDOR", appQuery.toString());
		}else{
			hql = hql.replace("VENDOR", " ");
		}

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(enqSendToVendorReqDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(enqSendToVendorReqDto.getToDate()));
		query.setParameter("storeId",enqSendToVendorReqDto.getStoreId() != null ? enqSendToVendorReqDto.getStoreId() : 0);
		query.setParameter("assetTypeId",enqSendToVendorReqDto.getAssetTypeId() != null ? enqSendToVendorReqDto.getAssetTypeId() : 0);
		//query.setParameter("vendorId",enqSendToVendorReqDto.getVendorId() !=null ? enqSendToVendorReqDto.getVendorId() : 0);
		query.setParameter("qrNumber",enqSendToVendorReqDto.getQrNumber() !=null ? '%'+enqSendToVendorReqDto.getQrNumber().toLowerCase() + '%' : "");
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("unitId", enqSendToVendorReqDto.getUnitId()!=null ? enqSendToVendorReqDto.getUnitId(): sessionService.getUnitId());
		query.setParameter("CancelledStatusId", com.param.common.constants.Status.Cancelled.getStatus());

		if(venderIds != null && !venderIds.isEmpty()){
			query.setParameterList("vendorId", venderIds);
		}

		return query.list();
	
	}
}
