package com.param.procedure.dao;


import com.param.global.common.Response;
import com.param.service.dto.TrustMasterDto;

import in.param.exception.ApplicationException;

public interface ITrustMasterDao {

	public Response saveTrust(TrustMasterDto trustMasterDto) throws ApplicationException;

	public Response getTrustMasterById(Integer orgId, Integer unitId, Integer trustId) throws ApplicationException;

	public Response updateTrustMaster(TrustMasterDto trustMasterDto)throws ApplicationException;

	public Response activeInactiveTrustMaster(Integer orgId, Integer trustId, Character trustStatus)throws ApplicationException;

	public Response listTrustMaster(TrustMasterDto trustMasterDto)throws ApplicationException;

	public Response getTotalTrustMasterRecord(TrustMasterDto trustMasterDto)throws ApplicationException;

	public Response getActiveTrustList(TrustMasterDto trustMasterDto)throws ApplicationException;

	public Response getVisitType(Integer trustId ,Integer orgId, Integer unitId)throws ApplicationException;



}
