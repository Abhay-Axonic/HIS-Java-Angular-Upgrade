package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.CampMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ICampMasterService {

	public Response saveCampMaster(CampMasterDto campMasterDto) throws ApplicationException;

	public Response getCampMasterList(CampMasterDto campMasterDto) throws ApplicationException;

	public Response updateCampMasterStatus(CampMasterDto campMasterDto) throws ApplicationException;

	public Response updateCampMaster(CampMasterDto campMasterDto) throws ApplicationException;

	public Response getCampMasterListPaginationList(CampMasterDto campMasterDto)throws ApplicationException;

	public Response getCampMasterListCount(CampMasterDto campMasterDto)throws ApplicationException;

}
