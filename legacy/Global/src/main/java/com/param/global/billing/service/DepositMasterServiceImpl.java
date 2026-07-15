	package com.param.global.billing.service;

import java.io.File;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableList;

import com.param.billing.global.model.DepositMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dao.IPaymentDepositDetailsDao;
import com.param.global.billing.dto.DepositCollectionReqDto;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.DepositSearchReqDto;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.billing.dto.VisitTypeEncounterReqDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.mail.IMailingService;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.org.common.dao.IPatientLoyaltyCardMapperDao;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.global.utility.SerialNoUtility;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;  

@Service
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class DepositMasterServiceImpl implements IDepositMasterService, ICommonConstants,IBillingCommonConstants{

	final static Logger logger = Logger.getLogger(DepositMasterServiceImpl.class);

	@Autowired
	private IDepositMasterDao iDepositMasterDao;
	
	@Autowired
	IMpesaMasterDao iMpesaMasterDao;
	
	@Autowired
	private SerialNoUtility serialNoUtility;
	@Autowired
	private IPaymentDepositDetailsDao iPaymentDepositDetailsDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	private IPatientLoyaltyCardMapperDao iPatientLoyaltyCardMapperDao;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	IMailingService iMailingService;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;
	
	@Override
	@Transactional
	public Response addDeposit(DepositCollectionReqDto reqDto) throws BillingException {
		try{
			
			DepositMaster depositMaster =null;
			Date date = TimeZoneComponent.getDateByzone(reqDto.getDepositMasterDto().getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			List<PatientLoyaltyCardMapperDto>  mailList =  new 	ArrayList<PatientLoyaltyCardMapperDto>();  
		
			
			ListIterator<PaymentDepositDetailsDto> paymentMPesaItr = reqDto.getListPaymentDepositDetailsDto().listIterator();
			PaymentDepositDetailsDto detailsMPesaDto = null;			

					
			
			//get current deposit id
			Long depositId = iDepositMasterDao.getCurrentId();
			//save deposit master 
			DepositMasterDto deposit = reqDto.getDepositMasterDto();
			deposit.setReceivedFrom(deposit.getReceivedFrom());
			deposit.setAvailableDeposit(deposit.getDepositAmount());
			deposit.setConsumedDeposit(0);
			deposit.setCreatedDate(dateTime);
			deposit.setDepositDate(dateTime);
			if(deposit.getSignetureImage()!=null && !deposit.getSignetureImage().isEmpty())
			{
				
				//BASE64Decoder decoder = new BASE64Decoder();
				//byte[] signature = decoder.decodeBuffer(deposit.getSignetureImage().split(",")[1]);
				 byte[]  signature = Base64.getDecoder().decode(new String(deposit.getSignetureImage().split(",")[1]).getBytes("UTF-8"));
				//byte[]  signature = Base64.getDecoder().decode(new String(deposit.getSignetureImage()).getBytes("UTF-8"));
				 
				deposit.setSignature(signature);
			}
			
			String depositNumber = iGlobalUnitMasterDao.getGeneratedNo(0, deposit.getUnitId(), depositScreenId);
			if(depositNumber==null)
			{
				return new Response<>(ERROR, ERROR_CODE, "Please Configure Deposit Number.", null, null);
			}
			
			
			deposit.setDepositNo(depositNumber);
			deposit.setStatus(ACTIVE);
			deposit.setUpdatedBy(deposit.getCreatedBy());
			if(deposit.getVisitTypeId()!=null)
			{
				if(deposit.getVisitTypeId().intValue()==OP)
				{
					deposit.setEncounterId(deposit.getEncounterId());
					deposit.setAdmissionId(null);
				}
				else if(deposit.getVisitTypeId().intValue()==IP||deposit.getVisitTypeId().intValue()==DC||deposit.getVisitTypeId().intValue()==ER)
				{
					deposit.setAdmissionId(deposit.getEncounterId());
					deposit.setEncounterId(null);
				}
			}
			deposit.setUpdatedDate(dateTime);
			Response depositRes = iDepositMasterDao.saveDeposit(deposit);
			if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null){
			  depositMaster = (DepositMaster)depositRes.getObject();
			  deposit.setDepositMasterId(depositMaster.getDepositMasterId());
				//save payment details
				ListIterator<PaymentDepositDetailsDto> paymentItr = reqDto.getListPaymentDepositDetailsDto().listIterator();
				PaymentDepositDetailsDto detailsDto = null;
				while(paymentItr.hasNext())
				{
					detailsDto = paymentItr.next();
					detailsDto.setCreatedDate(dateTime);
					detailsDto.setDepositId(depositMaster.getDepositMasterId());
					detailsDto.setUpdatedBy(deposit.getCreatedBy());
					detailsDto.setUpdatedDate(dateTime);
					detailsDto.setBalanceAmount(detailsDto.getAmount());
					detailsDto.setRefundAmount(0.00);
					iPaymentDepositDetailsDao.savePaymentDepositDetails(detailsDto);
				}
			}
			
			MpesaMasterDto masterDto = new MpesaMasterDto();
			while(paymentMPesaItr.hasNext()) { 
				detailsMPesaDto = paymentMPesaItr.next();	
				if(detailsMPesaDto.getPaymentModeId() == 5 && (detailsMPesaDto.getSkipMpesaValidation()!=null && detailsMPesaDto.getSkipMpesaValidation())) 
				{
					masterDto.setMpesaTxcode(detailsMPesaDto.getCardNo());	
					masterDto.setUnitId(reqDto.getDepositMasterDto().getUnitId());
					masterDto.setOrganizationId(reqDto.getDepositMasterDto().getOrgId());
					masterDto.setUpdatedBy(detailsMPesaDto.getCreatedBy());
					masterDto.setMpesaAmount(detailsMPesaDto.getAmount());
					iMpesaMasterDao.validateMpesaTrancastion(masterDto);
					
				}
				
				
				if(detailsMPesaDto.getPaymentModeId() == 12) 
				{
					if(detailsMPesaDto.getPaymentGatewayProviderId() == 1) {
						
						iMpesaMasterDao.updateRazorPay(detailsMPesaDto.getTransactionNo());
						
					}else if(detailsMPesaDto.getPaymentGatewayProviderId() == 2){
						
						iMpesaMasterDao.updateCCAvenue(detailsMPesaDto.getTransactionNo(),deposit.getDepositMasterId());
					}
					
				}
				 if(detailsMPesaDto.getPaymentModeId() == 3) 
	                {
	                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
	                	pOSMachineResponseMapperDto.setCreatedDate(dateTime);
	                	pOSMachineResponseMapperDto.setUpdatedDate(dateTime);
	                	pOSMachineResponseMapperDto.setUnitId(reqDto.getDepositMasterDto().getUnitId());
	                	pOSMachineResponseMapperDto.setOrganizationId(reqDto.getDepositMasterDto().getOrgId());
	                	pOSMachineResponseMapperDto.setUpdatedBy(detailsMPesaDto.getCreatedBy());
	                	pOSMachineResponseMapperDto.setIsConsumed(true);
	                	pOSMachineResponseMapperDto.setRequestedUniqueId(detailsMPesaDto.getRequestedUniqueId());
	                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
	                }
				
				if(detailsMPesaDto.getPaymentModeId()!=null && detailsMPesaDto.getPaymentModeId().intValue()==6)
				{
					PatientLoyaltyCardMapperDto dto =new PatientLoyaltyCardMapperDto();
					dto.setCardAmount(detailsMPesaDto.getAmount());
					dto.setCardNo(detailsMPesaDto.getCardNo());
					dto.setCreatedBy(detailsMPesaDto.getCreatedBy());
					dto.setCreatedDate(dateTime);
					dto.setOrganisationId(reqDto.getDepositMasterDto().getOrgId());
					dto.setUnitId(reqDto.getDepositMasterDto().getUnitId());
					Response loyalityCardResponse=iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(dto);
					if(loyalityCardResponse.getListObject().size() >0 && loyalityCardResponse.getListObject()!=null){
						PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)loyalityCardResponse.getListObject().get(0);
						//checking that front end amount should be less than or equal to card amount
						if(patientLoyaltyCardMapperDto.getCardAmount()>=detailsMPesaDto.getAmount())
							{
							patientLoyaltyCardMapperDto.setConsumeAmount(detailsMPesaDto.getAmount());
							iPatientLoyaltyCardMapperDao.updateLoyaltyCardAmountByCard(dto);
							mailList.add(patientLoyaltyCardMapperDto);
						}else{
							return new Response(ERROR, ERROR_CODE, "Amount should be less than or equals to card amount", null, 0.0);
						}
					}
					
				}
			}	
			
			
			if(deposit.getDepositTypeId()!=null &&deposit.getDepositTypeId().intValue()==3)
			{
				AdmissionNoteDto  admissionNoteDto = new AdmissionNoteDto();
				admissionNoteDto.setAdmissionNoteId(deposit.getAdmNoteId());
				admissionNoteDto.setDepositPending(deposit.getDepositPending()-deposit.getDepositAmount());
				admissionNoteDto.setDepositCollected(deposit.getDepositAmount());
				//Code check 
				
					admissionNoteDto.setDepositAmount(admissionNoteDto.getDepositPending()+admissionNoteDto.getDepositCollected());
				if(admissionNoteDto.getDepositPending()==0||deposit.getDepositPending()==deposit.getDepositAmount())
				{
					admissionNoteDto.setDepositStatus(DEPOSIT_COLLECTED);
				}
				else if(deposit.getDepositPending()>deposit.getDepositAmount())
				{
					admissionNoteDto.setDepositStatus(DEPOSIT_PARTIAL_COLLECTED);
				}
				
				Response admissionNoteRes =null;
				if(!deposit.isBedReservation())
				{
					 admissionNoteRes = this.updateReservationDeposit(admissionNoteDto);
				}
				else {
					admissionNoteDto.setDepositAmount(0);
					admissionNoteRes = this.updateReservationDeposit(admissionNoteDto);
				}
				
				
				List<Integer> list = new ArrayList<Integer>();
				list.add(admissionNoteDto.getDepositStatus());
				if(admissionNoteRes.getCode().equals(SUCCESS_CODE))
					return new Response<>(SUCCESS, SUCCESS_CODE, DEPOSIT_ADDED_MSG + deposit.getDepositNo(),list,depositMaster.getDepositMasterId(),deposit);
				else
					return admissionNoteRes;
			}
//			if((deposit.getDepositTypeId().intValue()!=1 && deposit.getDepositTypeId().intValue()!=5 && deposit.getDepositTypeId().intValue()!=6) )/*&& deposit.getVisitTypeId()==2*/
//			{
//				Response res = iDepositMasterDao.getDocIdFromVisitId(deposit.getAdmissionId(), deposit.getEncounterId());
//				if(res.getObject()!=null)
//				{
//					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
//						smsInfo.setUnitId(depositMaster.getUnitId());
//						smsInfo.setPatientId(depositMaster.getPatientId());
//						smsInfo.setTreatingDoctorId((Integer)res.getObject());
//						smsInfo.setBillAmount(new BigDecimal(deposit.getDepositAmount()));
//						smsInfo.setDepositNo(deposit.getDepositNo());
//						smsInfo.setDateTime(dateTime);
//					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ADVANCE,smsInfo);
//				}
//			}
			
			for (PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto : mailList) {
				iMailingService.consumeLoyaltyAmount(patientLoyaltyCardMapperDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, DEPOSIT_ADDED_MSG + deposit.getDepositNo(), null, depositMaster.getDepositMasterId(),deposit);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchDeposit(DepositSearchReqDto dto) throws BillingException {
		try{
			return iDepositMasterDao.searchDeposit(dto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getEncountersForDeposit(VisitTypeEncounterReqDto reqDto) throws BillingException {
		try{
			Response response = new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			switch(reqDto.getVisitTypeId()){
			case 1 :
				response = iDepositMasterDao.getEncountersByPatIdForDeposit(reqDto);
				break;
			case 2 :
				response = iDepositMasterDao.getAdmissionByPatVisitForDeposit(reqDto);
				break;
			case 3 :
				response = iDepositMasterDao.getAdmissionByPatVisitForDeposit(reqDto);
				break;
			case 4 :
				response = iDepositMasterDao.getAdmissionByPatVisitForDeposit(reqDto);
				break;	
			}
			return response;
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getAvailableDepositByPatient(int orgId, int unitId, int patientId,int encounterId,int visitTypeId,int otReservFlag, Boolean depositType) throws BillingException {
		try{
			return iDepositMasterDao.getAvailableDepositByPatient(orgId, unitId, patientId,encounterId,visitTypeId,otReservFlag,depositType);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateDepositMaster(PaymentDepositDto paymentDepositDto) {
		try{
			return iDepositMasterDao.updateDepositMaster(paymentDepositDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getAvailableDepositByPatient(int patientId,int unitId)
			throws BillingException {
		try{
			return iDepositMasterDao.getAvailableDepositByPatient(patientId,unitId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	@Transactional
	public Response getDepositListForRefund(int orgId, int unitId, int patientId, int depositTypeId)
			throws BillingException {
		try{
			return iDepositMasterDao.getDepositListForRefund(orgId, unitId, patientId,depositTypeId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDepositDetailsByDepositMaster(int orgId, int unitId, int depositMasterId)
			throws BillingException {
		try{
			return iDepositMasterDao.getDepositDetailsByDepositMaster(orgId, unitId, depositMasterId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getDepositDetailsByDepositId(int depositMasterId) throws BillingException {
		try{
			return iDepositMasterDao.getDepositDetailsByDepositId(depositMasterId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateDepositDetails(PaymentDepositDetailsDto PaymentDepositDetailsDto) throws BillingException {
		try{
			return iDepositMasterDao.updateDepositDetails(PaymentDepositDetailsDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateReservationDeposit(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		
		try{
			return iDepositMasterDao.updateReservationDeposit(admissionNoteDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response addDepositAgainstAdmission(DepositCollectionReqDto reqDto) throws BillingException {
		try{
			
			DepositMaster depositMaster = null;
			Date date = TimeZoneComponent.getDateByzone(reqDto.getDepositMasterDto().getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
			
			//get current deposit id
			Long depositId = iDepositMasterDao.getCurrentId();
			//save deposit master 
			DepositMasterDto deposit = reqDto.getDepositMasterDto();
			deposit.setAvailableDeposit(deposit.getDepositAmount());
			deposit.setConsumedDeposit(0);
			deposit.setCreatedDate(dateTime);
			deposit.setDepositDate(dateTime);
		//	deposit.setDepositNo(serialNoUtility.generateDepositNo(depositId));
			
			String depositNumber = iGlobalUnitMasterDao.getGeneratedNo(0, deposit.getUnitId(), depositScreenId);
			if(depositNumber==null)
			{
				return new Response<>(ERROR, ERROR_CODE, "Please Configure Deposit Number.", null, null);
			}
			
			deposit.setDepositNo(depositNumber);
			deposit.setStatus(ACTIVE);
			deposit.setUpdatedBy(deposit.getCreatedBy());
			if(deposit.getVisitTypeId()!=null)
			{
				if(deposit.getVisitTypeId().intValue()==OP)
				{
					deposit.setEncounterId(deposit.getEncounterId());
					deposit.setAdmissionId(null);
				}
				else if(deposit.getVisitTypeId().intValue()==IP||deposit.getVisitTypeId().intValue()==DC||deposit.getVisitTypeId().intValue()==ER)
				{
					deposit.setAdmissionId(deposit.getAdmissionId());
					deposit.setEncounterId(null);
				}
			}
			deposit.setUpdatedDate(dateTime);
			Response depositRes = iDepositMasterDao.saveDeposit(deposit);
			if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null){
				 depositMaster = (DepositMaster)depositRes.getObject();
				
				//save payment details
				ListIterator<PaymentDepositDetailsDto> paymentItr = reqDto.getListPaymentDepositDetailsDto().listIterator();
				PaymentDepositDetailsDto detailsDto = null;
				while(paymentItr.hasNext()){
					detailsDto = paymentItr.next();
					detailsDto.setCreatedDate(dateTime);
					detailsDto.setDepositId(depositMaster.getDepositMasterId());
					detailsDto.setUpdatedBy(deposit.getCreatedBy());
					detailsDto.setUpdatedDate(dateTime);
					detailsDto.setBalanceAmount(detailsDto.getAmount());
					detailsDto.setRefundAmount(0.00);
					iPaymentDepositDetailsDao.savePaymentDepositDetails(detailsDto);
				}
			}
		
			if(deposit.getAdmNoteId()!=null &&deposit.getAdmNoteId()>0)
			{
				this.updateDepositAdmission(deposit);
				/*AdmissionNoteDto  admissionNoteDto = new AdmissionNoteDto();
				admissionNoteDto.setAdmissionNoteId(deposit.getAdmNoteId());
				admissionNoteDto.setDepositPending(deposit.getDepositPending()-deposit.getDepositAmount());
				admissionNoteDto.setDepositCollected(deposit.getDepositAmount());
				//Code check 
				admissionNoteDto.setDepositAmount(admissionNoteDto.getDepositPending()+admissionNoteDto.getDepositCollected());
				if(deposit.getDepositPending()==0)
				{
					admissionNoteDto.setDepositStatus(DEPOSIT_COLLECTED);
				}
				else if(deposit.getDepositPending()>deposit.getDepositAmount())
				{
					admissionNoteDto.setDepositStatus(DEPOSIT_PARTIAL_COLLECTED);
				}
				Response admissionNoteRes = this.updateReservationDeposit(admissionNoteDto);
				if(admissionNoteRes.getCode().equals(SUCCESS_CODE))
					return new Response<>(SUCCESS, SUCCESS_CODE, DEPOSIT_ADDED_MSG + deposit.getDepositNo(), null,admissionNoteDto.getDepositStatus());
				else
					return admissionNoteRes;*/
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, DEPOSIT_ADDED_MSG + deposit.getDepositNo(), null, depositMaster.getDepositMasterId());
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateDepositAdmission(DepositMasterDto depositMasterDto) throws BillingException {
		try{
			return iDepositMasterDao.updateDepositAdmission(depositMasterDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response rechargeLoyaltyCard(DepositCollectionReqDto reqDto) throws BillingException {
    try{
			DepositMaster depositMaster =null;
			Date date = TimeZoneComponent.getDateByzone(reqDto.getDepositMasterDto().getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			ListIterator<PaymentDepositDetailsDto> paymentMPesaItr = reqDto.getListPaymentDepositDetailsDto().listIterator();
			PaymentDepositDetailsDto detailsMPesaDto = null;			
			MpesaMasterDto masterDto = new MpesaMasterDto();
			while(paymentMPesaItr.hasNext()) { 
				detailsMPesaDto = paymentMPesaItr.next();	
				if(detailsMPesaDto.getPaymentModeId() == 5 && (detailsMPesaDto.getSkipMpesaValidation()!=null && detailsMPesaDto.getSkipMpesaValidation())) 
				{
					masterDto.setMpesaTxcode(detailsMPesaDto.getCardNo());	
					masterDto.setUnitId(reqDto.getDepositMasterDto().getUnitId());
					masterDto.setOrganizationId(reqDto.getDepositMasterDto().getOrgId());
					masterDto.setUpdatedBy(detailsMPesaDto.getCreatedBy());
					masterDto.setMpesaAmount(detailsMPesaDto.getAmount());
					iMpesaMasterDao.validateMpesaTrancastion(masterDto);
					
				}
				 if(detailsMPesaDto.getPaymentModeId() == 3) 
	                {
	                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
	                	pOSMachineResponseMapperDto.setCreatedDate(dateTime);
	                	pOSMachineResponseMapperDto.setUpdatedDate(dateTime);
	                	pOSMachineResponseMapperDto.setUnitId(reqDto.getDepositMasterDto().getUnitId());
	                	pOSMachineResponseMapperDto.setOrganizationId(reqDto.getDepositMasterDto().getOrgId());
	                	pOSMachineResponseMapperDto.setUpdatedBy(detailsMPesaDto.getCreatedBy());
	                	pOSMachineResponseMapperDto.setIsConsumed(true);
	                	pOSMachineResponseMapperDto.setRequestedUniqueId(detailsMPesaDto.getRequestedUniqueId());
	                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
	                }
			}			
			
			//get current deposit id
			Long depositId = iDepositMasterDao.getCurrentId();
			//save deposit master 
			DepositMasterDto deposit = reqDto.getDepositMasterDto();
			deposit.setAvailableDeposit(deposit.getDepositAmount());
			deposit.setConsumedDeposit(0);
			deposit.setCreatedDate(dateTime);
			deposit.setDepositDate(dateTime);
			deposit.setDepositNo(serialNoUtility.generateDepositNo(depositId));
			deposit.setStatus(ACTIVE);
			deposit.setUpdatedBy(deposit.getCreatedBy());
			if(deposit.getVisitTypeId()!=null)
			{
				if(deposit.getVisitTypeId().intValue()==OP)
				{
					deposit.setEncounterId(deposit.getEncounterId());
					deposit.setAdmissionId(null);
				}
				else if(deposit.getVisitTypeId().intValue()==IP||deposit.getVisitTypeId().intValue()==DC||deposit.getVisitTypeId().intValue()==ER)
				{
					deposit.setAdmissionId(deposit.getEncounterId());
					deposit.setEncounterId(null);
				}
			}
			deposit.setUpdatedDate(dateTime);
			Response depositRes = iDepositMasterDao.saveDeposit(deposit);
			if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null){
			  depositMaster = (DepositMaster)depositRes.getObject();
				
				//save payment details
				ListIterator<PaymentDepositDetailsDto> paymentItr = reqDto.getListPaymentDepositDetailsDto().listIterator();
				PaymentDepositDetailsDto detailsDto = null;
				while(paymentItr.hasNext()){
					detailsDto = paymentItr.next();
					detailsDto.setCreatedDate(dateTime);
					detailsDto.setDepositId(depositMaster.getDepositMasterId());
					detailsDto.setUpdatedBy(deposit.getCreatedBy());
					detailsDto.setUpdatedDate(dateTime);
					detailsDto.setBalanceAmount(detailsDto.getAmount());
					detailsDto.setRefundAmount(0.00);
					iPaymentDepositDetailsDao.savePaymentDepositDetails(detailsDto);
				}
			}
			
			PatientLoyaltyCardMapperDto dto = new PatientLoyaltyCardMapperDto();
			dto.setPatientId(deposit.getPatientId());
			dto.setCardAmount(deposit.getDepositAmount());
			dto.setCreatedBy(deposit.getCreatedBy());
			dto.setCreatedDate(dateTime);
			dto.setPatientLoyaltyCardMpprId(deposit.getPatientLoyaltyCardMpprId());
			iPatientLoyaltyCardMapperDao.updateLoyaltyCardAmount(dto);
			return new Response<>(SUCCESS, SUCCESS_CODE, RECHARGE_ADDED_MSG , null, depositMaster.getDepositMasterId());
	}catch(BillingException be){
		throw be;
	}catch(Exception e){
		
		throw new BillingException(e.getMessage());
	}
	}

	@Override
	public Response transferToAvtiveDeposit(PaymentRecieptMasterDto paymentRecieptMasterDto) {
		try{
			List<PaymentReceiptDetailsDto> depositDetailsList = null;
			Double amount = paymentRecieptMasterDto.getDummyAmount();
			Double payeeDueAmount = paymentRecieptMasterDto.getPayeeDueAmount();
			Double balanceAmount = paymentRecieptMasterDto.getPayeeDueAmount();
			Double payeeExcessAmount = 0.00;
			Double transferToAvtiveDepositAmount =  Double.valueOf(OrderMasterServiceImpl.df2.format(amount-payeeDueAmount));
			
			List<PaymentReceiptDetailsDto> reverseList = ImmutableList.copyOf(paymentRecieptMasterDto.getPaymentReceiptDetailsDtosList()).reverse();
			
			Iterator<PaymentReceiptDetailsDto> iterator = reverseList.iterator();
			while(iterator.hasNext())
			{
				
				PaymentReceiptDetailsDto detailsDto = iterator.next();
				
				if(detailsDto.getAmount() > balanceAmount){
					payeeExcessAmount = detailsDto.getAmount()-balanceAmount;
					detailsDto.setAmount(balanceAmount);
					detailsDto.setAvailableFund(payeeExcessAmount);
					//depositDetailsList.add(detailsDto);
				}
				balanceAmount = balanceAmount-detailsDto.getAmount();
			}
			List<PaymentReceiptDetailsDto> straightList = ImmutableList.copyOf(reverseList).reverse();
			
			String depositNumber = iGlobalUnitMasterDao.getGeneratedNo(0, paymentRecieptMasterDto.getUnitId(), depositScreenId);
			if(depositNumber==null)
			{
				return new Response<>(ERROR, ERROR_CODE, "Please Configure Deposit Number.", null, null);
			}
			
			//Generating DepositMaster
			DepositMasterDto depositMaster = new DepositMasterDto();
				depositMaster.setAvailableDeposit(transferToAvtiveDepositAmount);
				depositMaster.setConsumedDeposit(0);
				depositMaster.setCreatedBy(paymentRecieptMasterDto.getCreatedBy());
				depositMaster.setCreatedDate(paymentRecieptMasterDto.getCreatedDate());
				depositMaster.setDepositAmount(transferToAvtiveDepositAmount);
				depositMaster.setDepositDate(paymentRecieptMasterDto.getCreatedDate());
				depositMaster.setDepositNo(depositNumber);
				depositMaster.setDepositTypeId(1); //Active
				depositMaster.setIdentificationNo(paymentRecieptMasterDto.getIdentificationNo());
				depositMaster.setOrgId(paymentRecieptMasterDto.getOrgnizationId());
				depositMaster.setPatientId(paymentRecieptMasterDto.getPatientId());
				depositMaster.setPayeeId(paymentRecieptMasterDto.getPayeeId());
				depositMaster.setReceivedFrom(paymentRecieptMasterDto.getReceivedFrom());
				depositMaster.setStatus(ACTIVE);
				depositMaster.setUnitId(paymentRecieptMasterDto.getUnitId());
				depositMaster.setUpdatedBy(paymentRecieptMasterDto.getUpdatedBy());
				depositMaster.setUpdatedDate(paymentRecieptMasterDto.getUpdatedDate());
				depositMaster.setRefundAmount(0.00);
				depositMaster.setCounterId(paymentRecieptMasterDto.getCounterId());
				depositMaster.setIsAddendum('N');
				depositMaster.setBillId(paymentRecieptMasterDto.getBillingMasterId());
				depositMaster.setAgainstPaymentRecieptId(paymentRecieptMasterDto.getPaymentRecieptId());
			Response depositRes = iDepositMasterDao.saveDeposit(depositMaster);
			
			if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null){
				DepositMaster  depositMast = (DepositMaster)depositRes.getObject();
					PaymentDepositDetailsDto paymentDepositDetailsDto = new PaymentDepositDetailsDto();
						paymentDepositDetailsDto.setDepositId(depositMast.getDepositMasterId());
						paymentDepositDetailsDto.setPaymentModeId(7);
						paymentDepositDetailsDto.setAmount(transferToAvtiveDepositAmount);
						paymentDepositDetailsDto.setConsumedAmount(0.00);
						paymentDepositDetailsDto.setBalanceAmount(transferToAvtiveDepositAmount);
						paymentDepositDetailsDto.setRefundAmount(0.00);
						paymentDepositDetailsDto.setCreatedDate(paymentRecieptMasterDto.getCreatedDate());
						paymentDepositDetailsDto.setCreatedBy(paymentRecieptMasterDto.getCreatedBy());
						paymentDepositDetailsDto.setUpdatedDate(paymentRecieptMasterDto.getUpdatedDate());
						paymentDepositDetailsDto.setUpdatedBy(paymentRecieptMasterDto.getUpdatedBy());
						paymentDepositDetailsDto.setPaymentRecieptDetailsId(paymentRecieptMasterDto.getPaymentRecieptDetailsId());
					iPaymentDepositDetailsDao.savePaymentDepositDetails(paymentDepositDetailsDto);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, RECHARGE_ADDED_MSG , straightList, depositMaster);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response saveTransferToActiveDepositFromPaymentCollection(DepositMasterDto forTransferToActiveDeposit) {
		try{
			
			Response depositRes = iDepositMasterDao.saveDeposit(forTransferToActiveDeposit);
			if(depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null){
				DepositMaster depositMaster = (DepositMaster)depositRes.getObject();
					
					//save payment details
					ListIterator<PaymentReceiptDetailsDto> paymentItr = forTransferToActiveDeposit.getPaymentDetailsList().listIterator();
					while(paymentItr.hasNext()){
						PaymentReceiptDetailsDto detailsDto = paymentItr.next();
						
						PaymentDepositDetailsDto paymentDepositDetailsDto = new PaymentDepositDetailsDto();
							paymentDepositDetailsDto.setDepositId(depositMaster.getDepositMasterId());
							paymentDepositDetailsDto.setPaymentModeId(detailsDto.getPaymentModeId());
							paymentDepositDetailsDto.setAmount(detailsDto.getAvailableFund());
							paymentDepositDetailsDto.setBankId(detailsDto.getBankId());
							paymentDepositDetailsDto.setAccountNumber(detailsDto.getAccountNumber());
							paymentDepositDetailsDto.setChequeNumber(detailsDto.getChequeNumber());
							paymentDepositDetailsDto.setCardNo(detailsDto.getCardNumber());
							paymentDepositDetailsDto.setCardHolderName(detailsDto.getCardHolderName());
							paymentDepositDetailsDto.setExpiryDate(detailsDto.getExpiryDate());
							paymentDepositDetailsDto.setCardTypeId(detailsDto.getCardTypeId());
							paymentDepositDetailsDto.setChequeDate(detailsDto.getChequeDate());
							paymentDepositDetailsDto.setConsumedAmount(0.00);
							paymentDepositDetailsDto.setBalanceAmount(detailsDto.getAvailableFund());
							paymentDepositDetailsDto.setRefundAmount(0.00);
							paymentDepositDetailsDto.setCreatedDate(detailsDto.getCreatedDate());
							paymentDepositDetailsDto.setCreatedBy(detailsDto.getCreatedBy());
							paymentDepositDetailsDto.setUpdatedDate(detailsDto.getUpdatedDate());
							paymentDepositDetailsDto.setUpdatedBy(detailsDto.getUpdatedBy());
							paymentDepositDetailsDto.setPaymentRecieptDetailsId(detailsDto.getPaymentRecieptDetailsId());
						iPaymentDepositDetailsDao.savePaymentDepositDetails(paymentDepositDetailsDto);
					}
				}
			return new Response<>(SUCCESS, SUCCESS_CODE, RECHARGE_ADDED_MSG , null, null);
		}catch(Exception e){
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDepositConsumtionDetails(Integer depositId) throws ApplicationException {
		try{
			return iDepositMasterDao.getDepositConsumtionDetails(depositId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getMySqlData() throws ApplicationException {
		try{
			return iDepositMasterDao.getMySqlData();
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateSignatureImage(int orgId, int unitId, int depositId, MultipartFile inputFile)
			throws BillingException {
		try{
			
		        	    String imageName = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId).getTime(),"dd_MM_yyyy_HH_mm_ss")+"_"+depositId+".jpeg";
		        	 
		        	    
		        	    String ServerDirPath =  "\\src\\main\\webapp\\resources\\assets\\images\\signature";
		        	    //System.out.println(servletContext.getRealPath(servletContext.getContextPath()+ServerDirPath));
		        	    String dirPath = servletContext.getRealPath(servletContext.getContextPath()+ServerDirPath);
		                File ServerDir = new File(ServerDirPath);
		                String S = "\\";
		                if (!ServerDir.exists()) {
		                    ServerDir.mkdirs();
		                }
		                FileOutputStream fosFor = new FileOutputStream(
		                        new File(ServerDirPath + "\\" +imageName));
		                fosFor.write(inputFile.getBytes());
		                fosFor.close();
			
			return iDepositMasterDao.updateSignatureImage(orgId, unitId, depositId, inputFile.getBytes(),ServerDir.getAbsolutePath()+S+imageName);
		}catch(BillingException be){
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDocIdFromVisitId(Integer admissionId, Integer encounterId) throws ApplicationException {
		try{
			return iDepositMasterDao.getDocIdFromVisitId( admissionId,encounterId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response ccAvenueCheckvalidation(String transactionNo) throws ApplicationException {
		try{
			return iDepositMasterDao.ccAvenueCheckvalidation(transactionNo);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getGRNDetailsByGRNMaster(int orgId, int unitId, int gRNId) throws ApplicationException {
		try{
			return iDepositMasterDao.getGRNDetailsByGRNMaster( orgId,unitId,gRNId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelDepositReceipt(DepositMasterDto dto) throws ApplicationException {
		try{
				Integer cancelStatus = iDepositMasterDao.cancelDepositReceipt(dto);
				if(cancelStatus == 1){
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, null, cancelStatus);
				}
				else{
					return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}
		}
		catch (Exception e){
			logger.error("cancelDepositReceipt failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response validateDepositAccess(Integer userId, Integer unitId, Integer organizationId) {
		return iDepositMasterDao.validateDepositAccess(userId,unitId,organizationId);
	}

}
