package com.param.global.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.model.master.Tax;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.INewEMRTemplateDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.InvestigationOrderMasterNewDto;
import com.param.global.dto.NewEMRTemplateDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.dao.IComplaintAppointmentMapperDao;
import com.param.opd.coversheet.dao.ICoversheetTemaplateDao;
import com.param.opd.coversheet.dao.IMedicationDAO;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@Transactional
@SuppressWarnings({"rawtypes","unchecked"})
public class NewEMRTemplateServiceImpl implements INewEMRTemplateService, ICommonConstants, IBillingCommonConstants {

	private static final Logger logger = Logger.getLogger(NewEMRTemplateServiceImpl.class);

	@Autowired
	INewEMRTemplateDao iNewEMRTemplateDao;

	@Autowired
	private ICoversheetTemaplateDao iCoversheetTemaplateDao;
	
	@Autowired
	private IMedicationDAO iMedicationDAO;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	private IComplaintAppointmentMapperDao iComplaintAppointmentMapperDao;
	
	@Autowired
	private IOrderMasterService iOrderMasterService;
	
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IOrderMasterDao iOrderMasterDao;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Override
	public Response saveNewEMRTemplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto) {try {
		Response res=null;
		if(coversheetTemplateMasterDto.getCovTemplateId() != null) {
			Response resList= iCoversheetTemaplateDao.checkAvailableCoversheetAsTemaplate(coversheetTemplateMasterDto);
			if (resList.getListObject() != null && resList.getListObject().size() > 0) {

				return new Response<>(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}
		}			
		 else {
			 
			 res = iNewEMRTemplateDao.saveNewEMRTemplate(coversheetTemplateMasterDto);
			 if(res.getObject()!=null) {
					CoversheetTemplateMasterDto resDto=(CoversheetTemplateMasterDto) res.getObject();
					
					//1 For complaints
					ComplaintAppointmentMapperDto complaintAppointmentMapperDto=new ComplaintAppointmentMapperDto();
					complaintAppointmentMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					complaintAppointmentMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
					complaintAppointmentMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
					Response complaintListRes = iComplaintAppointmentMapperDao.getListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
					if(complaintListRes.getListObject() != null && complaintListRes.getListObject().size() > 0){
						List<ComplaintAppointmentMapperDto> list=complaintListRes.getListObject();
						iComplaintAppointmentMapperDao.savefComplaintAppointmentMapper(list, resDto);
					}
					
//					for Investigation
					Response investListRes = iGlobalUnitMasterDao.getInvestigationsListForCoversheet(coversheetTemplateMasterDto.getPatientId(),false,coversheetTemplateMasterDto.getUnitId());
					if(investListRes.getListObject() != null && investListRes.getListObject().size() > 0){
						List<InvestigationOrderMasterNewDto> list=investListRes.getListObject();

						
						Integer selfPayeeId =null ;
						Integer defaultTariffId = null;
						Date date  = TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId());
						Response orderRes = null;
						Character isPreBilling = null;
						Integer defaultDiscountOwnership = 0;
						UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
							unitApplicationConfigurationDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId() );
							unitApplicationConfigurationDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
						Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
						
						if(appConfigRes.getObject()!=null)
						{
							UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
							
							if(unitApplicationConfigurationDtos.getDefaultSelfTariffId()==null)
								return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
							else
								defaultTariffId= unitApplicationConfigurationDtos.getDefaultSelfTariffId();
								
							if(unitApplicationConfigurationDtos.getSelfPayeeId()==null)
								return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee Id is not defined !!", null, null);
							else
								selfPayeeId=unitApplicationConfigurationDtos.getSelfPayeeId();
							
							if(unitApplicationConfigurationDtos.getIsPreOrPostBilling()==null)
								return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null, null);
							else	
								isPreBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();
								
							defaultDiscountOwnership= unitApplicationConfigurationDtos.getDefaultDiscountOwnership();
						}else{
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Unit Application Configuration is yet to set !!", null, null);
						}
				     
				   
						ListIterator<OrderMasterDto> iteratorOrderMaster = (ListIterator<OrderMasterDto>) list;
						
						while(iteratorOrderMaster.hasNext())	
						{
							List orderDetailsList = new ArrayList();
							OrderMasterDto orderMasterDto2 = iteratorOrderMaster.next();
							orderMasterDto2 = iGlobalUnitMasterDao.getPatientClassByVisitForNewCoversheet(orderMasterDto2);
							orderMasterDto2.setDefaultDiscountOwnership(defaultDiscountOwnership);
							if(orderMasterDto2.getListOrderDetailsMasterDto().size()>0)
							{
								if(orderMasterDto2.getEncounterId()==null && orderMasterDto2.getAdmissionId()==null)
								{    
									ListIterator<OrderDetailsMasterDto> iterator = orderMasterDto2.getListOrderDetailsMasterDto().listIterator();
									while(iterator.hasNext())
									{
										OrderMasterDto orderMasterDtoFutureOrder =  orderMasterDto2;
										OrderDetailsMasterDto detailsMasterDto = iterator.next();
										List orderDetailsListFuture = new LinkedList();
										OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
												orderDetailsMasterDto.setServiceId(detailsMasterDto.getServiceId());
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
												orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
												orderDetailsMasterDto.setOrderDate(orderMasterDto2.getCreatedDate());
												orderDetailsMasterDto.setOrderSchDate(orderMasterDto2.getCreatedDate());
												orderDetailsMasterDto.setCreatedDate(orderMasterDto2.getCreatedDate());
												orderDetailsMasterDto.setUpdatedDate(orderMasterDto2.getUpdatedDate());
												orderDetailsMasterDto.setStatus('A');
												orderDetailsMasterDto.setOrgId(orderMasterDto2.getOrgId());
												orderDetailsMasterDto.setOrgUnitId(orderMasterDto2.getOrgUnitId());
												orderDetailsMasterDto.setOrderQty(1);
												orderDetailsMasterDto.setOrdDocId(detailsMasterDto.getOrdDocId());
												orderDetailsMasterDto.setOrdDocSplId(detailsMasterDto.getOrdDocSplId());
												orderDetailsMasterDto.setServiceRenderingDeptId(detailsMasterDto.getServiceRenderingDeptId());
												
//												orderDetailsMasterDto.setPriorityId(2);
												orderDetailsMasterDto.setPriorityId(detailsMasterDto.getServicePriorityId());
												
												orderDetailsMasterDto.setIsOutsourced('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setOrdCancelled('N');
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setIsInvestigationOrder('Y');
												orderDetailsMasterDto.setIsRefDoctorShare(detailsMasterDto.getIsRefDoctorShare());
												orderDetailsMasterDto.setDepartmentId(detailsMasterDto.getDepartmentId());
												orderDetailsMasterDto.setSubDepartmentId(detailsMasterDto.getSubDepartmentId());
												orderDetailsMasterDto.setOrderSetId(detailsMasterDto.getOrderSetId());
												orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
												orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
												orderDetailsMasterDto.setProcedureOrderMstId((detailsMasterDto.getProcedureOrderMstId()!=null && detailsMasterDto.getProcedureOrderMstId()!=0)?detailsMasterDto.getProcedureOrderMstId():null);
												orderDetailsMasterDto.setBedId(detailsMasterDto.getBedId());
												orderDetailsMasterDto.setWardId(detailsMasterDto.getWardId());
												orderDetailsMasterDto.setRoomId(detailsMasterDto.getRoomId());
												orderDetailsMasterDto.setOrdRemarks(detailsMasterDto.getOrdRemarks());
												if(orderDetailsMasterDto.getOrderSchDate()!=null) {
													orderDetailsMasterDto.setOrderSchDate(detailsMasterDto.getOrderSchDate());
												}
												else {
													orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
												}
									
										orderDetailsListFuture.add(orderDetailsMasterDto);
										orderMasterDtoFutureOrder.setListOrderDetailsMasterDto(orderDetailsListFuture);
										orderRes = iOrderMasterService.saveOrderMaster(orderMasterDtoFutureOrder);
									}
									
								}
								else
								{
									ListIterator<OrderDetailsMasterDto> iterator = orderMasterDto2.getListOrderDetailsMasterDto().listIterator();
									while(iterator.hasNext())
									{
										OrderDetailsMasterDto masterDto = iterator.next();
										
											OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
												orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setOrderQty(1);
												orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
												orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
												orderDetailsMasterDto.setOrderDate(orderMasterDto2.getCreatedDate());
												orderDetailsMasterDto.setOrderSchDate(orderMasterDto2.getCreatedDate());
												orderDetailsMasterDto.setCreatedDate(orderMasterDto2.getCreatedDate());
												orderDetailsMasterDto.setUpdatedDate(orderMasterDto2.getUpdatedDate());
												orderDetailsMasterDto.setStatus('A');
												orderDetailsMasterDto.setOrgId(orderMasterDto2.getOrgId());
												orderDetailsMasterDto.setOrgUnitId(orderMasterDto2.getOrgUnitId());
												orderDetailsMasterDto.setOrdDocId(masterDto.getOrdDocId());
												orderDetailsMasterDto.setOrdDocSplId(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId()==1 && masterDto.getOrdDocSplId()!=null && masterDto.getOrdDocSplId() > 1 ? masterDto.getOrdDocSplId() : masterDto.getOrdDocSplId());
												orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getServiceRenderingDeptId());
												orderDetailsMasterDto.setOrdCancelled('N');
												
//												orderDetailsMasterDto.setPriorityId(2);
												orderDetailsMasterDto.setPriorityId(masterDto.getServicePriorityId());

												orderDetailsMasterDto.setIsOutsourced('N');
												orderDetailsMasterDto.setIsInvestigationOrder('Y');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setIsRefDoctorShare(masterDto.getIsRefDoctorShare());
												orderDetailsMasterDto.setDepartmentId(masterDto.getDepartmentId());
												orderDetailsMasterDto.setSubDepartmentId(masterDto.getSubDepartmentId());
												orderDetailsMasterDto.setOrderSetId(masterDto.getOrderSetId());
												orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
												orderDetailsMasterDto.setNursingServiceOrderFlag(masterDto.getNursingServiceOrderFlag());
												orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
												orderDetailsMasterDto.setProcedureOrderMstId((masterDto.getProcedureOrderMstId()!=null && masterDto.getProcedureOrderMstId()!=0)?masterDto.getProcedureOrderMstId():null);
												orderDetailsMasterDto.setBedId(masterDto.getBedId());
												orderDetailsMasterDto.setWardId(masterDto.getWardId());
												orderDetailsMasterDto.setRoomId(masterDto.getRoomId());
												orderDetailsMasterDto.setOrdRemarks(masterDto.getOrdRemarks());
											if(masterDto.isPackage())
											{
												    orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
												    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
												    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
													Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(masterDto.getServiceId(),orderMasterDto2.getOrgUnitId());
													BigDecimal serviceAmt = BigDecimal.ZERO;
													BigDecimal netAmt= BigDecimal.ZERO;
													BigDecimal taxAmt= BigDecimal.ZERO;
													if(tax != null)
													{
														orderDetailsMasterDto.setTaxId(tax.getId());
														orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
														serviceAmt = masterDto.getServiceAmt()!=null?masterDto.getServiceAmt():new BigDecimal(0);
														taxAmt = serviceAmt.multiply((orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01))));
														netAmt = serviceAmt.add(taxAmt);
													}else
													{
														orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
														serviceAmt = masterDto.getServiceAmt();
														netAmt = serviceAmt;
													}
													orderDetailsMasterDto.setOrdTotalAmt(serviceAmt);
													orderDetailsMasterDto.setTaxAmount(taxAmt);
													orderDetailsMasterDto.setNetAmount(netAmt);
													orderDetailsMasterDto.setOrdConcession(masterDto.getOrdConcession());
													orderDetailsMasterDto.setOrdDiscount(masterDto.getOrdDiscount());
													orderDetailsMasterDto.setSelfPayable(masterDto.getSelfPayable());
													orderDetailsMasterDto.setCreditPayable(masterDto.getCreditPayable());
													orderDetailsMasterDto.setTariffId(defaultTariffId);
													orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
													orderDetailsMasterDto.setOrderEmergencyFlag('N');
													orderDetailsMasterDto.setServiceRendered(masterDto.getServiceRendered());
													orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
													orderDetailsMasterDto.setServiceChargeable(1);
													orderDetailsMasterDto.setServiceIsBilled(0);
													orderDetailsMasterDto.setPayeeId(selfPayeeId);
													orderDetailsMasterDto.setIsPackage(true);
													orderDetailsMasterDto.setPackage(true);
													orderDetailsMasterDto.setApportionedPrice(masterDto.getApportionedPrice());
													orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
													orderDetailsMasterDto.setPackageTypeId(masterDto.getPackageTypeId());
													orderDetailsMasterDto.setIsPackageService(masterDto.getIsPackageService());
													//orderDetailsMasterDto.setPackageEitherOrGroupId(dto.get);
													orderDetailsMasterDto.setIsCapService(masterDto.getIsCapService());
													orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
													orderDetailsMasterDto.setOrdCancelled('N');
													orderDetailsMasterDto.setIsDrug('N');
													orderDetailsMasterDto.setServiceRendered(0);
													orderDetailsMasterDto.setStatus('A');
													orderDetailsMasterDto.setIsInvestigationOrder('N');
													orderDetailsMasterDto.setCoPayPer(masterDto.getCoPayPer());
													orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(masterDto.getOrderDetailsPayeeMapperDtosList());
													orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(masterDto.getOrderDetailsPackageMapperDtosList());
											}
											else if(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId().intValue()==1 && !masterDto.isPackage())
											{
												orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
											    orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
											    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
												orderDetailsMasterDto.setTariffId(defaultTariffId);
												orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
												orderDetailsMasterDto.setOrderEmergencyFlag('N');
												orderDetailsMasterDto.setServiceRendered(masterDto.getServiceRendered());
												orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setPayeeId(selfPayeeId);
												orderDetailsMasterDto.setIsPackage(false);
												orderDetailsMasterDto.setPackage(false);
												orderDetailsMasterDto.setApportionedPrice(masterDto.getApportionedPrice());
												orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
												orderDetailsMasterDto.setPackageTypeId(masterDto.getPackageTypeId());
												orderDetailsMasterDto.setIsPackageService(masterDto.getIsPackageService());
												//orderDetailsMasterDto.setPackageEitherOrGroupId(dto.get);
												orderDetailsMasterDto.setIsCapService(masterDto.getIsCapService());
												orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
												orderDetailsMasterDto.setOrdCancelled('N');
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setStatus('A');
												orderDetailsMasterDto.setIsInvestigationOrder('N');
												orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
												List<OrderDetailsPayeeMapperDto> list1 = new LinkedList<>();
												OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
													detailsPayeeMapperDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
													detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
													detailsPayeeMapperDto.setServiceId(orderDetailsMasterDto.getServiceId());
												list1.add(detailsPayeeMapperDto);	
												orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list1);
												orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(masterDto.getOrderDetailsPackageMapperDtosList());
												
											  }
											else{
												
												  ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
													consumptionDto.setOrganizationId(orderMasterDto2.getOrgId());
													consumptionDto.setUnitId(orderMasterDto2.getOrgUnitId());
													consumptionDto.setVisitTypeId(orderMasterDto2.getVisitTypeId());
													consumptionDto.setPatientTypeId(orderMasterDto2.getPatientTypeId());
													consumptionDto.setBillingBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
													consumptionDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
													consumptionDto.setServiceId(masterDto.getServiceId());
													consumptionDto.setCurrentDate(orderMasterDto2.getCurrentDate());
													consumptionDto.setContractId(orderMasterDto2.getContractId());
													consumptionDto.setEncounterId(orderMasterDto2.getEncounterId());
													consumptionDto.setAdmissionId(orderMasterDto2.getAdmissionId());
													consumptionDto.setDefaultSelfTariffId(defaultTariffId);
													consumptionDto.setSelfPayeeId(selfPayeeId);
													consumptionDto.setPatientId(orderMasterDto2.getPatientId());
													consumptionDto.setPatientClassId(orderMasterDto2.getPatientClassId());
												Response res1 =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
													consumptionDto=(ContractConsumptionDto)res1.getObject();
												
												if(res1.getObject()!=null)
												{
													orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
													orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
													orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
													orderDetailsMasterDto.setTariffId(defaultTariffId);
													orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
													orderDetailsMasterDto.setOrderEmergencyFlag('N');
													orderDetailsMasterDto.setServiceRendered(0);
													orderDetailsMasterDto.setServiceChargeable(1);
													orderDetailsMasterDto.setServiceIsBilled(0);
													orderDetailsMasterDto.setIsDrug('N');
													orderDetailsMasterDto.setPackage(masterDto.isPackage());
													orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
													orderDetailsMasterDto.setIsInvestigationOrder('Y');
													orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
													orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
													orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
													orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
													orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf(consumptionDto.getServiceRate()));
													orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
													orderDetailsMasterDto.setNetAmount(BigDecimal.valueOf(consumptionDto.getNetAmount()));
													orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
													orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
													orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
													orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
													orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
													orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
													orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
													orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
													orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
													if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
														orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
														
												}
											 }
										
											if(orderDetailsMasterDto.getOrderDetailsId()!=null && orderDetailsMasterDto.getOrderDetailsId()>0) {								
												orderDetailsMasterDto.setIsInvestigationOrder('Y');
												if(masterDto.getOrderId()==null)
													iOrderMasterDao.updateOrderDetailsByOdrDtlsId(orderDetailsMasterDto.getOrderDetailsId());
												else {
													 orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
													iOrderDetailsMasterDao.updateOrderDetails(orderDetailsMasterDto,'N',selfPayeeId);
												}							
												
											}
												
											else
												 orderDetailsList.add(orderDetailsMasterDto);
											if(orderMasterDto2.getOrdDocSplId() == null) {
											   orderMasterDto2.setOrdDocSplId(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId()==1 && masterDto.getOrdDocSplId()!=null && masterDto.getOrdDocSplId() > 1 ? masterDto.getOrdDocSplId() : masterDto.getOrdDocSplId());
											}
											   orderMasterDto2.setListOrderDetailsMasterDto(orderDetailsList);
									}
			
									if(orderMasterDto2.getSlotMasterDtoList()!=null && orderMasterDto2.getSlotMasterDtoList().size()>0){
										orderMasterDto2.setSlotMasterDtoList(orderMasterDto2.getSlotMasterDtoList());
									}
									if(orderMasterDto2.getListOrderDetailsMasterDto()!=null && orderMasterDto2.getListOrderDetailsMasterDto().size()>0)
										 orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto2);
								}
							}
						}
