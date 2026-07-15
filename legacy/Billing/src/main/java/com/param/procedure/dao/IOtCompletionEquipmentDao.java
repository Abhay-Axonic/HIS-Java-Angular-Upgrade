package com.param.procedure.dao;

import com.param.global.common.Response;
import com.param.procedure.dto.OtCompletionEquipmentDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOtCompletionEquipmentDao {

	public Response saveOtCompletionEquipment(OtCompletionEquipmentDto equipment)throws ApplicationException;

	public Response getOtCompletionEquipment(int proReqId)throws ApplicationException;
}
