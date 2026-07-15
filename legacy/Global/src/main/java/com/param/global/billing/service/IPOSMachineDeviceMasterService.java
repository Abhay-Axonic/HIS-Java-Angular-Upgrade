package com.param.global.billing.service;

import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.common.Response;

public interface IPOSMachineDeviceMasterService {
	public Response getPOSMachineDeviceMasterList(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto);

	public Response savePOSMachineDeviceMaster(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto);

	public Response uploadToServerForPOSMachine(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto);

	public Response updatePOSMachineDetails(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto);
}
