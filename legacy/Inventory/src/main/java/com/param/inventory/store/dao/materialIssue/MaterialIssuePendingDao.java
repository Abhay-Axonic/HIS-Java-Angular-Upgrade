package com.param.inventory.store.dao.materialIssue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.global.common.ServiceException;
import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.CommonDto;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.materialPicker.MaterialPickerDetailResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.dto.materialPicker.SubstituteMaterialIssueDetailDto;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.SubstitutePatientConsumableDto;

@Repository("MaterialIssuePendingDao")
public class MaterialIssuePendingDao extends AbstractDao<Integer, MaterialIssue> implements IMaterialIssuePendingDao {

	private static final Logger logger = Logger.getLogger(MaterialIssuePendingDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public MaterialIssue getMaterialIssue(Integer id) {
		String hql = " SELECT materialIssue FROM MaterialIssue AS materialIssue "
				+ "	WHERE materialIssue.id = :id AND materialIssue.isActive = true AND materialIssue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (MaterialIssue) query.uniqueResult();
	}

	@Override
	public List<MaterialIssueDetail> getMaterialIssueDetail(Integer id) {
		String hql = "SELECT materialIssueDetail FROM MaterialIssueDetail AS materialIssueDetail \r\n"
				+ "	INNER JOIN materialIssueDetail.materialIssue AS materialIssue WITH materialIssue.isActive = true AND materialIssue.isDeleted = false \r\n"
				+ " WHERE materialIssue.id = :id AND materialIssueDetail.isActive = true AND materialIssueDetail.isDeleted = false \r\n"
				+ " ORDER BY materialIssueDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<MaterialIssueBatchMapper> getMaterialIssueBatchList(Integer id) {
		String hql = "SELECT materialIssueBatch FROM MaterialIssueBatchMapper AS materialIssueBatch "
				+ "		INNER JOIN materialIssueBatch.materialIssueDetail AS materialIssueDetail "
				+ " WHERE materialIssueDetail.id = :id AND materialIssueBatch.isActive = true AND materialIssueBatch.isDeleted = false "
				+ " ORDER BY materialIssueBatch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<MaterialIssue> searchMaterialIssueList(MaterialPickerRequest request, Integer screenId) {
		
		List<MaterialIssue> list = new ArrayList<>();
		String hql = "SELECT materialIssue FROM MaterialIssue AS materialIssue "
				+ "	INNER JOIN materialIssue.addedBy as user " + "	INNER JOIN materialIssue.fromStore as fromStore "
				+ "	INNER JOIN materialIssue.toStore as toStore " + "	INNER JOIN materialIssue.status as status "
				+ "	LEFT JOIN materialIssue.materialPicker.storeIndent.indentType as indentType "
				+ "LEFT JOIN materialIssue.materialPicker.storeIndent IND"
				+ "	LEFT JOIN materialIssue.materialPicker.storeIndent.priority as priority "
				+ "WHERE materialIssue.isActive = true AND materialIssue.isDeleted = false AND materialIssue.unit.unitId = :unitId "
				+ "	AND date(materialIssue.addedDate) BETWEEN :fromDate AND :toDate "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR  toStore.storeId = :toStoreId ) "
				+ "	AND toStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "

				+ " AND (:indentNo = '' OR lower(materialIssue.materialPicker.storeIndent.indentNo) LIKE :indentNo) "

				+ " AND (:issueNo = '' OR lower(materialIssue.issueNo) LIKE :issueNo) "

				+ " AND (:statusId = 0 OR status.id = :statusId ) "
				+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "	AND  status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status)  "
				+ "ORDER BY materialIssue.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("indentNo", request.getIndentNo() != null ? request.getIndentNo().toLowerCase() + '%' : "");
		query.setParameter("issueNo", request.getIssueNo() != null ? request.getIssueNo().toLowerCase() + '%' : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query.setParameter("priorityId", request.getPriorityId() != null ? request.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		list.addAll(query.list());
		
		String hql1 = "SELECT materialIssue FROM MaterialIssue AS materialIssue "
				+ "	INNER JOIN materialIssue.addedBy as user " + "	INNER JOIN materialIssue.fromStore as fromStore "
				+ "	INNER JOIN materialIssue.toStore as toStore " + "	INNER JOIN materialIssue.status as status "
				+ "	LEFT JOIN materialIssue.materialPicker.storeIndent.indentType as indentType "
				+ "LEFT JOIN materialIssue.storeIndent IND "
				+ "	LEFT JOIN materialIssue.materialPicker.storeIndent.priority as priority "
				+ "WHERE materialIssue.isActive = true AND materialIssue.isDeleted = false AND materialIssue.unit.unitId = :unitId "
				+ "	AND date(materialIssue.addedDate) BETWEEN :fromDate AND :toDate "
				+ " AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ " AND (:toStoreId = 0 OR  toStore.storeId = :toStoreId ) "
				+ "	AND toStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "

				+ " AND (:indentNo = '' OR lower(materialIssue.materialPicker.storeIndent.indentNo) LIKE :indentNo) "

				+ " AND (:issueNo = '' OR lower(materialIssue.issueNo) LIKE :issueNo) "

				+ " AND (:statusId = 0 OR status.id = :statusId  )"
				+ " AND IND.forceClose =  true "
				+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ " AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "ORDER BY materialIssue.id DESC";

		Query query1 = getSession().createQuery(hql1);
		query1.setParameter("fromDate", request.getFromDate());
		query1.setParameter("toDate", request.getToDate());
		query1.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query1.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query1.setParameter("indentNo", request.getIndentNo() != null ? request.getIndentNo().toLowerCase() + '%' : "");
		query1.setParameter("issueNo", request.getIssueNo() != null ? request.getIssueNo().toLowerCase() + '%' : "");
		query1.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query1.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
		query1.setParameter("priorityId", request.getPriorityId() != null ? request.getPriorityId() : 0);
		query1.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query1.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		list.addAll(query1.list());
		
		return list ;
	}

	@Override
	public MaterialIssueDetail getMaterialIssueDetailById(Integer id) {
		String hql = "SELECT materialIssueDetail FROM MaterialIssueDetail AS materialIssueDetail \r\n"
				+ "	INNER JOIN materialIssueDetail.materialIssue AS materialIssue WITH materialIssue.isActive = true AND materialIssue.isDeleted = false \r\n"
				+ " WHERE materialIssueDetail.id = :id AND materialIssueDetail.isActive = true AND materialIssueDetail.isDeleted = false \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (MaterialIssueDetail) query.uniqueResult();
	}

	@Override
	public Long getbatchIdByMaterialIssueDetailsId(Integer id) {
		String hql = "SELECT batch.id FROM MaterialIssueBatchMapper AS materialIssueBatchMapper "
				+ " INNER JOIN materialIssueBatchMapper.batch AS batch  WITH batch.isActive = true AND batch.isDeleted = false \r\n"
				+ " WHERE batch.id = :id AND materialIssueBatchMapper.isActive = true AND materialIssueBatchMapper.isDeleted = false \r\n"
				+ " ORDER BY materialIssueBatchMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (Long) query.uniqueResult();
	}

	@Override
	public MaterialIssue saveMaterialIssuePending(MaterialIssue materialIssue) {
		getSession().save(materialIssue);
		return materialIssue;
	}

	@Override
	public MaterialIssue updateMaterialIssue(MaterialIssue materialIssue) {
		getSession().update(materialIssue);

		return materialIssue;
	}

	@Override
	public StoreStock updateStoreStock(StoreStock storeStock) {
		getSession().update(storeStock);
		return storeStock;
	}

	@Override
	public MaterialIssueDetail saveMaterialIssuePendingDetails(MaterialIssueDetail detail) {
		getSession().save(detail);
		return detail;
	}

	/* New Search Method for Material Issue Pending */
	@Override
	public List<MaterialPickerResponse> searchMaterialIssuePendingList(MaterialPickerRequest materialPickerRequest,
			Integer screenId) {
		String hql = "SELECT materialIssue.id AS id, materialIssue.materialPicker.id AS pickerId ,storeIndent.addedDate AS indentDate, storeIndent.indentNo AS indentNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		priority.priorityDesc AS priority, indentType.type AS indentType, storeIndent.isConsignment AS isConsignment, "
				+ "		materialIssue.remark AS remark, status.status AS status"
				+ " FROM MaterialIssue AS materialIssue " + "		INNER JOIN materialIssue.addedBy as user "
				+ "		INNER JOIN materialIssue.storeIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.fromStore AS fromStore "
				+ "		INNER JOIN storeIndent.toStore AS toStore "
				+ "		INNER JOIN storeIndent.priority AS priority "
				+ "		INNER JOIN storeIndent.indentType AS indentType "
				+ "		INNER JOIN materialIssue.status AS status "
				+ "	WHERE materialIssue.isActive = true AND materialIssue.isDeleted = false AND materialIssue.unit.unitId = :unitId and (storeIndent.forceClose <>true or storeIndent.forceClose is null ) "
				+ " 	AND storeIndent.status!=9 AND storeIndent.approvalStatus!=7 AND materialIssue.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ "     AND (:toStoreId = 0 OR toStore.storeId = :toStoreId  ) "
				+ "			AND toStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ "     AND (:indentNo = '' OR lower(materialIssue.storeIndent.indentNo) LIKE :indentNo) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
				+ "		AND (:priorityId = 0 OR priority.priorityId = :priorityId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY priority.priorityId ASC, materialIssue.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(materialPickerRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(materialPickerRequest.getToDate()));
		query.setParameter("fromStoreId",
				materialPickerRequest.getFromStoreId() != null ? materialPickerRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId",
				materialPickerRequest.getToStoreId() != null ? materialPickerRequest.getToStoreId() : 0);
		query.setParameter("indentNo",
				materialPickerRequest.getIndentNo() != null ? '%'+materialPickerRequest.getIndentNo().toLowerCase() + '%'
						: "");
		query.setParameter("statusId",
				materialPickerRequest.getStatusId() != null ? materialPickerRequest.getStatusId() : 0);
		query.setParameter("indentTypeId",
				materialPickerRequest.getIndentTypeId() != null ? materialPickerRequest.getIndentTypeId() : 0);
		query.setParameter("priorityId",
				materialPickerRequest.getPriorityId() != null ? materialPickerRequest.getPriorityId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", materialPickerRequest.getUnitId()!=null?materialPickerRequest.getUnitId():sessionService.getUnitId());
		query.setParameter("userId", materialPickerRequest.getUserId()!=null?materialPickerRequest.getUserId():sessionService.getUserId());
		query.setResultTransformer(Transformers.aliasToBean(MaterialPickerResponse.class));

		return query.list();
	}

	@Override
	public MaterialPickerResponse getMaterialIssuePending(Integer id) {
		String hql = "SELECT materialIssue.id AS id, materialIssue.materialPicker.id AS pickerId ,storeIndent.addedDate AS indentDate, storeIndent.indentNo AS indentNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		priority.priorityDesc AS priority, indentType.type AS indentType, storeIndent.isConsignment AS isConsignment, "
				+ "		materialIssue.remark AS remark, status.status AS status, picker.userName AS pickerName"
				+ " FROM MaterialIssue AS materialIssue "
				+ "		INNER JOIN materialIssue.storeIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.fromStore AS fromStore "
				+ "		INNER JOIN storeIndent.toStore AS toStore "
				+ "		INNER JOIN storeIndent.priority AS priority "
				+ "		INNER JOIN storeIndent.indentType AS indentType "
				+ "		INNER JOIN materialIssue.status AS status " + "		INNER JOIN materialIssue.picker AS picker "
				+ "	WHERE materialIssue.id = :id AND materialIssue.isActive = true AND materialIssue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(MaterialPickerResponse.class));

		return (MaterialPickerResponse) query.uniqueResult();

	}

	@Override
	public MaterialPickerResponse getMaterialIssuePendingWithoutPicker(Integer id) {
		String hql = "SELECT materialIssue.id AS id,storeIndent.addedDate AS indentDate, storeIndent.indentNo AS indentNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		priority.priorityDesc AS priority, indentType.type AS indentType, storeIndent.isConsignment AS isConsignment, "
				+ "		materialIssue.remark AS remark, status.status AS status"
				+ " FROM MaterialIssue AS materialIssue "
				+ "		INNER JOIN materialIssue.storeIndent AS storeIndent "
				+ "		INNER JOIN storeIndent.fromStore AS fromStore "
				+ "		INNER JOIN storeIndent.toStore AS toStore "
				+ "		INNER JOIN storeIndent.priority AS priority "
				+ "		INNER JOIN storeIndent.indentType AS indentType "
				+ "		INNER JOIN materialIssue.status AS status "
				// + " INNER JOIN materialIssue.picker AS picker "
				+ "	WHERE materialIssue.id = :id AND materialIssue.isActive = true AND materialIssue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(MaterialPickerResponse.class));

		return (MaterialPickerResponse) query.uniqueResult();

	}

	@Override
	public List<MaterialPickerDetailResponse> getMaterialIssuePendingDetail(Integer id) {

		String hql = "SELECT materialIssueDetails.id AS id, materialIssue.id as materialIssueId, item.id AS itemId, item.code AS itemCode, item.name AS itemName, generic.id AS genericId, storeIndentDetail.id as storeIndentDetailId, "
				+ "		generic.name AS genericName, uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, item.strength as dose , strengthUnit.units as doseUnit, "
				+ "		storeIndentDetail.indentQuantity AS indentQuantity, storeIndentDetail.pendingQuantity AS pendingQuantity, materialIssueDetails.parentMaterialIssueId AS parentMaterialIssueId, "
				+ "	    COALESCE(SUM(materialIssueDetails.issueQuantity), 0) AS issueQuantity , item.isBatchRequired as isBatchRequired ,item.formulationType.id as formulationTypeId , storeIndentDetail.authorizedQuantity as indentAppprovedQuantity, item.isThreeUOMApplicable as isThreeUOMApplicable  "
				+ " FROM MaterialIssue AS materialIssue "
				+ " 	INNER JOIN materialIssue.materialIssueDetails AS materialIssueDetails WITH materialIssueDetails.isActive = true AND materialIssueDetails.isDeleted = false "
				+ "		INNER JOIN materialIssueDetails.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		LEFT JOIN item.strengthUnit AS strengthUnit WITH strengthUnit.isDeleted = false AND strengthUnit.isActive = true "
				+ "		INNER JOIN item.unitOfMeasurementList AS uom WITH uom.isActive = true AND uom.isDeleted = false AND uom.storeUom = true "
				+ "		INNER JOIN materialIssueDetails.storeIndentDetail AS storeIndentDetail WITH storeIndentDetail.isActive = true and storeIndentDetail.isDeleted = false "
				+ "		INNER JOIN storeIndentDetail.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN storeIndentDetail.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "	WHERE materialIssue.id = :id AND materialIssue.isActive = true AND materialIssue.isDeleted = false and storeIndentDetail.authorizedQuantity>0 "
				+ "  GROUP BY materialIssueDetails.id, materialIssue.id, item.id, item.code, item.name, generic.id, generic.name, storeIndentDetail.id, uomType.id, uomType.type, uomUnit.id, uomUnit.units,item.strength, strengthUnit.units ,"
				+ "		storeIndentDetail.indentQuantity, storeIndentDetail.pendingQuantity,item.formulationType.id ,storeIndentDetail.authorizedQuantity, materialIssueDetails.parentMaterialIssueId, "
				+ " item.isBatchRequired ,item.formulationType.id,  item.isThreeUOMApplicable ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(MaterialPickerDetailResponse.class));

		return query.list();
	}

	@Override
	public MaterialIssueDetail getPickerQty(Integer id) {

		String hql = "SELECT materialIssueDetail FROM MaterialIssueDetail AS materialIssueDetail  "
				+ " WHERE materialIssueDetail.id = :id AND materialIssueDetail.isActive = true AND materialIssueDetail.isDeleted = false  ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (MaterialIssueDetail) query.uniqueResult();

	}

	@Override
	public MaterialIssueDetail getMaterialPickerData(Integer id) {
		// materialPicker

		String hql = "SELECT materialIssueDetail FROM MaterialIssueDetail AS materialIssueDetail \r\n"
				+ "	INNER JOIN materialIssueDetail.materialIssue AS materialIssue WITH materialIssue.isActive = true AND materialIssue.isDeleted = false \r\n"
				+ " WHERE materialIssue.materialPicker.id = :id AND materialIssueDetail.isActive = true AND materialIssueDetail.isDeleted = false \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		if (query.list().size() > 0) {
			return (MaterialIssueDetail) query.list().get(0);
		} else {
			return null;
		}

	}

	@Override
	public void updateMaterialIssuePending(MaterialIssue materialIssue) {

		getSession().update(materialIssue);
		//return materialIssue;
	
	}

	@Override
	public MaterialIssueDetail getMaterialIssueDetailsForSubstitute(Integer parentId) {
		try {
			String hql = " SELECT materialIssueDetail FROM MaterialIssueDetail AS materialIssueDetail "
					+ " WHERE materialIssueDetail.isActive = true AND materialIssueDetail.isDeleted = false AND materialIssueDetail.id = :id"
					+ " AND materialIssueDetail.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", parentId);
			query.setParameter("unitId", sessionService.getUnitId());

			return (MaterialIssueDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while fetching material issue details for substitute", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<SubstituteMaterialIssueDetailDto> getSubstituteMaterialIssueDetails(Integer id) {
		try {
			String hql = " select  " 
					+ "	tid.id as \"id\", "
					+ "	tid.material_issue_id as \"materialIssueId\", " 
					+ "	tid.material_picker_detail_id as \"materialPickerDetailId\", "
					+ "	tid.store_indent_detail_id as \"storeIndentDetailId\", " 
					+ "	tid.issue_quantity as \"issueQuantity\", "
					+ "	tid.item_id as \"itemId\", " 
					+ "	tid.parent_material_issue_id as \"parentMaterialIssueId\", "
					+ "	mi.name as \"itemName\",  " 
					+ "	mi.code as \"itemCode\",  "
					+ "	generic.name as \"genericName\",  "
					+ "	tpid.indent_quantity as \"indentQuantity\"  " 
					+ " from  "
					+ "	inventory.t_material_issue_detail tid  "
					+ "	inner join inventory.t_store_indent_detail tpid on tpid.id =tid.store_indent_detail_id  "
					+ "	inner join public.m_item mi on tpid.item_id =mi.id   "
					+ "	inner join public.m_uom_unit muu on muu.id =tpid.uom_unit_id   "
					+ "	inner join public.m_uom_type mut on mut.id=tpid.uom_type_id   "
					+ " inner join public.m_generic generic on generic.id =mi.generic_id  " 
					+ " where  "
					+ " tid.material_issue_id = :id " 
					+ "	and tid.parent_issue_detail_id is not null"
					;
			Query query = getSession().createSQLQuery(hql)
					.addScalar("id")
					.addScalar("materialIssueId")
					.addScalar("materialPickerDetailId")
					.addScalar("storeIndentDetailId")
					.addScalar("issueQuantity")
					.addScalar("itemId")
					.addScalar("genericName")
					.addScalar("parentMaterialIssueId")
					.addScalar("netAmoun");
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(SubstitutePatientConsumableDto.class));

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching substitute material issue details", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<MaterialIssueBatchMapper> getMaterialBatchMapperForSubstitute(Integer id) {
		String hql = "SELECT materialIssueBatch FROM MaterialIssueBatchMapper AS materialIssueBatch "
				+ "		INNER JOIN materialIssueBatch.materialIssueDetail AS materialIssueDetail "
				+ " WHERE materialIssueDetail.id = :id AND materialIssueBatch.isActive = true AND materialIssueBatch.isDeleted = false "
				+ " ORDER BY materialIssueBatch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public StoreIndentDetail getStoreIndentDetailsForSubstitute(Integer id) {
		try {
			String hql = " SELECT storeIndentDetail FROM StoreIndentDetail AS storeIndentDetail "
					+ " WHERE storeIndentDetail.isActive = true AND storeIndentDetail.isDeleted = false AND storeIndentDetail.id = :id"
					+ " AND storeIndentDetail.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());

			return (StoreIndentDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while fetching store indent details for substitute", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public MaterialIssueDetail saveMaterialIssuePendingDetailsForSubstitute(MaterialIssueDetail mterialDtl) {
		getSession().save(mterialDtl);
		return mterialDtl;
	}

	@Override
	public List<SubstituteMaterialIssueDetailDto> getSubstituteIssueDetailsForMaterialIssueDetail(Integer materialIssueId) {
		try {
			String hql = " select  " 
					+ "	tid.id as \"id\", "
					+ "	tid.material_issue_id as \"materialIssueId\", " 
					+ "	tid.material_picker_detail_id as \"materialPickerDetailId\", "
					+ "	tid.store_indent_detail_id as \"storeIndentDetailId\", " 
					+ "	tid.issue_quantity as \"issueQuantity\", "
					+ "	tid.item_id as \"itemId\", " 
					+ "	tid.parent_material_issue_id as \"parentMaterialIssueId\", "
					+ "	mi.name as \"itemName\",  " 
					+ "	mi.code as \"itemCode\",  "
					+ " muu.id as \"uomUnitId\", " + 
					"	muu.unit as \"uomUnit\", " + 
					"	mut.id as \"uomTypeId\", " + 
					"	mut.type as \"uomType\", "
					+ "	generic.name as \"genericName\",  "
					+ "	tpid.indent_quantity as \"indentQuantity\"  " 
					+ " from  "
					+ "	inventory.t_material_issue_detail tid  "
					+ "	inner join inventory.t_store_indent_detail tpid on tpid.id =tid.store_indent_detail_id  "
					+ "	inner join public.m_item mi on tid.item_id =mi.id   "
					+ "	inner join public.m_uom_unit muu on muu.id =tpid.uom_unit_id   "
					+ "	inner join public.m_uom_type mut on mut.id=tpid.uom_type_id   "
					+ " inner join public.m_generic generic on generic.id =mi.generic_id  " 
					+ " where  "
					+ " tid.material_issue_id = :id " 
					+ "	and tid.parent_material_issue_id is not null"
					;
			Query query = getSession().createSQLQuery(hql)
					.addScalar("id")
					.addScalar("materialIssueId")
					.addScalar("materialPickerDetailId")
					.addScalar("storeIndentDetailId")
					.addScalar("issueQuantity")
					.addScalar("itemId")
					.addScalar("itemName")
					.addScalar("itemCode")
					.addScalar("genericName")
					.addScalar("uomUnitId")
					.addScalar("uomUnit")
					.addScalar("uomTypeId")
					.addScalar("uomType")
					.addScalar("indentQuantity")
					.addScalar("parentMaterialIssueId");
			query.setParameter("id", materialIssueId);
			query.setResultTransformer(Transformers.aliasToBean(SubstituteMaterialIssueDetailDto.class));

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching substitute issue details for material issue detail", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<MaterialIssueBatchMapper> getMaaterialBatchMapperForSubstitute(Integer id) {
		try {
			String hql = " SELECT materialIssueBatchMapper FROM MaterialIssueBatchMapper AS materialIssueBatchMapper "
					+ " WHERE materialIssueBatchMapper.isActive = true AND materialIssueBatchMapper.isDeleted = false AND materialIssueBatchMapper.materialIssueDetail.id =:id "
					+ " AND materialIssueBatchMapper.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching material batch mapper for substitute", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public SubstituteMaterialIssueDetailDto getMaterialIssueForSub(Integer id) {
//		String hql = " SELECT materialIssue.id,  FROM MaterialIssue AS materialIssue "
//				+ "	WHERE materialIssue.id = :id AND materialIssue.isActive = true AND materialIssue.isDeleted = false ";
		String hql = " select " + 
				"	tmi.id as \"id\", " + 
				"	tmi.material_picker_id as \"materialPickerId\", " + 
				"	tmi.store_indent_id as \"storeIndentId\", " + 
				"	tmi.carrier_id as \"carrierId\", " + 
				"	tmi.status_id as \"statusId\", " + 
				"	tmi.remark as \"remark\", " + 
				"	tmi.from_store_id as \"fromStoreId\", " + 
				"	tmi.to_store_id as \"toStoreId\", " + 
				"	tmi.issue_no as \"issueNo\", " + 
				"	tmi.picker_id as \"pickerId\", " + 
				"	tmi.is_consignment as \"isConsignment\" " + 
				"from " + 
				"	inventory.t_material_issue tmi " + 
				"where " + 
				"	id = :id ";
		Query query = getSession().createSQLQuery(hql)
				.addScalar("id")
				.addScalar("fromStoreId")
				.addScalar("toStoreId")
				.addScalar("materialPickerId")
				.addScalar("storeIndentId")
				.addScalar("carrierId")
				.addScalar("statusId")
				.addScalar("remark")
				.addScalar("issueNo")
				.addScalar("isConsignment")
				.addScalar("pickerId");

		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(SubstituteMaterialIssueDetailDto.class));
		return (SubstituteMaterialIssueDetailDto) query.uniqueResult();
	}

	@Override
	public void updateStoreIndentStatus(Integer id, Integer status) {
		getSession().createSQLQuery(" update inventory.t_store_indent set status_id  = "+status+" where id="+id).executeUpdate();
		
	}

	@Override
	public Integer saveMaterialIssue(Integer id) {
        CommonDto idStatus = (CommonDto) getSessionFactory().getCurrentSession()
                .createSQLQuery("    select status_id as \"id\" from inventory.t_material_issue ti  where  is_active = true and is_deleted = false and id ="+id)
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).uniqueResult();
        return idStatus.getId();
	}

	@Override
	public List<UomResponse> getStoreStockByItemPharmacyAll(Integer toStoreId, Integer unitId) {
		Date date = TimeZoneComponent.getDateByzone(unitId);
		String sql = "SELECT  coalesce(stock.totalQty, 0) AS stockAvailable,item.id AS itemId "
				+ " FROM public.m_item item "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ " INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.is_quarantine=false )"
				+ " and (to_char(batch.expiry_date,'YYYY-MM-DD') >= '"+GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd")+"' or batch.expiry_date is null )"
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0  "
				+ " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";
		Query query = getSession().createSQLQuery(sql)
				.addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameter("storeId", toStoreId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
        query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public List<MaterialIssueBatchMapper> getMaterialIssueBatchListAll(List<Integer> issueIds) {
		String hql = "SELECT materialIssueBatch FROM MaterialIssueBatchMapper AS materialIssueBatch "
				+ "		INNER JOIN materialIssueBatch.materialIssueDetail AS materialIssueDetail "
				+ " WHERE materialIssueDetail.id in(:id) AND materialIssueBatch.isActive = true AND materialIssueBatch.isDeleted = false "
				+ " ORDER BY materialIssueBatch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", issueIds);

		return query.list();
	}

	@Override
	public List<MaterialIssueDetail> getPickerQtyAll(List<Integer> issueIds) {
		String hql = "SELECT materialIssueDetail FROM MaterialIssueDetail AS materialIssueDetail  "
				+ " WHERE materialIssueDetail.id in (:id) AND materialIssueDetail.isActive = true AND materialIssueDetail.isDeleted = false  ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", issueIds);

		return query.list();
	}

	@Override
	public List<SubstituteMaterialIssueDetailDto> getSubstituteIssueDetailsForMaterialIssueDetailAll(
			List<Integer> materialIssueIds) {
		String hql = " select  " 
				+ "	tid.id as \"id\", "
				+ "	tid.material_issue_id as \"materialIssueId\", " 
				+ "	tid.material_picker_detail_id as \"materialPickerDetailId\", "
				+ "	tid.store_indent_detail_id as \"storeIndentDetailId\", " 
				+ "	tid.issue_quantity as \"issueQuantity\", "
				+ "	tid.item_id as \"itemId\", " 
				+ "	tid.parent_material_issue_id as \"parentMaterialIssueId\", "
				+ "	mi.name as \"itemName\",  " 
				+ "	mi.code as \"itemCode\",  "
				+ " muu.id as \"uomUnitId\", " + 
				"	muu.unit as \"uomUnit\", " + 
				"	mut.id as \"uomTypeId\", " + 
				"	mut.type as \"uomType\", "
				+ "	generic.name as \"genericName\",  "
				+ "	tpid.indent_quantity as \"indentQuantity\"  " 
				+ " from  "
				+ "	inventory.t_material_issue_detail tid  "
				+ "	inner join inventory.t_store_indent_detail tpid on tpid.id =tid.store_indent_detail_id  "
				+ "	inner join public.m_item mi on tid.item_id =mi.id   "
				+ "	inner join public.m_uom_unit muu on muu.id =tpid.uom_unit_id   "
				+ "	inner join public.m_uom_type mut on mut.id=tpid.uom_type_id   "
				+ " inner join public.m_generic generic on generic.id =mi.generic_id  " 
				+ " where  "
				+ " tid.material_issue_id in(:id) " 
				+ "	and tid.parent_material_issue_id is not null"
				;
		Query query = getSession().createSQLQuery(hql)
				.addScalar("id")
				.addScalar("materialIssueId")
				.addScalar("materialPickerDetailId")
				.addScalar("storeIndentDetailId")
				.addScalar("issueQuantity")
				.addScalar("itemId")
				.addScalar("itemName")
				.addScalar("itemCode")
				.addScalar("genericName")
				.addScalar("uomUnitId")
				.addScalar("uomUnit")
				.addScalar("uomTypeId")
				.addScalar("uomType")
				.addScalar("indentQuantity")
				.addScalar("parentMaterialIssueId");
		query.setParameterList("id", materialIssueIds);
		query.setResultTransformer(Transformers.aliasToBean(SubstituteMaterialIssueDetailDto.class));

		return query.list();
	}

	@Override
	public List<MaterialIssueBatchMapper> getMaaterialBatchMapperForSubstituteAll(List<Integer> subDetailsIds) {
		try {
			String hql = " SELECT materialIssueBatchMapper FROM MaterialIssueBatchMapper AS materialIssueBatchMapper "
					+ " WHERE materialIssueBatchMapper.isActive = true AND materialIssueBatchMapper.isDeleted = false AND materialIssueBatchMapper.materialIssueDetail.id IN (:id) "
					+ " AND materialIssueBatchMapper.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameterList("id", subDetailsIds);
			query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("Error while fetching material batch mapper for substitute (batch)", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
