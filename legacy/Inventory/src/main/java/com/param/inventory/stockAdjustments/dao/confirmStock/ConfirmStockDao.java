package com.param.inventory.stockAdjustments.dao.confirmStock;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ComputeStockDetail;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.ConfirmStockDetail;
import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.inventory.UploadStockDetail;
import com.param.entity.model.inventory.VarianceApprovalDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;

@Repository
public class ConfirmStockDao extends AbstractDao<Integer, ConfirmStock> implements IConfirmStockDao {

	private static final Logger logger = Logger.getLogger(ConfirmStockDao.class);

	@Autowired
	private SessionService sessionService;
	
	
	@Override
	public List<ConfirmStockResponse> searchConfirmStockList(UploadStockRequest request, Integer screenId) {
		try {
			String hql = "SELECT confirmStock.id AS Id, confirmStock.stockAdjustmentNo AS stockAdjustmentNo, "
					+ " confirmStock.addedDate AS stockAdjustmentDate, "
					+ " store.storeId AS storeId, store.storeName AS store, "
					+ " computeUser.userId AS computedById , computeUser.userName AS computedBy,  "
					//+ " uploadUser.userId AS uploadedById, uploadUser.userName AS uploadedBy,"
					+ " status.id As statusId, status.status AS status "
					+ " FROM ConfirmStock AS confirmStock "
					+ " INNER JOIN confirmStock.addedBy as user   "
					+ " INNER JOIN confirmStock.computeStock AS computeStock WITH computeStock.isActive = true AND computeStock.isDeleted = false "
					+ " INNER JOIN computeStock.addedBy AS computeUser WITH user.unitId = :unitId "
					//+ " INNER JOIN confirmStock.varianceApproval AS varianceApproval WITH varianceApproval.isActive = true AND varianceApproval.isDeleted = false "
					
					//+ " INNER JOIN varianceApproval.uploadStock AS uploadStock WITH uploadStock.isActive = true AND uploadStock.isDeleted = false "
					//+ " INNER JOIN uploadStock.addedBy AS uploadUser WITH user.unitId = :unitId "
					
					+ " INNER JOIN confirmStock.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN confirmStock.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " WHERE confirmStock.unit.unitId = :unitId AND confirmStock.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND (:storeId = 0 OR store.storeId =:storeId ) "
					+ " AND confirmStock.isActive = true AND confirmStock.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " 
					+ " ORDER BY confirmStock.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setResultTransformer(Transformers.aliasToBean(ConfirmStockResponse.class));

			return query.list();
		} catch (Exception e) {
			logger.error("Error while searching the confirm stock list", e);
			throw new ServiceException(e.getMessage());
		}
	}

//	@Override
//	public List<ConfirmStockResponse> searchConfirmStockList(UploadStockRequest request, Integer screenId) {
//		try {
//			String hql = "SELECT confirmStock.id AS Id, confirmStock.stockAdjustmentNo AS stockAdjustmentNo, "
//					+ " confirmStock.addedDate AS stockAdjustmentDate, "
//					+ " store.storeId AS storeId, store.storeName AS store, "
//					+ " computeUser.userId AS computedById , computeUser.userName AS computedBy,  "
//					+ " uploadUser.userId AS uploadedById, uploadUser.userName AS uploadedBy, status.id As statusId, status.status AS status "
//					+ " FROM ConfirmStock AS confirmStock "
//					+ " INNER JOIN confirmStock.addedBy as user WITH user.unitId = :unitId "
//					+ " INNER JOIN confirmStock.computeStock AS computeStock WITH computeStock.isActive = true AND computeStock.isDeleted = false "
//					+ " INNER JOIN computeStock.addedBy AS computeUser WITH user.unitId = :unitId "
//					+ " INNER JOIN confirmStock.varianceApproval AS varianceApproval WITH varianceApproval.isActive = true AND varianceApproval.isDeleted = false "
//					
//					+ " INNER JOIN varianceApproval.uploadStock AS uploadStock WITH uploadStock.isActive = true AND uploadStock.isDeleted = false "
//					+ " INNER JOIN uploadStock.addedBy AS uploadUser WITH user.unitId = :unitId "
//					
//					+ " INNER JOIN varianceApproval.store AS store WITH store.isActive = true AND store.isDeleted = false "
//					+ " INNER JOIN confirmStock.status AS status WITH status.isActive = true AND status.isDeleted = false "
//					+ " WHERE confirmStock.unit.unitId = :unitId AND confirmStock.addedDate BETWEEN :fromDate AND :toDate "
//					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
//					+ " AND (:storeId = 0 OR store.storeId =:storeId ) "
//					+ " AND confirmStock.isActive = true AND confirmStock.isDeleted = false "
//					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
//					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
//					+ "	INNER JOIN screenStatus.status as status) " 
//					+ " ORDER BY confirmStock.id DESC ";
//
//			Query query = getSession().createQuery(hql);
//			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
//			query.setParameter("toDate", Util.getDate(request.getToDate()));
//			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
//			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
//			query.setParameter("unitId", sessionService.getUnitId());
//			query.setParameter("screenId", screenId);
//			query.setResultTransformer(Transformers.aliasToBean(ConfirmStockResponse.class));
//
//			return query.list();
//		} catch (Exception e) {
//			
//		}
//		return null;
//	}

	
	@Override
	public ConfirmStock getConfirmStock(Integer id , Integer unitId) {

		String hql = " SELECT confirmStock FROM ConfirmStock AS confirmStock "
				+ " WHERE confirmStock.id = :id AND confirmStock.unit.unitId = :unitId AND confirmStock.isActive = true "
				+ " AND confirmStock.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId",unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

		return (ConfirmStock) query.uniqueResult();
	}
	
