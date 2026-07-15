package com.param.procurement.quotation.dao.quotationFill;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.QuotationFill;
import com.param.entity.model.procurement.QuotationFillDetail;
import com.param.entity.model.procurement.QuotationRequestAccessoriesMapper;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.QuotationVendorDetail;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillRequest;

@Repository
public class QuotationFillDao extends AbstractDao<Integer, QuotationFill> implements IQuotationFillDao {
	@Autowired
	private SessionService sessionService;

	@Override
	public List<QuotationFill> searchQuotationFillList(QuotationFillRequest quotationFillRequest, Integer screenId) {
		String hql = "SELECT quotationFill FROM QuotationFill AS quotationFill "
				+ "	INNER JOIN quotationFill.addedBy as user  " + "	LEFT JOIN quotationFill.store as store "
				+ "	LEFT JOIN quotationFill.status as status " + " LEFT JOIN quotationFill.assetType as assetType "
				+ " WHERE quotationFill.isActive = true AND quotationFill.isDeleted = false AND quotationFill.unit.unitId = :unitId "
				+ " 	AND quotationFill.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ " 	AND (:vendorId = 0 OR quotationFill.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(quotationFill.vendor.name) LIKE :vendorName ) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY quotationFill.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(quotationFillRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(quotationFillRequest.getToDate()));
		query.setParameter("storeId",
				quotationFillRequest.getStoreId() != null ? quotationFillRequest.getStoreId() : 0);
		query.setParameter("statusId",
				quotationFillRequest.getStatusId() != null ? quotationFillRequest.getStatusId() : 0);
		if (quotationFillRequest.getVendorId() != null && quotationFillRequest.getVendorId() > 0) {
			query.setParameter("vendorId",
					quotationFillRequest.getVendorId() != null ? quotationFillRequest.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (quotationFillRequest.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					quotationFillRequest.getVendorName() != null
							? quotationFillRequest.getVendorName().toLowerCase() + '%'
							: "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}

		query.setParameter("screenId", screenId);
		query.setParameter("unitId", quotationFillRequest.getUnitId()!=null ? quotationFillRequest.getUnitId(): sessionService.getUnitId());
		return query.list();
	}

	/*
	 * 
	 * @Override public List<PurchaseRequestDetail>
	 * getPurchaseRequestDetails(List<Integer> purchaseRequestDetailIds) { String
	 * hql =
	 * "SELECT purchaseRequestDetail FROM PurchaseRequestDetail AS purchaseRequestDetail \r\n"
	 * + " WHERE purchaseRequestDetail.id in :purchaseRequestDetailIds " +
	 * " AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false"
	 * ;
	 * 
	 * Query query = getSession().createQuery(hql);
	 * query.setParameterList("purchaseRequestDetailIds", purchaseRequestDetailIds);
	 * return query.list(); }
	 * 
	 * @Override public List<PurchaseRequestDetailsResponse>
	 * getPurchaseRequestDetail(Integer qrId) {
	 * 
	 * String hql =
	 * " SELECT purchaseRequest.id AS id, purchaseRequest.purchaseNo AS prNumber ,"
	 * + " purchaseRequest.addedDate AS prDate " +
	 * " FROM com.param.entity.model.procurement.PurchaseRequest AS purchaseRequest "
	 * +
	 * " WHERE purchaseRequest.quotationRequest.id = :qrId AND purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false "
	 * + " ORDER BY purchaseRequest.id ";
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("qrId",
	 * qrId); query.setResultTransformer(Transformers.aliasToBean(
	 * PurchaseRequestDetailsResponse.class)); return query.list(); }
	 */

	@Override
	public QuotationFill getQuotationFill(Integer id) {
		String hql = "SELECT quotationFill FROM QuotationFill AS quotationFill "
				+ "WHERE quotationFill.id = :id  AND quotationFill.isActive = true AND quotationFill.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (QuotationFill) query.uniqueResult();
	}

	@Override
	public List<QuotationFillDetail> getQuotationFillDetail(Integer id) {
		String hql = "SELECT quotationFillDetail FROM QuotationFillDetail AS quotationFillDetail "
				+ " WHERE quotationFillDetail.quotationFill.id = :id  AND quotationFillDetail.isActive = true AND quotationFillDetail.isDeleted = false "
				+ " ORDER BY quotationFillDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public QuotationFill saveQuotationFill(QuotationFill quotationFill) {
		getSession().save(quotationFill);
		return quotationFill;

	}

	@Override
	public QuotationFill updateQuotationFill(QuotationFill quotationFill) {
		getSession().update(quotationFill);
		return quotationFill;
	}

	@Override
	public List<QuotationVendor> getQuotationVendorSearchList(QuotationFillRequest quotationFillRequest,
			List<Integer> statusIds) {
		String hql = "SELECT quotationVendor FROM QuotationVendor AS quotationVendor "
				+ "	INNER JOIN quotationVendor.addedBy as user  " + "	LEFT JOIN quotationVendor.store as store  "
				+ " INNER JOIN quotationVendor.status AS status "
				+ " INNER JOIN quotationVendor.quotationRequest AS quotationRequest "
				+ " INNER JOIN quotationVendor.vendor AS vendor "
				+ " INNER JOIN quotationRequest.assetType AS assetType "
				+ " INNER JOIN quotationVendor.approvalStatus as approvalStatus "
				+ " WHERE quotationVendor.isActive = true AND quotationVendor.isDeleted = false AND quotationVendor.unit.unitId = :unitId "
				+ " 	AND quotationVendor.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "   	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ " 	AND (:vqNumber = '' OR lower(quotationVendor.vqNumber) LIKE :vqNumber) "
				+ "      AND quotationVendor.id NOT IN (SELECT qf.quotationVendor.id FROM QuotationFill AS qf "
				+ "			      					    WHERE qf.vendor.id = :vendorId AND qf.approvalStatus.id=5)"
				+ "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId)"
				+ "     AND (approvalStatus.id IN (:approvalStatusId)) " + " ORDER BY quotationVendor.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(quotationFillRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(quotationFillRequest.getToDate()));
		query.setParameter("storeId",
				quotationFillRequest.getStoreId() != null ? quotationFillRequest.getStoreId() : 0);
		query.setParameter("vendorId",
				quotationFillRequest.getVendorId() != null ? quotationFillRequest.getVendorId() : 0);
		query.setParameter("vqNumber",
				quotationFillRequest.getVqrNumber() != null ? quotationFillRequest.getVqrNumber().toLowerCase() + '%'
						: "");
		query.setParameter("assetTypeId",
				quotationFillRequest.getAssetTypeId() != null ? quotationFillRequest.getAssetTypeId() : 0);
		query.setParameterList("approvalStatusId", statusIds);
		query.setParameter("unitId", quotationFillRequest.getUnitId()!=null ? quotationFillRequest.getUnitId(): sessionService.getUnitId());
		return query.list();
	}

	@Override
	public QuotationVendor getQuotationVendor(Integer id) {
		String hql = "SELECT quotationVendor FROM QuotationVendor AS quotationVendor "
				+ "WHERE quotationVendor.id = :id  AND quotationVendor.isActive = true AND quotationVendor.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (QuotationVendor) query.uniqueResult();
	}

	@Override
	public QuotationVendorDetail getQuotationVendorDetail(Integer itemId, Integer vqrId) {
		String hql = "SELECT quotationVendorDetail FROM QuotationVendorDetail AS quotationVendorDetail "
				+ "INNER JOIN quotationVendorDetail.qrDetail AS qrDetail "
				+ " WHERE qrDetail.item.id = :itemId  AND quotationVendorDetail.quotationVendor.id = :vqrId "
				+ " AND quotationVendorDetail.isActive = true AND quotationVendorDetail.isDeleted = false "
				+ " ORDER BY quotationVendorDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("itemId", itemId);
		query.setParameter("vqrId", vqrId);
		return (QuotationVendorDetail) query.uniqueResult();
	}

	@Override
	public List<QuotationRequestAccessoriesMapper> getQuotationRequestAccessoriesMapperList(Integer id) {
		String hql = "SELECT qrAccessoriesMapper FROM QuotationRequestAccessoriesMapper AS qrAccessoriesMapper "
				+ " WHERE qrAccessoriesMapper.quotationRequestDetail.id = :id  AND qrAccessoriesMapper.isActive = true AND qrAccessoriesMapper.isDeleted = false "
				+ " ORDER BY qrAccessoriesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public QuotationRequestAccessoriesMapper saveQuotationRequestAccessoriesMapper(
			QuotationRequestAccessoriesMapper quotationRequestAccessoriesMapper) {
		getSession().save(quotationRequestAccessoriesMapper);
		return quotationRequestAccessoriesMapper;
	}

	@Override
	public List<QuotationRequestDetail> getQuotationRequestDetail(Integer qrId, List<Integer>  qrDetailId) {

		String hql = "SELECT quotationRequestDetail FROM QuotationRequestDetail AS quotationRequestDetail "
				+ " WHERE quotationRequestDetail.quotationRequest.id = :qrId  AND quotationRequestDetail.id not in ( :qrDetailId )  AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false ";
				//+ " ORDER BY qrAccessoriesMapper.id ";
		
		
	/*	String hqlNew =" select quotationRequestDetail QuotationFillDetail as quotationFillDetail inner join quotationRequestDetail as quotationRequestDetail  "
		+ " WHERE quotationRequestDetail.quotationRequest.id = :qrId AND quotationFillDetail.quotationFill.vendor.id = : vendorId AND quotationRequestDetail.id not in ( :qrDetailId )  AND quotationRequestDetail.isActive = true AND quotationRequestDetail.isDeleted = false ";
	*/	
		Query query = getSession().createQuery(hql);
		query.setParameter("qrId", qrId);
		query.setParameterList("qrDetailId", qrDetailId);
		return query.list();

	}

	@Override
	public void updateQuotationReqDetail(QuotationRequestDetail r) {

		getSession().update(r);
	
		
	}

	@Override
	public Integer getMapperId(Integer id) {
        Integer count=(Integer) sessionFactory.getCurrentSession().createSQLQuery(" select qr_accessories_id from procurement.t_quotation_fill_accessories where  id="+id)
                .uniqueResult();
        return count;
		
	}

	@Override
	public void updateDetailscolumn(String details, Integer mapperId) {
        sessionFactory.getCurrentSession().createSQLQuery(" update procurement.t_qr_accessories_mapper set accessories_detail='"+details+"' where  id="+mapperId)
                .executeUpdate();
        
		
	}

}
