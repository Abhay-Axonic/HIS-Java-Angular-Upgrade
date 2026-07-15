package com.param.inventory.store.dao.shelfAllocation;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.ShelfItem;
import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationRequest;
import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationResponse;

@Repository
public class ShelfAllocationDao extends AbstractDao<Integer, ShelfItem> implements IShelfAllocationDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ShelfAllocationResponse> getAllocatedItemList(ShelfAllocationRequest request) {

		String hql = "SELECT shelfItem.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, "
				+ "		batch.expiryDate AS expiry, rack.id AS rackId, rack.name AS rack, shelf.id AS shelfId, shelf.name AS shelf, store.storeId AS storeId, "
				+ "		COALESCE(storeStock.availableQty, 0) AS availableQuantity, shelfItem.isActive AS status "
				+ "	FROM ShelfItem AS shelfItem "
				+ "		INNER JOIN shelfItem.rack AS rack WITH rack.isActive = true AND rack.isDeleted = false "
				+ "		INNER JOIN shelfItem.shelf AS shelf WITH shelf.isActive = true AND shelf.isDeleted = false "
				+ "		INNER JOIN shelfItem.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.storeId = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "	WHERE shelfItem.isActive = true AND shelfItem.isDeleted = false AND shelfItem.unit.unitId = :unitId AND storeStock.availableQty >0 "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND (:itemId = 0 OR item.id = :itemId) "
				+ "		AND ((:itemId != 0 AND :itemCode != '') OR :itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND ((:itemId != 0 AND :itemName != '') OR :itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ "		AND (:batchId = 0L OR batch.id = :batchId) "
				+ "		AND ((:batchId != 0L AND :batchNo != '') OR :batchNo = '' OR upper(batch.batchNo) like upper(:batchNo)) "
				+ "	ORDER BY shelfItem.id, item.id, batch.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("batchId", request.getBatchId() != null ? request.getBatchId() : 0L);
		query.setParameter("batchNo", request.getBatchNo() != null ? request.getBatchNo() + '%' : "");
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ShelfAllocationResponse.class));

		return query.list();
	}
	
	@Override
	public List<ShelfAllocationResponse> getUnAllocatedItemList(ShelfAllocationRequest request) {

		String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ "		COALESCE(storeStock.availableQty, 0) AS availableQuantity, store.storeId AS storeId "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.storeId = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "	WHERE batch.isActive = true AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.unit.unitId = :unitId AND storeStock.availableQty >0 "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND (:itemId = 0 OR item.id = :itemId) "
				+ "		AND ((:itemId != 0 AND :itemCode != '') OR :itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND ((:itemId != 0 AND :itemName != '') OR :itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ "		AND (:batchId = 0L OR batch.id = :batchId) "
				+ "		AND ((:batchId != 0L AND :batchNo != '') OR :batchNo = '' OR upper(batch.batchNo) like upper(:batchNo)) "
				+ "		AND (:storeId = 0 OR productCategory.id IN (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "		AND (batch.id NOT IN (SELECT batch.id FROM ShelfItem AS shelfItem "
				+ "									INNER JOIN shelfItem.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ "									INNER JOIN shelfItem.store AS store WITH store.storeId = :storeId AND store.isActive = true AND store.isDeleted = false "
				+ "								WHERE shelfItem.isDeleted = false )) "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId", request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("batchId", request.getBatchId() != null ? request.getBatchId() : 0L);
		query.setParameter("batchNo", request.getBatchNo() != null ? request.getBatchNo() + '%' : "");
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ShelfAllocationResponse.class));

		return query.list();
	}
	
	@Override
	public List<ShelfAllocationResponse> getAllocatedItems(Integer storeId, Integer rackId, Integer shelfId, Integer unitId) {

		String hql = "SELECT item.code AS itemCode, item.name AS itemName, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ "		COALESCE(storeStock.availableQty, 0) AS availableQuantity, rack.name AS rack, shelf.name AS shelf "
				+ "	FROM ShelfItem AS shelfItem "
				+ "		INNER JOIN shelfItem.rack AS rack WITH rack.isActive = true AND rack.isDeleted = false "
				+ "		INNER JOIN shelfItem.shelf AS shelf WITH shelf.isActive = true AND shelf.isDeleted = false "
				+ "		INNER JOIN shelfItem.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.store.storeId = :storeId AND storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "	WHERE shelfItem.isDeleted = false AND shelfItem.unit.unitId = :unitId "
				+ "		AND rack.id = :rackId AND shelf.id = :shelfId "
				+ "	ORDER BY shelfItem.id, item.id, batch.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("rackId", rackId);
		query.setParameter("shelfId", shelfId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ShelfAllocationResponse.class));

		return query.list();
	}
	
	@Override
	public ShelfItem getShelfItem(Integer id,Integer unitId) {
		String hql = "SELECT shelfItem FROM ShelfItem AS shelfItem "
				+ "	WHERE shelfItem.id = :id AND shelfItem.isDeleted = false AND shelfItem.unit.unitId = :unitId ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

		return (ShelfItem) query.uniqueResult();
	}
	
	@Override
	public ShelfItem updateShelfItem(ShelfItem shelfItem) {
		getSession().update(shelfItem);

		return shelfItem;
	}
}