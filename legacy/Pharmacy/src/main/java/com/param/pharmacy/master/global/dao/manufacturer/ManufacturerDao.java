package com.param.pharmacy.master.global.dao.manufacturer;

import java.util.List;

import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerListResponse;
import com.param.service.global.SessionService;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Manufacturer;

@Repository("ManufacturerDao")
public class ManufacturerDao extends AbstractDao<Integer, Manufacturer> implements IManufacturerDao {
	
	@Autowired
	SessionService sessionService;

	@Override
	public List<ManufacturerListResponse> getAll() {
		String hql = "SELECT manufacturer.id AS id, manufacturer.code AS code, " +
				"	manufacturer.name AS name, manufacturer.address AS address, manufacturer.isActive AS status "
				+ "	FROM Manufacturer AS manufacturer "
				+ "	WHERE manufacturer.isDeleted = false AND manufacturer.organization.organizationId = :orgId "
				+ "	ORDER BY manufacturer.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("orgId", sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(ManufacturerListResponse.class));

		return query.list();
	}

	@Override
	public Manufacturer getById(Integer id, Integer orgId) {
		Criteria criteria = getSession().createCriteria(Manufacturer.class).createAlias("organization","organization");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId",orgId!=null ? orgId : sessionService.getOrganizationId()));
		return (Manufacturer) criteria.uniqueResult();
	}

	@Override
	public Integer create(Manufacturer manufacturer) {
		getSession().save(manufacturer);
		return manufacturer.getId();
	}

	@Override
	public Integer update(Manufacturer manufacturer) {
		getSession().update(manufacturer);
		return manufacturer.getId();
	}

	@Override
	public Long getManufacturerCountFromItem(Integer id) {
		String hql = " SELECT count(item) from Item as item WHERE item.manufacturer.id = :id AND item.isActive = true AND item.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	@Override
	public Long getManufacturerCountFromVendor(Integer id) {
		String hql = " SELECT count(vendor) from Vendor as vendor WHERE vendor.manufacturer.id = :id AND vendor.isActive = true AND vendor.isDeleted = false";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
}
