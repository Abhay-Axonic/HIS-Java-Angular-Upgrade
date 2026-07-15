package com.param.assetManagement.indent.assetIssue.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueDetailResponse;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetIssueBatchMapper;
import com.param.entity.model.asset.AssetIssueDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.global.utility.Util;

@Repository
public class AssetIssueDao extends AbstractDao<Integer, AssetIssue> implements IAssetIssueDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<AssetIssueResponse> searchAssetIssuePendingList(AssetIndentRequest request, Integer screenId) {
		String hql ="";
		if(request.getScreenId().equals(1))
		{
			//pending
			 hql = "SELECT assetIssue.id AS id, assetIndent.addedDate AS indentDate, assetIndent.indentNo AS indentNo, "
					+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
					+ "		priority.priorityDesc AS priority, indentType.type AS type, status.id AS statusId, status.status AS status, "
					+ "		assetIssue.remark AS remark " 
					+ " FROM AssetIssue AS assetIssue "
					+ "		INNER JOIN assetIssue.assetIndent AS assetIndent "
					+ "		INNER JOIN assetIssue.fromStore AS fromStore "
					+ "		INNER JOIN assetIssue.toStore AS toStore " + "		INNER JOIN assetIssue.priority AS priority "
					+ "		INNER JOIN assetIssue.indentType AS indentType "
					+ "		INNER JOIN assetIssue.status AS status "
					+ "	WHERE assetIssue.isActive = true AND assetIssue.isDeleted = false AND assetIssue.unit.unitId = :unitId "
					+ " 	AND assetIssue.addedDate BETWEEN :fromDate AND :toDate "
					+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
					+ "     AND (:toStoreId = 0 OR toStore.storeId = :toStoreId ) "
					+ "     AND (:indentNo = '' OR lower(assetIssue.assetIndent.indentNo) LIKE :indentNo) "
					+ "     AND (:statusId = 0 OR status.id = :statusId) "
					+ "		AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
					+ "		AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
					+ "		AND status.id IN (2,18) "
					+ "ORDER BY priority.priorityId ASC, assetIssue.id DESC";
			
		}
		else
		{
			//issued
			 hql = "SELECT assetIssue.id AS id, assetIndent.addedDate AS indentDate, assetIndent.indentNo AS indentNo, "
					+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
					+ "		priority.priorityDesc AS priority, indentType.type AS type, status.id AS statusId, status.status AS status, "
					+ "		assetIssue.remark AS remark " 
					+ " FROM AssetIssue AS assetIssue "
					+ "		INNER JOIN assetIssue.assetIndent AS assetIndent "
					+ "		INNER JOIN assetIssue.fromStore AS fromStore "
					+ "		INNER JOIN assetIssue.toStore AS toStore " + "		INNER JOIN assetIssue.priority AS priority "
					+ "		INNER JOIN assetIssue.indentType AS indentType "
					+ "		INNER JOIN assetIssue.status AS status "
					+ "	WHERE assetIssue.isActive = true AND assetIssue.isDeleted = false AND assetIssue.unit.unitId = :unitId "
					+ " 	AND assetIssue.addedDate BETWEEN :fromDate AND :toDate "
					+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
					+ "     AND (:toStoreId = 0 OR toStore.storeId = :toStoreId ) "
					+ "     AND (:indentNo = '' OR lower(assetIssue.assetIndent.indentNo) LIKE :indentNo) "
					+ "     AND (:statusId = 0 OR status.id = :statusId) "
					+ "		AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
					+ "		AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
					+ "		AND status.id IN (4,8) "
					+ "ORDER BY priority.priorityId ASC, assetIssue.id DESC";
			
		}
		

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("indentNo", request.getIndentNo() != null ? '%'+request.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query.setParameter("priorityId", request.getPriorityId() != null ? request.getPriorityId() : 0);
		//query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(AssetIssueResponse.class));

		return query.list();
	}

	@Override
	public AssetIssue getAssetIssue(Integer id) {

		String hql = " SELECT assetIssue FROM AssetIssue AS assetIssue "
				+ "	WHERE assetIssue.id = :id AND assetIssue.isActive = true AND assetIssue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (AssetIssue) query.uniqueResult();
	}

