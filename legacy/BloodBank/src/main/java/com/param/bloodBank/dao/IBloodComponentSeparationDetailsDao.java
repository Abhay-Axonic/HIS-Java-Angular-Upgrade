package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodComponentSeparationDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBloodComponentSeparationDetailsDao {

	Response saveBloodComponentSeparationDetails(BloodComponentSeparationDetailsDto detailsDto)throws ApplicationException;

}
