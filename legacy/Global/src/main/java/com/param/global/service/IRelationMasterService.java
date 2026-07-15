package com.param.global.service;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.RelationMasterDto;
@SuppressWarnings("rawtypes")
public interface IRelationMasterService {

	@Transactional
	Response getRelationMasterList(RelationMasterDto relationMasterDto)throws ApplicationException;

	@Transactional
	Response addRelationMaster(RelationMasterDto relationMasterDto)throws ApplicationException;

	@Transactional
	Response getRelationById(RelationMasterDto relationMasterDto)throws ApplicationException;

	@Transactional
	Response updateRelationMaster(RelationMasterDto relationMasterDto)throws ApplicationException;

	@Transactional
	Response updateStatusForRelation(RelationMasterDto relationMasterDto) throws ApplicationException;

	@Transactional
	Response getActiveRelationList(Integer orgId) throws ApplicationException;

	@Transactional
	Response getRelationCount(RelationMasterDto relationMasterDto)throws ApplicationException;

	@Transactional
	Response getActiveRelationListForWebPage(RelationMasterDto relationMasterDto)throws ApplicationException;


}
