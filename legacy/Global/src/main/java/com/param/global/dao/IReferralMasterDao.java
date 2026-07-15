package com.param.global.dao;

import com.param.adt.master.global.model.ReferralMaster;
import com.param.global.common.Response;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ReferralMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IReferralMasterDao extends IGenericDao<ReferralMaster, Integer>{

	
	Response getReferralByName(ReferralMasterDto referralMasterDto) throws ApplicationException;

	Response addReferralTypeTypeMaster(ReferralMasterDto referralMasterDto) throws ApplicationException;

	Response getReferralMasterList(ReferralMasterDto referralMasterDto) throws ApplicationException;

	Response getReferralById(ReferralMasterDto referralMasterDto) throws ApplicationException;

	Response getActiveReferralList() throws ApplicationException;

	Response getReferralByNameNotId(ReferralMasterDto referralMasterDto)throws ApplicationException;

	Response updateReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException;

	Response updateReferralMasterStatus(ReferralMasterDto referralMasterDto)throws ApplicationException;

	Response getAreaByCityId(AreaMasterDto areaMasterDto) throws ApplicationException;

	Response getCount(ReferralMasterDto referralMasterDto)throws ApplicationException;

	Response getReferralListByReferralTypeId(ReferralMasterDto referralMasterDto)throws ApplicationException;

	Response getReferralListByReferralTypeIds(ReferralMasterDto referralMasterDto);

	Response getReferralListForTypeSearch(ReferralMasterDto referralMasterDto);

	Response getAreaByCityIdPagination(AreaMasterDto areaMasterDto)throws ApplicationException;

	Response getReferralListByReferralName(ReferralMasterDto referralMasterDto)throws ApplicationException;

	Response getReferralListByOrgId(ReferralMasterDto referralMasterDto)throws ApplicationException;

}
