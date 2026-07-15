package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdRylesTubeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface IIpdRylesTubeDao {

	public Response saveIpdRylesTubeOrder(List<IpdRylesTubeDto> ipdRylesTubeOrderDtoList) throws ApplicationException;

	public Response getIpdRylesTubeOrderList(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException;

	public Response updateIpdRylesTubeOrder(List<IpdRylesTubeDto> ipdRylesTubeOrderDtoList) throws ApplicationException;

	public Response cancelRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto)throws ApplicationException;
}
