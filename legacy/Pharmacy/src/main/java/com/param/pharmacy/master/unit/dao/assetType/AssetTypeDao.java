package com.param.pharmacy.master.unit.dao.assetType;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.AssetType;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;

@Repository("AssetTypeDao")
public class AssetTypeDao extends AbstractDao<Integer, AssetType> implements IAssetTypeDao {

	@Override
	public List<AssetType> getAll() {
		Criteria criteria = getSession().createCriteria(AssetType.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));

		return criteria.list();
	}

	@Override
	public AssetType getById(Integer id) {
		Criteria criteria = getSession().createCriteria(AssetType.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (AssetType) criteria.uniqueResult();
	}

	@Override
	public Integer create(AssetType assetType) {
		getSession().save(assetType);
		return assetType.getId();
	}

	@Override
	public Integer update(AssetType assetType) {
		getSession().update(assetType);
		return assetType.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AssetTypeListResponse> getAllAssetTypesBetterPerformance() {
		
		String hql = "select assetType.id as id, assetType.code as code, assetType.type as type, assetType.isActive as status from "
				+ "AssetType assteType where assteType.isActive = true AND assteType.isDeleted = false";

		List<AssetTypeListResponse> list = getSession().createQuery(hql)

				.setResultTransformer(
						Transformers.aliasToBean(AssetTypeListResponse.class))
				.list();
		return list;
		
	}

}