//					return new Response(SUCCESS, null, "Order placed !!", null, selfPayeeId,orderRes,orderRes.getListObject2());
				
					}
					
					//11 for prescription 
					PrescriptionDto prescriptionDtoSave=new PrescriptionDto();
					prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
					prescriptionDtoSave.setPatientId(coversheetTemplateMasterDto.getPatientId());
					prescriptionDtoSave.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
					Response detailedMedications = iMedicationDAO.getMedicationDetailsByDoctorId(prescriptionDtoSave);
					if(detailedMedications.getListObject() != null && detailedMedications.getListObject().size() > 0){
						List<MedicationDTO> list=detailedMedications.getListObject();
						for (MedicationDTO medicationDTO : list) {
						 
							prescriptionDtoSave.setIsIntervention(FALSE);
							prescriptionDtoSave.setIsActive(TRUE);
							prescriptionDtoSave.setIsDeleted(FALSE);
							prescriptionDtoSave.setIsValidate(FALSE);
							prescriptionDtoSave.setIsDischarge(FALSE);
							prescriptionDtoSave.setReconciliationDone(FALSE);
							prescriptionDtoSave.setIsNonFormulary('N');
							prescriptionDtoSave.setJustificationStop(medicationDTO.getJustificationStop());
							prescriptionDtoSave.setIsMedicationStop('N');
							prescriptionDtoSave.setVisitTypeId(medicationDTO.getVisitTypeId());
							prescriptionDtoSave.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDtoSave.setAuthorisedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDtoSave.setAuthorisedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDtoSave.setPriorityId(2);
							prescriptionDtoSave.setStatusId(1);
							prescriptionDtoSave.setPrescriptionDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDtoSave.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							prescriptionDtoSave.setMedicationType(1);
							prescriptionDtoSave.setAdmissionId(null);
							prescriptionDtoSave.setEncounterId(null);
							prescriptionDtoSave.setPatientId(null);
//							prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
							prescriptionDtoSave.setIsMedicationStop('N');	
//							prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
							String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, prescriptionDtoSave.getUnitId(), PRES_NO);
							prescriptionDtoSave.setIsNonFormulary('N');
							
							prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
							Response presRes = iMedicationDAO.savePrescription(prescriptionDtoSave);
							prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
						}
						List<PrescriptionDetailsDto> listDetails=new ArrayList<>();
						for (MedicationDTO medicationDTO : list) {
							PrescriptionDetailsDto details=new PrescriptionDetailsDto();
							details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							details.setUnitId(coversheetTemplateMasterDto.getUnitId());
							details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
							
							
							details.setIsDiscontinued(FALSE);
							details.setIsMedicationStop('N');
							details.setIsPreStop(FALSE);
							details.setIsIntervention(FALSE);
							details.setDuration(medicationDTO.getDuration()!=null ? medicationDTO.getDuration() :null);
							details.setDurationUnit(medicationDTO.getDurationUnit()!=null ? medicationDTO.getDurationUnit() :null);
							details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							details.setOrderQty(medicationDTO.getOrderQty());
							details.setPriorityId(medicationDTO.getPriorityId());
							details.setIsDeleted(FALSE);
							details.setIsActive(TRUE);
							details.setStatusId(1);
							details.setPrnflag(FALSE);
							details.setItemId(medicationDTO.getItemId()!=null ? medicationDTO.getItemId():null);
//							details.setPrescribedDose(medicationDTO.getPrescribedDose() !=null ? medicationDTO.getPrescribedDose(): null);
							details.setStatflag(medicationDTO.getStatflag());
							details.setGenericName(medicationDTO.getGenericName());
							details.setItemName(medicationDTO.getItemName());
							details.setIsNonFormulary(medicationDTO.getIsNonFormulary());
							details.setInfusionflag(FALSE);
							details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
							details.setInstructionDesc(medicationDTO.getInstructionDesc());
							details.setUpdatedDate(medicationDTO.getAddedDate());
							if(details.getIsDischarge()!=null){
								details.setIsDischarge(medicationDTO.getIsDischarge());
							}else{
								details.setIsDischarge(false);
							}
							listDetails.add(details);
						}
						
						prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
						ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
						while(iterator.hasNext())
						{
							PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
								prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
								prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
								prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
								prescriptionDetailsDto.setIsDiscontinued(FALSE);
								prescriptionDetailsDto.setIsMedicationStop('N');
								prescriptionDetailsDto.setIsPreStop(FALSE);
								prescriptionDetailsDto.setIsIntervention(FALSE);
								prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
								prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
								prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
								prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
								prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
								prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
								prescriptionDetailsDto.setIsDeleted(FALSE);
								prescriptionDetailsDto.setIsActive(TRUE);
								prescriptionDetailsDto.setStatusId(1);
								prescriptionDetailsDto.setPrnflag(FALSE);
								prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
								prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
								prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
								prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
								prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
								prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
								prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
								prescriptionDetailsDto.setInfusionflag(FALSE);
								prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
								prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
								prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
								if(prescriptionDetailsDto.getIsDischarge()!=null){
									prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
								}else{
									prescriptionDetailsDto.setIsDischarge(false);
								}
							Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);	
								
							prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();
									
							iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
						}
					
//					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDtoSave);
					}
					
					
					
					
				}
		}
		
		
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
	} catch (Exception e) {
		logger.error("saveNewEMRTemplate failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
}
