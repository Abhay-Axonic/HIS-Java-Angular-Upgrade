package com.param.inventory.stockAdjustments.dao.computeStock;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ComputeStock;
import com.param.entity.model.inventory.ComputeStockDetail;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.inventory.VarianceApproval;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockRequest;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent; 

@Repository("ComputeStockDao")
public class ComputeStockDao extends AbstractDao<Integer, ComputeStock> implements IComputeStockDao  {

	private static final Logger logger = Logger.getLogger(ComputeStockDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ComputeStockResponse> searchComputeStockList(ComputeStockRequest request, Integer screenId) {
		String hql = " 	SELECT computeStock.id AS id, stopTransaction.requestNo AS requestNo, stopTransaction.addedDate AS requestDate,  "
				+ "		uploadStock.uploadNo AS uploadNo, stopTransaction.startDateTime AS requestStartDate, stopTransaction.stopDateTime AS requestStopDate,"
				+ " 	user.userId AS requestedById, user.userName AS requestedBy , store.storeId AS storeId, store.storeName AS store, "
				+ " 	status.id AS statusId , status.status AS status FROM ComputeStock AS computeStock "
				+ "		INNER JOIN computeStock.uploadStock AS uploadStock WITH uploadStock.isActive = true AND uploadStock.isDeleted = false "
				+ "		INNER JOIN computeStock.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		INNER JOIN computeStock.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN uploadStock.stopTransaction AS stopTransaction WITH stopTransaction.isActive = true AND stopTransaction.isDeleted = false "
				+ "		INNER JOIN stopTransaction.addedBy as user WITH user.status = 'A' "
				+ "		WHERE computeStock.isActive = true AND computeStock.isDeleted = false AND computeStock.unit.unitId = :unitId "
				+ " 	AND computeStock.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:storeId = 0 OR  store.storeId = :storeId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) " + "		ORDER BY computeStock.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ComputeStockResponse.class));

		return query.list();
	}

	@Override
	public ComputeStock getComputeStock(Integer id) {
		String hql = "SELECT computeStock FROM com.param.entity.model.inventory.ComputeStock AS computeStock "
				+ "WHERE computeStock.id = :id  AND computeStock.isActive = true AND computeStock.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (ComputeStock) query.uniqueResult();
	}

	@Override
	public ComputeStock updateComputeStock(ComputeStock computeStock) {
		getSession().update(computeStock);
		return computeStock;
	}
	
	
	@Override
	public 	VarianceApproval saveVarianceApproval(VarianceApproval varianceApproval)
	{
		getSession().save(varianceApproval);
		return varianceApproval;
	}
	
	@Override
	public ConfirmStock saveConfirmStock(ConfirmStock confirmStock) {
		getSession().save(confirmStock);
		return confirmStock;
	}
	
	
	@Override
	public List<ComputeStockDetail> getComputeStockDetail(Integer id) {

		String hql = "SELECT computeStockDetail FROM ComputeStockDetail AS computeStockDetail "
				+ "	INNER JOIN computeStockDetail.ComputeStock AS computeStock WITH computeStock.isActive = true AND computeStock.isDeleted = false "
				+ "WHERE computeStock.id = :id AND computeStockDetail.isActive = true AND computeStockDetail.isDeleted = false "
				+ "ORDER BY computeStockDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}
	
	@Override
	public Long getStoreStockByItemForComputeStock(Integer storeId, Integer itemId) {
				
		String hql = "SELECT COALESCE(SUM(storeStock.availableQty), 0) FROM StoreStock AS storeStock \r\n"
				+ "		INNER JOIN storeStock.batch AS batch WITH  batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true"
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "WHERE store.storeId = :storeId AND item.id = :itemId AND"
				+ "	storeStock.isDeleted = false AND storeStock.isActive = true";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}
	
	@Override
	public List<Batch> getBatchList(Integer itemId, Long batchId, Integer storeId) {
		try {
			Date current = TimeZoneComponent.getDateByzone(sessionService.getUnitId());
			Calendar cal = Calendar.getInstance();
			cal.setTime(current);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			current = cal.getTime();

			String hql = "SELECT batch FROM Batch AS batch "
					+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
					+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "			
					+ "WHERE item.id = :itemId AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  AND batch.unit.unitId = :unitId "
					+ "		AND storeStock.store.storeId = :storeId AND  batch.expiryDate > :greaterThanCurrentMonth "
					+ " ORDER BY batch.expiryDate ASC";

			Query query = getSession().createQuery(hql);
			query.setParameter("itemId", itemId);
			//query.setParameter("batchId", batchId);
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("storeId", storeId);
			query.setParameter("greaterThanCurrentMonth", current);

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching the batch list", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer getUploadStockByItemForComputeStock(Integer itemId, Integer computeId) {
		
		SQLQuery query = getSession().             
				createSQLQuery("select CAST(sum(approved_quantity) as integer) as totalUploadQty from inventory.t_compute_stock_detail where compute_stock_id=:computeId and item_id=:itemId");
				
		query.setParameter("computeId", computeId);
		query.setParameter("itemId", itemId);

		return (query.uniqueResult() != null ? (Integer) query.uniqueResult() : 0);

	}
	
	@Override
	public Batch getQuantity(Integer storeId, Long batchId) {
		
		String hql = "SELECT batch FROM Batch AS batch \r\n"
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId \r\n"
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId \r\n"
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId \r\n"
				+ "WHERE store.storeId = :storeId AND batch.id = :batchId \r\n"
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (Batch) query.uniqueResult();
	}
	
	@Override
	public List<Batch> getNewBatchList(Integer itemId, Integer storeId,Integer computeStockId , Integer unitId) {
		try {
			Date current = TimeZoneComponent.getDateByzone(unitId);
			Calendar cal = Calendar.getInstance();
			cal.setTime(current);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			current = cal.getTime();

			String hql = "SELECT batch FROM Batch AS batch "
					+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
					+ "		INNER JOIN FETCH batch.storeStockList AS storeStock "			
					+ "WHERE item.id = :itemId AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  AND batch.unit.unitId = :unitId "
					+ "		AND storeStock.store.storeId = :storeId AND  batch.expiryDate > :greaterThanCurrentMonth"
					+ " AND  batch.id NOT IN (SELECT computeDetail.batch.id FROM ComputeStockDetail AS computeDetail WHERE computeDetail.ComputeStock.id =:computeStockId)"
					+ " ORDER BY batch.expiryDate ASC";

			Query query = getSession().createQuery(hql);
			query.setParameter("itemId", itemId);
			//query.setParameter("batchId", batchId);
			query.setParameter("computeStockId", computeStockId);
			query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", storeId);
			query.setParameter("greaterThanCurrentMonth", current);

			return query.list();

		} catch (Exception e) {
			logger.error("Error while fetching the new batch list", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ComputeStockDetail saveComputeStockDetail(ComputeStockDetail computeStockDetail) {
		getSession().save(computeStockDetail);
		return computeStockDetail;
	}	

	@Override
	public UploadStockDetail saveUploadStockDetail(UploadStockDetail uploadStockDetail) {
		getSession().save(uploadStockDetail);
		return uploadStockDetail;
	}
	
	@Override
	public List<Long> getBatchIds(Integer computeStockId){
		SQLQuery query = getSession().createSQLQuery("SELECT batch_id FROM inventory.t_compute_stock_detail where compute_stock_id =:computeStockId " );               
		query.setLong("computeStockId",  computeStockId);
		//query.addEntity("emp",  Employee.class);                        
		return (List<Long>) query.list();
	
	}
	

	public Boolean CheckDataForConfirmStock(Integer id){		
		Boolean  isExit = false ;
		try {
			SQLQuery query = getSession().             
					createSQLQuery("select 1 from inventory.t_compute_stock_detail where compute_stock_id=:id and status_id =:statusId");
			          query.setParameter("id",id );
			          query.setParameter("statusId",8);
			
			isExit = query.uniqueResult() != null;

		} catch (Exception e) {
			logger.error("Error while checking data for confirm stock", e);
			throw new ServiceException(e.getMessage());
		}
		return  isExit;
	}
//	@Override
//	public ComputeStock saveComputeStock(ComputeStock computeStock) {
//		getSession().save(computeStock);
//		return computeStock;
//	}
//	
//	@Override
//	public UploadStock saveUploadStock(UploadStock uploadStock) {
//		getSession().save(uploadStock);
//		return uploadStock;
//	}
	

}