	@Override
	public ConfirmStock updateConfirmStock(ConfirmStock confirmStock) {
		getSession().update(confirmStock);
		return confirmStock;
	}	
		
	
	@Override
	public ConfirmStockDetail getConfirmDetailStock(Integer id , Integer unitId ) {

		String hql = " SELECT confirmStockDetail FROM ConfirmStockDetail AS confirmStockDetail "
				+ " WHERE confirmStockDetail.id = :id AND confirmStockDetail.unit.unitId = :unitId"
				+ " AND confirmStockDetail.isActive = true  AND confirmStockDetail.isDeleted = false";
			
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);	
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

		return (ConfirmStockDetail) query.uniqueResult();
	}
	
	@Override
	public VarianceApprovalDetail getvarianceApprovalDetail(Integer id , Integer unitId) {

		String hql = " SELECT varianceApprovalDetail FROM VarianceApprovalDetail AS varianceApprovalDetail "
				+ " WHERE varianceApprovalDetail.computeStockDetail.id = :id AND varianceApprovalDetail.unit.unitId = :unitId"
				+ " AND varianceApprovalDetail.isActive = true  AND varianceApprovalDetail.isDeleted = false";
			
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);	
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId :sessionService.getUnitId());

		return (VarianceApprovalDetail) query.uniqueResult();
	}	

	@Override
	public UploadStockDetail getUploadDetailStock(Integer id , Integer unitId) {

		String hql = " SELECT uploadStockDetail FROM UploadStockDetail AS uploadStockDetail "		
				+ " WHERE uploadStockDetail.id = :id AND uploadStockDetail.unit.unitId = :unitId AND uploadStockDetail.isActive = true "
				+ " AND uploadStockDetail.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

		return (UploadStockDetail) query.uniqueResult();
	}
	
	@Override
	public StopTransactionItemDetail getStopTransactionItemDetail(Integer stopTransactionId,Long batchId) {

		String hql = " SELECT stopTransactionItemDetail FROM StopTransactionItemDetail AS stopTransactionItemDetail "		
				+ " WHERE stopTransactionItemDetail.StopTransaction.id = :stopTransactionId AND stopTransactionItemDetail.batch.id =:batchId AND stopTransactionItemDetail.unit.unitId = :unitId AND stopTransactionItemDetail.isActive = true "
				+ " AND stopTransactionItemDetail.isDeleted = false ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("stopTransactionId", stopTransactionId);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (StopTransactionItemDetail) query.uniqueResult();
	}
	
	@Override
	public ConfirmStockDetail updateConfirStockDetail(ConfirmStockDetail confirmStockDetail) {
		getSession().update(confirmStockDetail);
		return confirmStockDetail;
	}	

	@Override
	public UploadStockDetail updateUploadStockDetail(UploadStockDetail uploadStockDetail) {
		getSession().update(uploadStockDetail);
		return uploadStockDetail;
	}
	
	@Override
	public VarianceApprovalDetail updateVarianceApprovalDetail(VarianceApprovalDetail varianceApprovalDetail) {
		getSession().update(varianceApprovalDetail);
		return varianceApprovalDetail;
	}
	
	@Override
	public StopTransactionItemDetail updateStopTransactionItemDetail(StopTransactionItemDetail stopTransactionItemDetail) {
		getSession().update(stopTransactionItemDetail);
		return stopTransactionItemDetail;
	}
	
	@Override
	public Integer getStoreStockForByBatchForOtherStore(Integer storeId, Long batchId) {
				
		String hql = "SELECT COALESCE(SUM(storeStock.availableQty), 0) FROM StoreStock AS storeStock \r\n"
				+ "		INNER JOIN storeStock.batch AS batch WITH  batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true"
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ " WHERE store.storeId != :storeId AND storeStock.batch.id = :batchId AND"
				+ "	storeStock.isDeleted = false AND storeStock.isActive = true";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);

		return (query.uniqueResult() != null ? (Integer) query.uniqueResult() : 0);

	}
	

	@Override
	public Batch getBatch(Long id) {
		String hql = "SELECT batch FROM Batch as batch  WHERE batch.id = :id  AND batch.isActive = true AND batch.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Batch) query.uniqueResult();

	}

	@Override
	public void updateStopTransaction(StopTransaction stopTransaction) {
		getSession().update(stopTransaction);
	}
}
