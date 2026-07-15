package com.param.inventory.store.dao.materialIssueAcceptance;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.global.common.ServiceException;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.service.global.SessionService;

@Repository("MaterialIssueAcceptanceDao")
public class MaterialIssueAcceptanceDao extends AbstractDao<Integer, MaterialIssue>
		implements IMaterialIssueAcceptanceDao {

	private static final Logger logger = Logger.getLogger(MaterialIssueAcceptanceDao.class);

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	SessionFactory sessionFactory;

	/*
	 * @Override public List<MaterialIssue>
	 * searchMaterialIssueAcceptanceList(MaterialPickerRequest request, Integer
	 * screenId) {
	 * 
	 * String hql = "SELECT materialIssue FROM MaterialIssue AS materialIssue " +
	 * "	INNER JOIN materialIssue.addedBy as user " +
	 * "	INNER JOIN materialIssue.materialPicker.storeIndent.fromStore as fromStore "
	 * + "	INNER JOIN materialIssue.materialPicker.storeIndent.toStore as toStore "
	 * + "	INNER JOIN materialIssue.status as status " +
	 * "	INNER JOIN materialIssue.materialPicker.storeIndent.indentType as indentType "
	 * +
	 * "	INNER JOIN materialIssue.materialPicker.storeIndent.priority as priority "
	 * +
	 * "WHERE materialIssue.isActive = true AND materialIssue.isDeleted = false AND materialIssue.unit.unitId = :unitId "
	 * + "	AND materialIssue.addedDate BETWEEN :fromDate AND :toDate " +
	 * " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) " +
	 * " AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId"
	 * 
	 * +
	 * "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
	 * +
	 * "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
	 * +
	 * "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
	 * 
	 * +
	 * " AND (:indentNo = '' OR lower(materialIssue.materialPicker.storeIndent.indentNo) LIKE :indentNo) "
	 * + " AND (:statusId = 0 OR status.id = :statusId) " +
	 * " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) " +
	 * " AND (:priorityId = 0 OR priority.priorityId = :priorityId) " +
	 * "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus " +
	 * "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
	 * + "								INNER JOIN screenStatus.status as status) "
	 * + "ORDER BY materialIssue.id DESC";
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("fromDate",
	 * request.getFromDate()); query.setParameter("toDate", request.getToDate());
	 * query.setParameter("fromStoreId", request.getFromStoreId() != null ?
	 * request.getFromStoreId() : 0); query.setParameter("toStoreId",
	 * request.getToStoreId() != null ? request.getToStoreId() : 0);
	 * query.setParameter("indentNo", request.getIndentNo() != null ?
	 * request.getIndentNo().toLowerCase() + '%' : "");
	 * query.setParameter("statusId", request.getStatusId() != null ?
	 * request.getStatusId() : 0); query.setParameter("indentTypeId",
	 * request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
	 * query.setParameter("priorityId", request.getPriorityId() != null ?
	 * request.getPriorityId() : 0); query.setParameter("screenId", screenId);
	 * query.setParameter("unitId", sessionService.getUnitId()); query.setParameter("userId",
	 * sessionService.getUserId());
	 * 
	 * return query.list(); }
	 */

	@Override
	public MaterialIssue updateStoreIndent(MaterialIssue materialIssue) {
		getSession().update(materialIssue);
		return materialIssue;
	}

	@Override
	public Reject saveReject(Reject reject) {
		getSession().save(reject);
		return reject;
	}

	@Override
	public StoreStock save(StoreStock storeStock) {
		getSession().save(storeStock);
		return storeStock;
	}

	@Override
	public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
		getSession().save(stockTransaction);
		return stockTransaction;
	}

	/* New Method For Material Issue Acceptance Search */
	@Override
	public List<MaterialIssueResponse> searchMaterialIssueAcceptanceList(MaterialPickerRequest request,
			Integer screenId) {

		String hql = "SELECT materialIssue.id AS id, materialIssue.addedDate AS issueDate, materialIssue.issueNo AS issueNo,"
				+ " materialPicker.storeIndent.indentNo AS indentNo,"
				+ " fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore,"
				+ " priority.priorityDesc AS priority, indentType.type AS indentType,"
				+ " materialIssue.isConsignment AS isConsignment, materialIssue.remark AS remark, status.status AS status"
				+ " FROM MaterialIssue AS materialIssue " + "	INNER JOIN materialIssue.addedBy as user "
				+ "	INNER JOIN materialIssue.fromStore as fromStore "
				+ "	INNER JOIN materialIssue.toStore as toStore " + "	INNER JOIN materialIssue.status as status "
				+ "	INNER JOIN materialIssue.materialPicker.storeIndent.indentType as indentType "
				+ " INNER JOIN materialIssue.materialPicker as materialPicker "
				+ "	INNER JOIN materialIssue.materialPicker.storeIndent.priority as priority "
				+ "WHERE materialIssue.isActive = true AND materialIssue.isDeleted = false AND materialIssue.unit.unitId = :unitId "
				+ "	AND materialIssue.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId"

				+ "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "

				+ " AND (:indentNo = '' OR lower(materialIssue.materialPicker.storeIndent.indentNo) LIKE :indentNo) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY materialIssue.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("indentNo", request.getIndentNo() != null ? request.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query.setParameter("priorityId", request.getPriorityId() != null ? request.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("userId", sessionService.getUserId());
		query.setResultTransformer(Transformers.aliasToBean(MaterialIssueResponse.class));

		return query.list();
	}
	
	@Override
	public List<MaterialIssue> searchAllMaterialIssueAcceptanceList(MaterialPickerRequest request,
			Integer screenId) {

		String hql = "SELECT materialIssue "
				+ " FROM MaterialIssue AS materialIssue " + "	INNER JOIN materialIssue.addedBy as user "
				+ "	INNER JOIN materialIssue.fromStore as fromStore "
				+ "	INNER JOIN materialIssue.toStore as toStore " + "	INNER JOIN materialIssue.status as status "
				+ "	LEFT JOIN materialIssue.materialPicker.storeIndent.indentType as indentType "
				+ " LEFT JOIN materialIssue.materialPicker as materialPicker "
				+ "	LEFT JOIN materialIssue.materialPicker.storeIndent.priority as priority "
				+ "WHERE materialIssue.isActive = true AND materialIssue.isDeleted = false AND materialIssue.unit.unitId = :unitId "
				+ "	AND date(materialIssue.addedDate) BETWEEN :fromDate AND :toDate "
				+ " AND fromStore.storeId in(:fromStoreId) "
				+ " AND (:toStoreId = 0 OR  toStore.storeId = :toStoreId )"

				+ "	AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "

				+ " AND (:indentNo = '' OR lower(materialIssue.materialPicker.storeIndent.indentNo) LIKE :indentNo) "
				+ " AND (:issueNo = '' OR lower(materialIssue.issueNo) LIKE :issueNo) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY materialIssue.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameterList("fromStoreId",  request.getFromStoreIds());
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("indentNo", request.getIndentNo() != null ? '%'+request.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("issueNo", request.getIssueNo() != null ? '%'+request.getIssueNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query.setParameter("priorityId", request.getPriorityId() != null ? request.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public MaterialIssueBatchMapper save(MaterialIssueBatchMapper batchMapper) {
		getSession().save(batchMapper);
		return batchMapper;
	}

	@Override
	public StoreStock saveStoreStock(StoreStock storeStock) {
		Integer getBatchId=(Integer) getSession()
				.createSQLQuery(" select cast(batch_id as int) from inventory.t_store_stock where batch_id =:id and store_id="+storeStock.getStore().getStoreId()+" ")
				.setParameter("id", storeStock.getBatch().getId().intValue()).uniqueResult();
		if(getBatchId!=null && getBatchId.equals(storeStock.getBatch().getId().intValue()))
		{
			getSession()
			.createSQLQuery(" update inventory.t_store_stock  set available_qty=available_qty+"+storeStock.getAvailableQty()+", t_qty=t_qty+"+storeStock.gettQty()+",total_qty=total_qty+"+storeStock.getTotalQty()+" where batch_id =:id and store_id="+storeStock.getStore().getStoreId()+" ")
			.setParameter("id", storeStock.getBatch().getId())
			.executeUpdate();
		}
		else
		{
			getSession().save(storeStock);
		}
		return storeStock;
	}

	@Override
	public MaterialIssue updateMaterialIssueStoreTransaction(Integer detailsId, Integer screenId) {
		try {
			try {
				String query = "select id as \"stockTransactionId\" from inventory.t_stock_transaction where  transaction_details_id =:detailsId and doc_id =:screenId order by id desc limit 1 ";

				MaterialReurnRequest dto = (MaterialReurnRequest) sessionFactory.getCurrentSession()
						.createSQLQuery(query).setParameter("detailsId", detailsId).setParameter("screenId", screenId)
						.setResultTransformer(Transformers.aliasToBean(MaterialReurnRequest.class)).uniqueResult();
				
				if(dto!= null) {
					Integer result = getSession()
							.createSQLQuery(" update inventory.t_stock_transaction  set is_transit  = false where id =:id ")
							.setParameter("id", dto.getStockTransactionId())
							.executeUpdate();
				}
				
				return null;
			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
				logger.error("Error while updating material issue store transaction", e);
				throw new ServiceException(e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Error while updating material issue store transaction", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void updateMaterialIssueIndendStoreTransaction(Integer detailsId, Integer screenId, Integer indentQty, Integer batchId) {
		try {
			try {
				String query = "select id as \"stockTransactionId\" from inventory.t_stock_transaction where  transaction_details_id =:detailsId and doc_id =:screenId and out_qty =:indentQty and batch_id =:batchId order by id desc limit 1 ";

				MaterialReurnRequest dto = (MaterialReurnRequest) sessionFactory.getCurrentSession()
						.createSQLQuery(query).setParameter("detailsId", detailsId)
						.setParameter("screenId", screenId)
						.setParameter("indentQty", indentQty)
						.setParameter("batchId", batchId)
						.setResultTransformer(Transformers.aliasToBean(MaterialReurnRequest.class)).uniqueResult();
				
				if(dto!= null) {
					Integer result = getSession()
							.createSQLQuery(" update inventory.t_stock_transaction  set is_transit  = false where id =:id ")
							.setParameter("id", dto.getStockTransactionId())
							.executeUpdate();
				}
				
			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
				logger.error("Error while updating material issue indent store transaction", e);
				throw new ServiceException(e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Error while updating material issue indent store transaction", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
