package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMedicolegalDao {

	Response updateMedicolegalDetails(AdmissionDto admissionDto) throws ApplicationException;
}
