package com.param.inventory.store.dao.MaterialInwardApproval;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.MaterialInward;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;

@Repository
public class MaterialInwardApprovalDao extends AbstractDao<Integer, MaterialInward>
		implements IMaterialInwardApprovalDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<MaterialInwardResponse> searchMaterialInwardApprovalList(MaterialInwardRequest materialInwardRequest,
			Integer screenId) {
		String hql = "SELECT materialInward.id AS id, materialInward.addedDate AS inwardDate, materialInward.inwordNo AS inwardNo, "
				+ "		store.storeId AS storeId, store.storeName AS store, vendor.id AS vendorId, vendor.name AS vendor, "
				+ "		assetType.id AS assetTypeId, assetType.type AS assetType, requestType.id AS replacementTypeId,"
				+ "		requestType.type AS replacementType, returnGPNo.retGpNo as returnGPNo ,"
				+ "		user.userName as addedBY, status.status AS status  "
				+ " 	FROM MaterialInward AS materialInward "
				+ "		INNER JOIN materialInward.store AS store " + "		INNER JOIN materialInward.vendor AS vendor "
				//+ "		INNER JOIN materialInward.requestingStore AS requestingStore "
				+ "		INNER JOIN materialInward.assetType AS assetType "
				+ "		INNER JOIN materialInward.requestType AS requestType "
				+ "		INNER JOIN materialInward.addedBy as user " + "		INNER JOIN materialInward.status AS status "
				+ "		INNER JOIN materialInward.returnableGatepass AS returnGPNo "
				+ "	WHERE materialInward.isActive = true AND materialInward.isDeleted = false AND materialInward.unit.unitId = :unitId "
				+ " 	AND materialInward.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:storeId = 0 OR store.storeId = :storeId) "
				//+ "     AND (:requestingStoreId = 0 OR requestingStore.storeId = :requestingStoreId) "
				+ "     AND (:inwordNo = '' OR lower(materialInward.inwordNo) LIKE :inwordNo) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "		AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:requestTypeId = 0 OR  requestType.id = :requestTypeId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) "
				+ "ORDER BY materialInward.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(materialInwardRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(materialInwardRequest.getToDate()));
		query.setParameter("storeId",
				materialInwardRequest.getStoreId() != null ? materialInwardRequest.getStoreId() : 0);
		//query.setParameter("requestingStoreId",materialInwardRequest.getRequestingStoreId() != null ? materialInwardRequest.getRequestingStoreId() : 0);
	
		query.setParameter("vendorId",
				materialInwardRequest.getVendorId() != null ? materialInwardRequest.getVendorId() : 0);
		query.setParameter("assetTypeId",
				materialInwardRequest.getAssetTypeId() != null ? materialInwardRequest.getAssetTypeId() : 0);
		query.setParameter("inwordNo",
				materialInwardRequest.getInwardNo() != null ? materialInwardRequest.getInwardNo().toLowerCase() + '%'
						: "");
		query.setParameter("statusId",
				materialInwardRequest.getStatusId() != null ? materialInwardRequest.getStatusId() : 0);
		query.setParameter("requestTypeId",
				materialInwardRequest.getRequestTypeId() != null ? materialInwardRequest.getRequestTypeId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", materialInwardRequest.getUnitId()!=null ? materialInwardRequest.getUnitId() : sessionService.getUnitId());
	/*	query.setParameterList("approvalStatusId",
				new ArrayList<Integer>(Arrays.asList(com.param.common.constants.Status.Approved.getStatus(),
						com.param.common.constants.Status.PartiallyApproved.getStatus())));*/
		query.setResultTransformer(Transformers.aliasToBean(MaterialInwardResponse.class));

		return query.list();
	}

	@Override
	public MaterialInward getMaterialInwardApproval(Integer id) {
		String hql = "SELECT materialInward FROM com.param.entity.model.inventory.MaterialInward AS materialInward "
				+ "WHERE materialInward.id = :id  AND materialInward.isActive = true AND materialInward.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (MaterialInward) query.uniqueResult();
	}

}
