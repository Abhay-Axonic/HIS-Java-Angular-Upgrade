
package com.param.inventory.store.dao.materialPicker;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.MaterialPicker;
import com.param.entity.model.inventory.MaterialPickerDetail;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;

@Repository("MaterialPickerDao")
public class MaterialPickerDao extends AbstractDao<Integer, MaterialPicker> implements IMaterialPickerDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<MaterialPickerResponse> searchMaterialPickerList(MaterialPickerRequest materialPickerRequest,
			Integer screenId) {

		String hql = "SELECT materialPicker.id AS id, storeIndent.addedDate AS indentDate, storeIndent.indentNo AS indentNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		priority.priorityDesc AS priority, indentType.type AS indentType, storeIndent.isConsignment AS isConsignment, "
				+ "		materialPicker.remark AS remark, status.status AS status "
				+ " FROM MaterialPicker AS materialPicker " + "		INNER JOIN materialPicker.addedBy as user "
				+ "		INNER JOIN materialPicker.storeIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.fromStore AS fromStore "
				+ "		INNER JOIN storeIndent.toStore AS toStore "
				+ "		INNER JOIN storeIndent.priority AS priority "
				+ "		INNER JOIN storeIndent.indentType AS indentType "
				+ "		INNER JOIN materialPicker.status AS status "
				+ "	WHERE materialPicker.isActive = true AND materialPicker.isDeleted = false AND materialPicker.unit.unitId = :unitId "
				+ " 	AND materialPicker.addedDate BETWEEN :fromDate AND :toDate "
				// + " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ "     AND (:toStoreId = 0 OR toStore.storeId = :toStoreId ) "
				+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId ) "
				+ "			AND toStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "

				+ "     AND (:indentNo = '' OR lower(materialPicker.storeIndent.indentNo) LIKE :indentNo) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ "		AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY priority.priorityId ASC, materialPicker.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(materialPickerRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(materialPickerRequest.getToDate()));
		query.setParameter("fromStoreId",
				materialPickerRequest.getFromStoreId() != null ? materialPickerRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				materialPickerRequest.getToStoreId() != null ? materialPickerRequest.getToStoreId() : 0);
		query.setParameter("indentNo",
				materialPickerRequest.getIndentNo() != null ? materialPickerRequest.getIndentNo().toLowerCase() + '%'
						: "");
		query.setParameter("statusId",
				materialPickerRequest.getStatusId() != null ? materialPickerRequest.getStatusId() : 0);
		query.setParameter("indentTypeId",
				materialPickerRequest.getIndentTypeId() != null ? materialPickerRequest.getIndentTypeId() : 0);
		query.setParameter("priorityId",
				materialPickerRequest.getPriorityId() != null ? materialPickerRequest.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", materialPickerRequest.getUnitId()!=null ? materialPickerRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", materialPickerRequest.getUserId()!=null?materialPickerRequest.getUserId() : sessionService.getUserId());
		query.setResultTransformer(Transformers.aliasToBean(MaterialPickerResponse.class));

		return query.list();
	}

	@Override
	public MaterialPickerResponse getMaterialPicker(Integer id) {
		String hql = "SELECT materialPicker.id AS id, storeIndent.addedDate AS indentDate, storeIndent.indentNo AS indentNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		priority.priorityDesc AS priority, indentType.type AS indentType, storeIndent.isConsignment AS isConsignment, "
				+ "		materialPicker.remark AS remark, status.status AS status "
				+ " FROM MaterialPicker AS materialPicker "
				+ "		INNER JOIN materialPicker.storeIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.fromStore AS fromStore "
				+ "		INNER JOIN storeIndent.toStore AS toStore "
				+ "		INNER JOIN storeIndent.priority AS priority "
				+ "		INNER JOIN storeIndent.indentType AS indentType "
				+ "		INNER JOIN materialPicker.status AS status "
				+ "	WHERE materialPicker.id = :id AND materialPicker.isActive = true AND materialPicker.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(MaterialPickerResponse.class));

		return (MaterialPickerResponse) query.uniqueResult();
	}

	@Override
	public MaterialPicker getMaterialPickerByIndent(Integer id) {
		String hql = "SELECT materialPicker FROM MaterialPicker AS materialPicker "
				+ "	WHERE materialPicker.storeIndent.id = :id AND materialPicker.isActive = true AND materialPicker.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		MaterialPicker materialPicker = (MaterialPicker) query.uniqueResult();
		// query.setResultTransformer(Transformers.aliasToBean(MaterialPickerResponse.class));

		return materialPicker;
	}

	public List<MaterialPickerDetailResponse> getMaterialPickerDetails(Integer id) {
		String hql = "SELECT materialPickerDetails.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName,"
				+ "		generic.name AS genericName, uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		storeIndentDetail.authorizedQuantity AS indentQuantity, storeIndentDetail.pendingQuantity AS pendingQuantity, "
				+ "		materialPickerDetails.pickerQuantity AS pickerQuantity, COALESCE(SUM(materialIssueDetail.issueQuantity), 0) AS issueQuantity "
				+ " FROM MaterialPicker AS materialPicker "
				+ " 	INNER JOIN materialPicker.materialPickerDetails AS materialPickerDetails WITH materialPickerDetails.isActive = true AND materialPickerDetails.isDeleted = false "
				+ "		INNER JOIN materialPickerDetails.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN item.unitOfMeasurementList AS uom WITH uom.isActive = true AND uom.isDeleted = false AND uom.storeUom = true "
				+ "		INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN uom.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		INNER JOIN materialPickerDetails.storeIndentDetail AS storeIndentDetail WITH storeIndentDetail.isActive = true and storeIndentDetail.isDeleted = false "
				+ "		LEFT JOIN materialPickerDetails.materialIssueDetail AS materialIssueDetail WITH materialIssueDetail.isActive = true and materialIssueDetail.isDeleted = false "
				+ "	WHERE materialPicker.id = :id AND materialPicker.isActive = true AND materialPicker.isDeleted = false "
				+ "  GROUP BY materialPickerDetails.id, item.id, item.code, item.name, generic.name, uomType.id, uomType.type, uomUnit.id, uomUnit.units, "
				+ "		storeIndentDetail.authorizedQuantity, storeIndentDetail.pendingQuantity, materialPickerDetails.pickerQuantity ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(MaterialPickerDetailResponse.class));

		return query.list();
	}

	@Override
	public List<MaterialIssueBatchMapper> getMaterialPickerBatchList(Integer id) {
		String hql = "SELECT materialIssueBatch FROM MaterialIssueBatchMapper AS materialIssueBatch "
				+ "		INNER JOIN materialIssueBatch.materialIssueDetail AS materialIssueDetail "
				+ "		INNER JOIN materialIssueDetail.materialPickerDetail AS materialPickerDetail "
				+ " WHERE materialIssueDetail.materialPickerDetail.id = :id AND materialIssueBatch.isActive = true AND materialIssueBatch.isDeleted = false "
				+ " ORDER BY materialIssueBatch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public MaterialPicker fetchMaterialPicker(Integer id) {
		String hql = "SELECT materialPicker FROM MaterialPicker AS materialPicker "
				+ "	WHERE materialPicker.id = :id AND materialPicker.isActive = true AND materialPicker.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (MaterialPicker) query.uniqueResult();
	}

	@Override
	public List<MaterialPickerDetail> fetchMaterialPickerDetail(Integer id) {
		String hql = "SELECT materialPickerDetail FROM MaterialPickerDetail AS materialPickerDetail \r\n"
				+ "	INNER JOIN materialPickerDetail.materialPicker AS materialPicker WITH materialPicker.isActive = true AND materialPicker.isDeleted = false \r\n"
				+ " WHERE materialPicker.id = :id AND materialPickerDetail.isActive = true AND materialPickerDetail.isDeleted = false \r\n"
				+ " ORDER BY materialPickerDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public MaterialIssue saveMaterialIssue(MaterialIssue materialIssue) {
		getSession().save(materialIssue);

		return materialIssue;
	}

	// Material Issue Methods
	@Override
	public List<MaterialPicker> searchMaterialPicker(MaterialPickerRequest materialPickerRequest, Integer screenId) {
		String hql = "SELECT materialPicker FROM MaterialPicker AS materialPicker "
				+ "	    INNER JOIN materialPicker.addedBy as user "
				+ " 	INNER JOIN materialPicker.storeIndent.fromStore as fromStore "
				+ " 	INNER JOIN materialPicker.storeIndent.toStore as toStore "
				+ "	    INNER JOIN materialPicker.status as status "
				+ " 	INNER JOIN materialPicker.storeIndent.indentType as indentType "
				+ " 	INNER JOIN materialPicker.storeIndent.priority as priority "
				+ "WHERE materialPicker.isActive = true AND materialPicker.isDeleted = false AND materialPicker.unit.unitId = :unitId "
				+ " 	AND materialPicker.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ "     AND (:toStoreId = 0 OR toStore.storeId = :toStoreId )"
				/*
				 * +
				 * "	           AND fromStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				 * +
				 * "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				 * +
				 * "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "
				 */ + "    AND (:indentNo = '' OR lower(materialPicker.storeIndent.indentNo) LIKE :indentNo) "
				+ "    AND (:statusId = 0 OR status.id = :statusId) "
				+ "    AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ "    AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "	   AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY materialPicker.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", materialPickerRequest.getFromDate());
		query.setParameter("toDate", materialPickerRequest.getToDate());
		query.setParameter("fromStoreId",
				materialPickerRequest.getFromStoreId() != null ? materialPickerRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				materialPickerRequest.getToStoreId() != null ? materialPickerRequest.getToStoreId() : 0);
		query.setParameter("indentNo",
				materialPickerRequest.getIndentNo() != null ? materialPickerRequest.getIndentNo().toLowerCase() + '%'
						: "");
		query.setParameter("statusId",
				materialPickerRequest.getStatusId() != null ? materialPickerRequest.getStatusId() : 0);
		query.setParameter("indentTypeId",
				materialPickerRequest.getIndentTypeId() != null ? materialPickerRequest.getIndentTypeId() : 0);
		query.setParameter("priorityId",
				materialPickerRequest.getPriorityId() != null ? materialPickerRequest.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		// query.setParameter("userId", sessionService.getUserId());

		return query.list();
	}

	@Override
	public List<MaterialPickerDetail> getMaterialPickerDetail(Integer id) {
		String hql = "SELECT materialPickerDetail FROM MaterialPickerDetail AS materialPickerDetail \r\n"
				+ "	INNER JOIN materialPickerDetail.materialPicker AS materialPicker WITH materialPicker.isActive = true AND materialPicker.isDeleted = false \r\n"
				+ " WHERE materialPicker.id = :id AND materialPickerDetail.isActive = true AND materialPickerDetail.isDeleted = false \r\n"
				+ " ORDER BY materialPickerDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public MaterialPickerResponse getPickerData(Integer id) {

		String hql = "SELECT materialPickerDetail FROM MaterialPickerDetail AS materialPickerDetail \r\n"
				+ "	INNER JOIN materialPickerDetail.materialPicker AS materialPicker WITH materialPicker.isActive = true AND materialPicker.isDeleted = false \r\n"
				+ " WHERE materialPicker.id = :id AND materialPickerDetail.isActive = true AND materialPickerDetail.isDeleted = false \r\n"
				+ " ORDER BY materialPickerDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		if(query.list().size() >0)
		{
			return (MaterialPickerResponse) query.list().get(0);
		}
		else
		{
			return null;
		}
	
	}

	@Override
	public MaterialIssue getMaterialIssue(Integer id) {

		String hql = "SELECT materialIssue FROM MaterialIssue AS materialIssue "
				+ "	WHERE materialIssue.materialPicker.id = :id AND materialIssue.isActive = true AND materialIssue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (MaterialIssue) query.uniqueResult();
	
	}

	@Override
	public void updateMaterialIssue(MaterialIssue maIssue) {
		getSession().update(maIssue);
		
	}

	@Override
	public void saveMaterialIssueDetails(MaterialIssueDetail issueDetail) {
		getSession().save(issueDetail);
		
	}

}
