package com.param.CSSD.dao;

import com.param.CSSD.dto.CssdOpeningBalanceRequest;
import com.param.CSSD.dto.CssdOpeningStockDto;
import com.param.CSSD.model.CssdOpeningBalanceDetailStock;
import com.param.CSSD.model.CssdOpeningBalanceStock;
import com.param.entity.model.inventory.Batch;
import com.param.global.common.Response;

public interface ICSSDOpeningStockDao {

	Response saveOpeningStock(CssdOpeningStockDto cssdOpeningStockDto);

	Response getCssdOpeningBalanceList(CssdOpeningBalanceRequest cssdOpeningStockDto);

	Batch saveBatch(Batch batch);

	CssdOpeningBalanceStock getCssdOpeningBalanceById(Integer opbId);

	CssdOpeningBalanceStock updateCssdOpeningBalance(CssdOpeningBalanceStock openingBalance);

	Response getCssdOpeningBalanceImplantByOpeningBalanceId(CssdOpeningBalanceRequest request);

	Response updateCssdOpeningBalanceStock(CssdOpeningStockDto cssdOpeningStockDto);

	Response saveStockTransactionLog(CssdOpeningBalanceDetailStock details, Integer storeId);


}
