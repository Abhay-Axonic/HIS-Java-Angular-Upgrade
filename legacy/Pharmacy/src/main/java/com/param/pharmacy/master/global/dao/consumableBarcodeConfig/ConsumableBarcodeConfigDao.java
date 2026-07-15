package com.param.pharmacy.master.global.dao.consumableBarcodeConfig;


import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.ConsumableBarcodeConfig;
import com.param.pharmacy.master.global.dto.consumableBarcodeConfig.ConsumableBarcodeConfigResponse;

@Repository("ConsumableBarcodeConfigDao")
public class ConsumableBarcodeConfigDao extends AbstractDao<Integer, ConsumableBarcodeConfigResponse> implements IConsumableBarcodeConfigDao {

	@Override
	public List<ConsumableBarcodeConfig> getAll(Integer unitId)
	{
		String hql="SELECT consumableBarcodeConfig FROM ConsumableBarcodeConfig AS consumableBarcodeConfig"
				+ " where consumableBarcodeConfig.isActive=true AND consumableBarcodeConfig.isDeleted=false  and consumableBarcodeConfig.unit.unitId = :unitId " 
				+ "ORDER BY consumableBarcodeConfig.id DESC";
				
		Query query = getSession().createQuery(hql).setParameter("unitId", unitId);
		return query.list();
	}
	@Override
	public ConsumableBarcodeConfig getConsumableBarcodeConfig(Integer id) {
		String hql = "SELECT consumableBarcodeConfig FROM ConsumableBarcodeConfig AS consumableBarcodeConfig "
	            + "WHERE consumableBarcodeConfig.id = :id  AND consumableBarcodeConfig.isActive = true AND consumableBarcodeConfig.isDeleted = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (ConsumableBarcodeConfig) query.uniqueResult();
	}

	@Override
	public ConsumableBarcodeConfig saveConsumableBarcodeConfig(ConsumableBarcodeConfig consumableBarcodeConfig) {
		getSession().save(consumableBarcodeConfig);
		return consumableBarcodeConfig;
	}

	
	@Override
	public Integer update(ConsumableBarcodeConfig consumableBarcodeConfig) {
		getSession().update(consumableBarcodeConfig);
		return consumableBarcodeConfig.getId();
	}
	
}
