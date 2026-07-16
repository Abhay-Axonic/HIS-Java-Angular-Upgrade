package com.param.global.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.model.RegistrationFieldConfiguration;
import com.param.billing.global.transaction.config.model.UnitApplicationBillingConfiguration;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ApplicationConfigurationDto;
import com.param.global.dto.RegistrationFieldConfigurationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitApplicationBillingConfigurationDaoImpl extends GenericDao<UnitApplicationBillingConfiguration, Integer> implements IUnitApplicationBillingConfigurationDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(UnitApplicationBillingConfigurationDaoImpl.class);

	public UnitApplicationBillingConfigurationDaoImpl() {
		super(UnitApplicationBillingConfiguration.class);
		
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	@Override
	public Response getUnitApplicationBillingConfigByOrgUnit(
			UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		try {
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEFAULT_SELF_TARIFF_FROM_APP_BILLING_CONF")
					.setParameter("unitId", unitApplicationConfigurationDto.getUnitId())
					.setParameter("organizationId", unitApplicationConfigurationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class))
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, unitApplicationConfigurationDtos);
	}catch (HibernateException he) {
			logger.error("getUnitApplicationBillingConfigByOrgUnit failed", he);
			throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getUnitApplicationBillingConfigByOrgUnit failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response saveUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		try {
			
			UnitApplicationBillingConfiguration unitApplicationBillingConfiguration=null;
				if(unitApplicationConfigurationDto.getUnitApplicationBillingConfigurationId()!=null)
				{
					unitApplicationBillingConfiguration = (UnitApplicationBillingConfiguration) sessionFactory.getCurrentSession().get(UnitApplicationBillingConfiguration.class, unitApplicationConfigurationDto.getUnitApplicationBillingConfigurationId());
				}else{
					unitApplicationBillingConfiguration = new UnitApplicationBillingConfiguration();
				}
				    unitApplicationBillingConfiguration.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
					unitApplicationBillingConfiguration.setIsAppointmentRequired(unitApplicationConfigurationDto.getIsAppointmentRequired());
					unitApplicationBillingConfiguration.setIsPreOrPostBilling(unitApplicationConfigurationDto.getIsPreOrPostBilling());
					unitApplicationBillingConfiguration.setIsCounterApplicable(unitApplicationConfigurationDto.getIsCounterApplicable());
					unitApplicationBillingConfiguration.setRadiologySpecialityId(unitApplicationConfigurationDto.getRadiologySpecialityId());
					unitApplicationBillingConfiguration.setPatientCategoryId(unitApplicationConfigurationDto.getPatientCategoryId());
					unitApplicationBillingConfiguration.setDefaultPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
					unitApplicationBillingConfiguration.setOrganizationId(unitApplicationConfigurationDto.getOrganizationId());
					unitApplicationBillingConfiguration.setUnitId(unitApplicationConfigurationDto.getUnitId());
					unitApplicationBillingConfiguration.setStatus(unitApplicationConfigurationDto.getStatus());
					unitApplicationBillingConfiguration.setCreatedBy(unitApplicationConfigurationDto.getCreatedBy());
					unitApplicationBillingConfiguration.setCreatedDate(GlobalCommonDateUtils.getDate(unitApplicationConfigurationDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					unitApplicationBillingConfiguration.setUpdatedBy(unitApplicationConfigurationDto.getUpdatedBy());
					unitApplicationBillingConfiguration.setUpdatedDate(GlobalCommonDateUtils.getDate(unitApplicationConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					unitApplicationBillingConfiguration.setDocRoleId(unitApplicationConfigurationDto.getDocRoleId());
					unitApplicationBillingConfiguration.setNurseRoleId(unitApplicationConfigurationDto.getNurseRoleId());
					unitApplicationBillingConfiguration.setStaffRoleId(unitApplicationConfigurationDto.getStaffRoleId());
					unitApplicationBillingConfiguration.setAdminRoleId(unitApplicationConfigurationDto.getAdminRoleId());
					unitApplicationBillingConfiguration.setIsServiceWiseSplitBill(unitApplicationConfigurationDto.getIsServiceWiseSplitBill());
					unitApplicationBillingConfiguration.setPdfGenerationUrl(unitApplicationConfigurationDto.getPdfGenerationUrl());
					unitApplicationBillingConfiguration.setPatientRegSerId(unitApplicationConfigurationDto.getPatientRegSerId());
					unitApplicationBillingConfiguration.setOpdBillingCatId(unitApplicationConfigurationDto.getOpdBillingCatId());
					unitApplicationBillingConfiguration.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
					unitApplicationBillingConfiguration.setConsultationSpecialityId(unitApplicationConfigurationDto.getConsultationSpecialityId());
					unitApplicationBillingConfiguration.setConsultationSubSpecialityId(unitApplicationConfigurationDto.getConsultationSubSpecialityId());
					unitApplicationBillingConfiguration.setSenderId(unitApplicationConfigurationDto.getSenderId());
					unitApplicationBillingConfiguration.setCasualtySpecialityId(unitApplicationConfigurationDto.getCasualtySpecialityId());
					unitApplicationBillingConfiguration.setPaediatricsSpecialityId(unitApplicationConfigurationDto.getPaediatricsSpecialityId());
					unitApplicationBillingConfiguration.setPharmacyServiceId(unitApplicationConfigurationDto.getPharmacyServiceId());
					unitApplicationBillingConfiguration.setCompanyPatientCategory(unitApplicationConfigurationDto.getCompanyPatientCategory());
					unitApplicationBillingConfiguration.setInsurancePatientCategory(unitApplicationConfigurationDto.getInsurancePatientCategory());
					unitApplicationBillingConfiguration.setIsKioskRequired(unitApplicationConfigurationDto.getIsKioskRequired());
					unitApplicationBillingConfiguration.setOtRentServiceId(unitApplicationConfigurationDto.getOtRentServiceId());
					unitApplicationBillingConfiguration.setSurgeonChargesServiceId(unitApplicationConfigurationDto.getSurgeonChargesServiceId());
					unitApplicationBillingConfiguration.setRadiologySpecialityId(unitApplicationConfigurationDto.getRadiologySpecialityId());
					unitApplicationBillingConfiguration.setPharmacyReturnSaleServiceId(unitApplicationConfigurationDto.getPharmacyReturnSaleServiceId());
					unitApplicationBillingConfiguration.setUnitApplicationBillingConfigurationId(unitApplicationConfigurationDto.getUnitApplicationBillingConfigurationId());
					unitApplicationBillingConfiguration.setSurchargeServiceId(unitApplicationConfigurationDto.getSurchargeServiceId());
					unitApplicationBillingConfiguration.setCompanyPatientCategory(unitApplicationConfigurationDto.getCompanyPatientCategory());
					unitApplicationBillingConfiguration.setInsurancePatientCategory(unitApplicationConfigurationDto.getInsurancePatientCategory());
					unitApplicationBillingConfiguration.setIsNursingAssessment(unitApplicationConfigurationDto.getIsNursingAssessment());
					unitApplicationBillingConfiguration.setErSpecialityId(unitApplicationConfigurationDto.getErSpecialityId());
					unitApplicationBillingConfiguration.setIsRenderedServiceRateEditable(unitApplicationConfigurationDto.getIsRenderedServiceRateEditable());
					unitApplicationBillingConfiguration.setAdmissionChargesServiceId(unitApplicationConfigurationDto.getAdmissionChargesServiceId());
					unitApplicationBillingConfiguration.setIsDigitalSignatureRequired(unitApplicationConfigurationDto.getIsDigitalSignatureRequired());
					unitApplicationBillingConfiguration.setIsPinPadRequired(unitApplicationConfigurationDto.getIsPinPadRequired());
					unitApplicationBillingConfiguration.setRegisteredPinPadDevice(unitApplicationConfigurationDto.getRegisteredPinPadDevice());
					unitApplicationBillingConfiguration.setIsSmartCardRequired(unitApplicationConfigurationDto.getIsSmartCardRequired());
					unitApplicationBillingConfiguration.setIsShowPatientByBed(unitApplicationConfigurationDto.getIsShowPatientByBed());
					unitApplicationBillingConfiguration.setIsPACSRequired(unitApplicationConfigurationDto.getIsPACSRequired());
					//Smart Card
					unitApplicationBillingConfiguration.setGroupPracticeNo(unitApplicationConfigurationDto.getGroupPracticeNo());
					unitApplicationBillingConfiguration.setGroupPracticeName(unitApplicationConfigurationDto.getGroupPracticeName());
					unitApplicationBillingConfiguration.setPracticeNo(unitApplicationConfigurationDto.getPracticeNo());
					unitApplicationBillingConfiguration.setStartDateFormat(unitApplicationConfigurationDto.getStartDateFormat());
					unitApplicationBillingConfiguration.setIsCorporateEmpVerificationReq(unitApplicationConfigurationDto.getIsCorporateEmpVerificationReq());
					unitApplicationBillingConfiguration.setIsAdviceTabVisible(unitApplicationConfigurationDto.getIsAdviceTabVisible());
					unitApplicationBillingConfiguration.setIsRateVisibleOnCoversheet(unitApplicationConfigurationDto.getIsRateVisibleOnCoversheet());
					unitApplicationBillingConfiguration.setObgynSpecialityId(unitApplicationConfigurationDto.getObgynSpecialityId());
					unitApplicationBillingConfiguration.setHemodialysisSpecialityId(unitApplicationConfigurationDto.getHemodialysisSpecialityId());
					unitApplicationBillingConfiguration.setIsConsolidatedBill(unitApplicationConfigurationDto.getIsConsolidatedBill());
					unitApplicationBillingConfiguration.setAssetMaintenanceSpecialityId(unitApplicationConfigurationDto.getAssetMaintenanceSpecialityId());
					unitApplicationBillingConfiguration.setPhysiotherapySpecialityId(unitApplicationConfigurationDto.getPhysiotherapySpecialityId());
					//unitApplicationBillingConfiguration.setIsProcessShareAgainstSettledBill(unitApplicationConfigurationDto.getIsProcessShareAgainstSettledBill());
					unitApplicationBillingConfiguration.setAdditionalOtrentService(unitApplicationConfigurationDto.getAdditionalOtrentService());
					unitApplicationBillingConfiguration.setSkipRISReportEntry(unitApplicationConfigurationDto.getSkipRISReportEntry());
					unitApplicationBillingConfiguration.setIsSendMailOnMailPrescription(unitApplicationConfigurationDto.getIsSendMailOnMailPrescription());
					unitApplicationBillingConfiguration.setIsVitalQualifierVisible(unitApplicationConfigurationDto.getIsVitalQualifierVisible());
					unitApplicationBillingConfiguration.setIsAdvisedDateVisible(unitApplicationConfigurationDto.getIsAdvisedDateVisible());
					unitApplicationBillingConfiguration.setIsKioskAnnouncementRequired(unitApplicationConfigurationDto.getIsKioskAnnouncementRequired());
					unitApplicationBillingConfiguration.setVaccinationChartHide(unitApplicationConfigurationDto.getVaccinationChartHide());
					unitApplicationBillingConfiguration.setBccEmailId(unitApplicationConfigurationDto.getBccEmailId()!=null ?unitApplicationConfigurationDto.getBccEmailId():null);
					unitApplicationBillingConfiguration.setWhatsAppOption(unitApplicationConfigurationDto.getWhatsAppOption() != null ? unitApplicationConfigurationDto.getWhatsAppOption() : null);
	        		unitApplicationBillingConfiguration.setSelfPaidConsultAlert(unitApplicationConfigurationDto.isSelfPaidConsultAlert());
	        		unitApplicationBillingConfiguration.setServiceWiseDoctorListReq(unitApplicationConfigurationDto.getServiceWiseDoctorListReq());
	        		unitApplicationBillingConfiguration.setGradeWiseSeviceApplicable(unitApplicationConfigurationDto.getGradeWiseSeviceApplicable());
	        		unitApplicationBillingConfiguration.setIsEmrRequired(unitApplicationConfigurationDto.getIsEmrRequired());

	        		
					if(unitApplicationConfigurationDto.getMorningFromTime()!=null) {

						LocalTime startOutputTime1 = LocalTime.parse(unitApplicationConfigurationDto.getMorningFromTime());
						LocalDate today = LocalDate.now();
						unitApplicationBillingConfiguration.setMorningFromTime(startOutputTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant());
					}
					if(unitApplicationConfigurationDto.getMorningToTime()!=null) {
						LocalTime time = LocalTime.parse(unitApplicationConfigurationDto.getMorningToTime());
						LocalDate today = LocalDate.now();

						unitApplicationBillingConfiguration.setMorningToTime(time.atDate(today).atZone(ZoneId.of("UTC")).toInstant());
					}
					if(unitApplicationConfigurationDto.getEveningFromTime()!=null) {
						LocalTime time = LocalTime.parse(unitApplicationConfigurationDto.getEveningFromTime());
						LocalDate today = LocalDate.now();
						unitApplicationBillingConfiguration.setEveningFromTime(time.atDate(today).atZone(ZoneId.of("UTC")).toInstant());
					}
					if(unitApplicationConfigurationDto.getEveningToTime()!=null) {
						LocalTime time = LocalTime.parse(unitApplicationConfigurationDto.getEveningToTime());
						LocalDate today = LocalDate.now();

						unitApplicationBillingConfiguration.setEveningToTime(time.atDate(today).atZone(ZoneId.of("UTC")).toInstant());
					}
					unitApplicationBillingConfiguration.setBadDebtsDays(unitApplicationConfigurationDto.getBadDebtsDays());
					unitApplicationBillingConfiguration.setIsSchemeDiscount(unitApplicationConfigurationDto.getIsSchemeDiscount());
					unitApplicationBillingConfiguration.setIsBackDatedAdmission(unitApplicationConfigurationDto.getIsBackDatedAdmission());
					unitApplicationBillingConfiguration.setPddDays(unitApplicationConfigurationDto.getPddDays());
					unitApplicationBillingConfiguration.setSkipReadyForBilling(unitApplicationConfigurationDto.getSkipReadyForBilling());
					unitApplicationBillingConfiguration.setSkipAdmissionAcceptance(unitApplicationConfigurationDto.getSkipAdmissionAcceptance());
					unitApplicationBillingConfiguration.setSkipHousekeeping(unitApplicationConfigurationDto.getSkipHousekeeping());
					unitApplicationBillingConfiguration.setIsDocConsultReq(unitApplicationConfigurationDto.getIsDocConsultReq());
					unitApplicationBillingConfiguration.setIsRadiologyReq(unitApplicationConfigurationDto.getIsRadiologyReq());
					unitApplicationBillingConfiguration.setIsEHCReq(unitApplicationConfigurationDto.getIsEHCReq());
					unitApplicationBillingConfiguration.setIsLabReq(unitApplicationConfigurationDto.getIsLabReq());
					unitApplicationBillingConfiguration.setIsHospitalVisitReq(unitApplicationConfigurationDto.getIsHospitalVisitReq());
					unitApplicationBillingConfiguration.setIsFreeVisitReq(unitApplicationConfigurationDto.getIsFreeVisitReq());
					unitApplicationBillingConfiguration.setIsReviewAppReq(unitApplicationConfigurationDto.getIsReviewAppReq());
					unitApplicationBillingConfiguration.setIsGstVisible(unitApplicationConfigurationDto.getIsGstVisible());
					unitApplicationBillingConfiguration.setOptimizeTransferFlow(unitApplicationConfigurationDto.getOptimizeTransferFlow());
					unitApplicationBillingConfiguration.setAllowOtherDoctorToConsult(unitApplicationConfigurationDto.getAllowOtherDoctorToConsult());
					unitApplicationBillingConfiguration.setDiagnosisRequire(unitApplicationConfigurationDto.getDiagnosisRequire());
					unitApplicationBillingConfiguration.setDefaultDiscountOwnership(unitApplicationConfigurationDto.getDefaultDiscountOwnership());
					unitApplicationBillingConfiguration.setKioskLandingScreen(unitApplicationConfigurationDto.getKioskLandingScreen());
					unitApplicationBillingConfiguration.setDoctorSlipTDS(unitApplicationConfigurationDto.getDoctorSlipTDS());
					unitApplicationBillingConfiguration.setBiomedicalsChargesServiceId(unitApplicationConfigurationDto.getBiomedicalsChargesServiceId());
					unitApplicationBillingConfiguration.setMrdChargesServiceId(unitApplicationConfigurationDto.getMrdChargesServiceId());
					unitApplicationBillingConfiguration.setHospitalChargesServiceId(unitApplicationConfigurationDto.getHospitalChargesServiceId());
					unitApplicationBillingConfiguration.setSkipTransferAcceptance(unitApplicationConfigurationDto.getSkipTransferAcceptance());
					unitApplicationBillingConfiguration.setId(unitApplicationConfigurationDto.getEmailId()!=null ? unitApplicationConfigurationDto.getEmailId(): null);
					unitApplicationBillingConfiguration.setPassword(unitApplicationConfigurationDto.getPassword()!=null ? unitApplicationConfigurationDto.getPassword(): null);
				    unitApplicationBillingConfiguration.setIsPaymentGatewayReqForWebApp(unitApplicationConfigurationDto.getIsPaymentGatewayReqForWebApp()!=null ? unitApplicationConfigurationDto.getIsPaymentGatewayReqForWebApp():'N');
				    unitApplicationBillingConfiguration.setIsRisPatientDetailsOnFirstPage(unitApplicationConfigurationDto.getIsRisPatientDetailsOnFirstPage()!=null ? unitApplicationConfigurationDto.getIsRisPatientDetailsOnFirstPage():'N');
				    unitApplicationBillingConfiguration.setRazorPayKeyId(unitApplicationConfigurationDto.getRazorPayKeyId());
				    unitApplicationBillingConfiguration.setRazorPaySecretKey(unitApplicationConfigurationDto.getRazorPaySecretKey());
				    unitApplicationBillingConfiguration.setIsReportAddIntoDischargeSummary(unitApplicationConfigurationDto.getIsReportAddIntoDischargeSummary());
				    unitApplicationBillingConfiguration.setAdmissionCancellationTime(unitApplicationConfigurationDto.getAdmissionCancellationTime()!=null?unitApplicationConfigurationDto.getAdmissionCancellationTime():null);
				    unitApplicationBillingConfiguration.setIsPackageApplicableDateEditable(unitApplicationConfigurationDto.getIsPackageApplicableDateEditable());
				    unitApplicationBillingConfiguration.setWebAppUserId(unitApplicationConfigurationDto.getWebAppUserId());
				    unitApplicationBillingConfiguration.setMailProviderId(unitApplicationConfigurationDto.getMailProviderId() !=null? unitApplicationConfigurationDto.getMailProviderId():1);
				    unitApplicationBillingConfiguration.setMailCredential(unitApplicationConfigurationDto.getMailCredential());
				    unitApplicationBillingConfiguration.setCashAmountValidation(unitApplicationConfigurationDto.getCashAmountValidation()!= null ? unitApplicationConfigurationDto.getCashAmountValidation():null);
				    unitApplicationBillingConfiguration.setCashAmountLimit(unitApplicationConfigurationDto.getCashAmountLimit()!= null ? unitApplicationConfigurationDto.getCashAmountLimit():null);
				    unitApplicationBillingConfiguration.setBlockReportPrint(unitApplicationConfigurationDto.getBlockReportPrint());
				    unitApplicationBillingConfiguration.setPayGatewayProviderId(unitApplicationConfigurationDto.getPayGatewayProviderId() != null ? unitApplicationConfigurationDto.getPayGatewayProviderId() : null);
				    unitApplicationBillingConfiguration.setEmailValidationRequired(unitApplicationConfigurationDto.getEmailValidationRequired() !=null ? unitApplicationConfigurationDto.getEmailValidationRequired() : 'N');
				    unitApplicationBillingConfiguration.setDateFormat(unitApplicationConfigurationDto.getDateFormat());
				    unitApplicationBillingConfiguration.setDepositTypeWiseRequired(unitApplicationConfigurationDto.getDepositTypeWiseRequired()!= null ?unitApplicationConfigurationDto.getDepositTypeWiseRequired() : false );
				    unitApplicationBillingConfiguration.setPdfGenerationUrlMis(unitApplicationConfigurationDto.getPdfGenerationUrlMis());
				    unitApplicationBillingConfiguration.setCimsOption(unitApplicationConfigurationDto.getCimsOption() !=null ? unitApplicationConfigurationDto.getCimsOption() : 'N');
				    unitApplicationBillingConfiguration.setBackDateAdmissionDays(unitApplicationConfigurationDto.getBackDateAdmissionDays()!= null ? unitApplicationConfigurationDto.getBackDateAdmissionDays(): 2);
				    unitApplicationBillingConfiguration.setAppointmentUniqueIdSearch(unitApplicationConfigurationDto.getAppointmentUniqueIdSearch());
				    unitApplicationBillingConfiguration.setCouponSystemApplicable(unitApplicationConfigurationDto.getCouponSystemApplicable());
				    unitApplicationBillingConfiguration.setTokenNumber(unitApplicationConfigurationDto.getTokenNumber());
				    unitApplicationBillingConfiguration.setMobileValidationRequired(unitApplicationConfigurationDto.getMobileValidationRequired());
				    unitApplicationBillingConfiguration.setMandatoryScan(unitApplicationConfigurationDto.getMandatoryScan());
				    unitApplicationBillingConfiguration.setExceededBillAmountSettlement(unitApplicationConfigurationDto.getExceededBillAmountSettlement());
                    unitApplicationBillingConfiguration.setRunTimeRateChangeUsingTarrif(unitApplicationConfigurationDto.getRunTimeRateChangeUsingTarrif());
				    unitApplicationBillingConfiguration.setQmsPrnPrint(unitApplicationConfigurationDto.getQmsPrnPrint());
				    unitApplicationBillingConfiguration.setDischargeSummaryDataViewOptionId(unitApplicationConfigurationDto.getDischargeSummaryDataViewOptionId());
				    unitApplicationBillingConfiguration.setAmbulanceChargesServiceId(unitApplicationConfigurationDto.getAmbulanceChargesServiceId());
				    unitApplicationBillingConfiguration.setGstForBillingBedCategory(unitApplicationConfigurationDto.getGstForBillingBedCategory());
				    unitApplicationBillingConfiguration.setRoa(unitApplicationConfigurationDto.getRoa());
				    unitApplicationBillingConfiguration.setDosage(unitApplicationConfigurationDto.getDosage());
				    unitApplicationBillingConfiguration.setLocalStorageBucketJson(unitApplicationConfigurationDto.getLocalStorageJson());
				    unitApplicationBillingConfiguration.setBucketFlag(unitApplicationConfigurationDto.getBucketFlag());
				    unitApplicationBillingConfiguration.setSmsType(unitApplicationConfigurationDto.getSmsType());
				    unitApplicationBillingConfiguration.setSmsTypejson(unitApplicationConfigurationDto.getSmsTypejson());
				    unitApplicationBillingConfiguration.setIsBillManipulationAllowed(unitApplicationConfigurationDto.getIsBillManipulationAllowed());
				    unitApplicationBillingConfiguration.setInvoiceCreationInsurance(unitApplicationConfigurationDto.getInvoiceCreationInsurance());
				    unitApplicationBillingConfiguration.setInvoiceCreationCorporate(unitApplicationConfigurationDto.getInvoiceCreationCorporate());
			        unitApplicationBillingConfiguration.setIspdfPasswordRequire(unitApplicationConfigurationDto.getIspdfPasswordRequire());
					unitApplicationBillingConfiguration.setTallyIp(unitApplicationConfigurationDto.getTallyIp() != null ? unitApplicationConfigurationDto.getTallyIp() : "http://127.0.0.1");
					unitApplicationBillingConfiguration.setTallyPortNo(unitApplicationConfigurationDto.getTallyPortNo() != null ? unitApplicationConfigurationDto.getTallyPortNo() : "9000");
					unitApplicationBillingConfiguration.setIsTallyLocalHost(unitApplicationConfigurationDto.getIsTallyLocalHost() != null ? unitApplicationConfigurationDto.getIsTallyLocalHost() : 'Y');
					unitApplicationBillingConfiguration.setAdminServiceMasterId(unitApplicationConfigurationDto.getAdminServiceMasterId());
					unitApplicationBillingConfiguration.setOtherOrgLogoRequired(unitApplicationConfigurationDto.getOtherOrgLogoRequired() != null ? unitApplicationConfigurationDto.getOtherOrgLogoRequired() : 'N');
					unitApplicationBillingConfiguration.setIsAiDoctorEnabled(unitApplicationConfigurationDto.getIsAiDoctorEnabled() != null ? unitApplicationConfigurationDto.getIsAiDoctorEnabled() : 'N');
					unitApplicationBillingConfiguration.setMaxInputAmount(unitApplicationConfigurationDto.getMaxInputAmount());
				    sessionFactory.getCurrentSession().saveOrUpdate(unitApplicationBillingConfiguration);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitApplicationBillingConfiguration);
	}catch (HibernateException he) {
		logger.error("saveUnitApplicationBillingConfig failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("saveUnitApplicationBillingConfig failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response updateUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		
		try{	
			if(unitApplicationConfigurationDto.getUnitApplicationBillingConfigurationId()!= null){
			 sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_UNIT_APPLICATION_BILLING_CONFIGURATION_MAPPER")
					.setCharacter("status", 'I')
					.setParameter("organizationId", unitApplicationConfigurationDto.getOrganizationId())
					.setParameter("unitId", unitApplicationConfigurationDto.getUnitId())
					.setParameter("unitApplicationBillingConfigurationId", unitApplicationConfigurationDto.getUnitApplicationBillingConfigurationId())
					.executeUpdate();
				}
			
					UnitApplicationBillingConfiguration unitApplicationBillingConfiguration=new UnitApplicationBillingConfiguration();
						unitApplicationBillingConfiguration.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
						unitApplicationBillingConfiguration.setIsAppointmentRequired(unitApplicationConfigurationDto.getIsAppointmentRequired());
						unitApplicationBillingConfiguration.setIsCounterApplicable(unitApplicationConfigurationDto.getIsCounterApplicable());
						unitApplicationBillingConfiguration.setIsPreOrPostBilling(unitApplicationConfigurationDto.getIsPreOrPostBilling());
						unitApplicationBillingConfiguration.setRadiologySpecialityId(unitApplicationConfigurationDto.getRadiologySpecialityId());
						unitApplicationBillingConfiguration.setPatientCategoryId(unitApplicationConfigurationDto.getPatientCategoryId());
						unitApplicationBillingConfiguration.setDefaultPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
						unitApplicationBillingConfiguration.setOrganizationId(unitApplicationConfigurationDto.getOrganizationId());
						unitApplicationBillingConfiguration.setUnitId(unitApplicationConfigurationDto.getUnitId());
						unitApplicationBillingConfiguration.setStatus('A');
						unitApplicationBillingConfiguration.setCreatedBy(unitApplicationConfigurationDto.getCreatedBy());
						unitApplicationBillingConfiguration.setCreatedDate(GlobalCommonDateUtils.getDate(unitApplicationConfigurationDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
						unitApplicationBillingConfiguration.setUpdatedBy(unitApplicationConfigurationDto.getUpdatedBy());
						unitApplicationBillingConfiguration.setUpdatedDate(GlobalCommonDateUtils.getDate(unitApplicationConfigurationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
						unitApplicationBillingConfiguration.setDocRoleId(unitApplicationConfigurationDto.getDocRoleId());
						unitApplicationBillingConfiguration.setNurseRoleId(unitApplicationConfigurationDto.getNurseRoleId());
						unitApplicationBillingConfiguration.setStaffRoleId(unitApplicationConfigurationDto.getStaffRoleId());
						unitApplicationBillingConfiguration.setAdminRoleId(unitApplicationConfigurationDto.getAdminRoleId());
						unitApplicationBillingConfiguration.setIsServiceWiseSplitBill(unitApplicationConfigurationDto.getIsServiceWiseSplitBill());
						unitApplicationBillingConfiguration.setPdfGenerationUrl(unitApplicationConfigurationDto.getPdfGenerationUrl());
						unitApplicationBillingConfiguration.setPatientRegSerId(unitApplicationConfigurationDto.getPatientRegSerId());
						unitApplicationBillingConfiguration.setOpdBillingCatId(unitApplicationConfigurationDto.getOpdBillingCatId());
						unitApplicationBillingConfiguration.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
						unitApplicationBillingConfiguration.setConsultationSpecialityId(unitApplicationConfigurationDto.getConsultationSpecialityId());
						unitApplicationBillingConfiguration.setConsultationSubSpecialityId(unitApplicationConfigurationDto.getConsultationSubSpecialityId());
						unitApplicationBillingConfiguration.setSenderId(unitApplicationConfigurationDto.getSenderId());
						unitApplicationBillingConfiguration.setCasualtySpecialityId(unitApplicationConfigurationDto.getCasualtySpecialityId());
						unitApplicationBillingConfiguration.setPaediatricsSpecialityId(unitApplicationConfigurationDto.getPaediatricsSpecialityId());
						unitApplicationBillingConfiguration.setPharmacyServiceId(unitApplicationConfigurationDto.getPharmacyServiceId());
						unitApplicationBillingConfiguration.setCompanyPatientCategory(unitApplicationConfigurationDto.getCompanyPatientCategory());
						unitApplicationBillingConfiguration.setInsurancePatientCategory(unitApplicationConfigurationDto.getInsurancePatientCategory());
						unitApplicationBillingConfiguration.setPharmacyReturnSaleServiceId(unitApplicationConfigurationDto.getPharmacyReturnSaleServiceId());
						unitApplicationBillingConfiguration.setIsKioskRequired(unitApplicationConfigurationDto.getIsKioskRequired());
						unitApplicationBillingConfiguration.setOtRentServiceId(unitApplicationConfigurationDto.getOtRentServiceId());
						unitApplicationBillingConfiguration.setSurgeonChargesServiceId(unitApplicationConfigurationDto.getSurgeonChargesServiceId());
						unitApplicationBillingConfiguration.setAdditionalOtrentService(unitApplicationConfigurationDto.getAdditionalOtrentService());
						
						//Smart Card
						unitApplicationBillingConfiguration.setGroupPracticeNo(unitApplicationConfigurationDto.getGroupPracticeNo());
						unitApplicationBillingConfiguration.setGroupPracticeName(unitApplicationConfigurationDto.getGroupPracticeName());
						unitApplicationBillingConfiguration.setPracticeNo(unitApplicationConfigurationDto.getPracticeNo());
						unitApplicationBillingConfiguration.setStartDateFormat(unitApplicationConfigurationDto.getStartDateFormat());
						
						unitApplicationBillingConfiguration.setIsConsolidatedBill(unitApplicationConfigurationDto.getIsConsolidatedBill());
						unitApplicationBillingConfiguration.setIsSchemeDiscount(unitApplicationConfigurationDto.getIsSchemeDiscount());
						unitApplicationBillingConfiguration.setIsBackDatedAdmission(unitApplicationConfigurationDto.getIsBackDatedAdmission());
						unitApplicationBillingConfiguration.setSkipReadyForBilling(unitApplicationConfigurationDto.getSkipReadyForBilling());
						unitApplicationBillingConfiguration.setSkipAdmissionAcceptance(unitApplicationConfigurationDto.getSkipAdmissionAcceptance());
						unitApplicationBillingConfiguration.setSkipHousekeeping(unitApplicationConfigurationDto.getSkipHousekeeping());
						unitApplicationBillingConfiguration.setIsGstVisible(unitApplicationConfigurationDto.getIsGstVisible());
						unitApplicationBillingConfiguration.setOptimizeTransferFlow(unitApplicationConfigurationDto.getOptimizeTransferFlow());
						unitApplicationBillingConfiguration.setAllowOtherDoctorToConsult(unitApplicationConfigurationDto.getAllowOtherDoctorToConsult());
						unitApplicationBillingConfiguration.setDiagnosisRequire(unitApplicationConfigurationDto.getDiagnosisRequire());
						unitApplicationBillingConfiguration.setDefaultDiscountOwnership(unitApplicationConfigurationDto.getDefaultDiscountOwnership());
						unitApplicationBillingConfiguration.setIsSendMailOnMailPrescription(unitApplicationConfigurationDto.getIsSendMailOnMailPrescription());
						unitApplicationBillingConfiguration.setKioskLandingScreen(unitApplicationConfigurationDto.getKioskLandingScreen());
						unitApplicationBillingConfiguration.setDoctorSlipTDS(unitApplicationConfigurationDto.getDoctorSlipTDS());
						unitApplicationBillingConfiguration.setBiomedicalsChargesServiceId(unitApplicationConfigurationDto.getBiomedicalsChargesServiceId());
						unitApplicationBillingConfiguration.setMrdChargesServiceId(unitApplicationConfigurationDto.getMrdChargesServiceId());
						unitApplicationBillingConfiguration.setHospitalChargesServiceId(unitApplicationConfigurationDto.getHospitalChargesServiceId());
						unitApplicationBillingConfiguration.setSkipTransferAcceptance(unitApplicationConfigurationDto.getSkipTransferAcceptance());
						unitApplicationBillingConfiguration.setRegisteredPinPadDevice(unitApplicationConfigurationDto.getRegisteredPinPadDevice());
						unitApplicationBillingConfiguration.setWhatsAppOption(unitApplicationConfigurationDto.getWhatsAppOption() != null ? unitApplicationConfigurationDto.getWhatsAppOption() : null);
						unitApplicationBillingConfiguration.setSelfPaidConsultAlert(unitApplicationConfigurationDto.isSelfPaidConsultAlert());
		        		unitApplicationBillingConfiguration.setServiceWiseDoctorListReq(unitApplicationConfigurationDto.getServiceWiseDoctorListReq());
		        		unitApplicationBillingConfiguration.setGradeWiseSeviceApplicable(unitApplicationConfigurationDto.getGradeWiseSeviceApplicable());
		        		unitApplicationBillingConfiguration.setPdfGenerationUrlMis(unitApplicationConfigurationDto.getPdfGenerationUrlMis());
						unitApplicationBillingConfiguration.setCimsOption(unitApplicationConfigurationDto.getCimsOption() !=null ? unitApplicationConfigurationDto.getCimsOption() : 'N');
						unitApplicationBillingConfiguration.setDischargeSummaryDataViewOptionId(unitApplicationConfigurationDto.getDischargeSummaryDataViewOptionId());
						unitApplicationBillingConfiguration.setRoa(unitApplicationConfigurationDto.getRoa());
						unitApplicationBillingConfiguration.setDosage(unitApplicationConfigurationDto.getDosage());
						unitApplicationBillingConfiguration.setTallyIp(unitApplicationConfigurationDto.getTallyIp() != null ? unitApplicationConfigurationDto.getTallyIp() : "http://127.0.0.1");
						unitApplicationBillingConfiguration.setTallyPortNo(unitApplicationConfigurationDto.getTallyPortNo() != null ? unitApplicationConfigurationDto.getTallyPortNo() : "9000");
						unitApplicationBillingConfiguration.setIsTallyLocalHost(unitApplicationConfigurationDto.getIsTallyLocalHost() != null ? unitApplicationConfigurationDto.getIsTallyLocalHost() : 'Y');
                        unitApplicationBillingConfiguration.setAdminServiceMasterId(unitApplicationConfigurationDto.getAdminServiceMasterId());
						unitApplicationBillingConfiguration.setOtherOrgLogoRequired(unitApplicationConfigurationDto.getOtherOrgLogoRequired() != null ? unitApplicationConfigurationDto.getOtherOrgLogoRequired() : 'N');
						unitApplicationBillingConfiguration.setIsAiDoctorEnabled(unitApplicationConfigurationDto.getIsAiDoctorEnabled() != null ? unitApplicationConfigurationDto.getIsAiDoctorEnabled() : 'N');

                        save(unitApplicationBillingConfiguration);
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, unitApplicationBillingConfiguration);


				}catch (HibernateException he) {
					logger.error("updateUnitApplicationBillingConfig failed", he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error("updateUnitApplicationBillingConfig failed", e);
					throw new ServiceException(e.getMessage());
				}
		}



	

	@Override
	public Response updateUnitApplicationBillingConfigStatus(UnitApplicationConfigurationDto unitApplicationConfigurationDto) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("update UnitApplicationBillingConfiguration set status='I' where organizationId=:orgId AND unitId=:unitId")
			.setParameter("orgId",unitApplicationConfigurationDto.getOrganizationId())
			.setParameter("unitId", unitApplicationConfigurationDto.getUnitId())
			.executeUpdate();
		
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}catch (HibernateException he) {
				logger.error("updateUnitApplicationBillingConfigStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateUnitApplicationBillingConfigStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
			}

	@Override
	public Response getUnitApplicationBillingConfigList(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		try{
			List<UnitApplicationConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_UNIT_APPLICATION_BILLING_CONFIGURATION")
					.setParameter("organizationId", unitApplicationConfigurationDto.getOrganizationId())
					.setParameter("unitId", unitApplicationConfigurationDto.getUnitId())
					.setFirstResult(unitApplicationConfigurationDto.getOffset() != null ? unitApplicationConfigurationDto.getOffset() : 0)
					.setMaxResults(unitApplicationConfigurationDto.getNoOfRecordsPerPage() != null ? unitApplicationConfigurationDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getUnitApplicationBillingConfigList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getUnitApplicationBillingConfigCount(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(UnitApplicationBillingConfiguration.class);
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error("getUnitApplicationBillingConfigCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public String getURLForPdfGeneration(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<UnitApplicationConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_URL_FOR_PDF_GENERATION")
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
				    .setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();
			
		return list.getFirst().getPdfGenerationUrl();

		} catch (HibernateException he) {
			logger.error("getURLForPdfGeneration failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getURLForPdfGeneration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public String getURLForPdfGenerationMis(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<UnitApplicationConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_URL_FOR_PDF_GENERATION_MIS")
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
				    .setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();
			
		return list.getFirst().getPdfGenerationUrlMis();

		} catch (HibernateException he) {
			logger.error("getURLForPdfGenerationMis failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getURLForPdfGenerationMis failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getOPDBillingBedCatagory(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<UnitApplicationConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPD_BILLING_BED_CATAGORY")
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
				    .setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();

		return  new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getOPDBillingBedCatagory failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getOPDBillingBedCatagory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getSelfPayeeList(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<UnitApplicationConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SELF_PAYEE_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
				    .setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();

		return  new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getSelfPayeeList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getSelfPayeeList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveUnitApplicationRegistrationFieldConfig(
			RegistrationFieldConfigurationDto dto) throws ApplicationException {
		try{
			
			RegistrationFieldConfiguration model=new RegistrationFieldConfiguration();
			model.setBirthDate(dto.getBirthDate());
			model.setCallingCode(dto.getCallingCode());
			model.setCodeDigit(dto.getCodeDigit());
			model.setFirstName(dto.getFirstName());
			model.setMiddleName(dto.getMiddleName());
			model.setLastName(dto.getLastName());
			model.setMaritalStatus(dto.getMaritalStatus());
			model.setMobileDigit(dto.getMobileDigit());
			model.setMobileNo(dto.getMobileNo());
			model.setNationality(dto.getNationality());
			model.setOccupation(dto.getOccupation());
			model.setIdentificationDoc(dto.getIdentificationDoc());
			model.setThumbImpression(dto.getThumbImpression());
			model.setPrefix(dto.getPrefix());
			model.setPhoto(dto.getPhoto());
			model.setPatientCategory(dto.getPatientCategory());
			model.setRace(dto.getRace());
			model.setSex(dto.getSex());
			model.setEmail(dto.getEmail());
			model.setCreatedBy(dto.getCreatedBy());
			model.setUpdatedBy(dto.getUpdatedBy());
			model.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			model.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			model.setOrganizationId(dto.getOrganizationId());
			model.setUnitId(dto.getUnitId());
			model.setAddress(dto.getAddress());
			model.setKinDetails(dto.getKinDetails());
			model.setSourceId(dto.getSourceId());
			model.setKinprefix(dto.getKinprefix());
			model.setKinName(dto.getKinName());
			model.setKinMobileNo(dto.getKinMobileNo());
			model.setRelationId(dto.getRelationId());
			model.setPhoneNo(dto.getPhoneNo());
			model.setIdentificationId(dto.getIdentificationId());
			model.setIdentificationNo(dto.getIdentificationNo());
			model.setKinAddress(dto.getKinAddress());
			model.setCountryId(dto.getCountryId());
			model.setStateId(dto.getStateId());
			model.setDistrictId(dto.getDistrictId());
			model.setCityId(dto.getCityId());
			model.setAreaId(dto.getAreaId());
			model.setPostCode(dto.getPostCode());
	
//			if(dto.getSaveUpdateFlag()=='Y'){
//				sessionFactory.getCurrentSession().save(model);
//			}else{
				model.setUnitApplRegistrationConfigId(dto.getUnitApplRegistrationConfigId());
				sessionFactory.getCurrentSession().saveOrUpdate(model);
//			}
				return  new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, model);
		} catch (HibernateException he) {
			logger.error("saveUnitApplicationRegistrationFieldConfig failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveUnitApplicationRegistrationFieldConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitApplicationRegistrationFieldConfig(Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			List<RegistrationFieldConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_APPLICATION_REGISTRATION_FIELD_CONFIG")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
				    .setResultTransformer(Transformers.aliasToBean(RegistrationFieldConfigurationDto.class)).list();

		return  new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getUnitApplicationRegistrationFieldConfig failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getUnitApplicationRegistrationFieldConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@Override
	public Response saveDefaultEntry(Integer orgId,Integer unitId,Integer selfPayeeId,Integer adminRoleId,Integer userId,Integer docRoleId, Integer nurseRoleId)
			throws ApplicationException {
		
		Connection connection =null;
		PreparedStatement statement = null;
		PreparedStatement statementPharmecy = null;
		PreparedStatement statementRegistartionFields = null;
		try {
				connection = driverManager.getConnection();
				connection.setAutoCommit(false);
			
				 statement = connection.prepareStatement("INSERT INTO public.t_unit_application_billing_configuration "
					+"( organization_id, unit_id, self_payee_id,admin_role_id,doc_role_id,nurse_role_id,bucket_flag,skip_ready_for_billing,skip_admission_acceptance,skip_housekeeping) "
					+"VALUES( ?, ?, ?, ?, ?, ?,1,'Y', 'Y', 'Y') ");
					statement.setInt(1, orgId);
	                statement.setInt(2, unitId);
	                statement.setInt(3, selfPayeeId);
	                statement.setInt(4, adminRoleId);
	                statement.setInt(5, docRoleId);
	                statement.setInt(6, nurseRoleId);
				int i = statement.executeUpdate();
				
				//For Pharmecy Start
				 statementPharmecy = connection.prepareStatement("INSERT INTO public.m_application_configuration "
				+"( organization_id, unit_id, item_name,issue_material_picker_required, is_active, updated_by,added_by,is_deleted, tax_inclusive, assign_picker_required, is_markup, markup_up, is_dept_pharmacy_required, is_pharmvalidation_op_required, is_assign_picker_op_required) "
				+"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " );
				statementPharmecy.setInt(1, orgId);
				statementPharmecy.setInt(2, unitId);
				statementPharmecy.setBoolean(3, false);
				statementPharmecy.setBoolean(4, false);
				statementPharmecy.setBoolean(5, false);
				statementPharmecy.setInt(6, userId);
				statementPharmecy.setInt(7, userId);
				statementPharmecy.setBoolean(8, false);
				statementPharmecy.setBoolean(9, false);
				statementPharmecy.setBoolean(10, false);
				statementPharmecy.setBoolean(11, false);
				statementPharmecy.setBoolean(12, false);
				statementPharmecy.setBoolean(13, false);
				statementPharmecy.setBoolean(14, false);
				statementPharmecy.setBoolean(15, false);
				
				
				int j = statementPharmecy.executeUpdate();
				// For Pharmecy End
				
				 statementRegistartionFields = connection.prepareStatement(" INSERT INTO public.t_unit_application_registration_configuration "
						+" (prefix, first_name, middle_name, last_name, birth_date, sex, calling_code, code_digit, mobile_no, mobile_digit, nationality, identification_doc, race, marital_status, occupation, patient_category, email, photo, thumb_impression, unit_id, organization_id, created_by, updated_by) "
						+" VALUES('Y', 'N', 'N', 'Y', 'Y', 'Y', 'N', NULL, 'Y', 10, 'N', 'N', 'N', 'N', 'N', 'Y', 'N', 'N', 'N', ?, ?, ?, ?)" );
				statementRegistartionFields.setInt(1, orgId);
				statementRegistartionFields.setInt(2, unitId);
				statementRegistartionFields.setInt(3, userId);
				statementRegistartionFields.setInt(4, userId);
				j = statementRegistartionFields.executeUpdate();
				
				connection.commit();  
			
		return  new Response(SUCCESS, SUCCESS_CODE, null, null, i);

		} catch (HibernateException he) {
			logger.error("saveDefaultEntry failed", he);
			throw new ServiceException(he.getMessage());
		} catch (BatchUpdateException e) {
			logger.error("saveDefaultEntry failed", e);
			throw new ServiceException(e.getMessage());
		}
			catch (Exception e) {
			logger.error("saveDefaultEntry failed", e);
			throw new ServiceException(e.getMessage());
		}finally{
			try {
				if (statement != null) {
					statement.close();
				}
				if (statementPharmecy != null) {
					statementPharmecy.close();
				}
				if (statementRegistartionFields != null) {
					statementRegistartionFields.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
				logger.error("saveDefaultEntry failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	}
	
	@Override
	public Integer getDefaultDiscountOwnership(Integer unitId) throws ApplicationException {
		try {
			Integer defaultDiscountOwnership = (Integer) sessionFactory.getCurrentSession()
					.createSQLQuery("select default_discount_ownership from t_unit_application_billing_configuration where unit_id ="+ unitId)
					.uniqueResult();
			return defaultDiscountOwnership != null ? defaultDiscountOwnership : 0;
		} catch (HibernateException he) {
			logger.error("getDefaultDiscountOwnership failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDefaultDiscountOwnership failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public String getURLForPdfGenerationForBI(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<UnitApplicationConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_URL_FOR_PDF_GENERATION_FOR_BI")
					.setParameter("orgId", organizationId)
//					.setParameter("unitId", unitId)
				    .setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();
			
		return list.getFirst().getPdfGenerationUrl();

		} catch (HibernateException he) {
			logger.error("getURLForPdfGenerationForBI failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getURLForPdfGenerationForBI failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getApplicationConfigurationByOrgUnit(ApplicationConfigurationDto applicationConfigurationDto) throws ApplicationException {
		try {
			
			String query = "select "
					+ " 	appn.is_pharmacological_validation_skip_ip as \"isPharmacologicalValidationSkipIp\" "
					+ " from " 
					+ "	m_application_configuration appn" 
					+ " where appn.organization_id =:orgId"
					+ " and appn.unit_id  =:unitId " 
					+ " and appn.is_active = true ";
			
			List<ApplicationConfigurationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",applicationConfigurationDto.getUnitId())
					.setParameter("orgId",applicationConfigurationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ApplicationConfigurationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getApplicationConfigurationByOrgUnit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String getPublicUrl(Integer orgUnitId) throws ApplicationException {
		try {
			
			String query = "select "
					+ " 	appn.public_url as \"publicUrl\" "
					+ " from " 
					+ "	m_application_configuration appn" 
					+ " where appn.unit_id  =:unitId " 
					+ " and appn.is_active = true ";
			
			List<ApplicationConfigurationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",orgUnitId)
					.setResultTransformer(Transformers.aliasToBean(ApplicationConfigurationDto.class)).list();
			
			if(list != null && list.size() == 1) {
				return list.getFirst().getPublicUrl();
			}else {
				return null;
			}

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPublicUrl failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSPrnFormat(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		try {
			
			String query = "select "
					+ " 	appn.qms_prn_print_format as \"qmsPrnPrintFormat\" "
					+ " from " 
					+ "	t_unit_application_billing_configuration appn" 
					+ " where  appn.unit_id  =:unitId " 
					+ " and appn.status = 'A' ";
			
			List<UnitApplicationConfigurationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",unitApplicationConfigurationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getQMSPrnFormat failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	}
