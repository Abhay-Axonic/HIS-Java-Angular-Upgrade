package com.param.assetManagement.auctionVendorApproval.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AuctionVendorApproval;
import com.param.entity.model.asset.AuctionVendorApprovalDetail;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.asset.VendorApprovalAuctionMapper;

@Repository
public class AuctionVendorApprovalDao extends AbstractDao<Integer, ItemAuction> implements IAuctionVendorApprovalDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<AuctionVendorApproval> searchAuctionVendorApproval(FixedAssetOpeningBalanceRequest request, Integer screenId) {

		String hql = " SELECT auctionVendorApproval FROM AuctionVendorApproval AS auctionVendorApproval "
				+ " INNER JOIN auctionVendorApproval.itemAuction AS itemAuction WITH itemAuction.isActive = true AND itemAuction.isDeleted = false AND itemAuction.unit.unitId = :unitId "
				+ " INNER JOIN itemAuction.store as store "
				+ " INNER JOIN itemAuction.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " WHERE auctionVendorApproval.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:storeId = 0 OR store.storeId = :storeId) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ "	AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " ORDER BY auctionVendorApproval.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public AuctionVendorApproval getAuctionVendorApprovalById(Integer id) {
		String hql = "SELECT auctionVendorApproval FROM AuctionVendorApproval AS auctionVendorApproval WHERE auctionVendorApproval.id = :id AND auctionVendorApproval.isActive = true AND auctionVendorApproval.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (AuctionVendorApproval) query.uniqueResult();
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
	public AuctionVendorApproval saveAuctionVendorApproval(AuctionVendorApproval auctionVendorApproval){
		getSession().update(auctionVendorApproval);

		return auctionVendorApproval;
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
	
}