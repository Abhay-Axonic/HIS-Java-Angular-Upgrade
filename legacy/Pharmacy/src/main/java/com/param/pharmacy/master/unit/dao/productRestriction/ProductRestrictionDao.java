package com.param.pharmacy.master.unit.dao.productRestriction;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemRestriction;
import com.param.entity.model.master.StoreProductCategoryMapper;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionPCListResponse;

@Repository("ProductRestrictionDao")
public class ProductRestrictionDao extends AbstractDao<Integer, StoreProductCategoryMapper>
		implements IProductRestrictionDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ProductRestrictionPCListResponse> getAll(Integer storeId,Integer orgId) {
		String sql = "SELECT pcMapper.id AS id, assetType.type AS assetType, assetCategory.category AS assetCategory, "
				+ "		productCategory.id AS productCategoryId, productCategory.category AS productCategory, coalesce(pcMapper.is_active, false) AS status "
				+ " FROM public.m_product_category productCategory "
				+ "		INNER JOIN public.m_asset_category AS assetCategory ON assetCategory.id = productCategory.asset_category_id "
				+ "			AND assetCategory.is_active = true AND assetCategory.is_deleted = false "
				+ "		INNER JOIN public.m_asset_type AS assetType ON assetType.id = assetCategory.asset_type_id "
				+ "			AND assetType.is_active = true AND assetType.is_deleted = false "
				+ " 	LEFT JOIN (SELECT pcMapper.id, pcMapper.product_category_id, pcMapper.is_active "
				+ "					FROM public.m_store_pc_mapper AS pcMapper "
				+ "					WHERE pcMapper.is_deleted = false AND pcMapper.store_id = :storeId) AS pcMapper ON pcMapper.product_category_id = productCategory.id "
				+ " WHERE productCategory.is_active = true AND productCategory.is_deleted = false  AND productCategory.organization_id =:organizationId ";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("assetType").addScalar("assetCategory")
				.addScalar("productCategoryId").addScalar("productCategory").addScalar("status");
		query.setParameter("storeId", storeId);
		query.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ProductRestrictionPCListResponse.class));
		return query.list();
	}

	@Override
	public Integer checkingStoreIdPresence(Integer storeId) {
		String hql = "SELECT COUNT(*) "
				+ " FROM com.param.entity.model.master.StoreProductCategoryMapper AS storePCMapper "
				+ " WHERE storePCMapper.store.storeId = :storeId  AND storePCMapper.isActive = true AND storePCMapper.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		Long result = null;
		for (Iterator it = query.iterate(); it.hasNext();) {
			result = (Long) it.next();
		}

		if (result == null) {
			result = 0L;
		}
		return (int) (long) result;
	}

	@Override
	public List<ProductRestrictionPCListResponse> getAllWithoutStoreId() {
		String hql = "SELECT  assetCategory.id AS assetCategoryId, assetCategory.category AS assetCategory,"
				+ " productCategory.id AS productCategoryId,productCategory.category AS productCategory, "
				+ "  assetType.id AS assetTypeId, assetType.type AS assetType, coalesce(false) AS status "
				+ " FROM ProductCategory AS productCategory "
				+ " INNER JOIN productCategory.assetCategory AS assetCategory "
				+ " INNER JOIN assetCategory.assetType as assetType" +
				"  INNER JOIN productCategory.organization as organization " +
				" WHERE  organization.organizationId=:organizationId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ProductRestrictionPCListResponse.class));
		return query.list();
	}

	@Override
	public List<StoreItemRestriction> getAllItems() {
		Criteria criteria = getSession().createCriteria(StoreItemRestriction.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false)).add(Restrictions.eq("isActive", true));

		Criteria storeCriteria = criteria.createCriteria("store");
		storeCriteria.add(Restrictions.eq("isDeleted", false)).add(Restrictions.eq("isActive", true));

		Criteria itemCriteria = criteria.createCriteria("item");
		itemCriteria.add(Restrictions.eq("isDeleted", false)).add(Restrictions.eq("isActive", true));

		Criteria genericCriteria = itemCriteria.createCriteria("generic");
		genericCriteria.add(Restrictions.eq("isDeleted", false)).add(Restrictions.eq("isActive", true));

		Criteria productCriteria = genericCriteria.createCriteria("productCategory");
		productCriteria.add(Restrictions.eq("isDeleted", false)).add(Restrictions.eq("isActive", true));

		Criteria assetCategoryCriteria = productCriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false)).add(Restrictions.eq("isActive", true));

		Criteria assetTypeCriteria = assetCategoryCriteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false)).add(Restrictions.eq("isActive", true));

		return criteria.list();
	}

	@Override
	public List<StoreProductCategoryMapper> getBystoreId() {
		Criteria criteria = getSession().createCriteria(StoreProductCategoryMapper.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));

		Criteria storeCriteria = criteria.createCriteria("store");
		storeCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria productCriteria = criteria.createCriteria("productCategory");
		productCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetCategoryCriteria = productCriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetTypeCriteria = assetCategoryCriteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false));

		return criteria.list();
	}

	@Override
	public StoreProductCategoryMapper getById(BigInteger id) {
		Criteria criteria = getSession().createCriteria(StoreProductCategoryMapper.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (StoreProductCategoryMapper) criteria.uniqueResult();
	}

	@Override
	public BigInteger create(StoreProductCategoryMapper storeProductCategoryMapper) {
		getSession().save(storeProductCategoryMapper);
		return storeProductCategoryMapper.getId();
	}

	@Override
	public BigInteger update(StoreProductCategoryMapper storeProductCategoryMapper) {
		getSession().update(storeProductCategoryMapper);
		return storeProductCategoryMapper.getId();
	}

	@Override
	public Store getStore(Integer storeId) {
		return (Store) getSession().get(Store.class, storeId);
	}

	@Override
	public ProductCategory getProductCategory(Integer productCategoryId) {
		return (ProductCategory) getSession().get(ProductCategory.class, productCategoryId);
	}

	@Override
	public StoreItemRestriction getItemRestrictionById(BigInteger id) {
		Criteria criteria = getSession().createCriteria(StoreItemRestriction.class);
		criteria.add(Restrictions.eq("id", id));
		// criteria.add(Restrictions.eq("isDeleted", false));
		return (StoreItemRestriction) criteria.uniqueResult();
	}

	@Override
	public BigInteger createItemRestriction(StoreItemRestriction storeItemRestriction) {
		getSession().save(storeItemRestriction);
		return storeItemRestriction.getId();
	}

	@Override
	public Item getItem(Integer itemId) {
		return (Item) getSession().get(Item.class, itemId);
	}

	@Override
	public BigInteger update(StoreItemRestriction storeItemRestriction) {
		getSession().update(storeItemRestriction);
		return storeItemRestriction.getId();
	}

	/*
	 * @Override public List<StoreProductCategoryMapper> getBystoreId(Integer
	 * storeId) { Criteria criteria =
	 * getSession().createCriteria(StoreProductCategoryMapper.class);
	 * criteria.add(Restrictions.eq("storeId", storeId));
	 * criteria.add(Restrictions.eq("isDeleted", false)); return
	 * (List<StoreProductCategoryMapper>) criteria.list();
	 * 
	 * 
	 * 
	 * session.bySimpleNaturalId(StudentTestMapping.class) .load(studentId)
	 * 
	 * }
	 */

	@Override
	public List<ProductRestrictionPCListResponse> getBystoreId(Integer storeId) {

		String hql = "SELECT storePCMapper.id AS id, store.storeId AS storeId, store.storeName AS storeName, ac.id AS assetCategoryId, ac.category AS assetCategory ,"
				+ " pc.id AS productCategoryId, pc.category AS productcategory, storePCMapper.isActive AS status, at.id AS assetTypeId, at.type AS assetType "
				+ " FROM StoreProductCategoryMapper AS storePCMapper " + " INNER JOIN storePCMapper.store AS store "
				+ " INNER JOIN storePCMapper.productCategory AS pc " + " INNER JOIN pc.assetCategory AS ac "
				+ " INNER JOIN ac.assetType AS at "
				+ " WHERE storePCMapper.store.storeId = :storeId  AND storePCMapper.isActive = true AND storePCMapper.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(ProductRestrictionPCListResponse.class));
		return query.list();
	}

	@Override
	public Long updateProductRestrictionMapper(BigInteger id, Boolean status) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "UPDATE FROM StoreProductCategoryMapper SET isActive =:status WHERE id = :id ";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("status", status);
		query.executeUpdate();
		return id.longValue();
	}

	@Override
	public List<ProductRestrictionItemRestrictionResponse> getItemRestrictionsBystoreId(Integer storeId) {
		// remove storeItemRestriction.isDeleted = false and get
		// storeItemRestriction.isDeleted insted of storeItemRestriction.isActive to
		// show all record
		String hql = "SELECT storeItemRestriction.id AS id, store.storeId AS storeId, store.storeName AS storeName,"
				+ " item.id AS itemId, item.code AS itemCode, item.name AS itemName, "
				+ " ac.id AS assetCategoryId, ac.category AS assetCategory ,"
				+ " pc.id AS productCategoryId, pc.category AS productCategory, storeItemRestriction.isActive AS status "// isActive
																															// to
																															// isDeleted
				+ " FROM StoreItemRestriction AS storeItemRestriction "
				+ " LEFT JOIN storeItemRestriction.store AS store " + " LEFT JOIN storeItemRestriction.item AS item"
				+ " LEFT JOIN item.generic AS generic " + " LEFT JOIN generic.productCategory AS pc "
				+ " LEFT JOIN pc.assetCategory AS ac " + " WHERE storeItemRestriction.store.storeId = :storeId ";// AND
																													// storeItemRestriction.isDeleted
																													// =
																													// false
																													// ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setResultTransformer(Transformers.aliasToBean(ProductRestrictionItemRestrictionResponse.class));
		return query.list();
	}

	@Override
	public Long getProductRestrictionFromProductCategory(Integer id) {
		String hql = "SELECT count(pc) FROM ProductCategory as pc "
				+ " WHERE pc.id = :id AND pc.isActive = false AND pc.isDeleted = false";
		// String hql = " SELECT count(productCategory) from ProductCategory as
		// productCategory "
		// + "INNER JOIN productCategory.storeMapperList As storeMapper WITH
		// storeMapper.isDeleted = false "
		// + " INNER JOIN storeMapper.productCategory AS pc WITH pc.id = :id AND
		// pc.isActive = false AND pc.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
}
