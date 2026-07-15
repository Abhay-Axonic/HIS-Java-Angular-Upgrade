package com.param.rabbitmq.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;

@Repository("ConsumerDao")
public class ConsumerDao extends AbstractDao<Integer, Item> implements IConsumerDao {
	
	@Override
	public Item getById(Integer id) {
		Criteria criteria = getSession().createCriteria(Item.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("isActive", true));
		return (Item) criteria.uniqueResult();
	}

	@Override
	public Store getNarcoticStores() {
		Criteria criteria = getSession().createCriteria(Store.class);
		criteria.addOrder(Order.desc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("isActive", true));

		Criteria storeTypeCriteria = criteria.createCriteria("storeType");
		storeTypeCriteria.add(Restrictions.eq("isDeleted", false));
		storeTypeCriteria.add(Restrictions.eq("code", "NAR"));
		
		return (Store) criteria.uniqueResult();
	}

	@Override
	public Store getIPStore() {
		Criteria criteria = getSession().createCriteria(Store.class);
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.addOrder(Order.desc("storeId"));
		criteria.add(Restrictions.eq("isActive", true));
		criteria.setMaxResults(1);
		
		Criteria visitTypeCriteria = criteria.createCriteria("visitType");
		visitTypeCriteria.add(Restrictions.eq("visitTypeCode", "IP"));
		return (Store) criteria.uniqueResult();
	}

	@Override
	public Long create(Prescription prescription) {
		getSession().save(prescription);
		return prescription.getId();
	}

	@Override
	public Integer create(PrescriptionFrequencyDetail prescriptionFrequencyDetail) {
		getSession().save(prescriptionFrequencyDetail);
		return prescriptionFrequencyDetail.getId();
	}

}
