package com.param.pharmacy.master.global.dao.equipment;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Equipment;
import com.param.pharmacy.master.global.dto.equipment.EquipmentResponse;

@Repository("EquipmentDao")
public class EquipmentDao extends AbstractDao<Integer, EquipmentResponse> implements IEquipmentDao {

	@Override
	public List<Equipment> getAll()
	{
		String hql="SELECT equipment FROM Equipment AS equipment"
				+ " where equipment.isActive=true AND equipment.isDeleted=false " 
				+ "ORDER BY equipment.id DESC";
				
		Query query = getSession().createQuery(hql);
		return query.list();
	}
	
	@Override
	public Equipment getEquipment(Integer id) {
		String hql = "SELECT equipment FROM Equipment AS equipment "
	            + "WHERE equipment.id = :id  AND equipment.isActive = true AND equipment.isDeleted = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (Equipment) query.uniqueResult();
	}
	

	@Override
	public Equipment saveEquipment(Equipment equipment) {
		getSession().save(equipment);
		return equipment;
	}

	
	@Override
	public Integer update(Equipment equipment) {
		getSession().update(equipment);
		return equipment.getId();
	}

	
}
