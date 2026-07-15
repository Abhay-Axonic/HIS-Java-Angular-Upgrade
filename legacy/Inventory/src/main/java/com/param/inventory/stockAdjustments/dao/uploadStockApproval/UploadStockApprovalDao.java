package com.param.inventory.stockAdjustments.dao.uploadStockApproval;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.UploadStock;
import com.param.entity.model.master.Store;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;

@Repository
public class UploadStockApprovalDao extends AbstractDao<Integer, UploadStock> implements IUploadStockApprovalDao {

	private static final Logger logger = Logger.getLogger(UploadStockApprovalDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<UploadStockResponse> searchUploadStockApprovalList(UploadStockRequest request, Integer screenId) {
		try {
			String hql = "SELECT uploadStock.id AS Id, uploadStock.uploadNo AS uploadNo, uploadStock.addedDate AS uploadDate, "
					+ " store.storeId AS storeId, store.storeName AS store, stopTransaction.requestNo AS requestNo, "
					+ " stopTransaction.startDateTime AS requestStartDate, stopTransaction.stopDateTime AS requestStopDate, "
					+ " user.userId AS uploadedById, user.userName AS uploadedBy, status.id As statusId, status.status AS status,"
					+ " approvalStatus.id AS approvalStatusId, approvalStatus.status AS approvalStatus "
					+ " FROM UploadStock AS uploadStock "
					+ " INNER JOIN uploadStock.addedBy as user "
					+ " Left JOIN uploadStock.stopTransaction AS stopTransaction WITH stopTransaction.isActive = true AND stopTransaction.isDeleted = false "
					+ " INNER JOIN uploadStock.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN uploadStock.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
					+ " INNER JOIN uploadStock.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE uploadStock.unit.unitId = :unitId AND uploadStock.updatedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND (:storeId = 0 OR store.storeId =:storeId ) "
					+ " AND uploadStock.isActive = true AND uploadStock.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY uploadStock.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setResultTransformer(Transformers.aliasToBean(UploadStockResponse.class));

			return query.list();
		} catch (Exception e) {
			logger.error("Error while searching the upload stock approval list", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
