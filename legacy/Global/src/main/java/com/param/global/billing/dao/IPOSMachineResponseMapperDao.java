package com.param.global.billing.dao;

import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.common.Response;

public interface IPOSMachineResponseMapperDao {

	Response savePOSMachineUniqueId(POSMachineResponseMapperDto dto);

	Response getPOSMachineUniqueId(String invoice);

	Response updatePosMachineTransactionResponse(POSMachineResponseMapperDto dto1);

	Response checkGeneratedResponseAvailable(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto);

	Response updatePOSMachineResponseMapperFlag(POSMachineResponseMapperDto pOSMachineResponseMapperDto);

	Response getPOSMachineConfigDetails(POSMachineResponseMapperDto dto);
}
