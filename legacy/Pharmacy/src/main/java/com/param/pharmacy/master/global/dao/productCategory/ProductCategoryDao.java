package com.param.pharmacy.master.global.dao.productCategory;

import java.util.List;

import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryListResponse;
import com.param.service.global.SessionService;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.ProductCategory;

@Repository("ProductCategoryDao")
public class ProductCategoryDao extends AbstractDao<Integer, ProductCategory> implements IProductCategoryDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<ProductCategoryListResponse> getAll(Integer orgId) {
		String hql =
				"	SELECT productCategory.id AS id, assetType.type AS assetType, assetCategory.category AS assetCategory, "
				+ "		productCategory.code AS code, productCategory.category AS category, productCategory.isActive AS status ,service.serviceStandardName as serviceName ,service.serviceMasterId as serviceId "
				+ ",productCategory.generalLedgerId as generalLedgerId	FROM ProductCategory AS productCategory "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false "
				+ "		INNER JOIN assetCategory.assetType AS assetType WITH assetType.isDeleted = false "
				+ "		LEFT JOIN productCategory.serviceMaster AS service  "
				+ " WHERE productCategory.isDeleted = false AND productCategory.organization.organizationId = :orgId "
				+ "	ORDER BY productCategory.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("orgId", orgId!=null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ProductCategoryListResponse.class));

		return query.list();
	}

	@Override
	public ProductCategory getById(Integer id,Integer orgId) {
		Criteria criteria = getSession().createCriteria(ProductCategory.class).
				createAlias("organization","organization");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId",orgId!=null ? orgId : sessionService.getOrganizationId()));

		return (ProductCategory) criteria.uniqueResult();
	}

	@Override
	public Integer create(ProductCategory productCategory) {
		getSession().save(productCategory);

		return productCategory.getId();
	}

	@Override
	public Integer update(ProductCategory productCategory) {
		getSession().update(productCategory);
		return productCategory.getId();
	}

	@Override
	public AssetCategory getAssetCategory(Integer id) {
		return (AssetCategory) getSession().get(AssetCategory.class, id);
	}

	@Override
	public Long getProductCategoryCountFromItem(Integer id) {
		String hql = " SELECT count(item) from Item as item WHERE item.productCategory.id = :id AND item.isActive = true AND item.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getProductCategoryCountFromGeneric(Integer id) {
		String hql = " SELECT count(generic) from Generic as generic WHERE generic.productCategory.id = :id AND generic.isActive = true AND generic.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getProductCategoryCountFromPoductRestriction(Integer id) {
		String hql = " SELECT count(storeProductCategoryMapper) from StoreProductCategoryMapper as storeProductCategoryMapper WHERE storeProductCategoryMapper.productCategory.id = :id AND storeProductCategoryMapper.isActive = true AND storeProductCategoryMapper.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	@Override
	public Long getProductCategoryCountFromAssetcategory(Integer id) {		
		String hql = " SELECT count(assetCategory) from AssetCategory as assetCategory "
				+ "INNER JOIN assetCategory.productCategoryList As productCategory WITH productCategory.id = :id AND productCategory.isDeleted = false "
				+ "WHERE assetCategory.isActive = false AND assetCategory.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long)query.uniqueResult();
		return count;
	}

	@Override
	public Integer updateProductCategoryByService(Integer serviceId, Integer productCategoryId, Integer unitId) {
		String hql = " UPDATE UnitServiceMasterDetails SET productCategoryId = :productCategoryId WHERE serviceMasterId=:serviceId and unitId =:unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("serviceId", serviceId);
		query.setParameter("productCategoryId", productCategoryId);
		query.setParameter("unitId", unitId);
		Integer count = (Integer)query.executeUpdate();
		return count;
	}
}
