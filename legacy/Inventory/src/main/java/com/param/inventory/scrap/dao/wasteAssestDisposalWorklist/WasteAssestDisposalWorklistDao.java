package com.param.inventory.scrap.dao.wasteAssestDisposalWorklist;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.global.utility.Util;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

@Repository
public class WasteAssestDisposalWorklistDao extends AbstractDao<Integer, AssetDisposalWorklistRequest>
		implements IWasteAssestDisposalWorklistDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ScrapResponse> getWasteAssestDisposalWorklistList(ScrapRequest request, Integer screenId) {

		String hql = "SELECT assetDisposalWorklistRequest.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "		store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType,"
				+ "		user.userName AS requestedBy, status.status AS status, witness.userId AS witnessId, witness.userName AS witnessName, witnessApprovalStatus.status AS approvalStatus  "
				+ " FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				//+ "     INNER JOIN wasteAssetDisposalWorklist.assetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				+ "		INNER JOIN assetDisposalWorklistRequest.scrapRequest AS scrapRequest "
				+ " 	LEFT JOIN assetDisposalWorklistRequest.witness AS witness "
				+ "		INNER JOIN scrapRequest.addedBy AS user " + "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN assetDisposalWorklistRequest.status AS status "
				+ "		INNER JOIN scrapRequest.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true " +
				"       AND approvalStatus.isDeleted = false"
				+ "     INNER JOIN assetDisposalWorklistRequest.witnessApprovalStatus AS witnessApprovalStatus WITH witnessApprovalStatus.isActive = true " +
				"       AND witnessApprovalStatus.isDeleted = false"
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
		query.setParameter("unitId",request.getUnitId()!=null? request.getUnitId(): sessionService.getUnitId());
		query.setParameter("scrapType",request.getScrapType());
		query.setParameter("approvalStatus", com.param.common.constants.Status.Approved.getStatus());

		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return query.list();

	}

	@Override
	public WasteAssetDisposalWorklist saveWasteAssestDisposalWorklist(
			WasteAssetDisposalWorklist wasteAssetDisposalWorklist) {
		getSession().save(wasteAssetDisposalWorklist);
		return wasteAssetDisposalWorklist;
	}

	@Override
	public ScrapResponse getWasteAssestDisposalWorklist(Integer id) {

		String hql = "SELECT assetDisposalWorklistRequest.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "   store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType, "
				+ "	  user.userName AS requestedBy, status.status AS status, status.id AS statusId, witness.userId AS witnessId,"
				+ "  witness.userName AS witnessName,  approvalStatus.status AS approvalStatus  "
				+ " FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				//+ "		INNER JOIN wasteAssetDisposalWorklist.assetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				+ " 	INNER JOIN assetDisposalWorklistRequest.scrapRequest as scrapRequest "
				+ " 	LEFT JOIN assetDisposalWorklistRequest.witness AS witness "
				+ "		INNER JOIN scrapRequest.addedBy as user " 
				+ "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN assetDisposalWorklistRequest.status AS status "
				+ "		INNER JOIN assetDisposalWorklistRequest.witnessApprovalStatus AS approvalStatus WITH approvalStatus.isActive = true "
				+ "	WHERE assetDisposalWorklistRequest.id = :id AND assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return (ScrapResponse) query.uniqueResult();
	}

	@Override
	public List<ItemResponse> getWasteAssestDisposalWorklistDetail(Integer id) {

		String hql = "SELECT assetDisposalWorklistRequestDetails.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ "		generic.name AS genericName, uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		batch.batchNo AS batchNo, batch.lotNo AS lotNo, batch.cop AS cop, batch.expiryDate AS expiryDate, scrapRequestDetails.scrapQty AS scrapQuantity, "
				+ "	    scrapReason.id AS scrapReasonId, scrapReason.reasonDesc AS scrapReason , wasteAssetDisposalWorklistDetails.disposedQty AS disposeQuantity, scrapRequestDetails.acceptedQty AS acceptedQuantity " //, wasteAssetDisposalWorklistDetails.disposedQty AS disposeQuantity
				+ " FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest "
				//+ "		INNER JOIN assetDisposalWorklistRequest.wasteAssetDisposalWorklistDetails AS wasteAssetDisposalWorklistDetails "
				+ "		LEFT JOIN assetDisposalWorklistRequest.wasteAssetDisposalWorklists AS wasteAssetDisposalWorklists "
				//+ "		LEFT JOIN wasteAssetDisposalWorklists.wasteAssetDisposalWorklistDetails AS wasteAssetDisposalWorklistDetails " //commented for resolved HIS-10575
				+ " 	INNER JOIN assetDisposalWorklistRequest.assetDisposalWorklistRequestDetails AS assetDisposalWorklistRequestDetails WITH assetDisposalWorklistRequestDetails.isActive = true AND assetDisposalWorklistRequestDetails.isDeleted = false "
				/* start change code for resolved HIS-10575 After save consumable disposal worklist click on details medications showing two times on consumable disposal worklist screen */
				+ "		LEFT JOIN assetDisposalWorklistRequestDetails.wasteAssetDisposalWorklistDetails AS wasteAssetDisposalWorklistDetails "
				/* end change code for resolved HIS-10575 After save consumable disposal worklist click on details medications showing two times on consumable disposal worklist screen */
				+ " 	INNER JOIN assetDisposalWorklistRequestDetails.scrapRequestDetail AS scrapRequestDetails WITH scrapRequestDetails.isActive = true AND scrapRequestDetails.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.batch AS batch WITH batch.isActive = true and batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN scrapRequestDetails.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.scrapReason AS scrapReason  "
				+ "	WHERE assetDisposalWorklistRequest.id = :id AND assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false "
				+ "  GROUP BY assetDisposalWorklistRequestDetails.id, item.id, item.code, item.name, generic.name, uomType.id, uomType.type, uomUnit.id, uomUnit.units, "
				+ "		batch.batchNo, batch.lotNo, batch.cop, batch.expiryDate, scrapRequestDetails.scrapQty, scrapReason.id, scrapReason, wasteAssetDisposalWorklistDetails.disposedQty, scrapRequestDetails.acceptedQty " +
				" ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));

		return query.list();
	}
	
	/*
	 * @Override public List<ItemResponse>
	 * getWasteAssestDisposalWorklistDetail(Integer id) {
	 * 
	 * String hql =
	 * "SELECT assetDisposalWorklistRequestDetails.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
	 * +
	 * "		generic.name AS genericName, uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
	 * +
	 * "		batch.batchNo AS batchNo, batch.lotNo AS lotNo, batch.cop AS cop, batch.expiryDate AS expiryDate, scrapRequestDetails.scrapQty AS scrapQuantity, "
	 * + "	    scrapReason.id AS scrapReasonId, scrapReason.reason AS scrapReason "
	 * +
	 * " FROM com.param.entity.model.inventory.AssetDisposalWorklistRequest AS assetDisposalWorklistRequest "
	 * +
	 * " 	INNER JOIN assetDisposalWorklistRequest.assetDisposalWorklistRequestDetails AS assetDisposalWorklistRequestDetails WITH assetDisposalWorklistRequestDetails.isActive = true AND assetDisposalWorklistRequestDetails.isDeleted = false "
	 * +
	 * " 	INNER JOIN assetDisposalWorklistRequestDetails.scrapRequestDetail AS scrapRequestDetails WITH scrapRequestDetails.isActive = true AND scrapRequestDetails.isDeleted = false "
	 * +
	 * "		INNER JOIN scrapRequestDetails.batch AS batch WITH batch.isActive = true and batch.isDeleted = false "
	 * +
	 * "		INNER JOIN batch.item AS item WITH item.isActive = true and item.isDeleted = false "
	 * +
	 * "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
	 * +
	 * "		INNER JOIN scrapRequestDetails.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
	 * +
	 * "		INNER JOIN scrapRequestDetails.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
	 * +
	 * "		INNER JOIN scrapRequestDetails.scrapReason AS scrapReason WITH scrapReason.isActive = true AND scrapReason.isDeleted = false "
	 * +
	 * "	WHERE assetDisposalWorklistRequest.id = :id AND assetDisposalWorklistRequest.isActive = true AND assetDisposalWorklistRequest.isDeleted = false "
	 * +
	 * " GROUP BY assetDisposalWorklistRequestDetails.id, item.id, item.code, item.name, generic.name, uomType.id, uomType.type, uomUnit.id, uomUnit.units, "
	 * +
	 * "		batch.batchNo, batch.lotNo, batch.cop, batch.expiryDate, scrapRequestDetails.scrapQty, scrapReason.id, scrapReason.reason "
	 * ;
	 * 
	 * Query query = getSession().createQuery(hql); query.setParameter("id", id);
	 * query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));
	 * 
	 * return query.list(); }
	 */

	@Override
	public WasteAssetDisposalWorklist getWasteAssestsDisposalWorklist(Integer id) {
		String hql = "SELECT wasteAssetDisposalWorklist FROM com.param.entity.model.inventory.WasteAssetDisposalWorklist AS wasteAssetDisposalWorklist WHERE wasteAssetDisposalWorklist.id = :id  AND wasteAssetDisposalWorklist.isActive = true AND wasteAssetDisposalWorklist.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WasteAssetDisposalWorklist) query.uniqueResult();
	}

	@Override
	public WasteAssetDisposalWorklist updateWasteAssestDisposalWorklist(
			WasteAssetDisposalWorklist wasteAssetDisposalWorklist) {
		getSession().update(wasteAssetDisposalWorklist);
		return wasteAssetDisposalWorklist;
	}

}
