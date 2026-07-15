package com.param.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dao.IMPeriodicityServiceConfigDao;
import com.param.service.dto.MPeriodicityServiceConfigDto;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings("rawtypes")
public class MPeriodicityServiceConfigServiceImpl implements IMPeriodicityServiceConfigService, ICommonConstants{

	@Autowired
	private IMPeriodicityServiceConfigDao iMPeriodicityServiceConfigDao;
	
	@Override
	@Transactional
	public Response saveMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto) throws ServiceException {
		try{
			//check if already exists
			Response pServRes = null;
			boolean flag = false;
			if(dto.getPeriodicityServiceConfigId() != null && dto.getPeriodicityServiceConfigId() > 0)
			{
				 flag =iMPeriodicityServiceConfigDao.checkExistsPServConfigUpdate(dto.getServiceId(), dto.getEquipmentId(), dto.getUnitId(), dto.getPeriodicityServiceConfigId(),dto.getVisitTypeId());
			}
			else
			{
				 pServRes = iMPeriodicityServiceConfigDao.validatePServiceConfigAvail(dto);
				 flag = (boolean)pServRes.getObject();
			}
			
			if(flag==true){
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE, "Already Exist..!!", null, null);
			}else{
				dto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				return iMPeriodicityServiceConfigDao.saveMPeriodicityServiceConfig(dto);
			}
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMPeriodicityList(int orgId, int unitId) throws ServiceException {
		try{
			return iMPeriodicityServiceConfigDao.getMPeriodicityList(orgId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateMPeriodicityStatusById(int id, char status) throws ServiceException {
		try{
			return iMPeriodicityServiceConfigDao.updateMPeriodicityStatusById(id, status);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto) throws ServiceException {
		try{
			//check if already exits 
			dto.setEquipmentId((dto.getEquipmentId() != null ? dto.getEquipmentId() : 0));
			boolean flag = iMPeriodicityServiceConfigDao.checkExistsPServConfigUpdate(dto.getServiceId(), dto.getEquipmentId(), dto.getUnitId(), dto.getPeriodicityServiceConfigId(),dto.getVisitTypeId());
			if(flag)
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE, "Already Exist..!!", null, null);
			
			dto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			return iMPeriodicityServiceConfigDao.updateMPeriodicityServiceConfig(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchPatientForPServiceOrder(int visitTypeId, int orgId, int unitId, String name)throws ServiceException {
		try{
			return iMPeriodicityServiceConfigDao.searchPatientForPServiceOrder(visitTypeId, orgId, unitId, name);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response validatePServiceConfigAvail(int serviceId, int orgId, int unitId,int visitTypeId) throws ServiceException {
		try{
			MPeriodicityServiceConfigDto dto = new MPeriodicityServiceConfigDto();
			dto.setServiceId(serviceId);
			dto.setOrganisationId(orgId);
			dto.setVisitTypeId(visitTypeId);
			dto.setUnitId(unitId);
			dto.setEquipmentId(null);
			return iMPeriodicityServiceConfigDao.validatePServiceConfigAvail(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRatePServiceConfig(int serviceId, int orgId, int unitId, int visitTypeId,
			int periodicityServiceConfigId) throws ServiceException {
		try{			
			return iMPeriodicityServiceConfigDao.validatePServiceConfigAvailUpdate(serviceId,orgId,unitId,visitTypeId,periodicityServiceConfigId);
		}catch(Exception e){
			
			throw e;
		}
	}

	
}
