package com.param.assetManagement.itemForAuction.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AuctionRequisitionDetail;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.asset.ItemAuctionDetail;
import com.param.entity.model.asset.VendorAuctionMapper;

@Repository
public class ItemForAuctionDao extends AbstractDao<Integer, ItemAuction> implements IItemForAuctionDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ItemAuction> searchItemAuction(FixedAssetOpeningBalanceRequest request, Integer screenId) {

		String hql = " SELECT itemAuction FROM ItemAuction AS itemAuction "
				+ " INNER JOIN itemAuction.store as store "
				+ " INNER JOIN itemAuction.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " WHERE itemAuction.isActive = true AND itemAuction.isDeleted = false AND itemAuction.unit.unitId = :unitId "
				+ "	AND itemAuction.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:storeId = 0 OR store.storeId = :storeId) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ "	AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " ORDER BY itemAuction.id DESC";

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
	public ItemAuction getItemAuctionById(Integer id) {
		String hql = "SELECT itemAuction FROM ItemAuction AS itemAuction WHERE itemAuction.id = :id AND itemAuction.isActive = true AND itemAuction.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (ItemAuction) query.uniqueResult();
	}

	@Override
	public List<ItemAuctionDetails> getItemAuctionDetail(Integer itemAuctionId) {
		String hql = "SELECT itemAuctionDetail.id AS id, auctionRequisitionDetail.id AS auctionRequisitionDetailId, item.id AS assetId, item.code AS assetCode, item.name AS assetName,"
				+ " batch.batchNo AS serialNo, batch.lotNo AS lotNo, batch.modelNumber AS modelNo, "
				+ " goodReceiptNote.id AS grnId, goodReceiptNote.grnNumber AS grnNo, goodReceiptNote.addedDate AS grnDate, "
				+ " batch.cop AS cop, auctionRequisitionDetail.approvedQuantity AS quantity FROM ItemAuctionDetail AS itemAuctionDetail \r\n"
				+ " INNER JOIN itemAuctionDetail.itemAuction AS itemAuction WITH itemAuction.isActive = true AND itemAuction.isDeleted = false \r\n "
				+ " INNER JOIN itemAuctionDetail.auctionRequisitionDetail AS auctionRequisitionDetail WITH auctionRequisitionDetail.isActive = true AND auctionRequisitionDetail.isDeleted = false "
				+ " INNER JOIN auctionRequisitionDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " LEFT JOIN batch.goodReceiptNoteDetail AS goodReceiptNoteDetail WITH goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ "	LEFT JOIN goodReceiptNoteDetail.goodReceiptNote AS goodReceiptNote WITH goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
				+ " WHERE itemAuction.id = :itemAuctionId AND itemAuctionDetail.isActive = true AND itemAuctionDetail.isDeleted = false \r\n"
				+ " ORDER BY itemAuctionDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemAuctionId", itemAuctionId);
		query.setResultTransformer(Transformers.aliasToBean(ItemAuctionDetails.class));
		return query.list();
	}
	
	@Override
	public AuctionRequisitionDetail getAuctionRequisitionDetail(Integer id)
	{
		String hql = "SELECT auctionRequisitionDetail FROM AuctionRequisitionDetail AS auctionRequisitionDetail "
				+ " INNER JOIN auctionRequisitionDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " WHERE auctionRequisitionDetail.id = :id  AND auctionRequisitionDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (AuctionRequisitionDetail) query.uniqueResult();
	}
	
	@Override
	public ItemAuctionDetail getItemAuctionDetailById(Integer itemAuctionDetailId)
	{
		String hql = "SELECT itemAuctionDetail FROM ItemAuctionDetail AS itemAuctionDetail "
				+ " WHERE itemAuctionDetail.id = :id  AND itemAuctionDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", itemAuctionDetailId);

		return (ItemAuctionDetail) query.uniqueResult();
	}
	
	@Override
	public ItemAuction saveItemAuction(ItemAuction itemAuction){
		getSession().update(itemAuction);

		return itemAuction;
	}
	
	@Override
	public VendorAuctionMapper getVendorAuctionMapper(Integer id)
	{
		String hql = "SELECT vendorAuctionMapper FROM VendorAuctionMapper AS vendorAuctionMapper "
				+ " WHERE vendorAuctionMapper.id = :id  AND vendorAuctionMapper.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (VendorAuctionMapper) query.uniqueResult();
	}
}