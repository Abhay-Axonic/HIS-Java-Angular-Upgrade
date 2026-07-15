package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.TallyIntegrationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface ITallyIntegrationDao {
	public Response postOPLegderCreditData(String date);
	public Response postDepositData(String date, List<Integer> unitIds);
	public Response postDoctorShare(String date, List<Integer> unitIds);
	public Response postCreditNotes(String date, List<Integer> unitIds);
	
	/* IP procedures */
	public Response postIpSelfServLedger(String date);
	public Response postIpCompServLedger(String date);
	public Response postIPBSelfCollectionData(String date);
	public Response postIPBCompCollectionData(String date);
	public Response postAdvanceRefund(String date, List<Integer> unitIds);
	public Response postBillRefund(String date);
	Response postVisitFaData(String date, List<Integer> unitIds);
	Response postCompanySettlement(String date, List<Integer> unitIds);
	Response postSaleNotes(String date, List<Integer> unitIds);
	Response postGrnNotes(String date, List<Integer> unitIds);
	Response postCOGSPurchaseAndSale(String date, List<Integer> unitIds);
	public Response inactiveTariffServiceBasedOnEffectiveDate();
	public Response getIpLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response getOpLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response getInvetoryLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response getDoctorShareLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response getAllLedgersNameFromHis(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
	public Response postDailyStock(List<Integer> unitIds)throws ApplicationException;

	public Response  icuUtilization(List<Integer> unitIds)throws ApplicationException;
    public Response callProcedureTallyMismatch() throws ApplicationException;

    public Response getTallyInterfaceCommunicationKeys(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException;
}
