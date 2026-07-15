package com.param.pharmacy.master.global.dao.unitOfMeasurement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.UnitOfMeasurement;
import com.param.global.dto.items.UomResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementListResponse;
import com.param.service.global.SessionService;

@Repository("UnitOfMeasurementDao")
public class UnitOfMeasurementDao extends AbstractDao<Integer, UnitOfMeasurement> implements IUnitOfMeasurementDao {
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	SessionService sessionService;

	
	@Override
	public List<UnitOfMeasurement> getAll() {
		String hql = "SELECT uom  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ " WHERE uom.isDeleted = false AND uom.isActive = true AND uom.storeUom = true AND uom.organization.organizationId=:organizationId"
				+ "ORDER BY uom.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("organizationId",sessionService.getOrganizationId());
		return query.list();
	}
	
	
	
	@Override
	public List<UnitOfMeasurement> getAllByItemId(Integer itemId,Integer orgId) {
		String hql = "SELECT uom  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ " WHERE uom.item.id = :itemId AND uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId=:organizationId "
				+ " ORDER BY uom.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("itemId",itemId);
		query.setParameter("organizationId",orgId!=null ? orgId : sessionService.getOrganizationId());
		return query.list();
	}

	@Override
	public UnitOfMeasurement getById(Integer id,Integer orgId) {
		Criteria criteria = getSession().createCriteria(UnitOfMeasurement.class).createAlias("organization","organization");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId",orgId!=null ? orgId : sessionService.getOrganizationId()));
		return (UnitOfMeasurement) criteria.uniqueResult();
	}
	
	@Override
	public UnitOfMeasurement getUOMByItem(Integer itemId,Integer unitTypeId, Integer orgId) {
		String hql = "SELECT uom  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ " WHERE uom.item.id = :itemId AND uom.uomType.id= :unitTypeId AND uom.isDeleted = false AND uom.isActive = true" +
				" AND uom.organization.organizationId=:organizationId "
				+ " ORDER BY uom.id desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("itemId",itemId);
		query.setParameter("unitTypeId",unitTypeId);
		query.setParameter("organizationId" ,orgId!=null ? orgId : sessionService.getOrganizationId()).setMaxResults(1);
		return (UnitOfMeasurement) query.uniqueResult();
	}

	@Override
	public Integer create(UnitOfMeasurement unitOfMeasurement) {
		getSession().save(unitOfMeasurement);
		/*getSession().merge(unitOfMeasurement);*/
		return unitOfMeasurement.getId();
	}

		
	@Override
	public Integer update(UnitOfMeasurement unitOfMeasurement) {
		getSession().update(unitOfMeasurement);
		return unitOfMeasurement.getItem().getId();
	}

	@Override
	public Item getItem(Integer itemId) {
		return (Item) getSession().get(Item.class, itemId);
	}



	@Override
	public List<UnitOfMeasurementListResponse> getAllUoms(Integer orgId, Integer offset, Integer noOfRecordsPerPage, String itemName) {
		String hql = "SELECT uom.isActive as status, item.id as itemId, assetCategory.id as assetCategoryId, "
				+ "assetCategory.category as assetCategory, productCategory.id as productCategoryId, productCategory.category as productCategory, "
				+ "item.code as itemCode, item.name as itemName "
				+ "  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ "INNER JOIN uom.item item "
				+ "INNER JOIN item.generic generic "
				+ "INNER JOIN generic.productCategory productCategory "
				+ "INNER JOIN productCategory.assetCategory assetCategory "
				+ " WHERE uom.isDeleted = false AND uom.isActive = true AND uom.storeUom = true AND uom.organization.organizationId = :orgId and  (:itemName = '' OR upper(item.name) like upper(:itemName))"
				+ "ORDER BY uom.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("orgId",orgId!=null ? orgId : sessionService.getOrganizationId());
		query.setParameter("itemName", itemName != null && !itemName.equalsIgnoreCase("undefined")? '%'+itemName.toUpperCase() +'%' : "");
		query.setFirstResult(offset != null ? offset : 0);
		query.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage :10);
		query.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurementListResponse.class));
		return query.list();
	}

	@Override
	public List<UnitOfMeasurementListResponse> getByItemId(Integer itemId, Integer orgId) {
		String hql = "SELECT uom.isActive as status, item.id as itemId, assetCategory.id as assetCategoryId, "
				+ "assetCategory.category as assetCategory, productCategory.id as productCategoryId, productCategory.category as productCategory, "
				+ "item.code as itemCode, item.name as itemName "
				+ "  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ "INNER JOIN uom.item item "
				+ "INNER JOIN item.generic generic "
				+ "INNER JOIN generic.productCategory productCategory "
				+ "INNER JOIN productCategory.assetCategory assetCategory "
				+ " WHERE uom.isDeleted = false AND uom.isActive = true AND uom.storeUom = true AND uom.organization.organizationId = :orgId and item.id= :itemId "
				+ "ORDER BY uom.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("orgId",orgId!=null ? orgId : sessionService.getOrganizationId());
		query.setParameter("itemId",itemId);
		query.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurementListResponse.class));
		return query.list();
	}


	@Override
	public List<UomResponse> getAllByItemIds(List<Integer> itemIds) {
		String hql = "SELECT uom.id as id, uomType.type as uomType, uomType.id as uomTypeId, uomUnit.id as uomUnitId, uomUnit.units as uomUnit, "
				+ "uom.conversions as conversion, uom.ipUom as ipUom, uom.opUom as opUom, uom.storeUom as storeUom, item.id as itemId "
				+ "  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ "LEFT JOIN uom.uomType uomType "
				+ "LEFT JOIN uom.uomUnit uomUnit "
				+ "LEFT JOIN uom.item as item"
				+ " WHERE item.id in (:itemIds) AND uom.isDeleted = false AND uom.isActive = true AND uom.organization.organizationId = :orgId "
				+ " ORDER BY uom.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("itemIds",itemIds);
		query.setParameter("orgId",sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}



	@Override
	public Integer getAllUomsCount(Integer orgId) {
		String hql = "SELECT uom.isActive as status, item.id as itemId, assetCategory.id as assetCategoryId, "
				+ "assetCategory.category as assetCategory, productCategory.id as productCategoryId, productCategory.category as productCategory, "
				+ "item.code as itemCode, item.name as itemName "
				+ "  FROM com.param.entity.model.master.UnitOfMeasurement AS uom "
				+ "INNER JOIN uom.item item "
				+ "INNER JOIN item.generic generic "
				+ "INNER JOIN generic.productCategory productCategory "
				+ "INNER JOIN productCategory.assetCategory assetCategory "
				+ " WHERE uom.isDeleted = false AND uom.isActive = true AND uom.storeUom = true AND uom.organization.organizationId = :orgId "
				+ "ORDER BY uom.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("orgId",orgId!=null ? orgId : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurementListResponse.class));
		return query.list().size();
	}



	
	


}
