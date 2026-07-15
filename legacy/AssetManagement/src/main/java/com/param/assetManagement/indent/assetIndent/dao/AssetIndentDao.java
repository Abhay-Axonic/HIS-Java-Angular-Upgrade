package com.param.assetManagement.indent.assetIndent.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AssetIndent;
import com.param.entity.model.asset.AssetIndentAccessories;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.asset.AuctionRequisitionDetail;
import com.param.entity.model.asset.ItemAuction;

@Repository
public class AssetIndentDao extends AbstractDao<Integer, ItemAuction> implements IAssetIndentDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<AssetIndent> searchAssetIndent(AssetIndentRequest request, Integer screenId) {

		String hql = " SELECT assetIndent FROM AssetIndent AS assetIndent "
				+ " INNER JOIN assetIndent.fromStore AS fromStore WITH fromStore.isActive = true AND fromStore.isDeleted = false "
				+ " INNER JOIN assetIndent.toStore AS toStore WITH toStore.isActive = true AND toStore.isDeleted = false "
				+ " INNER JOIN assetIndent.indentType AS indentType WITH indentType.isActive = true AND indentType.isDeleted = false "
				+ " INNER JOIN assetIndent.priority AS priority WITH priority.status = 'A' "
				+ " INNER JOIN assetIndent.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " WHERE assetIndent.addedDate BETWEEN :fromDate AND :toDate AND assetIndent.isActive = true AND assetIndent.isDeleted = false "
				+ " AND assetIndent.unit.unitId = :unitId "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR toStore.storeId = :toStoreId ) "
				+ " AND (:indentNo = '' OR lower(assetIndent.indentNo) LIKE :indentNo) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " 					INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ " 					INNER JOIN screenStatus.status as status) "
				+ "	ORDER BY priority.priorityId ASC, assetIndent.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query.setParameter("indentNo", request.getIndentNo() != null ? '%'+request.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());

		return query.list();
	}

	@Override
	public AssetIndent getAssetIndentById(Integer id) {
		String hql = "SELECT assetIndent FROM AssetIndent AS assetIndent WHERE assetIndent.id = :id AND assetIndent.isActive = true AND assetIndent.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (AssetIndent) query.uniqueResult();
	}

	@Override
	public List<AssetIndentDetail> getAssetIndentDetail(Integer assetIndentId) {
		String hql = "SELECT assetIndentDetail FROM AssetIndentDetail AS assetIndentDetail "
				+ " INNER JOIN assetIndentDetail.indent AS assetIndent WITH assetIndent.isActive = true AND assetIndent.isDeleted = false "
				+ " INNER JOIN assetIndentDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " WHERE assetIndentDetail.isActive = true AND assetIndentDetail.isDeleted = false AND assetIndent.id = :id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", assetIndentId);
		return query.list();
	}

	@Override
	public List<AssetIndentAccessories> getAssetIndentAccessories(Integer assetIndentId) {
		String hql = "SELECT assetIndentAccessories FROM AssetIndentAccessories AS assetIndentAccessories "
				+ " INNER JOIN assetIndentAccessories.indentDetail AS assetIndentDetail WITH assetIndentDetail.isActive = true AND assetIndentDetail.isDeleted = false "
				+ " INNER JOIN assetIndentAccessories.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " WHERE assetIndentAccessories.isActive = true AND assetIndentAccessories.isDeleted = false AND assetIndentDetail.id = :id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", assetIndentId);
		return query.list();
	}

	@Override
	public AuctionRequisitionDetail getAuctionRequisitionDetail(Integer id) {
		String hql = "SELECT auctionRequisitionDetail FROM AuctionRequisitionDetail AS auctionRequisitionDetail "
				+ " INNER JOIN auctionRequisitionDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " WHERE auctionRequisitionDetail.id = :id  AND auctionRequisitionDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (AuctionRequisitionDetail) query.uniqueResult();
	}

	@Override
	public AssetIndent saveAssetIndent(AssetIndent assetIndent) {
		getSession().save(assetIndent);

		return assetIndent;
	}

	@Override
	public AssetIndent updateAssetIndent(AssetIndent assetIndent) {
		getSession().update(assetIndent);

		return assetIndent;
	}

}