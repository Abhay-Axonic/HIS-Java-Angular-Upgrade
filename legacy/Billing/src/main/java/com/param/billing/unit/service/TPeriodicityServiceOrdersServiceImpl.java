package com.param.billing.unit.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.billing.ipd.service.IpdChargesService;
import com.param.billing.unit.dao.ITPeriodicityServiceOrdersDao;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.entity.model.master.Tax;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.ITPackageConsumptionCapDetailsDao;
import com.param.global.dao.ITPackageConsumptionServiceDetailDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PackageServiceCheckReqDto;
import com.param.global.dto.TPackageConsumptionServiceDetailDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IUnitServiceTariffMasterService;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings("rawtypes")
public class TPeriodicityServiceOrdersServiceImpl implements ITPeriodicityServiceOrdersService, ICommonConstants{

	@Autowired
	private ITPeriodicityServiceOrdersDao iTPeriodicityServiceOrdersDao;
	@Autowired
	IpdChargesService ipdChargesService;
	@Autowired
	private IUnitServiceTariffMasterService iUnitServiceTariffMasterDao;
	/*@Autowired
	private IMRoomRentChargesConfigDao iMRoomRentChargesConfigDao;*/
	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	@Autowired
	private IOrderMasterService iOrderMasterService;
	@Autowired
	private ITPackageConsumptionCapDetailsDao iTPackageConsumptionCapDetailsDao;
	@Autowired
	private ITPackageConsumptionServiceDetailDao iTPackageConsumptionServiceDetailDao;
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	@Autowired
	private IOrderDetailsMasterDao iOrderDetailsMasterDao;
	@Autowired
	IBillingMasterDao billingMasterDao;
	
	//private static final Logger log = LoggerFactory.getLogger(RoomRentBatchChargesServiceImpl.class);
	private static final Logger log = LoggerFactory.getLogger(TPeriodicityServiceOrdersServiceImpl.class);
	
