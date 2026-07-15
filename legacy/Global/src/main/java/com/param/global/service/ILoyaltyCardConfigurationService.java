package com.param.global.service;

import javax.transaction.Transactional;


import com.param.global.common.Response;
import com.param.global.dto.LoyaltyCardConfigurationDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ILoyaltyCardConfigurationService {
	
	public Response saveLoyaltyCardConfiguration(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException;
	
	public Response updateLoyaltyCardConfigurationStatus(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException;
	
	public Response getLoyaltyCardConfigurationById(Integer cabinId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getLoyaltyCardConfigurationList(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException;
	
	public Response getLoyaltyCardConfigurationCount(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException;

	
}
