package com.param.pharmacy.master.unit.genralinsurancedao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Area;
import com.param.entity.model.master.GeneralInsurance;
import com.param.entity.model.master.Vendor;

@Repository("GeneralInsuranceMasterDao")
public class GeneralInsuranceMasterDao extends AbstractDao<Integer, GeneralInsurance> implements IGeneralInsuranceMasterDao{
	
	@Override
	public List<GeneralInsurance> getAll()
	{
		Criteria criteria = getSession().createCriteria(GeneralInsurance.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
	    return criteria.list();
	}
	
	@Override
	public GeneralInsurance getById(Integer id) {
		Criteria criteria = getSession().createCriteria(GeneralInsurance.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (GeneralInsurance) criteria.uniqueResult();
	}

	
	@Override
	public Integer save(GeneralInsurance genralInsurance) {
		getSession().save(genralInsurance);
		return genralInsurance.getId();
	}
	

	@Override
	public Integer update(GeneralInsurance genralInsurance) {
		getSession().update(genralInsurance);
		return genralInsurance.getId();
	}
	
	@Override
	public Area getArea(Integer id) {
		return (Area) getSession().get(Area.class, id);
	}

}
