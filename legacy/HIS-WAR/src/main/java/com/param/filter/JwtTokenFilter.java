package com.param.filter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.param.global.utility.UserComponent;
import in.param.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.param.security.config.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtTokenFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String domain = null;

		String originName = request.getHeader("Origin");
		//HIS-37402 Security - Missing Security Header in response
		response.addHeader("Strict-Transport-Security","max-age=31536000");
		response.addHeader("Content-Security-Policy", "object-src 'none'; frame-ancestors *");
		response.addHeader("X-XSS-Protection","1; mode=block");
		response.addHeader("X-Frame-Options", "SAMEORIGIN");
		response.addHeader("X-Content-Type-Options","nosniff");

		try {
			final String authorizationHeader = request.getHeader("Authorization");
			String requestUrl = request.getRequestURI();
			String[] splitUrl = requestUrl.split("/");

			if (request instanceof HttpServletRequest servletRequest) {
				String url = servletRequest.getRequestURL().toString();

				domain = url.replace(requestUrl, "");
				boolean isdomainAllowed = checkDomain(UserComponent.getAllowedDomains(),domain);
				if(! isdomainAllowed) {

					throw new ApplicationException(domain+" Not Allowed Please Contact Administrator.");
				}

				if(originName != null){
					boolean isoriginNameAllowed = checkDomain(UserComponent.getAllowedDomains(),originName);
					if(! isoriginNameAllowed){
						System.out.println("Origin "+originName +" Not Allowed ... for domain "+domain);
						throw new ApplicationException(" Origin "+originName+" Not Allowed Please Contact Administrator.");
					}
				}

			}



			Integer userId = request.getHeader("userId") != null ? Integer.valueOf(request.getHeader("userId")) : 0;
			Integer unitId = request.getHeader("unitId") != null ? Integer.valueOf(request.getHeader("unitId")) : null;


  if (unitId != null && userId != null && authorizationHeader != null
					&& !(requestUrl.contains("/adt/userLogin") || requestUrl.contains("/adt/organizationMasterList")
					|| requestUrl.contains("/adt/getUnitByCredentials")
					|| requestUrl.contains("/ldapUserCredentialCheck") || requestUrl.contains("/Report")
					|| requestUrl.contains("/generateToken") || requestUrl.contains("/QR")
					|| requestUrl.contains("/pathology/report") || requestUrl.contains("/RIS/RADIOLOGY")
					|| requestUrl.contains("/api/billing/mylife/sync/appointment")
					|| requestUrl.contains("/api/global/getVideoConsultationLinkFromHIS")
					|| requestUrl.contains("/applicationConfiguration/screen-images")
					|| requestUrl.contains("/api/mylife/sync/appointment/cancelorRescheduleAppointmentFromApp")
					|| requestUrl.contains("/api/billing/unit/getDoctorCharges")
					|| requestUrl.contains("/adt/discharge/dischargeSummaryCard")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/api/opd/getTemplateDataByCovTempId")
					|| requestUrl.contains("/api/common/getSearchedInstructionList")
					|| requestUrl.contains("/api/opd/coversheetInstructionList/1")
					|| requestUrl.contains("/api/opd/coversheetInstruction")
					|| requestUrl.contains("/api/opd/updateCoversheetInstructionStatus")
					|| requestUrl.contains("/api/opd/getComplaintAppointment")
					|| requestUrl.contains("/api/opd/saveComplaintAppointment/")
					|| requestUrl.contains("/api/opd/getMasterListOfICPCSystemForCoversheet")
					|| requestUrl.contains("/api/opd/updateComplaintAppointmentStatusToRemoveComplaint")
					|| requestUrl.contains("/api/opd/getPatientDiagnosisByPatientIdForNewEMR")
					|| requestUrl.contains("/api/opd/savePatientDiagnosisForNewEMR")
					|| requestUrl.contains("/api/opd/saveFreeTexPatientDiagnosis")
					|| requestUrl.contains("/api/opd/savePatientCancelledDiagnosis")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/saveCovTemplateDataForPatient")
					|| requestUrl.contains("/api/opd/getTemplateDataByCovTempId")
					|| requestUrl.contains("/api/opd/updateCovTemplateDataForPatient")
					|| requestUrl.contains("/api/opd/getDailySlots")
					|| requestUrl.contains("/api/opd/scheduleAppointment")
					|| requestUrl.contains("/api/opd/updateFollowUpDateOfPatient")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/global/serviceMaster")
					|| requestUrl.contains("/api/opd/getInvestigationsListForNewEMR")
					|| requestUrl.contains("/api/opd/saveFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/saveInvestigation")
					|| requestUrl.contains("/api/billing/bill/service/cancel")
					|| requestUrl.contains("/api/opd/cancelFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/getTemplateDataByCovTempId")
					|| requestUrl.contains("/api/opd/getQuickNotesMapperList")
					|| requestUrl.contains("/api/opd/saveQuickNotesMapper")
					|| requestUrl.contains("/api/common/getActiveInspectionMasterList")
					|| requestUrl.contains("/api/globals/frequency")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/api/opd/getMedicationDetailsForNewEMR")
					|| requestUrl.contains("/api/opd/saveMedication")
					|| requestUrl.contains("/api/outPatientFeedback")
					|| requestUrl.contains("/api/inPatientFeedback")
					|| requestUrl.contains("/api/opd/searchDrugByGenericNameOrItemName")
					|| requestUrl.contains("/api/opd/getSubstituteListForNewEMR")
					|| requestUrl.contains("/api/opd/getFrequencyList")
					|| requestUrl.contains("/api/opd/saveJustificationForMedicationDelete")
					|| requestUrl.contains("/api/opd/getOPDMedicationListById")
					|| requestUrl.contains("/api/opd/medicationUpdate")
					|| requestUrl.contains("/api/opd/saveJustificationForMedicationDeleteMultipleMedication")
					|| requestUrl.contains("/api/opd/getLastEncounteredListOfPatient")
					|| requestUrl.contains("/api/opd/updateEncounterDetailsForNewEMR")
					|| requestUrl.contains("/api/global/getPatientDetailsListForNewEMR")
					|| requestUrl.contains("/api/opd/updateCovTemplateDataForPatient")
					|| requestUrl.contains("/api/opd/getListOfVitalsByPatientIdForCoversheet")
					|| requestUrl.contains("/api/opd/saveAppointmentVitalMapper/")
					|| requestUrl.contains("/api/global/savePatientRemarksForNewEMR")
					|| requestUrl.contains("/api/opd/updateEncounterDetailsForNewEMR")
					|| requestUrl.contains("/api/opd/getCompleteConsultationReport")
					|| requestUrl.contains("/api/global/getPatientDetailsListForNewEMR")
					|| requestUrl.contains("/api/opd/getLastEncounterDataOfPatientForWebAPP")
					|| requestUrl.contains("/adt/getCookieDataForNewEMR")
					|| requestUrl.contains("/api/opd/updateCovTemplateDataForPatient")
					|| requestUrl.contains("/api/global/getPatientDetailsListForNewEMROnlyFavoritesList")
					|| requestUrl.contains("/adt/getPatientDetailsForWorklistForWebPage")
					|| requestUrl.contains("/adt/getCookieDataForNewEMR")
					|| requestUrl.contains("/api/opd/searchFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/getFreeTextInvestigations")
					|| requestUrl.contains("/api/common/getSearchedFreeTextInstructionList")
					|| requestUrl.contains("/api/opd/searchFreeTextComplaint")
					|| requestUrl.contains("/api/opd/serchFreeTexPatientDiagnosis")
					|| requestUrl.contains("/api/billing/unit/bill/Report")
					|| requestUrl.contains("/api/opd/getCoversheetListData")
					|| requestUrl.contains("/adt/getListOfDocumentForBookView")
					|| requestUrl.contains("/api/opd/getInvestigationsListForBookView")
					|| requestUrl.contains("/api/opd/getMedicationHistoryList")
					|| requestUrl.contains("/api/opd/getCurrentPatientHabitDetails")
					|| requestUrl.contains("/api/opd/getCurrentPatientFamilyHistoryForCoversheet")
					|| requestUrl.contains("/api/opd/getOldPatientFamilyHistory")
					|| requestUrl.contains("/api/opd/getCurrentAllergyPatientMapperForCoversheet")
					|| requestUrl.contains("/api/opd/getCurrentPatientSurgicalHistoryForCoversheet")
					|| requestUrl.contains("/api/opd/getOldPatientSurgicalHistory")
					|| requestUrl.contains("/api/global/updateEmployeeStatus")
					|| requestUrl.contains("/api/global/updateDoctorStatus") || requestUrl.contains("/br/")
					|| requestUrl.contains("/global/getPopUpDataWithId") || requestUrl.contains("/resultUrl")
					|| requestUrl.contains("/validationResultResponse")
					|| requestUrl.contains("/api/opd/patientDoc/details")
					|| requestUrl.contains("/api/unit/clinical/getDoctorFavouriteMedicationForCoversheet")
					|| requestUrl.contains("/HIS-WAR/swagger-ui.html")
					|| requestUrl.contains("/global/saveTemplateSummery")
					|| requestUrl.contains("/getBillNoByPatient")
					|| requestUrl.contains("/getBillReceiptBytesMyLife")
					|| requestUrl.contains("/updatePatientRegistrationFromMyLife")


					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/typography.css")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/reset.css")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/screen.css")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/handlebars-2.0.0.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/underscore-min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/swagger-ui.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js")

					|| requestUrl.contains("/HIS-WAR/swagger-ui.html")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/backbone-min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jsoneditor.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/highlight.7.3.pack.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/marked.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/springfox.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/swagger-oauth.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/print.css")

					|| requestUrl.contains("/HIS-WAR/images/favicon-32x32.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-16x16.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-32x32.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-16x16.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-32x32.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-16x16.png")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/images/logo_small.png")

					|| requestUrl.contains("/HIS-WAR/configuration/ui")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans-Bold.ttf")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans.ttf")

					// || requestUrl.contains("/HIS-WAR/api-ui")

					|| requestUrl.contains("/HIS-WAR/swagger-resources")

					|| requestUrl.contains("/HIS-WAR/v2/api-docs")

					|| requestUrl.contains("/HIS-WAR/configuration/security")
					|| requestUrl.contains("/getOrderIdUnitSubscriptionPay")
					|| requestUrl.contains("/unit/subcription")
					|| requestUrl.contains("/https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook")
					|| requestUrl.contains("/token/Doctor/")
					|| requestUrl.contains("/checkParti/")
					|| requestUrl.contains("/checkParti/")
					|| requestUrl.contains("/api/saveTwilioMsg")
					|| requestUrl.contains("/api/getVideoataForNotification")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants/Patient/dataroom_41015")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants/*")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants/**")
					|| requestUrl.contains("/api/deleteParticipants")
					|| requestUrl.contains("/api/abdm/request/otp")
					|| requestUrl.contains("/api/abdm/saveDetailsOfAbdm")
					|| requestUrl.contains("/api/abdm/dataPush")
					|| requestUrl.contains("/decrypt")
					|| requestUrl.contains("/api/abdm/getHiuDecryptedData")
					|| requestUrl.contains("/api/global/growthDevelopmentalMilestones/patient")
					|| requestUrl.contains("/api/global/growthDevelopmentalMilestones/detail")
					|| requestUrl.contains("/checkEmailAvailability")
					|| requestUrl.contains("/otpVerification")
					|| requestUrl.contains("/forgotPasswordGeneration")
					|| requestUrl.contains("/updateNABH")
					|| requestUrl.contains("/unit/org/")
					|| requestUrl.contains("/adt/getActivePrefixListForWebPage")
					|| requestUrl.contains("/adt/getActiveRelationListForWebPage")
					|| requestUrl.contains("/adt/getActiveCountryList/")
					|| requestUrl.contains("/adt/getActiveSpecialityList")
					|| requestUrl.contains("/api/global/getUserListForStore/")
					|| requestUrl.contains("/api/global/webappointment/getDoctors")
					|| requestUrl.contains("/api/webappointment/getActivePrefixList/")
					|| requestUrl.contains("/api/doctorApp/getApplicationConfig/")
					|| requestUrl.contains("/api/webappointment/getDetailsForBookAppointment")
					|| requestUrl.contains("/adt/getSpecialityListByDoctorIdByOrg")
					|| requestUrl.contains("/adt/getActiveStateListByCountryId")
					|| requestUrl.contains("/adt/getActiveDistrictListByStateId")
					|| requestUrl.contains("/adt/getAreaByCityId")
					|| requestUrl.contains("/adt/getCountryById")
					|| requestUrl.contains("/adt/getNationalityByCountryId")
		  			|| requestUrl.contains("/adt/getActiveStateList")
		  			|| requestUrl.contains("/adt/getDistrictMasterList")
		  			|| requestUrl.contains("/adt/getCityMasterList")
		  			|| requestUrl.contains("/adt/getAreaMasterList")
		  			|| requestUrl.contains("/adt/getActiveCityListByDistrictId")
					|| requestUrl.contains("/api/global/appoitments/doctors")
					|| requestUrl.contains("/api/global/appointments/doctorsForWebUser")
		  			|| requestUrl.contains("/roomRent")
					|| requestUrl.contains("/api/abdm/v3/updateStatus")
		 			|| requestUrl.contains("/getSerivceRateGlobalList")
		            || requestUrl.contains("/api/createOrganizationFromWeb")
		            || requestUrl.contains("/api/refreshAllCaches")
		            || requestUrl.contains("/LIS/global/getTotalLanguageMasterRecordActive/")
		            || requestUrl.contains("/api/global/glablServiceMaster")
		  			|| requestUrl.contains("opd/saveMedication")
		  			|| requestUrl.contains("update/service")
				    || requestUrl.contains("generateTokenForWebUser")
		  			|| requestUrl.contains("api/opd/patientDoc/saveAnalyzedReport")
		  			|| requestUrl.contains("/api/billing/service/preBilling/orderedServicesByMobileNoForWebUser")
		  			|| requestUrl.contains("/adt/getAppointmentsByPatientId")
		  			|| requestUrl.contains("/api/mylife/sync/savePatientThroughMylife")

		  			|| requestUrl.contains("/api/opd/patientDoc/fromMylife")


					  || requestUrl.contains("api/opd/global/patientTypeHeadSearchForSelfRegistration")
					  || requestUrl.contains("api/opd/appointment/check")
					  || requestUrl.contains("api/global/patientTypeHeadSearchForAppointmentUnique")
					  || requestUrl.contains("api/billing/kiosk/saveAndPay")
					  || requestUrl.contains("adt/getActivePrefixList")
		              || requestUrl.contains("adt/getPrefixMasterList")
		              || requestUrl.contains("api/global/patientRegistration")

		  || requestUrl.contains("api/global/patientTypeHeadSearchForSelfRegistration")

		  || requestUrl.contains("api/global/getAvailableDoctorBySpeciality")

		  			|| requestUrl.contains("/api/ipd/saveMyLatestMedicalSummaryCallBack")


		  || requestUrl.contains("/api/opd/saveAIAppointmentInQueue")

		  || requestUrl.contains("/api/opd/getAIAppointmentCount")




					
			)) {
				String username = null;
				String token = null;

				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
					token = authorizationHeader.substring(7);
				} else {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
				}

				if (JwtTokenUtil.validateToken(token, userId, unitId)
						&& SecurityContextHolder.getContext().getAuthentication() == null) {
					// Validate the token and set up the Spring Security context
					username = JwtTokenUtil.extractUsername(token);
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
							null, null);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					chain.doFilter(request, response);
				} else if (JwtTokenUtil.validateToken(token, userId, unitId)
						&& SecurityContextHolder.getContext().getAuthentication() != null) {
					chain.doFilter(request, response);
				} else {
					System.out.println(
							"UNAUTHORIZED ##" + requestUrl + "##" + splitUrl.length + "##" + userId + "##" + unitId);
					if(requestUrl.contains(("/views/"))) {
						response.setStatus(HttpStatus.OK.value());
					}
					else{
						response.setStatus(HttpStatus.UNAUTHORIZED.value());
					}
				}

			} else if (splitUrl.length <= 2 || (requestUrl.contains("/resources/")) || (requestUrl.contains("/views/"))
					|| (requestUrl.contains("/adt/userLogin") || requestUrl.contains("/adt/organizationMasterList")
					|| requestUrl.contains("/adt/getUnitByCredentials")
					|| requestUrl.contains("/ldapUserCredentialCheck") || requestUrl.contains("/Report")
					|| requestUrl.contains("/generateToken") || requestUrl.contains("/QR")
					|| requestUrl.contains("/pathology/report") || requestUrl.contains("/RIS/RADIOLOGY")
					|| requestUrl.contains("/api/billing/mylife/sync/appointment")
					|| requestUrl.contains("/applicationConfiguration/screen-images")
					|| requestUrl.contains("/api/mylife/sync/appointment/cancelorRescheduleAppointmentFromApp")
					|| requestUrl.contains("/api/billing/unit/getDoctorCharges")
					|| requestUrl.contains("/adt/discharge/dischargeSummaryCard")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/api/opd/getTemplateDataByCovTempId")
					|| requestUrl.contains("/api/common/getSearchedInstructionList")
					|| requestUrl.contains("/api/opd/coversheetInstructionList/1")
					|| requestUrl.contains("/api/opd/coversheetInstruction")
					|| requestUrl.contains("/api/opd/updateCoversheetInstructionStatus")
					|| requestUrl.contains("/api/opd/getComplaintAppointment")
					|| requestUrl.contains("/api/opd/saveComplaintAppointment/")
					|| requestUrl.contains("/api/opd/getMasterListOfICPCSystemForCoversheet")
					|| requestUrl.contains("/api/opd/updateComplaintAppointmentStatusToRemoveComplaint")
					|| requestUrl.contains("/api/opd/getPatientDiagnosisByPatientIdForNewEMR")
					|| requestUrl.contains("/api/opd/savePatientDiagnosisForNewEMR")
					|| requestUrl.contains("/api/opd/saveFreeTexPatientDiagnosis")
					|| requestUrl.contains("/api/opd/savePatientCancelledDiagnosis")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/saveCovTemplateDataForPatient")
					|| requestUrl.contains("/api/opd/getTemplateDataByCovTempId")
					|| requestUrl.contains("/api/opd/updateCovTemplateDataForPatient")
					|| requestUrl.contains("/api/opd/getDailySlots")
					|| requestUrl.contains("/api/opd/scheduleAppointment")
					|| requestUrl.contains("/api/opd/updateFollowUpDateOfPatient")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/global/serviceMaster")
					|| requestUrl.contains("/api/opd/getInvestigationsListForNewEMR")
					|| requestUrl.contains("/api/opd/saveFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/saveInvestigation")
					|| requestUrl.contains("/api/billing/bill/service/cancel")
					|| requestUrl.contains("/api/opd/cancelFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/getTemplateDataByCovTempId")
					|| requestUrl.contains("/api/opd/getQuickNotesMapperList")
					|| requestUrl.contains("/api/opd/saveQuickNotesMapper")
					|| requestUrl.contains("/api/common/getActiveInspectionMasterList")
					|| requestUrl.contains("/api/globals/frequency")
					|| requestUrl.contains("/api/opd/getCoversheetAsTemaplateList")
					|| requestUrl.contains("/api/opd/saveTemplateListDataForNewEMR")
					|| requestUrl.contains("/api/opd/getMedicationDetailsForNewEMR")
					|| requestUrl.contains("/api/opd/saveMedication")
					|| requestUrl.contains("/api/opd/searchDrugByGenericNameOrItemName")
					|| requestUrl.contains("/api/opd/getSubstituteListForNewEMR")
					|| requestUrl.contains("/api/opd/getFrequencyList")
					|| requestUrl.contains("/api/outPatientFeedback")
					|| requestUrl.contains("/api/inPatientFeedback")
					|| requestUrl.contains("/api/opd/saveJustificationForMedicationDelete")
					|| requestUrl.contains("/api/opd/getOPDMedicationListById")
					|| requestUrl.contains("/api/opd/medicationUpdate")
					|| requestUrl.contains("/api/opd/saveJustificationForMedicationDeleteMultipleMedication")
					|| requestUrl.contains("/api/opd/getLastEncounteredListOfPatient")
					|| requestUrl.contains("/api/opd/updateEncounterDetailsForNewEMR")
					|| requestUrl.contains("/api/global/getPatientDetailsListForNewEMR")
					|| requestUrl.contains("/api/opd/updateCovTemplateDataForPatient")
					|| requestUrl.contains("/api/opd/getListOfVitalsByPatientIdForCoversheet")
					|| requestUrl.contains("/api/opd/saveAppointmentVitalMapper/")
					|| requestUrl.contains("/api/global/savePatientRemarksForNewEMR")
					|| requestUrl.contains("/api/opd/updateEncounterDetailsForNewEMR")
					|| requestUrl.contains("/api/opd/getCompleteConsultationReport")
					|| requestUrl.contains("/api/global/getPatientDetailsListForNewEMR")
					|| requestUrl.contains("/api/opd/getLastEncounterDataOfPatientForWebAPP")
					|| requestUrl.contains("/adt/getCookieDataForNewEMR")
					|| requestUrl.contains("/api/opd/updateCovTemplateDataForPatient")
					|| requestUrl.contains("/api/global/getPatientDetailsListForNewEMROnlyFavoritesList")
					|| requestUrl.contains("/adt/getPatientDetailsForWorklistForWebPage")
					|| requestUrl.contains("/adt/getCookieDataForNewEMR")
					|| requestUrl.contains("/api/opd/searchFreeTextInvestigations")
					|| requestUrl.contains("/api/opd/getFreeTextInvestigations")
					|| requestUrl.contains("/api/common/getSearchedFreeTextInstructionList")
					|| requestUrl.contains("/api/opd/searchFreeTextComplaint")
					|| requestUrl.contains("/api/opd/serchFreeTexPatientDiagnosis")
					|| requestUrl.contains("/api/billing/unit/bill/Report")
					|| requestUrl.contains("/api/opd/getCoversheetListData")
					|| requestUrl.contains("/adt/getListOfDocumentForBookView")
					|| requestUrl.contains("/api/opd/getInvestigationsListForBookView")
					|| requestUrl.contains("/api/opd/getMedicationHistoryList")
					|| requestUrl.contains("/api/opd/getCurrentPatientHabitDetails")
					|| requestUrl.contains("/api/opd/getCurrentPatientFamilyHistoryForCoversheet")
					|| requestUrl.contains("/api/opd/getOldPatientFamilyHistory")
					|| requestUrl.contains("/api/opd/getCurrentAllergyPatientMapperForCoversheet")
					|| requestUrl.contains("/api/opd/getCurrentPatientSurgicalHistoryForCoversheet")
					|| requestUrl.contains("/api/global/getVideoConsultationLinkFromHIS")
					|| requestUrl.contains("/api/opd/getOldPatientSurgicalHistory")
					|| requestUrl.contains("/api/global/updateEmployeeStatus")
					|| requestUrl.contains("/api/opd/patientDoc/details")
					|| requestUrl.contains("/api/unit/clinical/getDoctorFavouriteMedicationForCoversheet")
					|| requestUrl.contains("/api/global/updateDoctorStatus") || requestUrl.contains("/br/")
					|| requestUrl.contains("/global/getPopUpDataWithId") || requestUrl.contains("/resultUrl")
					|| requestUrl.contains("/validationResultResponse")
					|| requestUrl.contains("/global/saveTemplateSummery")
					|| requestUrl.contains("/getBillNoByPatient")
					|| requestUrl.contains("/getBillReceiptBytesMyLife")
					|| requestUrl.contains("/updatePatientRegistrationFromMyLife")


					|| requestUrl.contains("/HIS-WAR/swagger-ui.html")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/typography.css")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/reset.css")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/screen.css")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/handlebars-2.0.0.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/underscore-min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/swagger-ui.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js")

					|| requestUrl.contains("/HIS-WAR/swagger-ui.html")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/backbone-min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/jsoneditor.min.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/highlight.7.3.pack.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/marked.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/springfox.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/lib/swagger-oauth.js")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/css/print.css")

					|| requestUrl.contains("/HIS-WAR/images/favicon-32x32.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-16x16.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-32x32.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-16x16.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-32x32.png")

					|| requestUrl.contains("/HIS-WAR/images/favicon-16x16.png")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/images/logo_small.png")

					|| requestUrl.contains("/HIS-WAR/configuration/ui")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans-Bold.ttf")

					|| requestUrl.contains("/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans.ttf")

					// || requestUrl.contains("/HIS-WAR/api-ui")

					|| requestUrl.contains("/HIS-WAR/swagger-resources")

					|| requestUrl.contains("/HIS-WAR/v2/api-docs")

					|| requestUrl.contains("/HIS-WAR/configuration/security")
					|| requestUrl.contains("/getOrderIdUnitSubscriptionPay")
					|| requestUrl.contains("/unit/subcription")
					|| requestUrl.contains("/https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook")
					|| requestUrl.contains("/token/Doctor/")
					|| requestUrl.contains("/checkParti/")
					|| requestUrl.contains("/api/saveTwilioMsg")
					|| requestUrl.contains("/api/getVideoataForNotification")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants/Patient/dataroom_41015")
					|| requestUrl.contains("deleteParticipants")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants/*")
					|| requestUrl.contains("/HIS-WAR/deleteParticipants/**")
					|| requestUrl.contains("/api/deleteParticipants")
					|| requestUrl.contains("/api/abdm/request/otp")
					|| requestUrl.contains("/api/abdm/saveDetailsOfAbdm")
					|| requestUrl.contains("/api/abdm/dataPush")
					|| requestUrl.contains("/api/abdm/getHiuDecryptedData")
					|| requestUrl.contains("/decrypt")
					|| requestUrl.contains("/api/global/growthDevelopmentalMilestones/patient")
					|| requestUrl.contains("/api/global/growthDevelopmentalMilestones/detail")
					|| requestUrl.contains("/checkEmailAvailability")
					|| requestUrl.contains("/otpVerification")
					|| requestUrl.contains("/forgotPasswordGeneration")
					|| requestUrl.contains("/updateNABH")
					|| requestUrl.contains("/unit/org/")
					|| requestUrl.contains("/adt/getActivePrefixListForWebPage")
					|| requestUrl.contains("/adt/getActiveRelationListForWebPage")
					|| requestUrl.contains("/adt/getActiveCountryList/")
					|| requestUrl.contains("/adt/getActiveCountryList/")
					|| requestUrl.contains("/adt/getActiveSpecialityList")
					|| requestUrl.contains("/api/global/getUserListForStore/")
					|| requestUrl.contains("/api/global/webappointment/getDoctors")
					|| requestUrl.contains("/api/webappointment/getActivePrefixList/")
					|| requestUrl.contains("/api/doctorApp/getApplicationConfig/")
					|| requestUrl.contains("/api/webappointment/getDetailsForBookAppointment")
					|| requestUrl.contains("/adt/getSpecialityListByDoctorIdByOrg")
				    || requestUrl.contains("/adt/getActiveStateListByCountryId")
				    || requestUrl.contains("/adt/getActiveDistrictListByStateId")
				    || requestUrl.contains("/adt/getAreaByCityId")
				    || requestUrl.contains("/adt/getCountryById")
				    || requestUrl.contains("/adt/getNationalityByCountryId")
				    || requestUrl.contains("/adt/getActiveStateList")
				    || requestUrl.contains("/adt/getDistrictMasterList")
				    || requestUrl.contains("/adt/getCityMasterList")
				    || requestUrl.contains("/adt/getAreaMasterList")
				    || requestUrl.contains("/adt/getActiveCityListByDistrictId")
				    || requestUrl.contains("/api/global/appoitments/doctors")
				    || requestUrl.contains("/api/global/appointments/doctorsForWebUser")
		  			|| requestUrl.contains("/roomRent")
					|| requestUrl.contains("/api/abdm/v3/updateStatus")
		    		|| requestUrl.contains("/getSerivceRateGlobalList")
		            || requestUrl.contains("/api/createOrganizationFromWeb")
		            || requestUrl.contains("/api/refreshAllCaches")
		  			|| requestUrl.contains("/LIS/global/getTotalLanguageMasterRecordActive/")
		            || requestUrl.contains("/api/global/glablServiceMaster")
		  			|| requestUrl.contains("opd/saveMedication")
		  			|| requestUrl.contains("update/service")
		  			|| requestUrl.contains("generateTokenForWebUser")
		  			|| requestUrl.contains("api/opd/patientDoc/saveAnalyzedReport")
                    || requestUrl.contains("/api/billing/service/preBilling/orderedServicesByMobileNoForWebUser")
                    || requestUrl.contains("/adt/getAppointmentsByPatientId")
		            || requestUrl.contains("/api/mylife/sync/savePatientThroughMylife")
		            || requestUrl.contains("/api/opd/patientDoc/fromMylife")
		  || requestUrl.contains("/api/opd/patientDoc/fromMylife")



		  || requestUrl.contains("api/opd/global/patientTypeHeadSearchForSelfRegistration")
		  || requestUrl.contains("api/opd/appointment/check")
		  || requestUrl.contains("api/global/patientTypeHeadSearchForAppointmentUnique")
		  || requestUrl.contains("api/billing/kiosk/saveAndPay")
		  || requestUrl.contains("adt/getActivePrefixList")
		  || requestUrl.contains("adt/getPrefixMasterList")

		  || requestUrl.contains("api/global/patientRegistration")
		  || requestUrl.contains("api/global/patientTypeHeadSearchForSelfRegistration")

		  || requestUrl.contains("api/global/getAvailableDoctorBySpeciality")

		  || requestUrl.contains("/api/ipd/saveMyLatestMedicalSummaryCallBack")

		  || requestUrl.contains("/api/opd/saveAIAppointmentInQueue")

		  || requestUrl.contains("/api/opd/getAIAppointmentCount")

			)) {
				chain.doFilter(request, response);
			} else {
				System.out.println(
						"UNAUTHORIZED ##" + requestUrl + "##" + splitUrl.length + "##" + userId + "##" + unitId);
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
		}
		catch(ApplicationException ae){
			logger.error(ae.getMessage(), ae);
			response.sendError(502,domain+" Not Allowed Please configure "+ae.getMessage());
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}

	private boolean checkDomain(List<String> allowedDomainsList, String domain) throws ApplicationException {
		String plainDomain = domain.replace("//", "");
			String [] domainAray = plainDomain.split(":");
		String finalIp = null;
			if(domainAray.length>1) {
				finalIp  = domainAray[0]+":"+domainAray[1];
				if(allowedDomainsList.contains(finalIp)){
					return true;
				}
				throw new ApplicationException(finalIp);
			}else {
				return false;
			}
	}
}
