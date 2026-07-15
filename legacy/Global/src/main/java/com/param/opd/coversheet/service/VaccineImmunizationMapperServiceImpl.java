package com.param.opd.coversheet.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.coversheet.dao.IVaccineImmunizationDAO;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class VaccineImmunizationMapperServiceImpl implements IVaccineImmunizationMapperService, ICommonConstants{

	@Autowired
	IVaccineImmunizationDAO iVaccineImmunizationDAO;
	
	@Autowired
	ISmsUtilityConfigService smsUtilityService;
	
	@Override
	@Transactional
	public Response getListOfVaccineImmunizationMapper(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException {
		return iVaccineImmunizationDAO.getListOfVaccineImmunizationMapper(vaccineImmunizationMapperDto);
	}



	@Override
	@Transactional
	public Response saveVaccineImmunizationMapper(List<VaccineImmunizationMapperDto> vaccineImmunizationMapperDto)
			throws ApplicationException {
		return iVaccineImmunizationDAO.saveVaccineImmunizationMapper(vaccineImmunizationMapperDto);
		
		
	}



	@Override
	@Transactional
	public Response updateVaccineImmunizationMapper(VaccineImmunizationMapperDto dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iVaccineImmunizationDAO.updateVaccineImmunizationMapper(dto);
	}



	@Override
	@Transactional
	public Response batchJobToSendMailAndMessageForVaccinationDueDate(List<Integer> unitIds) throws Exception {
        try {
        	for (int i = 0; i < 2; i++) {
        		  Response response=iVaccineImmunizationDAO.getVaccineDueDatePatientsForSendingMailAndMsgs(i,unitIds);
                  if(response.getListObject()!=null && response.getListObject().size()>0){
                      List<VaccineImmunizationMapperDto> list = response.getListObject();
                      for (VaccineImmunizationMapperDto dto : list) {
                          SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
                          smsInfo.setUnitId(dto.getUnitId());
                          smsInfo.setVaccineName(dto.getVaccineName());
                          smsInfo.setOrganizationId(dto.getOrganizationId());
                          smsInfo.setPatientId(dto.getPatientId());
                          smsInfo.setDateTime(dto.getCalculatedDate());
                          smsInfo.setUniqueId(dto.getVaccineImmunMapperId().toString());
                          smsInfo.setSeriesName(dto.getSeriesName()!=null ? dto.getSeriesName() : null);
                          smsInfo.setSiteName(dto.getSiteName()!=null ? dto.getSiteName() : null);
                          smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.VACCINATION_DUE_DATE_ALERT,smsInfo);

                          smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.VACCINATION_DUE_DATE_ALERT, smsInfo);
                      }
                  }
        	}
            
            return new Response<>(SUCCESS, SUCCESS_CODE, "", null, null);
           
        } catch (Exception e) {
        	throw e;
        }
	}



	@Override
	@Transactional
	public Response updateVaccineImmunizationMapperForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException {
		return iVaccineImmunizationDAO.updateVaccineImmunizationMapperForNewCov(vaccineImmunizationMapperDto);
	}



	@Override
	@Transactional
	public Response removeFreeTextVaccineForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iVaccineImmunizationDAO.removeFreeTextVaccineForNewCov(vaccineImmunizationMapperDto);
	}



	@Override
	public Response batchJobToSendMessageForCycliceAppointmentDueDate(List<Integer> unitIds)
			throws ApplicationException {
		  try {
	        		  Response response=iVaccineImmunizationDAO.batchJobToSendMessageForCycliceAppointmentDueDate(unitIds);
	                  if(response.getListObject()!=null && response.getListObject().size()>0){
	                	   List<SlotMasterDto> list = response.getListObject();
	                	  for (SlotMasterDto dto : list) {
	      					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
	      					smsInfo.setUnitId(dto.getUnitId());
	      					smsInfo.setOrganizationId(dto.getOrganizationId());
	      					smsInfo.setPatientId(dto.getPatientId());
	      					smsInfo.setConsultingDoctorId(dto.getDoctorId());
	      					smsInfo.setTreatingDoctorId(dto.getDoctorId());
	      					smsInfo.setCyclicOrderDate(dto.getCyclicOrdScheduledDate()+" "+dto.getAppointmentTime()+":00");
	      					smsInfo.setReferringDoctorId(null);
	      					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.Appointment_Template, smsInfo);
	                      }
	                  }
	                  
	                  
	                  Response response1=iVaccineImmunizationDAO.batchJobToSendMessageForCycliceAppointmentServiceDueDate(unitIds);
	                  if(response1.getListObject()!=null && response1.getListObject().size()>0){
	                	   List<SlotMasterDto> list = response1.getListObject();
	                	  for (SlotMasterDto dto : list) {
	      					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
	      					smsInfo.setUnitId(dto.getUnitId());
	      					smsInfo.setOrganizationId(dto.getOrganizationId());
	      					smsInfo.setPatientId(dto.getPatientId());
	      					smsInfo.setCyclicOrderDate(dto.getCyclicOrdScheduledDate());
	      					smsInfo.setTestDesc(dto.getServiceStandardName());
	      					smsInfo.setReferringDoctorId(null);
	      					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.Appointment_Template, smsInfo);
	                      }
	                  }
	            
	            return new Response<>(SUCCESS, SUCCESS_CODE, "", null, null);
	           
	        } catch (Exception e) {
	        	throw e;
	        }
	}



	@Override
	public Response dailyBasisPendingAmountIpPatientSMS(List<Integer> unitIds) throws ApplicationException {
		  try {
	        		  Response response=iVaccineImmunizationDAO.dailyBasisPendingAmountIpPatientSMS(unitIds);
	        		  List<OrderMasterDto> pendingList =  new ArrayList<OrderMasterDto>();
	                  if(response.getListObject()!=null && response.getListObject().size()>0){
	                	  pendingList.addAll(response.getListObject());
	                	  
	                	  Response response1=iVaccineImmunizationDAO.dailyBasisDepositAmountIpPatientSMS(unitIds);
	                	  List<OrderMasterDto> depositList =  new ArrayList<OrderMasterDto>();
		                  if(response1.getListObject()!=null && response1.getListObject().size()>0){
		                	  depositList.addAll(response1.getListObject());
		                  }
		                  
		                  
		                  for (OrderMasterDto pending : pendingList) {
		                	  
		                	  if(pending.getPendingSelfPayable() != null && pending.getPendingSelfPayable().doubleValue()>0) {
		                		 
		                		  
		                		  Double depositAmount  = 0.0;
		                		  Double pendingDuaAmount  = pending.getPendingSelfPayable().doubleValue();
		                		  
		                		  for (OrderMasterDto deposit : depositList) {
				                	  
		                			  	if(deposit.getDepositAmount()!= null  && deposit.getDepositAmount()>0) {
		                			  	}
										if(deposit.getDepositAmount()!= null  && deposit.getDepositAmount()>0 && pending.getPatientId().equals(deposit.getPatientId())) {
											depositAmount = deposit.getDepositAmount(); 
											pendingDuaAmount = pending.getPendingSelfPayable().doubleValue() - depositAmount;
											
											if(pendingDuaAmount == 0 || pendingDuaAmount<0) {
												pendingDuaAmount  = 0.0;
											}
										}
									}
		                		  
		                		  
		                		  SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
			      					smsInfo.setUnitId(pending.getOrgUnitId());
			      					smsInfo.setOrganizationId(pending.getOrgId());
			      					smsInfo.setPatientId(pending.getPatientId());
			      					smsInfo.setTotalDepositAmount(depositAmount);
			      					smsInfo.setTotalPendingDuaAmount(pendingDuaAmount);
			      					smsInfo.setSelfAmount(pending.getPendingSelfPayable());
			      					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PendingPatientDuaAmount, smsInfo);
		                		  
		                	  }
							
		                  }
		                  
	                  }
	                  
	            
	            return new Response<>(SUCCESS, SUCCESS_CODE, "", null, null);
	           
	        } catch (Exception e) {
	        	throw e;
	        }
	
	}

}
