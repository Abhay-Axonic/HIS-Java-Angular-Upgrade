package com.param.pharmacy.master.unit.dao.pharmacologicalClassification;

import java.util.List;

import com.param.entity.model.master.Unit;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationListResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.service.global.SessionService;

@Repository("PharmacologicalClassificationDao")
public class PharmacologicalClassificationDao extends AbstractDao<Integer, PharmacologicalClassification>
		implements IPharmacologicalClassificationDao {
	
	@Autowired
    SessionService sessionService;

	@Override
	public List<PharmacologicalClassification> getAll(Integer orgId) {
		Criteria criteria = getSession().createCriteria(PharmacologicalClassification.class);
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		
		/*Criteria unitCriteria = criteria.createCriteria("unit");
        unitCriteria.add(Restrictions.eq("unitId", sessionService.getUnitId()));*/
		Criteria unitCriteria = criteria.createCriteria("organization");
		unitCriteria.add(Restrictions.eq("organizationId",orgId != null ? orgId : sessionService.getOrganizationId()));



		return criteria.list();
	}

	@Override
	public PharmacologicalClassification getById(Integer id) {
		Criteria criteria = getSession().createCriteria(PharmacologicalClassification.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		return (PharmacologicalClassification) criteria.uniqueResult();
	}

	@Override
	public Integer create(PharmacologicalClassification pharmaClassification) {
		getSession().save(pharmaClassification);
		return pharmaClassification.getId();
	}

	@Override
	public Integer update(PharmacologicalClassification pharmacologicalClassification) {
		getSession().update(pharmacologicalClassification);
		return pharmacologicalClassification.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PharmacologicalClassificationListResponse> getAllPharmacology(Integer organizationId) {

		String hql = "select phar.id as id, phar.classification as classification from "
				+ "PharmacologicalClassification phar where phar.isActive = true AND phar.isDeleted = false  AND phar.organizationId = "+organizationId;

		List<PharmacologicalClassificationListResponse> list = getSession().createQuery(hql).setResultTransformer(
						Transformers.aliasToBean(PharmacologicalClassificationListResponse.class)).list();
		return list;
	}

}
