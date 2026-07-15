package com.param.linenLaundry.rejectedLinenAcceptance.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Reject;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectLinenResponse;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectedLinenRequest;

@Repository
public class RejectedLinenAcceptanceDao extends AbstractDao<Integer, Reject>
implements IRejectedLinenAcceptanceDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<RejectLinenResponse> searchRejectLinen(RejectedLinenRequest request, Integer screenId) {
		// TODO Auto-generated method stub
		String hql = "SELECT reject.id AS id, reject.rejectedDate AS returnDate, reject.returnNo AS returnNo, fromStore.storeId AS fromStoreId, "
				+ " fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, user.userName AS returnBy, "
				+ " materialIssue.remark AS remark, status.status AS status "
				+ " FROM com.param.entity.model.inventory.Reject AS reject " + "	INNER JOIN reject.addedBy as user "
				+ "	INNER JOIN reject.fromStore as fromStore " + "	INNER JOIN reject.toStore as toStore "
				+ " LEFT JOIN reject.linenIssue AS linenIssue " + "	INNER JOIN reject.status as status "
				+ "WHERE reject.isActive = true AND reject.isDeleted = false AND reject.unit.unitId = :unitId "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId"

				+ "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "

				+ " AND (:returnNo = '' OR lower(reject.returnNo) LIKE :returnNo) "
				
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) " + "ORDER BY reject.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("returnNo", request.getReturnNo() != null ? "%" + request.getReturnNo().toLowerCase() + "%" : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("userId", sessionService.getUserId());

		query.setResultTransformer(Transformers.aliasToBean(RejectLinenResponse.class));
		return query.list();
	}

}
