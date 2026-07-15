package com.param.inventory.scrap.dao.witnessApproval;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.WasteAssetDisposalWorklist;
import com.param.entity.model.inventory.WasteAssetDisposalWorklistDetail;
import com.param.global.utility.Util;
import com.param.inventory.scrap.dto.scrapRequest.ItemResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

@Repository
public class WitnessApprovalDao extends AbstractDao<Integer, WasteAssetDisposalWorklist>
		implements IWitnessApprovalDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ScrapResponse> getWitnessApprovalList(ScrapRequest request, Integer screenId) {

		String hql = "SELECT wasteAssetDisposalWorklist.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "		store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType,"
				+ "		user.userName AS requestedBy, status.status AS status, witness.userId AS witnessId, witness.userName AS witnessName "
				+ " FROM com.param.entity.model.inventory.WasteAssetDisposalWorklist AS wasteAssetDisposalWorklist "
				+ " 	INNER JOIN wasteAssetDisposalWorklist.assetDisposalWorklistRequest AS assetDisposalWorklistRequest"
				+ "		INNER JOIN assetDisposalWorklistRequest.scrapRequest AS scrapRequest "
				+ " 	INNER JOIN assetDisposalWorklistRequest.witness AS witness "
				+ "		INNER JOIN scrapRequest.addedBy AS user " 
				+ "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN wasteAssetDisposalWorklist.status AS status "
				+ "	WHERE wasteAssetDisposalWorklist.isActive = true AND wasteAssetDisposalWorklist.isDeleted = false AND wasteAssetDisposalWorklist.unit.unitId = :unitId "
				+ "		AND wasteAssetDisposalWorklist.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "     AND (:scrapType = 0 OR scrapRequest.scrapType = :scrapType) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY wasteAssetDisposalWorklist.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("scrapType",request.getScrapType());
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return query.list();
	}

	@Override
	public WasteAssetDisposalWorklist getWasteAssestDisposalWorklist(Integer id) {

		String hql = "SELECT wasteAssetDisposalWorklist FROM com.param.entity.model.inventory.WasteAssetDisposalWorklist AS wasteAssetDisposalWorklist "
				+ " WHERE wasteAssetDisposalWorklist.id =:id  AND wasteAssetDisposalWorklist.isActive = true AND wasteAssetDisposalWorklist.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WasteAssetDisposalWorklist) query.uniqueResult();
	}

	@Override
	public List<WasteAssetDisposalWorklistDetail> getWasteAssestDisposalWorklistDetail(
			Integer wasteAssetDisposalWorklistId) {
		String hql = "SELECT wasteAssetDisposalWorklistDetail FROM com.param.entity.model.inventory.WasteAssetDisposalWorklistDetail AS wasteAssetDisposalWorklistDetail \r\n"
				+ "		INNER JOIN wasteAssetDisposalWorklistDetail.wasteAssetDisposalWorklist AS wasteAssetDisposalWorklist WITH wasteAssetDisposalWorklist.isActive = true AND wasteAssetDisposalWorklist.isDeleted = false \r\n"
				+ "WHERE wasteAssetDisposalWorklist.id = :wasteAssetDisposalWorklistId AND wasteAssetDisposalWorklistDetail.isActive = true AND wasteAssetDisposalWorklistDetail.isDeleted = false \r\n"
				+ "ORDER BY wasteAssetDisposalWorklistDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("wasteAssetDisposalWorklistId", wasteAssetDisposalWorklistId);

		return query.list();
	}

	@Override
	public WasteAssetDisposalWorklist saveWitnessApproval(WasteAssetDisposalWorklist wasteAssetDisposalWorklist) {
		getSession().update(wasteAssetDisposalWorklist);
		return wasteAssetDisposalWorklist;

	}

	@Override
	public ScrapResponse getWitnessApproval(Integer id) {

		String hql = "SELECT wasteAssetDisposalWorklist.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "   store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType, "
				+ "	  user.userName AS requestedBy, status.status AS status, status.id AS statusId, witness.userId AS witnessId, witness.userName AS witnessName "
				+ " FROM com.param.entity.model.inventory.WasteAssetDisposalWorklist AS wasteAssetDisposalWorklist "
				+ " 	INNER JOIN wasteAssetDisposalWorklist.assetDisposalWorklistRequest AS assetDisposalWorklistRequest "	
				+ " 	INNER JOIN assetDisposalWorklistRequest.scrapRequest as scrapRequest "
				+ " 	INNER JOIN assetDisposalWorklistRequest.witness AS witness "
				+ "		INNER JOIN scrapRequest.addedBy as user " 
				+ "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN wasteAssetDisposalWorklist.status AS status "
				+ "	WHERE wasteAssetDisposalWorklist.id = :id AND wasteAssetDisposalWorklist.isActive = true AND wasteAssetDisposalWorklist.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return (ScrapResponse) query.uniqueResult();

	}

	@Override
	public List<ItemResponse> getWitnessApprovalDetail(Integer id) {

		String hql = "SELECT wasteAssetDisposalWorklistDetails.id AS id, item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ "		generic.name AS genericName, uomType.id AS uomTypeId, uomType.type AS uomType, uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, "
				+ "		batch.batchNo AS batchNo, batch.lotNo AS lotNo, batch.cop AS cop, batch.expiryDate AS expiryDate, scrapRequestDetails.scrapQty AS scrapQuantity, "
				+ "	    scrapReason.reasonMasterId AS scrapReasonId, scrapReason.reasonDesc AS scrapReason, wasteAssetDisposalWorklistDetails.disposedQty AS disposeQuantity,wasteAssetDisposalWorklistDetails.approvedDisposedQty AS approvedDisposedQty, wasteAssetDisposalWorklistDetails.rejectQuantity as rejectQuantity, " +
				" batch.id AS batchId "
				+ " FROM com.param.entity.model.inventory.WasteAssetDisposalWorklist AS wasteAssetDisposalWorklist "
				+ " 	INNER JOIN wasteAssetDisposalWorklist.wasteAssetDisposalWorklistDetails AS wasteAssetDisposalWorklistDetails WITH wasteAssetDisposalWorklistDetails.isActive = true AND wasteAssetDisposalWorklistDetails.isDeleted = false "
				+ " 	INNER JOIN wasteAssetDisposalWorklistDetails.assetDisposalWorklistRequestDetail AS assetDisposalWorklistRequestDetails WITH assetDisposalWorklistRequestDetails.isActive = true AND assetDisposalWorklistRequestDetails.isDeleted = false "
				+ " 	INNER JOIN assetDisposalWorklistRequestDetails.scrapRequestDetail AS scrapRequestDetails WITH scrapRequestDetails.isActive = true AND scrapRequestDetails.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.batch AS batch WITH batch.isActive = true and batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		LEFT JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		LEFT JOIN scrapRequestDetails.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		LEFT JOIN scrapRequestDetails.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		INNER JOIN scrapRequestDetails.scrapReason AS scrapReason "
				+ "	WHERE wasteAssetDisposalWorklist.id = :id AND wasteAssetDisposalWorklist.isActive = true AND wasteAssetDisposalWorklist.isDeleted = false "
				+ " GROUP BY wasteAssetDisposalWorklistDetails.id, item.id, item.code, item.name, generic.name, uomType.id, uomType.type, uomUnit.id, uomUnit.units, "
				+ "		batch.batchNo, batch.lotNo, batch.cop, batch.expiryDate, scrapRequestDetails.scrapQty, scrapReason.reasonMasterId, scrapReason.reasonDesc, wasteAssetDisposalWorklistDetails.disposedQty," +
				" wasteAssetDisposalWorklistDetails.approvedDisposedQty,wasteAssetDisposalWorklistDetails.rejectQuantity, batch.id"
				+ " ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));

		return query.list();

	}

}
