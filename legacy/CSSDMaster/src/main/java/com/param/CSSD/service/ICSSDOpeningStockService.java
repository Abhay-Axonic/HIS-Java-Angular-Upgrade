package com.param.CSSD.service;

import com.param.CSSD.dto.CssdOpeningBalanceRequest;
import com.param.CSSD.dto.CssdOpeningStockDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ICSSDOpeningStockService {

	Response saveOpeningStock(CssdOpeningStockDto cssdOpeningStockDto);

	Response getCssdOpeningBalanceList(CssdOpeningBalanceRequest cssdOpeningStockDto);

	Response approveCssdOpeningBalanceStock(CssdOpeningBalanceRequest request);

	Response cancelCssdOpeningBalanceStock(CssdOpeningBalanceRequest request);

	Response getCssdOpeningBalanceImplantByOpeningBalanceId(CssdOpeningBalanceRequest request);

	Response updateCssdOpeningBalanceStock(CssdOpeningStockDto cssdOpeningStockDto);
	
}
	