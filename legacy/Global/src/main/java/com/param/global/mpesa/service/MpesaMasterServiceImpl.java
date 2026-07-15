package com.param.global.mpesa.service;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.model.mpesa.MpesaTransactionDetails;
import com.param.global.billing.dto.PinPadDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Mpesa;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.mpesa.Dao.IMpesaTransctionDetailsDao;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.mpesa.dto.MpesaTransctionDetailsDto;
import com.param.global.mpesa.dto.StkCallback;
import com.param.global.mpesa.dto.TransactionResultDto;
import com.param.global.unit.dao.IMpesaAppConfigDao;
import com.param.global.unit.dto.MpesaApplicationConfigurationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class MpesaMasterServiceImpl implements IMpesaMasterServiceImpl,ICommonConstants {

	final static Logger logger = Logger.getLogger(MpesaMasterServiceImpl.class);

	@Autowired
	IMpesaMasterDao iMpesaMasterServiceDao;
	
	@Autowired
	IMpesaAppConfigDao iMpesaAppConfigDao;
	
	@Autowired
	IMpesaTransctionDetailsDao iMpesaTransctionDetailsDao;
	
	@Override
	@Transactional
	public Response validateAmountByNumber(MpesaMasterDto mpesaMasterDto) {
		try {
			MpesaApplicationConfigurationDto dto = new MpesaApplicationConfigurationDto();
			dto.setOrganizationId(mpesaMasterDto.getOrganizationId());
			dto.setUnitId(mpesaMasterDto.getUnitId());
			Response res = iMpesaAppConfigDao.getMpesaAppConfig(dto);
			if(res.getObject()!=null) {
				dto = (MpesaApplicationConfigurationDto) res.getObject();
				
				Response  res1 =  iMpesaMasterServiceDao.getTransactionData(mpesaMasterDto.getMpesaTxcode());
				if(res1.getObject()!=null) {
				
					MpesaMasterDto dtoData = (MpesaMasterDto)res1.getObject();
					if(dtoData.getStatus().equals("D")) {
						return new Response(ERROR, ERROR_CODE, "Already Consumed", null, null);
					}else {
						dtoData.setMpesaTxcode(mpesaMasterDto.getMpesaTxcode());
						dtoData.setCreatedBy(mpesaMasterDto.getCreatedBy());
						dtoData.setCreatedDate(mpesaMasterDto.getCreatedDate());
						dtoData.setUpdatedBy(mpesaMasterDto.getUpdatedBy());
						dtoData.setUpdatedDate(mpesaMasterDto.getUpdatedDate());
						dtoData.setUnitId(mpesaMasterDto.getUnitId());
						dtoData.setOrganizationId(mpesaMasterDto.getOrganizationId());
						dtoData.setUhid(mpesaMasterDto.getUhid());
						dtoData.setMpesaSendermobile(mpesaMasterDto.getMpesaSendermobile());
						dtoData.setChecked(false);
						return iMpesaMasterServiceDao.validateAmountByNumber(dtoData);
					}
					
				}else {
					
					Mpesa.transactionStatus(mpesaMasterDto.getMpesaTxcode(),dto.getInitiator(),dto.getStatusResultURL(),dto.getValidationPassword(),dto.getBusinessShortcode(),dto.getAppKey(),dto.getAppSecretKey());
					
						
						TimeUnit.SECONDS.sleep(10);
						
						Response  getSaveData =  iMpesaMasterServiceDao.getTransactionData(mpesaMasterDto.getMpesaTxcode());
						
						if(getSaveData.getObject()!=null) {
							
							MpesaMasterDto dtoData = (MpesaMasterDto)getSaveData.getObject();
							dtoData.setMpesaTxcode(mpesaMasterDto.getMpesaTxcode());
							dtoData.setCreatedBy(mpesaMasterDto.getCreatedBy());
							dtoData.setCreatedDate(mpesaMasterDto.getCreatedDate());
							dtoData.setUpdatedBy(mpesaMasterDto.getUpdatedBy());
							dtoData.setUpdatedDate(mpesaMasterDto.getUpdatedDate());
							dtoData.setUnitId(mpesaMasterDto.getUnitId());
							dtoData.setOrganizationId(mpesaMasterDto.getOrganizationId());
							dtoData.setUhid(mpesaMasterDto.getUhid());
							dtoData.setMpesaSendermobile(mpesaMasterDto.getMpesaSendermobile());
							dtoData.setChecked(false);
							return iMpesaMasterServiceDao.validateAmountByNumber(dtoData);
						
						}else {
							
							return new Response<>(ERROR, ERROR_CODE,"Please check transaction detials again", null, null);
						}
						
				}
				
			}else {
				
				return new Response<>(ERROR, ERROR_CODE, "Please Configure Mpesa Credential At Application Level ", null, null);
			}
			
			
		}catch(Exception e) {
			logger.error("validateAmountByNumber failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public Response savePinPanDetaild(PinPadDetailsDto pinPadDetailsDto) throws ApplicationException {
		try {
			
			return iMpesaMasterServiceDao.savePinPanDetaild(pinPadDetailsDto);

		}catch(Exception e) {
			logger.error("savePinPanDetaild failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updatePinpadDetails(PinPadDetailsDto pinPadDetailsDto) throws ApplicationException {
		try {
					
					return iMpesaMasterServiceDao.updatePinpadDetails(pinPadDetailsDto);

			}catch(Exception e) {
					logger.error("updatePinpadDetails failed", e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	@Transactional
	public Response requestSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)throws ApplicationException {
		
		try {
			double a = mpesaTransctionDetailsDto.getAmount();
			Integer amt = (int)a;
			String amount =amt.toString();
			String mobNumber = mpesaTransctionDetailsDto.getPhoneNumber();
			MpesaApplicationConfigurationDto dto = new MpesaApplicationConfigurationDto();
			dto.setOrganizationId(mpesaTransctionDetailsDto.getOrganizationId());
			dto.setUnitId(mpesaTransctionDetailsDto.getUnitId());
			Response res = iMpesaAppConfigDao.getMpesaAppConfig(dto);
			if(res.getObject()!=null) {
				dto = (MpesaApplicationConfigurationDto) res.getObject();
				
				okhttp3.Response response = Mpesa.STKPushSimulation(dto.getBusinessShortcode(),dto.getStkPushPassword(),dto.getStkTimestamp(),dto.getTransactionType(),amount,mobNumber,mobNumber,dto.getPartyB(),dto.getCallbackUrl(),dto.getAccountRefernece(),dto.getTransactionDescription(),dto.getAppKey(),dto.getAppSecretKey());
			
				JSONObject jsonObject1=new JSONObject(response.body().string());
				 
				mpesaTransctionDetailsDto.setMerchantRequestId(jsonObject1.getString("MerchantRequestID"));
		        mpesaTransctionDetailsDto.setCheckoutRequestId(jsonObject1.getString("CheckoutRequestID"));
		        mpesaTransctionDetailsDto.setCustomerMessage(jsonObject1.getString("CustomerMessage"));
		        mpesaTransctionDetailsDto.setResponseCode(jsonObject1.getString("ResponseCode"));
		        mpesaTransctionDetailsDto.setResponseDescription(jsonObject1.getString("ResponseDescription"));
		        
		        return  iMpesaTransctionDetailsDao.saveSTKPushPayment(mpesaTransctionDetailsDto);
				
			}else {
				
				return new Response<>(ERROR, ERROR_CODE, "Please Configure Mpesa Credential At Application Level ", null, null);
			}

	}catch(Exception e) {
			logger.error("requestSTKPushPayment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response validateSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)
			throws ApplicationException {
		try {
			
			Response res= iMpesaTransctionDetailsDao.validateSTKPushPayment(mpesaTransctionDetailsDto);
				if(res.getObject() != null ) {
				
					MpesaTransctionDetailsDto dto = (MpesaTransctionDetailsDto) res.getObject();
				
					if(dto.getUsed() == false) {
						Response res1=null;
							if(dto.getResponseCode().equals("0")) {
								
								MpesaApplicationConfigurationDto dto2 = new MpesaApplicationConfigurationDto();
								dto2.setOrganizationId(mpesaTransctionDetailsDto.getOrganizationId());
								dto2.setUnitId(mpesaTransctionDetailsDto.getUnitId());
								Response res2= iMpesaAppConfigDao.getMpesaAppConfig(dto2);
								if(res2.getObject()!=null) {
									dto2 = (MpesaApplicationConfigurationDto) res2.getObject();
								
									String c = dto.getCheckoutRequestId();
									okhttp3.Response response = Mpesa.validateSTKPushSimulation(dto2.getBusinessShortcode(),dto2.getStkPushPassword(),dto2.getStkTimestamp(),c,dto2.getAppKey(),dto2.getAppSecretKey());
									JSONObject jsonObject1=new JSONObject(response.body().string());
									mpesaTransctionDetailsDto.setMpesaTransactionId(dto.getMpesaTransactionId());
									mpesaTransctionDetailsDto.setMerchantRequestId(jsonObject1.getString("MerchantRequestID"));
									mpesaTransctionDetailsDto.setCheckoutRequestId(jsonObject1.getString("CheckoutRequestID"));
									mpesaTransctionDetailsDto.setResponseCode(jsonObject1.getString("ResponseCode"));
									mpesaTransctionDetailsDto.setResponseDescription(jsonObject1.getString("ResponseDescription"));
									mpesaTransctionDetailsDto.setResultCode(jsonObject1.getString("ResultCode"));
									mpesaTransctionDetailsDto.setResultDesc(jsonObject1.getString("ResultDesc"));
									mpesaTransctionDetailsDto.setAmount(dto.getAmount());
									if(mpesaTransctionDetailsDto.getResultCode().equals("0") || mpesaTransctionDetailsDto.getResultCode().equals("1032") ) {
										
										 res1= iMpesaTransctionDetailsDao.updateSTKPushSimulation(mpesaTransctionDetailsDto);
									}else {
						
										return new Response<>(ERROR, ERROR_CODE,"Request Pendding", null, null);
									}
									
									if(res1.getObject()!=null ) {
							
											MpesaTransactionDetails dto1 = (MpesaTransactionDetails) res1.getObject();
											if(dto1.getResultCode().equals("0")) {
								
												return new Response(SUCCESS, SUCCESS_CODE, dto1.getResultDesc(), null, dto1); 
											}else {
								
												return new Response<>(ERROR, ERROR_CODE,dto1.getResultDesc(), null, null);
											}
									}
								}else {
									
									return new Response<>(ERROR, ERROR_CODE, "Please Configure Mpesa Credential At Application Level ", null, null);
								}
							}
							return new Response<>(ERROR, ERROR_CODE, dto.getResponseDescription(), null, null);
				
					}else {
					
						return new Response<>(ERROR, ERROR_CODE," Already Validated Or Used", null, null);
					}
				}
				return new Response<>(ERROR, ERROR_CODE, "Please Make Request First Then Validate ", null, null);

	}catch(Exception e) {
			logger.error("validateSTKPushPayment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveTransactionDate(TransactionResultDto dto) throws ApplicationException {
		try {

			return iMpesaMasterServiceDao.saveTransactionDate(dto);

	}catch(Exception e) {
			logger.error("saveTransactionDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateTransactionDate(StkCallback stkDto) throws ApplicationException {
		try {

			return iMpesaMasterServiceDao.updateTransactionDate(stkDto);

	}catch(Exception e) {
			logger.error("updateTransactionDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveValidationData(StkCallback stkDto) throws ApplicationException {
		try {

			return iMpesaMasterServiceDao.saveValidationData(stkDto);

	}catch(Exception e) {
			logger.error("saveValidationData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
