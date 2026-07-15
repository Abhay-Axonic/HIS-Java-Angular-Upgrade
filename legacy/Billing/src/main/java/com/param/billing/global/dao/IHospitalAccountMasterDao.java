package com.param.billing.global.dao;

import com.param.billing.common.Response;
import com.param.billing.global.dto.HospitalAccountMasterDto;

@SuppressWarnings({"rawtypes"})
public interface IHospitalAccountMasterDao  {

	Response getActiveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response getHospitalAccountMasterByName(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response saveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response getHospitalAccountMasterById(Integer hospitalAccountId, Integer orgId, Integer unitId);

	Response getHospitalAccountMasterList(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response getHospitalAccountMasterByNameNotId(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response updateHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response updateHospitalAccountMasterStatus(HospitalAccountMasterDto hospitalAccountMasterDto);

	Response getHospitalAccountMasterCount(HospitalAccountMasterDto hospitalAccountMasterDto);

	void updateAllHospitalAccount(HospitalAccountMasterDto hospitalAccountMasterDto);

}
