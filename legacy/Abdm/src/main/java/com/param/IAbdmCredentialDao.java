package com.param;
import com.param.entity.model.master.AbdmCallBackResponse;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAbdmCredentialDao {

	Response getCredentialDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException;

	Response registerOrgnaisationInABDM(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response addUpdateServicesInAbdm(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response generateOtpAadhar(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response verifyOTPOtpAadhar(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response resendAadhaarOtp(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;
	
	Response addGetServiceAbdm(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response checkAndGenerateMobileOTP(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response checkHealthCardAlreadyExists(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getpngCard(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response fetchModesValidation(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response onFetchModes(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response authInit(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response authConfirm(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getProfileDetailsByAdharNumber(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveAbdmCallBackDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response updateOnInitCall(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response updateOnConfirm(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;
	
	Response savePatientRegistrationFromABDMinHIS(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response savePatientRegistrationAfterConfirmCall(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getProfileDetailsByCallBackURLResponse(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response checkPatientAlreadyExist(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception;

	Response saveShareResponse(AbdmCredentialdDto ab)throws ApplicationException, Exception;

	Response getShareDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getTranscationTableDetailsById(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;
	
	Response searchByHealthId(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response authInitMethod(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response confirmWithMobileOtp(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response confirmWithAadhaarOtp(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveDiscoverResponse(AbdmCredentialdDto ab)throws ApplicationException, Exception;

	Response saveInitDiscoverResponse(AbdmCredentialdDto ab)throws ApplicationException, Exception;

	Response verifyMobileOTP(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response createHealthIdWithPreVerified(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response profileDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response suggestion(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response isExist(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response createPhrAddress(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getPngCardNew(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveConfirmDiscoverResponse(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response addContexts(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveOnAddContextsResponse(AbdmCredentialdDto ab)throws ApplicationException, Exception;

	Response consentsHipNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveLinksContextOnNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response patientsSmsNotify2(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response savePatientSmsOnNotify(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException,Exception;

	Response saveConsentsHipNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException,Exception;

	Response saveHealthInformationHipRequest(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException,Exception;

	Response saveHealthInformationNotify(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception;

	Response searchByMobile(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveAbdmShareResponse(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response mobileResendOtp(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response generateOtpForLogin(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response verifyOtpForLogin(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response verifyUserForAbhaLogin(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getPofileDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getAbhaCard(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response requestOtpForAadhaar(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response enrollmentEnrolByAadhaar(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;
	Response enrollmentEnrolAbhaAddress(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;
	Response enrollmentAuthByAbdm(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response enrollmentEnrolSuggestion(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getCard(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response hidBenifitCreateHealthIdDemoAuth(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response enrollmentEnrolbyDocument(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response authInitVersionNew(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response authConfirmWithAadhaarOtp(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getAccountProfile(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v2AuthConfirmWithMobileOTP(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3PhrWebLoginAbhaSearch(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3PhrWebLoginAbhaRequestOtp(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3PhrWebLoginAbhaVerify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3PhrWebloginProfileAbhaProfile(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3PhrWebloginProfileAbhaPhrCard(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3TokenGenerateToken(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3LinkCarecontext(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3UserInitiatedLinkingPatientCareContextOnDiscover(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3UserInitiatedLinkingLinkCareContextOnInit(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3UserInitiatedLinkingLinkCareContextOnConfirm(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3DataFlowHealthInformationHipOnRequest(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response patientHiuDataNotification(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3DataFlowHealthInformationNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;
	
	Response v3BridgeUrl(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3LinkPatientLinksSmsNotify2(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ConsentRequestHipOnNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveOnGenerateToken(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response saveOnCarecontext(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response updateShareDetailsByTokenNumber(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getPatientReqDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getEncounterNumberByPatientId(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getAdmissionNumberByPatientId(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3LinkCarecontextNew(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ConsentRequestInit(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ConsentRequestStatus(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ConsentRequestHiuOnNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ConsentFetch(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3DataFlowHealthInformationRequest(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ConsentRequestOnInit(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ConsentRequestNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getConsentRequestDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3HealthInformationNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3RequestHiuOnNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getCareContextCreatedById(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;
	Response v3OnRequestHiu(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getDatapushUrlsDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getHiuDecryptedData(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3HiuConsentRequestOnStatus(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response checkAccessOfABDMOfUnit(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getPatientDataByPatientId(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3UpdateStatus(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response truncateFHIRDetails(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;


	Response saveCarecontextOnNotify(AbdmCredentialdDto ab)throws ApplicationException, Exception;

	Response getGrantedHiTypes(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3LinkCarecontextNotify(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ProfileAccountAbhaSearch(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response v3ProfileLoginRequestOtp(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;

	Response getLinkedCareContextById(AbdmCredentialdDto abdmCredentialdDto)throws ApplicationException, Exception;


}

