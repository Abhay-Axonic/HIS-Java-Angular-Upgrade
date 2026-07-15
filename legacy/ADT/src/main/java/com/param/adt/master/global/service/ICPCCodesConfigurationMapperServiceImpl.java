package com.param.adt.master.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.dao.IICPCCodesConfigurationDao;
import com.param.adt.master.global.dao.IICPCCodesConfigurationMapperDao;
import com.param.adt.master.global.dto.ICPCCodesConfigurationDto;
import com.param.adt.master.global.dto.ICPCCodesConfigurationMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.ICPCCodeConfiguration;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class ICPCCodesConfigurationMapperServiceImpl implements IICPCCodesConfigurationMapperService, ICommonConstants{

	@Autowired
	private IICPCCodesConfigurationMapperDao iICPCCodesConfigurationMapperDao;
	
	@Autowired
	private IICPCCodesConfigurationDao iICPCCodesConfigurationDao;

	@Override
	public Response saveICPCCodesConfigurationMapper(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			
			ICPCCodesConfigurationDto iCPCCodesConfigurationDto = new ICPCCodesConfigurationDto();
			iCPCCodesConfigurationDto.setIcpcSystemId(iCPCCodesConfigurationMapperDto.getIcpcSystemId());
			iCPCCodesConfigurationDto.setOrganizationId(iCPCCodesConfigurationMapperDto.getOrganizationId());
			iCPCCodesConfigurationDto.setUnitId(iCPCCodesConfigurationMapperDto.getUnitId());
			iCPCCodesConfigurationDto.setCreatedDate(iCPCCodesConfigurationMapperDto.getCreatedDate());
			iCPCCodesConfigurationDto.setUpdatedBy(iCPCCodesConfigurationMapperDto.getUpdatedBy());
			iCPCCodesConfigurationDto.setUpdatedDate(iCPCCodesConfigurationMapperDto.getUpdatedDate());
			iCPCCodesConfigurationDto.setCreatedBy(iCPCCodesConfigurationMapperDto.getCreatedBy());
			iCPCCodesConfigurationDto.setStatus(iCPCCodesConfigurationMapperDto.getStatus());
			if(iCPCCodesConfigurationMapperDto.getIcpcCodeConfigurationId()!=null){
				iCPCCodesConfigurationDto.setIcpcCodeConfigurationId(iCPCCodesConfigurationMapperDto.getIcpcCodeConfigurationId());
				
			}
			
			Response res =  iICPCCodesConfigurationDao.saveICPCCodesConfiguration(iCPCCodesConfigurationDto);
			Integer icpcCodeConfigurationId = null;
			if(res.getStatus().equals(SUCCESS)) {
				
				
				ICPCCodeConfiguration mstr= (ICPCCodeConfiguration)res.getObject();
				icpcCodeConfigurationId= mstr.getIcpcSystemId();
				if(icpcCodeConfigurationId!=null) {
					iCPCCodesConfigurationMapperDto.setIcpcConfigurationId(icpcCodeConfigurationId);
					return iICPCCodesConfigurationMapperDao.saveICPCCodesConfigurationMapper(iCPCCodesConfigurationMapperDto);

			}
			}
			else {
				
				return res;
			}
			
		} catch(Exception e){
			
		}
		return new Response(ERROR, ERROR_CODE, ERROR, null,null);
	}

	@Override
	public Response getICPCCodesConfigurationMapperList(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			return iICPCCodesConfigurationMapperDao.getICPCCodesConfigurationMapperList(iCPCCodesConfigurationMapperDto);
		
		} catch(Exception e){
			
		}
		return new Response(ERROR, ERROR_CODE, ERROR, null,null);
	}

	@Override
	public Response getICPCSystemList(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			return iICPCCodesConfigurationMapperDao.getICPCSystemList(iCPCCodesConfigurationMapperDto);
		
		} catch(Exception e){
			
		}
		return new Response(ERROR, ERROR_CODE, ERROR, null,null);
	}

	@Override
	public Response updateICPCCodesConfigurationStatus(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			return iICPCCodesConfigurationDao.updateICPCCodesConfigurationStatus(iCPCCodesConfigurationMapperDto);
		
		} catch(Exception e){
			
		}
		return new Response(ERROR, ERROR_CODE, ERROR, null,null);
	}

	@Override
	public Response getSpecialityListByIcpcSystemId(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)
			throws ApplicationException {
		try{
			return iICPCCodesConfigurationMapperDao.getSpecialityListByIcpcSystemId(iCPCCodesConfigurationMapperDto);
		
		} catch(Exception e){
			
		}
		return new Response(ERROR, ERROR_CODE, ERROR, null,null);
	}

}
