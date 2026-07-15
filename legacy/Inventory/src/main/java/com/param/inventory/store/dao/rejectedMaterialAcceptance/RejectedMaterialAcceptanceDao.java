package com.param.inventory.store.dao.rejectedMaterialAcceptance;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.RejectDetail;
import com.param.global.common.Response;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.rejectMaterialAcceptance.RejectMaterialResponse;

@Repository("RejectedMaterialAcceptanceDao")
public class RejectedMaterialAcceptanceDao extends AbstractDao<Integer, Reject>
		implements IRejectedMaterialAcceptanceDao {

	@Autowired
	private SessionService sessionService;
	@Autowired
	SessionFactory sessionFactory;
	/*
	 * @Override public List<Reject> searchRejectMaterial(MaterialPickerRequest
	 * request, Integer screenId) {
	 * 
	 * String hql =
	 * "SELECT reject FROM com.param.entity.model.inventory.Reject AS reject " +
	 * "	INNER JOIN reject.addedBy as user " +
	 * "	INNER JOIN reject.fromStore as fromStore " +
	 * "	INNER JOIN reject.toStore as toStore " +
	 * "	INNER JOIN reject.status as status " +
	 * "WHERE reject.isActive = true AND reject.isDeleted = false AND reject.unit.unitId = :unitId "
	 * + " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) " +
	 * " AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId"
	 * 
	 * +
	 * "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
	 * +
	 * "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
	 * +
	 * "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
	 * 
	 * + " AND (:statusId = 0 OR status.id = :statusId) " +
	 * "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus " +
	 * "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
	 * + "								INNER JOIN screenStatus.status as status) "
	 * + "ORDER BY reject.id DESC";
	 * 
	 * Query query = getSession().createQuery(hql);
	 * query.setParameter("fromStoreId", request.getFromStoreId() != null ?
	 * request.getFromStoreId() : 0); query.setParameter("toStoreId",
	 * request.getToStoreId() != null ? request.getToStoreId() : 0);
	 * query.setParameter("statusId", request.getStatusId() != null ?
	 * request.getStatusId() : 0); query.setParameter("screenId", screenId);
	 * query.setParameter("unitId", sessionService.getUnitId()); query.setParameter("userId",
	 * sessionService.getUserId());
	 * 
	 * return query.list(); }
	 */

	@Override
	public List<RejectMaterialResponse> searchRejectMaterial(MaterialPickerRequest request, Integer screenId) {

		String hql = "SELECT reject.id AS id, reject.rejectedDate AS returnDate, reject.returnNo AS returnNo, fromStore.storeId AS fromStoreId, "
				+ " fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, user.userName AS returnBy, "
				+ " materialIssue.isConsignment AS isConsignment, materialIssue.remark AS remark, status.status AS status, materialIssue.issueNo AS issueNo "
				+ " FROM com.param.entity.model.inventory.Reject AS reject " + "	INNER JOIN reject.addedBy as user "
				+ "	INNER JOIN reject.fromStore as fromStore " + "	INNER JOIN reject.toStore as toStore "
				+ " LEFT JOIN reject.materialIssue AS materialIssue " + "	INNER JOIN reject.status as status "
				+ "WHERE reject.isActive = true AND reject.isDeleted = false AND reject.unit.unitId = :unitId "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId"

				+ "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "

				+ " AND (:returnNo = '' OR lower(reject.returnNo) LIKE :returnNo) "
				
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) " + "ORDER BY reject.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("returnNo", request.getReturnNo() != null ? "%" + request.getReturnNo().toLowerCase() + "%" : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		query.setResultTransformer(Transformers.aliasToBean(RejectMaterialResponse.class));
		return query.list();
	}

	@Override
	public Reject getReject(Integer id) {
		String hql = " SELECT reject FROM com.param.entity.model.inventory.Reject AS reject "
				+ "	WHERE reject.id = :id AND reject.isActive = true AND reject.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Reject) query.uniqueResult();
	}

	@Override
	public List<RejectDetail> getRejectDetail(Integer id) {
		String hql = "SELECT rejectDetail FROM com.param.entity.model.inventory.RejectDetail AS rejectDetail \r\n"
				+ "	INNER JOIN rejectDetail.reject AS reject WITH reject.isActive = true AND reject.isDeleted = false \r\n"
				+ " WHERE reject.id = :id AND rejectDetail.isActive = true AND rejectDetail.isDeleted = false \r\n"
				+ " ORDER BY rejectDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public RejectDetail getRejectDetailById(Integer id) {
		String hql = "SELECT rejectDetail FROM RejectDetail AS rejectDetail \r\n"
				+ "	INNER JOIN rejectDetail.reject AS reject WITH reject.isActive = true AND reject.isDeleted = false \r\n"
				+ " WHERE rejectDetail.id = :id AND rejectDetail.isActive = true AND rejectDetail.isDeleted = false \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (RejectDetail) query.uniqueResult();
	}

	@Override
	public Reject updateReject(Reject reject) {
		getSession().update(reject);
		return reject;
	}

	@Override
	public Reject save(Reject rejectNew) {
		getSession().save(rejectNew);
		return rejectNew;
	}
	
	@Override
	public Integer updatereturn(Integer reject, Integer acceptQty) {

		Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update inventory.t_return_detail set status_id=7,return_qty=0 where  return_id="+reject)
				.executeUpdate();
		return count;
		
	
			
			
				
		
       
		
		
		
	}
	

}
