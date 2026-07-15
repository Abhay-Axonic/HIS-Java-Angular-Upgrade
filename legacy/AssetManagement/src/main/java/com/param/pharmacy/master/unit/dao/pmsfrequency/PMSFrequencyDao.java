package com.param.pharmacy.master.unit.dao.pmsfrequency;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.pmsfrequency.dao.IPMSFrequencyDao;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.master.AssetCategory;
import com.param.service.global.SessionService;

@Repository
public class PMSFrequencyDao extends AbstractDao<Integer, PMSFrequency> implements IPMSFrequencyDao {
	@Autowired
	SessionService sessionService;

	@Override
	public void save(PMSFrequency frq) {

		getSession().save(frq);

	}

	@Override
	public List<PMSFrequency> list(Integer unitId) {

		Criteria criteria = getSession().createCriteria(PMSFrequency.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));

		Criteria unitCriteria = criteria.createCriteria("unit");
		unitCriteria.add(Restrictions.eq("unitId", unitId != null ? unitId : sessionService.getUnitId()));

		return criteria.list();

	}

	@Override
	public PMSFrequency getPmsFrequency(Integer id) {
		// TODO Auto-generated method stub
		return (PMSFrequency) getSession().load(PMSFrequency.class, id);
	}

	@Override
	public void update(PMSFrequency frq) {

		getSession().update(frq);

	}
}
