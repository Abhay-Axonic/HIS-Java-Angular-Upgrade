package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceFacilitiesDto;
import com.param.global.model.AmbulanceFacilities;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAmbulanceFacilitiesDao extends IGenericDao<AmbulanceFacilities, Integer>{

	
	Response getAmbulanceFacilitiesByName(AmbulanceFacilitiesDto ambulanceFacilitiesDto)throws ApplicationException;

	Response saveAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto)throws ApplicationException;

	Response getAmbulanceFacilitiesList(AmbulanceFacilitiesDto ambulanceFacilitiesDto)throws ApplicationException;
	
	Response getAmbulanceFacilitiesActiveList(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	Response getSingleAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto)throws ApplicationException;

	Response updateAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	Response getAmbulanceFacilitiesByID(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;

	Response changeAmbulanceFacilitiesStatus(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException;



	

}
