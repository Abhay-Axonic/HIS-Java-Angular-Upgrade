package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdRylesTubeDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
@Transactional
public interface IIpdRylesTubesService {

	public Response saveIpdRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException;

	public Response getIpdRylesTubeOrderList(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException;

	public Response updateIpdRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException;

	public Response cancelRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException;
		

}
