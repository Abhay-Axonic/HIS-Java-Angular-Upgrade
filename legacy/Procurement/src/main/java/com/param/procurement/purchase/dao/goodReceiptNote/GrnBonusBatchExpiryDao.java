package com.param.procurement.purchase.dao.goodReceiptNote;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dto.goodReceiptNote.IGrnBonusBatchExpiryDao;
import com.param.service.global.SessionService;

@Repository
public class GrnBonusBatchExpiryDao extends AbstractDao<Integer, GrnBonusBatchExpiry> implements IGrnBonusBatchExpiryDao {
	@Autowired
	private SessionService sessionService;

	@Override
	public void updateBonusBatchExpiry(Integer grndetailId) {
		String hql = "delete FROM procurement.t_grn_bonus_batch_expiry  where grn_detail_id=:grnDetailId ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("grnDetailId", grndetailId);
		 query.executeUpdate();
		
	}

	@Override
	public List<GrnBonusBatchExpiry> getGrnBonusBatchExpiryByGrnDetailId(Integer id) {
		String hql = "SELECT grnBonusBatchExpiry FROM GrnBonusBatchExpiry AS grnBonusBatchExpiry where grnDetailId=:grnDetailId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("grnDetailId", id);
		return query.list();
	}

	@Override
	public void saveBonusBatchExpiry(GrnBonusBatchExpiry grnBatchExpiry1) {
		getSession().save(grnBatchExpiry1);
		
	}

	@Override
	public void updateBonusBatchExpiryApproval(GrnBonusBatchExpiry grnBatchExpiry1) {
			getSession().update(grnBatchExpiry1);
	}

	@Override
	public void updateReturnQuantity(GrnBonusBatchExpiry grnBatchExpiry1) {
		String hql = "update procurement.t_grn_bonus_batch_expiry set return_quantity=:returnQuantity where batch_id=:id ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("returnQuantity", grnBatchExpiry1.getReturnQuantity());
		query.setParameter("id", grnBatchExpiry1.getBatchId());
		query.executeUpdate();
		
	}
}
