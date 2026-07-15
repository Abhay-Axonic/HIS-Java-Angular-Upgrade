package com.param.global.service;

import com.param.global.common.Response;
import com.param.global.dto.ReferalDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IReferalDetailsService {

	Response saveReferalDetails(ReferalDetailsDto referalDetailsDto)throws ApplicationException;

}
