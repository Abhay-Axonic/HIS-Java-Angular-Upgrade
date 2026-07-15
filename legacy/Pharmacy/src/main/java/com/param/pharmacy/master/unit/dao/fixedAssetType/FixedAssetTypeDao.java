package com.param.pharmacy.master.unit.dao.fixedAssetType;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.FixedAssetType;

@Repository("FixedAssetTypeDao")
public class FixedAssetTypeDao extends AbstractDao<Integer, FixedAssetType> implements IFixedAssetTypeDao {

	@Override
	public List<FixedAssetType> getAll() {
		Criteria criteria = getSession().createCriteria(FixedAssetType.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		return criteria.list();
	}

	@Override
	public FixedAssetType getById(Integer id) {
		Criteria criteria = getSession().createCriteria(FixedAssetType.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (FixedAssetType) criteria.uniqueResult();
	}

	@Override
	public Integer create(FixedAssetType fixedAssetType) {
		getSession().save(fixedAssetType);
		return fixedAssetType.getId();
	}

	@Override
	public Integer update(FixedAssetType fixedAssetType) {
		getSession().update(fixedAssetType);

		return fixedAssetType.getId();
	}

}
