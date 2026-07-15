package com.param.linenLaundry.linenIssueList.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueBatchMapper;
import com.param.global.utility.Util;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

@Repository
public class LinenIssueListDao extends AbstractDao<Integer, LinenIssue> implements ILinenIssueListDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<LinenIssue> searchLinenIssueList(LinenRequest request, Integer screenId) {

		String hql = "SELECT linenIssue FROM LinenIssue AS linenIssue "
				+ "     INNER JOIN linenIssue.linenIndent AS linenIndent WITH linenIndent.isActive = true AND linenIndent.isDeleted = false "
				+ "		INNER JOIN linenIssue.addedBy as user "
				+ "		LEFT JOIN linenIssue.fromStore as fromStore "
				+ "		LEFT JOIN linenIssue.toStore as toStore  " 
				+ "		LEFT JOIN linenIssue.status as status "
				+ "		WHERE linenIssue.isActive = true AND linenIssue.isDeleted = false AND linenIssue.unit.unitId = :unitId "
				+ "		AND linenIssue.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " 	AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId "
				+ "		AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "									WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "								 	AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
				+ " 	AND (:indentNo = '' OR lower(linenIndent.indentNo) LIKE :indentNo) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY linenIssue.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("fromStoreId",
				request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("indentNo",
				request.getIndentNo() != null ? request.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public LinenIssue saveLinenIssueList(LinenIssue linenIssue) {
		getSession().save(linenIssue);

		return linenIssue;
	}
	
	@Override
	public LinenIssue getLinenIssueList(Integer id)
	{
		String hql = "SELECT linenIssue FROM LinenIssue AS linenIssue WHERE linenIssue.id = :id  AND linenIssue.isActive = true AND linenIssue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (LinenIssue) query.uniqueResult();
	}
	
	@Override
	public List<ItemResponse> getLinenIssueListDetail(Integer linenIssueId)
	{
	
		String hql = " SELECT linenIssueDetail.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName,"
				+ " linenIndentDetail.indentQuantity AS quantity, linenIndentDetail.pendingQuantity AS pendingQuantity,"
				+ " linenIssueDetail.issueQuantity AS issueQuantity, batch.id AS batchId, batch.batchNo AS batchNo FROM LinenIssueDetail AS linenIssueDetail \r\n"
				+ "	INNER JOIN linenIssueDetail.linenIssue AS linenIssue WITH linenIssue.isActive = true AND linenIssue.isDeleted = false "
				+ " INNER JOIN linenIssueDetail.linenIndentDetail AS linenIndentDetail WITH linenIndentDetail.isActive = true AND linenIndentDetail.isDeleted = false "
				+ " INNER JOIN linenIssueDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " INNER JOIN linenIssueDetail.linenIssueBatchMapper AS linenIssueBatchMapper WITH linenIssueBatchMapper.isActive = true AND linenIssueBatchMapper.isDeleted = false "
				+ " INNER JOIN linenIssueBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false \r\n"
				+ "	WHERE linenIssue.id = :id AND linenIssueDetail.isActive = true AND linenIssueDetail.isDeleted = false \r\n"
				+ "	ORDER BY linenIssueDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", linenIssueId);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));
		
		return query.list();
	}
	
	

	@Override
	public LinenIssue updateLinenIssueList(LinenIssue linenIssue) {
		getSession().update(linenIssue);

		return linenIssue;
	}
	
	@Override
	public List<LinenIssueBatchMapper> getLinenIssueBatchMapper(Integer linenIssueDetailId)
	{
		String hql = " SELECT linenIssueBatchMapper FROM LinenIssueBatchMapper AS linenIssueBatchMapper \r\n"
				+ " INNER JOIN linenIssueBatchMapper.linenIssueDetail AS linenIssueDetail WITH linenIssueDetail.isDeleted = false AND linenIssueDetail.isActive = true "
				+ "	WHERE linenIssueDetail.id = :id AND linenIssueBatchMapper.isActive = true AND linenIssueBatchMapper.isDeleted = false \r\n"
				+ "	ORDER BY linenIssueBatchMapper.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", linenIssueDetailId);

		return query.list();
	}
}