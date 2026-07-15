package com.param.billing.global.service;

import javax.transaction.Transactional;

import com.param.billing.common.Response;
import com.param.billing.global.dto.HospitalAccountMasterDto;

@SuppressWarnings("rawtypes")
@Transactional
public interface IHospitalAccountMasterService {

	Response getActiveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response saveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response getHospitalAccountMasterById(Integer hospitalAccountId, Integer orgId, Integer unitId);

	Response getHospitalAccountMasterList(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response updateHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response updateHospitalAccountMasterStatus(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response getHospitalAccountMasterCount(HospitalAccountMasterDto hospitalAccountMasterDto);

}
