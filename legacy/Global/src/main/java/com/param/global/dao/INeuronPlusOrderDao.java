package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.OrderMasterDto;

public interface INeuronPlusOrderDao {

	Response saveNeuronPlusorderedData(OrderMasterDto dto);

}
