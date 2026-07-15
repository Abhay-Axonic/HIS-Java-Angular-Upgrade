package com.param.pharmacy.master.global.dao.strengthUnit;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.StrengthUnit;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitListResponse;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryListResponse;
import com.param.service.global.SessionService;

@Repository("StrengthUnitDao")
public class StrengthUnitDao extends AbstractDao<Integer, StrengthUnit> implements IStrengthUnitDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<StrengthUnit> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(StrengthUnit.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("organization");
        unitCriteria.add(Restrictions.eq("organizationId", orgId!=null ? sessionService.getOrganizationId() : orgId ));

		return criteria.list();
	}

	@Override
	public StrengthUnit getById(Integer id) {
		Criteria criteria = getSession().createCriteria(StrengthUnit.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (StrengthUnit) criteria.uniqueResult();
	}

	@Override
	public Integer create(StrengthUnit strengthUnit) {
		getSession().save(strengthUnit);
		return strengthUnit.getId();
	}

	@Override
	public Integer update(StrengthUnit strengthUnit) {
		getSession().update(strengthUnit);
		return strengthUnit.getId();
	}

	@Override
	public Long getStrengthUnitDaoCountFromItem(Integer id) {
		String hql = " SELECT count(item) from Item as item WHERE item.strengthUnit.id = :id AND item.isActive = true AND item.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StrengthUnitListResponse> getStrengthUnitList(Integer organizationId) {
		String hql = "select strengthUnit.id as id, strengthUnit.units as unit from "
				+ "StrengthUnit strengthUnit where strengthUnit.isActive = true AND strengthUnit.isDeleted = false AND strengthUnit.organization =:organizationId ";

		List<StrengthUnitListResponse> list = getSession().createQuery(hql).setResultTransformer(
						Transformers.aliasToBean(StrengthUnitListResponse.class))
				.setParameter("organizationId", organizationId).list();
		return list;
	}
	
}
