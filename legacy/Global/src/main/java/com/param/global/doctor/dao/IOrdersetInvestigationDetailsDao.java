package com.param.global.doctor.dao;

import com.param.doctor.model.OrdersetInvestigationDetails;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OrdersetInvestigationDetailsDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOrdersetInvestigationDetailsDao extends IGenericDao<OrdersetInvestigationDetails, Integer>{
	public Response saveOrdersetInvestigationDetails(OrdersetInvestigationDetailsDto dto)throws ApplicationException;

	public Response updateOrdersetInvestigationDetails(OrdersetInvestigationDetailsDto dto, int oldOrdersetMasterId)throws ApplicationException;
}
