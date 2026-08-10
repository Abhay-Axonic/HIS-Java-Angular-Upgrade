package com.param.billing.global.transaction.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.param.billing.global.transaction.dto.BillListCNDto;
import com.param.global.dto.*;
import com.param.opd.common.CommonDateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.dto.BillListResDto;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.dto.EncounterPaymentDto;
import com.param.billing.global.model.DepositMaster;
import com.param.billing.global.smartcard.dto.Claim;
import com.param.billing.global.transaction.config.dao.IRequestDiscountDao;
import com.param.billing.global.transaction.dao.IBillPayeeDetailsDao;
import com.param.billing.global.transaction.dao.IBillStatusMapperDao;
import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.IConcessessionManagerDao;
import com.param.billing.global.transaction.dao.ICreditNoteMasterDao;
import com.param.billing.global.transaction.dao.IPaymentDepositConsumptionDao;
import com.param.billing.global.transaction.dao.IPaymentRecieptDetailsDao;
import com.param.billing.global.transaction.dao.IPaymentRecieptMasterDao;
import com.param.billing.global.transaction.dao.ITCompanyInvoiceCreationDao;
import com.param.billing.global.transaction.dto.BillDetailsByBillIdDto;
import com.param.billing.global.transaction.dto.BillRecieptDto;
import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.billing.global.transaction.model.BillDetailsPayeeMapper;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.BillingStatusMapperId;
import com.param.billing.global.transaction.model.PaymentReceiptDetails;
import com.param.billing.ipd.dao.IPreAuthorizationRequest;
import com.param.billing.packages.model.TPackageConsumptionMaster;
import com.param.billing.unit.dao.ITPeriodicityServiceOrdersDao;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.billing.unit.service.ITPeriodicityServiceOrdersService;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dao.IpdChargesDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.dto.BillingStatusMapperDto;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.billing.service.IPatientCouponTemplateMapperService;
import com.param.global.common.AppointmentStatusMasterConstant;
import com.param.global.common.BillingCommonMethods;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.IBillingStatusMasterConstant;
import com.param.global.common.IEncounterTypeMasterConstant;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.common.SmsUtility;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IOrdeDetailsPackageMapperLogDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IPackageConsumptionDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IServiceMasterDao;
import com.param.global.dao.ITPacakgeBedCategoryDao;
import com.param.global.dao.ITPackageCapDetailsDao;
import com.param.global.dao.ITPackageCategoryWiseConsumableDetailsDao;
import com.param.global.dao.ITPackageConsumptionCapDetailsDao;
import com.param.global.dao.ITPackageConsumptionEoDetailsDao;
import com.param.global.dao.ITPackageConsumptionMasterDao;
import com.param.global.dao.ITPackageConsumptionServiceDetailDao;
import com.param.global.dao.ITPackageEitherOrGroupDetailsDao;
import com.param.global.dao.ITPackageIncExcDetailsDao;
import com.param.global.dao.ITPackageServicesDetailsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUnitServiceMapperDao;
import com.param.global.dao.IUnitServiceTariffMasterDao;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.mail.IMailingService;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IAppointmentSchedulingService;
import com.param.global.opd.service.IEncounterAssignmenMapperService;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.pesapal.dao.IPesaPalIntegrationDao;
import com.param.global.pesapal.dto.PesapalPaymentDto;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IItrimBillService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsDiscountService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.service.IPatientSponsorDetailsService;
import com.param.global.service.IUnitServiceMasterDetailsService;
import com.param.global.service.IUnitServiceTariffMasterService;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.global.utility.SerialNoUtility;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.SessionService;
import com.param.service.global.model.OrdeDetailsPackageMapperLog;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked", "restriction", "unused" })
public class BillingMasterServiceImpl implements IBillingMasterService, ICommonConstants, IBillingStatusMasterConstant, IEncounterTypeMasterConstant {

	final static Logger logger = Logger.getLogger(BillingMasterServiceImpl.class);

	@Autowired
	private IBillingMasterDao billingMasterDao;
	@Autowired
	private IBillingDetailsService iBillingDetailsService;
	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private IOrderMasterService iOrderMasterService;
	@Autowired
	private IBillPayeeDetailsService iBillPayeeDetailsService;
	@Autowired
	private IPaymentRecieptMasterService iPaymentRecieptMasterService;
	@Autowired
	private IUnitServiceTariffMasterService iUnitServiceTariffMasterService;
	@Autowired
	private IBillStatusMapperDao iBillStatusMapperDao;
	@Autowired
	private IOrderDetailsDiscountService iOrderDetailsDiscountService;
	@Autowired
	private IMPackageMasterDao iMPackageMasterDao;
	@Autowired
	private ITPackageConsumptionMasterDao iTPackageConsumptionMasterDao;
	@Autowired
	private ITPackageServicesDetailsDao iTPackageServicesDetailsDao;
	@Autowired
	private ITPackageConsumptionServiceDetailDao iTPackageConsumptionServiceDetailDao;
	@Autowired
	private ITPackageCategoryWiseConsumableDetailsDao iTPackageCategoryWiseConsumableDetailsDao;
	@Autowired
	private ITPackageCapDetailsDao iTPackageCapDetailsDao;
	@Autowired
	private ITPacakgeBedCategoryDao iTPacakgeBedCategoryDao;
	@Autowired
	private ITPackageConsumptionCapDetailsDao iTPackageConsumptionCapDetailsDao;
	@Autowired
	private ITPackageEitherOrGroupDetailsDao iTPackageEitherOrGroupDetailsDao;
	@Autowired
	private ITPackageConsumptionEoDetailsDao iTPackageConsumptionEoDetailsDao;
	@Autowired
	private ITPackageIncExcDetailsDao iTPackageIncExcDetailsDao;
	@Autowired
	private IOrderDetailsMasterDao iOrderDetailsMasterDao;
	@Autowired
	private IOrderDetailsMasterService iOrderDetailsMasterService;
	@Autowired
	IDepositMasterService iDepositMasterService;
	@Autowired
	private IServiceMasterDao iServiceMasterDao;
	@Autowired
	private IAppointmentSchedulingService iAppointmentSchedulingService;
	@Autowired
	private IEncounterMasterService iEncounterMasterService;
	@Autowired
	private IBillPayeeDetailsDao iBillPayeeDetailsDao;
	@Autowired
	private IPaymentDepositConsumptionService iPaymentDepositConsumptionService;
	@Autowired
	IRequestDiscountDao iRequestDiscountDao;
	@Autowired
	private IDepositMasterDao iDepositMasterDao;
	@Autowired
	private SerialNoUtility serialNoUtility;
	@Autowired
	private IPaymentRecieptMasterDao iPaymentRecieptMasterDao;
	@Autowired
	private IPaymentRecieptDetailsDao iPaymentRecieptDetailsDao;
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;
	@Autowired
	IUnitServiceMapperDao iUnitServiceMapperDao;
	@Autowired
	IUnitServiceTariffMasterDao iUnitServiceTariffMasterDao;
	@Autowired
	IPackageConsumptionDao iPackageConsumptionDao;
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	private IMpesaMasterDao iMpesaMasterDao;
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	@Autowired
	IEncounterAssignmenMapperService iEncounterAssignmenMapperService;
	@Autowired
	IItrimBillService iIntrimBillService;
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	@Autowired
	IPatientSponsorDetailsService iPatientSponsorDetailsService;
	@Autowired
	IBillingDetailsDao iBillingDetailsDao;
	@Autowired
	IOrderMasterDao iOrderMasterDao;
	@Autowired
	IBillingMasterDao iBillingMasterDao;
	@Autowired
	SessionService sessionService;
	@Autowired
	ITCompanyInvoiceCreationDao iTCompanyInvoiceCreationDao;
	@Autowired
	ITPeriodicityServiceOrdersService itPeriodicityServiceOrdersService;
	@Autowired
	ITPeriodicityServiceOrdersDao itPeriodicityServiceOrdersDao;
	@Autowired
	IWalkInSalesService iWalkInSalesService;
	@Autowired
	IWalkInSalesDao iWalkInSalesDao;
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	@Autowired
	IPatientRegistrationService patientRegistrationServiceImpl;
	@Autowired
	IMailingService iMailingService;
	@Autowired
	IPesaPalIntegrationDao pesaPalIntegrationDao;
	@Autowired
	IUnitServiceMasterDetailsService iUnitServiceMasterDetailsService;
	@Autowired
	IBillingDao iBillingDao;
	@Autowired
	IBillingService globalBillingService;
	@Autowired
	IConcessessionManagerDao iConcessessionManagerDao;
	@Autowired
    IpdChargesDao ipdChargesDao;
	@Autowired
	IPaymentDepositConsumptionDao iPaymentDepositConsumptionDao;
	@Autowired
	IPreAuthorizationRequest iPreAuthorizationRequest;
	
	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;

	@Autowired
	ICreditNoteMasterDao iCreditNoteMasterDao;
	
	@Autowired
	IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private Gson gson;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	IPatientRegistrationDao iPatientMasterDao;
	
	@Autowired
	IPatientCouponTemplateMapperService iPatientCouponTemplateMapperService;
	
	public static final String xmlFilePath = "C:\\Users\\admin\\Documents\\smartIntegration\\employee.xml";

	@Autowired
	private BillingCommonMethods billingCommonMethods;
	
	@Autowired
	IOrdeDetailsPackageMapperLogDao iOrdeDetailsPackageMapperLogDao;

	public UnitServiceTariffMasterDto getServiceBasePrice(BillingMasterDto billingMasterDto, List serviceList) {
		try {
			UnitServiceTariffMasterDto unitServiceTariffMasterDto = new UnitServiceTariffMasterDto();
			unitServiceTariffMasterDto.setOrganizationId(billingMasterDto.getOrganisationId());
			unitServiceTariffMasterDto.setUnitId(billingMasterDto.getUnitId());
			unitServiceTariffMasterDto.setVisitTypeId(OP);
			unitServiceTariffMasterDto.setBillingBedCategoryId(billingMasterDto.getBedTypeId());
			unitServiceTariffMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
			unitServiceTariffMasterDto.setPaymentEntitlementId(billingMasterDto.getPatientTypeId());
			unitServiceTariffMasterDto.setServiceList(serviceList);
			unitServiceTariffMasterDto.setOrderDate(billingMasterDto.getOrderDate());
			unitServiceTariffMasterDto.setTariffId(billingMasterDto.getTariffId());

			com.param.global.common.Response response = iUnitServiceTariffMasterService
					.getBasePriceOfServicesFromTariffMasterByServiceList(unitServiceTariffMasterDto);

			if (response.getStatus().equals(SUCCESS) && response.getObject() != null) {
				UnitServiceTariffMasterDto masterDto = (UnitServiceTariffMasterDto) response.getObject();
				if (masterDto != null) {
					return masterDto;
				}
			}
		} catch (Exception e) {
			
			throw e;
		}
		return null;
	}

	@Override
	@Transactional
	public Response updateBillSummaryAmount(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			billingMasterDao.updateBillSummaryAmount(billingMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getAutoRenderedServicesByPatient(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			// --------get Auto-Rendered Services---------//

			List<ServiceMasterDto> listServiceMasterDto = billingMasterDao
					.getAutoRenderedServicesByPatient(serviceMasterDto).getListObject();
			ListIterator<ServiceMasterDto> iterator = listServiceMasterDto.listIterator();
			if (serviceMasterDto.getPaymentEntitlementTypeId() == 1) {
				while (iterator.hasNext()) {
					ServiceMasterDto masterDto = iterator.next();

					UnitServiceTariffMasterDto unitServiceTariffMasterDto = new UnitServiceTariffMasterDto();
					unitServiceTariffMasterDto.setOrganizationId(serviceMasterDto.getOrganizationId());
					unitServiceTariffMasterDto.setUnitId(serviceMasterDto.getUnitId());
					unitServiceTariffMasterDto.setVisitTypeId(OP);
					unitServiceTariffMasterDto.setBillingBedCategoryId(serviceMasterDto.getBedCategoryId());
					unitServiceTariffMasterDto.setPatientTypeId(serviceMasterDto.getPatientTypeId());
					unitServiceTariffMasterDto.setPaymentEntitlementId(serviceMasterDto.getPaymentEntitlementTypeId());
					unitServiceTariffMasterDto.setServiceId(masterDto.getServiceMasterId());
					unitServiceTariffMasterDto.setOrderDate(masterDto.getOrderDateString());
					unitServiceTariffMasterDto.setTariffId(serviceMasterDto.getDefaultSelfTariffId());
					unitServiceTariffMasterDto = iUnitServiceTariffMasterService
							.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);

					if (unitServiceTariffMasterDto != null) {
						masterDto.setBasePrice(unitServiceTariffMasterDto.getRate());
					} else {
						return new Response<>(ERROR, COMMON_ERROR_CODE,
								"Cannot get service price as order date !> effective date !!", null, null);
					}
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getBillsByFilterForBillList(BillListReqDto reqDto) throws BillingException {
		try {
			return billingMasterDao.getBillsByFilterForBillList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getOrderedServicesByEncounterId(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			Response res = billingMasterDao.getOrderedServicesByEncounterId(serviceMasterDto);
			List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = res.getListObject();
			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList) 
			{
			Integer discountTypeId = billingMasterDao.getdiscountTypeIdBySaleId(orderDetailsByEncounterIdDto.getSaleId());
			orderDetailsByEncounterIdDto.setDiscountTypeId(discountTypeId);
			
			}
			List<Integer> orderDetailsIds = new LinkedList<>();
			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList) 
			{
				orderDetailsIds.add(orderDetailsByEncounterIdDto.getOrderDetailsId());
			}
			
			Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(orderDetailsIds,serviceMasterDto.getBillId());
			
			List<OrderDetailsPayeeMapperDto> list = res2.getListObject();
			//orderDetailsByEncounterIdDto.setOrderDetailsPayeeMapperDtosList(res2.getListObject());
			if(list!=null && list.size()>0) {
				//orderDetailsByEncounterIdDto.setIsTariffRateApplicable(list.get(0).getIsTariffRateApplicable());	
			}

			Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsIds);
			List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
			/*orderDetailsByEncounterIdDto.setOrderDetailsPackageMapperDtosList(mpprList);*/
			
			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList) 
			{
				Stream<OrderDetailsPayeeMapperDto> orderDetailsPayeeStream = list.stream();
				List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList = orderDetailsPayeeStream
						.filter(str -> str.getOrderDetailsId().equals(orderDetailsByEncounterIdDto.getOrderDetailsId()))
						.collect(Collectors.toList());
				
				Stream<OrderDetailsPackageMapperDto> orderDetailsPackageStream = mpprList.stream();
				List<OrderDetailsPackageMapperDto> orderDetailsPackageList = orderDetailsPackageStream
						.filter(str -> str.getOrderDetailsId().equals(orderDetailsByEncounterIdDto.getOrderDetailsId()))
						.collect(Collectors.toList());
				
				
				orderDetailsByEncounterIdDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeList);
				orderDetailsByEncounterIdDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageList);
				BigDecimal bd2 = new BigDecimal("0.00");
				if(orderDetailsByEncounterIdDto.getTaxAmt().equals(bd2))
				{
					orderDetailsByEncounterIdDto.setTaxName("-");
				}
				if(orderDetailsByEncounterIdDto.getTaxName()==null && !orderDetailsByEncounterIdDto.getTaxPercentage().equals(bd2))
				{
					orderDetailsByEncounterIdDto.setTaxName("T "+orderDetailsByEncounterIdDto.getTaxPercentage().intValue());
				}
				
				
			}
			
			
			return new Response<>(SUCCESS, null, null, orderDetailsByEncounterIdDtosList, null);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getOrderedServicesByEncounterIdForPreBilling(ServiceMasterDto serviceMasterDto)
			throws ApplicationException {
		try {
			Response res = billingMasterDao.getOrderedServicesByEncounterIdForPreBilling(serviceMasterDto);
			List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = res.getListObject();
			List<Integer> orderDetailsIds = orderDetailsByEncounterIdDtosList.stream().map(odts->odts.getOrderDetailsId()).collect(Collectors.toList());
			
			Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(orderDetailsIds,serviceMasterDto.getBillId());
			List<OrderDetailsPayeeMapperDto> list = res2.getListObject();

			Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsIds);
			List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
			
			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList) 
			{
				Stream<OrderDetailsPayeeMapperDto> orderDetailsPayeeStream = list.stream();
				List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList = orderDetailsPayeeStream
						.filter(str -> str.getOrderDetailsId().equals(orderDetailsByEncounterIdDto.getOrderDetailsId()))
						.collect(Collectors.toList());
				
				Stream<OrderDetailsPackageMapperDto> orderDetailsPackageStream = mpprList.stream();
				List<OrderDetailsPackageMapperDto> orderDetailsPackageList = orderDetailsPackageStream
						.filter(str -> str.getOrderDetailsId().equals(orderDetailsByEncounterIdDto.getOrderDetailsId()))
						.collect(Collectors.toList());
				
				orderDetailsByEncounterIdDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeList);
				orderDetailsByEncounterIdDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageList);
				
			}
			return new Response<>(SUCCESS, null, null, orderDetailsByEncounterIdDtosList, null);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getRenderedServicesByEncounterIdForBillSave(Integer encounterId, Integer visitTypeId,
			boolean debitNoteService, Integer unitId, Set<Integer> orderDetailsIdsList, Character isPreBilling)
			throws ApplicationException {
		try {
			List<BillPayeeDetailsDto> billPayeeDetailsDtosFinalList = new LinkedList<>();
			Response res = billingMasterDao.getRenderedServicesByEncounterIdForBillSave(encounterId, visitTypeId,debitNoteService, isPreBilling);
			List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = res.getListObject();
			List<Integer> orderDetailsIds = new LinkedList<>();
			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList) 
			{
				orderDetailsIds.add(orderDetailsByEncounterIdDto.getOrderDetailsId());
			}
			Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(orderDetailsIds,0);
			List<OrderDetailsPayeeMapperDto> list = res2.getListObject();

			Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsIds);
			List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();

			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList) 
			{
				Stream<OrderDetailsPayeeMapperDto> orderDetailsPayeeStream = list.stream();
				List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList = orderDetailsPayeeStream
						.filter(str -> str.getOrderDetailsId().equals(orderDetailsByEncounterIdDto.getOrderDetailsId()))
						.collect(Collectors.toList());
				
				Stream<OrderDetailsPackageMapperDto> orderDetailsPackageStream = mpprList.stream();
				List<OrderDetailsPackageMapperDto> orderDetailsPackageList = orderDetailsPackageStream
						.filter(str -> str.getOrderDetailsId().equals(orderDetailsByEncounterIdDto.getOrderDetailsId()))
						.collect(Collectors.toList());
				
				orderDetailsByEncounterIdDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeList);
				orderDetailsByEncounterIdDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageList);
				
			}
			
			
			if (visitTypeId == 1) 
			{
				BillPayeeDetailsDto selfPayeeDetailsDto = billingMasterDao.getSelfPayeeAmount(encounterId, unitId,debitNoteService);
				List<BillPayeeDetailsDto> creditPayeeDetailsDtosList = billingMasterDao.getCreditPayeeAmount(encounterId, unitId, debitNoteService);

				if (selfPayeeDetailsDto != null)
					billPayeeDetailsDtosFinalList.add(selfPayeeDetailsDto);

				if (creditPayeeDetailsDtosList != null && creditPayeeDetailsDtosList.size() > 0) 
				{
					for (BillPayeeDetailsDto billPayeeDetailsDto : creditPayeeDetailsDtosList) 
					{
						billPayeeDetailsDtosFinalList.add(billPayeeDetailsDto);
					}
				}
			}
			if (visitTypeId != 1) 
			{
				BillPayeeDetailsDto selfPayeeDetailsDto = billingMasterDao.getSelfPayeeAmountForAdmission(encounterId,
						unitId, debitNoteService, orderDetailsIdsList);
				List<BillPayeeDetailsDto> creditPayeeDetailsDtosList = billingMasterDao.getCreditPayeeAmountForAdmission(encounterId, unitId, debitNoteService, orderDetailsIdsList);

				if (selfPayeeDetailsDto != null)
					billPayeeDetailsDtosFinalList.add(selfPayeeDetailsDto);

				if (creditPayeeDetailsDtosList != null && creditPayeeDetailsDtosList.size() > 0) {
					for (BillPayeeDetailsDto billPayeeDetailsDto : creditPayeeDetailsDtosList) {
						billPayeeDetailsDtosFinalList.add(billPayeeDetailsDto);
					}
				}
			}

			return new Response<>(SUCCESS, null, null, orderDetailsByEncounterIdDtosList,
					billPayeeDetailsDtosFinalList);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveBillService(BillingMasterDto billingMasterDto) throws Exception {
		try {
			return saveBill(billingMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveIntrimBill(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {

			List<OrderDetailsMasterDto> listToBeAdded = new LinkedList<>();
			if (billingMasterDto.getOrderDetailsMasterDtosList() != null
					&& billingMasterDto.getOrderDetailsMasterDtosList().size() > 0) {
				OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setPatientId(billingMasterDto.getPatientId());
				orderMasterDto.setOrderId(billingMasterDto.getOrderId());
				orderMasterDto.setEncounterId(billingMasterDto.getEncounterId());
				orderMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				orderMasterDto.setCreatedBy(billingMasterDto.getCreatedBy());
				orderMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
				orderMasterDto.setCreatedDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
				orderMasterDto.setOrderDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setPriorityId(2);
				orderMasterDto.setOrgId(billingMasterDto.getOrganisationId());
				orderMasterDto.setOrgUnitId(billingMasterDto.getUnitId());
				orderMasterDto.setOrderStatusId(1);// order status==1
				orderMasterDto.setDoctorId(billingMasterDto.getOrdDoctorId());
				orderMasterDto.setListOrderDetailsMasterDto(listToBeAdded);
				orderMasterDto.setAdmissionId(billingMasterDto.getAdmissionId());
				orderMasterDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
				orderMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
				orderMasterDto.setPaymentEntitlementTypeId(billingMasterDto.getPaymentEntitlementId());
				orderMasterDto.setNetAmount(BigDecimal.valueOf(billingMasterDto.getNetAmount()));
				orderMasterDto.setReqDisAmt(billingMasterDto.getReqDisAmt());
				orderMasterDto.setDiscType(billingMasterDto.getDiscType());
				orderMasterDto.setRequestSendTo(billingMasterDto.getRequestSentTo());
				orderMasterDto.setListOrderDetailsMasterDto(billingMasterDto.getOrderDetailsMasterDtosList());
				return iIntrimBillService.saveIntrimBill(orderMasterDto);
			}
		} catch (Exception e) {
			
			throw e;
		}

		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveBill(BillingMasterDto billingMasterDto) {
		try {
			Integer pharmacyServiceId = null;
			List<Integer> pharmacyServiceOrderDetailsIds =new ArrayList<Integer>();
			Integer appointmentId = null;
			Character isNursingAssessmentRequired = null;
			Integer encounterId = null;
			BigDecimal selfDueAmount = BigDecimal.ZERO;
			BigDecimal creditDueAmount = BigDecimal.ZERO;
			Character isPrePostBilling = null;
			Integer billPayeeDetailsId = null;
			Integer selfBillPayeeDetailsId = null;
			Integer selfPayeeId = null;
			BigDecimal requestTotalAmt = BigDecimal.ZERO;
			OrderMasterDto orderMasterDto = null;
			Character isAppoinmentRequired = null;
			Integer radiologySpecialityId = null;
			List<Integer> tempServiceIdList=new ArrayList<Integer>();
            Set<Integer> serviceIdList=new LinkedHashSet<Integer>();
			boolean debitNoteService = billingMasterDto.getIsDrNote() == 'Y' ? true : false;
			
			if (billingMasterDto.getVisitTypeId() != null && billingMasterDto.getVisitTypeId().intValue()==OP)
				billingMasterDto.setIsIntermediateBill('N');
			
			List<OrderDetailsMasterDto> lineLevelFinal = new ArrayList<>();
			List<Integer> pharmacyOrderIdList = new LinkedList<>();
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(billingMasterDto.getOrganisationId());
				unitApplicationConfigurationDto.setUnitId(billingMasterDto.getUnitId());
			Response configRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if (configRes.getObject() != null) 
			{
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) configRes.getObject();
				pharmacyServiceId = unitApplicationConfigurationDto.getPharmacyServiceId();
				isPrePostBilling = unitApplicationConfigurationDto.getIsPreOrPostBilling();
				selfPayeeId = unitApplicationConfigurationDto.getSelfPayeeId();
				isAppoinmentRequired=unitApplicationConfigurationDto.getIsAppointmentRequired();
				radiologySpecialityId=unitApplicationConfigurationDto.getRadiologySpecialityId();
				isNursingAssessmentRequired = unitApplicationConfigurationDto.getIsNursingAssessment();
			}
			
			List<OrderDetailsMasterDto> listToBeAdded = new ArrayList<>();
			List<OrderDetailsMasterDto> listToBeUpdated = new ArrayList<>();
			Set<Integer> orderDetailsIdList = new LinkedHashSet<>();
			Set<Integer> orderDetailsIdListForUpdateIsBilledFlag = new LinkedHashSet<>();
			Integer reqDisId = null;
			if (billingMasterDto.getOrderDetailsMasterDtosList() != null && billingMasterDto.getOrderDetailsMasterDtosList().size() > 0) 
			{
				ListIterator<OrderDetailsMasterDto> iterator = billingMasterDto.getOrderDetailsMasterDtosList().listIterator();
				while (iterator.hasNext()) 
				{
					OrderDetailsMasterDto detailsByEncounterIdDto = iterator.next();
						detailsByEncounterIdDto.setServiceAmt(detailsByEncounterIdDto.getApportionedPrice() != null && detailsByEncounterIdDto.getApportionedPrice().intValue()>0
								? detailsByEncounterIdDto.getApportionedPrice()
								: detailsByEncounterIdDto.getServiceAmt());
						detailsByEncounterIdDto.setOrdTotalAmt(detailsByEncounterIdDto.getApportionedPrice() != null && detailsByEncounterIdDto.getApportionedPrice().intValue()>0
								? detailsByEncounterIdDto.getApportionedPrice()
								: detailsByEncounterIdDto.getOrdTotalAmt());
						detailsByEncounterIdDto.setEncounterId(billingMasterDto.getEncounterId());
						detailsByEncounterIdDto.setAdmissionId(billingMasterDto.getAdmissionId());

					if(tempServiceIdList.contains(detailsByEncounterIdDto.getServiceId())) 
					{
						serviceIdList.add(detailsByEncounterIdDto.getServiceId());
					}else {
						tempServiceIdList.add(detailsByEncounterIdDto.getServiceId());
					}
					
					if (detailsByEncounterIdDto.getIsDiscountApplicable() != null
							&& detailsByEncounterIdDto.getIsDiscountApplicable() == 'Y') 
					{
						requestTotalAmt = requestTotalAmt.add(detailsByEncounterIdDto.getOrdTotalAmt()
								.subtract(detailsByEncounterIdDto.getOrdConcession()));
					}
					if(isAppoinmentRequired!=null && isAppoinmentRequired=='Y' && radiologySpecialityId!=null && radiologySpecialityId==detailsByEncounterIdDto.getDepartmentId()) {
						UnitServiceMasterDetailsDto dto=new UnitServiceMasterDetailsDto();
						dto.setServiceMasterId(detailsByEncounterIdDto.getServiceId());
						dto.setUnitId(billingMasterDto.getUnitId());
						Response res=iUnitServiceMasterDetailsService.getUnitServiceMasterDetailsByServiceId(dto);
						if(res.getObject()!=null && res.getStatus().equals(SUCCESS)) 
						{
							UnitServiceMasterDetailsDto resDto=(UnitServiceMasterDetailsDto)res.getObject();
							detailsByEncounterIdDto.setIsAppointableRadiology(resDto.getIsAppointable());
						}
					}
					if (detailsByEncounterIdDto.getOrderId() == null)
						listToBeAdded.add(detailsByEncounterIdDto);
					else {
						detailsByEncounterIdDto.setTariffId(detailsByEncounterIdDto.getTariffId()!=null ? detailsByEncounterIdDto.getTariffId() : unitApplicationConfigurationDto.getDefaultSelfTariffId());
						orderDetailsIdList.add(detailsByEncounterIdDto.getOrderDetailsId());
						listToBeUpdated.add(detailsByEncounterIdDto);
					}
					
							iBillingMasterDao.updatePharmacy(detailsByEncounterIdDto,detailsByEncounterIdDto.getOrderDetailsId(),billingMasterDto.getPaymentEntitlementId(),billingMasterDto.getPayeeId());
						
				}
				if(billingMasterDto.getIsFinalBill() == 'Y' || billingMasterDto.getIsIntermediateBill() == 'Y')
				{
					Integer count = iOrderDetailsMasterDao.checkIsBilledServices(orderDetailsIdList);
					if(count==1)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Bill is already generated !", null, null);
					}
					
					if(billingMasterDto.getIsFinalBill() == 'Y' && !billingMasterDto.getIsBillManipulationAllowed())
					{
						boolean isbillAmountsMatched = billingMasterDao.matchBillAmounts(billingMasterDto,orderDetailsIdList);
						if(!isbillAmountsMatched)
							return new Response<>(ERROR, ALREADY_EXIST_CODE, "Dispute in orders count, refreshing orders !", null, (!isbillAmountsMatched));
					}
				}
				
				
				
			}
			if (billingMasterDto.getCancelledDiscountsList() != null
					&& billingMasterDto.getCancelledDiscountsList().size() > 0)
				iOrderDetailsDiscountService.saveCancelledDiscounts(billingMasterDto.getCancelledDiscountsList());

			if (billingMasterDto.getIsDiscountApplied() != null && billingMasterDto.getIsDiscountApplied() == 'Y'
					&& billingMasterDto.getReqDiscId() != null) {
				iRequestDiscountDao.approveAndApply(billingMasterDto.getReqDiscId());
			}

			// <-----Request for discount approval
			if (billingMasterDto.getReqDisAmt() != null	&& billingMasterDto.getReqDisAmt().compareTo(new BigDecimal("0")) == 1) 
			{
				BigDecimal amt = BigDecimal.ZERO;
				RequestDiscountDto requestDiscountDto = new RequestDiscountDto();
					requestDiscountDto.setOrganizationId(billingMasterDto.getOrganisationId());
					requestDiscountDto.setUnitId(billingMasterDto.getUnitId());
					requestDiscountDto.setRequestDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
					requestDiscountDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
					requestDiscountDto.setEncounterId(billingMasterDto.getEncounterId());
					requestDiscountDto.setAdmissionId(billingMasterDto.getAdmissionId());
					requestDiscountDto.setPatientId(billingMasterDto.getPatientId());
					if (billingMasterDto.getLineLevelDiscountOrderList().size() == 0)
						requestDiscountDto.setNetAmount(requestTotalAmt);
					else {
						for (OrderDetailsMasterDto dto : billingMasterDto.getLineLevelDiscountOrderList()) {
							amt = amt.add(dto.getOrdTotalAmt().subtract(dto.getOrdConcession()));
						}
						requestDiscountDto.setNetAmount(amt);
					}
	
					requestDiscountDto.setReqDisAmt(billingMasterDto.getReqDisAmt());
					requestDiscountDto.setDiscType(billingMasterDto.getDiscType());
					requestDiscountDto.setRequestSentTo(billingMasterDto.getRequestSentTo());
					requestDiscountDto.setRequestSentBy(billingMasterDto.getCreatedBy());
					requestDiscountDto.setRequestNumber(generateRequestNumber());
					requestDiscountDto.setCreatedBy(billingMasterDto.getCreatedBy());
					requestDiscountDto.setUpdatedBy(billingMasterDto.getCreatedBy());
					requestDiscountDto.setApprovedDiscAmt(new BigDecimal("0.00"));
					requestDiscountDto.setReqStatus(1); // pending
					requestDiscountDto.setRemark(billingMasterDto.getRemark());
					requestDiscountDto.setRequestType(debitNoteService == true ? 4 : 1); // For Discount Request
					requestDiscountDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
					requestDiscountDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
				Response resDis = iRequestDiscountDao.requestDiscount(requestDiscountDto);
				reqDisId = (Integer) resDis.getObject();
			}
			// ----->

			// <-----Request for financial clearance
            if (billingMasterDto.getIsFinancialClearance().equals('Y') && (billingMasterDto.getFinancialRequestSendTo()!=null && billingMasterDto.getFinancialRequestSendTo()>0)) {

				RequestDiscountDto requestDiscountDto = new RequestDiscountDto();
					requestDiscountDto.setOrganizationId(billingMasterDto.getOrganisationId());
					requestDiscountDto.setUnitId(billingMasterDto.getUnitId());
					requestDiscountDto.setRequestDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
					requestDiscountDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
					requestDiscountDto.setEncounterId(billingMasterDto.getEncounterId());
					requestDiscountDto.setAdmissionId(billingMasterDto.getAdmissionId());
					requestDiscountDto.setPatientId(billingMasterDto.getPatientId());
					requestDiscountDto.setDiscType(billingMasterDto.getDiscType());
					requestDiscountDto.setRequestSentTo(billingMasterDto.getFinancialRequestSendTo());
					requestDiscountDto.setRequestSentBy(billingMasterDto.getCreatedBy());
					requestDiscountDto.setRequestNumber(generateRequestNumber());
					requestDiscountDto.setCreatedBy(billingMasterDto.getCreatedBy());
					requestDiscountDto.setUpdatedBy(billingMasterDto.getCreatedBy());
					requestDiscountDto.setReqStatus(1); // pending
					requestDiscountDto.setRequestType(2); // For financial clearance.
					requestDiscountDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
					requestDiscountDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
				Response resDis = iRequestDiscountDao.requestDiscount(requestDiscountDto);
				reqDisId = (Integer) resDis.getObject();
            }else {
                if (billingMasterDto.getIsFinancialClearance().equals('Y') && billingMasterDto.getVisitTypeId()!=OP)
                {  
                    iRequestDiscountDao.updateAdmissionsIsFinancialClearance(billingMasterDto.getAdmissionId());
                }else if(billingMasterDto.getIsFinancialClearance().equals('Y') && billingMasterDto.getVisitTypeId()==OP)
                {
                	iRequestDiscountDao.updateEncounterIsFinancialClearance(billingMasterDto.getEncounterId());
                }
			}
			// ----->

			orderMasterDto = new OrderMasterDto();
				orderMasterDto.setPatientId(billingMasterDto.getPatientId());
				orderMasterDto.setEncounterId(billingMasterDto.getEncounterId());
				orderMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				orderMasterDto.setCreatedBy(billingMasterDto.getCreatedBy());
				orderMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
				orderMasterDto.setCreatedDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
				orderMasterDto.setOrderDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setPriorityId(2);
				orderMasterDto.setOrgId(billingMasterDto.getOrganisationId());
				orderMasterDto.setOrgUnitId(billingMasterDto.getUnitId());
				orderMasterDto.setOrderStatusId(1);// order status==1
				orderMasterDto.setDoctorId(billingMasterDto.getOrdDoctorId());
				orderMasterDto.setListOrderDetailsMasterDto(listToBeAdded);
				orderMasterDto.setAdmissionId(billingMasterDto.getAdmissionId());
				orderMasterDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
				orderMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
				orderMasterDto.setPaymentEntitlementTypeId(billingMasterDto.getPaymentEntitlementId());
				orderMasterDto.setNetAmount(BigDecimal.valueOf(billingMasterDto.getNetAmount()));
				orderMasterDto.setReqDisAmt(billingMasterDto.getReqDisAmt());
				orderMasterDto.setDiscType(billingMasterDto.getDiscType());
				orderMasterDto.setRequestSendTo(billingMasterDto.getRequestSentTo());
				orderMasterDto.setReferralId(billingMasterDto.getReferralId());
				orderMasterDto.setLineLevelDiscountOrderList(billingMasterDto.getLineLevelDiscountOrderList());
				orderMasterDto.setHomeCollection(billingMasterDto.isHomeCollection());
				orderMasterDto.setSelfPayeeId(selfPayeeId);
				
			if (listToBeAdded != null && listToBeAdded.size() > 0) 
			{
				Response orderRes = iOrderMasterService.saveOrderMasterWithDiscount(orderMasterDto, reqDisId);
				orderDetailsIdList.addAll(orderRes.getListObject());
				lineLevelFinal = (List<OrderDetailsMasterDto>) orderRes.getResObject();
				savePackageConsumption(billingMasterDto, listToBeAdded, orderRes);
			}
			
			// Updating services in order details
			if (listToBeUpdated != null && listToBeUpdated.size() > 0)
			{
				char updateSevices = ((billingMasterDto.getIsFinalBill() == 'Y' || billingMasterDto.getIsIntermediateBill() == 'Y') && !billingMasterDto.isSaveFromSplitBill()) ? 'Y' : 'N';
				iOrderDetailsMasterService.updateOrderDetailsWithDiscount(listToBeUpdated, reqDisId,(billingMasterDto.getLineLevelDiscountOrderList()!=null)?billingMasterDto.getLineLevelDiscountOrderList().size():null, orderMasterDto,updateSevices);
			}else {
				
				ListIterator<OrderDetailsMasterDto> iterator = 	listToBeUpdated.listIterator();
				List<Integer> orderIds = new LinkedList<Integer>();
				while(iterator.hasNext())
				{
					OrderDetailsMasterDto detailsMasterDto=iterator.next();
					orderIds.add(detailsMasterDto.getOrderId());
				}
				
				if(orderMasterDto.getEncounterId()!=null && orderIds.size()>0)
					iOrderMasterService.updateIsHomeCollection(orderIds,orderMasterDto.isHomeCollection());
			}

			if (lineLevelFinal.size() == 0) {
				lineLevelFinal = billingMasterDto.getLineLevelDiscountOrderList();
			}
			if (lineLevelFinal != null && lineLevelFinal.size() > 0) {
				for (OrderDetailsMasterDto orderDetailsMasterDtoLineLevel : lineLevelFinal) {
					RequestDiscountDetailsDto requestDiscountDetailsDto = new RequestDiscountDetailsDto();
					if (orderDetailsMasterDtoLineLevel.getOrderDetailsId() > 0) 
					{
						requestDiscountDetailsDto.setReqDiscId(reqDisId);
						requestDiscountDetailsDto.setOrderId(orderDetailsMasterDtoLineLevel.getOrderId());
						requestDiscountDetailsDto.setOrderDetailsId(orderDetailsMasterDtoLineLevel.getOrderDetailsId());
						requestDiscountDetailsDto.setServiceId(orderDetailsMasterDtoLineLevel.getServiceId());
						requestDiscountDetailsDto.setNetAmount(orderDetailsMasterDtoLineLevel.getOrdTotalAmt().subtract(orderDetailsMasterDtoLineLevel.getOrdConcession()));
						requestDiscountDetailsDto.setReqDisAmt(orderDetailsMasterDtoLineLevel.getReqDisAmt());
						requestDiscountDetailsDto.setApprovedDiscAmt(new BigDecimal("0.00"));
						requestDiscountDetailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
						requestDiscountDetailsDto.setUpdatedBy(billingMasterDto.getCreatedBy());
						requestDiscountDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "yyy-MM-dd HH:mm:ss"));
						requestDiscountDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "yyy-MM-dd HH:mm:ss"));
						requestDiscountDetailsDto.setOrganizationId(billingMasterDto.getOrganisationId());
						requestDiscountDetailsDto.setUnitId(billingMasterDto.getUnitId());
						iRequestDiscountDao.requestDiscountDetails(requestDiscountDetailsDto);
					}
				}
			}
			if (billingMasterDto.getReqDiscId() != null)
				iRequestDiscountDao.approvedWithoutBill(billingMasterDto.getReqDiscId());

			Response resOrderDetUpdate=iOrderDetailsMasterDao.updateOrderDetail(tempServiceIdList,serviceIdList,orderDetailsIdList,billingMasterDto.getUnitId());
			
			if (billingMasterDto.getIsFinalBill() == 'Y' || billingMasterDto.getIsIntermediateBill() == 'Y') 
			{

				Response res = getRenderedServicesByEncounterIdForBillSave(billingMasterDto.getEncounterId() != null ? billingMasterDto.getEncounterId(): billingMasterDto.getAdmissionId(),
						billingMasterDto.getVisitTypeId(), 
						debitNoteService, 
						billingMasterDto.getUnitId(),
						orderDetailsIdList, 
						isPrePostBilling);
				List<OrderDetailsByEncounterIdDto> listForSavingBill = res.getListObject();
				if (listForSavingBill.size() > 0) 
				{
					
					Response sponserRes = iPatientSponsorDetailsDao.getSponsoredDetailsForUpdatePayee(
							billingMasterDto.getAdmissionId(),billingMasterDto.getEncounterId());

					List<PatientSponsorDetailsDto> sponserList = sponserRes.getListObject();
					PatientSponsorDetailsDto sponsorStramObj = null;
					sponsorStramObj = sponserList.stream().filter(sponsor -> sponsor.getPriorityId() == 1).findAny().orElse(null);

					if(billingMasterDto.getIsAddendumBill()!=null && billingMasterDto.getIsAddendumBill()=='Y' && billingMasterDto.getAgainstBillId()!=null)
					{
						BillingMaster forEditBill = billingMasterDao.findById(billingMasterDto.getAgainstBillId());
						billingMasterDto.setBillNumber(forEditBill.getBillNumber());
						billingMasterDto.setBillDateTime(forEditBill.getBillDateTime());
					}else{
						//generateBillNumberCall
	                    billingMasterDto.setBillNumber((billingMasterDto.getIsDrNote() == 'Y') ? getDebitNoteNumber(billingMasterDto.getUnitId()): getBillNumber(billingMasterDto.getUnitId(),sponsorStramObj.getPaymentEntitlementId(),billingMasterDto.getVisitTypeId()));
	                    billingMasterDto.setBillDateTime(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
					}
					billingMasterDto.setIsInterimOrFinalBill(billingMasterDto.getIsFinalBill() == 'Y' ? 2 : 1); // 1-Intermediate bill
					billingMasterDto.setOutstanding(billingMasterDto.getNetAmount());
					billingMasterDto.setIsInvoiceCreation('N');
					billingMasterDto.setIsDoctorReferralPosted('N');
					Response billRes = billingMasterDao.saveBillingMaster(billingMasterDto);
					Integer billMasterId = (Integer) billRes.getObject();
					
					ListIterator<OrderDetailsByEncounterIdDto> listIterator = listForSavingBill.listIterator();
					
					List<BillingDetailsDto> billingDetailsDtoList = new LinkedList<>();
					
					while (listIterator.hasNext()) 
					{
						OrderDetailsByEncounterIdDto detailsByEncounterIdDto = listIterator.next();
						
						if (billingMasterDto.getVisitTypeId() == OP || (billingMasterDto.getVisitTypeId() != OP
								&& orderDetailsIdList.contains(detailsByEncounterIdDto.getOrderDetailsId()))) 
						{
							encounterId = detailsByEncounterIdDto.getEncounterId();
							appointmentId = detailsByEncounterIdDto.getAppointmentId();
							if ((detailsByEncounterIdDto.getOrdCancelled() != null	&& !detailsByEncounterIdDto.getOrdCancelled().equals('Y'))
									&& (detailsByEncounterIdDto.getStatus() != null	&& detailsByEncounterIdDto.getStatus().equals('A'))) 
							{
								BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
									billingDetailsDto.setBillingMasterId(billMasterId);
									billingDetailsDto.setServiceId(detailsByEncounterIdDto.getServiceMasterId());
									billingDetailsDto.setRate(detailsByEncounterIdDto.getBasePrice() != null ? Double.valueOf(detailsByEncounterIdDto.getBasePrice().doubleValue())	: 0.00);
									billingDetailsDto.setQuantity(detailsByEncounterIdDto.getQuantity() != null	? Double.valueOf(detailsByEncounterIdDto.getQuantity().doubleValue()) : 0.00);
									billingDetailsDto.setConcession(detailsByEncounterIdDto.getConcession() != null	? Double.valueOf(detailsByEncounterIdDto.getConcession().doubleValue())	: 0.00);
									billingDetailsDto.setGrossAmount(detailsByEncounterIdDto.getTotalAmt() != null	? Double.valueOf(detailsByEncounterIdDto.getTotalAmt().doubleValue()): 0.00);
									billingDetailsDto.setCoPayPercentage(detailsByEncounterIdDto.getCoPayPer() != null? Double.valueOf(detailsByEncounterIdDto.getCoPayPer().doubleValue())	: Double.valueOf(0.00)); 
									billingDetailsDto.setUnitId(detailsByEncounterIdDto.getOrgUnitId());
									billingDetailsDto.setOrganizationId(detailsByEncounterIdDto.getOrgId());
									billingDetailsDto.setStatus('A');
									billingDetailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
									billingDetailsDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
									billingDetailsDto.setCreatedDate(billingMasterDto.getCreatedDate());
									billingDetailsDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
									billingDetailsDto.setDiscount(detailsByEncounterIdDto.getDiscount() != null	? Double.valueOf(detailsByEncounterIdDto.getDiscount().doubleValue()) : 0.00);
									billingDetailsDto.setTaxId(detailsByEncounterIdDto.getTaxId());
									billingDetailsDto.setTaxPer(detailsByEncounterIdDto.getTaxPercentage() != null ? Double.valueOf(detailsByEncounterIdDto.getTaxPercentage().doubleValue()) : 0.00);
									billingDetailsDto.setTaxAmount(detailsByEncounterIdDto.getTaxAmt() != null ? Double.valueOf(detailsByEncounterIdDto.getTaxAmt().doubleValue()): 0.00);
									billingDetailsDto.setNetAmt(detailsByEncounterIdDto.getNetPay() != null	? Double.valueOf(detailsByEncounterIdDto.getNetPay().doubleValue())	: 0.00);
									billingDetailsDto.setOrdDocId(detailsByEncounterIdDto.getDoctorId());
									billingDetailsDto.setOrdDocSplId(detailsByEncounterIdDto.getDoctorSpecialityId()!=null && detailsByEncounterIdDto.getDoctorSpecialityId().intValue()>0 ? detailsByEncounterIdDto.getDoctorSpecialityId(): detailsByEncounterIdDto.getSpecialityId());
									billingDetailsDto.setDrugId(detailsByEncounterIdDto.getDrugId());
									billingDetailsDto.setBatchId(detailsByEncounterIdDto.getBatchId());
									// billingDetailsDto.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(detailsByEncounterIdDto.getOrderDateString()));
									// billingDetailsDto.setOrderSchDate(detailsByEncounterIdDto.getOrderSchDate());
									billingDetailsDto.setSelfPayable(detailsByEncounterIdDto.getSelfPayable() != null ? Double.valueOf(detailsByEncounterIdDto.getSelfPayable().doubleValue()): 0.00);
									selfDueAmount = detailsByEncounterIdDto.getSelfPayable();
									billingDetailsDto.setCreditPayable(detailsByEncounterIdDto.getCreditPayable() != null ? Double.valueOf(detailsByEncounterIdDto.getCreditPayable().doubleValue()): 0.00);
									creditDueAmount = creditDueAmount.add(detailsByEncounterIdDto.getCreditPayable());
									billingDetailsDto.setPayeeId(detailsByEncounterIdDto.getPayeeId());
									billingDetailsDto.setOrderDetailsId(detailsByEncounterIdDto.getOrderDetailsId());
									billingDetailsDto.setPackageId(detailsByEncounterIdDto.getPackageId());
									billingDetailsDto.setPackageConsumptionAmt(detailsByEncounterIdDto.getPackageConsumptionAmt() != null? Double.valueOf(detailsByEncounterIdDto.getPackageConsumptionAmt().doubleValue()): Double.valueOf(0.00));
                                    billingDetailsDto.setCoSharePer(billingDetailsDto.getSelfPayable()>0 ? (billingDetailsDto.getSelfPayable()/billingDetailsDto.getNetAmt())*100 : Double.valueOf(0.00));
									billingDetailsDto.setIsDoctorSharePosted('N');
									billingDetailsDto.setOrderDetailsPayeeMapperDtosList(detailsByEncounterIdDto.getOrderDetailsPayeeMapperDtosList());
									billingDetailsDto.setOrderDetailsPackageMapperDtosList(detailsByEncounterIdDto.getOrderDetailsPackageMapperDtosList());
									if (detailsByEncounterIdDto.getServiceMasterId().equals(pharmacyServiceId)) 
									{
										pharmacyOrderIdList.add(detailsByEncounterIdDto.getOrderId());
										pharmacyServiceOrderDetailsIds.add(detailsByEncounterIdDto.getOrderDetailsId());
									}
									orderDetailsIdListForUpdateIsBilledFlag.add(detailsByEncounterIdDto.getOrderDetailsId());
								//iBillingDetailsService.saveBillingDetails(billingDetailsDto);
								
								billingDetailsDtoList.add(billingDetailsDto);
								
							} else {
								orderDetailsIdListForUpdateIsBilledFlag.add(detailsByEncounterIdDto.getOrderDetailsId());
							}
						}
					}
					if(billingDetailsDtoList.size()>0)
						iBillingDetailsService.saveBillingDetails(billingDetailsDtoList);
					
					if (orderDetailsIdListForUpdateIsBilledFlag != null	&& orderDetailsIdListForUpdateIsBilledFlag.size() > 0)
						iOrderDetailsMasterService.updateIsBilledStatus(orderDetailsIdListForUpdateIsBilledFlag,billMasterId, pharmacyOrderIdList);

					
					
					ListIterator<BillPayeeDetailsDto> iterator2 = ((List) res.getObject()).listIterator();
					while (iterator2.hasNext()) 
					{
						BillPayeeDetailsDto billPayeeDetailsReqDto = iterator2.next();
						
							BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();
								billPayeeDetailsDto.setBillId(billMasterId);
								billPayeeDetailsDto.setStatus(ACTIVE);
								billPayeeDetailsDto.setPayeeId(billPayeeDetailsReqDto.getPayeeId());
								billPayeeDetailsDto.setAssociateCompanyId(billPayeeDetailsReqDto.getAssociateCompanyId());
								billPayeeDetailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
								billPayeeDetailsDto.setUpdatedBy(billingMasterDto.getCreatedBy());
								billPayeeDetailsDto.setCreatedDate(billingMasterDto.getCreatedDate());
								billPayeeDetailsDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
                                billPayeeDetailsDto.setBillAmount(!billPayeeDetailsReqDto.getPayeeId().equals(selfPayeeId) ? billPayeeDetailsReqDto.getBillAmount() : billingMasterDto.getSelfPayable());
                                billPayeeDetailsDto.setDueAmount(!billPayeeDetailsReqDto.getPayeeId().equals(selfPayeeId) ? billPayeeDetailsReqDto.getBillAmount() : billingMasterDto.getSelfPayable());
								billPayeeDetailsDto.setOrgId(billingMasterDto.getOrganisationId());
								billPayeeDetailsDto.setUnitId(billingMasterDto.getUnitId());
								billPayeeDetailsDto.setPaidAmount(0.00);
								billPayeeDetailsDto.setCrNetAmount(0.00);
								sponsorStramObj = new PatientSponsorDetailsDto();
								if(billPayeeDetailsReqDto.getAssociateCompanyId()!=null){
									sponsorStramObj = sponserList.stream()
											.filter(sponsor -> sponsor.getPayeeId().equals(billPayeeDetailsReqDto.getPayeeId()) && (billPayeeDetailsReqDto.getAssociateCompanyId()!=null && billPayeeDetailsReqDto.getAssociateCompanyId().equals(sponsor.getInsuranceId())) )
											.findAny().orElse(null);
								}else{
									sponsorStramObj = sponserList.stream()
											.filter(sponsor -> sponsor.getPayeeId().equals(billPayeeDetailsReqDto.getPayeeId()))
											.findAny().orElse(null);
								}
								
								if(sponsorStramObj!=null)
								{
									billPayeeDetailsDto.setPriorityId(sponsorStramObj.getPriorityId()!=null ? sponsorStramObj.getPriorityId():null);
									billPayeeDetailsDto.setPaymentEntitlementId(sponsorStramObj.getPaymentEntitlementId());
								}
								
								if(billingMasterDto.getSelfPayable()==0 && billPayeeDetailsDto.getPriorityId()==1 && billingMasterDto.getRoundOff()!=0)
								{
									billPayeeDetailsDto.setBillAmount(billPayeeDetailsReqDto.getBillAmount()-billingMasterDto.getRoundOff());
									billPayeeDetailsDto.setDueAmount(billPayeeDetailsReqDto.getBillAmount()-billingMasterDto.getRoundOff());
								}
								
						Response billPayeeDtls = iBillPayeeDetailsService.saveBillPayeeDetails(billPayeeDetailsDto);
						billPayeeDetailsId = (Integer) billPayeeDtls.getObject();
						
						if(billPayeeDetailsReqDto.getPayeeId().equals(selfPayeeId))
							selfBillPayeeDetailsId = billPayeeDetailsId;
					}

					// save bill status logs
					BillingStatusMapperDto mapperDto = new BillingStatusMapperDto();
					BillingStatusMapperId mapperId = new BillingStatusMapperId();
					mapperId.setBillId(billMasterId);

					if (billingMasterDto.getNetAmount().intValue() == 0)
						mapperId.setBillStatusId(SETTLED);

					else if ((billingMasterDto.getSelfPayable().equals(0.00) && (billingMasterDto.getCreditPayable() > 0.00)))
						mapperId.setBillStatusId(ONHOLD);
					
					else if(billingMasterDto.getIsFinancialClearance()=='Y')
						mapperId.setBillStatusId(ONHOLD);
					
					else
						mapperId.setBillStatusId(PENDING);

					mapperDto.setBillingStatusMapperId(mapperId);
					mapperDto.setCreatedBy(billingMasterDto.getCreatedBy());
					mapperDto.setOrgnisationId(billingMasterDto.getOrganisationId());
					mapperDto.setUnitId(billingMasterDto.getUnitId());
					mapperDto.setStatus(ACTIVE);
					iBillStatusMapperDao.saveBillStatus(mapperDto);

					if (billingMasterDto.getReqDiscId() != null || reqDisId != null)
						iRequestDiscountDao.updateBillIdToRequestDiscountDetails(billMasterId,
								billingMasterDto.getReqDiscId()!=null ? billingMasterDto.getReqDiscId() : reqDisId);

					// update appointment status 
					if (billingMasterDto.getVisitTypeId()==OP && isPrePostBilling.equals('Y')
							&& (mapperId.getBillStatusId() == SETTLED 
								|| (selfDueAmount.compareTo(BigDecimal.ZERO) == 0 && (creditDueAmount.compareTo(BigDecimal.ZERO) == 1))
								|| (billingMasterDto.getIsFinancialClearance()=='Y'))) 
					{
						if (appointmentId != null && appointmentId > 0) 
						{
							
							Integer check = 0;
							SlotMasterDto dtott = new SlotMasterDto();
							dtott.setAppointmentId(appointmentId);
							Integer appointmentStatusId = billingMasterDao.checkAppoinmentStatussss(dtott);
							
							if(appointmentStatusId != null) {
								if(appointmentStatusId == 6) {
									check = 1;
								}
							}
							if(check == 0) {
								SlotMasterDto slotMasterDto = new SlotMasterDto();
								slotMasterDto.setAppointmentId(appointmentId);
								slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID5);
								slotMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
								slotMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
							iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
							}
						}

						if (encounterId != null && encounterId > 0) 
						{
							EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
								encounterMasterDto.setEncounterId(encounterId);
								encounterMasterDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
								encounterMasterDto.setStatusId(5);
								encounterMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
							iEncounterMasterService.updateEncounterStatus(encounterMasterDto);
							
						}

					}
					
					//Close the bill for op pharmacy bill if visit type=1 and ConsolidatedBill()=='Y'
					if(pharmacyServiceOrderDetailsIds!=null && pharmacyServiceOrderDetailsIds.size() >0 )
						iWalkInSalesDao.updateConsolidatedBillStatusBySaleDetails(pharmacyServiceOrderDetailsIds,billMasterId);
					
					if(billingMasterDto.isSaveFromSplitBill())
						iPreAuthorizationRequest.updateConsumedAmount(billingMasterDto.getAdmissionId(), billingMasterDto.getEncounterId());
					
					
					if (billingMasterDto.getIsFinalBill() == 'Y' && billingMasterDto.getAdmissionId() != null) 
					{
						billingMasterDao.updateFinalBill(billingMasterDto.getAdmissionId());

						if (billingMasterDto.getSelfPayable().equals(0.00)
								&& (billingMasterDto.getCreditPayable() > 0.00))
							iRequestDiscountDao.updateAdmissionsIsFinancialClearance(billingMasterDto.getAdmissionId());
						else if (billingMasterDto.getSelfPayable().equals(0.00)
								&& billingMasterDto.getNetAmount().equals(0.00))
							iRequestDiscountDao.updateAdmissionsIsFinancialClearance(billingMasterDto.getAdmissionId());

					}
					//update order detail
					if(billingMasterDto.getAgainstBillId()!=null && billingMasterDto.getAgainstBillId() > 0)
					   iBillingMasterDao.updateIsBillReopened(billingMasterDto.getAgainstBillId());
					
					
					//iBillingMasterDao.insertBillDataJson(billingMasterDto.getBillingMasterId());
					
					
					if(((billingMasterDto.getPaymentReceiptDetailsDtosList()!=null 
							&& billingMasterDto.getPaymentReceiptDetailsDtosList().size() > 0) 
								|| (billingMasterDto.getCashSettlement()!=null 
									&& billingMasterDto.getCashSettlement().equals('Y'))
										|| billingMasterDto.getTotalDepositConsumption()!=null) 
											&& billingMasterDto.getSelfPayable() > 0)
					{

						Double exceeAmount = 0.00;
						Double totalPayeeAmount = 0.00;
						//Double totalPayeeAmount = billingMasterDto.getSelfPayable();
						Integer billId = null;
	
						BillPaymentCollectionReqDto paymentCollectionDto = new BillPaymentCollectionReqDto();
							paymentCollectionDto.setBillingMasterId(billMasterId);
							paymentCollectionDto.setBillPayeeDetailsId(selfBillPayeeDetailsId);
							paymentCollectionDto.setDateOfReciept(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
							paymentCollectionDto.setPayeeId(selfPayeeId);
							paymentCollectionDto.setOrgnizationId(billingMasterDto.getOrganisationId());
							paymentCollectionDto.setUnitId(billingMasterDto.getUnitId());
							paymentCollectionDto.setIsPreOrPostBilling(isPrePostBilling);
							paymentCollectionDto.setIsFinancialClearance('N');
							paymentCollectionDto.setAppointmentId(appointmentId);
							paymentCollectionDto.setEncounterId(encounterId);
							paymentCollectionDto.setPatientId(billingMasterDto.getPatientId());
							paymentCollectionDto.setPaymentDepositDetailsList(null);
							paymentCollectionDto.setPayeeDueAmount(billingMasterDto.getSelfPayable());
							paymentCollectionDto.setReceiptType(1);
							paymentCollectionDto.setCreatedBy(billingMasterDto.getCreatedBy());
							paymentCollectionDto.setUpdatedBy(billingMasterDto.getCreatedBy());
							paymentCollectionDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
							paymentCollectionDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
							paymentCollectionDto.setTotalSelfPayable(billingMasterDto.getSelfPayable().doubleValue());
							paymentCollectionDto.setTotalCreditPayable(billingMasterDto.getCreditPayable()!=null ? billingMasterDto.getCreditPayable() : 0.00);
							paymentCollectionDto.setIsInterimOrFinalBill(billingMasterDto.getIsFinalBill()!=null && billingMasterDto.getIsFinalBill()=='Y' ? 2 : 1);
							paymentCollectionDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
							paymentCollectionDto.setIsNursingAssessmentRequired(isNursingAssessmentRequired);
							paymentCollectionDto.setOutstanding(billingMasterDto.getOutstanding());
							paymentCollectionDto.setAmount(billingMasterDto.getSelfPayable());
							paymentCollectionDto.setTotalDepositConsumption(billingMasterDto.getTotalDepositConsumption());
							paymentCollectionDto.setPaymentDepositDetailsList(billingMasterDto.getPaymentDepositDetailsList());
							paymentCollectionDto.setReferalDocId(billingMasterDto.getReferralId());
						List<PaymentReceiptDetailsDto> receiptDetailsDtosList = new LinkedList<>();

						if (billingMasterDto.getPaymentReceiptDetailsDtosList() != null	&& billingMasterDto.getPaymentReceiptDetailsDtosList().size() > 0) 
						{
							ListIterator<PaymentReceiptDetailsDto> recieptDetailsItr = billingMasterDto.getPaymentReceiptDetailsDtosList().listIterator();
							MpesaMasterDto masterDto = new MpesaMasterDto();
							while (recieptDetailsItr.hasNext()) 
							{
								PaymentReceiptDetailsDto detailsDto = recieptDetailsItr.next();
								detailsDto.setPaymentModeId(detailsDto.getPaymentModeId());
								detailsDto.setAmount(detailsDto.getAmount());
								totalPayeeAmount = totalPayeeAmount + detailsDto.getAmount();
								if (paymentCollectionDto.getPayeeDueAmount() <= totalPayeeAmount) 
								{
									exceeAmount = exceeAmount + (totalPayeeAmount - paymentCollectionDto.getPayeeDueAmount());
								}
								detailsDto.setCreatedBy(detailsDto.getCreatedBy());

								if (detailsDto.getPaymentModeId() == 5  && (detailsDto.getSkipMpesaValidation()!=null && detailsDto.getSkipMpesaValidation())) 
								{
									masterDto.setMpesaTxcode(detailsDto.getCardNo());
									masterDto.setUnitId(billingMasterDto.getUnitId());
									masterDto.setOrganizationId(billingMasterDto.getOrganisationId());
									masterDto.setUpdatedBy(detailsDto.getCreatedBy());
									masterDto.setMpesaAmount(detailsDto.getAmount());
									iMpesaMasterDao.validateMpesaTrancastion(masterDto);
									
								}
								if(detailsDto.getPaymentModeId() == 3) 
				                {
				                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
				                	pOSMachineResponseMapperDto.setCreatedDate(detailsDto.getCreatedDate());
				                	pOSMachineResponseMapperDto.setUpdatedDate(detailsDto.getCreatedDate());
				                	pOSMachineResponseMapperDto.setUnitId(detailsDto.getUnitId());
				                	pOSMachineResponseMapperDto.setOrganizationId(detailsDto.getOrgnizationId());
				                	pOSMachineResponseMapperDto.setUpdatedBy(detailsDto.getCreatedBy());
				                	pOSMachineResponseMapperDto.setIsConsumed(true);
				                	pOSMachineResponseMapperDto.setRequestedUniqueId(detailsDto.getTransactionNo());
				                	
				                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
				                }
								receiptDetailsDtosList.add(detailsDto);
							}
							paymentCollectionDto.setPaymentReceiptDetailsDtosList(receiptDetailsDtosList);
							paymentCollectionDto.setAmount(totalPayeeAmount + (billingMasterDto.getTotalDepositConsumption() != null ? billingMasterDto.getTotalDepositConsumption() : 0.00));
							paymentCollectionDto.setIsTransferToActiveDeposit(exceeAmount > 0 && (billingMasterDto.getCashSettlement()!=null && billingMasterDto.getCashSettlement().equals('N'))  ? 'Y' : 'N');
						} else if (billingMasterDto.getCashSettlement() != null	&& billingMasterDto.getCashSettlement().equals('Y')) 
						{
							PaymentReceiptDetailsDto detailsDto = new PaymentReceiptDetailsDto();
								detailsDto.setOrgnizationId(billingMasterDto.getOrganisationId());
								detailsDto.setUnitId(billingMasterDto.getUnitId());
								detailsDto.setPaymentModeId(1);
								detailsDto.setAmount(billingMasterDto.getSelfPayable());
								detailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
								detailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
								detailsDto.setDateOfTransaction(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
							receiptDetailsDtosList.add(detailsDto);
							paymentCollectionDto.setPaymentReceiptDetailsDtosList(receiptDetailsDtosList);
							paymentCollectionDto.setIsTransferToActiveDeposit('N');
						}else if((billingMasterDto.getPaymentReceiptDetailsDtosList() == null || billingMasterDto.getPaymentReceiptDetailsDtosList().size() == 0) 
								&& (billingMasterDto.getPaymentDepositDetailsList()!=null && billingMasterDto.getPaymentDepositDetailsList().size() > 0)){
							paymentCollectionDto.setAmount(billingMasterDto.getTotalDepositConsumption() != null ? billingMasterDto.getTotalDepositConsumption() : 0.00);
							paymentCollectionDto.setIsTransferToActiveDeposit('N');
						}
						return new Response<>(SUCCESS, (billPayeeDetailsId != null ? billPayeeDetailsId.toString() : null),"Bill Generated Successfully !!", listToBeAdded, billMasterId,paymentCollectionDto);
					}
					return new Response<>(SUCCESS, (billPayeeDetailsId != null ? billPayeeDetailsId.toString() : null),"Bill Generated Successfully !!", listToBeAdded, billMasterId);
				}
			}
			return new Response<>(SUCCESS, null, "Order Generated Successfully !!", listToBeAdded, null);

		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	public String generateRequestNumber() {
		try {
			Long incrementedId = billingMasterDao.generateRequestNumber();
			String billPrefix = "REQ-";

			StringBuilder bill = new StringBuilder(billPrefix);

			int length = Long.valueOf(incrementedId).toString().length();
			bill = length == 1 ? (bill.append("000000").append(incrementedId))
					: length == 2 ? (bill.append("00000").append(incrementedId))
							: length == 3 ? (bill.append("0000").append(incrementedId))
									: length == 4 ? (bill.append("000").append(incrementedId))
											: length == 5 ? (bill.append("00").append(incrementedId))
													: length == 6 ? (bill.append("0").append(incrementedId))
															: length == 7 ? (bill.append(incrementedId))
																	: bill.append("");
			return bill.toString();
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public Response packageConsumption(BillingMasterDto billingMasterDto, List<OrderDetailsMasterDto> listToBeAdded, Response orderRes) throws Exception {
		try {
			Response res= savePackageConsumption(billingMasterDto, listToBeAdded, orderRes);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, listToBeAdded,res.getResObject());
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	// METHOD TO SAVE PACKGE CONSUMPTION
	public Response savePackageConsumption(BillingMasterDto billingMasterDto, List<OrderDetailsMasterDto> listToBeAdded,Response orderRes) throws Exception {
		try {
			Map<Integer, Integer> packageOrders = (Map) orderRes.getObject();
			Map<Integer, Integer> ehcPackageIds = new HashMap();

			ListIterator<OrderDetailsMasterDto> packageItr = listToBeAdded.listIterator();
			if (packageOrders != null && packageOrders.size() > 0) 
			{
				while (packageItr.hasNext()) 
				{
					OrderDetailsMasterDto orderDtls = packageItr.next();
					if (orderDtls.isPackage() && orderDtls.getPackageId() != null && orderDtls.getIsPackageService() != 'Y' && (orderDtls.getStatus()!=null && orderDtls.getStatus()=='A')) 
					{

						if (orderDtls.getPackageTypeId() != null && orderDtls.getPackageTypeId() == 1 && orderDtls.isPackage()) 
						{
							for (Map.Entry<Integer, Integer> entry : packageOrders.entrySet()) 
							{
								if (entry.getKey().intValue() == orderDtls.getPackageId().intValue()) 
								{
									ehcPackageIds.put(entry.getKey(), entry.getValue());
								}
							}
						}

						Integer createdBy = billingMasterDto.getCreatedBy();
						Integer updatedBy = billingMasterDto.getUpdatedBy();
						String createdDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime(), "dd-M-yyyy HH:mm:ss");
						String updatedDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime(), "dd-M-yyyy HH:mm:ss");

						// save package into package consumption
						Response packRes = iMPackageMasterDao.getPackageById(orderDtls.getPackageId());
						if (packRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packRes.getObject() != null) 
						{
							MPackageMasterDto mPackage = (MPackageMasterDto) packRes.getObject();
							TPackageConsumptionMasterDto consMstDto = new TPackageConsumptionMasterDto();
								consMstDto.setPackageConsumptionMasterId(orderDtls.getPackageConsumptionMasterId());
								consMstDto.setAdmissionId(billingMasterDto.getAdmissionId());
								consMstDto.setApplicableNoOfVisit(mPackage.getNoOfEncounters());
								consMstDto.setEncounterSpecialityId(billingMasterDto.getEncounterSpecialityId());
								consMstDto.setEncounterId(billingMasterDto.getVisitTypeId().intValue()==1?billingMasterDto.getEncounterId():null);
								consMstDto.setOrderDetailId(packageOrders.get(mPackage.getPackageMasterId()));
								consMstDto.setOrganisationId(billingMasterDto.getOrganisationId());
								consMstDto.setPackageId(mPackage.getPackageMasterId());
								consMstDto.setPackageTypeId(mPackage.getPackageTypeId());
								consMstDto.setPatientId(billingMasterDto.getPatientId());
								consMstDto.setPayeeId(orderDtls.getPayeeId());
								consMstDto.setRate(mPackage.getPackagePrice());
								consMstDto.setStatus(ACTIVE);
								consMstDto.setUnitId(billingMasterDto.getUnitId());
								consMstDto.setVisitType(billingMasterDto.getVisitTypeId());
								consMstDto.setCreatedBy(createdBy);
								consMstDto.setCreatedDate(createdDate);
								consMstDto.setUpdatedBy(updatedBy);
								consMstDto.setUpdatedDate(updatedDate);
								consMstDto.setVisisted(false);
								consMstDto.setBillingBedCategoryId(mPackage.getBillingBedCategoryId());
								consMstDto.setTariffId(mPackage.getTariffId());
								consMstDto.setParentPackageId(mPackage.getParentPackageId());
								consMstDto.setPackageApplicableDate(orderDtls.getPackageApplicableDate());
							
							// if multi encounter package or all exlusive
							if (mPackage.getPackageTypeId() == 5||mPackage.getPackageTypeId() == 4||mPackage.getPackageTypeId() == 2) 
							{
								Integer validityInDays = mPackage.getValidityPeriodIndays()-1;
								LocalDate ldate =null; 
								if(orderDtls.getPackageApplicableDate()!=null)
								{
									
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
									String date = orderDtls.getPackageApplicableDate();

									ldate = LocalDate.parse(date, formatter).plusDays(validityInDays);
								}else{
									ldate = LocalDate.now().plusDays(validityInDays);
								}
								Date validityDate = GlobalCommonDateUtils.getDateWithEndTime(ldate);
								consMstDto.setValidityFrom(orderDtls.getPackageApplicableDate()!=null ? orderDtls.getPackageApplicableDate() : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-MM-yyyy"));
								consMstDto.setValidityTo(GlobalCommonDateUtils.getStringDate(validityDate, "dd-M-yyyy HH:mm:ss"));
								consMstDto.setEncounterSpecialityId(billingMasterDto.getEncounterSpecialityId());
								
								if(mPackage.getPackageTypeId() == 4||mPackage.getPackageTypeId() == 2)
									consMstDto.setApplicableNoOfVisit(1);
							}

							Response packConsRes = iTPackageConsumptionMasterDao.saveTPackageConsumptionMaster(consMstDto);
							
							
							if (packConsRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
									&& packConsRes.getObject() != null) 
							{
								TPackageConsumptionMaster consMaster = (TPackageConsumptionMaster) packConsRes
										.getObject();
								Integer packageConsumptionId = consMaster.getPackageConsumptionMasterId();
								
								Response packageBbbMpprRes =  	iTPackageConsumptionMasterDao.getBillingBedCategoryIdsByParentPackageId(consMstDto);
								List<PackageBBCategoryMpprDto> mpprList = packageBbbMpprRes.getListObject();
								for (Iterator iterator = mpprList.iterator(); iterator.hasNext();) {
									PackageBBCategoryMpprDto mpprDto = (PackageBBCategoryMpprDto) iterator.next();
									mpprDto.setPackageId(consMstDto.getPackageId());
									mpprDto.setPackageConsumptionMasterId(packageConsumptionId);
									iTPackageConsumptionMasterDao.savePackageBbcMppr(mpprDto);
								}
							
								orderDtls.setPackageConsumptionMasterId(packageConsumptionId);
								Response packServRes = iTPackageServicesDetailsDao
										.getPackageServicesByPackageId(mPackage.getPackageMasterId());
								if (packServRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
										&& packServRes.getListObject() != null
										&& packServRes.getListObject().size() > 0) {

									// save package service wise details
									ListIterator<TPackageServicesDetailsDto> srvItr = packServRes.getListObject()
											.listIterator();
									TPackageConsumptionServiceDetailDto consumpServDtls = null;
									while (srvItr.hasNext()) 
									{
										TPackageServicesDetailsDto servDts = srvItr.next();
										consumpServDtls = new TPackageConsumptionServiceDetailDto();
										consumpServDtls.setApportionedPrice(servDts.getApportionedPrice());
										
										if(servDts.getNumberToBeUse()!=null)
										consumpServDtls.setBalancePackageQuantity(
												mPackage.getPackageTypeId() == 1 ? (servDts.getNumberToBeUse() - 1)
														: servDts.getNumberToBeUse());
										
										consumpServDtls.setIsServiceItem(servDts.getIsServiceItem());
										consumpServDtls.setItemId(servDts.getItemId());
										consumpServDtls.setOrganisationId(billingMasterDto.getOrganisationId());
										consumpServDtls.setPackageConsumptionMasterId(packageConsumptionId);
										consumpServDtls.setPackageEoDetailId(servDts.getPackageEitherOrGroupId());
										consumpServDtls.setPackageQuantity(servDts.getNumberToBeUse());
										consumpServDtls.setServiceId(servDts.getServiceId());
										consumpServDtls.setStatus(ACTIVE);
										consumpServDtls.setUnitId(billingMasterDto.getUnitId());
										consumpServDtls.setCreatedBy(createdBy);
										consumpServDtls.setCreatedDate(createdDate);
										consumpServDtls.setUpdatedBy(updatedBy);
										consumpServDtls.setUpdatedDate(updatedDate);
										consumpServDtls.setIsCap(servDts.getIsCap());
										consumpServDtls.setServiceCap(servDts.getServiceCap());
										consumpServDtls.setAvailableCap(servDts.getServiceCap());
										consumpServDtls.setIsInclusiveExclusive(2);
										consumpServDtls.setProductCategoryId(servDts.getProductCategoryId());
										iTPackageConsumptionServiceDetailDao
												.saveTPackageConsumptionServiceDetail(consumpServDtls);
									}
								}
								// save package cap wise details
								if (consMstDto.getPackageTypeId() == 3) {
									Response packCapRes = iTPackageCapDetailsDao
											.getPackageCapDetailsByPackageId(mPackage.getPackageMasterId());
									if (packCapRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
											&& packCapRes.getListObject() != null
											&& packCapRes.getListObject().size() > 0) {
										ListIterator<TPackageCapDetailsDto> capItr = packCapRes.getListObject()
												.listIterator();
										TPackageConsumptionCapDetailsDto consCapDtls = null;
										while (capItr.hasNext()) 
										{
											TPackageCapDetailsDto capDtls = capItr.next();
											consCapDtls = new TPackageConsumptionCapDetailsDto();
											consCapDtls.setCreatedBy(createdBy);
											consCapDtls.setCreatedDate(createdDate);
											consCapDtls.setDepartmentCapAmount(capDtls.getDepartmentCapAmount());
											consCapDtls.setDepartmentId(capDtls.getDepartmentId());
											consCapDtls.setIsServiceItem(capDtls.getIsServiceItem());
											consCapDtls.setOrganisationId(billingMasterDto.getOrganisationId());
											consCapDtls.setPackageConsumptionMasterId(packageConsumptionId);
											consCapDtls.setProductCategoryId(capDtls.getProductCateroyId());
											consCapDtls.setStatus(ACTIVE);
											
											if(capDtls.getQuantity()!=null && capDtls.getQuantity()>0) 
											{
												consCapDtls.setQuantity(capDtls.getQuantity());
												consCapDtls.setPackageQty(capDtls.getQuantity());
											}
												
											
											if(capDtls.getProductCateroyCapAmount()!=null &&capDtls.getProductCateroyCapAmount()>0)
											{
												consCapDtls.setCapAmount(capDtls.getProductCateroyCapAmount());
												consCapDtls.setAvailableCap(capDtls.getProductCateroyCapAmount());
											}
											else if (capDtls.getDepartmentCapAmount() != null
													&& capDtls.getDepartmentCapAmount() > 0) {
												consCapDtls.setCapAmount(capDtls.getDepartmentCapAmount());
												consCapDtls.setAvailableCap(capDtls.getDepartmentCapAmount());
											} else {
												consCapDtls.setCapAmount(capDtls.getSubDepartmentCapAmount());
												consCapDtls.setAvailableCap(capDtls.getSubDepartmentCapAmount());
											}

											consCapDtls.setSubDepartmentId(capDtls.getSubDepartmentId());
											consCapDtls.setUnitId(billingMasterDto.getUnitId());
											consCapDtls.setUpdatedBy(updatedBy);
											consCapDtls.setUpdatedDate(updatedDate);
											iTPackageConsumptionCapDetailsDao
													.saveTPackageConsumptionCapDetails(consCapDtls);
										}
									}

									// for bedCategory Applicable Days
									Response tPackageBedCategoryRes = iTPacakgeBedCategoryDao
											.getPackageBedCategoryForBilling(mPackage.getPackageMasterId());
									if (tPackageBedCategoryRes.getListObject() != null
											&& tPackageBedCategoryRes.getListObject().size() > 0) {
										ListIterator<TPackageBedCategoryDto> bedCtr = tPackageBedCategoryRes
												.getListObject().listIterator();
										TPackageBedCategoryDto bedCategoryDtls = null;
										while (bedCtr.hasNext()) {
											bedCategoryDtls = new TPackageBedCategoryDto();
											bedCategoryDtls = bedCtr.next();
											bedCategoryDtls.setCreatedBy(createdBy);
											bedCategoryDtls.setCreatedDate(createdDate);
											bedCategoryDtls.setOrganisationId(billingMasterDto.getOrganisationId());
											bedCategoryDtls.setPackageConsumptionMasterId(packageConsumptionId);
											bedCategoryDtls.setStatus(ACTIVE);
											bedCategoryDtls.setUnitId(billingMasterDto.getUnitId());
											bedCategoryDtls.setUpdatedBy(updatedBy);
											bedCategoryDtls.setUpdatedDate(updatedDate);
											iTPacakgeBedCategoryDao.saveTPackageBedCategory(bedCategoryDtls);
										}
									}
								}
								// save package eo wise details
								if (consMstDto.getPackageTypeId() == 2) {
									Response packEoRes = iTPackageEitherOrGroupDetailsDao
											.getPackageEitherOrGroupDetailsByPackageId(mPackage.getPackageMasterId());
									if (packEoRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
											&& packEoRes.getListObject() != null
											&& packEoRes.getListObject().size() > 0) {
										ListIterator<TPackageEitherorGroupDetailsDto> eoItr = packEoRes.getListObject()
												.listIterator();
										TPackageConsumptionEoDetailsDto eoDetailsDto = null;
										while (eoItr.hasNext()) {
											TPackageEitherorGroupDetailsDto packEoDtls = eoItr.next();
											eoDetailsDto = new TPackageConsumptionEoDetailsDto();
											eoDetailsDto.setBalanceQuantity(mPackage.getPackageTypeId() == 1
													? (packEoDtls.getNumberServiceUsable() - 1)
													: packEoDtls.getNumberServiceUsable());
											eoDetailsDto.setCreatedBy(createdBy);
											eoDetailsDto.setCreatedDate(createdDate);
											eoDetailsDto.setGroupPrice(packEoDtls.getGroupPrice());
											eoDetailsDto.setOrganisationId(billingMasterDto.getOrganisationId());
											eoDetailsDto
													.setPacakgeEighterorGroupId(packEoDtls.getPackageGroupDetailsId());
											eoDetailsDto.setPackageConsumptionMasterId(packageConsumptionId);
											eoDetailsDto.setTotalQuantity(packEoDtls.getNumberServiceUsable());
											eoDetailsDto.setUnitId(billingMasterDto.getUnitId());
											eoDetailsDto.setUpdatedBy(updatedBy);
											eoDetailsDto.setUpdatedDate(updatedDate);
											iTPackageConsumptionEoDetailsDao
													.saveTPackageConsumptionEoDetails(eoDetailsDto);
										}
									}
								}

								// save inc exclusive details
								if (consMstDto.getPackageTypeId() == 3 || consMstDto.getPackageTypeId() == 4) {
									Response packIncExcRes = iTPackageIncExcDetailsDao
											.getTPackageIncExcDetailsForBillingByPackage(mPackage.getPackageMasterId());
									TPackageConsumptionServiceDetailDto consumpServDetails = null;
									if (packIncExcRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
											&& packIncExcRes.getListObject() != null
											&& packIncExcRes.getListObject().size() > 0) 
									{
										ListIterator<TPackageIncExcDetailsDto> packIncExcItr = packIncExcRes
												.getListObject().listIterator();
										TPackageIncExcDetailsDto packIncExcDto = null;
										while (packIncExcItr.hasNext()) {
											packIncExcDto = packIncExcItr.next();
											consumpServDetails = new TPackageConsumptionServiceDetailDto();
											consumpServDetails.setApportionedPrice(packIncExcDto.getApportionedPrice());
											consumpServDetails.setIsServiceItem(packIncExcDto.getIsServiceItem());
											consumpServDetails.setItemId(packIncExcDto.getItemId());
											consumpServDetails
													.setIsInclusiveExclusive(packIncExcDto.getIsInclusiveService());
											consumpServDetails.setOrganisationId(billingMasterDto.getOrganisationId());
											consumpServDetails.setPackageConsumptionMasterId(packageConsumptionId);
											consumpServDetails.setServiceId(packIncExcDto.getServiceId());
											consumpServDetails.setStatus(ACTIVE);
											consumpServDetails.setUnitId(billingMasterDto.getUnitId());
											consumpServDetails.setCreatedBy(createdBy);
											consumpServDetails.setCreatedDate(createdDate);
											consumpServDetails.setUpdatedBy(updatedBy);
											consumpServDetails.setUpdatedDate(updatedDate);
											consumpServDetails.setIsCap(1);
											consumpServDetails.setServiceCap(0.00);
											consumpServDetails.setAvailableCap(0.00);
											consumpServDetails.setIsGroupwiseExlusion(packIncExcDto.getIsGroupwiseExlusion());
											consumpServDetails.setDeptId(packIncExcDto.getDeptId());
											consumpServDetails.setSubDeptId(packIncExcDto.getSubDeptId());
											consumpServDetails.setProductCategoryId(packIncExcDto.getPackageCategoryConsumableId());
											iTPackageConsumptionServiceDetailDao
													.saveTPackageConsumptionServiceDetail(consumpServDetails);
										}
									}
								}
								
								
								
								// save categoryWiseConsumable
								if (consMstDto.getPackageTypeId() == 3) {
									Response consumableRes = iTPackageCategoryWiseConsumableDetailsDao
											.getTPackageCategoryWiseConsumableDetails(mPackage.getPackageMasterId());
									TPackageConsumptionServiceDetailDto consumpServDetails = null;
									if (consumableRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
											&& consumableRes.getListObject() != null
											&& consumableRes.getListObject().size() > 0) 
									{
										ListIterator<TPackageCategoryWiseConsumableDetailsDto> consumable = consumableRes
												.getListObject().listIterator();
										TPackageCategoryWiseConsumableDetailsDto consumableDto = null;
										while (consumable.hasNext()) 
										{
											consumableDto = consumable.next();
											consumpServDetails = new TPackageConsumptionServiceDetailDto();
											consumpServDetails.setBalancePackageQuantity(consumableDto.getNumberToBeUse());
											consumpServDetails.setPackageQuantity(consumableDto.getNumberToBeUse());
											consumpServDetails.setIsServiceItem(2);//for item
											consumpServDetails.setProductCategoryId(consumableDto.getCategoryId());
											consumpServDetails.setIsInclusiveExclusive(2);
											consumpServDetails.setOrganisationId(billingMasterDto.getOrganisationId());
											consumpServDetails.setPackageConsumptionMasterId(packageConsumptionId);
											consumpServDetails.setStatus(ACTIVE);
											consumpServDetails.setUnitId(billingMasterDto.getUnitId());
											consumpServDetails.setCreatedBy(createdBy);
											consumpServDetails.setCreatedDate(createdDate);
											consumpServDetails.setUpdatedBy(updatedBy);
											consumpServDetails.setUpdatedDate(updatedDate);
											consumpServDetails.setIsCap(1);
											consumpServDetails.setServiceCap(0.00);
											consumpServDetails.setAvailableCap(0.00);
											iTPackageConsumptionServiceDetailDao
													.saveTPackageConsumptionServiceDetail(consumpServDetails);
										}
									}
								}
							}
						}
					}
				}
			}

			// ehc consultation services
			if (ehcPackageIds != null && ehcPackageIds.size() > 0) {
				for (Map.Entry<Integer, Integer> entry : ehcPackageIds.entrySet()) {
					if(billingMasterDto.getEncounterId()!=null && billingMasterDto.getEncounterId().intValue()>0) {
						EncounterMasterDto dto = new EncounterMasterDto();
						dto.setOrderDetailsIdEhc(entry.getValue());
						dto.setEhcPackageId(entry.getKey());
						dto.setOrganizationId(billingMasterDto.getOrganisationId());
						dto.setUnitId(billingMasterDto.getUnitId());
						dto.setCreatedBy(billingMasterDto.getCreatedBy());
						dto.setUpdatedBy(billingMasterDto.getCreatedBy());
						
						dto.setEncounterId(billingMasterDto.getEncounterId());
						dto.setCreatedDate(billingMasterDto.getCreatedDate());
						iMPackageMasterService.saveEhcEncounterEhcConsultation(dto);
					}
					
				}
			}
			return new Response<>(SUCCESS, null, null, null, listToBeAdded,ehcPackageIds);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getActiveDiscountCategories(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return billingMasterDao.getActiveDiscountCategories(orgId, unitId);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	public String getBillNumber(Integer unitId, Integer paymentEntitlementId, Integer visitTypeId) {
		try {
			/*
			 * Long incrementedId = billingMasterDao.getNextValGetBillId(); String
			 * billPrefix="BILL-18-";
			 * 
			 * StringBuilder bill=new StringBuilder(billPrefix);
			 * 
			 * int length = Long.valueOf(incrementedId).toString().length(); bill = length
			 * == 1 ? (bill.append("000000").append(incrementedId)) : length == 2 ?
			 * (bill.append("00000").append(incrementedId)) : length == 3 ?
			 * (bill.append("0000").append(incrementedId)) : length == 4 ?
			 * (bill.append("000").append(incrementedId)) : length == 5 ?
			 * (bill.append("00").append(incrementedId)) : length == 6 ?
			 * (bill.append("0").append(incrementedId)) : length == 7 ?
			 * (bill.append(incrementedId)) : bill.append("");
			 */
			
			if(visitTypeId  == 1) {
				if(paymentEntitlementId == 1) {
					Integer tt = IBillingCommonConstants.OP_CASH_BILL_SEQUENCE;
					Integer screenId = billingMasterDao.getcheckScreenIdAvailable(unitId,tt);
					if(screenId>0) {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.OP_CASH_BILL_SEQUENCE );
					}else {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
					}
					
				}
				else if(paymentEntitlementId == 2) {
					Integer tt = IBillingCommonConstants.OP_INSURANCE_BILL_SEQUENCE;
					Integer screenId = billingMasterDao.getcheckScreenIdAvailable(unitId,tt);
					if(screenId>0) {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.OP_INSURANCE_BILL_SEQUENCE );
					}else {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
					}
				}
				else if(paymentEntitlementId == 3) {
					Integer tt = IBillingCommonConstants.OP_CORPORATE_BILL_SEQUENCE;
					Integer screenId = billingMasterDao.getcheckScreenIdAvailable(unitId,tt);
					if(screenId>0) {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.OP_CORPORATE_BILL_SEQUENCE );
					}else {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
					}
				}
			}
			else if(visitTypeId  == 2) {
				if(paymentEntitlementId == 1) {
					Integer tt = IBillingCommonConstants.IP_CASH_BILL_SEQUENCE;
					Integer screenId = billingMasterDao.getcheckScreenIdAvailable(unitId,tt);
					if(screenId>0) {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.IP_CASH_BILL_SEQUENCE );
					}else {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
					}
				}
				else if(paymentEntitlementId == 2) {

					Integer tt = IBillingCommonConstants.IP_INSURANCE_BILL_SEQUENCE;
					Integer screenId = billingMasterDao.getcheckScreenIdAvailable(unitId,tt);
					if(screenId>0) {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.IP_INSURANCE_BILL_SEQUENCE );
					}else {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
					}
				
				}
				else if(paymentEntitlementId == 3) {
					Integer tt = IBillingCommonConstants.IP_CORPORATE_BILL_SEQUENCE;
					Integer screenId = billingMasterDao.getcheckScreenIdAvailable(unitId,tt);
					if(screenId>0) {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.IP_CORPORATE_BILL_SEQUENCE );
					}else {
						return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
					}
				}
			}else {
				return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
			}
			
			
			
//			return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.BILL_NUMBER);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}

		return "error";

	}

	public String getDebitNoteNumber(Integer unitId) {
		try {
			/*
			 * Long incrementedId = billingMasterDao.getNextValGetBillId(); String
			 * debitNotePrefix="DN-18-";
			 * 
			 * StringBuilder debitNote=new StringBuilder(debitNotePrefix);
			 * 
			 * int length = Long.valueOf(incrementedId).toString().length(); debitNote =
			 * length == 1 ? (debitNote.append("000000").append(incrementedId)) : length ==
			 * 2 ? (debitNote.append("00000").append(incrementedId)) : length == 3 ?
			 * (debitNote.append("0000").append(incrementedId)) : length == 4 ?
			 * (debitNote.append("000").append(incrementedId)) : length == 5 ?
			 * (debitNote.append("00").append(incrementedId)) : length == 6 ?
			 * (debitNote.append("0").append(incrementedId)) : length == 7 ?
			 * (debitNote.append(incrementedId)) : debitNote.append("");
			 */
			return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.DEBIT_NOTE_NUMBER);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}


	}

	@Override
	@Transactional
	public Response collectPayment(BillPaymentCollectionReqDto reqDto) {
		try {
			double totalBillAmount = 0.0;
			double dueAmount = 0.0;
			double depositCounsumtionAmount = 0.0;
			double netAmount = 0.0;
			double concessionAmount = 0.0;
			double totalDueAmount = 0.0;
			double totalSelfDueAmount = 0.0;
			double totalCreditDueAmount = 0.0;
			Integer billStatusId = 0;
			Integer recieptId = null;
			Integer depositConsumptionId = null;
			Integer seftPayeeId = null;
			BillListResDto dto = null;
			Response savePaymentres = null;
			Character isNursingAssessmentRequiredForUnit = null;
			Character isKioskRequired = null;
			Character isNursingAssessmentRequiredForSpeciality = reqDto.getIsNursingAssessmentRequired() != null
					? reqDto.getIsNursingAssessmentRequired()
					: 'Y';
			Character isPaidAppointment = reqDto.getIsPaidAppointment() != null ? reqDto.getIsPaidAppointment() : 'N';
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(reqDto.getOrgnizationId());
			unitApplicationConfigurationDto.setUnitId(reqDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao
					.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);

			if (appConfigRes.getObject() != null) {
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
						.getObject();

				if (unitApplicationConfigurationDtos.getSelfPayeeId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
				else
					seftPayeeId = unitApplicationConfigurationDtos.getSelfPayeeId();

				if (unitApplicationConfigurationDtos.getIsPreOrPostBilling() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null,
							null);
				else
					reqDto.setIsPreOrPostBilling(unitApplicationConfigurationDtos.getIsPreOrPostBilling());

				if (unitApplicationConfigurationDtos.getIsNursingAssessment() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Is Nursing Assessment Required is not defined !!",
							null, null);
				else
					isNursingAssessmentRequiredForUnit = unitApplicationConfigurationDtos
							.getIsNursingAssessment() != null
									? unitApplicationConfigurationDtos.getIsNursingAssessment()
									: 'Y';

				if (unitApplicationConfigurationDtos.getIsKioskRequired() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Is Kiosk Required is not defined !!", null, null);
				else
					isKioskRequired = unitApplicationConfigurationDtos.getIsKioskRequired() != null
							? unitApplicationConfigurationDtos.getIsKioskRequired()
							: 'N';

			} else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Application Configuration is yet to set !!", null,
						null);
			}

			Response billPayeeDtlsRes =	iBillingDao.getBillPayeeDetailsByBillId(reqDto.getBillingMasterId());
			List<BillPayeeDetailsResDto> listBillPayeeDetailsResDto = billPayeeDtlsRes.getListObject();
			
			for(BillPayeeDetailsResDto payeeDto : listBillPayeeDetailsResDto)
			{
				if(payeeDto.getPayeeId().equals(seftPayeeId))
					totalSelfDueAmount = totalSelfDueAmount + payeeDto.getDueAmount();
				else 
					totalCreditDueAmount = totalCreditDueAmount + payeeDto.getDueAmount();
			}
			
			
			
			
			Response billRes = billingMasterDao.getBillMasterById(reqDto.getBillingMasterId());

			if (billRes.getStatus().equals(SUCCESS) && billRes.getObject() != null) {
				BillingMasterDto billingMasterDto = (BillingMasterDto) billRes.getObject();
				
				if(billingMasterDto!=null && billingMasterDto.getBillCancelled()=='Y')
					return new Response<>(ERROR, ALREADY_EXIST_CODE, "Bill is already Cancelled!!", null,	null);	
				
				if(reqDto.getOutstanding()!=null && !(billingMasterDto.getOutstanding().equals(reqDto.getOutstanding())))
					return new Response<>(ERROR, ALREADY_EXIST_CODE, "Selected bill is already in use...!!", null,	null);	
				
				totalBillAmount = billingMasterDto.getTotalBillAmount();
				dueAmount = reqDto.getPayeeDueAmount();
				totalDueAmount = billingMasterDto.getOutstanding();
				netAmount = billingMasterDto.getNetAmount();
				concessionAmount = billingMasterDto.getConcessionAmount();

				// consume deposit master

				if (reqDto.getPaymentDepositDetailsList() != null && reqDto.getPaymentDepositDetailsList().size() > 0) {
					Response res = iPaymentDepositConsumptionService.consumeDeposit(reqDto);
					if (res.getStatus().equals(SUCCESS)) {
						dueAmount = dueAmount - reqDto.getTotalDepositConsumption();
						dueAmount = Math.round(dueAmount * 100.0) / 100.0;
						depositConsumptionId = Integer.parseInt(res.getMessage());
					}
				}
				// signiture image

				// PaymentRecieptMaster and PaymentRecieptDeatilsMaster
				PaymentRecieptMasterDto paymentRecieptMasterDto = new PaymentRecieptMasterDto();
					paymentRecieptMasterDto.setBillingMasterId(reqDto.getBillingMasterId());
					paymentRecieptMasterDto.setAmount(reqDto.getAmount());
					paymentRecieptMasterDto.setPayeeDueAmount(reqDto.getPayeeDueAmount());
					paymentRecieptMasterDto.setIsTransferToActiveDeposit(reqDto.getIsTransferToActiveDeposit());
					paymentRecieptMasterDto.setDateOfReciept(reqDto.getDateOfReciept());
					paymentRecieptMasterDto.setUnitId(reqDto.getUnitId());
					paymentRecieptMasterDto.setOrgnizationId(reqDto.getOrgnizationId());
					paymentRecieptMasterDto.setCreatedDate(reqDto.getCreatedDate());
					paymentRecieptMasterDto.setCreatedBy(reqDto.getCreatedBy());
					paymentRecieptMasterDto.setUpdatedDate(reqDto.getUpdatedDate());
					paymentRecieptMasterDto.setUpdatedBy(reqDto.getUpdatedBy());
					paymentRecieptMasterDto.setReceiptType(reqDto.getReceiptType());
					paymentRecieptMasterDto.setPayeeId(reqDto.getPayeeId());
					paymentRecieptMasterDto.setReceivedFrom(reqDto.getReceivedFrom());
					paymentRecieptMasterDto.setIdentificationNumber(reqDto.getIdentificationNumber());
					paymentRecieptMasterDto.setIsPrinted('N');
					paymentRecieptMasterDto.setCounterId(reqDto.getCounterId());
					paymentRecieptMasterDto.setIsFinancialClearance(reqDto.getIsFinancialClearance());
					paymentRecieptMasterDto.setPaymentReceiptDetailsDtosList(reqDto.getPaymentReceiptDetailsDtosList());
					paymentRecieptMasterDto.setAdmissionId(reqDto.getAdmissionId());// admissionId
					paymentRecieptMasterDto.setEncounterId(reqDto.getEncounterId());// encounterId
					paymentRecieptMasterDto.setPatientId(reqDto.getPatientId());// patientId
					paymentRecieptMasterDto.setVisitTypeId(reqDto.getVisitTypeId());// visitTypeId
					paymentRecieptMasterDto.setReceivedFrom(reqDto.getPaymentReceiptDetailsDtosList()!=null && reqDto.getPaymentReceiptDetailsDtosList().size()!=0 ? reqDto.getPaymentReceiptDetailsDtosList().getFirst().getReceivedFrom():(reqDto.getReceivedFrom()!=null ? reqDto.getReceivedFrom():""));
				if (reqDto.getDisallowedAmount() != null && reqDto.getDisallowedAmount() > 0 && reqDto.getDisallowedAgainstInvoiceDtlsId() != null	&& reqDto.getDisallowedAgainstInvoiceDtlsId() > 0)
					paymentRecieptMasterDto.setAgainstDisallowedAmount('Y');

				if (reqDto.getSignetureImage() != null && !reqDto.getSignetureImage().isEmpty()) {

					String base64Image = reqDto.getSignetureImage().split(",")[1];
					byte[] signature = Base64.getDecoder().decode(base64Image);
					paymentRecieptMasterDto.setSignature(signature);
				}

				if (depositConsumptionId != null)
					paymentRecieptMasterDto.setDepositeConsumptionId(depositConsumptionId);

				savePaymentres = iPaymentRecieptMasterService.savePaymentReciept(paymentRecieptMasterDto,dueAmount,reqDto.getTotalDepositConsumption()!=null && reqDto.getTotalDepositConsumption()>0 ? reqDto.getTotalDepositConsumption() : 0.00);
				dueAmount = (double) savePaymentres.getObject();
				
				if(reqDto.getPayeeId().equals(seftPayeeId) && reqDto.getTotalCreditPayable()!=null)
					dueAmount = dueAmount + totalCreditDueAmount;
				else if(!(reqDto.getPayeeId().equals(seftPayeeId)))
					dueAmount = dueAmount + totalSelfDueAmount;
				
				recieptId = Integer.parseInt(savePaymentres.getMessage());

				// update bill
				BillingMasterDto billUpdateDto = new BillingMasterDto();
					billUpdateDto.setBillingMasterId(reqDto.getBillingMasterId());
					billUpdateDto.setNetAmount(netAmount);
					billUpdateDto.setConcessionAmount(concessionAmount);
					billUpdateDto.setTotalBillAmount(totalBillAmount);
					billUpdateDto.setOutstanding(dueAmount > 0.00 ? dueAmount : 0.00);
					billUpdateDto.setBillStatusId(dueAmount > 0.00 ? IBillingStatusMasterConstant.ONHOLD : IBillingStatusMasterConstant.SETTLED);
					billStatusId = (dueAmount > 0.00 ? IBillingStatusMasterConstant.ONHOLD : IBillingStatusMasterConstant.SETTLED);
				billingMasterDao.updateBillSummaryAmount(billUpdateDto);
				if(billUpdateDto.getBillStatusId()==3)
				{
					billingMasterDao.updateSaleTableBillstatus(billUpdateDto);
				}

				// update MPesa Transaction Status
				List<PaymentReceiptDetailsDto> paymentReceiptDetailDtoList = reqDto.getPaymentReceiptDetailsDtosList();
				MpesaMasterDto masterDto = new MpesaMasterDto();
				if(reqDto.getPaymentReceiptDetailsDtosList()!=null && reqDto.getPaymentReceiptDetailsDtosList().size()>0)
				{
					for (PaymentReceiptDetailsDto paymentReceiptDetailDto : paymentReceiptDetailDtoList) 
					{
						if (paymentReceiptDetailDto.getPaymentModeId() == 5  && (paymentReceiptDetailDto.getSkipMpesaValidation()!=null && paymentReceiptDetailDto.getSkipMpesaValidation())) 
						{
							masterDto.setMpesaTxcode(paymentReceiptDetailDto.getCardNo());
							masterDto.setUnitId(reqDto.getUnitId());
							masterDto.setOrganizationId(reqDto.getOrgnizationId());
							masterDto.setUpdatedBy(paymentReceiptDetailDto.getCreatedBy());
							masterDto.setMpesaAmount(paymentReceiptDetailDto.getAmount());
							Response mpesaRes = iMpesaMasterDao.validateMpesaTrancastion(masterDto);
							
						}
						PesapalPaymentDto pesapalPaymentDto = new PesapalPaymentDto();
						if (paymentReceiptDetailDto.getPaymentModeId() == 9) 
						{
							pesapalPaymentDto.setTransactionId(paymentReceiptDetailDto.getTransactionNo());
							pesapalPaymentDto.setOrganizationId(paymentReceiptDetailDto.getOrgnizationId());
							pesapalPaymentDto.setUnitId(paymentReceiptDetailDto.getUnitId());
							pesapalPaymentDto.setUpdatedBy(paymentReceiptDetailDto.getUpdatedBy());
							pesapalPaymentDto.setTransactionStatus("CONSUMED");
							Response resPesaPal = pesaPalIntegrationDao.updatePesapalTransactionStatus(pesapalPaymentDto);
						}
						
		                if(paymentReceiptDetailDto.getPaymentModeId() == 12) 
		                {
		                	if(paymentReceiptDetailDto.getPaymentGatewayProviderId() == 1) {
		                		iMpesaMasterDao.updateRazorPay(paymentReceiptDetailDto.getTransactionNo());
		                	}else if(paymentReceiptDetailDto.getPaymentGatewayProviderId() == 2){
		                		iMpesaMasterDao.updateCCAvenue(paymentReceiptDetailDto.getTransactionNo(),reqDto.getBillingMasterId());
		                	}
		                }
		                if(paymentReceiptDetailDto.getPaymentModeId() == 3) 
		                {
		                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
		                	pOSMachineResponseMapperDto.setCreatedDate(reqDto.getCreatedDate());
		                	pOSMachineResponseMapperDto.setUpdatedDate(reqDto.getCreatedDate());
		                	pOSMachineResponseMapperDto.setUnitId(paymentReceiptDetailDto.getUnitId());
		                	pOSMachineResponseMapperDto.setOrganizationId(paymentReceiptDetailDto.getOrgnizationId());
		                	pOSMachineResponseMapperDto.setUpdatedBy(paymentReceiptDetailDto.getCreatedBy());
		                	pOSMachineResponseMapperDto.setIsConsumed(true);
		                	pOSMachineResponseMapperDto.setRequestedUniqueId(paymentReceiptDetailDto.getTransactionNo());
		                	
		                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
		                }
					}
				}
				
				// update bill status mapper
				BillingStatusMapperDto mapperDto = new BillingStatusMapperDto();
				BillingStatusMapperId mapperId = new BillingStatusMapperId();
					mapperId.setBillId(reqDto.getBillingMasterId());
					mapperId.setBillStatusId(billUpdateDto.getBillStatusId());
					mapperDto.setBillingStatusMapperId(mapperId);
					mapperDto.setCreatedBy(billingMasterDto.getCreatedBy());
					mapperDto.setOrgnisationId(billingMasterDto.getOrganisationId());
					mapperDto.setUnitId(billingMasterDto.getUnitId());
					mapperDto.setStatus(ACTIVE);
				iBillStatusMapperDao.saveBillStatus(mapperDto);

				// update bill payee details
				iBillPayeeDetailsDao.updateDueAmtByPayeeAndBillId(reqDto.getBillingMasterId(), 
						reqDto.getPayeeId(),
						(reqDto.getPayeeDueAmount() - reqDto.getAmount()) > 0 ? (reqDto.getPayeeDueAmount() - reqDto.getAmount()) : 0,reqDto.getAmount());

				// update is Financial Clearance Flag if self amount to be paid is settled
				// completely.
				if (reqDto.getAdmissionId() != null && seftPayeeId.equals(reqDto.getPayeeId())
						&& (reqDto.getPayeeDueAmount() - reqDto.getAmount()) <= 0
						&& reqDto.getIsInterimOrFinalBill() == 2) {
					iRequestDiscountDao.updateAdmissionsIsFinancialClearance(reqDto.getAdmissionId());
				}

				
				if(reqDto.getPatientCouponId() != null && reqDto.getPatientCouponId() >0) {
                	
					SlotMasterDto slotMasterDto = new SlotMasterDto();
					slotMasterDto.setAppointmentId(reqDto.getAppointmentId());
					slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID1);
					slotMasterDto.setUpdatedDate(reqDto.getCreatedDate());
					slotMasterDto.setUpdatedBy(reqDto.getUpdatedBy());
					iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
                    }
				
				// update appointment status
				if (reqDto.getIsPreOrPostBilling().equals('Y') && reqDto.getAppointmentId() != null	&& 
					(isPaidAppointment == 'N' || (reqDto.isTeleConsultApp()==true)) )
				{
					if (reqDto.getAppointmentId() != null && reqDto.getAppointmentId() > 0) 
					{
						
                        
                        Integer check = 0;
                        SlotMasterDto dtott = new SlotMasterDto();
                        dtott.setAppointmentId(reqDto.getAppointmentId());
                        Integer appointmentStatusId = billingMasterDao.checkAppoinmentStatussss(dtott);
                        
                        if(appointmentStatusId != null) {
                            if(appointmentStatusId == 6) {
                                check = 1;
                            }
                        }
                        if(check == 0 && reqDto.getPatientCouponId() == null) {
                        	
						SlotMasterDto slotMasterDto = new SlotMasterDto();
						slotMasterDto.setAppointmentId(reqDto.getAppointmentId());
						slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID5);
						slotMasterDto.setUpdatedDate(reqDto.getCreatedDate());
						slotMasterDto.setUpdatedBy(reqDto.getUpdatedBy());
						iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
                        }
					}

					if (reqDto.getEncounterId() != null) 
					{
						EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
						encounterMasterDto.setEncounterId(reqDto.getEncounterId());
						encounterMasterDto.setCurrentDate(reqDto.getCreatedDate());
						encounterMasterDto.setStatusId(5);
						encounterMasterDto.setUpdatedBy(reqDto.getUpdatedBy());
						encounterMasterDto.setPatientId(reqDto.getPatientId());
						encounterMasterDto.setAppointmentId(reqDto.getAppointmentId());
						encounterMasterDto.setOrganizationId(reqDto.getOrgnizationId());
						encounterMasterDto.setUnitId(reqDto.getUnitId());
						encounterMasterDto.setIsCalled('Y');
						iEncounterMasterService.updateEncounterStatus(encounterMasterDto);
						// <----For skipping nursing assessment
						if (isNursingAssessmentRequiredForUnit != null && (isNursingAssessmentRequiredForUnit == 'N'
								|| isNursingAssessmentRequiredForSpeciality == 'N')) {
							iEncounterAssignmenMapperService
									.saveEncounterAssignmentIfAssessignmentIsFalse(encounterMasterDto);

							if (isKioskRequired == 'Y')
								iEncounterMasterService.patientCalled(encounterMasterDto);
						}
						// For skipping nursing assessment---->
					}

				}
				
				billingMasterDao.updatelabSampleDetailsStatusId(reqDto.getBillingMasterId());
			}

			if (reqDto.getDisallowedAmount() != null && reqDto.getDisallowedAmount() > 0
					&& reqDto.getDisallowedAgainstInvoiceDtlsId() != null
					&& reqDto.getDisallowedAgainstInvoiceDtlsId() > 0) {
				billingMasterDao.updateBillsDisallowedAmount(reqDto.getDisallowedAmount(), reqDto.getBillingMasterId());
			}

			BigDecimal roundOffNetAmount = new BigDecimal(reqDto.getAmount()).setScale(2, RoundingMode.HALF_EVEN);
			BigDecimal roundOffSelfAmount = new BigDecimal(reqDto.getTotalSelfPayable()).setScale(2, RoundingMode.HALF_EVEN);
			BigDecimal roundOffCreditAmount = new BigDecimal(reqDto.getTotalCreditPayable()).setScale(2, RoundingMode.HALF_EVEN);
			BigDecimal roundOutstandingAmount = new BigDecimal(dueAmount).setScale(2, RoundingMode.HALF_EVEN);

			SmsUtilityConfigInfoDto smsInfo1 = null;
			  
			if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
					&& reqDto.getIsInterimOrFinalBill() == 2 && reqDto.getVisitTypeId() == OP) {
				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setUnitId(reqDto.getUnitId());
				smsInfo.setOrganizationId(reqDto.getOrgnizationId());
				smsInfo.setPatientId(reqDto.getPatientId());
				smsInfo.setConsultingDoctorId(reqDto.getDoctorId());
				smsInfo.setBillAmount(roundOffNetAmount);
				smsInfo.setSelfAmount(roundOffSelfAmount);
				smsInfo.setCreditAmount(roundOffCreditAmount);
				smsInfo.setOutstandingAmount(roundOutstandingAmount);
				smsInfo.setDateTime(reqDto.getDateOfReciept());
				smsInfo.setReferringDoctorId(reqDto.getReferalDocId());
				smsInfo.setRecieptNo(savePaymentres.getCode()!=null ? (String) savePaymentres.getCode() : null);
				smsInfo.setBillNo(reqDto.getBillNumber()!=null ? reqDto.getBillNumber():null);
				smsInfo.setBillId(reqDto.getBillingMasterId());
				smsInfo.setRecieptId(recieptId);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.OPBILL, smsInfo);
				smsInfo1 = smsInfo;
				//smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.OPBILL, smsInfo );
			}
			if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
					&& reqDto.getIsInterimOrFinalBill() == 2 && reqDto.getVisitTypeId() != OP) {
				dto = billingMasterDao.getBillSumsAdmissionId(reqDto.getAdmissionId());

				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setUnitId(reqDto.getUnitId());
				smsInfo.setOrganizationId(reqDto.getOrgnizationId());
				smsInfo.setPatientId(reqDto.getPatientId());
				smsInfo.setTreatingDoctorId(reqDto.getDoctorId());
				smsInfo.setBillAmount(new BigDecimal(dto.getNetAmount()).setScale(2, RoundingMode.HALF_EVEN));
				smsInfo.setSelfAmount(
						new BigDecimal(dto.getTotalSelfPayable()).setScale(2, RoundingMode.HALF_EVEN));
				smsInfo.setCreditAmount(
						new BigDecimal(dto.getTotalCreditPayable()).setScale(2, RoundingMode.HALF_EVEN));
				smsInfo.setOutstandingAmount(
						new BigDecimal(dto.getOutstanding()).setScale(2, RoundingMode.HALF_EVEN));
				smsInfo.setDateTime(reqDto.getDateOfReciept());
				smsInfo.setReferringDoctorId(reqDto.getReferalDocId());
				smsInfo.setRecieptNo(savePaymentres.getCode()!=null ? (String) savePaymentres.getCode() : null);
				smsInfo.setBillNo(reqDto.getBillNumber()!=null ? reqDto.getBillNumber():null);
				smsInfo.setBillId(reqDto.getBillingMasterId());
				smsInfo.setRecieptId(recieptId);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.IP_FINAL_BILL, smsInfo);
			}
			if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
					&& reqDto.getIsInterimOrFinalBill() == 1
					&& reqDto.getVisitTypeId() != OP) {
				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setUnitId(reqDto.getUnitId());
				smsInfo.setOrganizationId(reqDto.getOrgnizationId());
				smsInfo.setPatientId(reqDto.getPatientId());
				smsInfo.setConsultingDoctorId(reqDto.getDoctorId());
				smsInfo.setBillAmount(roundOffNetAmount);
				smsInfo.setSelfAmount(roundOffSelfAmount);
				smsInfo.setCreditAmount(roundOffCreditAmount);
				smsInfo.setDateTime(reqDto.getDateOfReciept());
				smsInfo.setReferringDoctorId(reqDto.getReferalDocId());
				smsInfo.setRecieptNo(savePaymentres.getCode()!=null ? (String) savePaymentres.getCode() : null);
				smsInfo.setBillNo(reqDto.getBillNumber()!=null ? reqDto.getBillNumber():null);
				smsInfo.setBillId(reqDto.getBillingMasterId());
				smsInfo.setRecieptId(recieptId);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.INTERMEDIATE_BILL, smsInfo);
			}

			String receiptNo= (String) savePaymentres.getCode();
			
			BillRecieptDto dto1 = new BillRecieptDto();
			dto1.setBillId(reqDto.getBillingMasterId()); 
			dto1.setRecieptId(recieptId != null ? recieptId : 0);
			dto1.setDepositConsumptionId(depositConsumptionId != null ? depositConsumptionId : 0);
			dto1.setSmsInfo(smsInfo1);
			dto1.setReceiptNo(receiptNo);
			dto1.setBillStatusId(billStatusId);
			List<PatientLoyaltyCardMapperDto> mailList = savePaymentres.getListObject();

			for (PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto : mailList) {
				iMailingService.consumeLoyaltyAmount(patientLoyaltyCardMapperDto);
			}
			
			
			Boolean isSyncScheduling=(Boolean)sessionFactory.getCurrentSession()
					.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
					.setParameter("organizationId", reqDto.getOrgnizationId())
					.uniqueResult();
			
			String uuid = null;
			if(reqDto.getPatientId()!= null) {
			 uuid=(String)sessionFactory.getCurrentSession()
					.createSQLQuery("  select om.uuid as \"uuid\" from patient.t_patient_registration om where  om.patient_id=:patientId ")
					.setParameter("patientId", reqDto.getPatientId())
					.uniqueResult();
			}
			
			if (isSyncScheduling != null) {
				if (isSyncScheduling == true && reqDto.getAppointmentId() != null) {
					String Global_Scheduling_URI = null;
					String MY_LIFE_URI = null;
					URI url = null;
					Response resp = iScheduleLogsDao.getConnectionStringForMyLife();
					MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) resp.getObject();
					if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null
							&& connectionLinkDto.getMyLifeLink() != ""
							&& connectionLinkDto.getGlobalSchedulingLink() != null
							&& connectionLinkDto.getGlobalSchedulingLink() != "") {
						Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
						MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
					//}

					Response responseAppointment = iAppointmentMasterDao
							.getDoctorAppointmentToSync(reqDto.getAppointmentId());
					AppointmentSyncSchedulingDto appointment = (AppointmentSyncSchedulingDto) responseAppointment
							.getObject();
					if (appointment != null) {
						if (uuid == null) {
                            try{
							Response response = iPatientMasterDao.getPatientByIdForSync(reqDto.getPatientId());
							PatientSyncDto patient = (PatientSyncDto) response.getObject();
							if (patient != null) {
								url = URI.create(MY_LIFE_URI + "/sync/patients/realtime");
								HttpHeaders headers = new HttpHeaders();
								headers.set("channelId",
										(patient.getChannelId() != null && patient.getChannelId() != 0)
												? patient.getChannelId().toString()
												: "1");
								org.springframework.http.HttpEntity<PatientSyncDto> request = new org.springframework.http.HttpEntity<>(
										patient, headers);
								String stringResponse = restTemplate.postForObject(url, request, String.class);
								Response commonResponse = null;
								commonResponse = gson.fromJson(stringResponse, Response.class);
								if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
									if (commonResponse.getObject() != null)
										iPatientMasterDao.updateUuidByPatient(reqDto.getPatientId(),
												(String) commonResponse.getObject());
								}
							}

							if (appointment != null) {
								appointment.setCreatedDate(GlobalCommonDateUtils
										.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy"));
								appointment.setApplicationFlag(true);
								String stringResponse = null;
								if (appointment.getDirectEncounter() == null) {
									appointment.setDirectEncounter('N');
								}
								appointment.setAppointmentStatusId(APPOINTMENTSTATUSID1);

								if (appointment.getAppointmentStatusId() == 8) {
									appointment.setAppointmentStatusId(AppointmentStatusMasterConstant.PENDING);
								}
								if (appointment.getAppointmentStatusId() == AppointmentStatusMasterConstant.PENDING
										|| (appointment.getDirectEncounter().equals('Y')
												&& appointment.getAppointmentStatusId() == APPOINTMENTSTATUSID1)) {

									url = URI.create(Global_Scheduling_URI + "/appointment/bookAppointment");
									ObjectMapper objectMapper = new ObjectMapper();
									String json = objectMapper.writeValueAsString(appointment);
									System.out.println("Appointment SChedule JSON is:" + json);
									HttpHeaders headers = new HttpHeaders();
								String token = SmsUtility.generateJWTTokenMylife(connectionLinkDto.getGlobalSchedulingLink());
								headers.set("Authorization", "Bearer " + token);
								headers.set("userName", SmsUtility.USER_TOKEN_MYLIFE);
									headers.set("channelId",
											(appointment.getChannelId() != null && appointment.getChannelId() != 0)
													? appointment.getChannelId().toString()
													: "1");
									org.springframework.http.HttpEntity<AppointmentSyncSchedulingDto> request = new org.springframework.http.HttpEntity<>(
											appointment, headers);
									stringResponse = restTemplate.postForObject(url, request, String.class);
									Response commonResponse = null;
									commonResponse = gson.fromJson(stringResponse, Response.class);
									if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
										System.out.println("Appointment Booked Successfully");
									} else {
										System.out.println("Appointment Booking Failed");
									}
								}
							}
							
						} catch (Exception e) {
						    logger.error(e.getMessage(), e);
						    return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
						}

						} else if (appointment.getIsAppointmentFromMylife() == 'N') {

							// Response responseAppointment = iAppointmentMasterDao
							// .getDoctorAppointmentToSync(reqDto.getAppointmentId());
							// AppointmentSyncSchedulingDto appointment = (AppointmentSyncSchedulingDto)
							// responseAppointment
							// .getObject();
							if (appointment != null) {
								try{
								appointment.setCreatedDate(GlobalCommonDateUtils
										.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy"));
								appointment.setApplicationFlag(true);
								String stringResponse = null;
								if (appointment.getDirectEncounter() == null) {
									appointment.setDirectEncounter('N');
								}
								appointment.setAppointmentStatusId(APPOINTMENTSTATUSID1);

								if (appointment.getAppointmentStatusId() == 8) {
									appointment.setAppointmentStatusId(AppointmentStatusMasterConstant.PENDING);
								}
								if (appointment.getAppointmentStatusId() == AppointmentStatusMasterConstant.PENDING
										|| (appointment.getDirectEncounter().equals('Y')
												&& appointment.getAppointmentStatusId() == APPOINTMENTSTATUSID1)) {
									url = URI.create(Global_Scheduling_URI + "/appointment/bookAppointment");
									ObjectMapper objectMapper = new ObjectMapper();
									String json = objectMapper.writeValueAsString(appointment);
									System.out.println("Appointment SChedule JSON is:" + json);
									HttpHeaders headers = new HttpHeaders();
									String token = SmsUtility.generateJWTTokenMylife(connectionLinkDto.getGlobalSchedulingLink());
									headers.set("Authorization", "Bearer " + token);
									headers.set("userName", SmsUtility.USER_TOKEN_MYLIFE);
									headers.set("channelId",
											(appointment.getChannelId() != null && appointment.getChannelId() != 0)
													? appointment.getChannelId().toString()
													: "1");
									org.springframework.http.HttpEntity<AppointmentSyncSchedulingDto> request = new org.springframework.http.HttpEntity<>(
											appointment, headers);
									stringResponse = restTemplate.postForObject(url, request, String.class);
									Response commonResponse = null;
									commonResponse = gson.fromJson(stringResponse, Response.class);
									if (commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)) {
										System.out.println("Appointment Booked Successfully");
									} else {
										System.out.println("Appointment Booking Failed");
									}
								}
								
							} catch (Exception e) {
							    logger.error(e.getMessage(), e);
							    return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
							}
							}
						}

					}

				}
			}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, "Payment collected successfully !!!", null, dto1);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Transactional
	public Response updatePackageConsumptionQty(BillingMasterDto billDto, List<OrderDetailsMasterDto> listToBeAdded) {
		try {

			ListIterator<OrderDetailsMasterDto> packageItr = listToBeAdded.listIterator();
			boolean updateVisitFlag = false;
			List<Integer> packageIds = new ArrayList<Integer>();
			while (packageItr.hasNext()) {
				OrderDetailsMasterDto orderDtls = packageItr.next();
				if (orderDtls.getPackageId() != null && orderDtls.getIsPackageService() == 'Y'
						&& orderDtls.getOrdCancelled() != null && orderDtls.getOrdCancelled() == 'N') {
					// CHECK IF SERVICE FROM PACKAGE
					// GET PACKAGEID, PATIENTID, ENCOUTERID, SERVICEID --> UPDATE QTY-1
					if (orderDtls.getPackageTypeId() == 5) {
						// multi encounter package consumption if package assigned on previous encounter
						iTPackageConsumptionServiceDetailDao.updatePackageConsumptionServiceQtyMultiEnc(
								orderDtls.getPackageId(), billDto.getPatientId(), orderDtls.getServiceId(),
								orderDtls.getAdmissionId(), billDto.getVisitTypeId(), billDto.getCreatedBy());
						updateVisitFlag = true;
						packageIds.add(orderDtls.getPackageId());
					} else if (orderDtls.getPackageTypeId() == 5
							|| (orderDtls.getPackageTypeId() == 3 && orderDtls.getIsCapService() != 'Y')) {
						// FOR MULTI-ENCOUNTER && CAP
						iTPackageConsumptionServiceDetailDao.updatePackageConsumptionServiceQty(
								orderDtls.getPackageId(), billDto.getPatientId(), orderDtls.getEncounterId(),
								orderDtls.getServiceId(), orderDtls.getAdmissionId(), billDto.getVisitTypeId());
					} else if (orderDtls.getPackageTypeId() == 3 && orderDtls.getIsCapService() == 'Y') {
						// FOR MULTI-ENCOUNTER && CAP
						iTPackageConsumptionServiceDetailDao.updatePackageConsumptionCapBalanceQty(
								orderDtls.getPackageId(), billDto.getPatientId(), orderDtls.getEncounterId(),
								orderDtls.getDepartmentId(), orderDtls.getSubDepartmentId(), orderDtls.getServiceAmt(),
								orderDtls.getAdmissionId(), billDto.getVisitTypeId());
					} else if (orderDtls.getPackageTypeId() == 2) {
						if (orderDtls.getPackageEitherOrGroupId() != null) {
							// FOR EighterOr package
							iTPackageConsumptionServiceDetailDao.updatePackageConsumptionEOBalanceQty(
									orderDtls.getPackageId(), billDto.getPatientId(), orderDtls.getEncounterId(),
									orderDtls.getServiceId(), orderDtls.getAdmissionId(), billDto.getVisitTypeId());
						} else {
							iTPackageConsumptionServiceDetailDao.updatePackageConsumptionServiceQty(
									orderDtls.getPackageId(), billDto.getPatientId(), orderDtls.getEncounterId(),
									orderDtls.getServiceId(), orderDtls.getAdmissionId(), billDto.getVisitTypeId());
						}
					}
				}
			}
			if (updateVisitFlag) {
				Response packageResponse = iTPackageConsumptionMasterDao.getActiveMEPackage(billDto.getPatientId(),
						null, billDto.getVisitTypeId(), billDto.getOrganisationId(), billDto.getUnitId());
				if (packageResponse.getCode().equals(SUCCESS_CODE) && packageResponse.getListObject() != null) {
					for (Iterator iterator = packageResponse.getListObject().iterator(); iterator.hasNext();) {
						PackageConsumptionMasterResDto obj = (PackageConsumptionMasterResDto) iterator.next();
						if ((obj.getEncounterSpecialityId() != null && billDto.getEncounterSpecialityId() != null)
								&& obj.getEncounterSpecialityId().intValue() == billDto.getEncounterSpecialityId()
										.intValue()
								&& packageIds.contains(obj.getPackageId())) {
							if (obj.getEncounterId().intValue() == billDto.getEncounterId().intValue()
									&& !obj.isVisited()) {
								iTPackageConsumptionMasterDao.updateNoOfVisitsME(obj);
							} else if (obj.getEncounterId().intValue() != billDto.getEncounterId().intValue()) {
								obj.setEncounterId(billDto.getEncounterId());
								iTPackageConsumptionMasterDao.updateNoOfVisitsME(obj);
							}

						}
					}

				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response cancelUnRenderedServices(List<CancelOrderedServicesDto> cancelOrderedServicesDtoList)
			throws ApplicationException {
		try {

			ListIterator<CancelOrderedServicesDto> iterator = cancelOrderedServicesDtoList.listIterator();
			while (iterator.hasNext()) 
			{
				CancelOrderedServicesDto cancelOrderedServicesDto = iterator.next();
				billingMasterDao.cancelUnRenderedServices(cancelOrderedServicesDto);
			}
			return new Response<>(SUCCESS, null, "Services Cancelled Successfully !!", null, null);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Transactional
	public Response cancelPackage(BillingMasterDto billDto,List<OrderDetailsMasterDto> listToBeUpdated) 
	{
		try {

			ListIterator<OrderDetailsMasterDto> ordrItr = listToBeUpdated.listIterator();
			OrderDetailsMasterDto order = null;
			Integer ehcPackageId = null;
			Response packCanclRes = null;
			Integer pacakgeId = null;
			while (ordrItr.hasNext())
			{
				order = ordrItr.next();
				if (order.getOrdCancelled() != null && order.getOrdCancelled() == 'Y') 
				{
					if (order.isPackage()) 
					{
						// cancel package
						packCanclRes = iTPackageConsumptionMasterDao.cancelPackageConsumption(
								order.getPackageId(), 
								billDto.getPatientId(), 
								order.getEncounterId(),
								order.getOrdCancelReasonId(), 
								order.getOrdCancelRemark(), 
								order.getAdmissionId(),
								billDto.getVisitTypeId(),
								order.getOrderDetailsId());
						
						if(order.getPackageTypeId()!=null && order.getPackageTypeId().intValue()==1)
							ehcPackageId = order.getPackageId();
						
						pacakgeId = order.getPackageId();
			         }
		          }
	           }
			
			for (Iterator iterator = listToBeUpdated.iterator(); iterator.hasNext();) 
			{
				OrderDetailsMasterDto pkgDto = (OrderDetailsMasterDto) iterator.next();
				if(pkgDto.getPackageId()!=null && pkgDto.getPackageId().equals(ehcPackageId))
        	    {
					pkgDto.setOrdCancelled('Y');
					pkgDto.setStatus('I');
        	    }
				if(pkgDto.getForceAddRemoved()==1 && pacakgeId!=null) 
				{
				  Integer pkgId =pacakgeId;
				 Optional<OrderDetailsPackageMapperDto> result = pkgDto.getOrderDetailsPackageMapperDtosList()
					      .stream().parallel()
					      .filter(ord -> ord.getPackageId()!=null && ord.getPackageId().equals(pkgId)).findAny();
				 
					if(result.isPresent())
					{ 
						pkgDto.setForceAddRemoved(0);
					}
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listToBeUpdated, packCanclRes!=null ? (Integer)packCanclRes.getObject() : 0);
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response billListByPatientId(int orgId, int unitId, int patientId, char isDrNote)
			throws ApplicationException {
		try {
			return billingMasterDao.billListByPatientId(orgId, unitId, patientId, isDrNote);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getBillsForCreditNote(int orgId, int unitId, int patientId) throws ApplicationException {
		try {
			return billingMasterDao.getBillsForCreditNote(orgId, unitId, patientId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getIntermediateBillDetailsByAdmissionId(int admissionId) throws BillingException {
		try {
			return billingMasterDao.getIntermediateBillDetailsByAdmissionId(admissionId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getDebitNoteFilterList(BillListReqDto reqDto) throws BillingException {
		try {
			return billingMasterDao.getDebitNoteFilterList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPatientServiceOrderList(AdmissionDto admissionDto) {
		try {
			Response res = billingMasterDao.getPatientServiceOrderList(admissionDto);
			List<OrderDetailsByEncounterIdDto> orderDetailsList = res.getListObject();
			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsList) 
			{
				Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(orderDetailsByEncounterIdDto.getOrderDetailsId(),null);
				List<OrderDetailsPayeeMapperDto> list = res2.getListObject();
				orderDetailsByEncounterIdDto.setOrderDetailsPayeeMapperDtosList(res2.getListObject());
				if(list.size()>0) {
					orderDetailsByEncounterIdDto.setIsTariffRateApplicable(list.getFirst().getIsTariffRateApplicable());	
				}

				// for package
				Response packageMpprRes = iOrderDetailsMasterDao
						.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsByEncounterIdDto.getOrderDetailsId());
				List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
				orderDetailsByEncounterIdDto.setOrderDetailsPackageMapperDtosList(mpprList);

			}
			return new Response<>(SUCCESS, null, null, orderDetailsList,null);
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}


	@Override
	@Transactional
	public Response updateBillStatusByBillId(Integer statusId, int billId) throws BillingException {
		try {
			return billingMasterDao.updateBillStatusByBillId(statusId, billId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	public void transferPayments(BillingMasterDto bill) {
		try {
			// get payment reciepts
			String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bill.getUnitId()), "dd-M-yyyy HH:mm:ss");
			Response receiptRes = iPaymentRecieptMasterDao.getPaymentRecieptsByBillId(bill.getOldBillId());
			if (receiptRes.getCode().equals(SUCCESS_CODE) && receiptRes.getListObject() != null
					&& receiptRes.getListObject().size() > 0) {
				ListIterator<PaymentRecieptMasterDto> receiptItr = receiptRes.getListObject().listIterator();
				double totalReceiptAmt = 0;
				while (receiptItr.hasNext()) {
					PaymentRecieptMasterDto receipt = receiptItr.next();
					totalReceiptAmt = totalReceiptAmt + receipt.getAmount();

					// update recipt as cancel
					receipt.setCancelledBy(bill.getCreatedBy());
					receipt.setCancelRemarks("Cancelled By Addendum bill");
					receipt.setRecieptNumber(GlobalCommonDateUtils.generateRandomString());
					iPaymentRecieptMasterDao.updatePaymentReceiptAsCancelById(receipt);
				}

				// insert payment receipt master
				PaymentRecieptMasterDto newReceipt = new PaymentRecieptMasterDto();
				newReceipt.setAmount(totalReceiptAmt);
				newReceipt.setBillingMasterId(bill.getNewBillId());
				newReceipt.setCounterId(bill.getCounterId());
				newReceipt.setCreatedBy(bill.getCreatedBy());
				newReceipt.setCreatedDate(dateTime);
				newReceipt.setDateOfReciept(dateTime);
				newReceipt.setOrgnizationId(bill.getOrganisationId());
				newReceipt.setReceiptType(1);
				newReceipt.setStatus(ACTIVE);
				newReceipt.setUnitId(bill.getUnitId());
				Response paymentReceiptRes = iPaymentRecieptMasterDao.savePaymentRecieptMaster(newReceipt);

				Integer paymentReceiptId = (Integer) paymentReceiptRes.getObject();

				// get payment receipt details
				Response paymentDtlsRes = iPaymentRecieptDetailsDao
						.getPaymentReceieptDetailsForAddendumByBill(bill.getOldBillId());
				if (paymentDtlsRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && paymentDtlsRes.getListObject() != null
						&& paymentDtlsRes.getListObject().size() > 0) {

					ListIterator<PaymentReceiptDetailsDto> paymentItr = paymentDtlsRes.getListObject().listIterator();
					while (paymentItr.hasNext()) {
						PaymentReceiptDetailsDto paymentDtls = paymentItr.next();
						paymentDtls.setPaymentReceiptMasterId(paymentReceiptId);
						paymentDtls.setCreatedBy(bill.getCreatedBy());
						paymentDtls.setCreatedDate(dateTime);
						paymentDtls.setStatus(ACTIVE);
						paymentDtls.setOrgnizationId(bill.getOrganisationId());
						paymentDtls.setUnitId(bill.getUnitId());
						iPaymentRecieptDetailsDao.savePaymentRecieptDetails(paymentDtls);
					}
				}
			}
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveBillFromKiosk(EncounterMasterDto encounterMasterDto) throws BillingException {
		try {

			Integer billId = null;
			Integer billPayeeDetailsId = null;

			if (encounterMasterDto.getPatientId() == 0) {
				PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
					patientRegistrationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
					patientRegistrationDto.setUnitId(encounterMasterDto.getUnitId());
					patientRegistrationDto.setPrefixId(encounterMasterDto.getPrefixId());
					patientRegistrationDto.setFirstName(encounterMasterDto.getFirstName());
					patientRegistrationDto.setMiddleName(encounterMasterDto.getMiddleName());
					patientRegistrationDto.setLastName(encounterMasterDto.getLastName());
					patientRegistrationDto.setDob(encounterMasterDto.getDob());
					patientRegistrationDto.setGenderId(encounterMasterDto.getGenderId());
					patientRegistrationDto.setMobileNumber(encounterMasterDto.getMobileNumber());
					patientRegistrationDto.setRegistrationTypeId(5);
					patientRegistrationDto.setPatientCategoryId(encounterMasterDto.getPatientCategoryId());
					patientRegistrationDto.setCreatedBy(encounterMasterDto.getCreatedBy());
					patientRegistrationDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
					patientRegistrationDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
					patientRegistrationDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
					patientRegistrationDto.setStatus('A');
					patientRegistrationDto.setEmail(encounterMasterDto.getEmail());
					patientRegistrationDto.setIdentificationNumber(encounterMasterDto.getIdentificationNumber());
					patientRegistrationDto.setIdentificationTypeId(encounterMasterDto.getIdentificationTypeId());
				Response patientRegistraion = patientRegistrationServiceImpl.savePatientRegistration(patientRegistrationDto);
				if (patientRegistraion.getStatus().equals(SUCCESS)) {
					encounterMasterDto.setRegistrationTypeId(5);
					encounterMasterDto.setPatientId((Integer) patientRegistraion.getObject());
					encounterMasterDto.getAppointmentObject().setPatientId((Integer) patientRegistraion.getObject());
				} else
					return new Response(ERROR, null, "Patient Already Exist !!", null, null);
			}

			if (encounterMasterDto.getAppointmentId() != null && encounterMasterDto.getAppointmentId() > 0) {
				Response arrivalRes = iAppointmentSchedulingDao.markArrival(encounterMasterDto.getAppointmentId(),
						encounterMasterDto.getCurrentDate(), encounterMasterDto.getUnitId());
				encounterMasterDto.setUnitTokenNumber((Integer) arrivalRes.getObject());
			}
			encounterMasterDto.setVisitTypeId(OP);
			encounterMasterDto.setEncounterDate(GlobalCommonDateUtils.getLongDateFromStrigDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss"));
			Response encRes = iEncounterMasterService.saveEncounterMaster(encounterMasterDto);
			if (encRes.getCode().equals("409")) {
				return encRes;
			}
			EncounterMasterDto encResObj = (EncounterMasterDto) encRes.getObject();

			if(encRes.getCode().equals("Y"))
				iEncounterMasterService.updateAutorenderServicesAfterEncounter((EncounterMasterDto)encRes.getObject(),2);
			if(encRes.getListObject()!=null)
				iEncounterMasterService.saveOrderDetailsAfterEncounter((EncounterMasterDto)encRes.getObject(), (Integer)encRes.getListObject().getFirst(),2);
			
			
			// save bill call
			BillingMasterDto billingMasterDto = new BillingMasterDto();
			billingMasterDto.setPatientId(encounterMasterDto.getPatientId());
			billingMasterDto.setEncounterId(encResObj.getEncounterId());
			billingMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			billingMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
			billingMasterDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
			billingMasterDto.setUpdatedDate(encounterMasterDto.getCreatedDate());
			billingMasterDto.setOrganisationId(encounterMasterDto.getOrganizationId());
			billingMasterDto.setUnitId(encounterMasterDto.getUnitId());
			billingMasterDto.setIsFinalBill('Y');
			billingMasterDto.setOrdDoctorId(encResObj.getDoctorId());
			billingMasterDto.setVisitTypeId(OP);
			billingMasterDto.setBillDateTime(encounterMasterDto.getCreatedDate());
			billingMasterDto.setNetAmount(encounterMasterDto.getTotalBillAmt());
			billingMasterDto.setDiscountAmount(0.00);
			billingMasterDto.setConcessionAmount(0.00);
			billingMasterDto.setGst(encounterMasterDto.getTaxAmt());
			billingMasterDto.setTotalBillAmount(encounterMasterDto.getServiceAmt());
			billingMasterDto.setRoundOff(0.00);
			billingMasterDto.setIsCreditBill('N');
			billingMasterDto.setBillCancelled('N');
			billingMasterDto.setIsFinancialClearance('N');
			billingMasterDto.setBillStatusId(1);
			billingMasterDto.setSelfPayable(encounterMasterDto.getTotalBillAmt());
			billingMasterDto.setCreditPayable(0.00);
			billingMasterDto.setCancelledDiscountsList(null);
			billingMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
			billingMasterDto.setPaymentEntitlementId(SELF);
			billingMasterDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
			billingMasterDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
			billingMasterDto.setIsInterimBill('N');
			billingMasterDto.setIsDrNote('N');
			billingMasterDto.setTotalBillAmount(encounterMasterDto.getTotalBillAmt());
			// billingMasterDto.setCounterId(null); ask vishal
			billingMasterDto.setEncounterSpecialityId(encResObj.getSpecialityId());

			List<BillPayeeDetailsDto> billPayeeDetailsDtosList = new LinkedList<>();
			BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();
			billPayeeDetailsDto.setPayeeId(encounterMasterDto.getPayeeId());
			billPayeeDetailsDto.setBillAmount(encounterMasterDto.getTotalBillAmt());
			billPayeeDetailsDtosList.add(billPayeeDetailsDto);
			billingMasterDto.setBillPayeeDetailsDtosList(billPayeeDetailsDtosList);

			Response billRes = saveBill(billingMasterDto);

			if (billRes.getStatus().equals(SUCCESS)) {
				billId = (Integer) billRes.getObject();
				billPayeeDetailsId = Integer.parseInt(billRes.getCode());
			}

			BillPaymentCollectionReqDto paymentCollectionDto = new BillPaymentCollectionReqDto();
			paymentCollectionDto.setBillingMasterId(billId);
			paymentCollectionDto.setBillPayeeDetailsId(billPayeeDetailsId);
			paymentCollectionDto.setDateOfReciept(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			paymentCollectionDto.setPayeeId(encounterMasterDto.getPayeeId());
			paymentCollectionDto.setOrgnizationId(encounterMasterDto.getOrganizationId());
			paymentCollectionDto.setUnitId(encounterMasterDto.getUnitId());
			paymentCollectionDto.setAmount(encounterMasterDto.getPaidAmount());
			paymentCollectionDto.setIsPreOrPostBilling(encounterMasterDto.getIsPreOrPostBilling());
			paymentCollectionDto.setIsFinancialClearance('N');
			paymentCollectionDto.setAppointmentId(encounterMasterDto.getAppointmentId());
			paymentCollectionDto.setEncounterId(encResObj.getEncounterId());
			paymentCollectionDto.setPaymentDepositDetailsList(null);
			paymentCollectionDto.setPayeeDueAmount(encounterMasterDto.getTotalBillAmt());
			paymentCollectionDto.setReceiptType(1);
			paymentCollectionDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			paymentCollectionDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
			paymentCollectionDto.setCreatedDate(
					GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			paymentCollectionDto.setUpdatedDate(
					GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
//					paymentCollectionDto.setIsTransferToActiveDeposit('N');
			paymentCollectionDto.setIsTransferToActiveDeposit(encounterMasterDto.getIsTransferToActiveDeposit() != null
					? encounterMasterDto.getIsTransferToActiveDeposit()
					: 'N');
			paymentCollectionDto.setTotalSelfPayable(encounterMasterDto.getTotalBillAmt());
			paymentCollectionDto.setTotalCreditPayable(0.00);
			paymentCollectionDto.setIsInterimOrFinalBill(2);
			paymentCollectionDto.setVisitTypeId(OP);
			paymentCollectionDto.setIsPaidAppointment(encounterMasterDto.getIsPaidAppointment());
			/*
			 * List<PaymentReceiptDetailsDto> receiptDetailsDtosList = new LinkedList<>();
			 * PaymentReceiptDetailsDto detailsDto = new PaymentReceiptDetailsDto();
			 * detailsDto.setPaymentModeId(5);
			 * detailsDto.setAmount(encounterMasterDto.getPaidAmount());
			 * detailsDto.setTransactionNo(encounterMasterDto.getMpesaTxcode());
			 * detailsDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			 * receiptDetailsDtosList.add(detailsDto);
			 * paymentCollectionDto.setPaymentReceiptDetailsDtosList(receiptDetailsDtosList)
			 * ;
			 */
			if (encounterMasterDto.getPaymentReceiptDetailsDtosList() != null
					&& encounterMasterDto.getPaymentReceiptDetailsDtosList().size() > 0) {
				paymentCollectionDto
						.setPaymentReceiptDetailsDtosList(encounterMasterDto.getPaymentReceiptDetailsDtosList());
			}

			/*
			 * collectPayment(paymentCollectionDto); return billRes;
			 */
			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, encRes, paymentCollectionDto);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response billlistForBookView(Integer patientId, Integer unitId, Integer orgId, String fromDate,
			String toDate) throws BillingException {
		try {
			return billingMasterDao.billlistForBookView(patientId, unitId, orgId, fromDate, toDate);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response readSmartCard(Claim claim) throws ApplicationException {
		try {

			return billingMasterDao.readSmartCard(claim);

		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	/*public void syncAppointmentForMyLife(SlotMasterDto slotMasterDto) {
		ScheduleLogsDto logDto = new ScheduleLogsDto(this);
		// insert into log table
		String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss");
		logDto.setAction(ScheduleActions.INSERT);
		logDto.setAddedBy(slotMasterDto.getUpdatedBy());
		logDto.setAddedDate(dateTime);
		logDto.setDateTime(dateTime);
		logDto.setApplication(ScheduleActions.HIS);
		logDto.setErrorCount(0);
		logDto.setPriority(ScheduleActions.PRIORITY_NORM);
		logDto.setRecordId(slotMasterDto.getAppointmentId());
		logDto.setStatus(ScheduleActions.STATUS_PENDING);
		logDto.setTableName(ScheduleTables.APPOINTMENT_MASTER);
		applicationEventPublisher.publishEvent(logDto);
	}*/

	@Override
	@Transactional
	public Response collectPaymentFromEncounter(EncounterPaymentDto encounterMasterDto,Character cashSettlement)
			throws ApplicationException, Exception {
		try {
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal selfTotalAmount = BigDecimal.ZERO;
			BigDecimal totalTaxAmount = BigDecimal.ZERO;
			BigDecimal netAmount = BigDecimal.ZERO;
			Double exceeAmount = 0.00;
			Double totalPayeeAmount = 0.00;
			Integer billId = null;
			Integer billPayeeDetailsId = null;

			if(encounterMasterDto.getEncounterMasterDto().getPatientSponsorDetailsDtosList()!=null && encounterMasterDto.getEncounterMasterDto().getPatientSponsorDetailsDtosList().size()>0)
			{
				for(PatientSponsorDetailsDto patientSponsorDetailsDto : encounterMasterDto.getEncounterMasterDto().getPatientSponsorDetailsDtosList())
				{
					if(patientSponsorDetailsDto.getPriorityId()==1){
						encounterMasterDto.getEncounterMasterDto().setPaymentEntitlementId(patientSponsorDetailsDto.getPaymentEntitlementId());
						encounterMasterDto.getEncounterMasterDto().setPaymentEntitlementTypeId(patientSponsorDetailsDto.getPaymentEntitlementId());
					}
						
				}
			}
			encounterMasterDto.getEncounterMasterDto().setIsNursingAssessmentRequiredForSpeciality(encounterMasterDto.getIsNursingAssessmentRequired());
			Response encRes = iEncounterMasterService.saveEncounterMaster(encounterMasterDto.getEncounterMasterDto());

			if(encRes.getCode().equals("Y"))
				iEncounterMasterService.updateAutorenderServicesAfterEncounter((EncounterMasterDto)encRes.getObject(),2);
			if(encRes.getListObject()!=null)
				iEncounterMasterService.saveOrderDetailsAfterEncounter((EncounterMasterDto)encRes.getObject(), (Integer)encRes.getListObject().getFirst(),2);
			
			
			if (encRes.getStatus().equals(ERROR))
				return encRes;

			EncounterMasterDto encResObj = (EncounterMasterDto) encRes.getObject();
			encounterMasterDto.getEncounterMasterDto().setAppointmentId(encResObj.getAppointmentId());
			encounterMasterDto.getEncounterMasterDto().setPatientId(encResObj.getPatientId());
			ListIterator<OrderDetailsMasterDto> iterator = encounterMasterDto.getOrderDetailsList().listIterator();
			while (iterator.hasNext()) {
				OrderDetailsMasterDto detailsMasterDto = iterator.next();
				netAmount = netAmount.add(detailsMasterDto.getNetAmount());
				selfTotalAmount = selfTotalAmount.add(detailsMasterDto.getSelfPayable());
				totalTaxAmount = totalTaxAmount.add(detailsMasterDto.getTaxAmount());
				totalAmount = totalAmount.add(detailsMasterDto.getServiceAmt());
				
				
				//for package
				if(detailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && detailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0)
				{
					for (Iterator iterator2 = detailsMasterDto.getOrderDetailsPackageMapperDtosList().iterator(); iterator2.hasNext();) {
						OrderDetailsPackageMapperDto pkgMpprDto = (OrderDetailsPackageMapperDto) iterator2.next();
						if(pkgMpprDto.getIsPackageService()!=null && pkgMpprDto.getIsPackageService()=='Y' && pkgMpprDto.getPackageQty()!=null && pkgMpprDto.getPackageQty()>0)
						{
							iTPackageConsumptionCapDetailsDao.updatePackageQty(pkgMpprDto.getPacakgeConsumptionServiceDetailId(), (pkgMpprDto.getPackageQty())*-1);
						}
						
					}
				}
			}

			// save bill call
			BillingMasterDto billingMasterDto = new BillingMasterDto();
			billingMasterDto.setPatientId(encounterMasterDto.getEncounterMasterDto().getPatientId());
			billingMasterDto.setEncounterId(encResObj.getEncounterId());
			billingMasterDto.setCreatedBy(encounterMasterDto.getEncounterMasterDto().getCreatedBy());
			billingMasterDto.setCreatedDate(encounterMasterDto.getEncounterMasterDto().getCreatedDate());
			billingMasterDto.setUpdatedBy(encounterMasterDto.getEncounterMasterDto().getCreatedBy());
			billingMasterDto.setUpdatedDate(encounterMasterDto.getEncounterMasterDto().getCreatedDate());
			billingMasterDto.setOrganisationId(encounterMasterDto.getEncounterMasterDto().getOrganizationId());
			billingMasterDto.setUnitId(encounterMasterDto.getEncounterMasterDto().getUnitId());
			billingMasterDto.setIsFinalBill('Y');
			billingMasterDto.setOrdDoctorId(encResObj.getDoctorId());
			billingMasterDto.setVisitTypeId(OP);
			billingMasterDto.setBillDateTime(encounterMasterDto.getEncounterMasterDto().getCreatedDate());
			billingMasterDto.setNetAmount(netAmount.doubleValue());
			billingMasterDto.setDiscountAmount(0.00);
			billingMasterDto.setConcessionAmount(0.00);
			billingMasterDto.setGst(totalTaxAmount.doubleValue());
			billingMasterDto.setTotalBillAmount(totalAmount.doubleValue());
			billingMasterDto.setRoundOff(0.00);
			billingMasterDto.setIsCreditBill('N');
			billingMasterDto.setBillCancelled('N');
			billingMasterDto.setIsFinancialClearance('N');
			billingMasterDto.setBillStatusId(1);
			billingMasterDto.setSelfPayable(selfTotalAmount.doubleValue());
			billingMasterDto.setCreditPayable(0.00);
			billingMasterDto.setCancelledDiscountsList(null);
			billingMasterDto.setTariffId(encounterMasterDto.getEncounterMasterDto().getDefaultSelfTariffId());
			billingMasterDto.setPaymentEntitlementId(SELF);
			billingMasterDto.setPatientTypeId(encounterMasterDto.getEncounterMasterDto().getPatientCategoryId());
			billingMasterDto
					.setBillingBedCategoryId(encounterMasterDto.getEncounterMasterDto().getBillingBedCategoryId());
			billingMasterDto.setIsInterimBill('N');
			billingMasterDto.setIsDrNote('N');
			billingMasterDto.setEncounterSpecialityId(encResObj.getSpecialityId());

			Response billRes = saveBill(billingMasterDto);

			if (billRes.getStatus().equals(SUCCESS)) {
				billId = (Integer) billRes.getObject();
				billPayeeDetailsId = Integer.parseInt(billRes.getCode());
			}

			BillPaymentCollectionReqDto paymentCollectionDto = new BillPaymentCollectionReqDto();
			paymentCollectionDto.setBillingMasterId(billId);
			paymentCollectionDto.setBillPayeeDetailsId(billPayeeDetailsId);
			paymentCollectionDto.setDateOfReciept(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getEncounterMasterDto().getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			paymentCollectionDto.setPayeeId(encounterMasterDto.getEncounterMasterDto().getPatientSponsorDetailsDtosList().getFirst().getPayeeId());
			paymentCollectionDto.setOrgnizationId(encounterMasterDto.getEncounterMasterDto().getOrganizationId());
			paymentCollectionDto.setUnitId(encounterMasterDto.getEncounterMasterDto().getUnitId());
			paymentCollectionDto.setIsPreOrPostBilling(encounterMasterDto.getEncounterMasterDto().getIsPreOrPostBilling());
			paymentCollectionDto.setIsFinancialClearance('N');
			paymentCollectionDto.setAppointmentId(encounterMasterDto.getEncounterMasterDto().getAppointmentId());
			paymentCollectionDto.setEncounterId(encResObj.getEncounterId());
			paymentCollectionDto.setPatientId(encounterMasterDto.getEncounterMasterDto().getPatientId());
			paymentCollectionDto.setPaymentDepositDetailsList(null);
			paymentCollectionDto.setPayeeDueAmount(netAmount.doubleValue());
			paymentCollectionDto.setReceiptType(1);
			paymentCollectionDto.setCreatedBy(encounterMasterDto.getEncounterMasterDto().getCreatedBy());
			paymentCollectionDto.setUpdatedBy(encounterMasterDto.getEncounterMasterDto().getCreatedBy());
			paymentCollectionDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getEncounterMasterDto().getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			paymentCollectionDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getEncounterMasterDto().getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			paymentCollectionDto.setTotalSelfPayable(selfTotalAmount.doubleValue());
			paymentCollectionDto.setTotalCreditPayable(0.00);
			paymentCollectionDto.setIsInterimOrFinalBill(2);
			paymentCollectionDto.setVisitTypeId(OP);
			paymentCollectionDto.setTotalDepositConsumption(encounterMasterDto.getTotalDepositConsumption());
			paymentCollectionDto.setPaymentDepositDetailsList(encounterMasterDto.getPaymentDepositDetailsList());
			paymentCollectionDto.setIsNursingAssessmentRequired(encounterMasterDto.getIsNursingAssessmentRequired());
			paymentCollectionDto.setOutstanding(netAmount.doubleValue());
			paymentCollectionDto.setAmount(netAmount.doubleValue());
			List<PaymentReceiptDetailsDto> receiptDetailsDtosList = new LinkedList<>();

			if(encounterMasterDto.getPaymentReceiptDetailsDtosList()!=null && encounterMasterDto.getPaymentReceiptDetailsDtosList().size()>0) 
			{
				ListIterator<PaymentReceiptDetailsDto> listIterator = encounterMasterDto.getPaymentReceiptDetailsDtosList().listIterator();
				MpesaMasterDto masterDto = new MpesaMasterDto();
				while (listIterator.hasNext()) 
				{

					PaymentReceiptDetailsDto detailsDto = listIterator.next();
					detailsDto.setPaymentModeId(detailsDto.getPaymentModeId());
					detailsDto.setAmount(detailsDto.getAmount());
					totalPayeeAmount = totalPayeeAmount + detailsDto.getAmount();
					if (netAmount.doubleValue() <= totalPayeeAmount) 
					{
						exceeAmount = exceeAmount + (totalPayeeAmount - netAmount.doubleValue());
					}
					detailsDto.setCreatedBy(detailsDto.getCreatedBy());

					if (detailsDto.getPaymentModeId() == 5 && (detailsDto.getSkipMpesaValidation()!=null && detailsDto.getSkipMpesaValidation())) 
					{
						masterDto.setMpesaTxcode(detailsDto.getCardNo());
						masterDto.setUnitId(encounterMasterDto.getEncounterMasterDto().getUnitId());
						masterDto.setOrganizationId(encounterMasterDto.getEncounterMasterDto().getOrganizationId());
						masterDto.setUpdatedBy(detailsDto.getCreatedBy());
						masterDto.setMpesaAmount(detailsDto.getAmount());
					    iMpesaMasterDao.validateMpesaTrancastion(masterDto);
						
					}
					if(detailsDto.getPaymentModeId() == 3) 
	                {
	                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
//	                	pOSMachineResponseMapperDto.setCreatedDate(dateTime);
//	                	pOSMachineResponseMapperDto.setUpdatedDate(dateTime);
	                	pOSMachineResponseMapperDto.setUnitId(detailsDto.getUnitId());
	                	pOSMachineResponseMapperDto.setOrganizationId(detailsDto.getOrgnizationId());
	                	pOSMachineResponseMapperDto.setUpdatedBy(detailsDto.getCreatedBy());
	                	pOSMachineResponseMapperDto.setIsConsumed(true);
	                	pOSMachineResponseMapperDto.setRequestedUniqueId(detailsDto.getRequestedUniqueId());
	                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
	                }
					receiptDetailsDtosList.add(detailsDto);
				}
				paymentCollectionDto.setPaymentReceiptDetailsDtosList(receiptDetailsDtosList);
				paymentCollectionDto.setAmount(totalPayeeAmount + (encounterMasterDto.getTotalDepositConsumption() != null ? encounterMasterDto.getTotalDepositConsumption(): 0.00));
				paymentCollectionDto.setIsTransferToActiveDeposit(encounterMasterDto.getTransferToActiveDeposit() != null ? encounterMasterDto.getTransferToActiveDeposit()	: 'N');
			}else if(cashSettlement=='Y')
			{
				PaymentReceiptDetailsDto detailsDto = new PaymentReceiptDetailsDto();
					detailsDto.setOrgnizationId(encounterMasterDto.getEncounterMasterDto().getOrganizationId());
					detailsDto.setUnitId(encounterMasterDto.getEncounterMasterDto().getUnitId());
					detailsDto.setPaymentModeId(1);
					detailsDto.setAmount(netAmount.doubleValue());
					detailsDto.setCreatedBy(encounterMasterDto.getEncounterMasterDto().getCreatedBy());
					detailsDto.setCreatedBy(encounterMasterDto.getEncounterMasterDto().getCreatedBy());
					detailsDto.setDateOfTransaction(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getEncounterMasterDto().getUnitId()), "dd-MM-yyyy HH:mm:ss") );
				receiptDetailsDtosList.add(detailsDto);
				
				paymentCollectionDto.setPaymentReceiptDetailsDtosList(receiptDetailsDtosList);
				paymentCollectionDto.setAmount(netAmount.doubleValue());
				paymentCollectionDto.setIsTransferToActiveDeposit('N');
				
			}else if(encounterMasterDto.getTotalDepositConsumption() != null &&  encounterMasterDto.getTotalDepositConsumption()>0){
				
				paymentCollectionDto.setAmount(encounterMasterDto.getTotalDepositConsumption());
				
			}
			
			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, encRes, paymentCollectionDto);

		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public Response submitSmartCard(SmartcardClaimDto smartcardClaimDto) throws ApplicationException, Exception {
		try {

			return billingMasterDao.submitSmartCard(smartcardClaimDto);

		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updatePayee(UpdatePayeeDto billingMasterDto) throws ApplicationException, Exception {
		try {

			
			iPatientSponsorDetailsService.inactiveSponsorDetailsForUpdatePayee(billingMasterDto.getAdmissionId(),billingMasterDto.getEncounterId());

			return iPatientSponsorDetailsService.savePatientSponsorDetails(
					billingMasterDto.getPatientSponsorDetailsDtoList(), billingMasterDto.getEncounterId(), billingMasterDto.getAdmissionId(),billingMasterDto.getSelfPayeeId());

		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateOrderDetailsForPayee(UpdatePayeeDto billingMasterDto,List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList,UnitApplicationConfigurationDto configDto) throws ApplicationException, Exception {
		try {

			List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = new LinkedList<>();
			List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtoList = new LinkedList<>();
			Date date = TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId());
			List<OrderDetailsByEncounterIdDto> pharamcyList = new ArrayList<>();
			Boolean skipPackageService = false;
			for (OrderDetailsByEncounterIdDto dto : orderDetailsByEncounterIdDtosList) 
			{
				if (dto.getServiceRendered() == 1 && !dto.getIsPackage()) 
				{
					OrderDetailsMaster masterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(dto.getOrderDetailsId());
					Response orderLogRes = iOrderDetailsMasterDao.orderDetailsLogAfterUpdatePayee(masterDto);
					if (orderLogRes.getStatus().equals(SUCCESS))
					{
						if (dto.getPeriodicityServiceOrderId() != null) {
							TPeriodicityServiceOrdersDto periodicityServiceOrdersDto =  itPeriodicityServiceOrdersDao.getTPeriodicityServicesByOrderDetailsId(dto.getOrderDetailsId());
							periodicityServiceOrdersDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
							periodicityServiceOrdersDto.setDefaultSelfTariffId(billingMasterDto.getDefaultSelfTariffId());
							AdmissionListIpdChargesResDto ipdChargesResDto = new AdmissionListIpdChargesResDto();
							ipdChargesResDto.setAdmissionId(billingMasterDto.getAdmissionId());
							ipdChargesResDto.setPatientId(periodicityServiceOrdersDto.getPatientId());
							ipdChargesResDto.setPatientCategoryId(billingMasterDto.getPatientCategoryId());
							ipdChargesResDto.setBedBillingCategoryId(billingMasterDto.getBillingBedCategoryId());
							ipdChargesResDto.setPaymentEntitlementId(billingMasterDto.getPaymentEntitlementId());
							ipdChargesResDto.setContractId(billingMasterDto.getContractId());
							periodicityServiceOrdersDto.setServiceId(masterDto.getServiceId());
							periodicityServiceOrdersDto.setChargesResDto(ipdChargesResDto);
							itPeriodicityServiceOrdersService.placeOrder(periodicityServiceOrdersDto);
						} else if (dto.getServiceMasterId() != null && (dto.getServiceMasterId().intValue() == configDto.getPharmacyServiceId().intValue())) 
						{
							pharamcyList.add(dto);
						} else {
							/*
							 * BigDecimal serviceAmt = BigDecimal.ZERO; BigDecimal taxAmt = BigDecimal.ZERO;
							 * BigDecimal netAmt = BigDecimal.ZERO;
							 */
							ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
								consumptionDto.setOrganizationId(masterDto.getOrgId());
								consumptionDto.setUnitId(masterDto.getOrgUnitId());
								consumptionDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
								consumptionDto.setPatientTypeId(billingMasterDto.getPatientCategoryId());
								consumptionDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
								consumptionDto.setPaymentEntitlementId(billingMasterDto.getPaymentEntitlementId());
								consumptionDto.setServiceId(masterDto.getServiceId());
								consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(masterDto.getOrderDate(), "yyyy-MM-dd"));
								consumptionDto.setContractId(billingMasterDto.getContractId());
								consumptionDto.setEncounterId(billingMasterDto.getEncounterId());
								consumptionDto.setAdmissionId(billingMasterDto.getAdmissionId());
								consumptionDto.setDefaultSelfTariffId(billingMasterDto.getDefaultSelfTariffId());
								consumptionDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
								consumptionDto.setPatientId(billingMasterDto.getPatientId());
								consumptionDto.setPatientClassId(billingMasterDto.getPatientClassId());
								//if((dto.getIsProcedureService()==null || dto.getIsProcedureService()=='N') && dto.getProcedureOrderMstId()==null )
								if(dto.getIsConsultationService()!=null && dto.getIsConsultationService().equals('Y'))
								{
									consumptionDto.setIsConsultationService('Y');
								}else {
									//for bill of material services 
									if(dto.getProcedureOrderMstId()!=null && dto.getProcedureOrderMstId()>0 && dto.getIsProcedureService()=='Y')
									{
										consumptionDto.setProcedureId(dto.getProcedureOrderMstId());
										consumptionDto.setServiceRate(dto.getTotalAmt().doubleValue());
									}
								}
								
							Response rateRes = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
							consumptionDto = (ContractConsumptionDto) rateRes.getObject();

							if (rateRes.getObject() != null) {
								masterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
								masterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
								masterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
								masterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
								masterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
								masterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
								masterDto.setPayeeId(consumptionDto.getPayeeId());
								masterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
								masterDto.setContractId(consumptionDto.getContractId());
								masterDto.setIsPackageService('N');
								if (dto.getIsDoctorRequired().equals('Y') || dto.getIsRefDoctorShare().equals('Y')) 
								{
									OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
									orderDetailsMasterDto.setOrdDocId(masterDto.getOrdDocId());
									orderDetailsMasterDto.setOrdDocSplId(masterDto.getOrdDocSplId());
									orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
									orderDetailsMasterDto.setOrgUnitId(masterDto.getOrgUnitId());
									orderDetailsMasterDto.setTariffId(masterDto.getTariffId());
									orderDetailsMasterDto.setOrdTotalAmt(masterDto.getOrdTotalAmt());
									orderDetailsMasterDto.setOrdDiscount(masterDto.getOrdDiscount());
									orderDetailsMasterDto.setOrdConcession(orderDetailsMasterDto.getOrdConcession());
									orderDetailsMasterDto.setDepartmentId(consumptionDto.getSpecialityId());
									orderDetailsMasterDto.setOrderDate(masterDto.getOrderDate());
									orderDetailsMasterDto.setOrderQty(masterDto.getOrderQty()!= null ? masterDto.getOrderQty() : 1);
									//<----Referal & Doctor Share logic
									Double serAmt = (masterDto.getOrdTotalAmt().subtract(masterDto.getOrdDiscount())).doubleValue();
									orderDetailsMasterDto.setDiscountOwnership(masterDto.getDiscountOwnership()!=null ? masterDto.getDiscountOwnership() : 0);
									
									if(serAmt >0 && dto.getIsDoctorRequired().equals('Y') && dto.getDoctorId() != null && dto.getDoctorId() > 0)
										masterDto.setDocSharePer(new BigDecimal(iOrderMasterService.getDoctorSharePercentage(orderDetailsMasterDto, 
														billingMasterDto.getVisitTypeId(),
														billingMasterDto.getBillingBedCategoryId(),
														billingMasterDto.getPatientCategoryId(),
														billingMasterDto.getPaymentEntitlementId())));
									if(serAmt >0 &&dto.getIsRefDoctorShare().equals('Y')
											&& billingMasterDto.getReferralId() != null)
										masterDto.setRefDocSharePer(new BigDecimal(iOrderMasterService.getDoctorReferralPercentage(
														orderDetailsMasterDto, billingMasterDto.getReferralId(),
														billingMasterDto.getVisitTypeId(),
														billingMasterDto.getBillingBedCategoryId(),
														billingMasterDto.getPatientCategoryId(),
														billingMasterDto.getPaymentEntitlementId())));
									
									if(serAmt >0 &&masterDto.getDocSharePer()!=null && (masterDto.getDiscountOwnership()!=null && masterDto.getDiscountOwnership()!=0) && (masterDto.getOrdDiscount()!=null || masterDto.getOrdConcession()!=null))
									{
										if(masterDto.getDiscountOwnership()==1) //Discount owned by doctor
										{
											masterDto.setDocSharePer(masterDto.getDocSharePer().subtract(masterDto.getOrdDiscount().add(masterDto.getOrdConcession())));
										}
									}
									
									if(serAmt >0 && masterDto.getDocSharePer()!=null && masterDto.getDocSharePer().doubleValue() > 0 && masterDto.getRefDocSharePer()!=null && masterDto.getRefDocSharePer().doubleValue() >0)
									{
										Double totalShare = masterDto.getDocSharePer().doubleValue() +  masterDto.getRefDocSharePer().doubleValue();
										
										if(masterDto.getDocSharePer().doubleValue()==serAmt)
										{
											masterDto.setRefDocSharePer(BigDecimal.ZERO);
										}else if(totalShare > serAmt)
										{
											masterDto.setRefDocSharePer(new BigDecimal(serAmt-masterDto.getDocSharePer().doubleValue()));
										}
									}
									masterDto.setHospitalShare(new BigDecimal(serAmt-((masterDto.getRefDocSharePer()!=null ? masterDto.getRefDocSharePer().doubleValue() : 0.00)+(masterDto.getDocSharePer()!=null ? masterDto.getDocSharePer().doubleValue() : 0.00))));
									//Referal & Doctor Share logic---->
								} else{
									masterDto.setDocSharePer(null);
									masterDto.setRefDocSharePer(null);
									masterDto.setHospitalShare(null);
								}
									
								orderDetailsPayeeMapperDtosList = consumptionDto.getOrderDetailsPayeeMapperDtosList();
								
								if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
									orderDetailsPackageMapperDtoList = consumptionDto.getOrderDetailsPackageMapperDtosList();
								iOrderDetailsMasterDao.saveOrderDetailsMaster(masterDto,orderDetailsPayeeMapperDtosList,orderDetailsPackageMapperDtoList);
							}
						}
					 }
				  }
			}
			return new Response<>(SUCCESS, null, "Payee Updated Successfully..!!", pharamcyList, null);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelAutoPostingAndOrederServicesByAdmission(DischargeDto dto) {
		try {

			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId());
			Integer billId = billingMasterDao.getFinalbillByAdmissionId(dto.getAdmissionId());
			if (billId > 0) {
				billingMasterDao.inactivateBillbyBillId(dto.getAdmissionId(), billId);
				Response res = billingMasterDao.getBillDetailsByBillId(billId);
				if (res.getListObject() != null && res.getListObject().size() > 0) {
					List<BillDetailsByBillIdDto> list = res.getListObject();
					for (BillDetailsByBillIdDto bdto : list) {
						billingMasterDao.cancelFinalBillForCancelDischarge(bdto.getBillingDetailsId(),
								bdto.getOrderDetailsId());
					}
				}
			}else {
				billingMasterDao.inactivateBillbyBillId(dto.getAdmissionId(), 0);
			}

			Integer orderId = null;
			Response orderDetailsRes = iOrderMasterDao.getAutoPostingOrderedByAdmittionAndDate(dto.getAdmissionId(),
					date);
			if (orderDetailsRes.getListObject() != null && orderDetailsRes.getListObject().size() > 0) 
			{
				List<OrderDetailsMasterDto> list = orderDetailsRes.getListObject();
				Integer ordId = list.getFirst().getOrderId();

				OrderMasterDto orderMasterDto = new OrderMasterDto();
					orderMasterDto.setOrderId(ordId);
					orderMasterDto.setOrgId(dto.getOrganizationId());
					orderMasterDto.setOrgUnitId(dto.getUnitId());
					orderMasterDto.setOrderDate(date.getTime());
					orderMasterDto.setCreatedBy(dto.getCreatedBy());
					orderMasterDto.setCreatedDate(date.getTime());
					orderMasterDto.setUpdatedBy(dto.getCreatedBy());
					orderMasterDto.setUpdatedDate(date.getTime());
					orderMasterDto.setAdmissionId(dto.getAdmissionId());
					orderMasterDto.setVisitTypeId(dto.getVisitTypeId());
				Response orderMstRes = iOrderMasterService.creditNoteCancelSerivces(orderMasterDto);
				orderId = (Integer) orderMstRes.getObject();
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, orderDetailsRes.getListObject(), null);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}
		
		catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelDischarge(DischargeDto dischargeDto) {
		try {

			dischargeDto.setStatus('A');
			dischargeDto.setIsCancel('N');
			dischargeDto.setIsIcu('N');
			dischargeDto.setIsDeath('N');
			dischargeDto.setIsHandoverMedication('N');
			dischargeDto.setIsShowPatientByBed('N');
			
			Response billRes = billingMasterDao.checkBillSettledForCancelDischarge(dischargeDto);
			if (billRes.getStatus().equals(SUCCESS)) {
				Integer billStatusId = (Integer) billRes.getObject();
				    if(billStatusId!=null && billStatusId.equals(3)) {
					return new Response(ERROR, ERROR_CODE, "Bill is settled. cancel discharge will not proceed.", null, null);
				    }
				 }
			this.updatePdd(dischargeDto);
			return billRes;

		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response postServiceToEncounterBill(OrderDetailsMaster orderDetailsMasterDto,
			AdmissionsForBatchProcessDto vistDetailsObj) {
		try {
			Set<Integer> orderDetailsIdList = new LinkedHashSet<>();
			Integer billStatusId = 0;
			List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = null;
			orderDetailsIdList.add(orderDetailsMasterDto.getOrderDetailsId());
			Response res = getRenderedServicesByEncounterIdForBillSave(
					vistDetailsObj.getEncounterId() != null ? vistDetailsObj.getEncounterId()
							: vistDetailsObj.getAdmissionId(),
					vistDetailsObj.getVisitTypeId(), false, vistDetailsObj.getUnitId(), orderDetailsIdList,
					vistDetailsObj.getIsPreBilling());
			List<OrderDetailsByEncounterIdDto> listForSavingBill = res.getListObject();

			BillingMaster billingMasterObj = billingMasterDao.getBillMasterObject(vistDetailsObj.getBillId());
			billingMasterObj
					.setNetAmount(billingMasterObj.getNetAmount() + orderDetailsMasterDto.getNetAmount().doubleValue());
			billingMasterObj.setGst(billingMasterObj.getGst() + orderDetailsMasterDto.getTaxAmount().doubleValue());
			billingMasterObj.setTotalBillAmount(
					billingMasterObj.getTotalBillAmount() + orderDetailsMasterDto.getOrdTotalAmt().doubleValue());
			billingMasterObj.setOutstanding(billingMasterObj.getOutstanding() + billingMasterObj.getOutstanding());
			billingMasterObj.setSelfPayable(
					billingMasterObj.getSelfPayable() + orderDetailsMasterDto.getSelfPayable().doubleValue());
			billingMasterObj.setCreditPayable(
					billingMasterObj.getCreditPayable() + orderDetailsMasterDto.getCreditPayable().doubleValue());

			if (billingMasterObj.getOutstanding().intValue() == 0)
				billingMasterObj.setBillStatusId(SETTLED);
			else if (((billingMasterObj.getSelfPayable().equals(0.00) && (billingMasterObj.getCreditPayable() > 0.00))))
				billingMasterObj.setBillStatusId(ONHOLD);
			else
				billingMasterObj.setBillStatusId(PENDING);
			billStatusId = billingMasterObj.getBillStatusId();
			billingMasterDao.save(billingMasterObj);

			List<BillingDetailsDto> billingDetailsDtosList = new LinkedList<>();
			
			ListIterator<OrderDetailsByEncounterIdDto> listIterator = listForSavingBill.listIterator();
			while (listIterator.hasNext()) 
			{
				OrderDetailsByEncounterIdDto detailsByEncounterIdDto = listIterator.next();
				BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
				billingDetailsDto.setBillingMasterId(vistDetailsObj.getBillId());
				billingDetailsDto.setServiceId(detailsByEncounterIdDto.getServiceMasterId());
				billingDetailsDto.setRate(detailsByEncounterIdDto.getBasePrice() != null
						? Double.valueOf(detailsByEncounterIdDto.getBasePrice().doubleValue())
						: 0.00);
				billingDetailsDto.setQuantity(detailsByEncounterIdDto.getQuantity() != null
						? Double.valueOf(detailsByEncounterIdDto.getQuantity().doubleValue())
						: 0.00);
				billingDetailsDto.setConcession(detailsByEncounterIdDto.getConcession() != null
						? Double.valueOf(detailsByEncounterIdDto.getConcession().doubleValue())
						: 0.00);
				billingDetailsDto.setGrossAmount(detailsByEncounterIdDto.getTotalAmt() != null
						? Double.valueOf(detailsByEncounterIdDto.getTotalAmt().doubleValue())
						: 0.00);
				billingDetailsDto.setCoPayPercentage(detailsByEncounterIdDto.getCoPayPer() != null
						? Double.valueOf(detailsByEncounterIdDto.getCoPayPer().doubleValue())
						: Double.valueOf(0.00));
				billingDetailsDto.setUnitId(detailsByEncounterIdDto.getOrgUnitId());
				billingDetailsDto.setOrganizationId(detailsByEncounterIdDto.getOrgId());
				billingDetailsDto.setStatus('A');
				billingDetailsDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
				billingDetailsDto.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
				billingDetailsDto.setCreatedDate(orderDetailsMasterDto.getCreatedDate());
				billingDetailsDto.setUpdatedDate(orderDetailsMasterDto.getUpdatedDate());
				billingDetailsDto.setDiscount(detailsByEncounterIdDto.getDiscount() != null
						? Double.valueOf(detailsByEncounterIdDto.getDiscount().doubleValue())
						: 0.00);
				billingDetailsDto.setTaxId(detailsByEncounterIdDto.getTaxId());
				billingDetailsDto.setTaxPer(detailsByEncounterIdDto.getTaxPercentage() != null
						? Double.valueOf(detailsByEncounterIdDto.getTaxPercentage().doubleValue())
						: 0.00);
				billingDetailsDto.setTaxAmount(detailsByEncounterIdDto.getTaxAmt() != null
						? Double.valueOf(detailsByEncounterIdDto.getTaxAmt().doubleValue())
						: 0.00);
				billingDetailsDto.setNetAmt(detailsByEncounterIdDto.getNetPay() != null
						? Double.valueOf(detailsByEncounterIdDto.getNetPay().doubleValue())
						: 0.00);
				billingDetailsDto.setOrdDocId(detailsByEncounterIdDto.getDoctorId());
				billingDetailsDto.setOrdDocSplId(detailsByEncounterIdDto.getSpecialityId());
				billingDetailsDto.setDrugId(detailsByEncounterIdDto.getDrugId());
				billingDetailsDto.setBatchId(detailsByEncounterIdDto.getBatchId());
				billingDetailsDto.setSelfPayable(detailsByEncounterIdDto.getSelfPayable() != null
						? Double.valueOf(detailsByEncounterIdDto.getSelfPayable().doubleValue())
						: 0.00);
				billingDetailsDto.setCreditPayable(detailsByEncounterIdDto.getCreditPayable() != null
						? Double.valueOf(detailsByEncounterIdDto.getCreditPayable().doubleValue())
						: 0.00);
				billingDetailsDto.setPayeeId(detailsByEncounterIdDto.getPayeeId());
				billingDetailsDto.setOrderDetailsId(detailsByEncounterIdDto.getOrderDetailsId());
				billingDetailsDto.setPackageId(detailsByEncounterIdDto.getPackageId());
				billingDetailsDto.setPackageConsumptionAmt(detailsByEncounterIdDto.getPackageConsumptionAmt() != null
						? Double.valueOf(detailsByEncounterIdDto.getPackageConsumptionAmt().doubleValue())
						: Double.valueOf(0.00));
				billingDetailsDto.setCoSharePer(detailsByEncounterIdDto.getCoPayPer() != null
						? Double.valueOf(detailsByEncounterIdDto.getCoPayPer().doubleValue())
						: Double.valueOf(0.00));
				billingDetailsDto.setIsDoctorSharePosted('N');
				billingDetailsDto.setOrderDetailsPayeeMapperDtosList(
						detailsByEncounterIdDto.getOrderDetailsPayeeMapperDtosList());
				orderDetailsPayeeMapperDtosList = detailsByEncounterIdDto.getOrderDetailsPayeeMapperDtosList();
				//iBillingDetailsService.saveBillingDetails(billingDetailsDto);
				billingDetailsDtosList.add(billingDetailsDto);
			}
			if(billingDetailsDtosList.size()>0)
				iBillingDetailsService.saveBillingDetails(billingDetailsDtosList);
			
			if (orderDetailsIdList != null && orderDetailsIdList.size() > 0)
				iOrderDetailsMasterService.updateIsBilledStatus(orderDetailsIdList, vistDetailsObj.getBillId(), null);

			Response billPayeeDtlsRes = iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(vistDetailsObj.getBillId());
			List<BillPayeeDetailsResDto> listBillPayeeDetailsResDto = billPayeeDtlsRes.getListObject();


			Integer adtId = vistDetailsObj.getAdmissionId()!=null ? vistDetailsObj.getAdmissionId():0;
			Integer encId = vistDetailsObj.getEncounterId()!=null ? vistDetailsObj.getEncounterId(): 0;
			Response sponserRes =  iPatientSponsorDetailsDao.getSponsoredDetailsForUpdatePayee(adtId,encId);
			
			for (BillPayeeDetailsResDto detailsResDto : listBillPayeeDetailsResDto) {
				for (OrderDetailsPayeeMapperDto detailsPayeeMapperDto : orderDetailsPayeeMapperDtosList) {
					if (detailsPayeeMapperDto.getPayeeId() == detailsResDto.getPayeeId()) {
						detailsResDto.setBillAmount(
								detailsResDto.getBillAmount() + detailsPayeeMapperDto.getPayeeAmount().doubleValue());
						detailsResDto.setDueAmount(
								detailsResDto.getBillAmount() + detailsPayeeMapperDto.getPayeeAmount().doubleValue());
						iBillPayeeDetailsDao.updatePayeeDetailsByPayeeAndBillId(detailsResDto);
					} else {
						BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();
						billPayeeDetailsDto.setBillId(vistDetailsObj.getBillId());
						billPayeeDetailsDto.setStatus(ACTIVE);
						billPayeeDetailsDto.setPayeeId(detailsPayeeMapperDto.getPayeeId());
						billPayeeDetailsDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
						billPayeeDetailsDto.setUpdatedBy(orderDetailsMasterDto.getCreatedBy());
						billPayeeDetailsDto.setCreatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
						billPayeeDetailsDto.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
						billPayeeDetailsDto.setBillAmount(detailsPayeeMapperDto.getPayeeAmount().doubleValue());
						billPayeeDetailsDto.setDueAmount(detailsPayeeMapperDto.getPayeeAmount().doubleValue());
						billPayeeDetailsDto.setOrgId(orderDetailsMasterDto.getOrgId());
						billPayeeDetailsDto.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						
						if(sponserRes!=null && sponserRes.getListObject()!=null && sponserRes.getListObject().size()>0) {
							 List<PatientSponsorDetailsDto> sponserList = sponserRes.getListObject();
										if(sponserList!=null && sponserList.size()>0) {
											for (PatientSponsorDetailsDto patientSponsorDetailsDto : sponserList) {
												if(patientSponsorDetailsDto.getPayeeId().equals(detailsPayeeMapperDto.getPayeeId())) {
													billPayeeDetailsDto.setPriorityId(patientSponsorDetailsDto.getPriorityId()!=null ? patientSponsorDetailsDto.getPriorityId():null);
													billPayeeDetailsDto.setPaymentEntitlementId(patientSponsorDetailsDto.getPaymentEntitlementId());
												}
											}
										}
						}
						
						iBillPayeeDetailsService.saveBillPayeeDetails(billPayeeDetailsDto);
					}
				}
			}

			// save bill status logs
			BillingStatusMapperDto mapperDto = new BillingStatusMapperDto();
			BillingStatusMapperId mapperId = new BillingStatusMapperId();
			mapperId.setBillId(vistDetailsObj.getBillId());
			mapperId.setBillStatusId(billStatusId);
			mapperDto.setBillingStatusMapperId(mapperId);
			mapperDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
			mapperDto.setOrgnisationId(orderDetailsMasterDto.getOrgId());
			mapperDto.setUnitId(orderDetailsMasterDto.getOrgUnitId());
			mapperDto.setStatus(ACTIVE);
			iBillStatusMapperDao.saveBillStatus(mapperDto);

		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveBillFromKioskFutureDate(EncounterMasterDto encounterMasterDto) {
		try {

			Character isReshedule = encounterMasterDto.getIsReshedule() != null ? encounterMasterDto.getIsReshedule() : 'N';
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal selfTotalAmount = BigDecimal.ZERO;
			BigDecimal totalTaxAmount = BigDecimal.ZERO;
			BigDecimal netAmount = BigDecimal.ZERO;
			Double exceeAmount = 0.00;
			Double totalPayeeAmount = 0.00;
			Integer billId = null;
			Integer billPayeeDetailsId = null;
			if (encounterMasterDto.getPatientId() == 0) 
			{
				PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
				patientRegistrationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				patientRegistrationDto.setUnitId(encounterMasterDto.getUnitId());
				patientRegistrationDto.setPrefixId(encounterMasterDto.getPrefixId());
				patientRegistrationDto.setFirstName(encounterMasterDto.getFirstName());
				patientRegistrationDto.setMiddleName(encounterMasterDto.getMiddleName());
				patientRegistrationDto.setLastName(encounterMasterDto.getLastName());
				patientRegistrationDto.setDob(encounterMasterDto.getDob());
				patientRegistrationDto.setGenderId(encounterMasterDto.getGenderId());
				patientRegistrationDto.setMobileNumber(encounterMasterDto.getMobileNumber());
				if(encounterMasterDto.getEmail() != null) 
				{
					patientRegistrationDto.setEmail(encounterMasterDto.getEmail());
				}
				patientRegistrationDto.setRegistrationTypeId(5);
				patientRegistrationDto.setPatientCategoryId(encounterMasterDto.getPatientCategoryId());
				patientRegistrationDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				patientRegistrationDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				patientRegistrationDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
				patientRegistrationDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				patientRegistrationDto.setStatus('A');
				patientRegistrationDto.setIdentificationNumber(encounterMasterDto.getIdentificationNumber());
				patientRegistrationDto.setIdentificationTypeId(encounterMasterDto.getIdentificationTypeId());
				patientRegistrationDto.setBloodGroup(encounterMasterDto.getBloodGroup() != null ? encounterMasterDto.getBloodGroup()  : null);
				encounterMasterDto.setNewPatient(true);
				Response patientRegistraion = patientRegistrationServiceImpl.savePatientRegistration(patientRegistrationDto);
				if (patientRegistraion.getStatus().equals(SUCCESS)) {
					encounterMasterDto.setRegistrationTypeId(5);
					encounterMasterDto.setPatientId((Integer) patientRegistraion.getObject());
					encounterMasterDto.getAppointmentObject().setPatientId((Integer) patientRegistraion.getObject());
				} else
					return new Response(ERROR, null, "Patient Already Exist !!", null, null);
			}else{
				encounterMasterDto.setNewPatient(false);
			}
			
			if(isReshedule.equals('Y'))
			{
				boolean isClinqApp = iAppointmentSchedulingDao.isMyLifeAppointment(encounterMasterDto.getRescheduleAppointmentId());
				encounterMasterDto.getAppointmentObject().setIsAppointmentFromMyLife(isClinqApp ? 'Y' : 'N');
			}
			encounterMasterDto.getAppointmentObject().setIsPaidAppointment('Y');
			encounterMasterDto.getAppointmentObject().setIsKiosk('N');
			encounterMasterDto.getAppointmentObject().setGenerateToken('N');
			encounterMasterDto.getAppointmentObject().setAppointmentStatusId(APPOINTMENTSTATUSID1);
			encounterMasterDto.getAppointmentObject().setReSchedule(isReshedule);
			encounterMasterDto.setIsPaidAppointment('Y');
			encounterMasterDto.setVisitTypeId(OP);
			encounterMasterDto.setPatientCategoryId(encounterMasterDto.getPatientCategoryId());
			encounterMasterDto.setEncounterDate(GlobalCommonDateUtils.getLongDateFromStrigDate(encounterMasterDto.getAppointmentObject().getAppointmentDate(),"yyyy-MM-dd"));
			Response encRes = iEncounterMasterService.saveEncounterMaster(encounterMasterDto);
			if (encRes.getCode().equals("409")) 
			{
				return encRes;
			}
			
			if(encRes.getStatus().equals(SUCCESS) && isReshedule.equals('Y'))
			{
				iAppointmentSchedulingDao.rescheduleAppointmentForPaidAppointment(encounterMasterDto);
			}
			
			EncounterMasterDto encResObj = (EncounterMasterDto) encRes.getObject();

			
			if(encRes.getCode().equals("Y"))
				iEncounterMasterService.updateAutorenderServicesAfterEncounter((EncounterMasterDto)encRes.getObject(),2);
			if(encRes.getListObject()!=null)
				iEncounterMasterService.saveOrderDetailsAfterEncounter((EncounterMasterDto)encRes.getObject(), (Integer)encRes.getListObject().getFirst(),2);

			
			// save bill call
			BillingMasterDto billingMasterDto = new BillingMasterDto();
			billingMasterDto.setPatientId(encounterMasterDto.getPatientId());
			billingMasterDto.setEncounterId(encResObj.getEncounterId());
			// billingMasterDto.setAppointmentId(appointmentMaster.getAppointmentId());
			billingMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			billingMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
			billingMasterDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
			billingMasterDto.setUpdatedDate(encounterMasterDto.getCreatedDate());
			billingMasterDto.setOrganisationId(encounterMasterDto.getOrganizationId());
			billingMasterDto.setUnitId(encounterMasterDto.getUnitId());
			billingMasterDto.setIsFinalBill('Y');
			billingMasterDto.setOrdDoctorId(encounterMasterDto.getDoctorId());
			billingMasterDto.setVisitTypeId(OP);
			billingMasterDto.setBillDateTime(encounterMasterDto.getCreatedDate());
			if(encounterMasterDto.getDiscountAmount()!= null && encounterMasterDto.getDiscountAmount() >0){
				billingMasterDto.setNetAmount(encounterMasterDto.getTotalBillAmt() - encounterMasterDto.getDiscountAmount());
				billingMasterDto.setSelfPayable(encounterMasterDto.getTotalBillAmt()- encounterMasterDto.getDiscountAmount());
			}else{
				billingMasterDto.setNetAmount(encounterMasterDto.getTotalBillAmt());
				billingMasterDto.setSelfPayable(encounterMasterDto.getTotalBillAmt());
			}
			billingMasterDto.setTotalBillAmount(encounterMasterDto.getTotalBillAmt());
			billingMasterDto.setDiscountAmount(encounterMasterDto.getDiscountAmount()!= null ?encounterMasterDto.getDiscountAmount() : 0.00 );
			billingMasterDto.setConcessionAmount(0.00);
			billingMasterDto.setGst(encounterMasterDto.getTaxAmt());
			billingMasterDto.setRoundOff(0.00);
			billingMasterDto.setIsCreditBill('N');
			billingMasterDto.setBillCancelled('N');
			billingMasterDto.setIsFinancialClearance('N');
			billingMasterDto.setBillStatusId(1);
			billingMasterDto.setCreditPayable(0.00);
			billingMasterDto.setCancelledDiscountsList(null);
			billingMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
			billingMasterDto.setPaymentEntitlementId(SELF);
			billingMasterDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
			billingMasterDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
			billingMasterDto.setIsInterimBill('N');
			billingMasterDto.setIsDrNote('N');
			// billingMasterDto.setCounterId(null); ask vishal
			billingMasterDto.setEncounterSpecialityId(encounterMasterDto.getSpecialityId());

			List<BillPayeeDetailsDto> billPayeeDetailsDtosList = new LinkedList<>();
			BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();
			billPayeeDetailsDto.setPayeeId(encounterMasterDto.getPayeeId());
			
			billPayeeDetailsDto.setBillAmount(encounterMasterDto.getTotalBillAmt());
		
			billPayeeDetailsDtosList.add(billPayeeDetailsDto);
			billingMasterDto.setBillPayeeDetailsDtosList(billPayeeDetailsDtosList);

			Response billRes = saveBill(billingMasterDto);

			if (billRes.getStatus().equals(SUCCESS)) {
				billId = (Integer) billRes.getObject();
				billPayeeDetailsId = Integer.parseInt(billRes.getCode());
			}

			BillPaymentCollectionReqDto paymentCollectionDto = new BillPaymentCollectionReqDto();
				paymentCollectionDto.setBillingMasterId(billId);
				paymentCollectionDto.setBillPayeeDetailsId(billPayeeDetailsId);
				paymentCollectionDto.setBillNumber(billingMasterDto.getBillNumber());
				paymentCollectionDto.setDateOfReciept(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				paymentCollectionDto.setPayeeId(encounterMasterDto.getPayeeId());
				paymentCollectionDto.setOrgnizationId(encounterMasterDto.getOrganizationId());
				paymentCollectionDto.setUnitId(encounterMasterDto.getUnitId());
				paymentCollectionDto.setAmount(encounterMasterDto.getPaidAmount());
				paymentCollectionDto.setIsPreOrPostBilling(encounterMasterDto.getIsPreOrPostBilling());
				paymentCollectionDto.setIsFinancialClearance('N');
				paymentCollectionDto.setAppointmentId(encounterMasterDto.getAppointmentId());
				paymentCollectionDto.setEncounterId(encResObj.getEncounterId());
				// paymentCollectionDto.setAppointmentId(appointmentMaster.getAppointmentId());
				paymentCollectionDto.setPaymentDepositDetailsList(null);
				
				if(encounterMasterDto.getDiscountAmount()!= null && encounterMasterDto.getDiscountAmount() >0){
					paymentCollectionDto.setPayeeDueAmount(encounterMasterDto.getTotalBillAmt()- encounterMasterDto.getDiscountAmount());
					paymentCollectionDto.setTotalSelfPayable(encounterMasterDto.getTotalBillAmt()- encounterMasterDto.getDiscountAmount());
				}else{
					paymentCollectionDto.setPayeeDueAmount(encounterMasterDto.getTotalBillAmt());
					paymentCollectionDto.setTotalSelfPayable(encounterMasterDto.getTotalBillAmt());
				}
				paymentCollectionDto.setReceiptType(1);
				paymentCollectionDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				paymentCollectionDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
				paymentCollectionDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				paymentCollectionDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
	//					paymentCollectionDto.setIsTransferToActiveDeposit('N');
				paymentCollectionDto.setIsTransferToActiveDeposit(encounterMasterDto.getIsTransferToActiveDeposit() != null	? encounterMasterDto.getIsTransferToActiveDeposit()	: 'N');
				paymentCollectionDto.setTotalCreditPayable(0.00);
				paymentCollectionDto.setIsInterimOrFinalBill(2);
				paymentCollectionDto.setVisitTypeId(OP);
				List<PaymentReceiptDetailsDto> receiptDetailsDtosList = new LinkedList<>();
				
				if(encounterMasterDto.getPatientId()!=null) {
					paymentCollectionDto.setPatientId(encounterMasterDto.getPatientId());
				}
				if(encounterMasterDto.getPaymentReceiptDetailsDtosList()!=null && encounterMasterDto.getPaymentReceiptDetailsDtosList().size()>0) 
				{
					ListIterator<PaymentReceiptDetailsDto> listIterator = encounterMasterDto.getPaymentReceiptDetailsDtosList()
							.listIterator();
					MpesaMasterDto masterDto = new MpesaMasterDto();
					while (listIterator.hasNext()) 
					{
	
						PaymentReceiptDetailsDto detailsDto = listIterator.next();
						detailsDto.setPaymentModeId(detailsDto.getPaymentModeId());
						detailsDto.setAmount(detailsDto.getAmount());
						totalPayeeAmount = totalPayeeAmount + detailsDto.getAmount();
						if (netAmount.doubleValue() <= totalPayeeAmount) 
						{
							exceeAmount = exceeAmount + (totalPayeeAmount - netAmount.doubleValue());
						}
						detailsDto.setCreatedBy(detailsDto.getCreatedBy());
	
						if (detailsDto.getPaymentModeId() == 5 && (detailsDto.getSkipMpesaValidation()!=null && detailsDto.getSkipMpesaValidation())) 
						{
							masterDto.setMpesaTxcode(detailsDto.getCardNo());
							masterDto.setUnitId(encounterMasterDto.getUnitId());
							masterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
							masterDto.setUpdatedBy(detailsDto.getCreatedBy());
							masterDto.setMpesaAmount(detailsDto.getAmount());
							iMpesaMasterDao.validateMpesaTrancastion(masterDto);
							
						}
						 if(detailsDto.getPaymentModeId() == 3) 
			                {
			                	POSMachineResponseMapperDto pOSMachineResponseMapperDto=new POSMachineResponseMapperDto();
			                	pOSMachineResponseMapperDto.setCreatedDate(detailsDto.getCreatedDate());
			                	pOSMachineResponseMapperDto.setUpdatedDate(detailsDto.getUpdatedDate());
			                	pOSMachineResponseMapperDto.setUnitId(detailsDto.getUnitId());
			                	pOSMachineResponseMapperDto.setOrganizationId(detailsDto.getOrgnizationId());
			                	pOSMachineResponseMapperDto.setUpdatedBy(detailsDto.getCreatedBy());
			                	pOSMachineResponseMapperDto.setIsConsumed(true);
			                	pOSMachineResponseMapperDto.setRequestedUniqueId(detailsDto.getRequestedUniqueId());
			                	iPOSMachineResponseMapperDao.updatePOSMachineResponseMapperFlag(pOSMachineResponseMapperDto);
			                }
						receiptDetailsDtosList.add(detailsDto);
					}
					paymentCollectionDto.setPaymentReceiptDetailsDtosList(receiptDetailsDtosList);
					paymentCollectionDto.setIsTransferToActiveDeposit(encounterMasterDto.getIsTransferToActiveDeposit() != null ? encounterMasterDto.getIsTransferToActiveDeposit()	: 'N');
				}
				if(encounterMasterDto.getPaymentDepositDetailsList()!=null && encounterMasterDto.getPaymentDepositDetailsList().size()>0)
				{
					paymentCollectionDto.setTotalDepositConsumption(encounterMasterDto.getTotalDepositConsumption());
					paymentCollectionDto.setPaymentDepositDetailsList(encounterMasterDto.getPaymentDepositDetailsList());
				}
				paymentCollectionDto.setAmount(totalPayeeAmount + (encounterMasterDto.getTotalDepositConsumption() != null ? encounterMasterDto.getTotalDepositConsumption(): 0.00));
				if(encounterMasterDto.getDiscountAmount()!= null && encounterMasterDto.getDiscountAmount() >0){
					paymentCollectionDto.setOutstanding(encounterMasterDto.getTotalBillAmt()-encounterMasterDto.getDiscountAmount());
				}else{
					paymentCollectionDto.setOutstanding(encounterMasterDto.getTotalBillAmt());
				}
				
			/*
			 * collectPayment(paymentCollectionDto); return billRes;
			 */
			Integer uniqueAppointmentId = (Integer) encRes.getResObject();
			if(uniqueAppointmentId !=null) {
				if(encounterMasterDto.getDoctorId()!=null){
					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setOrganizationId(encounterMasterDto.getOrganizationId());
					smsInfo.setUnitId(encounterMasterDto.getUnitId());
					if (encounterMasterDto.getAppointmentDate() != null) {
						smsInfo.setDateTime(
								encounterMasterDto.getAppointmentDate().substring(0, 10)
										+ " at "
										+ encounterMasterDto.getFromTimeString()
						);
					}

					smsInfo.setPatientId(encounterMasterDto.getPatientId());
					smsInfo.setConsultingDoctorId(encounterMasterDto.getDoctorId());
					smsInfo.setAppointmentId(encounterMasterDto.getAppointmentId());
					smsInfo.setAppointmentUniqueId(uniqueAppointmentId);
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);

				}
			}

			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, encRes, paymentCollectionDto);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response orderPackageOnSelection(BillingMasterDto billingMasterDto) throws Exception 
	{
		try {
			List<OrderDetailsMasterDto> listToBeAdded = new ArrayList<>();
			List<OrderDetailsMasterDto> listToBeUpdated = new ArrayList<>();
			ContractConsumptionDto consumptionDto;
			boolean needToUpdatePharmacyService = false;
			Long orderDate=TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime();
			if (billingMasterDto.getOrderDetailsMasterDtosList() != null && !billingMasterDto.getOrderDetailsMasterDtosList().isEmpty())
			{
                for (OrderDetailsMasterDto orderDtls : billingMasterDto.getOrderDetailsMasterDtosList()) {
                    orderDtls.setForceAddRemoved(orderDtls.getForceAddRemoved() == null ? 0 : orderDtls.getForceAddRemoved());
                    if (orderDtls.isPackage() && orderDtls.getOrderDetailsId() == null) {
                        orderDtls.setOrderDate(orderDate);
                        orderDtls.setCreatedBy(billingMasterDto.getCreatedBy());
                        orderDtls.setUpdatedBy(billingMasterDto.getCreatedBy());
                        orderDtls.setCreatedDate(orderDate);
                        orderDtls.setUpdatedDate(orderDate);
                      /**  ServiceSearchResDto searchResDto = new ServiceSearchResDto();
                        searchResDto.setAdmissionId(billingMasterDto.getAdmissionId());
                        searchResDto.setEncounterId(billingMasterDto.getEncounterId());
                        searchResDto.setContractId(billingMasterDto.getContractId());
                        searchResDto.setServiceMasterId(orderDtls.getServiceId());
                        searchResDto.setOrderDate(orderDtls.getOrderDate() != null ? GlobalCommonDateUtils.getStringDate(orderDtls.getOrderDate(), "yyyy-MM-dd HH:mm:ss") : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(orderDtls.getOrgUnitId()), "yyyy-MM-dd HH:mm:ss"));
                        searchResDto.setOrganizationId(orderDtls.getOrgId());
                        searchResDto.setUnitId(orderDtls.getOrgUnitId());
                        searchResDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
                        searchResDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
                        searchResDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
                        searchResDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
                        searchResDto.setPackageMasterId(orderDtls.getPackageId());
                        searchResDto.setBasePrice(orderDtls.getServiceAmt());
                        searchResDto.setOrdDiscAmt(orderDtls.getOrdDiscount().doubleValue());
                        searchResDto.setPatientClassPer(orderDtls.getPatientClassPercentage() != null ? orderDtls.getPatientClassPercentage().doubleValue() : 0.00);
                        searchResDto.setApplyAuth(billingMasterDto.isAddPreAuth());
                        searchResDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());*/
                        Response resContract = iContractConsumptionService.consumePackageFromContract(billingCommonMethods.createServiceSearchResDto(billingMasterDto,orderDtls,false));
                        consumptionDto = (ContractConsumptionDto) resContract.getObject();

                        orderDtls.setCoPayPer(BigDecimal.valueOf(consumptionDto.getCoShare()));
                        orderDtls.setSelfPayable(BigDecimal.valueOf(consumptionDto.getSelfPayable()));
                        orderDtls.setCreditPayable(BigDecimal.valueOf(consumptionDto.getCreaditPayable()));
                        orderDtls.setNetAmount(BigDecimal.valueOf(consumptionDto.getFinalPrice()));
                        orderDtls.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
                        orderDtls.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());

                        needToUpdatePharmacyService = true;
                        listToBeAdded.add(orderDtls);
                    } else if (orderDtls.isPackage() && orderDtls.getStatus().equals('A')) {
                        orderDtls.setUpdatedBy(billingMasterDto.getCreatedBy());
                        orderDtls.setUpdatedDate(orderDate);
                        listToBeUpdated.add(orderDtls);
                    } else {
                        orderDtls.setUpdatedBy(billingMasterDto.getCreatedBy());
                        orderDtls.setUpdatedDate(orderDate);
                        listToBeUpdated.add(orderDtls);
                    }
                }
			}
			
			if(!listToBeAdded.isEmpty())
			{
				/**OrderMasterDto orderMasterDto = new OrderMasterDto();
					orderMasterDto.setPatientId(billingMasterDto.getPatientId());
					orderMasterDto.setEncounterId(billingMasterDto.getEncounterId());
					orderMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
					orderMasterDto.setCreatedBy(billingMasterDto.getCreatedBy());
					orderMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
					orderMasterDto.setCreatedDate(orderDate);
					orderMasterDto.setUpdatedDate(orderDate);
					orderMasterDto.setOrderDate(orderDate);
					orderMasterDto.setOrderStatus('A');
					orderMasterDto.setPriorityId(2);
					orderMasterDto.setOrgId(billingMasterDto.getOrganisationId());
					orderMasterDto.setOrgUnitId(billingMasterDto.getUnitId());
					orderMasterDto.setOrderStatusId(1);// order status==1
					orderMasterDto.setDoctorId(billingMasterDto.getOrdDoctorId());
					orderMasterDto.setListOrderDetailsMasterDto(listToBeAdded);
					orderMasterDto.setAdmissionId(billingMasterDto.getAdmissionId());
					orderMasterDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
					orderMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
					orderMasterDto.setPaymentEntitlementTypeId(billingMasterDto.getPaymentEntitlementId());
					orderMasterDto.setNetAmount(BigDecimal.valueOf(billingMasterDto.getNetAmount()));
					orderMasterDto.setReqDisAmt(billingMasterDto.getReqDisAmt());
					orderMasterDto.setDiscType(billingMasterDto.getDiscType());
					orderMasterDto.setRequestSendTo(billingMasterDto.getRequestSentTo());
					orderMasterDto.setReferralId(billingMasterDto.getReferralId());
					orderMasterDto.setLineLevelDiscountOrderList(billingMasterDto.getLineLevelDiscountOrderList());*/

				Response orderRes = iOrderMasterService.saveOrderMasterWithDiscount(billingCommonMethods.createOrderMasterDto(billingMasterDto,orderDate,listToBeAdded,false), null);
				
				Response packageConsumpationRes=this.packageConsumption(billingMasterDto, listToBeAdded, orderRes);
				return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, listToBeUpdated, packageConsumpationRes.getResObject(),needToUpdatePharmacyService);
			}
			// Update Package
			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, listToBeUpdated, null,needToUpdatePharmacyService);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updatePackageCoverSheet(BillingMasterDto billDto,Character isPreOrPostBilling,boolean isUpdatePayee,boolean isForPreAuth) throws ApplicationException, Exception {
		try {
			List<OrderDetailsByEncounterIdDto> orderDetailsList = null;
			List<OrderDetailsMasterDto> listToBeUpdated = new ArrayList<OrderDetailsMasterDto>();
			ServiceMasterDto serviceDto = new  ServiceMasterDto();
			serviceDto.setEncounterId((billDto.getEncounterId()!=null && billDto.getEncounterId()>0)?billDto.getEncounterId():billDto.getAdmissionId());
			serviceDto.setVisitTypeId(billDto.getVisitTypeId());
			Response serviceRes =null;
			
			ContractConsumptionDto contractDto = new ContractConsumptionDto();
			contractDto.setPatientId(billDto.getPatientId());
			contractDto.setEncounterId(billDto.getEncounterId()!=null?billDto.getEncounterId():billDto.getAdmissionId());
			contractDto.setBillingBedCategoryId(billDto.getBillingBedCategoryId());
			contractDto.setPatientCategoryId(billDto.getPatientTypeId());
			contractDto.setVisitTypeId(billDto.getVisitTypeId());
			contractDto.setOrganizationId(billDto.getOrganisationId());
			contractDto.setUnitId(billDto.getUnitId());
			contractDto.setTariffId(billDto.getTariffId());
			contractDto.setPaymentEntitlementId(billDto.getPaymentEntitlementId());
			contractDto.setListOrderDetailsMasterDto(listToBeUpdated);
			contractDto.setSelfPayeeId(billDto.getSelfPayeeId());
			contractDto.setPatientClassId(billDto.getPatientClassId());
		    ContractConsumptionDto contractDtoClone = contractDto;
			
		    
			
			if(isPreOrPostBilling=='Y' && billDto.getVisitTypeId().intValue()==OP)
				
			 serviceRes = this.getOrderedServicesByEncounterIdForPreBilling(serviceDto);
			else 
				 serviceRes = this.getOrderedServicesByEncounterId(serviceDto);
			if(serviceRes.getListObject()!=null && serviceRes.getListObject().size()>0)
			{
				orderDetailsList = serviceRes.getListObject();
				OrderDetailsMasterDto dto  =null;
				
				for (OrderDetailsByEncounterIdDto ordEndDto : orderDetailsList) 
				{
					 if(ordEndDto.getStatus().equals('A'))
					 {
						 dto  = new OrderDetailsMasterDto();
						 dto.setOrderDetailsId(ordEndDto.getOrderDetailsId());
						 dto.setOrgId(ordEndDto.getOrgId());
						 dto.setOrgUnitId(ordEndDto.getOrgUnitId());
						 dto.setOrderId(ordEndDto.getOrderId());
						 dto.setOrderQty(ordEndDto.getQuantity());
						 dto.setServiceId(ordEndDto.getServiceMasterId());
						 dto.setPriorityId(ordEndDto.getPriorityId());
						 dto.setIsOutsourced(ordEndDto.getIsOutsourced());
						 dto.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(ordEndDto.getOrderDateString(),"yyyy-MM-dd"));
						 dto.setOrderSchDate(GlobalCommonDateUtils.getLongDateFromStrigDate(ordEndDto.getOrderSchDateString(),"yyyy-MM-dd"));
						 dto.setOrdDocSplId(ordEndDto.getDoctorSpecialityId());
						 dto.setOrdDocId(ordEndDto.getDoctorId());
						 dto.setDepartmentId(ordEndDto.getServiceSpecialityId());
						 dto.setSubDepartmentId(ordEndDto.getServiceSubSpecialityId());
						 dto.setTaxId(ordEndDto.getTaxId());
						 dto.setTaxPer(ordEndDto.getTaxPercentage());
						 dto.setPayeeId(ordEndDto.getPayeeId());
						 dto.setOrderSetId(ordEndDto.getOrderSetId());
						 dto.setContractId(ordEndDto.getContractId());
						 dto.setBedCategoryId(ordEndDto.getBedCategoryId());
						 dto.setCoPayPer(ordEndDto.getCoPayPer());
						 dto.setDocSharePer(ordEndDto.getDocSharePer());
						 dto.setRefDocSharePer(ordEndDto.getRefDocSharePer());
						 dto.setOrdCancelBy(ordEndDto.getOrdCancelBy());
						 dto.setOrdRemarks(ordEndDto.getOrdRemarks());
						 dto.setTariffId(ordEndDto.getTariffId());
						 dto.setBillingClassId(ordEndDto.getBillingClassId());
						 dto.setBedId(ordEndDto.getBedId());
						 dto.setRoomId(ordEndDto.getRoomId());
						 dto.setWardId(ordEndDto.getWardId());
						 dto.setOrderEmergencyFlag(ordEndDto.getOrderEmergencyFlag());
						 dto.setPackageId(ordEndDto.getPackageId());
						 dto.setDrugId(ordEndDto.getDrugId());
						 dto.setBatchId(ordEndDto.getBatchId());
						 dto.setOldOrdDtlId(ordEndDto.getOldOrdDtlId());
						 dto.setServiceRendered(ordEndDto.getServiceRendered());
						 dto.setServiceChargeable(ordEndDto.getServiceChargeable());
						 dto.setServiceRenderingDeptId(ordEndDto.getServiceRenderingDeptId());
						 dto.setOrdRenderDatetime(ordEndDto.getOrdRenderDatetime());
						 dto.setServiceIsBilled(ordEndDto.getServiceIsBilled());
						 dto.setCreatedDate(ordEndDto.getCreatedDate());
						 dto.setCreatedBy(ordEndDto.getCreatedBy());
						 dto.setOrdCancelled(ordEndDto.getOrdCancelled());
						 dto.setOrdCancelReasonId(ordEndDto.getOrdCancelReasonId());
						 dto.setOrdCancelDatetime(ordEndDto.getOrdCancelDatetime());
						 dto.setStatus(ordEndDto.getStatus());
						 dto.setIsDrug(ordEndDto.getIsDrug());
						 dto.setIsPackage(ordEndDto.getIsPackage());
						 dto.setForceAddRemoved(ordEndDto.getForceAddRemoved());
						 dto.setIsConsumed(ordEndDto.getIsConsumed());
						 dto.setIsProcedureService(ordEndDto.getIsProcedureService());
						 dto.setProcedureOrderMstId(ordEndDto.getProcedureOrderMstId());
						 dto.setSaleDetailId(ordEndDto.getSaleDetailId());
						 dto.setSaleId(ordEndDto.getSaleId());
						 dto.setIsInvestigationOrder(ordEndDto.getIsInvestigationOrder());
						 dto.setIsOrderVisibleDepartment(ordEndDto.getIsOrderVisibleDepartment());
						 dto.setIsConsultationService(ordEndDto.getIsConsultationService());
						 dto.setIsPeriodicityService(ordEndDto.getPeriodicityServiceOrderId()!=null ? 'Y':'N');
						 dto.setOrdDiscount(BigDecimal.ZERO);
						 dto.setDiscountValue(BigDecimal.ZERO);
						 dto.setPatientClassPercentage(ordEndDto.getPatientClassPercentage());
						 dto.setOrdTotalAmt(ordEndDto.getBasePrice());
						 dto.setNetAmount(ordEndDto.getNetPay());
						 dto.setServiceAmt(ordEndDto.getBasePrice());
						 dto.setIsDocReq(ordEndDto.getIsDoctorRequired());
						 dto.setIsRefDoctorShare(ordEndDto.getIsRefDoctorShare());
						 dto.setDiscountOwnership(ordEndDto.getDiscountOwnership());
						 BigDecimal concessionAmt =BigDecimal.ZERO;
						 BigDecimal ordTotalAmt =BigDecimal.ZERO;
						 BigDecimal ordTaxAmt =BigDecimal.ZERO;
						 if(!isUpdatePayee)
						 {
								 dto.setDummyServiceAmt(ordEndDto.getDummyServiceAmt());
								 dto.setPatientClassPercentage(ordEndDto.getPatientClassPercentage());
								 dto.setMarkupStatus(ordEndDto.getMarkupStatus());
								 dto.setServiceAmt(ordEndDto.getBasePrice());
								 dto.setOrdTotalAmt(ordEndDto.getBasePrice());
								 dto.setOrdConcessionPer(dto.getForceAddRemoved().equals(1)?BigDecimal.ZERO:ordEndDto.getPatientClassPercentage());
								 if(ordEndDto.getPatientClassPercentage()!=null && ordEndDto.getPatientClassPercentage().doubleValue()>0)
								 {
									 concessionAmt = dto.getOrdTotalAmt().multiply((ordEndDto.getPatientClassPercentage().multiply(BigDecimal.valueOf(0.01))));
								 }
								 if(ordEndDto.getTaxPercentage()!=null && ordEndDto.getTaxPercentage().doubleValue()>0) {
									 ordTaxAmt = (dto.getOrdTotalAmt().subtract(concessionAmt)).multiply((ordEndDto.getTaxPercentage().multiply(BigDecimal.valueOf(0.01))));
									 dto.setNetAmount(dto.getOrdTotalAmt().subtract(concessionAmt).add(ordTaxAmt));
								 }
								 else {
									 dto.setNetAmount(dto.getOrdTotalAmt().subtract(concessionAmt));
								 }
								 dto.setOrdConcession(dto.getForceAddRemoved().equals(1)?BigDecimal.ZERO:concessionAmt);
								 
								 
								 dto.setTaxAmount(ordTaxAmt);
								 dto.setPackageConsumptionAmt(BigDecimal.ZERO);
								 dto.setOldNetPay(ordEndDto.getNetPay());
								 dto.setSelfPayable(dto.getNetAmount());
								 dto.setCreditPayable(BigDecimal.ZERO);
								 dto.setOldSelfPayable(ordEndDto.getSelfPayable());
								 dto.setOldCreditPayable(ordEndDto.getCreditPayable());
						  }
						else if (billDto.getPharmacyServiceId() != null && billDto.getPharmacyServiceId() > 0
								&& (dto.getServiceId().intValue() != billDto.getPharmacyServiceId())
								//&& (dto.getIsConsultationService() == null|| dto.getIsConsultationService().equals('N'))
								&& (dto.getProcedureOrderMstId() == null)
								&& (dto.getIsPeriodicityService() == null || dto.getIsPeriodicityService() == 'N')) 
						{
							 
							   contractDtoClone.setContractId(billDto.getContractId());
							   dto.setContractId(billDto.getContractId());
							   contractDtoClone.setAdmissionId(billDto.getAdmissionId());
							   
							   if(dto.getIsConsultationService()=='Y')
								   
									contractDtoClone.setIsConsultationService(dto.getIsConsultationService());
								else{
									contractDtoClone.setConsultationAmount(null);
									contractDtoClone.setIsConsultationService('N');
								}
									
							    contractDtoClone.setCurrentDate(dto.getOrderDate() == null ? GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billDto.getUnitId()), "yyyy-MM-dd")  : GlobalCommonDateUtils.getStringDate(ordEndDto.getOrderDate(), "yyyy-MM-dd"));
							    contractDtoClone.setServiceId(dto.getServiceId());
							    contractDtoClone.setPatientClassPercentage(dto.getPatientClassPercentage().doubleValue());
							    //rate by billingBedCategoryId
							    contractDtoClone.setBillingBedCategoryId(dto.getBedCategoryId()!=null && dto.getBedCategoryId()>0?dto.getBedCategoryId():billDto.getBillingBedCategoryId());
							    
							    Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contractDtoClone, false,true);
								ContractConsumptionDto globalRatesRes = (ContractConsumptionDto) serviceRateRes.getObject();
								
								dto.setCoPayPer(BigDecimal.valueOf(globalRatesRes.getCoShare()));
								dto.setSelfPayable(new BigDecimal(globalRatesRes.getSelfPayable()));
								dto.setCreditPayable(new BigDecimal(globalRatesRes.getCreaditPayable()));
								dto.setServiceAmt(new BigDecimal(globalRatesRes.getServiceRate()));
								dto.setDummyServiceAmt(new BigDecimal(globalRatesRes.getServiceRate()));
								dto.setOrdConcession(new BigDecimal(globalRatesRes.getConcession()));
								dto.setOrdTotalAmt(new BigDecimal(globalRatesRes.getServiceRate()));
								dto.setNetAmount(new BigDecimal(globalRatesRes.getFinalPrice()));
								dto.setOrdDiscount(new BigDecimal(globalRatesRes.getOrdDiscount()));
								dto.setOrdConcessionPer(BigDecimal.valueOf(globalRatesRes.getPatientClassPercentage()));
							 
							 if(ordEndDto.getPatientClassPercentage()!=null && ordEndDto.getPatientClassPercentage().doubleValue()>0)
							 {
								   concessionAmt = dto.getOrdTotalAmt().multiply((ordEndDto.getPatientClassPercentage().multiply(BigDecimal.valueOf(0.01))));
							 }
							 if(ordEndDto.getTaxPercentage()!=null && ordEndDto.getTaxPercentage().doubleValue()>0) {
								 ordTaxAmt = (dto.getOrdTotalAmt().subtract(concessionAmt)).multiply((ordEndDto.getTaxPercentage().multiply(BigDecimal.valueOf(0.01))));
								 dto.setNetAmount(dto.getOrdTotalAmt().subtract(concessionAmt).add(ordTaxAmt));
							 }
							 else {
								 dto.setNetAmount(dto.getOrdTotalAmt().subtract(concessionAmt));
							 }
							 dto.setOrdConcession(concessionAmt);
							 dto.setTaxAmount(ordTaxAmt);
							 dto.setSelfPayable(dto.getNetAmount());
						 }
						 contractDtoClone.setContractId(billDto.getContractId());
						 dto.setContractId(billDto.getContractId());
						 dto.setOldNetPay(dto.getNetAmount());
						 dto.setOldCreditPayable(dto.getNetAmount());
						 dto.setPackageConsumptionAmt(BigDecimal.ZERO);
						 dto.setCreditPayable(BigDecimal.ZERO);
						 dto.setOldCreditPayable(BigDecimal.ZERO);
						 dto.setOrderDetailsPayeeMapperDtosList(ordEndDto.getOrderDetailsPayeeMapperDtosList());
						 dto.setOrderDetailsPackageMapperDtosList(ordEndDto.getOrderDetailsPackageMapperDtosList());
				
						 if(!ordEndDto.getIsPackage() && (ordEndDto.getPackageTypeId()==null||ordEndDto.getPackageTypeId()!=1))
						 { 
							 listToBeUpdated.add(dto);
						 }
						 //pharmacy
						 /*if (billDto.getPharmacyServiceId() != null &&billDto.getPharmacyServiceId()>0 && (dto.getServiceId().intValue() == billDto.getPharmacyServiceId()) && dto.getOrdTotalAmt().doubleValue()>0) 
						 {
							pharamcyList.add(dto);
						 } */
					 }
					 
				}
				
				//set pkg mppr list null
				for (OrderDetailsMasterDto ordDtls : contractDto.getListOrderDetailsMasterDto())
				{
					if(ordDtls.getPackageTypeId()==null||ordDtls.getPackageTypeId()!=1)
					{
						if(ordDtls.getProcedureOrderMstId()!=null && ordDtls.getProcedureOrderMstId()>0 && ordDtls.getIsProcedureService()=='Y')
						{
							contractDtoClone.setServiceId(ordDtls.getProcedureOrderMstId());
							
							if(ordDtls.getOrderDetailsPayeeMapperDtosList()!=null)
					    	for(OrderDetailsPayeeMapperDto odPayeeDto : ordDtls.getOrderDetailsPayeeMapperDtosList())
									odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
							
							if(ordDtls.getOrderDetailsPackageMapperDtosList()!=null)
							for(OrderDetailsPackageMapperDto odPayeeDto : ordDtls.getOrderDetailsPackageMapperDtosList())
									odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
						}else{
							contractDtoClone.setServiceId(ordDtls.getServiceId());
						}
						
						contractDtoClone.setOrderDetailsPayeeMapperDtosList(ordDtls.getOrderDetailsPayeeMapperDtosList());
						contractDtoClone.setOrderDetailsPackageMapperDtosList(ordDtls.getOrderDetailsPackageMapperDtosList());
						contractDtoClone.setSpecialityId(ordDtls.getDepartmentId());
						//contractDtoClone.setServiceId(ordDtls.getServiceId());
						contractDtoClone.setEncounterId(billDto.getEncounterId());
						contractDtoClone.setAdmissionId(billDto.getAdmissionId());	
						contractDtoClone.setContractId(ordDtls.getContractId());
						//if(isForPreAuth==false)
							iContractConsumptionService.addCapAmount(contractDtoClone);
						
						if(!ordDtls.isPackage() && ordDtls.getOrdTotalAmt().doubleValue()>0)
						{
							ordDtls.setOrderDetailsPackageMapperDtosList(ordDtls.getForceAddRemoved().equals(1)?ordDtls.getOrderDetailsPackageMapperDtosList():null);
							ordDtls.setOrderDetailsPayeeMapperDtosList(null);
							ordDtls.setOrderDate(ordDtls.getOrderDate()==null?TimeZoneComponent.getDateByzone(billDto.getUnitId()).getTime():ordDtls.getOrderDate());
							//change by ganesh
							ordDtls.setSelfPayable(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:ordDtls.getOrdTotalAmt());
							ordDtls.setNetAmount(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:ordDtls.getNetAmount());
							ordDtls.setPackageConsumptionAmt(ordDtls.getForceAddRemoved().equals(1)?ordDtls.getOrdTotalAmt():BigDecimal.ZERO);
							ordDtls.setCreditPayable(BigDecimal.ZERO);
						}
						
					}
					billDto.setAddPreAuth(true);
				}
				
				Response packageRes = iMPackageMasterService.updatePackage(contractDto);
				if(packageRes.getObject()!=null)
				{
					contractDto =(ContractConsumptionDto) packageRes.getObject();
					
					for (OrderDetailsMasterDto ordDtlsDto : contractDto.getListOrderDetailsMasterDto()) 
					{
						if(billDto.getPharmacyServiceId() != null &&billDto.getPharmacyServiceId()>0 && (dto.getServiceId().intValue() != billDto.getPharmacyServiceId()) && ordDtlsDto.getStatus().equals('A') && ordDtlsDto.getOrdTotalAmt().doubleValue()>0)
						{
							contractDtoClone.setSpecialityId(ordDtlsDto.getDepartmentId());
							contractDtoClone.setServiceId(ordDtlsDto.getServiceId());
							contractDtoClone.setEncounterId(billDto.getEncounterId());
							contractDtoClone.setAdmissionId(billDto.getAdmissionId());	
							contractDtoClone.setTariffId(billDto.getTariffId());
							contractDtoClone.setContractId(ordDtlsDto.getContractId());
							contractDtoClone.setDefaultSelfTariffId(ordDtlsDto.getTariffId());
							contractDtoClone.setCurrentDate(ordDtlsDto.getOrderDate()!=null ? GlobalCommonDateUtils.getStringDate(ordDtlsDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss") :  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
							contractDtoClone.setUpdateCap(null);
							if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && ordDtlsDto.getOrderDetailsPackageMapperDtosList().size()>0)
								{
													
									if(ordDtlsDto.getSelfPayable().doubleValue()>0 && !ordDtlsDto.isPackage() && (ordDtlsDto.getPackageTypeId()==null||ordDtlsDto.getPackageTypeId()!=1))
									{	
										OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
										oDPmpprDto.setIsPackageService('N');
										oDPmpprDto.setIsCapService('N');
										oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
										oDPmpprDto.setServiceAmount(ordDtlsDto.getSelfPayable());
										//ordDtlsDto.setNetAmount(ordDtlsDto.getSelfPayable());
										ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(oDPmpprDto);
									}
									else {
										  OrderDetailsPayeeMapperDto payeeDto = new OrderDetailsPayeeMapperDto();
										  payeeDto.setOrderDetailsId(ordDtlsDto.getOrderDetailsId());
										  payeeDto.setPayeeAmount(BigDecimal.ZERO);
										  payeeDto.setPayeeId(billDto.getSelfPayeeId());
										  payeeDto.setStatus('A');
										  payeeDto.setOrganizationId(billDto.getOrganisationId());
										  payeeDto.setUnitId(billDto.getUnitId());
										List<OrderDetailsPayeeMapperDto> list = new ArrayList<>();
											list.add(payeeDto);
										ordDtlsDto.setOrderDetailsPayeeMapperDtosList(list);
									}
									
									if(ordDtlsDto.getSelfPayable().doubleValue()>0 && !ordDtlsDto.isPackage() && (ordDtlsDto.getPackageTypeId()==null||ordDtlsDto.getPackageTypeId()!=1))
									{
										contractDtoClone.setSelfPayable(ordDtlsDto.getSelfPayable().doubleValue());
										contractDtoClone.setOrdDiscount(ordDtlsDto.getOrdDiscount().doubleValue());
										contractDtoClone.setPatientClassPercentage(ordDtlsDto.getPatientClassPercentage().doubleValue());
										Response resContract = iContractConsumptionService.consumeServiceFromContractPackage(contractDtoClone, null);
										ContractConsumptionDto contractResp = (ContractConsumptionDto) resContract.getObject();

										ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
										ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
										ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getFinalPrice()));
										ordDtlsDto.setOrdConcession(BigDecimal.valueOf(contractResp.getConcession()));
										ordDtlsDto.setTaxAmount(BigDecimal.valueOf(contractResp.getTaxAmt()));
										ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
										ordDtlsDto.setOrdConcessionPer(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
										ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
									}
									else {
										  OrderDetailsPayeeMapperDto payeeDto = new OrderDetailsPayeeMapperDto();
										  payeeDto.setOrderDetailsId(ordDtlsDto.getOrderDetailsId());
										  payeeDto.setPayeeAmount(BigDecimal.ZERO);
										  payeeDto.setPayeeId(billDto.getSelfPayeeId());
										  payeeDto.setStatus('A');
										  payeeDto.setOrganizationId(billDto.getOrganisationId());
										  payeeDto.setUnitId(billDto.getUnitId());
										List<OrderDetailsPayeeMapperDto> list = new ArrayList<>();
											list.add(payeeDto);
										ordDtlsDto.setOrderDetailsPayeeMapperDtosList(list);
									}
									
								}
								else 
								{
									Response resContract = null;
									List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
									if(ordDtlsDto.getSelfPayable().doubleValue() >0) 
									{	
										OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
											oDPmpprDto.setIsPackageService('N');
											oDPmpprDto.setIsCapService('N');
											oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
											oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
										mppr.add(oDPmpprDto);
										ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
									}
									
									if(ordDtlsDto.getSelfPayable().doubleValue()>=0 && !ordDtlsDto.isPackage())
									{
										    contractDtoClone.setSelfPayable(ordDtlsDto.getSelfPayable().doubleValue());
											contractDtoClone.setFinalPrice(ordDtlsDto.getNetAmount().doubleValue());
											contractDtoClone.setIsConsultationService(ordDtlsDto.getIsConsultationService());
											
											contractDtoClone.setConsultationAmount(null);
											contractDtoClone.setIsPeriodicityService('N');
											contractDtoClone.setIsBOM('N');
											
											if(ordDtlsDto.getIsConsultationService()!=null && ordDtlsDto.getIsConsultationService().equals('Y'))
											{
												contractDtoClone.setConsultationAmount((ordDtlsDto.getSelfPayable().doubleValue()));
												//contractDtoClone.setOrdDiscount(ordDtlsDto.getOrdDiscount().doubleValue());
												contractDtoClone.setOrdDiscountPer((ordDtlsDto.getOrdDiscountPer().doubleValue()/(ordDtlsDto.getServiceAmt().doubleValue())-(ordDtlsDto.getOrdConcession().doubleValue()))*100);
												contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
												contractDtoClone.setConsultationAmount((ordDtlsDto.getServiceAmt().doubleValue()));
											}else if(ordDtlsDto.getIsPeriodicityService()!=null && ordDtlsDto.getIsPeriodicityService()=='Y')
											{
												contractDtoClone.setQuantity(ordDtlsDto.getOrderQty().doubleValue());
												contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
												contractDtoClone.setConsultationAmount(null);
												contractDtoClone.setIsPeriodicityService('Y');
											//resContract = iContractConsumptionService.consumeServiceFromContractForPeriodicityBilling(contractDtoClone);
											}else {
												
												contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
												contractDtoClone.setOrdDiscountPer((ordDtlsDto.getOrdDiscountPer().doubleValue()/(ordDtlsDto.getServiceAmt().doubleValue())-(ordDtlsDto.getOrdConcession().doubleValue()))*100);
												contractDtoClone.setConsultationAmount(null);
												
												if(ordDtlsDto.getProcedureOrderMstId()!=null && ordDtlsDto.getProcedureOrderMstId()>0 && ordDtlsDto.getIsProcedureService()=='Y')
												{
													contractDtoClone.setProcedureId(ordDtlsDto.getProcedureOrderMstId());
													//contractDtoClone.setServiceRate(ordDtlsDto.getServiceAmt().doubleValue());
													contractDtoClone.setIsBOM('Y');
												}else if (ordDtlsDto.getProcedureOrderMstId() != null && ordDtlsDto.getProcedureOrderMstId() > 0 && ordDtlsDto.getIsProcedureService() == 'N'){
													contractDtoClone.setProcedureId(ordDtlsDto.getProcedureOrderMstId());
													//contractDtoClone.setServiceRate(ordDtlsDto.getServiceAmt().doubleValue());
													contractDtoClone.setIsBOM('N');
												}else{
													contractDtoClone.setProcedureId(null);
													contractDtoClone.setServiceRate(null);
													contractDtoClone.setIsBOM('N');
												}
									        }
										
												contractDtoClone.setUpdateCap(null);
												contractDtoClone.setPatientClassPercentage(dto.getPatientClassPercentage().doubleValue());
											resContract = iContractConsumptionService.consumeServiceFromContract(contractDtoClone);
										
										ContractConsumptionDto contractResp =  (ContractConsumptionDto)resContract.getObject();
										
										if(ordDtlsDto.getProcedureOrderMstId()!=null && ordDtlsDto.getProcedureOrderMstId()>0 && ordDtlsDto.getIsProcedureService()=='Y')
									    {
									    	contractResp.setServiceId(ordDtlsDto.getServiceId());
									    	
									    	if(contractResp.getOrderDetailsPayeeMapperDtosList()!=null)
									    	for(OrderDetailsPayeeMapperDto odPayeeDto : contractResp.getOrderDetailsPayeeMapperDtosList())
													odPayeeDto.setServiceId(ordDtlsDto.getServiceId());
									    	
									    	if(contractResp.getOrderDetailsPackageMapperDtosList()!=null)
											for(OrderDetailsPackageMapperDto odPayeeDto : contractResp.getOrderDetailsPackageMapperDtosList())
													odPayeeDto.setServiceId(ordDtlsDto.getServiceId());
									    }
										ordDtlsDto.setIsRefDoctorShare(contractResp.getIsRefReq());
										ordDtlsDto.setIsDocReq(contractResp.getIsDocReq());
										ordDtlsDto.setCoPayPer(BigDecimal.valueOf(contractResp.getCoShare()));
										ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
										ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
										ordDtlsDto.setOrdConcession(BigDecimal.valueOf(contractResp.getConcession()));
										ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getNetAmount()));
										ordDtlsDto.setOrdDiscount(new BigDecimal(contractResp.getOrdDiscount()));
										ordDtlsDto.setOrdConcessionPer(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
										ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
										ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
									}
									else {
										  OrderDetailsPayeeMapperDto payeeDto = new OrderDetailsPayeeMapperDto();
										  payeeDto.setOrderDetailsId(ordDtlsDto.getOrderDetailsId());
										  payeeDto.setPayeeAmount(BigDecimal.ZERO);
										  payeeDto.setPayeeId(billDto.getSelfPayeeId());
										  payeeDto.setStatus('A');
										  payeeDto.setOrganizationId(billDto.getOrganisationId());
										  payeeDto.setUnitId(billDto.getUnitId());
										List<OrderDetailsPayeeMapperDto> list = new ArrayList<>();
											list.add(payeeDto);
										ordDtlsDto.setOrderDetailsPayeeMapperDtosList(list);
									}
									
								}
						}else if (billDto.getPharmacyServiceId() != null && billDto.getPharmacyServiceId()>0 && (dto.getServiceId().intValue() == billDto.getPharmacyServiceId())){
							//pharamcy
							List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
							pharamcyList.add(ordDtlsDto);
							iWalkInSalesService.updatePayeePharamcy(pharamcyList, billDto);
						}
						
					}
					billDto.setOrderDetailsMasterDtosList(contractDto.getListOrderDetailsMasterDto());
					
					OrderMasterDto orderMasterDto = new OrderMasterDto();
					orderMasterDto.setPatientId(billDto.getPatientId());
					orderMasterDto.setEncounterId(billDto.getEncounterId());
					orderMasterDto.setVisitTypeId(billDto.getVisitTypeId());
					orderMasterDto.setCreatedBy(billDto.getCreatedBy());
					orderMasterDto.setUpdatedBy(billDto.getUpdatedBy());
					orderMasterDto.setCreatedDate(billDto.getCreatedDate());
					orderMasterDto.setUpdatedDate(billDto.getUpdatedDate());
					orderMasterDto.setOrderDate(billDto.getCreatedDate());
					orderMasterDto.setOrderStatus('A');
					orderMasterDto.setPriorityId(2);
					orderMasterDto.setOrgId(billDto.getOrganisationId());
					orderMasterDto.setOrgUnitId(billDto.getUnitId());
					orderMasterDto.setOrderStatusId(1);// order status==1
					orderMasterDto.setDoctorId(billDto.getOrdDoctorId());
					orderMasterDto.setListOrderDetailsMasterDto(contractDto.getListOrderDetailsMasterDto());
					orderMasterDto.setAdmissionId(billDto.getAdmissionId());
					orderMasterDto.setBillingBedCategoryId(billDto.getBillingBedCategoryId());
					orderMasterDto.setPatientTypeId(billDto.getPatientTypeId());
					orderMasterDto.setPaymentEntitlementTypeId(billDto.getPaymentEntitlementId());
					orderMasterDto.setReqDisAmt(billDto.getReqDisAmt());
					orderMasterDto.setDiscType(billDto.getDiscType());
					orderMasterDto.setRequestSendTo(billDto.getRequestSentTo());
					orderMasterDto.setReferralId(billDto.getReferralId());
					orderMasterDto.setSelfPayeeId(billDto.getSelfPayeeId());
					orderMasterDto.setLineLevelDiscountOrderList(billDto.getLineLevelDiscountOrderList());
					//this.saveBill(billDto);	
					iOrderDetailsMasterService.updateOrderDetailsWithDiscount(contractDto.getListOrderDetailsMasterDto(), null,null, orderMasterDto,'N');
			   }
				
			}
				return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, listToBeUpdated, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getBadDebitListByFilterForBadDebitList(BillListReqDto reqDto) throws BillingException {
		try {
			return billingMasterDao.getBadDebitListByFilterForBadDebitList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response requestForBadDebit(BillingMasterDto billingMasterDto) throws BillingException {
		try {
			
				BigDecimal amt = new BigDecimal(billingMasterDto.getNetAmount());
				RequestDiscountDto requestDiscountDto = new RequestDiscountDto();
				requestDiscountDto.setOrganizationId(billingMasterDto.getOrganisationId());
				requestDiscountDto.setUnitId(billingMasterDto.getUnitId());
				requestDiscountDto.setRequestDate(
						GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
				requestDiscountDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				requestDiscountDto.setEncounterId(billingMasterDto.getEncounterId());
				requestDiscountDto.setAdmissionId(billingMasterDto.getAdmissionId());
				requestDiscountDto.setPatientId(billingMasterDto.getPatientId());
				requestDiscountDto.setNetAmount(amt);
				requestDiscountDto.setReqDisAmt(billingMasterDto.getReqDisAmt());
				requestDiscountDto.setRequestSentTo(billingMasterDto.getRequestSentTo());
				requestDiscountDto.setRequestSentBy(billingMasterDto.getCreatedBy());
				requestDiscountDto.setRequestNumber(generateRequestNumber());
				requestDiscountDto.setCreatedBy(billingMasterDto.getCreatedBy());
				requestDiscountDto.setUpdatedBy(billingMasterDto.getCreatedBy());
				requestDiscountDto.setApprovedDiscAmt(new BigDecimal("0.00"));
				requestDiscountDto.setReqStatus(1); 
				if(billingMasterDto.getRemark() != null) {
					requestDiscountDto.setRemark(billingMasterDto.getRemark());// pending	
				}
				requestDiscountDto.setRequestType(billingMasterDto.getDiscType()); // For Discount Request RequestType = DiscType 9-self 10-company/insurance
				requestDiscountDto.setBillId(billingMasterDto.getBillingMasterId());
				requestDiscountDto.setPayeeId(billingMasterDto.getSelfPayeeId()); //it's selfPayeeId parameter for both payeeId
				requestDiscountDto.setCreatedDate(
						GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
				requestDiscountDto.setUpdatedDate(
						GlobalCommonDateUtils.getStringDate(billingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
				if(billingMasterDto.gettSaleId()!=null)
				    requestDiscountDto.settSaleId(billingMasterDto.gettSaleId());
				
				Response resDis = iRequestDiscountDao.requestDiscount(requestDiscountDto);
				return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, null);
			
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response saveBillCycylicAppoinment(EncounterMasterDto encounterMasterDto,List<OrderDetailsMasterDto> orderDetailsMasterDtoList) 
	{
		try {
			BillingMasterDto billingMasterDto = new BillingMasterDto();
			billingMasterDto.setPatientId(encounterMasterDto.getPatientId());
			billingMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
			billingMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			billingMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
			billingMasterDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
			billingMasterDto.setUpdatedDate(encounterMasterDto.getCreatedDate());
			billingMasterDto.setOrganisationId(encounterMasterDto.getOrganizationId());
			billingMasterDto.setUnitId(encounterMasterDto.getUnitId());
			billingMasterDto.setIsFinalBill('Y');
			billingMasterDto.setOrdDoctorId(encounterMasterDto.getDoctorId());
			billingMasterDto.setVisitTypeId(OP);
			billingMasterDto.setBillDateTime(encounterMasterDto.getCreatedDate());
			billingMasterDto.setNetAmount(0.00);
			billingMasterDto.setDiscountAmount(0.00);
			billingMasterDto.setConcessionAmount(0.00);
			billingMasterDto.setGst(encounterMasterDto.getTaxAmt());
			billingMasterDto.setTotalBillAmount(encounterMasterDto.getServiceAmt());
			billingMasterDto.setRoundOff(0.00);
			billingMasterDto.setIsCreditBill('N');
			billingMasterDto.setBillCancelled('N');
			billingMasterDto.setIsFinancialClearance('N');
			billingMasterDto.setBillStatusId(1);
			billingMasterDto.setSelfPayable(0.00);
			billingMasterDto.setCreditPayable(0.00);
			billingMasterDto.setCancelledDiscountsList(null);
			billingMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
			billingMasterDto.setPaymentEntitlementId(encounterMasterDto.getPaymentEntitlementId());
			billingMasterDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
			billingMasterDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
			billingMasterDto.setIsInterimBill('N');
			billingMasterDto.setIsDrNote('N');
			// billingMasterDto.setCounterId(null); ask vishal
			billingMasterDto.setEncounterSpecialityId(encounterMasterDto.getSpecialityId());
			List<BillPayeeDetailsDto> billPayeeDetailsDtosList = new LinkedList<>();
			BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();
			billPayeeDetailsDto.setPayeeId(encounterMasterDto.getPayeeId());
			billPayeeDetailsDto.setBillAmount(0.00);
			billPayeeDetailsDtosList.add(billPayeeDetailsDto);
			billingMasterDto.setBillPayeeDetailsDtosList(billPayeeDetailsDtosList);
			billingMasterDto.setOrderDetailsMasterDtosList(orderDetailsMasterDtoList);
			Response billRes = saveBill(billingMasterDto);
			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, null, null, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response changePatientClassId(ServiceMasterDto serviceMasterDto) throws BillingException 
	{
		try {
			return billingMasterDao.changePatientClassId(serviceMasterDto);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getRenderedServicesByEncounterIdForBillSave(Integer encounterId, Integer visitTypeId,
			Boolean debitNoteService, Character isPreBilling) throws ApplicationException {
		try {
			
			//return billingMasterDao.getRenderedServicesByEncounterIdForBillSave(encounterId, visitTypeId, debitNoteService, isPreBilling);
			
			Response res = billingMasterDao.getRenderedServicesByEncounterIdForBillSave(encounterId, visitTypeId,
					debitNoteService, isPreBilling);
			List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = res.getListObject();

			for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList) {
				if (orderDetailsByEncounterIdDto.getStatus() == 'A') {
					Response res2 = iOrderDetailsMasterDao
							.getOrderPayeeDetailsByOrderDetailsId(orderDetailsByEncounterIdDto.getOrderDetailsId(),null);
					orderDetailsByEncounterIdDto.setOrderDetailsPayeeMapperDtosList(res2.getListObject());
				}
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, orderDetailsByEncounterIdDtosList, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}
		
	
	@Override
	@Transactional
	public Response getOrderDetailsPayeeData(Integer orderDetailsId) throws ApplicationException {
		try {

				Response res2 = iOrderDetailsMasterDao
						.getOrderPayeeDetailsByOrderDetailsId(orderDetailsId,null);
				List<OrderDetailsPayeeMapperDto> payeeDetails = res2.getListObject();

				// for package
				Response packageMpprRes = iOrderDetailsMasterDao
						.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsId);
				List<OrderDetailsPackageMapperDto> packageDetails = packageMpprRes.getListObject();

			return new Response<>(SUCCESS, null, null, payeeDetails, packageDetails);

		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response generateCyclicServiceAppointment(List<SlotMasterDto> slotMasterDto) {
		try {
			Date currentDate = TimeZoneComponent.getDateByzone(slotMasterDto.getFirst().getUnitId());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strCurrentDate = formatter.format(currentDate);
			BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;
			List<OrderDetailsMasterDto> orderDetailsMasterList = new ArrayList<OrderDetailsMasterDto>();
			List<OrderDetailsPayeeMapperDto> orderPayeeList = new ArrayList<OrderDetailsPayeeMapperDto>();
			List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList = new ArrayList<>();
			List<Integer> orderDetailsIdList = new LinkedList<>();
			Integer i = 0;
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos=null;
			EncounterMasterDto encounterMasterDto = null;
			AdmissionListIpdChargesResDto admissionListIpdChargesResDto = null;
			
			if (slotMasterDto.getFirst().getParentPackageId() != null || slotMasterDto.getFirst().getIsFromCoversheet().equals('Y')) {
			
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(slotMasterDto.getFirst().getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(slotMasterDto.getFirst().getUnitId());
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
						.getObject();
				
				ContractConsumptionDto consumptionDto = null;
				Integer referralId;
				Integer billingBedCatId;
				Integer patientCatId;
				Integer payementEntilementId;
				if(slotMasterDto.getFirst().getEncounterId()!=null) 
				{
					Response encounterRes = iEncounterMasterService.getEncounterDetailsById(slotMasterDto.getFirst().getEncounterId());
					encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
					referralId = encounterMasterDto.getReferralId();
					billingBedCatId = unitApplicationConfigurationDtos.getOpdBillingCatId();
					payementEntilementId = encounterMasterDto.getPatientSponsorDetailsDtosList().getFirst().getPaymentEntitlementId();
					patientCatId = encounterMasterDto.getPatientCategoryId();
				}
				else
				{
					Response admissionRes = ipdChargesDao.getAdmissionsById(slotMasterDto.getFirst().getAdmissionId());
					admissionListIpdChargesResDto=(AdmissionListIpdChargesResDto)admissionRes.getObject();
					referralId = admissionListIpdChargesResDto.getReferralId();
					billingBedCatId = admissionListIpdChargesResDto.getBedBillingCategoryId();
					payementEntilementId = admissionListIpdChargesResDto.getPaymentEntitlementId();
					patientCatId = admissionListIpdChargesResDto.getPatientCategoryId();
				}
			
		
				ContractConsumptionDto consumptionDtoReq = new ContractConsumptionDto();
				if(slotMasterDto.getFirst().getEncounterId()!=null) 
				{
					consumptionDtoReq.setOrganizationId(slotMasterDto.getFirst().getOrganizationId());
					consumptionDtoReq.setUnitId(slotMasterDto.getFirst().getUnitId());
					consumptionDtoReq.setVisitTypeId(OP);
					consumptionDtoReq.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
					consumptionDtoReq.setBillingBedCategoryId(unitApplicationConfigurationDtos.getOpdBillingCatId());
					consumptionDtoReq.setPaymentEntitlementId(encounterMasterDto.getPaymentEntitlementTypeId());
					consumptionDtoReq.setServiceId(slotMasterDto.getFirst().getServiceMasterId());
					//consumptionDtoReq.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd"));
					consumptionDtoReq.setContractId(encounterMasterDto.getContractId());
					consumptionDtoReq.setEncounterId(encounterMasterDto.getEncounterId());
					consumptionDtoReq.setDefaultSelfTariffId(unitApplicationConfigurationDtos.getDefaultSelfTariffId());
					consumptionDtoReq.setPatientId(encounterMasterDto.getPatientId());
					consumptionDtoReq.setSelfPayeeId(unitApplicationConfigurationDtos.getSelfPayeeId());
				    consumptionDtoReq.setPatientClassId(encounterMasterDto.getPatientClassId());	
				
				}
				else
				{
					consumptionDtoReq.setOrganizationId(slotMasterDto.getFirst().getOrganizationId());
					consumptionDtoReq.setUnitId(slotMasterDto.getFirst().getUnitId());
					consumptionDtoReq.setVisitTypeId(slotMasterDto.getFirst().getVisitTypeId());
					consumptionDtoReq.setPatientTypeId(admissionListIpdChargesResDto.getPatientCategoryId());
					consumptionDtoReq.setBillingBedCategoryId(admissionListIpdChargesResDto.getBedBillingCategoryId());
					consumptionDtoReq.setPaymentEntitlementId(admissionListIpdChargesResDto.getPaymentEntitlementId());
					consumptionDtoReq.setServiceId(slotMasterDto.getFirst().getServiceMasterId());
					//consumptionDtoReq.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd"));
					consumptionDtoReq.setContractId(admissionListIpdChargesResDto.getContractId());
					consumptionDtoReq.setAdmissionId(admissionListIpdChargesResDto.getAdmissionId());
					consumptionDtoReq.setDefaultSelfTariffId(admissionListIpdChargesResDto.getDefaultSelfTariffId());
					consumptionDtoReq.setSelfPayeeId(unitApplicationConfigurationDtos.getSelfPayeeId());
					consumptionDtoReq.setPatientId(admissionListIpdChargesResDto.getPatientId());
				}
				
			
			
			// create order detail list
			for (SlotMasterDto slotDto : slotMasterDto) {
				 consumptionDto = new ContractConsumptionDto();
				 consumptionDtoReq.setCurrentDate(slotDto.getAppointmentDate());
				  Response serviceRes =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDtoReq);
					consumptionDto=(ContractConsumptionDto)serviceRes.getObject();
					String appDate[] = slotDto.getAppointmentDate().split("-");
					String scheduleOrderDate = appDate[2] + "-" + appDate[1] + "-" + appDate[0] + " 00:00:00";
					OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
					orderDetailsMasterDto.setServiceId(slotDto.getServiceMasterId());
					orderDetailsMasterDto.setCreatedBy(slotDto.getCreatedBy());
					orderDetailsMasterDto.setUpdatedBy(slotDto.getUpdatedBy());
					orderDetailsMasterDto.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(scheduleOrderDate));
					orderDetailsMasterDto.setOrderSchDate(GlobalCommonDateUtils.getLongDateFromStrigDate(scheduleOrderDate));
					orderDetailsMasterDto.setOrdRenderDatetime(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
					orderDetailsMasterDto.setCreatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
					orderDetailsMasterDto.setUpdatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
					orderDetailsMasterDto.setStatus('A');
					orderDetailsMasterDto.setOrgId(slotDto.getOrganizationId());
					orderDetailsMasterDto.setOrgUnitId(slotDto.getUnitId());
					orderDetailsMasterDto.setOrderQty(1);
					orderDetailsMasterDto.setOrdCancelled('N');
					orderDetailsMasterDto.setIsOrderVisibleDepartment('I');
					orderDetailsMasterDto.setServiceRenderingDeptId(consumptionDto.getSubSpecialityId());
					if (slotDto.getBillId() != null) {
						orderDetailsMasterDto.setServiceRendered(0);
						orderDetailsMasterDto.setServiceIsBilled(1);
						orderDetailsMasterDto.setServiceBillId(slotDto.getBillId());
					} else {
						orderDetailsMasterDto.setServiceRendered(0);
						orderDetailsMasterDto.setServiceIsBilled(0);
					}
					orderDetailsMasterDto.setPriorityId(2);
					orderDetailsMasterDto.setIsOutsourced('N');
					orderDetailsMasterDto.setTariffId(slotDto.getDefaultSelfTariffId());
					orderDetailsMasterDto.setOrderEmergencyFlag('N');
					orderDetailsMasterDto.setIsDrug('N');
					orderDetailsMasterDto.setIsInvestigationOrder('N');
					orderDetailsMasterDto.setIsAppointableRadiology('N');
					orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					if (slotDto.getParentPackageId() != null)
					 orderDetailsMasterDto.setPackageId(slotDto.getParentPackageId());
					orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
					orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
					orderDetailsMasterDto.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
					orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
					orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
					orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
					orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
					orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
					orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
					orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
					orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
					orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
					orderDetailsMasterDto.setOrdDiscount(new BigDecimal(consumptionDto.getOrdDiscount()));
					orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
					orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
					if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
						orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						
					orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
					
					orderDetailsMasterList.add(orderDetailsMasterDto);
			}

			
			
			// save order master
				SlotMasterDto slotMasterDto1 = slotMasterDto.getFirst();
				OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setPatientId(slotMasterDto1.getPatientId());
				orderMasterDto.setCreatedBy(slotMasterDto1.getCreatedBy());
				orderMasterDto.setUpdatedBy(slotMasterDto1.getUpdatedBy());
				String appDate[] = slotMasterDto1.getAppointmentDate().split("-");
				String scheduleOrderDate = appDate[2] + "-" + appDate[1] + "-" + appDate[0] + " 00:00:00";
					orderMasterDto.setCreatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
					orderMasterDto.setUpdatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
					orderMasterDto.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(scheduleOrderDate));
					orderMasterDto.setOrderStatus('A');
					orderMasterDto.setOrgId(slotMasterDto1.getOrganizationId());
					orderMasterDto.setOrgUnitId(slotMasterDto1.getUnitId());
					orderMasterDto.setVisitTypeId(slotMasterDto1.getVisitTypeId());
					orderMasterDto.setPriorityId(2);
					orderMasterDto.setIsPharmacy('N');
					orderMasterDto.setOrderStatusId(1);//
					orderMasterDto.setReferralId(referralId);
					orderMasterDto.setPaymentEntitlementTypeId(payementEntilementId);
					orderMasterDto.setPatientCategoryId(patientCatId);
					orderMasterDto.setPatientTypeId(patientCatId);
					orderMasterDto.setBillingBedCategoryId(billingBedCatId);
				if (slotMasterDto1.getEncounterId() != null) {
					orderMasterDto.setEncounterId(slotMasterDto1.getEncounterId());
				}

				if (slotMasterDto1.getAdmissionId() != null) {
					orderMasterDto.setAdmissionId(slotMasterDto1.getAdmissionId());
				}

				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsMasterList);
				Response orderRes = iOrderMasterService.saveOrderMasterWithDiscount(orderMasterDto, null);
				orderDetailsIdList.addAll(orderRes.getListObject());


				if (slotMasterDto.getFirst().getParentPackageId() != null && slotMasterDto.getFirst().getBillId() != null) {
					OrderDetailsMasterDto orderDetailsMasterDto = orderDetailsMasterList.getFirst();
					for (i = 0; i < slotMasterDto.size(); i++) {
						BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
						billingDetailsDto.setBillingMasterId(slotMasterDto.getFirst().getBillId());
						billingDetailsDto.setServiceId(orderDetailsMasterDto.getServiceId());
						billingDetailsDto.setRate(orderDetailsMasterDto.getServiceAmt().doubleValue());
						billingDetailsDto.setQuantity(1.00);
						billingDetailsDto.setConcession(0.00);
						billingDetailsDto.setGrossAmount(orderDetailsMasterDto.getServiceAmt().doubleValue());
						billingDetailsDto.setCoPayPercentage(Double.valueOf(0.00));
						billingDetailsDto.setUnitId(slotMasterDto.getFirst().getUnitId());
						billingDetailsDto.setOrganizationId(slotMasterDto.getFirst().getOrganizationId());
						billingDetailsDto.setStatus('A');
						billingDetailsDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
						billingDetailsDto.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
						billingDetailsDto.setCreatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
						billingDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
						billingDetailsDto.setDiscount(0.00);

						if (orderDetailsMasterDto.getTaxId() != null) {
							billingDetailsDto.setTaxId(orderDetailsMasterDto.getTaxId());
							billingDetailsDto.setTaxPer(orderDetailsMasterDto.getTaxPer().doubleValue());
						} else {
							billingDetailsDto.setTaxPer(0.00);
						}

						billingDetailsDto.setTaxAmount(orderDetailsMasterDto.getTaxAmount().doubleValue());
						billingDetailsDto.setNetAmt(0.00);
						billingDetailsDto.setSelfPayable(0.00);
						billingDetailsDto.setCreditPayable(0.00);
						billingDetailsDto.setOrderDetailsId(orderDetailsIdList.get(i));
						billingDetailsDto.setPackageId(orderDetailsMasterDto.getPackageId());
						billingDetailsDto.setPackageConsumptionAmt(orderDetailsMasterDto.getServiceAmt().doubleValue());
						billingDetailsDto.setIsDoctorSharePosted('N');

						List<OrderDetailsPayeeMapperDto> orderPayeeBillList = new ArrayList<OrderDetailsPayeeMapperDto>();
						orderPayeeBillList.addAll(orderDetailsMasterDto.getOrderDetailsPayeeMapperDtosList());

						billingDetailsDto.setOrderDetailsPayeeMapperDtosList(orderPayeeBillList);
						iBillingDao.saveBillingDetails(billingDetailsDto);
					}
				}
		}
			
			
			
			// appointment save call
			for (i = 0; i < slotMasterDto.size(); i++) {
				SlotMasterDto appointmentDto = slotMasterDto.get(i);

				if (appointmentDto.getOrderDetailsId() == null) {
					if(orderDetailsIdList.size() > 0) {
						appointmentDto.setOrderDetailsId(orderDetailsIdList.get(i));
					}
					
				}
				appointmentDto.setServiceId(appointmentDto.getServiceMasterId());
				appointmentDto.setIsCyclicAppointment('Y');
				appointmentDto.setAppointmentTypeId(APPOINTMENTTYPEID10);
				// generate appointment
				Response appRes = iAppointmentSchedulingService.generateAppointment(appointmentDto);
				AppointmentMaster appMaster = (AppointmentMaster) appRes.getObject();
				appointmentDto.setAppointmentId(appMaster.getAppointmentId());
				   iAppointmentSchedulingDao.createCyclicAppointmentLog(appointmentDto);
				if (appointmentDto.getParentPackageId() == null && appointmentDto.getIsFromCoversheet().equals('N')) {
					iAppointmentSchedulingDao.updateOrderDetailForServiceAppointment(appointmentDto);
				}
			}

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateBillFromConcessionManager(BillingMasterDto billingMasterDto) {
		try {

			//get Old Bill Details By Bill Id
			BillingMaster billObj = billingMasterDao.getBillById(billingMasterDto.getBillingMasterId());
			
			com.param.billing.common.Response res = iBillingDetailsDao.getBillDetailsObjByBillId(billingMasterDto.getBillingMasterId());
			
			List<BillingDetails> billingDetailsList = res.getListObject();
			
			List<BillPayeeDetails> billPayeeDetailsList = billObj.getBillPayeeDetailsList();
			
			Map<Integer,Double> payeeData = new HashMap<>();
			
			iConcessessionManagerDao.saveConcessessionManagerLog(billObj,billingDetailsList,billPayeeDetailsList);
			
			Double totalConcession = 0.00;
			Double totalDiscount = 0.00;
			Double totalTaxAmt = 0.00;
			Double totalNetAmt = 0.00;
			Double totalSelfPayable = 0.00;
			Double totalCreditPayable = 0.00;
			for(BillingDetails billingDetails : billingDetailsList)
			{
				for(OrderDetailsMasterDto od : billingMasterDto.getOrderDetailsMasterDtosList())
				{
					if(billingDetails.getBillingDetailsId().equals(od.getBillDetailsId())) 
					{
						Double concession = 0.00;
						Double discount = 0.00;
						Double effectiveAmt = 0.00;
						Double taxAmt = 0.00;
						Double netAmt = 0.00;
						Double coShare = billingDetails.getNetAmt() != 0 ? (billingDetails.getSelfPayable().doubleValue()/billingDetails.getNetAmt().doubleValue())*100 : 0;
						Double selfPayable = 0.00;
						Double creditPayable = 0.00;
						
						OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(billingDetails.getOrderDetailsId());
						
						if(od.getConcessionPer()>0)
						{
							concession = od.getOrdConcession().doubleValue();
							effectiveAmt = billingDetails.getGrossAmount() - concession;
						}else {
							concession = 0.00;
							effectiveAmt = billingDetails.getGrossAmount();
						}
						
						if(od.getDiscountPer()!=null && od.getDiscountPer()>0)
						{
							discount = od.getOrdDiscount().doubleValue();
							effectiveAmt = effectiveAmt - discount;
						}else {
							if(effectiveAmt > 0.00) {
								discount = 0.00;
								//effectiveAmt = effectiveAmt-concession;
							}else {
								discount = 0.00;
								effectiveAmt = 0.00;
							}
							
						}
						
						if(billingDetails.getTaxPer()!=null && billingDetails.getTaxPer()>0.00)
						{
							taxAmt = od.getTaxAmount().doubleValue();
						}else {
							taxAmt = 0.00;
						}
						netAmt = od.getNetAmount().doubleValue();
						
						selfPayable = od.getSelfPayable().doubleValue();
						creditPayable = od.getCreditPayable().doubleValue();
							
						double totalPayeeCreditPayable = 0.00;
						for(BillDetailsPayeeMapper dto : billingDetails.getBillDetailsPayeeMappersList())
						{
							if((dto.getPayeeId().equals(billingMasterDto.getSelfPayeeId())))
							{
								dto.setPayeeAmount(new BigDecimal(OrderMasterServiceImpl.df2.format(selfPayable)));	
							}else {
								Double per = 0.0;
								double amount = 0.0;
									if(dto.getPayeeAmount().doubleValue() != 0 && billingDetails.getCreditPayable() != 0){
										per = Double.valueOf(OrderMasterServiceImpl.df2.format((dto.getPayeeAmount().doubleValue() / billingDetails.getCreditPayable()) * 100)); 
										amount = (per/100) * creditPayable;
									}
										
								totalPayeeCreditPayable = totalPayeeCreditPayable + amount;
								
								dto.setPayeeAmount(new BigDecimal(OrderMasterServiceImpl.df2.format(amount)));
							}
							
							if(payeeData.containsKey(dto.getPayeeId()))
							{
								payeeData.put(dto.getPayeeId(),payeeData.get(dto.getPayeeId())+dto.getPayeeAmount().doubleValue());
							}else{
								payeeData.put(dto.getPayeeId(), dto.getPayeeAmount().doubleValue());
							}
							
						}
						
						
						selfPayable =selfPayable!=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(selfPayable)) : 0 ;
						creditPayable = creditPayable!=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(creditPayable)) : 0 ;
						concession = concession !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(concession)) : 0 ;
						discount = discount !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(discount)) : 0 ;
						taxAmt = taxAmt !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(taxAmt)) : 0 ;
						netAmt = netAmt !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(netAmt)) : 0 ;
						
						billingDetails.setSelfPayable(selfPayable);
						billingDetails.setCreditPayable(creditPayable);
						billingDetails.setConcession(concession);
						billingDetails.setDiscount(discount);
						billingDetails.setTaxAmount(taxAmt);
						billingDetails.setNetAmt(netAmt);
						
						
						iBillingDetailsDao.updateBillDetailsObj(billingDetails);
						
						
						Response payeeRes = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId(),null);
						List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = payeeRes.getListObject();
						
						for(OrderDetailsPayeeMapperDto dto : orderDetailsPayeeMapperDtosList)
						{
							if(dto.getPayeeId().equals(billingMasterDto.getSelfPayeeId()))
							{
									dto.setPayeeAmount(new BigDecimal(selfPayable));	
								iOrderDetailsMasterDao.updateOrderDetailsPayeeMapper(dto);
							}else {
									
								dto.setPayeeAmount(new BigDecimal(payeeData.get(dto.getPayeeId())));
								iOrderDetailsMasterDao.updateOrderDetailsPayeeMapper(dto);
							}
						}
						
						orderDetailsMasterDto.setSelfPayable(new BigDecimal(selfPayable));
						orderDetailsMasterDto.setCreditPayable(new BigDecimal(creditPayable));
						orderDetailsMasterDto.setOrdDiscount(new BigDecimal(discount));
						orderDetailsMasterDto.setOrdConcession(new BigDecimal(concession));
						orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(od.getConcessionPer()));
						orderDetailsMasterDto.setTaxAmount(new BigDecimal(taxAmt));
						orderDetailsMasterDto.setNetAmount(new BigDecimal(netAmt));
						
						iOrderDetailsMasterService.updateOrderDetailsObj(orderDetailsMasterDto,0.00/*(crNetAmount)*/);
						
						
						totalConcession = totalConcession + (concession !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(concession)) : 0);
						totalDiscount = totalDiscount + (discount!=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(discount)):0);
						totalTaxAmt = totalTaxAmt + (taxAmt!=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(taxAmt)):0);
						totalSelfPayable = totalSelfPayable + (selfPayable !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(selfPayable)):0);
						totalCreditPayable = totalCreditPayable + (creditPayable !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(creditPayable)):0);
						totalNetAmt = totalNetAmt + (netAmt !=0 ? Double.valueOf(OrderMasterServiceImpl.df2.format(netAmt)):0);
					}
				}
				
			}
			double paidAmt = billObj.getNetAmount()-billObj.getOutstanding();
			
			billObj.setSelfPayable(Double.valueOf(OrderMasterServiceImpl.df2.format(billingMasterDto.getSelfPayable())));
			billObj.setCreditPayable(Double.valueOf(OrderMasterServiceImpl.df2.format(billingMasterDto.getCreditPayable())));
			billObj.setRoundOff(Double.valueOf(OrderMasterServiceImpl.df2.format(billingMasterDto.getRoundOff())));
			billObj.setNetAmount(Double.valueOf(OrderMasterServiceImpl.df2.format(billingMasterDto.getNetAmount())));
			
			totalNetAmt = billingMasterDto.getNetAmount();
			totalSelfPayable = billObj.getSelfPayable();
			totalCreditPayable = billObj.getCreditPayable();
			
			billObj.setConcessionAmount(Double.valueOf(OrderMasterServiceImpl.df2.format(totalConcession)));
			billObj.setDiscountAmount(Double.valueOf(OrderMasterServiceImpl.df2.format(totalDiscount)));
			billObj.setGst(Double.valueOf(OrderMasterServiceImpl.df2.format(totalTaxAmt)));
			
			if(billObj.getBillStatusId()==1)
				billObj.setOutstanding(Double.valueOf(OrderMasterServiceImpl.df2.format(totalNetAmt)));
			else {
				billObj.setOutstanding(Double.valueOf(OrderMasterServiceImpl.df2.format(totalNetAmt - (paidAmt))));
			}
			
			if(billObj.getOutstanding().equals(0.00))
			{
				billObj.setBillStatusId(3);
				if(billingMasterDto.getAdmissionId() !=null && billObj.getIsInterimOrFinalBill() == 2){
					iBillingDao.updateAdmissionIsFinanceFlag(billingMasterDto);
				}
			}else if(billObj.getOutstanding() > 0 && (billObj.getOutstanding() <= billObj.getNetAmount()) ){
				billObj.setBillStatusId(2);
			}else{
				billObj.setBillStatusId(1);
			}
			
			iBillingDao.updateBillMaster(billObj);
			
			for(BillPayeeDetails billPayeeDetails : billPayeeDetailsList)
			{
				if(billPayeeDetails.getPayeeId().equals(billingMasterDto.getSelfPayeeId()))
				{
						billPayeeDetails.setDueAmount(billPayeeDetails.getDueAmount() - (billPayeeDetails.getBillAmount()-billObj.getSelfPayable()));	
						billPayeeDetails.setBillAmount(billObj.getSelfPayable());
					iBillPayeeDetailsDao.updatePayeeDetailsObject(billPayeeDetails);
				}else {
						if(billObj.getSelfPayable()==0 && billPayeeDetails.getPriorityId()!=null && billPayeeDetails.getPriorityId()==1 && billObj.getRoundOff()!=0)
						{
							billPayeeDetails.setDueAmount((billPayeeDetails.getDueAmount() - (billPayeeDetails.getBillAmount()-payeeData.get(billPayeeDetails.getPayeeId())))+billObj.getRoundOff());
							billPayeeDetails.setBillAmount(payeeData.get(billPayeeDetails.getPayeeId())+billObj.getRoundOff());
						}else{
							billPayeeDetails.setDueAmount(billPayeeDetails.getDueAmount() - (billPayeeDetails.getBillAmount()-payeeData.get(billPayeeDetails.getPayeeId())));	
							billPayeeDetails.setBillAmount(payeeData.get(billPayeeDetails.getPayeeId()));
						}
						
					iBillPayeeDetailsDao.updatePayeeDetailsObject(billPayeeDetails);
				}
			}
			
			return new Response<>(SUCCESS, null, "Bill Updated Successfully...!!", null, null);

		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response billListByPatientIdForConcessionManager(int orgId,int unitId, int patientId) {
		try {

			return billingMasterDao.billListByPatientIdForConcessionManager(orgId,unitId,patientId);

		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelBill(BillingMasterDto billingMasterDto) {
		try {

				BillingMaster billObj = billingMasterDao.getBillById(billingMasterDto.getBillingMasterId());
				
				Response billPayeeRes = iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(billingMasterDto.getBillingMasterId());
				
				List<BillPayeeDetailsResDto> billPayeeDetailsList = billPayeeRes.getListObject();	
				
				for(BillPayeeDetailsResDto billPayeeDetails: billPayeeDetailsList)
				{
					if(!(billPayeeDetails.getPayeeId().equals(billingMasterDto.getSelfPayeeId())) && !(billPayeeDetails.getDueAmount().equals(billPayeeDetails.getBillAmount())))
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Insurance settled, bill can not be cancelled...!!", null, null); 
					}
					
					if(billPayeeDetails.getPayeeId().equals(billingMasterDto.getSelfPayeeId()) && billPayeeDetails.getRefundedAmount()!=null && billPayeeDetails.getRefundedAmount()>0)
					{
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Refund generated against bill, cannot be cancelled...!!", null, null); 
					}
				}
				
				iRequestDiscountDao.getCancelDiscountReqByBillId(billObj.getBillingMasterId());
				
				List<BillPaymentCollectionReqDto> paymentRecieptList = iPaymentDepositConsumptionDao.getPaymentRecieptDetails(billingMasterDto.getBillingMasterId());
				if(paymentRecieptList.size()>0)
				{
					for(BillPaymentCollectionReqDto dto : paymentRecieptList)
					{
						if(dto.getReceiptId()!=null && dto.getReceiptId() > 0 )
						{
							if(dto.getDepositConsumptionId()!=null)	
								billingMasterDto.setDepositConsumptionId(dto.getDepositConsumptionId());
							else
								billingMasterDto.setDepositConsumptionId(null);
							
							billingMasterDto.setPaymentRecieptId(dto.getReceiptId());
							billingMasterDto.setIsAddendumBill('Y');
							this.cancelReciept(billingMasterDto);
						}
						if(billObj.getEncounterId()!=null && billObj.getEncounterId()>0)
						{
							updateAppointmentStatusAfterCanceldBill(billObj.getEncounterId(),billingMasterDto.getBillCancelBy());
						}
					}
				}
				billObj.setOutstanding(billObj.getNetAmount());
				billObj.setStatus('I');
				billObj.setBillCancelled('Y');
				billObj.setBillCancelReasonId(billingMasterDto.getBillCancelReasonId());
				billObj.setBillCancelDatetime(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
				billObj.setBillCancelBy(billingMasterDto.getBillCancelBy());
				billObj.setBillCancelRemark(billingMasterDto.getBillCancelRemark());
			    iCreditNoteMasterDao.cancelCreditNote(null,billingMasterDto.getBillingMasterId());
			    
			    iPatientCouponTemplateMapperService.updateOrderDetailsAndPatientCoupon(billingMasterDto.getBillingMasterId());
				// sneha code HIS-27519
				Response resUpdate= iBillingDao.updateAdmissionsIsFinancialClearanceN(billingMasterDto);
				
			return billingMasterDao.inactivateBillAndBillDetails(billObj);
			
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelReciept(BillingMasterDto billingMasterDto) {
		try {
			
			DepositMaster depositMaster = iDepositMasterDao.getDepositAgainstReceipt(billingMasterDto.getPaymentRecieptId(),billingMasterDto.getBillingMasterId());
			if(depositMaster!=null)
			{
				if(depositMaster.getConsumedDeposit()>0)
				{
					if(billingMasterDto.getIsAddendumBill()==null)
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Reciept cannot be cancelled...!!", null, null); 
					else
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Bill cannot be cancelled...!!", null, null); 
				}else{
					
					depositMaster.setDepositAmount(0.00);
					depositMaster.setAvailableDeposit(0.00);
					depositMaster.setStatus('I');
					iDepositMasterDao.updateDepositObject(depositMaster);
				}
			}
			Response billPayeeRes = iBillPayeeDetailsDao.getBillPayeeDetailsByBillId(billingMasterDto.getBillingMasterId());
			
			List<BillPayeeDetailsResDto> billPayeeDetailsListRefundCheck = billPayeeRes.getListObject();	
			
			for(BillPayeeDetailsResDto billPayeeDetails: billPayeeDetailsListRefundCheck)
			{
				if(billPayeeDetails.getPayeeId().equals(billingMasterDto.getSelfPayeeId()) && billPayeeDetails.getRefundedAmount()!=null && billPayeeDetails.getRefundedAmount()>0)
				{
					return new Response<>(ERROR, ALREADY_EXIST_CODE, "Refund generated against bill, cannot be cancelled...!!", null, null); 
				}
			}
			
			Response res=billingMasterDao.cancelReciept(billingMasterDto);
			Double totalRecieptAmount = billingMasterDto.getPaidAmount();
			
			if(billingMasterDto.getDepositConsumptionId()!=null && billingMasterDto.getDepositConsumptionId()>0)
			{
				iPaymentDepositConsumptionDao.addDepositAfterCancelReciept(billingMasterDto.getDepositConsumptionId());
			}
			
			List<PaymentReceiptDetails> paymentReceiptDetailsList = iPaymentRecieptDetailsDao.getPaymentReceieptDetailsForBillCancellation(billingMasterDto.getPaymentRecieptId());
			
			for(PaymentReceiptDetails receiptDetails : paymentReceiptDetailsList)
			{
					receiptDetails.setStatus('A');
				iPaymentRecieptDetailsDao.inactiveRecieptDetails(receiptDetails);
			}
			
			if(billingMasterDto.getIsAddendumBill()==null)
			{
				BillingMaster  billingMaster = billingMasterDao.getBillById(billingMasterDto.getBillingMasterId());
				
				billingMaster.setOutstanding(billingMaster.getOutstanding() + billingMasterDto.getPaidAmount());
				Double crAmount =  0.00;
				for (BillPayeeDetails payeedto : billingMaster.getBillPayeeDetailsList()) {
					crAmount = crAmount + (payeedto.getCrNetAmt() != null ? payeedto.getCrNetAmt():0.00);
				}
				
				if(billingMaster.getNetAmount().equals(billingMaster.getOutstanding() + crAmount))
					billingMaster.setBillStatusId(PENDING);
				else if(billingMaster.getOutstanding() > 0 && (billingMaster.getOutstanding() + crAmount) < billingMaster.getNetAmount())
					billingMaster.setBillStatusId(ONHOLD);
				else
					billingMaster.setBillStatusId(SETTLED);
				
				iBillingDao.updateBillMaster(billingMaster);
				
				if(billingMaster.getBillStatusId()==PENDING)
				{
					if(billingMaster.getEncounterId()!=null && billingMaster.getEncounterId()>0)
					{
						updateAppointmentStatusAfterCanceldBill(billingMaster.getEncounterId(),billingMasterDto.getBillCancelBy());
					}
				}
				
				List<BillPayeeDetails> billPayeeDetailsList = billingMaster.getBillPayeeDetailsList();
				for(BillPayeeDetails billPayeeDetails : billPayeeDetailsList)
				{
					if(billPayeeDetails.getPayeeId().equals(billingMasterDto.getSelfPayeeId()))
					{
							billPayeeDetails.setDueAmount(billPayeeDetails.getDueAmount() + billingMasterDto.getPaidAmount());
							billPayeeDetails.setPaidAmount(billPayeeDetails.getPaidAmount() - billingMasterDto.getPaidAmount());
						iBillPayeeDetailsDao.updatePayeeDetailsObject(billPayeeDetails);
					}
				}
				
				
				// sneha code HIS-27519
				Response resUpdate= iBillingDao.updateAdmissionsIsFinancialClearanceN(billingMasterDto);
				
			}
			return res;
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}
	@Override
	@Transactional
	public Response getBillListByFilterForbillCancellationList(BillListReqDto reqDto) throws BillingException {
		try {
			return billingMasterDao.getBillListByFilterForbillCancellationList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getBillsByFilterForCancelledBillList(BillListReqDto reqDto) throws BillingException {
		try {
			return billingMasterDao.getBillsByFilterForCancelledBillList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateDischarge(DischargeDto dischargeDto) {
		try {
			return billingMasterDao.canceDischarge(dischargeDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updatePdd(DischargeDto dischargeDto) {
		try {
			return billingMasterDao.updatePDD(dischargeDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	public int updateAppointmentStatusAfterCanceldBill(Integer enconterId,Integer userId) {
		try {
			EncounterMasterDto dto = iOrderDetailsMasterDao.checkConsultationService(enconterId);
			
			if (dto!=null && dto.getAppointmentId() > 0) 
			{
				
				Integer check = 0;
				SlotMasterDto dtott = new SlotMasterDto();
				dtott.setAppointmentId(dto.getAppointmentId());
				Integer appointmentStatusId = billingMasterDao.checkAppoinmentStatussss(dtott);
				
				if(appointmentStatusId != null) {
					if(appointmentStatusId == 2) {//for cancelled appointment appointment status not going to change//as discussed with vishal
						check = 1;
					}
				}
				if(check == 0) {
					
					SlotMasterDto slotMasterDto = new SlotMasterDto();
					slotMasterDto.setAppointmentId(dto.getAppointmentId());
					slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID8);
					slotMasterDto.setUpdatedBy(userId);
					iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
					
					EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
					encounterMasterDto.setEncounterId(enconterId);
					encounterMasterDto.setStatusId(8);
					encounterMasterDto.setUpdatedBy(userId);
				  iEncounterMasterService.updateEncounterStatus(encounterMasterDto);
				}
					
					
				
				return 1;
			}
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
		return 0;
	}
	
	@Override
	@Transactional
	public Response cancelPendingService(CancelOrderDto cancelOrderDto) {
		try 
		{
			//OrderDetailsMaster orderDetailsMaster = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(cancelOrderDto.getOrderDetailsId());
		    Response ordDtls = iOrderDetailsMasterDao.getOrderDetailsById(cancelOrderDto.getOrderDetailsId());
			OrderDetailsMasterDto orderDetailsMaster = (OrderDetailsMasterDto)ordDtls.getObject();
			
			if(orderDetailsMaster.getOrdCancelled()=='Y')
			{
				return new Response(ERROR, ALREADY_EXIST_CODE, "Service is already cancelled by another user...!!", null, null);
			}else {
			
				if(cancelOrderDto.getEncounterId()==null && cancelOrderDto.getAdmissionId()==null)
				{
					iOrderDetailsMasterDao.cancelUnRenderedServices(cancelOrderDto);
				}else if(!orderDetailsMaster.isPackage())
				{
					ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto.setEncounterId(cancelOrderDto.getEncounterId());
						consumptionDto.setAdmissionId(cancelOrderDto.getAdmissionId());
						consumptionDto.setSpecialityId(cancelOrderDto.getSpecialityId());
						consumptionDto.setServiceId(orderDetailsMaster.getServiceId());
						consumptionDto.setQuantity(orderDetailsMaster.getOrderQty().doubleValue());
						consumptionDto.setOrderDetailsId(cancelOrderDto.getOrderDetailsId());
						consumptionDto.setUserId(cancelOrderDto.getUserId()!=null ? cancelOrderDto.getUserId() : orderDetailsMaster.getCreatedBy());
						consumptionDto.setCreatedBy(cancelOrderDto.getUserId()!=null ? cancelOrderDto.getUserId() : orderDetailsMaster.getCreatedBy());
					if(cancelOrderDto.getOrderDetailsPayeeMapperDtosList()!=null && cancelOrderDto.getOrderDetailsPayeeMapperDtosList().size()>0) 
					{
						consumptionDto.setOrderDetailsPayeeMapperDtosList(cancelOrderDto.getOrderDetailsPayeeMapperDtosList());
					}else 
					{
						Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(cancelOrderDto.getOrderDetailsId(),null);
						List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = res2.getListObject();
						consumptionDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
						
						
						Response pckRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(cancelOrderDto.getOrderDetailsId());
						List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtoList = pckRes.getListObject();
						consumptionDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageMapperDtoList);
					}
					consumptionDto.setOrdCancelReasonId(cancelOrderDto.getOrdCancelReasonId() != null ? cancelOrderDto.getOrdCancelReasonId():0);
					iContractConsumptionService.addCapAmount(consumptionDto);
				}
				
				if(cancelOrderDto.getSaleId()!=null && cancelOrderDto.getSaleId()>0)
				{
					cancelOrderDto.setScreenId(9);//static screen id
					iWalkInSalesService.revertAllocatedQty(cancelOrderDto.getSaleId().longValue(), cancelOrderDto.getScreenId(),orderDetailsMaster.getOrgUnitId(),null,null,null);
				}
				
				
				if(orderDetailsMaster.isPackage())
				{   
				//	iOrderDetailsMasterDao.cancelUnRenderedServices(cancelOrderDto);
					
					BillingMasterDto billDto = new BillingMasterDto();
					billDto.setOrganisationId(orderDetailsMaster.getOrgId());
					billDto.setUnitId(orderDetailsMaster.getOrgUnitId());
					billDto.setCreatedBy(orderDetailsMaster.getCreatedBy());
					billDto.setUpdatedBy(orderDetailsMaster.getCreatedBy());
					billDto.setTariffId(cancelOrderDto.getTariffId());
					billDto.setPharmacyServiceId(cancelOrderDto.getPharmacyServiceId());
					if(cancelOrderDto.getAdmissionId() != null && cancelOrderDto.getAdmissionId()>0 ) {
						Response admissionDto = ipdChargesDao.getAdmissionsById(cancelOrderDto.getAdmissionId());
						AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
						if(adminDto == null) {
							return new Response<>(ERROR, ERROR_CODE, "Patient may be pending for Nursing acceptance or discharged...!! ", null, null);
					      }
						else {
							billDto.setPatientId(adminDto.getPatientId());
							billDto.setVisitTypeId(adminDto.getVisitTypeId());
							billDto.setAdmissionId(adminDto.getAdmissionId());
							billDto.setPaymentEntitlementId(adminDto.getPaymentEntitlementId());
							billDto.setPatientTypeId(adminDto.getPatientCategoryId());
							billDto.setBillingBedCategoryId(adminDto.getBedBillingCategoryId());
							billDto.setContractId(adminDto.getContractId());
							billDto.setPatientClassId(adminDto.getPatientClassId());
						}
					}
					 else  if(cancelOrderDto.getEncounterId() != null && cancelOrderDto.getEncounterId()>0 ) {
						Response encounterRes = iEncounterMasterService.getEncounterDetailsById(cancelOrderDto.getEncounterId());
						EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
							billDto.setPatientId(encounterMasterDto.getPatientId());
							billDto.setVisitTypeId(OP);
							billDto.setEncounterId(encounterMasterDto.getEncounterId());
							billDto.setPaymentEntitlementId(encounterMasterDto.getPaymentEntitlementId());
							billDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
							billDto.setBillingBedCategoryId(cancelOrderDto.getOpdBillingCatId());
							billDto.setContractId(encounterMasterDto.getContractId());
							billDto.setPatientClassId(encounterMasterDto.getPatientClassId());
					}
					
					List<OrderDetailsMasterDto> listToBeUpdated = new ArrayList<OrderDetailsMasterDto>();
					ServiceMasterDto serviceDto = new  ServiceMasterDto();
						serviceDto.setEncounterId((billDto.getEncounterId()!=null && billDto.getEncounterId()>0)?billDto.getEncounterId():billDto.getAdmissionId());
						serviceDto.setVisitTypeId(billDto.getVisitTypeId());
					Response serviceRes =null;
					
					ContractConsumptionDto contractDto = new ContractConsumptionDto();
						contractDto.setPatientId(billDto.getPatientId());
						contractDto.setEncounterId(billDto.getEncounterId()!=null?billDto.getEncounterId():billDto.getAdmissionId());
						contractDto.setBillingBedCategoryId(billDto.getBillingBedCategoryId());
						contractDto.setPatientCategoryId(billDto.getPatientTypeId());
						contractDto.setVisitTypeId(billDto.getVisitTypeId());
						contractDto.setOrganizationId(billDto.getOrganisationId());
						contractDto.setUnitId(billDto.getUnitId());
						contractDto.setTariffId(billDto.getTariffId());
						contractDto.setPaymentEntitlementId(billDto.getPaymentEntitlementId());
						contractDto.setSelfPayeeId(billDto.getSelfPayeeId());
						contractDto.setPatientClassId(billDto.getPatientClassId());
				    ContractConsumptionDto contractDtoClone = contractDto;
				    
				    
					if(cancelOrderDto.getPreOrPostBilling()=='Y' && billDto.getVisitTypeId().intValue()==OP)
						 serviceRes = this.getOrderedServicesByEncounterIdForPreBilling(serviceDto);
						else 
						serviceRes = this.getOrderedServicesByEncounterId(serviceDto);
					if(serviceRes.getListObject()!=null && serviceRes.getListObject().size()>0)
					{
						List<OrderDetailsByEncounterIdDto> orderDetailsList = serviceRes.getListObject();
						OrderDetailsMasterDto dto  =null;
						for (OrderDetailsByEncounterIdDto ordEndDto : orderDetailsList) 
						{
							 if(ordEndDto.getStatus().equals('A'))
							 {
								 dto  = new OrderDetailsMasterDto();
								 if(ordEndDto.getOrderDetailsId().intValue()==cancelOrderDto.getOrderDetailsId().intValue())
								 {
									 ordEndDto.setStatus('I');
									 ordEndDto.setOrdCancelled('Y');
									 ordEndDto.setOrdCancelReasonId(cancelOrderDto.getOrdCancelReasonId());
								 }
								 dto.setOrderDetailsId(ordEndDto.getOrderDetailsId());
								 dto.setOrgId(ordEndDto.getOrgId());
								 dto.setOrgUnitId(ordEndDto.getOrgUnitId());
								 dto.setOrderId(ordEndDto.getOrderId());
								 dto.setOrderQty(ordEndDto.getQuantity());
								 dto.setServiceId(ordEndDto.getServiceMasterId());
								 dto.setPriorityId(ordEndDto.getPriorityId());
								 dto.setIsOutsourced(ordEndDto.getIsOutsourced());
								 dto.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(ordEndDto.getOrderDateString(),"yyyy-MM-dd"));
								 dto.setOrderSchDate(GlobalCommonDateUtils.getLongDateFromStrigDate(ordEndDto.getOrderSchDateString(),"yyyy-MM-dd"));
								
								 dto.setOrdDocSplId(ordEndDto.getDoctorSpecialityId());
								 dto.setOrdDocId(ordEndDto.getDoctorId());
								 dto.setDepartmentId(ordEndDto.getServiceSpecialityId());
								 dto.setSubDepartmentId(ordEndDto.getServiceSubSpecialityId());
								 dto.setTaxId(ordEndDto.getTaxId());
								 dto.setTaxPer(ordEndDto.getTaxPercentage());
								 dto.setPayeeId(ordEndDto.getPayeeId());
								 dto.setOrderSetId(ordEndDto.getOrderSetId());
								 dto.setContractId(ordEndDto.getContractId());
								 dto.setBedCategoryId(ordEndDto.getBedCategoryId());
								 dto.setCoPayPer(ordEndDto.getCoPayPer());
								 dto.setDocSharePer(ordEndDto.getDocSharePer());
								 dto.setRefDocSharePer(ordEndDto.getRefDocSharePer());
								 dto.setOrdCancelBy(ordEndDto.getOrdCancelBy());
								 dto.setOrdRemarks(ordEndDto.getOrdRemarks());
								 dto.setTariffId(ordEndDto.getTariffId());
								 dto.setBillingClassId(ordEndDto.getBillingClassId());
								 dto.setBedId(ordEndDto.getBedId());
								 dto.setRoomId(ordEndDto.getRoomId());
								 dto.setWardId(ordEndDto.getWardId());
								 dto.setOrderEmergencyFlag(ordEndDto.getOrderEmergencyFlag());
								 dto.setPackageId(ordEndDto.getPackageId());
								 dto.setDrugId(ordEndDto.getDrugId());
								 dto.setBatchId(ordEndDto.getBatchId());
								 dto.setOldOrdDtlId(ordEndDto.getOldOrdDtlId());
								 dto.setServiceRendered(ordEndDto.getServiceRendered());
								 dto.setServiceChargeable(ordEndDto.getServiceChargeable());
								 dto.setServiceRenderingDeptId(ordEndDto.getServiceRenderingDeptId());
								 dto.setOrdRenderDatetime(ordEndDto.getOrdRenderDatetime());
								 dto.setServiceIsBilled(ordEndDto.getServiceIsBilled());
								 dto.setCreatedDate(ordEndDto.getCreatedDate());
								 dto.setCreatedBy(ordEndDto.getCreatedBy());
								 dto.setOrdCancelled(ordEndDto.getOrdCancelled());
								 dto.setOrdCancelReasonId(ordEndDto.getOrdCancelReasonId());
								 dto.setOrdCancelDatetime(ordEndDto.getOrdCancelDatetime());
								 dto.setStatus(ordEndDto.getStatus());
								 dto.setOrdCancelled(ordEndDto.getOrdCancelled());
								 dto.setIsDrug(ordEndDto.getIsDrug());
								 dto.setIsPackage(ordEndDto.getIsPackage());
								 dto.setForceAddRemoved(ordEndDto.getForceAddRemoved());
								 dto.setIsConsumed(ordEndDto.getIsConsumed());
								 dto.setSaleDetailId(ordEndDto.getSaleDetailId());
								 dto.setSaleId(ordEndDto.getSaleId());
								 dto.setIsInvestigationOrder(ordEndDto.getIsInvestigationOrder());
								 dto.setIsConsultationService(ordEndDto.getIsConsultationService());
								 dto.setIsPeriodicityService(ordEndDto.getPeriodicityServiceOrderId()!=null ? 'Y':'N');
								 dto.setOrdDiscount(BigDecimal.ZERO);
								 dto.setDiscountValue(BigDecimal.ZERO);
								 dto.setPatientClassPercentage(ordEndDto.getPatientClassPercentage());
								 dto.setOrdTotalAmt(ordEndDto.getBasePrice());
								 dto.setNetAmount(ordEndDto.getNetPay());
								 dto.setServiceAmt(ordEndDto.getBasePrice());
								 BigDecimal concessionAmt =BigDecimal.ZERO;
								 BigDecimal ordTotalAmt =BigDecimal.ZERO;
								 BigDecimal ordTaxAmt =BigDecimal.ZERO;
								if (billDto.getPharmacyServiceId() != null && billDto.getPharmacyServiceId() > 0
										&& (dto.getServiceId().intValue() != billDto.getPharmacyServiceId())
										&& (dto.getIsConsultationService() == null|| dto.getIsConsultationService().equals('N'))
										&& (dto.getProcedureOrderMstId() == null)
										&& (dto.getIsPeriodicityService() == null || dto.getIsPeriodicityService() == 'N')) 
								{
									 
									   contractDtoClone.setContractId(billDto.getContractId());
									   dto.setContractId(billDto.getContractId());
									   contractDtoClone.setAdmissionId(billDto.getAdmissionId());
									   
									   if(dto.getIsConsultationService()=='Y')
											contractDtoClone.setIsConsultationService(dto.getIsConsultationService());
										else
											contractDtoClone.setIsConsultationService('N');
									   
									    contractDtoClone.setCurrentDate(dto.getOrderDate() == null ? GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billDto.getUnitId()), "yyyy-MM-dd")  : GlobalCommonDateUtils.getStringDate(ordEndDto.getOrderDate(), "yyyy-MM-dd"));
									    contractDtoClone.setServiceId(dto.getServiceId());
									    contractDtoClone.setPatientClassPercentage(dto.getPatientClassPercentage().doubleValue());
									    //rate by billingBedCategoryId
									    contractDtoClone.setBillingBedCategoryId(dto.getBedCategoryId()!=null && dto.getBedCategoryId()>0?dto.getBedCategoryId():billDto.getBillingBedCategoryId());
									    
									    Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contractDtoClone, false, false);
										ContractConsumptionDto globalRatesRes = (ContractConsumptionDto) serviceRateRes.getObject();
										
										dto.setCoPayPer(BigDecimal.valueOf(globalRatesRes.getCoShare()));
										dto.setSelfPayable(new BigDecimal(globalRatesRes.getSelfPayable()));
										dto.setCreditPayable(new BigDecimal(globalRatesRes.getCreaditPayable()));
										dto.setServiceAmt(new BigDecimal(globalRatesRes.getServiceRate()));
										dto.setDummyServiceAmt(new BigDecimal(globalRatesRes.getServiceRate()));
										dto.setOrdConcession(new BigDecimal(globalRatesRes.getConcession()));
										dto.setOrdTotalAmt(new BigDecimal(globalRatesRes.getServiceRate()));
										dto.setNetAmount(new BigDecimal(globalRatesRes.getFinalPrice()));
										dto.setOrdDiscount(new BigDecimal(globalRatesRes.getOrdDiscount()));
										dto.setOrdConcessionPer(BigDecimal.valueOf(globalRatesRes.getPatientClassPercentage()));
									 
									 if(ordEndDto.getPatientClassPercentage()!=null && ordEndDto.getPatientClassPercentage().doubleValue()>0)
									 {
										   concessionAmt = dto.getOrdTotalAmt().multiply((ordEndDto.getPatientClassPercentage().multiply(BigDecimal.valueOf(0.01))));
									 }
									 if(ordEndDto.getTaxPercentage()!=null && ordEndDto.getTaxPercentage().doubleValue()>0) {
										 ordTaxAmt = (dto.getOrdTotalAmt().subtract(concessionAmt)).multiply((ordEndDto.getTaxPercentage().multiply(BigDecimal.valueOf(0.01))));
										 dto.setNetAmount(dto.getOrdTotalAmt().subtract(concessionAmt).add(ordTaxAmt));
									 }
									 else {
										 dto.setNetAmount(dto.getOrdTotalAmt().subtract(concessionAmt));
									 }
									 dto.setOrdConcession(concessionAmt);
									 dto.setTaxAmount(ordTaxAmt);
									 dto.setSelfPayable(dto.getNetAmount());
								 }
								 dto.setOldNetPay(dto.getNetAmount());
								 dto.setOldCreditPayable(dto.getNetAmount());
								 dto.setPackageConsumptionAmt(BigDecimal.ZERO);
								 dto.setCreditPayable(BigDecimal.ZERO);
								 dto.setOldCreditPayable(BigDecimal.ZERO);
								 dto.setOrderDetailsPayeeMapperDtosList(ordEndDto.getOrderDetailsPayeeMapperDtosList());
								 dto.setOrderDetailsPackageMapperDtosList(ordEndDto.getOrderDetailsPackageMapperDtosList());
							     listToBeUpdated.add(dto);
							 }
							 
						}
						billDto.setOrderDetailsMasterDtosList(listToBeUpdated);
						this.cancelPackageFromPendingService(billDto);
					}
				}
				return new Response(SUCCESS, null, "Order Cancelled !!", null, null);
			}
			
		}catch (Exception e) 
		{
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelPackageFromPendingService(BillingMasterDto billingMasterDto) {
		try {
			
			
			
			for(OrderDetailsMasterDto detailsMasterDto : billingMasterDto.getOrderDetailsMasterDtosList())
			{
				if(detailsMasterDto.getOrderDetailsId()!=null && (detailsMasterDto.getPackageTypeId() == null || detailsMasterDto.getPackageTypeId() != 1))
				{
					
					ContractConsumptionDto addCapDto = new ContractConsumptionDto();
						addCapDto.setAdmissionId(billingMasterDto.getAdmissionId());
						addCapDto.setEncounterId(billingMasterDto.getEncounterId());
						//addCapDto.setServiceId(detailsMasterDto.getServiceId());
						addCapDto.setOrderDetailsPayeeMapperDtosList(detailsMasterDto.getOrderDetailsPayeeMapperDtosList());
						addCapDto.setOrderDetailsPackageMapperDtosList(detailsMasterDto.getOrderDetailsPackageMapperDtosList());
						addCapDto.setSpecialityId(detailsMasterDto.getDepartmentId());
						
						if (detailsMasterDto.getProcedureOrderMstId() != null && detailsMasterDto.getProcedureOrderMstId() > 0 && detailsMasterDto.getIsProcedureService() == 'Y') 
						{
							addCapDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
							for (OrderDetailsPayeeMapperDto odPayeeDto : detailsMasterDto.getOrderDetailsPayeeMapperDtosList())
								odPayeeDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
							for (OrderDetailsPackageMapperDto odPayeeDto : detailsMasterDto.getOrderDetailsPackageMapperDtosList())
								odPayeeDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
						} else 
						{
							addCapDto.setServiceId(detailsMasterDto.getServiceId());
						}	
				}
			}
			
			Integer isPackageCancelled = 0;
			List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
			// for cancel package
			Response packCanRes = this.cancelPackage(billingMasterDto, billingMasterDto.getOrderDetailsMasterDtosList());
			isPackageCancelled = packCanRes.getObject()!=null ? (Integer)packCanRes.getObject() : 0;
			
			Response res = this.orderPackageOnSelection(billingMasterDto);
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				List<OrderDetailsMasterDto> listToBeUpdated = res.getListObject();
				ContractConsumptionDto contractDto = new ContractConsumptionDto();
				contractDto.setPatientId(billingMasterDto.getPatientId());
				contractDto.setEncounterId(billingMasterDto.getEncounterId() != null ? billingMasterDto.getEncounterId()
						: billingMasterDto.getAdmissionId());
				contractDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
				contractDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
				contractDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				contractDto.setOrganizationId(billingMasterDto.getOrganisationId());
				contractDto.setUnitId(billingMasterDto.getUnitId());
				contractDto.setTariffId(billingMasterDto.getTariffId());
				contractDto.setPaymentEntitlementId(billingMasterDto.getPaymentEntitlementId());
				contractDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				contractDto.setListOrderDetailsMasterDto(listToBeUpdated);
				contractDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
				contractDto.setPatientClassId(billingMasterDto.getPatientClassId());

				ContractConsumptionDto contractDtoClone = contractDto;
				// set pkg mppr list null
				for (OrderDetailsMasterDto ordDtls : contractDto.getListOrderDetailsMasterDto()) 
				{
					if (ordDtls.getPackageTypeId() == null || ordDtls.getPackageTypeId() != 1 ) 
					{
						if (ordDtls.getProcedureOrderMstId() != null && ordDtls.getProcedureOrderMstId() > 0 && ordDtls.getIsProcedureService() == 'Y') 
						{
							contractDtoClone.setServiceId(ordDtls.getProcedureOrderMstId());
							for (OrderDetailsPayeeMapperDto odPayeeDto : ordDtls.getOrderDetailsPayeeMapperDtosList())
								odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
							for (OrderDetailsPackageMapperDto odPayeeDto : ordDtls
									.getOrderDetailsPackageMapperDtosList())
								odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
						} else 
						{
							contractDtoClone.setServiceId(ordDtls.getServiceId());
						}
						contractDtoClone.setOrderDetailsPayeeMapperDtosList(ordDtls.getOrderDetailsPayeeMapperDtosList());
						contractDtoClone.setOrderDetailsPackageMapperDtosList(ordDtls.getOrderDetailsPackageMapperDtosList());
						contractDtoClone.setSpecialityId(ordDtls.getDepartmentId());
						contractDtoClone.setEncounterId(billingMasterDto.getEncounterId());
						contractDtoClone.setAdmissionId(billingMasterDto.getAdmissionId());
						contractDtoClone.setContractId(billingMasterDto.getContractId());
						
						if(billingMasterDto.getPharmacyServiceId() != null &&billingMasterDto.getPharmacyServiceId()>0 && (ordDtls.getServiceId().intValue() != billingMasterDto.getPharmacyServiceId())) {
						 iContractConsumptionService.addCapAmount(contractDtoClone);
						}
						if (!ordDtls.isPackage() && ordDtls.getOrdTotalAmt().doubleValue()>=0) 
						{
							
							if (isPackageCancelled == 1 && ordDtls.getPackageConsumptionAmt() != null
									&& ordDtls.getPackageConsumptionAmt().doubleValue() > 0.00
									&& billingMasterDto.getPharmacyServiceId() != null
									&& (ordDtls.getServiceId().intValue() != billingMasterDto.getPharmacyServiceId())
									&& (ordDtls.getIsConsultationService() == null|| ordDtls.getIsConsultationService().equals('N'))
									&& (ordDtls.getProcedureOrderMstId() == null)
									&& (ordDtls.getIsPeriodicityService() == null || ordDtls.getIsPeriodicityService() == 'N'))
							{
								if(ordDtls.getIsConsultationService()=='Y')
									contractDtoClone.setIsConsultationService(ordDtls.getIsConsultationService());
								else
									contractDtoClone.setIsConsultationService('N');
								contractDtoClone.setPatientClassPercentage(ordDtls.getPatientClassPercentage().doubleValue());
								contractDtoClone.setCurrentDate(ordDtls.getOrderDate() == null ? GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "yyyy-MM-dd")  : GlobalCommonDateUtils.getStringDate(ordDtls.getOrderDate(), "yyyy-MM-dd"));
								 //rate by billingBedCategoryId
							    contractDtoClone.setBillingBedCategoryId(ordDtls.getBedCategoryId()!=null && ordDtls.getBedCategoryId()>0?ordDtls.getBedCategoryId():billingMasterDto.getBillingBedCategoryId());
								
							    Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contractDtoClone, false, false);
								ContractConsumptionDto globalRatesRes = (ContractConsumptionDto) serviceRateRes.getObject();
								
								ordDtls.setCoPayPer(BigDecimal.valueOf(globalRatesRes.getCoShare()));
								ordDtls.setSelfPayable(new BigDecimal(globalRatesRes.getSelfPayable()));
								ordDtls.setCreditPayable(new BigDecimal(globalRatesRes.getCreaditPayable()));
								ordDtls.setServiceAmt(new BigDecimal(globalRatesRes.getServiceRate()));
								ordDtls.setOrdConcession(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:new BigDecimal(globalRatesRes.getConcession()));
								ordDtls.setOrdTotalAmt(new BigDecimal(globalRatesRes.getServiceRate()));
								ordDtls.setNetAmount(new BigDecimal(globalRatesRes.getFinalPrice()));
								ordDtls.setOrdDiscount(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:new BigDecimal(globalRatesRes.getOrdDiscount()));
								ordDtls.setOrdConcessionPer(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:new BigDecimal(globalRatesRes.getPatientClassPercentage()));
							}
							ordDtls.setOrderDetailsPackageMapperDtosList(ordDtls.getForceAddRemoved().equals(1)?ordDtls.getOrderDetailsPackageMapperDtosList():null);
							ordDtls.setOrderDetailsPayeeMapperDtosList(null);
							ordDtls.setOrderDate(ordDtls.getOrderDate() == null ? TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime() : ordDtls.getOrderDate());
							//ordDtls.setSelfPayable(ordDtls.getOrdTotalAmt().subtract(ordDtls.getOrdDiscount()).subtract(ordDtls.getOrdConcession()));
							ordDtls.setSelfPayable(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:ordDtls.getOrdTotalAmt());
							ordDtls.setNetAmount(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:ordDtls.getNetAmount());
							ordDtls.setPackageConsumptionAmt(ordDtls.getForceAddRemoved().equals(1)?ordDtls.getOrdTotalAmt():BigDecimal.ZERO);
							ordDtls.setCreditPayable(BigDecimal.ZERO);
						}
					}
					 if (billingMasterDto.getPharmacyServiceId() != null &&billingMasterDto.getPharmacyServiceId()>0 && (ordDtls.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId()) && ordDtls.getOrdTotalAmt().doubleValue()>0 ) 
					 {
						pharamcyList.add(ordDtls);
					 } 
				}

				Response packageRes = iMPackageMasterService.updatePackage(contractDto);
				if (packageRes.getObject() != null) 
				{
					contractDto = (ContractConsumptionDto) packageRes.getObject();

					for (OrderDetailsMasterDto ordDtlsDto : contractDto.getListOrderDetailsMasterDto()) 
					{
						
						if(!ordDtlsDto.isPackage()  && (ordDtlsDto.getStatus()==null || ordDtlsDto.getStatus().equals('A')) && ordDtlsDto.getOrdTotalAmt().doubleValue()>=0)
						{
							contractDtoClone.setSpecialityId(ordDtlsDto.getDepartmentId());
							contractDtoClone.setServiceId(ordDtlsDto.getServiceId());
							contractDtoClone.setEncounterId(billingMasterDto.getEncounterId());
							contractDtoClone.setAdmissionId(billingMasterDto.getAdmissionId());
							contractDtoClone.setTariffId(billingMasterDto.getTariffId());
							contractDtoClone.setContractId(billingMasterDto.getContractId());
							contractDtoClone.setDefaultSelfTariffId(billingMasterDto.getTariffId());
							contractDtoClone.setCurrentDate(ordDtlsDto.getOrderDate()!=null ? GlobalCommonDateUtils.getStringDate(ordDtlsDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss") :  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
							if (ordDtlsDto.getOrderDetailsPackageMapperDtosList() != null && ordDtlsDto.getOrderDetailsPackageMapperDtosList().size() > 0) 
							{
								if (ordDtlsDto.getSelfPayable().doubleValue() > 0 && !ordDtlsDto.isPackage()
										&& (ordDtlsDto.getPackageTypeId() == null || ordDtlsDto.getPackageTypeId() != 1)) 
								{
									OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
									oDPmpprDto.setIsPackageService('N');
									oDPmpprDto.setIsCapService('N');
									oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
									oDPmpprDto.setServiceAmount(ordDtlsDto.getSelfPayable());
									//ordDtlsDto.setNetAmount(ordDtlsDto.getSelfPayable());
									ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(oDPmpprDto);
								}else {
									OrderDetailsPayeeMapperDto dto = new OrderDetailsPayeeMapperDto();
										dto.setOrderDetailsId(ordDtlsDto.getOrderDetailsId());
										dto.setPayeeAmount(ordDtlsDto.getSelfPayable());
										dto.setPayeeId(billingMasterDto.getSelfPayeeId());
										dto.setStatus('A');
										dto.setOrganizationId(billingMasterDto.getOrganisationId());
										dto.setUnitId(billingMasterDto.getUnitId());
									List<OrderDetailsPayeeMapperDto> list = new ArrayList<>();
										list.add(dto);
									ordDtlsDto.setOrderDetailsPayeeMapperDtosList(list);
								}
								
								if (ordDtlsDto.getSelfPayable().doubleValue() > 0 && !ordDtlsDto.isPackage()
										&& (ordDtlsDto.getPackageTypeId() == null || ordDtlsDto.getPackageTypeId() != 1)) 
								{
									contractDtoClone.setSelfPayable(ordDtlsDto.getSelfPayable().doubleValue());
									contractDtoClone.setOrdDiscount(ordDtlsDto.getOrdDiscount().doubleValue());
									contractDtoClone.setUpdateCap(null);
									Response resContract = iContractConsumptionService.consumeServiceFromContractPackage(contractDtoClone, null);
									ContractConsumptionDto contractResp = (ContractConsumptionDto) resContract.getObject();
									ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
									ordDtlsDto.setTaxAmount(BigDecimal.valueOf(contractResp.getTaxAmt()));
									ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
									ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getFinalPrice()));
									ordDtlsDto.setOrdConcession(BigDecimal.valueOf(contractResp.getConcession()));
									ordDtlsDto.setOrdConcessionPer(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
									ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
									ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
								}
								else if(ordDtlsDto.isPackage()){
									Response resContract = null;
									ServiceSearchResDto searchResDto = new ServiceSearchResDto();
										searchResDto.setAdmissionId(contractDtoClone.getAdmissionId());
										searchResDto.setEncounterId(contractDtoClone.getEncounterId());
										searchResDto.setContractId(contractDtoClone.getContractId());
										searchResDto.setServiceMasterId(contractDtoClone.getServiceId());
										searchResDto.setOrderDate(contractDtoClone.getCurrentDate());
										searchResDto.setOrganizationId(contractDtoClone.getOrganizationId());
										searchResDto.setUnitId(contractDtoClone.getUnitId());
										searchResDto.setVisitTypeId(contractDtoClone.getVisitTypeId());
										searchResDto.setBillingBedCategoryId(contractDtoClone.getBillingBedCategoryId());
										searchResDto.setPatientCategoryId(contractDtoClone.getPatientCategoryId()!=null ? contractDtoClone.getPatientCategoryId() : contractDtoClone.getPatientTypeId());
										searchResDto.setDefaultSelfTariffId(contractDtoClone.getDefaultSelfTariffId());
										searchResDto.setPackageMasterId(ordDtlsDto.getPackageId());
										searchResDto.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
										searchResDto.setOrdDiscAmt(ordDtlsDto.getOrdDiscount().doubleValue());
										searchResDto.setBasePrice(ordDtlsDto.getServiceAmt());
										searchResDto.setPatientClassPer(ordDtlsDto.getPatientClassPercentage()!=null ? ordDtlsDto.getPatientClassPercentage().doubleValue() : 0.00);
										searchResDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
									resContract =iContractConsumptionService.consumePackageFromContract(searchResDto);	
									ContractConsumptionDto contractResp = (ContractConsumptionDto) resContract.getObject();
									ordDtlsDto.setCoPayPer(BigDecimal.valueOf(contractResp.getCoShare()));
									ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
									ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
									ordDtlsDto.setOrdConcession(BigDecimal.valueOf(	contractResp.getConcession() != null ? contractResp.getConcession() : 0));
									ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getNetAmount()));
									ordDtlsDto.setOrdConcessionPer(	BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
									ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
									ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
								}
							} else {
								Response resContract = null;
								List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
								if (ordDtlsDto.getSelfPayable()!=null) 
								{
									OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
									oDPmpprDto.setIsPackageService('N');
									oDPmpprDto.setIsCapService('N');
									oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
									oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
									mppr.add(oDPmpprDto);
									ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
								}

								if (ordDtlsDto.getSelfPayable()!=null && !ordDtlsDto.isPackage()) 
								{
									contractDtoClone.setSelfPayable(ordDtlsDto.getSelfPayable().doubleValue());
									contractDtoClone.setFinalPrice(ordDtlsDto.getNetAmount().doubleValue());
									contractDtoClone.setIsConsultationService(ordDtlsDto.getIsConsultationService());

									if (ordDtlsDto.getIsConsultationService() != null
											&& ordDtlsDto.getIsConsultationService().equals('Y')) 
									{
										//contractDtoClone.setOrdDiscount(ordDtlsDto.getOrdDiscount().doubleValue());
										contractDtoClone.setOrdDiscountPer(ordDtlsDto.getOrdDiscountPer().doubleValue());
										contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
										contractDtoClone.setConsultationAmount((ordDtlsDto.getServiceAmt().doubleValue()));
										contractDtoClone.setIsPeriodicityService('N');
									}else if (ordDtlsDto.getIsPeriodicityService() != null && ordDtlsDto.getIsPeriodicityService() == 'Y') 
									{
										contractDtoClone.setQuantity(ordDtlsDto.getOrderQty().doubleValue());
										contractDtoClone.setServiceRate(ordDtlsDto.getServiceAmt().doubleValue());
										contractDtoClone.setConsultationAmount(null);
										contractDtoClone.setIsPeriodicityService('Y');
										//resContract = iContractConsumptionService.consumeServiceFromContractForPeriodicityBilling(contractDtoClone);
									}else 
									{
										// for bill of material services
										if (ordDtlsDto.getProcedureOrderMstId() != null	&& ordDtlsDto.getProcedureOrderMstId() > 0 && ordDtlsDto.getIsProcedureService() == 'Y') 
										{
											contractDtoClone.setProcedureId(ordDtlsDto.getProcedureOrderMstId());
											contractDtoClone.setServiceRate(ordDtlsDto.getServiceAmt().doubleValue());
											contractDtoClone.setIsBOM('Y');
										}if (ordDtlsDto.getProcedureOrderMstId() != null && ordDtlsDto.getProcedureOrderMstId() > 0 && ordDtlsDto.getIsProcedureService() == 'N')
										{
											contractDtoClone.setProcedureId(ordDtlsDto.getProcedureOrderMstId());
											contractDtoClone.setServiceRate(ordDtlsDto.getServiceAmt().doubleValue());
											contractDtoClone.setIsBOM('N');
										}
										else {
											contractDtoClone.setProcedureId(null);
											contractDtoClone.setServiceRate(null);
											contractDtoClone.setIsBOM('N');
										}
										contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
										//contractDtoClone.setOrdDiscount(ordDtlsDto.getOrdDiscount().doubleValue());
										contractDtoClone.setOrdDiscountPer(ordDtlsDto.getOrdDiscountPer().doubleValue());
										contractDtoClone.setConsultationAmount(null);
									}
									if(ordDtlsDto.isPackage()){
										
										ServiceSearchResDto searchResDto = new ServiceSearchResDto();
											searchResDto.setAdmissionId(contractDtoClone.getAdmissionId());
											searchResDto.setEncounterId(contractDtoClone.getEncounterId());
											searchResDto.setContractId(contractDtoClone.getContractId());
											searchResDto.setServiceMasterId(contractDtoClone.getServiceId());
											searchResDto.setOrderDate(contractDtoClone.getCurrentDate());
											searchResDto.setOrganizationId(contractDtoClone.getOrganizationId());
											searchResDto.setUnitId(contractDtoClone.getUnitId());
											searchResDto.setVisitTypeId(contractDtoClone.getVisitTypeId());
											searchResDto.setBillingBedCategoryId(contractDtoClone.getBillingBedCategoryId());
											searchResDto.setPatientCategoryId(contractDtoClone.getPatientCategoryId()!=null ? contractDtoClone.getPatientCategoryId() : contractDtoClone.getPatientTypeId());
											searchResDto.setDefaultSelfTariffId(contractDtoClone.getDefaultSelfTariffId());
											searchResDto.setPackageMasterId(ordDtlsDto.getPackageId());
											searchResDto.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
											searchResDto.setBasePrice(ordDtlsDto.getServiceAmt());
											searchResDto.setPatientClassPer(ordDtlsDto.getPatientClassPercentage()!=null ? ordDtlsDto.getPatientClassPercentage().doubleValue() : 0.00);
											searchResDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
										resContract =iContractConsumptionService.consumePackageFromContract(searchResDto);	
									}else {
										contractDtoClone.setUpdateCap(null);
										contractDtoClone.setPatientClassPercentage(ordDtlsDto.getPatientClassPercentage().doubleValue());
										resContract = iContractConsumptionService.consumeServiceFromContract(contractDtoClone);
									}
									ContractConsumptionDto contractResp = new ContractConsumptionDto();
									contractResp = (ContractConsumptionDto) resContract.getObject();

									// for bill of material services
									if (ordDtlsDto.getProcedureOrderMstId() != null
											&& ordDtlsDto.getProcedureOrderMstId() > 0
											&& ordDtlsDto.getIsProcedureService() == 'Y') 
									{
										contractResp.setServiceId(ordDtlsDto.getServiceId());

										if (contractResp.getOrderDetailsPayeeMapperDtosList().size() > 0)
											for (OrderDetailsPayeeMapperDto odPayeeDto : contractResp.getOrderDetailsPayeeMapperDtosList()) 
											{
												odPayeeDto.setServiceId(ordDtlsDto.getServiceId());
											}

										if (contractResp.getOrderDetailsPackageMapperDtosList() != null
												&& contractResp.getOrderDetailsPackageMapperDtosList().size() > 0)
											for (OrderDetailsPackageMapperDto odPayeeDto : contractResp.getOrderDetailsPackageMapperDtosList()) 
											{
												odPayeeDto.setServiceId(ordDtlsDto.getServiceId());
											}

									}
									ordDtlsDto.setCoPayPer(BigDecimal.valueOf(contractResp.getCoShare()));
									ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
									ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
									ordDtlsDto.setOrdConcession(BigDecimal.valueOf(contractResp.getConcession() != null ? contractResp.getConcession() : 0));
									ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getNetAmount()));
									ordDtlsDto.setOrdConcessionPer(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
									ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
									ordDtlsDto.setOrdDiscount(BigDecimal.valueOf(contractResp.getOrdDiscount()));
									ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
								//}
							}
						}
					  }
					}
					billingMasterDto.setOrderDetailsMasterDtosList(contractDto.getListOrderDetailsMasterDto());
					billingMasterDto.setIsFinalBill('N');
					billingMasterDto.setIsIntermediateBill('N');
					billingMasterDto.setIsFinancialClearance('N');
					this.saveBillService(billingMasterDto);
					if (res.getObject() != null) {
						// ehc consultation services
						Map<Integer, Integer> ehcPackageIds = (Map) res.getObject();
						if (ehcPackageIds != null) {
							for (Map.Entry<Integer, Integer> entry : ehcPackageIds.entrySet()) {
								EncounterMasterDto dto = new EncounterMasterDto();
								dto.setOrderDetailsIdEhc(entry.getValue());
								dto.setEhcPackageId(entry.getKey());
								dto.setOrganizationId(billingMasterDto.getOrganisationId());
								dto.setUnitId(billingMasterDto.getUnitId());
								dto.setCreatedBy(billingMasterDto.getCreatedBy());
								dto.setUpdatedBy(billingMasterDto.getCreatedBy());
								dto.setEncounterId(billingMasterDto.getEncounterId());
								dto.setCreatedDate(billingMasterDto.getCreatedDate());
								iMPackageMasterService.saveEhcEncounterEhcConsultation(dto);
							}
						}
					}
					iWalkInSalesService.updatePayeePharamcy(pharamcyList, billingMasterDto);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public Response printLog(int type, int userId, int billId) {
		try {
			return billingMasterDao.printLog(type,userId,billId);
		}catch(Exception e){
			throw new BillingException(e.getMessage());
		}
	}
	@Transactional
	@Override
	public Response receiptPrintLog(int type, int userId, int recieptId) {
		try {
			return billingMasterDao.receiptPrintLog(type,userId,recieptId);
		}catch(Exception e){
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getSmartCardBalance(SmartcardClaimDto smartcardClaimDto) throws ApplicationException, Exception {
		try {

			return billingMasterDao.getSmartCardBalance(smartcardClaimDto);

		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getBillReferenceData(BillListReqDto reqDto) throws ApplicationException, Exception {
		try {
			return billingMasterDao.getBillReferenceData(reqDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public BillListResDto getBillSumsEncounterId(Integer encounterId) throws ApplicationException, Exception {
		// TODO Auto-generated method stub
		try {
			return billingMasterDao.getBillSumsEncounterId(encounterId);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updatePackageEffectiveDate(TPackageConsumptionMasterDto tPackageConsumptionMasterDto) {
		try {
			return iTPackageConsumptionMasterDao.updatePackageApplicableDate(tPackageConsumptionMasterDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response checkDischargeOrNot(DischargeDto dischargeDto) throws ApplicationException, Exception {
		try {
			return billingMasterDao.checkDischargeOrNot(dischargeDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public void saveBillStatusId(Integer object) {
		 billingMasterDao.saveBillStatusId(object);
		
	}

	@Override
	public Response printReceiptRPTReport(int orgId, int unitId,int billingMasterId, int receiptId, int depositConsumptionId)
			throws ApplicationException {
		try {
			 String json = "{ \"appName\": \"HIS\","
			 		+ "\"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\","
			 		+ "\"reportDesignFilePath\": \"OPDPaymentReceipt_listview.rpt\","
			 		+ "\"reportFormat\": \"PDF\","
			 		+ "\"exportFileName\": \"PaymentReciept\","
			 		+ "\"reportParams\": "
			 		+ "			[    "
			 		+ "				{ "
			 		+ "					\"paramName\": \"@BillID\","
			 		+ "					\"paramType\": \"Integer\",      "
			 		+ "					\"paramValue\":"+billingMasterId+" "
			 		+ "				},    "
			 		+ "				{   \"paramName\": \"@PaymentID\","
			 		+ "					\"paramType\": \"Integer\","
			 		+ "					\"paramValue\": "+receiptId+" "
			 		+ "				},"
			 		+ "				{   \"paramName\": \"@dep_consumption_id\","
			 		+ "					\"paramType\": \"Integer\","
			 		+ "					\"paramValue\": "+depositConsumptionId+" "
			 		+ "				},"
			 		+ "				{	\"paramName\": \"@isDuplicate\","
			 		+ "					\"paramType\": \"Integer\","
			 		+ "					\"paramValue\": 0"
			 		+ "				}"
			 		+ "			]"
			 		+ "}";
				 ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
					CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);

					if (API_URL == null)
						return null;

					HttpPost post = new HttpPost(API_URL);
					post.addHeader("Content-Type", "application/json");
					post.setEntity(new StringEntity(json));

					HttpResponse result = httpClient.execute(post);
					HttpEntity resEntity = result.getEntity();
					resEntity.writeTo(byteOutPutStream);
					byteOutPutStream.close();
					return new Response(SUCCESS, SUCCESS_CODE, null, null, byteOutPutStream.toByteArray());
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateAdvisedBy(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateAdvisedBy(billingMasterDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public boolean matchBillAmounts(BillingMasterDto billingMasterDto, Set<Integer> orderDetailsIdList) {
		return iBillingMasterDao.matchBillAmounts(billingMasterDto,orderDetailsIdList);
	}

	@Override
	@Transactional
	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iBillingMasterDao.getAllInclusivePackageConsumeAmount(dto);
	}

	@Override
	@Transactional
	public Response getFreeTextOrderServices(BillingMasterDto dto) throws ApplicationException {
		try {
			return iBillingMasterDao.getFreeTextOrderServices(dto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public void updateReceivedFrom(Object resObject, List<PaymentReceiptDetailsDto> list) {
		billingMasterDao.updateReceivedFrom(resObject,list);
		
	}

	@Override
	public String getTallyInterfaceDataInJsonFormat(CabinMasterDto dto) throws ApplicationException {
		try {
			return iBillingMasterDao.getTallyInterfaceDataInJsonFormat(dto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}


	@Override
	public String updateTallyInterfaceDataById(CabinMasterDto dto) throws ApplicationException {
		try {
			return iBillingMasterDao.updateTallyInterfaceDataById(dto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getDataForCheckBillStatusByBillNumber(BillingMasterDto dto) throws ApplicationException {
		try {
			return iBillingMasterDao.getDataForCheckBillStatusByBillNumber(dto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateRenderedDate(BillingMasterDto dto) {
		return iBillingMasterDao.updateRenderedDate(dto);
	}

	@Override
	public String getTallyInterfaceDataInJsonFormatByMode(CabinMasterDto dto) throws ApplicationException {
		try {
			return iBillingMasterDao.getTallyInterfaceDataInJsonFormatByMode(dto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public String updateTallyInterfaceDataByIdByMode(CabinMasterDto dto) throws ApplicationException {
		try {
			return iBillingMasterDao.updateTallyInterfaceDataByIdByMode(dto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response patientCouponTemplate(Integer patientCouponTemplateMapperId) throws ApplicationException {
		try {
			return iBillingMasterDao.patientCouponTemplate(patientCouponTemplateMapperId);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDataForFinanacialRequest(RequestDiscountDto requestDiscountDto) throws ApplicationException {
		try {
			return iBillingMasterDao.getDataForFinanacialRequest(requestDiscountDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getOrderSumNetAndSelfCreditAmount(List<Integer> orderDetailsIds) throws ApplicationException {
		try {
			return iBillingMasterDao.getOrderSumNetAndSelfCreditAmount(orderDetailsIds);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDataOfPatientToCheckDueAmountOfPharmacyService(BillingMasterDto billingMasterDto)
			throws ApplicationException {
		try {
			return iBillingMasterDao.getDataOfPatientToCheckDueAmountOfPharmacyService(billingMasterDto);
        } catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getBillIdByEncounterId(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			return iBillingMasterDao.getBillIdByEncounterId(billingMasterDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response checkCapWithPackageBalanceAmount(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			
			Response res  =  iBillingMasterDao.getCapWithPackageBalanceAmount(billingMasterDto);
			Boolean checkFlag = false;
			if(res.getListObject() != null && res.getListObject().size()>0) {
				
				List<TPackageConsumptionCapDetailsDto> list = res.getListObject();
				
				for (TPackageConsumptionCapDetailsDto tPackageConsumptionCapDetailsDto : list) {
				
					Response res1 = iBillingMasterDao.checkCapWithPackageAndOrderAmount(billingMasterDto,tPackageConsumptionCapDetailsDto);
					
					if(res1.getListObject() != null && res1.getListObject().size()>0) {
						
						if(tPackageConsumptionCapDetailsDto.getAvailableCap() > 0) {
							checkFlag = true;
						}
						
						/*Response consumeAmount = iBillingMasterDao.checkCapWithPackageAndConsumeAmount(billingMasterDto,tPackageConsumptionCapDetailsDto);
						
						if(consumeAmount.getObject()!= null) {
							
							OrderDetailsMasterDto obj  = (OrderDetailsMasterDto) consumeAmount.getObject();
							
							if((obj.getPackageConsumptionAmt().doubleValue()) < tPackageConsumptionCapDetailsDto.getCapAmount()) {
								checkFlag = true;
							}
							
						}*/
					}
					
				}
				
			}
			
			return new Response(SUCCESS, null, null, null, checkFlag);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response checkCapWithPackageBalanceAmountAndOrderAmount(BillingMasterDto billingMasterDto)
			throws ApplicationException {

		try {
			
			Response res  =  iBillingMasterDao.getCapWithPackageBalanceAmount(billingMasterDto);
			Boolean checkFlag = false;
			if(res.getListObject() != null && res.getListObject().size()>0) {
				
				List<TPackageConsumptionCapDetailsDto> list = res.getListObject();
				
				return iBillingMasterDao.checkCapWithPackageBalanceAmountAndOrderAmount(billingMasterDto,list.getFirst().getPackageApplicableDate());
				
			}
			return new Response(SUCCESS, null, null,null, null/*,null,pharreturnList*/);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	
	}

	@Override
	@Transactional
	public Response adjustCapWithPackageAvailableAmount(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			return iBillingMasterDao.adjustCapWithPackageAvailableAmount(billingMasterDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response resetCapWithPackage(BillingMasterDto billingMasterDto) throws ApplicationException {
		try {
			return iBillingMasterDao.resetCapWithPackage(billingMasterDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response storePackCapAmountLog(OrderDetailsMasterDto detailsMasterDto)
			throws ApplicationException {
		try {
			
			
			if(detailsMasterDto.getForceAddRemoved()==1) {
				Response res =  iBillingMasterDao.storePackCapAmountLog(detailsMasterDto);
				
				if(res.getListObject() != null && res.getListObject().size()>0) {
					
					List<OrdeDetailsPackageMapperLogDto> list = res.getListObject();
					
					for (OrdeDetailsPackageMapperLogDto dto : list) {
						OrdeDetailsPackageMapperLog master = new OrdeDetailsPackageMapperLog();
						master.setOrderDetailsId(dto.getOrderDetailsId());
						master.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
						master.setOrganizationId(detailsMasterDto.getOrgId());
						master.setPackageAmount(dto.getPackageAmount());
						master.setPackageId(dto.getPackageId());
						master.setUnitId(detailsMasterDto.getOrgUnitId());
						master.setCreatedBy(detailsMasterDto.getUpdatedBy());
						master.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
						
						iOrdeDetailsPackageMapperLogDao.savePackageConsumptionLog(master);
					}
				}else {
					iOrdeDetailsPackageMapperLogDao.updatePackageConsumptionLog(detailsMasterDto);
				}
			}else if(detailsMasterDto.getForceAddRemoved()==-1)	{
				iOrdeDetailsPackageMapperLogDao.updatePackageConsumptionLog(detailsMasterDto);
			}
			
			
			
			return null;
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response checkPackageMinusAmount(BillingMasterDto billingMasterDto) throws ApplicationException {

		try {
			return iBillingMasterDao.checkPackageMinusAmount(billingMasterDto);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	
	}

	@Override
	@Transactional
	public Response checkBillCancelledOrNot(BillingMasterDto billingMasterDto) throws ApplicationException {

		try {
			return iBillingMasterDao.checkBillCancelledOrNot(billingMasterDto);
		} catch (Exception e) {

			throw new BillingException(e.getMessage());
		}
	}

	
	@Override
	@Transactional
	public Response getBillNoByPatientForMyLife(BillListReqDto billListReqDto) throws ApplicationException {
		try {
			Response res = billingMasterDao.getBillNoByPatientForMyLife(billListReqDto);
			List<BillListCNDto> listBillListCNDto = res.getListObject();
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			for(BillListCNDto item : listBillListCNDto){
				LocalTime timeSlot1 = LocalTime.parse(item.getEncounterTime(), timeFormatter);
				ZonedDateTime zonedDateTime = timeSlot1.atDate(LocalDate.now())  // Combine with current date
						.atZone(ZoneId.systemDefault());
				Instant timeSlot = zonedDateTime.toInstant();
				item.setTimeSlot(timeSlot);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listBillListCNDto, null);
		} catch (Exception e) {
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateDoctorIdAndSpecialityId(AdmissionDto dto) throws ApplicationException {

		try {
			return iBillingMasterDao.updateDoctorIdAndSpecialityId(dto);
		} catch (Exception e) {

			throw new BillingException(e.getMessage());
		}
	
	}

	@Override
	@Transactional
	public Response updateOrderdetailsPackageMapper(Integer encounterId, Integer orgId, Integer unitId, Integer visitTypeId) throws ApplicationException {
		try {
			return iBillingMasterDao.updateOrderdetailsPackageMapper(encounterId,orgId,unitId,visitTypeId);
		} catch (Exception e) {

			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getOrderDetailsPackageMapperDetails(List<Integer> orderdId) throws ApplicationException {
		return  iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(orderdId);
	}

    @Override
    @Transactional
    public Response orderedServicesByMobileNoForWebUser(AdmissionDto admissionDto) throws ApplicationException {
        try {
             List <AdmissionDto> patientsList = iOrderDetailsMasterDao.getPatientsDetailsListByMobileNumber(admissionDto);
             if(patientsList != null && !patientsList.isEmpty()){
                 for(AdmissionDto admDto : patientsList) {

                     admDto.setVisitTypeId(1);

                     BillListReqDto billReqDto = new BillListReqDto();
                     billReqDto.setPatientId(admDto.getPatientId());
                     billReqDto.setVisitTypeId(admDto.getVisitTypeId());
                     billReqDto.setUnitId(admissionDto.getUnitId());
                     billReqDto.setOrgId(admissionDto.getOrganizationId());
                     Response billListRes = billingMasterDao.getBillsByFilterForBillList(billReqDto);

                     List<BillListResDto> listBills = new ArrayList<>();
                     if(billListRes.getStatus().equals(SUCCESS) && billListRes.getListObject() != null && !billListRes.getListObject().isEmpty()){
                         listBills = billListRes.getListObject();
                         List<Integer> billIds = listBills.stream()
                                 .map(BillListResDto::getBillingMasterId)
                                 .collect(Collectors.toList());

                         Map<Integer, Integer> billMap = listBills.stream()
                             .collect(Collectors.toMap(
                                     BillListResDto::getBillingMasterId,
                                     BillListResDto::getAppointmentId
                             ));

                         if(billIds != null && !billIds.isEmpty()) {
                             Response billDetailsRes = iBillingMasterDao.getBillDetailsByListOfBillIds(billIds);

                             if (billDetailsRes.getStatus().equals(SUCCESS) && billDetailsRes.getListObject() != null && !billDetailsRes.getListObject().isEmpty()) {
                                 List<Object> ordersList = billDetailsRes.getListObject();
                                 for(Object obj : ordersList){
                                     BillDetailsByBillIdDto order = (BillDetailsByBillIdDto)obj;
                                     Integer billingMasterId = order.getBillingMasterId();
                                     if (billMap.containsKey(billingMasterId)) {
                                         order.setAppointmentId(billMap.get(billingMasterId));
                                     }
                                 }

                                 admDto.setOrderDetailsList(ordersList);
                             }
                         }
                     }
                 }

                 return new Response<>(SUCCESS, SUCCESS_CODE, null, patientsList, null);
             }
            return new Response<>(ERROR, COMMON_ERROR_CODE, "Record Not Found", null, null);
        } catch (Exception e) {
            throw new BillingException(e.getMessage());
        }
    }

}
