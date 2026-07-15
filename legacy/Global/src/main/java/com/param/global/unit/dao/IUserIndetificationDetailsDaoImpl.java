package com.param.global.unit.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.unit.dto.UserIndetificationDetailsDto;

@SuppressWarnings({"rawtypes"})
public interface IUserIndetificationDetailsDaoImpl {

	public Response  saveIdentificationDetailsOfUser(UserIndetificationDetailsDto userIndetificationDetailsDto)throws ApplicationException;

	public Response getListOfDoctorIdentificationDocuumentList( UserIndetificationDetailsDto userIndetificationDetailsDto)throws ApplicationException;

	public Response updateDoctorIdentificationDetails(UserIndetificationDetailsDto userIndetificationDetailsDto)throws ApplicationException;

	public Response updateIdentificationDetailsOfUser(UserIndetificationDetailsDto dto)throws ApplicationException;

}
