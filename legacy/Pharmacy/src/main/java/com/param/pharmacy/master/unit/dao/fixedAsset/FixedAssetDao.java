package com.param.pharmacy.master.unit.dao.fixedAsset;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.Constants;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.service.global.SessionService;


@Repository("FixedAssetDao")
public class FixedAssetDao extends AbstractDao<Integer, Item> implements IFixedAssetDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<Item> getAll() {
		Criteria criteria = getSession().createCriteria(Item.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria manufacturercriteria = criteria.createCriteria("manufacturer");
		manufacturercriteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria unitCriteria = criteria.createCriteria("unit");
		unitCriteria.add(Restrictions.eq("unitId", sessionService.getUnitId()));
		
		Criteria productCategorycriteria = criteria.createCriteria("productCategory");
		productCategorycriteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria assetCategoryCriteria = productCategorycriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));

		Criteria assetTypeCriteria = assetCategoryCriteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false));
		assetTypeCriteria.add(Restrictions.eq("id", Constants.fixedAssetId));

		return criteria.list();
	}

	@Override
	public Item getById(Integer id) {
		Criteria criteria = getSession().createCriteria(Item.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));

		return (Item) criteria.uniqueResult();
	}

	@Override
	public Integer create(Item item) {

		Integer existingId = (Integer) getSession()
				.createQuery("SELECT i.id FROM Item i WHERE i.organizationId = :organizationId AND i.unitId = :unitId AND i.name = :name")
				.setParameter("organizationId", item.getOrganizationId())
				.setParameter("unitId", item.getUnitId())
				.setParameter("name", item.getName())
				.uniqueResult();

		if (existingId != null) {
			return existingId;
		}
		getSession().save(item);
		return item.getId();
	}

	@Override
	public ProductCategory getProductCategory(Integer productCategoryId) {
		return (ProductCategory) getSession().get(ProductCategory.class, productCategoryId);
	}

	@Override
	public Manufacturer getManufacturer(Integer manufactureId) {
		return (Manufacturer) getSession().get(Manufacturer.class, manufactureId);
	}

	@Override
	public Integer update(Item item) {
		getSession().update(item);
		return item.getId();
	}

	@Override
	public ItemRequestDetail getItemRequestDetail(Integer id) {
		
		String hql = "SELECT itemRequestDetail FROM com.param.entity.model.procurement.ItemRequestDetail AS itemRequestDetail "
				+ "WHERE itemRequestDetail.id =:id";
		
		org.hibernate.Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (com.param.entity.model.procurement.ItemRequestDetail) query.uniqueResult();

	}

	@Override
	public Integer updateItemRequestDetail(ItemRequestDetail itemRequestDetail) {
		getSession().update(itemRequestDetail);
		return itemRequestDetail.getId();

	}

	@Override
	public List<Item> getAssetNameListByAssetCategoryId(Integer assetCategoryId) {
		Criteria criteria = getSession().createCriteria(Item.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria manufacturercriteria = criteria.createCriteria("manufacturer");
		manufacturercriteria.add(Restrictions.eq("isDeleted", false));

		
		Criteria productCategorycriteria = criteria.createCriteria("productCategory");
		productCategorycriteria.add(Restrictions.eq("isDeleted", false));
		
		Criteria assetCategoryCriteria = productCategorycriteria.createCriteria("assetCategory");
		assetCategoryCriteria.add(Restrictions.eq("isDeleted", false));
		assetCategoryCriteria.add(Restrictions.eq("id", assetCategoryId));

		Criteria assetTypeCriteria = assetCategoryCriteria.createCriteria("assetType");
		assetTypeCriteria.add(Restrictions.eq("isDeleted", false));
		assetTypeCriteria.add(Restrictions.eq("id", Constants.fixedAssetId));

		return criteria.list();

	}

}
