package com.param.inventory.store.dao.drugRecall;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.DrugRecall;
import com.param.entity.model.inventory.DrugRecallDetail;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.drugRecall.DrugRecallRequest;
import com.param.inventory.store.dto.drugRecall.DrugRecallResponse;

@Repository("DrugRecallDao")
public class DrugRecallDao extends AbstractDao<Integer, DrugRecall> implements IDrugRecallDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<DrugRecallResponse> searchDrugRecallList(DrugRecallRequest request, Integer screenId) {
		String hql = "SELECT drugRecall.id AS id, drugRecall.addedDate AS recallDate, drugRecall.recallNo AS recallNo, "
				+ "		requestType.id AS requestTypeId, requestType.type AS requestType, "
				+ "		user.userName as requestedBy,status.id AS statusId , status.status AS status "
				+ " FROM DrugRecall AS drugRecall " + "		INNER JOIN drugRecall.requestType AS requestType "
				+ "		INNER JOIN drugRecall.addedBy as user " + "		INNER JOIN drugRecall.status AS status "
				+ "	WHERE drugRecall.isActive = true AND drugRecall.isDeleted = false AND drugRecall.unit.unitId = :unitId "
				+ " 	AND drugRecall.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:recallNo = '' OR lower(drugRecall.recallNo) LIKE :recallNo) "
				+ "		AND (:requestTypeId = 0 OR  requestType.id = :requestTypeId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) " + "		ORDER BY drugRecall.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("requestTypeId", request.getRequestTypeId() != null ? request.getRequestTypeId() : 0);
		query.setParameter("recallNo", request.getRecallNo() != null ? request.getRecallNo().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(DrugRecallResponse.class));

		return query.list();
	}

	@Override
	public DrugRecall saveDrugRecall(DrugRecall drugRecall) {
		getSession().save(drugRecall);
		return drugRecall;
	}

	@Override
	public DrugRecall getDrugRecall(Integer id) {
		String hql = "SELECT drugRecall FROM com.param.entity.model.inventory.DrugRecall AS drugRecall "
				+ "WHERE drugRecall.id = :id  AND drugRecall.isActive = true AND drugRecall.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (DrugRecall) query.uniqueResult();
	}

	@Override
	public DrugRecall updateDrugRecall(DrugRecall drugRecall) {
		getSession().update(drugRecall);
		return drugRecall;
	}

	@Override
	public DrugRecallDetail saveDrugRecallDetail(DrugRecallDetail recall) {
		getSession().save(recall);
		return recall;
	}

}
