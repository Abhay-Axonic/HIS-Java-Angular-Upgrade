package com.param.inventory.stock.dao.openingBalance;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.OpeningBalanceImplant;
import com.param.entity.model.inventory.OpeningBalanceImplantDetail;
import com.param.entity.model.master.Item;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;

@Repository
public class OpeningBalanceImplantDao extends AbstractDao<Integer, Item> implements IOpeningBalanceImplantDao
{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<OpeningBalanceImplant> searchOpeningBalanceImplant(OpeningBalanceRequest request) {

		String hql = "SELECT openingBalanceImplant FROM OpeningBalanceImplant AS openingBalanceImplant "
				+ "		LEFT JOIN openingBalanceImplant.store as store "
				+ "WHERE openingBalanceImplant.isActive = true AND openingBalanceImplant.isDeleted = false AND openingBalanceImplant.unit.unitId = :unitId "
				+ "		AND openingBalanceImplant.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "										AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ "ORDER BY openingBalanceImplant.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		return query.list();
	}

	
	
	//for Main Search get Item Implant List
	@Override
	public List<ItemListResponse> implantItemsList(ItemsRequest itemsRequest) {
		/*String hql =" select item0.id as itemId, assetcateg2.category as assetCategory,	productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName "
				+"from public.m_item item0 "
				+"inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)"
				+"inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)"
				+"inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)"
				+"inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)"
				+"where item0.is_deleted=false and item0.is_active=true and item0.unit_id=:unitId  "
				+"	AND(  :itemCode= '' OR UPPER( item0.code ) LIKE UPPER(:itemCode)) "  
				+"	AND(  :itemName= ''	OR UPPER( item0.name ) LIKE UPPER(:itemName)) AND  "
				+"(:assetTypeId = 0 or assettype3.id = :assetTypeId) and (:assetTypeId = 0 or productcat1.id IN "
				+"(select storePc.product_category_id from public.m_store_pc_mapper storePc "
				+"inner join public.m_product_category pc on pc.id = storePc.product_category_id "
				+"inner join public.m_asset_category ac on ac.id = pc.asset_category_id And ac.asset_type_id = :assetTypeId where storePc.store_id =:storeId)) and "
				+ " (:assetCategoryId = 0 or assetcateg2.id = :assetCategoryId) and (:assetCategoryId = 0 or productcat1.id IN " 
				+"				(select storePc.product_category_id from public.m_store_pc_mapper storePc "
				+"				inner join public.m_product_category pc on pc.id = storePc.product_category_id " 
				+"				inner join public.m_asset_category ac on ac.id = pc.asset_category_id And ac.id = :assetCategoryId where storePc.store_id =:storeId)) and "
				+"(item0.is_implant = true)";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);

		query.setParameter("storeId",itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		List<ItemListResponse> list=(List<ItemListResponse>)query.list();
		//System.out.println("unit:-"+sessionService.getUnitId());
		return list;*/
		String hql = "select distinct item0.id as itemId, assetcateg3.category as assetCategory, productcat2_.category as "
				+ " productCategory, item0.code as itemCode, item0.name as itemName " + " from public.m_item item0 "
				//+ " inner join public.m_generic generic1_ on item0.generic_id=generic1_.id and (generic1_.is_deleted=false and generic1_.is_active=true) "
				+ " INNER join public.m_product_category productcat2_ on item0.product_category_id=productcat2_.id and (productcat2_.is_deleted=false "
				+ " and productcat2_.is_active=true) "
				+ " inner join public.m_asset_category assetcateg3 on productcat2_.asset_category_id=assetcateg3.id "
				+ " and (assetcateg3.is_deleted=false and assetcateg3.is_active=true) inner join public.m_asset_type assettype4_ "
				+ " on assetcateg3.asset_type_id=assettype4_.id and (assettype4_.is_deleted=false and assettype4_.is_active=true) "
				+ " inner join public.m_manufacturer manufactur5_ on item0.manufacturer_id=manufactur5_.id and "
				+ " (manufactur5_.is_deleted=false and manufactur5_.is_active=true) "
				+ " inner join public.m_tax tax6_ on item0.p_tax_id=tax6_.id and (tax6_.is_deleted=false and tax6_.is_active=true) "
				+ " inner join public.m_store_item_mapper store_item_mapper on "
				+ "	item0.id = store_item_mapper.item_id " + "	and( "
				+ "	store_item_mapper.is_deleted=false and store_item_mapper.is_active=true and store_item_mapper.unit_id=:unitId 	) "
				//+ " where item0.is_deleted=false " + " and item0.is_active=true " + " and item0.unit_id=:unitId "
				+ " where item0.is_deleted=false " + " and item0.is_active=true "
				+  " and item0.is_implant = true "
				+ " and (:assetTypeId = 0 or assettype4_.id = :assetTypeId) "
				+ " and (:assetCategoryId = 0 or assetcateg3.id = :assetCategoryId) "
				+ " and (:productCategoryId = 0 or productcat2_.id = :productCategoryId)"
				//+ " and (item0.is_consignment = false) "
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
		query.setParameter("unitId", itemsRequest.getUnitId() !=null ? itemsRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		// new api
		return (List<ItemListResponse>) query.list();
	}
	
	
	@Override
	public List<ItemListResponse> consignmentImplantItemsList(ItemsRequest itemsRequest) {
		String hql =" select item0.id as itemId, assetcateg2.category as assetCategory,	productcat1.category as productCategory, item0.code as itemCode, item0.name as itemName "
				+"from public.m_item item0 "
				+"inner join public.m_product_category productcat1 on item0.product_category_id=productcat1.id and (productcat1.is_deleted=false and productcat1.is_active=true)"
				+"inner join public.m_asset_category assetcateg2 on productcat1.asset_category_id=assetcateg2.id and (assetcateg2.is_deleted=false and assetcateg2.is_active=true)"
				+"inner join public.m_asset_type assettype3 on assetcateg2.asset_type_id=assettype3.id and (assettype3.is_deleted=false and assettype3.is_active=true)"
				+"inner join public.m_manufacturer manufactur4 on item0.manufacturer_id=manufactur4.id and (manufactur4.is_deleted=false and manufactur4.is_active=true)"
				+" inner join public.m_store_item_mapper store_item_mapper on " + 
				"	item0.id = store_item_mapper.item_id " + 
				"	and( " + 
				"		store_item_mapper.is_deleted=false and store_item_mapper.is_active=true " + 
				"	) "
				+"where item0.is_deleted=false and item0.is_active=true and item0.unit_id=:unitId and"
				+"(:assetTypeId = 0 or assettype3.id = :assetTypeId) and (:assetCategoryId = 0 or productcat1.id IN "
				+"(select storePc.product_category_id from public.m_store_pc_mapper storePc "
				+"inner join public.m_product_category pc on pc.id = storePc.product_category_id "
				+"inner join public.m_asset_category ac on ac.id = pc.asset_category_id And ac.asset_type_id = :assetTypeId where storePc.store_id =:storeId and pc.asset_category_id=:assetCategoryId)) "
				+ "and (:storeId = 0 or store_item_mapper.store_id = :storeId)";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("storeId",itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
		query.setParameter("unitId", itemsRequest.getUnitId()!=null ?itemsRequest.getUnitId() : sessionService.getUnitId());
		
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));
		List<ItemListResponse> list=(List<ItemListResponse>)query.list();
		//System.out.println("unit:-"+sessionService.getUnitId());
		return list;
	}
	//For Save Method
	@Override
	public OpeningBalanceImplant saveOpeningBalanceImplant(OpeningBalanceImplant openingBalanceImplant) {
		getSession().save(openingBalanceImplant);

		return openingBalanceImplant;
	}
	//For Details Parent Method
	@Override
	public OpeningBalanceImplant getOpeningBalaceImplant(Integer id) {
		String hql = "SELECT openingBalanceImplant FROM OpeningBalanceImplant AS openingBalanceImplant WHERE openingBalanceImplant.id = :id AND openingBalanceImplant.isActive = true AND openingBalanceImplant.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (OpeningBalanceImplant) query.uniqueResult();
	}
	//For Details child Method
	@Override
	public List<OpeningBalanceImplantDetail> getOpeningbalaceImplantDetail(Integer openingBalanceImplantId) {
		String hql = "SELECT openingBalanceImplantDetail FROM OpeningBalanceImplantDetail AS openingBalanceImplantDetail \r\n"
				+ " INNER JOIN openingBalanceImplantDetail.openingBalanceImplant AS openingBalanceImplant WITH openingBalanceImplant.isActive = true AND openingBalanceImplant.isDeleted = false \r\n"
				+ " WHERE openingBalanceImplant.id = :openingBalanceImplantId AND openingBalanceImplantDetail.isActive = true AND openingBalanceImplantDetail.isDeleted = false \r\n"
				+ " ORDER BY openingBalanceImplantDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("openingBalanceImplantId", openingBalanceImplantId);

		return query.list();
	}
	
	//For Update Method
	@Override
	public OpeningBalanceImplant updateOpeningBalanceImplant(OpeningBalanceImplant openingBalanceImplant)
	{
		getSession().update(openingBalanceImplant);
		return openingBalanceImplant;
	}
	
	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}


}
