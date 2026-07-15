package com.param.pharmacy.master.unit.dao.mainStore;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreType;
import com.param.entity.model.master.VisitType;
import com.param.service.global.SessionService;

@Repository("MainStoreDao")
public class MainStoreDao extends AbstractDao<Integer, Store> implements IMainStoreDao {
	
    @Autowired
    SessionService sessionService;

	@Override
	public List<Store> getAll(Integer unitId , Integer orgId) {
		Criteria criteria = getSession().createCriteria(Store.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		//criteria.add(Restrictions.eq("isActive", true));

		Criteria storeTypeCriteria = criteria.createCriteria("storeType");
		storeTypeCriteria.add(Restrictions.eq("isDeleted", false));
		storeTypeCriteria.add(Restrictions.eq("isActive", true));
		//storeTypeCriteria.add(Restrictions.eq("unitId", sessionService.getUnitId()));
        
		//storeTypeCriteria
		//HIS-11207
		//Criteria storeTypeCriteria1 = storeTypeCriteria.createCriteria("unit");
		//storeTypeCriteria1.add(Restrictions.eq("unitId", sessionService.getUnitId()));

		
        Criteria orgCriteria = criteria.createCriteria("organization");
		orgCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));

		Criteria unitCriteria = criteria.createCriteria("unit");
		unitCriteria.add(Restrictions.eq("unitId", unitId!=null ? unitId : sessionService.getUnitId()));

		return criteria.list();
	}

	@Override
	public Store getById(Integer id) {
		Criteria criteria = getSession().createCriteria(Store.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		return (Store) criteria.uniqueResult();
	}

	@Override
	public Integer create(Store store) {
		getSession().save(store);
		return store.getStoreId();
	}

	@Override
	public StoreType getStoretype(Integer storeTypeId) {
		return (StoreType) getSession().get(StoreType.class, storeTypeId);
	}

	@Override
	public Integer update(Store store) {
		getSession().update(store);
		return store.getStoreId();
	}

	@Override
	public VisitType getVisitType(Integer visitTypeId) {
		return (VisitType) getSession().get(VisitType.class, visitTypeId);
	}

}
