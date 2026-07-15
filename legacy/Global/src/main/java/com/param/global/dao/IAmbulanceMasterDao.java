package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceMasterDto;
import com.param.global.model.AmbulanceMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAmbulanceMasterDao  extends IGenericDao<AmbulanceMaster, Integer>{

	public Response saveAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	public Response getAmbulanceMasterDataList(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	public Response getAmbulanceMasterDataActiveList(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	public Response getSingleAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	public Response updateAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	public Response changeAmbulanceMasterDataStatus(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;

	public Response getAmbulanceMasterByName(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException;
	
	
	
}
