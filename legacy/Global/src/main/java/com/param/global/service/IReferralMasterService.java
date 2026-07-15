package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ReferralMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IReferralMasterService {

	@Transactional
	//Response addReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException;
	
	Response saveReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException;

	@Transactional 
	Response getReferralMasterList(ReferralMasterDto referralMasterDto) throws ApplicationException;

	@Transactional
	Response updateReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException;

	@Transactional
	Response getReferralById(ReferralMasterDto referralMasterDto) throws ApplicationException;

	@Transactional
	Response updateStatusForReferral(ReferralMasterDto referralMasterDto) throws ApplicationException;

	@Transactional
	Response getActiveReferralList() throws ApplicationException;
	
	@Transactional
	Response getAreaByCityId(AreaMasterDto areaMasterDto) throws ApplicationException;

	@Transactional
	Response getReferralCount(ReferralMasterDto referralMasterDto)throws ApplicationException;

	@Transactional
	Response getReferralListByReferralTypeId(ReferralMasterDto referralMasterDto)throws ApplicationException;

	@Transactional
	Response getReferralListByReferralTypeIds(ReferralMasterDto referralMasterDto);

	@Transactional
	Response getReferralListForTypeSearch(ReferralMasterDto referralMasterDto);

	@Transactional
	Response getAreaByCityIdPagination(AreaMasterDto areaMasterDto)throws ApplicationException;

	@Transactional
	Response getReferralListByReferralName(ReferralMasterDto referralMasterDto)throws ApplicationException;

	@Transactional
	Response getReferralListByOrgId(ReferralMasterDto referralMasterDto)throws ApplicationException;

	@Transactional
	public void invalidateAreaByCityPaginationCache(Integer cityId)throws ApplicationException;
}
