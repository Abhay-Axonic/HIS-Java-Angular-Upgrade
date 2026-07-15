package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.DoctorInCabinDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorInCabinDao {

	Response saveDoctorInCabin(DoctorInCabinDto dto) throws ApplicationException;

	Response inactivePreviousDoctorInCabin(DoctorInCabinDto dto)throws ApplicationException;

	Response getDoctorInCabin(DoctorInCabinDto dto);

}
