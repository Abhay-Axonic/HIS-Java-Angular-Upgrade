package com.param.global.dao.items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.RequestType;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Rack;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.items.BatchAccessoriesMapperDto;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.BatchSearchResponse;
import com.param.global.dto.items.ItemListCompareMinResponse;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.ItemsResponse;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
public class ItemsDaoImpl extends AbstractDao<Integer, Item> implements IItemsDao {
	final static Logger logger = Logger.getLogger(ItemsDaoImpl.class);


	@Autowired
	private SessionService sessionService;

	@Override
	public List<ItemsSearchResponse> itemsSearch(Integer unitId ,boolean isOtc, String search, Integer storeId) {

		Date current = TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		/*String isOtcFlag="";
		if(isOtc == true)
		{
			isOtcFlag="true";	
		}
		else
		{
			isOtcFlag="true , false ";		
		}*/
		
		String hql = "SELECT item.id AS id, item.formulationType.id AS formulationTypeId, item.code || ' | ' || item.name AS details, item.isThreeUOMApplicable as isThreeUOMApplicable, item.isLasa as isLasa FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true  "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
			/*	+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				+ "		INNER JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "*/
				//+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId AND item.isOtc in ( "+isOtcFlag+" )"
				+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.isConsumable = true "
				//+ "AND item.isOtc in ( "+isOtcFlag+" )"
				+ "		AND (upper(item.code) like upper(:search) OR upper(item.name) like upper(:search)) "
				//+ "		AND store.storeId = :storeId "
		/*		+ "AND item.id NOT IN (SELECT itemRestriction.item.id "
				+ "							FROM StoreItemRestriction AS itemRestriction "
				+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							WHERE store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "*/
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  AND batch.unit.unitId = :unitId "
				// + " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR
				// batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "

				+ "        AND  storeStock.store.storeId = :storeId AND storeStock.availableQty >= 0 AND storeStock.availableQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth OR batch.expiryDate is null) "

				+ "		GROUP BY item.id" + "     ORDER BY item.id";

		/*
		 * String hql =
		 * "SELECT item.id AS id, item.code || ' | ' || item.name AS details " +
		 * "	FROM Item AS item " +
		 * "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
		 * +
		 * "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
		 * +
		 * "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
		 * +
		 * "		INNER JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
		 * +
		 * "	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId AND item.isOtc = :isOtc "
		 * +
		 * "		AND (upper(item.code) like upper(:search) OR upper(item.name) like upper(:search)) "
		 * + "		AND store.storeId = :storeId " +
		 * "		AND item.id NOT IN (SELECT itemRestriction.item.id " +
		 * "							FROM StoreItemRestriction AS itemRestriction " +
		 * "								INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
		 * +
		 * "							WHERE store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
		 * +
		 * "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
		 * + "	ORDER BY item.id";
		 */

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId);
		//query.setParameter("isOtc", isOtc);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("search", '%'+search + '%');
		query.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));

		return query.list();
	}

	@Override
	public List<BatchSearchResponse> batchSearch(boolean isOtc, String search) {

		String hql = "SELECT item.id AS id, batch.id AS batchId, batch.batchNo || ' | ' || TO_CHAR(batch.expiryDate, 'Mon-yyyy') AS details "
				+ "	FROM Batch as batch "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId   "
				+ "		AND (upper(batch.batchNo) like upper(:search)) "
				+ "		AND storeProduct.store.storeId = :storeId "
				+ "		AND storeProduct.store.isDeleted = false AND storeProduct.store.isActive = true "
				+ "		AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "							WHERE itemRestriction.store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
				+ "	ORDER BY item.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", sessionService.getStoreId());
		query.setParameter("unitId", sessionService.getUnitId());
		//query.setParameter("isOtc", isOtc);
		query.setParameter("search", search + '%');
		query.setResultTransformer(Transformers.aliasToBean(BatchSearchResponse.class));

		return query.list();
	}

	@Override
	public List<BatchSearchResponse> expiryAndAvailableQuantityWisebatches(boolean isOtc, String search, Integer unitId,Integer storeId) {
		Date current = TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
 
		String hql = "SELECT  distinct item.id AS id, batch.id AS batchId, batch.batchNo || ' | ' || TO_CHAR(batch.expiryDate, 'Mon-yyyy') AS details, " 
				+ " batch.markup AS markup, batch.rate AS rate " + "	FROM Batch as batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true and item.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId AND batch.isQuarantine = false    "
				+ " 	AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND ( batch.expiryDate > :greaterThanCurrentMonth OR batch.expiryDate is null)  "
				+ "		AND (upper(batch.batchNo) like upper(:search)) "
				+ "		AND storeProduct.store.storeId = :storeId "
				+ "		AND storeProduct.store.isDeleted = false AND storeProduct.store.isActive = true "
				+ "		AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "							WHERE itemRestriction.store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
				+ "	ORDER BY item.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId !=null ? storeId : sessionService.getStoreId());
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		//query.setParameter("isOtc", isOtc);
		//query.setParameter("storeId", unitId);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("search", search + '%');

		query.setResultTransformer(Transformers.aliasToBean(BatchSearchResponse.class));

		return query.list();
	}

	@Override
	public List<ItemListResponse> itemsList(ItemsRequest itemsRequest) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true  and store_item_mapper.unit_id=:unitId " + "	) "
				//+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = false)  "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"

				+ " union"

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true and store_item_mapper.unit_id=:unitId " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+ " and (item0.is_consignment = false)  "
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))";

		Query query = getSession().createSQLQuery(hql);
		//query.setParameter("assetTypeId", Constants.getFixedAssetId());
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId());
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();
	}

	@Override
	public List<ItemListResponse> itemsListForStoreIndent(ItemsRequest itemsRequest) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				
				+ " inner  join "  
				+ "	(select from_store_item_mapper.item_id from public.m_store_item_mapper from_store_item_mapper "
				+ "	where from_store_item_mapper.store_id = :fromStoreId and from_store_item_mapper.unit_id = :unitId "  
				+ "	group by item_id) from_store on from_store.item_id = item0.id"
				
				+ " inner  join "
				+ "	(select to_store_item_mapper.item_id from public.m_store_item_mapper to_store_item_mapper "  
				+ "	where to_store_item_mapper.store_id = :toStoreId and to_store_item_mapper.unit_id = :unitId "  
				+ "	group by item_id) to_store on to_store.item_id = item0.id "
				
				+ " left  join "  
				+ "	(select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper " 
				+ "	where store_pc_mapper.store_id = :fromStoreId group by product_category_id) "
				+ "	prodcat_1 on prodcat_1.product_category_id = productcat2_.id "
				
				+ " left  join "
				+ "	(select store_item_restriction.item_id from public.m_store_item_restriction store_item_restriction "
				+ "	where store_item_restriction.store_id = :fromStoreId and store_item_restriction.unit_id = :unitId " 
				+ "	group by item_id) store_item_rest_1 on store_item_rest_1.item_id <> item0.id "
				
				+ " left  join " 
				+ "	(select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper "  
				+ "	where store_pc_mapper.store_id = :toStoreId group by product_category_id) " 
				+ "	prodcat_2 on prodcat_2.product_category_id = productcat2_.id "
				
				+ " left  join "  
				+ "	(select store_item_restriction.item_id "  
				+ "	from public.m_store_item_restriction store_item_restriction "  
				+ "	where store_item_restriction.store_id = :toStoreId and store_item_restriction.unit_id = :unitId " 
				+ "	group by item_id) store_item_rest_2 on store_item_rest_2.item_id <> item0.id"
				
				+ " where item0.is_deleted=false  " + " and item0.is_active=true   "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"
				+ " and (item0.is_consignment = false)  " 
				
//				+ " and (item0.id in (select from_store_item_mapper.item_id from public.m_store_item_mapper from_store_item_mapper where from_store_item_mapper.store_id=:fromStoreId   and from_store_item_mapper.unit_id=:unitId) " + 
//						"	and item0.id in (select to_store_item_mapper.item_id from public.m_store_item_mapper to_store_item_mapper where to_store_item_mapper.store_id=:toStoreId  and to_store_item_mapper.unit_id=:unitId))" 
//				+ " and ( productcat2_.id in (select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper where store_pc_mapper.store_id = :fromStoreId) "
//				+ "											AND item0.id NOT IN ("
//				+ "												select store_item_restriction.item_id from public.m_store_item_restriction  store_item_restriction where store_item_restriction.store_id = :fromStoreId and store_item_restriction.unit_id=:unitId"
//				+ "												))"
//				+ "AND ( productcat2_.id in (select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper where store_pc_mapper.store_id = :toStoreId) "
//				+ "									AND item0.id NOT IN ("
//				+ "									select store_item_restriction.item_id from public.m_store_item_restriction  store_item_restriction where store_item_restriction.store_id = :toStoreId  and store_item_restriction.unit_id=:unitId"
//				+ "									)) "

				+ " union  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				
				+ " inner  join " 
				+ "	(select from_store_item_mapper.item_id from public.m_store_item_mapper from_store_item_mapper "  
				+ "	where from_store_item_mapper.store_id = :fromStoreId and from_store_item_mapper.unit_id = :unitId "  
				+ "	group by item_id) from_store on from_store.item_id = item0.id"
				
				+ " inner  join "  
				+ "	(select to_store_item_mapper.item_id from public.m_store_item_mapper to_store_item_mapper " 
				+ "	where to_store_item_mapper.store_id = :toStoreId and to_store_item_mapper.unit_id = :unitId " 
				+ "	group by item_id) to_store on to_store.item_id = item0.id "
				
				+ " left  join "  
				+ "	(select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper "  
				+ "	where store_pc_mapper.store_id = :fromStoreId group by product_category_id) "  
				+ "	prodcat_1 on prodcat_1.product_category_id = productcat1.id "
				
				+ " left  join "  
				+ "	(select store_item_restriction.item_id from public.m_store_item_restriction store_item_restriction "  
				+ "	where store_item_restriction.store_id = :fromStoreId and store_item_restriction.unit_id = :unitId "  
				+ "	group by item_id) store_item_rest_1 on store_item_rest_1.item_id <> item0.id "
				
				
				+ " left  join " 
				+ "	(select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper "  
				+ "	where store_pc_mapper.store_id = :toStoreId group by product_category_id) " 
				+ "	prodcat_2 on prodcat_2.product_category_id = productcat1.id "
				
				+ " left  join "  
				+ "	(select store_item_restriction.item_id " 
				+ "	from public.m_store_item_restriction store_item_restriction " 
				+ "	where store_item_restriction.store_id = :toStoreId and store_item_restriction.unit_id = :unitId "
				+ "	group by item_id) store_item_rest_2 on store_item_rest_2.item_id <> item0.id "
				
				+ " where item0.is_deleted=false  " + " and item0.is_active=true   "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"
