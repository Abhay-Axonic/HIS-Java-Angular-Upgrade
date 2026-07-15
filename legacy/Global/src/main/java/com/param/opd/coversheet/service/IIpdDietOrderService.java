package com.param.opd.coversheet.service;

import javax.transaction.Transactional;


import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdDietOrderDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
@Transactional
public interface IIpdDietOrderService {
	
	public Response saveIpdDietOrder(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException;
	
	public Response getIpdDietOrderList(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException;

	public Response updateIpdDietOrder(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException;

	public Response cancelDietOrder(IpdDietOrderDto ipdDietOrderDto)throws ApplicationException;
}
