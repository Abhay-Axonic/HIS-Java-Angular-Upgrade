package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.VisitTypeMasterDto;
import com.param.global.model.VisitTypeMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IVisitTypeMasterDao extends IGenericDao<VisitTypeMaster, Integer>{
	
	public Response getActiveVisitTypeList(Integer unitId, Integer orgId)throws ApplicationException;

	public Response getVisitTypeList(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;

	public Response getVisitTypeCount(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;

	public Response updateVisitTypeStatus(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;

	public Response updateVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException ;

	public Response getVisitTypeById(Integer visitId, Integer orgId) throws ApplicationException;

	public Response saveVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;
	
	public Response getVisitTypeByName(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;
	
	public Response getVisitTypeByNameNotById(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException;
}
