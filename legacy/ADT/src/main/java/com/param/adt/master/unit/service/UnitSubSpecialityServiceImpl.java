package com.param.adt.master.unit.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dao.IUnitSubSpecialityDao;
import com.param.adt.master.unit.dto.UnitSubSpecialityMapperDto;
import com.param.common.dto.CommonResponse;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitSubSpecialityServiceImpl implements IUnitSubSpecialityService, ICommonConstants {

	@Autowired
	IUnitSubSpecialityDao iUnitSubSpecialityDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;

	@Override
	public Response saveUnitSubSpecialtiy(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
//			Response resTrunc = iUnitSubSpecialityDao.truncateUnitSubSpecialtiy(unitSubSpecialityMapperDto);
//			if (resTrunc.getStatus().equals(SUCCESS)) {
				Iterator<UnitSubSpecialityMapperDto> itr = unitSubSpecialityMapperDto
						.getUnitSubSpecialityMapperDtosList().iterator();
				while (itr.hasNext()) {
					UnitSubSpecialityMapperDto subSpecialityMapperDto = new UnitSubSpecialityMapperDto();

					UnitSubSpecialityMapperDto obj = itr.next();

					subSpecialityMapperDto.setUnitId(unitSubSpecialityMapperDto.getUnitId());
					subSpecialityMapperDto.setOrganizationId(unitSubSpecialityMapperDto.getOrganizationId());
					subSpecialityMapperDto.setSpecialityId(unitSubSpecialityMapperDto.getSpecialityId());
					subSpecialityMapperDto.setSubSpecialityId(obj.getSubSpecialityMasterId());
					subSpecialityMapperDto.setStatus('A');
					subSpecialityMapperDto.setCreatedBy(unitSubSpecialityMapperDto.getCreatedBy());
					subSpecialityMapperDto.setCreatedDate(unitSubSpecialityMapperDto.getCreatedDate());
					subSpecialityMapperDto.setUpdatedBy(unitSubSpecialityMapperDto.getUpdatedBy());
					subSpecialityMapperDto.setUpdatedDate(unitSubSpecialityMapperDto.getUpdatedDate());

					iUnitSubSpecialityDao.saveUnitSubSpecialitiy(subSpecialityMapperDto);
					
					UnitSubSpecialityMapperDto UnitSubDto = new UnitSubSpecialityMapperDto();
					
					UnitSubDto.setCreatedBy(unitSubSpecialityMapperDto.getCreatedBy());
					UnitSubDto.setSubSpecialityId(obj.getSubSpecialityMasterId());
					UnitSubDto.setSubSpecialityName(obj.getSubSpecialityName());
					UnitSubDto.setOrganizationId(unitSubSpecialityMapperDto.getOrganizationId());
					UnitSubDto.setCreatedDate(unitSubSpecialityMapperDto.getCreatedDate());
					UnitSubDto.setUpdatedBy(unitSubSpecialityMapperDto.getCreatedBy());
					UnitSubDto.setUpdatedDate(unitSubSpecialityMapperDto.getCreatedDate());
					UnitSubDto.setSpecialityId(unitSubSpecialityMapperDto.getSpecialityId());
					UnitSubDto.setUnitId(unitSubSpecialityMapperDto.getUnitId());
					UnitSubDto.setStatus('A');
					
					CommonResponse response = new CommonResponse();
					response.setMessage("speciality master Inserted Successfully");
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(UnitSubDto);
					request.setKey(MasterKeyConstants.SubSpeciality.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					//EmrService.publish(request);
					
				}
//			} else {
//				return new Response(ERROR, null, null, null, null);
//			}

			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getUnitSubSpecialityList(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {

			return iUnitSubSpecialityDao.getUnitSubSpecialityList(unitSubSpecialityMapperDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForUnitSubSpeciality(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			Response deptResponse = iUnitSubSpecialityDao.getUnitSubSpecialityById(unitSubSpecialityMapperDto);
			if (deptResponse.getStatus().equals(SUCCESS)) {

				iUnitSubSpecialityDao.updateStatusForUnitSpecialityList(unitSubSpecialityMapperDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSubSpecialityCount(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {

			return iUnitSubSpecialityDao.getUnitSubSpecialityCount(unitSubSpecialityMapperDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityBySpecialityId(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			return iUnitSubSpecialityDao.getSubSpecialityBySpecialityId(unitSubSpecialityMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityBySpecialityArray(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			return iUnitSubSpecialityDao.getSubSpecialityBySpecialityArray(unitSubSpecialityMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getActiveUnitSubSpecialityList(UnitSubSpecialityMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {

			return iUnitSubSpecialityDao.getActiveUnitSubSpecialityList(unitSpecialtyMapperDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityBySpecialityArrayForTariff(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			return iUnitSubSpecialityDao.getSubSpecialityBySpecialityArrayForTariff(unitSubSpecialityMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response unitSubDepartmentMasterUniversalSearch(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			return iUnitSubSpecialityDao.unitSubDepartmentMasterUniversalSearch(unitSubSpecialityMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
