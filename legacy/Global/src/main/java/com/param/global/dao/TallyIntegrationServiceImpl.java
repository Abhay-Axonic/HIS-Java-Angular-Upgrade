package com.param.global.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.TallyIntegrationDto;
import com.param.global.dto.global.VisitorDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class TallyIntegrationServiceImpl implements ITallyIntegrationService, ICommonConstants{
	
	final static Logger logger = Logger.getLogger(TallyIntegrationServiceImpl.class);
	
	@Autowired
	private ITallyIntegrationDao iTallyIntegrationDao;
	
	@Override
	@Transactional
	public Response postVisitFaData(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postVisitFaData(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response postDepositData(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postDepositData(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response postDoctorShare(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postDoctorShare(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response postCreditNotes(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postCreditNotes(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response postCompanySettlement(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postCompanySettlement(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response postSaleNotes(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postSaleNotes(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response postGrnNotes(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postGrnNotes(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response postAdvanceRefund(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postAdvanceRefund(date,unitIds);
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response postIpCompServLedger() {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			Response response = iTallyIntegrationDao.postIpCompServLedger(date);
			if(response.getCode().equals(SUCCESS_CODE) && response.getObject() != null){
				boolean flag = (boolean)response.getObject();
				if(!flag)
					logger.error("Error occured during Ip company service ledger data posting for tally for date :: " + date);
			}
			return response;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response postIPBSelfCollectionData() {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			Response response = iTallyIntegrationDao.postIPBSelfCollectionData(date);
			if(response.getCode().equals(SUCCESS_CODE) && response.getObject() != null){
				boolean flag = (boolean)response.getObject();
				if(!flag)
					logger.error("Error occured during Ip self collection data posting for tally for date :: " + date);
			}
			return response;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response postIPBCompCollectionData() {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			Response response = iTallyIntegrationDao.postIPBCompCollectionData(date);
			if(response.getCode().equals(SUCCESS_CODE) && response.getObject() != null){
				boolean flag = (boolean)response.getObject();
				if(!flag)
					logger.error("Error occured during Ip company collection data posting for tally for date :: " + date);
			}
			return response;
		}catch(Exception e){
			
			throw e;
		}
	}

	

	@Override
	@Transactional
	public Response postBillRefund() {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			Response response = iTallyIntegrationDao.postBillRefund(date);
			if(response.getCode().equals(SUCCESS_CODE) && response.getObject() != null){
				boolean flag = (boolean)response.getObject();
				if(!flag)
					logger.error("Error occured during Bill refund data posting for tally for date :: " + date);
			}
			return response;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response postOPLegderCreditData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postIpSelfServLedger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Response postCOGSPurchaseAndSale(List<Integer> unitIds) {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "dd-MM-yyyy");
			return iTallyIntegrationDao.postCOGSPurchaseAndSale(date,unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response inactiveTariffServiceBasedOnEffectiveDate() {
		try{
			return iTallyIntegrationDao.inactiveTariffServiceBasedOnEffectiveDate();
			
		}catch(Exception e){
			
			throw e;
		}
	}
/* ---------------------------------------------------Tally Integration Related Functions Start--------------------------------------------*/	


	@Override
	@Transactional
	public Response getIpLedger(TallyIntegrationDto tallyIntegrationDto) 
		throws ApplicationException {
			try {
				return iTallyIntegrationDao.getIpLedger(tallyIntegrationDto);

			} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
			
	}

	@Override
	@Transactional
	public Response getOpLedger(TallyIntegrationDto tallyIntegrationDto) 
		throws ApplicationException {
			try {
				return iTallyIntegrationDao.getOpLedger(tallyIntegrationDto);

			} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
			
	}

	@Override
	@Transactional
	public Response getInvetoryLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try {
			return iTallyIntegrationDao.getInvetoryLedger(tallyIntegrationDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	
	@Override
	@Transactional
	public Response getDoctorShareLedger(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try {
			return iTallyIntegrationDao.getDoctorShareLedger(tallyIntegrationDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response sendTallyData(List<TallyIntegrationDto> tallyIntegrationDto) throws ApplicationException {
		try{
			if(tallyIntegrationDto!=null && !tallyIntegrationDto.isEmpty())
			{
				int i=0;
				String ledger="";
				String strDate;
				String voucherDate = "";
				String voucherType = "";
				String voucherNarration = "";
				Double billingAmount = 0.0;
				BigDecimal debit = new BigDecimal(0);
				BigDecimal credit = new BigDecimal(0);
				String uniqueNumber1 = "";
				int uniqueNumber = 0;
				String instrumentDate = "";
				String bankName = "";
				String instrumentNumber = "";
				String voucherNo1 = "";
				String nextVoucherNo1 = "";
				Boolean boolVoucher = false;
				Boolean changeVoucherNo = false;
				
				String xmlstc = (("<ENVELOPE>" + "\r\n") 
				            + (("<HEADER>" + "\r\n") 
				            + (("<TALLYREQUEST>Import Data</TALLYREQUEST>" + "\r\n") 
				            + (("</HEADER>" + "\r\n") + ("<BODY>" + "\r\n")))));
				
	
				StartSelf:
					for(i=0; i<tallyIntegrationDto.size(); i++)
					{
						
				
					ledger = tallyIntegrationDto.get(i).getLedger();
					voucherDate = tallyIntegrationDto.get(i).getVoucherDate();
					voucherType = tallyIntegrationDto.get(i).getVoucherType();
					voucherNarration = tallyIntegrationDto.get(i).getVoucherNarration();
					uniqueNumber1 = tallyIntegrationDto.get(i).getUniqueNumber();
					instrumentDate = tallyIntegrationDto.get(i).getInstrumentDate();
					bankName = tallyIntegrationDto.get(i).getBankName();
					instrumentNumber = tallyIntegrationDto.get(i).getInstrumentNumber();
					voucherNo1 = tallyIntegrationDto.get(i).getVoucherNo();
					debit = tallyIntegrationDto.get(i).getDebit();
					credit = tallyIntegrationDto.get(i).getCredit();
					strDate = tallyIntegrationDto.get(i).getVoucherDate();
					
					
					if((uniqueNumber1 == null) || ((uniqueNumber1.trim()).isEmpty())){
						uniqueNumber = 0;
					}
					else
					{
						uniqueNumber = Integer.parseInt(tallyIntegrationDto.get(i).getUniqueNumber());
					}
					
					
					
					if((i<=tallyIntegrationDto.size())&& !(voucherNo1.trim().isEmpty()))
					{
						if(i<(tallyIntegrationDto.size()-1))
						{
							nextVoucherNo1 = tallyIntegrationDto.get(i+1).getVoucherNo();
						}
						
						else if(i<tallyIntegrationDto.size())
						{
							
							nextVoucherNo1 = tallyIntegrationDto.get(i).getVoucherNo();
						}
						changeVoucherNo = (voucherNo1.equals(nextVoucherNo1));
					
					
					
					
					
					if(boolVoucher==false){
						
						xmlstc = ((xmlstc + ("<IMPORTDATA> <REQUESTDESC> <REPORTNAME>All Masters</REPORTNAME> </REQUESTDESC> " + ("\r\n" + ("<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">" + "\r\n")))) + ("<VOUCHER VCHTYPE=" + ("\"" 
			                    + (voucherType+ ("\"" 
			                    + ((" ACTION=\"Create\">" + "\r\n") + ("<DATE>" 
			                    + (voucherDate
			                    + (("</DATE>" + "\r\n") + ("<NARRATION>" 
			                    + (voucherNarration 
			                    + (("</NARRATION>" + "\r\n") + ("<VOUCHERTYPENAME>" 
			                    + (voucherType 
			                    + (("</VOUCHERTYPENAME>" + "\r\n") + ("<EFFECTIVEDATE>" 
			                    + (voucherDate + ("</EFFECTIVEDATE>" + "\r\n"))))))))))))))))));
						
						boolVoucher = true;
					}
					

						if ((debit.compareTo(BigDecimal.ZERO))==0) {
                            xmlstc = (xmlstc 
                                        + (("<ALLLEDGERENTRIES.LIST>" + "\r\n") + ("<LEDGERNAME>" 
                                        + (ledger 
                                        + (("</LEDGERNAME>" + "\r\n") 
                                        + (("<REMOVEZEROENTRIES>NO</REMOVEZEROENTRIES> <LEDGERFROMITEM>NO</LEDGERFROMITEM> <ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>"+"\r\n")
                                        + ("<AMOUNT>" 
                                        + (credit
                                        + (("</AMOUNT>" + "\r\n") + ("</ALLLEDGERENTRIES.LIST>" + "\r\n"))))))))));
						}
                        
						else {
                            xmlstc = (xmlstc 
                                        + (("<ALLLEDGERENTRIES.LIST>" + "\r\n") + ("<LEDGERNAME>" 
                                        + (ledger 
                                        + (("</LEDGERNAME>" + "\r\n") 
                                        + (("<REMOVEZEROENTRIES>NO</REMOVEZEROENTRIES> <LEDGERFROMITEM>NO</LEDGERFROMITEM> <ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>" + "\r\n")
                                        + ("<AMOUNT>-"
                                        + (debit +  ("</AMOUNT>" + "\r\n")))))))));
						
                            	
                            if(uniqueNumber!=0){
                                xmlstc = (xmlstc 
                                            + (("<BANKALLOCATIONS.LIST>" + "\r\n") + ("<DATE>" 
                                            + (voucherDate
                                            + (("</DATE>" + "\r\n") + ("<INSTRUMENTDATE>" 
                                            + (instrumentDate
                                            + (("</INSTRUMENTDATE>" + "\r\n") + ("<NAME>" 
                                            + (uniqueNumber 
                                            + (("</NAME>" + "\r\n") + ("<TRANSACTIONTYPE>" + ("Cheque/DD" 
                                            + (("</TRANSACTIONTYPE>" + "\r\n") + ("<BANKNAME>" 
                                            + (bankName 
                                            + (("</BANKNAME>" + "\r\n") + ("<PAYMENTFAVOURING>" 
                                            + (voucherNarration 
                                            + (("</PAYMENTFAVOURING>" + "\r\n") + ("<INSTRUMENTNUMBER>" 
                                            + (instrumentNumber
                                            + (("</INSTRUMENTNUMBER>" + "\r\n") + ("<UNIQUEREFERENCENUMBER>" 
                                            + (uniqueNumber
                                            + (("</UNIQUEREFERENCENUMBER>" + "\r\n") 
                                            + (("<STATUS>No</STATUS>" + "\r\n") 
                                            + (("<PAYMENTMODE>Transacted</PAYMENTMODE>" + "\r\n") 
                                            + (("<BANKPARTYNAME></BANKPARTYNAME>" + "\r\n") 
                                            + (("<ISCONNECTEDPAYMENT>No</ISCONNECTEDPAYMENT>" + "\r\n") 
                                            + (("<ISSPLIT>No</ISSPLIT>" + "\r\n") 
                                            + (("<ISCONTRACTUSED>No</ISCONTRACTUSED>" + "\r\n") 
                                            + (("<CHEQUEPRINTED>1</CHEQUEPRINTED>" + "\r\n") + ("<AMOUNT>-" 
                                            + (debit 
                                            + (("</AMOUNT>" + "\r\n") 
                                            + (("<CONTRACTDETAILS.LIST></CONTRACTDETAILS.LIST>" + "\r\n") + ("</BANKALLOCATIONS.LIST>" + "\r\n"))))))))))))))))))))))))))))))))))))));
                            
                            	}
                            xmlstc = (xmlstc + ("</ALLLEDGERENTRIES.LIST>" + "\r\n"));
					}
						 if (changeVoucherNo==false) {
     						
     						xmlstc = (xmlstc + ("</VOUCHER>" + ("\r\n" + ("</TALLYMESSAGE>" + ("\r\n" + ("</IMPORTDATA>" + "\r\n"))))));
     						boolVoucher = false;
     						continue StartSelf;
     					}	
						
					}
			} 
				if(tallyIntegrationDto.size()>1)
				{
					
					xmlstc = (xmlstc + ("</VOUCHER>" + ("\r\n" + ("</TALLYMESSAGE>" + ("\r\n" + ("</IMPORTDATA>" + "\r\n"))))));
					boolVoucher = false;
				
				}
					
					xmlstc = (xmlstc 
	                        + (("</BODY>" + "\r\n") 
	                        + "</ENVELOPE>"));


					return new Response(SUCCESS, null, TALLY_SUCCESS, null, xmlstc);

		}
		else
		{
				return new Response(ERROR, null, TALLY_NOT_FOUND, null, null);
		}
					
			}catch (Exception e) 
			{
		throw new ApplicationException(e.getMessage(), e);
	}
				
	}


	@Override
	@Transactional
	public Response exportTallyData(List<TallyIntegrationDto> tallyIntegrationDto) throws ApplicationException {
		try{
			if(tallyIntegrationDto!=null && tallyIntegrationDto.size()>0)
			{
				int i=0;
				String ledger="";
				String strDate;
				String voucherDate = "";
				String voucherType = "";
				String voucherNarration = "";
				Double billingAmount = 0.0;
				BigDecimal debit = new BigDecimal(0);
				BigDecimal credit = new BigDecimal(0);
				String uniqueNumber1 = "";
				int uniqueNumber = 0;
				String instrumentDate = "";
				String bankName = "";
				String instrumentNumber = "";
				String voucherNo1 = "";
				String nextVoucherNo1 = "";
				Boolean boolVoucher = false;
				Boolean changeVoucherNo = false;
				
				String xmlstc = (("<ENVELOPE>" + "\r\n") 
				            + (("<HEADER>" + "\r\n") 
				            + (("<TALLYREQUEST>Import Data</TALLYREQUEST>" + "\r\n") 
				            + (("</HEADER>" + "\r\n") + ("<BODY>" + "\r\n")))));
				
	
				StartSelf:
					for(i=0; i<tallyIntegrationDto.size(); i++)
					{
						
					ledger = tallyIntegrationDto.get(i).getLedger();
					voucherDate = tallyIntegrationDto.get(i).getVoucherDate();
					voucherType = tallyIntegrationDto.get(i).getVoucherType();
					voucherNarration = tallyIntegrationDto.get(i).getVoucherNarration();
					uniqueNumber1 = tallyIntegrationDto.get(i).getUniqueNumber();
					instrumentDate = tallyIntegrationDto.get(i).getInstrumentDate();
					bankName = tallyIntegrationDto.get(i).getBankName();
					instrumentNumber = tallyIntegrationDto.get(i).getInstrumentNumber();
					voucherNo1 = tallyIntegrationDto.get(i).getVoucherNo();
					debit = tallyIntegrationDto.get(i).getDebit();
					credit = tallyIntegrationDto.get(i).getCredit();
					strDate = tallyIntegrationDto.get(i).getVoucherDate();
					
					
					if((uniqueNumber1 == null) || ((uniqueNumber1.trim()).isEmpty())){
						uniqueNumber = 0;
					}
					else
					{
						uniqueNumber = Integer.parseInt(tallyIntegrationDto.get(i).getUniqueNumber());
					}
					
					
					
					if(i<=tallyIntegrationDto.size())
					{
						if(i<(tallyIntegrationDto.size()-1))
						{
							nextVoucherNo1 = tallyIntegrationDto.get(i+1).getVoucherNo();
						}
						
						else if(i<tallyIntegrationDto.size())
						{
							
							nextVoucherNo1 = tallyIntegrationDto.get(i).getVoucherNo();
						}
					}
					
					changeVoucherNo = (voucherNo1.equals(nextVoucherNo1));
					
					
					if(boolVoucher==false){
						
						xmlstc = ((xmlstc + ("<IMPORTDATA> <REQUESTDESC> <REPORTNAME>All Masters</REPORTNAME> </REQUESTDESC> " + ("\r\n" + ("<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">" + "\r\n")))) + ("<VOUCHER VCHTYPE=" + ("\"" 
			                    + (voucherType+ ("\"" 
			                    + ((" ACTION=\"Create\">" + "\r\n") + ("<DATE>" 
			                    + (voucherDate
			                    + (("</DATE>" + "\r\n") + ("<NARRATION>" 
			                    + (voucherNarration 
			                    + (("</NARRATION>" + "\r\n") + ("<VOUCHERTYPENAME>" 
			                    + (voucherType 
			                    + (("</VOUCHERTYPENAME>" + "\r\n") + ("<EFFECTIVEDATE>" 
			                    + (voucherDate + ("</EFFECTIVEDATE>" + "\r\n"))))))))))))))))));
						
						boolVoucher = true;
					}
					

						if ((debit.compareTo(BigDecimal.ZERO))==0) {
                            xmlstc = (xmlstc 
                                        + (("<ALLLEDGERENTRIES.LIST>" + "\r\n") + ("<LEDGERNAME>" 
                                        + (ledger 
                                        + (("</LEDGERNAME>" + "\r\n") 
                                        + (("<REMOVEZEROENTRIES>NO</REMOVEZEROENTRIES> <LEDGERFROMITEM>NO</LEDGERFROMITEM> <ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>"+"\r\n")
                                        + ("<AMOUNT>" 
                                        + (credit
                                        + (("</AMOUNT>" + "\r\n") + ("</ALLLEDGERENTRIES.LIST>" + "\r\n"))))))))));
						}
                        
						else {
                            xmlstc = (xmlstc 
                                        + (("<ALLLEDGERENTRIES.LIST>" + "\r\n") + ("<LEDGERNAME>" 
                                        + (ledger 
                                        + (("</LEDGERNAME>" + "\r\n") 
                                        + (("<REMOVEZEROENTRIES>NO</REMOVEZEROENTRIES> <LEDGERFROMITEM>NO</LEDGERFROMITEM> <ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>" + "\r\n")
                                        + ("<AMOUNT>-"
                                        + (debit +  ("</AMOUNT>" + "\r\n")))))))));
						
                            	
                            if(uniqueNumber!=0){
                                xmlstc = (xmlstc 
                                            + (("<BANKALLOCATIONS.LIST>" + "\r\n") + ("<DATE>" 
                                            + (voucherDate
                                            + (("</DATE>" + "\r\n") + ("<INSTRUMENTDATE>" 
                                            + (instrumentDate
                                            + (("</INSTRUMENTDATE>" + "\r\n") + ("<NAME>" 
                                            + (uniqueNumber 
                                            + (("</NAME>" + "\r\n") + ("<TRANSACTIONTYPE>" + ("Cheque/DD" 
                                            + (("</TRANSACTIONTYPE>" + "\r\n") + ("<BANKNAME>" 
                                            + (bankName 
                                            + (("</BANKNAME>" + "\r\n") + ("<PAYMENTFAVOURING>" 
                                            + (voucherNarration 
                                            + (("</PAYMENTFAVOURING>" + "\r\n") + ("<INSTRUMENTNUMBER>" 
                                            + (instrumentNumber
                                            + (("</INSTRUMENTNUMBER>" + "\r\n") + ("<UNIQUEREFERENCENUMBER>" 
                                            + (uniqueNumber
                                            + (("</UNIQUEREFERENCENUMBER>" + "\r\n") 
                                            + (("<STATUS>No</STATUS>" + "\r\n") 
                                            + (("<PAYMENTMODE>Transacted</PAYMENTMODE>" + "\r\n") 
                                            + (("<BANKPARTYNAME></BANKPARTYNAME>" + "\r\n") 
                                            + (("<ISCONNECTEDPAYMENT>No</ISCONNECTEDPAYMENT>" + "\r\n") 
                                            + (("<ISSPLIT>No</ISSPLIT>" + "\r\n") 
                                            + (("<ISCONTRACTUSED>No</ISCONTRACTUSED>" + "\r\n") 
                                            + (("<CHEQUEPRINTED>1</CHEQUEPRINTED>" + "\r\n") + ("<AMOUNT>-" 
                                            + (debit 
                                            + (("</AMOUNT>" + "\r\n") 
                                            + (("<CONTRACTDETAILS.LIST></CONTRACTDETAILS.LIST>" + "\r\n") + ("</BANKALLOCATIONS.LIST>" + "\r\n"))))))))))))))))))))))))))))))))))))));
                            
                            	}
                            xmlstc = (xmlstc + ("</ALLLEDGERENTRIES.LIST>" + "\r\n"));
					}
						 if (changeVoucherNo==false) {
     						
     						xmlstc = (xmlstc + ("</VOUCHER>" + ("\r\n" + ("</TALLYMESSAGE>" + ("\r\n" + ("</IMPORTDATA>" + "\r\n"))))));
     						boolVoucher = false;
     						continue StartSelf;
     					}	
						
				
			} 
				if(tallyIntegrationDto.size()>1)
				{
					
					xmlstc = (xmlstc + ("</VOUCHER>" + ("\r\n" + ("</TALLYMESSAGE>" + ("\r\n" + ("</IMPORTDATA>" + "\r\n"))))));
					boolVoucher = false;
				
				}
					
					xmlstc = (xmlstc 
	                        + (("</BODY>" + "\r\n") 
	                        + "</ENVELOPE>"));
					
					
					
					return new Response(SUCCESS, null, TALLY_SUCCESS, null, xmlstc);
	
		}
		else
		{
				return new Response(ERROR, null, TALLY_NOT_FOUND, null, null);
		}
					
			}catch (Exception e) 
			{
		throw new ApplicationException(e.getMessage(), e);
	}
				
	}

	
	@Override
	@Transactional
	public Response getAllLedgersNameFromHis(TallyIntegrationDto tallyIntegrationDto) 
		throws ApplicationException {
			try {
				return iTallyIntegrationDao.getAllLedgersNameFromHis(tallyIntegrationDto);

			} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
			
	}

	@Override
	@Transactional
	public Response sendLedgerNameRequest(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			String StrRequestXML = ("<ENVELOPE>" + ("\r\n" 
									+ ("<HEADER>" + ("\r\n" 
									+ ("<TALLYREQUEST>Export Data</TALLYREQUEST> " + ("\r\n" 
									+ ("</HEADER>" + ("\r\n" 
									+ ("<BODY>" + ("\r\n" 
									+ ("<EXPORTDATA>" + ("\r\n" 
									+ ("<REQUESTDESC>" + ("\r\n" 
									+ ("<REPORTNAME>List of Accounts</REPORTNAME>" + ("\r\n" 
									+ ("<STATICVARIABLES>" + ("\r\n" 
									+ ("<SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>" + ("\r\n" 
									+ ("<ACCOUNTTYPE>Ledgers</ACCOUNTTYPE>" + ("\r\n" 
									+ ("</STATICVARIABLES>" + ("\r\n" 
									+ ("</REQUESTDESC>" + ("\r\n" 
									+ ("</EXPORTDATA>" + ("\r\n" 
									+ ("</BODY>" + ("\r\n" 
									+ "</ENVELOPE>"))))))))))))))))))))))))))))));

			
			return new Response(SUCCESS, null, TALLY_SUCCESS, null, StrRequestXML);
			
			}catch (Exception e) 
			{
		throw new ApplicationException(e.getMessage(), e);
	}
				
	}

	@Override
	public Response verifyLedgerNames(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try
		{
			List<String>tallyLedgerNamesList = new ArrayList<>();
			List<String>tallyLedgerNamesListLowerCase = new ArrayList<>();
			String ledgerNamesFromTally = tallyIntegrationDto.getLedgerNamesFromTally();
			String checkedLedgerName = "";
			String ledgerNameTest = "";
			String [] ledgerNamesFromTally_CR = ledgerNamesFromTally.split("\r\n");
			int lenOfDto = 0;


			for(String value_split_CR1 : ledgerNamesFromTally_CR){
				if(value_split_CR1.contains(("<LEDGER NAME").trim())){
					String[] value_split = value_split_CR1.split(("\"").trim());

					String str = value_split[1].trim();
					checkedLedgerName = str.replace("&amp;", "&");
					tallyLedgerNamesList.add(checkedLedgerName);
					// System.out.println(finalStr);
				}
			}

			for (int i = 0; i<tallyLedgerNamesList.size(); i++){

				tallyLedgerNamesListLowerCase.add(tallyLedgerNamesList.get(i).toLowerCase());

			}

			if(tallyIntegrationDto!=null && tallyIntegrationDto.getLedgerNamesFromHis().size()>0){
				boolean check = true;
				lenOfDto = tallyIntegrationDto.getLedgerNamesFromHis().size();

				for(int i=0; i<lenOfDto; i++){

					check = tallyLedgerNamesListLowerCase.contains(tallyIntegrationDto.getLedgerNamesFromHis().get(i).getLedger().toLowerCase());

					if(i<lenOfDto-1){
						if(!check){

							ledgerNameTest = tallyIntegrationDto.getLedgerNamesFromHis().get(i).getLedger();
							return new Response(ERROR, null, Ledger_Not_Found, tallyLedgerNamesList, ledgerNameTest);

						}
					}

					else if(i==lenOfDto-1){
						if(check){

							return new Response(SUCCESS, null, TALLY_SUCCESS, tallyLedgerNamesList, check);
						}
						else{

							ledgerNameTest = tallyIntegrationDto.getLedgerNamesFromHis().get(i).getLedger();
							return new Response(ERROR, null, Ledger_Not_Found, tallyLedgerNamesList, ledgerNameTest);

						}
					}

				}
			}

		} catch (Exception e)
		{
		logger.error("verifyLedgerNames failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response sendCreateLedgerRequest(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			
			String newLedgerName = tallyIntegrationDto.getNewLedger();
			String underGroup = tallyIntegrationDto.getUnderGroupName();
			String StrRequestXML = "";
			StrRequestXML = ("<ENVELOPE>" + ("\r\n" 
						+ ("<HEADER>" + ("\r\n" 
						+ ("<VERSION>1</VERSION>" + ("\r\n" 
						+ ("<TALLYREQUEST>Import </TALLYREQUEST>" + ("\r\n" 
						+ ("<TYPE>Data</TYPE>" + ("\r\n" 
						+ ("<ID>All Masters</ID>" + ("\r\n" 
						+ ("</HEADER>" + ("\r\n" + ("<BODY>" 
						+ ("\r\n" + ("<DESC>" + ("\r\n" 
						+ ("<STATICVARIABLES>" + ("\r\n" 
						+ ("<SVCURRENTCOMPANY>##SVCurrentCompany</SVCURRENTCOMPANY>" + ("\r\n" 
						+ ("</STATICVARIABLES>" + ("\r\n" 
						+ ("</DESC>" + ("\r\n" 
						+ ("<DATA>" + ("\r\n" 
						+ ("<TALLYMESSAGE>" + ("\r\n" 
						+ ("<LEDGER>" + ("\r\n" 
						+ ("<NAME.LIST>" + ("\r\n" 
						+ ("<NAME>"+ (newLedgerName.trim() + ("</NAME>" + "\r\n")))))))))))))))))))))))))))))))))))));
			
			StrRequestXML = (StrRequestXML + ("</NAME.LIST>" + ("\r\n" 
						+ ("<PARENT>" + (underGroup.trim() + ("</PARENT>" + "\r\n"))))));
			
			StrRequestXML = (StrRequestXML + ("<ADDRESS.LIST>" + "\r\n"));
			
			StrRequestXML = (StrRequestXML + ("<ADDRESS></ADDRESS>" + "\r\n"));
			
			StrRequestXML = (StrRequestXML + ("</ADDRESS.LIST>" + "\r\n"));
			
			StrRequestXML = (StrRequestXML + ("</LEDGER>" + ("\r\n" 
						+ ("</TALLYMESSAGE>" + ("\r\n" 
						+ ("</DATA>" + ("\r\n" 
						+ ("</BODY>" + ("\r\n" 
						+ ("</ENVELOPE>" + "\r\n"))))))))));
			
		
			
			return new Response(SUCCESS, null, TALLY_SUCCESS, null, StrRequestXML);
			
			}catch (Exception e) 
			{
		throw new ApplicationException(e.getMessage(), e);
	}
				
	}

	@Override
	public Response refreshTallyLedgerNameList(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			List<String>tallyLedgerNamesList = new ArrayList<>();
			String ledgerNamesFromTally = tallyIntegrationDto.getLedgerNamesFromTally();
			String ledgerName = "";
			String [] ledgerNamesFromTally_CR = ledgerNamesFromTally.split("\r\n");



		   for(String value_split_CR1 : ledgerNamesFromTally_CR){
		       if(value_split_CR1.contains(("<LEDGER NAME").trim())){
		           String[] value_split = value_split_CR1.split(("\"").trim());

		           String str = value_split[1].trim();
		           ledgerName = str.replace("&amp;", "&");
		           tallyLedgerNamesList.add(ledgerName);
		          
		       }
		   }
		   
		   return new Response(SUCCESS, null, TALLY_SUCCESS, tallyLedgerNamesList, null);
	
		}catch (Exception e) 
		{
		logger.error("refreshTallyLedgerNameList failed", e);
		}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response insertDailyStock(List<Integer> unitIds) throws ApplicationException {
		try{
			return iTallyIntegrationDao.postDailyStock(unitIds);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response icuUtilization(List<Integer> unitIds) {
		try{
			return iTallyIntegrationDao.icuUtilization(unitIds);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	@Transactional
	public Response callProcedureTallyMismatch() throws ApplicationException {
		try{
			return iTallyIntegrationDao.callProcedureTallyMismatch();
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTallyInterfaceCommunicationKeys(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			return iTallyIntegrationDao.getTallyInterfaceCommunicationKeys(tallyIntegrationDto);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	@Transactional
	public Response sendRequestToTally(TallyIntegrationDto tallyIntegrationDto) throws ApplicationException {
		try{
			String tallyUrl = tallyIntegrationDto.getTallyIp() + ":" + tallyIntegrationDto.getTallyPortNo() + "/";

			OkHttpClient client = new OkHttpClient();
			MediaType xml = MediaType.parse("application/xml");
			RequestBody body = RequestBody.create(xml, tallyIntegrationDto.getRequestXmlString());

			Request request = new Request.Builder()
					.url(tallyUrl)
					.post(body)
					.addHeader("Content-Type", "application/xml")
					.build();

			try (okhttp3.Response response = client.newCall(request).execute()) {
				String responseBody = response.body().string();
//				System.out.println(responseBody);
				return new Response(SUCCESS, SUCCESS_CODE, TALLY_SUCCESS, null, responseBody);
			} catch (Exception e) {
			logger.error("sendRequestToTally failed", e);
				return new Response(ERROR, ERROR_CODE, "Tally request failed: "+ e.getMessage(), null, null);
			}
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}
}
