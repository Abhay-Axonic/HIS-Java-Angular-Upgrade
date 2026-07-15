package com.param.inventory.stockAdjustments.dao.varianceApproval;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ConfirmStock;
import com.param.entity.model.inventory.VarianceApproval;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;

@Repository
public class VarianceApprovalDao extends AbstractDao<Integer, VarianceApproval> implements IVarianceApprovalDao {

	private static final Logger logger = Logger.getLogger(VarianceApprovalDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<UploadStockResponse> searchVarianceApprovalList(UploadStockRequest request, Integer screenId) {
		try {
			String hql = "SELECT varianceApproval.id AS Id, stopTransaction.requestNo AS requestNo, stopTransaction.addedDate AS requestDate, "
					// + "uploadStock.uploadNo AS uploadNo, uploadStock.addedDate AS uploadDate,
					// stopTransaction.requestNo AS againstRequestNo, "
					+ " store.storeId AS storeId, store.storeName AS store, "
					+ " stopTransaction.startDateTime AS startDateTime, stopTransaction.stopDateTime AS stopDateTime, "
					+ " user.userId AS uploadedById, user.userName AS uploadedBy, status.id As statusId, status.status AS status "
					+ " FROM VarianceApproval AS varianceApproval "
					+ " INNER JOIN varianceApproval.addedBy as user WITH user.unitId = :unitId "
					+ " INNER JOIN varianceApproval.uploadStock AS uploadStock WITH uploadStock.isActive = true AND uploadStock.isDeleted = false "
					+ " INNER JOIN uploadStock.stopTransaction AS stopTransaction WITH stopTransaction.isActive = true AND stopTransaction.isDeleted = false "
					+ " INNER JOIN varianceApproval.status AS status WITH status.isActive = true AND status.isDeleted = false "
					// + " INNER JOIN uploadStock.approvalStatus AS approvalStatus WITH
					// approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
					+ " INNER JOIN varianceApproval.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE varianceApproval.unit.unitId = :unitId AND varianceApproval.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND (:storeId = 0 OR store.storeId =:storeId ) "
					+ " AND varianceApproval.isActive = true AND varianceApproval.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY uploadStock.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0); 
			query.setParameter("unitId",request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setResultTransformer(Transformers.aliasToBean(UploadStockResponse.class));

			return query.list();
		} catch (Exception e) {
			logger.error("Error while searching variance approval list", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public VarianceApproval getVarianceApproval(Integer id ,Integer unitId) {

		String hql = " SELECT varianceApproval FROM VarianceApproval AS varianceApproval "
				+ " WHERE varianceApproval.id = :id AND varianceApproval.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId",unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

		return (VarianceApproval) query.uniqueResult();
	}

	@Override
	public VarianceApproval updateVarianceApproval(VarianceApproval varianceApproval) {
		getSession().update(varianceApproval);
		return varianceApproval;
	}

	@Override
	public Batch getQuantity(Integer storeId, Long batchId, Integer unitId) {
		
		String hql = "SELECT batch FROM Batch AS batch \r\n"
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.unit.unitId = :unitId \r\n"
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId \r\n"
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId \r\n"
				+ "WHERE store.storeId = :storeId AND batch.id = :batchId \r\n"
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

		return (Batch) query.uniqueResult();
	}

	@Override
	public ConfirmStock saveVarianceApproval(ConfirmStock confirmStock) {
		getSession().save(confirmStock);
		return confirmStock;
	}

}
