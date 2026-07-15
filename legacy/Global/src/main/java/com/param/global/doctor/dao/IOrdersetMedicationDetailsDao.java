package com.param.global.doctor.dao;

import com.param.doctor.model.OrdersetMedicationDetails;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OrdersetMedicationDetailsDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOrdersetMedicationDetailsDao extends IGenericDao<OrdersetMedicationDetails, Integer>{
	public Response saveOrdersetMedicationDetails(OrdersetMedicationDetailsDto dto)throws ApplicationException;

	public Response updateOrdersetMedicationDetails(OrdersetMedicationDetailsDto dto, int oldOrdersetMasterId)throws ApplicationException;
}
