package com.param.pharmacy.master.global.dao.storageUnit;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.StorageUnit;
import com.param.service.global.SessionService;

@Repository("StorageUnitDao")
public class StorageUnitDao extends AbstractDao<Integer, StorageUnit> implements IStorageUnitDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<StorageUnit> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(StorageUnit.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("organization");
        unitCriteria.add(Restrictions.eq("organizationId", orgId!=null ? orgId : sessionService.getOrganizationId()));
		
		return criteria.list();
	}

	@Override
	public StorageUnit getById(Integer id) {
		Criteria criteria = getSession().createCriteria(StorageUnit.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (StorageUnit) criteria.uniqueResult();
	}

	@Override
	public Integer create(StorageUnit storageUnit) {
		getSession().save(storageUnit);
		return storageUnit.getId();
	}

	@Override
	public Integer update(StorageUnit storageUnit) {
		getSession().update(storageUnit);
		return storageUnit.getId();
	}

	@Override
	public Long getStorageUnitCountFromItem(Integer id) {
		String hql = " SELECT count(item) from Item as item WHERE item.storageUnit.id = :id AND item.isActive = true AND item.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	
}
