package com.param.billing.global.transaction.config.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	
	@NamedQuery(name="GET_DEFAULT_SELF_TARIFF_FROM_APP_BILLING_CONF",
			query="SELECT uabc.unitApplicationBillingConfigurationId as unitApplicationBillingConfigurationId, "
			+ "uabc.organizationId as organizationId,"
			+ "uabc.unitId as unitId, "
			+ "uabc.defaultSelfTariffId as defaultSelfTariffId, "
			+ "uabc.isPreOrPostBilling as isPreOrPostBilling, "
			+ "uabc.isAppointmentRequired as isAppointmentRequired, "
			+ "uabc.radiologySpecialityId as radiologySpecialityId, "
			+ "uabc.hemodialysisSpecialityId as hemodialysisSpecialityId, "
			+ "uabc.isCounterApplicable as isCounterApplicable,  "
			+ "uabc.consultationSpecialityId as consultationSpecialityId, "
			+ "uabc.consultationSubSpecialityId as consultationSubSpecialityId, "
			+ "uabc.patientCategoryId as patientCategoryId, "
			+ "uabc.defaultPatientCategoryId as defaultPatientCategoryId, "
			+ "uabc.docRoleId as docRoleId, "
			+ "uabc.nurseRoleId as nurseRoleId, "
			+ " uabc.vaccinationChartHide  as vaccinationChartHide,  "
			+ "uabc.adminRoleId as adminRoleId,"
			+ "uabc.isServiceWiseSplitBill as isServiceWiseSplitBill, "
			+ "uabc.selfPayeeId as selfPayeeId, "
			+ "uabc.patientRegSerId as patientRegSerId, "
			+ "uabc.opdBillingCatId as opdBillingCatId, "
			+ "uabc.pdfGenerationUrl as pdfGenerationUrl, "
			+ "uabc.pdfGenerationUrlMis as pdfGenerationUrlMis, "
			+ "uabc.casualtySpecialityId as casualtySpecialityId, "
			+ "uabc.badDebtsDays as badDebtsDays, "
			+ "uabc.isSchemeDiscount as isSchemeDiscount, "
			+ " uabc.serviceWiseDoctorListReq as serviceWiseDoctorListReq, "
			+ "uabc.isBackDatedAdmission as isBackDatedAdmission, "
			+ "uabc.senderId as senderId, "
			+ "COALESCE(uabc.paediatricsSpecialityId,0) as paediatricsSpecialityId, "
			+ "COALESCE(uabc.otRentServiceId,0) as otRentServiceId, "
			+ "COALESCE(uabc.additionalOtrentService,0) as additionalOtrentService, "
			+ "COALESCE(uabc.surgeonChargesServiceId,0) as surgeonChargesServiceId, "
			+ "uabc.pharmacyServiceId as pharmacyServiceId, "
			+ "uabc.companyPatientCategory as companyPatientCategory, "
			+ "uabc.insurancePatientCategory as insurancePatientCategory, "
			+ "uabc.isKioskRequired as isKioskRequired, "
			+ "uabc.surchargeServiceId as surchargeServiceId,"
			+ "uabc.isNursingAssessment as  isNursingAssessment,"
			+ "uabc.isRenderedServiceRateEditable as isRenderedServiceRateEditable ,"
			+ "uabc.erSpecialityId as erSpecialityId,"
			+ "uabc.admissionChargesServiceId as admissionChargesServiceId,"
			+ "uabc.ambulanceChargesServiceId as ambulanceChargesServiceId,"
			+ "uabc.isSmartCardRequired as isSmartCardRequired,"
			+ "uabc.isDigitalSignatureRequired as isDigitalSignatureRequired,"
			+ "uabc.isPinPadRequired as isPinPadRequired,"
			+ "uabc.registeredPinPadDevice as registeredPinPadDevice,"
			+ "uabc.groupPracticeNo as groupPracticeNo,  "
			+ "uabc.groupPracticeName as groupPracticeName,  "
			+ "uabc.gradeWiseSeviceApplicable as gradeWiseSeviceApplicable,  "
			+ "uabc.isConsolidatedBill as isConsolidatedBill , "
			+ "uabc.practiceNo as practiceNo,  "
			+ "COALESCE(uabc.doctorSlipTDS,0) as doctorSlipTDS,  "
			+ "uabc.startDateFormat as startDateFormat ,  "
			+ "uabc.isShowPatientByBed as isShowPatientByBed,"
			+ "uabc.consultationSpecialityId as consultationSpecialityId,"
			+ "uabc.isCorporateEmpVerificationReq as isCorporateEmpVerificationReq, "
			+ "uabc.isAdviceTabVisible as isAdviceTabVisible, "
			+ "uabc.isRateVisibleOnCoversheet as isRateVisibleOnCoversheet, "
			+ "uabc.isSendMailOnMailPrescription as isSendMailOnMailPrescription, "
			+ "uabc.isVitalQualifierVisible as isVitalQualifierVisible, "
			+ "uabc.isAdvisedDateVisible as isAdvisedDateVisible, "
			+ "uabc.isPACSRequired as isPACSRequired,"
			+ "uabc.assetMaintenanceSpecialityId as assetMaintenanceSpecialityId, "
			+ "uabc.physiotherapySpecialityId as physiotherapySpecialityId, "
			+ "uabc.obgynSpecialityId as obgynSpecialityId, "
			+ "uabc.hemodialysisSpecialityId as hemodialysisSpecialityId, "
			+ "uabc.pharmacyServiceId as pharmacyServiceId,uabc.staffRoleId as staffRoleId, "
			+ "uabc.refreshTime as refreshTime, "
			+ "COALESCE(uabc.pddDays,0) as pddDays, "
		//	+ "uabc.isProcessShareAgainstSettledBill as isProcessShareAgainstSettledBill, "
			+ "to_char(uabc.morningFromTime,'HH24:MI') as morningFromTime,to_char(uabc.morningToTime,'HH24:MI') as morningToTime, "
			+ "to_char(uabc.eveningFromTime,'HH24:MI') as eveningFromTime,to_char(uabc.eveningToTime,'HH24:MI') as eveningToTime, "
			+ "uabc.skipReadyForBilling as skipReadyForBilling, "
			+ "uabc.skipAdmissionAcceptance as skipAdmissionAcceptance, "
			+ "uabc.skipHousekeeping as skipHousekeeping, "
			+ "uabc.isFreeVisitReq as isFreeVisitReq, "
			+ "uabc.isHospitalVisitReq as isHospitalVisitReq, "
			+ "uabc.isReviewAppReq as isReviewAppReq, "
			+ "uabc.isLabReq as isLabReq, "
			+ "uabc.isEHCReq as isEHCReq, "
			+ "uabc.isRadiologyReq as isRadiologyReq, "
			+ "uabc.isDocConsultReq as isDocConsultReq, "
			+ "uabc.host as host, "
			+ "uabc.port as port, "
			+ "uabc.id as id, "
			+ "uabc.password as password, "
			+ "uabc.isGstVisible as isGstVisible, "
			+ "uabc.diagnosisRequire  as diagnosisRequire , "
			+ "uabc.allowOtherDoctorToConsult as allowOtherDoctorToConsult, "
			+ "uabc.optimizeTransferFlow as optimizeTransferFlow, "
			+ "uabc.skipTransferAcceptance as skipTransferAcceptance, "
			+ "uabc.skipRISReportEntry as skipRISReportEntry, "
			+ "uabc.defaultDiscountOwnership as defaultDiscountOwnership, "
			+ "uabc.kioskLandingScreen as kioskLandingScreen , uabc.biomedicalsChargesServiceId as biomedicalsChargesServiceId,uabc.hospitalChargesServiceId as hospitalChargesServiceId, uabc.mrdChargesServiceId as mrdChargesServiceId,"
			+ "uabc.isPaymentGatewayReqForWebApp as isPaymentGatewayReqForWebApp , "
			+ "uabc.isRisPatientDetailsOnFirstPage as isRisPatientDetailsOnFirstPage,  "
			+ "uabc.razorPayKeyId as razorPayKeyId, "
			+ "uabc.razorPaySecretKey as razorPaySecretKey,uabc.isReportAddIntoDischargeSummary as isReportAddIntoDischargeSummary,"
			+ "uabc.admissionCancellationTime as admissionCancellationTime, "
			+ "uabc.isPackageApplicableDateEditable as isPackageApplicableDateEditable, "
			+ "uabc.webAppUserId as webAppUserId ,"
			+ "uabc.bccEmailId as bccEmailId ,"
			+ "uabc.whatsAppOption as whatsAppOption ,"
			+ "uabc.neuronPlus as neuronPlus ,"
			+ "uabc.razorPayCallBackUrl as razorPayCallBackUrl, "
			+ "uabc.isKioskAnnouncementRequired as isKioskAnnouncementRequired, "
			+ "uabc.cashAmountValidation as cashAmountValidation ,"
			+ "uabc.cashAmountLimit as cashAmountLimit ,"
			+ "uabc.blockReportPrint as blockReportPrint, "
			+ "uabc.payGatewayProviderId as payGatewayProviderId,"
			+ "uabc.emailValidationRequired as emailValidationRequired , "
			+ "uabc.mobileValidationRequired as mobileValidationRequired , "
			+ "uabc.depositTypeWiseRequired as depositTypeWiseRequired , "
			+ "uabc.cimsOption as cimsOption ,  "
			+ "uabc.dateFormat as dateFormat , "
			+ "uabc.backDateAdmissionDays as backDateAdmissionDays, "
			+ "uabc.appointmentUniqueIdSearch as appointmentUniqueIdSearch ,"
			+ "uabc.couponSystemApplicable as couponSystemApplicable, "
			+ "uabc.tokenNumber as tokenNumber, "
			+ "uabc.mandatoryScan as mandatoryScan ,"
			+ "uabc.exceededBillAmountSettlement as exceededBillAmountSettlement, "
            + "uabc.runTimeRateChangeUsingTarrif as runTimeRateChangeUsingTarrif, "
			+ "uabc.qmsPrnPrint as qmsPrnPrint, "
			+ "uabc.dischargeSummaryDataViewOptionId as dischargeSummaryDataViewOptionId, "
            + "uabc.isEmrRequired as isEmrRequired ,"
            + "uabc.tableHeaderColor as tableHeaderColor, "
            + "uabc.roa as roa ,  "
            + "uabc.dosage as dosage ,  "
            + "uabc.gstForBillingBedCategory as gstForBillingBedCategory, "
            + "uabc.singlePageRefresh as singlePageRefresh, "
            + "uabc.isBillManipulationAllowed as isBillManipulationAllowed, "
            + " uabc.invoiceCreationInsurance as invoiceCreationInsurance, "
            + " uabc.invoiceCreationCorporate as invoiceCreationCorporate, "
					+ " uabc.ayushmanBharatIntegration as ayushmanBharatIntegration, "
            + " uabc.isAiButtonConfigc as isAiButtonConfigc,uabc.drugTodrugCheck as drugTodrugCheck, "
            + " uabc.adminServiceMasterId as adminServiceMasterId, "
            + " uabc.maxInputAmount as maxInputAmount, "
			+ " uabc.isAiDoctorEnabled as isAiDoctorEnabled "
			+ "FROM UnitApplicationBillingConfiguration uabc "
			+ "WHERE uabc.organizationId=:organizationId "
			+ "AND uabc.unitId=:unitId "
			+ "AND uabc.status='A' "
			),
	
	@NamedQuery(name="UPDATE_UNIT_APPLICATION_BILLING_CONFIGURATION_MAPPER",
	query="UPDATE UnitApplicationBillingConfiguration uabc SET uabc.status=:status"
			+ " WHERE uabc.organizationId= :organizationId "
			+ " AND uabc.unitId = :unitId"
			+ " AND uabc.unitApplicationBillingConfigurationId = :unitApplicationBillingConfigurationId"
			),
	
	@NamedQuery(name="GET_URL_FOR_PDF_GENERATION",query="SELECT distinct(pdfGenerationUrl) as pdfGenerationUrl "
			+ "FROM UnitApplicationBillingConfiguration apbc "
			+ "WHERE organizationId=:orgId "
			+ "AND unitId=:unitId "
			+ "AND status='A' "),
	
	@NamedQuery(name="GET_URL_FOR_PDF_GENERATION_MIS",query="SELECT distinct(pdfGenerationUrlMis) as pdfGenerationUrlMis "
			+ "FROM UnitApplicationBillingConfiguration apbc "
			+ "WHERE organizationId=:orgId "
			+ "AND unitId=:unitId "
			+ "AND status='A' "),
	
	@NamedQuery(name="GET_URL_FOR_PDF_GENERATION_FOR_BI",query="SELECT distinct(pdfGenerationUrl) as pdfGenerationUrl "
			+ "FROM UnitApplicationBillingConfiguration apbc "
			+ "WHERE organizationId=:orgId "
//			+ "AND unitId=:unitId "
			+ "AND status='A' "),
	
	@NamedQuery(name="GET_OPD_BILLING_BED_CATAGORY",query="SELECT billingBedCategory.billingBedCategoryId as opdBillingCatId, "
			+ "billingBedCategory.billingBedDesc as billingBedDesc "
			+ "FROM BillingBedCategoryMaster billingBedCategory "
			+ "INNER JOIN billingBedCategory.bedVisitTypeMappersList visit "
			+ "WHERE billingBedCategory.organizationId=:orgId "
			+ "AND billingBedCategory.unitId=:unitId "
			+ "AND billingBedCategory.status='A' "
			+ "AND visit.visitTypeId=1 "),
	
	@NamedQuery(name="GET_SELF_PAYEE_LIST",query="SELECT companyId as selfPayeeId , "
			+ "companyDesc as companyDesc  "
			+ "FROM CompanyMaster "
			+ "WHERE companyTypeId=1 "
			+ "AND unitId=:unitId "
			+ "AND organizationId=:organizationId ")

	})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_LIST_UNIT_APPLICATION_BILLING_CONFIGURATION",
					query="SELECT uabc.unit_application_billing_configuration_id as \"unitApplicationBillingConfigurationId\", "
					+ "uabc.organization_id as \"organizationId\", tariff_mst.tariff_code as \"tariffCode\" ,tariff_mst.tariff_desc as \"tariffDesc\", spl_mst.speciality_name as \"specialityName\" ,"
					+ "uabc.unit_id as \"unitId\" , uabc.status as \"status\", "
					+ "uabc.default_self_tariff_id as \"defaultSelfTariffId\", "
					+ "uabc.is_pre_or_post_billing as \"isPreOrPostBilling\", "
					+ "uabc.is_appointment_required as \"isAppointmentRequired\", "
					+ "uabc.radiology_speciality_id as \"radiologySpecialityId\", "
					+ "uabc.is_counter_applicable as \"isCounterApplicable\", "
					+ "uabc.loyalticard_patient_category as \"patientCategoryId\", "
					+ "uabc.default_patient_category_id as \"defaultPatientCategoryId\", "
					+ "uabc.is_service_wise_split_bill as \"isServiceWiseSplitBill\", "
					+ "uabc.casualty_speciality_id as \"casualtySpecialityId\", "
					+ "uabc.doc_role_id as \"docRoleId\", "
					+ "uabc.nurse_role_id as \"nurseRoleId\", "
					+ "uabc.admin_role_id as \"adminRoleId\", "
					+ "uabc.staff_role_id as \"staffRoleId\", "
					+ "uabc.self_payee_id as \"selfPayeeId\", "
					+ "uabc.patient_reg_ser_id as \"patientRegSerId\", "
					+ "uabc.opd_billing_cat_id as \"opdBillingCatId\", "
					+ "uabc.pharmacy_service_id as \"pharmacyServiceId\", "
					+ "ser_mst.service_standard_name as \"pharmacyServiceName\", "
					+ "uabc.consultation_speciality_id as \"consultationSpecialityId\", "
					+ "uabc.consultation_sub_speciality_id as \"consultationSubSpecialityId\", "
					+ "uabc.paediatrics_speciality_id as \"paediatricsSpecialityId\", "
					+ "uabc.pdf_generation_url as \"pdfGenerationUrl\", "
					+ "uabc.pdf_generation_url_mis as \"pdfGenerationUrlMis\", "
					+ "uabc.sender_id as \"senderId\", "
					+ "uabc.is_kiosk_required as \"isKioskRequired\", "
					+ "uabc.surcharge_service_id as \"surchargeServiceId\" ,"
					+ " uabc.is_vaccination_chart_hide  as \"vaccinationChartHide\",  "
					+ "surser_mst.service_standard_name as \"surchargeServiceName\", "
					+ "uabc.pharmacy_return_sale_service_id as \"pharmacyReturnSaleServiceId\", "	
					+ " returnchrser_mst.service_standard_name as \"pharmacyReturnSaleName\","
					+ " uabc.surgeon_charges_service_id as \"surgeonChargesServiceId\", "
					+ " suchser_mst.service_standard_name as \"surgeonChargesServiceName\", "
					+ " uabc.company_patient_category as \"companyPatientCategory\" ,"
					+ " uabc.insurance_patient_category as \"insurancePatientCategory\",  "
					+ " uabc.ot_rent_service_id as \"otRentServiceId\",  "
					+ " otser_mst.service_standard_name as \"otRentServiceName\","
					+ " uabc.additional_otrent_service as \"additionalOtrentService\",  "
					+ " addotrentservicechrser_mst.service_standard_name as \"additionalOtrentServiceName\","
					+ " uabc.is_nursing_assessment as \"isNursingAssessment\" ,"
					+ "uabc.is_rendered_service_rate_editable as \"isRenderedServiceRateEditable\","
					+ "uabc.er_speciality_id as \"erSpecialityId\" ,"
					+ "uabc.admission_charges_service_id as \"admissionChargesServiceId\","
					+ "uabc.ambulance_charges_service_id as \"ambulanceChargesServiceId\","
					+ "admburser_mst.service_standard_name as \"ambulanceChargesService\","
					+ "adchrser_mst.service_standard_name as \"admissionChargesServiceName\","
					+ "uabc.is_smart_card_required as \"isSmartCardRequired\","
					+ "uabc.is_digital_signature_required as \"isDigitalSignatureRequired\","
					+ "uabc.is_pin_pad_required as \"isPinPadRequired\","
					+ "uabc.registered_pin_pad_device as \"registeredPinPadDevice\","
					+ "uabc.group_practice_no as \"groupPracticeNo\" ,  "
					+ "uabc.group_practice_name as \"groupPracticeName\" ,  "
					+ "uabc.grade_wise_sevice_applicable as \"gradeWiseSeviceApplicable\" ,  "
					+ "uabc.practice_no as \"practiceNo\" ,  "
					+ "COALESCE(uabc.doctor_slip_tds,0) as \"doctorSlipTDS\" ,  "
					+ "uabc.start_date_format as \"startDateFormat\" , "
					+ "uabc.is_show_patient_by_bed as \"isShowPatientByBed\" , "
					+ "uabc.is_consolidated_bill as \"isConsolidatedBill\" , "
					+ "uabc.bad_debts_days as \"badDebtsDays\" , "
					+ "uabc.pdd_days as \"pddDays\" , "
					+ "uabc.is_scheme_discount as \"isSchemeDiscount\", "
					+ "uabc.is_back_dated_admission as \"isBackDatedAdmission\", "
					+ "uabc.is_corporate_emp_verification_req as \"isCorporateEmpVerificationReq\" ,"
					+ "uabc.is_advise_tab_visible as \"isAdviceTabVisible\", "
					+ "uabc.is_rate_visible_on_coversheet as \"isRateVisibleOnCoversheet\", "
					+ "uabc.is_send_mail_on_mail_prescription as \"isSendMailOnMailPrescription\", "
					+ "uabc.is_vital_qualifier_visible as \"isVitalQualifierVisible\", "
					+ "uabc.is_advised_date_visible as \"isAdvisedDateVisible\", "
					+ "uabc.is_pacs_required as \"isPACSRequired\" ,"
					+ "uabc.obgyn_speciality_id as \"obgynSpecialityId\" ,uabc.hemodialysis_speciality_id as \"hemodialysisSpecialityId\", "
					+ "uabc.asset_maintenance_speciality_id as \"assetMaintenanceSpecialityId\","
					+ "uabc.physiotherapy_speciality_id as \"physiotherapySpecialityId\", "
				//	+ "uabc.is_process_share_against_settled_bill as \"isProcessShareAgainstSettledBill\", "
					+ "to_char(uabc.morning_start_time,'HH24:MI') as \"morningFromTime\",to_char(uabc.morning_end_time,'HH24:MI') as \"morningToTime\","
					+ "to_char(uabc.evening_start_time,'HH24:MI') as \"eveningFromTime\","
					+ "to_char(uabc.evening_end_time,'HH24:MI') as \"eveningToTime\", "
					+ "uabc.skip_ready_for_billing as \"skipReadyForBilling\", "
					+ "uabc.skip_admission_acceptance as \"skipAdmissionAcceptance\", "
					+ "uabc.skip_housekeeping as \"skipHousekeeping\", "
					+ "uabc.is_free_visit_req as \"isFreeVisitReq\", "
					+ "uabc.is_hospital_visit_req as \"isHospitalVisitReq\", "
					+ "uabc.bucket_flag as \"bucketFlag\", "
					+ "uabc.local_storage_bucket_json as \"localStorageJson\", "					
                    + "uabc.sms_type as \"smsType\", "
                    + "uabc.sms_type_json as \"smsTypejson\", "					
					+ "uabc.is_review_app_req as \"isReviewAppReq\", "
					+ "uabc.is_lab_req as \"isLabReq\", "
					+ "uabc.is_ehc_req as \"isEHCReq\", "
					+ "uabc.is_radiology_req as \"isRadiologyReq\", "
					+ "uabc.is_doc_consult_req as \"isDocConsultReq\", "
					+ "uabc.is_gst_visible as \"isGstVisible\", "
					+ "uabc.optimize_transfer_flow as \"optimizeTransferFlow\", "
					+ "uabc.skip_transfer_acceptance as \"skipTransferAcceptance\", "
					+ "uabc.diagnosis_require  as \"diagnosisRequire\" , "
					+ "uabc.allow_other_doctor_to_consult as \"allowOtherDoctorToConsult\","
					+ "uabc.skip_ris_report_entry as \"skipRISReportEntry\", "
					+ "uabc.default_discount_ownership as \"defaultDiscountOwnership\", "
					+ "uabc.kiosk_landing_screen as \"kioskLandingScreen\" ,"					
					+ "mrd_ser_mst.service_standard_name as \"mrdChargesServiceName\","
					+ "uabc.mrd_charges_service_id as \"mrdChargesServiceId\", "
					+ "uabc.hospital_charges_service_id as \"hospitalChargesServiceId\","
					+ "hos_ser_mst.service_standard_name as \"hospitalChargesServiceName\","
					+ "uabc.biomedicals_charges_service_id as \"biomedicalsChargesServiceId\","
					+ "uabc.smtp_user_id as \"emailId\","
					+ "uabc.smtp_user_pwd as \"password\","
					+ "biomdical_ser_mst.service_standard_name as \"biomedicalsChargesServiceName\","
					+ "uabc.payment_gateway_req_for_web_appointment as \"isPaymentGatewayReqForWebApp\", "
					+ "uabc.ris_patient_details_on_first_page as \"isRisPatientDetailsOnFirstPage\", "
					+ "uabc.razor_pay_key_id as \"razorPayKeyId\", "
					+ "uabc.razor_pay_secret_key as \"razorPaySecretKey\" ,uabc.is_add_report_to_discharge_summary as \"isReportAddIntoDischargeSummary\","
					+ " uabc.amission_cancellation_time as \"admissionCancellationTime\", "
					+ " uabc.is_package_applicable_date_editable as \"isPackageApplicableDateEditable\", "
					+ " uabc.web_app_user_id as \"webAppUserId\", "
					+ " uabc.mail_provider_id as \"mailProviderId\", "
					+ " uabc.mail_credential as \"mailCredential\", "
					+ " uabc.bcc_email_id as \"bccEmailId\", "
					+ " uabc.is_kiosk_announcement_required as \"isKioskAnnouncementRequired\" ,"
					+ " uabc.cash_amount_validation as \"cashAmountValidation\" ,"
					+ " uabc.cash_amount_limit as \"cashAmountLimit\" ,"
					+ " uabc.block_report_print as \"blockReportPrint\", "
					+ " uabc.whats_app_option as \"whatsAppOption\", "
					+ " uabc.neuron_plus as \"neuronPlus\", "
					+ " uabc.pay_gateway_provider_id as \"payGatewayProviderId\" ,"
					+ " uabc.email_validation_required as \"emailValidationRequired\" , "
					+ " uabc.mobile_validation_required as \"mobileValidationRequired\" , "
					+ " uabc.deposit_type_wise_required as \"depositTypeWiseRequired\" , "
					+ " uabc.date_format as \"dateFormat\" , "
					+ " uabc.cims_option as \"cimsOption\" ,  "
					+ " uabc.service_wise_doctor_list_req as \"serviceWiseDoctorListReq\" , "
					+ " uabc.self_paid_consult_alert as \"selfPaidConsultAlert\" , "
					+ " uabc.back_date_admission_days as \"backDateAdmissionDays\" , "
					+ " uabc.appointment_unique_id_search as \"appointmentUniqueIdSearch\" , "
					+ " uabc.coupon_system_applicable as \"couponSystemApplicable\" ,"
					+ " uabc.token_number as \"tokenNumber\" ,"
					+ " uabc.mandatory_scan as \"mandatoryScan\" ,"
					+ " uabc.is_emr_required as \"isEmrRequired\", "
					+ " uabc.exceeded_bill_amount_settlement as \"exceededBillAmountSettlement\", "
                    + " uabc.run_time_rate_change_using_tarrif as \"runTimeRateChangeUsingTarrif\" ,"
                    + " uabc.discharge_summary_data_view_option_id as \"dischargeSummaryDataViewOptionId\", "
					+ " uabc.qms_prn_print as \"qmsPrnPrint\", "
					+ " uabc.roa as \"roa\",  "
			        + " uabc.dosage as \"dosage\" ,"
			        + " uabc.is_bill_manipulation_allowed as \"isBillManipulationAllowed\", "  
			        + " uabc.invoice_creation_insurance as \"invoiceCreationInsurance\", "  
			        + " uabc.invoice_creation_corporate as \"invoiceCreationCorporate\", "
							+ " uabc.ayushman_bharat_integration as \"ayushmanBharatIntegration\", "
							+ " uabc.ispdf_password_require as \"ispdfPasswordRequire\", "
							+ " uabc.gst_for_billing_bed_category as \"gstForBillingBedCategory\", "
					+ " uabc.tally_ip as \"tallyIp\", "
					+ " uabc.is_tally_local_host as \"isTallyLocalHost\", "
					+ " uabc.tally_port_no as \"tallyPortNo\", "
					+ " uabc.admin_service_master_id as \"adminServiceMasterId\", "
					+ " uabc.other_org_logo_required as \"otherOrgLogoRequired\", "
					+ " uabc.max_input_amount as \"maxInputAmount\", "
					+ " uabc.is_ai_doctor_enabled as \"isAiDoctorEnabled\" "
					+ "from public.t_unit_application_billing_configuration uabc "
					+ "left join service.m_tariff_master tariff_mst"
					+ " on uabc.default_self_tariff_id=tariff_mst.tariff_id "
					+ "left join doctor.m_speciality_master spl_mst"
					+ " on uabc.radiology_speciality_id=spl_mst.speciality_id "
					+ " left join service.m_service_master ser_mst"
					+" on uabc.pharmacy_service_id=ser_mst.service_master_id "
					+ " left join service.m_service_master otser_mst"
					+" on uabc.ot_rent_service_id =otser_mst.service_master_id"
					+ " left join service.m_service_master suchser_mst"
					+" on uabc.surgeon_charges_service_id =suchser_mst.service_master_id"
					+ " left join service.m_service_master surser_mst"
					+" on uabc.surcharge_service_id =surser_mst.service_master_id"
					+ " left join service.m_service_master adchrser_mst"
					+" on uabc.admission_charges_service_id =adchrser_mst.service_master_id"
					+ " left join service.m_service_master admburser_mst"
					+" on uabc.ambulance_charges_service_id =admburser_mst.service_master_id"
					+ " left join service.m_service_master returnchrser_mst"
					+" on uabc.pharmacy_return_sale_service_id = returnchrser_mst.service_master_id"
					+ " left join service.m_service_master addotrentservicechrser_mst"
					+" on uabc.additional_otrent_service = addotrentservicechrser_mst.service_master_id "
					+ " left join service.m_service_master mrd_ser_mst"
					+" on uabc.mrd_charges_service_id = mrd_ser_mst.service_master_id "
					+ " left join service.m_service_master hos_ser_mst"
					+" on uabc.hospital_charges_service_id = hos_ser_mst.service_master_id "
					+ " left join service.m_service_master biomdical_ser_mst"
					+" on uabc.biomedicals_charges_service_id = biomdical_ser_mst.service_master_id "
					+ " WHERE uabc.organization_id=:organizationId "
					+ "AND uabc.unit_id=:unitId "
					+ "AND uabc.status= 'A' "
),
	
	@NamedNativeQuery(name="GET_UNIT_APPLICATION_REGISTRATION_FIELD_CONFIG",query=" select conf.unit_appl_registration_config_id as \"unitApplRegistrationConfigId\" , conf.prefix as \"prefix\", conf.first_name as \"firstName\", " 
+" conf.middle_name as \"middleName\" ,conf.last_name as \"lastName\" ,conf.birth_date as \"birthDate\", conf.sex as \"sex\",conf.calling_code as \"callingCode\", conf.email as \"email\", " 
+" conf.mobile_no as \"mobileNo\" , conf.mobile_digit as \"mobileDigit\" , conf.nationality as \"nationality\", conf.identification_doc as \"identificationDoc\", " 
+" conf.race as \"race\", conf.marital_status as \"maritalStatus\",conf.occupation as \"occupation\",conf.patient_category as \"patientCategory\",conf.photo as \"photo\", " 
+" conf.thumb_impression as \"thumbImpression\",conf.kin_details as \"kinDetails\", conf.address as \"address\", conf.code_digit as \"codeDigit\",conf.source_id as \"sourceId\",conf.kinprefix as\"kinprefix\",conf.kin_name as\"kinName\",conf.relation_id as \"relationId\",conf.kin_mobile as\"kinMobileNo\","
+ "conf.phone_no as \"phoneNo\",conf.identification_id as \"identificationId\",conf.identificationno_id as \"identificationNo\",conf.kin_address as\"kinAddress\",conf.country_id as \"countryId\",conf.state_id as \"stateId\",conf.district_id as \"districtId\",conf.city_id as \"cityId\","
+ "conf.area as \"areaId\",conf.post_id as \"postCode\", conf.unit_id as \"unitId\", conf.organization_id as \"organizationId\" "
+" from public.t_unit_application_registration_configuration conf " 
+" where conf.unit_id=:unitId " 
+"  and conf.organization_id=:organizationId ")
	
})
@Entity
@Table(name="t_unit_application_billing_configuration",schema="public")
@SequenceGenerator(name="unit_application_billing_configuration_seq",sequenceName="public.unit_application_billing_configuration_seq",allocationSize=1)
public class UnitApplicationBillingConfiguration {

