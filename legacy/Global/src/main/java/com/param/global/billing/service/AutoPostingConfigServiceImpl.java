package com.param.global.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IAutoPostingConfigDao;
import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;


@Service
@SuppressWarnings("rawtypes")
public class AutoPostingConfigServiceImpl implements IAutoPostingConfigService,ICommonConstants{

	@Autowired
	IAutoPostingConfigDao iAutoPostingConfigDao;
	
	@Override
	public Response saveAutoPostingConfig(AutoPostingServicesConfigDto postingServicesConfigDto) throws ApplicationException{
		try{
			if(postingServicesConfigDto.getPostingServicesConfigDtosList()!=null && postingServicesConfigDto.getPostingServicesConfigDtosList().size()>0)
			{
					iAutoPostingConfigDao.saveAutoPostingConfig(postingServicesConfigDto);
			}	
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getAutoPostingConfigList(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			return iAutoPostingConfigDao.getAutoPostingConfigList(orgId,unitId);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response updateServiceAutoPostingStatus(AutoPostingServicesConfigDto postingServicesConfigDto)
			throws ApplicationException {
		try{
			return iAutoPostingConfigDao.updateServiceAutoPostingStatus(postingServicesConfigDto);
		}catch(Exception e){
			throw e;
		}
	}

}
