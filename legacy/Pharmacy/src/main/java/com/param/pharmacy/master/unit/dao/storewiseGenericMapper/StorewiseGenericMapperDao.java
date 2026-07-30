package com.param.pharmacy.master.unit.dao.storewiseGenericMapper;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StorewiseGenericMapper;
import com.param.entity.model.master.Ved;
import com.param.service.global.SessionService;

@Repository("StorewiseGenericMapperDao")
public class StorewiseGenericMapperDao extends AbstractDao<Integer, StorewiseGenericMapper>
		implements IStorewiseGenericMapperDao {
	
	@Autowired
    SessionService sessionService;

	@Override
	public List<StorewiseGenericMapper> getAll(Integer unitId) {
		Criteria criteria = getSession().createCriteria(StorewiseGenericMapper.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));

		Criteria storeCriteria = criteria.createCriteria("store");
		storeCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria genericCriteria = criteria.createCriteria("generic");
		genericCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria productCategoryCriteria = criteria.createCriteria("generic.productCategory");
		productCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetCategoryCriteria = criteria.createCriteria("generic.productCategory.assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria vedCriteria = criteria.createCriteria("ved");
		vedCriteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("unit");
        unitCriteria.add(Restrictions.eq("unitId", unitId!=null ? unitId : sessionService.getUnitId()));

		return criteria.list();
	}

	@Override
	public StorewiseGenericMapper getById(Integer id, Integer unitId) {
		// BR-121: previously had no unitId restriction at all -- any id was
		// readable/writable by any authenticated user regardless of unit.
		Criteria criteria = getSession().createCriteria(StorewiseGenericMapper.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("unitId", unitId));

		return (StorewiseGenericMapper) criteria.uniqueResult();
	}

	@Override
	public Integer create(StorewiseGenericMapper storewiseGenericMapper) {
		getSession().save(storewiseGenericMapper);
		return storewiseGenericMapper.getId();
	}

	@Override
	public Integer update(StorewiseGenericMapper storewiseGenericMapper) {
		getSession().update(storewiseGenericMapper);
		return storewiseGenericMapper.getId();
	}

	@Override
	public Store getStore(Integer storeId) {
		return (Store) getSession().get(Store.class, storeId);
	}

	@Override
	public Generic getGeneric(Integer genericId) {
		return (Generic) getSession().get(Generic.class, genericId);
	}

	@Override
	public Ved getVed(Integer vedId) {
		return (Ved) getSession().get(Ved.class, vedId);
	}

	@Override
	public List<Ved> getVedList() {
		String hql = "SELECT ved FROM Ved AS ved \r\n" + "WHERE ved.isActive = true AND ved.isDeleted = false\r\n";

		Query query = getSession().createQuery(hql);
		return query.list();

	}

	@Override
	public List<Generic> getGenericList(String name, Integer unitId, Integer orgId) {

		// BR-121: the OR here was previously unparenthesized, so this parsed as
		// (unitId AND orgId AND name-match) OR (code-match AND isActive AND
		// isDeleted=false) -- any tenant's generic matching by code bypassed
		// the unit/org scoping entirely, regardless of the unitId/orgId
		// parameters passed in. Parenthesizing the OR restores the intended
		// "scoped to this unit/org, matching by either name or code" meaning.
		String hql = "SELECT generic FROM Generic as generic"
				+ " WHERE generic.unitId = :unitId AND generic.organizationId = :orgId "
				+ " AND (lower(generic.name) LIKE :name OR lower(generic.code) LIKE :name) "
				+ " AND generic.isActive = true AND generic.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		query.setParameter("unitId", unitId != null ? unitId : 0);
		query.setParameter("orgId", orgId != null ? orgId : 0);
		// query.setParameter("unitId", sessionService.getUnitId());
		return query.list();

	}

	@Override
	public Long getStorewiseGenericFromGeneric(Integer id) {
		String hql = " SELECT count(generic) from Generic as generic "
				+ "INNER JOIN generic.storewiseGenericList As storewiseGeneric WITH storewiseGeneric.id = :id AND storewiseGeneric.isDeleted = false "
				+ "WHERE generic.isActive = false AND generic.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
}
