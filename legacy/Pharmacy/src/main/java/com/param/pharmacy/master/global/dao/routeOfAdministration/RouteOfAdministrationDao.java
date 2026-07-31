package com.param.pharmacy.master.global.dao.routeOfAdministration;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.service.global.SessionService;

@Repository("RouteOfAdministrationDao")
public class RouteOfAdministrationDao extends AbstractDao<Integer, RouteOfAdministration> implements IRouteOfAdministrationDao{

	 @Autowired
	    private SessionService sessionService;

	 
	@Override
	public List<RouteOfAdministration> getAll(Integer orgId) {
		// orgId is always the server-verified organizationId by the time it
		// reaches here (see RouteOfAdministrationService) -- no client-supplied
		// or sessionService fallback used, closing the cross-tenant IDOR this
		// query previously had (BR-pending, found during batch 35 Step 0/1).
		Criteria criteria = getSession().createCriteria(RouteOfAdministration.class);
		Criteria org = criteria.createCriteria("organization");
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		org.add(Restrictions.eq("organizationId" , orgId));
		return criteria.list();
	}

	@Override
	public RouteOfAdministration getById(Integer id, Integer orgId) {
		// orgId added (BR-pending, batch 35 Step 0/1): this query previously had
		// zero organization scoping at all -- any authenticated user could fetch
		// any organization's record purely by id. orgId is always the
		// server-verified organizationId by the time it reaches here.
		Criteria criteria = getSession().createCriteria(RouteOfAdministration.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organizationId", orgId));

		return (RouteOfAdministration) criteria.uniqueResult();
	}

	@Override
	public Integer create(RouteOfAdministration routeOfAdministration) {
		getSession().save(routeOfAdministration);
		return routeOfAdministration.getId();
	}

	@Override
	public Integer update(RouteOfAdministration routeOfAdministration) {
		getSession().update(routeOfAdministration);
		return routeOfAdministration.getId();
	}

	@Override
	public Long getRouteOfAdministrationCountFromFormulationROA(Integer id) {
		String hql = " SELECT count(formulation_and_ROA) from FormulationAndRoa as formulation_and_ROA "
				+" INNER JOIN formulation_and_ROA.routeOfAdministration as routeOfAdministration WITH routeOfAdministration.id = :id  "
				+ "  WHERE  formulation_and_ROA.isActive = true AND formulation_and_ROA.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
}
