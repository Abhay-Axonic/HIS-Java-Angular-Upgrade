package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.TallyIntegrationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface ITallyIntegrationService {
	public Response postOPLegderCreditData();
	public Response postDepositData(List<Integer> unitIds);
	public Response postDoctorShare(List<Integer> unitIds);
	public Response postCreditNotes(List<Integer> unitIds);
	
	/* IP procedures */
	public Response postIpSelfServLedger();
	public Response postIpCompServLedger();
	public Response postIPBSelfCollectionData();
	public Response postIPBCompCollectionData();
	public Response postAdvanceRefund(List<Integer> unitIds);
	public Response postBillRefund();
	Response postCompanySettlement(List<Integer> unitIds);
	Response postVisitFaData(List<Integer> unitIds);
	Response postSaleNotes(List<Integer> unitIds);
	Response postGrnNotes(List<Integer> unitIds);
	Response postCOGSPurchaseAndSale(List<Integer> unitIds);
	public Response inactiveTariffServiceBasedOnEffectiveDate();
	public Response getIpLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response getOpLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response getInvetoryLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response getDoctorShareLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response sendTallyData(List<TallyIntegrationDto> tallyIntegrationDto) throws ApplicationException;
	public Response exportTallyData(List<TallyIntegrationDto> tallyIntegrationDto) throws ApplicationException;
	public Response getAllLedgersNameFromHis(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response sendLedgerNameRequest(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response verifyLedgerNames(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response sendCreateLedgerRequest(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response refreshTallyLedgerNameList(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response insertDailyStock(List<Integer> unitIds)throws ApplicationException;
	public Response icuUtilization(List<Integer> unitIds);
	public Response callProcedureTallyMismatch() throws ApplicationException;

	public Response getTallyInterfaceCommunicationKeys(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;

	public Response sendRequestToTally(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
}