package com.param.global.org.common.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.org.model.LoyaltiCardMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.SmsUtility;
import com.param.global.dao.ILoyaltyCardLogDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dto.LoyaltiCardMasterDto;
import com.param.global.dto.LoyaltyCardLogDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.mail.IMailingService;
import com.param.global.org.common.dao.IPatientLoyaltyCardMapperDao;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.service.ILoyaltiCardMasterService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class PatientLoyaltyCardMapperServiceImpl implements IPatientLoyaltyCardMapperService,ICommonConstants {
	
	@Autowired
	IPatientLoyaltyCardMapperDao iPatientLoyaltyCardMapperDao;
	
	@Autowired
	ILoyaltiCardMasterService iLoyaltiCardMasterService;
	
	@Autowired
	private SmsUtility smsUtility;
	
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@Autowired
	private IMailingService iMailingService;
	
	@Autowired
	private IUserMasterDao iUserMasterDao;
	
	@Autowired
	private ILoyaltyCardLogDao iLoyaltyCardLogDao;
	
	@Override
	@Transactional
	public Response savePatientLoyaltiCardMapper(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			dto.setCreatedDate(dateTime);
			LoyaltiCardMasterDto loyaltyCardDto = new LoyaltiCardMasterDto();
			loyaltyCardDto.setIsUserDefined('Y');
			loyaltyCardDto.setOrganisationId(dto.getOrganisationId());
			loyaltyCardDto.setUnitId(dto.getUnitId());
			loyaltyCardDto.setCardNo(dto.getCardNo());
			loyaltyCardDto.setStatus('A');
			loyaltyCardDto.setCreatedBy(dto.getCreatedBy());
			loyaltyCardDto.setUpdatedBy(dto.getUpdatedBy());
			loyaltyCardDto.setCreatedDate(dateTime);
			loyaltyCardDto.setUpdatedDate(dateTime);
			Response loyaltyCardRes = iLoyaltiCardMasterService.saveLoyaltiCardMaster(loyaltyCardDto);
			if (loyaltyCardRes.getCode().equals(SUCCESS_CODE)) {
				LoyaltiCardMaster master = (LoyaltiCardMaster) loyaltyCardRes.getObject();
				dto.setLoyaltyCardId(master.getLoyaltyCardId());
			}
			Response res =  iPatientLoyaltyCardMapperDao.savePatientLoyaltiCardMapper(dto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);

		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getAvailableActiveDepositWithCardNumber(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException {
		try{
			
			if (dto.getPatientLoyaltyCardMpprId()>0)
			{
				return iPatientLoyaltyCardMapperDao.getAvailableActiveDepositWithCardNumber(dto);
			}
			else {
				return iPatientLoyaltyCardMapperDao.getLoyaltyCardConfiguration(dto);
			}
				
		 
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getLoyaltiCardDetailsByPatient(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			return iPatientLoyaltyCardMapperDao.getLoyaltiCardDetailsByPatient(dto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPatientDetailsByLoyaltiCardNo(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			return iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(dto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	
	@Override
	@Transactional
	public Response generateOTP(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			
			Date currentDate=TimeZoneComponent.getDateByzone(dto.getUnitId());
			SlotMasterDto SlotMasterDto=new SlotMasterDto();
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(dto.getOrganisationId());
			unitApplicationConfigurationDto.setUnitId(dto.getUnitId());
		Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
		
		if(unitApplicationConfigurationDtos != null){
			SlotMasterDto.setSender(unitApplicationConfigurationDtos.getSenderId());
		}
		else{
			return new Response(ERROR, null, "Unit application configuraion not yet defined.!!", null, null);
		}
		PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto = new PatientLoyaltyCardMapperDto();
		 Response res1=iPatientLoyaltyCardMapperDao.getLoyaltiDetailsByMapprId(dto);
				if(res1.getListObject().size()>0 && res1.getListObject()!=null){
					 patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)res1.getListObject().get(0);
					
					if(patientLoyaltyCardMapperDto.getCardBlockDate() !=null ? patientLoyaltyCardMapperDto.getCardBlockDate().before(currentDate) :true){
						
					}else{
						return new Response(ERROR, ERROR_CODE, "Your Card has been blocked for 24 hour", null, null);
					}
			}else {
				
				return new Response(ERROR, ERROR_CODE, "invalid card number", null, null);
			}
			    // this will convert any number sequence into 6 character.
			    String otp=null;
			    while(true)
			    {
			    	 Random rnd = new Random();
					 Integer number = rnd.nextInt(999999);
					     if(number.toString().trim().length()==6) {
					    	 otp=number.toString();
					    	 break;
					     }
					    	
			    };
			    
			    String authKey=null;
			    while(true)
			    {
			    	 Random rnd = new Random();
					 Integer number = rnd.nextInt(999999);
					     if(number.toString().trim().length()==6) {
					    	 authKey=number.toString();
					    	 break;
					     }
					    	
			    };
			    
			    SlotMasterDto.setOpt(otp);
			    SlotMasterDto.setMobileNumber(dto.getMobileNumber());
			    SlotMasterDto.setCardNo(dto.getCardNo());
			    Calendar now = Calendar.getInstance();
			    now.add(Calendar.MINUTE, 3);
			    Date oneMinutesFromNow = now.getTime();
			    dto.setOtpExpiredTime(oneMinutesFromNow);
			    dto.setOtp(Integer.parseInt(otp));
				 Response res3=  iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(dto);
				 if(res3.getListObject().size()>0) {
					 
					 List<PatientLoyaltyCardMapperDto> list =res3.getListObject();
					 dto.setEmail(list.get(0).getEmail());
				 }
				
				  Response rese= iPatientLoyaltyCardMapperDao.getUnitName(dto);
				  PatientLoyaltyCardMapperDto unitNameDto = new PatientLoyaltyCardMapperDto();
				  if(rese.getObject()!= null) {
					  unitNameDto = (PatientLoyaltyCardMapperDto) rese.getObject();
					  dto.setUnitName(unitNameDto.getUnitName());
				  }
				  
			    Response res=iPatientLoyaltyCardMapperDao.updateOTP(dto);
			    LoyaltyCardLogDto loyaltyCardLogDto = new LoyaltyCardLogDto();
			    loyaltyCardLogDto.setAmount(dto.getConsumeAmount());
			    loyaltyCardLogDto.setAuthKey(authKey);
			    loyaltyCardLogDto.setCreatedBy(dto.getCreatedBy());
			    loyaltyCardLogDto.setCreatedDate(dto.getCreatedDate());
			    loyaltyCardLogDto.setLoyaltyCardNo(dto.getCardNo());
			    loyaltyCardLogDto.setOrganizationId(dto.getOrganisationId());
			    loyaltyCardLogDto.setOtp(otp);
			    loyaltyCardLogDto.setOtpCount(0);
			    loyaltyCardLogDto.setLoyaltyCardStatus(OTP_GENERATED);
			    loyaltyCardLogDto.setUnitId(dto.getUnitId());
			    loyaltyCardLogDto.setUpdatedBy(dto.getUpdatedBy());
			    loyaltyCardLogDto.setUpdatedDate(dto.getUpdatedDate());
				Response keyRes= iLoyaltyCardLogDao.saveLoyaltyCardLog(loyaltyCardLogDto);
			    if(res.getStatus().equalsIgnoreCase(SUCCESS)){
			    	  smsUtility.sendOTP(SlotMasterDto);
			    	  dto.setEmail(patientLoyaltyCardMapperDto.getEmail()!=null ? patientLoyaltyCardMapperDto.getEmail() : null);
			    	  if(dto.getEmail()!=null){
			    		  iMailingService.sendOTP(dto);
			    	  }
			    	  return new Response(SUCCESS, SUCCESS_CODE, "OTP sent successfully to the registered mobile no. "+dto.getMobileNumber(), null,loyaltyCardLogDto.getAuthKey());
			    }else{
			    	return new Response(ERROR, ERROR_CODE, "OTP sent fail", null, null);
			    }
			   
			   
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getLoyaltyCardPatientList(int orgId, int unitId) throws ApplicationException {
		try{
			return iPatientLoyaltyCardMapperDao.getLoyaltyCardPatientList(orgId,unitId);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updatePatientByLocaltiCard(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			return iPatientLoyaltyCardMapperDao.updatePatientByLocaltiCard(dto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updatePatientPatientCagtegory(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			return iPatientLoyaltyCardMapperDao.updatePatientPatientCagtegory(dto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	@Override
	@Transactional
	public Response validateOTP(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			Date currentDate=TimeZoneComponent.getDateByzone(dto.getUnitId());
			 Calendar now = Calendar.getInstance();
			    now.add(Calendar.HOUR, 24);
			    Date hours24FromNow = now.getTime();
			 
			// Response res=iPatientLoyaltyCardMapperDao.getLoyaltiDetailsByMapprId(dto);
			
			 
/*			if(res.getListObject().size()>0 && res.getListObject()!=null){
				PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)res.getListObject().get(0);
				
				//first check card has block or not 
				if(patientLoyaltyCardMapperDto.getCardBlockDate() !=null ? patientLoyaltyCardMapperDto.getCardBlockDate().before(currentDate) :true){
					
				
							//first check enter card no and front end card no is valid or not
						if(patientLoyaltyCardMapperDto.getCardNo().toLowerCase().trim().equals(dto.getCardNo().toLowerCase().trim())){
							
						 // checking enter otp is coming with in time or not (i.e. with in 3 minutes )
							if(currentDate.before(patientLoyaltyCardMapperDto.getOtpExpiredTime()) || currentDate==patientLoyaltyCardMapperDto.getOtpExpiredTime()){
								//checking entered otp and db otp is matched or not
								if(patientLoyaltyCardMapperDto.getOtp().equals(dto.getOtp())){
									if(patientLoyaltyCardMapperDto.getIsVerified()=='N' && patientLoyaltyCardMapperDto.getIsVerified()!='\u0000')
									{
										dto.setIsVerified('Y');
										iPatientLoyaltyCardMapperDao.updateOTPCount(dto);
										return new Response(SUCCESS, SUCCESS_CODE, "OTP validated successfully", null, null);
									}
									else
										return new Response(ERROR, ERROR_CODE, "OTP already validated", null, null);
								}else{
									// update otp count 
									dto.setCardBlockDate((patientLoyaltyCardMapperDto.getOtpCount()+1)>=3 ? hours24FromNow : currentDate);
									Response res1=iPatientLoyaltyCardMapperDao.updateOTPCount(dto);
									
									if((patientLoyaltyCardMapperDto.getOtpCount()+1)<=3)
										return new Response(ERROR, ERROR_CODE, "Invalid OTP ", null, patientLoyaltyCardMapperDto.getOtpCount()+1);
									else
										return new Response(ERROR, ERROR_CODE, "Your Card has been blocked for 24 hour", null, null);
								}
							}else{
								
								return new Response(ERROR, ERROR_CODE, "OTP Expired", null, null);
								  
							}
						}else{
							return new Response(ERROR, ERROR_CODE, "Invalid Card Number", null, null);
						}							
					}else{
						return new Response(ERROR, ERROR_CODE, "Your Card has been blocked for 24 hour", null, null);
					}
			}*/
			    PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto = new PatientLoyaltyCardMapperDto();
			    Response res=iPatientLoyaltyCardMapperDao.getLoyaltiDetailsByMapprId(dto);
				if(res.getListObject().size()>0 && res.getListObject()!=null){
					
					 patientLoyaltyCardMapperDto=(PatientLoyaltyCardMapperDto)res.getListObject().get(0);
					 
					}
				if(patientLoyaltyCardMapperDto.getCardBlockDate() !=null ? patientLoyaltyCardMapperDto.getCardBlockDate().before(currentDate) :true){
					
				    LoyaltyCardLogDto loyaltyCardLogDto = new LoyaltyCardLogDto();
				    Response logData=iLoyaltyCardLogDao.getLogDataByCardNo(dto);
					if(logData.getListObject().size()>0 && logData.getListObject()!=null){
						 loyaltyCardLogDto=(LoyaltyCardLogDto)logData.getListObject().get(0);
						
						if(loyaltyCardLogDto.getLoyaltyCardNo().toLowerCase().trim().equals(dto.getCardNo().toLowerCase().trim())){
							
								if(currentDate.before(loyaltyCardLogDto.getOtpExpiredTimeInDate()) || currentDate==loyaltyCardLogDto.getOtpExpiredTimeInDate()){
									
									if(loyaltyCardLogDto.getOtp().equals(dto.getOtp().toString())){
										
											if(loyaltyCardLogDto.getIsVerified()=='N' && loyaltyCardLogDto.getIsVerified()!='\u0000')
											{
												
												if(loyaltyCardLogDto.getAmount().equals(dto.getConsumeAmount()))
												{
													dto.setCardBlockDate(currentDate);
													Response res1=iPatientLoyaltyCardMapperDao.updateOTPCount(dto,1);
													
													loyaltyCardLogDto.setLoyaltyCardStatus(OTP_VALIDATED);
													iLoyaltyCardLogDao.updateLogs(loyaltyCardLogDto);
													return new Response(SUCCESS, SUCCESS_CODE, "OTP validated successfully", null, null);
												}else {
													
													return new Response(ERROR, ERROR_CODE, "Incorrect transaction amount for entered OTP", null, null);
												}
											}else {
												
												return new Response(ERROR, ERROR_CODE, "OTP already validated", null, null);
											}
										}else {
											
											dto.setCardBlockDate((patientLoyaltyCardMapperDto.getOtpCount()+1)>=3 ? hours24FromNow : currentDate);
											Response res1=iPatientLoyaltyCardMapperDao.updateOTPCount(dto,0);
											
											return new Response(ERROR, ERROR_CODE, "Invalid OTP ", null, null);
										}
									}else {
										loyaltyCardLogDto.setLoyaltyCardStatus(OTP_TIME_OUT);
										iLoyaltyCardLogDao.updateLogs(loyaltyCardLogDto);
										return new Response(ERROR, ERROR_CODE, "OTP Expired", null, null);
									}
								}else {
									return new Response(ERROR, ERROR_CODE, "Invalid Card Number", null, null);
								}
					}else {
						loyaltyCardLogDto.setLoyaltyCardStatus(OTP_INVALID);
						iLoyaltyCardLogDao.updateLogs(loyaltyCardLogDto);
						return new Response(ERROR, ERROR_CODE, "Invalid OTP ", null, null);
					}
				
				}else{
					return new Response(ERROR, ERROR_CODE, "Your Card has been blocked for 24 hour", null, null);
				}
				
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

}
