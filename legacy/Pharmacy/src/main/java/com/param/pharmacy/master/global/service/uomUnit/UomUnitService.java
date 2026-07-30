package com.param.pharmacy.master.global.service.uomUnit;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.UomUnit;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.uomUnit.IUomUnitDao;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitListResponse;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitRabbitDto;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitRequest;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("UomUnitService")
@SuppressWarnings("rawtypes")
public class UomUnitService implements IUomUnitService {
	@Autowired
	IUomUnitDao UomUnitDao;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	IOtimsService iOtimsService;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<UomUnitListResponse> getAll(Integer orgId) {
		List<UomUnitListResponse> list = new ArrayList<UomUnitListResponse>();

		for (UomUnit units : UomUnitDao.getAll(orgId)) {
			UomUnitListResponse uomUnitRes = new UomUnitListResponse();
			uomUnitRes.setId((Integer) units.getId());
			uomUnitRes.setCode((String) units.getCode());
			uomUnitRes.setUnits((String) units.getUnits());
			uomUnitRes.setStatus((Boolean) units.getIsActive());
			list.add(uomUnitRes);
		}

		return list;
	}

	@Override
	@Transactional
	public UomUnitDetailsResponse getById(Integer id,Integer orgId) {

		UomUnitDetailsResponse response = new UomUnitDetailsResponse();

		UomUnit uomUnit = UomUnitDao.getById(id, orgId);

		if (uomUnit != null) {
			response.setId((Integer) uomUnit.getId());
			response.setCode((String) uomUnit.getCode());
			response.setUnits((String) uomUnit.getUnits());
			response.setStatus((Boolean) uomUnit.getIsActive());
		} else {

		}

		return response;
	}

	
	@Override
	@Transactional
	public CommonResponse create(UomUnitRequest uomUnitRequest) {

		UomUnit uomUnit = new UomUnit();
		uomUnit.setCode(uomUnitRequest.getCode());
		uomUnit.setUnits(uomUnitRequest.getUnits());
		uomUnit.setIsActive(uomUnitRequest.getStatus());

		CommonResponse response = new CommonResponse();
		//sessionService.setEntityDefaults(uomUnit);
		uomUnit.setCreateDefaults(uomUnitRequest.getOrganizationId()!=null ? uomUnitRequest.getOrganizationId() : sessionService.getOrganizationId(),uomUnitRequest.getUnitId() !=null ? uomUnitRequest.getUnitId() :sessionService.getUnitId() ,uomUnitRequest.getUserId()!=null ? uomUnitRequest.getUserId() : sessionService.getUserId());
		
		response.setId(UomUnitDao.create(uomUnit).longValue());
		response.setMessage("Uom Unit Inserted Successfully");
		uomUnit.setId(response.getId().intValue());
		UomUnitRabbitDto uomUnitRabbitDto = new UomUnitRabbitDto(uomUnit.getId(),uomUnit.getCode(),uomUnit.getUnits(),
				uomUnitRequest.getUserId(),uomUnit.getAddedDate(),uomUnitRequest.getUserId(),uomUnit.getUpdatedDate(),
				uomUnit.getIsDeleted(),uomUnit.getIsActive(),uomUnitRequest.getOrganizationId(),uomUnitRequest.getUnitId());
		
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(uomUnitRabbitDto);
		request.setKey(MasterKeyConstants.UomUnitMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);
		request.setKey(MasterKeyConstants.Unit_Of_Measurements.toString());
		//iOtimsService.publish(request);
		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(UomUnitRequest uomUnitRequest) throws CustomeException {

		UomUnit uomUnit = UomUnitDao.getById(uomUnitRequest.getId(), uomUnitRequest.getOrganizationId());

		if (uomUnit == null) {
			throw new CustomeException(uomUnitRequest.getId() != null ? uomUnitRequest.getId().longValue() : null,
					"Unit not found", false);
		}

		if (uomUnitRequest.getCode() != null && uomUnitRequest.getCode() != "") {
			uomUnit.setCode(uomUnitRequest.getCode());
		}

		if (uomUnitRequest.getUnits() != null && uomUnitRequest.getUnits() != "") {
			uomUnit.setUnits(uomUnitRequest.getUnits());
		}

		if (uomUnitRequest.getStatus() != null) {
			uomUnit.setIsActive(uomUnitRequest.getStatus());
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Uom Unit Updated Successfully");
		Long count = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (!uomUnit.getIsActive()) {
			count = UomUnitDao.getUnitOfMesurementCountFromUnit(uomUnit.getId(),uomUnitRequest.getOrganizationId());
		}
		if (count <= 0) {
			UnableToUpdateStatus = false;
			uomUnit.setUpdatedDate(TimeZoneComponent.getDateByzone(uomUnit.getUnitId()));
			uomUnit.setUpdatedByUser(uomUnitRequest.getUserId()!=null ? uomUnitRequest.getUserId() : sessionService.getUserId());
			if (uomUnitRequest.getStatus() != null) {
				uomUnit.setIsActive(uomUnitRequest.getStatus());
			}
		} else {
			UnableToUpdateStatus = true;
			if (uomUnitRequest.getStatus() != null) {
				uomUnit.setIsActive(true);
			}
			response.setMessage("Unit is assigned to Unit Of Mesurement");
		}

		response.setId(UomUnitDao.update(uomUnit).longValue());
		uomUnit.setId(response.getId().intValue());
		UomUnitRabbitDto uomUnitRabbitDto = new UomUnitRabbitDto(uomUnit.getId(),uomUnit.getCode(),uomUnit.getUnits(),
				uomUnit.getAddedBy().getUserId(),uomUnit.getAddedDate(),uomUnit.getUpdatedBy().getUserId(),uomUnit.getUpdatedDate(),
				uomUnit.getIsDeleted(),uomUnit.getIsActive(),uomUnit.getOrganization().getOrganizationId(),uomUnit.getUnit().getUnitId());

		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(uomUnitRabbitDto);
		request.setKey(MasterKeyConstants.UomUnitMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);
		request.setKey(MasterKeyConstants.Unit_Of_Measurements.toString());
		//iOtimsService.publish(request);

		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					uomUnit.getIsActive());
			throw customeException;
		}
		return response;
	}

}
