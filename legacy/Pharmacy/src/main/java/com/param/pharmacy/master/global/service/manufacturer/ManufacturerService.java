package com.param.pharmacy.master.global.service.manufacturer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Manufacturer;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.manufacturer.IManufacturerDao;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerDetailsResponse;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerListResponse;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerRequset;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.EmrService;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ManufacturerService")
public class ManufacturerService implements IManufacturerService {

	@Autowired
	IManufacturerDao ManufacturerDao;

	@Autowired
	private SessionService sessionService;
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Override
	@Transactional
	public List<ManufacturerListResponse> getAll() {
		return ManufacturerDao.getAll();
	}

	@Override
	@Transactional
	public ManufacturerDetailsResponse getById(Integer id, Integer orgId) {
		ManufacturerDetailsResponse response = new ManufacturerDetailsResponse();

		Manufacturer manufacturer = ManufacturerDao.getById(id, orgId);

		if (manufacturer != null) {
			response.setId((Integer) manufacturer.getId());
			response.setCode((String) manufacturer.getCode());
			response.setName((String) manufacturer.getName());
			response.setAddress((String) manufacturer.getAddress());
			response.setStatus((Boolean) manufacturer.getIsActive());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(ManufacturerRequset manufacturerRequset) {

		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setCode(manufacturerRequset.getCode());
		manufacturer.setName(manufacturerRequset.getName());
		manufacturer.setAddress(manufacturerRequset.getAddress());
		manufacturer.setIsActive(manufacturerRequset.getStatus());

		CommonResponse response = new CommonResponse();
		// sessionService.setEntityDefaults(manufacturer);
		manufacturer.setCreateDefaults(
				manufacturerRequset.getOrganizationId() != null ? manufacturerRequset.getOrganizationId()
						: sessionService.getOrganizationId(),
				manufacturerRequset.getUnitId() != null ? manufacturerRequset.getUnitId() : sessionService.getUnitId(),
				manufacturerRequset.getUserId() != null ? manufacturerRequset.getUserId() : sessionService.getUserId());
		response.setId(ManufacturerDao.create(manufacturer).longValue());
		response.setMessage("Manufacturer Inserted Successfully");

		manufacturer.setId(response.getId().intValue());
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(manufacturer);
		request.setKey(MasterKeyConstants.ManufacturerMasterHISKey.toString());
		request.setPayload(payload);
		IEmrService emrService = new EmrService();
		// EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(ManufacturerRequset manufacturerRequset) throws CustomeException {

		Manufacturer manufacturer = ManufacturerDao.getById(manufacturerRequset.getId(),
				manufacturerRequset.getOrganizationId());

		if (manufacturerRequset.getCode() != null && manufacturerRequset.getCode() != "") {
			manufacturer.setCode(manufacturerRequset.getCode());
		}

		if (manufacturerRequset.getName() != null && manufacturerRequset.getName() != "") {
			manufacturer.setName(manufacturerRequset.getName());
		}

		if (manufacturerRequset.getAddress() != null && manufacturerRequset.getAddress() != "") {
			manufacturer.setAddress(manufacturerRequset.getAddress());
		}

		if (manufacturerRequset.getStatus() != null) {
			manufacturer.setIsActive(manufacturerRequset.getStatus());
		}

		CommonResponse response = new CommonResponse();

		response.setMessage("Manufacturer Updated Successfully");

		Long countFromItem = new Long(0);
		Long countFromVendor = new Long(0);
		if (!manufacturer.getIsActive()) {
			countFromItem = ManufacturerDao.getManufacturerCountFromItem(manufacturerRequset.getId());
			countFromVendor = ManufacturerDao.getManufacturerCountFromVendor(manufacturerRequset.getId());
		}
		boolean UnableToUpdateStatus = false;
		if (countFromItem <= 0 && countFromVendor <= 0) {
			if (manufacturerRequset.getStatus() != null) {
				manufacturer.setUpdatedDate(TimeZoneComponent.getDateByzone(manufacturer.getUnitId()));
				manufacturer.setUpdatedByUser(manufacturerRequset.getUserId() != null ? manufacturerRequset.getUserId()
						: sessionService.getUserId());
				manufacturer.setIsActive(manufacturerRequset.getStatus());
			}
			UnableToUpdateStatus = false;
		} else if (countFromVendor > 0) {
			if (manufacturerRequset.getStatus() != null) {
				manufacturer.setIsActive(true);
			}
			response.setMessage("Manufacturer is assigned to Vendor");
			UnableToUpdateStatus = true;
		} else if (countFromItem > 0) {
			if (manufacturerRequset.getStatus() != null) {
				manufacturer.setIsActive(true);
			}
			response.setMessage("Manufacturer is assigned to Items");
			UnableToUpdateStatus = true;
		}

		response.setId(ManufacturerDao.update(manufacturer).longValue());
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					manufacturer.getIsActive());
			throw customeException;
		}

		return response;
	}

	@Override
	@Transactional
	public Object GetPrefixById(Integer orgId, Integer screenId) {
		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setCode(iGlobalUnitMasterDao.getGeneratedGlobalNo(0, orgId,0, screenId));
		return manufacturer;
	}
}
