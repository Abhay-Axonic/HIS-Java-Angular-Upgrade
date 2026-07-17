package com.param.pharmacy.master.global.service.strengthUnit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.StrengthUnit;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.strengthUnit.IStrengthUnitDao;
import com.param.pharmacy.master.global.dto.strengthUnit.RabbitStrengthUnitDto;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitListResponse;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitRequest;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StrengthUnitService")
public class StrengthUnitService implements IStrengthUnitService {

	@Autowired
	IStrengthUnitDao StrengthUnitDao;
	
	@Autowired
	IEmrService EmrService;
	

	@Autowired
	private SessionService sessionService;


	@Override
	@Transactional
	public List<StrengthUnitListResponse> getAll(Integer orgId) {
		List<StrengthUnitListResponse> list = new ArrayList<StrengthUnitListResponse>();

		List<StrengthUnit> lists = StrengthUnitDao.getAll(orgId);
		for (StrengthUnit strengthUnit : lists) {
			StrengthUnitListResponse StrengthUnitRes = new StrengthUnitListResponse();
			StrengthUnitRes.setId((Integer) strengthUnit.getId());
			StrengthUnitRes.setCode((String) strengthUnit.getCode());
			StrengthUnitRes.setUnit((String) strengthUnit.getUnits());
			StrengthUnitRes.setStatus((Boolean) strengthUnit.getIsActive());
			list.add(StrengthUnitRes);
		}
		return list;
	}

	@Override
	@Transactional
	public StrengthUnitDetailsResponse getById(Integer id) {
		StrengthUnitDetailsResponse response = new StrengthUnitDetailsResponse();

		StrengthUnit strengthUnit = StrengthUnitDao.getById(id);

		if (strengthUnit != null) {
			response.setId((Integer) strengthUnit.getId());
			response.setCode((String) strengthUnit.getCode());
			response.setUnit((String) strengthUnit.getUnits());
			response.setStatus((Boolean) strengthUnit.getIsActive());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(StrengthUnitRequest strengthUnitRequest) {

		StrengthUnit strengthUnit = new StrengthUnit();

		strengthUnit.setCode(strengthUnitRequest.getCode());
		strengthUnit.setUnits(strengthUnitRequest.getUnit());
		strengthUnit.setIsActive(strengthUnitRequest.getStatus());

		CommonResponse response = new CommonResponse();
		//sessionService.setEntityDefaults(strengthUnit);
		strengthUnit.setCreateDefaults(strengthUnitRequest.getOrganizationId()!=null ? strengthUnitRequest.getOrganizationId() : sessionService.getOrganizationId(),strengthUnitRequest.getUnitId() !=null ? strengthUnitRequest.getUnitId() :sessionService.getUnitId() ,strengthUnitRequest.getUserId()!=null ? strengthUnitRequest.getUserId() : sessionService.getUserId());
		response.setId(StrengthUnitDao.create(strengthUnit).longValue());
		response.setMessage("Strength Unit Inserted Successfully");

		RabbitStrengthUnitDto rabbitStrengthUnitDto = new RabbitStrengthUnitDto(response.getId().intValue(),
				strengthUnit.getCode(),strengthUnit.getUnits(),strengthUnitRequest.getOrganizationId(),
				strengthUnitRequest.getUnitId(),strengthUnit.getIsActive(),strengthUnit.getIsDeleted(),
				strengthUnitRequest.getUserId(),strengthUnit.getAddedDate(),strengthUnitRequest.getUserId(),
				strengthUnit.getUpdatedDate());
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(rabbitStrengthUnitDto);
		request.setKey(MasterKeyConstants.StrengthUnitMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(StrengthUnitRequest strengthUnitRequest) throws CustomeException {

		StrengthUnit strengthUnit = StrengthUnitDao.getById(strengthUnitRequest.getId());

		if (strengthUnitRequest.getCode() != null && strengthUnitRequest.getCode() != "") {
			strengthUnit.setCode(strengthUnitRequest.getCode());
		}

		if (strengthUnitRequest.getUnit() != null && strengthUnitRequest.getUnit() != "") {
			strengthUnit.setUnits(strengthUnitRequest.getUnit());
		}

		if (strengthUnitRequest.getStatus() != null) {
			strengthUnit.setIsActive(strengthUnitRequest.getStatus());
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Strength Unit Updated Successfully");
		Long countFromItem = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (!strengthUnit.getIsActive()) {
			countFromItem = StrengthUnitDao.getStrengthUnitDaoCountFromItem(strengthUnitRequest.getId());
		}
		if (countFromItem <= 0) {
			UnableToUpdateStatus = false;
			strengthUnit.setUpdatedDate(TimeZoneComponent.getDateByzone(strengthUnit.getUnitId()));
			strengthUnit.setUpdatedByUser(strengthUnitRequest.getUserId()!=null ? strengthUnitRequest.getUserId() : sessionService.getUserId());
			if (strengthUnitRequest.getStatus() != null) {
				strengthUnit.setIsActive(strengthUnitRequest.getStatus());
			}
		} else {
			UnableToUpdateStatus = true;
			if (strengthUnitRequest.getStatus() != null) {
				strengthUnit.setIsActive(true);
			}
			response.setMessage("Storage Unit is assigned to Items");
		}

		response.setId(StrengthUnitDao.update(strengthUnit).longValue());
		
		RabbitStrengthUnitDto rabbitStrengthUnitDto = new RabbitStrengthUnitDto(response.getId().intValue(),
				strengthUnit.getCode(),strengthUnit.getUnits(),strengthUnit.getOrganization().getOrganizationId(),
				strengthUnit.getUnit().getUnitId(),strengthUnit.getIsActive(),strengthUnit.getIsDeleted(),
				strengthUnit.getAddedBy().getUserId(),strengthUnit.getAddedDate(),strengthUnit.getUpdatedBy().getUserId(),
				strengthUnit.getUpdatedDate());

		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(rabbitStrengthUnitDto);
		request.setKey(MasterKeyConstants.StrengthUnitMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);
		
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					strengthUnit.getIsActive());
			throw customeException;
		}
		return response;
	}

}
