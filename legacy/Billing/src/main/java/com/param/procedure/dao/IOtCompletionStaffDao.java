package com.param.procedure.dao;

import com.param.global.common.Response;
import com.param.global.procedure.dto.BillOfMaterialDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IOtCompletionStaffDao {

	public Response saveOtCompletionStaff(BillOfMaterialDto dto)throws ApplicationException;
	public Response getOtCompletionStaffByProReqId(int id, Integer orgId, Integer unitId, Integer billingBedCategoryId)throws ApplicationException;
	public Response getDoctorList(BillOfMaterialDto billOfMaterialDto)throws ApplicationException;
}
