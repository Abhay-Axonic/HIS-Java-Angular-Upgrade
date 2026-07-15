package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.IntraOperativeDetailsDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.WhoChecklistDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IWhoChecklistDao {

	Response saveWhoChecklist(WhoChecklistDto dto)throws ApplicationException;

	Response getWhoChecklistByPreOperativeId(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getWhoChecklistByIntraOperativeId(IntraOperativeDetailsDto intraOperativeDetailsDto)throws ApplicationException;

}
