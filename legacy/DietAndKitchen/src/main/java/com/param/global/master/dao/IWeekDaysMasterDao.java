package com.param.global.master.dao;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IWeekDaysMasterDao {

	Response getAllWeekDaysMaster() throws ApplicationException;

}
