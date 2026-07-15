package com.param.pharmacy.master.global.dao.uomUnit;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.UomUnit;
import com.param.service.global.SessionService;

@Repository("UomUnitDao")
public class UomUnitDao extends AbstractDao<Integer, UomUnit> implements IUomUnitDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<UomUnit> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(UomUnit.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("organization");
        unitCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));

		return criteria.list();
	}

	@Override
	public UomUnit getById(Integer id) {
		Criteria criteria = getSession().createCriteria(UomUnit.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (UomUnit) criteria.uniqueResult();
	}

	@Override
	public Integer create(UomUnit uomUnit) {
		getSession().save(uomUnit);
		return uomUnit.getId();
	}

	@Override
	public Integer update(UomUnit uomUnit) {
		getSession().update(uomUnit);
		return uomUnit.getId();
	}

	@Override
	public Long getUnitOfMesurementCountFromUnit(Integer id,Integer orgId) {
		String hql = " SELECT count(unitofmeasurement) from UnitOfMeasurement as unitofmeasurement WHERE unitofmeasurement.uomUnit.id = :id AND unitofmeasurement.isActive = true AND unitofmeasurement.isDeleted = false" +
				" AND unitofmeasurement.organization.organizationId=:organizationId";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId",orgId!=null ? orgId : sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

}
