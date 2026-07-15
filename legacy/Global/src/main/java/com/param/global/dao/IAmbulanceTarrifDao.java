package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceTarrifDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.AmbulanceTarrif;
import com.param.global.model.AmbulanceType;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAmbulanceTarrifDao extends IGenericDao<AmbulanceTarrif, Integer>{

	Response getAmbulanceTarrifByName(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response saveAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response getAmbulanceTarrifList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response getAmbulanceTarrifActiveList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;
	
	Response getAmbulanceTarrifByID(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response getSingleAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response updateAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;

	Response changeAmbulanceTarrifStatus(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException;


	

}
