package com.param.pharmacy.master.global.dao.otherCharges;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.OtherCharge;
import com.param.service.global.SessionService;

@Repository("OtherChargesDao")
public class OtherChargesDao extends AbstractDao<Integer, OtherCharge> implements IOtherChargesDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<OtherCharge> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(OtherCharge.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("organization");
        unitCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));

		return criteria.list();
	}

	@Override
	public OtherCharge getById(Integer id) {
		Criteria criteria = getSession().createCriteria(OtherCharge.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (OtherCharge) criteria.uniqueResult();
	}

	@Override
	public Integer create(OtherCharge otherCharge) {
		getSession().save(otherCharge);
		return otherCharge.getId();
	}

	@Override
	public Integer update(OtherCharge otherCharge) {
		getSession().update(otherCharge);
		return otherCharge.getId();
	}

}
