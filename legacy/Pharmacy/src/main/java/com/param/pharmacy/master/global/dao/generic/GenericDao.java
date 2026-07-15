package com.param.pharmacy.master.global.dao.generic;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.CombinationGeneric;
import com.param.entity.model.master.Generic;
import com.param.pharmacy.master.global.dto.generic.GenericListResponse;
import com.param.service.global.SessionService;

@Repository("GenericDao")
@SuppressWarnings("unchecked")
public class GenericDao extends AbstractDao<Integer, Generic> implements IGenericDao {
	
	@Autowired
	SessionService sessionService;

	@SuppressWarnings("unchecked")
	@Override
	public List<Generic> getAll() {
		/*
		 * String hql =
		 * "SELECT gen.id, at.type, ac.category, pc.category, gen.code, gen.name, gen.isActive\r\n"
		 * + "FROM Generic as gen\r\n" + "	INNER JOIN gen.ProductCategory as pc\r\n" +
		 * " INNER JOIN pc.AssetCategory as ac\r\n" +
		 * " INNER JOIN ac.AssetType as at\r\n" +
		 * "WHERE gen.isDeleted = false AND pc.isDeleted = false AND ac.isDeleted = false AND at.isDeleted = false\r\n"
		 * + "ORDER BY gen.id";
		 * 
		 * Query query = getSession().createQuery(hql);
		 * 
		 * List<Object[]> result = query.list();
		 * 
		 * return result;
		 */

		Criteria criteria = getSession().createCriteria(Generic.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));

		Criteria productCategoryCriteria = criteria.createCriteria("productCategory");
		productCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetCategoryCriteria = productCategoryCriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetTypeCriteria = assetCategoryCriteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("organization");
        unitCriteria.add(Restrictions.eq("organizationId", sessionService.getOrganizationId()));

		return criteria.list();
	}

	@Override
	public Generic getById(Integer id, Integer orgId) {
		Criteria criteria = getSession().createCriteria(Generic.class).createAlias("organization","organization");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));
		return (Generic) criteria.uniqueResult();

		/* return (Generic) getSession().get(Generic.class, id); */
	}
	
	@Override
	public List<CombinationGeneric> getCombinationDetailsById(Integer id) {
		
		String hql = "select combinationGeneric " + 
				"from CombinationGeneric as combinationGeneric" + 
				"	inner join combinationGeneric.generic AS generic WITH generic.isDeleted = false " +
				"	where generic.id = :id " ;
		
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (List<CombinationGeneric>)query.list();
	}
 
	@Override
	public Integer create(Generic generic) {
		getSession().save(generic);
		return generic.getId();
	}

	@Override
	public Integer update(Generic generic) {
		getSession().update(generic);
		return generic.getId();
	}

	/*
	 * @Override public ProductCategory getProductCategory(Integer id) { return
	 * (ProductCategory) getSession().get(ProductCategory.class, id); }
	 */

	@Override
	public Long getGenericCountFromItem(Integer id) {
		String hql = " SELECT count(item) from Item as item WHERE item.generic.id = :id AND item.isActive = true AND item.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getGenericCountFromStoreWiseMapper(Integer id) {
		String hql = " SELECT count(storewiseGenericMapper) from StorewiseGenericMapper as storewiseGenericMapper WHERE storewiseGenericMapper.generic.id = :id AND storewiseGenericMapper.isActive = true AND storewiseGenericMapper.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getGenericCountFromProductCategory(Integer id) {
		String hql = " SELECT count(productCategory) from ProductCategory as productCategory "
				+ "INNER JOIN productCategory.genericsList As generics WITH generics.id = :id AND generics.isDeleted = false "
				+ "WHERE productCategory.isActive = false AND productCategory.isDeleted = false AND productCategory.organization.organizationId =:organizationId";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId", sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public List<GenericListResponse> getAllGenericMasters(Integer orgId) {
		
		String hql = "select" + 
				"	generic.id as id," + 
				"	generic.code as code," + 
				"	generic.name as name," + 
				"	generic.isActive as status," + 
				"	assetType.type as assetType," + 
				"	assetCategory.category as assetCategory," + 
				"	productCategory.category as productCategory " + 
				"from Generic as generic" + 
				"	inner join generic.productCategory as productCategory WITH productCategory.isDeleted = false " +
				"	inner join productCategory.assetCategory as assetCategory WITH assetCategory.isDeleted = false" +
				"	inner join assetCategory.assetType as assetType WITH assetType.isDeleted = false " +
				"where generic.isDeleted=false AND generic.organization.organizationId = :orgId " +
				" ORDER BY generic.name ASC";
		
				List<GenericListResponse> list = getSession().createQuery(hql)
						.setParameter("orgId", orgId!=null ? orgId : sessionService.getOrganizationId())

				.setResultTransformer(
						Transformers.aliasToBean(GenericListResponse.class))
				.list();
		return list;
	}
	
	@Override
	public List<GenericListResponse> getAllIsCombinationGeneric(Boolean isCombination) {
		
		String hql = "select" + 
				"	generic.id as id," + 
				"	generic.code as code," + 
				"	generic.name as name," + 
				"	generic.isActive as status," + 
				"	assetType.type as assetType," + 
				"	assetCategory.category as assetCategory," + 
				"	productCategory.category as productCategory " + 
				"from Generic as generic" + 
				"	inner join generic.productCategory as productCategory WITH productCategory.isDeleted = false " +
				"	inner join productCategory.assetCategory as assetCategory WITH assetCategory.isDeleted = false" +
				"	inner join assetCategory.assetType as assetType WITH assetType.isDeleted = false " +
				"where generic.isDeleted=false AND generic.organization.organizationId = :orgId AND generic.isCombination = :isCombination" +
				" ORDER BY generic.id";
		
				List<GenericListResponse> list = getSession().createQuery(hql)
						.setParameter("orgId", sessionService.getOrganizationId())
						.setParameter("isCombination", isCombination)

				.setResultTransformer(
						Transformers.aliasToBean(GenericListResponse.class))
				.list();
		return list;
	}
}
