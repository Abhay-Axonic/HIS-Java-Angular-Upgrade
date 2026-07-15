package com.param.inventory.store.dao.storeIndent;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.MaterialPicker;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.global.common.ServiceException;
import com.param.global.dto.CommonDto;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndentApproval.StoreIndentApprovalRequest;
import com.param.service.global.SessionService;

@Repository
public class StoreIndentDao extends AbstractDao<Integer, StoreIndent> implements IStoreIndentDao {

	private static final Logger logger = Logger.getLogger(StoreIndentDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<StoreIndent> searchStoreIndent(StoreIndentRequest storeIndentRequest, Integer screenId) {

		String hql = "SELECT storeIndent FROM StoreIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.addedBy as user "
				+ "		LEFT JOIN storeIndent.fromStore as fromStore "
				+ "		LEFT JOIN storeIndent.toStore as toStore  " + "		LEFT JOIN storeIndent.status as status "
				+ "		LEFT JOIN storeIndent.approvalStatus as approvalStatus "
				+ "		LEFT JOIN storeIndent.indentType as indentType "
				+ "		LEFT JOIN storeIndent.priority as priority "
				+ "WHERE storeIndent.isActive = true AND storeIndent.isDeleted = false AND storeIndent.unit.unitId = :unitId "
				+ "		AND storeIndent.updatedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " 	AND (:toStoreId = 0 OR toStore.storeId = :toStoreId )"
				+ "			AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ " 	AND (:indentNo = '' OR lower(storeIndent.indentNo) LIKE :indentNo) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ " 	AND ( status.id !=8 and approvalStatus.id !=9) "
				+ " 	AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " 	AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY storeIndent.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(storeIndentRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(storeIndentRequest.getToDate()));
		query.setParameter("fromStoreId",
				storeIndentRequest.getFromStoreId() != null ? storeIndentRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				storeIndentRequest.getToStoreId() != null ? storeIndentRequest.getToStoreId() : 0);
		query.setParameter("indentNo",
				storeIndentRequest.getIndentNo() != null ? '%'+storeIndentRequest.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", storeIndentRequest.getStatusId() != null ? storeIndentRequest.getStatusId() : 0);
		query.setParameter("indentTypeId",
				storeIndentRequest.getIndentTypeId() != null ? storeIndentRequest.getIndentTypeId() : 0);
		query.setParameter("priorityId",
				storeIndentRequest.getPriorityId() != null ? storeIndentRequest.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",
				storeIndentRequest.getUnitId() != null ? storeIndentRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId",
				storeIndentRequest.getUserId() != null ? storeIndentRequest.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public StoreIndent getStoreIndent(Integer id) {
		String hql = "SELECT storeIndent FROM StoreIndent AS storeIndent WHERE storeIndent.id = :id  AND storeIndent.isActive = true AND storeIndent.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (StoreIndent) query.uniqueResult();
	}

	@Override
	public StoreIndent saveStoreIndent(StoreIndent storeIndent) {
		getSession().save(storeIndent);

		return storeIndent;
	}

	@Override
	public StoreIndent updateStoreIndent(StoreIndent storeIndent) {
		getSession().update(storeIndent);

		return storeIndent;
	}

	@Override
	public MaterialPicker saveMaterialPicker(MaterialPicker materialPicker) {
		getSession().save(materialPicker);

		return materialPicker;
	}

	@Override
	public List<StoreIndent> searchStoreIndentPending(StoreIndentRequest storeIndentRequest, Integer screenId) {

		String hql = "SELECT storeIndent FROM StoreIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.addedBy as user "
				+ "		LEFT JOIN storeIndent.fromStore as fromStore "
				+ "		LEFT JOIN storeIndent.toStore as toStore  " + "		LEFT JOIN storeIndent.status as status "
				+ "		LEFT JOIN storeIndent.indentType as indentType "
				+ "		LEFT JOIN storeIndent.priority as priority "
				+ "WHERE storeIndent.isActive = true AND storeIndent.isDeleted = false AND storeIndent.unit.unitId = :unitId "
				+ "		AND storeIndent.updatedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " 	AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId "
				+ "			AND toStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
				+ " 	AND (:indentNo = '' OR lower(storeIndent.indentNo) LIKE :indentNo) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ " 	AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " 	AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY storeIndent.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(storeIndentRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(storeIndentRequest.getToDate()));
		query.setParameter("fromStoreId",
				storeIndentRequest.getFromStoreId() != null ? storeIndentRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				storeIndentRequest.getToStoreId() != null ? storeIndentRequest.getToStoreId() : 0);
		query.setParameter("indentNo",
				storeIndentRequest.getIndentNo() != null ? storeIndentRequest.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", storeIndentRequest.getStatusId() != null ? storeIndentRequest.getStatusId() : 0);
		query.setParameter("indentTypeId",
				storeIndentRequest.getIndentTypeId() != null ? storeIndentRequest.getIndentTypeId() : 0);
		query.setParameter("priorityId",
				storeIndentRequest.getPriorityId() != null ? storeIndentRequest.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",
				storeIndentRequest.getUnitId() != null ? storeIndentRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId",
				storeIndentRequest.getUserId() != null ? storeIndentRequest.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public List<StoreIndentDetail> getStoreIndentDetail(Integer storeIndentId) {
		String hql = "SELECT storeIndentDetail FROM StoreIndentDetail AS storeIndentDetail \r\n"
				+ "		INNER JOIN storeIndentDetail.storeIndent AS storeIndent WITH storeIndent.isActive = true AND storeIndent.isDeleted = false \r\n"
				+ " WHERE storeIndent.id = :storeIndentId AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false  "
				+ " AND (storeIndentDetail.parentStoreIndentDtlId = 0 OR storeIndentDetail.parentStoreIndentDtlId IS NULL) "
				+ " ORDER BY storeIndentDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeIndentId", storeIndentId);

		return query.list();
	}

	@Override
	public List<StoreIndent> searchStoreIndentScreen(StoreIndentRequest storeIndentRequest, Integer screenId) {

		String hql = "SELECT storeIndent FROM StoreIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.addedBy as user "
				+ "		LEFT JOIN storeIndent.fromStore as fromStore "
				+ "		LEFT JOIN storeIndent.toStore as toStore  " + "		LEFT JOIN storeIndent.status as status "
				+ "		LEFT JOIN storeIndent.indentType as indentType "
				+ "		LEFT JOIN storeIndent.priority as priority "
				+ "WHERE storeIndent.isActive = true AND storeIndent.isDeleted = false AND storeIndent.unit.unitId = :unitId "
				+ "		AND storeIndent.updatedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " 	AND (:toStoreId = 0 OR toStore.storeId = :toStoreId )"
				+ "			AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ " 	AND (:indentNo = '' OR lower(storeIndent.indentNo) LIKE :indentNo) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ " 	AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " 	AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY storeIndent.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(storeIndentRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(storeIndentRequest.getToDate()));
		query.setParameter("fromStoreId",
				storeIndentRequest.getFromStoreId() != null ? storeIndentRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				storeIndentRequest.getToStoreId() != null ? storeIndentRequest.getToStoreId() : 0);
		query.setParameter("indentNo",
				storeIndentRequest.getIndentNo() != null ? '%'+storeIndentRequest.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", storeIndentRequest.getStatusId() != null ? storeIndentRequest.getStatusId() : 0);
		query.setParameter("indentTypeId",
				storeIndentRequest.getIndentTypeId() != null ? storeIndentRequest.getIndentTypeId() : 0);
		query.setParameter("priorityId",
				storeIndentRequest.getPriorityId() != null ? storeIndentRequest.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",
				storeIndentRequest.getUnitId() != null ? storeIndentRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId",
				storeIndentRequest.getUserId() != null ? storeIndentRequest.getUserId() : sessionService.getUserId());

		return query.list();
	}

	@Override
	public StoreIndentDetail getStoreIndentDetailById(Integer id) {
		String hql = "SELECT storeIndentDetail FROM StoreIndentDetail AS storeIndentDetail "
				+ "WHERE storeIndentDetail.id = :storeIndentId AND storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeIndentId", id);

		return (StoreIndentDetail) query.uniqueResult();
	}

	@Override
	public void updateStoreIndentD(Integer qty , Integer id) {
		getSession().createSQLQuery("update inventory.t_store_indent_detail set pending_quantity=authorized_quantity-"+qty+" where id="+id).executeUpdate();

	}

	@Override
	public StoreIndentDetail saveStoreIndentDetail(StoreIndentDetail storeIndentSubDetail) {
		getSessionFactory().getCurrentSession().save(storeIndentSubDetail);

		return storeIndentSubDetail;
	}

	@Override
	public Integer StoreIndentStatus(Integer id) {
		CommonDto idStatus = (CommonDto) getSessionFactory().getCurrentSession()
				.createSQLQuery("	select approval_status_id as \"id\" from inventory.t_store_indent ti  where  is_active = true and is_deleted = false and id ="+id)
				.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).uniqueResult();
		return idStatus.getId();
	}

	@Override
	public Integer updateStoreIndentForForceClose(StoreIndentApprovalRequest request) {
		try {
			getSession().createSQLQuery("update inventory.t_store_indent set force_close = true  where id="+request.getId()).executeUpdate();
			return 1;
		} catch (Exception e) {
			logger.error("Error while force closing the store indent", e);
			throw new ServiceException(e.getMessage());
		}
	}
}