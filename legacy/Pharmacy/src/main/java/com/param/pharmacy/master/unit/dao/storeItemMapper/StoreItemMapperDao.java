package com.param.pharmacy.master.unit.dao.storeItemMapper;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemMapper;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.pharmacy.master.unit.dto.StoreStockRequestDate;
import com.param.pharmacy.master.unit.dto.StoreStockResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperListResponse;
import com.param.service.global.SessionService;
@SuppressWarnings({"unchecked","rawtypes","unused"})
@Repository("StoreItemMapperDao")
public class StoreItemMapperDao extends AbstractDao<Integer, StoreItemMapper> implements IStoreItemMapperDao {

	@Autowired
	SessionService sessionService;

	@Override
	public List<StoreItemMapper> getAll(Integer storeId) {
		Criteria criteria = getSession().createCriteria(StoreItemMapper.class).createAlias("organization","organization");
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("organizationId", sessionService.getOrganizationId()));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("store.storeId", storeId));
		
		Criteria storeCriteria = getSession().createCriteria(Store.class);
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria genericCriteria = getSession().createCriteria(Generic.class);
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria productCategoryCriteria = genericCriteria.createCriteria("productCategory");
		productCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetCategoryCriteria = productCategoryCriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		return criteria.list();
	}
	
	@Override
	public List<StoreItemMapper> update() {
		Criteria criteria = getSession().createCriteria(StoreItemMapper.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria storeCriteria = getSession().createCriteria(Store.class);
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria itemCriteria = getSession().createCriteria(Item.class);
		criteria.add(Restrictions.eq("isDeleted", false));
		
		return criteria.list();
	}
	

	@Override
	public StoreItemMapper getById(BigInteger id) {
		Criteria criteria = getSession().createCriteria(StoreItemMapper.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (StoreItemMapper) criteria.uniqueResult();
	}

	/*@Override
	public StoreItemMapper getStoreItemMapper(BigInteger id) {
		String hql = "SELECT storeItemMapper FROM StoreItemMapper AS equipment "
	            + "WHERE storeItemMapper.id = :id  AND storeItemMapper.isActive = true AND storeItemMapper.isDeleted = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (StoreItemMapper) query.uniqueResult();
	}
	*/
	
	@Override
	public BigInteger create(StoreItemMapper storeItemMapper) {
		getSession().save(storeItemMapper);
		return storeItemMapper.getId();
	}
	
	@Override
	public BigInteger update(StoreItemMapper storeItemMapper) {
		getSession().update(storeItemMapper);
		return storeItemMapper.getId();
	}
	
	@Override
	public Store getStore(Integer storeId) {
		return (Store) getSession().get(Store.class, storeId);
	}

	@Override
	public Item getItem(Integer itemId) {
		return (Item) getSession().get(Item.class, itemId);
	}

	@Override
	public List<StoreItemMapperListResponse> getListBetterPerformance(Integer storeId) {
		
		
		String hql = "select " + 
				"storeItemMapper.id as id," + 
				"storeItemMapper.minQty as minQty," + 
				"storeItemMapper.maxQty as maxQty," + 
				"storeItemMapper.isActive as status," + 
				"store.storeId as storeId," + 
				"store.storeName as storeName," + 
				"coalesce(productCategory1.id, productCategory2.id) as productCategoryId," + 
				"coalesce(productCategory1.category, productCategory2.category) as productCategory," + 
				"coalesce(assetCategory1.id, assetCategory2.id) as assetCategoryId," + 
				"coalesce(assetCategory1.category, assetCategory2.category) as assetCategory, " + 
				"item.id as itemId, " + 
				"item.name as itemName, " + 
				"item.code as itemCode " +
				"from StoreItemMapper as storeItemMapper " + 
				" left join storeItemMapper.store as store " + 
				"left join storeItemMapper.item as item " + 
				"left join item.generic as generic " + 
				"left join generic.productCategory as productCategory1 " + 
				"left join item.productCategory as productCategory2 " + 
				"left join productCategory1.assetCategory as assetCategory1 " + 
				"left join productCategory2.assetCategory as assetCategory2 " + 
				"where " + 
				"storeItemMapper.isDeleted = false " + 
				"and store.isDeleted = false " + 
				"and generic.isDeleted = false " + 
				"and productCategory1.isDeleted = false " + 
				"and assetCategory1.isDeleted = false " + 
				"and store.storeId = :storeId";

		List<StoreItemMapperListResponse> list = getSession().createQuery(hql)
				.setParameter("storeId", storeId)
				.setResultTransformer(Transformers.aliasToBean(StoreItemMapperListResponse.class))
				.list();
		return list;
		
	}

	@Override
	public CommonResponse GetMultipleStore(StoreItemMapperListResponse request) {

		
		
		String hql = "select " + 
				"storeItemMapper.id as id," + 
				"storeItemMapper.minQty as minQty," + 
				"storeItemMapper.maxQty as maxQty," + 
				"storeItemMapper.isActive as status," + 
				"store.storeId as storeId," + 
				"store.storeName as storeName," + 
				"coalesce(productCategory1.id, productCategory2.id) as productCategoryId," + 
				"coalesce(productCategory1.category, productCategory2.category) as productCategory," + 
				"coalesce(assetCategory1.id, assetCategory2.id) as assetCategoryId," + 
				"coalesce(assetCategory1.category, assetCategory2.category) as assetCategory, " + 
				"item.id as itemId, " + 
				"item.name as itemName, " + 
				"item.code as itemCode " +
				" from StoreItemMapper as storeItemMapper " + 
				" left join storeItemMapper.store as store " + 
				"left join storeItemMapper.item as item " + 
				"left join item.generic as generic " + 
				"left join generic.productCategory as productCategory1 " + 
				"left join item.productCategory as productCategory2 " + 
				"left join productCategory1.assetCategory as assetCategory1 " + 
				"left join productCategory2.assetCategory as assetCategory2 " + 
				"where " + 
				"storeItemMapper.isDeleted = false " +
				" AND  storeItemMapper.isActive=true " +
				"and store.isDeleted = false " + 
				"and store.storeId  in ";
		     StringBuilder appQuery =  new StringBuilder(" ( ");
		     if(request.getMultipleStoreRequest() != null && request.getMultipleStoreRequest().size()>0) {
		    	 for(MultipleStoreRequest r : request.getMultipleStoreRequest()) {
			    	 
			    	 appQuery.append(r.getId() +", ");
			     }
		     }
		    
		     appQuery.append(" 0 ) ");
		     
		     appQuery.append(" order by storeItemMapper.id asc ");
		     
		     hql = hql+appQuery.toString();
		List<StoreItemMapperListResponse> list = getSession().createQuery(hql)
				.setMaxResults(request.getNoOfRecordsPerPage())
				.setFirstResult(request.getOffset())
				//.setParameter("storeId", storeId)
				.setResultTransformer(Transformers.aliasToBean(StoreItemMapperListResponse.class))
				.list();
		
		List<StoreItemMapperListResponse> list1 = getSession().createQuery(hql)
				.setResultTransformer(Transformers.aliasToBean(StoreItemMapperListResponse.class))
				.list();
		
		Integer count = 0;
		
		if(list1!= null && list1.size()>0) {
			count = list1.size();
		}
		
		CommonResponse res = new CommonResponse();
		res.setListObject(list);
		res.setObject(count);
		return res;
		
	
	}

	@Override
	public List<StoreStockResponse> checkStock(StoreStockRequestDate request) {


		String isConsignmentCondation = request.getIsConsignment() != null
				? " and item.is_consignment=" + request.getIsConsignment() + " "
				: " ";

		String sql = "select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	 coalesce(stock.totalQty, 0) as currentStock,   case  when coalesce(stock.totalTransitQty, 0) >=0 then coalesce(stock.totalTransitQty, 0)  else   0  end as stockInTransit , "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_generic generic on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				+ " 	inner join public.m_product_category productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner join  public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId and batch.added_date <=:added_date and (batch.expiry_date >= current_date  or batch.expiry_date is null)  ) "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true)  and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false  and storeIndent.unit_id=:unitId  "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				// + " where item.is_active=true and item.is_deleted=false and
				// item.is_consignment=:isConsignment and stock.totalQty >0 "

				// start code by minesh for consignment(flag remove) stock condation for
				// storestock for resolved HIS-8544
				+ " where item.is_active=true and item.is_deleted=false  " + isConsignmentCondation
				+ " and stock.totalQty >0 "
				// end code by minesh for consignment(flag remove) stock condation for
				// storestock for resolved HIS-8544

				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				//+ " 	and (item.added_date <=:added_date) "

				+ " 	and (:storeId=0 or :vedId=0 or generic.id in (select generic.id "
				+ " 										from public.m_storewise_generic storewisGeneric "
				+ " 											inner join public.m_generic generic on storewisGeneric.generic_id=generic.id "
				+ " 												and (generic.is_active=true and generic.is_deleted=false) "
				+ " 											inner join public.m_ved ved on storewisGeneric.ved_id=ved.id "
				+ " 												and (ved.id=:vedId and ved.is_active=true and ved.is_deleted=false) "
				+ " 											inner join public.m_store store on storewisGeneric.store_id=store.store_id "
				+ " 												and (store.store_id=:storeId and store.is_active=true and store.is_deleted=false))) "
				+ " 	and (:storeId=0 or (productCategory.id in (select storePcMapper.product_category_id "
				+ " 									from public.m_store_pc_mapper storePcMapper "
				+ " 									where storePcMapper.store_id=:storeId)) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId))) "
				// + " order by item.id";

				+ " union"

				+ " select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	coalesce(stock.totalQty, 0) as currentStock, case  when coalesce(stock.totalTransitQty, 0) >=0 then coalesce(stock.totalTransitQty, 0)  else   0  end as stockInTransit , "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				// + " left join public.m_generic generic on item.generic_id=generic.id and
				// (generic.is_deleted=false and generic.is_active=true) "
				+ " 	inner join public.m_product_category productCategory on item.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner  join public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId and batch.added_date <=:added_date    ) "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true) and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false and storeIndent.unit_id=:unitId "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				// + " where item.is_active=true and item.is_deleted=false and
				// item.is_consignment=:isConsignment and stock.totalQty >0 "

				// start code by minesh for consignment stock condation for storestock for
				// resolved HIS-8544
				+ " where item.is_active=true and item.is_deleted=false " + isConsignmentCondation
				+ " and stock.totalQty >0 "
				// end code by minesh for consignment stock condation for storestock for
				// resolved HIS-8544

				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				// + " and (item.added_date <=:added_date) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId)) ";
		// + " order by id";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemCode").addScalar("itemName")
				.addScalar("assetCategory").addScalar("productCategory").addScalar("minimumStock")
				.addScalar("currentStock", StandardBasicTypes.LONG).addScalar("stockInTransit", StandardBasicTypes.LONG)
				.addScalar("pendingIndentQuantity", StandardBasicTypes.LONG);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("vedId", request.getVedId() != null ? request.getVedId() : 0);
		// query.setParameter("isConsignment", request.getIsConsignment() != null ?
		// request.getIsConsignment() : false);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("added_date", request.getFromDate());
		query.setParameter("organizationId", sessionService.getOrganizationId());
		query.setParameter("unitId", sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(StoreStockResponse.class));

		return query.list();

	
	}

	@Override
	public List<StoreItemMapperListResponse> getStoreItemMappedList(Integer itemId) {

		
		
		String hql = "select " + 
				"storeItemMapper.id as id," + 
				"storeItemMapper.minQty as minQty," + 
				"storeItemMapper.maxQty as maxQty," + 
				"storeItemMapper.isActive as status," + 
				"store.storeId as storeId," + 
				"store.storeName as storeName," + 
				"coalesce(productCategory1.id, productCategory2.id) as productCategoryId," + 
				"coalesce(productCategory1.category, productCategory2.category) as productCategory," + 
				"coalesce(assetCategory1.id, assetCategory2.id) as assetCategoryId," + 
				"coalesce(assetCategory1.category, assetCategory2.category) as assetCategory, " + 
				"item.id as itemId, " + 
				"item.name as itemName, " + 
				"item.code as itemCode " +
				"from StoreItemMapper as storeItemMapper " + 
				"  join storeItemMapper.store as store " + 
				" join storeItemMapper.item as item " + 
				"left join item.generic as generic " + 
				"left join generic.productCategory as productCategory1 " + 
				"left join item.productCategory as productCategory2 " + 
				"left join productCategory1.assetCategory as assetCategory1 " + 
				"left join productCategory2.assetCategory as assetCategory2 " + 
				"where " + 
				"storeItemMapper.isDeleted = false " + 
				"and store.isDeleted = false " + 
				"and generic.isDeleted = false " + 
				"and productCategory1.isDeleted = false " + 
				"and assetCategory1.isDeleted = false " + 
				"and item.id = :id";

		List<StoreItemMapperListResponse> list = getSession().createQuery(hql)
				.setParameter("id", itemId)
				.setResultTransformer(Transformers.aliasToBean(StoreItemMapperListResponse.class))
				.list();
		return list;
		
	
	}

	@Override
	public List<StoreItemMapperListResponse> getStoreItemMappedListForItemMasterNew(Integer id, Integer unitId) {


		
		
		String hql = "select " + 
				"storeItemMapper.id as id," + 
				"storeItemMapper.minQty as minQty," + 
				"storeItemMapper.maxQty as maxQty," + 
				"storeItemMapper.isActive as status," + 
				"store.storeId as storeId," + 
				"store.storeName as storeName," + 
				"coalesce(productCategory1.id, productCategory2.id) as productCategoryId," + 
				"coalesce(productCategory1.category, productCategory2.category) as productCategory," + 
				"coalesce(assetCategory1.id, assetCategory2.id) as assetCategoryId," + 
				"coalesce(assetCategory1.category, assetCategory2.category) as assetCategory, " + 
				"item.id as itemId, " + 
				"item.name as itemName, " + 
				"item.code as itemCode " +
				"from StoreItemMapper as storeItemMapper " + 
				"  join storeItemMapper.store as store " + 
				" join storeItemMapper.item as item " + 
				"left join item.generic as generic " + 
				"left join generic.productCategory as productCategory1 " + 
				"left join item.productCategory as productCategory2 " + 
				"left join productCategory1.assetCategory as assetCategory1 " + 
				"left join productCategory2.assetCategory as assetCategory2 " + 
				"where " + 
				" storeItemMapper.isDeleted = false " + 
				" and store.isDeleted = false " + 
				" and generic.isDeleted = false "
				+ " and storeItemMapper.isActive = true " + 
				" and productCategory1.isDeleted = false " + 
				" and assetCategory1.isDeleted = false "
				+ " and item.unitId = :unitId " + 
				" and item.id = :id";

		List<StoreItemMapperListResponse> list = getSession().createQuery(hql)
				.setParameter("id", id)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(StoreItemMapperListResponse.class))
				.list();
		return list;
		
	
	}

	@Override
	public List<StoreItemMapperListResponse> GetTransactionCount(List<MultipleStoreRequest> request,
			BigInteger itemId) {
	        String sql="Select cast(count(*) as int ) as \"transactionCount\",tst.store_id as \"storeId\" from inventory.t_stock_transaction tst where item_id="+itemId+"  and unit_id="+sessionService.getUnitId()+" and store_id in ";
	        StringBuilder appQuery =  new StringBuilder(" ( ");
		     for(MultipleStoreRequest r : request) {
		    	 
		    	 appQuery.append(r.getId() +", ");
		     }
		     appQuery.append(" 0 )");
		     sql = sql+appQuery.toString()+"group by tst.store_id";
		     Query query = getSession().createSQLQuery(sql);
	        return (List<StoreItemMapperListResponse>) query.list();
	}


}
