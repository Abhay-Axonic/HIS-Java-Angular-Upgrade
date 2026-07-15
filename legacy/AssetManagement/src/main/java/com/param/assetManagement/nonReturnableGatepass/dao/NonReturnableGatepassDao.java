package com.param.assetManagement.nonReturnableGatepass.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssRequest;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssResponse;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssResponseItem;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AuctionVendorApprovalDetail;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.asset.NonReturnableGatepass;
import com.param.entity.model.asset.VendorApprovalAuctionMapper;

@Repository
public class NonReturnableGatepassDao extends AbstractDao<Integer, ItemAuction> implements INonReturnableGatepssDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<NonReturnableGatepass> searchNonReturnableGatepass(NonReturnableGatepssRequest request, Integer screenId) {

		String hql = " SELECT nonReturnableGatepass FROM NonReturnableGatepass AS nonReturnableGatepass "
				+ " INNER JOIN nonReturnableGatepass.store AS store "
				+ " INNER JOIN nonReturnableGatepass.addedBy AS user WITH user.status = 'A' "
				+ "	INNER JOIN nonReturnableGatepass.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ " INNER JOIN nonReturnableGatepass.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " WHERE nonReturnableGatepass.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:storeId = 0 OR store.storeId = :storeId) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ " AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ " AND (:nonReturnableGatepassNo = '' OR lower(nonReturnableGatepass.nonRetGpNo) LIKE :nonReturnableGatepassNo ) "
				+ "	AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " ORDER BY nonReturnableGatepass.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("nonReturnableGatepassNo", request.getNonReturnableGatepassNo() != null ? request.getNonReturnableGatepassNo().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public NonReturnableGatepass getNonReturnableGatepassById(Integer id) {
		String hql = "SELECT nonReturnableGatepass FROM NonReturnableGatepass AS nonReturnableGatepass WHERE nonReturnableGatepass.id = :id AND nonReturnableGatepass.isActive = true AND nonReturnableGatepass.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (NonReturnableGatepass) query.uniqueResult();
	}

	@Override
	public List<ItemAuctionDetails> getAuctionVendorApprovalDetail(Integer id) {
		String hql = "SELECT auctionVendorApprovalDetail.id AS id, auctionVendorApproval.id AS auctionVendorApprovalId, itemAuctionDetail.id AS itemAuctionDetailId,"
				+ " item.id AS assetId, item.code AS assetCode, item.name AS assetName FROM AuctionVendorApprovalDetail AS auctionVendorApprovalDetail"
				+ " INNER JOIN auctionVendorApprovalDetail.itemAuctionDetail AS itemAuctionDetail WITH itemAuctionDetail.isActive = true AND itemAuctionDetail.isDeleted = false "
				+ " INNER JOIN auctionVendorApprovalDetail.auctionVendorApproval AS auctionVendorApproval WITH auctionVendorApproval.isActive = true AND auctionVendorApproval.isDeleted = false "
				+ " INNER JOIN itemAuctionDetail.itemAuction AS itemAuction WITH itemAuction.isActive = true AND itemAuction.isDeleted = false \r\n "
				+ " INNER JOIN itemAuctionDetail.auctionRequisitionDetail AS auctionRequisitionDetail WITH auctionRequisitionDetail.isActive = true AND auctionRequisitionDetail.isDeleted = false "
				+ " INNER JOIN auctionRequisitionDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " WHERE auctionVendorApproval.id = :id AND auctionVendorApprovalDetail.isActive = true AND auctionVendorApprovalDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemAuctionDetails.class));
		return query.list();
	}
	
	@Override
	public AuctionVendorApprovalDetail getAuctionAuctionVendorApprovalDetail(Integer id)
	{
		String hql = "SELECT auctionVendorApprovalDetail FROM AuctionVendorApprovalDetail AS auctionVendorApprovalDetail "
				+ " WHERE auctionVendorApprovalDetail.id = :id  AND auctionVendorApprovalDetail.isDeleted = false AND auctionVendorApprovalDetail.isActive = true ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (AuctionVendorApprovalDetail) query.uniqueResult();
	}

	@Override
	public VendorApprovalAuctionMapper getVendorApprovalAuctionMapper(Integer id)
	{
		String hql = "SELECT vendorApprovalAuctionMapper FROM VendorApprovalAuctionMapper AS vendorApprovalAuctionMapper "
				+ " WHERE vendorApprovalAuctionMapper.id = :id  AND vendorApprovalAuctionMapper.isDeleted = false AND vendorApprovalAuctionMapper.isActive = true ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (VendorApprovalAuctionMapper) query.uniqueResult();
	}
	
	@Override
	public NonReturnableGatepass saveNonReturnableGatepass(NonReturnableGatepass nonReturnableGatepass)
	{
		getSession().save(nonReturnableGatepass);
		return nonReturnableGatepass;
	}
	
	@Override
	public NonReturnableGatepass updateNonReturnableGatepass(NonReturnableGatepass nonReturableGatepass)
	{
		getSession().update(nonReturableGatepass);
		return nonReturableGatepass;
	}
	
	@Override
	public List<NonReturnableGatepssResponse> getAuctionVendorApprovalList(NonReturnableGatepssRequest request)
	{
		String hql = " SELECT auctionVendorApproval.id AS id, itemAuction.requestNo AS requestNo, itemAuction.addedDate AS requestDate, "
				+ " auctionRequision.auctionRequisitionNo AS againstRequisition, user.userId AS approveById, user.userName AS approveBy, auctionRequision.id AS auctionRequisitionId FROM AuctionVendorApproval AS auctionVendorApproval "
				+ " INNER JOIN auctionVendorApproval.itemAuction AS itemAuction WITH itemAuction.isActive = true AND itemAuction.isDeleted = false "
				+ " INNER JOIN itemAuction.auctionRequision AS auctionRequision WITH auctionRequision.isActive = true AND auctionRequision.isDeleted = false "
				+ " INNER JOIN auctionRequision.approvedBy AS user WITH user.status = 'A' "
				+ " WHERE auctionVendorApproval.addedDate BETWEEN :fromDate AND :toDate AND auctionVendorApproval.unit.unitId = :unitId "
				+ " AND (:requestNo = '' OR lower(itemAuction.requestNo) LIKE :requestNo ) "
				+ " ORDER BY auctionVendorApproval.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("requestNo", request.getRequestNo() != null ? request.getRequestNo().toLowerCase() + '%' : "");
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(NonReturnableGatepssResponse.class));
		return query.list();
	}
	
	@Override
	public List<NonReturnableGatepssResponse> getAuctionVendorApprovalVendorList(NonReturnableGatepssRequest request, Integer id)
	{
		String hql = " SELECT vendor.id AS vendorId, vendor.name AS vendor FROM VendorApprovalAuctionMapper AS vendorApprovalAuctionMapper "
				+ "	   INNER JOIN vendorApprovalAuctionMapper.auctionVendorApprovalDetail AS auctionVendorApprovalDetail WITH auctionVendorApprovalDetail.isActive = true AND auctionVendorApprovalDetail.isDeleted = false "
				+ "	   INNER JOIN auctionVendorApprovalDetail.auctionVendorApproval AS auctionVendorApproval WITH auctionVendorApproval.isActive = true AND auctionVendorApproval.isDeleted = false "
				+ "    INNER JOIN auctionVendorApprovalDetail.vendorApprovalAuctionMapper AS vendorApprovalAuctionMapper WITH vendorApprovalAuctionMapper.isActive = true AND vendorApprovalAuctionMapper.isDeleted = false "
				+ "    INNER JOIN vendorApprovalAuctionMapper.vendorAuctionMapper AS vendorAuctionMapper WITH vendorAuctionMapper.isActive = true AND vendorAuctionMapper.isDeleted = false "
				+ "    INNER JOIN vendorAuctionMapper.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "    WHERE auctionVendorApproval.id = :id AND auctionVendorApproval.isActive = true AND auctionVendorApproval.isDeleted = false "
				+ "    AND (:vendorId = 0 OR vendor.id = :vendorId) ";
		Query query = getSession().createQuery(hql);
		query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(NonReturnableGatepssResponse.class));
		return query.list();
	}
	
	@Override
	public List<NonReturnableGatepssResponseItem> getAuctionVendorApprovalItemList(Integer id)
	{
		String hql = " SELECT auctionVendorApprovalDetail.id AS id, item.id AS itemId, item.name AS itemName, item.code AS itemCode, auctionRequisitionDetail.auctionQuantity AS auctionQuantity, "
				+ "	   auctionRequisitionDetail.approvedQuantity AS pendingAuctionQuantity, auctionRequisitionDetail.auctionPrice AS auctionAmount, batch.id AS batchId, batch.batchNo AS batchNo, auctionRequisitionDetail.id AS auctionRequisitionDetailId FROM AuctionVendorApproval AS auctionVendorApproval "
				+ "	   INNER JOIN auctionVendorApproval.auctionVendorApprovalDetail AS auctionVendorApprovalDetail WITH auctionVendorApprovalDetail.isActive = true AND auctionVendorApprovalDetail.isDeleted  = false "
				+ "    INNER JOIN auctionVendorApprovalDetail.itemAuctionDetail AS itemAuctionDetail WITH itemAuctionDetail.isActive = true AND itemAuctionDetail.isDeleted  = false "
				+ "    INNER JOIN itemAuctionDetail.auctionRequisitionDetail AS auctionRequisitionDetail WITH auctionRequisitionDetail.isActive = true AND auctionRequisitionDetail.isDeleted  = false "
				+ "    INNER JOIN auctionRequisitionDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted  = false "
				+ "    INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "	   WHERE auctionVendorApproval.id = :id AND auctionVendorApproval.isActive = true AND auctionVendorApproval.isDeleted = false ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(NonReturnableGatepssResponseItem.class));
		return query.list();
	}
	
}