	@Override
	@Transactional
	public Response orderTPeriodicityServices(List<TPeriodicityServiceOrdersDto> listPserv) throws BillingException {
		try{
			ListIterator<TPeriodicityServiceOrdersDto> pservItr = listPserv.listIterator();
			String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(listPserv.getFirst().getUnitId()), "dd-MM-yyyy HH:mm:ss");
			TPeriodicityServiceOrdersDto dto = null;
			while(pservItr.hasNext())
			{
				dto = pservItr.next();
				
				dto = placeOrder(dto);
				
				if(dto.getPeriodicityServiceOrderId() != null){
					//update by default calls saveOrUpdate();
					iTPeriodicityServiceOrdersDao.saveTPeriodicityServiceOrders(dto);
				}else{
					//insert new 
					dto.setStatus(ACTIVE);
					dto.setCreatedDate(dateTime);
					iTPeriodicityServiceOrdersDao.saveTPeriodicityServiceOrders(dto);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, PERIODICITY_SERV_ORDERED, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	@Override
	public TPeriodicityServiceOrdersDto placeOrder(TPeriodicityServiceOrdersDto dto) 
	{
		try{
			BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;
			double noOfUnits = 0.0;
			ContractConsumptionDto consumptionDto = null;
			if(dto.getOrderDetailId()==null)
			{
				//calculate and post rates
				OrderMasterDto orderMasterDto = new OrderMasterDto();
					orderMasterDto.setVisitTypeId(dto.getVisitTypeId());// op==1
					orderMasterDto.setAdmissionId(dto.getChargesResDto().getAdmissionId());
					orderMasterDto.setPatientId(dto.getChargesResDto().getPatientId());
					orderMasterDto.setCreatedBy(dto.getCreatedBy());
					orderMasterDto.setUpdatedBy(dto.getUpdatedBy());
					orderMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderMasterDto.setOrderStatus('A');
					orderMasterDto.setOrgId(dto.getOrganisationId());
					orderMasterDto.setOrgUnitId(dto.getUnitId());
					orderMasterDto.setOrderStatusId(1);// order status==1
					
				//-------order details-------//
				OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
					orderDetailsMasterDto.setServiceId(dto.getServiceId());
					orderDetailsMasterDto.setServiceIsBilled(0);
					orderDetailsMasterDto.setOrderQty(0);
					orderDetailsMasterDto.setCreatedBy(dto.getCreatedBy());
					orderDetailsMasterDto.setUpdatedBy(dto.getCreatedBy());
					orderDetailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime());
					orderDetailsMasterDto.setStatus('A');
					orderDetailsMasterDto.setOrgId(dto.getOrganisationId());
					orderDetailsMasterDto.setOrgUnitId(dto.getUnitId());
					orderDetailsMasterDto.setOrdDocId(dto.getChargesResDto().getDoctorId());
					orderDetailsMasterDto.setOrdDocSplId(dto.getChargesResDto().getDepartmentId());
					orderDetailsMasterDto.setServiceRenderingDeptId(dto.getSpecialityId());
					orderDetailsMasterDto.setOrdCancelled('N');
					orderDetailsMasterDto.setIsDrug('N');
					orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
					orderDetailsMasterDto.setServiceRendered(0);
					orderDetailsMasterDto.setServiceAmt(BigDecimal.ZERO);
					orderDetailsMasterDto.setOrdConcession(BigDecimal.ZERO);
					orderDetailsMasterDto.setOrdDiscount(BigDecimal.ZERO);
					orderDetailsMasterDto.setOrderEmergencyFlag('N');
					orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.ZERO);
					orderDetailsMasterDto.setTaxAmount(BigDecimal.ZERO);
					orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
					orderDetailsMasterDto.setSelfPayable(BigDecimal.ZERO);
					orderDetailsMasterDto.setCreditPayable(BigDecimal.ZERO);
					orderDetailsMasterDto.setWardId(dto.getWardId());
					orderDetailsMasterDto.setRoomId(dto.getRoomId());
					orderDetailsMasterDto.setBedId(dto.getBedId());
					if(dto.getToDate()!=null && dto.getToTime()!=null)
					{
						 Double rate = null;
						 Double serviceRate = null;
						 ContractConsumptionDto consumptionDtoFirst = new ContractConsumptionDto();
							 consumptionDtoFirst.setOrganizationId(dto.getOrganisationId());
							 consumptionDtoFirst.setUnitId(dto.getUnitId());
							 consumptionDtoFirst.setVisitTypeId(dto.getVisitTypeId());
							 consumptionDtoFirst.setPatientTypeId(dto.getChargesResDto().getPatientCategoryId());
							 consumptionDtoFirst.setBillingBedCategoryId(dto.getChargesResDto().getBedBillingCategoryId());
							 consumptionDtoFirst.setPaymentEntitlementId(SELF);
							 consumptionDtoFirst.setServiceId(dto.getServiceId());
							 consumptionDtoFirst.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
							 consumptionDtoFirst.setAdmissionId(dto.getChargesResDto().getAdmissionId());
							 consumptionDtoFirst.setDefaultSelfTariffId(dto.getDefaultSelfTariffId());
							 consumptionDtoFirst.setSelfPayeeId(dto.getSelfPayeeId());
						 
						Response selfRes =iContractConsumptionService.consumeServiceFromContract(consumptionDtoFirst);
						consumptionDtoFirst=(ContractConsumptionDto)selfRes.getObject();

						String fromDateStr = dto.getFromDate().substring(0,10); // dd-MM-yyyy
						String fromTimeIso = String.valueOf(dto.getFromTime()); // ISO format
						String fromTimeFormatted = fromTimeIso.substring(11, 16); // "05:00"
						String fullDateTime = fromDateStr + " " + fromTimeFormatted;
						SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						Date fromDate = format.parse(fullDateTime);


						String toDateStr = dto.getToDate().substring(0,10); // dd-MM-yyyy
						String ToTimeIso = String.valueOf(dto.getToTime()); // ISO format
						String ToTimeFormatted = ToTimeIso.substring(11, 16); // "05:00"
						String ToTimeTime = toDateStr + " " + ToTimeFormatted;
						SimpleDateFormat formats = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						Date toDate = formats.parse(ToTimeTime);

					//	Date toDate = GlobalCommonDateUtils.getDate(dto.getToDate().substring(0,10) + " " + dto.getToTime(),"dd-MM-yyyy HH:mm");
						long duration = dto.getDuration();
						long diff = toDate.getTime() - fromDate.getTime();
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
						
						Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(dto.getServiceId(),dto.getUnitId());
						if(tax != null)
						{
							orderDetailsMasterDto.setTaxId(tax.getId());
							orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
							serviceAmt = new BigDecimal(serviceRate);
							taxAmt = serviceAmt.multiply((orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01))));
						    taxAmt = new BigDecimal(Double.parseDouble(String.format("%.2f", taxAmt)));
							netAmt = new BigDecimal(rate).add(taxAmt);
						}else
						{
							orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
							serviceAmt = new BigDecimal(serviceRate);
							netAmt = new BigDecimal(rate);
						}		
							
					 consumptionDto = new ContractConsumptionDto();
						consumptionDto.setOrganizationId(dto.getOrganisationId());
						consumptionDto.setUnitId(dto.getUnitId());
						consumptionDto.setVisitTypeId(dto.getVisitTypeId());
						consumptionDto.setPatientTypeId(dto.getChargesResDto().getPatientCategoryId());
						consumptionDto.setBillingBedCategoryId(dto.getChargesResDto().getBedBillingCategoryId());
						consumptionDto.setPaymentEntitlementId(SELF);
						consumptionDto.setServiceId(dto.getServiceId());
						consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
						consumptionDto.setContractId(dto.getChargesResDto().getContractId());
						consumptionDto.setAdmissionId(dto.getChargesResDto().getAdmissionId());
						consumptionDto.setDefaultSelfTariffId(dto.getDefaultSelfTariffId());
						consumptionDto.setSelfPayeeId(dto.getSelfPayeeId());
						consumptionDto.setQuantity(1.0);
						consumptionDto.setServiceRate(serviceRate);
						consumptionDto.setFinalPrice(netAmt.doubleValue());
						consumptionDto.setPatientId(dto.getPatientId());
						consumptionDto.setSpecialityId(consumptionDtoFirst.getSpecialityId());
						consumptionDto.setSubSpecialityId(consumptionDtoFirst.getSubSpecialityId());
						consumptionDto.setIsPeriodicityService('Y');
					Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto = (ContractConsumptionDto) res.getObject();
						
					if(res.getObject()!=null)
					{
						orderDetailsMasterDto.setOrderQty(1);
						orderDetailsMasterDto.setServiceAmt(serviceAmt);
						orderDetailsMasterDto.setOrdConcession(new BigDecimal(0.00));
						orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
						orderDetailsMasterDto.setTariffId(dto.getDefaultSelfTariffId());
						orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
						orderDetailsMasterDto.setServiceRendered(1);
						orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(rate));
						orderDetailsMasterDto.setTaxAmount(taxAmt);
						orderDetailsMasterDto.setNetAmount(netAmt);
						orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
						orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
						orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
						orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
						orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
						orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
						orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
						orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
					}
				 }
					
				List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<>();
				orderDetailsList.add(orderDetailsMasterDto);
				
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				
				Response orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto);
				
