package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtCompletionTimeSheetDto;
import com.param.otims.model.OtCompletionTimeSheet;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOtCompletionTimeSheetDao extends IGenericDao<OtCompletionTimeSheet, Integer>{
	public Response saveOrUpdateOtCompletionTimeSheet(OtCompletionTimeSheetDto dto)throws ApplicationException;
	public Response getOtTimeSheetByProReq(int id)throws ApplicationException;
}
