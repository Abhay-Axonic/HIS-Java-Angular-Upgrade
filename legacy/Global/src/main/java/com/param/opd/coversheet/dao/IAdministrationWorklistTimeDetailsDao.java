package com.param.opd.coversheet.dao;

import com.param.data.dto.AdministrationWorklistTimeDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAdministrationWorklistTimeDetailsDao {

	Response saveAdministratorWorkList(AdministrationWorklistTimeDetailsDto dto)throws ApplicationException;

}
