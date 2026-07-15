package com.param.pharmacy.master.global.dao.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.ItemResponse;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.MultiItemsRequest;
import com.param.global.otims.dto.BatchDto;
import com.param.global.otims.dto.UomDto;
import com.param.pharmacy.master.global.dto.item.ItemRequest;
import com.param.pharmacy.master.global.dto.item.ItemSearchByNameResponse;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
@Repository("ItemDao")
public class ItemDao extends AbstractDao<Integer, Item> implements IItemDao, ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;

	@Autowired
	private SessionService sessionService;
	
	final static Logger logger = Logger.getLogger(ItemDao.class);

	@Override
	public List<Item> getAll() {
		Criteria criteria = getSession().createCriteria(Item.class).createAlias("organization", "organization");
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId", sessionService.getOrganizationId()));

		Criteria formulationTypeCriteria = criteria.createCriteria("formulationType");
		formulationTypeCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria genericCriteria = criteria.createCriteria("generic");
		genericCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria productCategoryCriteria = genericCriteria.createCriteria("productCategory");
		genericCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetCategoryCriteria = productCategoryCriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetTypeCriteria = assetCategoryCriteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria storageUnitCriteria = criteria.createCriteria("storageUnit");
		storageUnitCriteria.add(Restrictions.eq("isDeleted", false));

	

		return criteria.list();

	}

	@Override
	public Item getById(Integer id, Integer orgId) {
		Criteria criteria = getSession().createCriteria(Item.class).createAlias("organization", "organization");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));
		return (Item) criteria.uniqueResult();
	}

	@Override
	public ItemRequestDetail getItemRequestDetail(Integer id) {

		String hql = "SELECT itemRequestDetail FROM com.param.entity.model.procurement.ItemRequestDetail AS itemRequestDetail "
				+ "WHERE itemRequestDetail.id =:id";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (com.param.entity.model.procurement.ItemRequestDetail) query.uniqueResult();

	}


	@Override
	public Integer updateItemRequestDetail(ItemRequestDetail itemRequestDetail) {
		getSession().update(itemRequestDetail);
		return itemRequestDetail.getId();
	}

	@Override
	public Integer create(Item item) {
		getSession().save(item);
		return item.getId();
	}

	@Override
	public Integer update(Item item) {
		getSession().update(item);
		return item.getId();
	}

	@Override
	public FormulationType getFormulationtype(Integer formulationTypeId) {
		return (FormulationType) getSession().get(FormulationType.class, formulationTypeId);
	}

	@Override
	public Generic getGeneric(Integer genericId) {
		return (Generic) getSession().get(Generic.class, genericId);
	}

	@Override
	public Manufacturer getManufacturer(Integer genericId) {
		return (Manufacturer) getSession().get(Manufacturer.class, genericId);
	}

	@Override
	public StrengthUnit getStrengthUnit(Integer strengthUnitId) {
		return (StrengthUnit) getSession().get(StrengthUnit.class, strengthUnitId);
	}

	@Override
	public PharmacologicalClassification getPharmacologicalClassification(Integer pharmacologicalClassificationId) {
		return (PharmacologicalClassification) getSession().get(PharmacologicalClassification.class,
				pharmacologicalClassificationId);
	}

	@Override
	public Tax getTax(Integer id) {
		return (Tax) getSession().get(Tax.class, id);
	}

	@Override
	public StorageUnit getStorageUnit(Integer storageUnitId) {
		return (StorageUnit) getSession().get(StorageUnit.class, storageUnitId);
	}

	@Override
	public Integer getRouteOfAdministratorByFormulationType(Integer formulationTypeId) {
		String hql = "SELECT routeOfAdministration.id AS id " + " FROM Item AS item "
				+ " INNER JOIN item.formulationType AS formulationType "
				+ " INNER JOIN formulationType.formulationAndRoaList AS formulationAndRoaList "
				+ " INNER JOIN formulationAndRoaList.routeOfAdministration AS routeOfAdministration "
				+ " WHERE ( formulationType.id = :formulationTypeId OR :formulationTypeId = 0 ) ";
		Query query = getSession().createQuery(hql);
		query.setParameter("formulationTypeId", formulationTypeId);
		return (Integer) query.uniqueResult();
	}

	@Override
	public List<com.param.pharmacy.master.global.dto.item.ItemListResponse> itemsList(ItemsRequest itemsRequest) {
		
		String hql = "select	item0_.id as itemId," + 
					"	assetcateg3_.category as assetCategory," + 
					"	productcat2_.category as productCategory," + 
					"	item0_.code as itemCode," + 
					"	item0_.name as itemName" + 
					"	from" + 
					"	public.m_item item0_" + 
					"	inner join public.m_generic generic1_ on	item0_.generic_id = generic1_.id	and(generic1_.is_deleted = false	and generic1_.is_active = true)" + 
					"	inner join public.m_product_category productcat2_ on	generic1_.product_category_id = productcat2_.id	and(productcat2_.is_deleted = false	and productcat2_.is_active = true)" + 
					"	inner join public.m_asset_category assetcateg3_ on productcat2_.asset_category_id = assetcateg3_.id	and(assetcateg3_.is_deleted = false	and assetcateg3_.is_active = true)" + 
					"	inner join public.m_asset_type assettype4_ on	assetcateg3_.asset_type_id = assettype4_.id	and(assettype4_.is_deleted = false and assettype4_.is_active = true)" + 
					"	inner join public.m_manufacturer manufactur5_ on item0_.manufacturer_id = manufactur5_.id	and(manufactur5_.is_deleted = false	and manufactur5_.is_active = true)" + 
					"	inner join public.m_tax tax6_ on	item0_.p_tax_id = tax6_.id	and(tax6_.is_deleted = false and tax6_.is_active = true)" + 
					"	where" + 
					"	item0_.is_deleted = false" + 
					"	and item0_.is_active = true" + 
					"	and item0_.organization_id =:organizationId" + 
					"	and(:consignment = 0 OR  item0_.is_consignment =:isConsignment)" + 
					"	and(:itemCode = '' OR  upper( item0_.code ) like upper(:itemCode))" + 
					"	and(:itemName = '' OR   upper( item0_.name ) like upper(:itemName))" + 
					"	and(:assetTypeId = 0 OR  assettype4_.id =:assetTypeId)" + 
					"	and(:assetCategoryId = 0 OR  assetcateg3_.id =:assetCategoryId)" + 
					"	and(:productCategoryId = 0 OR  productcat2_.id =:productCategoryId)" + 
					"	and((:fromStoreId = 0 OR :vedId = 0) OR " + 
					"		 generic1_.id in(select	generic8_.id from	public.m_storewise_generic storewiseg7_" + 
					"			inner join public.m_generic generic8_ on	storewiseg7_.generic_id = generic8_.id and(	generic8_.is_active = true and generic8_.is_deleted = false)" + 
					"			inner join public.m_ved ved9_ on storewiseg7_.ved_id = ved9_.id and(ved9_.id =:vedId and ved9_.is_active = true and ved9_.is_deleted = false	)" + 
					"			inner join public.m_store store10_ on" + 
					"				storewiseg7_.store_id = store10_.store_id" + 
					"				and(store10_.store_id =:fromStoreId	and store10_.is_active = true	and store10_.is_deleted = false	)" + 
					"		)" + 
					"	)" + 
					"	and(:fromStoreId = 0 OR (" + 
					"			productcat2_.id in(select storeprodu11_.product_category_id	from	public.m_store_pc_mapper storeprodu11_	where	storeprodu11_.store_id =:fromStoreId)" + 
					"		)" + 
					"		and(" + 
					"			item0_.id not in(" + 
					"				select storeitemr12_.item_id from	public.m_store_item_restriction storeitemr12_ where	storeitemr12_.store_id =:fromStoreId and storeitemr12_.is_deleted = false and storeitemr12_.is_active = true" + 
					"			)" + 
					"		)" + 
					"	)" + 
					"	and( :toStoreId = 0 OR  " + 
					"		(" + 
					"			productcat2_.id in(" + 
					"				select storeprodu13_.product_category_id from	public.m_store_pc_mapper storeprodu13_	where storeprodu13_.store_id =:toStoreId" + 
					"			)" + 
					"		)" + 
					"		and(" + 
					"			item0_.id not in(" + 
					"				select storeitemr14_.item_id from public.m_store_item_restriction storeitemr14_ where	storeitemr14_.store_id =:toStoreId	and storeitemr14_.is_deleted = false	and storeitemr14_.is_active = true" + 
					"			)" + 
					"		)" + 
					"	)" + 
					"	and(:storeId = 0 OR (" + 
					"			productcat2_.id in(select storeprodu11_.product_category_id	from	public.m_store_pc_mapper storeprodu11_	where	storeprodu11_.store_id =:storeId)" + 
					"		)" + 
					"		and(" + 
					"			item0_.id not in(" + 
					"				select storeitemr12_.item_id from	public.m_store_item_restriction storeitemr12_ where	storeitemr12_.store_id =:storeId and storeitemr12_.is_deleted = false and storeitemr12_.is_active = true" + 
					"			)" + 
					"		)" + 
					"	) \r\n" + 
					
					"	union \r\n" +
					
					"select	item0_.id as itemId," + 
					"	assetcateg3_.category as assetCategory," + 
					"	productcat2_.category as productCategory," + 
					"	item0_.code as itemCode," + 
					"	item0_.name as itemName" + 
					"	from" + 
					"	public.m_item item0_" + 
					"	inner join public.m_product_category productcat2_ on	item0_.product_category_id = productcat2_.id	and(productcat2_.is_deleted = false	and productcat2_.is_active = true)" + 
					"	inner join public.m_asset_category assetcateg3_ on productcat2_.asset_category_id = assetcateg3_.id	and(assetcateg3_.is_deleted = false	and assetcateg3_.is_active = true)" + 
					"	inner join public.m_asset_type assettype4_ on	assetcateg3_.asset_type_id = assettype4_.id	and(assettype4_.is_deleted = false and assettype4_.is_active = true)" + 
					"	inner join public.m_manufacturer manufactur5_ on item0_.manufacturer_id = manufactur5_.id	and(manufactur5_.is_deleted = false	and manufactur5_.is_active = true)" + 
					"	inner join public.m_tax tax6_ on	item0_.p_tax_id = tax6_.id	and(tax6_.is_deleted = false and tax6_.is_active = true)" + 
					"	where" + 
					"	item0_.is_deleted = false" + 
					"	and item0_.is_active = true" + 
					"	and item0_.organization_id =:organizationId" + 
					"	and(:consignment = 0 OR  item0_.is_consignment =:isConsignment)" + 
					"	and(:itemCode = '' OR  upper( item0_.code ) like upper(:itemCode))" + 
					"	and(:itemName = '' OR   upper( item0_.name ) like upper(:itemName))" + 
					"	and(:assetTypeId = 0 OR  assettype4_.id =:assetTypeId)" + 
					"	and(:assetCategoryId = 0 OR  assetcateg3_.id =:assetCategoryId)" + 
					"	and(:productCategoryId = 0 OR  productcat2_.id =:productCategoryId)" + 
					"	and(:fromStoreId = 0 OR (" + 
					"			productcat2_.id in(select storeprodu11_.product_category_id	from	public.m_store_pc_mapper storeprodu11_	where	storeprodu11_.store_id =:fromStoreId)" + 
					"		)" + 
					"		and(" + 
					"			item0_.id not in(" + 
					"				select storeitemr12_.item_id from	public.m_store_item_restriction storeitemr12_ where	storeitemr12_.store_id =:fromStoreId and storeitemr12_.is_deleted = false and storeitemr12_.is_active = true" + 
					"			)" + 
					"		)" + 
					"	)" + 
					"	and( :toStoreId = 0 OR  " + 
					"		(" + 
					"			productcat2_.id in(" + 
					"				select storeprodu13_.product_category_id from	public.m_store_pc_mapper storeprodu13_	where storeprodu13_.store_id =:toStoreId" + 
					"			)" + 
					"		)" + 
					"		and(" + 
					"			item0_.id not in(" + 
					"				select storeitemr14_.item_id from public.m_store_item_restriction storeitemr14_ where	storeitemr14_.store_id =:toStoreId	and storeitemr14_.is_deleted = false	and storeitemr14_.is_active = true" + 
					"			)" + 
					"		)" + 
					"	)" + 
					"	and(:storeId = 0 OR (" + 
					"			productcat2_.id in(select storeprodu11_.product_category_id	from	public.m_store_pc_mapper storeprodu11_	where	storeprodu11_.store_id =:storeId)" + 
					"		)" + 
					"		and(" + 
					"			item0_.id not in(" + 
					"				select storeitemr12_.item_id from	public.m_store_item_restriction storeitemr12_ where	storeitemr12_.store_id =:storeId and storeitemr12_.is_deleted = false and storeitemr12_.is_active = true" + 
					"			)" + 
					"		)" + 
					"	)" ;
		
		
		Query query = getSession().createSQLQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? '%'+itemsRequest.getItemCode().toUpperCase() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? '%'+itemsRequest.getItemName().toUpperCase() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ? itemsRequest.getFromStoreId() : 0);
		query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameter("vedId", itemsRequest.getVedId() != null ? itemsRequest.getVedId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("organizationId", itemsRequest.getOrganizationId()!=null ? itemsRequest.getOrganizationId() : sessionService.getOrganizationId());
		query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);

		query.setResultTransformer(Transformers.aliasToBean(com.param.pharmacy.master.global.dto.item.ItemListResponse.class));

		return query.list();
	}

	@Override
	public List<com.param.pharmacy.master.global.dto.item.ItemListResponse> assetItemsList(ItemsRequest itemsRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.organization.organizationId = :organizationId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
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
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
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
		query.setParameter("organizationId", itemsRequest.getOrganizationId()!=null ? itemsRequest.getOrganizationId() : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	}

	@Override
	public Long getgenericCount(Integer id) {
		String hql = " SELECT count(generic) from Generic as generic "
				+ "INNER JOIN generic.itemsList As item WITH item.id= :id AND item.isActive = false AND item.isDeleted = false AND item.organization.organizationId=:organizationId "
				+ "WHERE generic.isActive = false AND generic.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getstoreWiseCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.generic as generic WITH generic.isActive = true AND generic.isDeleted = false "
				+ " INNER JOIN generic.storewiseGenericList as storewiseGeneric WITH storewiseGeneric.isActive = false AND storewiseGeneric.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getproductCategoryCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.productCategory as productCategory WITH productCategory.isActive = false AND productCategory.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getformulationTypeCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.formulationType as formulationType WITH formulationType.isActive = false AND formulationType.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getmanufactoreCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.manufacturer as manufacturer WITH manufacturer.isActive = false AND manufacturer.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getstrengthUnitCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.strengthUnit as strengthUnit WITH strengthUnit.isActive = false AND strengthUnit.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false "
				+ " AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long gettaxCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.salesTax as salesTax WITH salesTax.isActive = false AND salesTax.isDeleted = false "
				+ " INNER JOIN item.purchaseTax as purchaseTax WITH purchaseTax.isActive = false AND purchaseTax.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false "
				+ " AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getstorageUnitCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.storageUnit as storageUnit WITH storageUnit.isActive = false AND storageUnit.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false "
				+ " AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public List<com.param.pharmacy.master.global.dto.item.ItemListResponse> getIsEnabledVaccinationList() {
	
		String query = "SELECT id as id, name as name FROM Item where isVaccin=true and organization.organizationId =:organizationId"
				+ " ORDER BY name ASC ";
		List<com.param.pharmacy.master.global.dto.item.ItemListResponse> itemList = sessionFactory.getCurrentSession()
				.createQuery(query)
				.setParameter("organizationId", sessionService.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(com.param.pharmacy.master.global.dto.item.ItemListResponse.class))
				.list();

		return itemList;
	}

	@Override
	public List<ItemSearchByNameResponse> getItemSearchForAutoComplete(String name) {

		String hql = "SELECT item.id AS itemId, item.code AS itemCode, item.name AS itemName, CONCAT(item.code,'|', item.name) AS detail FROM Item as item "
				+ " WHERE (lower(item.name) LIKE :name OR ( lower(item.code) LIKE :name)) "
				+ " AND item.isActive = true AND item.isDeleted = false AND item.isImplant = false  AND item.organization.organizationId =:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("organizationId", sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ItemSearchByNameResponse.class));

		return (List<ItemSearchByNameResponse>) query.list();

	}

	@Override
	public List<com.param.pharmacy.master.global.dto.item.ItemListResponse> getAllItems(Integer orgId,Integer offset,Integer noOfRecordsPerPage,String searchKeyward) throws Throwable {

		String hql = "select items.id as id, items.code as code, items.name as name,"
				+ "items.productCode as productCode, items.isActive as status,"
				+ "items.strength as strength, items.isReusable as isReusable,"
				+ "items.isVaccin as isVaccine, items.isOtc as isOtc,  " + "items.isCombination as isCombination,   "
				+ "items.isBatchRequired as isBatchRequired,   " + "items.isHighRiskProduct as isHighrisk,   "
				+ "items.isPsychotropic as isPsychotropic, items.isKit as isKit,  "
				+ "items.isInfusion as isInfusion,   " + "items.isNarcotic as isNarcotic,   "
				+ "items.hsnCode as hsnCode, items.isHospitalFormulary as isHospitalFormulary,   "
				+ "items.isConsumable as isConsumable,   " + "items.isImplant as isImplant,   "
				+ "items.isSterilize as isSterilize, items.isConsignment as isConsignment,  "
				+ "formulationType.id as formulationTypeId, formulationType.type as formulationType, generic.id as genericId,   "
				+ "generic.name as genericName, "
				+ "	productCategory.id as productCategoryId, productCategory.category as productCategory,  "
				+ "	assetCategory.id as assetCategoryId,assetCategory.category as assetCategory, "
				+ " assetType.id as assetTypeId, assetType.type as assetType,  "
				+ "	strengthUnit.id as strengthUnitId, strengthUnit.units as strengthUnit, purchaseTax.name as purchaseTax,   "
				+ "	salesTax.name as saleTax,   "
				+ "	pharmacologicalClassification.classification as pharmalogicalClassification, manufacturer.name as manufacturer, "
				+ "storageUnit.unitName as storageUnitName ,"
				+ " items.isEmergencyDrug as isEmergencyDrug,  "
				+ " items.isLasa as isLasa  "
				+ " from Item as items  "
				+ "	left join items.formulationType as formulationType  " + "	left join items.generic as generic  "
				+ "	left join items.strengthUnit as strengthUnit  " + "	left join items.purchaseTax as purchaseTax  "
				+ "	left join items.salesTax as salesTax   "
				+ "	left join items.pharmacologicalClassification as pharmacologicalClassification  "
				+ "	left join items.storageUnit as storageUnit "
				+ "	left join generic.productCategory as productCategory  "
				+ "	inner join items.manufacturer as manufacturer  "
				+ "	inner join productCategory.assetCategory as assetCategory  "
				+ "	inner join assetCategory.assetType as assetType   "
				+ "where formulationType.isDeleted = false and generic.isDeleted = false and items.isDeleted = false and items.organization.organizationId = :orgId and productCategory.isDeleted = false and assetCategory.isDeleted = false "
				+ "	and assetType.isDeleted = false and storageUnit.isDeleted = false";

		List<com.param.pharmacy.master.global.dto.item.ItemListResponse> unitSpecialtyMapperDtosList=null;
		String roughQuery= sessionFactory.getCurrentSession().createQuery(hql).getQueryString();
		StringBuilder stringBuilderQuery = new StringBuilder(roughQuery);
		if(!searchKeyward.equals("null") && !searchKeyward.equalsIgnoreCase("") && searchKeyward.length()>0)
		{
			stringBuilderQuery.append(" and "
									+"(LOWER(items.name) like '%"+searchKeyward.toLowerCase()+"%'"
									+" or LOWER(items.productCode) like '%"+searchKeyward.toLowerCase()+"%'"
									+" or LOWER(generic.name) like '%"+searchKeyward.toLowerCase()+"%'"
									+" or LOWER(assetType.type) like '%"+searchKeyward.toLowerCase()+"%'"
									+" or LOWER(assetCategory.category) like '%"+searchKeyward.toLowerCase()+"%'"
									+" or LOWER(productCategory.category) like '%"+searchKeyward.toLowerCase()+"%'"
									+" or LOWER(formulationType.type) like '%"+searchKeyward.toLowerCase()+"%'"
									+" or LOWER(cast(items.strength as string)) like '%"+searchKeyward.toLowerCase()+"%')"
			);
		}

		try {
			List<com.param.pharmacy.master.global.dto.item.ItemListResponse> list = sessionFactory.getCurrentSession()
					.createQuery(stringBuilderQuery.toString())
					.setParameter("orgId", orgId!=null ? orgId : sessionService.getOrganizationId())
					.setFirstResult(offset!=null ? offset:0)
					.setMaxResults(noOfRecordsPerPage!=null ? noOfRecordsPerPage:100)
					.setResultTransformer(
							Transformers.aliasToBean(com.param.pharmacy.master.global.dto.item.ItemListResponse.class))
					.list();

			return list;

		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public List<com.param.pharmacy.master.global.dto.item.ItemListResponse> MultiItemsList(
			MultiItemsRequest itemsRequest) {

		String hql = "select    item0_.id as itemId," + 
                "    assetcateg3_.category as assetCategory," + 
                "    productcat2_.category as productCategory," + 
                "    item0_.code as itemCode," + 
                "    item0_.name as itemName" + 
                "    from" + 
                "    public.m_item item0_" + 
                "    inner join public.m_generic generic1_ on    item0_.generic_id = generic1_.id    and(generic1_.is_deleted = false    and generic1_.is_active = true)" + 
                "    inner join public.m_product_category productcat2_ on    generic1_.product_category_id = productcat2_.id    and(productcat2_.is_deleted = false    and productcat2_.is_active = true)" + 
                "    inner join public.m_asset_category assetcateg3_ on productcat2_.asset_category_id = assetcateg3_.id    and(assetcateg3_.is_deleted = false    and assetcateg3_.is_active = true)" + 
                "    inner join public.m_asset_type assettype4_ on    assetcateg3_.asset_type_id = assettype4_.id    and(assettype4_.is_deleted = false and assettype4_.is_active = true)" + 
                "    inner join public.m_manufacturer manufactur5_ on item0_.manufacturer_id = manufactur5_.id    and(manufactur5_.is_deleted = false    and manufactur5_.is_active = true)" + 
                "    inner join public.m_tax tax6_ on    item0_.p_tax_id = tax6_.id    and(tax6_.is_deleted = false and tax6_.is_active = true)" + 
                "    where" + 
                "    item0_.is_deleted = false" + 
                "    and item0_.is_active = true" + 
                "    and item0_.organization_id =:organizationId" + 
                "    and(:consignment = 0 OR  item0_.is_consignment =:isConsignment)" + 
                "    and(:itemCode = '' OR  upper( item0_.code ) like upper(:itemCode))" + 
                "    and(:itemName = '' OR   upper( item0_.name ) like upper(:itemName))" + 
                "    and(:assetTypeId = 0 OR  assettype4_.id =:assetTypeId)" + 
                "    and(:assetCategoryId = 0 OR  assetcateg3_.id =:assetCategoryId)" + 
                "    and(:productCategoryId = 0 OR  productcat2_.id =:productCategoryId)" + 
                "	and(( :vedId = 0) OR " + 
                "         generic1_.id in(" + 
                "            select" + 
                "                generic8_.id" + 
                "            from" + 
                "                public.m_storewise_generic storewiseg7_" + 
                "            inner join public.m_generic generic8_ on" + 
                "                storewiseg7_.generic_id = generic8_.id" + 
                "                and(" + 
                "                    generic8_.is_active = true" + 
                "                    and generic8_.is_deleted = false" + 
                "                )" + 
                "            inner join public.m_ved ved9_ on" + 
                "                storewiseg7_.ved_id = ved9_.id" + 
                "                and(" + 
                "                    ved9_.is_active = true" + 
                "                    and ved9_.is_deleted = false" + 
                "                )" + 
                "            inner join public.m_store store10_ on" + 
                "                storewiseg7_.store_id = store10_.store_id" + 
                "                and(" + 
                "                    (" + 
                "                        store10_.store_id in(:StoreIds)" + 
                "                    )" + 
                "                    and store10_.is_active = true" + 
                "                    and store10_.is_deleted = false" + 
                "                )" + 
                "        )" + 
                "    )" +
                "    and((" + 
                "            productcat2_.id in(select storeprodu11_.product_category_id    from    public.m_store_pc_mapper storeprodu11_    where    storeprodu11_.store_id in (:StoreIds))" + 
                "        )" + 
                "        and(" + 
                "            item0_.id not in(" + 
                "                select storeitemr12_.item_id from    public.m_store_item_restriction storeitemr12_ where    storeitemr12_.store_id in (:StoreIds) and storeitemr12_.is_deleted = false and storeitemr12_.is_active = true" + 
                "            )" + 
                "        )" + 
                "    ) " + 
                
                "    union " +
                
                "	select    item0_.id as itemId," + 
                "    assetcateg3_.category as assetCategory," + 
                "    productcat2_.category as productCategory," + 
                "    item0_.code as itemCode," + 
                "    item0_.name as itemName" + 
                "    from" + 
                "    public.m_item item0_" + 
                "    inner join public.m_product_category productcat2_ on    item0_.product_category_id = productcat2_.id    and(productcat2_.is_deleted = false    and productcat2_.is_active = true)" + 
                "    inner join public.m_asset_category assetcateg3_ on productcat2_.asset_category_id = assetcateg3_.id    and(assetcateg3_.is_deleted = false    and assetcateg3_.is_active = true)" + 
                "    inner join public.m_asset_type assettype4_ on    assetcateg3_.asset_type_id = assettype4_.id    and(assettype4_.is_deleted = false and assettype4_.is_active = true)" + 
                "    inner join public.m_manufacturer manufactur5_ on item0_.manufacturer_id = manufactur5_.id    and(manufactur5_.is_deleted = false    and manufactur5_.is_active = true)" + 
                "    inner join public.m_tax tax6_ on    item0_.p_tax_id = tax6_.id    and(tax6_.is_deleted = false and tax6_.is_active = true)" + 
                "    where" + 
                "    item0_.is_deleted = false" + 
                "    and item0_.is_active = true" + 
                "    and item0_.organization_id =:organizationId" + 
                "    and(:consignment = 0 OR  item0_.is_consignment =:isConsignment)" + 
                "    and(:itemCode = '' OR  upper( item0_.code ) like upper(:itemCode))" + 
                "    and(:itemName = '' OR   upper( item0_.name ) like upper(:itemName))" + 
                "    and(:assetTypeId = 0 OR  assettype4_.id =:assetTypeId)" + 
                "    and(:assetCategoryId = 0 OR  assetcateg3_.id =:assetCategoryId)" + 
                "    and(:productCategoryId = 0 OR  productcat2_.id =:productCategoryId)" + 
                "    and( (" + 
                "            productcat2_.id in(select storeprodu11_.product_category_id    from    public.m_store_pc_mapper storeprodu11_    where    storeprodu11_.store_id in (:StoreIds))" + 
                "        )" + 
                "        and(" + 
                "            item0_.id not in(" + 
                "                select storeitemr12_.item_id from    public.m_store_item_restriction storeitemr12_ where    storeitemr12_.store_id in (:StoreIds) and storeitemr12_.is_deleted = false and storeitemr12_.is_active = true" + 
                "            )" + 
                "        )" + 
                "    )" ;
		List<Integer> storeList = new ArrayList<Integer>();
				for (MultipleStoreRequest  r : itemsRequest.getStores()) {
					storeList.add(r.getId());
						 }
        Query query = getSession().createSQLQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
		// query.setParameter("fromStoreId", storesId);
		//query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameterList("StoreIds",storeList);
		query.setParameter("vedId", itemsRequest.getVedId() != null ? itemsRequest.getVedId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("organizationId", itemsRequest.getOrganizationId()!=null ? itemsRequest.getOrganizationId() : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.pharmacy.master.global.dto.item.ItemListResponse.class));

		return query.list();

	}

	@Override
	public List<com.param.pharmacy.master.global.dto.item.ItemListResponse> MultiAssetItemsList(
			MultiItemsRequest itemsRequest) {


		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, item.code AS itemCode, item.name AS itemName "
				+ "	FROM Item AS item "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isDeleted = false AND manufacturer.isActive = true "
				+ "	WHERE item.isDeleted = false AND item.isActive = true AND item.organization.organizationId = :organizationId "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND ( productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId in STOT ) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId in STOT "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ "ORDER BY item.id ";
		List<MultipleStoreRequest> storesId = itemsRequest.getStores();

		StringBuilder appQuery = new StringBuilder(" ( ");
		for (MultipleStoreRequest r : itemsRequest.getStores()) {

			appQuery.append(r.getId() + ", ");
		}
		appQuery.append(" 0 )");
		hql = hql.replace("STOT", appQuery.toString());
		Query query = getSession().createQuery(hql);
		query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() + '%' : "");
		query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() + '%' : "");
		query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
	//	query.setParameter("fromStoreId", itemsRequest.getFromStoreId() != null ? itemsRequest.getFromStoreId() : 0);
	//	query.setParameter("toStoreId", itemsRequest.getToStoreId() != null ? itemsRequest.getToStoreId() : 0);
		query.setParameter("isConsignment",
				itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
		query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
		query.setParameter("organizationId", itemsRequest.getOrganizationId()!=null ? itemsRequest.getOrganizationId() : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class));

		return query.list();
	}

	
	@Override
	public CommonResponse checkForUniqueItemCode(ItemRequest itemRequest) throws ApplicationException {
		try {
			String hql = "select itm.id as id from Item itm where lower(itm.code) =:code "
					  + "AND itm.organization.organizationId = :orgnisationId ";
			StringBuilder query = new StringBuilder();
			if(itemRequest.getId()!=null) {
				query.append(" AND itm.id !="+itemRequest.getId());
				hql = hql.concat(query.toString());
			}
			List<ItemRequest> itemList = sessionFactory.getCurrentSession()
					.createQuery(hql)
					.setParameter("orgnisationId", sessionService.getOrganizationId())
					.setParameter("code", itemRequest.getCode().trim().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ItemRequest.class)).list();
			return new CommonResponse<>(SUCCESS, SUCCESS_CODE, null, itemList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public CommonResponse checkForUniqueItemName(ItemRequest itemRequest) throws ApplicationException {
		try {
			String hql = "select itm.id as id from Item itm where lower(itm.name) =:name "
					  + "AND itm.organization.organizationId = :orgnisationId and itm.isActive = true ";

			StringBuilder query = new StringBuilder();
			if(itemRequest.getId()!=null) {
				query.append(" AND itm.id !="+itemRequest.getId());
				hql = hql.concat(query.toString());
			}
			List<ItemRequest> itemList = sessionFactory.getCurrentSession()
					.createQuery(hql)
					.setParameter("orgnisationId",itemRequest.getOrganizationId())
					.setParameter("name", itemRequest.getName().trim().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ItemRequest.class)).list();
			return new CommonResponse<>(SUCCESS, SUCCESS_CODE, null, itemList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<com.param.pharmacy.master.global.dto.item.ItemListResponse> itemTypeaheadSearch(
			MultiItemsRequest itemsRequest) {
		try {
			
			String hql = "select itm.id as itemId,itm.name as itemName from Item itm "
					+ "	where lower(itm.name) like :itemName  "
					+ "AND itm.organization.organizationId = :orgnisationId ";
			
			List<com.param.pharmacy.master.global.dto.item.ItemListResponse> itemList = sessionFactory.getCurrentSession()
					.createQuery(hql)
					.setParameter("itemName",  "%" + itemsRequest.getItemName().trim().toLowerCase() + "%")
					.setParameter("orgnisationId", sessionService.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(com.param.pharmacy.master.global.dto.item.ItemListResponse.class)).list();
			return itemList;

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getItemIdAndName(Integer organizationId) throws ApplicationException {
		try {
			
			String hql = "select itm.id as itemId,itm.name as itemName from Item itm "
					+ "	where itm.organization.organizationId = :orgnisationId ";
			
			List<ItemListResponse> itemList = sessionFactory.getCurrentSession()
					.createQuery(hql)
					.setParameter("orgnisationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(ItemListResponse.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, itemList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBatchByItemId(Integer itemId, Integer storeId) throws ApplicationException {
		try {
			List<BatchDto> kitBatchList=sessionFactory.getCurrentSession().createSQLQuery("select" + 
					" batch0_.batch_no as \"batchNo\"," + 
					" batch0_.id as \"longBatchId\" " + 
					" from" + 
					" inventory.t_batch batch0_" + 
					" inner join public.m_item item1_ on" + 
					" batch0_.item_id = item1_.id" + 
					" and (item1_.id = :itemId" + 
					" and item1_.is_deleted = false" + 
					" and item1_.is_active = true)" + 
					" inner join inventory.t_store_stock storestock on " + 
					" batch0_.id = storestock.batch_id " + 
					" and (storestock.store_id = :storeId " + 
					" and storestock.is_active = true" + 
					" and storestock.is_deleted = false" + 
					" and storestock.available_qty>0)"  
					+"   ")
					.setParameter("itemId", itemId)
					.setParameter("storeId",storeId)
					.setResultTransformer(Transformers.aliasToBean(BatchDto.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, kitBatchList, null);
	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getUomType(Integer organizationId) throws ApplicationException {
		try {
			List<UomDto> kitBatchList=sessionFactory.getCurrentSession().createSQLQuery("SELECT uomty.id as \"uomTypeId\" ,"
					+ " uomty.type as \"uomType\" "
					+ "FROM public.m_uom_type uomty ")
					//.setParameter("organizationId",organizationId)
					.setResultTransformer(Transformers.aliasToBean(UomDto.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, kitBatchList, null);
	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response saveItemMaster(List<Item> itemMaster) throws ApplicationException {
		int x[];
		Connection connection =null;
		PreparedStatement statement = null;
			try {
					connection = driverManager.getConnection();
					connection.setAutoCommit(false);
					
					 statement = connection.prepareStatement("INSERT INTO public.m_item " +
							"( organization_id, unit_id, storage_unit_id, generic_id, pharmacological_classification_id, manufacturer_id, code,name, product_code, strength, hsn_code, is_reusable, is_vaccin, is_otc, is_combination, is_batch_required, is_high_risk_product, is_psychotropic, is_kit, is_infusion, is_narcotic, is_active, updated_by, added_by, is_deleted, formulation_type_id, s_tax_id, p_tax_id, strength_unit_id, is_consumable, is_hospital_formulary) " + 
							"VALUES(?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?,?,?,?,?, ?, ?, ?,?);"); 
			     
			        int i = 0;

			        for (Item entity : itemMaster) 
			        {
			        	statement.setInt(1, entity.getOrganizationId());
			        	statement.setInt(2,entity.getUnitId());
			        	statement.setInt(3,entity.getStorageUnit().getId());
			        	statement.setInt(4,entity.getGeneric().getId());
			        	statement.setInt(5,entity.getPharmacologicalClassification().getId());
			        	statement.setInt(6,entity.getManufacturer().getId());
			        	statement.setString(7,entity.getCode());
			        	statement.setString(8,entity.getName());
			        	statement.setString(9,entity.getProductCode());
			        	statement.setDouble(10,entity.getStrength());
			        	statement.setString(11,entity.getHsnCode());
			        	statement.setBoolean(12,entity.getIsReusable());
			        	statement.setBoolean(13,entity.getIsVaccin());
			        	statement.setBoolean(14,false);
			        	statement.setBoolean(15,entity.getIsCombination());
			        	statement.setBoolean(16,entity.getIsBatchRequired());
			        	statement.setBoolean(17,entity.getIsHighRiskProduct());
			        	statement.setBoolean(18,entity.getIsPsychotropic());
			        	statement.setBoolean(19,entity.getIsKit());
			        	statement.setBoolean(20,entity.getIsInfusion());
			        	statement.setBoolean(28,entity.getIsThreeUOMApplicable());
			        	statement.setBoolean(21,entity.getIsNarcotic());
			        	statement.setBoolean(22,true);
			        	statement.setInt(23,entity.getAddedByUser());
			        	//statement.setDate(24,(Date) entity.getUpdatedDate());
			        	statement.setInt(24,entity.getAddedByUser());
			        	//statement.setDate(26,(Date) entity.getAddedDate());
			        	statement.setBoolean(25,false);
			        	statement.setInt(26,(entity.getFormulationType()!= null && entity.getFormulationType().getId() != null) ?  entity.getFormulationType().getId() : null);
			        	statement.setInt(27,entity.getSalesTax().getId());
			        	statement.setInt(28,entity.getPurchaseTax().getId());
			        	statement.setInt(29,(entity.getStrengthUnit()!= null && entity.getStrengthUnit().getId() != null )? entity.getStrengthUnit().getId() : null);
			        	statement.setBoolean(30,entity.getIsConsumable());
			        	statement.setBoolean(31,entity.getIsHospitalFormulary());
			        	//statement.setInt(34,(entity.getProductCategory()!= null && entity.getProductCategory().getId() != null) ? entity.getProductCategory().getId() : null);
			            statement.addBatch();
			            i++;

			            if (i % 500 == 0 || i == itemMaster.size()) 
			            {
			               x = statement.executeBatch(); // Execute every 500 items.
			                connection.commit();  
			            }
			        }
			} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}try {
			connection.setAutoCommit(true);
			connection.close();
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePreviousItemInActive(ItemRequest itemRequest) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update " + 
					"	public.m_store_item_mapper " + 
					"set " + 
					"	is_active = false " + 
					"where " + 
					"	item_id =:id "+
					"	and organization_id =:organizationId " +
					"	and unit_id =:unitId ")
					.setParameter("id", itemRequest.getId())
					.setParameter("organizationId", itemRequest.getOrganizationId())
					.setParameter("unitId", itemRequest.getUnitId())
			.executeUpdate();
		 return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public String getupdateItemCode(Integer organizationId) throws ApplicationException {

		try {

			ItemResponse dto = (ItemResponse)sessionFactory.getCurrentSession().createSQLQuery("select code as \"itemCode\" from m_item mi where organization_id  =:organizationId "+
							" order by id  desc limit 1 ")
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(ItemResponse.class))
					.uniqueResult();
			
			Integer i= 1 ;
			
			boolean b = false;
			
			try{
				 
				 i=Integer.parseInt(dto.getItemCode());

			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
				if(b== true){
					return "1";
				}else{
					i = i+1;
					return i.toString();
				}

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

}
