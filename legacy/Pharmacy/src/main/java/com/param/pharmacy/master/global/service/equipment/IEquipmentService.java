package com.param.pharmacy.master.global.service.equipment;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.equipment.EquipmentRequest;
import com.param.pharmacy.master.global.dto.equipment.EquipmentResponse;

public interface IEquipmentService {

	CommonResponse saveEquipment(EquipmentRequest equipmentRequest);

	CommonResponse update(EquipmentRequest equipmentRequest);

	CommonResponse create(EquipmentRequest equipmentRequest);

	EquipmentResponse getById(Integer id);

	List<EquipmentResponse> getAll();

}
