package com.param.adt.master.unit.service;

import java.util.Iterator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dao.IUnitSpecialityDao;
import com.param.adt.master.unit.dto.UnitSpecialtyMapperDto;
import com.param.common.dto.CommonResponse;
import com.param.global.dao.ISpecialityMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })

public class UnitSpecialityServiceImpl implements IUnitSpecialityService, ICommonConstants {

	@Autowired
	IUnitSpecialityDao iUnitSpecialityDao;

	@Autowired
	ISpecialityMasterDao iSpecialityMasterDao;

	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Override
	public Response saveUnitSpecialtiy(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		try {
			//updating status as T ,is removed 
//			Response resTrunc = iUnitSpecialityDao.truncateUnitSpecialtiy(unitSpecialtyMapperDto);
//			if (resTrunc.getStatus().equals(SUCCESS)) {
			int pddDays = 0;
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(unitSpecialtyMapperDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(unitSpecialtyMapperDto.getUnitId());
			com.param.global.common.Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if (appConfigRes.getObject() != null) {
				unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
						.getObject();
				pddDays = (unitApplicationConfigurationDtos.getPddDays() != null && unitApplicationConfigurationDtos.getPddDays() > 0) ? unitApplicationConfigurationDtos.getPddDays() : 0; 
			} 
			
			
			Iterator<UnitSpecialtyMapperDto> itr = unitSpecialtyMapperDto.getUnitSpecialtyMapperDtosList()
					.iterator();
					while (itr.hasNext()) {
					UnitSpecialtyMapperDto unMapperDto = new UnitSpecialtyMapperDto();

					SpecialityMasterDto spDto= new SpecialityMasterDto();


					UnitSpecialtyMapperDto obj = itr.next();

					spDto.setSpecialityId(obj.getSpecialityId());
					unMapperDto.setUnitId(unitSpecialtyMapperDto.getUnitId());
					unMapperDto.setOrganizationId(unitSpecialtyMapperDto.getOrganizationId());
					unMapperDto.setSpecialityId(obj.getSpecialityId());
					unMapperDto.setStatus('A');
					unMapperDto.setCreatedBy(unitSpecialtyMapperDto.getCreatedBy());
					unMapperDto.setCreatedDate(unitSpecialtyMapperDto.getCreatedDate());
					unMapperDto.setUpdatedBy(unitSpecialtyMapperDto.getUpdatedBy());
					unMapperDto.setUpdatedDate(unitSpecialtyMapperDto.getUpdatedDate());
					unMapperDto.setIsSurgicalCode('N');
					unMapperDto.setIsClinicalSpeciality('N');
					unMapperDto.setIsRenderingSpeciality('N');
					unMapperDto.setDeptWiseAppointment('N');
					unMapperDto.setIsKioskDept('N');
					unMapperDto.setIsNursingAssessmentRequired('N');
					unMapperDto.setAllowedOverbookingSlots(0);
					unMapperDto.setPddDays(pddDays);
					iUnitSpecialityDao.saveUnitSpecialtiy(unMapperDto);


					com.param.global.common.Response res= iSpecialityMasterDao.getSpecialityNameById(spDto);
					SpecialityMasterDto specialityMaste = (SpecialityMasterDto) res.getObject();

					UnitSpecialtyMapperDto unMapperDtoVO = new UnitSpecialtyMapperDto();
					unMapperDtoVO.setCreatedBy(unMapperDto.getCreatedBy());
					unMapperDtoVO.setSpecialityId(unMapperDto.getSpecialityId());
					unMapperDtoVO.setUnitId(unMapperDto.getUnitId());
					unMapperDtoVO.setOrganizationId(unMapperDto.getOrganizationId());
					unMapperDtoVO.setSpecialityName(specialityMaste.getSpecialityName());
					unMapperDtoVO.setCreatedDate(unMapperDto.getCreatedDate());
					unMapperDtoVO.setUpdatedBy(unMapperDto.getUpdatedBy());
					unMapperDtoVO.setUpdatedDate(unMapperDto.getUpdatedDate());


					CommonResponse response = new CommonResponse();
					response.setMessage("speciality master Inserted Successfully");
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(unMapperDtoVO);
					request.setKey(MasterKeyConstants.Speciality.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					//EmrService.publish(request);

					}
//			} else {
//				return new Response(ERROR, null, null, null, null);
//			}

			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSpecialityList(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		try {

			return iUnitSpecialityDao.getUnitSpecialtiyList(unitSpecialtyMapperDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForUnitSpecialityList(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			Response deptResponse = iUnitSpecialityDao.getUnitSpecialityById(unitSpecialtyMapperDto);
			if (deptResponse.getStatus().equals(SUCCESS)) {

				iUnitSpecialityDao.updateStatusForUnitSpecialityList(unitSpecialtyMapperDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, "ID doen not exist", null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSpecialityCount(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		try {

			return iUnitSpecialityDao.getCount(unitSpecialtyMapperDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityListByDoctorId(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			return iUnitSpecialityDao.getSpecialityListByDoctorId(unitSpecialtyMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSpecialityById(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		try {
			return iUnitSpecialityDao.getUnitSpecialityById(unitSpecialtyMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveUnitSpecialityList(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			return iUnitSpecialityDao.getActiveUnitSpecialityList(unitSpecialtyMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response unitDepartmentMasterUniversalSearch(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			return iUnitSpecialityDao.unitDepartmentMasterUniversalSearch(unitSpecialtyMapperDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getSpecialityListByDoctorIdByOrg(UnitSpecialtyMapperDto unitSpecialtyMapperDto) {
		try {
			return iUnitSpecialityDao.getSpecialityListByDoctorIdByOrg(unitSpecialtyMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateUnitSpecialtyMapperDetails(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		
		
		
		try {
			iUnitSpecialityDao.updateUnitSpecialtyMapperDetails(unitSpecialtyMapperDto);
				return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
		
		
	}

	@Override
	public Response getSpecialityListByUserIdByUnitId(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			return iUnitSpecialityDao.getSpecialityListByUserIdByUnitId(unitSpecialtyMapperDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	

}