				Integer orderDtlId= (Integer)orderRes.getResObject();
				
				dto.setOrderDetailId(orderDtlId);
				dto.setNoOfUnit((int)noOfUnits);
				return dto;
			
			}else{
				
				OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(dto.getOrderDetailId());
				
				if(dto.getToDate()!=null && dto.getToTime()!=null)
				 {
					 Double rate = null;
					 Double serviceRate = null;
					 Double serviceRateperMin = null;
					 ContractConsumptionDto consumptionDtoFirst = new ContractConsumptionDto();
						 consumptionDtoFirst.setOrganizationId(dto.getOrganisationId());
						 consumptionDtoFirst.setUnitId(dto.getUnitId());
						 consumptionDtoFirst.setVisitTypeId(dto.getVisitTypeId());
						 consumptionDtoFirst.setPatientTypeId(dto.getChargesResDto().getPatientCategoryId());
						 consumptionDtoFirst.setBillingBedCategoryId(dto.getChargesResDto().getBedBillingCategoryId());
						 consumptionDtoFirst.setPaymentEntitlementId(SELF);
						 consumptionDtoFirst.setServiceId(dto.getServiceId());
						 consumptionDtoFirst.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
						 consumptionDtoFirst.setAdmissionId(dto.getChargesResDto().getAdmissionId());
						 consumptionDtoFirst.setDefaultSelfTariffId(dto.getDefaultSelfTariffId());
						 consumptionDtoFirst.setSelfPayeeId(dto.getSelfPayeeId());
						 consumptionDtoFirst.setPatientId(dto.getPatientId());
						 
					Response selfRes =iContractConsumptionService.consumeServiceFromContract(consumptionDtoFirst);
						consumptionDtoFirst=(ContractConsumptionDto)selfRes.getObject();
					
						//calculate no of units
					 String fromDateStr = dto.getFromDate().substring(0,10); // dd-MM-yyyy
					 String fromTimeIso = String.valueOf(dto.getFromTime()); // ISO format
					 String fromTimeFormatted = fromTimeIso.substring(11, 16); // "05:00"
					 String fullDateTime = fromDateStr + " " + fromTimeFormatted;
					 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					 Date fromDate = format.parse(fullDateTime);


					 String toDateStr = dto.getToDate().substring(0,10); // dd-MM-yyyy
					 String ToTimeIso = String.valueOf(dto.getToTime()); // ISO format
					 String ToTimeFormatted = ToTimeIso.substring(11, 16); // "05:00"
					 String ToTimeTime = toDateStr + " " + ToTimeFormatted;
					 SimpleDateFormat formats = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					 Date toDate = formats.parse(ToTimeTime);
					 
						long duration = dto.getDuration();
						long diff = toDate.getTime() - fromDate.getTime();
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
						Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(dto.getServiceId(),dto.getUnitId());
						if(tax != null)
						{
							orderDetailsMasterDto.setTaxId(tax.getId());
							orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
							serviceAmt = new BigDecimal(rate);
							taxAmt = serviceAmt.multiply((orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01))));
							netAmt = serviceAmt.add(taxAmt);
						}else
						{
							orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
							serviceAmt = new BigDecimal(rate);
							netAmt = serviceAmt;
						}		
							
						 consumptionDto = new ContractConsumptionDto();
							consumptionDto.setOrganizationId(dto.getOrganisationId());
							consumptionDto.setUnitId(dto.getUnitId());
							consumptionDto.setVisitTypeId(dto.getVisitTypeId());
							consumptionDto.setPatientTypeId(dto.getChargesResDto().getPatientCategoryId());
							consumptionDto.setBillingBedCategoryId(dto.getChargesResDto().getBedBillingCategoryId());
							consumptionDto.setPaymentEntitlementId(SELF);
							consumptionDto.setServiceId(dto.getServiceId());
							consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
							consumptionDto.setContractId(dto.getChargesResDto().getContractId());
							consumptionDto.setAdmissionId(dto.getChargesResDto().getAdmissionId());
							consumptionDto.setDefaultSelfTariffId(dto.getDefaultSelfTariffId());
							consumptionDto.setSelfPayeeId(dto.getSelfPayeeId());
							consumptionDto.setQuantity(1.0);
							consumptionDto.setServiceRate(serviceRate);
							consumptionDto.setFinalPrice(netAmt.doubleValue());
							consumptionDto.setPatientId(dto.getPatientId());
							consumptionDto.setSpecialityId(consumptionDtoFirst.getSpecialityId());
							consumptionDto.setSubSpecialityId(consumptionDtoFirst.getSubSpecialityId());
							consumptionDto.setIsPeriodicityService('Y');
						Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
							consumptionDto = (ContractConsumptionDto) res.getObject();
							
						if(res.getObject()!=null)
						{
							orderDetailsMasterDto.setOrderQty(1);
							orderDetailsMasterDto.setServiceAmt(serviceAmt);
							orderDetailsMasterDto.setOrdConcession(new BigDecimal(0.00));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(dto.getDefaultSelfTariffId());
							orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
							orderDetailsMasterDto.setServiceRendered(1);
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(rate));
							orderDetailsMasterDto.setTaxAmount(taxAmt);
							orderDetailsMasterDto.setNetAmount(netAmt);
							orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
						}
						
						iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto,consumptionDto.getOrderDetailsPayeeMapperDtosList());
				 }
				
				dto.setOrderDetailId(dto.getOrderDetailId());
				dto.setNoOfUnit((int)noOfUnits);
				return dto;
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	@Transactional
	public Response getTPeriodicityServicesByAdmissionId(Integer admissionId)
			throws BillingException {
		try{
			
			return iTPeriodicityServiceOrdersDao.getTPeriodicityServicesByAdmissionId(admissionId);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	public Response orderPeriodicityServices(List<TPeriodicityServiceOrdersDto> listPserv){
		try{
			Date today = TimeZoneComponent.getDateByzone(listPserv.getFirst().getUnitId());
			if(listPserv != null && listPserv.size() > 0){
				
			//get admission details
			Response admRes = iTPeriodicityServiceOrdersDao.getAdmissionByIdForBatchPServicePosting(listPserv.getFirst().getAdmissionId());
			if(admRes.getCode().equals(SUCCESS_CODE) && admRes.getObject() != null){
				AdmissionsForBatchProcessDto admission = (AdmissionsForBatchProcessDto)admRes.getObject();

				
				//get default tariff
				UnitApplicationConfigurationDto uAppConfDto = new UnitApplicationConfigurationDto();
				uAppConfDto.setOrganizationId(admission.getOrganizationId());
				uAppConfDto.setUnitId(admission.getUnitId());
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(uAppConfDto);
				if(appConfigRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && appConfigRes.getObject() != null){
					uAppConfDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				}

				//create order master
				List<OrderDetailsMasterDto> listOrderDetails = new LinkedList<>();
				OrderMasterDto orderMst = new OrderMasterDto();
				orderMst.setAdmissionId(admission.getAdmissionId());
				orderMst.setCreatedBy(0);
				orderMst.setCreatedDate(today.getTime());
				orderMst.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "dd-MM-yyyy"));
				orderMst.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
				orderMst.setDiscountPercentage(0);
				orderMst.setDoctorId(admission.getDoctorId());
				orderMst.setOrderDate(today.getTime());
				orderMst.setOrderStatus(ACTIVE);
				orderMst.setOrderStatusId(1);
				orderMst.setOrdRemarks("Periodicity Services");
				orderMst.setOrgId(admission.getOrganizationId());
				orderMst.setOrgUnitId(admission.getUnitId());
				orderMst.setPatientId(admission.getPatientId());
				orderMst.setPaymentEntitlementTypeId(admission.getPaymentEntitlementId());
				orderMst.setPriorityId(1);
				orderMst.setUpdatedBy(1);
				orderMst.setUpdatedDate(today.getTime());
				orderMst.setVisitTypeId(admission.getVisitTypeId());
				
				ListIterator<TPeriodicityServiceOrdersDto> pServItr = listPserv.listIterator();
				
				while(pServItr.hasNext()){
					TPeriodicityServiceOrdersDto pService = pServItr.next();
						if(pService.getOrderStatusId() != 2 && pService.getToDate() != null && !pService.getToDate().isEmpty() && pService.getToTime() != null){
							//get service price
							UnitServiceTariffMasterDto tariffDto = new UnitServiceTariffMasterDto();
							tariffDto.setServiceId(pService.getServiceId());
							tariffDto.setTariffId(admission.getTariffId());
							tariffDto.setPaymentEntitlementId(admission.getPaymentEntitlementId());
							tariffDto.setVisitTypeId(admission.getVisitTypeId());
							tariffDto.setPatientTypeId(admission.getPatientTypeId());
							tariffDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
							tariffDto.setOrganizationId(admission.getOrganizationId());
							tariffDto.setUnitId(admission.getUnitId());
							tariffDto.setOrderDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "yyyy-M-dd"));
							UnitServiceTariffMasterDto serviceConfig = iUnitServiceTariffMasterDao.getBasePriceByServiceTariffMaster(tariffDto);
						
							//calculate no of units
							Date fromDate = GlobalCommonDateUtils.getDate(pService.getFromDate() + " " + pService.getFromTime(),"dd-MM-yyyy HH:mm");
							Date toDate = GlobalCommonDateUtils.getDate(pService.getToDate() + " " + pService.getToTime(),"dd-MM-yyyy HH:mm");
							int noOfUnits = 0;
							long duration = pService.getDuration();
							long diff = toDate.getTime() - fromDate.getTime();
							switch(pService.getMeasurementUnitId()){
							case 1 : //minute
								long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
								noOfUnits = (int)(diffMinutes / duration);
								break;
							case 2 : //hour
								long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
								noOfUnits = (int)(diffHours / duration);
								break;
							case 3 : //day
								long diffInDays = TimeUnit.MILLISECONDS.toDays(diff);
								noOfUnits = (int)(diffInDays / duration);
								break;
							}
							
							if(noOfUnits > 0){
								//check services available in active packages
								PackageServiceCheckReqDto packServReqDto = new PackageServiceCheckReqDto();
								packServReqDto.setPatientId(admission.getPatientId());
								packServReqDto.setEncounterId(admission.getAdmissionId());
								packServReqDto.setVisitType(admission.getVisitTypeId());
								packServReqDto.setOrganisationId(admission.getOrganizationId());
								packServReqDto.setUnitId(admission.getUnitId());
								packServReqDto.setServiceId(pService.getServiceId());
								packServReqDto.setGroupId(pService.getSpecialityId());
								packServReqDto.setSubGroupId(pService.getSubSpecialityId());
								packServReqDto.setTariffId(admission.getTariffId());
								packServReqDto.setPaymentEntitlementId(admission.getPaymentEntitlementId());
								packServReqDto.setPatientTypeId(admission.getPatientTypeId());
								packServReqDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
								
								Response packageServRes = iMPackageMasterService.checkIfServiceFromPackage(packServReqDto);
								
								if(packageServRes.getCode().equals(SUCCESS_CODE)){
									TPackageConsumptionServiceDetailDto serviceDtls = (TPackageConsumptionServiceDetailDto)packageServRes.getObject();
									//service available in package
									OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
									orderDetails.setBedCategoryId(admission.getBedCategoryId());
									orderDetails.setBedId(admission.getBedId());
									orderDetails.setCreatedBy(0);
									orderDetails.setCreatedDate(today.getTime());
									orderDetails.setIsCapService(serviceDtls.getIsCapService());
									orderDetails.setIsPackageService('Y');
									orderDetails.setNetAmount(BigDecimal.valueOf(0));
									orderDetails.setOrderDate(today.getTime());
									orderDetails.setOrderQty(1);
									orderDetails.setOrdTotalAmt(BigDecimal.valueOf(0));
									orderDetails.setOrgId(admission.getOrganizationId());
									orderDetails.setOrgUnitId(admission.getUnitId());
									orderDetails.setPackageConsumptionAmt(orderDetails.getIsCapService() == 'Y' ? BigDecimal.valueOf(serviceConfig.getRate()) : BigDecimal.valueOf(serviceDtls.getApportionedPrice()));
									orderDetails.setPackageId(serviceDtls.getPackageId());
									orderDetails.setRoomId(admission.getRoomId());
									orderDetails.setSelfPayable(orderDetails.getIsCapService() == 'Y' ? BigDecimal.valueOf(serviceConfig.getRate() - serviceDtls.getBalanceCapAmount()) : BigDecimal.valueOf(0));
									orderDetails.setServiceAmt(BigDecimal.valueOf(0));
									orderDetails.setServiceId(pService.getServiceId());
									orderDetails.setServiceIsBilled(0);
									orderDetails.setServiceRendered(1);
									orderDetails.setStatus(ACTIVE);
									orderDetails.setTariffId(admission.getTariffId());
									orderDetails.setUpdatedBy(0);
									orderDetails.setUpdatedDate(today.getTime());
									orderDetails.setWardId(admission.getWardId());
									orderDetails.setPriorityId(2);
									orderDetails.setIsOutsourced('N');
									orderDetails.setOrderSchDate(today.getTime());
									orderDetails.setOrdConcession(BigDecimal.valueOf(0));
									orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
									orderDetails.setOrdDocId(admission.getDoctorId());
									orderDetails.setServiceRenderingDeptId(pService.getSpecialityId());
									orderDetails.setOrdCancelled('N');
									orderDetails.setIsDrug('N');
									
									//update package consumption qty
									if(orderDetails.getIsCapService() == 'Y'){
										//update cap amount
										iTPackageConsumptionCapDetailsDao.updateTPackageConsumptCapAmtByDeptSubDept(serviceDtls.getPackageId(), admission.getPatientId(), null, pService.getSpecialityId(), pService.getSubSpecialityId(), BigDecimal.valueOf(serviceConfig.getRate() <= serviceDtls.getBalanceCapAmount() ? serviceConfig.getRate() : serviceDtls.getBalanceCapAmount()).negate(), admission.getAdmissionId(), admission.getVisitTypeId());
									}else{
										//update service qty
										iTPackageConsumptionServiceDetailDao.updatePackageConsumptionServiceQty(serviceDtls.getPackageId(), admission.getPatientId(), null, pService.getServiceId(), admission.getAdmissionId(), admission.getVisitTypeId());
									}
									
									listOrderDetails.add(orderDetails);
								}else{
									//update as normal service
									OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
									orderDetails.setBedCategoryId(admission.getBedCategoryId());
									orderDetails.setBedId(admission.getBedId());
									orderDetails.setCreatedBy(0);
									orderDetails.setCreatedDate(today.getTime());
									orderDetails.setNetAmount(BigDecimal.valueOf(serviceConfig.getRate()));
									orderDetails.setOrderDate(today.getTime());
									orderDetails.setOrderQty(noOfUnits);
									orderDetails.setOrdTotalAmt(BigDecimal.valueOf(serviceConfig.getRate()));
									orderDetails.setOrgId(admission.getOrganizationId());
									orderDetails.setOrgUnitId(admission.getUnitId());
									orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
									orderDetails.setRoomId(admission.getRoomId());
									orderDetails.setSelfPayable(BigDecimal.valueOf(serviceConfig.getRate()));
									orderDetails.setServiceAmt(BigDecimal.valueOf(serviceConfig.getRate()));
									orderDetails.setServiceId(pService.getServiceId());
									orderDetails.setServiceIsBilled(0);
									orderDetails.setServiceRendered(1);
									orderDetails.setStatus(ACTIVE);
									orderDetails.setTariffId(admission.getTariffId());
									orderDetails.setUpdatedBy(0);
									orderDetails.setUpdatedDate(today.getTime());
									orderDetails.setWardId(admission.getWardId());
									orderDetails.setPriorityId(2);
									orderDetails.setIsOutsourced('N');
									orderDetails.setOrderSchDate(today.getTime());
									orderDetails.setOrdConcession(BigDecimal.valueOf(0));
									orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
									orderDetails.setOrdDocId(admission.getDoctorId());
									orderDetails.setServiceRenderingDeptId(pService.getSpecialityId());
									orderDetails.setOrdCancelled('N');
									orderDetails.setIsDrug('N');
									
									listOrderDetails.add(orderDetails);
								}
								//update no of units and last updation
								iTPeriodicityServiceOrdersDao.updateTPeriodicityNoOfUnitsById(noOfUnits, pService.getPeriodicityServiceOrderId(), 2);
							}
						}
				   }
				
				if(orderMst != null && listOrderDetails.size() > 0){
					//save order details
					orderMst.setListOrderDetailsMasterDto(listOrderDetails);
					iOrderMasterService.saveOrderMasterWithDiscount(orderMst,null);
					orderMst = null;
					listOrderDetails.clear();
					log.info("Batch Job : periodicity services charges posted for admission : " + admission.getAdmissionId() + " on " + today);
				}
				}
			}
		}catch(Exception e){
			
			throw e;
		}
		
		return null;
	}
	@Override
	@Transactional
	public Response cancelOrderedPeriodicityService(CancelOrderedServicesDto cancelOrderedServicesDto) throws BillingException {
		try{
				iTPeriodicityServiceOrdersDao.cancelOrderedPeriodicityService(cancelOrderedServicesDto.getPeriodicityServiceOrderId(),cancelOrderedServicesDto.getStatus());
				if(cancelOrderedServicesDto.getStatus().equals('I'))
					return billingMasterDao.cancelUnRenderedServices(cancelOrderedServicesDto);
				else
					return billingMasterDao.activateUnRenderedServices(cancelOrderedServicesDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public TPeriodicityServiceOrdersDto getPeriodicityServiceDetails(Integer orderDetailsId) throws BillingException {
		try{
				return iTPeriodicityServiceOrdersDao.getTPeriodicityServicesByOrderDetailsId(orderDetailsId);
		}catch(Exception e){
			
			throw e;
		}
	}


}
