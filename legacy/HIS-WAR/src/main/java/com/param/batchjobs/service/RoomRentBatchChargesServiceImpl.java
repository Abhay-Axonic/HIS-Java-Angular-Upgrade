package com.param.batchjobs.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IAutoPostingConfigDao;
import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.opd.coversheet.dao.IAdmissionDAO;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RoomRentBatchChargesServiceImpl implements IRoomRentBatchChargesService, ICommonConstants {

	@Autowired
	private IAdmissionDAO iAdmissionDAO;
	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	@Autowired
	private IOrderMasterService iOrderMasterService;
	@Autowired
	private IAutoPostingConfigDao iAutoPostingConfigDao;

	@Override
	@Transactional
	public void postRoomRentCharges( List<Integer> unitIds) {
		try {

			Date zoneDate = TimeZoneComponent.getDateByzone(unitIds.getFirst());
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(zoneDate);
			cal.add(Calendar.DAY_OF_MONTH, -1); // Move one day back
	        // Set time to 11:59 PM
	        cal.set(Calendar.HOUR_OF_DAY, 23);
	        cal.set(Calendar.MINUTE, 59);
	        cal.set(Calendar.SECOND, 59);
	        cal.set(Calendar.MILLISECOND, 999);
	        Date previosDate = cal.getTime();
			
			int startPoint = 0;
			int maxResults = 1000;
			Response response = iAdmissionDAO.getAdmissionsForBatchProcess(startPoint, maxResults,unitIds);
			
			List<AdmissionsForBatchProcessDto> forBatchProcessDtosList = response.getListObject();
			
			Response serviceConfigResForAllUnits = iAutoPostingConfigDao.getAutoPostingConfigListForRoomRent();
			List<AutoPostingServicesConfigDto> autoPostingServicesConfigDtoListForAllUnits = serviceConfigResForAllUnits.getListObject();
			
			
			//getAutoPostingConfigListForRoomRent
			if (response.getCode().equals(SUCCESS_CODE) && response.getListObject() != null	&& response.getListObject().size() > 0) 
			{
				for (AdmissionsForBatchProcessDto admission : forBatchProcessDtosList) 
				{
					List<OrderDetailsMasterDto> orderDetailsList = new LinkedList<>();
					
					ContractConsumptionDto consumptionDto = null;
					OrderMasterDto orderMasterDto = new OrderMasterDto();
						orderMasterDto.setVisitTypeId(IP);// op==1
						orderMasterDto.setPatientId(admission.getPatientId());
						orderMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
						orderMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
						orderMasterDto.setOrderDate(previosDate.getTime());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrgId(admission.getOrganizationId());
						orderMasterDto.setOrgUnitId(admission.getUnitId());
						orderMasterDto.setOrderStatusId(1);// order status==1
						orderMasterDto.setAdmissionId(admission.getAdmissionId());
						orderMasterDto.setPatientTypeId(admission.getPatientTypeId());
						orderMasterDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
						orderMasterDto.setPaymentEntitlementTypeId(admission.getPaymentEntitlementId());
						orderMasterDto.setReferralId(admission.getReferralId());
						orderMasterDto.setDefaultDiscountOwnership(admission.getDefaultDiscountOwnership());
					
					List<AutoPostingServicesConfigDto> autoPostingServicesConfigDtoList = new LinkedList<>();
							
					autoPostingServicesConfigDtoList=autoPostingServicesConfigDtoListForAllUnits
							.stream().filter(data -> data.getUnitId().equals(admission.getUnitId()))
							.collect(Collectors.toList());
					if (!(autoPostingServicesConfigDtoList.isEmpty())) 
					{
						for(AutoPostingServicesConfigDto dto : autoPostingServicesConfigDtoList) 
						{
							Integer serviceChargeStatus = autoServiceChargesCalculation(dto, admission);
							// -------order details-------//
							if (serviceChargeStatus != 2) 
							{
								OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
								Integer bedCateServiceId = admission.getServiceIdForBedCharges();
								orderDetailsMasterDto.setServiceId((dto.getIsBedChargesService()!=null && dto.getIsBedChargesService()=='Y') ? bedCateServiceId>0 ? bedCateServiceId :dto.getServiceId() :dto.getServiceId());
								orderDetailsMasterDto.setServiceIsBilled(0);
								orderDetailsMasterDto.setOrderQty(1);
								orderDetailsMasterDto.setOrderDate(previosDate.getTime());
								orderDetailsMasterDto.setOrderSchDate(previosDate.getTime());
								orderDetailsMasterDto.setOrdRenderDatetime(previosDate.getTime());
								orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setStatus('A');
								orderDetailsMasterDto.setOrgId(admission.getOrganizationId());
								orderDetailsMasterDto.setOrgUnitId(admission.getUnitId());
								orderDetailsMasterDto.setServiceRenderingDeptId(dto.getSpecialityId());
								orderDetailsMasterDto.setOrdCancelled('N');
								orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
								orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
								orderDetailsMasterDto.setTariffId(admission.getDefaultSelfTariffId());
								orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
								orderDetailsMasterDto.setOrderEmergencyFlag('N');
								orderDetailsMasterDto.setServiceRendered(1);
								orderDetailsMasterDto.setServiceChargeable(1);
								orderDetailsMasterDto.setDepartmentId(dto.getSpecialityId());
								orderDetailsMasterDto.setBedId(admission.getBedId());
								orderDetailsMasterDto.setWardId(admission.getWardId());
								orderDetailsMasterDto.setRoomId(admission.getRoomId());
								orderDetailsMasterDto.setIsChargedHalfDay(serviceChargeStatus==0 ? 'Y' : 'N');
								orderDetailsMasterDto.setBedCategoryId(admission.getBillingBedCategoryId());
								orderDetailsMasterDto.setIsDrug('N');
									consumptionDto = new ContractConsumptionDto();
									consumptionDto.setOrganizationId(admission.getOrganizationId());
									consumptionDto.setUnitId(admission.getUnitId());
									consumptionDto.setVisitTypeId(IP);
									consumptionDto.setPatientTypeId(admission.getPatientTypeId());
									consumptionDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
									consumptionDto.setPaymentEntitlementId(SELF);
									consumptionDto.setServiceId((dto.getIsBedChargesService()!=null && dto.getIsBedChargesService()=='Y') ? bedCateServiceId>0 ? bedCateServiceId :dto.getServiceId() :dto.getServiceId());
									consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(previosDate, "yyyy-MM-dd HH:mm:ss"));
									consumptionDto.setContractId(admission.getContractId());
									consumptionDto.setDefaultSelfTariffId(admission.getDefaultSelfTariffId());
									consumptionDto.setSelfPayeeId(admission.getPayeeId());
									consumptionDto.setAdmissionId(admission.getAdmissionId());
									consumptionDto.setEncounterId(admission.getAdmissionId());
									consumptionDto.setPatientId(admission.getPatientId());
									consumptionDto.setServiceChargeStatus(serviceChargeStatus);
									consumptionDto.setPatientClassId(admission.getPatientClassId());
									consumptionDto.setIsBedService(dto.getIsBedChargesService());
								Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
									
									consumptionDto = (ContractConsumptionDto) res.getObject();

									if (consumptionDto != null) 
									{
										orderDetailsMasterDto.setServiceAmt(consumptionDto.getServiceRate()!=null ? new BigDecimal(consumptionDto.getServiceRate()) : new BigDecimal(0));
										orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));	
										orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
										orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
										orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
                                        orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
										orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
										orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
										orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
										orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
										orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
										orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
										orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
										orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
										if(orderDetailsMasterDto.getBedCategoryId() == null){
											orderDetailsMasterDto.setBedCategoryId(admission.getBillingBedCategoryId()== null ? consumptionDto.getBillingBedCategoryId(): admission.getBillingBedCategoryId());
										}
										orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
										orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
										orderDetailsMasterDto.setDiscountOwnership(admission.getDefaultDiscountOwnership());
										if(consumptionDto.getIsDocReq()!=null && consumptionDto.getIsDocReq()=='Y')
										{
											orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());	
											orderDetailsMasterDto.setOrdDocId(admission.getDoctorId());
											orderDetailsMasterDto.setOrdDocSplId(admission.getSpecialityId());
										}
									}
									if(admission.getPayeeId() == null || admission.getPayeeId() == 1) {
										orderDetailsMasterDto.setCoPayPer(new BigDecimal(100));
									}
									if(orderDetailsMasterDto.getOrdTotalAmt().doubleValue() > 0)
									{
										orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
										
										if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
											orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
										orderDetailsList.add(orderDetailsMasterDto);
									}	
							}
						}
						if(orderDetailsList.size()>0)
						{
							orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
							iOrderMasterService.saveOrderMaster(orderMasterDto);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private Integer autoServiceChargesCalculation(AutoPostingServicesConfigDto autoPostingServicesConfigDto,
			AdmissionsForBatchProcessDto admission) {
		LocalTime totalTime = null;
		LocalTime halfDayPlusBufferTime = null;
		LocalTime btime = null;
		String admissionTime = admission.getAdmissionDateTime().substring(11, 16);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		// admission.setAdmissionDateTime(admissionTime);
		LocalDate appDate = LocalDate.parse(admission.getAdmissionDateTime(), formatter);
		LocalDate currentDate = LocalDate.now();
		long diffInDays = ChronoUnit.DAYS.between(appDate, currentDate);
		if (autoPostingServicesConfigDto.getPostingTime() == 1) 
		{
			autoPostingServicesConfigDto.setServicePostingTime("00:00");
			if (diffInDays > 1) {
				return 1;
			}
			if (diffInDays == 1 && admissionTime.equals("00:00")) {
				return 1;
			}
		} else {
			autoPostingServicesConfigDto.setServicePostingTime("12:00");
			if (diffInDays > 0) {
				return 1;
			}
		}

		if (autoPostingServicesConfigDto.getIsHalfDay().equals('Y')) {

			LocalTime appTime = LocalTime.parse(admissionTime);
			LocalTime batchJobTime = LocalTime.parse(autoPostingServicesConfigDto.getServicePostingTime());
			LocalTime halfDay = LocalTime.parse(autoPostingServicesConfigDto.getHalfDayHrs() < 10
					? "0" + autoPostingServicesConfigDto.getHalfDayHrs().toString() + ":00"
					: autoPostingServicesConfigDto.getHalfDayHrs().toString() + ":00");
			if(autoPostingServicesConfigDto.getBufferTimeForAdmission()!=null)
			btime = LocalTime.parse(autoPostingServicesConfigDto.getBufferTimeForAdmission() < 10
					? "0" + autoPostingServicesConfigDto.getBufferTimeForAdmission().toString() + ":00"
					: autoPostingServicesConfigDto.getBufferTimeForAdmission().toString() + ":00");

			totalTime = batchJobTime.minusHours(appTime.getHour()).minusMinutes(appTime.getMinute());
			
			//System.out.println("totalTime-" + totalTime);
			
			if(btime!=null)
				if(totalTime.isAfter(btime))
				halfDayPlusBufferTime = halfDay.plusHours(btime.getHour());
			else
				halfDayPlusBufferTime=halfDay;
			
			//System.out.println("Half day and Buffer Time-" + halfDayPlusBufferTime);

			
				if (halfDayPlusBufferTime!=null && (totalTime.isBefore(halfDayPlusBufferTime) || totalTime.equals(halfDayPlusBufferTime))) {
					//System.out.println("Half Day");
					return 0;
				} else {
					//System.out.println("Full Day");
					return 1;
				}

		} else {
			return 1; 
		}
	}

	@Override
	@Transactional
	public void postPeriodicServices(List<Integer> unitIds) {
		try {

			Date zoneDate = TimeZoneComponent.getDateByzone(unitIds.getFirst());
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(zoneDate);
			cal.add(Calendar.DAY_OF_MONTH, -1); // Move one day back
	        // Set time to 11:59 PM
	        cal.set(Calendar.HOUR_OF_DAY, 23);
	        cal.set(Calendar.MINUTE, 59);
	        cal.set(Calendar.SECOND, 59);
	        cal.set(Calendar.MILLISECOND, 999);
	        Date previosDate = cal.getTime();
			
			int startPoint = 0;
			int maxResults = 1000;
			Response response = iAdmissionDAO.getAdmissionsForPeriodicPosting(startPoint, maxResults,unitIds);
			if (response.getCode().equals(SUCCESS_CODE) && response.getListObject() != null	&& response.getListObject().size() > 0) 
			{
				ListIterator<AdmissionsForBatchProcessDto> admissionsItr = response.getListObject().listIterator();
				while (admissionsItr.hasNext()) 
				{
					
					AdmissionsForBatchProcessDto admission = admissionsItr.next();
					List<OrderDetailsMasterDto> orderDetailsList = new LinkedList<>();
					
					ContractConsumptionDto consumptionDto = null;
					OrderMasterDto orderMasterDto = new OrderMasterDto();
						orderMasterDto.setVisitTypeId(IP);// op==1
						orderMasterDto.setPatientId(admission.getPatientId());
						orderMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
						orderMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
						orderMasterDto.setOrderDate(previosDate.getTime());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrgId(admission.getOrganizationId());
						orderMasterDto.setOrgUnitId(admission.getUnitId());
						orderMasterDto.setOrderStatusId(1);// order status==1
						orderMasterDto.setAdmissionId(admission.getAdmissionId());
						orderMasterDto.setPatientTypeId(admission.getPatientTypeId());
						orderMasterDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
						orderMasterDto.setPaymentEntitlementTypeId(admission.getPaymentEntitlementId());
						orderMasterDto.setReferralId(admission.getReferralId());
						orderMasterDto.setDefaultDiscountOwnership(admission.getDefaultDiscountOwnership());
					Response serviceConfigRes = iAutoPostingConfigDao.getAutoPostingConfigList(admission.getOrganizationId(), admission.getUnitId());
					List<AutoPostingServicesConfigDto> autoPostingServicesConfigDtoList = new LinkedList<>();
					if (serviceConfigRes.getListObject().size() > 0) 
					{
						autoPostingServicesConfigDtoList = (List<AutoPostingServicesConfigDto>) serviceConfigRes.getObject();
						if (!(autoPostingServicesConfigDtoList.isEmpty())) 
						{
							ListIterator<AutoPostingServicesConfigDto> iterator = autoPostingServicesConfigDtoList.listIterator();
							while (iterator.hasNext()) 
							{
								AutoPostingServicesConfigDto dto = iterator.next();
								Integer serviceChargeStatus = 1;
								// -------order details-------//
								if (serviceChargeStatus != 2) 
								{
									OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
									orderDetailsMasterDto.setServiceId(dto.getServiceId());
									orderDetailsMasterDto.setServiceIsBilled(0);
									orderDetailsMasterDto.setOrderQty(1);
									orderDetailsMasterDto.setOrderDate(previosDate.getTime());
									orderDetailsMasterDto.setOrderSchDate(previosDate.getTime());
									orderDetailsMasterDto.setOrdRenderDatetime(previosDate.getTime());
									orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
									orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
									orderDetailsMasterDto.setStatus('A');
									orderDetailsMasterDto.setOrgId(admission.getOrganizationId());
									orderDetailsMasterDto.setOrgUnitId(admission.getUnitId());
									orderDetailsMasterDto.setServiceRenderingDeptId(dto.getSpecialityId());
									orderDetailsMasterDto.setOrdCancelled('N');
									orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
									orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
									orderDetailsMasterDto.setTariffId(admission.getDefaultSelfTariffId());
									orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
									orderDetailsMasterDto.setOrderEmergencyFlag('N');
									orderDetailsMasterDto.setServiceRendered(1);
									orderDetailsMasterDto.setServiceChargeable(1);
									orderDetailsMasterDto.setDepartmentId(dto.getSpecialityId());
									orderDetailsMasterDto.setBedId(admission.getBedId());
									orderDetailsMasterDto.setWardId(admission.getWardId());
									orderDetailsMasterDto.setRoomId(admission.getRoomId());
									orderDetailsMasterDto.setIsChargedHalfDay(serviceChargeStatus==0 ? 'Y' : 'N');
									if(dto.getIsDocReq()!=null && dto.getIsDocReq()=='Y')
									{
										orderDetailsMasterDto.setOrdDocId(admission.getDoctorId());
										orderDetailsMasterDto.setOrdDocSplId(admission.getSpecialityId());
									}
									orderDetailsMasterDto.setIsDrug('N');
										consumptionDto = new ContractConsumptionDto();
										consumptionDto.setOrganizationId(admission.getOrganizationId());
										consumptionDto.setUnitId(admission.getUnitId());
										consumptionDto.setVisitTypeId(IP);
										consumptionDto.setPatientTypeId(admission.getPatientTypeId());
										consumptionDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
										consumptionDto.setPaymentEntitlementId(SELF);
										consumptionDto.setServiceId(dto.getServiceId());
										consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(previosDate, "yyyy-MM-dd HH:mm:ss"));
										consumptionDto.setContractId(admission.getContractId());
										consumptionDto.setDefaultSelfTariffId(admission.getDefaultSelfTariffId());
										consumptionDto.setSelfPayeeId(admission.getPayeeId());
										consumptionDto.setAdmissionId(admission.getAdmissionId());
										consumptionDto.setEncounterId(admission.getAdmissionId());
										consumptionDto.setPatientId(admission.getPatientId());
										consumptionDto.setServiceChargeStatus(serviceChargeStatus);
										consumptionDto.setPatientClassId(admission.getPatientClassId());
									Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
										
										consumptionDto = (ContractConsumptionDto) res.getObject();

										if (consumptionDto != null) 
										{
											orderDetailsMasterDto.setServiceAmt(consumptionDto.getServiceRate()!=null ? new BigDecimal(consumptionDto.getServiceRate()) : new BigDecimal(0));
											orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));	
											orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
											orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
											orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
                                            orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
											orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
											orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
											orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
											orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
											orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
											orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
											orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
											orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
											orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
											orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
											orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
											orderDetailsMasterDto.setDiscountOwnership(admission.getDefaultDiscountOwnership());
											if(consumptionDto.getIsDocReq()!=null && consumptionDto.getIsDocReq()=='Y')
											{
												orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());	
												orderDetailsMasterDto.setOrdDocId(admission.getDoctorId());
												orderDetailsMasterDto.setOrdDocSplId(admission.getSpecialityId());
											}
										}

										if(orderDetailsMasterDto.getOrdTotalAmt().doubleValue() > 0)
										{
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
											
											if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
												orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
											orderDetailsList.add(orderDetailsMasterDto);
										}	
								}
								
							}
						}
						if(orderDetailsList.size()>0)
						{
							orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
							iOrderMasterService.saveOrderMaster(orderMasterDto);
						}	
					} 
				}
				
			}
		} catch (Exception e) {
			throw e;
		}
		
	}

}
