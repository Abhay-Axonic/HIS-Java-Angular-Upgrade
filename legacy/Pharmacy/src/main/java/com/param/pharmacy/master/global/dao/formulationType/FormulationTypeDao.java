package com.param.pharmacy.master.global.dao.formulationType;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.ProductCategory;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeListResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;
import com.param.service.global.SessionService;

@Repository("FormulationTypeDao")
public class FormulationTypeDao extends AbstractDao<Integer, FormulationType> implements IFormulationTypeDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<FormulationType> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(FormulationType.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));

		Criteria unitCriteria = criteria.createCriteria("organization");
        unitCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));

		/*Criteria assetCategoryCriteria = productCategoryCriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetTypeCriteria = assetCategoryCriteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false));
*/
		return criteria.list();
	}

	@Override
	public FormulationType getById(Integer id, Integer orgId) {
		Criteria criteria = getSession().createCriteria(FormulationType.class).createAlias("organization","organization");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));
		return (FormulationType) criteria.uniqueResult();
	}

	@Override
	public Integer create(FormulationType formulationType) {
		getSession().save(formulationType);
		return formulationType.getId();
	}

	@Override
	public ProductCategory getId(Integer id) {
		return (ProductCategory) getSession().get(ProductCategory.class, id);
	}

	@Override
	public Integer update(FormulationType formulationType) {
		getSession().update(formulationType);
		return formulationType.getId();
	}
	
	/*@Override
	public List<FormulationType> getFormulationTypeList() {
		String hql = "SELECT formulationType FROM FormulationType AS formulationType \r\n"
				+ "WHERE formulationType.isActive = true AND formulationType.isDeleted = false AND formulationType.unit.unitId = :unitId\r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
		
	}*/
	@Override
	public Long getFormulationTypeCountFromItem(Integer id) {
		String hql = " SELECT count(item) from Item as item WHERE item.formulationType.id = :id AND item.isActive = true AND item.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	@Override
	public Long getFormulationTypeCountFromFormulaROA(Integer id) {
		String hql = " SELECT count(formulation_and_ROA) from FormulationAndRoa as formulation_and_ROA WHERE formulation_and_ROA.formulationType.id = :id AND formulation_and_ROA.isActive = true AND formulation_and_ROA.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormulationTypeListResponse> getAllFormulation(Integer organizationId) {

		String hql = "select formulationType.id as id, formulationType.type as type from "
				+ "FormulationType formulationType where formulationType.isActive = true AND formulationType.isDeleted = false and formulationType.organization =:organizationId ";

		List<FormulationTypeListResponse> list = getSession().createQuery(hql)
				.setParameter("organizationId", organizationId)
				.setResultTransformer(Transformers.aliasToBean(FormulationTypeListResponse.class)).list();
		return list;
	}
}
