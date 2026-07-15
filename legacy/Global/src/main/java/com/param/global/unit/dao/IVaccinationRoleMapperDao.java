package com.param.global.unit.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.unit.dto.VaccinationRoleMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IVaccinationRoleMapperDao {
	
	

	public	Response getVaccinationRoleMapperByRoleId(VaccinationRoleMapperDto vaccinationRoleMapperDto);
	public void inactiveVaccinationByRoleId(List<Integer> vacUserMapper);
	
	public Response getVaccinationRoleMapperByRoleId1(VaccinationRoleMapperDto vaccinationRoleMapperDto);

	

}
