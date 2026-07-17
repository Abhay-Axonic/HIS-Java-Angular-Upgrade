package com.param.otims.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.model.master.Tax;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IOTCompletionDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.global.PatientConsession;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.otims.dao.IOperationTableDao;
import com.param.global.otims.dao.IOperationTheatreDao;
import com.param.global.otims.dao.IProcedureOrderEquipmentDao;
import com.param.global.otims.dto.BatchDto;
import com.param.global.otims.dto.OTTDetailsVO;
import com.param.global.otims.dto.OperationTableTypeDto;
import com.param.global.otims.dto.OperationTheatreDto;
import com.param.global.otims.dto.ProcedureConfigurationChecklistDto;
import com.param.global.otims.dto.ProcedureConfigurationConsentDto;
import com.param.global.otims.dto.ProcedureConfigurationEquipmentDto;
import com.param.global.otims.dto.ProcedureConfigurationInventoryItemDto;
import com.param.global.otims.dto.ProcedureConfigurationNoteDto;
import com.param.global.otims.dto.ProcedureConfigurationPatientEducationDto;
import com.param.global.otims.dto.ProcedureConfigurationServiceDto;
import com.param.global.otims.dto.ProcedureConfigurationStaffDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.procedure.VO.ProcedureConfigurationVO;
import com.param.global.procedure.VO.ProcedureTypeMasterVO;
import com.param.global.procedure.VO.SpecialityMasterVO;
import com.param.global.procedure.dao.IBillOfMaterialDao;
import com.param.global.procedure.dao.IProcedureConfigurationDao;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.otims.dao.IProcedureConfigurationChecklistDao;
import com.param.otims.dao.IProcedureConfigurationConsentDao;
import com.param.otims.dao.IProcedureConfigurationEquipmentDao;
import com.param.otims.dao.IProcedureConfigurationInventoryItemDao;
import com.param.otims.dao.IProcedureConfigurationNoteDao;
import com.param.otims.dao.IProcedureConfigurationPatientEducationDao;
import com.param.otims.dao.IProcedureConfigurationServiceDao;
import com.param.otims.dao.IProcedureConfigurationStaffDao;
import com.param.otims.dao.IProcedureTemplateDao;
import com.param.otims.model.ProcedureConfiguration;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProcedureTemplateServiceImpl implements IProcedureTemplateService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(ProcedureTemplateServiceImpl.class);

	@Autowired
	IProcedureTemplateDao iProcedureTemplateDao;

	@Autowired
	IProcedureConfigurationChecklistDao iProcedureConfigurationChecklistDao;

	@Autowired
	IProcedureConfigurationConsentDao iProcedureConfigurationConsentDao;

	@Autowired
	IProcedureConfigurationInventoryItemDao iProcedureConfigurationInventoryItemDao;

	@Autowired
	IProcedureConfigurationNoteDao iProcedureConfigurationNoteDao;

	@Autowired
	IProcedureConfigurationPatientEducationDao iProcedureConfigurationPatientEducationDao;

	@Autowired
	IProcedureConfigurationServiceDao iProcedureConfigurationServiceDao;

	@Autowired
	IProcedureConfigurationStaffDao iProcedureConfigurationStaffDao;

	@Autowired
	private IProcedureConfigurationDao iProcedureConfigurationDao;

	@Autowired
	private IOtimsService iOtimsService;

	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	IEmrService EmrService;
	
	@Autowired
	IProcedureConfigurationEquipmentDao iProcedureConfigurationEquipmentDao;
	
	@Autowired
	IOperationTheatreDao iOperationTheatreDao; 
	
	@Autowired
	IOperationTableDao iOperationTableDao;
	
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	SessionService sessionService;
	

	@Autowired
	IBillOfMaterialDao iBillOfMaterialDao;
	
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	
	@Autowired
	IProcedureOrderEquipmentDao iProcedureOrderEquipmentDao;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	IOTCompletionDao OTCompletionDao;
	
	@Autowired
	IGlobalDao GlobalDao;

	@Override
	public Response saveProcedureConfigTemplate(ProcedureConfigurationDto reqDto) throws ApplicationException {
		try {

			boolean flag = false;
			boolean serviceFlag = false;
			if (reqDto.getProcedureConfigId() != null) {

				flag = iProcedureTemplateDao.validProcConfByServiceOrgUnitConfigId(reqDto.getServiceId(),
						reqDto.getOrgId(), reqDto.getUnitId(), reqDto.getProcedureConfigId());
			}else{
				
				serviceFlag = iProcedureTemplateDao.validProcConfByServiceOrgUnitServiceId(reqDto.getServiceId(),
						reqDto.getOrgId(), reqDto.getUnitId());
			} 
			
			if(serviceFlag) {
				
				return new Response(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
				
			} else if(flag) {
				
				return new Response(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			
			} else {
				Response res = iProcedureTemplateDao.saveProcedureConfigTemplate(reqDto);

				if (res.getObject() != null) {

					ProcedureConfiguration mstr = (ProcedureConfiguration) res.getObject();

					if (reqDto.getChecklistDetailList().size() > 0) {

						iProcedureConfigurationChecklistDao.saveProcedureConfigChecklist(
								reqDto.getChecklistDetailList(), mstr.getProcedureConfigId());
					}

					if (reqDto.getConsentDetailList().size() > 0) {

						iProcedureConfigurationConsentDao.saveProcedureConfigConsent(reqDto.getConsentDetailList(),
								mstr.getProcedureConfigId());
					}

					if (reqDto.getInventoryItemList().size() > 0) {

						iProcedureConfigurationInventoryItemDao.saveProcedureConfigInventoryItem(
								reqDto.getInventoryItemList(), mstr.getProcedureConfigId());
					}

					if (reqDto.getNoteList().size() > 0) {

						iProcedureConfigurationNoteDao.saveProcedureConfigNote(reqDto.getNoteList(),
								mstr.getProcedureConfigId());
					}

					if (reqDto.getServiceList().size() > 0) {

						iProcedureConfigurationServiceDao.saveProcedureConfigService(reqDto.getServiceList(),
								mstr.getProcedureConfigId());
					}
					
					if (reqDto.getEquipmentDetailList().size() > 0) {

						iProcedureConfigurationEquipmentDao.saveProcedureConfigEquipment(reqDto.getEquipmentDetailList(),
								mstr.getProcedureConfigId());
					}

					if (reqDto.getStaffDetailList().size() > 0) {

						iProcedureConfigurationStaffDao.saveProcedureConfigStaff(reqDto.getStaffDetailList(),
								mstr.getProcedureConfigId());
					}

					if (reqDto.getPatientEduObj() != null) {

						iProcedureConfigurationPatientEducationDao.saveProcedureConfigPatientEducation(
								reqDto.getPatientEduObj(), mstr.getProcedureConfigId());
					}

					String ProcedureName = iProcedureConfigurationDao.getProcedureNameById(reqDto.getProcedureTypeId());
					String SpecialityName = iProcedureConfigurationDao.getSpecialityNameById(reqDto.getSpecialityId());

					ProcedureTypeMasterVO procedureTypeMasterVO = new ProcedureTypeMasterVO();
					procedureTypeMasterVO.set_id(reqDto.getProcedureTypeId().toString());
					procedureTypeMasterVO.setName(ProcedureName);

					SpecialityMasterVO specialityMasterVO = new SpecialityMasterVO();

					specialityMasterVO.set_id(reqDto.getSpecialityId().toString());
					specialityMasterVO.setName(SpecialityName);

					ProcedureConfigurationVO procedureConfigurationVO = new ProcedureConfigurationVO(
							mstr.getProcedureConfigId(), reqDto.getCptCode(), reqDto.getCptDescription(),
							reqDto.getDuration(), reqDto.getAnesthesiaTypeId(), reqDto.getStatus(),
							reqDto.getCreatedBy(), reqDto.getOrgId(), reqDto.getUnitId(), reqDto.getUpdatedBy(),
							reqDto.getService(), reqDto.getCreatedDate(), reqDto.getUpdatedDate(),
							procedureTypeMasterVO, specialityMasterVO, reqDto.getAnesthesiaType());

					//System.out.println("procedureConfigurationVO             " + procedureConfigurationVO);

					Request request = new Request();
					Payload payload = new Payload();

					if (reqDto.getProcedureConfigId() != null) {

						payload.setActionType("update");

					} else {

						payload.setActionType("create");
					}

					payload.setData(procedureConfigurationVO);
					request.setKey(MasterKeyConstants.ProcedureConfigurationHISKey.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					//EmrService.publish(request);

				}
				return new Response<>(SUCCESS, SUCCESS_CODE, res.getMessage(), null, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProcedureConfigTemplate(ProcedureConfigurationDto reqDto) throws ApplicationException {
		try {
			ProcedureConfigurationDto responseDto = new ProcedureConfigurationDto();
			Response procedureOrderRes = iProcedureConfigurationConsentDao.getConsentListByProcedureConfigId(reqDto);
			if(procedureOrderRes.getListObject()!=null&& procedureOrderRes.getListObject().size()>0  ) {
				
				List<ProcedureConfigurationConsentDto> list =  procedureOrderRes.getListObject();
				responseDto.setProcedureConfigId(list.getFirst().getProcedureConfigId());
			}
			responseDto.setConsentDetailList(procedureOrderRes.getListObject());

			Response procedureChecklist = iProcedureConfigurationChecklistDao.getChecklistListByProcedureConfigId(reqDto);
			
			if(procedureChecklist.getListObject()!=null&& procedureChecklist.getListObject().size()>0  ) {
				
				List<ProcedureConfigurationChecklistDto> list =  procedureChecklist.getListObject();
				responseDto.setProcedureConfigId(list.getFirst().getProcedureConfigId());
			}
			responseDto.setChecklistDetailList(procedureChecklist.getListObject());
			
			Response procedureStafflist = iProcedureConfigurationStaffDao.getStaffListByProcedureConfigId(reqDto);
			if(procedureStafflist.getListObject()!=null&& procedureStafflist.getListObject().size()>0  ) {
				
				List<ProcedureConfigurationStaffDto> list =  procedureStafflist.getListObject();
				responseDto.setProcedureConfigId(list.getFirst().getProcedureConfigId());
			}
			
			responseDto.setStaffDetailList(procedureStafflist.getListObject());

			Response procedureInventoryItemlist = iProcedureConfigurationInventoryItemDao.getInventoryItemsByProcedureConfigId(reqDto);
			if(procedureInventoryItemlist.getListObject()!=null&& procedureInventoryItemlist.getListObject().size()>0  ) {
				
				List<ProcedureConfigurationInventoryItemDto> list =  procedureInventoryItemlist.getListObject();
				responseDto.setProcedureConfigId(list.getFirst().getProcedureConfigId());
			}
			responseDto.setInventoryItemList(procedureInventoryItemlist.getListObject());
			
			Response procedureNoteItemlist = iProcedureConfigurationNoteDao.getNoteByProcedureConfigId(reqDto);
			if(procedureNoteItemlist.getListObject()!=null&& procedureNoteItemlist.getListObject().size()>0  ) {
				
				List<ProcedureConfigurationNoteDto> list =  procedureNoteItemlist.getListObject();
				responseDto.setProcedureConfigId(list.getFirst().getProcedureConfigId());
			}
			responseDto.setNoteList(procedureNoteItemlist.getListObject());

			Response procedureEquipmentlist = iProcedureConfigurationEquipmentDao.getEquipmentByProcedureConfigId(reqDto);
			if(procedureEquipmentlist.getListObject()!=null&& procedureEquipmentlist.getListObject().size()>0  ) {
				
				List<ProcedureConfigurationEquipmentDto> list =  procedureEquipmentlist.getListObject();
				responseDto.setProcedureConfigId(list.getFirst().getProcedureConfigId());
			}
			responseDto.setEquipmentDetailList(procedureEquipmentlist.getListObject());

			Response procedurePatientEducationObj = iProcedureConfigurationPatientEducationDao.getPatientEducationByProcedureConfigId(reqDto);
			if(procedurePatientEducationObj.getObject()!=null) {
				
				ProcedureConfigurationPatientEducationDto list =  (ProcedureConfigurationPatientEducationDto) procedurePatientEducationObj.getObject();
				responseDto.setProcedureConfigId(list.getProcedureConfigId());
			}
			responseDto.setPatientEduObj((ProcedureConfigurationPatientEducationDto) procedurePatientEducationObj.getObject());

			Response procedureServiceList = iProcedureConfigurationServiceDao.getServiceListByProcedureConfigId(reqDto);
			if(procedureServiceList.getListObject()!=null&& procedureServiceList.getListObject().size()>0  ) {
				
				List<ProcedureConfigurationServiceDto> list =  procedureServiceList.getListObject();
				responseDto.setProcedureConfigId(list.getFirst().getProcedureConfigId());
			}
			responseDto.setServiceList(procedureServiceList.getListObject());
			
			Response ottres = iProcedureTemplateDao.getOTT(responseDto.getProcedureConfigId());
			ProcedureConfigurationDto dto = new ProcedureConfigurationDto();
			if(ottres.getObject()!=null) {
				
				 dto = (ProcedureConfigurationDto) ottres.getObject();
			}
			responseDto.setOperationTheatreTypeId(dto.getOperationTheatreTypeId());
			responseDto.setProcedureType(dto.getProcedureTypeName());
			responseDto.setTheatreTypeName(dto.getTheatreTypeName());
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, responseDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProcedureConfigList(int orgId, int unitId) throws ApplicationException {
		try {

			return iProcedureTemplateDao.getProcedureConfigList(orgId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getOTTThetreAndTableByServiceId(int serviceId, int orgId, int unitId) throws ApplicationException {
		try {
			OTTDetailsVO finalDto = new OTTDetailsVO();
			Response res=  iProcedureTemplateDao.getOTTThetreAndTableByServiceId(serviceId,orgId, unitId);
			
			if(res.getObject()!= null) {
			
				finalDto = (OTTDetailsVO) res.getObject();
				
				Response OTheatreRes =  iOperationTheatreDao.getOperationTheatreByTypeId(finalDto.getTheatreTypeId(), orgId, unitId);
				List<OperationTheatreDto> list = new ArrayList<>();
				if(OTheatreRes.getListObject().size()>0 && OTheatreRes.getListObject()!=null) {
					
					list.addAll(OTheatreRes.getListObject());
					OperationTableTypeDto dto = new OperationTableTypeDto();
				
					for (OperationTheatreDto operationTheatreDto : list) {
						dto.setUnitId(unitId);
						dto.setOrgId(orgId);
						dto.setOperationTheatreId(operationTheatreDto.getOperationTheatreId());
						Response tableRes =  iOperationTableDao.getActiveOperationTableListByTheatreId(dto);
						
						if(tableRes.getListObject().size()>0 && tableRes.getListObject()!= null) {
							operationTheatreDto.setOperationTableList(tableRes.getListObject());
						}
					}
					finalDto.setOperationTheatreList(list);
					
				}
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null,finalDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProcedureConfigureOnOTBillingCompletion(int admissionId, int encounterId, int serviceId, int orgId, int unitId,String date, String fromTime, String toTime,String OTToDate) {
		try {
			ProcedureConfigurationDto reqDto = new ProcedureConfigurationDto();
			reqDto.setServiceId(serviceId);
			reqDto.setOrgId(orgId);
			reqDto.setUnitId(unitId);
			Response res = getProcedureConfigTemplate(reqDto);
			if(res.getObject() !=null ) {
				ProcedureConfigurationDto responseDto = (ProcedureConfigurationDto) res.getObject();
				
				
				Integer  patientTypeId = null;
				Integer  visitTypeId = null;
				Integer  patientClassId = null;
				Integer  billingBedCategoryId = null;
				Integer  paymentEntitlementId = null;
				Integer  contractId = null;
				Integer  payeeId = null;
				Integer defaultSelfTariffId = null;

				UnitApplicationConfigurationDto uAppConfDto = new UnitApplicationConfigurationDto();
				uAppConfDto.setOrganizationId(orgId);
				uAppConfDto.setUnitId(unitId);
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(uAppConfDto);
				if (appConfigRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && appConfigRes.getObject() != null) 
				{
					uAppConfDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
				}
				if(admissionId > 0 ) 
				{
					
					Response admissionDto = iBillOfMaterialDao.getAdmissionsById(admissionId);
					AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
						patientTypeId = adminDto.getPatientCategoryId();
						billingBedCategoryId = adminDto.getBedBillingCategoryId();
						paymentEntitlementId = adminDto.getPaymentEntitlementId();
						contractId = adminDto.getContractId();
						payeeId = adminDto.getPayeeId();
						visitTypeId = adminDto.getVisitTypeId();
						patientClassId = adminDto.getPatientClassId();
						defaultSelfTariffId = adminDto.getDefaultSelfTariffId();
				}
				
				if(encounterId > 0) 
				{
					
					Response encounterRes = iEncounterMasterService.getEncounterDetailsById(encounterId);
					EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
						patientTypeId = encounterMasterDto.getPatientCategoryId();
						paymentEntitlementId = encounterMasterDto.getPaymentEntitlementTypeId()!=null?encounterMasterDto.getPaymentEntitlementTypeId():1;
						visitTypeId = 1;
						contractId = encounterMasterDto.getContractId();
						payeeId = encounterMasterDto.getPayeeId();
						patientClassId = encounterMasterDto.getPatientClassId();
						billingBedCategoryId = uAppConfDto.getOpdBillingCatId();
						defaultSelfTariffId = encounterMasterDto.getPatientSponsorDetailsDtosList().getFirst().getTariffId();
				}
				
				
				if(responseDto.getServiceList() != null && responseDto.getServiceList().size()>0) {
					
					for (ProcedureConfigurationServiceDto serviceDto : responseDto.getServiceList()) {
						
						ContractConsumptionDto contrcDto = new ContractConsumptionDto();
						
						contrcDto.setOrganizationId(orgId);
						contrcDto.setUnitId(unitId);
						contrcDto.setAdmissionId(admissionId!=0?admissionId:(encounterId!=0?encounterId:null));
						contrcDto.setEncounterId(admissionId!=0?admissionId:(encounterId!=0?encounterId:null));
						contrcDto.setBillingBedCategoryId(billingBedCategoryId);
						contrcDto.setPatientCategoryId(patientTypeId);
						contrcDto.setVisitTypeId(visitTypeId);
						contrcDto.setServiceId(serviceDto.getServiceId());
						contrcDto.setTariffId(defaultSelfTariffId);
						contrcDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd"), "yyyy-MM-dd"));
						Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contrcDto, false, false);
						
						if(serviceRateRes.getObject()!=null) {
							
							ContractConsumptionDto 	consumptionDto = (ContractConsumptionDto) serviceRateRes.getObject();
							serviceDto.setRate(consumptionDto.getFinalPrice());
						}
						
					}
				}
				
				if(responseDto.getEquipmentDetailList()!=null && responseDto.getEquipmentDetailList().size()>0) {
					
					 Double rate = null;
					 Double serviceRate = null;
					 BigDecimal serviceAmt = BigDecimal.ZERO;
					 BigDecimal taxAmt = BigDecimal.ZERO;
					
					 
					for (ProcedureConfigurationEquipmentDto OtEquipmentDto : responseDto.getEquipmentDetailList()) 
					{
						BigDecimal netAmt = BigDecimal.ZERO;
						Response equipmentRes = iProcedureOrderEquipmentDao.getserviceIdByAssetCat(OtEquipmentDto.getItemId(),orgId,unitId,visitTypeId,0);
						OtEquipmentDto.setFromDate(date.substring(0,10));
						OtEquipmentDto.setToDate(OTToDate.substring(0,10));
						OtEquipmentDto.setFromTime(fromTime);
						OtEquipmentDto.setToTime(toTime);
						OtEquipmentDto.setAmount(netAmt.doubleValue());
						
						if(equipmentRes.getObject()!= null) 
						{
							ProcedureOrderMasterDto equipserviceId = (ProcedureOrderMasterDto) equipmentRes.getObject();
							OtEquipmentDto.setServiceId(equipserviceId.getServiceId());
							OtEquipmentDto.setMeasurementUnitId(equipserviceId.getMeasurementUnitId());
							OtEquipmentDto.setDuration(equipserviceId.getDuration());
							if(equipserviceId.getServiceId()!=null && equipserviceId.getMeasurementUnitId()!=null && equipserviceId.getDuration()!=null)
							{
								ContractConsumptionDto consumptionDtoFirst = new ContractConsumptionDto();
								 consumptionDtoFirst.setOrganizationId(orgId);
								 consumptionDtoFirst.setUnitId(unitId);
								 consumptionDtoFirst.setVisitTypeId(visitTypeId);
								 consumptionDtoFirst.setPatientTypeId(patientTypeId);
								 consumptionDtoFirst.setBillingBedCategoryId(billingBedCategoryId);
								 consumptionDtoFirst.setPaymentEntitlementId(paymentEntitlementId);
								 consumptionDtoFirst.setServiceId(OtEquipmentDto.getServiceId());
								 consumptionDtoFirst.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd"), "yyyy-MM-dd"));
								 consumptionDtoFirst.setAdmissionId(admissionId!=0?admissionId: null);
								 consumptionDtoFirst.setEncounterId(encounterId!=0?encounterId: null);
								 consumptionDtoFirst.setDefaultSelfTariffId(defaultSelfTariffId);
								 consumptionDtoFirst.setSelfPayeeId(payeeId);
								 
							Response selfRes =iContractConsumptionService.consumeServiceFromContract(consumptionDtoFirst);
								consumptionDtoFirst=(ContractConsumptionDto)selfRes.getObject();
							
								Date fromDate = GlobalCommonDateUtils.getDate(date.substring(0,10)+ " " + fromTime,"dd-M-yyyy HH:mm");
								Date toDate = GlobalCommonDateUtils.getDate(OTToDate.substring(0,10) + " " + toTime,"dd-M-yyyy HH:mm");
							
								double noOfUnits = 0.0;
								long duration = equipserviceId.getDuration();
								long diff = toDate.getTime() - fromDate.getTime();
								switch(equipserviceId.getMeasurementUnitId())
								{
								case 1 : //minute
									long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
									
									noOfUnits = (((double)diffMinutes) / duration);
									
									if((((double)diffMinutes) % duration)>0.0)
										noOfUnits = (int)noOfUnits + 1;
									
									serviceRate = consumptionDtoFirst.getServiceRate()*noOfUnits;
									rate=consumptionDtoFirst.getServiceRate()*noOfUnits;
									break;
								case 3 : //days
									diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
									
									noOfUnits = ((((double)diffMinutes/60)/24) / duration);
									
									if((((double)diffMinutes/60) % duration)>0.0)
										noOfUnits = (int)noOfUnits + 1;
									
									//serviceRateperMin = consumptionDtoFirst.getServiceRate()/60;
									serviceRate = consumptionDtoFirst.getServiceRate()*noOfUnits;
									rate=consumptionDtoFirst.getServiceRate()*noOfUnits;
									break;
								}
								
								Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(OtEquipmentDto.getServiceId(),unitId);
								if(tax != null)
								{
									serviceAmt = new BigDecimal(serviceRate);
									taxAmt = new BigDecimal(rate).multiply(serviceAmt.multiply((new BigDecimal(tax.getTaxPercentage()).multiply(new BigDecimal(0.01)))));
									netAmt = new BigDecimal(rate).add(taxAmt);
								}else
								{
									serviceAmt = new BigDecimal(serviceRate);
									netAmt = new BigDecimal(rate);
								}		
								OtEquipmentDto.setAmount(netAmt.doubleValue());
							}
						}
					}
					
					
				}
				
				if(responseDto.getInventoryItemList()!=null && responseDto.getInventoryItemList().size()>0) {
					
					for (ProcedureConfigurationInventoryItemDto dto : responseDto.getInventoryItemList()) {
						
						Integer prodCategId = null;
						Integer assetCatId = null;
						if(dto.isImplant() == true) {
							prodCategId = dto.getImpProductId();
							assetCatId = dto.getImpAssetCategoryId();
						}else {
							prodCategId = dto.getMedProductId();
							assetCatId = dto.getMedAssetCategoryId();

						}
						
						Response res1 = OTCompletionDao.getBatchListByItemId(sessionService.getStoreId(),dto.getItemId(),orgId,unitId);
						if(res1.getStatus().equals(SUCCESS)) {
							List<BatchDto> batchList = res1.getListObject();
							for (BatchDto  batchDto : batchList) {
								Response res2 = OTCompletionDao.getUOMListByBatchId(batchDto.getBatchId(),orgId,unitId);
								if(res2.getStatus().equals(SUCCESS)) {
									batchDto.setUom(res2.getListObject());
								}
							}
							dto.setBatch(batchList);
						}
						
						List<Integer> prodCatId = new ArrayList<Integer>();
						prodCatId.add(prodCategId);
						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatId, paymentEntitlementId);
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
							dto.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							dto.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
						}
						
						PatientConsession patientConsession = new PatientConsession();
						if(encounterId != 0 && encounterId > 0) {
							patientConsession = GlobalDao.getPatientConsessionEncounter(dto.getItemId(), 
									encounterId, 
									patientTypeId, 
									assetCatId,
									prodCategId,
									null,null);
						}
						else if(admissionId!= 0 && admissionId > 0) {
							patientConsession = GlobalDao.getPatientConsessionAdmission(dto.getItemId(), 
									admissionId, 
									patientTypeId, 
									assetCatId,
									prodCategId);
						}
						
						if(patientConsession != null && patientConsession.isMarkupStatus()==false) {
							dto.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							dto.setMarkupStatus(patientConsession.isMarkupStatus());
						}
						else {
							dto.setPatientClassPercentage(0);
							dto.setMarkupStatus(false);
						}
					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, null, null,responseDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null,null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
