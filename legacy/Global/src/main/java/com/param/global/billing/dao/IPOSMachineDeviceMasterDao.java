package com.param.global.billing.dao;

import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.common.Response;

public interface IPOSMachineDeviceMasterDao {

	public Response getPOSMachineDeviceMasterList(POSMachineDeviceMasterDto creditNoteMasterDto);
}
