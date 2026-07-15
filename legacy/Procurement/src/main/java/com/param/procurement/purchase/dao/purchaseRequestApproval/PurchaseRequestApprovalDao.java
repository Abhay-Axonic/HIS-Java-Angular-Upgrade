package com.param.procurement.purchase.dao.purchaseRequestApproval;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApproval;
import com.param.service.global.SessionService;
import com.param.global.common.ServiceException;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import java.util.List;

@Repository
public class PurchaseRequestApprovalDao extends AbstractDao<Integer, PurchaseRequest>
		implements IPurchaseRequestApprovalDao {

	final static Logger logger = Logger.getLogger(PurchaseRequestApprovalDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<PurchaseRequest> purchaseRequestApprovalSearch(PurchaseRequestApproval search,
			Integer purchaseRequestApprovalScreenId, Integer purchaseRequestScreenId) {
		try {
			List<PurchaseRequest> purchaseRequests = null;

			String hql = "SELECT purchaseRequest FROM PurchaseRequest AS purchaseRequest \r\n"
					+ "WHERE  "
					+ "DATE(purchaseRequest.addedDate) BETWEEN :fromDate AND :toDate "
					+ "AND (:storeId = 0 OR purchaseRequest.store.storeId = :storeId) "
					+ "AND (:requestingStoreId = 0 OR purchaseRequest.requestingStore.storeId = :requestingStoreId) "
					+ "AND (:purchaseRequestScreenId = 0 OR purchaseRequest.screen.id = :purchaseRequestScreenId) "
					+ "AND (:statusId = 0 OR purchaseRequest.status.id = :statusId) "
					+ "AND (:priorityId = 0 OR purchaseRequest.priority.priorityId = :priorityId) AND  (purchaseRequest.unit.unitId = :unitId )"
//				+ "AND status.id !=8 AND purchaseRequest.approvalStatus.id !=9 "
					+ "AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ "INNER JOIN screenStatus.screen as screen WITH screen.id = :purchaseRequestApprovalScreenId "
					+ "INNER JOIN screenStatus.status as status) " + "ORDER BY purchaseRequest.id DESC\r\n";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", search.getFromDate());
			query.setParameter("toDate", search.getToDate());
			query.setParameter("purchaseRequestScreenId", purchaseRequestScreenId);
			query.setParameter("purchaseRequestApprovalScreenId", purchaseRequestApprovalScreenId);
			query.setParameter("storeId", search.getStoreId() != null ? search.getStoreId() : 0);
			query.setParameter("requestingStoreId",
					search.getRequestingStoreId() != null ? search.getRequestingStoreId() : 0);
			query.setParameter("statusId", search.getStatusId() != null ? search.getStatusId() : 0);
			query.setParameter("priorityId", search.getPriorityId() != null ? search.getPriorityId() : 0);
			query.setParameter("unitId", search.getUnitId() != null ? search.getUnitId() : sessionService.getUnitId());

			purchaseRequests = query.list();

			return purchaseRequests;
		}catch(Exception e){
			logger.error("purchaseRequestApprovalSearch failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
}
