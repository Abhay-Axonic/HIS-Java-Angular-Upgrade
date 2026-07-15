package com.param.service.services;

import java.util.List;
import java.util.Map;

import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.PackageSearchRequestDto;
import com.param.global.dto.UpdatePayeeDto;

import in.param.exception.ApplicationException;

/**
 * Package consumption service interface
 * @author User
 *
 */

public interface IPackageConsumptionService {

	public Response getPackageServiceByPackageType(PackageSearchRequestDto packageSearchRequestDto) throws ApplicationException;
	
	
	
	
	
}
