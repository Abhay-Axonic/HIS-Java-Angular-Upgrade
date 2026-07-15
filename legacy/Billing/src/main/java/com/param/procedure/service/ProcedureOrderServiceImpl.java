package com.param.procedure.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.adt.master.global.model.ReasonMaster;
import com.param.billing.global.transaction.service.ITPackageConsumptionCapDetailsService;
import com.param.billing.global.transaction.service.ITPackageConsumptionServiceDetailService;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.billing.unit.service.ITPeriodicityServiceOrdersService;
import com.param.common.constants.Screen;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.global.billing.dao.IpdChargesDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.ServletUtils;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IOTCompletionDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.items.UomResponse;
import com.param.global.mail.IMailingService;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.otims.dao.IAdditionalOtTimeDao;
import com.param.global.otims.dao.IImplantDetailsDao;
import com.param.global.otims.dao.IMedicationDetailsDao;
import com.param.global.otims.dao.IOTCompletionNoteDao;
import com.param.global.otims.dao.IOTTransferRequestDao;
import com.param.global.otims.dao.IOperationTheatreDao;
import com.param.global.otims.dao.IOtCompletionTimeSheetDao;
import com.param.global.otims.dao.IOtcCompletionInvImplantsDao;
import com.param.global.otims.dao.IOtcCompletionInvMedicationDao;
import com.param.global.otims.dao.IProcedureOrderChecklistDao;
import com.param.global.otims.dao.IProcedureOrderConsentDao;
import com.param.global.otims.dao.IProcedureOrderEquipmentDao;
import com.param.global.otims.dao.IProcedureOrderPatientEducationDao;
import com.param.global.otims.dao.IProcedureOrderServiceDao;
import com.param.global.otims.dao.IProcedureOrderStaffDao;
import com.param.global.otims.dao.IResourceChargeConfigurationDao;
import com.param.global.otims.dao.ISubSequentDetailsDao;
import com.param.global.otims.dao.ISubSequentDetailsInfoDao;
import com.param.global.otims.dao.ISubSequentProcedureConfigDao;
import com.param.global.otims.dao.ISurgeryResourceConfigDao;
import com.param.global.otims.dto.AdditionalOtTimeDto;
import com.param.global.otims.dto.BatchDto;
import com.param.global.otims.dto.ImplantDetailsDto;
import com.param.global.otims.dto.MedicationDetailsDto;
import com.param.global.otims.dto.OTCompletionNoteDto;
import com.param.global.otims.dto.OTManagerDashboardDto;
import com.param.global.otims.dto.OTTransferRequestDto;
import com.param.global.otims.dto.OtCompletionTimeSheetDto;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.ProcedureOrderChecklistDto;
import com.param.global.otims.dto.ProcedureOrderComponentDetailsDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderEquipmentDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureOrderPatientEducationDto;
import com.param.global.otims.dto.ProcedureOrderServiceDto;
import com.param.global.otims.dto.ProcedureOrderStaffDto;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.global.otims.dto.SubSequentDetailsDto;
import com.param.global.otims.dto.SubSequentDetailsInfoDto;
import com.param.global.otims.dto.SubSequentProcedureConfigDto;
import com.param.global.otims.dto.SurgeryResourceConfigByCritertiaReqDto;
import com.param.global.otims.dto.SurgeryResourceConfigDto;
import com.param.global.otims.dto.SurgicalCalenderMapperDto;
import com.param.global.otims.service.ISurgicalCalendarMasterService;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.procedure.dao.IBillOfMaterialDao;
import com.param.global.procedure.dto.BillOfMaterialDto;
import com.param.global.procedure.dto.OTCompletionItemRequest;
import com.param.global.procedure.dto.OTCompletionRequest;
import com.param.global.procedure.dto.OtCompletionMedicalGasDto;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOTCompletionService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IUnitServiceTariffMasterService;
import com.param.global.service.global.IGlobalService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.ot.dao.IImplantIndentAcceptanceOTDao;
import com.param.otims.model.ProcedureOrderMaster;
import com.param.otims.model.ProcedureReqMaster;
import com.param.procedure.dao.IOTCompletionCssdKitDao;
import com.param.procedure.dao.IOTCompletionDetailsDao;
import com.param.procedure.dao.IOTServiceDao;
import com.param.procedure.dao.IOtCompletionEquipmentDao;
import com.param.procedure.dao.IOtCompletionMedicalGasDao;
import com.param.procedure.dao.IOtCompletionStaffDao;
import com.param.procedure.dao.IProcedureOrderDao;
import com.param.procedure.dao.IProcedureReqDao;
import com.param.procedure.dto.CssdKitVO;
import com.param.procedure.dto.OTCompletionCssdKitDto;
import com.param.procedure.dto.OTCompletionDetailsDto;
import com.param.procedure.dto.OTServiceDto;
import com.param.procedure.dto.OtCompletionEquipmentDto;
import com.param.procedure.dto.ProcedureOrderListReqDto;
import com.param.procedure.dto.ProcedureOrderReqDto;
import com.param.procedure.dto.ProcedureOrderServSrchReqDto;
import com.param.procedure.dto.ProcedureRequestDto;
import com.param.procedure.dto.VOProcedureOrderMasterDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class ProcedureOrderServiceImpl implements IProcedureOrderService, ICommonConstants ,IBillingCommonConstants{

	@Autowired
	private IProcedureOrderDao iProcedureOrderDao;
	@Autowired
	private IOTServiceDao iOTServiceDao;
	@Autowired
	private IOtCompletionStaffDao iOtCompletionStaffDao;
	@Autowired
	IOTCompletionDao OTCompletionDao;
	@Autowired
	private IProcedureReqDao iProcedureReqDao;
	@Autowired
	private IOtCompletionTimeSheetDao iOtCompletionTimeSheetDao;
	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	@Autowired
	private ITPackageConsumptionCapDetailsService iTPackageConsumptionCapDetailsService;
	@Autowired
	private ITPackageConsumptionServiceDetailService iTPackageConsumptionServiceDetailService;
	@Autowired
	private IOrderMasterService iOrderMasterService;
	@Autowired
	private ISubSequentProcedureConfigDao iSubSequentProcedureConfigDao;
	@Autowired
	private ISurgeryResourceConfigDao iSurgeryResourceConfigDao;
	@Autowired
	IAdditionalOtTimeDao iAdditionalOtTimeDao;
	@Autowired
	IOTCompletionService iOTCompletionService;
	@Autowired
	private ISurgicalCalendarMasterService iSurgicalCalendarMasterService;
	@Autowired
	private IResourceChargeConfigurationDao iResourceChargeConfigurationDao;
	@Autowired
	IMedicationDetailsDao iMedicationDetailsDao;
	@Autowired
	private IImplantDetailsDao iImplantDetailsDao;
	@Autowired
	private IOtcCompletionInvMedicationDao iOtcCompletionInvMedicationDao;
	@Autowired
	private IOtcCompletionInvImplantsDao iOtcCompletionInvImplantsDao;
	@Autowired
	IOperationTheatreDao iOperationTheatreDao;
	@Autowired
	SessionService sessionService;
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	@Autowired
	IGlobalService GlobalService;
	@Autowired
	IGlobalDao GlobalDao;
	@Autowired
	private IUnitServiceTariffMasterService iUnitServiceTariffMasterService;
	@Autowired
	IImplantIndentAcceptanceOTDao iImplantIndentAcceptanceDao;
	@Autowired
	IProcedureOrderConsentDao iProcedureOrderConsentDao;
	@Autowired
	IProcedureOrderServiceDao iProcedureOrderServiceDao;
	@Autowired
	IProcedureOrderChecklistDao iProcedureOrderChecklistDao;
	@Autowired
	IProcedureOrderPatientEducationDao iProcedureOrderPatientEducationDao;
	@Autowired
	IProcedureOrderStaffDao iProcedureOrderStaffDao;
	@Autowired 
	IMailingService iMailingService;
	@Autowired
	IProcedureOrderEquipmentDao iProcedureOrderEquipmentDao;
	@Autowired 
	IBillOfMaterialDao iBillOfMaterialDao;
	@Autowired 
	IpdChargesDao ipdChargesDao;
	@Autowired
	IOTCompletionDetailsDao iOTCompletionDetailsDao;
	@Autowired
	IOTCompletionCssdKitDao iOTCompletionCssdKitDao;
	@Autowired
	IOTCompletionNoteDao iOTCompletionNoteDao;
	@Autowired
	IOtCompletionEquipmentDao iOtCompletionEquipmentDao;
	@Autowired
	IOtCompletionMedicalGasDao iOtCompletionMedicalGasDao;
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	IOTTransferRequestDao iOTTransferRequestDao;
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	@Autowired
	ITPeriodicityServiceOrdersService iTPeriodicityServiceOrdersService;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IWalkInSalesService iWalkInSalesService;
	
	@Autowired
	ISubSequentDetailsDao iSubSequentDetailsDao;
	
	@Autowired
	ISubSequentDetailsInfoDao iSubSequentDetailsInfoDao;

	
	static String BASEURL = "";

	@Override
	@Transactional
	public Response getProcedureOrderById(int proReqId, int tariffId, int storeId, HttpServletRequest request) throws Exception {
		try {
			 Double rate = null;
			 Double serviceRate = null;
			 Double serviceRateperMin = null;
			 BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;
				
			List<OTServiceDto> otServiceList = new ArrayList<OTServiceDto>();
			BASEURL = ServletUtils.getBaseUrl(request);
			Boolean otimsStatus = false;
			ProcedureOrderReqDto resDto = new ProcedureOrderReqDto();
			// get procedure orders
			Response procedureOrderRes = iProcedureOrderDao.getProcedureOrderById(proReqId);
			resDto.setProcedureOrders(procedureOrderRes.getListObject());
			Integer totalDuration = 160;
			Integer orgId = resDto.getProcedureOrders().get(0).getOrgId();
			Integer unitId = resDto.getProcedureOrders().get(0).getUnitId();
			String UhidNumber = resDto.getProcedureOrders().get(0).getUhidNumber();
			Integer admissionId  = resDto.getProcedureOrders().get(0).getAdmissionId();
			Integer encounterId = resDto.getProcedureOrders().get(0).getEncounterId();
			Integer procedureStatusId = resDto.getProcedureOrders().get(0).getProcedureStatusId();
			Integer visitTypeId =  resDto.getProcedureOrders().get(0).getVisitTypeId();
			
			Integer  patientTypeId = null;
			Integer  billingBedCategoryId = null;
			Integer  paymentEntitlementId = null;
			Integer  contractId = null;
			Integer  payeeId = null;
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(orgId);
			unitApplicationConfigurationDto.setUnitId(unitId);
			Response configRes = iUnitApplicationBillingConfigurationDao
					.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if (configRes.getObject() != null) {
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) configRes.getObject();
				
			    billingBedCategoryId = unitApplicationConfigurationDto.getOpdBillingCatId();
				payeeId = unitApplicationConfigurationDto.getSelfPayeeId();
			}

			
			if(admissionId != null ) {
				
				Response admissionDto = ipdChargesDao.getAdmissionsById(admissionId);
				AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
				if(adminDto == null) {
					
					return new Response<>(ERROR, ERROR_CODE, "Patient may be pending for Nursing acceptance or discharged...!! ", null, null);

				}else {
					
					  patientTypeId = adminDto.getPatientCategoryId();
					  billingBedCategoryId = adminDto.getBedBillingCategoryId();
					  paymentEntitlementId = adminDto.getPaymentEntitlementId();
					  contractId = adminDto.getContractId();
					  payeeId = adminDto.getPayeeId();
					
				}
				 				
			}
			
			if(encounterId != null) {
				
				Response encounterRes = iEncounterMasterService.getEncounterDetailsById(encounterId);
				EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
				
				if(encounterMasterDto == null) {
					
					return new Response<>(ERROR, ERROR_CODE, "Patient is not yet checked in ...!! ", null, null);

				}else{
					
					  patientTypeId = encounterMasterDto.getPatientSponsorDetailsDtosList().get(0).getPatientTypeId();
					  paymentEntitlementId = encounterMasterDto.getPaymentEntitlementTypeId()!=null?encounterMasterDto.getPaymentEntitlementTypeId():1;
					  visitTypeId = 1;
					  contractId = encounterMasterDto.getContractId();
					  payeeId = encounterMasterDto.getPayeeId();
				}
				
			}
			
			 List<OTCompletionNoteDto> oTCompletionNoteList  = new ArrayList<>();
			 List<OtCompletionEquipmentDto> Equipmentlist = new ArrayList<>();
			 List<BillOfMaterialDto> billOfresList = new ArrayList<>();
             List<ProcedureOrderEquipmentDto> orderEquipList = new ArrayList<>();

             List<MedicationDetailsDto> medDtoList = new ArrayList<>();
			List<ImplantDetailsDto> implantDtoList = new ArrayList<>();
			
			if(procedureStatusId.equals(3)) {
				
				Response resRquip = iProcedureOrderDao.getScheduleEquipment(resDto.getProcedureOrders().get(0).getProReqId());
				Equipmentlist.addAll(resRquip.getListObject());
			
				
					for (ProcedureOrderMasterDto dto : resDto.getProcedureOrders()) {
						
						ContractConsumptionDto contrcDto = new ContractConsumptionDto();
						
						contrcDto.setOrganizationId(orgId);
						contrcDto.setUnitId(unitId);
						contrcDto.setAdmissionId(admissionId!=null?admissionId:(encounterId!=null?encounterId:null));
						contrcDto.setEncounterId(admissionId!=null?admissionId:(encounterId!=null?encounterId:null));
						contrcDto.setBillingBedCategoryId(billingBedCategoryId);
						contrcDto.setPatientCategoryId(patientTypeId);
						contrcDto.setVisitTypeId(visitTypeId);
						contrcDto.setServiceId(dto.getServiceId());
						contrcDto.setTariffId(tariffId);
						contrcDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd"), "yyyy-MM-dd"));
						Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contrcDto, false, false);
						
						if(serviceRateRes.getObject()!=null) {
							
							ContractConsumptionDto 	consumptionDto = (ContractConsumptionDto) serviceRateRes.getObject();
							dto.setRate(consumptionDto.getFinalPrice());
						}
						
						MedicationDetailsDto medDto = new MedicationDetailsDto();
							
						medDto.setUnitId(unitId);
						medDto.setOrganizationId(orgId);
						medDto.setProcedureOrderId(dto.getProcedureOrderId());
						Response medListRes = iMedicationDetailsDao.getMedicationListByProcedureId(medDto);
						
						if(medListRes.getListObject()!=null && medListRes.getListObject().size()>0) {
							medDtoList.addAll(medListRes.getListObject());
						}

						ImplantDetailsDto implantDto = new ImplantDetailsDto();
						implantDto.setUnitId(unitId);
						implantDto.setOrganizationId(orgId);
						implantDto.setProcedureOrderId(dto.getProcedureOrderId());
						Response implantDtoListRes = iImplantDetailsDao.getImplantDetailsByProcedureId(implantDto,1);
						
						if(implantDtoListRes.getListObject()!=null && implantDtoListRes.getListObject().size()>0) {
							implantDtoList.addAll(implantDtoListRes.getListObject());
						}
						
						ProcedureOrderConsentDto consentDto = new ProcedureOrderConsentDto();
						consentDto.setProcedureOrderId(dto.getProcedureOrderId());
						consentDto.setOrganizationId(orgId);
						consentDto.setUnitId(unitId);
	                    Response equipmentRes1 = iProcedureOrderEquipmentDao.getEquipmentDetailsByProcedureOrderId(consentDto);
	                    orderEquipList.addAll(equipmentRes1.getListObject());
	                    
	                    for (OtCompletionEquipmentDto OtEquipmentDto : Equipmentlist) 
	                    {
	       				 
	                        for (ProcedureOrderEquipmentDto orderEquipDto : orderEquipList) {
	                       	 
	                            if(orderEquipDto.getItemId().equals(OtEquipmentDto.getItemId()) && orderEquipDto.getEquipmentTypeId().equals(OtEquipmentDto.getEquipmentTypeId())) {
	                           	 
	                           	 OtEquipmentDto.setProcedureOrderId(dto.getServiceId());
	                           	 }
	                      }
	       			 }
	                    
	                    SubSequentProcedureConfigDto subSequentProcedureConfigDto =  new SubSequentProcedureConfigDto();
	                    subSequentProcedureConfigDto.setOrgId(orgId);
	                    subSequentProcedureConfigDto.setSpecialityId(dto.getSpecialityId());
	                    subSequentProcedureConfigDto.setUnitId(unitId);
	                    List<SubSequentDetailsDto> subSequentList = new ArrayList<>();
	                    List<SubSequentDetailsDto> subSequentListFinal = new ArrayList<>();
	        			Response subSequentRes =  iSubSequentProcedureConfigDao.getSubSequentDetailsAndInfoListBySpecialityId(subSequentProcedureConfigDto);
	        			if(subSequentRes!=null && subSequentRes.getListObject()!=null && subSequentRes.getListObject().size()>0) {
	        				 subSequentList = subSequentRes.getListObject();
	        				for (SubSequentDetailsDto subSequentDetailsDto : subSequentList) {
	        					if(subSequentDetailsDto.getDiscPercentage()== null || subSequentDetailsDto.getDiscPercentage() == 0) {
	        						subSequentDetailsDto.setSkipZeroDiscount(true);
	        						Response subInfoListRes = iSubSequentDetailsInfoDao.getSubSequentDetailsInfo(subSequentDetailsDto);
	        						if(subInfoListRes!=null && subInfoListRes.getListObject()!=null && subInfoListRes.getListObject().size()>0) {
	        							List<SubSequentDetailsInfoDto>  subInfoList = subInfoListRes.getListObject();
	        							subSequentDetailsDto.setSubSequentDetailsInfoList(subInfoList);
	        						}
	        					}
	        					subSequentListFinal.add(subSequentDetailsDto);
	        				}
	        			}
	                    
	        		dto.setSubSequentDetailsList(subSequentListFinal);
	        		
	        		
					Response procedureNoteItemlist = iProcedureOrderDao.getNoteByProcedureConfigId(dto);
					oTCompletionNoteList.addAll(procedureNoteItemlist.getListObject());
					
					BillOfMaterialDto billOfMaterialDto = new BillOfMaterialDto();
					billOfMaterialDto.setProcedureId(dto.getServiceId());
					billOfMaterialDto.setOrganizationId(orgId);
					billOfMaterialDto.setUnitId(unitId);
					billOfMaterialDto.setBillingBedCatId(billingBedCategoryId);
				
					List<BillOfMaterialDto> billOfresList1 = new ArrayList<>();
					Response billOfres =  iBillOfMaterialDao.getBillOfMaterialListByProcedureId(billOfMaterialDto);
					billOfresList1.addAll(billOfres.getListObject());
					billOfresList.addAll(billOfresList1);
					//set Sum of Amt to procedure (charges)
					double sumOfAmt = 0.0;
					if(billOfresList1!=null && billOfresList1.size()>0) {
						for (BillOfMaterialDto billOfMaterialDto1 :billOfresList1) {
							billOfMaterialDto1.setOriginalAmount(billOfMaterialDto1.getAmount());
							sumOfAmt = sumOfAmt + billOfMaterialDto1.getAmount();
						}
					}
					dto.setAmount(sumOfAmt);
					dto.setRate(sumOfAmt);
						
						for (OtCompletionEquipmentDto OtEquipmentDto : Equipmentlist) 
						{
							
							Response equipmentRes = iProcedureOrderEquipmentDao.getserviceIdByAssetCat(OtEquipmentDto.getItemId(),orgId,unitId,visitTypeId,0);
							
							if(equipmentRes.getObject()!= null) 
							{
								ProcedureOrderMasterDto equipserviceId = (ProcedureOrderMasterDto) equipmentRes.getObject();
								OtEquipmentDto.setServiceId(equipserviceId.getServiceId());
								OtEquipmentDto.setMeasurementUnitId(equipserviceId.getMeasurementUnitId());
								OtEquipmentDto.setDuration(equipserviceId.getDuration());
								
							}
							OtEquipmentDto.setFromDate(OtEquipmentDto.getProdeureDate());
							OtEquipmentDto.setToDate(OtEquipmentDto.getProdeureDate());
							
							if(equipmentRes.getObject()!= null) 
							{
								ProcedureOrderMasterDto equipserviceId = (ProcedureOrderMasterDto) equipmentRes.getObject();
								//OtEquipmentDto.setServiceId(equipserviceId.getServiceId());
								
								if(OtEquipmentDto.getProcedureOrderId()==null) 
								{
									OtEquipmentDto.setProcedureOrderId(resDto.getProcedureOrders().get(0).getServiceId());
								}
								
								ContractConsumptionDto consumptionDto = null;
								
								if(OtEquipmentDto.getProdeureDate()!=null && OtEquipmentDto.getToTime()!=null)
								 {
									 ContractConsumptionDto consumptionDtoFirst = new ContractConsumptionDto();
										 consumptionDtoFirst.setOrganizationId(orgId);
										 consumptionDtoFirst.setUnitId(unitId);
										 consumptionDtoFirst.setVisitTypeId(visitTypeId);
										 consumptionDtoFirst.setPatientTypeId(patientTypeId);
										 consumptionDtoFirst.setBillingBedCategoryId(billingBedCategoryId);
										 consumptionDtoFirst.setPaymentEntitlementId(SELF);
										 consumptionDtoFirst.setServiceId(equipserviceId.getServiceId());
										 consumptionDtoFirst.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd"), "yyyy-MM-dd"));
										 consumptionDtoFirst.setAdmissionId(admissionId!=null?admissionId: null);
										 consumptionDtoFirst.setEncounterId(encounterId!=null?encounterId: null);
										 consumptionDtoFirst.setDefaultSelfTariffId(tariffId);
										 consumptionDtoFirst.setSelfPayeeId(payeeId);
										 
									Response selfRes =iContractConsumptionService.consumeServiceFromContract(consumptionDtoFirst);
										consumptionDtoFirst=(ContractConsumptionDto)selfRes.getObject();
									
										String fromDateString = GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(OtEquipmentDto.getProdeureDate(),"dd-MM-yyyy"), "yyyy-MM-dd");
										String toDateString = GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(OtEquipmentDto.getProdeureDate(),"dd-MM-yyyy"), "yyyy-MM-dd");
										
										//calculate no of units
										Date fromDate = GlobalCommonDateUtils.getDate(fromDateString+ " " + OtEquipmentDto.getFromTime(),"dd-MM-yyyy HH:mm");
										Date toDate = GlobalCommonDateUtils.getDate(toDateString + " " + OtEquipmentDto.getToTime(),"dd-MM-yyyy HH:mm");
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
			}
	
			// get SubSequent Charges 
			
			for (ProcedureOrderMasterDto dto : resDto.getProcedureOrders()) {
				
				 SubSequentProcedureConfigDto subSequentProcedureConfigDto =  new SubSequentProcedureConfigDto();
                 subSequentProcedureConfigDto.setOrgId(orgId);
                 subSequentProcedureConfigDto.setSpecialityId(dto.getSpecialityId());
                 subSequentProcedureConfigDto.setUnitId(unitId);
                 List<SubSequentDetailsDto> subSequentList = new ArrayList<>();
                 List<SubSequentDetailsDto> subSequentListFinal = new ArrayList<>();
     			Response subSequentRes =  iSubSequentProcedureConfigDao.getSubSequentDetailsAndInfoListBySpecialityId(subSequentProcedureConfigDto);
     			if(subSequentRes!=null && subSequentRes.getListObject()!=null && subSequentRes.getListObject().size()>0) {
     				 subSequentList = subSequentRes.getListObject();
     				for (SubSequentDetailsDto subSequentDetailsDto : subSequentList) {
     					if(subSequentDetailsDto.getDiscPercentage()== null || subSequentDetailsDto.getDiscPercentage() == 0) {
     						subSequentDetailsDto.setSkipZeroDiscount(true);
     						Response subInfoListRes = iSubSequentDetailsInfoDao.getSubSequentDetailsInfo(subSequentDetailsDto);
     						if(subInfoListRes!=null && subInfoListRes.getListObject()!=null && subInfoListRes.getListObject().size()>0) {
     							List<SubSequentDetailsInfoDto>  subInfoList = subInfoListRes.getListObject();
     							subSequentDetailsDto.setSubSequentDetailsInfoList(subInfoList);
     						}
     					}
     					subSequentListFinal.add(subSequentDetailsDto);
     				}
     			}
                 
     		dto.setSubSequentDetailsList(subSequentListFinal);
     		
     			if(!procedureStatusId.equals(3)) {
     			
     				BillOfMaterialDto billOfMaterialDto = new BillOfMaterialDto();
     				billOfMaterialDto.setProcedureId(dto.getServiceId());
     				billOfMaterialDto.setOrganizationId(orgId);
     				billOfMaterialDto.setUnitId(unitId);
     				billOfMaterialDto.setBillingBedCatId(billingBedCategoryId);
    		
     				List<BillOfMaterialDto> billOfresList1 = new ArrayList<>();
     				Response billOfres =  iBillOfMaterialDao.getBillOfMaterialListByProcedureId(billOfMaterialDto);
     				billOfresList1.addAll(billOfres.getListObject());
     			//	billOfresList.addAll(billOfresList1);
     				//set Sum of Amt to procedure (charges)
     				double sumOfAmt = 0.0;
     				if(billOfresList1!=null && billOfresList1.size()>0) {
     					for (BillOfMaterialDto billOfMaterialDto1 :billOfresList1) {
     						sumOfAmt = sumOfAmt + billOfMaterialDto1.getAmount();
     					}
     				}
     				dto.setOriginalAmount(sumOfAmt);
     			}
     		
				
			}
			
			
			// get OT services
			//resDto.setBillOfresList(billOfresList);
			
			resDto.setoTCompletionNoteList(oTCompletionNoteList);
			Response otServiceRes = iOTServiceDao.getOtServicesByProReqId(proReqId);
			otServiceList.addAll(otServiceRes.getListObject());
		
			// get time sheet
			OtCompletionTimeSheetDto otTimeSheet = new OtCompletionTimeSheetDto();
			OtCompletionTimeSheetDto otTimeSheet1 = new OtCompletionTimeSheetDto();
			Response timeSheetRes = iOtCompletionTimeSheetDao.getOtTimeSheetByProReq(proReqId);
			otTimeSheet1 = (OtCompletionTimeSheetDto) timeSheetRes.getObject();
			
			// get Ot staff
			List<BillOfMaterialDto> otStaffList = new ArrayList<BillOfMaterialDto>();
			Response otStaffCompletion = iOtCompletionStaffDao.getOtCompletionStaffByProReqId(proReqId,orgId,unitId,billingBedCategoryId);
			if(otStaffCompletion.getListObject().size()>0) {
				otStaffList.addAll(otStaffCompletion.getListObject());
				
				for (BillOfMaterialDto billOfMaterialDto : otStaffList) {
					List<SpecialityMasterDto> doctor = new ArrayList<>();
					if(billOfMaterialDto.getSpecilityId()!=null) {
					Response doctorList = iOtCompletionStaffDao.getDoctorList(billOfMaterialDto);
					if(doctorList.getListObject().size()>0) {
						doctor.addAll(doctorList.getListObject());
						billOfMaterialDto.setDoctorList(doctor);
					}
				}
				}
				
				billOfresList.addAll(otStaffList);
			}
			
			// get Ot Note 
			Response otNote = iOTCompletionNoteDao.getOtNoteList(proReqId);
			if(otNote.getListObject().size()>0) {
				oTCompletionNoteList.addAll(otNote.getListObject());	
			}
			
			// get OTdetails
			Response otCompletionDetails = iOTCompletionDetailsDao.getOtCompletionDetials(proReqId);
			if(otCompletionDetails.getObject()!= null) {
				OTCompletionDetailsDto dto = (OTCompletionDetailsDto) otCompletionDetails.getObject();
				resDto.setOtCompletionDetailsDto(dto);
			}
			
			// get CSSD Kit 
			List<OTCompletionCssdKitDto> cssdKit = new ArrayList<>();
			Response otCssdKit = iOTCompletionCssdKitDao.getOtCSSDKit(proReqId);
			if(otCssdKit.getListObject().size()>0) {
				
				cssdKit.addAll(otCssdKit.getListObject());	
				
				for (OTCompletionCssdKitDto otCompletionCssdKitDto : cssdKit) {
					
					CssdKitVO d= new CssdKitVO();
					d.setKitId(otCompletionCssdKitDto.getKitId());
					d.setStoreId(otCompletionCssdKitDto.getStoreId());
					d.setOrganizationId(otCompletionCssdKitDto.getOrganizationId());
					d.setUnitId(otCompletionCssdKitDto.getUnitId());
					
					 Response batchlistCssd = iProcedureOrderDao.getSterilizedKitBatchList(d);
					 List<CssdKitVO> cssdbatchKit = new ArrayList<>();
					 if(batchlistCssd.getListObject().size()>0) {
						 cssdbatchKit.addAll(batchlistCssd.getListObject());
                         for (CssdKitVO dto2 : cssdbatchKit) {
                             if(otCompletionCssdKitDto.getKitTransitionNo().equals(dto2.getKitTransitionId())) {
                                 otCompletionCssdKitDto.setKitExpiryDate(dto2.getKitExpiryDate());
                             }
                      }

						 otCompletionCssdKitDto.setBatchList(cssdbatchKit);
					 }
				}
				resDto.setOtCompletionCssdKitList(cssdKit);
			}
			
			// get OT Equipment 
			
			
			List<OtCompletionEquipmentDto> OtSaveEquipment = new ArrayList<>();
			Response otEquipment = iOtCompletionEquipmentDao.getOtCompletionEquipment(proReqId);
			if(otEquipment.getListObject().size()>0) {
				OtSaveEquipment.addAll(otEquipment.getListObject());
				Equipmentlist.addAll(OtSaveEquipment);
			}
			resDto.setEquipmentlist(Equipmentlist);
			
			// get OT Medical GAS 
			Response otMedicalGas = iOtCompletionMedicalGasDao.getOTMedicalGas(proReqId);
			if(otMedicalGas.getListObject().size()>0) {
				resDto.setOtCompletionMedicalGasList(otMedicalGas.getListObject());
			}
			
			// get OT Medication start 
			Response iOtcCompletionInvMedicatioRes =  iOtcCompletionInvMedicationDao.getOtMedications(proReqId);
			
			OtcCompletionInvMedicationDto ComplMedication = new OtcCompletionInvMedicationDto();
			ComplMedication.setUhidNumber(UhidNumber);
			Response medicationRes = OTCompletionDao.getIssueAcceptanceData(ComplMedication);
			List<OtcCompletionInvMedicationDto> medicationList = new ArrayList<OtcCompletionInvMedicationDto>();
			
			if(iOtcCompletionInvMedicatioRes.getListObject()!= null && iOtcCompletionInvMedicatioRes.getListObject().size()>0) {
				medicationList.addAll(iOtcCompletionInvMedicatioRes.getListObject());
				
			}
			
			if(medDtoList.size()>0) {
				for (MedicationDetailsDto  medDto : medDtoList) {
					OtcCompletionInvMedicationDto dto = new OtcCompletionInvMedicationDto();
					dto.setItemId(medDto.getItemId());
					dto.setItemName(medDto.getItemName());
					dto.setItemCode(medDto.getItemCode());
					dto.setUomTypeId(medDto.getUomTypeId());
					dto.setUomUnitId(medDto.getUomUnitId());
					dto.setTaxId(medDto.getTaxId());
					dto.setTaxName(medDto.getTaxName());
					dto.setTaxPercentage(medDto.getTaxPercent());
					dto.setProductCategoryId(medDto.getProductCategoryId());
					dto.setAssetCategoryId(medDto.getAssetCategoryId());
					dto.setFromOT(true);
					medicationList.add(dto);
				}
			}
			
			if(medicationRes.getListObject()!= null && medicationRes.getListObject().size()>0) {
				//medicationList.addAll(medicationRes.getListObject());
				List<OtcCompletionInvMedicationDto>  issueMedList = new ArrayList<>();
				issueMedList.addAll(medicationRes.getListObject());
				for (OtcCompletionInvMedicationDto otcCompletionInvMedicationDto : medicationList) {
					
					if(otcCompletionInvMedicationDto.getFromOT()!= null && otcCompletionInvMedicationDto.getFromOT() == true) {
							
						for (OtcCompletionInvMedicationDto issuemed : issueMedList) {
							
							if(otcCompletionInvMedicationDto.getItemId().equals(issuemed.getItemId())) {
								
								otcCompletionInvMedicationDto.setBatchId(issuemed.getBatchId());
								otcCompletionInvMedicationDto.setIssueDetailsBatchMpprId(issuemed.getIssueDetailsBatchMpprId());
								otcCompletionInvMedicationDto.setIssuedQty(issuemed.getIssuedQty());
								otcCompletionInvMedicationDto.setBookedQty(issuemed.getBookedQty());
								otcCompletionInvMedicationDto.setMrp(issuemed.getMrp());
								otcCompletionInvMedicationDto.setUomTypeId(issuemed.getUomTypeId());
								otcCompletionInvMedicationDto.setUomUnitId(issuemed.getUomUnitId());
								otcCompletionInvMedicationDto.setBatchDetails(issuemed.getBatchDetails());
								otcCompletionInvMedicationDto.setIssueId(issuemed.getIssueId());
							}
							
						}
					}
					
				}
				
			}
		
			
			
			if (medicationList != null && medicationList.size() > 0) {
				for (OtcCompletionInvMedicationDto dto : medicationList) {
					List<UomResponse> uomList = GlobalDao.getUnitOfMeasurementListByItem(dto.getItemId(), orgId);
					dto.setUomList(uomList);
					Response res1 = OTCompletionDao.getBatchListByItemId(storeId,dto.getItemId(),orgId,unitId);
					if(dto.getBatchId()== null) {
						dto.setBatchId(dto.getTempBatchId()!=null?dto.getTempBatchId().longValue(): null);
					}
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
					prodCatId.add(dto.getProductCategoryId());
					List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatId, paymentEntitlementId);
					if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
						PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
						dto.setMarkUpDown(pharmacyTariff.getMarkUpDown());
						dto.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
					}
					
					PatientConsession patientConsession = null;
					if(encounterId != null && encounterId > 0) {
						patientConsession = GlobalDao.getPatientConsessionEncounter(dto.getItemId(), 
								encounterId, 
								patientTypeId, 
								dto.getAssetCategoryId(),
								dto.getProductCategoryId(),null,null);
					}
					else if(admissionId!= null && admissionId > 0) {
						patientConsession = GlobalDao.getPatientConsessionAdmission(dto.getItemId(), 
								admissionId, 
								patientTypeId, 
								dto.getAssetCategoryId(),
								dto.getProductCategoryId());
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
			resDto.setOtcCompletionInvMedication(medicationList);
			// get OT Medication end 
			
			// get OT implant start 
			Response InvImplantsRes = iOtcCompletionInvImplantsDao.getInvImplants(proReqId);
			
			OtcCompletionInvImplantsDto ComplInvestigation = new OtcCompletionInvImplantsDto();
			ComplInvestigation.setUhidNumber(UhidNumber);
			Response implantRes = OTCompletionDao.getImplantIssueAcceptanceData(ComplInvestigation);
			
			List<OtcCompletionInvImplantsDto> implantList = new ArrayList<OtcCompletionInvImplantsDto>();
			
			if(implantDtoList.size()>0) {
				for (ImplantDetailsDto  impDto : implantDtoList) {
					OtcCompletionInvImplantsDto dto = new OtcCompletionInvImplantsDto();
					dto.setItemId(impDto.getItemId()!=null ? impDto.getItemId() : impDto.getKitItemId());
					dto.setItemName(impDto.getItemName()!=null ? impDto.getItemName() : impDto.getKitItemName());
					dto.setItemCode(impDto.getItemCode()!=null ? impDto.getItemCode() : impDto.getKitItemCode());
					dto.setUomTypeId(impDto.getUomTypeId());
					dto.setUomUnitId(impDto.getUomUnitId());
					dto.setTaxName(impDto.getItemTaxName()!=null ? impDto.getItemTaxName():impDto.getKitTaxName());
					dto.setTaxId(impDto.getItemTaxId()!=null ? impDto.getItemTaxId() : impDto.getKitTaxId());
					dto.setTaxPercentage(impDto.getItemTaxPercent()!=0.0 ? impDto.getItemTaxPercent(): impDto.getKitTaxPercent());
					dto.setProductCategoryId(impDto.getItemProductCategoryId()!=null?impDto.getItemProductCategoryId():impDto.getKitProductCategoryId());
					dto.setAssetCategoryId(impDto.getItemAssetCategoryId()!=null ? impDto.getItemAssetCategoryId() : impDto.getKitAssetCategoryId());
					dto.setFromOT(true);
					implantList.add(dto);
				}
			}
			
			if(InvImplantsRes.getListObject()!=null && InvImplantsRes.getListObject().size()>0 ) {
				
				implantList.addAll(InvImplantsRes.getListObject());
			}
			if(implantRes.getListObject()!=null && implantRes.getListObject().size()>0 ) {
				
				List<OtcCompletionInvImplantsDto>  issueImpList = new ArrayList<>();
				issueImpList.addAll(implantRes.getListObject());
				for (OtcCompletionInvImplantsDto otcCompletionInvIMPDto : implantList) {
					
					if(otcCompletionInvIMPDto.getFromOT()!= null && otcCompletionInvIMPDto.getFromOT() == true) {
							
						for (OtcCompletionInvImplantsDto issueImp : issueImpList) {
							
							if(otcCompletionInvIMPDto.getItemId().equals(issueImp.getItemId())) {
								
								otcCompletionInvIMPDto.setBatchId(issueImp.getBatchId());
								otcCompletionInvIMPDto.setIssueDetailsBatchMpprId(issueImp.getIssueDetailsBatchMpprId());
								otcCompletionInvIMPDto.setIssuedQty(issueImp.getIssuedQty());
								otcCompletionInvIMPDto.setBookedQty(issueImp.getBookedQty());
								otcCompletionInvIMPDto.setMrp(issueImp.getMrp());
								otcCompletionInvIMPDto.setBatchDetails(issueImp.getBatchDetails());
								otcCompletionInvIMPDto.setIssueId(issueImp.getIssueId());
							}
							
						}
					}
					
				}
			}
			
			if (implantList != null && implantList.size() > 0) {
				for (OtcCompletionInvImplantsDto dto : implantList) {
					if(dto.getBatchId()== null) {
						dto.setBatchId(dto.getTempBatchId()!=null?dto.getTempBatchId().longValue(): null);
					}
					Response res1 = OTCompletionDao.getBatchListByItemId(storeId,dto.getItemId(),orgId,unitId);
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
					prodCatId.add(dto.getProductCategoryId());
					List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatId, paymentEntitlementId);
					if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
						PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
						dto.setMarkUpDown(pharmacyTariff.getMarkUpDown());
						dto.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
					}
					
					PatientConsession patientConsession = null;
					if(encounterId != null && encounterId > 0) {
						patientConsession = GlobalDao.getPatientConsessionEncounter(dto.getItemId(), 
								encounterId, 
								patientTypeId, 
								dto.getAssetCategoryId(),
								dto.getProductCategoryId(),null,null);
					}
					else if(admissionId != null && admissionId > 0) {
						patientConsession = GlobalDao.getPatientConsessionAdmission(dto.getItemId(), 
								admissionId, 
								patientTypeId, 
								dto.getAssetCategoryId(),
								dto.getProductCategoryId());
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
			resDto.setOtcCompletionInvImplants(implantList);
			// get OT implant end
			
			resDto.setTimeSheet(otTimeSheet1);
			resDto.setOtimsStatus(otimsStatus);

			resDto.setBillOfresList(billOfresList);
			resDto.setOtServices(otServiceList);
			/*********************** End get timesheet list *******************/
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, resDto);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}
	
/*	@Override
	public Response getProcedureOrderById(int proReqId, String otimsId, int tariffId, int storeId,HttpServletRequest request) throws ServiceException, Exception {
		  try{
			  
			  Boolean otimsStatus=false; 
			  ProcedureOrderReqDto resDto = new ProcedureOrderReqDto(); //get procedure orders 
			  Response procedureOrderRes = iProcedureOrderDao.getProcedureOrderById(proReqId);
			  resDto.setProcedureOrders(procedureOrderRes.getListObject());
			  
			  Integer orgId = resDto.getProcedureOrders().get(0).getOrgId(); Integer unitId
			  =resDto.getProcedureOrders().get(0).getUnitId(); String UhidNumber =
			  resDto.getProcedureOrders().get(0).getUhidNumber(); //get OT services 
			  List<OTServiceDto> otServiceList = new ArrayList<OTServiceDto>(); 
			  
			  Response otServiceRes = iOTServiceDao.getOtServicesByProReqId(proReqId);
			  otServiceList.addAll(otServiceRes.getListObject()); //get time sheet
			 
			  OtCompletionTimeSheetDto otTimeSheet = new OtCompletionTimeSheetDto();
			  OtCompletionTimeSheetDto otTimeSheet1 = new OtCompletionTimeSheetDto();
			  
			  Response timeSheetRes = iOtCompletionTimeSheetDao.getOtTimeSheetByProReq(proReqId); otTimeSheet1 =
			  (OtCompletionTimeSheetDto)timeSheetRes.getObject(); //get Ot staff
			  
			  List< OtCompletionStaffDto> otStaffList = new
			  ArrayList<OtCompletionStaffDto>(); Response otStaffCompletion =
			  iOtCompletionStaffDao.getOtCompletionStaffByProReqId(proReqId);
			  
			  otStaffList.addAll(otStaffCompletion.getListObject()); otStaffList.size();
			  
			//  
			
			 * HttpServletRequest request1 = ServletUtils.getCurrentRequest(); =
			 * ServletUtils.getBaseUrl(request1);
			 
			  
			  //System.out.println("static String baseUrl--------"+BASEURL);
			  if(!otimsId.equals("null")) { 
				  
				  final String uri = Constants.OTIMS_ROOT_URL+"/procedureDetails";
			  
				//  final String uri ="http://13.126.135.186:4567/procedureDetails";
				
			  UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri).queryParam("id", otimsId); 
			  RestTemplate  restTemplate = new RestTemplate();
			  HttpHeaders headers = new HttpHeaders();
			  headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			 
			  HttpEntity<String> entity = new HttpEntity<String>(headers);
			  ResponseEntity<String> result =  restTemplate.exchange(builder.build().toUriString(),org.springframework.http.HttpMethod.GET, entity, String.class);
			  
			  //System.out.println(result.getBody());
			  
			  JSONObject jsonObj = new JSONObject(result.getBody());
			  
			  JSONArray doctorChargesArray = jsonObj.getJSONArray("doctorCharges");
			  if(doctorChargesArray.length()>0) { 
				  for (int i = 0; i <doctorChargesArray.length(); i++) { 
					  otimsStatus=true; 
					  JSONObject doctorChargesObj = doctorChargesArray.getJSONObject(i);
			  
					  JSONArray proceduresArray = doctorChargesObj.getJSONArray("procedures"); 
					  for(int j = 0; j < proceduresArray.length(); j++) {
						  JSONObject procedureObj = proceduresArray.getJSONObject(j); 
						  OtCompletionStaffDto dto = new OtCompletionStaffDto(); 
						  dto.setRoleName(procedureObj.getString("role"));
						  dto.setDoctorName(doctorChargesObj.getString("doctor"));
						  dto.setProcedureOrderId(Integer.parseInt(procedureObj.getString("procedureId")));
						 // dto.setProcedureRoleId(Integer.parseInt(procedureObj.getString("roleId")));
						  dto.setProcedureRoleId(1); //for surgeon
						  dto.setUserId(Integer.parseInt(doctorChargesObj.getString("doctorId")));
						  dto.setStatus('A');
			  
						  ResourceChargeConfigurationDto configDto = new ResourceChargeConfigurationDto(); 
						  configDto.setOrgId(orgId);
						  configDto.setUnitId(unitId); configDto.setRoleId(dto.getProcedureRoleId());
						  configDto.setDoctorId(dto.getUserId());
			  
			  
			  Optional<ProcedureOrderMasterDto> matchingObject =resDto.getProcedureOrders().stream().filter(p ->p.getProcedureOrderId().equals(dto.getProcedureOrderId())) .findFirst();
			  ProcedureOrderMasterDto procedureOrderMasterDto = matchingObject.get();
			  
			  configDto.setServiceId(procedureOrderMasterDto.getServiceId());
			  dto.setProcedureId(procedureOrderMasterDto.getServiceId()); 
			  Response surgionChargesRes = iResourceChargeConfigurationDao.getResourceCharegesByDoctor(configDto); 
			
			  if (surgionChargesRes.getObject() !=null) { 
				  ResourceChargeConfigurationDto rChargeConfigDto =(ResourceChargeConfigurationDto) surgionChargesRes .getObject();
				  dto.setAmount(rChargeConfigDto.getAmount().doubleValue()); 
			  }
			  
			  otStaffList.add(dto); 
			  } 
			} 
		}
			  
			  
			 *//*********************** Start get services from otims *******************//*
			
			  
			  JSONArray serviceArray = jsonObj.getJSONArray("services");
			  if(serviceArray.length()>0) { 
				  for (int i = 0; i < serviceArray.length(); i++){ 
					  otimsStatus=true; 
					  UnitServiceTariffMasterDto unitServiceTariffMasterDto = new UnitServiceTariffMasterDto(); 
					  OTServiceDto servicedto = new OTServiceDto(); 
					  JSONObject serviceObj = serviceArray.getJSONObject(i);
					  servicedto.setServiceName(serviceObj.getString("service"));
					  servicedto.setSpecialityId(Integer.parseInt(serviceObj.getString("specialityId")));
					  servicedto.setProcedureId(Integer.parseInt(serviceObj.getString("proReqId")));
					  servicedto.setServiceId(Integer.parseInt(serviceObj.getString("serviceId")));
					  servicedto.setStatus('A');
					  servicedto.setSpeciality(serviceObj.getString("speciality"));
					  servicedto.setUnitId(unitId); servicedto.setOrgId(orgId); 
					  List<Integer> serviceList = new ArrayList<Integer>();
					  serviceList.add(Integer.parseInt(serviceObj.getString("serviceId")));
					  serviceList.add(69); 
					  unitServiceTariffMasterDto.setTariffId(tariffId);
					  unitServiceTariffMasterDto.setUnitId(unitId);
					  unitServiceTariffMasterDto.setOrganizationId(orgId);
					  unitServiceTariffMasterDto.setPatientTypeId(resDto.getProcedureOrders().get(0).getPatientTypeId());
					  unitServiceTariffMasterDto.setPaymentEntitlementId(resDto.getProcedureOrders().get(0).getPaymentEntitlementId());
					  unitServiceTariffMasterDto.setBillingBedCategoryId(resDto.getProcedureOrders().get(0).getBillingBedCategoryId());
					  unitServiceTariffMasterDto.setOrderDate(resDto.getProcedureOrders().get(0). getOrderDateTime()); 
					  unitServiceTariffMasterDto.setServiceList(serviceList);
					  unitServiceTariffMasterDto.setVisitTypeId(resDto.getProcedureOrders().get(0).getVisitTypeId()); 
					  
					  Response servRateRes = iUnitServiceTariffMasterService. getBasePriceOfServicesFromTariffMasterByServiceList(unitServiceTariffMasterDto); 
					  
					  UnitServiceTariffMasterDto servRateDto =(UnitServiceTariffMasterDto)servRateRes.getObject(); 
					  if(servRateDto != null){
						  servicedto.setRate(servRateDto.getRate());
						  servicedto.setAmount(servRateDto.getRate()); 
					} 
					  
					  serviceList=null;
					  otServiceList.add(servicedto); 
					  } 
				 }
			  
			  
			 *//*********************** End get services from otims *******************//*
			
			
			
			
			*//*********************** Start Get Store Id from theatre ID *******************//*
			
			  int operationTheatreId = jsonObj.getInt("operationTheatreId"); 
			  Response store = iOperationTheatreDao.getOperationTheatreById(operationTheatreId, orgId, unitId); 
			  
			  OperationTheatreDto oTDto = new OperationTheatreDto();
			 
			  oTDto=(OperationTheatreDto)store.getListObject().get(0); 
			  int storeId1=oTDto.getStoreId(); 
			  //System.out.println("storeId           "+storeId );
			 *//*********************** End Get Store Id from theatre ID *******************//*
			
			
			*//*********************** Start get medication issued list *******************//*
			
			  
			  JSONArray inventoryMedArray = jsonObj.getJSONArray("inventoryItems");
			  JSONArray finalArray = new JSONArray(); 
			  
			  if (inventoryMedArray.length() > 0) {
			  for (int i = 0; i < inventoryMedArray.length(); i++) { 
				  JSONObject inventoryItemObj = inventoryMedArray.getJSONObject(i);
				  if(inventoryItemObj.get("inventoryType").equals("Medication") || inventoryItemObj.get("inventoryType").equals("Consumable")) { 
					  
					//  finalArray = new JSONArray(inventoryItemObj);
					  finalArray.put(inventoryMedArray.getJSONObject(i)); 
			  }
			  
			  if(inventoryItemObj.get("inventoryType").equals("Medication")) { //finalArray
				 
				  finalArray  = new JSONArray(inventoryItemObj);
				  finalArray.put(inventoryMedArray.getJSONObject(i)); 
				  } 
			  } 
			  }
			  
			  List<OtcCompletionInvMedicationDto> medicationList = this.getMedicationList(storeId1,UhidNumber, finalArray,proReqId ,unitId,orgId);
			  
			  resDto.setOtcCompletionInvMedication(medicationList);
			 *//*********************** End get medication issued list *******************//*
			
			
			
			*//*********************** Start get implant issued list *******************//*
			
			  JSONArray finalImplantArray = new JSONArray();  
			  if(inventoryMedArray.length() > 0) { 
				  for (int i = 0; i <inventoryMedArray.length(); i++) { 
					  JSONObject inventoryItemObj = inventoryMedArray.getJSONObject(i);
					  
					  if(inventoryItemObj.get("inventoryType").equals("Implant")){
						  
						  finalImplantArray.put(inventoryMedArray.getJSONObject(i)); 
					} 
				} 
			}
			  List<OtcCompletionInvImplantsDto> imaplantList = this.getImplantList(storeId1,UhidNumber, finalImplantArray,proReqId,unitId,orgId);
			  
			  resDto.setOtcCompletionInvImplants(imaplantList);
			 *//*********************** End get implant issued list *******************//*
			
			
			
			*//*********************** Start get timesheet list *******************//*
			
			  String mongo=null;
			  
			  JSONObject timeSheet = jsonObj.getJSONObject("timesheet");
			  
			  JSONArray procedureDuration =
			  timeSheet.getJSONArray("procedureDurationDetails"); 
			  
			  if(procedureDuration.length() > 0){
			  
			  for (int k = 0; k < procedureDuration.length(); k++) { 
				  otimsStatus=true;
				  JSONObject proDuration = procedureDuration.getJSONObject(k);
				  if(proDuration!=null) {
					  //System.out.println("anc"+proDuration.getString("procedureStartTime"));
					  //System.out.println("anc"+proDuration.getString("procedureEndTime"));
					  otTimeSheet.setProcedureStartTime(GlobalCommonDateUtils.getStringMongoDate(
					  proDuration.getString("procedureStartTime")));
					  otTimeSheet.setProcedureEndTime(GlobalCommonDateUtils.getStringMongoDate(
					  proDuration.getString("procedureEndTime"))); 
					  }
			  }
			  otTimeSheet.setWheelInTime(GlobalCommonDateUtils.getStringMongoDate(timeSheet .getString("wheelInTime")));
			  otTimeSheet.setWheelOutTime(GlobalCommonDateUtils.getStringMongoDate(timeSheet.getString("wheelOutTime")));
			  otTimeSheet.setTableOnTime(GlobalCommonDateUtils.getStringMongoDate(timeSheet .getString("tableOnTime")));
			  otTimeSheet.setTableOutTime(GlobalCommonDateUtils.getStringMongoDate(timeSheet.getString("tableOffTime")));
			  otTimeSheet.setAnesthesiaTime(GlobalCommonDateUtils.getStringMongoDate(timeSheet.getString("anaesthesiaTime")));
			  otTimeSheet.setScheduleDischarge(GlobalCommonDateUtils.getStringMongoDate(timeSheet.getString("scheduleDischarge")));
			  otTimeSheet.setActualDischarge(GlobalCommonDateUtils.getStringMongoDate(timeSheet.getString("actualDischarge")));
			  otTimeSheet.setProReqId(Integer.parseInt(timeSheet.getString("proReqId")));
			  mongo = timeSheet.getString("procId");
			  otTimeSheet.setMongoId(timeSheet.getString("procId"));
			  otTimeSheet.setStatus('A'); otTimeSheet.setUnitId(unitId);
			  otTimeSheet.setOrgId(orgId); 
			  
			 } 
			  
			  resDto.setOtimsStatus(otimsStatus);
			  otTimeSheet.setMongoId(mongo); 
			  resDto.setTimeSheet(otTimeSheet); 
			  }else {
			  
					//List<OtcCompletionInvMedicationDto> OtcCompletionInvMedicationList = new ArrayList<OtcCompletionInvMedicationDto>();
					Response iOtcCompletionInvMedicatioRes =  iOtcCompletionInvMedicationDao.getOtMedications(proReqId);
					//OtcCompletionInvMedicationList.addAll(iOtcCompletionInvMedicatioRes.getListObject());
					
					
					//List<OtcCompletionInvImplantsDto> OtcCompletionInvImplantsList = new ArrayList<OtcCompletionInvImplantsDto>();
					Response InvImplantsRes = iOtcCompletionInvImplantsDao.getInvImplants(proReqId);
					//OtcCompletionInvImplantsList.addAll(InvImplantsRes.getListObject());
					
					
					OtcCompletionInvMedicationDto ComplMedication = new OtcCompletionInvMedicationDto();
					ComplMedication.setUhidNumber(UhidNumber);

					OtcCompletionInvImplantsDto ComplInvestigation = new OtcCompletionInvImplantsDto();
					ComplInvestigation.setUhidNumber(UhidNumber);

					Response medicationRes = OTCompletionDao.getIssueAcceptanceData(ComplMedication);
					List<OtcCompletionInvMedicationDto> medicationList = new ArrayList<OtcCompletionInvMedicationDto>();
					
					if(iOtcCompletionInvMedicatioRes.getListObject()!= null && iOtcCompletionInvMedicatioRes.getListObject().size()>0) {
						medicationList.addAll(iOtcCompletionInvMedicatioRes.getListObject());
						
					}
					if(medicationRes.getListObject()!= null && medicationRes.getListObject().size()>0) {
						medicationList.addAll(medicationRes.getListObject());
						
					}
				
					if (medicationList != null && medicationList.size() > 0) {
						for (OtcCompletionInvMedicationDto dto : medicationList) {
							CloseableHttpClient httpClient = HttpClientBuilder.create().build();
							String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/" + unitId;
							HttpGet get = new HttpGet(uri1);
							org.apache.http.HttpResponse res = httpClient.execute(get);
							org.apache.http.HttpEntity resEntity = res.getEntity();
							String apiOutput = EntityUtils.toString(resEntity);
							JSONObject itemDetailsObj = new JSONObject(apiOutput);
							List<BatchDto> btDtoList = getBatchList(itemDetailsObj);
							dto.setBatch(btDtoList);
						}
						
					}
					
					resDto.setOtcCompletionInvMedication(medicationList);
					
					Response implantRes = OTCompletionDao.getImplantIssueAcceptanceData(ComplInvestigation);
					List<OtcCompletionInvImplantsDto> implantList = new ArrayList<OtcCompletionInvImplantsDto>();
					
					if(InvImplantsRes.getListObject()!=null && InvImplantsRes.getListObject().size()>0 ) {
						
						implantList.addAll(InvImplantsRes.getListObject());
					}
					if(implantRes.getListObject()!=null && implantRes.getListObject().size()>0 ) {
						
						implantList.addAll(implantRes.getListObject());
					}
					
					if (implantList != null && implantList.size() > 0) {
						for (OtcCompletionInvImplantsDto dto : implantList) {
							CloseableHttpClient httpClient = HttpClientBuilder.create().build();
							String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/" + unitId;
							HttpGet get = new HttpGet(uri1);
							org.apache.http.HttpResponse res = httpClient.execute(get);
							org.apache.http.HttpEntity resEntity = res.getEntity();
							String apiOutput = EntityUtils.toString(resEntity);
							JSONObject itemDetailsObj = new JSONObject(apiOutput);
							List<BatchDto> btDtoList = getBatchList(itemDetailsObj);
							dto.setBatch(btDtoList);
						}
						
					}
					resDto.setOtcCompletionInvImplants(implantList);
				
			  resDto.setTimeSheet(otTimeSheet1); resDto.setOtimsStatus(otimsStatus); 
			  }
			  
			  resDto.setOtCompletionStaff(otStaffList);
			  resDto.setOtServices(otServiceList);
			 *//*********************** End get timesheet list *******************//*
			
			  return new Response<>(SUCCESS,SUCCESS_CODE, null, null, resDto);
			}catch(Exception e){
				
				try { 
					throw e; 
					} catch (Exception e1) {
						 e1.printStackTrace();
						 } 
				} return new Response<>(ERROR, ERROR_CODE, null, null, null); 
	}
*/
	@Override
	@Transactional
	public Response getProcedureOrderListByStatus(ProcedureOrderListReqDto procedureOrderListReqDto)
			throws ServiceException {
		try {
			return iProcedureOrderDao.getProcedureOrderListByStatus(procedureOrderListReqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchProcedureServiceForOrder(ProcedureOrderServSrchReqDto reqDto) throws ApplicationException {
		try {
			return iProcedureOrderDao.searchProcedureServiceForOrder(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveProcedureList(List<ProcedureOrderMasterDto> procedureOrderMasterDtoList)
			throws ServiceException {
		try {
			Date date = TimeZoneComponent.getDateByzone(procedureOrderMasterDtoList.get(0).getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			ProcedureOrderMaster mst = new ProcedureOrderMaster();
			ProcedureReqMaster procedureReqMaster = new ProcedureReqMaster();
			String email  = procedureOrderMasterDtoList.get(0).getEmail();
			Response procedureReqRes = null;
			Integer proRequestId = 0;
			Integer cancelProcCount = 0;
			for (Iterator iterator = procedureOrderMasterDtoList.iterator(); iterator.hasNext();) {
					ProcedureOrderMasterDto procedureOrderMasterDto = (ProcedureOrderMasterDto) iterator.next();
					if(procedureOrderMasterDto.getProcedureReqMasterDto()!=null && procedureOrderMasterDto.getIsPrimarySecondary() == 'Y') {
						
						ProcedureReqMasterDto procedureReqMasterDto = procedureOrderMasterDto.getProcedureReqMasterDto();
							if(procedureReqMasterDto.getPreferedDateTime() == null) {
								
								if(procedureOrderMasterDto.getPreferedDateTime().length()<12) {
									String preDate = procedureOrderMasterDto.getPreferedDateTime()+" 00:00:00";
									procedureOrderMasterDto.setPreferedDateTime(preDate);
								}else {
									procedureReqMasterDto.setPreferedDateTime(procedureOrderMasterDto.getPreferedDateTime());
								}
							}
							procedureReqMasterDto.setPrimaryProcedureId(procedureOrderMasterDto.getServiceId());
							procedureReqMasterDto.setCreatedDate(dateTime);
							procedureReqMasterDto.setRequestDate(dateTime);
							procedureReqMasterDto.setRequestBy(procedureOrderMasterDto.getCreatedBy());
							procedureReqMasterDto.setProcedureStatusId(procedureOrderMasterDto.getProcedureStatusId());
							procedureReqMasterDto.setOrgId(procedureOrderMasterDto.getOrgId());
							procedureReqMasterDto.setUnitId(procedureOrderMasterDto.getUnitId());
							procedureReqMasterDto.setUpdatedDate(dateTime);
							procedureReqMasterDto.setPreferedTime(procedureOrderMasterDto.getPreferedTime());
							procedureReqRes = iProcedureReqDao.saveProcedureReq(procedureReqMasterDto);
							procedureReqMaster = (ProcedureReqMaster) procedureReqRes.getObject();
							proRequestId = procedureReqMaster.getProReqId();
					}
					
				
					if(procedureOrderMasterDto.getProReqId() != 0 ) {
						
						proRequestId = procedureOrderMasterDto.getProReqId();
					}
					
					if(procedureOrderMasterDto.getStatus().equals("A")) {
							procedureOrderMasterDto.setProReqId(proRequestId);
							procedureOrderMasterDto.setCreatedDate(dateTime);
							procedureOrderMasterDto.setOrderDateTime(dateTime);
							procedureOrderMasterDto.setExportedToOtims(false);
							Response res = iProcedureOrderDao.saveProcedureOrder(procedureOrderMasterDto);
							mst = (ProcedureOrderMaster) res.getObject();
							
							/***********************  Start Save All Procedure Order ***********************/
							
							if(procedureOrderMasterDto.getProcedureOrderConsentList()!=null && procedureOrderMasterDto.getProcedureOrderConsentList().size()>0) {
								
								 iProcedureOrderConsentDao.saveProcedureOrderConsent(procedureOrderMasterDto.getProcedureOrderConsentList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderCheckList()!=null && procedureOrderMasterDto.getProcedureOrderCheckList().size()>0) {
								
								 iProcedureOrderChecklistDao.saveProcedureOrderCheckList(procedureOrderMasterDto.getProcedureOrderCheckList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderServiceList()!=null && procedureOrderMasterDto.getProcedureOrderServiceList().size()>0) {
								
								  iProcedureOrderServiceDao.saveProcedureOrderServiceList(procedureOrderMasterDto.getProcedureOrderServiceList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderStaffList()!=null && procedureOrderMasterDto.getProcedureOrderStaffList().size()>0) {
								
								   iProcedureOrderStaffDao.saveProcedureOrderStaffList(procedureOrderMasterDto.getProcedureOrderStaffList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderPatientEducationDto()!=null) {
								
								   Response patieRes = iProcedureOrderPatientEducationDao.saveProcedureOrderPatientEducation(procedureOrderMasterDto.getProcedureOrderPatientEducationDto(),proRequestId,mst.getProcedureOrderId());
								
								   if(patieRes.getObject()!=null) {
											   
											   if(email!=null){
												   procedureOrderMasterDto.getProcedureOrderPatientEducationDto().setEmail(email);
												   iMailingService.sentPatinetEducationTemplate(procedureOrderMasterDto.getProcedureOrderPatientEducationDto());
											   }
									  }
							}
							
							if(procedureOrderMasterDto.getImplantDetailsList()!=null && procedureOrderMasterDto.getImplantDetailsList().size()>0) {
								
								iImplantDetailsDao.saveImplantdetails(procedureOrderMasterDto.getImplantDetailsList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getMedicationDetailsList()!=null && procedureOrderMasterDto.getMedicationDetailsList().size()>0) {
								
								iMedicationDetailsDao.saveMedicationDetails(procedureOrderMasterDto.getMedicationDetailsList(),mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderEquipmentList()!=null && procedureOrderMasterDto.getProcedureOrderEquipmentList().size()>0) {
								
								iProcedureOrderEquipmentDao.saveProcedureOrderEquipment(procedureOrderMasterDto.getProcedureOrderEquipmentList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							/***********************End Save All Procedure Order ***********************/


							if (procedureOrderMasterDto.getListCalenderMapperDto() != null
									&& procedureOrderMasterDto.getListCalenderMapperDto().size() > 0) {
								for (Iterator iterator2 = procedureOrderMasterDto.getListCalenderMapperDto().iterator(); iterator2
										.hasNext();) {
									SurgicalCalenderMapperDto dto = (SurgicalCalenderMapperDto) iterator2.next();
									dto.setProcedureOrderId(mst.getProcedureOrderId());
									dto.setOrganisationId(procedureOrderMasterDto.getOrgId());
									dto.setUnitId(procedureOrderMasterDto.getUnitId());
									dto.setIsDeleted('N');
									dto.setStatus('A');
									dto.setCreatedDate(dateTime);
									dto.setUpdatedDate(dateTime);
									iSurgicalCalendarMasterService.saveCalenderSlotMpper(dto);
								}
							}
						}
					else {
						List<ProcedureOrderMasterDto> list = new ArrayList<>(); 
						if(procedureOrderMasterDto.getIsPrimarySecondary() == 'Y') {
							 Response procedureOrderRes = iProcedureOrderDao.getProcedureOrderById(procedureOrderMasterDto.getProReqId()); 
							 list.addAll(procedureOrderRes.getListObject());
							 Integer primFlag = 0;
							 for (ProcedureOrderMasterDto procedureOrderMasterDto2 : list) {
								
								 if(primFlag == 1) {
									 
									 iProcedureOrderDao.updateProcedureOrderSeconderyToPrimary(procedureOrderMasterDto2.getProcedureOrderId());
									 
									 iProcedureReqDao.updateProcedureRequestSeconderyToPrimary(procedureOrderMasterDto.getProReqId(),procedureOrderMasterDto2.getServiceId());
								 }
								 
								 if(procedureOrderMasterDto.getProcedureOrderId().equals(procedureOrderMasterDto2.getProcedureOrderId())) {
									 primFlag++;
								 }
							}
						}
						 iOrderDetailsMasterDao.cancelProcedureSerivicesByProcedure(procedureOrderMasterDto);
						 Response cancelProcResponse  =  iProcedureOrderDao.updateAllProcedureOrderDetailsStatus(procedureOrderMasterDto);
						 if(list != null && list.size() <= 1) {
							 iProcedureReqDao.cancelProcedureReq(procedureOrderMasterDto.getProReqId());
						 }
						 if(cancelProcResponse.getObject()!=null) {
							 cancelProcCount = (Integer)cancelProcResponse.getObject();
						 }
					}
			}
			mst.setProReqId(proRequestId);
			if(cancelProcCount>0) {
				return new Response<>(SUCCESS, SUCCESS_CODE, PROCEDURE_CANCEL_MESSAGE, null, mst);
			}else {
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mst);
			}
			} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchServicesForProcedureOrder(ProcedureOrderServSrchReqDto reqDto) throws ApplicationException {
		try {
			return iProcedureOrderDao.searchServicesForProcedureOrder(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response orderProcedures(ProcedureOrderReqDto reqDto) throws ApplicationException,ServiceException,Exception {
		OrderMasterDto orderMst = null;
		List<OrderDetailsMasterDto> listOrderDetails = null;
		Integer proReqId = null;
		try {

			Integer admissionId = null;
			Integer encounterId =null;
			Integer procedureTotalDuration = 0;
			Integer orgId = reqDto.getProcedureOrders().get(0).getOrgId();
			Integer unitId = reqDto.getProcedureOrders().get(0).getUnitId();
			Integer createdBy = sessionService.getUserId();
			Integer doctorId = reqDto.getProcedureOrders().get(0).getDoctorId();
			Integer visitTypeId = reqDto.getProcedureOrders().get(0).getVisitTypeId();
			if(visitTypeId !=null && visitTypeId.intValue() == 1) {
				 encounterId = (reqDto.getProcedureOrders().get(0).getEncounterId()!=null&&reqDto.getProcedureOrders().get(0).getEncounterId()>0)? reqDto.getProcedureOrders().get(0).getEncounterId():reqDto.getProcedureReqMasterDto().getEncounterId();
			}if(visitTypeId !=null && visitTypeId.intValue() != 1) {
				 admissionId = (reqDto.getProcedureOrders().get(0).getAdmissionId()!=null && reqDto.getProcedureOrders().get(0).getAdmissionId()>0) ? reqDto.getProcedureOrders().get(0).getAdmissionId() : reqDto.getProcedureReqMasterDto().getAdmissionId();
			}
			
			Integer procedureRequestId = reqDto.getProcedureOrders().get(0).getProReqId() != null?reqDto.getProcedureOrders().get(0).getProReqId():null;
			Integer specialityId = reqDto.getProcedureOrders().get(0).getSpecialityId();
			Integer patientId = reqDto.getProcedureOrders().get(0).getPatientId();
			Integer procedureOrderId = reqDto.getProcedureOrders().get(0).getProcedureOrderId();
			Date currentDate = TimeZoneComponent.getDateByzone(unitId);
			Integer storeId = sessionService.getStoreId();
			List<Integer> procedureTypeIds = new ArrayList<Integer>();
			// Map<ServiceId,ProcedureOrderId>
			Map<Integer, Integer> procOrders = new HashMap<>();
			
			if(reqDto.getProcedureOrders().get(0).getProReqId() == null ) 
			{
				Response saveReqRes = iProcedureReqDao.saveProcedureReq(reqDto.getProcedureReqMasterDto());
				 ProcedureReqMaster procedureReqMaster  = (ProcedureReqMaster) saveReqRes.getObject();
				 procedureRequestId = procedureReqMaster.getProReqId();
				 proReqId = procedureReqMaster.getProReqId();
			}
			// save procedures
			String procedures = "";
			String procedureTypes="";
			String otTypes="";
			if (reqDto.getProcedureOrders() != null && reqDto.getProcedureOrders().size() > 0) 
			{

				ListIterator<ProcedureOrderMasterDto> procItr = reqDto.getProcedureOrders().listIterator();
				while (procItr.hasNext()) 
				{
					ProcedureOrderMasterDto order = procItr.next();
					order.setProReqId(proReqId);
					// Calculation
					procedureTypeIds.add(order.getProcedureTypeId());

					//procedureTotalDuration = (int) (procedureTotalDuration + reqDto.getTimeSheet().getProcedureDuration());

					if (order.getProcedureOrderId() == null) {
						order.setProReqId(procedureRequestId);
						Response procOrdRes = iProcedureOrderDao.saveProcedureOrder(order);
						if (procOrdRes.getCode() != null && procOrdRes.getObject() != null) {
							ProcedureOrderMaster master = (ProcedureOrderMaster) procOrdRes.getObject();
							procOrders.put(master.getServiceId(), master.getProcedureOrderId());
							order.setProcedureOrderId(master.getProcedureOrderId());
						}
					} else {
						Response procOrdRes = iProcedureOrderDao.updateProcedureOrder(order);
						procOrders.put(order.getServiceId(), order.getProcedureOrderId());

					}
					
					if(order.getIsPrimarySecondary()=='Y' && !order.getStatus().equals("I")) {
						procedures = order.getServiceName();
						procedureTypes = order.getProcedureType();
						Response otRes =  iProcedureOrderDao.getOTTThetreByServiceId(order);
						if(otRes.getObject()!=null) {
							ProcedureOrderMasterDto ot = (ProcedureOrderMasterDto)otRes.getObject();
							otTypes = ot.getTheatreTypeName();
						}
					}else if(order.getIsPrimarySecondary()=='N' && !order.getStatus().equals("I")){
						procedures = procedures+ ","+order.getServiceName();
						procedureTypes =procedureTypes +","+ order.getProcedureType();
						Response otRes =  iProcedureOrderDao.getOTTThetreByServiceId(order);
						if(otRes.getObject()!=null) {
							ProcedureOrderMasterDto ot = (ProcedureOrderMasterDto)otRes.getObject();
							otTypes = otTypes+","+ot.getTheatreTypeName();
						}
					}
				}
			}
			
			//OtCompletionDetails Save or Update
			if (reqDto.getOtCompletionDetailsDto() != null) 
			{
				reqDto.getOtCompletionDetailsDto().setProReqId(procedureRequestId);
				procedureTotalDuration = (int) (reqDto.getOtCompletionDetailsDto().getDuration());
				if(reqDto.getOtCompletionDetailsDto().getOtCompletionDetailsId() == null) {
					
					String otSheetNo = iGlobalUnitMasterDao.getGeneratedNo(storeId,unitId,OT_SHEET_NO);
					reqDto.getOtCompletionDetailsDto().setOtSheetNo(otSheetNo);
				}
				iOTCompletionDetailsDao.saveOtCompletionDetails(reqDto.getOtCompletionDetailsDto());
			}
			
			//update procedure Schedule
			if(reqDto.getOtCompletionDetailsDto().getProcedureScheduleId()!=null) {
				ProcedureOrderMasterDto procDto = new ProcedureOrderMasterDto();
				procDto.setProcedureScheduleId(reqDto.getOtCompletionDetailsDto().getProcedureScheduleId());
				procDto.setProcedureName(procedures);
				procDto.setProcedureType(procedureTypes);
				procDto.setTheatreTypeName(otTypes);
				Response res = iProcedureOrderDao.updateProcedureSchedulingDetails(procDto);
			}
			
			if (reqDto.getOtCompletionCssdKitList() != null && reqDto.getOtCompletionCssdKitList().size() > 0) {
				
				List<OTCompletionCssdKitDto> cssdKit = new ArrayList<>();
				Response otCssdKit = iOTCompletionCssdKitDao.getOtCSSDKit(procedureRequestId);
				if(otCssdKit.getListObject().size()>0) {
					
					cssdKit.addAll(otCssdKit.getListObject());	
					
					for (OTCompletionCssdKitDto otCompletionCssdKitDto : cssdKit) {
						int cssdCount = 0;
						for (OTCompletionCssdKitDto cssdDto : reqDto.getOtCompletionCssdKitList()) {
						
							if(otCompletionCssdKitDto.getKitId().equals(cssdDto.getKitId()) && otCompletionCssdKitDto.getKitTransitionNo().equals(cssdDto.getKitTransitionNo())) {
								cssdCount = cssdCount+1;
							}
						}
						if(cssdCount == 0) {
							iOTCompletionCssdKitDao.updateIsUsedFlag(otCompletionCssdKitDto,'N');
						}
					}
				}
				
				for (OTCompletionCssdKitDto cssdDto : reqDto.getOtCompletionCssdKitList()) {
					
					cssdDto.setProReqId(procedureRequestId);
					iOTCompletionCssdKitDao.saveOtCompletionCssdKit(cssdDto);
					iOTCompletionCssdKitDao.updateIsUsedFlag(cssdDto,'Y');
				}
				
				
			}
			

			if (reqDto.getoTCompletionNoteList() != null && reqDto.getoTCompletionNoteList().size() > 0) {
				
				for (OTCompletionNoteDto noteDto : reqDto.getoTCompletionNoteList()) {
					
					noteDto.setProReqId(procedureRequestId);
					iOTCompletionNoteDao.saveOtCompletionNote(noteDto);
				}
			}
			
			
			if (reqDto.getOtCompletionMedicalGasList() != null && reqDto.getOtCompletionMedicalGasList().size() > 0) {
				
				for (OtCompletionMedicalGasDto medicalGas : reqDto.getOtCompletionMedicalGasList()) {
					
					medicalGas.setProReqId(procedureRequestId);
					medicalGas.setOrgId(orgId);
					iOtCompletionMedicalGasDao.saveOtCompletionMedicalGas(medicalGas);
				}
			}
			
			if (reqDto.getEquipmentlist() != null && reqDto.getEquipmentlist().size() > 0) {
				
				for (OtCompletionEquipmentDto equipment : reqDto.getEquipmentlist()) {
					
					equipment.setProReqId(procedureRequestId);
					equipment.setOrganizationId(orgId);
					iOtCompletionEquipmentDao.saveOtCompletionEquipment(equipment);
				}
			}

			// save services
			if (reqDto.getOtServices() != null && reqDto.getOtServices().size() > 0) {
				ListIterator<OTServiceDto> otServItr = reqDto.getOtServices().listIterator();
				while (otServItr.hasNext()) {
					OTServiceDto otService = otServItr.next();
						otService.setStatus(ACTIVE);
						otService.setOrgId(orgId);
						otService.setProReqId(procedureRequestId);
						otService.setProcedureOrderId(procedureOrderId);
						iOTServiceDao.saveOTService(otService);
				}
			}

			/*if (reqDto.getOtCompletionStaff() != null && reqDto.getOtCompletionStaff().size() > 0) {
				ListIterator<OtCompletionStaffDto> otStaffItr = reqDto.getOtCompletionStaff().listIterator();
				while (otStaffItr.hasNext()) {
					OtCompletionStaffDto otCompletionStaffDto = otStaffItr.next();
					if (otCompletionStaffDto.getOtCompletionStaffId() == null) {
						// get order id by service id
						// otCompletionStaffDto.setProcedureOrderId(procOrders.get(otCompletionStaffDto.getProcedureOrderId()));
						otCompletionStaffDto.setStatus(ACTIVE);
						otCompletionStaffDto.setOrgId(orgId);
						otCompletionStaffDto.setUnitId(unitId);
						otCompletionStaffDto.setCreatedBy(createdBy);
						otCompletionStaffDto.setProcReqId(procedureRequestId);
						otCompletionStaffDto.setProcedureOrderId(procedureOrderId);
						iOtCompletionStaffDao.saveOtCompletionStaff(otCompletionStaffDto);
					}
				}
			}*/
			
			if (reqDto.getBillOfresList() != null && reqDto.getBillOfresList().size() > 0) {
				
					for (BillOfMaterialDto dto : reqDto.getBillOfresList()) {
						dto.setProcReqId(procedureRequestId);
						iOtCompletionStaffDao.saveOtCompletionStaff(dto);
					}
			}
			

			// save time sheet details
			if (reqDto.getTimeSheet() != null) {
				reqDto.getTimeSheet().setProReqId(procedureRequestId);
				iOtCompletionTimeSheetDao.saveOrUpdateOtCompletionTimeSheet(reqDto.getTimeSheet());
			}

			// save Medications
			if (reqDto.getOtcCompletionInvMedication() != null && reqDto.getOtcCompletionInvMedication().size() > 0) {
				for (OtcCompletionInvMedicationDto meddto : reqDto.getOtcCompletionInvMedication()) {

					meddto.setProcedureRequestId(meddto.getProcedureRequestId() != null ? meddto.getProcedureRequestId()
							: procedureRequestId);
						Response saveRes = iOtcCompletionInvMedicationDao.saveInvMedications(meddto);
						if (saveRes.getStatus().equalsIgnoreCase("Success")) {
							iOtcCompletionInvMedicationDao.updateIssueStatus(meddto);
						}
				}
			}

			// save Implants
			if (reqDto.getOtcCompletionInvImplants() != null && reqDto.getOtcCompletionInvImplants().size() > 0) {
				for (OtcCompletionInvImplantsDto implantDto : reqDto.getOtcCompletionInvImplants()) {

					implantDto.setProcedureRequestId(
							implantDto.getProcedureRequestId() != null ? implantDto.getProcedureRequestId()
									: procedureRequestId);
						Response saveRes = iOtcCompletionInvImplantsDao.saveInvImplants(implantDto);
						if (saveRes.getStatus().equalsIgnoreCase("Success")) {
							iOtcCompletionInvImplantsDao.updateIssueStatus(implantDto);
					}
				}
			}
			
			iProcedureReqDao.updateProcedureReqStatus(procedureRequestId, 4);
			iProcedureReqDao.updateProcedureScheduleStatus(procedureRequestId, 4);
			if(reqDto.getOtToWardTransfer()) {
				OTTransferRequestDto oTTransferRequestDto = new OTTransferRequestDto();
				oTTransferRequestDto.setAdmissionId(admissionId);
				oTTransferRequestDto.setOtTransferStatus(8); //for OT To Ward Transfer
				oTTransferRequestDto.setTransferRequestId(reqDto.getTransferRequestId());
				iOTTransferRequestDao.updateOTTransferRequest(oTTransferRequestDto);
			}
			// check if final save
			if (reqDto.getIsCompleted() == 'Y') 
			{
				
				
				ProcedureOrderMasterDto pOrder = null;
				Integer  patientTypeId = null;
				Integer  patientClassId = null;
				Integer  billingBedCategoryId = null;
				Integer  paymentEntitlementId = null;
				Integer  contractId = null;
				Integer  payeeId = null;
				Integer  associateCompanyId = null;
				Date currentDateTime = TimeZoneComponent.getDateByzone(unitId);
				boolean isOtRentPresentInbom =false;
				Integer  wardId = null;
				Integer  roomId = null;
				Integer  bedId = null;
				boolean taxInclusive  =  sessionService.getCookieObjectAppConfig().getTaxInclusive()== null ? false : sessionService.getCookieObjectAppConfig().getTaxInclusive();
				
				UnitApplicationConfigurationDto uAppConfDto = new UnitApplicationConfigurationDto();
				uAppConfDto.setOrganizationId(orgId);
				uAppConfDto.setUnitId(unitId);
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(uAppConfDto);
				if (appConfigRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && appConfigRes.getObject() != null) 
				{
					uAppConfDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
				}
				if(admissionId!=null && admissionId > 0 ) 
				{
					
					Response admissionDto = ipdChargesDao.getAdmissionsById(admissionId);
					AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
						patientTypeId = adminDto.getPatientCategoryId();
						billingBedCategoryId = (reqDto.getProcedureOrders().get(0).getBillingBedCategoryId()!=null && reqDto.getProcedureOrders().get(0).getBillingBedCategoryId()>0)?reqDto.getProcedureOrders().get(0).getBillingBedCategoryId():adminDto.getBedBillingCategoryId();
						paymentEntitlementId = adminDto.getPaymentEntitlementId();
						contractId = adminDto.getContractId();
						payeeId = adminDto.getPayeeId();
						visitTypeId = adminDto.getVisitTypeId();
						patientClassId = adminDto.getPatientClassId();
						wardId = adminDto.getWardId();
						roomId = adminDto.getRoomId();
						bedId =  adminDto.getBedId();
				}
				
				if(encounterId!=null && encounterId >0) 
				{
					
					Response encounterRes = iEncounterMasterService.getEncounterDetailsById(encounterId);
					EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
					
					for (PatientSponsorDetailsDto ss : encounterMasterDto.getPatientSponsorDetailsDtosList()) {
						if(ss.getInsuranceId() != null) {
							associateCompanyId =ss.getInsuranceId();
						}
						
					}
						patientTypeId = encounterMasterDto.getPatientCategoryId();
						paymentEntitlementId = encounterMasterDto.getPaymentEntitlementId();
						visitTypeId = 1;
						contractId = encounterMasterDto.getContractId();
						payeeId = encounterMasterDto.getPayeeId();
						patientClassId = encounterMasterDto.getPatientClassId();
						billingBedCategoryId = uAppConfDto.getOpdBillingCatId();
				}
				
				
				
				if (reqDto.getProcedureOrders() != null && reqDto.getProcedureOrders().size() > 0) 
				{
					ProcedureOrderMasterDto proOrder = reqDto.getProcedureOrders().get(0);
					Date today = TimeZoneComponent.getDateByzone(unitId);
					listOrderDetails = new LinkedList<>();

				/*	double primaryPercent = 0;
					double secondaryPercent = 0;*/

					//use procedure orders     // get sub sequent procedure config data
					/*Response subSeqRes = iSubSequentProcedureConfigDao.getSubSequentProcedureConfigByOrgUnitStatus(proOrder.getOrgId(), proOrder.getUnitId(), ACTIVE);
					if (subSeqRes.getCode().equals(SUCCESS_CODE)) {
						SubSequentProcedureConfigDto subSeqConfig = (SubSequentProcedureConfigDto) subSeqRes
								.getObject();
						primaryPercent = subSeqConfig.getPrimary();
						secondaryPercent = subSeqConfig.getSecondary();
					}*/
					
					//procedureTotalDuration = (int) (procedureTotalDuration + reqDto.getTimeSheet().getProcedureDuration());

					// get default tariff
				

					// insert order master
					orderMst = new OrderMasterDto();
					orderMst.setAdmissionId(proOrder.getAdmissionId());
					orderMst.setEncounterId(encounterId);
					orderMst.setCreatedBy(createdBy);
					orderMst.setCreatedDate(today.getTime());
					orderMst.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "dd-M-yyyy"));
					orderMst.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
					orderMst.setDiscountPercentage(0);
					orderMst.setOrderDate(today.getTime());
					orderMst.setOrderStatus(ACTIVE);
					orderMst.setOrderStatusId(1);
					orderMst.setOrdRemarks("Procedure Order");
					orderMst.setOrgId(proOrder.getOrgId());
					orderMst.setOrgUnitId(proOrder.getUnitId());
					orderMst.setPatientId(proOrder.getPatientId());
					orderMst.setPaymentEntitlementTypeId(proOrder.getPaymentEntitlementId());
					orderMst.setPriorityId(1);
					orderMst.setUpdatedBy(createdBy);
					orderMst.setUpdatedDate(today.getTime());
					orderMst.setVisitTypeId(proOrder.getVisitTypeId());

					proReqId = proOrder.getProReqId();

					// check services available in packages
					if (reqDto.getOtServices() != null && reqDto.getOtServices().size() > 0) 
					{
						ListIterator<OTServiceDto> otServiceItr = reqDto.getOtServices().listIterator();
						while (otServiceItr.hasNext()) 
						{

							BigDecimal serviceAmt = BigDecimal.ZERO;
							BigDecimal taxAmt = BigDecimal.ZERO;
							BigDecimal netAmt = BigDecimal.ZERO;

							OTServiceDto otService = otServiceItr.next();

							OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
								orderDetails.setCreatedBy(createdBy);
								orderDetails.setCreatedDate(today.getTime());
								orderDetails.setNetAmount(BigDecimal.valueOf(otService.getAmount()));
								orderDetails.setOrderDate(today.getTime());
								orderDetails.setOrderQty(1);
								orderDetails.setOrdTotalAmt(BigDecimal.valueOf(otService.getAmount()));
								orderDetails.setOrgId(otService.getOrgId());
								orderDetails.setOrgUnitId(otService.getUnitId());
								orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
								orderDetails.setServiceAmt(BigDecimal.valueOf(otService.getAmount()));
								orderDetails.setServiceId(otService.getServiceId());
								orderDetails.setServiceIsBilled(0);
								orderDetails.setServiceRendered(0);
								orderDetails.setStatus(ACTIVE);
								orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
								orderDetails.setUpdatedBy(createdBy);
								orderDetails.setUpdatedDate(today.getTime());
								orderDetails.setPriorityId(2);
								orderDetails.setIsOutsourced('N');
								orderDetails.setOrderSchDate(today.getTime());
								orderDetails.setOrdConcession(BigDecimal.valueOf(0));
								orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
								orderDetails.setServiceRenderingDeptId(otService.getSubSpecialityId());
								orderDetails.setOrdCancelled('N');
								orderDetails.setIsDrug('N');
								orderDetails.setIsProcedure(NO);
								orderDetails.setIsProcedureService(NO);
								orderDetails.setOrderDate(currentDate.getTime());
								orderDetails.setOrdRenderDatetime(currentDate.getTime());
								orderDetails.setIsInvestigationOrder('N');
								orderDetails.setCreatedBy(createdBy);
								orderDetails.setUpdatedBy(createdBy);
								orderDetails.setWardId(wardId);
								orderDetails.setRoomId(roomId);
								orderDetails.setBedId(bedId);
								//orderDetails.setProcedureOrderMstId(proOrder.getProcedureOrderId());
	
								ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
									consumptionDto.setOrganizationId(proOrder.getOrgId());
									consumptionDto.setUnitId(proOrder.getUnitId());
									consumptionDto.setVisitTypeId(visitTypeId);
									consumptionDto.setPatientTypeId(patientTypeId);
									consumptionDto.setBillingBedCategoryId(billingBedCategoryId);
									consumptionDto.setPaymentEntitlementId(paymentEntitlementId);
									consumptionDto.setServiceId(otService.getServiceId());
									consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(currentDate, "yyyy-MM-dd"));
									consumptionDto.setEncounterId(encounterId);
									consumptionDto.setAdmissionId(admissionId);
									consumptionDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
									consumptionDto.setSelfPayeeId(payeeId);
									consumptionDto.setPatientId(proOrder.getPatientId());
									consumptionDto.setContractId(contractId);
									consumptionDto.setAssociateCompanyId(associateCompanyId);
									consumptionDto.setPatientCategoryId(patientTypeId);
									consumptionDto.setPatientClassId(patientClassId);
								Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
								consumptionDto = (ContractConsumptionDto) res.getObject();

							if (res.getObject() != null) 
							{
								orderDetails.setServiceRenderingDeptId(consumptionDto.getSubSpecialityId());
								orderDetails.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
								orderDetails.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
								orderDetails.setOrdDiscount(new BigDecimal(0.00));
								orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
								orderDetails.setBillingClassId(proOrder.getBillingBedCategoryId());
								orderDetails.setOrderEmergencyFlag('N');
								orderDetails.setServiceRendered(0);
								orderDetails.setOrdRenderDatetime(currentDate.getTime());
								orderDetails.setServiceChargeable(1);
								orderDetails.setServiceIsBilled(0);
								orderDetails.setIsDrug('N');
								orderDetails.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
								orderDetails.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
								orderDetails.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
								orderDetails.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
								orderDetails.setTaxId(consumptionDto.getTaxId());
								orderDetails.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
								orderDetails.setPayeeId(consumptionDto.getPayeeId());
								orderDetails.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
								orderDetails.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
								orderDetails.setContractId(consumptionDto.getContractId());
								orderDetails.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
								orderDetails.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
								if (consumptionDto.getPayeeId() == reqDto.getPayeeId() && consumptionDto.getContractId() == null) 
								{
									List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
									OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
										detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
										detailsPayeeMapperDto.setPayeeAmount(netAmt);
										detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
										detailsPayeeMapperDto.setAssociateCompanyId(consumptionDto.getAssociateCompanyId());
										if(detailsPayeeMapperDto.getAssociateCompanyId() == null) {
											detailsPayeeMapperDto.setAssociateCompanyId(associateCompanyId);
										}
										list.add(detailsPayeeMapperDto);
									orderDetails.setOrderDetailsPayeeMapperDtosList(list);
								} else
									orderDetails.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
								
								
								if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
									orderDetails.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
								
								listOrderDetails.add(orderDetails);
							}
						}
					}
					if((reqDto.getEquipmentlist()!=null && reqDto.getEquipmentlist().size()>0)|| (reqDto.getOtCompletionMedicalGasList()!=null && reqDto.getOtCompletionMedicalGasList().size()>0))
					{
						
						List<TPeriodicityServiceOrdersDto> finalList = new LinkedList<>();
						for(OtCompletionEquipmentDto equipmentDto : reqDto.getEquipmentlist())
						{
							TPeriodicityServiceOrdersDto periodicityServiceOrdersDto = new TPeriodicityServiceOrdersDto();
							
							Response equipmentRes = iProcedureOrderEquipmentDao.getserviceIdByAssetCat(equipmentDto.getItemId(),orgId,unitId,visitTypeId,0);
								
							if(equipmentRes.getObject()!=null)
							{
								ProcedureOrderMasterDto equipserviceId = (ProcedureOrderMasterDto) equipmentRes.getObject();
								
								AdmissionListIpdChargesResDto chargesResDto = new AdmissionListIpdChargesResDto();
										chargesResDto.setAdmissionId(admissionId);
										chargesResDto.setPatientId(patientId);
										chargesResDto.setPatientCategoryId(patientTypeId);
										chargesResDto.setBedBillingCategoryId(billingBedCategoryId);
										chargesResDto.setPaymentEntitlementId(paymentEntitlementId);
										chargesResDto.setPayeeId(payeeId);
										chargesResDto.setContractId(contractId);
									periodicityServiceOrdersDto.setChargesResDto(chargesResDto);	
									periodicityServiceOrdersDto.setFromDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(equipmentDto.getFromDate(),"yyyy-MM-dd"), "dd-MM-yyyy"));
									periodicityServiceOrdersDto.setToDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(equipmentDto.getToDate(),"yyyy-MM-dd"), "dd-MM-yyyy"));	
									periodicityServiceOrdersDto.setVisitTypeId(visitTypeId);
									periodicityServiceOrdersDto.setPatientId(patientId);
									periodicityServiceOrdersDto.setAdmissionId(admissionId);
									periodicityServiceOrdersDto.setServiceId(equipserviceId.getServiceId());
									periodicityServiceOrdersDto.setToTime(equipmentDto.getToTime().substring(0,5));
									periodicityServiceOrdersDto.setFromTime(equipmentDto.getFromTime().substring(0,5));
									periodicityServiceOrdersDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
									periodicityServiceOrdersDto.setSelfPayeeId(uAppConfDto.getSelfPayeeId());
									periodicityServiceOrdersDto.setMeasurementUnitId(equipserviceId.getMeasurementUnitId());
									periodicityServiceOrdersDto.setDuration(equipserviceId.getDuration());
									periodicityServiceOrdersDto.setStatus('A');
									periodicityServiceOrdersDto.setOrganisationId(orgId);
									periodicityServiceOrdersDto.setUnitId(unitId);
									periodicityServiceOrdersDto.setEquipmentId(equipmentDto.getItemId());
									periodicityServiceOrdersDto.setWardId(wardId);
									periodicityServiceOrdersDto.setRoomId(roomId);
									periodicityServiceOrdersDto.setBedId(bedId);
								finalList.add(periodicityServiceOrdersDto);
							}
							
						}
						for(OtCompletionMedicalGasDto medicalGases : reqDto.getOtCompletionMedicalGasList())
						{
							TPeriodicityServiceOrdersDto periodicityServiceOrdersDto2 = new TPeriodicityServiceOrdersDto();
							
							Response equipmentRes = iProcedureOrderEquipmentDao.getserviceIdByAssetCat(0,orgId,unitId,visitTypeId,medicalGases.getServiceId());
							if(equipmentRes.getObject()!=null)
							{
								ProcedureOrderMasterDto equipserviceId = (ProcedureOrderMasterDto) equipmentRes.getObject();
								
								AdmissionListIpdChargesResDto chargesResDto = new AdmissionListIpdChargesResDto();
										chargesResDto.setAdmissionId(admissionId);
										chargesResDto.setPatientId(patientId);
										chargesResDto.setPatientCategoryId(patientTypeId);
										chargesResDto.setBedBillingCategoryId(billingBedCategoryId);
										chargesResDto.setPaymentEntitlementId(paymentEntitlementId);
										chargesResDto.setPayeeId(payeeId);
										chargesResDto.setContractId(contractId);
									periodicityServiceOrdersDto2.setChargesResDto(chargesResDto);	
									periodicityServiceOrdersDto2.setFromDate(medicalGases.getFromDate().substring(0,10));
									periodicityServiceOrdersDto2.setToDate(medicalGases.getToDate().substring(0,10));	
									periodicityServiceOrdersDto2.setVisitTypeId(visitTypeId);
									periodicityServiceOrdersDto2.setPatientId(patientId);
									periodicityServiceOrdersDto2.setAdmissionId(admissionId);
									periodicityServiceOrdersDto2.setServiceId(equipserviceId.getServiceId());
									periodicityServiceOrdersDto2.setToTime(medicalGases.getToTime().substring(0,5));
									periodicityServiceOrdersDto2.setFromTime(medicalGases.getFromTime().substring(0,5));
									periodicityServiceOrdersDto2.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
									periodicityServiceOrdersDto2.setSelfPayeeId(uAppConfDto.getSelfPayeeId());
									periodicityServiceOrdersDto2.setMeasurementUnitId(equipserviceId.getMeasurementUnitId());
									periodicityServiceOrdersDto2.setDuration(equipserviceId.getDuration());
									periodicityServiceOrdersDto2.setStatus('A');
									periodicityServiceOrdersDto2.setOrganisationId(orgId);
									periodicityServiceOrdersDto2.setUnitId(unitId);
									periodicityServiceOrdersDto2.setWardId(wardId);
									periodicityServiceOrdersDto2.setRoomId(roomId);
									periodicityServiceOrdersDto2.setBedId(bedId);
								finalList.add(periodicityServiceOrdersDto2);
							}
						}
						if(finalList!=null && finalList.size()>0) {
							iTPeriodicityServiceOrdersService.orderTPeriodicityServices(finalList);
						}
						
					}
					
					orderMst.setListOrderDetailsMasterDto(listOrderDetails);
					iOrderMasterService.saveOrderMasterWithDiscount(orderMst, null);

					
					
					//added by minesh for  Consignment Convert Consumption
					// save Medications //medication save
					if (reqDto.getOtcCompletionInvMedication() != null && reqDto.getOtcCompletionInvMedication().size() > 0) {
						int usedQty,uomTypeId;
						long batchId;
						Double taxAmount = 0.0;
						Double netAmount = 0.0;
						Double totalamount = 0.0;
						boolean isMedication = true;
						boolean isImplant = false;
						boolean isConsumable = false;
						boolean flag = false;
						OTCompletionRequest requestObj = null;
						List<OTCompletionItemRequest> itemList = new ArrayList<>();
						
						String returnNumber = GlobalService.getBillNoByGeneratedCode(storeId,unitId, Screen.PatientIssueReturn.getScreen().intValue());
						PatientIssueReturn issueReturn = new PatientIssueReturn(returnNumber,
								new PatientRegistration(patientId), new VisitType(visitTypeId),
								(admissionId !=null && admissionId>0)?new Admission(admissionId):null, new Store(storeId),
								new com.param.entity.model.master.Status(2));
						issueReturn.setEncounter((encounterId!=null && encounterId>0)?new Encounter(encounterId):null);
						//sessionService.setEntityDefaults(issueReturn);
						issueReturn.setCreateDefaults(orgId, unitId, createdBy);
						for (OtcCompletionInvMedicationDto dto : reqDto.getOtcCompletionInvMedication()) 
						{							
							if(dto.getUsedQty() > 0 && (dto.getIssueId() == null || dto.getIssueId() == 0)) {
								usedQty =dto.getUsedQty();
								batchId =Long.valueOf(dto.getBatchId());
								uomTypeId= dto.getUomTypeId() !=null ? dto.getUomTypeId() : 3 ;
								storeId= dto.getStoreId() !=null ? dto.getStoreId() : sessionService.getStoreId() ;
								//callUpdateStoreStockAndBatch(usedQty, batchId, uomTypeId, storeId);
							}
							
							/**@author Ganesh @Code : Ot Medication*/
							// SalesReturn
							OTCompletionItemRequest reqDetails = new OTCompletionItemRequest();
							if ((dto.getIssueId() == null || dto.getIssueId() == 0)
									&& dto.getUsedQty() != null && dto.getUsedQty() > 0)
							{
								netAmount = netAmount + dto.getNetAmount();
								taxAmount = taxAmount + dto.getTaxAmount();
								totalamount = totalamount + dto.getTotalAmount();
								reqDetails.setBatchId(new Long(dto.getBatchId()));
								reqDetails.setItemId(dto.getItemId());
								reqDetails.setMrp(dto.getMrp());
								reqDetails.setRate(dto.getMrp());
								reqDetails.setQuantity(dto.getUsedQty());
								reqDetails.setNetAmount(dto.getNetAmount());
								reqDetails.setTotalAmount(dto.getTotalAmount());
								reqDetails.setSelfPayable(dto.getNetAmount());
								reqDetails.setCreditPayable(0.00);
								reqDetails.setTaxId(dto.getTaxId());
								reqDetails.setTaxAmount(dto.getTaxAmount());
								reqDetails.setTaxPercentage(dto.getTaxPercent());
								reqDetails.setUnitConsessionAmount(dto.getUnitConsessionAmount());
								reqDetails.setUnitDiscountAmount(dto.getUnitDiscountAmount());
								reqDetails.setConsessionAmount(dto.getConsessionAmount());
								reqDetails.setConsessionPercentage(dto.getConsessionPercentage());
								reqDetails.setIssueId(dto.getIssueId() != null ? dto.getIssueId() : null);
								reqDetails.setIssueDetailsBatchMapperId(
										dto.getIssueDetailsBatchMpprId() != null ? dto.getIssueDetailsBatchMpprId()
												: null);
								reqDetails.setUomTypeId(dto.getUomTypeId() == null ? 0 : dto.getUomTypeId());
								reqDetails.setUomUnitId(dto.getUomUnitId() == null ? 0 : dto.getUomUnitId());
								
								reqDetails.setUsedQuantity(dto.getUsedQty());
								storeId = dto.getStoreId();
								reqDetails.setRemainingQty(dto.getRemainingQty());// ot Remaining Qty
								reqDetails.setProductCategoryId(dto.getProductCategoryId());
								itemList.add(reqDetails);
								
							} else if (dto.getIssueId() != null && dto.getIssueId() > 0
									&& dto.getRemainingQty() != null && dto.getRemainingQty() > 0) {
								flag = true;
								PatientIssueReturnDetail patientIssueReturnDetail = new PatientIssueReturnDetail(
										new IssueDetailBatchMapper(dto.getIssueDetailsBatchMpprId()),
										dto.getRemainingQty(), null,
										new com.param.entity.model.master.Status(2));
								
								patientIssueReturnDetail.setReturnReason(new ReasonMaster(1));//need to change
								issueReturn.setStore(new Store(dto.getStoreId() !=null ? dto.getStoreId() : sessionService.getStoreId()));
								issueReturn.setIsImplant(false);
								issueReturn.addPatientIssueReturnDetail(patientIssueReturnDetail);
								//sessionService.setEntityDefaults(patientIssueReturnDetail);
								issueReturn.setCreateDefaults(orgId, unitId, createdBy);
							}
						}
						if (itemList != null && itemList.size() > 0) {
							requestObj = new OTCompletionRequest();
							requestObj.setAdmissionId(admissionId);
							requestObj.setEncounterId(encounterId);
							requestObj.setIsConsumable(isConsumable);
							requestObj.setIsImplant(isImplant);
							requestObj.setPatientId(patientId);
							requestObj.setIsMedication(isMedication);
							requestObj.setItems(itemList);
							requestObj.setNetAmount(netAmount);
							requestObj.setStoreId(storeId);
							requestObj.setTaxAmount(taxAmount);
							requestObj.setTotalAmount(totalamount);
							requestObj.setDoctorId(doctorId);
							requestObj.setVisitTypeId(visitTypeId);
							requestObj.setTaxInclusive(taxInclusive);
							requestObj.setOrganizationId(orgId);
							requestObj.setUnitId(unitId);
						}
						if (requestObj != null) {
							iOTCompletionService.saveOTCompletion(requestObj);
						}
						if (flag == true) {
							iImplantIndentAcceptanceDao.savePatientIssueReturn(issueReturn);
						}
					}
										
					// save Implants // save implants
					if (reqDto.getOtcCompletionInvImplants() != null && reqDto.getOtcCompletionInvImplants().size() > 0) {
						int usedQty,uomTypeId;
						long batchId;
						Double taxAmount = 0.0;
						Double netAmount = 0.0;
						Double totalamount = 0.0;
						boolean isMedication = false;
						boolean isImplant = true;
						boolean isConsumable = false;
						boolean flag = false;
						OTCompletionRequest requestObj = null;
						List<OTCompletionItemRequest> implantList = new ArrayList<>();
						
						/**@author Ganesh @Code : Ot Implant*/
						// SalesReturn
						String returnNumber = GlobalService.getBillNoByGeneratedCode(storeId,
								unitId !=  null ? unitId : sessionService.getUnitId(), Screen.PatientIssueReturn.getScreen().intValue());
						PatientIssueReturn issueReturn = new PatientIssueReturn(returnNumber,
								new PatientRegistration(patientId), new VisitType(visitTypeId),
								(admissionId !=null && admissionId>0)?new Admission(admissionId):null, new Store(storeId),
								new com.param.entity.model.master.Status(2));//pending
						issueReturn.setEncounter((encounterId!=null && encounterId>0)?new Encounter(encounterId):null);
						//sessionService.setEntityDefaults(issueReturn);
						issueReturn.setCreateDefaults(orgId, unitId, createdBy);
						for (OtcCompletionInvImplantsDto dto : reqDto.getOtcCompletionInvImplants()) 
						{
							if(dto.getUsedQty() > 0 && (dto.getIssueId() == null || dto.getIssueId() == 0)) {
								usedQty =dto.getUsedQty();
								batchId =Long.valueOf(dto.getBatchId());
								uomTypeId= dto.getUomTypeId()!=null ? dto.getUomTypeId() :3 ;
								storeId= dto.getStoreId() !=null ? dto.getStoreId() : sessionService.getStoreId() ;
								//callUpdateStoreStockAndBatch(usedQty, batchId, uomTypeId, storeId);
							}
							
							OTCompletionItemRequest reqDetails = new OTCompletionItemRequest();
							if ((dto.getIssueId() == null || dto.getIssueId() == 0)
									&& dto.getUsedQty() != null && dto.getUsedQty()  > 0) 
							{
								
								taxAmount = taxAmount + dto.getTaxAmount();
								netAmount = netAmount + dto.getNetAmount();
								totalamount = totalamount + dto.getTotalAmount();

								reqDetails.setBatchId(new Long(dto.getBatchId()));
								reqDetails.setItemId(dto.getItemId());
								reqDetails.setMrp(dto.getMrp());
								reqDetails.setRate(dto.getMrp());
								reqDetails.setNetAmount(dto.getNetAmount());
								reqDetails.setQuantity(dto.getUsedQty());
								reqDetails.setIssueId(dto.getIssueId() != null ? dto.getIssueId() : null);
								reqDetails.setTotalAmount(dto.getTotalAmount());
								reqDetails.setTaxAmount(dto.getTaxAmount());
								reqDetails.setTaxPercentage(dto.getTaxPercent());
								reqDetails.setTaxId(dto.getTaxId());
								reqDetails.setUnitConsessionAmount(dto.getUnitConsessionAmount());
								reqDetails.setUnitDiscountAmount(dto.getUnitDiscountAmount());
								reqDetails.setConsessionAmount(dto.getConsessionAmount());
								reqDetails.setConsessionPercentage(dto.getConsessionPercentage());
								reqDetails.setSelfPayable(dto.getNetAmount());
								reqDetails.setCreditPayable(0.00);
								
								reqDetails.setTaxId(dto.getTaxId());
								reqDetails.setIssueDetailsBatchMapperId(
										dto.getIssueDetailsBatchMpprId() != null ? dto.getIssueDetailsBatchMpprId()
												: null);
								reqDetails.setUsedQuantity(dto.getUsedQty());
								reqDetails.setRemainingQty(dto.getRemainingQty()); // ot Remaining Qty
								storeId = dto.getStoreId();
								reqDetails.setProductCategoryId(dto.getProductCategoryId());
								implantList.add(reqDetails);
							} else if (dto.getIssueId() != null && dto.getIssueId() > 0
									&& dto.getRemainingQty() != null && dto.getRemainingQty() > 0) {
								flag = true;
								PatientIssueReturnDetail patientIssueReturnDetail = new PatientIssueReturnDetail(
										new IssueDetailBatchMapper(dto.getIssueDetailsBatchMpprId()),
										dto.getRemainingQty(), null,
										new com.param.entity.model.master.Status(2));//for pending
								patientIssueReturnDetail.setReturnReason(new ReasonMaster(1));//need to change
								issueReturn.setStore(new Store(dto.getStoreId() !=null ? dto.getStoreId() : sessionService.getStoreId()));
								issueReturn.setIsImplant(true);
								issueReturn.addPatientIssueReturnDetail(patientIssueReturnDetail);
								//sessionService.setEntityDefaults(patientIssueReturnDetail);
								patientIssueReturnDetail.setCreateDefaults(orgId, unitId, createdBy);
							}
							
						}
						if (implantList != null && implantList.size() > 0) {
							requestObj = new OTCompletionRequest();
							requestObj.setAdmissionId(admissionId);
							requestObj.setEncounterId(encounterId);
							requestObj.setIsConsumable(isConsumable);
							requestObj.setIsImplant(isImplant);
							requestObj.setIsMedication(isMedication);
							requestObj.setItems(implantList);
							requestObj.setPatientId(patientId);
							requestObj.setNetAmount(netAmount);
							requestObj.setStoreId(storeId);
							requestObj.setTaxAmount(taxAmount);
							requestObj.setTotalAmount(totalamount);
							requestObj.setDoctorId(doctorId);
							requestObj.setVisitTypeId(visitTypeId);
							requestObj.setTaxInclusive(taxInclusive);
							requestObj.setOrganizationId(orgId);
							requestObj.setUnitId(unitId);
							if (requestObj != null) {
								iOTCompletionService.saveOTCompletion(requestObj);
							}
							if (flag == true) {
								iImplantIndentAcceptanceDao.savePatientIssueReturn(issueReturn);
							}
						}
					}					
					//code end by minesh for  Consignment Convert Consumption
					
					// Surgion Charges
					//use billofres list
					Integer procedureId = null;
					
					
					if (reqDto.getBillOfresList() != null && reqDto.getBillOfresList().size() > 0) 
					{

						BigDecimal serviceAmt = BigDecimal.ZERO;
						BigDecimal taxAmt = BigDecimal.ZERO;
						BigDecimal netAmt = BigDecimal.ZERO;


						List<OrderDetailsMasterDto> list = new ArrayList<OrderDetailsMasterDto>();

						ListIterator<BillOfMaterialDto> iterator = reqDto.getBillOfresList().listIterator();
						while (iterator.hasNext()) 
						{
							BillOfMaterialDto dto = iterator.next();
							
							procedureId = dto.getProcedureId();
							
							OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
								orderDetails.setCreatedBy(createdBy);
								orderDetails.setCreatedDate(today.getTime());
								orderDetails.setNetAmount(BigDecimal.valueOf(dto.getAmount()));
								orderDetails.setServiceId(dto.getServiceId());
								orderDetails.setOrderDate(today.getTime());
								orderDetails.setOrderQty(1);
								orderDetails.setOrdTotalAmt(BigDecimal.valueOf(dto.getAmount()));
								orderDetails.setOrgId(dto.getOrganizationId());
								orderDetails.setOrgUnitId(dto.getUnitId());
								orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
								orderDetails.setOrdDocId(dto.getDoctorId());
								orderDetails.setSelfPayable(BigDecimal.valueOf(dto.getAmount()));
								orderDetails.setCreditPayable(BigDecimal.ZERO);
								orderDetails.setServiceAmt(BigDecimal.valueOf(dto.getAmount()));
								orderDetails.setServiceIsBilled(0);
								orderDetails.setServiceRendered(1);
								orderDetails.setStatus(ACTIVE);
								orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
								orderDetails.setUpdatedBy(createdBy);
								orderDetails.setUpdatedDate(today.getTime());
								orderDetails.setPriorityId(2);
								orderDetails.setIsOutsourced('N');
								orderDetails.setOrderSchDate(today.getTime());
								orderDetails.setOrdConcession(BigDecimal.valueOf(0));
								orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
								orderDetails.setServiceRenderingDeptId(null);
								orderDetails.setOrdCancelled('N');
								orderDetails.setIsDrug('N');
								orderDetails.setIsProcedure(NO);
								orderDetails.setIsProcedureService(NO);
								orderDetails.setProcedureOrderMstId(dto.getProcedureId());
								orderDetails.setIsFromAddservice('Y');
								orderDetails.setIsProcedureService('Y');
								orderDetails.setIsInvestigationOrder('N');
								orderDetails.setCreatedBy(createdBy);
								orderDetails.setUpdatedBy(createdBy);
								orderDetails.setWardId(wardId);
								orderDetails.setRoomId(roomId);
								orderDetails.setBedId(bedId);
								// Contract Code
								ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
									consumptionDto.setOrganizationId(orgId);
									consumptionDto.setUnitId(unitId);
									consumptionDto.setVisitTypeId(visitTypeId);
									consumptionDto.setCreaditPayable(0.00);
									consumptionDto.setSelfPayable(dto.getAmount());;
									consumptionDto.setPatientTypeId(proOrder.getPatientTypeId());
									consumptionDto.setBillingBedCategoryId(billingBedCategoryId);
									consumptionDto.setPaymentEntitlementId(proOrder.getPaymentEntitlementId());
									consumptionDto.setServiceId(dto.getServiceId());
									consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(currentDate, "yyyy-MM-dd"));
									consumptionDto.setEncounterId(proOrder.getEncounterId());
									consumptionDto.setAdmissionId(proOrder.getAdmissionId());
									consumptionDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
									consumptionDto.setSelfPayeeId(proOrder.getPayeeId());
									consumptionDto.setProcedureId(dto.getProcedureId());
									consumptionDto.setPatientId(proOrder.getPatientId());
									consumptionDto.setServiceRate(dto.getAmount());
									consumptionDto.setContractId(contractId);
									consumptionDto.setPatientClassId(patientClassId);
									consumptionDto.setIsBOM('Y');
								Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
									consumptionDto = (ContractConsumptionDto) res.getObject();	
								if (res.getObject() != null) 
								{
									orderDetails.setServiceRenderingDeptId(consumptionDto.getSubSpecialityId());
									orderDetails.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
									orderDetails.setOrdConcession(new BigDecimal(0.00));
									orderDetails.setOrdDiscount(new BigDecimal(0.00));
									orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
									orderDetails.setBillingClassId(proOrder.getBillingBedCategoryId());
									orderDetails.setOrderEmergencyFlag('N');
									orderDetails.setServiceRendered(1);
									orderDetails.setOrdRenderDatetime(currentDate.getTime());
									orderDetails.setServiceChargeable(1);
									orderDetails.setServiceIsBilled(0);
									orderDetails.setIsDrug('N');
									orderDetails.setPayeeId(proOrder.getPayeeId());
									orderDetails.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
									orderDetails.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
									orderDetails.setNetAmount(BigDecimal.valueOf(consumptionDto.getNetAmount()));
									orderDetails.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage() != null ? consumptionDto.getTaxPercentage() : 0.0));
									orderDetails.setTaxId(consumptionDto.getTaxId());
									orderDetails.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
									orderDetails.setPayeeId(consumptionDto.getPayeeId());
									orderDetails.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
									orderDetails.setContractId(consumptionDto.getContractId());
									orderDetails.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
									orderDetails.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
									orderDetails.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
								    orderDetails.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
								    orderDetails.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
									orderDetails.setOrderDate(currentDate.getTime());
									orderDetails.setOrdRenderDatetime(currentDate.getTime());
									list.add(orderDetails);
							}
								//check ot Rent service		
								if(uAppConfDto.getOtRentServiceId()!=null && uAppConfDto.getOtRentServiceId()>0 && uAppConfDto.getOtRentServiceId().equals(dto.getServiceId()))
								  isOtRentPresentInbom =true;
						}

						OrderMasterDto ordMstDto = new OrderMasterDto();
						ordMstDto.setAdmissionId(proOrder.getAdmissionId());
						ordMstDto.setEncounterId(encounterId);
						ordMstDto.setCreatedBy(createdBy);
						ordMstDto.setCreatedDate(today.getTime());
						ordMstDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "dd-M-yyyy"));
						ordMstDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
						ordMstDto.setDiscountPercentage(0);
						ordMstDto.setOrderDate(today.getTime());
						ordMstDto.setOrderStatus(ACTIVE);
						ordMstDto.setOrderStatusId(1);
						ordMstDto.setOrdRemarks("Procedure Order");
						ordMstDto.setOrgId(proOrder.getOrgId());
						ordMstDto.setOrgUnitId(proOrder.getUnitId());
						ordMstDto.setPatientId(proOrder.getPatientId());
						ordMstDto.setPaymentEntitlementTypeId(proOrder.getPaymentEntitlementId());
						ordMstDto.setPriorityId(1);
						ordMstDto.setUpdatedBy(createdBy);
						ordMstDto.setUpdatedDate(today.getTime());
						ordMstDto.setVisitTypeId(proOrder.getVisitTypeId());

						ordMstDto.setListOrderDetailsMasterDto(list);
						iOrderMasterService.saveOrderMasterOt(ordMstDto);
							
					}
					// TimesheetCharges Charges
					if (reqDto.getTimeSheet() != null) 
					{

						BigDecimal serviceAmt = BigDecimal.ZERO;
						BigDecimal taxAmt = BigDecimal.ZERO;
						BigDecimal netAmt = BigDecimal.ZERO;

						double otRent = 0;
						double extraMin =0;
						AdditionalOtTimeDto dto = new AdditionalOtTimeDto();
						dto.setOrgId(proOrder.getOrgId());
						dto.setUnitId(proOrder.getUnitId());
						dto.setProcedureTypeId(Collections.max(procedureTypeIds));
						Response addtionalOtTime = iAdditionalOtTimeDao.getAddtionalRate(dto);
						
						Integer otRentServiceId = isOtRentPresentInbom?uAppConfDto.getAdditionalOtrentService():uAppConfDto.getOtRentServiceId();
						
						if (reqDto.getTimeSheet().getWheelInTime()!=null && reqDto.getTimeSheet().getWheelOutTime()!=null && addtionalOtTime.getCode().equals(SUCCESS_CODE)&& (double) addtionalOtTime.getObject() > 0) 
						{
							double rate = (double) addtionalOtTime.getObject();
							Date wheelInTime = GlobalCommonDateUtils.getDate(reqDto.getTimeSheet().getWheelInTime(),"dd-MM-yyyy HH:mm:ss");
							Date wheelOutTime = GlobalCommonDateUtils.getDate(reqDto.getTimeSheet().getWheelOutTime(),"dd-MM-yyyy HH:mm:ss");
							long diffInMillies = Math.abs(wheelInTime.getTime() - wheelOutTime.getTime());
							long diffMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
							
							if(diffMinutes > procedureTotalDuration && isOtRentPresentInbom) 
							{
								 extraMin = diffMinutes - procedureTotalDuration;
							     otRent = rate * extraMin;
							}
							else {
							     otRent = rate * diffMinutes;
							 }
							if((isOtRentPresentInbom && extraMin > 0.00) || isOtRentPresentInbom==false) 
							{
								List<OrderDetailsMasterDto> list = new ArrayList<OrderDetailsMasterDto>();
								OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
								orderDetails.setCreatedBy(createdBy);
								orderDetails.setCreatedDate(today.getTime());
								orderDetails.setNetAmount(BigDecimal.valueOf(otRent));
								orderDetails.setOrderDate(today.getTime());
								orderDetails.setOrderQty(1);
								orderDetails.setOrdTotalAmt(BigDecimal.valueOf(otRent));
								orderDetails.setOrgId(dto.getOrgId());
								orderDetails.setOrgUnitId(dto.getUnitId());
								orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
								orderDetails.setSelfPayable(BigDecimal.valueOf(otRent));
								orderDetails.setServiceAmt(BigDecimal.valueOf(otRent));
								orderDetails.setServiceId(otRentServiceId);
								orderDetails.setServiceIsBilled(0);
								orderDetails.setServiceRendered(1);
								orderDetails.setStatus(ACTIVE);
								orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
								orderDetails.setUpdatedBy(createdBy);
								orderDetails.setUpdatedDate(today.getTime());
								orderDetails.setPriorityId(2);
								orderDetails.setIsOutsourced('N');
								orderDetails.setOrderSchDate(today.getTime());
								orderDetails.setOrdConcession(BigDecimal.valueOf(0));
								orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
								orderDetails.setServiceRenderingDeptId(null);
								orderDetails.setOrdCancelled('N');
								orderDetails.setIsDrug('N');
								orderDetails.setIsProcedure(NO);
								orderDetails.setIsProcedureService(NO);
								orderDetails.setProcedureOrderMstId(procedureId);
								orderDetails.setPayeeId(proOrder.getPayeeId());
								orderDetails.setIsInvestigationOrder('N');
								orderDetails.setOrderDate(currentDate.getTime());
								orderDetails.setOrdRenderDatetime(currentDate.getTime());
								orderDetails.setCreatedBy(createdBy);
								orderDetails.setUpdatedBy(createdBy);
								orderDetails.setWardId(wardId);
								orderDetails.setRoomId(roomId);
								orderDetails.setBedId(bedId);
								// Contract Code
								ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
									consumptionDto.setOrganizationId(orgId);
									consumptionDto.setUnitId(unitId);
									consumptionDto.setVisitTypeId(visitTypeId);
									consumptionDto.setCreaditPayable(0.00);
									consumptionDto.setSelfPayable(netAmt.doubleValue());
									consumptionDto.setPatientTypeId(proOrder.getPatientTypeId());
									consumptionDto.setBillingBedCategoryId(billingBedCategoryId);
									consumptionDto.setPaymentEntitlementId(proOrder.getPaymentEntitlementId());
									consumptionDto.setServiceId(otRentServiceId);
									consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(currentDate, "yyyy-MM-dd"));
									consumptionDto.setEncounterId(proOrder.getEncounterId());
									consumptionDto.setAdmissionId(proOrder.getAdmissionId());
									consumptionDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
									consumptionDto.setSelfPayeeId(proOrder.getPayeeId());
									consumptionDto.setServiceRate(otRent);
									consumptionDto.setPatientClassId(patientClassId);
									consumptionDto.setPatientId(proOrder.getPatientId());
									consumptionDto.setProcedureId(proOrder.getProcedureOrderId());
									consumptionDto.setIsBOM('N');
								Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
								consumptionDto = (ContractConsumptionDto) res.getObject();	

								if (res.getObject() != null) {
									orderDetails.setPayeeId(proOrder.getPayeeId());
									orderDetails.setServiceAmt(new BigDecimal(otRent));
									orderDetails.setOrdConcession(new BigDecimal(0.00));
									orderDetails.setOrdDiscount(new BigDecimal(0.00));
									orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
									orderDetails.setBillingClassId(proOrder.getBillingBedCategoryId());
									orderDetails.setOrderEmergencyFlag('N');
									orderDetails.setServiceRendered(1);
									orderDetails.setOrdRenderDatetime(currentDate.getTime());
									orderDetails.setServiceChargeable(1);
									orderDetails.setServiceIsBilled(0);
									orderDetails.setIsDrug('N');
									orderDetails.setOrdTotalAmt(BigDecimal.valueOf(otRent));
									orderDetails.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
									orderDetails.setNetAmount(BigDecimal.valueOf(consumptionDto.getNetAmount()));
									orderDetails.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage() != null ? consumptionDto.getTaxPercentage() : 0.0));
									orderDetails.setTaxId(consumptionDto.getTaxId());
									orderDetails.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
									orderDetails.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
									orderDetails.setContractId(consumptionDto.getContractId());
								    orderDetails.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
								    orderDetails.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
								    orderDetails.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
								    orderDetails.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
								    orderDetails.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
									list.add(orderDetails);

									OrderMasterDto ordMstDto = new OrderMasterDto();
									ordMstDto.setAdmissionId(proOrder.getAdmissionId());
									ordMstDto.setEncounterId(encounterId);
									ordMstDto.setCreatedBy(createdBy);
									ordMstDto.setCreatedDate(today.getTime());
									ordMstDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "dd-M-yyyy"));
									ordMstDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
									ordMstDto.setDiscountPercentage(0);
									ordMstDto.setOrderDate(today.getTime());
									ordMstDto.setOrderStatus(ACTIVE);
									ordMstDto.setOrderStatusId(1);
									ordMstDto.setOrdRemarks("Procedure Order");
									ordMstDto.setOrgId(proOrder.getOrgId());
									ordMstDto.setOrgUnitId(proOrder.getUnitId());
									ordMstDto.setPatientId(proOrder.getPatientId());
									ordMstDto.setPaymentEntitlementTypeId(proOrder.getPaymentEntitlementId());
									ordMstDto.setPriorityId(1);
									ordMstDto.setUpdatedBy(createdBy);
									ordMstDto.setUpdatedDate(today.getTime());
									ordMstDto.setVisitTypeId(proOrder.getVisitTypeId());

									ordMstDto.setListOrderDetailsMasterDto(list);
									iOrderMasterService.saveOrderMasterOt(ordMstDto);
								}
							}
						}

					}
				}
				// update procedure order status
				iProcedureReqDao.updateProcedureReqStatus(procedureRequestId, 2);// 2 as ot bill completion
				List<Integer> allocationIds = new ArrayList<>();
				
				for (OtcCompletionInvImplantsDto implantDto : reqDto.getOtcCompletionInvImplants()) {
					if(implantDto.getAllocationId()!=null) {
						allocationIds.add(implantDto.getAllocationId());
					}
				}
				
				
				for (OtcCompletionInvMedicationDto med : reqDto.getOtcCompletionInvMedication()) {
					if(med.getAllocationId()!=null) {
						allocationIds.add(med.getAllocationId());
					}
				}
				
				iWalkInSalesService.deleteAllocation(Screen.OTCompletion.getScreen(),
						null, allocationIds);
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, PROCEDURE_ORDERED, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcReqDetailsById(int id) throws ApplicationException {
		try {
			return iProcedureOrderDao.getProcReqDetailsById(id);
		} catch (Exception e) {
			
			throw e;
		}
	}

	public double calculateAmountOnPercent(double percentage, double amount) {
		try {
			double discount = (amount / 100) * percentage;
			return (amount - discount);
		} catch (Exception e) {
			throw e;
		}
	}

	public Map<Integer, List<ProcedureOrderComponentDetailsDto>> getProcedureOrderComponentDetails(
			Integer procedureOrderId, ProcedureOrderMasterDto dto, double procedureAmount) {
		try {
			Map<Integer, List<ProcedureOrderComponentDetailsDto>> mapProcedureOrderComponent = new HashMap<>();
			// get surgery resource config
			SurgeryResourceConfigByCritertiaReqDto surgCriteria = new SurgeryResourceConfigByCritertiaReqDto(
					dto.getSpecialityId(), dto.getProcedureTypeId(), 1, dto.getPatientTypeId(),
					dto.getPaymentEntitlementId(), dto.getBillingBedCategoryId(), dto.getOrgId(), dto.getUnitId());
			Response surgRes = iSurgeryResourceConfigDao.getConfigForProcPostingByCriteria(surgCriteria);
			if (surgRes.getCode().equals(SUCCESS_CODE) && surgRes.getObject() != null) {
				SurgeryResourceConfigDto criteriaRes = (SurgeryResourceConfigDto) surgRes.getObject();
				List<ProcedureOrderComponentDetailsDto> listProOrdComp = new ArrayList<>();
				int i = 1;
				while (i <= 4) {
					ProcedureOrderComponentDetailsDto compDto = new ProcedureOrderComponentDetailsDto();
					double amount = 0;
					switch (i) {
					case 1:
						amount = (procedureAmount / 100) * criteriaRes.getSurgeonPercentage();
						compDto.setSurgeonPer(criteriaRes.getSurgeonPercentage());
						break;
					case 2:
						amount = (procedureAmount / 100) * criteriaRes.getAsstSurgeonPercentage();
						compDto.setSurgeonPer(criteriaRes.getAsstSurgeonPercentage());
						break;
					case 3:
						amount = (procedureAmount / 100) * criteriaRes.getAnesthesiologistPercentage();
						compDto.setSurgeonPer(criteriaRes.getAnesthesiologistPercentage());
						break;
					case 4:
						amount = (procedureAmount / 100) * criteriaRes.getOtPercentage();
						compDto.setSurgeonPer(criteriaRes.getOtPercentage());
						break;
					}

					compDto.setAmount(amount);
					compDto.setCreatedBy(dto.getCreatedBy());
					compDto.setDependentId(i);// fixed 1-4
					compDto.setDiscount(0d);
					compDto.setDoctorId(dto.getDoctorId());
					compDto.setNetAmount(amount);
					compDto.setOrgId(dto.getOrgId());
					compDto.setProcedureId(dto.getServiceId());
					compDto.setServiceId(dto.getServiceId());
					compDto.setSpecialityId(dto.getDoctorSpecialityId());
					compDto.setUnitId(dto.getUnitId());
					listProOrdComp.add(compDto);

					i++;
				}
				mapProcedureOrderComponent.put(dto.getProcedureOrderId(), listProOrdComp);
			}
			return mapProcedureOrderComponent;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchProcedureOrder(ProcedureRequestDto procedureRequestDto) throws ApplicationException {
		try {
			return iProcedureOrderDao.searchProcedureOrder(procedureRequestDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getRoleListInOTBilling(int orgId, int orgUnitId) throws ApplicationException {
		try {
			return iProcedureOrderDao.getRoleListInOTBilling(orgId, orgUnitId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcedureOrderList(int encounterId) throws ApplicationException {
		try {
			return iProcedureOrderDao.getProcedureOrderList(encounterId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchProcedureAndPatientDetailsList(OTManagerDashboardDto otManagerDashboardDto)
			throws ApplicationException {
		try {
			return iProcedureOrderDao.searchProcedureAndPatientDetailsList(otManagerDashboardDto);
			/*
			 * Response res =
			 * iProcedureOrderDao.searchProcedureAndPatientDetailsList(otManagerDashboardDto
			 * ); List<OTManagerDashboardDto> listPatientAndProcedureDetailsList =
			 * res.getListObject();
			 * 
			 * List<OTManagerDashboardDto> listPatientAndProcedureDetailsList1 = new
			 * ArrayList();
			 */
			// List<OTManagerDashboardDto> listPatientAndProcedureDetailsList2 =
			// res.getListObject();
			/*
			 * for(OTManagerDashboardDto dto : listPatientAndProcedureDetailsList) {
			 * //System.out.println("listPatientAndProcedureDetailsList*****"+dto.
			 * getProcedureOrderId()); MedicationDetailsDto medicationDetailsDto = new
			 * MedicationDetailsDto();
			 * medicationDetailsDto.setProcedureOrderId(dto.getProcedureOrderId());
			 * medicationDetailsDto.setOrganizationId(dto.getOrganizationId());
			 * medicationDetailsDto.setUnitId(dto.getUnitId());
			 * medicationDetailsDto.setCreatedBy(dto.getCreatedBy()); Response medica=
			 * iMedicationDetailsDao.getMedicationDetailsByProcedureId(medicationDetailsDto)
			 * ;
			 * 
			 * List<MedicationDetailsDto> medList = medica.getListObject();
			 * List<MedicationDetailsDto> medFinalList = new ArrayList();
			 * for(MedicationDetailsDto medDto : medList) {
			 * //System.out.println("medDto*****"+medDto.getProcedureOrderId());
			 * if(medDto.getQty()!=null) { MedicationDetailsDto medDto1 = new
			 * MedicationDetailsDto();
			 * 
			 * medDto1.setKitId(medDto.getKitId()); medDto1.setItemId(medDto.getItemId());
			 * medDto1.setQty(medDto.getQty());
			 * medDto1.setItemQuantity(medDto.getItemQuantity());
			 * medDto1.setItemName(medDto.getItemName());
			 * medDto1.setKitItemName(medDto.getKitItemName());
			 * medDto1.setUomTypeId(medDto.getUomTypeId());
			 * medDto1.setUomUnitId(medDto.getUomUnitId());
			 * medDto1.setIsActive(medDto.getIsActive());
			 * medDto1.setKitItemId(medDto.getKitItemId());
			 * medDto1.setProcedureOrderId(medDto.getProcedureOrderId());
			 * medDto1.setIsKit(medDto.getIsKit());
			 * medDto1.setIsImplant(medDto.getIsImplant());
			 * medDto1.setIsConsignment(medDto.getIsConsignment());
			 * medFinalList.add(medDto1);
			 * 
			 * }
			 * 
			 * } //implants list ImplantDetailsDto implantDetailsDto = new
			 * ImplantDetailsDto();
			 * implantDetailsDto.setProcedureOrderId(dto.getProcedureOrderId());
			 * implantDetailsDto.setOrganizationId(dto.getOrganizationId());
			 * implantDetailsDto.setUnitId(dto.getUnitId());
			 * implantDetailsDto.setCreatedBy(dto.getCreatedBy()); Response implants=
			 * iImplantDetailsDao.getImplantDetailsByProcedureId(implantDetailsDto);
			 * 
			 * List<ImplantDetailsDto> implantList = implants.getListObject();
			 * List<ImplantDetailsDto> impFinalList = new ArrayList(); for(ImplantDetailsDto
			 * imptDto : implantList) {
			 * //System.out.println("imptDto*****"+imptDto.getProcedureOrderId());
			 * if(imptDto.getQty()!=null) { ImplantDetailsDto imptDto1 = new
			 * ImplantDetailsDto();
			 * 
			 * imptDto1.setKitId(imptDto.getKitId());
			 * imptDto1.setItemId(imptDto.getItemId()); imptDto1.setQty(imptDto.getQty());
			 * imptDto1.setItemQuantity(imptDto.getItemQuantity());
			 * imptDto1.setItemName(imptDto.getItemName());
			 * imptDto1.setKitItemName(imptDto.getKitItemName());
			 * imptDto1.setUomTypeId(imptDto.getUomTypeId());
			 * imptDto1.setUomUnitId(imptDto.getUomUnitId());
			 * imptDto1.setIsActive(imptDto.getIsActive());
			 * imptDto1.setKitItemId(imptDto.getKitItemId());
			 * imptDto1.setProcedureOrderId(imptDto.getProcedureOrderId());
			 * imptDto1.setIsKit(imptDto.getIsKit());
			 * imptDto1.setIsImplant(imptDto.getIsImplant());
			 * imptDto1.setIsConsignment(imptDto.getIsConsignment());
			 * impFinalList.add(imptDto1);
			 * 
			 * }
			 * 
			 * }
			 * 
			 * //final list OTManagerDashboardDto otmDto = new OTManagerDashboardDto();
			 * otmDto.setAdmissionId(dto.getAdmissionId()); otmDto.setBed(dto.getBed());
			 * otmDto.setWard(otmDto.getWard()); otmDto.setAge(dto.getAge());
			 * otmDto.setCreatedBy(dto.getCreatedBy());
			 * otmDto.setDoctorId(dto.getDoctorId());
			 * otmDto.setDoctorName(dto.getDoctorName());
			 * otmDto.setEncounterId(dto.getEncounterId());
			 * otmDto.setGenderName(dto.getGenderName());
			 * otmDto.setPatientId(dto.getPatientId());
			 * otmDto.setPatientName(dto.getPatientName());
			 * otmDto.setPriority(dto.getPriority());
			 * otmDto.setProcedureDate(dto.getProcedureDate());
			 * otmDto.setProcedureName(dto.getProcedureName());
			 * otmDto.setProcedureOrderId(dto.getProcedureOrderId());
			 * otmDto.setSpecialityName(dto.getSpecialityName());
			 * otmDto.setUhidNumber(dto.getUhidNumber());
			 * otmDto.setUrgency(dto.getUrgency());
			 * otmDto.setVisitTypeId(dto.getVisitTypeId());
			 * otmDto.setMedicationList(medFinalList); otmDto.setImplantList(impFinalList);
			 * listPatientAndProcedureDetailsList1.add(otmDto); }
			 */
		} catch (Exception e) {
			
			throw e;
		}

	}

	@Override
	@Transactional
	public Response procedureOrderUpdate(ProcedureOrderMasterDto procedureOrderMasterDto) {
		try {

			return iProcedureOrderDao.procedureOrderUpdate(procedureOrderMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcedureRequestId(int orderId) throws ApplicationException {
		try {
			return iProcedureOrderDao.getProcedureRequestId(orderId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getprocedureOrderbyVisitandDate(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try {
			List<ProcedureOrderMasterDto> ProcedureOrderMasterDtoList = new ArrayList<ProcedureOrderMasterDto>();
			Response response =  iProcedureOrderDao.getprocedureOrderbyVisitandDate(procedureOrderMasterDto);
			if(response.getListObject()!= null && response.getListObject().size()>0) {
				ProcedureOrderMasterDtoList.addAll(response.getListObject());
			}
			
			Response response1 =  iProcedureOrderDao.getEmergencyProcedure(procedureOrderMasterDto);
			if(response1.getListObject()!= null && response1.getListObject().size()>0) {
				ProcedureOrderMasterDtoList.addAll(response1.getListObject());
			}
			
		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, ProcedureOrderMasterDtoList, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getProcedureOrderListByAdmissionId(int admissionId, int encounterId, int proReqId) throws ApplicationException {
		try {
			
			List<ProcedureOrderMasterDto> list = new ArrayList<>();
			Response res =  iProcedureOrderDao.getProcedureOrderListByAdmissionId(admissionId,encounterId , proReqId);
			if(res.getListObject().size()>0 && res.getListObject()!=null) {
				list.addAll(res.getListObject());
				
				for (ProcedureOrderMasterDto reqDto : list) {
					
					if(reqDto.getProReqId()!=0) {
						
						ProcedureOrderConsentDto consentDto = new ProcedureOrderConsentDto();
						consentDto.setProcedureOrderId(reqDto.getProcedureOrderId());
						consentDto.setOrganizationId(reqDto.getOrgId());
						consentDto.setUnitId(reqDto.getUnitId());
						List<ProcedureOrderConsentDto> consentList = new ArrayList<>();
						Response consetRes = iProcedureOrderConsentDao.getConsentDetailsByProcedureOrderId(consentDto);
						consentList.addAll(consetRes.getListObject());
						reqDto.setProcedureOrderConsentList(consentList);
						
						List<ProcedureOrderChecklistDto> checkList = new ArrayList<>();
						Response checklistRes = iProcedureOrderChecklistDao.getChecklistDetailsByProcedureOrderId(consentDto);
						checkList.addAll(checklistRes.getListObject());
						reqDto.setProcedureOrderCheckList(checkList);
						
						List<ProcedureOrderStaffDto> staffList = new ArrayList<>();
						Response staffRes = iProcedureOrderStaffDao.getStaffDetailsByProcedureOrderId(consentDto);
						staffList.addAll(staffRes.getListObject());
						reqDto.setProcedureOrderStaffList(staffList);
						
						List<ProcedureOrderEquipmentDto> equipmentList = new ArrayList<>();
						Response equipmentRes = iProcedureOrderEquipmentDao.getEquipmentDetailsByProcedureOrderId(consentDto);
						equipmentList.addAll(equipmentRes.getListObject());
						reqDto.setProcedureOrderEquipmentList(equipmentList);
						
						List<ProcedureOrderServiceDto> serviceList = new ArrayList<>();
						Response serviceRes = iProcedureOrderServiceDao.getServiceDetailsByProcedureOrderId(consentDto);
						serviceList.addAll(serviceRes.getListObject());
						reqDto.setProcedureOrderServiceList(serviceList);
						
						MedicationDetailsDto medicationDetailsDto = new MedicationDetailsDto();
						medicationDetailsDto.setOrganizationId(reqDto.getOrgId());
						medicationDetailsDto.setUnitId(reqDto.getUnitId());
						medicationDetailsDto.setProcedureOrderId(reqDto.getProcedureOrderId());
						
						List<MedicationDetailsDto> medicationList = new ArrayList<>();
						Response medicationRes = iMedicationDetailsDao.getMedicationDetailsByProcedureIdOnOTIMS(medicationDetailsDto);
						medicationList.addAll(medicationRes.getListObject()) ;
						reqDto.setMedicationDetailsList(medicationList);
						
						ImplantDetailsDto implantDetailsDto = new ImplantDetailsDto();
						implantDetailsDto.setOrganizationId(reqDto.getOrgId());
						implantDetailsDto.setUnitId(reqDto.getUnitId());
						implantDetailsDto.setProcedureOrderId(reqDto.getProcedureOrderId());
						
						List<ImplantDetailsDto> implantList = new ArrayList<>();
						Response implantRes = iImplantDetailsDao.getImplantDetailsByProcedureIdOnOTIMS(implantDetailsDto);
						implantList.addAll(implantRes.getListObject());
						reqDto.setImplantDetailsList(implantList);
						
						Response petaientEdu = iProcedureOrderPatientEducationDao.getPatientEducationDetailsByProcedureOrderId(consentDto);
						if(petaientEdu.getObject()!=null) {
							reqDto.setProcedureOrderPatientEducationDto((ProcedureOrderPatientEducationDto)petaientEdu.getObject());
						}
					}
					
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcedureByAdmisssion(int admissionId,int encounterId) throws ApplicationException {
		try {
			return iProcedureOrderDao.getProcedureOrderListByAdmissionIdAndEncounterId(admissionId,encounterId,0);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response toCheckByAdmitionId(int proReqId) throws ApplicationException {
		try {
			return iProcedureOrderDao.toCheckByAdmitionId(proReqId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Transactional
	public List<OtcCompletionInvMedicationDto> getMedicationList(int storeId, String uhidNumber,
			JSONArray inventoryItemsArray, int proReqId, int unitId, Integer orgId) {
		List<OtcCompletionInvMedicationDto> finalMedicationList = new ArrayList<OtcCompletionInvMedicationDto>();
		try {
			String tax = "0.0";
			Double tax1 = 0.0;

			// get issued list pharmacy
			OtcCompletionInvMedicationDto ComplMedication = new OtcCompletionInvMedicationDto();
			ComplMedication.setUhidNumber(uhidNumber);
			Response issuedListRes = OTCompletionDao.getIssueAcceptanceData(ComplMedication);

			Map<Integer, List<OtcCompletionInvMedicationDto>> map = new HashMap<>();

			if (issuedListRes.getListObject() != null && issuedListRes.getListObject().size() > 0
					&& inventoryItemsArray != null && inventoryItemsArray.length() > 0) {

				for (Iterator iterator = issuedListRes.getListObject().iterator(); iterator.hasNext();) {
					OtcCompletionInvMedicationDto dto = (OtcCompletionInvMedicationDto) iterator.next();

					for (int i = 0; i < inventoryItemsArray.length(); i++) {
						JSONObject inventoryItemsObj = inventoryItemsArray.getJSONObject(i);

						// consume item id api
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + inventoryItemsObj.getString("inventoryItemId")
								+ "/store/" + storeId + "/" + unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						if ((inventoryItemsObj.get("inventoryType").equals("Medication"))
								|| (inventoryItemsObj.get("inventoryType").equals("Consumable"))) {

							Integer otimsItemId = Integer.parseInt(inventoryItemsObj.getString("inventoryItemId"));
							JSONObject inventoryItemObj = inventoryItemsArray.getJSONObject(i);
							int otimsusedQty = Integer.parseInt(inventoryItemObj.getString("usedQty"));
							Integer remainingQty = 0;
							Integer bookedQty = 0;
							if (dto.getItemId().intValue() == otimsItemId.intValue()) {
								if (map.containsKey(otimsItemId)) {

									if (!map.get(otimsItemId).get(0).getBatchId().equals(dto.getBatchId())
											&& !map.get(otimsItemId).get(0).getUomTypeId().equals(dto.getUomTypeId())) {
										OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
										newDto.setUsedQty(Integer.parseInt(inventoryItemsObj.getString("usedQty")));
										newDto.setItemId(
												Integer.parseInt(inventoryItemsObj.getString("inventoryItemId")));
										newDto.setGenericId(Integer.parseInt(itemDetailsObj.getString("genericId")));
										newDto.setManufacturerId(
												Integer.parseInt(itemDetailsObj.getString("manufacturerId")));
										newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
										newDto.setItemCode(itemDetailsObj.getString("itemCode"));
										newDto.setItemName(itemDetailsObj.getString("itemName"));
										newDto.setGenericName(itemDetailsObj.getString("genericName"));
										newDto.setManufacturerName(itemDetailsObj.getString("manufacturerName"));
										newDto.setTaxName(itemDetailsObj.getString("taxName"));
										newDto.setTaxPercent(
												Double.parseDouble(itemDetailsObj.getString("taxPercent") != null
														? itemDetailsObj.getString("taxPercent")
														: tax));
										newDto.setRejectedQty(0);
										newDto.setBatchId(dto.getBatchId());
										newDto.setUomType(dto.getUomType());
										newDto.setUomUnit(dto.getUomUnit());
										newDto.setProductCategoryId(Integer.parseInt(itemDetailsObj.getString("productCategoryId")));
										newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										newDto.setIssueId(dto.getIssueId());
										newDto.setUnitId(unitId);
										newDto.setOrgId(orgId);
										newDto.setProcedureRequestId(proReqId);
										newDto.setUomUnitId(dto.getUomUnitId());
										newDto.setUomTypeId(dto.getUomTypeId());
										newDto.setBatchNo(dto.getBatchNo());
										newDto.setBatchDetails(dto.getBatchDetails());
										newDto.setStoreId(storeId);
										newDto.setMrp(dto.getMrp());
										newDto.setIssueId(dto.getIssueId());
										newDto.setUsedQty(0);
										newDto.setBookedQty(dto.getBookedQty());
										newDto.setRemainingQty(dto.getBookedQty());
										newDto.setImplant(dto.isImplant());
										newDto.setConsumable(dto.isConsumable());
										newDto.setBatch(btDtoList);
										map.get(otimsItemId).add(newDto);
									} else {

										List<OtcCompletionInvMedicationDto> dtoList = (List<OtcCompletionInvMedicationDto>) map
												.get(otimsItemId);
										OtcCompletionInvMedicationDto dumyDto = new OtcCompletionInvMedicationDto();
										int listSize = dtoList.size();
										//System.out.print("=========" + listSize);
										int size = (listSize == 1 || listSize == 2) ? 1 : 2;
										dumyDto = dtoList.get(dtoList.size() - 1);
										Integer bQ = dumyDto.getBookedQty();
										Integer uQ = 0;
										if (dumyDto.getUsedQty() == 0) {
											uQ = Integer.parseInt(inventoryItemsObj.getString("usedQty"));
										} else {
											uQ = dumyDto.getUsedQty();
											bQ = dumyDto.getBookedQty() + dto.getBookedQty();
										}

										if (bQ >= uQ) {
											Integer rQ = bQ - uQ;
											dumyDto.setRemainingQty(rQ);
											dumyDto.setUsedQty(uQ);
											dumyDto.setBookedQty(bQ);
											map.get(otimsItemId).set(dtoList.size() - size, dumyDto);
										} else {
											dumyDto.setRemainingQty(0);
											dumyDto.setUsedQty(bQ);

											remainingQty = uQ - bQ;
											map.get(otimsItemId).set(dtoList.size() - size, dumyDto);

											OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
											newDto.setBatch(dto.getBatch());
											newDto.setBatchDetails(dto.getBatchDetails());
											newDto.setBatchId(0L);
											newDto.setBatchNo(null);
											newDto.setGenericName(dto.getGenericName());
											newDto.setItemCode(dto.getItemCode());
											newDto.setItemId(dto.getItemId());
											newDto.setProductCategoryId(dto.getProductCategoryId());
											newDto.setItemName(dto.getItemName());
											newDto.setManufacturerName(dto.getGenericName());
											newDto.setUnitId(unitId);
											newDto.setOrgId(orgId);
											newDto.setIssueId(dto.getIssueId());
											newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
											newDto.setProcedureRequestId(proReqId);
											newDto.setRejectedQty(dto.getRejectedQty());
											newDto.setUomUnit(dto.getUomUnit());
											newDto.setUomType(dto.getUomType());
											newDto.setTotalAmount(dto.getTotalAmount());
											newDto.setTaxPercent(
													dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
											newDto.setUomUnitId(dto.getUomUnitId());
											newDto.setUomTypeId(dto.getUomTypeId());
											newDto.setTaxId(dto.getTaxId());
											newDto.setStoreId(storeId);
											newDto.setMrp(dto.getMrp());
											newDto.setIssueId(dto.getIssueId());
											newDto.setRemainingQty(0);
											newDto.setBookedQty(0);
											newDto.setUsedQty(remainingQty);
											newDto.setImplant(dto.isImplant());
											newDto.setConsumable(dto.isConsumable());
											newDto.setBatch(btDtoList);
											map.get(otimsItemId).add(newDto);
										}
									}
								} else {
									if (dto.getBookedQty() >= otimsusedQty) {
										List<OtcCompletionInvMedicationDto> listTOAdd = new ArrayList<>();
										bookedQty = dto.getBookedQty();
										remainingQty = bookedQty - otimsusedQty;

										OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
										newDto.setBatch(dto.getBatch());
										newDto.setBatchDetails(dto.getBatchDetails());
										newDto.setBatchId(dto.getBatchId());
										newDto.setBatchNo(dto.getBatchNo());
										newDto.setGenericName(dto.getGenericName());
										newDto.setItemCode(dto.getItemCode());
										newDto.setItemId(dto.getItemId());
										newDto.setProductCategoryId(dto.getProductCategoryId());
										newDto.setItemName(dto.getItemName());
										newDto.setManufacturerName(dto.getGenericName());
										newDto.setUnitId(unitId);
										newDto.setOrgId(orgId);
										newDto.setProcedureRequestId(proReqId);
										newDto.setRejectedQty(dto.getRejectedQty());
										newDto.setUomUnit(dto.getUomUnit());
										newDto.setUomType(dto.getUomType());
										newDto.setTotalAmount(dto.getTotalAmount());
										newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
										newDto.setUomUnitId(dto.getUomUnitId());
										newDto.setUomTypeId(dto.getUomTypeId());
										newDto.setStoreId(storeId);
										newDto.setRemainingQty(remainingQty);
										newDto.setBookedQty(bookedQty);
										newDto.setUsedQty(otimsusedQty);
										newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										newDto.setIssueId(dto.getIssueId());
										newDto.setMrp(dto.getMrp());
										newDto.setTaxId(dto.getTaxId());
										newDto.setIssueId(dto.getIssueId());
										newDto.setImplant(dto.isImplant());
										newDto.setConsumable(dto.isConsumable());
										newDto.setBatch(btDtoList);
										listTOAdd.add(newDto);
										map.put(otimsItemId, listTOAdd);
									} else {
										List<OtcCompletionInvMedicationDto> listTOAdd = new ArrayList<>();
										bookedQty = dto.getBookedQty();
										remainingQty = otimsusedQty - bookedQty;

										OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
										newDto.setBatch(dto.getBatch());
										newDto.setBatchDetails(dto.getBatchDetails());
										newDto.setBatchId(dto.getBatchId());
										newDto.setBatchNo(dto.getBatchNo());
										newDto.setGenericName(dto.getGenericName());
										newDto.setItemCode(dto.getItemCode());
										newDto.setItemId(dto.getItemId());
										newDto.setProductCategoryId(dto.getProductCategoryId());
										newDto.setItemName(dto.getItemName());
										newDto.setManufacturerName(dto.getGenericName());
										newDto.setUnitId(unitId);
										newDto.setOrgId(orgId);
										newDto.setProcedureRequestId(proReqId);
										newDto.setRejectedQty(dto.getRejectedQty());
										newDto.setUomUnit(dto.getUomUnit());
										newDto.setUomType(dto.getUomType());
										newDto.setTotalAmount(dto.getTotalAmount());
										newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
										newDto.setUomUnitId(dto.getUomUnitId());
										newDto.setUomTypeId(dto.getUomTypeId());
										newDto.setStoreId(storeId);
										newDto.setMrp(dto.getMrp());
										newDto.setIssueId(dto.getIssueId());
										newDto.setRemainingQty(remainingQty);
										newDto.setTaxId(dto.getTaxId());
										newDto.setBookedQty(bookedQty);
										newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										newDto.setIssueId(dto.getIssueId());
										newDto.setUsedQty(otimsusedQty);
										newDto.setImplant(dto.isImplant());
										newDto.setConsumable(dto.isConsumable());
										newDto.setBatch(btDtoList);
										listTOAdd.add(newDto);

										// For Remaining Quantity
										OtcCompletionInvMedicationDto rNewDto = new OtcCompletionInvMedicationDto();
										rNewDto.setBatch(dto.getBatch());
										rNewDto.setBatchDetails(dto.getBatchDetails());
										rNewDto.setBatchId(dto.getBatchId());
										rNewDto.setBatchNo(dto.getBatchNo());
										rNewDto.setGenericName(dto.getGenericName());
										rNewDto.setItemCode(dto.getItemCode());
										rNewDto.setItemId(dto.getItemId());
										rNewDto.setProductCategoryId(dto.getProductCategoryId());
										rNewDto.setItemName(dto.getItemName());
										rNewDto.setManufacturerName(dto.getGenericName());
										rNewDto.setProcedureRequestId(proReqId);
										rNewDto.setRejectedQty(dto.getRejectedQty());
										rNewDto.setUnitId(unitId);
										rNewDto.setOrgId(orgId);
										rNewDto.setUomUnit(dto.getUomUnit());
										rNewDto.setUomType(dto.getUomType());
										rNewDto.setTotalAmount(dto.getTotalAmount());
										rNewDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
										rNewDto.setUomUnitId(dto.getUomUnitId());
										rNewDto.setUomTypeId(dto.getUomTypeId());
										rNewDto.setStoreId(storeId);
										rNewDto.setMrp(dto.getMrp());
										rNewDto.setIssueId(dto.getIssueId());
										rNewDto.setRemainingQty(0);
										rNewDto.setBookedQty(0);
										rNewDto.setUsedQty(remainingQty);
										rNewDto.setTaxId(dto.getTaxId());
										rNewDto.setImplant(dto.isImplant());
										rNewDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										rNewDto.setIssueId(dto.getIssueId());
										rNewDto.setConsumable(dto.isConsumable());
										List<BatchDto> btDtoList1 = getBatchList(itemDetailsObj);
										rNewDto.setBatch(btDtoList1);

										listTOAdd.add(rNewDto);
										map.put(otimsItemId, listTOAdd);
									}

								}
							} else {
								if (!map.containsKey(otimsItemId)) {
									List<OtcCompletionInvMedicationDto> listToAdd = new ArrayList<>();
									OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
									newDto.setItemId(Integer.parseInt(inventoryItemsObj.getString("inventoryItemId")));
									newDto.setGenericId(Integer.parseInt(itemDetailsObj.getString("genericId")));
									newDto.setManufacturerId(
											Integer.parseInt(itemDetailsObj.getString("manufacturerId")));
									newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
									newDto.setItemCode(itemDetailsObj.getString("itemCode"));
									newDto.setItemName(itemDetailsObj.getString("itemName"));
									newDto.setGenericName(itemDetailsObj.getString("genericName"));
									newDto.setManufacturerName(itemDetailsObj.getString("manufacturerName"));
									newDto.setTaxName(itemDetailsObj.getString("taxName"));
									newDto.setUsedQty(Integer.parseInt(inventoryItemsObj.getString("usedQty")));
									newDto.setTaxPercent(
											Double.parseDouble(itemDetailsObj.getString("taxPercent") != null
													? itemDetailsObj.getString("taxPercent")
													: tax));
									newDto.setRejectedQty(0);
									newDto.setBatchId(0L);
									newDto.setUnitId(unitId);
									newDto.setOrgId(orgId);
									newDto.setProductCategoryId(Integer.parseInt(itemDetailsObj.getString("productCategoryId")));
									newDto.setUomType(dto.getUomType());
									newDto.setUomUnit(dto.getUomUnit());
									newDto.setProcedureRequestId(proReqId);
									newDto.setUomUnitId(dto.getUomUnitId());
									newDto.setUomTypeId(dto.getUomTypeId());
									newDto.setBatchNo(dto.getBatchNo());
									newDto.setBatchDetails(dto.getBatchDetails());
									newDto.setStoreId(storeId);
									newDto.setMrp(dto.getMrp());
									newDto.setIssueId(dto.getIssueId());
									newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
									newDto.setBookedQty(0);
									newDto.setRemainingQty(0);
									newDto.setImplant(dto.isImplant());
									newDto.setConsumable(dto.isConsumable());
									newDto.setBatch(btDtoList);
									listToAdd.add(newDto);
									map.put(otimsItemId, listToAdd);
								}
								if (!map.containsKey(dto.getItemId())) {

									CloseableHttpClient httpClient2 = HttpClientBuilder.create().build();
									String uri3 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId
											+ "/" + unitId;
									HttpGet get2 = new HttpGet(uri3);
									org.apache.http.HttpResponse res2 = httpClient2.execute(get2);
									org.apache.http.HttpEntity resEntity2 = res2.getEntity();
									String apiOutput2 = EntityUtils.toString(resEntity2);
									//System.out.println("apiOutput" + apiOutput2);
									JSONObject itemDetailsObj2 = new JSONObject(apiOutput2);
									List<BatchDto> btDtoList2 = getBatchList(itemDetailsObj2);

									List<OtcCompletionInvMedicationDto> listToAdd = new ArrayList<>();
									OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
									newDto.setBatch(dto.getBatch());
									newDto.setBatchDetails(dto.getBatchDetails());
									newDto.setBatchId(dto.getBatchId());
									newDto.setBatchNo(dto.getBatchNo());
									newDto.setGenericName(dto.getGenericName());
									newDto.setUnitId(unitId);
									newDto.setOrgId(orgId);
									newDto.setItemCode(dto.getItemCode());
									newDto.setItemId(dto.getItemId());
									newDto.setProductCategoryId(dto.getProductCategoryId());
									newDto.setItemName(dto.getItemName());
									newDto.setManufacturerName(dto.getGenericName());
									newDto.setProcedureRequestId(proReqId);
									newDto.setRejectedQty(dto.getRejectedQty());
									newDto.setUomUnit(dto.getUomUnit());
									newDto.setUomType(dto.getUomType());
									newDto.setTotalAmount(dto.getTotalAmount());
									newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
									newDto.setTaxId(dto.getTaxId());
									newDto.setUomUnitId(dto.getUomUnitId());
									newDto.setUomTypeId(dto.getUomTypeId());
									newDto.setStoreId(storeId);
									newDto.setMrp(dto.getMrp());
									newDto.setIssueId(dto.getIssueId());
									newDto.setRemainingQty(dto.getBookedQty());
									newDto.setBookedQty(dto.getBookedQty());
									newDto.setUsedQty(0);
									newDto.setImplant(dto.isImplant());
									newDto.setConsumable(dto.isConsumable());
									newDto.setBatch(btDtoList2);
									newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
									newDto.setIssueId(dto.getIssueId());
									listToAdd.add(newDto);
									map.put(dto.getItemId(), listToAdd);
								}
							}
						}

					}
				}

			} else if (issuedListRes.getListObject().size() > 0
					&& (inventoryItemsArray == null || inventoryItemsArray.length() == 0)) {

				for (Iterator iterator = issuedListRes.getListObject().iterator(); iterator.hasNext();) {
					OtcCompletionInvMedicationDto dto = (OtcCompletionInvMedicationDto) iterator.next();

					CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/" + unitId;
					HttpGet get = new HttpGet(uri1);
					org.apache.http.HttpResponse res = httpClient.execute(get);
					org.apache.http.HttpEntity resEntity = res.getEntity();
					String apiOutput = EntityUtils.toString(resEntity);
					//System.out.println("apiOutput" + apiOutput);
					JSONObject itemDetailsObj = new JSONObject(apiOutput);
					List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

					OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
					newDto.setProductCategoryId(dto.getProductCategoryId());
					newDto.setBatch(dto.getBatch());
					newDto.setBatchDetails(dto.getBatchDetails());
					newDto.setBatchId(dto.getBatchId());
					newDto.setBatchNo(dto.getBatchNo());
					newDto.setGenericName(dto.getGenericName());
					newDto.setItemCode(dto.getItemCode());
					newDto.setItemId(dto.getItemId());
					newDto.setProductCategoryId(dto.getProductCategoryId());
					newDto.setItemName(dto.getItemName());
					newDto.setManufacturerName(dto.getGenericName());
					newDto.setProcedureRequestId(proReqId);
					newDto.setRejectedQty(dto.getRejectedQty());
					newDto.setUomUnit(dto.getUomUnit());
					newDto.setUomType(dto.getUomType());
					newDto.setUnitId(unitId);
					newDto.setOrgId(orgId);
					newDto.setTaxId(itemDetailsObj.getInt("taxId"));
					newDto.setTotalAmount(dto.getTotalAmount());
					newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
					newDto.setUomUnitId(dto.getUomUnitId());
					newDto.setUomTypeId(dto.getUomTypeId());
					newDto.setStoreId(storeId);
					newDto.setMrp(dto.getMrp());
					newDto.setIssueId(dto.getIssueId());
					newDto.setRemainingQty(dto.getBookedQty());
					newDto.setBookedQty(dto.getBookedQty());
					newDto.setUsedQty(0);
					newDto.setImplant(dto.isImplant());
					newDto.setConsumable(dto.isConsumable());
					newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
					newDto.setBatch(btDtoList);
					finalMedicationList.add(newDto);

				}

				if (proReqId != 0) {
					Response otcCompletionInvMedicationRes = iProcedureOrderDao.getMedicationList(proReqId);

					for (Iterator iterator = otcCompletionInvMedicationRes.getListObject().iterator(); iterator
							.hasNext();) {

						OtcCompletionInvMedicationDto dto = (OtcCompletionInvMedicationDto) iterator.next();

						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/"
								+ unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
						newDto.setProductCategoryId(dto.getProductCategoryId());
						newDto.setBatch(dto.getBatch());
						newDto.setOtcCompletionInvMedicationId(dto.getOtcCompletionInvMedicationId());
						newDto.setBatchId(dto.getBatchId());
						newDto.setItemCode(dto.getItemCode());
						newDto.setItemId(dto.getItemId());
						newDto.setProductCategoryId(dto.getProductCategoryId());
						newDto.setTaxId(itemDetailsObj.getInt("taxId"));
						newDto.setItemName(dto.getItemName());
						newDto.setManufacturerName(dto.getGenericName());
						newDto.setProcedureRequestId(proReqId);
						newDto.setRejectedQty(dto.getRejectedQty());
						newDto.setUomUnit(dto.getUomUnit());
						newDto.setUomType(dto.getUomType());
						newDto.setUnitId(unitId);
						newDto.setOrgId(orgId);
						newDto.setTotalAmount(dto.getTotalAmount());
						newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
						newDto.setUomUnitId(dto.getUomUnitId());
						newDto.setUomTypeId(dto.getUomTypeId());
						newDto.setStoreId(storeId);
						newDto.setMrp(dto.getMrp());
						newDto.setIssueId(dto.getIssueId());
						newDto.setRemainingQty(dto.getBookedQty());
						newDto.setBookedQty(dto.getBookedQty());
						newDto.setUsedQty(dto.getUsedQty());
						newDto.setImplant(dto.isImplant());
						newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
						newDto.setIssueId(dto.getIssueId());
						newDto.setConsumable(dto.isConsumable());
						newDto.setBatch(btDtoList);

						finalMedicationList.add(newDto);
					}

				}
			} else if (issuedListRes.getListObject().size() == 0 && (inventoryItemsArray.length() > 0)) {

				for (int i = 0; i < inventoryItemsArray.length(); i++) {
					JSONObject inventoryItemsObj = inventoryItemsArray.getJSONObject(i);

					// consume item id api
					CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					String uri1 = BASEURL + "/api/global/items/" + inventoryItemsObj.getString("inventoryItemId")
							+ "/store/" + storeId + "/" + unitId;
					HttpGet get = new HttpGet(uri1);
					org.apache.http.HttpResponse res = httpClient.execute(get);
					org.apache.http.HttpEntity resEntity = res.getEntity();
					String apiOutput = EntityUtils.toString(resEntity);
					//System.out.println("apiOutput" + apiOutput);
					JSONObject itemDetailsObj = new JSONObject(apiOutput);
					List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

					List<OtcCompletionInvMedicationDto> listToAdd = new ArrayList<>();
					OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
					newDto.setItemId(Integer.parseInt(inventoryItemsObj.getString("inventoryItemId")));
					newDto.setGenericId(Integer.parseInt(itemDetailsObj.getString("genericId")));
					newDto.setManufacturerId(Integer.parseInt(itemDetailsObj.getString("manufacturerId")));
					newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
					newDto.setItemCode(itemDetailsObj.getString("itemCode"));
					newDto.setItemName(itemDetailsObj.getString("itemName"));
					newDto.setGenericName(itemDetailsObj.getString("genericName"));
					newDto.setManufacturerName(itemDetailsObj.getString("manufacturerName"));
					newDto.setTaxName(itemDetailsObj.getString("taxName"));
					newDto.setUsedQty(Integer.parseInt(inventoryItemsObj.getString("usedQty")));
					newDto.setTaxPercent(Double.parseDouble(
							itemDetailsObj.getString("taxPercent") != null ? itemDetailsObj.getString("taxPercent")
									: tax));
					newDto.setRejectedQty(0);
					newDto.setTaxId(itemDetailsObj.getInt("taxId"));
					newDto.setBatchId(0L);
					newDto.setUnitId(unitId);
					newDto.setOrgId(orgId);
					newDto.setProductCategoryId(Integer.parseInt(itemDetailsObj.getString("productCategoryId")));
					// newDto.setUomType();
					// newDto.setUomUnit(dto.getUomUnit());
					newDto.setProcedureRequestId(proReqId);
					// newDto.setUomUnitId(dto.getUomUnitId());
					// newDto.setUomTypeId(dto.getUomTypeId());
					// newDto.setBatchNo(dto.getBatchNo());
					// newDto.setBatchDetails(dto.getBatchDetails());
					newDto.setStoreId(storeId);
					// newDto.setMrp(dto.getMrp());
					// newDto.setIssueId(dto.getIssueId());
					newDto.setBookedQty(0);
					newDto.setRemainingQty(0);
					// newDto.setImplant(dto.isImplant());
					// newDto.setConsumable(dto.isConsumable());
					newDto.setBatch(btDtoList);
					// listToAdd.add(newDto);
					finalMedicationList.add(newDto);

				}

				if (proReqId != 0) {
					Response otcCompletionInvMedicationRes = iProcedureOrderDao.getMedicationList(proReqId);

					for (Iterator iterator = otcCompletionInvMedicationRes.getListObject().iterator(); iterator
							.hasNext();) {

						OtcCompletionInvMedicationDto dto = (OtcCompletionInvMedicationDto) iterator.next();

						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/"
								+ unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
						newDto.setBatch(dto.getBatch());
						newDto.setOtcCompletionInvMedicationId(dto.getOtcCompletionInvMedicationId());
						newDto.setBatchId(dto.getBatchId());
						newDto.setItemCode(dto.getItemCode());
						newDto.setItemId(dto.getItemId());
						newDto.setProductCategoryId(dto.getProductCategoryId());
						newDto.setTaxId(itemDetailsObj.getInt("taxId"));
						newDto.setItemName(dto.getItemName());
						newDto.setManufacturerName(dto.getGenericName());
						newDto.setUnitId(unitId);
						newDto.setOrgId(orgId);
						newDto.setProcedureRequestId(dto.getProcedureRequestId());
						newDto.setRejectedQty(dto.getRejectedQty());
						newDto.setUomUnit(dto.getUomUnit());
						newDto.setUomType(dto.getUomType());
						newDto.setTotalAmount(dto.getTotalAmount());
						newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
						newDto.setUomUnitId(dto.getUomUnitId());
						newDto.setUomTypeId(dto.getUomTypeId());
						newDto.setStoreId(storeId);
						newDto.setMrp(dto.getMrp());
						newDto.setIssueId(dto.getIssueId());
						newDto.setRemainingQty(dto.getBookedQty());
						newDto.setBookedQty(dto.getBookedQty());
						newDto.setUsedQty(dto.getUsedQty());
						newDto.setImplant(dto.isImplant());
						newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
						newDto.setConsumable(dto.isConsumable());
						newDto.setBatch(btDtoList);

						finalMedicationList.add(newDto);
					}

				}
			} else {
				if (proReqId != 0) {
					Response otcCompletionInvMedicationRes = iProcedureOrderDao.getMedicationList(proReqId);

					for (Iterator iterator = otcCompletionInvMedicationRes.getListObject().iterator(); iterator
							.hasNext();) {
						OtcCompletionInvMedicationDto dto = (OtcCompletionInvMedicationDto) iterator.next();

						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/"
								+ unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						OtcCompletionInvMedicationDto newDto = new OtcCompletionInvMedicationDto();
						newDto.setOtcCompletionInvMedicationId(dto.getOtcCompletionInvMedicationId());
						newDto.setBatchId(dto.getBatchId());
						newDto.setItemCode(dto.getItemCode());
						newDto.setItemId(dto.getItemId());
						newDto.setProductCategoryId(dto.getProductCategoryId());
						newDto.setItemName(dto.getItemName());
						newDto.setManufacturerName(dto.getGenericName());
						newDto.setProcedureRequestId(dto.getProcedureRequestId());
						newDto.setTaxId(itemDetailsObj.getInt("taxId"));
						newDto.setRejectedQty(dto.getRejectedQty());
						newDto.setUnitId(unitId);
						newDto.setOrgId(orgId);
						newDto.setUomUnit(dto.getUomUnit());
						newDto.setUomType(dto.getUomType());
						newDto.setTotalAmount(dto.getTotalAmount());
						newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
						newDto.setUomUnitId(dto.getUomUnitId());
						newDto.setUomTypeId(dto.getUomTypeId());
						newDto.setStoreId(storeId);
						newDto.setMrp(dto.getMrp());
						newDto.setIssueId(dto.getIssueId());
						newDto.setRemainingQty(dto.getBookedQty());
						newDto.setBookedQty(dto.getBookedQty());
						newDto.setUsedQty(dto.getUsedQty());
						newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
						newDto.setIssueId(dto.getIssueId());
						newDto.setImplant(dto.isImplant());
						newDto.setConsumable(dto.isConsumable());
						newDto.setBatch(btDtoList);

						finalMedicationList.add(newDto);

					}

				}

			}

			for (Entry<Integer, List<OtcCompletionInvMedicationDto>> a : map.entrySet()) {
				for (OtcCompletionInvMedicationDto otcCompletionInvMedicationDto : a.getValue()) {
					finalMedicationList.add(otcCompletionInvMedicationDto);
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
		return finalMedicationList;

	}

	@Transactional
	public List<OtcCompletionInvImplantsDto> getImplantList(int storeId, String uhidNumber,
			JSONArray inventoryItemsArray, int proReqId, int unitId, Integer orgId) {

		List<OtcCompletionInvImplantsDto> finalImplantList = new ArrayList<OtcCompletionInvImplantsDto>();
		try {
			String tax = "0.0";
			Double tax1 = 0.0;

			// get issued list pharmacy
			OtcCompletionInvImplantsDto ComplImplant = new OtcCompletionInvImplantsDto();
			ComplImplant.setUhidNumber(uhidNumber);
			Response issuedListRes = OTCompletionDao.getImplantIssueAcceptanceData(ComplImplant);

			Map<Integer, List<OtcCompletionInvImplantsDto>> map = new HashMap<>();

			if (issuedListRes.getListObject() != null && issuedListRes.getListObject().size() > 0
					&& inventoryItemsArray != null && inventoryItemsArray.length() > 0) {

				for (Iterator iterator = issuedListRes.getListObject().iterator(); iterator.hasNext();) {
					OtcCompletionInvImplantsDto dto = (OtcCompletionInvImplantsDto) iterator.next();

					for (int i = 0; i < inventoryItemsArray.length(); i++) {
						JSONObject inventoryItemsObj = inventoryItemsArray.getJSONObject(i);

						// consume item id api
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + inventoryItemsObj.getString("inventoryItemId")
								+ "/store/" + storeId + "/" + unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						if ((inventoryItemsObj.get("inventoryType").equals("Implant"))) {

							Integer otimsItemId = Integer.parseInt(inventoryItemsObj.getString("inventoryItemId"));
							JSONObject inventoryItemObj = inventoryItemsArray.getJSONObject(i);
							int otimsusedQty = Integer.parseInt(inventoryItemObj.getString("usedQty"));
							Integer remainingQty = 0;
							Integer bookedQty = 0;
							if (dto.getItemId().intValue() == otimsItemId.intValue()) {
								if (map.containsKey(otimsItemId)) {
									if(map.get(otimsItemId).get(0).getBatchId()==0) {
										List<OtcCompletionInvImplantsDto> dtoList = (List<OtcCompletionInvImplantsDto>) map
												.get(otimsItemId);
										OtcCompletionInvImplantsDto dumyDto = new OtcCompletionInvImplantsDto();
										int listSize = dtoList.size();
										//System.out.print("=========" + listSize);
										int size = (listSize == 1 || listSize == 2) ? 1 : 2;
										dumyDto = dtoList.get(dtoList.size() - 1);
										Integer bQ = dumyDto.getBookedQty();
										Integer uQ = 0;
										if (dumyDto.getUsedQty() == 0) {
											uQ = Integer.parseInt(inventoryItemsObj.getString("usedQty"));
										} else {
											uQ = dumyDto.getUsedQty();
											bQ = dumyDto.getBookedQty() + dto.getBookedQty();
										}

										if (bQ >= uQ) {
											Integer rQ = bQ - uQ;
											dumyDto.setRemainingQty(rQ);
											dumyDto.setUsedQty(uQ);
											dumyDto.setBookedQty(bQ);
											map.get(otimsItemId).set(dtoList.size() - size, dumyDto);
										} else {
											dumyDto.setRemainingQty(0);
											dumyDto.setUsedQty(bQ);

											remainingQty = uQ - bQ;
											map.get(otimsItemId).set(dtoList.size() - size, dumyDto);

											OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
											newDto.setBatch(dto.getBatch());
											newDto.setBatchDetails(dto.getBatchDetails());
											newDto.setBatchId(0L);
											newDto.setBatchNo(null);
											newDto.setItemCode(dto.getItemCode());
											newDto.setItemId(dto.getItemId());
											newDto.setProductCategoryId(dto.getProductCategoryId());
											newDto.setItemName(dto.getItemName());
											newDto.setProcedureRequestId(proReqId);
											newDto.setRejectedQty(dto.getRejectedQty());
											//newDto.setUomUnit(dto.getUomUnit());
											//newDto.setUomType(dto.getUomType());
											newDto.setUnitId(unitId);
											newDto.setOrgId(orgId);
											newDto.setTotalAmount(dto.getTotalAmount());
											newDto.setTaxPercent(
													dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
											//newDto.setUomUnitId(dto.getUomUnitId());
											//newDto.setUomTypeId(dto.getUomTypeId());
											newDto.setStoreId(storeId);
											newDto.setMrp(dto.getMrp());
											newDto.setRemainingQty(0);
											newDto.setBookedQty(0);
											newDto.setUsedQty(remainingQty);
											newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
											newDto.setIssueId(dto.getIssueId());
											newDto.setImplant(dto.isImplant());
											newDto.setConsumable(dto.isConsumable());
											newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
											newDto.setBatch(btDtoList);
											map.get(otimsItemId).add(newDto);
										}
									}else {
									if (!map.get(otimsItemId).get(0).getBatchId().equals(dto.getBatchId())) {//&& !map.get(otimsItemId).get(0).getUomTypeId().equals(dto.getUomTypeId())
										OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
										newDto.setUsedQty(Integer.parseInt(inventoryItemsObj.getString("usedQty")));
										newDto.setItemId(
												Integer.parseInt(inventoryItemsObj.getString("inventoryItemId")));
										newDto.setManufacturerId(
												Integer.parseInt(itemDetailsObj.getString("manufacturerId")));
										newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
										newDto.setItemCode(itemDetailsObj.getString("itemCode"));
										newDto.setItemName(itemDetailsObj.getString("itemName"));
										newDto.setManufacturerName(itemDetailsObj.getString("manufacturerName"));
										newDto.setTaxName(itemDetailsObj.getString("taxName"));
										newDto.setTaxPercent(
												Double.parseDouble(itemDetailsObj.getString("taxPercent") != null
														? itemDetailsObj.getString("taxPercent")
														: tax));
										newDto.setRejectedQty(0);
										newDto.setUnitId(unitId);
										newDto.setOrgId(orgId);
										newDto.setProductCategoryId(Integer.parseInt(itemDetailsObj.getString("productCategoryId")));
										newDto.setBatchId(dto.getBatchId());
										//newDto.setUomType(dto.getUomType());
										//newDto.setUomUnit(dto.getUomUnit());
										newDto.setProcedureRequestId(proReqId);
										//newDto.setUomUnitId(dto.getUomUnitId());
										//newDto.setUomTypeId(dto.getUomTypeId());
										newDto.setBatchNo(dto.getBatchNo());
										newDto.setBatchDetails(dto.getBatchDetails());
										newDto.setStoreId(storeId);
										newDto.setMrp(dto.getMrp());
										newDto.setUsedQty(0);
										newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										newDto.setIssueId(dto.getIssueId());
										newDto.setBookedQty(dto.getBookedQty());
										newDto.setRemainingQty(dto.getBookedQty());
										newDto.setImplant(dto.isImplant());
										newDto.setConsumable(dto.isConsumable());
										newDto.setBatch(btDtoList);
										map.get(otimsItemId).add(newDto);
									} else {

										List<OtcCompletionInvImplantsDto> dtoList = (List<OtcCompletionInvImplantsDto>) map
												.get(otimsItemId);
										OtcCompletionInvImplantsDto dumyDto = new OtcCompletionInvImplantsDto();
										int listSize = dtoList.size();
										//System.out.print("=========" + listSize);
										int size = (listSize == 1 || listSize == 2) ? 1 : 2;
										dumyDto = dtoList.get(dtoList.size() - 1);
										Integer bQ = dumyDto.getBookedQty();
										Integer uQ = 0;
										if (dumyDto.getUsedQty() == 0) {
											uQ = Integer.parseInt(inventoryItemsObj.getString("usedQty"));
										} else {
											uQ = dumyDto.getUsedQty();
											bQ = dumyDto.getBookedQty() + dto.getBookedQty();
										}

										if (bQ >= uQ) {
											Integer rQ = bQ - uQ;
											dumyDto.setRemainingQty(rQ);
											dumyDto.setUsedQty(uQ);
											dumyDto.setBookedQty(bQ);
											map.get(otimsItemId).set(dtoList.size() - size, dumyDto);
										} else {
											dumyDto.setRemainingQty(0);
											dumyDto.setUsedQty(bQ);

											remainingQty = uQ - bQ;
											map.get(otimsItemId).set(dtoList.size() - size, dumyDto);

											OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
											newDto.setBatch(dto.getBatch());
											newDto.setBatchDetails(dto.getBatchDetails());
											newDto.setBatchId(0L);
											newDto.setBatchNo(null);
											newDto.setItemCode(dto.getItemCode());
											newDto.setItemId(dto.getItemId());
											newDto.setProductCategoryId(dto.getProductCategoryId());
											newDto.setItemName(dto.getItemName());
											newDto.setProcedureRequestId(proReqId);
											newDto.setRejectedQty(dto.getRejectedQty());
											//newDto.setUomUnit(dto.getUomUnit());
											//newDto.setUomType(dto.getUomType());
											newDto.setUnitId(unitId);
											newDto.setOrgId(orgId);
											newDto.setTotalAmount(dto.getTotalAmount());
											newDto.setTaxPercent(
													dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
											//newDto.setUomUnitId(dto.getUomUnitId());
											//newDto.setUomTypeId(dto.getUomTypeId());
											newDto.setStoreId(storeId);
											newDto.setMrp(dto.getMrp());
											newDto.setRemainingQty(0);
											newDto.setBookedQty(0);
											newDto.setUsedQty(remainingQty);
											newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
											newDto.setIssueId(dto.getIssueId());
											newDto.setImplant(dto.isImplant());
											newDto.setConsumable(dto.isConsumable());
											newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
											newDto.setBatch(btDtoList);
											map.get(otimsItemId).add(newDto);
										}
									}
								}
								} else {
									if (dto.getBookedQty() >= otimsusedQty) {
										List<OtcCompletionInvImplantsDto> listTOAdd = new ArrayList<>();
										bookedQty = dto.getBookedQty();
										remainingQty = bookedQty - otimsusedQty;

										OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
										newDto.setBatch(dto.getBatch());
										newDto.setBatchDetails(dto.getBatchDetails());
										newDto.setBatchId(dto.getBatchId());
										newDto.setBatchNo(dto.getBatchNo());
										newDto.setItemCode(dto.getItemCode());
										newDto.setItemId(dto.getItemId());
										newDto.setProductCategoryId(dto.getProductCategoryId());
										newDto.setItemName(dto.getItemName());
										newDto.setProcedureRequestId(proReqId);
										newDto.setUnitId(unitId);
										newDto.setOrgId(orgId);
										newDto.setRejectedQty(dto.getRejectedQty());
										//newDto.setUomUnit(dto.getUomUnit());
										//newDto.setUomType(dto.getUomType());
										newDto.setTotalAmount(dto.getTotalAmount());
										newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
										//newDto.setUomUnitId(dto.getUomUnitId());
										//newDto.setUomTypeId(dto.getUomTypeId());
										newDto.setStoreId(storeId);
										newDto.setRemainingQty(remainingQty);
										newDto.setBookedQty(bookedQty);
										newDto.setUsedQty(otimsusedQty);
										newDto.setMrp(dto.getMrp());
										newDto.setIssueId(dto.getIssueId());
										newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										newDto.setImplant(dto.isImplant());
										newDto.setConsumable(dto.isConsumable());
										newDto.setTaxId(dto.getTaxId()); // issued Item Id
										newDto.setBatch(btDtoList);
										listTOAdd.add(newDto);
										map.put(otimsItemId, listTOAdd);
									} else {
										List<OtcCompletionInvImplantsDto> listTOAdd = new ArrayList<>();
										bookedQty = dto.getBookedQty();
										remainingQty = otimsusedQty - bookedQty;

										OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
										newDto.setBatch(dto.getBatch());
										newDto.setBatchDetails(dto.getBatchDetails());
										newDto.setBatchId(dto.getBatchId());
										newDto.setBatchNo(dto.getBatchNo());
										newDto.setItemCode(dto.getItemCode());
										newDto.setItemId(dto.getItemId());
										newDto.setProductCategoryId(dto.getProductCategoryId());
										newDto.setItemName(dto.getItemName());
										newDto.setProcedureRequestId(proReqId);
										newDto.setRejectedQty(dto.getRejectedQty());
										//newDto.setUomUnit(dto.getUomUnit());
										newDto.setUnitId(unitId);
										newDto.setOrgId(orgId);
										//newDto.setUomType(dto.getUomType());
										newDto.setTotalAmount(dto.getTotalAmount());
										newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
										//newDto.setUomUnitId(dto.getUomUnitId());
										//newDto.setUomTypeId(dto.getUomTypeId());
										newDto.setStoreId(storeId);
										newDto.setMrp(dto.getMrp());
										newDto.setRemainingQty(remainingQty);
										newDto.setBookedQty(bookedQty);
										newDto.setUsedQty(otimsusedQty);
										newDto.setImplant(dto.isImplant());
										newDto.setIssueId(dto.getIssueId());
										newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										newDto.setConsumable(dto.isConsumable());
										newDto.setTaxId(dto.getTaxId()); // issued Item Id
										newDto.setBatch(btDtoList);
										listTOAdd.add(newDto);

										// For Remaining Quantity
										OtcCompletionInvImplantsDto rNewDto = new OtcCompletionInvImplantsDto();
										rNewDto.setBatch(dto.getBatch());
										rNewDto.setBatchDetails(dto.getBatchDetails());
										rNewDto.setBatchId(dto.getBatchId());
										rNewDto.setBatchNo(dto.getBatchNo());
										rNewDto.setItemCode(dto.getItemCode());
										rNewDto.setItemId(dto.getItemId());
										rNewDto.setProductCategoryId(dto.getProductCategoryId());
										rNewDto.setItemName(dto.getItemName());
										rNewDto.setProcedureRequestId(proReqId);
										rNewDto.setRejectedQty(dto.getRejectedQty());
										//rNewDto.setUomUnit(dto.getUomUnit());
										//rNewDto.setUomType(dto.getUomType());
										rNewDto.setUnitId(unitId);
										rNewDto.setOrgId(orgId);
										rNewDto.setTotalAmount(dto.getTotalAmount());
										rNewDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
										//rNewDto.setUomUnitId(dto.getUomUnitId());
										//rNewDto.setUomTypeId(dto.getUomTypeId());
										rNewDto.setStoreId(storeId);
										rNewDto.setMrp(dto.getMrp());
										rNewDto.setRemainingQty(0);
										rNewDto.setBookedQty(0);
										rNewDto.setUsedQty(remainingQty);
										rNewDto.setImplant(dto.isImplant());
										rNewDto.setConsumable(dto.isConsumable());
										rNewDto.setIssueId(dto.getIssueId());
										rNewDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
										rNewDto.setTaxId(dto.getTaxId()); // issued Item Id
										List<BatchDto> btDtoList1 = getBatchList(itemDetailsObj);
										rNewDto.setBatch(btDtoList1);

										listTOAdd.add(rNewDto);
										map.put(otimsItemId, listTOAdd);
									}

								}
							} else {
								if (!map.containsKey(otimsItemId)) {
									List<OtcCompletionInvImplantsDto> listToAdd = new ArrayList<>();
									OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
									newDto.setItemId(Integer.parseInt(inventoryItemsObj.getString("inventoryItemId")));
									newDto.setManufacturerId(
											Integer.parseInt(itemDetailsObj.getString("manufacturerId")));
									newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
									newDto.setItemCode(itemDetailsObj.getString("itemCode"));
									newDto.setItemName(itemDetailsObj.getString("itemName"));
									newDto.setManufacturerName(itemDetailsObj.getString("manufacturerName"));
									newDto.setTaxName(itemDetailsObj.getString("taxName"));
									newDto.setUsedQty(Integer.parseInt(inventoryItemsObj.getString("usedQty")));
									newDto.setTaxPercent(
											Double.parseDouble(itemDetailsObj.getString("taxPercent") != null
													? itemDetailsObj.getString("taxPercent")
													: tax));
									newDto.setRejectedQty(0);
									newDto.setBatchId(0L);
									newDto.setUnitId(unitId);
									newDto.setOrgId(orgId);
									newDto.setProductCategoryId(Integer.parseInt(itemDetailsObj.getString("productCategoryId")));
									//newDto.setUomType(dto.getUomType());
									//newDto.setUomUnit(dto.getUomUnit());
									newDto.setProcedureRequestId(proReqId);
								//	newDto.setUomUnitId(dto.getUomUnitId());
								//	newDto.setUomTypeId(dto.getUomTypeId());
									newDto.setBatchNo(dto.getBatchNo());
									newDto.setBatchDetails(dto.getBatchDetails());
									newDto.setStoreId(storeId);
									newDto.setMrp(dto.getMrp());
									newDto.setIssueId(dto.getIssueId());
									newDto.setBookedQty(0);
									newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
									newDto.setRemainingQty(0);
									newDto.setImplant(dto.isImplant());
									newDto.setConsumable(dto.isConsumable());
									newDto.setBatch(btDtoList);
									listToAdd.add(newDto);
									map.put(otimsItemId, listToAdd);
								}
								if (!map.containsKey(dto.getItemId())) {

									CloseableHttpClient httpClient1 = HttpClientBuilder.create().build();
									String uri2 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId
											+ "/" + unitId;
									HttpGet get1 = new HttpGet(uri2);
									org.apache.http.HttpResponse res1 = httpClient1.execute(get1);
									org.apache.http.HttpEntity resEntity1 = res1.getEntity();
									String apiOutput1 = EntityUtils.toString(resEntity1);
									//System.out.println("apiOutput" + apiOutput1);
									JSONObject itemDetailsObj1 = new JSONObject(apiOutput1);
									List<BatchDto> btDtoList1 = getBatchList(itemDetailsObj1);

									List<OtcCompletionInvImplantsDto> listToAdd = new ArrayList<>();
									OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
									newDto.setBatch(dto.getBatch());
									newDto.setBatchDetails(dto.getBatchDetails());
									newDto.setBatchId(dto.getBatchId());
									newDto.setBatchNo(dto.getBatchNo());
									newDto.setItemCode(dto.getItemCode());
									newDto.setItemId(dto.getItemId());
									newDto.setProductCategoryId(dto.getProductCategoryId());
									newDto.setItemName(dto.getItemName());
									newDto.setUnitId(unitId);
									newDto.setOrgId(orgId);
									newDto.setProcedureRequestId(proReqId);
									newDto.setRejectedQty(dto.getRejectedQty());
									//newDto.setUomUnit(dto.getUomUnit());
									//newDto.setUomType(dto.getUomType());
									newDto.setTaxId(dto.getTaxId());// get item id from dto
									newDto.setTotalAmount(dto.getTotalAmount());
									newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
								//	newDto.setUomUnitId(dto.getUomUnitId());
								//	newDto.setUomTypeId(dto.getUomTypeId());
									newDto.setStoreId(storeId);
									newDto.setMrp(dto.getMrp());
									newDto.setIssueId(dto.getIssueId());
									newDto.setRemainingQty(dto.getBookedQty());
									newDto.setBookedQty(dto.getBookedQty());
									newDto.setUsedQty(0);
									newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
									newDto.setImplant(dto.isImplant());
									newDto.setConsumable(dto.isConsumable());
									newDto.setBatch(btDtoList1);
									listToAdd.add(newDto);
									map.put(dto.getItemId(), listToAdd);
								}
							}
						}

					}
				}

			} else if (issuedListRes.getListObject().size() > 0
					&& (inventoryItemsArray == null || inventoryItemsArray.length() == 0)) {

				for (Iterator iterator = issuedListRes.getListObject().iterator(); iterator.hasNext();) {
					OtcCompletionInvImplantsDto dto = (OtcCompletionInvImplantsDto) iterator.next();

					CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/" + unitId;
					HttpGet get = new HttpGet(uri1);
					org.apache.http.HttpResponse res = httpClient.execute(get);
					org.apache.http.HttpEntity resEntity = res.getEntity();
					String apiOutput = EntityUtils.toString(resEntity);
					//System.out.println("apiOutput" + apiOutput);
					JSONObject itemDetailsObj = new JSONObject(apiOutput);
					List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

					OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
					newDto.setProductCategoryId(dto.getProductCategoryId());
					newDto.setBatch(dto.getBatch());
					newDto.setBatchDetails(dto.getBatchDetails());
					newDto.setBatchId(dto.getBatchId());
					newDto.setBatchNo(dto.getBatchNo());
					newDto.setItemCode(dto.getItemCode());
					newDto.setItemId(dto.getItemId());
					newDto.setProductCategoryId(dto.getProductCategoryId());
					newDto.setItemName(dto.getItemName());
					newDto.setUnitId(unitId);
					newDto.setOrgId(orgId);
					newDto.setProcedureRequestId(proReqId);
					newDto.setRejectedQty(dto.getRejectedQty());
					//newDto.setUomUnit(dto.getUomUnit());
					//newDto.setUomType(dto.getUomType());
					newDto.setTaxId(itemDetailsObj.getInt("taxId")!=0 ? itemDetailsObj.getInt("taxId"): 0);
					newDto.setTotalAmount(dto.getTotalAmount());
					newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
					//newDto.setUomUnitId(dto.getUomUnitId());
					//newDto.setUomTypeId(dto.getUomTypeId());
					newDto.setStoreId(storeId);
					newDto.setMrp(dto.getMrp());
					newDto.setRemainingQty(dto.getBookedQty());
					newDto.setBookedQty(dto.getBookedQty());
					newDto.setUsedQty(0);
					newDto.setIssueId(dto.getIssueId());
					newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
					newDto.setImplant(dto.isImplant());
					newDto.setConsumable(dto.isConsumable());
					newDto.setBatch(btDtoList);
					finalImplantList.add(newDto);

				}

				if (proReqId != 0) {
					Response otcCompletionInvImplantRes = iProcedureOrderDao.getImplantsList(proReqId);

					for (Iterator iterator = otcCompletionInvImplantRes.getListObject().iterator(); iterator
							.hasNext();) {

						OtcCompletionInvImplantsDto dto = (OtcCompletionInvImplantsDto) iterator.next();

						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/"
								+ unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
						newDto.setBatch(dto.getBatch());
						newDto.setOtcCompletionInvImplantsId(dto.getOtcCompletionInvImplantsId());
						newDto.setBatchId(dto.getBatchId());
						newDto.setItemCode(dto.getItemCode());
						newDto.setItemId(dto.getItemId());
						newDto.setProductCategoryId(dto.getProductCategoryId());
						newDto.setTaxId(itemDetailsObj.getInt("taxId"));
						newDto.setItemName(dto.getItemName());
						newDto.setUnitId(unitId);
						newDto.setOrgId(orgId);
						newDto.setProcedureRequestId(proReqId);
						newDto.setRejectedQty(dto.getRejectedQty());
						//newDto.setUomUnit(dto.getUomUnit());
						//newDto.setUomType(dto.getUomType());
						newDto.setTotalAmount(dto.getTotalAmount());
						newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
						//newDto.setUomUnitId(dto.getUomUnitId());
						//newDto.setUomTypeId(dto.getUomTypeId());
						newDto.setStoreId(storeId);
						newDto.setMrp(dto.getMrp());
						newDto.setRemainingQty(dto.getBookedQty());
						newDto.setBookedQty(dto.getBookedQty());
						newDto.setIssueId(dto.getIssueId());
						newDto.setUsedQty(dto.getUsedQty());
						newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
						newDto.setImplant(dto.isImplant());
						newDto.setConsumable(dto.isConsumable());
						newDto.setBatch(btDtoList);

						finalImplantList.add(newDto);
					}

				}
			} else if (issuedListRes.getListObject().size() == 0 && (inventoryItemsArray.length() > 0)) {

				for (int i = 0; i < inventoryItemsArray.length(); i++) {
					JSONObject inventoryItemsObj = inventoryItemsArray.getJSONObject(i);

					// consume item id api
					CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					String uri1 = BASEURL + "/api/global/items/" + inventoryItemsObj.getString("inventoryItemId")
							+ "/store/" + storeId + "/" + unitId;
					HttpGet get = new HttpGet(uri1);
					org.apache.http.HttpResponse res = httpClient.execute(get);
					org.apache.http.HttpEntity resEntity = res.getEntity();
					String apiOutput = EntityUtils.toString(resEntity);
					//System.out.println("apiOutput" + apiOutput);
					JSONObject itemDetailsObj = new JSONObject(apiOutput);
					List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

					List<OtcCompletionInvImplantsDto> listToAdd = new ArrayList<>();
					OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
					newDto.setItemId(Integer.parseInt(inventoryItemsObj.getString("inventoryItemId")));
					// newDto.setGenericId(Integer.parseInt(itemDetailsObj.getString("genericId")));
					newDto.setManufacturerId(Integer.parseInt(itemDetailsObj.getString("manufacturerId")));
					newDto.setTaxId(Integer.parseInt(itemDetailsObj.getString("taxId")));
					newDto.setItemCode(itemDetailsObj.getString("itemCode"));
					newDto.setItemName(itemDetailsObj.getString("itemName"));
					// newDto.setGenericName(itemDetailsObj.getString("genericName"));
					newDto.setManufacturerName(itemDetailsObj.getString("manufacturerName"));
					newDto.setTaxName(itemDetailsObj.getString("taxName"));
					newDto.setUsedQty(Integer.parseInt(inventoryItemsObj.getString("usedQty")));
					newDto.setTaxPercent(Double.parseDouble(
							itemDetailsObj.getString("taxPercent") != null ? itemDetailsObj.getString("taxPercent")
									: tax));
					newDto.setRejectedQty(0);
					// newDto.setTaxId(itemDetailsObj.getInt("taxId"));
					newDto.setBatchId(0L);
					newDto.setUnitId(unitId);
					newDto.setOrgId(orgId);
					// newDto.setUomType();
					// newDto.setUomUnit(dto.getUomUnit());
					newDto.setProcedureRequestId(proReqId);
					// newDto.setUomUnitId(dto.getUomUnitId());
					// newDto.setUomTypeId(dto.getUomTypeId());
					// newDto.setBatchNo(dto.getBatchNo());
					// newDto.setBatchDetails(dto.getBatchDetails());
					newDto.setStoreId(storeId);
					newDto.setProductCategoryId(Integer.parseInt(itemDetailsObj.getString("productCategoryId")));
					// newDto.setMrp(dto.getMrp());
					// newDto.setIssueId(dto.getIssueId());
					newDto.setBookedQty(0);
					newDto.setRemainingQty(0);
					// newDto.setImplant(dto.isImplant());
					// newDto.setConsumable(dto.isConsumable());
					newDto.setBatch(btDtoList);
					// listToAdd.add(newDto);
					finalImplantList.add(newDto);

				}

				if (proReqId != 0) {
					Response otcCompletionInvImplantRes = iProcedureOrderDao.getImplantsList(proReqId);

					for (Iterator iterator = otcCompletionInvImplantRes.getListObject().iterator(); iterator
							.hasNext();) {

						OtcCompletionInvImplantsDto dto = (OtcCompletionInvImplantsDto) iterator.next();

						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/"
								+ unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
						newDto.setBatch(dto.getBatch());
						newDto.setOtcCompletionInvImplantsId(dto.getOtcCompletionInvImplantsId());
						newDto.setBatchId(dto.getBatchId());
						newDto.setItemCode(dto.getItemCode());
						newDto.setItemId(dto.getItemId());
						newDto.setProductCategoryId(dto.getProductCategoryId());
						newDto.setTaxId(itemDetailsObj.getInt("taxId"));
						newDto.setItemName(dto.getItemName());
						// newDto.setManufacturerName(dto.getGenericName());
						newDto.setUnitId(unitId);
						newDto.setOrgId(orgId);
						newDto.setProcedureRequestId(dto.getProcedureRequestId());
						newDto.setRejectedQty(dto.getRejectedQty());
					//	newDto.setUomUnit(dto.getUomUnit());
					//	newDto.setUomType(dto.getUomType());
						newDto.setTotalAmount(dto.getTotalAmount());
						newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
					//	newDto.setUomUnitId(dto.getUomUnitId());
					//	newDto.setUomTypeId(dto.getUomTypeId());
						newDto.setStoreId(storeId);
						newDto.setMrp(dto.getMrp());
						newDto.setIssueId(dto.getIssueId());
						newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
						newDto.setRemainingQty(dto.getBookedQty());
						newDto.setBookedQty(dto.getBookedQty());
						newDto.setUsedQty(dto.getUsedQty());
						newDto.setImplant(dto.isImplant());
						newDto.setConsumable(dto.isConsumable());
						newDto.setBatch(btDtoList);

						finalImplantList.add(newDto);
					}

				}
			}

			else {
				if (proReqId != 0) {
					Response otcCompletionInvImplantRes = iProcedureOrderDao.getImplantsList(proReqId);

					for (Iterator iterator = otcCompletionInvImplantRes.getListObject().iterator(); iterator
							.hasNext();) {
						OtcCompletionInvImplantsDto dto = (OtcCompletionInvImplantsDto) iterator.next();

						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						String uri1 = BASEURL + "/api/global/items/" + dto.getItemId() + "/store/" + storeId + "/"
								+ unitId;
						HttpGet get = new HttpGet(uri1);
						org.apache.http.HttpResponse res = httpClient.execute(get);
						org.apache.http.HttpEntity resEntity = res.getEntity();
						String apiOutput = EntityUtils.toString(resEntity);
						//System.out.println("apiOutput" + apiOutput);
						JSONObject itemDetailsObj = new JSONObject(apiOutput);
						List<BatchDto> btDtoList = getBatchList(itemDetailsObj);

						OtcCompletionInvImplantsDto newDto = new OtcCompletionInvImplantsDto();
						newDto.setOtcCompletionInvImplantsId(dto.getOtcCompletionInvImplantsId());
						newDto.setBatchId(dto.getBatchId());
						newDto.setItemCode(dto.getItemCode());
						newDto.setItemId(dto.getItemId());
						newDto.setProductCategoryId(dto.getProductCategoryId());
						newDto.setItemName(dto.getItemName());
						newDto.setProcedureRequestId(dto.getProcedureRequestId());
						newDto.setTaxId(itemDetailsObj.getInt("taxId"));
						newDto.setRejectedQty(dto.getRejectedQty());
					//	newDto.setUomUnit(dto.getUomUnit());
					//	newDto.setUomType(dto.getUomType());
						newDto.setUnitId(unitId);
						newDto.setOrgId(orgId);
						newDto.setTotalAmount(dto.getTotalAmount());
						newDto.setTaxPercent(dto.getTaxPercent() != null ? dto.getTaxPercent() : tax1);
						//newDto.setUomUnitId(dto.getUomUnitId());
					//	newDto.setUomTypeId(dto.getUomTypeId());
						newDto.setStoreId(storeId);
						newDto.setIssueId(dto.getIssueId());
						newDto.setIssueDetailsBatchMpprId(dto.getIssueDetailsBatchMpprId());
						newDto.setMrp(dto.getMrp());
						newDto.setRemainingQty(dto.getBookedQty());
						newDto.setBookedQty(dto.getBookedQty());
						newDto.setUsedQty(dto.getUsedQty());
						newDto.setImplant(dto.isImplant());
						newDto.setConsumable(dto.isConsumable());
						newDto.setBatch(btDtoList);
						finalImplantList.add(newDto);

					}

				}

			}

			for (Entry<Integer, List<OtcCompletionInvImplantsDto>> a : map.entrySet()) {
				for (OtcCompletionInvImplantsDto otcCompletionInvImplantDto : a.getValue()) {
					finalImplantList.add(otcCompletionInvImplantDto);
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
		return finalImplantList;
	}

	public List<BatchDto> getBatchList(JSONObject batchListObj) throws JSONException {
		ObjectMapper mapper = new ObjectMapper();

		try {
			List<BatchDto> batchList = null;
			if (batchListObj != null) {
				batchList = mapper.readValue(batchListObj.getJSONArray("batch").toString(),
						new TypeReference<List<BatchDto>>() {
						});
			}
			return batchList;
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response getProcedureOrderListByDoctorForEMR(int doctorId, int orgId, int orgUnitId)
			throws ApplicationException {
		try {
			return iProcedureOrderDao.getProcedureOrderListByDoctorForEMR(doctorId, orgId, orgUnitId);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getConsentDetailsByProcedureOrderId(ProcedureOrderConsentDto dto)
			throws ApplicationException {
		try {
			return iProcedureOrderConsentDao.getConsentDetailsByProcedureOrderId(dto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveProcedureListFromOPD(List<ProcedureOrderMasterDto> procedureOrderMasterDtoList)
			throws ApplicationException {
		try 
		{
			Date date = TimeZoneComponent.getDateByzone(procedureOrderMasterDtoList.get(0).getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			List<Integer> orderIdList = new ArrayList() ;
			 ProcedureOrderMaster mst = null;
			 ProcedureReqMaster procedureReqMaster = null;
		   for (Iterator iterator = procedureOrderMasterDtoList.iterator(); iterator.hasNext();) 
		   {
			   ProcedureOrderMasterDto procedureOrderMasterDto = (ProcedureOrderMasterDto) iterator.next();
				ProcedureReqMasterDto procedureReqMasterDto = procedureOrderMasterDto.getProcedureReqMasterDto();
				procedureReqMasterDto.setPreferedDateTime(procedureOrderMasterDto.getPreferedDateTime());
				procedureReqMasterDto.setPrimaryProcedureId(procedureOrderMasterDto.getServiceId());
				procedureReqMasterDto.setCreatedDate(dateTime);
				procedureReqMasterDto.setRequestDate(dateTime);
				procedureReqMasterDto.setRequestBy(procedureOrderMasterDto.getCreatedBy());
				procedureReqMasterDto.setProcedureStatusId(procedureOrderMasterDto.getProcedureStatusId());
				procedureReqMasterDto.setOrgId(procedureOrderMasterDto.getOrgId());
				procedureReqMasterDto.setUnitId(procedureOrderMasterDto.getUnitId());
				procedureReqMasterDto.setUpdatedDate(dateTime);
				procedureReqMasterDto.setPreferedTime(procedureOrderMasterDto.getPreferedTime());
			
				if(procedureOrderMasterDto.getIsPrimarySecondary()=='Y') 
				   {
					Response procedureReqRes = iProcedureReqDao.saveProcedureReq(procedureReqMasterDto);
					 procedureReqMaster = (ProcedureReqMaster) procedureReqRes.getObject();

					if (procedureReqRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
							&& procedureReqRes.getObject() != null)
					{
						procedureOrderMasterDto.setProReqId(procedureReqMaster.getProReqId());
						procedureOrderMasterDto.setCreatedDate(dateTime);
						procedureOrderMasterDto.setOrderDateTime(dateTime);
						procedureOrderMasterDto.setExportedToOtims(false);
						Response res = iProcedureOrderDao.saveProcedureListFromOPD(procedureOrderMasterDto);
						mst = (ProcedureOrderMaster) res.getObject();
						orderIdList.add(mst.getProcedureOrderId());

					} 
		           }

				else 
				{

					procedureOrderMasterDto.setProReqId(procedureReqMaster.getProReqId());
					procedureOrderMasterDto.setCreatedDate(dateTime);
					procedureOrderMasterDto.setOrderDateTime(dateTime);
					Response res = iProcedureOrderDao.saveProcedureListFromOPD(procedureOrderMasterDto);
					mst = (ProcedureOrderMaster) res.getObject();
					orderIdList.add(mst.getProcedureOrderId());

				}
			   
			   
			   if(procedureOrderMasterDto.getListCalenderMapperDto()!=null&&procedureOrderMasterDto.getListCalenderMapperDto().size()>0)
			   {
				  for (Iterator iterator2 = procedureOrderMasterDto.getListCalenderMapperDto().iterator(); iterator2.hasNext();) {
					  SurgicalCalenderMapperDto dto = (SurgicalCalenderMapperDto) iterator2.next();
					  dto.setProcedureOrderId(mst.getProcedureOrderId());
					  dto.setOrganisationId(procedureOrderMasterDto.getOrgId());
					  dto.setUnitId(procedureOrderMasterDto.getUnitId());
					  dto.setIsDeleted('N');
					  dto.setStatus('A');
					  dto.setCreatedDate(dateTime);
					  dto.setUpdatedDate(dateTime);
					  iSurgicalCalendarMasterService.saveCalenderSlotMpper(dto);
				}
			   }
		   }
		   return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, orderIdList, mst);
		} catch (Exception e) {
		    	
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcedureOrderListByProReqID(Integer proReqId) throws ApplicationException {
		try {
			return iProcedureOrderDao.getProcedureOrderListByProReqID(proReqId);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getProcedureOrderListForDischargeSummary(Integer admissionId, Integer organizationId,
			Integer unitId) throws ApplicationException {
		try {
		 return iProcedureOrderDao.getProcedureOrderListByAdmissionId(admissionId,0,0);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	
	@Override
	public Response getProcedureOrderListForBookView(ProcedureOrderMasterDto procedureOrderMasterDto)throws ApplicationException {
		// TODO Auto-generated method stub
		return iProcedureOrderDao.getProcedureOrderListForBookView(procedureOrderMasterDto);
	}

	@Override
	public Response procedureOrderEquipment(List<VOProcedureOrderMasterDto> vOProcedureOrderMasterDto)
			throws ApplicationException {
		try {
			ProcedureOrderConsentDto procedureOrderConsentDto = new ProcedureOrderConsentDto();
			List<ProcedureOrderEquipmentDto>  list = new ArrayList<>();
			int count = 0;
			for (VOProcedureOrderMasterDto vodto : vOProcedureOrderMasterDto) {
				procedureOrderConsentDto.setProcedureOrderId(vodto.getProcedureOrderId());
				procedureOrderConsentDto.setOrganizationId(vodto.getOrgId());
				procedureOrderConsentDto.setUnitId(vodto.getUnitId());
			
				Response res = iProcedureOrderEquipmentDao.getEquipmentDetailsByProcedureOrderId(procedureOrderConsentDto);
					if(res.getListObject()!=null && res.getListObject().size()>0) {
						if(count == 0) {
							list.addAll(res.getListObject());
							count = count + 1;
						}else {
							List<ProcedureOrderEquipmentDto> getEquplis1 =  new  ArrayList<>();
							List<ProcedureOrderEquipmentDto> getEquplis = res.getListObject();
							for (ProcedureOrderEquipmentDto getEqupDto : getEquplis) {
								int existOrnot = 0;
								for (ProcedureOrderEquipmentDto addedEqupDto : list) {
									
									if(getEqupDto.getItemId().equals(addedEqupDto.getItemId())) {
										
										if(getEqupDto.getQty()>addedEqupDto.getQty()) {
											
											int quepCount =  getEqupDto.getQty() - addedEqupDto.getQty();
											
											getEqupDto.setQty(quepCount);
											
											//list.add(getEqupDto);
										}
										existOrnot = existOrnot + 1;
									}
								}
								
								if(existOrnot == 0){
									
									getEquplis1.add(getEqupDto);
								}
							}
							list.addAll(getEquplis1);
							count = count + 1;
						}
					}
				}
			List<ProcedureOrderEquipmentDto> finalList = new  ArrayList<>();
			for (ProcedureOrderEquipmentDto addedEqupDto : list) {
		 	
				int equpQty = addedEqupDto.getQty();
				if(equpQty>1) {
					
					for (int i = 1; i <= equpQty; i++) {
						ProcedureOrderEquipmentDto dto = addedEqupDto;
						Integer equpQty1 = 1;
						dto.setQty(equpQty1);
						finalList.add(dto);
					}
					
				}else {
					
					finalList.add(addedEqupDto);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
			

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response procedureOrderStaff(List<VOProcedureOrderMasterDto> vOProcedureOrderMasterDto) throws ApplicationException {
		try {
			 	ProcedureOrderConsentDto procedureOrderConsentDto = new ProcedureOrderConsentDto();
			 	List<ProcedureOrderStaffDto> list = new  ArrayList<>();
			 	int count = 0;
				for (VOProcedureOrderMasterDto vodto : vOProcedureOrderMasterDto) {
					procedureOrderConsentDto.setProcedureOrderId(vodto.getProcedureOrderId());
					procedureOrderConsentDto.setOrganizationId(vodto.getOrgId());
					procedureOrderConsentDto.setUnitId(vodto.getUnitId());
					Response res = iProcedureOrderStaffDao.getStaffDetailsByProcedureOrderId(procedureOrderConsentDto);
					
					if(res.getListObject()!=null && res.getListObject().size()>0) {
						if(count == 0) {
							list.addAll(res.getListObject());
							count = count + 1;
						}else {
							List<ProcedureOrderStaffDto> getStafflis1 =  new  ArrayList<>();
							List<ProcedureOrderStaffDto> getStafflist = res.getListObject();
							for (ProcedureOrderStaffDto getStaffDto : getStafflist) {
								int existOrnot = 0;
								for (ProcedureOrderStaffDto addedStaffDto : list) {
									
									if(getStaffDto.getRoleId().equals(addedStaffDto.getRoleId())) {
										
										if( getStaffDto.getStaffNumber()>addedStaffDto.getStaffNumber()) {
											
											int staffCount = getStaffDto.getStaffNumber() - addedStaffDto.getStaffNumber();
											
											getStaffDto.setStaffNumber(staffCount);
											
											//list.add(getStaffDto);
										}
										existOrnot = existOrnot + 1;
									}
								}
								
								if(existOrnot == 0){
									
									getStafflis1.add(getStaffDto);
								}
							}
							list.addAll(getStafflis1);
							count = count + 1;
						}
					}
				}
				
				List<ProcedureOrderStaffDto> finalList = new  ArrayList<>();
				for (ProcedureOrderStaffDto addedStaffDto : list) {
			 	
					//int staffNumber = addedStaffDto.getStaffNumber();
					if(addedStaffDto.getStaffNumber()>1) {
						
						for (int i = 1; i <= addedStaffDto.getStaffNumber(); i++) {
							ProcedureOrderStaffDto dto = addedStaffDto;
							Integer numberOfStaff = 1;
							dto.setStaffNumber(numberOfStaff);
							finalList.add(dto);
						}
						
					}else {
						
						finalList.add(addedStaffDto);
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);

	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response searchPlannedProcedure(ProcedureRequestDto procedureRequestDto) throws ApplicationException {
		try {
			return iProcedureOrderDao.searchPlannedProcedure(procedureRequestDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response procedureOrderChecklist(Integer proReqId,Integer userId) throws ApplicationException {
		try {
			Response roleres = iProcedureOrderDao.getUserOTIMSRoleByUserID(userId);
			if(roleres.getListObject().size()>0) {
				
				List<ProcedureOrderMasterDto> roleList = new ArrayList<>();
				roleList = roleres.getListObject();
				List<Integer> roleIds = new ArrayList();
				for (ProcedureOrderMasterDto integer : roleList) {
					roleIds.add(integer.getRoleId());
				}
				Response res =  iProcedureOrderDao.getProcedureCheckListByProreqId(proReqId/*,roleIds*/);
				List<ProcedureOrderChecklistDto> orderlist = new ArrayList<>();
				List<ProcedureOrderChecklistDto> finalCheckList = new ArrayList<>();
				if(res.getListObject().size()>0) {
					orderlist = res.getListObject();
					Integer i;
					boolean flag = false;
					for(i=1;i<=6;i++) {
						ProcedureOrderChecklistDto dto = new ProcedureOrderChecklistDto();
						dto.setStageId(i);
						List<ProcedureOrderChecklistDto> formlist = new ArrayList<>();
						for (ProcedureOrderChecklistDto procedureChecklistDto : orderlist) {
							List<ProcedureOrderChecklistDto> finalSavedList  = new ArrayList();
							
							if(i.equals(procedureChecklistDto.getStageId())) {
								Integer status = 1;
								Response ressave =	iProcedureOrderDao.getSaveChecklistItemList(procedureChecklistDto/*,roleIds*/,proReqId,i);
								if(ressave.getListObject().size()>0) {
									status = (Integer) ressave.getObject();
									List<ProcedureOrderChecklistDto> savedList = ressave.getListObject();
									Response res1 =	iProcedureOrderDao.getChecklistItemListByMasterId(procedureChecklistDto/*,roleIds*/);
									
									if(res1.getListObject().size()>0) {
										
										List<ProcedureOrderChecklistDto> orderedList = res1.getListObject();
										if(savedList.size() < orderedList.size())
										{
											for (ProcedureOrderChecklistDto orderedDto : orderedList) {
												Integer checkFlag = 0;
												
												for (ProcedureOrderChecklistDto savedDto : savedList) {
													if(savedDto.getChecklistId() == orderedDto.getChecklistId() && 
															savedDto.getChecklistItemMasterId() == orderedDto.getChecklistItemMasterId()) {
														checkFlag++;
													}
												}
												
												if(checkFlag == 0) {
													flag = true;
													finalSavedList.add(orderedDto);
												}
											}
										}
									}
									if(flag == true ) {
										
	                                    procedureChecklistDto.setChecklistStatusId(1);
									}else {
	                                    procedureChecklistDto.setChecklistStatusId(status);

									}
									savedList.addAll(finalSavedList);
									procedureChecklistDto.setChecklistItemList(savedList);
									
									
									
								}else {
								
									Response res1 =	iProcedureOrderDao.getChecklistItemListByMasterId(procedureChecklistDto/*,roleIds*/);
									if(res1.getListObject().size()>0) {
										procedureChecklistDto.setChecklistItemList(res1.getListObject());
										procedureChecklistDto.setChecklistStatusId(1); //1 for pending ,2 -completed ,3 -override
									}
								}
								int checkCount = 0;
								for (Integer userRoleId : roleIds) {
									
									if(procedureChecklistDto.getRoleId().equals(userRoleId)) {
										procedureChecklistDto.setUserMappedChecklist(true);
										checkCount = 1;
									}
									
								}
								
								if(checkCount == 0) {
									procedureChecklistDto.setUserMappedChecklist(false);
								}
								
								formlist.add(procedureChecklistDto);
								dto.setOtCheckList(formlist);
								finalCheckList.add(dto);
						}
						
						}
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, null, finalCheckList, null);
				}

				
			}else {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureServiceRate(List<ProcedureOrderMasterDto> procedureOrderList) {
		try {
			
			for (ProcedureOrderMasterDto dto : procedureOrderList) {
				
				ContractConsumptionDto contrcDto = new ContractConsumptionDto();
				
				contrcDto.setOrganizationId(dto.getOrgId());
				contrcDto.setUnitId(dto.getUnitId());
				contrcDto.setAdmissionId(dto.getAdmissionId());
				contrcDto.setEncounterId(dto.getAdmissionId());
				contrcDto.setBillingBedCategoryId(dto.getBillingBedCategoryId());
				contrcDto.setPatientCategoryId(dto.getPatientTypeId());
				contrcDto.setVisitTypeId(dto.getVisitTypeId());
				contrcDto.setServiceId(dto.getServiceId());
				contrcDto.setTariffId(dto.getTariffId());
				contrcDto.setProcedureId(null);
				contrcDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
				Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contrcDto, false, false);
				
				if(serviceRateRes.getObject()!=null) {
					
					ContractConsumptionDto 	consumptionDto = (ContractConsumptionDto) serviceRateRes.getObject();
				dto.setRate(consumptionDto.getFinalPrice());
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, procedureOrderList, null);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEquipmentRateFromContract(ProcedureOrderMasterDto procedureOrderDto)
			throws ApplicationException {
		try {
			 Double rate = null;
			 Double serviceRate = null;
			 Double serviceRateperMin = null;
			 BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;
			OtCompletionEquipmentDto dto = new OtCompletionEquipmentDto();
//			Response admissionDto = ipdChargesDao.getAdmissionsById(procedureOrderDto.getAdmissionId());
//			AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
			Response equipmentRes = iProcedureOrderEquipmentDao.getserviceIdByAssetCat(procedureOrderDto.getEquipmentId(),procedureOrderDto.getOrgId(),procedureOrderDto.getUnitId(),procedureOrderDto.getVisitTypeId(),0);
			
			if(equipmentRes.getObject()!= null) {
				ProcedureOrderMasterDto equipserviceId = (ProcedureOrderMasterDto) equipmentRes.getObject();
				dto.setServiceId(equipserviceId.getServiceId());
				dto.setEquipmentTypeId(procedureOrderDto.getEquipmentTypeId());
				dto.setDuration(equipserviceId.getDuration());
				dto.setMeasurementUnitId(equipserviceId.getMeasurementUnitId());
			}
			ContractConsumptionDto consumptionDto = null;
			
			if(dto!=null && dto.getServiceId()!=null && procedureOrderDto.getPreferedDateTime()!=null && procedureOrderDto.getToTime()!=null) {
				 ContractConsumptionDto consumptionDtoFirst = new ContractConsumptionDto();
					 consumptionDtoFirst.setOrganizationId(procedureOrderDto.getOrgId());
					 consumptionDtoFirst.setUnitId(procedureOrderDto.getUnitId());
					 consumptionDtoFirst.setVisitTypeId(procedureOrderDto.getVisitTypeId());
					 consumptionDtoFirst.setPatientTypeId(procedureOrderDto.getPatientTypeId());
					 consumptionDtoFirst.setBillingBedCategoryId(procedureOrderDto.getBillingBedCategoryId());
					 consumptionDtoFirst.setPaymentEntitlementId(SELF);
					 consumptionDtoFirst.setServiceId(dto.getServiceId());
					 consumptionDtoFirst.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(procedureOrderDto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
					 consumptionDtoFirst.setAdmissionId(procedureOrderDto.getAdmissionId()!=null?procedureOrderDto.getAdmissionId():null);
					 consumptionDtoFirst.setEncounterId(procedureOrderDto.getEncounterId()!=null?procedureOrderDto.getEncounterId():null);
					 consumptionDtoFirst.setDefaultSelfTariffId(procedureOrderDto.getTariffId());
					 consumptionDtoFirst.setSelfPayeeId(procedureOrderDto.getPayeeId());
					 
				Response selfRes =iContractConsumptionService.consumeServiceFromContract(consumptionDtoFirst);
					consumptionDtoFirst=(ContractConsumptionDto)selfRes.getObject();
				
					//calculate no of units
					Date fromDate = GlobalCommonDateUtils.getDate(procedureOrderDto.getPreferedDateTime().substring(0,10)+ " " + procedureOrderDto.getFromTime(),"dd-M-yyyy HH:mm");
					Date toDate = GlobalCommonDateUtils.getDate(procedureOrderDto.getPreferedDateTime().substring(0,10) + " " + procedureOrderDto.getToTime(),"dd-M-yyyy HH:mm");
					double noOfUnits = 0.0;
					long duration = dto.getDuration();
					long diff = toDate.getTime() - fromDate.getTime();
					/*switch(dto.getMeasurementUnitId())
					{
						case 1 : //minute
							long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
							
							noOfUnits = (((double)diffMinutes) / duration);
							
							if((((double)diffMinutes) % duration)>0.0)
								noOfUnits = (int)noOfUnits + 1;
							
							serviceRate = consumptionDtoFirst.getServiceRate()/60;
							rate = serviceRate * noOfUnits;
							break;
						case 2 : //hour
							diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
							
							noOfUnits = (((double)diffMinutes/60) / duration);
							
							if((((double)diffMinutes/60) % duration)>0.0)
								noOfUnits = (int)noOfUnits + 1;
							
							serviceRateperMin = consumptionDtoFirst.getServiceRate()/60;
							serviceRate = serviceRateperMin*60;
							rate=serviceRate*noOfUnits;
							break;
					}*/
					
					switch(dto.getMeasurementUnitId())
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
					
					Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(procedureOrderDto.getServiceId(),procedureOrderDto.getUnitId());
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
						
				 consumptionDto = new ContractConsumptionDto();
					consumptionDto.setOrganizationId(procedureOrderDto.getOrgId());
					consumptionDto.setUnitId(procedureOrderDto.getUnitId());
					consumptionDto.setVisitTypeId(procedureOrderDto.getVisitTypeId());
					consumptionDto.setPatientTypeId(procedureOrderDto.getPatientTypeId());
					consumptionDto.setBillingBedCategoryId(procedureOrderDto.getBillingBedCategoryId());
					consumptionDto.setPaymentEntitlementId(SELF);
					consumptionDto.setServiceId(dto.getServiceId());
					consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(procedureOrderDto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
					consumptionDto.setContractId(procedureOrderDto.getContractId());
					consumptionDto.setAdmissionId(procedureOrderDto.getAdmissionId()!=null?procedureOrderDto.getAdmissionId():null);
					consumptionDto.setEncounterId(procedureOrderDto.getEncounterId()!=null?procedureOrderDto.getEncounterId():null);
					consumptionDto.setDefaultSelfTariffId(procedureOrderDto.getTariffId());
					consumptionDto.setSelfPayeeId(procedureOrderDto.getPayeeId());
					consumptionDto.setQuantity(1.0);
					consumptionDto.setServiceRate(serviceRate);
					consumptionDto.setFinalPrice(netAmt.doubleValue());
					consumptionDto.setPatientId(procedureOrderDto.getPatientId());
					consumptionDto.setIsPeriodicityService('Y');
					Response res = iContractConsumptionService.consumeServiceFromContract(consumptionDto);
				//Response res =iContractConsumptionService.consumeServiceFromContractForPeriodicityBilling(consumptionDto);
					consumptionDto=(ContractConsumptionDto)res.getObject();
					dto.setAmount(consumptionDto.getFinalPrice());
		}else {
			dto.setAmount(0.00);
		}
		
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/*@Override
	public Response getChecklistDetailsByProcedureOrderId(ProcedureOrderChecklistDto dto)
			throws ApplicationException {
		try {
			return iProcedureOrderChecklistDao.getChecklistDetailsByProcedureOrderId(dto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getStaffDetailsByProcedureOrderId(ProcedureOrderStaffDto dto)
			throws ApplicationException {
		try {
			return iProcedureOrderStaffDao.getStaffDetailsByProcedureOrderId(dto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getServiceDetailsByProcedureOrderId(ProcedureOrderServiceDto dto)
			throws ApplicationException {
		try {
			return iProcedureOrderServiceDao.getServiceDetailsByProcedureOrderId(dto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientEducationDetailsByProcedureOrderId(ProcedureOrderPatientEducationDto dto)
			throws ApplicationException {
		try {
			return iProcedureOrderPatientEducationDao.getPatientEducationDetailsByProcedureOrderId(dto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEquipmentDetailsByProcedureOrderId(ProcedureOrderEquipmentDto dto) throws ApplicationException {
		try {
			return iProcedureOrderEquipmentDao.getEquipmentDetailsByProcedureOrderId(dto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/


	/*
	 * @Override
	 * 
	 * @Transactional public Response getProcedureOrderById(int proReqId,String
	 * otimsId,int tariffId, int storeId) throws Exception { try{
	 * 
	 * Boolean otimsStatus=false; ProcedureOrderReqDto resDto = new
	 * ProcedureOrderReqDto(); //get procedure orders Response procedureOrderRes =
	 * iProcedureOrderDao.getProcedureOrderById(proReqId);
	 * resDto.setProcedureOrders(procedureOrderRes.getListObject());
	 * 
	 * Integer orgId = resDto.getProcedureOrders().get(0).getOrgId(); Integer unitId
	 * =resDto.getProcedureOrders().get(0).getUnitId(); String UhidNumber =
	 * resDto.getProcedureOrders().get(0).getUhidNumber(); //get OT services List<
	 * OTServiceDto> otServiceList = new ArrayList<OTServiceDto>(); Response
	 * otServiceRes = iOTServiceDao.getOtServicesByProReqId(proReqId);
	 * otServiceList.addAll(otServiceRes.getListObject()); //get time sheet
	 * OtCompletionTimeSheetDto otTimeSheet = new OtCompletionTimeSheetDto();
	 * OtCompletionTimeSheetDto otTimeSheet1 = new OtCompletionTimeSheetDto();
	 * Response timeSheetRes =
	 * iOtCompletionTimeSheetDao.getOtTimeSheetByProReq(proReqId); otTimeSheet1 =
	 * (OtCompletionTimeSheetDto)timeSheetRes.getObject(); //get Ot staff
	 * 
	 * List< OtCompletionStaffDto> otStaffList = new
	 * ArrayList<OtCompletionStaffDto>(); Response otStaffCompletion =
	 * iOtCompletionStaffDao.getOtCompletionStaffByProReqId(proReqId);
	 * 
	 * otStaffList.addAll(otStaffCompletion.getListObject()); otStaffList.size();
	 * 
	 * HttpServletRequest request = ServletUtils.getCurrentRequest(); BASEURL =
	 * ServletUtils.getBaseUrl(request);
	 * 
	 * //System.out.println("static String baseUrl--------"+BASEURL);
	 * if(!otimsId.equals("null")) { final String uri =
	 * Constants.OTIMS_ROOT_URL+"/procedureDetails";
	 * 
	 * UriComponentsBuilder builder =
	 * UriComponentsBuilder.fromHttpUrl(uri).queryParam("id", otimsId); RestTemplate
	 * restTemplate = new RestTemplate(); HttpHeaders headers = new HttpHeaders();
	 * headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	 * HttpEntity<String> entity = new HttpEntity<String>(headers);
	 * ResponseEntity<String> result =
	 * restTemplate.exchange(builder.build().toUriString(),
	 * org.springframework.http.HttpMethod.GET, entity, String.class);
	 * 
	 * //System.out.println(result.getBody());
	 * 
	 * JSONObject jsonObj = new JSONObject(result.getBody());
	 * 
	 * JSONArray doctorChargesArray = jsonObj.getJSONArray("doctorCharges");
	 * if(doctorChargesArray.length()>0) { for (int i = 0; i <
	 * doctorChargesArray.length(); i++) { otimsStatus=true; JSONObject
	 * doctorChargesObj = doctorChargesArray.getJSONObject(i);
	 * 
	 * JSONArray proceduresArray = doctorChargesObj.getJSONArray("procedures"); for
	 * (int j = 0; j < proceduresArray.length(); j++) { JSONObject procedureObj =
	 * proceduresArray.getJSONObject(j); OtCompletionStaffDto dto = new
	 * OtCompletionStaffDto(); dto.setRoleName(procedureObj.getString("role"));
	 * dto.setDoctorName(doctorChargesObj.getString("doctor"));
	 * dto.setProcedureOrderId(Integer.parseInt(procedureObj.getString("procedureId"
	 * )));
	 * dto.setProcedureRoleId(Integer.parseInt(procedureObj.getString("roleId")));
	 * dto.setUserId(Integer.parseInt(doctorChargesObj.getString("doctorId")));
	 * dto.setStatus('A');
	 * 
	 * ResourceChargeConfigurationDto configDto = new
	 * ResourceChargeConfigurationDto(); configDto.setOrgId(orgId);
	 * configDto.setUnitId(unitId); configDto.setRoleId(dto.getProcedureRoleId());
	 * configDto.setDoctorId(dto.getUserId());
	 * 
	 * 
	 * Optional<ProcedureOrderMasterDto> matchingObject =
	 * resDto.getProcedureOrders().stream().filter(p ->
	 * p.getProcedureOrderId().equals(dto.getProcedureOrderId())) .findFirst();
	 * ProcedureOrderMasterDto procedureOrderMasterDto = matchingObject.get();
	 * 
	 * configDto.setServiceId(procedureOrderMasterDto.getServiceId());
	 * dto.setProcedureId(procedureOrderMasterDto.getServiceId()); Response
	 * surgionChargesRes = iResourceChargeConfigurationDao
	 * .getResourceCharegesByDoctor(configDto); if (surgionChargesRes.getObject() !=
	 * null) { ResourceChargeConfigurationDto rChargeConfigDto =
	 * (ResourceChargeConfigurationDto) surgionChargesRes .getObject();
	 * dto.setAmount(rChargeConfigDto.getAmount().doubleValue()); }
	 * 
	 * otStaffList.add(dto); } } }
	 * 
	 * 
	 *//*********************** Start get services from otims *******************/
	/*
	 * 
	 * JSONArray serviceArray = jsonObj.getJSONArray("services");
	 * if(serviceArray.length()>0) { for (int i = 0; i < serviceArray.length(); i++)
	 * { otimsStatus=true; UnitServiceTariffMasterDto unitServiceTariffMasterDto =
	 * new UnitServiceTariffMasterDto(); OTServiceDto servicedto = new
	 * OTServiceDto(); JSONObject serviceObj = serviceArray.getJSONObject(i);
	 * servicedto.setServiceName(serviceObj.getString("service"));
	 * servicedto.setSpecialityId(Integer.parseInt(serviceObj.getString(
	 * "specialityId")));
	 * servicedto.setProcedureId(Integer.parseInt(serviceObj.getString("proReqId")))
	 * ;
	 * servicedto.setServiceId(Integer.parseInt(serviceObj.getString("serviceId")));
	 * servicedto.setStatus('A');
	 * servicedto.setSpeciality(serviceObj.getString("speciality"));
	 * servicedto.setUnitId(unitId); servicedto.setOrgId(orgId); List<Integer>
	 * serviceList = new ArrayList<Integer>();
	 * serviceList.add(Integer.parseInt(serviceObj.getString("serviceId")));
	 * serviceList.add(69); unitServiceTariffMasterDto.setTariffId(tariffId);
	 * unitServiceTariffMasterDto.setUnitId(unitId);
	 * unitServiceTariffMasterDto.setOrganizationId(orgId);
	 * unitServiceTariffMasterDto.setPatientTypeId(resDto.getProcedureOrders().get(0
	 * ).getPatientTypeId());
	 * unitServiceTariffMasterDto.setPaymentEntitlementId(resDto.getProcedureOrders(
	 * ).get(0).getPaymentEntitlementId());
	 * unitServiceTariffMasterDto.setBillingBedCategoryId(resDto.getProcedureOrders(
	 * ).get(0).getBillingBedCategoryId());
	 * unitServiceTariffMasterDto.setOrderDate(resDto.getProcedureOrders().get(0).
	 * getOrderDateTime()); unitServiceTariffMasterDto.setServiceList(serviceList);
	 * unitServiceTariffMasterDto.setVisitTypeId(resDto.getProcedureOrders().get(0).
	 * getVisitTypeId()); Response servRateRes = iUnitServiceTariffMasterService.
	 * getBasePriceOfServicesFromTariffMasterByServiceList(
	 * unitServiceTariffMasterDto); UnitServiceTariffMasterDto servRateDto =
	 * (UnitServiceTariffMasterDto)servRateRes.getObject(); if(servRateDto != null){
	 * servicedto.setRate(servRateDto.getRate());
	 * servicedto.setAmount(servRateDto.getRate()); } serviceList=null;
	 * otServiceList.add(servicedto); } }
	 * 
	 * 
	 *//*********************** End get services from otims *******************/
	/*
	
	
	
	*//*********************** Start Get Store Id from theatre ID *******************/
	/*
	 * int operationTheatreId = jsonObj.getInt("operationTheatreId"); Response store
	 * = iOperationTheatreDao.getOperationTheatreById(operationTheatreId, orgId,
	 * unitId); OperationTheatreDto oTDto = new OperationTheatreDto();
	 * oTDto=(OperationTheatreDto)store.getListObject().get(0); int
	 * storeId1=oTDto.getStoreId(); //System.out.println("storeId           "+storeId
	 * );
	 *//*********************** End Get Store Id from theatre ID *******************/
	/*
	
	*//*********************** Start get medication issued list *******************/
	/*
	 * 
	 * JSONArray inventoryMedArray = jsonObj.getJSONArray("inventoryItems");
	 * JSONArray finalArray = new JSONArray(); if (inventoryMedArray.length() > 0) {
	 * for (int i = 0; i < inventoryMedArray.length(); i++) { JSONObject
	 * inventoryItemObj = inventoryMedArray.getJSONObject(i);
	 * if(inventoryItemObj.get("inventoryType").equals("Medication") ||
	 * inventoryItemObj.get("inventoryType").equals("Consumable")) { //finalArray =
	 * new JSONArray(inventoryItemObj);
	 * finalArray.put(inventoryMedArray.getJSONObject(i)); }
	 * 
	 * if(inventoryItemObj.get("inventoryType").equals("Medication")) { //finalArray
	 * = new JSONArray(inventoryItemObj);
	 * finalArray.put(inventoryMedArray.getJSONObject(i)); } } }
	 * List<OtcCompletionInvMedicationDto> medicationList =
	 * this.getMedicationList(storeId1,UhidNumber, finalArray,proReqId
	 * ,unitId,orgId);
	 * 
	 * resDto.setOtcCompletionInvMedication(medicationList);
	 *//*********************** End get medication issued list *******************/
	/*
	
	
	*//*********************** Start get implant issued list *******************/
	/*
	 * JSONArray finalImplantArray = new JSONArray(); ; if
	 * (inventoryMedArray.length() > 0) { for (int i = 0; i <
	 * inventoryMedArray.length(); i++) { JSONObject inventoryItemObj =
	 * inventoryMedArray.getJSONObject(i);
	 * if(inventoryItemObj.get("inventoryType").equals("Implant")) {
	 * finalImplantArray.put(inventoryMedArray.getJSONObject(i)); } } }
	 * List<OtcCompletionInvImplantsDto> imaplantList =
	 * this.getImplantList(storeId1,UhidNumber, finalImplantArray,proReqId
	 * ,unitId,orgId);
	 * 
	 * resDto.setOtcCompletionInvImplants(imaplantList);
	 *//*********************** End get implant issued list *******************/
	/*
	
	
	*//*********************** Start get timesheet list *******************/
	/*
	 * String mongo=null;
	 * 
	 * JSONObject timeSheet = jsonObj.getJSONObject("timesheet");
	 * 
	 * JSONArray procedureDuration =
	 * timeSheet.getJSONArray("procedureDurationDetails"); if
	 * (procedureDuration.length() > 0) {
	 * 
	 * for (int k = 0; k < procedureDuration.length(); k++) { otimsStatus=true;
	 * JSONObject proDuration = procedureDuration.getJSONObject(k);
	 * if(proDuration!=null) {
	 * //System.out.println("anc"+proDuration.getString("procedureStartTime"));
	 * //System.out.println("anc"+proDuration.getString("procedureEndTime"));
	 * otTimeSheet.setProcedureStartTime(GlobalCommonDateUtils.getStringMongoDate(
	 * proDuration.getString("procedureStartTime")));
	 * otTimeSheet.setProcedureEndTime(GlobalCommonDateUtils.getStringMongoDate(
	 * proDuration.getString("procedureEndTime"))); }
	 * 
	 * 
	 * }
	 * otTimeSheet.setWheelInTime(GlobalCommonDateUtils.getStringMongoDate(timeSheet
	 * .getString("wheelInTime")));
	 * otTimeSheet.setWheelOutTime(GlobalCommonDateUtils.getStringMongoDate(
	 * timeSheet.getString("wheelOutTime")));
	 * otTimeSheet.setTableOnTime(GlobalCommonDateUtils.getStringMongoDate(timeSheet
	 * .getString("tableOnTime")));
	 * otTimeSheet.setTableOutTime(GlobalCommonDateUtils.getStringMongoDate(
	 * timeSheet.getString("tableOffTime")));
	 * otTimeSheet.setAnesthesiaTime(GlobalCommonDateUtils.getStringMongoDate(
	 * timeSheet.getString("anaesthesiaTime")));
	 * otTimeSheet.setScheduleDischarge(GlobalCommonDateUtils.getStringMongoDate(
	 * timeSheet.getString("scheduleDischarge")));
	 * otTimeSheet.setActualDischarge(GlobalCommonDateUtils.getStringMongoDate(
	 * timeSheet.getString("actualDischarge")));
	 * otTimeSheet.setProReqId(Integer.parseInt(timeSheet.getString("proReqId")));
	 * mongo = timeSheet.getString("procId");
	 * otTimeSheet.setMongoId(timeSheet.getString("procId"));
	 * otTimeSheet.setStatus('A'); otTimeSheet.setUnitId(unitId);
	 * otTimeSheet.setOrgId(orgId); } resDto.setOtimsStatus(otimsStatus);
	 * otTimeSheet.setMongoId(mongo); resDto.setTimeSheet(otTimeSheet); }else {
	 * 
	 * 
	 * JSONArray finalArray = new JSONArray(); List<OtcCompletionInvMedicationDto>
	 * medicationList = this.getMedicationList(storeId,UhidNumber,
	 * finalArray,proReqId ,unitId,orgId);
	 * resDto.setOtcCompletionInvMedication(medicationList);
	 * 
	 * List<OtcCompletionInvImplantsDto> imaplantList =
	 * this.getImplantList(storeId,UhidNumber, finalArray,proReqId ,unitId,orgId);
	 * resDto.setOtcCompletionInvImplants(imaplantList);
	 * 
	 * resDto.setTimeSheet(otTimeSheet1); resDto.setOtimsStatus(otimsStatus); }
	 * 
	 * resDto.setOtCompletionStaff(otStaffList);
	 * resDto.setOtServices(otServiceList);
	 *//*********************** End get timesheet list *******************//*
																			 * return new Response<>(SUCCESS,
																			 * SUCCESS_CODE, null, null, resDto);
																			 * }catch(Exception e){ 
																			 * try { throw e; } catch (Exception e1) {
																			 * // TODO Auto-generated catch block
																			 * e1.printStackTrace(); } } return new
																			 * Response<>(ERROR, ERROR_CODE, null, null,
																			 * null); }
																			 */

	// start code by minesh for consignment convert consumption from batch and store
	/*private void callUpdateStoreStockAndBatch(int usedQty, long batchId, int uomTypeId, int storeId) {
		Batch batch = GlobalDao.getBatch(batchId);
		// added by minesh for consignment and consumption logic.
		Integer leaseQuantity = Util.GetLeaseQuantity(usedQty, batch.getBatchUomList(), uomTypeId);
		//List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batchId, storeId);
		List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batchId, storeId, batch.getLotNo(),null);
		Integer totalBatchCurrentQuantity = StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum();
		if (totalBatchCurrentQuantity >= leaseQuantity) {
			for (StoreStock storeStockObj : StoreStockList) {
				if (leaseQuantity != 0) {
					Integer storeAvaliableQuantity = storeStockObj.getAvailableQty();
					Long batchIdFromStore = storeStockObj.getBatch().getId();
					if (leaseQuantity <= storeAvaliableQuantity) {
						updateStoreStockAndBatch(storeStockObj, leaseQuantity, batchIdFromStore, storeId);
						leaseQuantity = 0;
						break;
					} else {
						int avalibleLeaseQuantity = storeStockObj.getAvailableQty();
						updateStoreStockAndBatch(storeStockObj, avalibleLeaseQuantity, batchIdFromStore, storeId);
						leaseQuantity = leaseQuantity - avalibleLeaseQuantity;
					}
				}
			}
		} else {
			throw new ApplicationException("Return quantity must be less than current quantity");
		}
		// code end by minesh for consignment and consumption logic.
	}*/
	
/*private void updateStoreStockAndBatch(StoreStock storeStockObj, int leaseQuantity, Long batchIdFromStore, int storeId) {
		
		Batch batch  = storeStockObj.getBatch();
		
		if(batch.getIsConsignment()) {
			
			//convertedConsumptionBatch Quantity and CurrentQuantity is 0.
			Batch convertedConsumptionBatch = getConsignmentConvertedBatchWithConsumption(batch, leaseQuantity);
			
			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);
			batch.setQuantity(batch.getQuantity()-leaseQuantity);			
			storeStockObj.setAvailableQty(storeStockObj.getAvailableQty()-leaseQuantity);
			//storeStockObj.settQty(storeStockObj.gettQty()+leaseQuantity);
			
			//for storeStockForConvertedConsumptionBatch AvailableQty is 0 and tQty is leaseQuantity.
			StoreStock storeStockForConvertedConsumptionBatch = new StoreStock(0, new Store(storeId),
					new Batch(convertedConsumptionBatch.getId()));
			storeStockForConvertedConsumptionBatch.setIsConsignment(false);
			sessionService.setEntityDefaults(storeStockForConvertedConsumptionBatch);
			storeStockForConvertedConsumptionBatch = GlobalDao.saveStoreStockLeaseQuantity(storeStockForConvertedConsumptionBatch);
			
			saveStockTransaction(leaseQuantity, storeStockForConvertedConsumptionBatch);
		}else {
			
			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);
			//batch.setQuantity(batch.getQuantity()-leaseQuantity);
			
			storeStockObj.setAvailableQty(storeStockObj.getAvailableQty()-leaseQuantity);
			//storeStockObj.settQty(storeStockObj.gettQty()+leaseQuantity);
		}		

		saveStockTransaction(leaseQuantity, storeStockObj);
	}*/
private Batch getConsignmentConvertedBatchWithConsumption(Batch batch, Integer leaseQuantity) {
	Batch newBatch = new Batch();
	newBatch.setOrganization(batch.getOrganization());
	newBatch.setUnit(batch.getUnit());
	newBatch.setUomUnit(batch.getUomUnit());
	newBatch.setItem(batch.getItem());
	newBatch.setBatchNo(batch.getBatchNo());
	newBatch.setLotNo(batch.getLotNo());
	newBatch.setQuantity(leaseQuantity);//leaseQuantity
	newBatch.setCurrentQuantity(0);//leaseQuantity
	newBatch.setExpiryDate(batch.getExpiryDate());
	newBatch.setCop(batch.getCop());
	newBatch.setRate(batch.getRate());
	newBatch.setSellingPrice(batch.getSellingPrice());
	newBatch.setMarkup(batch.getMarkup());
	newBatch.setLendingPrice(batch.getLendingPrice());
	newBatch.setActualLendingPrice(batch.getActualLendingPrice());
	newBatch.setTax(batch.getTax());
	newBatch.setTaxPercentage(batch.getTaxPercentage());
	newBatch.setTaxAmount(batch.getTaxAmount());
	newBatch.setBarcode(batch.getBarcode());
	newBatch.setIsOpb(batch.getIsOpb());
	newBatch.setIsActive(batch.getIsActive());
	newBatch.setAddedDate(TimeZoneComponent.getDateByzone(batch.getUnitId()) );
	newBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(batch.getUnitId()));			
	newBatch.setAddedByUser(sessionService.getUserId());
	newBatch.setUpdatedByUser(sessionService.getUserId());			
	newBatch.setIsDeleted(batch.getIsDeleted());
	newBatch.setBarcodePrintCount(batch.getBarcodePrintCount());
	newBatch.setIsQuarantine(batch.getIsQuarantine());
	newBatch.setVendor(batch.getVendor());
	newBatch.setServiceProvider(batch.getServiceProvider());
	newBatch.setWarrantyFrom(batch.getWarrantyFrom());
	newBatch.setWarrantyTo(batch.getWarrantyTo());
	newBatch.setPmsFrequency(batch.getPmsFrequency());
	newBatch.setYearOfManufacture(batch.getYearOfManufacture());
	newBatch.setWorkingCondition(batch.getWorkingCondition());
	newBatch.setModelNumber(batch.getModelNumber());
	newBatch.setIsPharmacy(batch.getIsPharmacy());
	newBatch.setIsIndent(batch.getIsIndent());
	newBatch.setIsReturn(false);
	newBatch.setRejectedQuantity(0);
	newBatch.setReturnedQuantity(0);
	newBatch.setIsConsignment(false);

	for (BatchUomMapper batchUomMapperObj : batch.getBatchUomList()) {
		BatchUomMapper batchUomMapper = new BatchUomMapper(batchUomMapperObj.getConversions(),
				batchUomMapperObj.getIpUom(), batchUomMapperObj.getOpUom(), batchUomMapperObj.getStoreUom(),
				new UomType(batchUomMapperObj.getUomType().getId()),
				new UomUnit(batchUomMapperObj.getUomUnit().getId()));
		newBatch.addBatchUom(batchUomMapper);
	}
newBatch = GlobalDao.saveBatch(newBatch);
return newBatch;
}

	@Override
	public Response getMedicationsAndImplantsByPeoReqId(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
	try {
			
			List<ProcedureOrderMasterDto> list = new ArrayList<>();
			Response res =  iProcedureOrderDao.getProcedureOrderListByAdmissionId(0,0, preOperativeDetailsDto.getProReqId());
			if(res.getListObject().size()>0 && res.getListObject()!=null) {
				list.addAll(res.getListObject());
				
				for (ProcedureOrderMasterDto reqDto : list) {
					
					if(reqDto.getProReqId()!=0) {
						
						MedicationDetailsDto medicationDetailsDto = new MedicationDetailsDto();
						medicationDetailsDto.setOrganizationId(reqDto.getOrgId());
						medicationDetailsDto.setUnitId(reqDto.getUnitId());
						medicationDetailsDto.setProcedureOrderId(reqDto.getProcedureOrderId());
						
						List<MedicationDetailsDto> medicationList = new ArrayList<>();
						Response medicationRes = iMedicationDetailsDao.getMedicationDetailsByProcedureIdOnOTIMS(medicationDetailsDto);
						medicationList.addAll(medicationRes.getListObject()) ;
						reqDto.setMedicationDetailsList(medicationList);
						
						ImplantDetailsDto implantDetailsDto = new ImplantDetailsDto();
						implantDetailsDto.setOrganizationId(reqDto.getOrgId());
						implantDetailsDto.setUnitId(reqDto.getUnitId());
						implantDetailsDto.setProcedureOrderId(reqDto.getProcedureOrderId());
						
						List<ImplantDetailsDto> implantList = new ArrayList<>();
						Response implantRes = iImplantDetailsDao.getImplantDetailsByProcedureIdOnOTIMS(implantDetailsDto);
						implantList.addAll(implantRes.getListObject());
						reqDto.setImplantDetailsList(implantList);
					}
					
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchProcedureByNameOrCode(ProcedureOrderMasterDto procedureOrderDto) throws ApplicationException {
		try {
			return iProcedureOrderDao.searchProcedureByNameOrCode(procedureOrderDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

/*private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock) {
	com.param.entity.model.master.Screen screen = GlobalDao.getScreen(Screen.OTCompletion.getScreen().intValue());
	StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
			new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
			storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()),
			new Batch(storeStock.getBatch().getId()));
	sessionService.setEntityDefaults(stockTransaction);
	GlobalDao.saveStockTransaction(stockTransaction);
}*/

//end code by minesh for consignment convert consumption from batch and store


	@Override
	public Response getDataByDateFromProcedureOrder(ProcedureOrderMasterDto dto) throws ApplicationException {
		try{
			List<ProcedureOrderMasterDto> resDtoList = new ArrayList<>();

			for(String date : dto.getDates()){
				List<ProcedureOrderMasterDto> orderProcedureList = new ArrayList<>();
				ProcedureOrderMasterDto resDto = new ProcedureOrderMasterDto();

				resDto.setAddedDate(date);
				if(dto.getOrderedProcedureList() != null && dto.getOrderedProcedureList().size()>0){
					for (ProcedureOrderMasterDto investigation : dto.getOrderedProcedureList()) {

						if (date.equals(investigation.getCreatedDate1())) {
							investigation.setAddedDate(date);
							orderProcedureList.add(investigation);
						}
					}
					resDto.setOrderedProcedureList(orderProcedureList);
				}
				resDtoList.add(resDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){

		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateFromProcedureOrder(ProcedureOrderMasterDto dto) throws ApplicationException {
		try{
			List<String>distinctDates = new ArrayList<>();
			ProcedureOrderMasterDto resDto = dto;

			if(dto.getOrderedProcedureList() != null && dto.getOrderedProcedureList().size()>0){
				for (ProcedureOrderMasterDto complaint : dto.getOrderedProcedureList()) {

					if (!distinctDates.contains(complaint.getCreatedDate1())) {
						distinctDates.add(complaint.getCreatedDate1());
					}
				}

				if(distinctDates != null && distinctDates.size()>0){
					Collections.sort(distinctDates, Comparator.reverseOrder());
					resDto.setDates(distinctDates);
					return getDataByDateFromProcedureOrder(resDto);
				}

			}
			else{
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
			}
		}
		catch (Exception e){
			System.out.println(e);
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}