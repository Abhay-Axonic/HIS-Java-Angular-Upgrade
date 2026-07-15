package com.param.pharmacy.master.global.service.equipment;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Equipment;
import com.param.pharmacy.master.global.dao.equipment.IEquipmentDao;
import com.param.pharmacy.master.global.dto.equipment.EquipmentRequest;
import com.param.pharmacy.master.global.dto.equipment.EquipmentResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("EquipmentService")
public class EquipmentService implements IEquipmentService {

	@Autowired
	IEquipmentDao EquipmentDao;
	
	@Autowired
	private SessionService sessionService;

	 @Override
		@Transactional
		public List<EquipmentResponse> getAll() {
			List<EquipmentResponse> responseList = new ArrayList<EquipmentResponse>();      
	       
			for (Equipment equipment : EquipmentDao.getAll()) {
				EquipmentResponse equipmentResponse = new EquipmentResponse(equipment.getId(), equipment.getIsActive(), equipment.getDescription(),
						 equipment.getCode());		
				
				responseList.add(equipmentResponse);
			}
			
			return responseList;
			}
	
	@Override
	@Transactional
	public CommonResponse update(EquipmentRequest request) {

		Equipment equipment = EquipmentDao.getEquipment(request.getId());

		equipment.updateEquipment(request.getCode(), request.getDescription());

		equipment.setUpdatedDate(TimeZoneComponent.getDateByzone(equipment.getUnitId()));
		equipment.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		EquipmentDao.update(equipment);

		CommonResponse response = new CommonResponse();
		response.setMessage("Equipment updated successfully");

		return response;
	}
	
	@Override
	@Transactional
	public CommonResponse saveEquipment(EquipmentRequest request) {
		
		Equipment equipment = new Equipment(request.getCode(), request.getDescription());
		//sessionService.setEntityDefaults(equipment);

		equipment.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
 
		Equipment equipmentresponse = EquipmentDao.saveEquipment(equipment);

		CommonResponse response = new CommonResponse();
		response.setId(equipmentresponse.getId().longValue());
		response.setMessage("Equipment saved successfully");

		return response;
	}

	@Override
	public CommonResponse create(EquipmentRequest equipmentRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EquipmentResponse getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}



	
}
