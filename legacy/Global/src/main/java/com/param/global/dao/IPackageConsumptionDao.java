package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.PackageSearchRequestDto;
import com.param.global.dto.UpdatePayeeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPackageConsumptionDao {
	
	public Response getPackageServiceByPackageType(PackageSearchRequestDto packageSearchRequestDto) throws ApplicationException;
	public Response getPackageConsumptionCapDetails(Integer packageConsumptionMasterId) throws ApplicationException;
	public Response getPackageConsumptionEODetails(Integer EODtlId, Integer packageConsumptionMasterId) throws ApplicationException;
	//Package Cancel For change of payee
	public Response chageOfPayeeInativatePackage(UpdatePayeeDto billingMasterDto,OrderDetailsByEncounterIdDto dto) throws ApplicationException;
}