	@Id
	@Column(name=" unit_application_billing_configuration_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="unit_application_billing_configuration_seq")
	private Integer  unitApplicationBillingConfigurationId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="default_self_tariff_id")
	private Integer defaultSelfTariffId;
	
	@Column(name="is_pre_or_post_billing")
	private Character isPreOrPostBilling;
	
	@Column(name="is_counter_applicable")
	private Character isCounterApplicable;
	
	@Column(name="is_appointment_required")
	private Character isAppointmentRequired;
	
	@Column(name="is_service_wise_split_bill")
	private Character isServiceWiseSplitBill;
	
	@Column(name="is_kiosk_required")
	private Character isKioskRequired;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="radiology_speciality_id")
	private Integer radiologySpecialityId;
	
	@Column(name="loyalticard_patient_category")
	private Integer patientCategoryId;
	
	@Column(name="default_patient_category_id")
	private Integer defaultPatientCategoryId;
	
	@Column(name="doc_role_id")
	private Integer docRoleId;
	
	@Column(name="nurse_role_id")
	private Integer nurseRoleId;
	
	@Column(name="staff_role_id")
	private Integer staffRoleId;
	
	@Column(name="admin_role_id")
	private Integer adminRoleId;
	
	@Column(name="pdf_generation_url")
	private String pdfGenerationUrl;
	
	@Column(name="patient_reg_ser_id")
	private Integer patientRegSerId;
	
	@Column(name="opd_billing_cat_id")
	private Integer opdBillingCatId;
	
	@Column(name="self_payee_id")
	private Integer selfPayeeId;
	
	@Column(name="consultation_speciality_id")
	private Integer consultationSpecialityId;
	
	@Column(name="consultation_sub_speciality_id")
	private Integer consultationSubSpecialityId;

	@Column(name="sender_id")
	private String senderId;
	
	@Column(name="casualty_speciality_id")
	private Integer casualtySpecialityId;
	
	@Column(name="paediatrics_speciality_id")
	private Integer paediatricsSpecialityId;
	
	@Column(name="pharmacy_service_id")
	private Integer pharmacyServiceId;
	
	@Column(name="company_patient_category")
	private Integer companyPatientCategory;
	
	@Column(name="insurance_patient_category")
	private Integer insurancePatientCategory;
	
	@Column(name="pharmacy_return_sale_service_id")
	private Integer pharmacyReturnSaleServiceId;
	
	@Column(name="ot_rent_service_id")
    private Integer otRentServiceId;

	@Column(name="surgeon_charges_service_id")
	private Integer surgeonChargesServiceId;
	
	@Column(name="surcharge_service_id")
	private Integer surchargeServiceId;
	
	@Column(name="is_nursing_assessment")
	private Character isNursingAssessment;
	
	@Column(name="is_rendered_service_rate_editable")
	private Character isRenderedServiceRateEditable;
	
	@Column(name="er_speciality_id")
	private Integer erSpecialityId;
	
	@Column(name="admission_charges_service_id")
	private Integer admissionChargesServiceId;
	
	@Column(name="is_smart_card_required")
	private Character isSmartCardRequired;
	
	@Column(name="is_digital_signature_required")
	private Character isDigitalSignatureRequired;
	
	@Column(name="is_pin_pad_required")
	private Character isPinPadRequired;
	
	@Column(name="is_show_patient_by_bed")
	private Character isShowPatientByBed;
	
	@Column(name="is_corporate_emp_verification_req")
	private Character isCorporateEmpVerificationReq;

	//For SmartCard
	@Column(name="group_practice_no")
	private String groupPracticeNo;
	
	@Column(name="group_practice_name")
	private String groupPracticeName;
	
	@Column(name="practice_no")
	private String practiceNo;
	
	@Column(name="start_date_format")
	private String startDateFormat;

	@Column(name="is_advise_tab_visible")
	private Character isAdviceTabVisible;
	
	@Column(name="is_pacs_required")
	private Character isPACSRequired;
	
	@Column(name="obgyn_speciality_id")
	private Integer obgynSpecialityId;
	
	@Column(name="hemodialysis_speciality_id")
	private Integer hemodialysisSpecialityId;
	
	@Column(name="is_consolidated_bill")
	private Character isConsolidatedBill;	
	
	@Column(name="asset_maintenance_speciality_id")
	private Integer assetMaintenanceSpecialityId;
	
	@Column(name="physiotherapy_speciality_id")
	private Integer physiotherapySpecialityId;
	
	@Column(name="grade_wise_sevice_applicable")
	private boolean gradeWiseSeviceApplicable;
	/*
	 * @Column(name="is_process_share_against_settled_bill") private Character
	 * isProcessShareAgainstSettledBill;
	 */
	
	@Column(name="refresh_time")
	private String refreshTime;
	
	@Column(name="morning_start_time")
	@Convert(converter = InstantConverter.class)
	private Instant morningFromTime;
	
	@Column(name="morning_end_time")
	@Convert(converter = InstantConverter.class)
	private Instant morningToTime;
	
	@Column(name="evening_start_time")
	@Convert(converter = InstantConverter.class)
	private Instant eveningFromTime;
	
	@Column(name="evening_end_time")
	@Convert(converter = InstantConverter.class)
	private Instant eveningToTime;
	
	@Column(name="additional_otrent_service")
	private Integer additionalOtrentService;
	
	@Column(name="bad_debts_days")
	private Integer badDebtsDays;
	
	@Column(name="is_scheme_discount")
	private Character isSchemeDiscount;
	
	@Column(name="is_back_dated_admission")
	private Character isBackDatedAdmission;
	
	@Column(name="pdd_days")
	private Integer pddDays;
	
	@Column(name="skip_ready_for_billing")
	private Character skipReadyForBilling;
	
	@Column(name="skip_admission_acceptance")
	private Character skipAdmissionAcceptance;
	
	@Column(name="skip_housekeeping")
	private Character skipHousekeeping;
	
	@Column(name="is_free_visit_req")
	private Character isFreeVisitReq;
	
	@Column(name="is_hospital_visit_req")
	private Character isHospitalVisitReq;
	
	@Column(name="is_review_app_req")
	private Character isReviewAppReq;
	
	@Column(name="is_lab_req")
	private Character isLabReq;
	
	@Column(name="is_ehc_req")
	private Character isEHCReq;
	
	@Column(name="is_radiology_req")
	private Character isRadiologyReq;
	
	@Column(name="is_doc_consult_req")
	private Character isDocConsultReq;
	
	@Column(name="smtp_host")
	private String host;
	
	@Column(name="smtp_port")
	private String port;
	
	@Column(name="smtp_user_id")
	private String id;
	
	@Column(name="smtp_user_pwd")
	private String password;
	
	@Column(name="is_gst_visible")
	private Character isGstVisible;
	
	@Column(name="optimize_transfer_flow")
	private Character optimizeTransferFlow;
	
	@Column(name="allow_other_doctor_to_consult")
	private Character allowOtherDoctorToConsult;
	
	@Column(name="diagnosis_require")
	private Character diagnosisRequire;
	
	@Column(name="skip_ris_report_entry")
	private Character skipRISReportEntry;
	
	@Column(name="default_discount_ownership")
	private Integer defaultDiscountOwnership;
	
	@Column(name="is_rate_visible_on_coversheet")
	private char isRateVisibleOnCoversheet;
	
	@Column(name="is_send_mail_on_mail_prescription")
	private char isSendMailOnMailPrescription;
	
	@Column(name="is_vital_qualifier_visible")
	private char isVitalQualifierVisible;
	
	@Column(name="is_advised_date_visible")
	private char isAdvisedDateVisible;
	
	@Column(name="kiosk_landing_screen")
	private String kioskLandingScreen;
	
	@Column(name="doctor_slip_tds")
	private Integer doctorSlipTDS;
	
	@Column(name="biomedicals_charges_service_id")
	private Integer biomedicalsChargesServiceId;
	
	@Column(name="hospital_charges_service_id")
	private Integer hospitalChargesServiceId;
	
	@Column(name="mrd_charges_service_id")
	private Integer mrdChargesServiceId;
	
	@Column(name="skip_transfer_acceptance")
	private Character skipTransferAcceptance;
	
	@Column(name="payment_gateway_req_for_web_appointment")
	private Character isPaymentGatewayReqForWebApp;
	
	@Column(name="ris_patient_details_on_first_page")
	private Character isRisPatientDetailsOnFirstPage;
	
	@Column(name="razor_pay_key_id")
	private String razorPayKeyId;
	
	@Column(name="razor_pay_secret_key")
	private String razorPaySecretKey;
	
	@Column(name="is_add_report_to_discharge_summary")
	private Character isReportAddIntoDischargeSummary;
	
	@Column(name="amission_cancellation_time")
	private Integer admissionCancellationTime;
	
	@Column(name="is_package_applicable_date_editable")
	private Character isPackageApplicableDateEditable;
	
	@Column(name="web_app_user_id")
	private Integer webAppUserId;

	@Column(name="is_kiosk_announcement_required")
	private Character isKioskAnnouncementRequired;
	
	@Column(name="razor_pay_call_back_url")
	private String razorPayCallBackUrl;
	
	@Column(name="mail_provider_id")
	private Integer mailProviderId;
	
	@Column(name="mail_credential")
	private String mailCredential;
	
	@Column(name="cash_amount_validation")
	private Integer cashAmountValidation;
	
	@Column(name="cash_amount_limit")
	private Integer cashAmountLimit;
	
	@Column(name="is_vaccination_chart_hide")
	private boolean vaccinationChartHide;
	
	@Column(name="bcc_email_id")
	private String bccEmailId;
	
	@Column(name="block_report_print")
	private Character blockReportPrint;
	
	@Column(name="pay_gateway_provider_id")
	private Integer payGatewayProviderId;
	
	@Column(name="email_validation_required")
	private Character emailValidationRequired;
	
	@Column(name="whats_app_option")
	private Integer whatsAppOption;
	
	@Column(name="date_format")
	private String dateFormat;

	@Column(name="self_paid_consult_alert")
	private boolean selfPaidConsultAlert;

	@Column(name="neuron_plus")
	private Character neuronPlus;
	
	@Column(name="deposit_type_wise_required")
	private boolean depositTypeWiseRequired;

	@Column(name="registered_pin_pad_device")
	private String registeredPinPadDevice;
	
	@Column(name="cims_option")
	private Character cimsOption;

	@Column(name="pdf_generation_url_mis")
	private String pdfGenerationUrlMis;
	
	@Column(name="service_wise_doctor_list_req")
	private boolean serviceWiseDoctorListReq;
	
	@Column(name="back_date_admission_days")
	private Integer backDateAdmissionDays;

	@Column(name="appointment_unique_id_search")
	private Character appointmentUniqueIdSearch;
	
	@Column(name="coupon_system_applicable")
	private boolean couponSystemApplicable;
	
	@Column(name="token_number")
	private Character tokenNumber;
	
	@Column(name="mobile_validation_required")
	private Character mobileValidationRequired;
	
	@Column(name="mandatory_scan")
	private Character mandatoryScan;
	
	@Column(name="exceeded_bill_amount_settlement")
	private boolean exceededBillAmountSettlement;
	
	@Column(name="run_time_rate_change_using_tarrif")
	private boolean runTimeRateChangeUsingTarrif;

	@Column(name="qms_prn_print")
	private Character qmsPrnPrint;
	
	@Column(name="ambulance_charges_service_id")
	private Integer ambulanceChargesServiceId;

	@Column(name="discharge_summary_data_view_option_id")
	private Integer dischargeSummaryDataViewOptionId;
	
	@Column(name="table_header_color")
	private String tableHeaderColor;
	
	@Column(name="cimms_data")
	private String cimmsData;
	
	@Column(name="gst_for_billing_bed_category")
	private boolean gstForBillingBedCategory;
	
	@Column(name="single_page_refresh")
	private String singlePageRefresh;
	
	@Column(name="roa")
	private Character roa;
	
	@Column(name="dosage")
	private Character dosage;
	
	@Column(name="local_storage_bucket_json")
	private String localStorageBucketJson;
	
	@Column(name="bucket_flag")
	private Integer bucketFlag;
	
	@Column(name="sms_type_json")
	private String smsTypejson;
	
	@Column(name="sms_type")
	private Integer smsType;
	
	@Column(name="is_bill_manipulation_allowed")
	private Character isBillManipulationAllowed;
	
	@Column(name="invoice_creation_insurance")
	private Character invoiceCreationInsurance;
	
	@Column(name="invoice_creation_corporate")
	private Character invoiceCreationCorporate;

	@Column(name="is_ai_button_configc")
	private Character isAiButtonConfigc;


	@Column(name="ayushman_bharat_integration")
	private Character ayushmanBharatIntegration;

	@Column(name="ispdf_password_require")
	private Character ispdfPasswordRequire;

	@Column(name="tally_ip")
	private String tallyIp;

	@Column(name="tally_port_no")
	private String tallyPortNo;

	@Column(name = "is_tally_local_host")
	private Character isTallyLocalHost;

    @Column(name = "admin_service_master_id")
	private Integer adminServiceMasterId;

	@Column(name = "other_org_logo_required")
	private Character otherOrgLogoRequired;


	@Column(name = "drug_to_drug_check")
	private Character drugTodrugCheck;

	@Column(name = "max_input_amount", precision = 15, scale = 0)
	private BigDecimal maxInputAmount;

	@Column(name = "is_ai_doctor_enabled")
	private Character isAiDoctorEnabled;
	
	public Character getIsBillManipulationAllowed() {
		return isBillManipulationAllowed;
	}

	public void setIsBillManipulationAllowed(Character isBillManipulationAllowed) {
		this.isBillManipulationAllowed = isBillManipulationAllowed;
	}

	public String getSinglePageRefresh() {
		return singlePageRefresh;
	}

	public void setSinglePageRefresh(String singlePageRefresh) {
		this.singlePageRefresh = singlePageRefresh;
	}

	public String getBccEmailId() {
		return bccEmailId;
	}

	public void setBccEmailId(String bccEmailId) {
		this.bccEmailId = bccEmailId;
	}
	
	public Character getIsKioskAnnouncementRequired() {
		return isKioskAnnouncementRequired;
	}

	public void setIsKioskAnnouncementRequired(Character isKioskAnnouncementRequired) {
		this.isKioskAnnouncementRequired = isKioskAnnouncementRequired;
	}

	public Character getSkipTransferAcceptance() {
		return skipTransferAcceptance;
	}

	public void setSkipTransferAcceptance(Character skipTransferAcceptance) {
		this.skipTransferAcceptance = skipTransferAcceptance;
	}

	public String getKioskLandingScreen() {
		return kioskLandingScreen;
	}

	public void setKioskLandingScreen(String kioskLandingScreen) {
		this.kioskLandingScreen = kioskLandingScreen;
	}

	public Integer getDefaultDiscountOwnership() {
		return defaultDiscountOwnership;
	}

	public void setDefaultDiscountOwnership(Integer defaultDiscountOwnership) {
		this.defaultDiscountOwnership = defaultDiscountOwnership;
	}

	public Character getOptimizeTransferFlow() {
		return optimizeTransferFlow;
	}

	public void setOptimizeTransferFlow(Character optimizeTransferFlow) {
		this.optimizeTransferFlow = optimizeTransferFlow;
	}

	public Character getIsGstVisible() {
		return isGstVisible;
	}

	public void setIsGstVisible(Character isGstVisible) {
		this.isGstVisible = isGstVisible;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Character getIsFreeVisitReq() {
		return isFreeVisitReq;
	}

	public void setIsFreeVisitReq(Character isFreeVisitReq) {
		this.isFreeVisitReq = isFreeVisitReq;
	}

	public Character getIsHospitalVisitReq() {
		return isHospitalVisitReq;
	}

	public void setIsHospitalVisitReq(Character isHospitalVisitReq) {
		this.isHospitalVisitReq = isHospitalVisitReq;
	}

	public Character getIsReviewAppReq() {
		return isReviewAppReq;
	}

	public void setIsReviewAppReq(Character isReviewAppReq) {
		this.isReviewAppReq = isReviewAppReq;
	}

	public Character getIsLabReq() {
		return isLabReq;
	}

	public void setIsLabReq(Character isLabReq) {
		this.isLabReq = isLabReq;
	}

	public Character getIsEHCReq() {
		return isEHCReq;
	}

	public void setIsEHCReq(Character isEHCReq) {
		this.isEHCReq = isEHCReq;
	}

	public Character getIsRadiologyReq() {
		return isRadiologyReq;
	}

	public void setIsRadiologyReq(Character isRadiologyReq) {
		this.isRadiologyReq = isRadiologyReq;
	}

	public Character getIsDocConsultReq() {
		return isDocConsultReq;
	}

	public void setIsDocConsultReq(Character isDocConsultReq) {
		this.isDocConsultReq = isDocConsultReq;
	}

	public Character getSkipAdmissionAcceptance() {
		return skipAdmissionAcceptance;
	}

	public void setSkipAdmissionAcceptance(Character skipAdmissionAcceptance) {
		this.skipAdmissionAcceptance = skipAdmissionAcceptance;
	}

	public Character getSkipHousekeeping() {
		return skipHousekeeping;
	}

	public void setSkipHousekeeping(Character skipHousekeeping) {
		this.skipHousekeeping = skipHousekeeping;
	}

	public Character getSkipReadyForBilling() {
		return skipReadyForBilling;
	}

	public void setSkipReadyForBilling(Character skipReadyForBilling) {
		this.skipReadyForBilling = skipReadyForBilling;
	}

	public Integer getPddDays() {
		return pddDays;
	}

	public void setPddDays(Integer pddDays) {
		this.pddDays = pddDays;
	}

	public Character getIsBackDatedAdmission() {
		return isBackDatedAdmission;
	}

	public void setIsBackDatedAdmission(Character isBackDatedAdmission) {
		this.isBackDatedAdmission = isBackDatedAdmission;
	}

	public Character getIsSchemeDiscount() {
		return isSchemeDiscount;
	}

	public void setIsSchemeDiscount(Character isSchemeDiscount) {
		this.isSchemeDiscount = isSchemeDiscount;
	}

	public Integer getBadDebtsDays() {
		return badDebtsDays;
	}

	public void setBadDebtsDays(Integer badDebtsDays) {
		this.badDebtsDays = badDebtsDays;
	}

	public String getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}

	/*
	 * public Character getIsProcessShareAgainstSettledBill() { return
	 * isProcessShareAgainstSettledBill; }
	 * 
	 * public void setIsProcessShareAgainstSettledBill(Character
	 * isProcessShareAgainstSettledBill) { this.isProcessShareAgainstSettledBill =
	 * isProcessShareAgainstSettledBill; }
	 */

	public Integer getDefaultPatientCategoryId() {
		return defaultPatientCategoryId;
	}

	public void setDefaultPatientCategoryId(Integer defaultPatientCategoryId) {
		this.defaultPatientCategoryId = defaultPatientCategoryId;
	}

	public Integer getSurchargeServiceId() {
		return surchargeServiceId;
	}

	public void setSurchargeServiceId(Integer surchargeServiceId) {
		this.surchargeServiceId = surchargeServiceId;
	}

	public Integer getCompanyPatientCategory() {
		return companyPatientCategory;
	}

	public void setCompanyPatientCategory(Integer companyPatientCategory) {
		this.companyPatientCategory = companyPatientCategory;
	}

	public Integer getInsurancePatientCategory() {
		return insurancePatientCategory;
	}

	public void setInsurancePatientCategory(Integer insurancePatientCategory) {
		this.insurancePatientCategory = insurancePatientCategory;
	}

	public Integer getAdminRoleId() {
		return adminRoleId;
	}

	public void setAdminRoleId(Integer adminRoleId) {
		this.adminRoleId = adminRoleId;
	}

	public Integer getUnitApplicationBillingConfigurationId() {
		return unitApplicationBillingConfigurationId;
	}

	public void setUnitApplicationBillingConfigurationId(Integer unitApplicationBillingConfigurationId) {
		this.unitApplicationBillingConfigurationId = unitApplicationBillingConfigurationId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public Character getIsCounterApplicable() {
		return isCounterApplicable;
	}

	public void setIsCounterApplicable(Character isCounterApplicable) {
		this.isCounterApplicable = isCounterApplicable;
	}

	public Character getIsAppointmentRequired() {
		return isAppointmentRequired;
	}

	public void setIsAppointmentRequired(Character isAppointmentRequired) {
		this.isAppointmentRequired = isAppointmentRequired;
	}

	public Character getIsServiceWiseSplitBill() {
		return isServiceWiseSplitBill;
	}

	public void setIsServiceWiseSplitBill(Character isServiceWiseSplitBill) {
		this.isServiceWiseSplitBill = isServiceWiseSplitBill;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getRadiologySpecialityId() {
		return radiologySpecialityId;
	}

	public void setRadiologySpecialityId(Integer radiologySpecialityId) {
		this.radiologySpecialityId = radiologySpecialityId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getDocRoleId() {
		return docRoleId;
	}

	public void setDocRoleId(Integer docRoleId) {
		this.docRoleId = docRoleId;
	}

	public Integer getNurseRoleId() {
		return nurseRoleId;
	}

	public void setNurseRoleId(Integer nurseRoleId) {
		this.nurseRoleId = nurseRoleId;
	}

	public Integer getStaffRoleId() {
		return staffRoleId;
	}

	public void setStaffRoleId(Integer staffRoleId) {
		this.staffRoleId = staffRoleId;
	}

	public String getPdfGenerationUrl() {
		return pdfGenerationUrl;
	}

	public void setPdfGenerationUrl(String pdfGenerationUrl) {
		this.pdfGenerationUrl = pdfGenerationUrl;
	}

	public Integer getPatientRegSerId() {
		return patientRegSerId;
	}

	public void setPatientRegSerId(Integer patientRegSerId) {
		this.patientRegSerId = patientRegSerId;
	}

	public Integer getOpdBillingCatId() {
		return opdBillingCatId;
	}

	public void setOpdBillingCatId(Integer opdBillingCatId) {
		this.opdBillingCatId = opdBillingCatId;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Integer getConsultationSubSpecialityId() {
		return consultationSubSpecialityId;
	}

	public void setConsultationSubSpecialityId(Integer consultationSubSpecialityId) {
		this.consultationSubSpecialityId = consultationSubSpecialityId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public Integer getCasualtySpecialityId() {
		return casualtySpecialityId;
	}

	public void setCasualtySpecialityId(Integer casualtySpecialityId) {
		this.casualtySpecialityId = casualtySpecialityId;
	}

	public Integer getPaediatricsSpecialityId() {
		return paediatricsSpecialityId;
	}

	public void setPaediatricsSpecialityId(Integer paediatricsSpecialityId) {
		this.paediatricsSpecialityId = paediatricsSpecialityId;
	}

	public Integer getPharmacyServiceId() {
		return pharmacyServiceId;
	}

	public void setPharmacyServiceId(Integer pharmacyServiceId) {
		this.pharmacyServiceId = pharmacyServiceId;
	}

	public Integer getPharmacyReturnSaleServiceId() {
		return pharmacyReturnSaleServiceId;
	}

	public void setPharmacyReturnSaleServiceId(Integer pharmacyReturnSaleServiceId) {
		this.pharmacyReturnSaleServiceId = pharmacyReturnSaleServiceId;
	}

	public Character getIsKioskRequired() {
		return isKioskRequired;
	}

	public void setIsKioskRequired(Character isKioskRequired) {
		this.isKioskRequired = isKioskRequired;
	}


	public Integer getOtRentServiceId() {
		return otRentServiceId;
	}

	public void setOtRentServiceId(Integer otRentServiceId) {
		this.otRentServiceId = otRentServiceId;
	}

	public Integer getSurgeonChargesServiceId() {
		return surgeonChargesServiceId;
	}

	public void setSurgeonChargesServiceId(Integer surgeonChargesServiceId) {
		this.surgeonChargesServiceId = surgeonChargesServiceId;
	}

	public Character getIsNursingAssessment() {
		return isNursingAssessment;
	}

	public void setIsNursingAssessment(Character isNursingAssessment) {
		this.isNursingAssessment = isNursingAssessment;
	}

	public Character getIsRenderedServiceRateEditable() {
		return isRenderedServiceRateEditable;
	}

	public void setIsRenderedServiceRateEditable(Character isRenderedServiceRateEditable) {
		this.isRenderedServiceRateEditable = isRenderedServiceRateEditable;
	}

	public Integer getErSpecialityId() {
		return erSpecialityId;
	}

	public void setErSpecialityId(Integer erSpecialityId) {
		this.erSpecialityId = erSpecialityId;
	}

	public Integer getAdmissionChargesServiceId() {
		return admissionChargesServiceId;
	}

	public void setAdmissionChargesServiceId(Integer admissionChargesServiceId) {
		this.admissionChargesServiceId = admissionChargesServiceId;
	}

	public Character getIsSmartCardRequired() {
		return isSmartCardRequired;
	}

	public void setIsSmartCardRequired(Character isSmartCardRequired) {
		this.isSmartCardRequired = isSmartCardRequired;
	}

	public Character getIsDigitalSignatureRequired() {
		return isDigitalSignatureRequired;
	}

	public void setIsDigitalSignatureRequired(Character isDigitalSignatureRequired) {
		this.isDigitalSignatureRequired = isDigitalSignatureRequired;
	}

	public Character getIsPinPadRequired() {
		return isPinPadRequired;
	}

	public void setIsPinPadRequired(Character isPinPadRequired) {
		this.isPinPadRequired = isPinPadRequired;
	}

	public Character getIsShowPatientByBed() {
		return isShowPatientByBed;
	}

	public void setIsShowPatientByBed(Character isShowPatientByBed) {
		this.isShowPatientByBed = isShowPatientByBed;
	}

	public String getGroupPracticeNo() {
		return groupPracticeNo;
	}

	public void setGroupPracticeNo(String groupPracticeNo) {
		this.groupPracticeNo = groupPracticeNo;
	}

	public String getGroupPracticeName() {
		return groupPracticeName;
	}

	public void setGroupPracticeName(String groupPracticeName) {
		this.groupPracticeName = groupPracticeName;
	}

	public String getPracticeNo() {
		return practiceNo;
	}

	public void setPracticeNo(String practiceNo) {
		this.practiceNo = practiceNo;
	}

	public String getStartDateFormat() {
		return startDateFormat;
	}

	public void setStartDateFormat(String startDateFormat) {
		this.startDateFormat = startDateFormat;
	}
	
	public Character getIsCorporateEmpVerificationReq() {
		return isCorporateEmpVerificationReq;
	}

	public void setIsCorporateEmpVerificationReq(Character isCorporateEmpVerificationReq) {
		this.isCorporateEmpVerificationReq = isCorporateEmpVerificationReq;
	}

	public Character getIsAdviceTabVisible() {
		return isAdviceTabVisible;
	}

	public void setIsAdviceTabVisible(Character isAdviceTabVisible) {
		this.isAdviceTabVisible = isAdviceTabVisible;
	}

	public Character getIsPACSRequired() {
		return isPACSRequired;
	}

	public void setIsPACSRequired(Character isPACSRequired) {
		this.isPACSRequired = isPACSRequired;
	}

	public Integer getObgynSpecialityId() {
		return obgynSpecialityId;
	}

	public void setObgynSpecialityId(Integer obgynSpecialityId) {
		this.obgynSpecialityId = obgynSpecialityId;
	}

	public Integer getHemodialysisSpecialityId() {
		return hemodialysisSpecialityId;
	}

	public void setHemodialysisSpecialityId(Integer hemodialysisSpecialityId) {
		this.hemodialysisSpecialityId = hemodialysisSpecialityId;
	}

	public Character getIsConsolidatedBill() {
		return isConsolidatedBill;
	}

	public void setIsConsolidatedBill(Character isConsolidatedBill) {
		this.isConsolidatedBill = isConsolidatedBill;
	}

	public Integer getAssetMaintenanceSpecialityId() {
		return assetMaintenanceSpecialityId;
	}

	public void setAssetMaintenanceSpecialityId(Integer assetMaintenanceSpecialityId) {
		this.assetMaintenanceSpecialityId = assetMaintenanceSpecialityId;
	}

	public Integer getPhysiotherapySpecialityId() {
		return physiotherapySpecialityId;
	}

	public void setPhysiotherapySpecialityId(Integer physiotherapySpecialityId) {
		this.physiotherapySpecialityId = physiotherapySpecialityId;
	}

	public Instant getMorningFromTime() {
		return morningFromTime;
	}

	public void setMorningFromTime(Instant morningFromTime) {
		this.morningFromTime = morningFromTime;
	}

	public Instant getMorningToTime() {
		return morningToTime;
	}

	public void setMorningToTime(Instant morningToTime) {
		this.morningToTime = morningToTime;
	}

	public Instant getEveningFromTime() {
		return eveningFromTime;
	}

	public void setEveningFromTime(Instant eveningFromTime) {
		this.eveningFromTime = eveningFromTime;
	}

	public Instant getEveningToTime() {
		return eveningToTime;
	}

	public void setEveningToTime(Instant eveningToTime) {
		this.eveningToTime = eveningToTime;
	}

	public Integer getAdditionalOtrentService() {
		return additionalOtrentService;
	}

	public void setAdditionalOtrentService(Integer additionalOtrentService) {
		this.additionalOtrentService = additionalOtrentService;
	}

	public Character getAllowOtherDoctorToConsult() {
		return allowOtherDoctorToConsult;
	}

	public void setAllowOtherDoctorToConsult(Character allowOtherDoctorToConsult) {
		this.allowOtherDoctorToConsult = allowOtherDoctorToConsult;
	}

	public Character getDiagnosisRequire() {
		return diagnosisRequire;
	}

	public void setDiagnosisRequire(Character diagnosisRequire) {
		this.diagnosisRequire = diagnosisRequire;
	}

	public Character getSkipRISReportEntry() {
		return skipRISReportEntry;
	}

	public void setSkipRISReportEntry(Character skipRISReportEntry) {
		this.skipRISReportEntry = skipRISReportEntry;
	}

	public char getIsRateVisibleOnCoversheet() {
		return isRateVisibleOnCoversheet;
	}

	public void setIsRateVisibleOnCoversheet(char isRateVisibleOnCoversheet) {
		this.isRateVisibleOnCoversheet = isRateVisibleOnCoversheet;
	}

	public char getIsSendMailOnMailPrescription() {
		return isSendMailOnMailPrescription;
	}

	public void setIsSendMailOnMailPrescription(char isSendMailOnMailPrescription) {
		this.isSendMailOnMailPrescription = isSendMailOnMailPrescription;
	}

	public char getIsVitalQualifierVisible() {
		return isVitalQualifierVisible;
	}

	public void setIsVitalQualifierVisible(char isVitalQualifierVisible) {
		this.isVitalQualifierVisible = isVitalQualifierVisible;
	}

	public char getIsAdvisedDateVisible() {
		return isAdvisedDateVisible;
	}

	public void setIsAdvisedDateVisible(char isAdvisedDateVisible) {
		this.isAdvisedDateVisible = isAdvisedDateVisible;
	}

	public Integer getDoctorSlipTDS() {
		return doctorSlipTDS;
	}

	public void setDoctorSlipTDS(Integer doctorSlipTDS) {
		this.doctorSlipTDS = doctorSlipTDS;
	}

	public Integer getBiomedicalsChargesServiceId() {
		return biomedicalsChargesServiceId;
	}

	public void setBiomedicalsChargesServiceId(Integer biomedicalsChargesServiceId) {
		this.biomedicalsChargesServiceId = biomedicalsChargesServiceId;
	}

	public Integer getHospitalChargesServiceId() {
		return hospitalChargesServiceId;
	}

	public void setHospitalChargesServiceId(Integer hospitalChargesServiceId) {
		this.hospitalChargesServiceId = hospitalChargesServiceId;
	}

	public Integer getMrdChargesServiceId() {
		return mrdChargesServiceId;
	}

	public void setMrdChargesServiceId(Integer mrdChargesServiceId) {
		this.mrdChargesServiceId = mrdChargesServiceId;
	}

	public Character getIsPaymentGatewayReqForWebApp() {
		return isPaymentGatewayReqForWebApp;
	}

	public void setIsPaymentGatewayReqForWebApp(Character isPaymentGatewayReqForWebApp) {
		this.isPaymentGatewayReqForWebApp = isPaymentGatewayReqForWebApp;
	}

	public Character getIsRisPatientDetailsOnFirstPage() {
		return isRisPatientDetailsOnFirstPage;
	}

	public void setIsRisPatientDetailsOnFirstPage(Character isRisPatientDetailsOnFirstPage) {
		this.isRisPatientDetailsOnFirstPage = isRisPatientDetailsOnFirstPage;
	}

	public String getRazorPayKeyId() {
		return razorPayKeyId;
	}

	public void setRazorPayKeyId(String razorPayKeyId) {
		this.razorPayKeyId = razorPayKeyId;
	}

	public String getRazorPaySecretKey() {
		return razorPaySecretKey;
	}

	public void setRazorPaySecretKey(String razorPaySecretKey) {
		this.razorPaySecretKey = razorPaySecretKey;
	}

	public Character getIsReportAddIntoDischargeSummary() {
		return isReportAddIntoDischargeSummary;
	}

	public void setIsReportAddIntoDischargeSummary(Character isReportAddIntoDischargeSummary) {
		this.isReportAddIntoDischargeSummary = isReportAddIntoDischargeSummary;
	}

	public Integer getAdmissionCancellationTime() {
		return admissionCancellationTime;
	}

	public void setAdmissionCancellationTime(Integer admissionCancellationTime) {
		this.admissionCancellationTime = admissionCancellationTime;
	}

	public Character getIsPackageApplicableDateEditable() {
		return isPackageApplicableDateEditable;
	}

	public void setIsPackageApplicableDateEditable(Character isPackageApplicableDateEditable) {
		this.isPackageApplicableDateEditable = isPackageApplicableDateEditable;
	}

	public Integer getWebAppUserId() {
		return webAppUserId;
	}

	public void setWebAppUserId(Integer webAppUserId) {
		this.webAppUserId = webAppUserId;
	}

	public String getRazorPayCallBackUrl() {
		return razorPayCallBackUrl;
	}

	public void setRazorPayCallBackUrl(String razorPayCallBackUrl) {
		this.razorPayCallBackUrl = razorPayCallBackUrl;
	}

	public Integer getMailProviderId() {
		return mailProviderId;
	}

	public void setMailProviderId(Integer mailProviderId) {
		this.mailProviderId = mailProviderId;
	}

	public String getMailCredential() {
		return mailCredential;
	}

	public void setMailCredential(String mailCredential) {
		this.mailCredential = mailCredential;
	}

	public Integer getCashAmountValidation() {
		return cashAmountValidation;
	}

	public void setCashAmountValidation(Integer cashAmountValidation) {
		this.cashAmountValidation = cashAmountValidation;
	}

	public Integer getCashAmountLimit() {
		return cashAmountLimit;
	}

	public void setCashAmountLimit(Integer cashAmountLimit) {
		this.cashAmountLimit = cashAmountLimit;
	}

	public boolean getVaccinationChartHide() {
		return vaccinationChartHide;
	}

	public void setVaccinationChartHide(boolean vaccinationChartHide) {
		this.vaccinationChartHide = vaccinationChartHide;
	}
	
	public Integer getPayGatewayProviderId() {
		return payGatewayProviderId;
	}

	public void setPayGatewayProviderId(Integer payGatewayProviderId) {
		this.payGatewayProviderId = payGatewayProviderId;
	}

	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}

	public Integer getWhatsAppOption() {
		return whatsAppOption;
	}

	public void setWhatsAppOption(Integer whatsAppOption) {
		this.whatsAppOption = whatsAppOption;
	}

	public Character getEmailValidationRequired() {
		return emailValidationRequired;
	}

	public void setEmailValidationRequired(Character emailValidationRequired) {
		this.emailValidationRequired = emailValidationRequired;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isSelfPaidConsultAlert() {
		return selfPaidConsultAlert;
	}
	public Character getNeuronPlus() {
		return neuronPlus;
	}

	public void setNeuronPlus(Character neuronPlus) {
		this.neuronPlus = neuronPlus;
	}


	public void setSelfPaidConsultAlert(boolean selfPaidConsultAlert) {
		this.selfPaidConsultAlert = selfPaidConsultAlert;
	}

	public boolean isDepositTypeWiseRequired() {
		return depositTypeWiseRequired;
	}

	public void setDepositTypeWiseRequired(boolean depositTypeWiseRequired) {
		this.depositTypeWiseRequired = depositTypeWiseRequired;
	}

	public String getRegisteredPinPadDevice() {
		return registeredPinPadDevice;
	}

	public void setRegisteredPinPadDevice(String registeredPinPadDevice) {
		this.registeredPinPadDevice = registeredPinPadDevice;
	}

	public Character getCimsOption() {
		return cimsOption;
	}

	public void setCimsOption(Character cimsOption) {
		this.cimsOption = cimsOption;
	}
	
	

	public String getPdfGenerationUrlMis() {
		return pdfGenerationUrlMis;
	}

	public void setPdfGenerationUrlMis(String pdfGenerationUrlMis) {
		this.pdfGenerationUrlMis = pdfGenerationUrlMis;
	}

	public boolean getServiceWiseDoctorListReq() {
		return serviceWiseDoctorListReq;
	}

	public void setServiceWiseDoctorListReq(boolean serviceWiseDoctorListReq) {
		this.serviceWiseDoctorListReq = serviceWiseDoctorListReq;
	}


	public Integer getBackDateAdmissionDays() {
		return backDateAdmissionDays;
	}

	public void setBackDateAdmissionDays(Integer backDateAdmissionDays) {
		this.backDateAdmissionDays = backDateAdmissionDays;
	}

	public Character getAppointmentUniqueIdSearch() {
		return appointmentUniqueIdSearch;
	}

	public void setAppointmentUniqueIdSearch(Character appointmentUniqueIdSearch) {
		this.appointmentUniqueIdSearch = appointmentUniqueIdSearch;
	}
	
	public boolean getGradeWiseSeviceApplicable() {
		return gradeWiseSeviceApplicable;
	}

	public void setGradeWiseSeviceApplicable(boolean gradeWiseSeviceApplicable) {
		this.gradeWiseSeviceApplicable = gradeWiseSeviceApplicable;

	}

	public boolean isCouponSystemApplicable() {
		return couponSystemApplicable;
	}

	public void setCouponSystemApplicable(boolean couponSystemApplicable) {
		this.couponSystemApplicable = couponSystemApplicable;
	}

	public Character getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Character tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public Character getMobileValidationRequired() {
		return mobileValidationRequired;
	}

	public void setMobileValidationRequired(Character mobileValidationRequired) {
		this.mobileValidationRequired = mobileValidationRequired;
	}

	public Character getMandatoryScan() {
		return mandatoryScan;
	}

	public void setMandatoryScan(Character mandatoryScan) {
		this.mandatoryScan = mandatoryScan;
	}

	public boolean isExceededBillAmountSettlement() {
		return exceededBillAmountSettlement;
	}

	public void setExceededBillAmountSettlement(boolean exceededBillAmountSettlement) {
		this.exceededBillAmountSettlement = exceededBillAmountSettlement;
	}

	public boolean isRunTimeRateChangeUsingTarrif() {
		return runTimeRateChangeUsingTarrif;
	}

	public void setRunTimeRateChangeUsingTarrif(boolean runTimeRateChangeUsingTarrif) {
		this.runTimeRateChangeUsingTarrif = runTimeRateChangeUsingTarrif;
	}

	public Character getQmsPrnPrint() {
		return qmsPrnPrint;
	}

	public void setQmsPrnPrint(Character qmsPrnPrint) {
		this.qmsPrnPrint = qmsPrnPrint;
	}
	public Integer getDischargeSummaryDataViewOptionId() {
		return dischargeSummaryDataViewOptionId;
	}

	public void setDischargeSummaryDataViewOptionId(Integer dischargeSummaryDataViewOptionId) {
		this.dischargeSummaryDataViewOptionId = dischargeSummaryDataViewOptionId;
	}



	public Integer getAmbulanceChargesServiceId() {
		return ambulanceChargesServiceId;
	}

	public void setAmbulanceChargesServiceId(Integer ambulanceChargesServiceId) {
		this.ambulanceChargesServiceId = ambulanceChargesServiceId;
	}


	/*---------------------------------------------------  "is_emr_required" column added here -------------------------------------*/
	@Column(name = "is_emr_required")
	private boolean isEmrRequired;
	
	public boolean getIsEmrRequired(){
		return isEmrRequired;
	}
	
	public void setIsEmrRequired(boolean isEmrRequired){
		this.isEmrRequired = isEmrRequired;
	}

	public String getTableHeaderColor() {
		return tableHeaderColor;
	}

	public void setTableHeaderColor(String tableHeaderColor) {
		this.tableHeaderColor = tableHeaderColor;
	}

	public String getCimmsData() {
		return cimmsData;
	}

	public void setCimmsData(String cimmsData) {
		this.cimmsData = cimmsData;
	}

	public boolean getGstForBillingBedCategory() {
		return gstForBillingBedCategory;
	}

	public void setGstForBillingBedCategory(boolean gstForBillingBedCategory) {
		this.gstForBillingBedCategory = gstForBillingBedCategory;
	}

	public Character getRoa() {
		return roa;
	}

	public void setRoa(Character roa) {
		this.roa = roa;
	}

	public Character getDosage() {
		return dosage;
	}

	public void setDosage(Character dosage) {
		this.dosage = dosage;
	}
	public String getLocalStorageBucketJson() {
		return localStorageBucketJson;
	}

	public void setLocalStorageBucketJson(String localStorageBucketJson) {
		this.localStorageBucketJson = localStorageBucketJson;
	}

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

	public String getSmsTypejson() {
		return smsTypejson;
	}

	public void setSmsTypejson(String smsTypejson) {
		this.smsTypejson = smsTypejson;
	}

	public Integer getSmsType() {
		return smsType;
	}

	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}

	public Character getInvoiceCreationInsurance() {
		return invoiceCreationInsurance;
	}

	public void setInvoiceCreationInsurance(Character invoiceCreationInsurance) {
		this.invoiceCreationInsurance = invoiceCreationInsurance;
	}

	public Character getInvoiceCreationCorporate() {
		return invoiceCreationCorporate;
	}

	public void setInvoiceCreationCorporate(Character invoiceCreationCorporate) {
		this.invoiceCreationCorporate = invoiceCreationCorporate;
	}

	public Character getIsAiButtonConfigc() {
		return isAiButtonConfigc;
	}

	public void setIsAiButtonConfigc(Character isAiButtonConfigc) {
		this.isAiButtonConfigc = isAiButtonConfigc;
	}


	public Character getAyushmanBharatIntegration() {
		return ayushmanBharatIntegration;
	}

	public void setAyushmanBharatIntegration(Character ayushmanBharatIntegration) {
		this.ayushmanBharatIntegration = ayushmanBharatIntegration;
	}

	public Character getIspdfPasswordRequire() {
		return ispdfPasswordRequire;
	}

	public void setIspdfPasswordRequire(Character ispdfPasswordRequire) {
		this.ispdfPasswordRequire = ispdfPasswordRequire;
	}

	public String getTallyIp() {
		return tallyIp;
	}

	public void setTallyIp(String tallyIp) {
		this.tallyIp = tallyIp;
	}

	public String getTallyPortNo() {
		return tallyPortNo;
	}

	public void setTallyPortNo(String tallyPortNo) {
		this.tallyPortNo = tallyPortNo;
	}

	public Character getIsTallyLocalHost() {
		return isTallyLocalHost;
	}

	public void setIsTallyLocalHost(Character isTallyLocalHost) {
		this.isTallyLocalHost = isTallyLocalHost;
	}

    public Integer getAdminServiceMasterId() {
        return adminServiceMasterId;
    }

    public void setAdminServiceMasterId(Integer adminServiceMasterId) {
        this.adminServiceMasterId = adminServiceMasterId;
    }

	public Character getOtherOrgLogoRequired() {
		return otherOrgLogoRequired;
	}

	public void setOtherOrgLogoRequired(Character otherOrgLogoRequired) {
		this.otherOrgLogoRequired = otherOrgLogoRequired;
	}

	public Character getDrugTodrugCheck() {
		return drugTodrugCheck;
	}

	public void setDrugTodrugCheck(Character drugTodrugCheck) {
		this.drugTodrugCheck = drugTodrugCheck;
	}

	public BigDecimal getMaxInputAmount() {
		return maxInputAmount;
	}

	public void setMaxInputAmount(BigDecimal maxInputAmount) {
		this.maxInputAmount = maxInputAmount;
	}

	public Character getIsAiDoctorEnabled() {
		return isAiDoctorEnabled;
	}

	public void setIsAiDoctorEnabled(Character isAiDoctorEnabled) {
		this.isAiDoctorEnabled = isAiDoctorEnabled;
	}
}
