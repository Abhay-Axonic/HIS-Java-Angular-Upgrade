package com.param.billing.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.dao.VaccinationMasterDao;
import com.param.billing.global.dto.VaccinationMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.VaccinationMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class VaccinationMasterServiceImpl implements VaccinationMasterService, ICommonConstants{

	@Autowired
	VaccinationMasterDao vaccinationMasterDao;
	
	@Autowired
	IEmrService EmrService;   

	@Override
	public Response saveVaccinationMaster(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		Response res=vaccinationMasterDao.getVaccinationMasterByName(vaccinationMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, "Vaccine Already Exist", null, null);
			}else{
				
				List<Integer> genderIdList = vaccinationMasterDto.getSelectedGenderList();
				if(genderIdList.size()>0 && genderIdList!=null)
				{
					for (Integer genderIdDto : genderIdList) {
					
						VaccinationMasterDto vaccinationMaster = new VaccinationMasterDto();
						vaccinationMaster.setGenderId(genderIdDto);
						vaccinationMaster.setVaccinationCode(vaccinationMasterDto.getVaccinationCode());
						vaccinationMaster.setAdministrationId(vaccinationMasterDto.getAdministrationId());
						vaccinationMaster.setSiteId(vaccinationMasterDto.getSiteId());
						vaccinationMaster.setDoseId(vaccinationMasterDto.getDoseId());
						vaccinationMaster.setUomId(vaccinationMasterDto.getUomId());
						vaccinationMaster.setAge(vaccinationMasterDto.getAge());
						vaccinationMaster.setDayid(vaccinationMasterDto.getDayid());
						vaccinationMaster.setOrganizationId(vaccinationMasterDto.getOrganizationId());
						vaccinationMaster.setUnitId(vaccinationMasterDto.getUnitId());
						vaccinationMaster.setStatus(vaccinationMasterDto.getStatus());
						vaccinationMaster.setDay(vaccinationMasterDto.getDay());
						vaccinationMaster.setGenericId(vaccinationMasterDto.getGenericId());
						vaccinationMaster.setMinAge(vaccinationMasterDto.getMinAge());
						vaccinationMaster.setMinDay(vaccinationMasterDto.getMinDay());
						vaccinationMaster.setMaxAge(vaccinationMasterDto.getMaxAge());
						vaccinationMaster.setMaxDay(vaccinationMasterDto.getMaxDay());
						vaccinationMaster.setSeries(vaccinationMasterDto.getSeries());
						vaccinationMaster.setAbbreviation(vaccinationMasterDto.getAbbreviation());
						vaccinationMaster.setRemark(vaccinationMasterDto.getRemark());
						vaccinationMaster.setInterval(vaccinationMasterDto.getInterval());
						vaccinationMaster.setChooseInterval(vaccinationMasterDto.getChooseInterval());
						vaccinationMaster.setCreatedBy(vaccinationMasterDto.getCreatedBy());
						vaccinationMaster.setCreatedDate(vaccinationMasterDto.getCreatedDate());
						vaccinationMaster.setUpdatedBy(vaccinationMasterDto.getUpdatedBy());
						vaccinationMaster.setUpdatedDate(vaccinationMasterDto.getUpdatedDate());
						
						Response resmstr=  vaccinationMasterDao.saveVaccinationMaster(vaccinationMaster);
						
						if(resmstr.getObject()!= null) {
							
							VaccinationMaster mstr =  (VaccinationMaster) resmstr.getObject();
							
							vaccinationMaster.setVaccinationId(mstr.getVaccinationId());
							
							Request request = new Request();
							Payload payload = new Payload();
							payload.setActionType("create");
							payload.setData(vaccinationMaster);
							request.setKey(MasterKeyConstants.VaccinationMaster.toString());
							request.setPayload(payload);
							//EmrService.publish(request);
							
						}
					
					}
				
					return new Response(SUCCESS, SUCCESS_CODE, "Vaccine Added Successfully", null, null);
				}	
			}

		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVaccinationMaster(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		Response res=vaccinationMasterDao.getVaccinationMasterByNameNotId(vaccinationMasterDto);
		//System.out.println(res);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE,"Vaccine Already Exist", null, null);
			}else{
				

				
				List<Integer> genderIdList = vaccinationMasterDto.getSelectedGenderList();
				if(genderIdList.size()>0 && genderIdList!=null)
				{
					for (Integer genderIdDto : genderIdList) {
					
							//VaccinationMaster vaccinationMaster = findById(vaccinationMasterDto.getVaccinationId());
						VaccinationMasterDto vaccinationMaster = new VaccinationMasterDto();
							vaccinationMaster.setVaccinationId(vaccinationMasterDto.getVaccinationId());
							vaccinationMaster.setGenderId(genderIdDto);
							vaccinationMaster.setVaccinationCode(vaccinationMasterDto.getVaccinationCode());
							vaccinationMaster.setAdministrationId(vaccinationMasterDto.getAdministrationId());
							vaccinationMaster.setSiteId(vaccinationMasterDto.getSiteId());
							vaccinationMaster.setDoseId(vaccinationMasterDto.getDoseId());
							vaccinationMaster.setUomId(vaccinationMasterDto.getUomId());
							vaccinationMaster.setAge(vaccinationMasterDto.getAge());
							vaccinationMaster.setDayid(vaccinationMasterDto.getDayid());
							vaccinationMaster.setOrganizationId(vaccinationMasterDto.getOrganizationId());
							vaccinationMaster.setUnitId(vaccinationMasterDto.getUnitId());
							vaccinationMaster.setDay(vaccinationMasterDto.getDay());
							vaccinationMaster.setGenericId(vaccinationMasterDto.getGenericId());
							vaccinationMaster.setMinAge(vaccinationMasterDto.getMinAge());
							vaccinationMaster.setMinDay(vaccinationMasterDto.getMinDay());
							vaccinationMaster.setMaxAge(vaccinationMasterDto.getMaxAge());
							vaccinationMaster.setMaxDay(vaccinationMasterDto.getMaxDay());
							vaccinationMaster.setAbbreviation(vaccinationMasterDto.getAbbreviation());
							vaccinationMaster.setSeries(vaccinationMasterDto.getSeries());
							vaccinationMaster.setRemark(vaccinationMasterDto.getRemark());
							vaccinationMaster.setInterval(vaccinationMasterDto.getInterval());
							vaccinationMaster.setChooseInterval(vaccinationMasterDto.getChooseInterval());
							vaccinationMaster.setUpdatedBy(vaccinationMasterDto.getUpdatedBy());
							vaccinationMaster.setUpdatedDate(vaccinationMasterDto.getUpdatedDate());
						
						Response resmstr=  vaccinationMasterDao.updateVaccinationMaster(vaccinationMaster);
						
						if(resmstr.getObject()!= null) {
							
							VaccinationMaster mstr =  (VaccinationMaster) resmstr.getObject();
							
							vaccinationMaster.setVaccinationId(mstr.getVaccinationId());
							
							Request request = new Request();
							Payload payload = new Payload();
							payload.setActionType("update");
							payload.setData(vaccinationMaster);
							request.setKey(MasterKeyConstants.VaccinationMaster.toString());
							request.setPayload(payload);
							//EmrService.publish(request);
							
						}
					
					}
				
					return new Response(SUCCESS, SUCCESS_CODE, "Vaccine Updated Successfully", null, null);
				}	
			
				//return vaccinationMasterDao.updateVaccinationMaster(vaccinationMasterDto);
			}

		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVaccinationMasterStatus(VaccinationMasterDto vaccinationMasterDto)
			throws ApplicationException {
		try {
			
			return vaccinationMasterDao.updateVaccinationMasterStatus(vaccinationMasterDto);
	
	} catch (Exception e) {
		
		throw e;
	}
//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccinationMasterById(Integer vaccinationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			
			return vaccinationMasterDao.getVaccinationMasterById(vaccinationId, organizationId, unitId);
	
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccinationMasterList(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		try {
			
			return vaccinationMasterDao.getVaccinationMasterList(vaccinationMasterDto);
	
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccinationMasterCount(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		try {
			
			return vaccinationMasterDao.getVaccinationMasterCount(vaccinationMasterDto);
	
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveVaccinationMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			
			return vaccinationMasterDao.getActiveVaccinationMaster(organizationId, unitId);
	
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getVaccinationMasterByName(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException {
		try {
			
			return vaccinationMasterDao.getVaccinationMasterByName(vaccinationMasterDto);
	
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccinationMasterByNameNotId(VaccinationMasterDto vaccinationMasterDto)
			throws ApplicationException {
		try {
			
			return vaccinationMasterDao.getVaccinationMasterByNameNotId(vaccinationMasterDto);
	
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRouteOfAdministrationList(Integer organizationId, Integer unitId) throws ApplicationException {
			try {
						
						return vaccinationMasterDao.getRouteOfAdministrationList(organizationId, unitId);
				
				} catch (Exception e) {
					
					throw e;
				}
				//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

	@Override
	public Response getActiveVaccinationMasterByGenderId(Integer organizationId, Integer genderId)
			throws ApplicationException {try {
				
				return vaccinationMasterDao.getActiveVaccinationMasterByGenderId(organizationId,genderId);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
}

