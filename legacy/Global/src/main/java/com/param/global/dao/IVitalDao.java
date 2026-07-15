package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.TVitalDetailsDto;
import com.param.global.dto.TVitalDto;
import com.param.global.dto.VitalSearchDto;
import com.param.global.model.TVital;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IVitalDao extends IGenericDao<TVital, Integer> {

	public Response saveVitals(TVitalDto dto)throws ApplicationException;
	public Response saveVitalDetails(TVitalDetailsDto dto)throws ApplicationException;
	public Response getVitalsByPatient(VitalSearchDto dto)throws ApplicationException;
}
