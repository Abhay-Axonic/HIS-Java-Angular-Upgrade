package com.param.adt.admission.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.admission.dto.BedAllocationDto;
import com.param.adt.admission.dto.NursingPatientAcceptanceDto;
import com.param.adt.admission.service.IBedAllocationService;
import com.param.adt.common.IADTConstants;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedCategoryMasterDto;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.ServiceOrdDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.procedure.service.IBillOfMaterialService;
import com.param.global.procedure.service.IProcedureConfigurationService;
import com.param.global.service.IKinDetailsService;
import com.param.global.service.IUnknownPatientRegistrationService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.service.IMedicationService;
import com.param.opd.coversheet.service.IPatientDiagnosisService;
import com.param.utility.TimeZoneComponent;


@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BedAllocationController implements ICommonConstants, IADTConstants 
{
	
	@Autowired
	IBedAllocationService iBedAllocationService;
	@Autowired
	IKinDetailsService iKinDetailsService;
	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao; 
	@Autowired
	IProcedureConfigurationService iProcedureConfigurationService;
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	@Autowired
	IBillOfMaterialService iBillOfMaterialService;
	@Autowired
	IUnknownPatientRegistrationService iUnknownPatientRegistrationService;
	@Autowired
	IBillingService globalBillingService;
	@Autowired
	IPatientDiagnosisService iPatientDiagnosisService;
	@Autowired
	IMedicationService iMedicationService;
	@Autowired
	private IDepositMasterService iDepositMasterService;
	
	@RequestMapping(value = "/getActiveBedList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveBedList(@RequestBody BedMasterDto bedMasterDto) {
		try {
			return iBedAllocationService.getActiveBedList(bedMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getBedListByMultipleCriteria", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedListByMultipleCriteria(@RequestBody BedMasterDto bedMasterDto) {
		try {
			return iBedAllocationService.getBedListByMultipleCriteria(bedMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getActiveFloorList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveFloorList(@RequestBody BedAllocationDto bedMasterDto) {
		try {

			return iBedAllocationService.getActiveFloorMasterList(bedMasterDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getActiveWardListByFloorIdByGender", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveWardListByFloorId(@RequestBody BedAllocationDto bedMasterDto) {
		try {
			return iBedAllocationService.getWardListByFloorId(bedMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getNursingStationListByWard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNursingStationListByWard(@RequestBody BedAllocationDto bedMasterDto) {
		try {
			return iBedAllocationService.getNursingStationListByWard(bedMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getBedCategoryListByOrgUnit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedCategoryListByOrgUnit(@RequestBody BedCategoryMasterDto bedCategoryMasterDto) {
		try {
			return iBedAllocationService.getBedCategoryList(bedCategoryMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getBedStatusList", method = RequestMethod.GET)
	public @ResponseBody Response getBedStatusList() {
		try {

			return iBedAllocationService.getBedStatusList();

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/getMealPreference", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMealPreference(@RequestBody MealPreferenceMasterDto mealPreferenceMasterDto) {
		try {
			return iBedAllocationService.getMealPreference(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@RequestMapping(value = "/savePatientAdmission ", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody synchronized Response savePatientAdmission(@RequestBody AdmissionDto admission) {
		try {
			
			if(admission.getReqDto() != null && admission.getReqDto().getListPaymentDepositDetailsDto()!= null) {
				

				for (PaymentDepositDetailsDto dto : admission.getReqDto().getListPaymentDepositDetailsDto()) {
					
					if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) {
						com.param.global.common.Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
						if(resCcavenue.getObject() == null) {
							return new Response<>(ERROR, null, dto.getTransactionNo()+" This already used in other trancation", null, null);
						}
					}
				}
			}
				//For Check Bed Already Booked or Not START
				 Response res1 =   iBedAllocationService.isBedBookedOrNot(admission);
				 if(res1.getListObject()== null || res1.getListObject().size()==0)
				 {
					 return new Response(ERROR, ALREADY_EXIST_CODE, "Bed is already occupied...!!", null, null);
				 }
				//For Check Bed Already Booked or Not END
				
			 	if(admission.getPatientSponsorDetailsDtosList()!=null && admission.getPatientSponsorDetailsDtosList().size()>0)
				{
					for(PatientSponsorDetailsDto patientSponsorDetailsDto : admission.getPatientSponsorDetailsDtosList())
					{
						if(patientSponsorDetailsDto.getPriorityId()==1){
							admission.setPaymentEntitlementId(patientSponsorDetailsDto.getPaymentEntitlementId());
						}
					}
				}
				Response res =   iBedAllocationService.savePatientAdmission(admission);
				
				if(res.getStatus().equals("success") && (res.getObject()!=null)) 
				{
					if(admission.getContractId()!=null && admission.getContractId()>0)
						iBedAllocationService.postAutorenderServices(admission);
						
					List<ServiceOrdDto> serviceOrdDtosList=null;
					if(admission.getDiagnosisServiceList()==null) {
						 serviceOrdDtosList=new LinkedList<>();
					}
					
					if(admission.getServiceId()!=null)
					{
						ServiceOrdDto dto = new ServiceOrdDto();
						dto.setServiceId(admission.getServiceId());
						if(admission.getDiagnosisServiceList()!=null) {
							admission.getDiagnosisServiceList().add(dto);
						}else {
							 serviceOrdDtosList.add(dto);
						}
					   
					    
					}
					if(admission.getBiomedicalsChargesServiceId()!=null)
					{
						ServiceOrdDto dto = new ServiceOrdDto();
						dto.setServiceId(admission.getBiomedicalsChargesServiceId());
						if(admission.getDiagnosisServiceList()!=null) {
							admission.getDiagnosisServiceList().add(dto);
						}else {
							 serviceOrdDtosList.add(dto);
						}				
					}
					if(admission.getMrdChargesServiceId()!=null)
					{
						ServiceOrdDto dto = new ServiceOrdDto();
						dto.setServiceId(admission.getMrdChargesServiceId());
						if(admission.getDiagnosisServiceList()!=null) {
							admission.getDiagnosisServiceList().add(dto);
						}else {
							 serviceOrdDtosList.add(dto);
						}				
					}
					if(admission.getHospitalChargesServiceId()!=null)
					{
						ServiceOrdDto dto = new ServiceOrdDto();
						dto.setServiceId(admission.getHospitalChargesServiceId());
						if(admission.getDiagnosisServiceList()!=null) {
							admission.getDiagnosisServiceList().add(dto);
						}else {
							 serviceOrdDtosList.add(dto);
						}				
					}
					if(admission.getDiagnosisServiceList()==null) {
						admission.setDiagnosisServiceList(serviceOrdDtosList);
					}					
					iBedAllocationService.postAdmissionServiceCharges(admission);	
					
							
					if(res.getStatus().equals(SUCCESS)) 
					{
						SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
						AdmissionDto adtDto = (AdmissionDto)res.getObject();
							smsInfo.setUnitId(admission.getUnitId());
							smsInfo.setAdmissionId(adtDto.getAdmissionId());
							smsInfo.setOrganizationId(admission.getOrganizationId());
							smsInfo.setPatientId(admission.getPatientId());
							smsInfo.setTreatingDoctorId(admission.getDoctorId());
							smsInfo.setBedDetails(admission.getWardName()+", "+admission.getBedNumber());
							smsInfo.setReferringDoctorId(admission.getReferralId());
							smsInfo.setAdmissionNo(admission.getAdmissionNumber());
							smsInfo.setAdmitingDoctorId(admission.getAdmittingDoctorId());
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ADMISSION,smsInfo);
						if(res.getObject()!=null) {
							
							smsInfo.setOrganizationId(admission.getOrganizationId());
							smsInfo.setAdmissionId(adtDto.getAdmissionId());
							smsInfo.setUnitId(admission.getUnitId());
							smsInfo.setPatientId(admission.getPatientId());
							smsInfo.setTreatingDoctorId(admission.getDoctorId());
							smsInfo.setBedDetails(admission.getWardName()+", "+admission.getBedNumber());
							smsInfo.setReferringDoctorId(admission.getReferralId());
							smsInfo.setAdmissionNo(admission.getAdmissionNumber());
							smsInfo.setAdmitingDoctorId(admission.getAdmittingDoctorId());
							smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ADMISSION, smsInfo);
						}
					}
					
					AdmissionDto dto = (AdmissionDto)res.getObject();
					com.param.global.common.Response rocedureRes = iProcedureConfigurationService.getFuturedOrderProcedureByEncounter(dto.getPatientId(),dto.getDoctorId());
					if(rocedureRes.getCode().equals(SUCCESS_CODE)&& rocedureRes.getListObject()!=null)
					{
						List<Integer> list = rocedureRes.getListObject();
						for (Iterator iterator = list.iterator(); iterator.hasNext();) {
							Integer proReqId = (Integer) iterator.next();
							iProcedureConfigurationService.convertProcedure(dto.getAdmissionId(), 2, proReqId);
						}
					}
					if(admission.getOtReservationList()!=null && admission.getOtReservationList().size()>0) 
					{
						for (OTReservationDto oTReservationDto : admission.getOtReservationList()) {
							oTReservationDto.setAdmissionId(dto.getAdmissionId());
							oTReservationDto.setVisitTypeId(admission.getVisitTypeId());
							iBillOfMaterialService.updateReservationAgainstAdmissionEncounter(oTReservationDto);
							iBillOfMaterialService.convertOtdepositeToActiveDeposite(oTReservationDto);
						}
					}
					if(admission.getDiagnosisList()!=null && admission.getDiagnosisList().size()>0)
					{
						DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto = new DiagnosisPatientAppointmentMapperDto(); 
							diagnosisPatientAppointmentMapperDto.setAdmissionId(admission.getAdmissionId());
							diagnosisPatientAppointmentMapperDto.setPatientId(admission.getPatientId());
							diagnosisPatientAppointmentMapperDto.setCreated_by(admission.getCreatedBy());
							diagnosisPatientAppointmentMapperDto.setUpdated_by(admission.getUpdatedBy());
							diagnosisPatientAppointmentMapperDto.setStatus('A');
							diagnosisPatientAppointmentMapperDto.setIsWhileAdmission('Y');
							diagnosisPatientAppointmentMapperDto.setOrganizationId(admission.getOrganizationId());
							diagnosisPatientAppointmentMapperDto.setUnitId(admission.getUnitId());
							diagnosisPatientAppointmentMapperDto.setDiagnosisType(1);
							diagnosisPatientAppointmentMapperDto.setListDiagnosisPatientAppointmentDetailsDto(admission.getDiagnosisList());
						iPatientDiagnosisService.savePatientDiagnosis(diagnosisPatientAppointmentMapperDto);
					}
					if(admission.getDiagnosisMedicationDetailList()!=null && admission.getDiagnosisMedicationDetailList().size()>0 && admission.getIsPharmacologicalValidationSkipIp()==false)
					{
						PrescriptionDto prescriptionDto = new PrescriptionDto();
							prescriptionDto.setOrganizationId(admission.getOrganizationId());
							prescriptionDto.setUnitId(admission.getUnitId());
							prescriptionDto.setVisitTypeId(admission.getVisitTypeId());
							prescriptionDto.setDoctorId(admission.getDoctorId());
							prescriptionDto.setPrescriptionDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
							prescriptionDto.setAuthorisedBy(admission.getDoctorId());
							prescriptionDto.setAuthorisedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
							prescriptionDto.setAddedBy(admission.getCreatedBy());
							prescriptionDto.setAddedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
							prescriptionDto.setAdmissionId(admission.getAdmissionId());
							prescriptionDto.setPatientId(admission.getPatientId());
							prescriptionDto.setDeptId(admission.getSpecialityId());
							prescriptionDto.setPriorityId(2);
							prescriptionDto.setPrescriptionDetailsDtosList(admission.getDiagnosisMedicationDetailList());
						iMedicationService.saveMedication(prescriptionDto);
					}
				}
			return res;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/isPatientAlreadyAdmitted/{patientId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Boolean isPatientAlreadyAdmitted(@PathVariable("patientId")int patientId,
														  @PathVariable("orgId")int orgId,
														  @PathVariable("unitId")int unitId) {
		try {
			return iBedAllocationService.isPatientAlreadyAdmitted(patientId,orgId,unitId);
		} catch (Exception e) {
			
		}
		return false;
	}
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody com.param.global.common.Response test() 
	{
		try {
			return iBedAllocationService.test();
		} catch (Exception e) {
			
		}
		return null;
	}
	
	@RequestMapping(value = "/nursing/patient/acceptance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientAcceptanceListForNursing(@RequestBody NursingPatientAcceptanceDto nursingPatientAcceptanceDto) {
		try {
			return iBedAllocationService.getPatientAcceptanceListForNursing(nursingPatientAcceptanceDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/nursing/patient/request/accept", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody synchronized Response acceptNursingAcceptanceRequet(@RequestBody AdmissionDto admission) {
		try {
			Response res =  iBedAllocationService.acceptNursingAcceptanceRequet(admission);
			
            if(admission.getOtReservationList()!=null && admission.getOtReservationList().size()>0) {
                
                for (OTReservationDto oTReservationDto : admission.getOtReservationList()) {
                    oTReservationDto.setAdmissionId(admission.getAdmissionId());
                    oTReservationDto.setVisitTypeId(admission.getVisitTypeId());
                    iBillOfMaterialService.updateReservationAgainstAdmissionEncounter(oTReservationDto);
                    
                    iBillOfMaterialService.convertOtdepositeToActiveDeposite(oTReservationDto);

                }
            }
            
            com.param.global.common.Response rocedureRes = iProcedureConfigurationService.getFuturedOrderProcedureByAdmission(admission.getAdmissionId());
			if(rocedureRes.getListObject()!=null && rocedureRes.getListObject().size()>0 )
			{
				List<Integer> list = rocedureRes.getListObject();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Integer proReqId = (Integer) iterator.next();
					com.param.global.common.Response resOrder = iProcedureConfigurationService.getorderDetailsIdByProcedureReqId(proReqId);
					List<OrderDetailsMasterDto> orderList = new ArrayList<>();
					
					if(resOrder.getListObject()!= null && resOrder.getListObject().size()>0) {
						orderList.addAll(resOrder.getListObject());
						
						ConvertServicesFromOptoIpDto costEstimationDto = new ConvertServicesFromOptoIpDto();
						
						costEstimationDto.setAdmissionId(admission.getAdmissionId());
						costEstimationDto.setUserId(admission.getUpdatedBy());
						costEstimationDto.setVisitTypeId(admission.getVisitTypeId());
						costEstimationDto.setPatientId(admission.getPatientId());
						costEstimationDto.setOrganizationId(admission.getOrganizationId());
						costEstimationDto.setUnitId(admission.getUnitId());
						List<Integer> orderDetailsId = new ArrayList<>();
						
						for (OrderDetailsMasterDto orderDetailsMasterDto : orderList) {
							orderDetailsId.add(orderDetailsMasterDto.getOrderDetailsId());
							costEstimationDto.setEncounterId(orderDetailsMasterDto.getEncounterId());
						}
						
						costEstimationDto.setOrderDetailsIds(orderDetailsId);
						globalBillingService.convertServicesFromOPToIP(costEstimationDto);
					}
					
				}
				
			}
            
            return res;
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/nursing/patient/request/reject", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody synchronized Response rejectNursingAcceptanceRequet(@RequestBody AdmissionDto admission) {
		try {
			return iBedAllocationService.rejectNursingAcceptanceRequet(admission);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActiveLaborBedList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveLaborBedList(@RequestBody BedMasterDto bedMasterDto) {
		try {
			return iBedAllocationService.getActiveLaborBedList(bedMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActiveHemodialysisBedList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveHemodialysisBedList(@RequestBody BedMasterDto bedMasterDto) {
		try {
			return iBedAllocationService.getActiveHemodialysisBedList(bedMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/nursing/patient/acceptance/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientAcceptanceListForNursingCount(@RequestBody NursingPatientAcceptanceDto nursingPatientAcceptanceDto) {
		try {
			return iBedAllocationService.getPatientAcceptanceListForNursingCount(nursingPatientAcceptanceDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getcheckBedAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getcheckBedAllocation(@RequestBody AdmissionDto admissionDto) {
		try {
			return iBedAllocationService.getcheckBedAllocation(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/nursing/patient/universalSearchForNursingAcceptance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response universalSearchForNursingAcceptance(@RequestBody NursingPatientAcceptanceDto nursingPatientAcceptanceDto) {
		try {
			return iBedAllocationService.universalSearchForNursingAcceptance(nursingPatientAcceptanceDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/getAdmisssoinTAT", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmisssoinTAT(@RequestBody AdmissionDto admissionDto) {
		try {
			return iBedAllocationService.getAdmisssoinTAT(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateNotRequiredServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNotRequiredServices(@RequestBody AdmissionDto admission) {
		try {
			return iBedAllocationService.updateNotRequiredServices(admission);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateRemarkAdmissionDetailsByAdissionId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRemarkAdmissionDetailsByAdissionId(@RequestBody AdmissionDto admission) {
		try {
			return iBedAllocationService.updateRemarkAdmissionDetailsByAdissionId(admission);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/editAdmissionDetailsByAdissionIdAdmiitedPatientList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response editAdmissionDetailsByAdissionIdAdmiitedPatientList(@RequestBody AdmissionDto admission) {
		try {
			return iBedAllocationService.editAdmissionDetailsByAdissionIdAdmiitedPatientList(admission);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateOtStatusAgainstAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateOtStatusAgainstAdmission(@RequestBody AdmissionDto admissionDto) {
		try {
			return iBedAllocationService.updateOtStatusAgainstAdmission(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getBedCategoryListByVisitId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedCategoryListByVisitId(@RequestBody BedCategoryMasterDto bedCategoryMasterDto) {
		try {
			return iBedAllocationService.getBedCategoryListByVisitId(bedCategoryMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
}
