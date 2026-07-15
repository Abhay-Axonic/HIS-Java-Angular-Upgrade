package com.param.assetManagement.indent.assetIssueList.dao;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AssetIssue;

@Repository
public class AssetIssueListDao extends AbstractDao<Integer, AssetIssue> implements IAssetIssueListDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<AssetIssue> searchAssetIssueList(AssetIssueRequest request, Integer screenId) {
		String hql = " SELECT assetIssue FROM AssetIssue AS assetIssue " 
				+ "	INNER JOIN assetIssue.addedBy as user "
				+ "	INNER JOIN assetIssue.fromStore as fromStore " 
				+ "	INNER JOIN assetIssue.toStore as toStore "
				+ "	INNER JOIN assetIssue.status as status "
				+ "	LEFT JOIN assetIssue.assetIndent.indentType as indentType "
				+ "	LEFT JOIN assetIssue.assetIndent.priority as priority "
				+ " WHERE assetIssue.isActive = true AND assetIssue.isDeleted = false AND assetIssue.unit.unitId = :unitId "
				+ "	AND assetIssue.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR toStore.storeId = :toStoreId )"
				+ " AND (:indentNo = '' OR lower(assetIssue.assetIndent.indentNo) LIKE :indentNo) "
				//+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "	AND status.id IN (:statusId) "
				+ "ORDER BY assetIssue.id DESC";

		List<Integer> list=new ArrayList<Integer>();
		if( request.getStatusId()==null)
		{
			list.add(4)	;
			list.add(8)	;
		}
		else
		{
			list.add( request.getStatusId())	;
		}
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("indentNo", request.getIndentNo() != null ? request.getIndentNo().toLowerCase() + '%' : "");
		 query.setParameterList("statusId", list);
		query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query.setParameter("priorityId", request.getPriorityId() != null ? request.getPriorityId() : 0);
		//query.setParameter("screenId", screenId);
		query.setParameter("unitId",  request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		// query.setParameter("userId", sessionService.getUserId());

		return query.list();
	}

	@Override
	public AssetIssue saveAssetIssueList(AssetIssue assetIssue) {
		getSession().save(assetIssue);
		return assetIssue;
	}

}
