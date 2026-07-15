package com.param.pharmacy.master.global.dao.formulationTypeAndROA;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.FormulationAndRoa;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.PrefixConfiguration;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.service.global.SessionService;

@Repository
@SuppressWarnings("unchecked")
public class FormulationTypeAndROADao extends AbstractDao<Integer, FormulationAndRoa>
		implements IFormulationTypeAndROADao {

	
	@Override
	public List<FormulationAndRoa> getAll(Integer orgId) {

		String hql = "SELECT formulationAndRoa FROM FormulationAndRoa AS formulationAndRoa "
				+ " INNER JOIN formulationAndRoa.routeOfAdministration roa WITH roa.isActive = true AND roa.isDeleted = false AND roa.organization.organizationId=:orgId"
				+ " where formulationAndRoa.isActive=true AND formulationAndRoa.isDeleted=false AND formulationAndRoa.organization.organizationId=:orgId"
				+ " ORDER BY formulationAndRoa.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("orgId", orgId);
		return query.list();

	}
	
	@Autowired
	private SessionService sessionService;
	/*
	 * @Override public Integer create(FormulationAndRoa formulationAndRoa) {
	 * getSession().save(formulationAndRoa); return formulationAndRoa.getId(); }
	 */

	@Override
	public FormulationAndRoa saveFormulationTypeAndROA(FormulationAndRoa formulationAndRoa) {
		getSession().save(formulationAndRoa);
		return formulationAndRoa;
	}

	@Override
	public FormulationAndRoa getById(Integer id) {

		String hql = "SELECT formulationAndRoa FROM FormulationAndRoa AS formulationAndRoa "
				+ "WHERE formulationAndRoa.id = :id  AND formulationAndRoa.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (FormulationAndRoa) query.uniqueResult();
	}

	@Override
	public FormulationType getFormulationType(Integer id) {
		return (FormulationType) getSession().get(FormulationType.class, id);
	}

	@Override
	public RouteOfAdministration getRouteOfAdministration(Integer id) {
		return (RouteOfAdministration) getSession().get(RouteOfAdministration.class, id);
	}

	@Override
	public Integer update(FormulationAndRoa formulationAndRoa) {
		getSession().update(formulationAndRoa);
		return formulationAndRoa.getId();
	}

	@Override
	public List<RouteOfAdministration> getDropdownList(Integer orgId) {
		String hql = "SELECT ROA FROM RouteOfAdministration AS ROA \r\n"
				+ "WHERE ROA.isActive = true AND ROA.isDeleted = false AND ROA.organization.organizationId=:organizationId";
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public Long getFormulationROACountFromFormulationType(Integer id) {
		String hql = " SELECT count(formulationType) from FormulationType as formulationType "
				+ " INNER JOIN formulationType.formulationAndRoaList AS formulationAndRoa WITH formulationAndRoa.id = :id AND formulationAndRoa.isDeleted = false "
				+ " WHERE formulationType.isActive = false AND formulationType.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getFormulationROACountFromROA(Integer id) {
		// Long count = new Long(0);
		String hql = " SELECT count(formulation_and_ROA) from FormulationAndRoa as formulation_and_ROA "
				+ " INNER JOIN formulation_and_ROA.routeOfAdministration as routeOfAdministration WITH routeOfAdministration.id =:id AND routeOfAdministration.isDeleted = false"
				+ " WHERE formulation_and_ROA.isActive = false AND formulation_and_ROA.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getCount(Integer orgId) {
		String hql = " SELECT count(formulation_and_ROA) from FormulationAndRoa as formulation_and_ROA where formulation_and_ROA.organization.organizationId=:organizationId  ";
		Query query = getSession().createQuery(hql)
		.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId());
		Long count = (Long) query.uniqueResult();
		return count;	
			
	}

	@Override
	public List<FormulationAndRoa> getFormulationTypeAndROA(Integer offset, Integer noOfRecordsPerPage,Integer orgId) {
		String hql = "SELECT formulationAndRoa FROM FormulationAndRoa AS formulationAndRoa "
				+ " where formulationAndRoa.organization.organizationId=:organizationId  "
				+ "ORDER BY formulationAndRoa.id DESC";

		Query query = getSession().createQuery(hql)
				.setParameter("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId())
				.setFirstResult(offset != null ? offset : 0)
				.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10);
		
		
		return query.list();
	}

}
