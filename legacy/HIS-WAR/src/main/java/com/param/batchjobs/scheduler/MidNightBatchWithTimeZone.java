package com.param.batchjobs.scheduler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.CSSD.service.IPreSterilizationProcessService;
import com.param.batchjobs.service.IBatchDao;
import com.param.batchjobs.service.IRoomRentBatchChargesService;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.service.ITDoctorShareService;
import com.param.data.dto.ExceptionDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.ITPackageConsumptionMasterDao;
import com.param.global.dao.ITallyIntegrationService;
import com.param.global.opd.service.IAppointmentSchedulingService;
import com.param.global.service.ICEODashboardService;
import com.param.global.service.IUserMasterService;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.coversheet.service.IPatientDiagnosisService;
import com.param.opd.coversheet.service.IVaccineImmunizationMapperService;
import com.param.opd.unit.service.ISlotMasterService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
public class MidNightBatchWithTimeZone {

	private static final Logger logger = LoggerFactory.getLogger(MidNightBatchWithTimeZone.class);

	@Autowired
	SessionService sessionService;

	@Autowired
	private IRoomRentBatchChargesService roomRentBatchChargesServ;

	@Autowired
	private ITallyIntegrationService iTallyIntegrationService;

	@Autowired
	IAppointmentSchedulingService iAppointmentSchedulingService;

	@Autowired
	IBatchDao batchDao;

	@Autowired
	IPatientDiagnosisService iPatientDiagnosisService;

	@Autowired
	ISmsUtilityConfigService iSmsUtilityConfigService;

	@Autowired
	IBillingMasterDao iBillingMasterDao;

	@Autowired
	IUserMasterService iUserMasterService;

	@Autowired
	ICEODashboardService iCEODashboardService;

	@Autowired
	ISlotMasterService iSlotMasterService;

	@Autowired
    IPreSterilizationProcessService iPreSterilizationProcessService;

	@Autowired
	ITPackageConsumptionMasterDao iTPackageConsumptionMasterDao;

	@Autowired
	IVaccineImmunizationMapperService ivaccineImmunizationMapperService;

	@Autowired
	private ITDoctorShareService iTDoctorShareService;

	static boolean lockBatch = true;

	static boolean lockRoomRent = true;

	static String claimLink = "https://data.smartapplicationsgroup.com:8087/sandbox/edi/claim";
	static String key = "IlAjdyf02tlM8ZRXN/jc2kK6oTnU6hK1dpzzPiloMNY09NR6ycuDrCgxOa40no8X";
	static String token = "d4vpH3VGhYxSlfxqUTNKBF4ke1MMDheDDQmlQEWzwIq";




