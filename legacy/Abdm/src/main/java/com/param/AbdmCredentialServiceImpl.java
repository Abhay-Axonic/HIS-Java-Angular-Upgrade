package com.param;


import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Serializable;
import java.net.URI;
import java.security.Key;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.Deserializers;
import org.hibernate.SessionFactory;
import org.scalastuff.scalabeans.DeserializablePropertyDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import com.param.entity.model.master.AbdmCallBackResponse;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.ServiceException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Msg91MessageSendNew;
import com.param.global.common.Response;
import com.param.global.dao.IMessageQueueDao;
import com.param.global.dao.ITemplateTransactionDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AmazonMailAttachement;
import com.param.global.dto.AmazonMailDto;
import com.param.global.dto.BulkMessageQueueDto;
import com.param.global.dto.MessageQueueDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.mail.IMailingService;
import com.param.global.model.MessageLog;
import com.param.global.unit.dto.BulkEmailDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.whatsAppRequestDto;
import com.param.global.unit.service.IwhatsAppServiceImpl;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.sendgrid.Attachments;

import in.param.exception.ApplicationException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import scala.tools.cmd.Demo;
@SuppressWarnings({"unchecked","rawtypes","unused"})
@Service
public class AbdmCredentialServiceImpl implements IAbdmCredentialControllerService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(AbdmCredentialServiceImpl.class);

	@Autowired
	IAbdmCredentialDao iAbdmCredentialDao;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getCredentialDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.getCredentialDetails(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response registerOrgnaisationInABDM(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.registerOrgnaisationInABDM(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response addUpdateServicesInAbdm(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.addUpdateServicesInAbdm(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response generateOtpAadhar(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.generateOtpAadhar(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	

	@Override
	public Response verifyOTPOtpAadhar(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.verifyOTPOtpAadhar(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response resendAadhaarOtp(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.resendAadhaarOtp(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response addGetServiceAbdm(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.addGetServiceAbdm(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkAndGenerateMobileOTP(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.checkAndGenerateMobileOTP(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkHealthCardAlreadyExists(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.checkHealthCardAlreadyExists(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getpngCard(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.getpngCard(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response fetchModesValidation(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.fetchModesValidation(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response onFetchModes(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.onFetchModes(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response authInit(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.authInit(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response authConfirm(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.authConfirm(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProfileDetailsByAdharNumber(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.getProfileDetailsByAdharNumber(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response QRcodeShare(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.getProfileDetailsByAdharNumber(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveAbdmCallBackDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.saveAbdmCallBackDetails(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateOnInitCall(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.updateOnInitCall(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateOnConfirm(AbdmCredentialdDto abdmCredentialdDto)  throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.updateOnConfirm(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePatientRegistrationFromABDMinHIS(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.savePatientRegistrationFromABDMinHIS(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePatientRegistrationAfterConfirmCall(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.savePatientRegistrationAfterConfirmCall(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveShareResponse(AbdmCredentialdDto ab) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.saveShareResponse(ab);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}	
	
	@Override
	public Response searchByHealthId(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
			try
			{
				return iAbdmCredentialDao.searchByHealthId(abdmCredentialdDto);

			} catch (Exception e)
			{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		}

	@Override
	public Response authInitMethod(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.authInitMethod(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getShareDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.getShareDetails(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public Response confirmWithMobileOtp(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.confirmWithMobileOtp(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTranscationTableDetailsById(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.getTranscationTableDetailsById(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response confirmWithAadhaarOtp(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.confirmWithAadhaarOtp(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDiscoverResponse(AbdmCredentialdDto ab) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.saveDiscoverResponse(ab);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveinitDiscoverResponse(AbdmCredentialdDto ab) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.saveInitDiscoverResponse(ab);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response verifyMobileOTP(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.verifyMobileOTP(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response createHealthIdWithPreVerified(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.createHealthIdWithPreVerified(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response profileDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.profileDetails(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response suggestion(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.suggestion(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response isExist(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.isExist(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response createPhrAddress(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try
		{
			return iAbdmCredentialDao.createPhrAddress(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPngCardNew(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try {
			return iAbdmCredentialDao.getPngCardNew(abdmCredentialdDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveConfirmDiscoverResponse(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try {
			return iAbdmCredentialDao.saveConfirmDiscoverResponse(abdmCredentialdDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response addContexts(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try {
			return iAbdmCredentialDao.addContexts(abdmCredentialdDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveOnAddContextsResponse(AbdmCredentialdDto ab) throws ApplicationException {
		try {
			return iAbdmCredentialDao.saveOnAddContextsResponse(ab);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response consentsHipNotify(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try {
			return iAbdmCredentialDao.consentsHipNotify(abdmCredentialdDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveLinksContextOnNotify(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try {
			return iAbdmCredentialDao.saveLinksContextOnNotify(abdmCredentialdDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response patientsSmsNotify2(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try {
			return iAbdmCredentialDao.patientsSmsNotify2(abdmCredentialdDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePatientSmsOnNotify(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
		try {
			return iAbdmCredentialDao.savePatientSmsOnNotify(abdmCredentialdDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveConsentsHipNotify(AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialDao.saveConsentsHipNotify(abdmCredentialdDto);
		}catch (Exception e) {
			throw e;
		}
		//return null;
	}

	@Override
	public Response saveHealthInformationHipRequest(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.saveHealthInformationHipRequest(abdmCredentialdDto);
		}catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response searchByMobile(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.searchByMobile(abdmCredentialdDto);
		}catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response saveAbdmShareResponse(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.saveAbdmShareResponse(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response mobileResendOtp(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try{
			return iAbdmCredentialDao.mobileResendOtp(abdmCredentialdDto);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response generateOtpForAadhaarNumber(AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialDao.generateOtpForLogin(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response verifyOtpForAadhaar(AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialDao.verifyOtpForLogin(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response verifyUserForAbhaLogin(AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialDao.verifyUserForAbhaLogin(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getPofileDetails(AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialDao.getPofileDetails(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getAbhaCard(AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialDao.getAbhaCard(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response requestOtpForAadhaar(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.requestOtpForAadhaar(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response enrollmentEnrolByAadhaar(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.enrollmentEnrolByAadhaar(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response enrollmentAuthByAbdm(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.enrollmentAuthByAbdm(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response enrollmentEnrolSuggestion(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.enrollmentEnrolSuggestion(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response enrollmentEnrolABhaAddress(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.enrollmentEnrolAbhaAddress(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCard(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.getCard(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response hidBenifitCreateHealthIdDemoAuth(AbdmCredentialdDto abdmCredentialdDto) {
	try {
		return iAbdmCredentialDao.hidBenifitCreateHealthIdDemoAuth(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response enrollmentEnrolbyDocument(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.enrollmentEnrolbyDocument(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response authInitVersionNew(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.authInitVersionNew(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response authConfirmWithAadhaarOtp(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.authConfirmWithAadhaarOtp(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAccountProfile(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.getAccountProfile(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v2AuthConfirmWithMobileOTP(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v2AuthConfirmWithMobileOTP(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3PhrWebLoginAbhaSearch(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3PhrWebLoginAbhaSearch(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3PhrWebLoginAbhaRequestOtp(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3PhrWebLoginAbhaRequestOtp(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3PhrWebLoginAbhaVerify(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3PhrWebLoginAbhaVerify(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3PhrWebloginProfileAbhaProfile(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3PhrWebloginProfileAbhaProfile(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3PhrWebloginProfileAbhaPhrCard(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3PhrWebloginProfileAbhaPhrCard(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3TokenGenerateToken(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3TokenGenerateToken(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3LinkCarecontext(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3LinkCarecontext(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3UserInitiatedLinkingPatientCareContextOnDiscover(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3UserInitiatedLinkingPatientCareContextOnDiscover(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3UserInitiatedLinkingLinkCareContextOnInit(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3UserInitiatedLinkingLinkCareContextOnInit(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3UserInitiatedLinkingLinkCareContextOnConfirm(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3UserInitiatedLinkingLinkCareContextOnConfirm(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3DataFlowHealthInformationHipOnRequest(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3DataFlowHealthInformationHipOnRequest(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response patientHiuDataNotification(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.patientHiuDataNotification(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response v3DataFlowHealthInformationNotify(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
		return iAbdmCredentialDao.v3DataFlowHealthInformationNotify(abdmCredentialdDto);
	} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}
	
	@Override
	public Response v3BridgeUrl(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.v3BridgeUrl(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3LinkPatientLinksSmsNotify2(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.v3LinkPatientLinksSmsNotify2(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ConsentRequestHipOnNotify(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.v3ConsentRequestHipOnNotify(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveOnGenerateToken(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.saveOnGenerateToken(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveOnCarecontext(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.saveOnCarecontext(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateShareDetailsByTokenNumber(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try {
			return iAbdmCredentialDao.updateShareDetailsByTokenNumber(abdmCredentialdDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientReqDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getPatientReqDetails(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEncounterNumberByPatientId(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getEncounterNumberByPatientId(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAdmissionNumberByPatientId(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getAdmissionNumberByPatientId(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3LinkCarecontextNew(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3LinkCarecontextNew(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ConsentRequestInit(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ConsentRequestInit(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ConsentRequestStatus(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ConsentRequestStatus(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ConsentRequestHiuOnNotify(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ConsentRequestHiuOnNotify(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ConsentFetch(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ConsentFetch(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3DataFlowHealthInformationRequest(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3DataFlowHealthInformationRequest(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ConsentRequestOnInit(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ConsentRequestOnInit(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ConsentRequestNotify(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ConsentRequestNotify(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getConsentRequestDetails(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getConsentRequestDetails(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3HealthInformationNotify(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3HealthInformationNotify(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3RequestHiuOnNotify(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3RequestHiuOnNotify(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCareContextCreatedById(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getCareContextCreatedById(abdmCredentialdDto);
		}catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public Response v3OnRequestHiu(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3OnRequestHiu(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDatapushUrlsDetails(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getDatapushUrlsDetails(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getHiuDecryptedData(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getHiuDecryptedData(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3HiuConsentRequestOnStatus(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3HiuConsentRequestOnStatus(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkAccessOfABDMOfUnit(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.checkAccessOfABDMOfUnit(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientDataByPatientId(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getPatientDataByPatientId(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3UpdateStatus(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3UpdateStatus(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response truncateFHIRDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.truncateFHIRDetails(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response v3LinkCarecontextNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception {
	try
	{
		return iAbdmCredentialDao.v3LinkCarecontextNotify(abdmCredentialdDto);
	} catch (Exception e)
	{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@Override
	public Response saveCarecontextOnNotify(AbdmCredentialdDto ab) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.saveCarecontextOnNotify(ab);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getGrantedHiTypes(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getGrantedHiTypes(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ProfileAccountAbhaSearch(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ProfileAccountAbhaSearch(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response v3ProfileLoginRequestOtp(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.v3ProfileLoginRequestOtp(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getLinkedCareContextById(AbdmCredentialdDto abdmCredentialdDto)
			throws ApplicationException, Exception {
		try
		{
			return iAbdmCredentialDao.getLinkedCareContextById(abdmCredentialdDto);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