	@Override
	public List<AssetIssueDetail> getAssetIssueDetail(Integer id) {

		String hql = "SELECT assetIssueDetail FROM AssetIssueDetail AS assetIssueDetail "
				+ "	INNER JOIN assetIssueDetail.assetIssue AS assetIssue WITH assetIssue.isActive = true AND assetIssue.isDeleted = false "
				+ " WHERE assetIssue.id = :id AND assetIssueDetail.isActive = true AND assetIssueDetail.isDeleted = false \r\n"
				+ " ORDER BY assetIssueDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<AssetIssueBatchMapper> getAssetIssueBatchList(Integer id) {

		String hql = "SELECT assetIssueBatchMapper FROM AssetIssueBatchMapper AS assetIssueBatchMapper "
				+ "		INNER JOIN assetIssueBatchMapper.assetIssueDetail AS assetIssueDetail "
				+ " WHERE assetIssueDetail.id = :id AND assetIssueBatchMapper.isActive = true AND assetIssueBatchMapper.isDeleted = false "
				+ " ORDER BY assetIssueBatchMapper.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public AssetIssueResponse getAssetIssuePending(Integer id) {

		String hql = "SELECT assetIssue.id AS id, assetIndent.addedDate AS indentDate, assetIndent.indentNo AS indentNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		priority.priorityDesc AS priority, indentType.type AS type, "
				+ "		assetIssue.remark AS remark, status.status AS status , carrier.userId AS carrierId " + " FROM AssetIssue AS assetIssue "
				+ "		INNER JOIN assetIssue.assetIndent AS assetIndent "
				+ "		INNER JOIN assetIndent.fromStore AS fromStore "
				+ "		INNER JOIN assetIndent.toStore AS toStore "
				+ "		INNER JOIN assetIndent.priority AS priority "
				+ "		INNER JOIN assetIndent.indentType AS indentType "
				+ "		INNER JOIN assetIssue.status AS status "
				+ "		LEFT JOIN assetIssue.carrier AS carrier "
				+ "	WHERE assetIssue.id = :id AND assetIssue.isActive = true AND assetIssue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(AssetIssueResponse.class));

		return (AssetIssueResponse) query.uniqueResult();
	}

	@Override
	public List<AssetIssueDetailResponse> getAssetIssuePendingDetail(Integer id) {

		String hql = "SELECT assetIssueDetail.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ "	 assetIndentDetail.indentQuantity AS indentQuantity, "
				+ "   assetIndentDetail.pendingQuantity AS pendingQuantity, "
				+ "	    COALESCE(SUM(assetIssueDetail.issueQuantity), 0) AS issueQuantity "
				+ " FROM AssetIssue AS assetIssue "
				+ " 	INNER JOIN assetIssue.assetIssueDetail AS assetIssueDetail WITH assetIssueDetail.isActive = true AND assetIssueDetail.isDeleted = false "
				+ "		INNER JOIN assetIssueDetail.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		LEFT JOIN assetIssueDetail.assetIndentDetail AS assetIndentDetail WITH assetIndentDetail.isActive = true and assetIndentDetail.isDeleted = false "
				+ "	WHERE assetIssue.id = :id AND assetIssue.isActive = true AND assetIssue.isDeleted = false "
				+ " GROUP BY assetIssueDetail.id, item.id, item.code, item.name, "
				+ "	assetIndentDetail.indentQuantity, assetIndentDetail.pendingQuantity ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(AssetIssueDetailResponse.class));

		return query.list();
	}

	@Override
	public List<AssetIssueBatchMapper> getAsssetIssueBatchList(Integer id) {
		String hql = "SELECT assetIssueBatch FROM AssetIssueBatchMapper AS assetIssueBatch "
				+ "	INNER JOIN assetIssueBatch.assetIssueDetail AS assetIssueDetail "
				+ " WHERE assetIssueDetail.id = :id AND assetIssueBatch.isActive = true AND assetIssueBatch.isDeleted = false "
				+ " ORDER BY assetIssueBatch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public AssetIssue updateMaterialIssue(AssetIssue assetIssue) {
		getSession().update(assetIssue);
		return assetIssue;
	}

	@Override
	public List<AssetIssueAccessory> getAssetIssueAccessoryList(Integer id) {

		String hql = "SELECT assetIssueAccessory FROM AssetIssueAccessory AS assetIssueAccessory "
				+ "		INNER JOIN assetIssueAccessory.assetIssueBatchMapper AS assetIssueBatchMapper "
				+ " WHERE assetIssueBatchMapper.id = :id AND assetIssueBatchMapper.isActive = true AND assetIssueBatchMapper.isDeleted = false "
				+ " ORDER BY assetIssueAccessory.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}
	
	@Override
	public StoreStock saveStoreStock(StoreStock storeStock) {
		getSession().save(storeStock);
		return storeStock;
	}

}
