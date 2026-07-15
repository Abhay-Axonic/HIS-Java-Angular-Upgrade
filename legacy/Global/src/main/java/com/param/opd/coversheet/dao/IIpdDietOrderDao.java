package com.param.opd.coversheet.dao;


import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdDietOrderDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IIpdDietOrderDao {
	public Response saveIpdDietOrder(List<IpdDietOrderDto> ipdDietOrderDtoList) throws ApplicationException;
	
	public Response getIpdDietOrderList(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException;

	public Response updateIpdDietOrder(List<IpdDietOrderDto> ipdDietOrderDtoList) throws ApplicationException;

	public Response cancelDietOrder(IpdDietOrderDto ipdDietOrderDto)throws ApplicationException;
}
