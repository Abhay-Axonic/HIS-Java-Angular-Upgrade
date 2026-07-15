package com.param.pharmacy.master.global.dao.equipment;

import java.util.List;

import com.param.entity.model.master.Equipment;

public interface IEquipmentDao {

	List<Equipment> getAll();
	
	Integer update(Equipment equipment);
	
	Equipment saveEquipment(Equipment equipment);

	Equipment getEquipment(Integer id);

}
