package com.param.pharmacy.master.global.dao.taxComponent;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.TaxComponent;
import com.param.service.global.SessionService;

@Repository("TaxComponentDao")
public class TaxComponentDao extends AbstractDao<Integer, TaxComponent> implements ITaxComponentDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<TaxComponent> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(TaxComponent.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("organization");
        unitCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));

		return criteria.list();

	}

	@Override
	public TaxComponent getById(Integer id, Integer orgId) {
		Criteria criteria = getSession().createCriteria(TaxComponent.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		Criteria orgCriteria = criteria.createCriteria("organization");
		orgCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));

		return (TaxComponent) criteria.uniqueResult();
	}

	@Override
	public Integer create(TaxComponent taxComponent) {
		getSession().save(taxComponent);
		return taxComponent.getId();
	}

	@Override
	public Integer update(TaxComponent taxComponent) {
		getSession().update(taxComponent);
		return taxComponent.getId();
	}

	@Override
	public List<TaxComponent> getTaxComponentList() {
		String hql = "SELECT taxComponent FROM TaxComponent AS taxComponent \r\n" +
				" WHERE taxComponent.organization.organizationId =:organizationId AND taxComponent.isActive = true" +
				" AND taxComponent.isDeleted = false " + " ORDER BY taxComponent.component";

		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId",sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public Long getTaxComponentFromTax(Integer id,Integer orgId) {
		String hql = "SELECT count(tax) from Tax as tax "
				+ "INNER JOIN tax.taxDetailList As taxDetail WITH taxDetail.isActive = true AND taxDetail.isDeleted = false "
				+ "INNER JOIN taxDetail.taxComponent As taxcomponent WITH taxcomponent.id = :id AND taxcomponent.isActive = true AND taxcomponent.isDeleted = false "
				+ "WHERE tax.isActive = true AND tax.isDeleted = false AND tax.organization.organizationId =:organizationId";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("organizationId",sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

}
