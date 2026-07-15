package com.param.inventory.stock.service.openingBalance;

import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;

public interface IOpeningBalanceService {

	Object getOpeningBalanceList(OpeningBalanceRequest request);

	Object saveOpeningBalance(OpeningBalanceRequest request);

	Object getOpeningBalance(Integer id);

	Object updateOpeningBalance(OpeningBalanceRequest request);

	Object cancelOpeningBalance(OpeningBalanceRequest request);

	Object approveOpeningBalance(OpeningBalanceRequest request);

	Object approveOpeningBalanceDetail(OpeningBalanceRequest request);

	Object getOpeningBalancePT(Integer id, Integer unitId, Integer orgId);

}