//				+ "and ((:fromStoreId = 0 or item0.id in (select from_store_item_mapper.item_id from public.m_store_item_mapper from_store_item_mapper where from_store_item_mapper.store_id=:fromStoreId and from_store_item_mapper.unit_id=:unitId) ) " + 
//				"				and (:toStoreId = 0 or item0.id in (select to_store_item_mapper.item_id from public.m_store_item_mapper to_store_item_mapper where to_store_item_mapper.store_id=:toStoreId and to_store_item_mapper.unit_id=:unitId)))" 
//				+ " and ( productcat1.id in (select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper where store_pc_mapper.store_id = :fromStoreId ) "
//				+ "											AND item0.id NOT IN ("
//				+ "												select store_item_restriction.item_id from public.m_store_item_restriction  store_item_restriction where store_item_restriction.store_id = :fromStoreId and store_item_restriction.unit_id=:unitId"
//				+ "												))"
//				+ "AND ( productcat1.id in (select store_pc_mapper.product_category_id from public.m_store_pc_mapper store_pc_mapper where store_pc_mapper.store_id = :toStoreId ) "
//				+ "									AND item0.id NOT IN ("
//				+ "									select store_item_restriction.item_id from public.m_store_item_restriction  store_item_restriction where store_item_restriction.store_id = :toStoreId  and store_item_restriction.unit_id=:unitId"
//				+ "									)) " + ""
						+ " and (item0.is_consignment = false)  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getFromStoreId());
		query.setParameter("toStoreId", itemsRequest.getToStoreId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();

	}

	@Override
	public List<ItemListResponse> itemsListWithTax(ItemsRequest itemsRequest) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (item0.is_consignment = false)  "

				+ " union all  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  "
				+ " and item0.s_tax_id IS NOT NULL  " + " and item0.p_tax_id Is NOT NULL  "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (item0.is_consignment = false)  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();

	}

	@Override
	public List<ItemListResponse> assetItemsList(ItemsRequest itemsRequest) {
		String hql = "SELECT distinct item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "


				+ "		LEFT JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "

				+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				// + " AND (:fromStoreId = 0 OR storeMapperFrom.store.storeId = :fromStoreId) "
				// + " AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "

				+ "		AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ "		AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				/*
				 * +
				 * "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				 * +
				 * "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				 * +
				 * "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				 * +
				 * "												WHERE itemRestriction.store.storeId = :toStoreId "
				 * +
				 * "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				 */ + "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	}

	@Override
	public List<ItemListResponse> assetItemsListForAssetIndent(ItemsRequest itemsRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
				+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND (:fromStoreId = 0 OR storeMapperFrom.store.storeId = :fromStoreId) "
				+ "		AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "

				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ? itemsRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	}

	@Override
	public List<ItemsResponse> itemsListByBatchExpiry(ItemsRequest itemsRequest) {
		String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.lotNo AS lotNo, goodReceiptNote.grnNumber AS grnNo, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, goodReceiptNote.addedDate AS grnDate, "
				+ "		batch.cop AS cop, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, generic.name AS genericName " + "	FROM Batch AS batch "
				+ " 	INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ " 	LEFT JOIN batch.goodReceiptNoteDetail as goodReceiptNoteDetail WITH goodReceiptNoteDetail.isDeleted = false AND goodReceiptNoteDetail.isActive = true "
				+ " 	LEFT JOIN goodReceiptNoteDetail.goodReceiptNote as goodReceiptNote WITH goodReceiptNote.isDeleted = false AND goodReceiptNote.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				// + " AND batch.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND batch.expiryDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:fromStoreId = 0 OR store.storeId = :fromStoreId) "
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, goodReceiptNote.grnNumber, goodReceiptNote.addedDate, "
				+ "		batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, generic.name "
				+ "	ORDER BY batch.expiryDate DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(itemsRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(itemsRequest.getToDate()));
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ? itemsRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("vendorId", itemsRequest.getVendorId() != null ? itemsRequest.getVendorId() : 0);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(ItemsResponse.class));

		return query.list();

	}

	@Override
	public Long productCategoryCount(List<Integer> idList) {
		String hql = "SELECT count(*) " + "	FROM Item as item "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "	WHERE item.id in (:id) AND item.isDeleted = false AND item.isActive = true";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", idList);
		// query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

		// return query.list();
	}
	
	@Override
	public List<ItemsDetailsListResponse> itemsDetails(List<Integer> idList, Integer storeId,Integer unitId) {
		String sql = "SELECT item.id as itemId, item.code AS itemCode, item.name AS itemName, item.is_batch_required AS isBatchRequired, generic.id AS genericId, generic.name AS genericName, "
				+ "			manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, tax.id AS taxId, tax.name AS taxName, tax.tax_percentage AS taxPercent, "
				+ "			item.is_otc AS isOtc, productCategory.id AS productCategoryId, productCategory.category AS productCategory, assetCategory.id AS assetCategoryId, "
                + "         assetCategory.category AS assetCategory, coalesce(stock.totalQty, 0) AS stockAvailable, item.is_three_uom_applicable as isThreeUOMApplicable "
				+ " FROM public.m_item item "
				+ "	INNER JOIN public.m_generic generic ON item.generic_id=generic.id AND (generic.is_deleted=false AND generic.is_active=true) "
				+ "	INNER JOIN public.m_product_category productCategory ON generic.product_category_id=productCategory.id AND (productCategory.is_deleted=false AND productCategory.is_active=true) "
				+ "	INNER JOIN public.m_asset_category assetCategory ON productCategory.asset_category_id=assetCategory.id AND (assetCategory.is_deleted=false AND assetCategory.is_active=true) "
				+ "	INNER JOIN public.m_manufacturer manufacturer ON item.manufacturer_id=manufacturer.id AND (manufacturer.is_deleted=false AND manufacturer.is_active=true) "
				+ "	INNER JOIN public.m_tax tax ON item.p_tax_id=tax.id AND (tax.is_deleted=false AND tax.is_active=true) "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ "					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) and batch.item_id in (:id) and batch.unit_id=:unitId "
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and stock.unit_id=:unitId  "
				+ "								and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE (item.id in (:id)) AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";

		Query query = getSession().createSQLQuery(sql).addScalar("itemId").addScalar("itemCode").addScalar("itemName")
				.addScalar("genericId").addScalar("genericName").addScalar("manufacturerId")
				.addScalar("manufacturerName").addScalar("taxId").addScalar("taxName").addScalar("taxPercent")
				.addScalar("isOtc").addScalar("productCategoryId").addScalar("productCategory")
				.addScalar("assetCategoryId").addScalar("assetCategory").addScalar("isBatchRequired")
				.addScalar("stockAvailable", StandardBasicTypes.LONG).addScalar("isThreeUOMApplicable");
		query.setParameterList("id", idList);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

//		return query.list();
		return (List<ItemsDetailsListResponse>) query.list();
	}

	@Override
	public List<ItemsDetailsListResponse> itemsDetailsOpeningBalance(List<Integer> idList, Integer storeId) {
		String sql = "SELECT item.id as itemId, item.code AS itemCode, item.name AS itemName, item.is_batch_required AS isBatchRequired, item.is_three_uom_applicable AS isThreeUOMApplicable, generic.id AS genericId, generic.name AS genericName, "
				+ "			manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, tax.id AS taxId, tax.name AS taxName, tax.tax_percentage AS taxPercent, "
				+ "			item.is_otc AS isOtc, productCategory.id AS productCategoryId, productCategory.category AS productCategory, assetCategory.id AS assetCategoryId, "
				+ "			assetCategory.category AS assetCategory, "
				+ "			coalesce(stock.totalQty, 0) AS stockAvailable, "
				+ "			item.is_batch_required AS isBatchRequired "
				+ " FROM public.m_item item "
				+ "	LEFT JOIN public.m_generic generic ON item.generic_id=generic.id AND (generic.is_deleted=false AND generic.is_active=true) "
				+ "	INNER JOIN public.m_product_category productCategory ON generic.product_category_id=productCategory.id AND (productCategory.is_deleted=false AND productCategory.is_active=true) "
				+ "	INNER JOIN public.m_asset_category assetCategory ON productCategory.asset_category_id=assetCategory.id AND (assetCategory.is_deleted=false AND assetCategory.is_active=true) "
				+ "	INNER JOIN public.m_manufacturer manufacturer ON item.manufacturer_id=manufacturer.id AND (manufacturer.is_deleted=false AND manufacturer.is_active=true) "
				+ "	INNER JOIN public.m_tax tax ON item.p_tax_id=tax.id AND (tax.is_deleted=false AND tax.is_active=true) "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ "					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) "
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE (item.id in (:id)) AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";

		Query query = getSession().createSQLQuery(sql).addScalar("itemId").addScalar("itemCode").addScalar("itemName")
				.addScalar("genericId").addScalar("genericName").addScalar("manufacturerId")
				.addScalar("manufacturerName").addScalar("taxId").addScalar("taxName").addScalar("taxPercent")
				.addScalar("isOtc").addScalar("productCategoryId").addScalar("productCategory")
				.addScalar("assetCategoryId").addScalar("assetCategory").addScalar("isBatchRequired").addScalar("isThreeUOMApplicable")
				.addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameterList("id", idList);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

		return query.list();
	}

	@Override
	public Long getStockAvailable(Integer storeId, Integer  idList,Integer unitId) {
/*		String sql = "SELECT  coalesce(stock.totalQty, 0) AS stockAvailable "
				+ " FROM public.m_item item "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ "					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) "
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true and (batch.expiry_date >= current_date or batch.expiry_date is null) and stock.available_qty > 0 and  batch.is_quarantine=false "
				+ " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE item.id =:id AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";*/

		// as discussed with divyesh
		Date date = TimeZoneComponent.getDateByzone(unitId);
		String sql = "SELECT  coalesce(stock.totalQty, 0) AS stockAvailable "
				+ " FROM public.m_item item "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ " INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.is_quarantine=false )"
				+ " and (to_char(batch.expiry_date,'YYYY-MM-DD') >=:expiryDate  or batch.expiry_date is null )"
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0  "
				+ " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE item.id =:id AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";
		Query query = getSession().createSQLQuery(sql)
				.addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameter("id", idList);
		query.setParameter("storeId", storeId);
		query.setParameter("expiryDate",GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}

	@Override
	public Long getLeaseQunatity(Integer storeId, Integer itemId) {

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
		query.setParameter("unitId", sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}
	// for get Grn Total Lease qty Method write for Mismatch pending qty issue

	@Override
	public Long getGrnLeaseQunatity(Integer storeId, Integer itemId) {

		String hql = "SELECT SUM (CASE WHEN goodReceiptNoteDetail.status.id = :statusId THEN COALESCE((goodReceiptNoteDetail.grnLeaseQuantity + goodReceiptNoteDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.goodReceiptNoteDetail AS goodReceiptNoteDetail WITH goodReceiptNoteDetail.isDeleted = false AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id = :itemId AND purchaseRequestDetail.purchaseOrderDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public List<ItemsDetailsListResponse> itemsDetailsFixedAsset(List<Integer> idList, Integer storeId) {
		String sql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ "		manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, "
				+ "     COALESCE(stock.totalQty, 0) AS stockAvailable, tax.id AS taxId, tax.name AS taxName,"
				+ "		(CASE WHEN tax.tax_percentage IS NULL THEN 0.0 ELSE tax.tax_percentage END) AS taxPercent, "
				+ "		productCategory.id AS productCategoryId, productCategory.category AS productCategory, assetCategory.id AS assetCategoryId,"
				+ "  assetCategory.category AS assetCategory, item.is_batch_required AS isBatchRequired, generic.name AS genericName, item.is_three_uom_applicable as isThreeUOMApplicable "
				+ "	FROM public.m_item item"
				+ "	left JOIN public.m_generic generic ON item.generic_id=generic.id AND (generic.is_deleted=false AND generic.is_active=true) "
				+ "	left JOIN public.m_product_category AS productCategory ON item.product_category_id=productCategory.id AND (productCategory.is_deleted=false AND productCategory.is_active=true) "
				+ "	left JOIN public.m_asset_category assetCategory ON productCategory.asset_category_id=assetCategory.id AND (assetCategory.is_deleted=false AND assetCategory.is_active=true) "
				+ "	INNER JOIN public.m_manufacturer manufacturer ON item.manufacturer_id=manufacturer.id AND (manufacturer.is_deleted=false AND manufacturer.is_active=true) "
				+ "	LEFT JOIN public.m_tax tax ON item.p_tax_id=tax.id AND (tax.is_deleted=false AND tax.is_active=true) "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ "					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) 	"
				+ " and( batch.expiry_date >= current_date or batch.expiry_date  is null)  and batch.is_quarantine=false "
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and stock.available_qty > 0 "
				+ "  and stock.available_qty is not null and stock.total_qty is not null "
				+ "								and (stock.is_deleted=false and stock.is_active=true) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ "	WHERE item.id in (:id) AND item.is_deleted = false AND item.is_active = true "
				+ "	ORDER BY item.id ";

		//HIS-11878 - left join added
		Query query = getSession().createSQLQuery(sql).addScalar("itemId").addScalar("itemCode").addScalar("itemName")
				.addScalar("manufacturerId").addScalar("manufacturerName").addScalar("productCategoryId")
				.addScalar("productCategory").addScalar("assetCategoryId").addScalar("assetCategory").addScalar("taxId")
				.addScalar("taxName").addScalar("taxPercent").addScalar("genericName").addScalar("stockAvailable", StandardBasicTypes.LONG).addScalar("isThreeUOMApplicable");
		query.setParameterList("id", idList);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

		return query.list();
	}

	@Override
	public List<ItemListResponse> workOrderItemsList(ItemsRequest itemsRequest) {
		String hql = "SELECT DISTINCT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				// + " FROM Item AS item "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList storeMapper WITH storeMapper.isDeleted = false AND storeMapper.isActive = true "
				+ "		INNER JOIN storeMapper.store store WITH store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId"
				+ "	WHERE item.isDeleted = false AND item.isActive = true  "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND store.storeId = :storeId "
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ? itemsRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("storeId", itemsRequest.getStoreId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	}

	@Override
	public List<BatchListResponse> batchList(ItemsRequest itemRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0 "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
				+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				+ "		AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "  
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id "
				+ "	ORDER BY item.id, batch.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? itemRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? itemRequest.getItemName() + '%' : "");
		query.setParameter("batchName", itemRequest.getBatchName() != null ? itemRequest.getBatchName() + '%' : "");
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}
	
	@Override
	public List<BatchListResponse> batchListBillableConsumption(ItemsRequest itemRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, batch.isConsignment AS isConsignment, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear, item.isBatchRequired AS isBatchRequired"
				+ "		assetCategory.id as assetCategoryId, productCategory.id as productCategoryId  "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0 "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				/*+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "*/
				+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE storeStock.availableQty > 0  and  (extract(MONTH FROM batch.expiryDate) >= extract(MONTH FROM now()) AND  extract(YEAR FROM batch.expiryDate) >= extract(YEAR FROM now()) )  "
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				/*+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "*/
				+ "		AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				/*+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "*/
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id,item.isBatchRequired  "
				+ "	ORDER BY item.id, batch.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		/*query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);*/
		query.setParameter("toStoreId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? itemRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? itemRequest.getItemName() + '%' : "");
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}


    @Override
    public List<BatchListResponse> batchListStoreWise(ItemsRequest itemRequest) {
    	
    	    Calendar c = Calendar.getInstance();

			c.setTime(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
			c.set(Calendar.DAY_OF_MONTH, 1);
			if(itemRequest.getFromScrap() != null && itemRequest.getFromScrap()== true && itemRequest.getStockAvailable()!= null && itemRequest.getStockAvailable() == 1 ){
				c.add(Calendar.MONTH, 3);
			}

    	//GlobalCommonDateUtils.getFirstDateOfMonth(TimeZoneComponent.getDateByzone(unitId));


		String hqlProduct = "SELECT storeProduct.productCategory.id  FROM StoreProductCategoryMapper AS storeProduct  WHERE storeProduct.store.storeId = :storeId ";

		Query query1 = getSession().createQuery(hqlProduct);
		query1.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		List<Integer> productCatgIds = query1.list();
		if(productCatgIds == null || productCatgIds.isEmpty()  || productCatgIds.size() == 0 ){
			productCatgIds.add(0);
		}


		String hqlRistricItem = "SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction  WHERE itemRestriction.store.storeId = :storeId  " +
				" AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId ";

		Query query2 = getSession().createQuery(hqlRistricItem);
		query2.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query2.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		List<Integer> restrictIds = query2.list();
		if(restrictIds == null || restrictIds.isEmpty()  || restrictIds.size() == 0 ){
			restrictIds.add(0);
		}

		String expiryCondition = itemRequest.getFromScrap() != null && itemRequest.getFromScrap()== true
				? " AND (batch.expiryDate <= :currentDate OR batch.expiryDate IS NULL) "
				: " AND (batch.expiryDate >= :currentDate OR batch.expiryDate IS NULL) ";

		String hql = "SELECT  item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(CAST(storeStock.availableQty AS long), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		cast(batch.tax.id as int) AS taxId, purchaseTax.name AS taxName, batch.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.name AS vendor, batch.yearOfManufacture AS manufactureYear, item.isBatchRequired AS isBatchRequired,  "
				+ "		COALESCE(assetCategory.id) as assetCategoryId, item.isLasa as isLasa, "
				+ "		COALESCE(productCategory.id) as productCategoryId, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty>0 "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "	    WHERE batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId and batch.isQuarantine = false  "
				+expiryCondition
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
				+ "		AND (:batchId = 0 OR batch.id = :batchId) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "
				+ "		AND (:storeId = 0 OR productCategory.id in (:productCatgListIds) "
				+ "							AND item.id NOT IN (:restrictIds)) "
				+ "	ORDER BY item.id, batch.id  ";

		Query query = getSession().createQuery(hql);
        query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
        query.setParameter("assetCategoryId",
                itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
        query.setParameter("productCategoryId",
                itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
        query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
        query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
        query.setParameter("isConsignment",
                itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
        query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
        query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
        query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
        query.setParameter("batchName", itemRequest.getBatchName() != null ? '%'+itemRequest.getBatchName().toUpperCase() + '%' : "");
        query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
        query.setParameter("currentDate", c.getTime());
//        query.setParameter("itemId", itemRequest.getItemId()!=null ? itemRequest.getItemId().intValue():0);
        query.setParameter("batchId", itemRequest.getBatchId()!=null ? itemRequest.getBatchId().intValue() :0);
       // query.setParameter("FromScrap", itemRequest.getFromScrap() != null && itemRequest.getFromScrap()== true ?  true:false);
		query.setParameterList("productCatgListIds", productCatgIds);
		query.setParameterList("restrictIds", restrictIds);
        query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

        return query.list();
    }

    @Override
    public List<BatchListResponse> batchListStoreWiseWithoutGeneric(ItemsRequest itemRequest) {
		
    	String sql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batch_no AS batchNo, batch.expiry_date AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.available_qty), 0) AS availableQuantity, batch.lot_no AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.tax_percentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contact_person_name AS vendor, batch.year_of_manufacture AS manufactureYear, item.is_batch_required AS isBatchRequired,  "
				+ "		COALESCE(assetCategory.id, itemAssetCategory.id) as assetCategoryId, "
				+ "		COALESCE(productCategory.id, itemProductCategory.id) as productCategoryId, item.is_three_uom_applicable as isThreeUOMApplicable  "
				+ "		FROM inventory.t_batch AS batch "
				+ "		INNER JOIN public.m_item AS item ON batch.item_id=item.id and (item.is_deleted=false and item.is_active=true) "
				+ "		INNER JOIN public.m_tax AS purchaseTax on item.p_tax_id= purchaseTax.id and (purchaseTax.is_active=true and purchaseTax.is_deleted=false) "
				+ "		INNER JOIN public.m_generic AS generic  on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				+ "		LEFT JOIN public.m_product_category AS productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ "		LEFT JOIN public.m_asset_category AS assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ "		LEFT JOIN public.m_asset_type as assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ "		LEFT JOIN public.m_product_category AS itemProductCategory on item.product_category_id=itemProductCategory.id and (itemProductCategory.is_deleted=false and itemProductCategory.is_active=true) "
				+ "		LEFT JOIN public.m_asset_category AS itemAssetCategory on itemProductCategory.asset_category_id=itemAssetCategory.id and (itemAssetCategory.is_deleted=false and itemAssetCategory.is_active=true) "
				+ "		LEFT JOIN inventory.t_store_stock AS storeStock on batch.id=storeStock.batch_id and (storeStock.is_deleted=false and storeStock.is_active=true and storeStock.available_qty>0) "
				+ "		LEFT JOIN public.m_store AS store on storeStock.store_id=store.store_id and (store.is_deleted=false and store.is_active=true) "
				+ "		LEFT JOIN public.m_vendor AS vendor on batch.vendor_id=vendor.id and (vendor.is_active=true and vendor.is_deleted=false) "
				+ "	    WHERE batch.is_deleted = false AND batch.is_active = true AND batch.unit_id = :unitId "
				+ "		AND (:consignment = 0 OR item.is_consignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeStock.store_id = :storeId) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	and ( :batchName='' or upper(batch.batch_no) like upper(:batchName)) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.product_category_id "
				+ "														FROM public.m_store_pc_mapper AS storeProduct WHERE storeProduct.store_id = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item_id FROM public.m_store_item_restriction AS itemRestriction "
				+ "												WHERE itemRestriction.store_id = :storeId "
				+ "												AND itemRestriction.is_deleted = false AND itemRestriction.is_active = true and itemRestriction.unit_id = :unitId)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batch_no, batch.expiry_date, batch.rate, "
				+ "	batch.lot_no, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.tax_percentage, vendor.id, vendor.contact_person_name, productCategory.id, item.is_batch_required, "
				+ "	assetCategory.id, itemAssetCategory.id, productCategory.id, itemProductCategory.id "
				//+ "	ORDER BY item.id, batch.id, assetCategory.id, productCategory.id "
					
					+ " union  "
					
				+ " SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batch_no AS batchNo, batch.expiry_date AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.available_qty), 0) AS availableQuantity, batch.lot_no AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.tax_percentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contact_person_name AS vendor, batch.year_of_manufacture AS manufactureYear, item.is_batch_required AS isBatchRequired,  "
				+ "		COALESCE(assetCategory.id) as assetCategoryId, "
				+ "		COALESCE(productCategory.id) as productCategoryId, item.is_three_uom_applicable as isThreeUOMApplicable  "
				+ "		FROM inventory.t_batch AS batch "
				+ "		INNER JOIN public.m_item AS item ON batch.item_id=item.id and (item.is_deleted=false and item.is_active=true) "
				+ "		INNER JOIN public.m_tax AS purchaseTax on item.p_tax_id= purchaseTax.id and (purchaseTax.is_active=true and purchaseTax.is_deleted=false) "
				//+ "		INNER JOIN public.m_generic AS generic  on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				//+ "		LEFT JOIN public.m_product_category AS productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				//+ "		LEFT JOIN public.m_asset_category AS assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				//+ "		LEFT JOIN public.m_asset_type as assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ "		LEFT JOIN public.m_product_category AS productCategory on item.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ "		LEFT JOIN public.m_asset_category AS assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ "		LEFT JOIN public.m_asset_type as assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ "		LEFT JOIN inventory.t_store_stock AS storeStock on batch.id=storeStock.batch_id and (storeStock.is_deleted=false and storeStock.is_active=true and storeStock.available_qty>0) "
				+ "		LEFT JOIN public.m_store AS store on storeStock.store_id=store.store_id and (store.is_deleted=false and store.is_active=true) "
				+ "		LEFT JOIN public.m_vendor AS vendor on batch.vendor_id=vendor.id and (vendor.is_active=true and vendor.is_deleted=false) "
				+ "	    WHERE batch.is_deleted = false AND batch.is_active = true AND batch.unit_id = :unitId "
				+ "		AND (:consignment = 0 OR item.is_consignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeStock.store_id = :storeId) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	and ( :batchName='' or upper(batch.batch_no) like upper(:batchName)) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.product_category_id "
				+ "														FROM public.m_store_pc_mapper AS storeProduct WHERE storeProduct.store_id = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item_id FROM public.m_store_item_restriction AS itemRestriction "
				+ "												WHERE itemRestriction.store_id = :storeId "
				+ "												AND itemRestriction.is_deleted = false AND itemRestriction.is_active = true and itemRestriction.unit_id = :unitId)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batch_no, batch.expiry_date, batch.rate, "
				+ "	batch.lot_no, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.tax_percentage, vendor.id, vendor.contact_person_name, productCategory.id, item.is_batch_required, "
				+ "	assetCategory.id, productCategory.id "
				//+ "	ORDER BY item.id, batch.id, assetCategory.id, productCategory.id";
				+ "	ORDER BY itemId, batchId, assetCategoryId, productCategoryId ";

		Query query = getSession().createSQLQuery(sql)
				.addScalar("itemId").addScalar("assetCategory").addScalar("productCategory").addScalar("prodCatId").addScalar("itemCode")
				.addScalar("itemName").addScalar("batchId", StandardBasicTypes.LONG).addScalar("batchNo").addScalar("expiry")
				.addScalar("mrp").addScalar("availableQuantity",StandardBasicTypes.LONG).addScalar("lotNo").addScalar("cop").addScalar("taxId").addScalar("taxName").addScalar("taxPercentage")
				.addScalar("vendorId").addScalar("vendor").addScalar("manufactureYear").addScalar("isBatchRequired")
				.addScalar("assetCategoryId").addScalar("productCategoryId").addScalar("isThreeUOMApplicable");
				
        query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
        query.setParameter("assetCategoryId",
                itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
        query.setParameter("productCategoryId",
                itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
        query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
        query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
        query.setParameter("isConsignment",
                itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
        query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
        query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
        query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
        query.setParameter("batchName", itemRequest.getBatchName() != null ? itemRequest.getBatchName() + '%' : "");
        query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
        query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

        return query.list();
    }

	@Override
	public List<BatchListResponse> batchList(ItemsRequest itemRequest, BatchProperty batchProperty) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
				+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE storeStock.availableQty > 0 AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ " 	AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				+ "		AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	 GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName "
				+ "	ORDER BY item.id, batch.id ";
		//System.out.println("Store Id : "+itemRequest.getStoreId());
		//System.out.println("To Store Id : "+itemRequest.getToStoreId());
		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("isPharmacy",
				batchProperty.getIsPharmacy() == null || batchProperty.getIsPharmacy() ? 0 : 1);
		query.setParameter("isReturn", batchProperty.getIsReturn() == null || batchProperty.getIsReturn() ? 0 : 1);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<BatchListResponse> FixedTypebatchList(ItemsRequest itemRequest) {
		
		 Calendar c = Calendar.getInstance();

			c.setTime(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
			c.set(Calendar.DAY_OF_MONTH, 1);
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0 "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true " 
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId  "
				 + "		AND ( (:FromScrap = true and batch.expiryDate <=:currentDate  OR batch.expiryDate is null ) or "
				    + "           (:FromScrap = false and batch.expiryDate >=:currentDate  OR batch.expiryDate is null ))   " 
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) " 
				+ " 	and ( :itemCode='' or upper(item.code) like upper(:itemCode)) "  
				+ " 	and ( :itemName='' or upper(item.name) like upper(:itemName)) "  
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "  
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("batchName", itemRequest.getBatchName() != null ? itemRequest.getBatchName() + '%' : "");
		 query.setParameter("FromScrap", itemRequest.getFromScrap() != null && itemRequest.getFromScrap()== true ?  true:false);
		 query.setParameter("currentDate", c.getTime());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<BatchListResponse> FixedTypebatchListNew(ItemsRequest itemRequest) {
		

		String hqlProduct = "SELECT storeProduct.productCategory.id  FROM StoreProductCategoryMapper AS storeProduct  WHERE storeProduct.store.storeId = :storeId ";

		Query query1 = getSession().createQuery(hqlProduct);
		query1.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		List<Integer> productCatgIds = query1.list();
		if(productCatgIds == null || productCatgIds.isEmpty()  || productCatgIds.size() == 0 ){
			productCatgIds.add(0);
		}


		String hqlRistricItem = "SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction  WHERE itemRestriction.store.storeId = :storeId  " +
				" AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId ";

		Query query2 = getSession().createQuery(hqlRistricItem);
		query2.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query2.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		List<Integer> restrictIds = query2.list();
		if(restrictIds == null || restrictIds.isEmpty()  || restrictIds.size() == 0 ){
			restrictIds.add(0);
		}
		
		
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		cast(batch.tax.id as int) AS taxId, purchaseTax.name AS taxName, batch.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear, item.isBatchRequired AS isBatchRequired ,  "
				+ "		assetCategory.id as assetCategoryId, item.isLasa as isLasa, "
				+ "		productCategory.id as productCategoryId , batch.warrantyFrom as  warrantyFrom , batch.warrantyTo as  warrantyTo, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty>0  "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true  AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				+ "		AND (:batchId = 0 OR batch.id = :batchId) "
				+ "		AND (:itemId = 0 OR batch.item.id = :itemId) "
				+ " 	and ( :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and ( :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "
				+ "		AND (:storeId = 0 OR productCategory.id in (:productCatgListIds) "
				+ "							AND item.id NOT IN (:restrictIds)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, item.isBatchRequired ,"
				+ " assetCategory.id , productCategory.id  "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		//query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("batchName", itemRequest.getBatchName() != null ? itemRequest.getBatchName() + '%' : "");
		query.setParameter("itemId", itemRequest.getItemId()!=null ? itemRequest.getItemId().intValue():0);
        query.setParameter("batchId", itemRequest.getBatchId()!=null ? itemRequest.getBatchId().intValue() :0);
    	query.setParameterList("productCatgListIds", productCatgIds);
		query.setParameterList("restrictIds", restrictIds);
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<BatchListResponse> FixedTypebatchList(ItemsRequest itemRequest, BatchProperty batchProperty) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ " 	AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "	WHERE storeStock.availableQty > 0 AND batch.isQuarantine = false AND batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("isPharmacy",
				batchProperty.getIsPharmacy() == null || batchProperty.getIsPharmacy() ? 0 : 1);
		query.setParameter("isReturn", batchProperty.getIsReturn() == null || batchProperty.getIsReturn() ? 0 : 1);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<BatchAccessoriesMapperDto> batchAccessoriesList(Integer batchId,Integer unitId) {
		String hql = "SELECT batchMapper.id AS id, item.code AS itemCode, item.name AS itemName, "
				+ " batch.id AS batchId, batch.batchNo AS batchNo, batch.yearOfManufacture AS manufactureYear,"
				+ " batch.quantity AS quantity " + "	FROM BatchAccessoriesMapper AS batchMapper "
				+ " INNER JOIN batchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  "
				+ "	INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "	WHERE batchMapper.batch.id = :batchId "
				+ "AND batchMapper.isDeleted = false AND batchMapper.isActive = true AND batchMapper.unit.unitId = :unitId and (extract(MONTH FROM batch.expiryDate) >= extract(MONTH FROM now()) AND  extract(YEAR FROM batch.expiryDate) >= extract(YEAR FROM now()) ) "
				+ "	ORDER BY batchMapper.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("batchId", batchId != null ? (long) batchId : 0);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchAccessoriesMapperDto.class));

		return query.list();
	}

	@Override
	public List<BatchResponse> batchListByItemId(Integer itemId, Integer storeId,Integer unitId) {
		String hql = "SELECT batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ "	 vendor.id AS currentVendorId, " + "	vendor.name AS currentVendorName,batch.currentQuantity as currentQuantity"
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId AND batch.currentQuantity > 0"
				+ " 	AND (:itemId = 0 OR item.id = :itemId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "	GROUP BY batch.id, batch.batchNo, batch.expiryDate, "
				+ "		vendor.id, vendor.name " + "	ORDER BY batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId != null ? storeId : 0);
		query.setParameter("itemId", itemId != null ? itemId : 0);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchResponse.class));
		return query.list();
	}

	@Override
	public List<ItemListResponse> consignedItemsList(ItemsRequest itemsRequest, boolean isConsignment) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (item0.is_consignment = :isConsignment)  "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"

				+ " union all  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (item0.is_consignment = :isConsignment)  "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"
		;

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("isConsignment", isConsignment);
		query.setParameter("storeId", itemsRequest.getStoreId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();
	}

	@Override
	public List<ItemListResponse> consignedItemsListForInword(ItemsRequest itemsRequest, boolean isConsignment) {

		String hql = "select distinct item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (item0.is_consignment = :isConsignment)  "

				+ " union all  "

				+ " select distinct item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " where item0.is_deleted=false " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (item0.is_consignment = :isConsignment)  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("isConsignment", isConsignment);

		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();
	}

	@Override
	public List<BatchListResponse> consignedBatchList(ItemsRequest itemRequest, boolean isConsignment) {

		Date current = TimeZoneComponent.getDateByzone(itemRequest.getUnitId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "select	item1_.id as itemId,assetcateg5_.category as assetCategory,productcat4_.category as productCategory,\r\n"
				+ "	item1_.code as itemCode,item1_.name as itemName,batch0_.id as batchId,\r\n"
				+ "	batch0_.batch_no as batchNo,batch0_.expiry_date as expiry,batch0_.rate as mrp,\r\n"
				+ "	coalesce(sum( storestock7_.available_qty ),0) as availableQuantity,\r\n"
				+ "	batch0_.lot_no as lotNo,batch0_.cop as cop,tax2_.id as taxId,tax2_.name as taxName,\r\n"
				+ "	tax2_.tax_percentage as taxPercentage,vendor9_.id as vendorId,vendor9_.contact_person_name as vendor,\r\n"
				+ "	batch0_.year_of_manufacture as manufactureYear\r\n" + "from\r\n"
				+ "	inventory.t_batch batch0_\r\n"
				+ "inner join public.m_item item1_ on batch0_.item_id = item1_.id and(item1_.is_deleted = false and item1_.is_active = true)\r\n"
				+ "inner join public.m_tax tax2_ on item1_.p_tax_id = tax2_.id and(tax2_.is_active = true and tax2_.is_deleted = false)\r\n"
				+ "inner join public.m_generic generic3_ on item1_.generic_id = generic3_.id and(generic3_.is_deleted = false	and generic3_.is_active = true)\r\n"
				+ "inner join public.m_product_category productcat4_ on	generic3_.product_category_id = productcat4_.id	and(productcat4_.is_deleted = false	and productcat4_.is_active = true)\r\n"
				+ "inner join public.m_asset_category assetcateg5_ on	productcat4_.asset_category_id = assetcateg5_.id	and(assetcateg5_.is_deleted = false and assetcateg5_.is_active = true	)\r\n"
				+ "inner join public.m_asset_type assettype6_ on	assetcateg5_.asset_type_id = assettype6_.id	and(assettype6_.is_deleted = false	and assettype6_.is_active = true)\r\n"
				+ "left outer join inventory.t_store_stock storestock7_ on	batch0_.id = storestock7_.batch_id	and(storestock7_.is_deleted = false	and storestock7_.is_active = true)\r\n"
				+ "left outer join public.m_store store8_ on	storestock7_.store_id = store8_.store_id	and(store8_.is_deleted = false and store8_.is_active = true)\r\n"
				+ "left outer join public.m_vendor vendor9_ on	batch0_.vendor_id = vendor9_.id	and(vendor9_.is_active = true	and vendor9_.is_deleted = false)\r\n"
				+ "where\r\n" + "	batch0_.is_deleted = false \r\n" + "	and batch0_.is_active = true \r\n"
				+ "	and batch0_.is_quarantine = false \r\n" + "	and batch0_.unit_id =:unitId \r\n"
				+ "	and storestock7_.available_qty > 0 \r\n"
				+ "	and batch0_.expiry_date >:greaterThanCurrentMonth \r\n"
				+ "	and item1_.is_consignment =:isConsignment\r\n"
				+ "	and(:assetTypeId = 0 OR  assettype6_.id = :assetTypeId)\r\n"
				+ "	and(:assetCategoryId = 0 OR assetcateg5_.id =:assetCategoryId)\r\n"
				+ "	and(:productCategoryId = 0 OR  productcat4_.id =:productCategoryId)\r\n"
				+ "	and(:vendorId = 0 OR  vendor9_.id =:vendorId)\r\n"
				+"	AND(  :itemCode= '' OR UPPER( item1_.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item1_.name ) LIKE UPPER(:itemName))"
				+ "	and(:storeId = 0 OR store8_.store_id =:storeId)\r\n"
				+ "	and(:storeId = 0 OR (productcat4_.id in(select	storeprodu10_.product_category_id	from	public.m_store_pc_mapper storeprodu10_	where	storeprodu10_.store_id =:storeId\r\n"
				+ "			))\r\n"
				+ "			and(item1_.id not in(select	storeitemr11_.item_id	from	public.m_store_item_restriction storeitemr11_	where	storeitemr11_.store_id =:storeId	and storeitemr11_.is_deleted = false and storeitemr11_.is_active = true\r\n"
				+ "				))\r\n" + "	)\r\n"
				+ "	and(:toStoreId = 0 OR (productcat4_.id in(select	storeprodu12_.product_category_id	from	public.m_store_pc_mapper storeprodu12_	where storeprodu12_.store_id =:storeId\r\n"
				+ "					))\r\n" + "		and(\r\n"
				+ "			item1_.id not in(	select	storeitemr13_.item_id	from	public.m_store_item_restriction storeitemr13_	where	storeitemr13_.store_id =:storeId	and storeitemr13_.is_deleted = false	and storeitemr13_.is_active = true\r\n"
				+ "			))\r\n" + "	)\r\n" + "group by\r\n"
				+ "	item1_.id,assetcateg5_.category,productcat4_.category,item1_.code,item1_.name,batch0_.id,batch0_.batch_no,\r\n"
				+ "	batch0_.expiry_date,batch0_.rate,batch0_.lot_no,\r\n"
				+ "	batch0_.cop,tax2_.id,tax2_.name,tax2_.tax_percentage,vendor9_.id,vendor9_.contact_person_name\r\n"
				+ "union all\r\n" + "select\r\n"
				+ "	item1_.id as itemId,assetcateg5_.category as assetCategory,productcat4_.category as productCategory,\r\n"
				+ "	item1_.code as itemCode,item1_.name as itemName,batch0_.id as batchId,\r\n"
				+ "	batch0_.batch_no as batchNo,batch0_.expiry_date as expiry,batch0_.rate as mrp,\r\n"
				+ "	coalesce(sum( storestock7_.available_qty ),0) as availableQuantity,\r\n"
				+ "	batch0_.lot_no as lotNo,batch0_.cop as cop,tax2_.id as taxId,tax2_.name as taxName,\r\n"
				+ "	tax2_.tax_percentage as taxPercentage,vendor9_.id as vendorId,vendor9_.contact_person_name as vendor,\r\n"
				+ "	batch0_.year_of_manufacture as manufactureYear\r\n" + "from\r\n"
				+ "	inventory.t_batch batch0_\r\n"
				+ "inner join public.m_item item1_ on batch0_.item_id = item1_.id and(item1_.is_deleted = false and item1_.is_active = true)\r\n"
				+ "inner join public.m_tax tax2_ on item1_.p_tax_id = tax2_.id and(tax2_.is_active = true and tax2_.is_deleted = false)\r\n"
				+ "inner join public.m_product_category productcat4_ on	item1_.product_category_id = productcat4_.id	and(productcat4_.is_deleted = false	and productcat4_.is_active = true)\r\n"
				+ "inner join public.m_asset_category assetcateg5_ on	productcat4_.asset_category_id = assetcateg5_.id	and(assetcateg5_.is_deleted = false and assetcateg5_.is_active = true	)\r\n"
				+ "inner join public.m_asset_type assettype6_ on	assetcateg5_.asset_type_id = assettype6_.id	and(assettype6_.is_deleted = false	and assettype6_.is_active = true)\r\n"
				+ "left outer join inventory.t_store_stock storestock7_ on	batch0_.id = storestock7_.batch_id	and(storestock7_.is_deleted = false	and storestock7_.is_active = true)\r\n"
				+ "left outer join public.m_store store8_ on	storestock7_.store_id = store8_.store_id	and(store8_.is_deleted = false and store8_.is_active = true)\r\n"
				+ "left outer join public.m_vendor vendor9_ on	batch0_.vendor_id = vendor9_.id	and(vendor9_.is_active = true	and vendor9_.is_deleted = false)\r\n"
				+ "where\r\n" + "	batch0_.is_deleted = false \r\n" + "	and batch0_.is_active = true \r\n"
				+ "	and batch0_.is_quarantine = false \r\n" + "	and batch0_.unit_id =:unitId \r\n"
				+ "	and storestock7_.available_qty > 0 \r\n"
				+ "	and batch0_.expiry_date >:greaterThanCurrentMonth \r\n"
				+ "	and item1_.is_consignment =:isConsignment\r\n"
				+ "	and(:assetTypeId = 0 OR  assettype6_.id = :assetTypeId)\r\n"
				+ "	and(:assetCategoryId = 0 OR assetcateg5_.id =:assetCategoryId)\r\n"
				+ "	and(:productCategoryId = 0 OR  productcat4_.id =:productCategoryId)\r\n"
				+ "	and(:vendorId = 0 OR  vendor9_.id =:vendorId)\r\n"
				+"	AND(  :itemCode= '' OR UPPER( item1_.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item1_.name ) LIKE UPPER(:itemName))"
				+ "	and(:storeId = 0 OR store8_.store_id =:storeId)\r\n"
				+ "	and(:storeId = 0 OR (productcat4_.id in(select	storeprodu10_.product_category_id	from	public.m_store_pc_mapper storeprodu10_	where	storeprodu10_.store_id =:storeId\r\n"
				+ "			))\r\n"
				+ "			and(item1_.id not in(select	storeitemr11_.item_id	from	public.m_store_item_restriction storeitemr11_	where	storeitemr11_.store_id =:storeId	and storeitemr11_.is_deleted = false and storeitemr11_.is_active = true\r\n"
				+ "				))\r\n" + "	)\r\n"
				+ "	and(:toStoreId = 0 OR (productcat4_.id in(select	storeprodu12_.product_category_id	from	public.m_store_pc_mapper storeprodu12_	where storeprodu12_.store_id =:storeId\r\n"
				+ "					))\r\n" + "		and(\r\n"
				+ "			item1_.id not in(	select	storeitemr13_.item_id	from	public.m_store_item_restriction storeitemr13_	where	storeitemr13_.store_id =:storeId	and storeitemr13_.is_deleted = false	and storeitemr13_.is_active = true\r\n"
				+ "			))\r\n" + "	)\r\n" + "group by\r\n"
				+ "	item1_.id,assetcateg5_.category,productcat4_.category,item1_.code,item1_.name,batch0_.id,batch0_.batch_no,\r\n"
				+ "	batch0_.expiry_date,batch0_.rate,batch0_.lot_no,\r\n"
				+ "	batch0_.cop,tax2_.id,tax2_.name,tax2_.tax_percentage,vendor9_.id,vendor9_.contact_person_name";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment", isConsignment);
		// query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1
		// : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<BatchListResponse> consignedBatchListItemsProductCategory(ItemsRequest itemRequest,
			boolean isConsignment) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId AND storeStock.availableQty > 0  "
				+ "		AND (item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment", isConsignment);
		// query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1
		// : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<Quantities> getLeaseQunatityBP(Integer storeId, List<Integer> consumabelItemIds) {
		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) AS quantity, purchaseRequestDetail.item.id as itemId "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id in (:itemId) AND purchaseRequestDetail.goodReceiptNoteDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId group by purchaseRequestDetail.item.id \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", consumabelItemIds);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));
		return query.list();
	}

	@Override
	public List<Quantities> getGrnLeaseQunatityBP(Integer storeId, List<Integer> consumabelItemIds) {
		String hql = "SELECT SUM (CASE WHEN goodReceiptNoteDetail.status.id = :statusId THEN COALESCE((goodReceiptNoteDetail.grnLeaseQuantity + goodReceiptNoteDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) AS quantity, "
				+ " purchaseRequestDetail.item.id as itemId " + " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.goodReceiptNoteDetail AS goodReceiptNoteDetail WITH goodReceiptNoteDetail.isDeleted = false AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id in (:itemId) AND purchaseRequestDetail.purchaseOrderDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId group by purchaseRequestDetail.item.id \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameterList("itemId", consumabelItemIds);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemListResponse> getItemsListOnTemplet(ItemsRequest itemsRequest) {
		/*String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (item0.is_consignment = false) " + "and productcat2_.id = :procd "

				+ " union all  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  "
				+ " and item0.s_tax_id IS NOT NULL  " + " and item0.p_tax_id Is NOT NULL  "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (item0.is_consignment = false) " + " and productcat1.id = :procd "
				+ " and lower(item0.code) like :itemCode " + " and lower(item0.name) like  :itemName ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("itemCode",
				itemsRequest.getItemCode() != null ? "%" + itemsRequest.getItemCode().toLowerCase() + "%" : "");
		query.setParameter("itemName",
				itemsRequest.getItemName() != null ? "%" + itemsRequest.getItemName().toLowerCase() + "%" : "");
		query.setParameter("procd",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();*/
		
		
		String hql = "select distinct item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true) "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false "
				+ " and productcat2_.is_active=true) "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_ "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "	store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false " + " and item0.is_active=true " + " and item0.unit_id=:unitId "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = false) "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		// new api
		return (List<ItemListResponse>) query.list();
	}

	@Override
	public List<ItemsDetailsListResponse> itemAndBatchList(ItemsRequest itemsRequest) {
		String hql = "select  bth.id  as \"batchId\", " + "	concat(itm.name ,' || ',bth.batch_no) as \"itemName\","
				+ " itm.name  as \"equipmentName\","
				+ "	itm.id as \"itemId\" " + "	from inventory.t_batch bth "
				+ "	inner join public.m_item itm on bth. item_id = itm.id "
				+ "	inner join public.m_product_category produc on itm.product_category_id = produc.id "
				+ "	inner join public.m_asset_category assetcateg on produc.asset_category_id = assetcateg.id "
				+ "	inner join public.m_asset_type assettype on	assetcateg.asset_type_id = assettype.id"
				+ "	where produc.is_deleted =false " + "	and assetcateg.is_deleted =false "
				+ "	and assetcateg.id = :assetCategoryId" + "	and assettype.is_deleted =false "
				+ "	and assettype.id = 2 " + "	and bth.unit_id = :unitId " + "	order by \"itemName\" asc ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

		return query.list();

	}

	@Override
	public List<BatchListResponse> batchListWithAvailableQtyCheck(ItemsRequest itemRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0 "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id "
				+ "	ORDER BY item.id, batch.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<BatchListResponse> FixedTypebatchListWithAvailableQtyCheck(ItemsRequest itemRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<ItemListResponse> consumableItemsList(ItemsRequest itemsRequest) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = false)  " + " and item0.is_consumable = true "

				+ " union all  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (item0.is_consignment = false)  " + " and item0.is_consumable = true ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();
	}

	@Override
	public List<ItemListResponse> consumableItemsListByConsignment(ItemsRequest itemsRequest) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName, generic1_.name as generic  "
				+ " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper from_store_item_mapper on "
				+ "	item0.id = from_store_item_mapper.item_id " + "	and( "
				+ "		from_store_item_mapper.is_deleted=false and from_store_item_mapper.is_active=true " + "	) "
				///
				+ " left join public.m_store_item_mapper to_store_item_mapper on "
				+ "	item0.id = to_store_item_mapper.item_id " + "	and( "
				+ "		to_store_item_mapper.is_deleted=false and to_store_item_mapper.is_active=true " + "	) "

				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = :isConsignment)  " 
				+ " and (item0.is_consumable = :isConsumable) "
				+ " and (:storeId = 0 or from_store_item_mapper.store_id = :storeId)"
				+ " and (:toStoreId = 0 or to_store_item_mapper.store_id = :toStoreId)"
				///

				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"

				+ " union"

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName, generic1_.name as generic "
				+ " from public.m_item item0  "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper from_store_item_mapper on "
				+ "	item0.id = from_store_item_mapper.item_id " + "	and( "
				+ "		from_store_item_mapper.is_deleted=false and from_store_item_mapper.is_active=true " + "	) "
				///
				+ " left join public.m_store_item_mapper to_store_item_mapper on "
				+ "	item0.id = to_store_item_mapper.item_id " + "	and( "
				+ "		to_store_item_mapper.is_deleted=false and to_store_item_mapper.is_active=true " + "	) "

				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or from_store_item_mapper.store_id = :storeId)"
				+ " and (:toStoreId = 0 or to_store_item_mapper.store_id = :toStoreId)"

				+ " and (item0.is_consignment = :isConsignment)  " 
				+ " and (item0.is_consumable = :isConsumable) "
				+"	AND( :itemCode <> '' OR :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND( :itemName <> ''	OR :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		//query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);

		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("isConsumable",
				itemsRequest.getIsConsumable() != null ? itemsRequest.getIsConsumable() : true);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();
	}


	@Override
	public List<ItemListResponse> consumableItemsListByConsignmentIndent(ItemsRequest itemsRequest) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName, generic1_.name as generic  "
				+ " from public.m_item item0 "
				+ " LEFT join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper from_store_item_mapper on "
				+ "	item0.id = from_store_item_mapper.item_id " + "	and( "
				+ "		from_store_item_mapper.is_deleted=false and from_store_item_mapper.is_active=true " + "	) "
				///
				+ " left join public.m_store_item_mapper to_store_item_mapper on "
				+ "	item0.id = to_store_item_mapper.item_id " + "	and( "
				+ "		to_store_item_mapper.is_deleted=false and to_store_item_mapper.is_active=true " + "	) "

				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  and item0.is_consumable=true "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = :isConsignment)  "
				//+ " and (item0.is_consumable = :isConsumable) "
				+ " and (:storeId = 0 or from_store_item_mapper.store_id = :storeId)"
				+ " and (:toStoreId = 0 or to_store_item_mapper.store_id = :toStoreId)"
				///
				+ " 	and ( :itemCode='' or upper(item0.code) like upper(:itemCode)) "  
				+ " 	and ( :itemName='' or upper(item0.name) like upper(:itemName)) " 
				//+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "
				//+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"

				+ " union"

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName, generic1_.name as generic "
				+ " from public.m_item item0  "
				+ " LEFT join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper from_store_item_mapper on "
				+ "	item0.id = from_store_item_mapper.item_id " + "	and( "
				+ "		from_store_item_mapper.is_deleted=false and from_store_item_mapper.is_active=true " + "	) "
				///
				+ " left join public.m_store_item_mapper to_store_item_mapper on "
				+ "	item0.id = to_store_item_mapper.item_id " + "	and( "
				+ "		to_store_item_mapper.is_deleted=false and to_store_item_mapper.is_active=true " + "	) "

				+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId and item0.is_consumable=true "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or from_store_item_mapper.store_id = :storeId)"
				+ " and (:toStoreId = 0 or to_store_item_mapper.store_id = :toStoreId)"

				+ " and (item0.is_consignment = :isConsignment)  "
				//+ " and (item0.is_consumable = :isConsumable) "
				+ " 	and ( :itemCode='' or upper(item0.code) like upper(:itemCode)) "  
				+ " 	and ( :itemName='' or upper(item0.name) like upper(:itemName)) " ;

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		//query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);

		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		//query.setParameter("isConsumable",
		//		itemsRequest.getIsConsumable() != null ? itemsRequest.getIsConsumable() : true);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		//query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();
	}


	@Override
	public Long getApprovedPRandPOQuantity(Integer storeId, Integer itemId, Integer unitId) {

		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id = :itemId AND purchaseRequestDetail.goodReceiptNoteDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id = :statusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", com.param.common.constants.Status.Approved.getStatus());
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId !=null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}

	@Override
	public Long getApprovedStoreIndentQuantity(Integer storeId, Integer itemId) {

		String hql = "SELECT SUM (storeIndentDetail.indentQuantity)"
				+ " FROM StoreIndentDetail AS storeIndentDetail  "
				+ " INNER JOIN storeIndentDetail.storeIndent AS storeIndent WITH storeIndent.isDeleted = false AND storeIndent.isActive = true AND storeIndent.unit.unitId = :unitId \r\n"
				+ " WHERE storeIndent.toStore.storeId = :storeId AND storeIndentDetail.item.id = :itemId   \r\n"
				+ " AND storeIndentDetail.isDeleted = false AND storeIndentDetail.isActive = true AND storeIndentDetail.unit.unitId = :unitId "
				+ " AND storeIndent.approvalStatus.id = :statusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", com.param.common.constants.Status.Approved.getStatus());
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}
	
	@Override
	public Long getApprovedPurchaseOrderQuantity(Integer storeId, Integer itemId,Integer unitId) {

		String hql = "SELECT SUM ( COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 ))"
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail  "
				+ " INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder WITH purchaseOrder.isDeleted = false AND purchaseOrder.isActive = true AND purchaseOrder.unit.unitId = :unitId \r\n"
				+ " WHERE purchaseOrder.store.storeId = :storeId AND purchaseOrder.requestType.id != :requestType AND purchaseOrderDetail.item.id = :itemId AND purchaseOrderDetail.grnDetail is null  \r\n"
				+ " AND purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " AND purchaseOrderDetail.status.id = :statusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", com.param.common.constants.Status.Approved.getStatus());
		query.setParameter("storeId", storeId);
		query.setParameter("requestType", RequestType.AgainstPurchaseRequest.getRequestType());
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}

	@Override
	public List<Quantities> getApprovedPRandPOQuantityList(Integer storeId, List<Integer> itemId, Integer unitId ) {

		String hql = "SELECT  purchaseRequestDetail.item.id as itemId,SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 ) "
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) as quantity"
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id in(:itemIds) AND purchaseRequestDetail.goodReceiptNoteDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id = :statusId " + " group by purchaseRequestDetail.item.id \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", com.param.common.constants.Status.Approved.getStatus());
		query.setParameter("storeId", storeId);
		query.setParameterList("itemIds", itemId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));

		return query.list();

	}

	@Override
	public List<Quantities> getApprovedPurchaseOrderQuantityList(Integer storeId, List<Integer> itemId, Integer unitId) {

		String hql = "SELECT purchaseOrderDetail.item.id as itemId,SUM ( COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )) as quantity "
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail  "
				+ " INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder WITH purchaseOrder.isDeleted = false AND purchaseOrder.isActive = true AND purchaseOrder.unit.unitId = :unitId \r\n"
				+ " WHERE purchaseOrder.store.storeId = :storeId AND purchaseOrder.requestType.id != :requestType  AND purchaseOrderDetail.item.id in(:itemIds) AND purchaseOrderDetail.grnDetail is null  \r\n"
				+ " AND purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " AND purchaseOrderDetail.status.id = :statusId " + " group by purchaseOrderDetail.item.id \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", com.param.common.constants.Status.Approved.getStatus());
		query.setParameter("storeId", storeId);
		query.setParameter("requestType", RequestType.AgainstPurchaseRequest.getRequestType());
		query.setParameterList("itemIds", itemId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));

		return query.list();

	}

	@Override
	public List<ItemsSearchResponse> searchGeneric(boolean isOtc, String search, Integer storeId, Integer unitId) {

		Date current = TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		/*String isOtcFlag="";
		if(isOtc == true)
		{
			isOtcFlag="true";	
		}
		else
		{
			isOtcFlag="true , false ";		
		}*/
		String hql = "SELECT item.id AS id, item.code || ' | ' || item.name || ' | ' || generic.name AS details FROM Batch AS batch"
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				+ "		INNER JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId    "
				+ "		AND (upper(generic.name) like upper(:search) ) "
				+ "		AND store.storeId = :storeId AND item.id NOT IN (SELECT itemRestriction.item.id "
				+ "							FROM StoreItemRestriction AS itemRestriction "
				+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							WHERE store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  AND batch.unit.unitId = :unitId "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
				+ "		GROUP BY item.id , generic.name " + "     ORDER BY item.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId);
		//query.setParameter("isOtc", isOtc);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("search", search + '%');
		query.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));
		return query.list();

	}

	@Override
	public List<ItemListResponse> getIssuingAndReuestedStoreWiseItemsList(ItemsRequest itemsRequest) {
		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " ,item0.is_consignment as isConsignment "// adde by minesh
				//+ " ,item0.is_consignment as \"isConsignment\" "// adde by minesh
				+ " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				/*+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				*/
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  "
				//+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and  (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				//+ " and (item0.is_consignment = true)  "//comment by minesh for get all list(is_consignment true/false)
				+ " and (:requestingStoreId =0 or (item0.id in (select from_store_item_mapper.item_id from public.m_store_item_mapper from_store_item_mapper where from_store_item_mapper.store_id=:requestingStoreId  and from_store_item_mapper.unit_id=:unitId )) " + 
				"	and (:storeId = 0 or  item0.id in (select to_store_item_mapper.item_id from public.m_store_item_mapper to_store_item_mapper where to_store_item_mapper.store_id=:storeId  and to_store_item_mapper.unit_id=:unitId)))"
				+"  and item0.organization_id = :organizationId "
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"

				+ " union"

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " ,item0.is_consignment as isConsignment "// adde by minesh
				//+ " ,item0.is_consignment as \"isConsignment\" "// adde by minesh
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				//+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				 + " where item0.is_deleted=false  " + " and item0.is_active=true  "
			//	+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
			   + " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				//+ " and (item0.is_consignment = true)  "//comment by minesh for get all list(is_consignment true/false)
				+ " and (:requestingStoreId =0 or (item0.id in (select from_store_item_mapper.item_id from public.m_store_item_mapper from_store_item_mapper where from_store_item_mapper.store_id=:requestingStoreId  and from_store_item_mapper.unit_id=:unitId )) " + 
				"	and (:storeId = 0 or  item0.id in (select to_store_item_mapper.item_id from public.m_store_item_mapper to_store_item_mapper where to_store_item_mapper.store_id=:storeId  and to_store_item_mapper.unit_id=:unitId)))"
				+"  and item0.organization_id = :organizationId "
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName)) ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		//query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("requestingStoreId",itemsRequest.getRequestingStoreId() != null ? itemsRequest.getRequestingStoreId() : 0);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("organizationId", itemsRequest.getOrganizationId()!=null ? itemsRequest.getOrganizationId() : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();

	}

	@Override
	public List<ItemListResponse> assetIssuingAndReuestedStoreWiseItemsList(ItemsRequest itemsRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList AS deliveryStore WITH deliveryStore.isDeleted = false AND deliveryStore.isActive = true "
				+ "		INNER JOIN item.storeItemList AS requestingStore WITH requestingStore.isDeleted = false AND requestingStore.isActive = true "
				//+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "	WHERE item.isDeleted = false AND item.isActive = true "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:toStoreId = 0 OR deliveryStore.store.id = :toStoreId) "
				+ " 	AND (:fromStoreId = 0 OR requestingStore.store.id = :fromStoreId) "
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction.unit.unitId=:unitId)) "
				+ "	    AND (:toStoreId = 0  OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction.unit.unitId=:unitId)) "
				+ "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("toStoreId",
				itemsRequest.getRequestingStoreId() != null ? itemsRequest.getRequestingStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	}

	@Override
	public List<ItemListResponse> geStoreWiseItemsList(ItemsRequest itemsRequest) {
		// add below line by minesh for resolved HIS-8808 Sub Module: Consignment - Op Balance item not display
		String	isConsignment = itemsRequest.getIsConsignment() != null ? " and (item0.is_consignment = "+itemsRequest.getIsConsignment()+") " : " ";
		// end by minesh for resolved HIS-8808 Sub Module: Consignment - Op Balance item not display
		
		String hql = "select distinct item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true) "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false "
				+ " and productcat2_.is_active=true) "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_ "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "	store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				//+ " where item0.is_deleted=false " + " and item0.is_active=true " + " and item0.unit_id=:unitId "
				+ " where item0.is_deleted=false " + " and item0.is_active=true and store_item_mapper.unit_id=:unitId "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				//+ " and (item0.is_consignment = false) "
				+ isConsignment // added by minesh for resolved HIS-8808 Sub Module: Consignment - Op Balance item not display
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))";

				/*+ " union  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName "
				+ " from public.m_item item0 "
				+ " inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true) "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true) "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true) "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "	store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false " + " and item0.is_active=true " + " and item0.unit_id=:unitId "
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+ " and (item0.is_consignment = false) "
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))";*/

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");		
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		// new api
		return (List<ItemListResponse>) query.list();

	}

	@Override
	public List<BatchListResponse> ExpiredMedicationsbatchList(ItemsRequest itemRequest) {

		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0 "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapper WITH storeMapper.isDeleted = false AND storeMapper.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId  and YEAR(batch.expiryDate) <= YEAR(now()) and MONTH (batch.expiryDate) <= MONTH( now())  "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeMapper.store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id "
				+ "	ORDER BY item.id, batch.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId",  itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();

	}

	@Override
	public List<BatchListResponse> ExpiredMedicationsFixedTypebatchList(ItemsRequest itemRequest) {

		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId  and YEAR(batch.expiryDate) <= YEAR(now()) and MONTH (batch.expiryDate) <= MONTH( now()) "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId(): sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();

	}

	@Override
	public List<ItemsResponse> itemsListByBatchExpired(ItemsRequest itemsRequest) {

		// java.util.Date date= TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(itemsRequest.getFromDate());
		cal.set(itemsRequest.getFromDate().getYear() + 1900, itemsRequest.getFromDate().getMonth(), 1);
		int fromMonth = cal.get(Calendar.MONTH) + 1;
		int fromYear = cal.get(Calendar.YEAR);

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(itemsRequest.getToDate());
		int daysOfmonth = cal1.get(Calendar.DAY_OF_MONTH);
		cal1.set(itemsRequest.getToDate().getYear() + 1900, itemsRequest.getToDate().getMonth(), daysOfmonth);
		// cal1.add(Calendar.DATE, 1);
		int toMonth = cal1.get(Calendar.MONTH) + 1;
		int toYear = cal1.get(Calendar.YEAR);
		

		SimpleDateFormat simpleDateFormat1 = null;
		Date todate = null;
		SimpleDateFormat simpleDateFormat2 = null;
		Date fromdate = null;
		Date d1 = null;
		Date d2 = null;
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy"); // Existing Pattern
			todate = simpleDateFormat.parse(cal1.getTime().toString()); // Returns Date Format,
			fromdate = simpleDateFormat.parse(cal.getTime().toString()); // Returns Date Format,
			simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); // New Pattern

		} catch (ParseException e) {
		logger.error("itemsListByBatchExpired failed", e);
		}

		String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.lotNo AS lotNo, goodReceiptNote.grnNumber AS grnNo, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, goodReceiptNote.addedDate AS grnDate, "
				+ "		batch.cop AS cop, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, generic.name AS genericName, item.isThreeUOMApplicable as isThreeUOMApplicable " 
				+ "	FROM Batch AS batch "
				+ " 	INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0 "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ " 	LEFT JOIN batch.goodReceiptNoteDetail as goodReceiptNoteDetail WITH goodReceiptNoteDetail.isDeleted = false AND goodReceiptNoteDetail.isActive = true "
				+ " 	LEFT JOIN goodReceiptNoteDetail.goodReceiptNote as goodReceiptNote WITH goodReceiptNote.isDeleted = false AND goodReceiptNote.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId  "
				// + " YEAR(batch.expiryDate) >="+fromYear+" and MONTH(batch.expiryDate)
				// >="+fromMonth +" AND YEAR(batch.expiryDate) <="+toYear+" and
				// MONTH(batch.expiryDate) <="+toMonth
				// + " AND YEAR(batch.expiryDate) <= YEAR(now()) and MONTH (batch.expiryDate) <=
				// MONTH( now())"
				//+ " 	AND batch.expiryDate BETWEEN to_date(:fromDate,'YYYY-MM-DD') AND to_date(:toDate,'YYYY-MM-DD') "
				+ " 	AND batch.expiryDate BETWEEN to_date( '" + simpleDateFormat1.format(fromdate).trim() + "','YYYY-MM-DD') AND to_date( '" + simpleDateFormat1.format(todate).trim() + "','YYYY-MM-DD') "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:fromStoreId = 0 OR store.storeId = :fromStoreId) "
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, goodReceiptNote.grnNumber, goodReceiptNote.addedDate, "
				+ "		batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, generic.name "
				+ "	ORDER BY batch.expiryDate DESC";

		Query query = getSession().createQuery(hql);
		////System.out.println("db from : " + GlobalCommonDateUtils.getDate(fromdate, "yyyy-MM-dd HH:mm:ss"));
		//query.setParameter("fromDate", simpleDateFormat1.format(fromdate).trim());
		//query.setParameter("toDate", simpleDateFormat1.format(todate).trim());
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ? itemsRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("vendorId", itemsRequest.getVendorId() != null ? itemsRequest.getVendorId() : 0);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId() );

		query.setResultTransformer(Transformers.aliasToBean(ItemsResponse.class));

		return query.list();

	}

	@Override
	public List<ItemListResponse> AccessoriesWiseItemsList(ItemsRequest itemsRequest) {

		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true and item0.is_accessories=true "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = false)  "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"

				+ " union all  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  and item0.is_accessories=true  "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+ " and (item0.is_consignment = false)  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();

	}

	@Override
	public List<ItemListResponse> SpareWiseItemsList(ItemsRequest itemsRequest) {

		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true and item0.is_spares=true "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = false)  "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"

				+ " union all  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  and item0.is_spares=true  "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+ " and (item0.is_consignment = false)  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();

	}

	@Override
	public List<ItemListResponse> assetAccessoriesWiseItemsList(ItemsRequest itemsRequest) {

		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId  and item.isAccessories=true "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				// + " AND (:fromStoreId = 0 OR storeMapperFrom.store.storeId = :fromStoreId) "
				// + " AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ "		AND (:storeId = 0 OR storeMapperTo.store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				/*
				 * +
				 * "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				 * +
				 * "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				 * +
				 * "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				 * +
				 * "												WHERE itemRestriction.store.storeId = :toStoreId "
				 * +
				 * "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				 */ + "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		// query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ?
		// itemsRequest.getFromStoreId() : 0);
		// query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ?
		// itemsRequest.getToStoreId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();

	}

	@Override
	public List<ItemListResponse> assetSpareWiseItemsList(ItemsRequest itemsRequest) {/*

		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId  and item.isSpares=true "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				// + " AND (:fromStoreId = 0 OR storeMapperFrom.store.storeId = :fromStoreId) "
				// + " AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ "		AND (:storeId = 0 OR storeMapperTo.store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				
				 * +
				 * "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				 * +
				 * "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				 * +
				 * "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				 * +
				 * "												WHERE itemRestriction.store.storeId = :toStoreId "
				 * +
				 * "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				  + "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		// query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ?
		// itemsRequest.getFromStoreId() : 0);
		// query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ?
		// itemsRequest.getToStoreId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();

	*/
		
	
	


		String hql = "select item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true and item0.is_spares=true "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = false)  "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"

				+ " union all  "

				+ " select item0.id as itemId, assetcateg2.category as assetCategory, productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName  "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + "	) "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  and item0.is_spares=true  "
				+ " and item0.unit_id=:unitId  " + " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+ " and (item0.is_consignment = false)  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();

	
	
	
	
	}

	@Override
	public List<ItemsResponse> getItemsListByDateExpired(ItemsRequest itemsRequest) {

		/*
		 * Calendar cal = Calendar.getInstance(); cal.setTime(TimeZoneComponent.getDateByzone(unitId));
		 * cal.add(Calendar.DATE, 0); cal.add(Calendar.YEAR, 2010);
		 */

		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, -200); // to get previous year add -1
		Date preYear = cal.getTime();

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(itemsRequest.getToDate());
		cal1.set(itemsRequest.getToDate().getYear() + 1900, itemsRequest.getToDate().getMonth(), 1);
		int lastDay = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal1.set(itemsRequest.getToDate().getYear() + 1900, itemsRequest.getToDate().getMonth(), lastDay);
		// cal1.add(Calendar.YEAR, itemsRequest.getToDate().getYear());
		itemsRequest.setToDate(cal1.getTime());
		SimpleDateFormat simpleDateFormat1 = null;
		Date todate = null;
		SimpleDateFormat simpleDateFormat2 = null;
		Date fromdate = null;
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy"); // Existing Pattern
			todate = simpleDateFormat.parse(cal1.getTime().toString()); // Returns Date Format,
			fromdate = simpleDateFormat.parse(cal.getTime().toString()); // Returns Date Format,
			simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); // New Pattern
			// //System.out.println(simpleDateFormat1.format(todate));
			

		} catch (ParseException e) {
		logger.error("getItemsListByDateExpired failed", e);
		}
		  String dd1= simpleDateFormat1.format(fromdate);
		  String dd2= simpleDateFormat1.format(todate);
		  //System.out.println(" formatted from date : " + dd1);
			//System.out.println(" formatted to date : " + dd2);
		String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.lotNo AS lotNo, goodReceiptNote.grnNumber AS grnNo, goodReceiptNoteDetail.discount AS discount, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, goodReceiptNote.addedDate AS grnDate, "
				+ "		batch.cop AS cop, purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.name AS vendor, generic.name AS genericName, item.isThreeUOMApplicable as isThreeUOMApplicable " + "	FROM Batch AS batch "
				+ " 	INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ " 	LEFT JOIN batch.goodReceiptNoteDetail as goodReceiptNoteDetail WITH goodReceiptNoteDetail.isDeleted = false AND goodReceiptNoteDetail.isActive = true "
				+ " 	LEFT JOIN goodReceiptNoteDetail.goodReceiptNote as goodReceiptNote WITH goodReceiptNote.isDeleted = false AND goodReceiptNote.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND storeStock.availableQty > 0 AND batch.unit.unitId = :unitId "
				// + " AND YEAR(batch.expiryDate) <="+toYear+" and MONTH(batch.expiryDate)
				// <="+toMonth
				// + " AND YEAR(batch.expiryDate) <= YEAR(now()) and MONTH (batch.expiryDate) <=
				// MONTH( now())"
			 // + " AND batch.expiryDate BETWEEN :fromDate AND :toDate "
				//+ " 	AND batch.expiryDate BETWEEN STR_TO_DATE(:fromDate,'%Y-%m-%d') AND STR_TO_DATE(:toDate,'%Y-%m-%d') "\
			
				+ " 	AND batch.expiryDate BETWEEN to_date( '" + dd1.trim() + "','YYYY-MM-DD') AND to_date( '" + dd2.trim() + "','YYYY-MM-DD') "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:fromStoreId = 0 OR store.storeId = :fromStoreId) "
				+ " 	and ( :itemCode='' or upper(item.code) like upper(:itemCode)) "  
				+ " 	and ( :itemName='' or upper(item.name) like upper(:itemName)) "  
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction.unit.unitId = :unitId)) "
				+ "	GROUP BY item.id, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.lotNo, goodReceiptNote.grnNumber, goodReceiptNote.addedDate, goodReceiptNoteDetail.discount, "
				+ "		batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.name, generic.name "
				+ "	ORDER BY batch.expiryDate DESC";

		Query query = getSession().createQuery(hql);
		// //System.out.println(Util.getDate(preYear));

		

		//query.setParameter("fromDate", dd1.trim());
		//query.setParameter("toDate", dd2.trim());
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ? itemsRequest.getFromStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("vendorId", itemsRequest.getVendorId() != null ? itemsRequest.getVendorId() : 0);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setFirstResult(itemsRequest.getOffset()!=null ? itemsRequest.getOffset():0);
        query.setMaxResults(itemsRequest.getNoOfRecordsPerPage()!=null ? itemsRequest.getNoOfRecordsPerPage():100);
		query.setResultTransformer(Transformers.aliasToBean(ItemsResponse.class));

		return query.list();

	}
	
	@Override
	public List<BatchListResponse> batchListForLoanOut(ItemsRequest itemRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0  "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true  "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
				//commented for Resolved HIS-8949 Incorrect available quantity on the Loan out add new search
				//+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				//commented for Resolved HIS-8949 Incorrect available quantity on the Loan out add new search
				//+ "		AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ " 	and ( :itemCode='' or upper(item.code) like upper(:itemCode)) "  
				+ " 	and ( :itemName='' or upper(item.name) like upper(:itemName)) "  
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "  
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id "
				+ "	ORDER BY item.id, batch.id";
		

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("batchName", itemRequest.getBatchName() != null ? '%'+itemRequest.getBatchName().toUpperCase() + '%' : "");

		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}
	
	@Override
	public List<BatchListResponse> FixedTypebatchListForLoanOut(ItemsRequest itemRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
			//	+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				//+ "		AND (:toStoreId = 0 OR storeMapperTo.store.storeId = :toStoreId) "
				+ " 	and ( :itemCode='' or upper(item.code) like upper(:itemCode)) "  
				+ " 	and ( :itemName='' or upper(item.name) like upper(:itemName)) "  
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "  
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) " 
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
	//	query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? itemRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? itemRequest.getItemName() + '%' : "");
		query.setParameter("batchName", itemRequest.getBatchName() != null ? itemRequest.getBatchName() + '%' : "");

		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<ItemsSearchResponse> searchBarcodeScanItem( Integer itemId , Integer batchId) {


		Date current = TimeZoneComponent.getDateByzone(0);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT item.id AS id, item.code || ' | ' || item.name AS details FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				+ "		INNER JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		AND  item.id = :itemId  and batch.id =:batchId"
				+ "		AND store.storeId = :storeId AND item.id NOT IN (SELECT itemRestriction.item.id "
				+ "							FROM StoreItemRestriction AS itemRestriction "
				+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							WHERE store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
				// + " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR
				// batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
				+ "		GROUP BY item.id" + "     ORDER BY item.id";


		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", sessionService.getStoreId());
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("itemId", itemId);
		query.setParameter("batchId", Long.valueOf(batchId));
		
		query.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));

		return query.list();
	
	}

	@Override
	public List<ItemsSearchResponse> searchGenericScanItem(Integer itemId) {
	Date current = TimeZoneComponent.getDateByzone(0);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();

		String hql = "SELECT item.id AS id, item.code || ' | ' || item.name AS details FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				+ "		INNER JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		AND  item.id in ( SELECT item1.id from Item  as item1  where item1.id =:itemId and item1.generic.id is not null ) "
				+ "		AND store.storeId = :storeId AND item.id NOT IN (SELECT itemRestriction.item.id "
				+ "							FROM StoreItemRestriction AS itemRestriction "
				+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							WHERE store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false AND batch.unit.unitId = :unitId "
				// + " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR
				// batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND batch.expiryDate > :greaterThanCurrentMonth "
				+ "		GROUP BY item.id" + "     ORDER BY item.id";

   //item.productCode = :productCode 
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", sessionService.getStoreId());
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("itemId", itemId);
		query.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));

		return query.list();
	
	
	}

	@Override
	public List<ItemsDetailsListResponse> itemsDetailsFixedAssetOpeningBalance(List<Integer> idList, Integer storeId) {
		String sql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ "		manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, "
				+ "     COALESCE(stock.totalQty, 0) AS stockAvailable, tax.id AS taxId, tax.name AS taxName,"
				+ "		(CASE WHEN tax.tax_percentage IS NULL THEN 0.0 ELSE tax.tax_percentage END) AS taxPercent, "
				+ "		productCategory.id AS productCategoryId, productCategory.category AS productCategory, assetCategory.id AS assetCategoryId,"
				+ "  assetCategory.category AS assetCategory, item.is_batch_required AS isBatchRequired, item.is_three_uom_applicable AS isThreeUOMApplicable, generic.name AS genericName "
				+ "	FROM public.m_item item"
				+ "	LEFT JOIN public.m_generic generic ON item.generic_id=generic.id AND (generic.is_deleted=false AND generic.is_active=true) "
				+ "	LEFT JOIN public.m_product_category AS productCategory ON item.product_category_id=productCategory.id AND (productCategory.is_deleted=false AND productCategory.is_active=true) "
				+ "	LEFT JOIN public.m_asset_category assetCategory ON productCategory.asset_category_id=assetCategory.id AND (assetCategory.is_deleted=false AND assetCategory.is_active=true) "
				+ "	INNER JOIN public.m_manufacturer manufacturer ON item.manufacturer_id=manufacturer.id AND (manufacturer.is_deleted=false AND manufacturer.is_active=true) "
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
				.addScalar("productCategory").addScalar("assetCategoryId").addScalar("assetCategory").addScalar("taxId")
				.addScalar("taxName").addScalar("taxPercent").addScalar("genericName").addScalar("stockAvailable", StandardBasicTypes.LONG).addScalar("isThreeUOMApplicable")
				.addScalar("isBatchRequired", new TrueFalseType());
		query.setParameterList("id", idList);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

		return query.list();
	}

	
	@Override
	public List<Rack> getRackByStoreId(Integer storeId) {
		String hql = "SELECT rack FROM Rack AS rack "
				+ " INNER JOIN rack.store AS store WITH store.storeId = :storeId "
				+ " WHERE rack.isActive = true AND rack.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);

		return query.list();
	}

	@Override
	public List<Quantities> getStockAvailableBP(Integer storeId, List<Integer> itemIds,Integer unitId) {
        Date date = TimeZoneComponent.getDateByzone(unitId);
        String sql = "SELECT  item.id as itemId, coalesce(stock.totalQty, 0) AS quantity "
                + " FROM public.m_item item "
                + "    LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
                + "                FROM public.m_item item "
                + " INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.is_quarantine=false )"
                + " and (to_char(batch.expiry_date,'YYYY-MM-DD') >=:expiryDate or batch.expiry_date is null )"
                + "                    INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
                + "                                and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0  "
                + " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
                + "                GROUP BY item.id) stock ON stock.itemId = item.id "
                + " WHERE item.id in (:id) AND item.is_deleted=false AND item.is_active=true "
                + "    ORDER BY item.id ";
        Query query = getSession().createSQLQuery(sql)
				.addScalar("quantity", StandardBasicTypes.LONG)
				.addScalar("itemId");
		query.setParameterList("id", itemIds);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId);
		query.setParameter("expiryDate", GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		query.setResultTransformer(Transformers.aliasToBean(Quantities.class));

		return query.list();
	}

	@Override
	public List<ItemListResponse> listWithMappedStoresWithCompareAvalablity(ItemsRequest itemsRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList AS deliveryStore WITH deliveryStore.isDeleted = false AND deliveryStore.isActive = true "
				+ "		INNER JOIN item.storeItemList AS requestingStore WITH requestingStore.isDeleted = false AND requestingStore.isActive = true "
				+ "  	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:toStoreId = 0 OR deliveryStore.store.id = :toStoreId) "
				+ " 	AND (:fromStoreId = 0 OR requestingStore.store.id = :fromStoreId) "
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	    AND (:toStoreId = 0  OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("toStoreId",
				itemsRequest.getRequestingStoreId() != null ? itemsRequest.getRequestingStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public ItemListCompareMinResponse getItemAvalablityForStore(Integer
	 * storeId, Integer Itemid) { try { if( storeId!=null && Itemid!=null) {
	 * List<ItemListCompareMinResponse> ItemListCompareMinResponseobj=
	 * getSession().createSQLQuery("select " +
	 * "	sum(stock.available_qty) as \"availableQty\", " +
	 * "	mapp.min_qty as \"minQty\", " + "	mapp.max_qty as \"maxQty\" " +
	 * "from " + "	inventory.t_store_stock stock " +
	 * "inner join inventory.t_batch batch on " + "	stock.batch_id = batch.id " +
	 * "inner join public.m_store_item_mapper mapp on " +
	 * "	mapp.item_id = batch.item_id " + "	and mapp.store_id = stock.store_id "
	 * + "where " + "	stock.store_id ="+storeId+"and mapp.item_id =" +Itemid+
	 * "group by " + "	stock.store_id, " + "	mapp.min_qty, " + "	mapp.max_qty, "
	 * + "	mapp.item_id " + "having " +
	 * "	sum(stock.available_qty) < mapp.min_qty ")
	 * .setResultTransformer(Transformers.aliasToBean(ItemListCompareMinResponse.
	 * class)).list(); if(ItemListCompareMinResponseobj.size()>0){ return
	 * ItemListCompareMinResponseobj.get(0); }
	 * 
	 * } } catch (HibernateException he) { h } catch (Exception
	 * e) {  } finally {  } return null; }
	 */

	@Override
	@SuppressWarnings("unchecked")
	public List<ItemListCompareMinResponse>  getItemAvalablityForStore(Integer storeId, List<Integer> itemIds) {
		try {
			if( itemIds!=null && itemIds.size()>0) {
				@SuppressWarnings("unchecked")
				List<ItemListCompareMinResponse> ItemListCompareMinResponseobj=  
						getSession().createSQLQuery("select " + 
						"   mapp.item_id  as  \"itemId\" ," +		
						"	sum(stock.available_qty) as \"availableQty\", " + 
						"	mapp.min_qty as \"minQty\", " + 
						"	mapp.max_qty as \"maxQty\" " + 
						"from " + 
						"	inventory.t_store_stock stock " + 
						"inner join inventory.t_batch batch on " + 
						"	stock.batch_id = batch.id " + 
						"inner join public.m_store_item_mapper mapp on " + 
						"	mapp.item_id = batch.item_id " + 
						"	and mapp.store_id = stock.store_id " + 
						"where " + 
						"	stock.store_id =:storeId and mapp.item_id in (:ids) " +
						"group by " + 
						"	stock.store_id, " + 
						"	mapp.min_qty, " + 
						"	mapp.max_qty, " + 
						"	mapp.item_id " + 
						"having " + 
						"	sum(stock.available_qty) < mapp.min_qty ")
						.setParameterList("ids", itemIds)
								.setParameter("storeId",storeId)
						.setResultTransformer(Transformers.aliasToBean(ItemListCompareMinResponse.class)).list();
				if(ItemListCompareMinResponseobj.size()>0){
							return ItemListCompareMinResponseobj;
				}
				
			}
			} catch (HibernateException he) {
			logger.error("getItemAvalablityForStore failed", he);
			} catch (Exception e) {
			logger.error("getItemAvalablityForStore failed", e);
			} finally {
				
			}
			return null;
	}

	@Override
	public List<ItemsDetailsListResponse> fixedItemsDetails(List<Integer> itemIds, Integer storeId, Integer unitId) {
		String sql = "SELECT item.id as itemId, item.code AS itemCode, item.name AS itemName, item.is_batch_required AS isBatchRequired, generic.id AS genericId, generic.name AS genericName, "
				+ "			manufacturer.id AS manufacturerId, manufacturer.name AS manufacturerName, tax.id AS taxId, tax.name AS taxName, tax.tax_percentage AS taxPercent, "
				+ "			item.is_otc AS isOtc, productCategory.id AS productCategoryId, productCategory.category AS productCategory, assetCategory.id AS assetCategoryId, "
				+ "			assetCategory.category AS assetCategory, coalesce(stock.totalQty, 0) AS stockAvailable "
				+ " FROM public.m_item item "
				+ "	LEFT JOIN public.m_generic generic ON item.generic_id=generic.id AND (generic.is_deleted=false AND generic.is_active=true) "
				+ "	LEFT JOIN public.m_product_category productCategory ON generic.product_category_id=productCategory.id AND (productCategory.is_deleted=false AND productCategory.is_active=true) "
				+ "	LEFT JOIN public.m_asset_category assetCategory ON productCategory.asset_category_id=assetCategory.id AND (assetCategory.is_deleted=false AND assetCategory.is_active=true) "
				+ "	LEFT JOIN public.m_manufacturer manufacturer ON item.manufacturer_id=manufacturer.id AND (manufacturer.is_deleted=false AND manufacturer.is_active=true) "
				+ "	LEFT JOIN public.m_tax tax ON item.p_tax_id=tax.id AND (tax.is_deleted=false AND tax.is_active=true) "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ "					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) and batch.item_id in (:id) and batch.unit_id=:unitId "
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and stock.unit_id=:unitId  "
				+ "								and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE (item.id in (:id)) AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id ";

		Query query = getSession().createSQLQuery(sql).addScalar("itemId").addScalar("itemCode").addScalar("itemName")
				.addScalar("genericId").addScalar("genericName").addScalar("manufacturerId")
				.addScalar("manufacturerName").addScalar("taxId").addScalar("taxName").addScalar("taxPercent")
				.addScalar("isOtc").addScalar("productCategoryId").addScalar("productCategory")
				.addScalar("assetCategoryId").addScalar("assetCategory").addScalar("isBatchRequired")
				.addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameterList("id", itemIds);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		
		query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

		return query.list();
	}

	@Override
	public List<ItemListResponse> assetIssuingAndReuestedStoreWiseItemsListAccessories(ItemsRequest itemsRequest) {

		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		LEFT JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "		INNER JOIN item.storeItemList AS deliveryStore WITH deliveryStore.isDeleted = false AND deliveryStore.isActive = true "
				+ "		INNER JOIN item.storeItemList AS requestingStore WITH requestingStore.isDeleted = false AND requestingStore.isActive = true "
				//+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
				+ "	WHERE item.isDeleted = false AND item.isActive = true AND  item.isAccessories=true "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:toStoreId = 0 OR deliveryStore.store.id = :toStoreId) "
				+ " 	AND (:fromStoreId = 0 OR requestingStore.store.id = :fromStoreId) "
				+ "		AND (:fromStoreId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :fromStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :fromStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction.unit.unitId=:unitId)) "
				+ "	    AND (:toStoreId = 0  OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct WHERE storeProduct.store.storeId = :toStoreId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :toStoreId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction.unit.unitId=:unitId)) "
				+ "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("toStoreId",
				itemsRequest.getRequestingStoreId() != null ? itemsRequest.getRequestingStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	
	}
//	Rahul G code---------------
	
	
	@Override
	public List<ItemListResponse> itemsList1(ItemsRequest itemsRequest) {

		Date date = TimeZoneComponent.getDateByzone(itemsRequest.getUnitId());

		String hql = "select item0.id as itemid, assetcateg3.category as assetcategory, productcat2_.category as "
				+ " productcategory, item0.code as itemcode, item0.name as itemname, stock.totalQty AS availquantity  " + " from public.m_item item0 "
				+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true)  "
				+ " INNER join public.m_product_category productcat2_ on generic1_.product_category_id=productcat2_.id and (productcat2_.is_deleted=false  "
				+ " and productcat2_.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id  "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_  "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and  "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true  and store_item_mapper.unit_id=:unitId " + "	) "

				+ " INNER JOIN (SELECT item0.id AS itemId, sum(stock.available_qty) AS totalQty , stock.store_id as storeId"
				+ "		FROM public.m_item item0 "
					+ "	INNER JOIN inventory.t_batch batch ON batch.item_id = item0.id AND ( to_char(batch.expiry_date,'YYYY-MM-DD') >= '"+GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd")+"'" + 
					" or batch.expiry_date is null)"
						+ " INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id  and stock.store_id = :storeId "
						+ " and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0)  GROUP BY item0.id,stock.store_id) "
						+ " stock ON stock.itemId = item0.id and stock.totalQty >0   and stock.storeId = :storeId   "

				//+ " where item0.is_deleted=false  " + " and item0.is_active=true  " + " and item0.unit_id=:unitId  "
				+ " where item0.is_deleted=false  " + " and item0.is_active=true  "
				
				
				
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				+ " and (item0.is_consignment = false)  "
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))"

				+ " union"

				+ " select item0.id as itemcd, assetcateg2.category as assetcategory, productcat1.category as productcategory, item0.code as itemcode,"
				+ " item0.name as itemname,stock.totalQty AS availquantity "
				+ " from public.m_item item0  "
				+ "  inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)  "
				+ " inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)   "
				+ " inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)  "
				+ " inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)  "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true and store_item_mapper.unit_id=:unitId " + "	) "
				

				+ " INNER JOIN (SELECT item0.id AS itemId, sum(stock.available_qty) AS totalQty , stock.store_id as storeId"
				+ "		FROM public.m_item item0 "
					+ "	INNER JOIN inventory.t_batch batch ON batch.item_id = item0.id AND ( to_char(batch.expiry_date,'YYYY-MM-DD') >= '"+GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd")+"'" + 
					" or batch.expiry_date is null)"
						+ " INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id  and stock.store_id = :storeId "
						+ " and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0)  GROUP BY item0.id,stock.store_id) "
						+ " stock ON stock.itemId = item0.id and stock.totalQty >0   and stock.storeId = :storeId   "
								+ " where item0.is_deleted=false  " + " and item0.is_active=true  "

				
				+ " and (:assetTypeId = 0 or assettype3.id = :assetTypeId)  "
				+ " and (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat1.id = :productCategoryId)"
				+ " and (:storeId = 0 or store_item_mapper.store_id = :storeId)"
				+ " and (item0.is_consignment = false)  "
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName))";

		Query query = getSession().createSQLQuery(hql).addScalar("itemid").addScalar("assetcategory").addScalar("productcategory")
				.addScalar("itemcode").addScalar("itemname").addScalar("availquantity", StandardBasicTypes.LONG);
		//query.setParameter("assetTypeId", Constants.getFixedAssetId());
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId());
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		return (List<ItemListResponse>) query.list();
	}

	@Override
	public List<ItemListResponse> itemsSearchForPendingIssueItemSearch(Integer unitId, boolean isOtc,
			boolean isNarcotic, String search, Integer storeId) {
		Date current = TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		String hql = "SELECT item.id AS id, item.code || ' | ' || item.name AS details FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.isNarcotic=:isNarcotic "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
				+ "		INNER JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
				//+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId AND item.isOtc in ( "+isOtcFlag+" )"
				+ "		WHERE item.isDeleted = false AND item.isActive = true AND item.isConsumable = true "
				//+ "AND item.isOtc in ( "+isOtcFlag+" )"
				+ "		AND (upper(item.code) like upper(:search) OR upper(item.name) like upper(:search)) "
				+ "		AND store.storeId = :storeId AND item.id NOT IN (SELECT itemRestriction.item.id "
				+ "							FROM StoreItemRestriction AS itemRestriction "
				+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							WHERE store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
				+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
				+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  AND batch.unit.unitId = :unitId "
				// + " AND ((:isPharmacy = 0 OR batch.isPharmacy = false ) OR (:isIndent = 0 OR
				// batch.isIndent = false) OR (:isReturn = 0 OR batch.isReturn = false)) "
				+ "		AND  storeStock.store.storeId = :storeId AND storeStock.availableQty > 0 AND ( batch.expiryDate >= :greaterThanCurrentMonth OR batch.expiryDate is null) "
				+ "		GROUP BY item.id" + "     ORDER BY item.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId);
		//query.setParameter("isOtc", isOtc);
		query.setParameter("isNarcotic", isNarcotic);
		query.setParameter("greaterThanCurrentMonth", current);
		query.setParameter("search", '%'+search + '%');
		query.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));

		return query.list();
	}
	@Override
	public Long getStockAvailable1(Integer storeId, Integer  idList,Integer unitId) {
		Date date = TimeZoneComponent.getDateByzone(unitId);
		String sql = "SELECT  coalesce(stock.totalQty, 0) AS stockAvailable "
				+ " FROM public.m_item item "
				+ "	LEFT JOIN (SELECT item.id AS itemId, sum(stock.available_qty) AS totalQty "
				+ "				FROM public.m_item item "
				+ " INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.is_quarantine=false )"
				+ " and (to_char(batch.expiry_date,'YYYY-MM-DD') >=:expiryDate or batch.expiry_date is null )"
				+ "					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and batch.unit_id =:unitId and stock.store_id = :storeId "
				+ "								and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0  "
				+ " and stock.available_qty is not null and stock.total_qty is not null and stock.unit_id =:unitId ) "
				+ "				GROUP BY item.id) stock ON stock.itemId = item.id "
				+ " WHERE item.id in (:id) AND item.is_deleted=false AND item.is_active=true "
				+ "	ORDER BY item.id LIMIT 1";
		Query query = getSession().createSQLQuery(sql)
		.addScalar("stockAvailable", StandardBasicTypes.LONG);
		query.setParameter("id", idList);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setParameter("expiryDate",GlobalCommonDateUtils.getStringDate(date ,"yyyy-MM-dd"));
		//query.setResultTransformer(Transformers.aliasToBean(ItemsDetailsListResponse.class));

		return (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);
	}

	@Override
	public List<BatchListResponse> batchListStoreWiseAndKit(ItemsRequest itemRequest) {

    	
	    Calendar c = Calendar.getInstance();

		c.setTime(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
		c.set(Calendar.DAY_OF_MONTH, 1);
	
		
		String hqlProduct = "SELECT storeProduct.productCategory.id  FROM StoreProductCategoryMapper AS storeProduct  WHERE storeProduct.store.storeId = :storeId ";

		Query query1 = getSession().createQuery(hqlProduct);
		query1.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		List<Integer> productCatgIds = query1.list();
		if(productCatgIds == null || productCatgIds.isEmpty()  || productCatgIds.size() == 0 ){
			productCatgIds.add(0);
		}


		String hqlRistricItem = "SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction  WHERE itemRestriction.store.storeId = :storeId  " +
				" AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId ";

		Query query2 = getSession().createQuery(hqlRistricItem);
		query2.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query2.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		List<Integer> restrictIds = query2.list();
		if(restrictIds == null || restrictIds.isEmpty()  || restrictIds.size() == 0 ){
			restrictIds.add(0);
		}
		
		
	String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
			+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
			+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
			+ "		cast( batch.tax.id as int) AS taxId, purchaseTax.name AS taxName, batch.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
			+ "		vendor.name AS vendor, batch.yearOfManufacture AS manufactureYear, item.isBatchRequired AS isBatchRequired,  "
			+ "		COALESCE(assetCategory.id, itemAssetCategory.id) as assetCategoryId, item.isLasa as isLasa,"
			+ "		COALESCE(productCategory.id, itemProductCategory.id) as productCategoryId, item.isThreeUOMApplicable as isThreeUOMApplicable  "
			+ "	FROM Batch AS batch "
			+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
			+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
			+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
			+ "		LEFT JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
			+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
			+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
			+ "		LEFT JOIN item.productCategory AS itemProductCategory WITH itemProductCategory.isDeleted = false AND itemProductCategory.isActive = true "
			+ "		LEFT JOIN itemProductCategory.assetCategory AS itemAssetCategory WITH itemAssetCategory.isDeleted = false AND itemAssetCategory.isActive = true  "
			+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
			+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
			+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
			+ "	    WHERE batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId and batch.isQuarantine = false  "
		    + "		AND ( batch.expiryDate >=:currentDate  OR batch.expiryDate is null )  "
			+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
			+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
			+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
			+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
			+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
			+ "		AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
			+ "		AND (:batchId = 0 OR batch.id = :batchId) "
			+ "		AND (:itemId = 0 OR batch.item.id = :itemId) "
			+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
			+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
			+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "
			+ "		AND (:storeId = 0 OR productCategory.id in (:productCatgListIds) "
			+ "							AND item.id NOT IN (:restrictIds)) "
			+ "  AND item.id IN (:itemIds) "
			+ "	 GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
			+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id, item.isBatchRequired, "
			+ "	assetCategory.id, itemAssetCategory.id, " 
			+ "	productCategory.id, itemProductCategory.id "
			+ "	ORDER BY item.id, batch.id, assetCategory.id, itemAssetCategory.id, "
			+ "	productCategory.id, itemProductCategory.id";

	Query query = getSession().createQuery(hql);
    query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
    query.setParameter("assetCategoryId",
            itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
    query.setParameter("productCategoryId",
            itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
    query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
    query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
    query.setParameter("isConsignment",
            itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
    query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
    query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
    query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
    query.setParameter("batchName", itemRequest.getBatchNo() != null ? itemRequest.getBatchNo() + '%' : "");
    query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
    query.setParameter("currentDate", c.getTime());
    query.setParameter("itemId", itemRequest.getItemId()!=null ? itemRequest.getItemId().intValue():0);
    query.setParameterList("itemIds", itemRequest.getItemIds());
    query.setParameter("batchId", itemRequest.getBatchId()!=null ? itemRequest.getBatchId().intValue() :0);
    query.setParameterList("productCatgListIds", productCatgIds);
   	query.setParameterList("restrictIds", restrictIds);
    query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

    return query.list();

	}

	@Override
	public List<BatchListResponse> batchListStoreWiseForStockAdjustment(ItemsRequest itemRequest) {
		Calendar c = Calendar.getInstance();

		c.setTime(TimeZoneComponent.getDateByzone(itemRequest.getUnitId()));
		c.set(Calendar.DAY_OF_MONTH, 1);
	//GlobalCommonDateUtils.getFirstDateOfMonth(TimeZoneComponent.getDateByzone(unitId));
	
		String hqlProduct = "SELECT storeProduct.productCategory.id  FROM StoreProductCategoryMapper AS storeProduct  WHERE storeProduct.store.storeId = :storeId ";

		Query query1 = getSession().createQuery(hqlProduct);
		query1.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		List<Integer> productCatgIds = query1.list();
		if(productCatgIds == null || productCatgIds.isEmpty()  || productCatgIds.size() == 0 ){
			productCatgIds.add(0);
		}


		String hqlRistricItem = "SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction  WHERE itemRestriction.store.storeId = :storeId  " +
				" AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId ";

		Query query2 = getSession().createQuery(hqlRistricItem);
		query2.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query2.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		List<Integer> restrictIds = query2.list();
		if(restrictIds == null || restrictIds.isEmpty()  || restrictIds.size() == 0 ){
			restrictIds.add(0);
		}
		
		
	String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
			+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
			+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
			+ "		cast(batch.tax.id as int) AS taxId, purchaseTax.name AS taxName, batch.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
			+ "		vendor.name AS vendor, batch.yearOfManufacture AS manufactureYear, item.isBatchRequired AS isBatchRequired,  "
			+ "		COALESCE(assetCategory.id, itemAssetCategory.id) as assetCategoryId, item.isLasa as isLasa,"
			+ "		COALESCE(productCategory.id, itemProductCategory.id) as productCategoryId, item.isThreeUOMApplicable as isThreeUOMApplicable  "
			+ "	FROM Batch AS batch "
			+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
			+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
			+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
			+ "		LEFT JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
			+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
			+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
			+ "		LEFT JOIN item.productCategory AS itemProductCategory WITH itemProductCategory.isDeleted = false AND itemProductCategory.isActive = true "
			+ "		LEFT JOIN itemProductCategory.assetCategory AS itemAssetCategory WITH itemAssetCategory.isDeleted = false AND itemAssetCategory.isActive = true  "
			+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
			+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
			+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
			//+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
			+ "	    WHERE batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId and batch.isQuarantine = false  "
		    + "		AND ( batch.expiryDate >=:currentDate  OR batch.expiryDate is null )  "
			//+ " and  extract(MONTH FROM batch.expiryDate) >= extract(MONTH FROM now()) AND  extract(YEAR FROM batch.expiryDate) >= extract(YEAR FROM now()) "
			+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
			+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
			+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
			+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
			+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
			+ "		AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
			+ "		AND (:batchId = 0 OR batch.id = :batchId) "
			+ "		AND (:itemId = 0 OR batch.item.id = :itemId) "
			+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
			+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
			+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "
			+ "		AND (:storeId = 0 OR productCategory.id in (:productCatgListIds) "
			+ "							AND item.id NOT IN (:restrictIds)) "
			+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
			+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id, item.isBatchRequired, "
			+ "	assetCategory.id, itemAssetCategory.id, " 
			+ "	productCategory.id, itemProductCategory.id ,batch.tax.id,batch.taxPercentage ,vendor.name,batch.yearOfManufacture "
			+ "	ORDER BY item.id, batch.id, assetCategory.id, itemAssetCategory.id, "
			+ "	productCategory.id, itemProductCategory.id";

	Query query = getSession().createQuery(hql);
    query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
    query.setParameter("assetCategoryId",
            itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
    query.setParameter("productCategoryId",
            itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
    query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
    query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
    query.setParameter("isConsignment",
            itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
    query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
    query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
    query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
    query.setParameter("batchName", itemRequest.getBatchName() != null ? '%'+itemRequest.getBatchName() + '%' : "");
    query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
    query.setParameter("currentDate", c.getTime());
    query.setParameter("itemId", itemRequest.getItemId()!=null ? itemRequest.getItemId().intValue():0);
    query.setParameter("batchId", itemRequest.getBatchId()!=null ? itemRequest.getBatchId().intValue() :0);
    query.setParameterList("productCatgListIds", productCatgIds);
	query.setParameterList("restrictIds", restrictIds);
    query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

    return query.list();
	}

	@Override
	public List<BatchListResponse> FixedTypebatchListNewForStockAdjustment(ItemsRequest itemRequest) {
		
		
		String hqlProduct = "SELECT storeProduct.productCategory.id  FROM StoreProductCategoryMapper AS storeProduct  WHERE storeProduct.store.storeId = :storeId ";

		Query query1 = getSession().createQuery(hqlProduct);
		query1.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		List<Integer> productCatgIds = query1.list();
		if(productCatgIds == null || productCatgIds.isEmpty()  || productCatgIds.size() == 0 ){
			productCatgIds.add(0);
		}


		String hqlRistricItem = "SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction  WHERE itemRestriction.store.storeId = :storeId  " +
				" AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId ";

		Query query2 = getSession().createQuery(hqlRistricItem);
		query2.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query2.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		List<Integer> restrictIds = query2.list();
		if(restrictIds == null || restrictIds.isEmpty()  || restrictIds.size() == 0 ){
			restrictIds.add(0);
		}
		
		
		
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		cast(batch.tax.id as int) AS taxId, purchaseTax.name AS taxName, batch.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear, item.isBatchRequired AS isBatchRequired ,  "
				+ "		assetCategory.id as assetCategoryId, item.isLasa as isLasa, "
				+ "		productCategory.id as productCategoryId , batch.warrantyFrom as  warrantyFrom , batch.warrantyTo as  warrantyTo, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true  "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
				//+ "		INNER JOIN item.storeItemList AS storeMapperTo WITH storeMapperTo.isDeleted = false AND storeMapperTo.isActive = true "
				//+ "	WHERE batch.isDeleted = false AND batch.isActive = true and  extract(MONTH FROM batch.expiryDate) >= extract(MONTH FROM now()) AND  extract(YEAR FROM batch.expiryDate) >= extract(YEAR FROM now()) AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true  AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				+ "		AND (:batchId = 0 OR batch.id = :batchId) "
				+ "		AND (:itemId = 0 OR batch.item.id = :itemId) "
				+ " 	and ( :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and ( :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "
				+ "		AND (:storeId = 0 OR productCategory.id in (:productCatgListIds) "
				+ "							AND item.id NOT IN (:restrictIds)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, item.isBatchRequired ,"
				+ " assetCategory.id , productCategory.id  "
				+ "	ORDER BY item.id, batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("vendorId", itemRequest.getVendorId() != null ? itemRequest.getVendorId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		//query.setParameter("toStoreId", itemRequest.getToStoreId() != null ? itemRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemRequest.getIsConsignment() != null ? itemRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("batchName", itemRequest.getBatchName() != null ? '%'+itemRequest.getBatchName() + '%' : "");
		query.setParameter("itemId", itemRequest.getItemId()!=null ? itemRequest.getItemId().intValue():0);
        query.setParameter("batchId", itemRequest.getBatchId()!=null ? itemRequest.getBatchId().intValue() :0);
        query.setParameterList("productCatgListIds", productCatgIds);
    	query.setParameterList("restrictIds", restrictIds);
		query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

		return query.list();
	}

	@Override
	public List<UomResponse> getApprovedPRandPOQuantityAll(Integer storeId, Integer unitId) {
		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) AS approvedPRAndPOQty,purchaseRequestDetail.item.id AS itemId "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.goodReceiptNoteDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id = :statusId group by purchaseRequestDetail.item.id \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", com.param.common.constants.Status.Approved.getStatus());
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId !=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<UomResponse> getApprovedPurchaseOrderQuantityAll(Integer storeId, Integer unitId) {
		String hql = "SELECT SUM ( COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )) AS approvedPOQty,purchaseOrderDetail.item.id AS itemId"
				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail  "
				+ " INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder WITH purchaseOrder.isDeleted = false AND purchaseOrder.isActive = true AND purchaseOrder.unit.unitId = :unitId \r\n"
				+ " WHERE purchaseOrder.store.storeId = :storeId AND purchaseOrder.requestType.id != :requestType AND purchaseOrderDetail.grnDetail is null  \r\n"
				+ " AND purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " AND purchaseOrderDetail.status.id = :statusId group by purchaseOrderDetail.item.id \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", com.param.common.constants.Status.Approved.getStatus());
		query.setParameter("storeId", storeId);
		query.setParameter("requestType", RequestType.AgainstPurchaseRequest.getRequestType());
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();
	}

	@Override
	public List<ItemsSearchResponse> itemsSearchStockAdjustment(Integer unitId, boolean isOtc, String search,
			Integer storeId, Integer assetTypeId) {
		Date current = TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		if(assetTypeId==2)
		{
		String hql1 = "SELECT distinct item.id AS id, item.formulationType.id AS formulationTypeId, item.code || ' | ' || item.name AS details, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true  "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
				+ "	WHERE batch.isDeleted = false AND batch.isActive = true  AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "		AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
				+ " 	and ( :itemName='' or upper(item.name) like upper(:itemName)) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, item.isBatchRequired ,"
				+ " assetCategory.id , productCategory.id  "
				+ "	ORDER BY item.id ";

		Query query1 = getSession().createQuery(hql1);
		query1.setParameter("assetTypeId", assetTypeId != null ? assetTypeId : 0);
		query1.setParameter("storeId", storeId != null ? storeId : 0);
		query1.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query1.setParameter("itemName", search != null ? '%'+search.toUpperCase() + '%' : "");
		query1.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));
		return query1.list();
		}
		else
		{
			String hql = "SELECT item.id AS id, item.formulationType.id AS formulationTypeId, item.code || ' | ' || item.name AS details, item.isThreeUOMApplicable as isThreeUOMApplicable FROM Batch AS batch "
					+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true  "
					+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
					+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
					+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
					+ "		INNER JOIN productCategory.storeMapperList AS storeProduct WITH storeProduct.isDeleted = false AND storeProduct.isActive = true "
					+ "		INNER JOIN storeProduct.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "		WHERE item.isDeleted = false AND item.isActive = true "
					+ "		AND (upper(item.code) like upper(:search) OR upper(item.name) like upper(:search)) "
					+ "		AND store.storeId = :storeId AND item.id NOT IN (SELECT itemRestriction.item.id "
					+ "							FROM StoreItemRestriction AS itemRestriction "
					+ "							INNER JOIN itemRestriction.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "							WHERE store.storeId = :storeId AND itemRestriction.unit.unitId = :unitId "
					+ "							AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true) "
					+ "		AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  AND batch.unit.unitId = :unitId "
					+ "        AND  storeStock.store.storeId = :storeId AND storeStock.availableQty >= 0 AND storeStock.availableQty is not null AND ( batch.expiryDate >= :greaterThanCurrentMonth OR batch.expiryDate is null)"
					+ " AND assetCategory.assetType.id=:assetTypeId "

					+ "		GROUP BY item.id" + "     ORDER BY item.id";
			Query query = getSession().createQuery(hql);
			query.setParameter("storeId", storeId);
			query.setParameter("unitId", unitId);
	        query.setParameter("assetTypeId", assetTypeId);
			query.setParameter("greaterThanCurrentMonth", current);
			query.setParameter("search", '%'+search + '%');
			query.setResultTransformer(Transformers.aliasToBean(ItemsSearchResponse.class));

			return query.list();
		}
		
		
	}

	@Override
	public List<UomResponse> getBatchwiseDiscount(List<Long> batchIds) {

		String hql = "  select discount as \"discount\" ,batch_id as \"integerBatchId\" from procurement.t_good_receipt_note_detail where batch_id in(:batchId)  ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameterList("batchId", batchIds);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));

		return query.list();

	}
	
	
}
