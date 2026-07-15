package com.param.assetManagement.fixedAssetOpeningBalance.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchAccessoriesMapper;
import com.param.entity.model.inventory.OpeningBalance;
import com.param.entity.model.inventory.OpeningBalanceAccessories;
import com.param.entity.model.inventory.OpeningBalanceDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;

@Repository
public class FixedAssetOpeningBalanceDao extends AbstractDao<Integer, OpeningBalance> implements IFixedAssetOpeningBalanceDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<OpeningBalance> searchOpeningBalance(FixedAssetOpeningBalanceRequest request, Integer screenId) {

		String hql = " SELECT openingBalance FROM OpeningBalance AS openingBalance "
				+ " INNER JOIN openingBalance.store as store "
				+ " INNER JOIN openingBalance.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " WHERE openingBalance.id IN (SELECT openingBalance1 FROM OpeningBalance AS openingBalance1 "
				+ "			INNER JOIN openingBalance1.openingBalanceDetailList AS openingBalanceDetailList WITH openingBalanceDetailList.isActive = true AND openingBalanceDetailList.isDeleted = false "
				+ "			INNER JOIN openingBalanceDetailList.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "  		INNER JOIN item.productCategory AS productCategory WITH productCategory.isActive = true AND productCategory.isDeleted = false "
				+ "			INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
				+ "			INNER JOIN assetCategory.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false AND assetType.id = 2 ) "
				+ " AND openingBalance.isActive = true AND openingBalance.isDeleted = false AND openingBalance.unit.unitId = :unitId "
				+ "	AND openingBalance.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:storeId = 0 OR store.storeId = :storeId) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ "	AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " ORDER BY openingBalance.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",  request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId",  request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public OpeningBalance getOpeningBalace(Integer id) {
		String hql = "SELECT openingBalance FROM OpeningBalance AS openingBalance WHERE openingBalance.id = :id AND openingBalance.isActive = true AND openingBalance.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (OpeningBalance) query.uniqueResult();
	}

	@Override
	public List<OpeningBalanceDetail> getOpeningbalaceDetail(Integer openingBalanceId) {
		String hql = "SELECT openingBalanceDetail FROM OpeningBalanceDetail AS openingBalanceDetail \r\n"
				+ " INNER JOIN openingBalanceDetail.openingBalance AS openingBalance WITH openingBalance.isActive = true AND openingBalance.isDeleted = false \r\n"
				+ " WHERE openingBalance.id = :openingBalanceId AND openingBalanceDetail.isActive = true AND openingBalanceDetail.isDeleted = false \r\n"
				+ " ORDER BY openingBalanceDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("openingBalanceId", openingBalanceId);

		return query.list();
	}
	
	@Override
	public OpeningBalance saveOpeningBalance(OpeningBalance openingBalance) {
		getSession().save(openingBalance);

		return openingBalance;
	}
	
	@Override
	public OpeningBalance updateOpeningBalance(OpeningBalance openingBalance) {
		getSession().update(openingBalance);

		return openingBalance;
	}
	
	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}
	
	@Override
	public BatchAccessoriesMapper saveBatchAccessoriesMapper(BatchAccessoriesMapper batchAccessoriesMapper)
	{
		getSession().save(batchAccessoriesMapper);
		return batchAccessoriesMapper;
	}

	@Override
	public StoreStock saveStoreStock(StoreStock storeStock) {
		getSession().save(storeStock);
		return storeStock;
	}

	@Override
	public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
		getSession().save(stockTransaction);
		return stockTransaction;
	}

	@Override
	public List<OpeningBalanceAccessories> getAccessoriesData(Integer id) {

	

		String hql = "SELECT openingBalanceAccessories FROM OpeningBalanceAccessories AS openingBalanceAccessories \r\n"
				+ " INNER JOIN openingBalanceAccessories.openingBalanceDetail AS openingBalanceDetail WITH openingBalanceDetail.isActive = true AND openingBalanceDetail.isDeleted = false \r\n"
				+ " WHERE openingBalanceDetail.id = :openingBalanceDetId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("openingBalanceDetId", id);

		return query.list();
	
	
	
	}

	@Override
	public Integer updateBatchVendorId(Batch batch,Integer vendorId1) {
		 String sql = " update inventory.t_batch set vendor_id=:vendorId where id=:batchId and unit_id=:unitId and organization_id=:organizationId ";

	        Query query = getSession().createSQLQuery(sql);
	        query.setParameter("vendorId", vendorId1);
	        query.setParameter("unitId", batch.getUnitId());
	        query.setParameter("organizationId", batch.getOrganizationId());
	        query.setParameter("batchId", batch.getId());
	        return query.executeUpdate();
	}
}