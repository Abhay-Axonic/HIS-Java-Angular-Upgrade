package com.param.pharmacy.master.unit.dao.implantItemMaster;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.Tax;


@Repository("ImlantItemMasterDao")
public class ImlantItemMasterDao extends AbstractDao<Integer, Item> implements IImplantItemMasterDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	SessionService sessionService;
	
	@Override
	public List<Item> getAll(Integer unitId) {
		/*String hql = "SELECT item FROM Item AS item" + " where item.isActive=true AND item.isDeleted=false AND item.isImplant=true " +
				" AND item.organization.organizationId=:organizationId"
					+ " ORDER BY item.id DESC";
		*/
		String hql = "SELECT item FROM Item AS item" + " where item.isDeleted=false AND item.isImplant=true " +
				" AND item.unit.unitId=:unitId"
					+ " ORDER BY item.id DESC";


		Query query = getSession().createQuery(hql);
		query.setParameter("unitId",unitId!=null ? unitId : sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public Item getItem(Integer id) {
		String hql = "SELECT item FROM Item AS item " + "WHERE item.id = :id  AND item.isDeleted = false And isImplant=true";
		// + "WHERE rack.id = :id AND rack.isActive = true AND rack.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Item) query.uniqueResult();
	}

	@Override
	public Item getById(Integer id) {
		String hql = "SELECT item FROM Item AS item " + "WHERE item.id = :id  AND item.isDeleted = false AND item.isImplant=true ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Item) query.uniqueResult();
	}
	
	@Override
	public Long getproductCategoryCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.productCategory as productCategory WITH productCategory.isActive = false AND productCategory.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	@Override
	public Long getmanufactoreCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.manufacturer as manufacturer WITH manufacturer.isActive = false AND manufacturer.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	
	@Override
	public Long gettaxCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.salesTax as salesTax WITH salesTax.isActive = false AND salesTax.isDeleted = false "
				+ " INNER JOIN item.purchaseTax as purchaseTax WITH purchaseTax.isActive = false AND purchaseTax.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	@Override
	public Long getstorageUnitCount(Integer id) {
		String hql = "SELECT count(item.id) FROM Item as item "
				+ " INNER JOIN item.storageUnit as storageUnit WITH storageUnit.isActive = false AND storageUnit.isDeleted = false "
				+ " WHERE item.id =:id AND item.isActive = true AND item.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	@Override
	public Manufacturer getManufacture(Integer genericId) {
		return (Manufacturer) getSession().get(Manufacturer.class, genericId);
	}
	
	@Override
	public ProductCategory getProductCategory(Integer genericId) {
		return (ProductCategory) getSession().get(ProductCategory.class, genericId);
	}
	
	@Override
	public StorageUnit getStorageUnit(Integer storageUnitId) {
		return (StorageUnit) getSession().get(StorageUnit.class, storageUnitId);
	}
	

	@Override
	public Tax getTax(Integer id) {
		return (Tax) getSession().get(Tax.class, id);
	}

	@Override
	public Item implantItemMasterSave(Item implantItemMaster) {
		getSession().save(implantItemMaster);
		return implantItemMaster;
		
	}
	
	@Override
	public Item updateItem(Item item) {
		getSession().update(item);
		return item;
	}
	
}
