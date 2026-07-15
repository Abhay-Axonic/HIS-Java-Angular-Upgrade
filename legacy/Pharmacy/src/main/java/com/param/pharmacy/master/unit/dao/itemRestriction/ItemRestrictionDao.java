package com.param.pharmacy.master.unit.dao.itemRestriction;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.StoreItemRestriction;
import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionResponse;

@Repository("ItemRestrictionDao")
public class ItemRestrictionDao extends AbstractDao<Integer, ItemRestrictionResponse> implements IItemRestrictionDao {

	@Override
	public List<StoreItemRestriction> getAll()
	{
		String hql="SELECT itemRestriction FROM StoreItemRestriction AS itemRestriction"
				+ " where itemRestriction.isActive=true " 
				+ "ORDER BY itemRestriction.id DESC";
		// comment as per saurabh AND itemRestriction.isDeleted=false 
		
		Query query = getSession().createQuery(hql);
		return query.list();
	}
	
	@Override
	public StoreItemRestriction getStoreItemRestriction(BigInteger id) {
		String hql = "SELECT itemRestriction FROM StoreItemRestriction AS itemRestriction "
	            + "WHERE itemRestriction.id = :id  AND itemRestriction.isActive = true AND itemRestriction.isDeleted = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (StoreItemRestriction) query.uniqueResult();
	}
	

	@Override
	public StoreItemRestriction saveItemRestriction(StoreItemRestriction itemRestriction) {
		getSession().save(itemRestriction);
		return itemRestriction;
	}


	@Override
	public StoreItemRestriction updateStoreItemRestriction(StoreItemRestriction storeItemRestriction) {
		getSession().update(storeItemRestriction);

		return storeItemRestriction;
	}

}
