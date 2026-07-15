package com.param.pharmacy.master.unit.dao.configuration;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.PrefixConfiguration;
import com.param.entity.model.master.PrefixRunningNumber;
import com.param.entity.model.master.Store;
import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDto;
import com.param.service.global.SessionService;

@Repository("PrefixConfigurationDao")
public class PrefixConfigurationDao extends AbstractDao<Integer, PrefixConfiguration> implements IPrefixConfigurationDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public Integer create(PrefixConfiguration prefixConfiguration) {
		getSession().save(prefixConfiguration);
		return prefixConfiguration.getId();
	}

	@Override
	public PrefixConfiguration getById(Integer id) {
		Criteria criteria = getSession().createCriteria(PrefixConfiguration.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		return (PrefixConfiguration) criteria.uniqueResult();
	}

	@Override
	public PrefixConfiguration getByScreenId(PrefixConfigurationDto obj) 
	{
		
		
		Criteria criteria = getSession().createCriteria(PrefixConfiguration.class);
		Criteria screenCriteria = criteria.createCriteria("screen");
		Criteria unitCriteria = criteria.createCriteria("unit");
		Criteria orgCriteria = criteria.createCriteria("organization");
		
		if(obj.getFlag()==true)
		{
			orgCriteria.add(Restrictions.eq("organizationId" , sessionService.getOrganizationId()));
			screenCriteria.add(Restrictions.eq("isGlobal",true));
			screenCriteria.add(Restrictions.eq("id",obj.getScreenId()));
		}else{
			unitCriteria.add(Restrictions.eq("unitId" , sessionService.getUnitId()));
			screenCriteria.add(Restrictions.eq("isGlobal",false));
			screenCriteria.add(Restrictions.eq("id",obj.getScreenId()));
		}
		criteria.add(Restrictions.eq("isDeleted", false));
		
		return (PrefixConfiguration) criteria.uniqueResult();
	}
	
	@Override
	public Integer updatePrefixConfiguration(PrefixConfiguration prefixConfiguration) {
		
		getSession().update(prefixConfiguration);
		return prefixConfiguration.getId();
	}

	@Override
	public List<PrefixConfiguration> getAll(Boolean flag,Integer orgId, Integer unitId) {
		Criteria criteria = getSession().createCriteria(PrefixConfiguration.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		Criteria unitCriteria = criteria.createCriteria("unit");
		Criteria unitCriteria1 = criteria.createCriteria("organization");
		Criteria screenCriteria = criteria.createCriteria("screen");
		
		//for unit
		if(flag == true) {
		
			unitCriteria.add(Restrictions.eq("unitId", unitId!=null ? unitId : sessionService.getUnitId()));
			unitCriteria1.add(Restrictions.eq("organizationId" , orgId!=null ? orgId : sessionService.getOrganizationId()));
			screenCriteria.add(Restrictions.eq("isGlobal",false));
			//isGlobal False from screen master
		}
		
		//for global
		if(flag == false) {
			unitCriteria1.add(Restrictions.eq("organizationId" , orgId!=null ? orgId : sessionService.getOrganizationId()));
			screenCriteria.add(Restrictions.eq("isGlobal",true));
			//isGlobal true from screen master
		}
		
		return criteria.list();
	}

	@Override
	public List<Store> getStoreList(int unitId) {
		
		String hql = "SELECT store FROM Store AS store "
				+ "WHERE store.isDeleted = false AND store.isActive = true AND store.unit.unitId = :unitId "
				+ " ORDER BY store.storeId";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	}

	@Override
	public Long save(PrefixRunningNumber prefixRunningNumber) {
		getSession().save(prefixRunningNumber);
		return prefixRunningNumber.getId();
	}
}
