package com.param.linenLaundry.linenIndent.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.linenAndLaundry.LinenIndent;
import com.param.entity.model.linenAndLaundry.LinenIndentDetail;
import com.param.global.utility.Util;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;

@Repository
public class LinenIndentDao extends AbstractDao<Integer, LinenIndent> implements ILinenIndentDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<LinenIndent> searchLinenIndent(LinenRequest request, Integer screenId) {

		String hql = "SELECT linenIndent FROM LinenIndent AS linenIndent "
				+ "		INNER JOIN linenIndent.addedBy as user "
				+ "		LEFT JOIN linenIndent.fromStore as fromStore "
				+ "		LEFT JOIN linenIndent.toStore as toStore  " 
				+ "		LEFT JOIN linenIndent.status as status "
				+ "		WHERE linenIndent.isActive = true AND linenIndent.isDeleted = false AND linenIndent.unit.unitId = :unitId "
				+ "		AND linenIndent.addedDate BETWEEN :fromDate AND :toDate "
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
				+ " ORDER BY linenIndent.id DESC";

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
	public LinenIndent saveLinenIndent(LinenIndent linenIndent) {
		getSession().save(linenIndent);

		return linenIndent;
	}
	
	@Override
	public LinenIndent getLinenIndent(Integer id)
	{
		String hql = "SELECT linenIndent FROM LinenIndent AS linenIndent WHERE linenIndent.id = :id  AND linenIndent.isActive = true AND linenIndent.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (LinenIndent) query.uniqueResult();
	}
	
	@Override
	public List<LinenIndentDetail> getLinenIndentDetail(Integer linenIndentId)
	{
		String hql = " SELECT linenIndentDetail FROM LinenIndentDetail AS linenIndentDetail \r\n"
				+ "	INNER JOIN linenIndentDetail.linenIndent AS linenIndent WITH linenIndent.isActive = true AND linenIndent.isDeleted = false \r\n"
				+ "	WHERE linenIndent.id = :id AND linenIndentDetail.isActive = true AND linenIndentDetail.isDeleted = false \r\n"
				+ "	ORDER BY linenIndentDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", linenIndentId);

		return query.list();
	}
	
	

	@Override
	public LinenIndent updateLinenIndent(LinenIndent linenIndent) {
		getSession().update(linenIndent);

		return linenIndent;
	}
	
}