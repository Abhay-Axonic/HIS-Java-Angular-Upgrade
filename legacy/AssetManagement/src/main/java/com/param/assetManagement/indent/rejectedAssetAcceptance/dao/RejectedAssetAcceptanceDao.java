package com.param.assetManagement.indent.rejectedAssetAcceptance.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.rejectedAssetAcceptance.dto.RejectAssetResponse;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.RejectedAssetAcceptance;
import com.param.entity.model.asset.RejectedAssetAcceptanceDetail;
import com.param.global.utility.Util;

@Repository
public class RejectedAssetAcceptanceDao extends AbstractDao<Integer, RejectedAssetAcceptance>
		implements IRejectedAssetAcceptanceDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<RejectAssetResponse> searchRejectAssetAcceptanceList(AssetIssueRequest request, Integer screenId) {

		String hql = "SELECT rejectedAssetAcceptance.id AS id, rejectedAssetAcceptance.rejectedOn AS returnDate, rejectedAssetAcceptance.returnNo AS returnNo, fromStore.storeId AS fromStoreId, "
				+ " fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, user.userName AS returnBy, "
				+ " assetIssue.remark AS remark, status.status AS status, status.id AS statusId "
				+ " FROM RejectedAssetAcceptance AS rejectedAssetAcceptance "
				+ "	INNER JOIN rejectedAssetAcceptance.addedBy as user "
				+ "	INNER JOIN rejectedAssetAcceptance.fromStore as fromStore "
				+ "	INNER JOIN rejectedAssetAcceptance.toStore as toStore "
				+ " LEFT JOIN rejectedAssetAcceptance.assetIssue AS assetIssue "
				+ "	INNER JOIN rejectedAssetAcceptance.status as status "
				+ "WHERE rejectedAssetAcceptance.isActive = true AND rejectedAssetAcceptance.isDeleted = false AND rejectedAssetAcceptance.unit.unitId = :unitId "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND rejectedAssetAcceptance.addedDate BETWEEN :fromDate AND :toDate "
				
				+ " AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId"

				+ "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "

				+ " AND (:status_id = 0 OR status.id = :status_id) "
				+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY rejectedAssetAcceptance.id DESC";

		Query query = getSession().createQuery(hql);	
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("status_id", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));

		query.setResultTransformer(Transformers.aliasToBean(RejectAssetResponse.class));
		return query.list();
	}

	@Override
	public RejectedAssetAcceptance getRejectAssetAcceptance(Integer id) {

		String hql = " SELECT rejectedAssetAcceptance FROM RejectedAssetAcceptance AS rejectedAssetAcceptance "
				+ "	WHERE rejectedAssetAcceptance.id = :id AND rejectedAssetAcceptance.isActive = true AND rejectedAssetAcceptance.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (RejectedAssetAcceptance) query.uniqueResult();
	}

	@Override
	public List<RejectedAssetAcceptanceDetail> getRejectAssetAcceptanceDetail(Integer id) {

		String hql = "SELECT rejectedAssetAcceptanceDetail FROM RejectedAssetAcceptanceDetail AS rejectedAssetAcceptanceDetail "
				+ "	INNER JOIN rejectedAssetAcceptanceDetail.rejectedAssetAcceptance AS rejectedAssetAcceptance WITH rejectedAssetAcceptance.isActive = true AND rejectedAssetAcceptance.isDeleted = false "
				+ " WHERE rejectedAssetAcceptance.id = :id AND rejectedAssetAcceptanceDetail.isActive = true AND rejectedAssetAcceptanceDetail.isDeleted = false "
				+ " ORDER BY rejectedAssetAcceptanceDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public RejectedAssetAcceptance updateRejectdAssetAcceptance(RejectedAssetAcceptance rejectedAssetAcceptance) {
		getSession().update(rejectedAssetAcceptance);
		return rejectedAssetAcceptance;
	}

	@Override
	public RejectedAssetAcceptance save(RejectedAssetAcceptance rejectedAsset) {
		getSession().save(rejectedAsset);
		return rejectedAsset;
	}

}
