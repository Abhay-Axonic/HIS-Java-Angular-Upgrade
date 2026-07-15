package com.param.pharmacy.master.unit.dao.assetCategory;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.AssetType;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryListResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;
import com.param.service.global.SessionService;

@Repository("AssetCategoryDao")
public class AssetCategoryDao extends AbstractDao<Integer, AssetCategory> implements IAssetCategoryDao {

	@Autowired
	SessionService sessionService;
	
	@Override
	public List<AssetCategory> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(AssetCategory.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetTypeCriteria = criteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false));
		
		/*Criteria unitCriteria = criteria.createCriteria("unit");
		unitCriteria.add(Restrictions.eq("unitId", sessionService.getUnitId()));
*/
		Criteria unitCriteria = criteria.createCriteria("organization");
		unitCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));

		
		return criteria.list();
	}

	@Override
	public AssetCategory getById(Integer id) {
		Criteria criteria = getSession().createCriteria(AssetCategory.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		return (AssetCategory) criteria.uniqueResult();
	}

	@Override
	public Integer create(AssetCategory assetCategory) {
		getSession().save(assetCategory);
		return assetCategory.getId();
	}

	@Override
	public AssetType getAssetType(Integer id) {
		return (AssetType) getSession().get(AssetType.class, id);
	}

	@Override
	public Integer update(AssetCategory assetCategory) {
		getSession().update(assetCategory);
		return assetCategory.getId();
	}

	
	@Override
	public Long getProductCategory(Integer id) {
		
		String hql = " SELECT count(productcategory) from ProductCategory as productcategory WHERE productcategory.assetCategory.id = :id AND productcategory.isActive = true AND productcategory.isDeleted = false AND productcategory.organization.organizationId =:organizationId";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId",sessionService.getOrganizationId());
		Long count = (Long)query.uniqueResult();
		
		return count ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AssetCategoryListResponse> getAllAssetCat(Integer organizationId) {
		String hql = "select assetCategory.id as id, assetCategory.category as category from "
				+ "AssetCategory assetCategory where assetCategory.isActive = true AND assetCategory.isDeleted = false AND assetCategory.organizationId = "+organizationId;

		List<AssetCategoryListResponse> list = getSession().createQuery(hql).setResultTransformer(
						Transformers.aliasToBean(AssetCategoryListResponse.class)).list();
		return list;
	}

}
