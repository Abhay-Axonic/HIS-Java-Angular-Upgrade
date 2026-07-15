package com.param.ER.service;

import com.param.adt.common.Response;
import com.param.global.dto.DischargeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IERDischargeService {

	Response getERDischargePatientList(DischargeDto dischargeDto) throws ApplicationException;

}
