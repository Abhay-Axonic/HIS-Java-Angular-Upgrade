package com.param.adt.admission.dao;

import com.param.adt.admission.dto.FallRiskScoreDto;
import com.param.adt.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IFallRiskScoreDao {

	Response saveFallRiskScore(FallRiskScoreDto dto)throws ApplicationException;

}
