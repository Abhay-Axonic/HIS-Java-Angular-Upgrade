package com.param.inventory.stockAdjustments.dao.uploadStock;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.log4j.Logger;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.ComputeStock;
import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.inventory.UploadStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.global.utility.Util;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionSearchResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.global.common.ServiceException;

@Repository
public class UploadStockDao extends AbstractDao<Integer, UploadStock> implements IUploadStockDao {

	private static final Logger logger = Logger.getLogger(UploadStockDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public UploadStock saveUploadStock(UploadStock uploadStock) {
		getSession().saveOrUpdate(uploadStock);
		return uploadStock;
	}

	@Override
	public List<UploadStockResponse> searchUploadStockList(UploadStockRequest request, Integer screenId) {
		try {
			String hql = "SELECT uploadStock.id AS Id, uploadStock.uploadNo AS uploadNo, uploadStock.addedDate AS uploadDate, "
					+ " store.storeId AS storeId, store.storeName AS store, stopTransaction.requestNo AS requestNo, "
					+ " stopTransaction.startDateTime AS requestStartDate, stopTransaction.stopDateTime AS requestStopDate, "
					+ " user.userId AS uploadedById, user.userName AS uploadedBy, status.id As statusId, status.status AS status, "
					+ " approvalStatus.id AS approvalStatusId, approvalStatus.status AS approvalStatus "
					+ " FROM UploadStock AS uploadStock "
					+ " INNER JOIN uploadStock.addedBy as user   "
					+ " Left JOIN uploadStock.stopTransaction AS stopTransaction WITH stopTransaction.isActive = true AND stopTransaction.isDeleted = false "
					+ " INNER JOIN uploadStock.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN uploadStock.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
					+ " INNER JOIN uploadStock.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE uploadStock.unit.unitId = :unitId AND uploadStock.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND (:storeId = 0 OR store.storeId =:storeId ) "
					+ " AND uploadStock.isActive = true AND uploadStock.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " 
					+ " ORDER BY uploadStock.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStatusId() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setResultTransformer(Transformers.aliasToBean(UploadStockResponse.class));

			return query.list();
		} catch (Exception e) {
			logger.error("Error while searching upload stock list", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<StopTransaction> getStopTransactionItems(UploadStockRequest request) {

		String hql = "SELECT stopTransaction FROM StopTransaction AS stopTransaction "
				+ " INNER JOIN stopTransaction.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ " WHERE stopTransaction.unit.unitId = :unitId  "
				+ " AND stopTransaction.startDateTime BETWEEN :fromDate AND  :toDate "
				+ " AND stopTransaction.stopDateTime BETWEEN :fromDate AND  :toDate "
				//+ " stopTransaction.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
				+ " AND stopTransaction.isActive = true AND stopTransaction.isDeleted = false "
				+ " ORDER BY stopTransaction.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("unitId",request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());

		return query.list();
	}

//	@Override
//	public List<StopTransactionItemDetail> getStopTransactionItemsDetails(UploadStockRequest request) {
//		
//		String hql = " SELECT stopTransactionItemDetail FROM StopTransactionItemDetail AS stopTransactionItemDetail "
////				+ " INNER JOIN stopTransactionItemDetail.StopTransaction AS StopTransaction WITH StopTransaction.isActive = true AND StopTransaction.isDeleted = false "
//				+ " WHERE stopTransactionItemDetail.isDeleted = false AND stopTransactionItemDetail.isActive = true "
//				+ " AND stopTransactionItemDetail.StopTransaction.id IN (:id) AND stopTransactionItemDetail.unit.unitId = :unitId";
//		
//		
//		Query query = getSession().createQuery(hql);
//		query.setParameterList("id", request.getStopTransactionsId());
//		query.setParameter("unitId", sessionService.getUnitId());
//
//		return query.list();
//	}

	
	@Override
	public List<StopTransactionItemDetail> getStopTransactionItemsDetails(UploadStockRequest request) {
		
		String hql = " SELECT stopTransactionItemDetail FROM StopTransactionItemDetail AS stopTransactionItemDetail "
				+ " INNER JOIN stopTransactionItemDetail.StopTransaction AS StopTransaction WITH StopTransaction.isActive = true AND StopTransaction.isDeleted = false "				
				+ " WHERE stopTransactionItemDetail.isDeleted = false AND stopTransactionItemDetail.isActive = true "
				+ " AND stopTransactionItemDetail.StopTransaction.id IN (:id) AND stopTransactionItemDetail.unit.unitId = :unitId"
				+ " AND stopTransactionItemDetail.batch.id IN (SELECT storeStock.batch.id FROM StoreStock as storeStock WHERE storeStock.store.id =:storeId)"
				+ " ORDER BY stopTransactionItemDetail.batch.item.name ASC";
				
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", request.getStopTransactionsId());
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
		query.setParameter("storeId", request.getStoreId());
		
		return query.list();
	}
	@Override
	public UploadStock getUploadStock(Integer id) {

		String hql = " SELECT uploadStock FROM UploadStock AS uploadStock "
				+ " WHERE uploadStock.id = :id ";
			//	+ "AND uploadStock.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		//query.setParameter("unitId",sessionService.getUnitId());

		return (UploadStock) query.uniqueResult();
	}

	@Override
	public UploadStock updateUploadStock(UploadStock uploadStock) {
		getSession().update(uploadStock);
		return uploadStock;
	}

	@Override
	public List<UploadStockDetail> getUploadStockDetail(Integer id) {

		String hql = "SELECT uploadStockDetail FROM UploadStockDetail AS uploadStockDetail "
				+ "	INNER JOIN uploadStockDetail.uploadStock AS uploadStock WITH uploadStock.isActive = true AND uploadStock.isDeleted = false "
				+ "WHERE uploadStock.id = :id AND uploadStockDetail.isActive = true AND uploadStockDetail.isDeleted = false "
				+ "ORDER BY uploadStockDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public ComputeStock saveComputeStock(ComputeStock computeStock) {
		getSession().save(computeStock);
		return computeStock;
	}

	@Override
	public StopTransaction getStopTransaction(UploadStockRequest request) {
		String hql = " SELECT stopTransaction FROM StopTransaction AS stopTransaction "
				+ " WHERE stopTransaction.isDeleted = false AND stopTransaction.isActive = true "
				+ " AND stopTransaction.id IN (:id) AND stopTransaction.unit.unitId = :unitId";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", request.getStopTransactionsId());
		query.setParameter("unitId",request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());

		return (StopTransaction) query.uniqueResult();
	}
	
	
	public String getPrefixRunningNumber(Integer storeId , int unitId, Integer screenId) {

		SQLQuery query = (SQLQuery) getSession()
				.createSQLQuery("SELECT*FROM t_generate_prefix_number(:storeId,:unitId, :screenId)");

		query.setParameter("unitId", unitId);
		query.setParameter("screenId", screenId);
		query.setParameter("storeId", storeId);
		
		return (String) query.uniqueResult();
	}
	
	@Override
	public List<StopTransactionItemDetail> getStopTransactionItemsDetailsOldBatch(UploadStockRequest request) {
		
		String hql = " SELECT stopTransactionItemDetail FROM StopTransactionItemDetail AS stopTransactionItemDetail "
				+ " INNER JOIN stopTransactionItemDetail.StopTransaction AS StopTransaction WITH StopTransaction.isActive = true AND StopTransaction.isDeleted = false "
				+ " WHERE stopTransactionItemDetail.isDeleted = false AND stopTransactionItemDetail.isActive = true "
				+ " AND stopTransactionItemDetail.StopTransaction.id IN (:id) AND stopTransactionItemDetail.unit.unitId = :unitId"
				+ " AND stopTransactionItemDetail.batch.id IN (SELECT storeStock.batch.id FROM StoreStock as storeStock WHERE storeStock.availableQty = 0 AND storeStock.store.id =:storeId)"
				+ " ORDER BY stopTransactionItemDetail.batch.item.name ASC";
		
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", request.getStopTransactionsId());
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
		query.setParameter("storeId", request.getStoreId());

		return query.list();
	}
	
	@Override
	public Integer getStoreStockByBatch(Integer storeId, Long batchId) {
		try {
			String hql = "SELECT storeStock.availableQty FROM StoreStock AS storeStock \r\n"
					+ "		INNER JOIN storeStock.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
					+ "		INNER JOIN storeStock.batch AS batch WITH batch.id = :batchId AND batch.isDeleted = false AND batch.isActive = true "
					+ "WHERE storeStock.isDeleted = false AND storeStock.isActive = true ";
	
			Query query = getSession().createQuery(hql);
			query.setParameter("storeId", storeId);
			query.setParameter("batchId", batchId);
	
			//Integer avaialableStoreStock = (Integer) query.uniqueResult();
			Integer avaialableStoreStock = (Integer) query.setMaxResults(1).uniqueResult();
	
			avaialableStoreStock = avaialableStoreStock == null ? 0 : avaialableStoreStock;
	
			return avaialableStoreStock;
		}catch(Exception e) {
			logger.error("Error while fetching store stock by batch", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public UploadStock getUploadStockWithUnitId(Integer id, Integer unitId) {


		String hql = " SELECT uploadStock FROM UploadStock AS uploadStock "
				+ " WHERE uploadStock.id = :id AND uploadStock.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

		return (UploadStock) query.uniqueResult();
	
	}

	@Override
	public void inactiveOldItems(Integer id) {
		String sql = " update inventory.t_upload_stock_detail set is_active=false where upload_stock_id=:id ";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.executeUpdate();
		
	}
		
}
