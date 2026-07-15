package com.param.assetManagement.indent.assetIssueAcceptance.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.RejectedAssetAcceptance;

@Repository
public class AssetIssueAcceptanceDao extends AbstractDao<Integer, AssetIssue> implements IAssetIssueAcceptanceDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<AssetIssueResponse> searchAssetIssueAcceptanceList(AssetIssueRequest request, Integer screenId) {

		String hql = "SELECT assetIssue.id AS id, assetIssue.addedDate AS issueDate, assetIssue.issueNo AS issueNo,"
				+ " fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore,"
				+ " priority.priorityDesc AS priority, indentType.type AS type,"
				+ " assetIssue.remark AS remark, status.status AS status, status.id AS statusId "
				+ " FROM AssetIssue AS assetIssue " + "	INNER JOIN assetIssue.addedBy as user "
				+ "	INNER JOIN assetIssue.fromStore as fromStore " + "	INNER JOIN assetIssue.toStore as toStore "
				+ "	INNER JOIN assetIssue.status as status "
				+ "	LEFT JOIN assetIssue.assetIndent.indentType as indentType "
				+ "	LEFT JOIN assetIssue.assetIndent.priority as priority "
				+ "WHERE assetIssue.isActive = true AND assetIssue.isDeleted = false AND assetIssue.unit.unitId = :unitId "
				+ "	AND assetIssue.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId"

				+ "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "

//				+ " AND (:indentNo = '' OR lower(assetIssue.assetIndent.indentNo) LIKE :indentNo) "
				+ " AND (:issueNo = '' OR lower(assetIssue.issueNo) LIKE :issueNo) "

				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY assetIssue.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
//		query.setParameter("indentNo", request.getIndentNo() != null ? request.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("issueNo", request.getIssueNo() != null ? '%'+request.getIssueNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query.setParameter("priorityId", request.getPriorityId() != null ? request.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		query.setResultTransformer(Transformers.aliasToBean(AssetIssueResponse.class));

		return query.list();
	}

	@Override
	public RejectedAssetAcceptance saveRejectedAssetAcceptance(RejectedAssetAcceptance rejectedAssetAcceptance) {
		getSession().save(rejectedAssetAcceptance);
		return rejectedAssetAcceptance;
	}

}
