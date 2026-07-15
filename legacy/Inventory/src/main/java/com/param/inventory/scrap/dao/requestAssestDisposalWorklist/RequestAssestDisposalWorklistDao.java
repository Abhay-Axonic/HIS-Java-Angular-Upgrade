package com.param.inventory.scrap.dao.requestAssestDisposalWorklist;

import java.util.List;

import javax.transaction.Transactional;

import com.param.common.constants.Status;
import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.AssetDisposalWorklistRequestDetail;
import com.param.entity.model.inventory.ScrapRequest;
import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.global.utility.Util;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

@Repository
public class RequestAssestDisposalWorklistDao extends AbstractDao<Integer, ScrapRequest>
		implements IRequestAssestDisposalWorklistDao {

	@Autowired
	private SessionService sessionService;

	/*
	 * @Override
	 * 
	 * @Transactional public List<ScrapResponse>
	 * getRequestAssestDisposalWorklistList(
	 * com.param.inventory.scrap.dto.scrapRequest.ScrapRequest request, Integer
	 * screenId) { String hql =
	 * "SELECT scrapRequest.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
	 * +
	 * "		store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType,"
	 * +
	 * "		user.userName AS requestedBy, approvalStatus.status AS approvalStatus, status.status AS status "
	 * + " FROM com.param.entity.model.inventory.ScrapRequest AS scrapRequest " +
	 * "		INNER JOIN scrapRequest.addedBy as user " +
	 * "		INNER JOIN scrapRequest.store AS store " +
	 * "		INNER JOIN scrapRequest.assetType AS assetType " +
	 * "		INNER JOIN scrapRequest.status AS status " +
	 * "		INNER JOIN scrapRequest.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
	 * +
	 * "	WHERE scrapRequest.isActive = true AND scrapRequest.isDeleted = false AND scrapRequest.unit.unitId = :unitId "
	 * + "		AND scrapRequest.addedDate BETWEEN :fromDate AND :toDate " +
	 * "     AND (:storeId = 0 OR store.storeId = :storeId) " +
	 * "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) " +
	 * "     AND (:statusId = 0 OR status.id = :statusId) " +
	 * "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
	 * +
	 * "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
	 * + "								INNER JOIN screenStatus.status as status) "
	 * + "ORDER BY scrapRequest.id DESC";
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("fromDate",
	 * Util.getDate(request.getFromDate())); query.setParameter("toDate",
	 * Util.getDate(request.getToDate())); query.setParameter("storeId",
	 * request.getStoreId() != null ? request.getStoreId() : 0);
	 * query.setParameter("assetTypeId", request.getAssetTypeId() != null ?
	 * request.getAssetTypeId() : 0); query.setParameter("statusId",
	 * request.getStatusId() != null ? request.getStatusId() : 0);
	 * query.setParameter("screenId", screenId); query.setParameter("unitId",
	 * sessionService.getUnitId());
	 * 
	 * query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));
	 * 
	 * return query.list(); }
	 */

	@Override
	@Transactional
	public List<ScrapResponse> getRequestAssestDisposalWorklistList(
			com.param.inventory.scrap.dto.scrapRequest.ScrapRequest request, Integer screenId) {
		String hql = "SELECT assetDisposalWorklistRequest.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "		store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType,"
				+ "		user.userName AS requestedBy, status.status AS status, witnessApprovalStatus.status AS approvalStatus  "
				+ " FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				+ "		INNER JOIN assetDisposalWorklistRequest.scrapRequest as scrapRequest "
				+ "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN scrapRequest.addedBy as user "
				+ "		INNER JOIN assetDisposalWorklistRequest.status AS status "
				+ "		INNER JOIN scrapRequest.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true " +
				" AND approvalStatus.isDeleted = false"
				+ "		INNER JOIN assetDisposalWorklistRequest.witnessApprovalStatus AS witnessApprovalStatus WITH witnessApprovalStatus.isActive = true " +
				" AND witnessApprovalStatus.isDeleted = false"

				+ "	WHERE assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false AND assetDisposalWorklistRequest.unit.unitId = :unitId "
				+ " 	AND approvalStatus.id =:approvalStatus "
				+ "		AND assetDisposalWorklistRequest.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "     AND (:scrapType = 0 OR scrapRequest.scrapType = :scrapType) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY assetDisposalWorklistRequest.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
		query.setParameter("scrapType",request.getScrapType());
		query.setParameter("approvalStatus", com.param.common.constants.Status.Approved.getStatus());


		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return query.list();
	}

	@Override
	@Transactional
	public AssetDisposalWorklistRequest saveRequestAssestDisposalWorklist(
			AssetDisposalWorklistRequest assetDisposalWorklistRequest) {
		getSession().save(assetDisposalWorklistRequest);
		return assetDisposalWorklistRequest;
	}

	@Override
	@Transactional
	public ScrapResponse getScrapRequest(Integer id) {
		String hql = "SELECT scrapRequest.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "   store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType, "
				+ "	  user.userName AS requestedBy, status.status AS status, status.id AS statusId, approvalStatus.status AS approvalStatus, "
				+ "   approvalStatus.id AS approvalStatusId "
				+ " FROM com.param.entity.model.inventory.ScrapRequest AS scrapRequest "
				+ "		INNER JOIN scrapRequest.addedBy as user " + "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN scrapRequest.status AS status "
				+ "		INNER JOIN scrapRequest.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE scrapRequest.id = :id AND scrapRequest.isActive = true AND scrapRequest.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return (ScrapResponse) query.uniqueResult();
	}

	@Override
	public ScrapResponse getRequestAssestDisposalWorklist(Integer id) {

		String hql = "SELECT assetDisposalWorklistRequest.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "   store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType, "
				+ "	  user.userName AS requestedBy, status.status AS status, status.id AS statusId, witness.userId AS witnessId, witness.userName AS witnessName "
				+ " FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				+ " 	LEFT JOIN assetDisposalWorklistRequest.witness AS witness "
				+ "		INNER JOIN assetDisposalWorklistRequest.scrapRequest as scrapRequest "
				+ "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN scrapRequest.addedBy as user "
				+ "		INNER JOIN assetDisposalWorklistRequest.status AS status "
				+ "	WHERE assetDisposalWorklistRequest.id = :id AND assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return (ScrapResponse) query.uniqueResult();
	}

	@Override
	public List<ItemResponse> getScrapRequestDetail(Integer id) {

		String hql = "SELECT scrapRequestDetails.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ "		generic.name AS genericName, uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		batch.batchNo AS batchNo, batch.lotNo AS lotNo, batch.cop AS cop, batch.expiryDate AS expiryDate, scrapRequestDetails.scrapQty AS scrapQuantity, "
				+ "	    scrapReason.id AS scrapReasonId, scrapReason.reason AS scrapReason "
				+ " FROM com.param.entity.model.inventory.ScrapRequest AS scrapRequest "
				+ " 	INNER JOIN scrapRequest.scrapRequestDetails AS scrapRequestDetails WITH scrapRequestDetails.isActive = true AND scrapRequestDetails.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.batch AS batch WITH batch.isActive = true and batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN scrapRequestDetails.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.scrapReason AS scrapReason WITH scrapReason.isActive = true AND scrapReason.isDeleted = false "
				+ "	WHERE scrapRequest.id = :id AND scrapRequest.isActive = true AND scrapRequest.isDeleted = false "
				+ " GROUP BY scrapRequestDetails.id, item.id, item.code, item.name, generic.name, uomType.id, uomType.type, uomUnit.id, uomUnit.units, "
				+ "		batch.batchNo, batch.lotNo, batch.cop, batch.expiryDate, scrapRequestDetails.scrapQty, scrapReason.id, scrapReason.reason ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));

		return query.list();
	}

	@Override
	public List<ItemResponse> getRequestAssestDisposalWorklistDetails(Integer id) {

		String hql = "SELECT assetDisposalWorklistRequestDetail.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ "		generic.name AS genericName, uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		batch.batchNo AS batchNo, batch.lotNo AS lotNo, batch.cop AS cop, batch.expiryDate AS expiryDate, scrapRequestDetails.acceptedQty AS scrapQuantity, "
				+ "	    scrapReason.reasonMasterId AS scrapReasonId, scrapReason.reasonDesc AS scrapReason "
				+ " FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				+ " 	INNER JOIN assetDisposalWorklistRequest.assetDisposalWorklistRequestDetails AS assetDisposalWorklistRequestDetail WITH assetDisposalWorklistRequestDetail.isActive = true AND assetDisposalWorklistRequestDetail.isDeleted = false "
				+ "		INNER JOIN assetDisposalWorklistRequestDetail.scrapRequestDetail AS scrapRequestDetails "
				+ "		INNER JOIN scrapRequestDetails.batch AS batch WITH batch.isActive = true and batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		LEFT JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		LEFT JOIN scrapRequestDetails.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		LEFT JOIN scrapRequestDetails.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		LEFT JOIN scrapRequestDetails.scrapReason AS scrapReason  "
				+ "	WHERE assetDisposalWorklistRequest.id = :id AND assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false "
				+ " GROUP BY assetDisposalWorklistRequestDetail.id, item.id, item.code, item.name, generic.name, uomType.id, uomType.type, uomUnit.id, uomUnit.units, "
				+ "		batch.batchNo, batch.lotNo, batch.cop, batch.expiryDate, scrapRequestDetails.acceptedQty"
				+ ", scrapReason.reasonMasterId, scrapReason.reasonDesc ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));

		return query.list();
	}

	@Override
	public ScrapRequest getScrapRequestById(Integer scrapRequestId) {

		String hql = "SELECT scrapRequest FROM com.param.entity.model.inventory.ScrapRequest AS scrapRequest WHERE scrapRequest.id = :id  AND scrapRequest.isActive = true AND scrapRequest.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", scrapRequestId);

		return (ScrapRequest) query.uniqueResult();
	}

	@Override
	public AssetDisposalWorklistRequest getRequestAssetDisposalWorklist(Integer id) {
		String hql = "SELECT assetDisposalWorklistRequest FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest WHERE assetDisposalWorklistRequest.id = :id  AND assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (AssetDisposalWorklistRequest) query.uniqueResult();
	}

	@Override
	public List<AssetDisposalWorklistRequestDetail> getRequestAssetDisposalWorklistDetail(Integer id) {

		String hql = "SELECT assetDisposalWorklistRequestDetail FROM com.param.entity.model.inventory.AssetDisposalWorklistRequestDetail AS assetDisposalWorklistRequestDetail \r\n"
				+ "	INNER JOIN assetDisposalWorklistRequestDetail.assetDisposalWorklistRequest AS assetDisposalWorklistRequest WITH assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false \r\n"
				+ "WHERE assetDisposalWorklistRequest.id = :id AND assetDisposalWorklistRequestDetail.isActive = true AND assetDisposalWorklistRequestDetail.isDeleted = false \r\n"
				+ "ORDER BY assetDisposalWorklistRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public WasteAssetDisposalWorklist saveWasteAssestDisposalWorklist(
			WasteAssetDisposalWorklist wasteAssetDisposalWorklist) {
		getSession().save(wasteAssetDisposalWorklist);
		return wasteAssetDisposalWorklist;
	}

	@Override
	public AssetDisposalWorklistRequest updateRequestAssestDisposalWorklist(
			AssetDisposalWorklistRequest assetDisposalWorklistRequest) {
		getSession().update(assetDisposalWorklistRequest);
		return assetDisposalWorklistRequest;
	}

}
