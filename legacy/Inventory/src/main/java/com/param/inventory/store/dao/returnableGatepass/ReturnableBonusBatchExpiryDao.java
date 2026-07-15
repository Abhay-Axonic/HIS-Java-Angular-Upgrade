package com.param.inventory.store.dao.returnableGatepass;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.returnableBonusBatchExpiry;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.service.global.SessionService;

@Repository
public class ReturnableBonusBatchExpiryDao extends AbstractDao<Integer, returnableBonusBatchExpiry> implements IReturnableBonusBatchExpiryDao {
	    @Autowired
	    private SessionService sessionService;

		@Override
		public void updateBonusBatchExpiry(Integer id) {
			String hql = "delete FROM inventory.t_returnable_bonus_batch_expiry  where returnable_gatepass_detail_id=:DetailId ";

			Query query = getSession().createSQLQuery(hql);
			query.setParameter("DetailId", id);
			 query.executeUpdate();
			
		}

		@Override
		public void saveBonusBatchExpiry(returnableBonusBatchExpiry grnBatchExpiry1) {
			getSession().save(grnBatchExpiry1);
			
		}

		@Override
		public void updateReturnQty(returnableBonusBatchExpiry grnBatchExpiry1) {
			String hql = "update inventory.t_returnable_bonus_batch_expiry set return_quantity=:Quantity  where returnable_bonus_batch_expiry_id=:DetailId ";

			Query query = getSession().createSQLQuery(hql);
			query.setParameter("DetailId", grnBatchExpiry1.getReturnableBonusBatchExpiryId());
			query.setParameter("Quantity", grnBatchExpiry1.getReturnQuantity());
			 query.executeUpdate();
			
		}

		@Override
		public void updateReturnedQty(
				com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry bonusBatches) {
			String hql = "update inventory.t_returnable_bonus_batch_expiry set returned_quantity=:Quantity,return_quantity=0  where returnable_bonus_batch_expiry_id=:DetailId ";

			Query query = getSession().createSQLQuery(hql);
			query.setParameter("DetailId", bonusBatches.getReturnableBonusBatchExpiryId());
			query.setParameter("Quantity", bonusBatches.getReturnQuantity());
			 query.executeUpdate();
			
		}
}
