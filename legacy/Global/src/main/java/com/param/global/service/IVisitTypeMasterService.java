package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.VisitTypeMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IVisitTypeMasterService {
	
	public Response getActiveVisitTypeList(Integer unitId, Integer orgId) throws ApplicationException;

	public Response getVisitTypeList(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;

	public Response getVisitTypeCount(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;

	public Response updateVisitTypeStatus(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;

	public Response updateVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException ;

	public Response getVisitTypeById(Integer visitId, Integer orgId) throws ApplicationException;

	public Response saveVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;
}