	public void midnightBatch(String timeZone, List<Integer> unitIds){

		List<ExceptionDto> errorList = new ArrayList<ExceptionDto>();
		try{
			if(lockBatch == true && batchDao.isBatchExecuted(timeZone,unitIds)==true)
			{
				lockBatch = false;
				try{
						batchDao.addBathLog(timeZone,unitIds);
					}catch(Exception e){
						logger.error(e.getMessage(), e);
						errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","addBathLog",unitIds.toString()));

					}

					try{
						iSmsUtilityConfigService.sendBatchSMS(unitIds);
					}catch(Exception e){
						logger.error(e.getMessage(), e);
						errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","sendBatchSMS",unitIds.toString()));
					}

					try{
						iAppointmentSchedulingService.batchJobToUpdateMultiVisitPackage(unitIds);
					}catch(Exception e){
						logger.error(e.getMessage(), e);
						errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","batchJobToUpdateMultiVisitPackage",unitIds.toString()));
					}

					try{
						iTPackageConsumptionMasterDao.getExistingActiveMultivisitPackageForDeactivation(unitIds);
					}catch(Exception e){
						logger.error(e.getMessage(), e);
						errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","getExistingActiveMultivisitPackageForDeactivation",unitIds.toString()));
					}

				/*Tally posting*/
						try{
							iTallyIntegrationService.postVisitFaData(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postVisitFaData",unitIds.toString()));
						}
						try{
							iTallyIntegrationService.postDepositData(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postDepositData",unitIds.toString()));
						}
						try{
							iTallyIntegrationService.postDoctorShare(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postDoctorShare",unitIds.toString()));
						}
						try{
							iTallyIntegrationService.postCreditNotes(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postCreditNotes",unitIds.toString()));
						}
						try{
							iTallyIntegrationService.postAdvanceRefund(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postAdvanceRefund",unitIds.toString()));
						}
						try{
							iTallyIntegrationService.postCompanySettlement(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postCompanySettlement",unitIds.toString()));
						}

						try{
							iTallyIntegrationService.postGrnNotes(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postGrnNotes",unitIds.toString()));
						}

						try{
							iTallyIntegrationService.postSaleNotes(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postSaleNotes",unitIds.toString()));
						}

						try{
							iUserMasterService.updateSchedulingLog(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","updateSchedulingLog",unitIds.toString()));
						}

						try{
							iCEODashboardService.postDashboardCounts(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postDashboardCounts",unitIds.toString()));
						}

						try {
							iPreSterilizationProcessService.updateSterilizationStock(unitIds);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","updateSterilizationStock",unitIds.toString()));
						}

						try{
							iTallyIntegrationService.postCOGSPurchaseAndSale(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","postCOGSPurchaseAndSale",unitIds.toString()));
						}

				/*tally posting*/

						try {
							ivaccineImmunizationMapperService.batchJobToSendMailAndMessageForVaccinationDueDate(unitIds);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","batchJobToSendMailAndMessageForVaccinationDueDate",unitIds.toString()));

						}

						try {
							ivaccineImmunizationMapperService.batchJobToSendMessageForCycliceAppointmentDueDate(unitIds);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","batchJobToSendMessageForCycliceAppointmentDueDate",unitIds.toString()));
						}

						try {
							ivaccineImmunizationMapperService.dailyBasisPendingAmountIpPatientSMS(unitIds);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","dailyBasisPendingAmountIpPatientSMS",unitIds.toString()));

						}
						try{
							iTallyIntegrationService.inactiveTariffServiceBasedOnEffectiveDate();
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","inactiveTariffServiceBasedOnEffectiveDate",unitIds.toString()));
						}
						try{
							iTallyIntegrationService.insertDailyStock(unitIds);
							iTallyIntegrationService.callProcedureTallyMismatch();
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","insertDailyStock",unitIds.toString()));
						}
						try{
							iTallyIntegrationService.icuUtilization(unitIds);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","insertDailyStock",unitIds.toString()));
						}
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			errorList.add(genarateDto(e.getCause().toString(),"midnightBatch","isBatchExecuted",unitIds.toString()));
		}
		finally {
			lockBatch = true;
		}

		for (ExceptionDto exceptionDto : errorList) {
			try {
				batchDao.addExceptionLog(exceptionDto);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void postRoomRent(String timeZone, List<Integer> unitIds){
		List<ExceptionDto> errorList = new ArrayList<ExceptionDto>();
		try{

			if(lockRoomRent == true && batchDao.isBatchExecutedRoomRent(timeZone,unitIds)==true)
			{
				lockRoomRent = false;
				try{
					batchDao.addBathLogForRoomRent(timeZone,unitIds);
				}catch(Exception e){
					logger.error(e.getMessage(), e);
					errorList.add(genarateDto(e.getCause().toString(),"postRoomRent","addBathLogForRoomRent",unitIds.toString()));
				}

				try{
					roomRentBatchChargesServ.postRoomRentCharges(unitIds);
				}catch(Exception e){
					logger.error(e.getMessage(), e);
					errorList.add(genarateDto(e.getCause().toString(),"postRoomRent","postRoomRentCharges",unitIds.toString()));
				}

				try{
					String pattern = "dd";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));
					if(date.toString().equals("01"))
					{
						roomRentBatchChargesServ.postPeriodicServices(unitIds);

						iSlotMasterService.deleteUnusedSlots(unitIds);
					}


				}catch(Exception e){
					logger.error(e.getMessage(), e);
					errorList.add(genarateDto(e.getCause().toString(),"postRoomRent","postPeriodicServices Or deleteUnusedSlots ",unitIds.toString()));
				}

			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			errorList.add(genarateDto(e.getCause().toString(),"postRoomRent","isBatchExecutedRoomRent",unitIds.toString()));
		}finally {
			lockRoomRent = true;
		}

		for (ExceptionDto exceptionDto : errorList) {
			try {
				batchDao.addExceptionLog(exceptionDto);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

	}


	// Called per timezone-group from RevertAllocatedQuantity's unprotected loops (no per-entry try/catch there) —
	// throwing here would abort every remaining hospital unit-group in the same scheduled run. See
	// docs/DECISION_NEEDED_HISWAR_BatchContinuation.md. Log only, preserve existing swallow-and-continue behavior.
	public void triggerScheduledMailsWeekly(String Execution,  List<Integer> unitIds) {
		try {
			iSmsUtilityConfigService.triggerScheduledMails(Execution,unitIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	public void triggerScheduledMailsMonthly(String Execution,  List<Integer> unitIds) {
		try {
			iSmsUtilityConfigService.triggerScheduledMails(Execution,unitIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	public void triggerScheduledMailsDaily(String Execution,  List<Integer> unitIds) {
		try {
			iSmsUtilityConfigService.triggerScheduledMails(Execution,unitIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	public void triggerScheduledMailsYearly(String Execution,  List<Integer> unitIds) {
		try {
			iSmsUtilityConfigService.triggerScheduledMails(Execution,unitIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}


	public void template(List<Integer> unitId) {
		try {
			ivaccineImmunizationMapperService.batchJobToSendMessageForCycliceAppointmentDueDate(unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}


	public void dailyBasisPendingAmountIpPatientSMS(List<Integer> unitId) {
		try {
			ivaccineImmunizationMapperService.dailyBasisPendingAmountIpPatientSMS(unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}


	}


	// Called per timezone-group from RevertAllocatedQuantity's unprotected loops — see
	// docs/DECISION_NEEDED_HISWAR_BatchContinuation.md. Log only, preserve existing swallow-and-continue behavior.
	public void doctorShare(List<Integer> unitList) {
		try {

			for (Integer unitId : unitList) {
				iTDoctorShareService.getProcessDocShare(unitId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	private ExceptionDto genarateDto(String err, String classname, String methodname, String unitIds) {
		ExceptionDto dto = new ExceptionDto();
		dto.setExceptionMessage(err);
		dto.setClassName(classname);
		dto.setMethodName(methodname);
		dto.setUnitIds("Unit Ids"+unitIds);
		return dto;
	}

	// Called per timezone-group from RevertAllocatedQuantity's unprotected loops — see
	// docs/DECISION_NEEDED_HISWAR_BatchContinuation.md. Log only, preserve existing method-level `return null;` fallthrough.
	public Response checkBatchJobExecutedOrNotForpostRoomRent(String timeZone, List<Integer> unitId) {
		try {

			return batchDao.checkBatchJobExecutedOrNotForpostRoomRent(timeZone,unitId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Response checkBatchJobExecutedOrNotFormidnightBatch(String timeZone, List<Integer> unitId) {
		try {

			return batchDao.checkBatchJobExecutedOrNotFormidnightBatch(timeZone,unitId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Response saveLog(String timeZone, List<Integer> unitId, String func) {
		try {

			return batchDao.saveLog(timeZone,unitId,func);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
