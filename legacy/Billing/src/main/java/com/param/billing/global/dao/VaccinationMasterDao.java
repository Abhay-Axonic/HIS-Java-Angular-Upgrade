package com.param.billing.global.dao;

import com.param.billing.global.dto.VaccinationMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface VaccinationMasterDao {

public Response saveVaccinationMaster(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException;
	
	public Response updateVaccinationMaster(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException;
	
	public Response updateVaccinationMasterStatus(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException;
	
	public Response getVaccinationMasterById(Integer vaccinationId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getVaccinationMasterList(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException;
	
	public Response getVaccinationMasterCount(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException;
	
	public Response getActiveVaccinationMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getVaccinationMasterByName(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException;
	
	public Response getVaccinationMasterByNameNotId(VaccinationMasterDto vaccinationMasterDto) throws ApplicationException;
	
	public Response getRouteOfAdministrationList(Integer organizationId, Integer unitId) throws ApplicationException;

	public Response getActiveVaccinationMasterByGenderId(Integer organizationId, Integer genderId)throws ApplicationException;

}
