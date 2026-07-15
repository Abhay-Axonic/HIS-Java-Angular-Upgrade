package com.param.global.master.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IWeekDaysMasterService {

	@Transactional
	Response getAllWeekDaysMaster() throws ApplicationException;

}
