package com.param.global.dao.kit.items;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.global.dto.kit.Items.KitItemsDetailsListResponse;

@Repository
public class KitItemDao  extends AbstractDao<Integer, Item> implements IKitItemDao {

	@Autowired
	private SessionService sessionService;
	
	@Override
	public List<Item> productCategoryCount(List<Integer> idList) {
		String hql = "SELECT item " + "	FROM Item as item "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "	WHERE item.id in (:id) AND item.isDeleted = false AND item.isActive = true";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", idList);
		

		return query.list();
	}
	
	@Override
	public List<KitItemsDetailsListResponse> kitItemsDetails(List<Integer> idList, Integer storeId) {
		String sql = "SELECT item.id as itemId, item.code AS itemCode, item.name AS itemName, generic.id AS genericId, generic.name AS genericName, "
				+ "		item.is_Implant AS isImplant,manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, tax.id AS taxId, tax.name AS taxName, tax.tax_percentage AS taxPercent, "
				+ "			item.is_otc AS isOtc, productCategory.id AS productCategoryId, productCategory.category AS productCategory, assetCategory.id AS assetCategoryId, "
				//+"	unitOfMeasurementList.uom_type_id AS uomTypeId,unitOfMeasurementList.uom_unit_id AS uomUnitId,"
				+ "			assetCategory.category AS assetCategory, coalesce(stock.totalQty, 0) AS stockAvailable "
				+ " FROM public.m_item item "
			//	+ "	INNER JOIN public.m_unit_of_measurement ON item.unitOfMeasurementList=unitOfMeasurementList.uom_type_id AND (unitOfMeasurementList.is_deleted=false AND unitOfMeasurementList.is_active=true) "
				+ "	LEFT JOIN public.m_generic generic ON item.generic_id=generic.id AND (generic.is_deleted=false AND generic.is_active=true) "
				+ "	LEFT JOIN public.m_product_category productCategory ON generic.product_category_id=productCategory.id AND (productCategory.is_deleted=false AND productCategory.is_active=true) "
				+ "	LEFT JOIN public.m_asset_category assetCategory ON productCategory.asset_category_id=assetCategory.id AND (assetCategory.is_deleted=false AND assetCategory.is_active=true) "
				+ "	LEFT JOIN public.m_manufacturer manufacturer ON item.manufacturer_id=manufacturer.id AND (manufacturer.is_deleted=false AND manufacturer.is_active=true) "
				+ "	LEFT JOIN public.m_tax tax ON item.p_tax_id=tax.id AND (tax.is_deleted=false AND tax.is_active=true) "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ "					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) "
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE (item.id in (:id)) AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";

		Query query = getSession().createSQLQuery(sql).addScalar("itemId").addScalar("itemCode").addScalar("itemName")
				.addScalar("genericId").addScalar("genericName").addScalar("manufacturerId")
				.addScalar("manufacturerName").addScalar("taxId").addScalar("taxName").addScalar("taxPercent")
				.addScalar("isOtc").addScalar("productCategoryId").addScalar("productCategory")
				//.addScalar("uomTypeId").addScalar("uomUnitId")
				//.addScalar("genericId").addScalar("genericName")
				.addScalar("isImplant")
				.addScalar("assetCategoryId").addScalar("assetCategory")
				.addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameterList("id", idList);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(KitItemsDetailsListResponse.class));

		return query.list();
	}
	
	@Override
	public List<KitItemsDetailsListResponse> kitItemsDetailsFixedAsset(List<Integer> idList, Integer storeId) {
		String sql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, generic.id AS genericId, generic.name AS genericName,"
				+ "		manufacturer.id AS manufacturerId, item.is_Implant AS isImplant,manufacturer.name AS manufacturerName,tax.id AS taxId, tax.name AS taxName, tax.tax_percentage AS taxPercent, "
			//	+"	unitOfMeasurementList.uom_type_id AS uomTypeId,unitOfMeasurementList.uom_unit_id AS uomUnitId,"
				+ "     COALESCE(stock.totalQty, 0) AS stockAvailable, "
				+ "		item.is_otc AS isOtc,productCategory.id AS productCategoryId, productCategory.category AS productCategory, assetCategory.id AS assetCategoryId, assetCategory.category AS assetCategory "
				+ "	FROM public.m_item item"
			//	+ "	INNER JOIN public.m_unit_of_measurement ON item.unitOfMeasurementList=unitOfMeasurementList.uom_type_id AND (unitOfMeasurementList.is_deleted=false AND unitOfMeasurementList.is_active=true) "
				+ "	LEFT JOIN public.m_generic generic ON item.generic_id=generic.id AND (generic.is_deleted=false AND generic.is_active=true) "
				+ "	LEFT JOIN public.m_product_category AS productCategory ON item.product_category_id=productCategory.id AND (productCategory.is_deleted=false AND productCategory.is_active=true) "
				+ "	LEFT JOIN public.m_asset_category assetCategory ON productCategory.asset_category_id=assetCategory.id AND (assetCategory.is_deleted=false AND assetCategory.is_active=true) "
				+ "	LEFT JOIN public.m_manufacturer manufacturer ON item.manufacturer_id=manufacturer.id AND (manufacturer.is_deleted=false AND manufacturer.is_active=true) "
				+ "	LEFT JOIN public.m_tax tax ON item.p_tax_id=tax.id AND (tax.is_deleted=false AND tax.is_active=true) "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ "					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) "
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ "	WHERE item.id in (:id) AND item.is_deleted = false AND item.is_active = true "
				+ "	ORDER BY item.id ";

		Query query = getSession().createSQLQuery(sql).addScalar("itemId").addScalar("itemCode").addScalar("itemName")
				.addScalar("manufacturerId").addScalar("manufacturerName").addScalar("productCategoryId")
				.addScalar("isOtc").addScalar("productCategory").addScalar("assetCategoryId").addScalar("assetCategory")
				.addScalar("taxId").addScalar("taxName").addScalar("taxPercent")
		//		.addScalar("uomTypeId").addScalar("uomUnitId")
				.addScalar("genericId").addScalar("genericName")
				.addScalar("isImplant").addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameterList("id", idList);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(KitItemsDetailsListResponse.class));

		return query.list();
	}
	@Override
	public Long getLeaseQunatity(Integer storeId, Integer itemId,Integer unitId) {

		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id = :itemId AND purchaseRequestDetail.goodReceiptNoteDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}
	
}
