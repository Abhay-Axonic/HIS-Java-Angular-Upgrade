package com.param.inventory.store.dao.materialInward;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchAccessoriesMapper;
import com.param.entity.model.inventory.InwardsUomMapper;
import com.param.entity.model.inventory.MaterialInward;
import com.param.entity.model.inventory.MaterialInwardAccessories;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.materialInward.CreditNoteVendorWise;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;

@Repository("MaterialInwardDao")
public class MaterialInwardDao extends AbstractDao<Integer, MaterialInward> implements IMaterialInwardDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<MaterialInwardResponse> searchMaterialInwardList(MaterialInwardRequest materialInwardRequest,
			Integer screenId) {
		String hql = "SELECT materialInward.id AS id, materialInward.addedDate AS inwardDate, materialInward.inwordNo AS inwardNo, "
				+ "		store.storeId AS storeId, store.storeName AS store, vendor.id AS vendorId, vendor.name AS vendor, "
				+ "		assetType.id AS assetTypeId, assetType.type AS assetType, requestType.id AS replacementTypeId,"
				+ "		requestType.type AS replacementType, returnGPNo.retGpNo as returnGPNo ,"
				+ "		user.userName as addedBY, status.status AS status  " + " FROM MaterialInward AS materialInward "
				+ "		INNER JOIN materialInward.store AS store " + "		INNER JOIN materialInward.vendor AS vendor "
			//	+ "		INNER JOIN materialInward.requestingStore AS requestingStore "
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
				+ "		INNER JOIN screenStatus.status as status) " + "ORDER BY materialInward.id DESC";

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
				materialInwardRequest.getInwardNo() != null ? '%'+materialInwardRequest.getInwardNo().toLowerCase() + '%'
						: "");
		query.setParameter("statusId",
				materialInwardRequest.getStatusId() != null ? materialInwardRequest.getStatusId() : 0);
		query.setParameter("requestTypeId",
				materialInwardRequest.getRequestTypeId() != null ? materialInwardRequest.getRequestTypeId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",materialInwardRequest.getUnitId()!=null ? materialInwardRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(MaterialInwardResponse.class));

		return query.list();
	}

	@Override
	public MaterialInward saveMaterialInward(MaterialInward materialInward) {
		getSession().save(materialInward);
		return materialInward;
	}

	@Override
	public MaterialInward getMaterialInward(Integer id) {
		String hql = "SELECT distinct materialInward FROM com.param.entity.model.inventory.MaterialInward AS materialInward "
				+ "WHERE materialInward.id = :id  AND materialInward.isActive = true AND materialInward.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (MaterialInward) query.uniqueResult();
	}

	@Override
	public MaterialInward updateMaterialInward(MaterialInward materialInward) {
		getSession().update(materialInward);
		return materialInward;
	}

	@Override
	public List<ReturnableGatepass> searchReturnableGatepassList(MaterialInwardRequest request, Integer screenId) {

		String hql = "SELECT returnableGatepass FROM ReturnableGatepass AS returnableGatepass "
				+ "		INNER JOIN returnableGatepass.store AS store "
				+ "		INNER JOIN returnableGatepass.vendor AS vendor "
				+ "		INNER JOIN returnableGatepass.assetType AS assetType "
				+ "		INNER JOIN returnableGatepass.status AS status "
				+ "	WHERE returnableGatepass.isActive = true AND returnableGatepass.isDeleted = false AND returnableGatepass.unit.unitId = :unitId "
				+ " 	AND returnableGatepass.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " AND returnableGatepass.approvalStatus=5 "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) " + "ORDER BY returnableGatepass.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		// query.setResultTransformer(Transformers.aliasToBean(MaterialInwardResponse.class));

		return query.list();
	}

	@Override
	public List<ReturnableGatepass> searchReturnableGatepassList(Integer id, Integer screenId,Integer unitId) {

		String hql = "SELECT returnableGatepass FROM ReturnableGatepass AS returnableGatepass "
				+ "		INNER JOIN returnableGatepass.store AS store "
				+ "		INNER JOIN returnableGatepass.vendor AS vendor "
				+ "		INNER JOIN returnableGatepass.assetType AS assetType "
				+ "		INNER JOIN returnableGatepass.status AS status "
				+ "	WHERE returnableGatepass.id = :id AND returnableGatepass.isActive = true AND returnableGatepass.isDeleted = false AND returnableGatepass.unit.unitId = :unitId "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) " + "ORDER BY returnableGatepass.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		// query.setResultTransformer(Transformers.aliasToBean(MaterialInwardResponse.class));

		return query.list();
	}

	@Override
	public ReturnableGatepass searchReturnableGatepass(Integer id, Integer screenId) {

		String hql = "SELECT returnableGatepass FROM ReturnableGatepass AS returnableGatepass "
				+ "		INNER JOIN returnableGatepass.store AS store "
				+ "		INNER JOIN returnableGatepass.vendor AS vendor "
				+ "		INNER JOIN returnableGatepass.assetType AS assetType "
				+ "		INNER JOIN returnableGatepass.status AS status "
				+ "	WHERE returnableGatepass.id = :id AND returnableGatepass.isActive = true AND returnableGatepass.isDeleted = false AND returnableGatepass.unit.unitId = :unitId "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) " + "ORDER BY returnableGatepass.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ReturnableGatepass.class));
		return (ReturnableGatepass) query.uniqueResult();

		//return query.list();
	}

	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}
	
	@Override
	public BatchAccessoriesMapper saveBatchAccessoriesMapper(BatchAccessoriesMapper batchAccessoriesMapper)
	{
		getSession().save(batchAccessoriesMapper);
		return batchAccessoriesMapper;
	}

	@Override
	public List<InwardsUomMapper> getInwardUomMapperList(Integer id) {
		String hql = "SELECT inwardsUomMapper FROM InwardsUomMapper AS inwardsUomMapper "
				+ " INNER JOIN inwardsUomMapper.materialInwardDetail AS materialInwardDetail WITH materialInwardDetail.id = :id AND materialInwardDetail.isDeleted = false AND materialInwardDetail.isActive = true "
				+ "WHERE inwardsUomMapper.isDeleted = false AND inwardsUomMapper.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
}
	
	@Override
	public List<MaterialInwardAccessories> getInwardAccessoriesList(Integer id) {
		String hql = "SELECT materialInwardAccessories FROM MaterialInwardAccessories AS materialInwardAccessories "
				+ " INNER JOIN materialInwardAccessories.materialInwardDetail AS materialInwardDetail WITH materialInwardDetail.id = :id AND materialInwardDetail.isDeleted = false AND materialInwardDetail.isActive = true "
				+ "WHERE materialInwardAccessories.isDeleted = false AND materialInwardAccessories.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
}

	@Override
	public List<CreditNoteVendorWise> getReturnableGatepassVendorWiseSearch(Integer vendorid) {
		//String hql = "select mateInward.id as mateInwardId , mateInwardDetails.id as mateInwardDetailsId, mateInwardDetails.tax_amount as taxAmount, mateInwardDetails.tax_percentage as taxPercentage,mateInwardDetails.total_amount as totalAmount, mateInwardDetails.credit_note_amount as creditNoteAmount, mateInwardDetails.credit_tax_amount as creditTaxAmount , mateInwardDetails.credit_net_amount as creditNetAmount , grn.grn_number as grnNumber , grn.added_date as grnDate, mateInward.replacement_number as replaceNumber from inventory.t_material_inwards mateInward join inventory.t_material_inwards_detail mateInwardDetails on mateInward.id=mateInwardDetails.material_inwards_id join procurement.t_good_receipt_note_detail grndetails on  mateInwardDetails.returnable_gatepass_detail_id=grndetails.id join procurement.t_good_receipt_note grn on grndetails.grn_id=grn.id  where mateInward.vendor_id="+vendorid +" and mateInward.status_id in (2)" ;
		//String hql=" select mateInward.id as mateInwardId , mateInwardDetails.id as mateInwardDetailsId, mateInwardDetails.tax_amount as taxAmount, mateInwardDetails.tax_percentage as taxPercentage,mateInwardDetails.total_amount as totalAmount, mateInwardDetails.credit_note_amount as creditNoteAmount, mateInwardDetails.credit_tax_amount as creditTaxAmount , mateInwardDetails.credit_net_amount as creditNetAmount , grn.ret_gp_no as rgNumber , grn.added_date as rgDate, mateInward.replacement_number as replaceNumber , mateInwardDetails.available_amount as availablenetamount from inventory.t_material_inwards mateInward join inventory.t_material_inwards_detail mateInwardDetails on mateInward.id=mateInwardDetails.material_inwards_id join inventory.t_returnable_gatepass_detail grndetails on  mateInwardDetails.returnable_gatepass_detail_id=grndetails.id join inventory.t_returnable_gatepass grn on grndetails.returnable_gatepass_id=grn.id where mateInward.vendor_id="+vendorid+" and mateInward.status_id in (2) ";
		//String sql=" select distinct mateInward.id as mateInwardId , mateInward.credit_note_amount as creditNoteAmount, mateInward.credit_tax_amount as creditTaxAmount , mateInward.credit_net_amount as creditNetAmount , rgpass.ret_gp_no as rgNumber , rgpass.added_date as rgDate, mateInward.replacement_number as replaceNumber , mateInward.available_amount as availablenetamount, mateInward.inword_no as inwordno from inventory.t_material_inwards mateInward join inventory.t_returnable_gatepass rgpass on mateInward.returnable_gatepass_id =rgpass.id join inventory.t_returnable_gatepass_detail grndetails on  	grndetails.returnable_gatepass_id=rgpass.id where mateInward.vendor_id = "+vendorid+ "and mateInward.status_id in (2)" ;
		String sql =" select distinct mateInward.id as mateInwardId , mateInward.credit_note_amount as creditNoteAmount, mateInward.credit_tax_amount as creditTaxAmount , mateInward.credit_net_amount as creditNetAmount , mateInward.replacement_number as replaceNumber , mateInward.available_amount as availablenetamount, mateInward.inword_no as inwordno from inventory.t_material_inwards mateInward where mateInward.vendor_id =" +vendorid+ " and mateInward.available_amount > 0";
		
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(CreditNoteVendorWise.class));
	//	query.setParameter("vendorid", vendorid);

		return query.list();
	}

	@Override
	public List<CreditNoteVendorWise> getReturnableGatepassDetails(Integer mateinwardid) {

		//String hql = "select mateInward.id as mateInwardId , mateInwardDetails.id as mateInwardDetailsId, mateInwardDetails.tax_amount as taxAmount, mateInwardDetails.tax_percentage as taxPercentage,mateInwardDetails.total_amount as totalAmount, mateInwardDetails.credit_note_amount as creditNoteAmount, mateInwardDetails.credit_tax_amount as creditTaxAmount , mateInwardDetails.credit_net_amount as creditNetAmount , grn.grn_number as grnNumber , grn.added_date as grnDate, mateInward.replacement_number as replaceNumber from inventory.t_material_inwards mateInward join inventory.t_material_inwards_detail mateInwardDetails on mateInward.id=mateInwardDetails.material_inwards_id join procurement.t_good_receipt_note_detail grndetails on  mateInwardDetails.returnable_gatepass_detail_id=grndetails.id join procurement.t_good_receipt_note grn on grndetails.grn_id=grn.id  where mateInward.vendor_id="+vendorid +" and mateInward.status_id in (2)" ;
	     String sql=" select distinct mateInward.id as mateInwardId , grn.ret_gp_no as rgNumber , grn.added_date as rgDate from inventory.t_material_inwards mateInward join inventory.t_material_inwards_detail mateInwardDetails  on mateInward.id=mateInwardDetails.material_inwards_id join inventory.t_returnable_gatepass_detail grndetails on  mateInwardDetails.returnable_gatepass_detail_id=grndetails.id join inventory.t_returnable_gatepass grn on grndetails.returnable_gatepass_id=grn.id where mateInward.id="+mateinwardid ;
			Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(CreditNoteVendorWise.class));
	//	query.setParameter("vendorid", vendorid);

		return query.list();
	
	}

	@Override
	public ReturnableGatepass getReturnableGatepass(ReturnableGatepass returnableGatepassId) {

		String hql = "SELECT gatepass FROM ReturnableGatepass AS gatepass "
				+ "	WHERE gatepass.id = :id AND gatepass.isActive = true AND gatepass.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", returnableGatepassId.getId());

		return (ReturnableGatepass) query.uniqueResult();
	
	}

	@Override
	public Integer getApprovedQuantity(ReturnableGatepass returnableGatepass) {
		String sql ="select cast(sum(tmid.approved_quantity+tmid.reject_quantity) as int) from inventory.t_material_inwards_detail tmid"
				+ " where tmid.unit_id="+returnableGatepass.getUnitId()+" and tmid.returnable_gatepass_id="+returnableGatepass.getId()+" ";
		Query query = getSession().createSQLQuery(sql);
		return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getNumberOfCreditDays(Integer vendorid) {
	     String sql=" select cast(credit_days as int) from public.m_vendor where id="+vendorid ;
			Query query = getSession().createSQLQuery(sql);
		return (Integer) query.uniqueResult();
	}

}
