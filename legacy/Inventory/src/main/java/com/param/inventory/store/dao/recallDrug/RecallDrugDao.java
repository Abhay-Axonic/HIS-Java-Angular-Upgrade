package com.param.inventory.store.dao.recallDrug;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.DrugRecall;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.RecallDrug.RecallDrugRequest;
import com.param.inventory.store.dto.RecallDrug.RecallDrugResponse;

@Repository("RecallDrugDao")
public class RecallDrugDao extends AbstractDao<Integer, DrugRecall> implements IRecallDrugDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<RecallDrugResponse> searchRecallDrugList(RecallDrugRequest request, Integer screenId) {
		String hql = "SELECT drugRecall.id AS id, drugRecall.addedDate AS drugRecallDate, item.code AS itemCode, "
				+ "		item.name AS itemName, drugRecall.recallNo AS recallAgainst, batch.batchNo AS batchNo, batch.expiryDate AS expiryDate, "
				+ "		COALESCE(storeStockList.availableQty, 0) AS availableQuantity, drugRecallDetailList.reason as reason "
				+ "		FROM DrugRecall AS drugRecall "
				+ "		INNER JOIN drugRecall.drugRecallDetailList AS drugRecallDetailList "
				+ "		INNER JOIN drugRecallDetailList.item AS item "
				+ "		LEFT JOIN drugRecallDetailList.batch AS batch "
				+ "		LEFT JOIN batch.storeStockList AS storeStockList "
				+ "		INNER JOIN drugRecall.addedBy as user " 
				+ "		INNER JOIN drugRecall.status AS status "
				+ "	WHERE drugRecall.isActive = true AND drugRecall.isDeleted = false AND drugRecall.unit.unitId = :unitId "
				+ "     AND drugRecallDetailList.isActive = true AND drugRecallDetailList.isDeleted = false "
				+ "		AND drugRecall.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY drugRecall.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));

		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(RecallDrugResponse.class));

		return query.list();
	}

}
