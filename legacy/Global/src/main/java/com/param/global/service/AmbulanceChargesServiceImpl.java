package com.param.global.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IpdChargesDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IAmbulanceChargesDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AmbulanceChargesDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.SubSpecialityMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class AmbulanceChargesServiceImpl implements IAmbulanceChargesService, ICommonConstants {

	final static Logger logger = Logger.getLogger(AmbulanceChargesServiceImpl.class);

	@Autowired
	private IAmbulanceChargesDao iAmbulanceChargesDao;
	
	@Autowired 
	IpdChargesDao ipdChargesDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	private IOrderMasterService iOrderMasterService;
	
	
	@Override
	public Response saveAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			OrderMasterDto orderMst = null;
			Integer  patientTypeId = null;
			Integer  patientClassId = null;
			Integer  billingBedCategoryId = null;
			Integer  paymentEntitlementId = null;
			Integer  contractId = null;
			Integer  payeeId = null;
			Date today = TimeZoneComponent.getDateByzone(ambulanceChargesDto.getUnitId());
			Integer  wardId = null;
			Integer  roomId = null;
			Integer  bedId = null;
			Integer visitTypeId =null;
			
			UnitApplicationConfigurationDto uAppConfDto = new UnitApplicationConfigurationDto();
			uAppConfDto.setOrganizationId(ambulanceChargesDto.getOrganizationId());
			uAppConfDto.setUnitId(ambulanceChargesDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(uAppConfDto);
			if (appConfigRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && appConfigRes.getObject() != null) 
			{
				uAppConfDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
			}
			if(ambulanceChargesDto.getAdmissionId()!=null && ambulanceChargesDto.getAdmissionId() > 0 ) 
			{
				
				Response admissionDto = ipdChargesDao.getAdmissionsById(ambulanceChargesDto.getAdmissionId());
				AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
					patientTypeId = adminDto.getPatientCategoryId();
					billingBedCategoryId = adminDto.getBedBillingCategoryId();
					paymentEntitlementId = adminDto.getPaymentEntitlementId();
					contractId = adminDto.getContractId();
					payeeId = adminDto.getPayeeId();
					visitTypeId = adminDto.getVisitTypeId();
					patientClassId = adminDto.getPatientClassId();
					wardId = adminDto.getWardId();
					roomId = adminDto.getRoomId();
					bedId =  adminDto.getBedId();
			}
			
			if(ambulanceChargesDto.getEncounterId()!=null && ambulanceChargesDto.getEncounterId() >0) 
			{
				
				Response encounterRes = iEncounterMasterService.getEncounterDetailsById(ambulanceChargesDto.getEncounterId());
				EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
					patientTypeId = encounterMasterDto.getPatientCategoryId();
					paymentEntitlementId = encounterMasterDto.getPaymentEntitlementId();
					visitTypeId = 1;
					contractId = encounterMasterDto.getContractId();
					payeeId = encounterMasterDto.getPayeeId();
					patientClassId = encounterMasterDto.getPatientClassId();
					billingBedCategoryId = uAppConfDto.getOpdBillingCatId();
			}

			BigDecimal serviceAmount = new BigDecimal(ambulanceChargesDto.getLpoAmount());
			
			// insert order master
			orderMst = new OrderMasterDto();
			orderMst.setAdmissionId(ambulanceChargesDto.getAdmissionId());
			orderMst.setEncounterId(ambulanceChargesDto.getEncounterId());
			orderMst.setCreatedBy(ambulanceChargesDto.getCreatedBy());
			orderMst.setCreatedDate(today.getTime());
			orderMst.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "dd-M-yyyy"));
			orderMst.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
			orderMst.setDiscountPercentage(0);
			orderMst.setOrderDate(today.getTime());
			orderMst.setOrderStatus(ACTIVE);
			orderMst.setOrderStatusId(1);
			orderMst.setOrdRemarks("Ambulance Service Order");
			orderMst.setOrgId(ambulanceChargesDto.getOrganizationId());
			orderMst.setOrgUnitId(ambulanceChargesDto.getUnitId());
			orderMst.setPatientId(ambulanceChargesDto.getPatientId());
			orderMst.setPaymentEntitlementTypeId(paymentEntitlementId);
			orderMst.setPriorityId(1);
			orderMst.setUpdatedBy(ambulanceChargesDto.getCreatedBy());
			orderMst.setUpdatedDate(today.getTime());
			orderMst.setVisitTypeId(visitTypeId);
			Response subn = iEncounterMasterService.getSubSpecialityById(ambulanceChargesDto.getServiceId());
			SubSpecialityMasterDto dto = new SubSpecialityMasterDto();
			
			if(subn.getObject() != null) {
				 dto = (SubSpecialityMasterDto) subn.getObject();
			}
					List<OrderDetailsMasterDto> listOrderDetails = new ArrayList<OrderDetailsMasterDto>();
					OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
						orderDetails.setCreatedBy(ambulanceChargesDto.getCreatedBy());
						orderDetails.setCreatedDate(today.getTime());
						orderDetails.setNetAmount(serviceAmount);
						orderDetails.setOrderDate(today.getTime());
						orderDetails.setOrderQty(1);
						orderDetails.setOrdTotalAmt(serviceAmount);
						orderDetails.setOrgId(ambulanceChargesDto.getOrganizationId());
						orderDetails.setOrgUnitId(ambulanceChargesDto.getUnitId());
						orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
						orderDetails.setServiceAmt(serviceAmount);
						orderDetails.setServiceId(ambulanceChargesDto.getServiceId());
						orderDetails.setServiceIsBilled(0);
						orderDetails.setServiceRendered(0);
						orderDetails.setStatus(ACTIVE);
						orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
						orderDetails.setUpdatedBy(ambulanceChargesDto.getCreatedBy());
						orderDetails.setUpdatedDate(today.getTime());
						orderDetails.setPriorityId(2);
						orderDetails.setIsOutsourced('N');
						orderDetails.setOrderSchDate(today.getTime());
						orderDetails.setOrdConcession(BigDecimal.valueOf(0));
						orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
						orderDetails.setServiceRenderingDeptId(dto.getSubSpecialityId());
						orderDetails.setOrdCancelled('N');
						orderDetails.setIsDrug('N');
						orderDetails.setIsProcedure(NO);
						orderDetails.setIsProcedureService(NO);
						orderDetails.setOrderDate(today.getTime());
						orderDetails.setOrdRenderDatetime(today.getTime());
						orderDetails.setIsInvestigationOrder('N');
						orderDetails.setWardId(wardId);
						orderDetails.setRoomId(roomId);
						orderDetails.setBedId(bedId);
						orderDetails.setSelfPayable(serviceAmount);
						orderDetails.setCreditPayable(new BigDecimal(0));
						orderDetails.setPackageConsumptionAmt(new BigDecimal(0));
						//orderDetails.setProcedureOrderMstId(proOrder.getProcedureOrderId());

						ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
							consumptionDto.setOrganizationId(ambulanceChargesDto.getOrganizationId());
							consumptionDto.setUnitId(ambulanceChargesDto.getUnitId());
							consumptionDto.setVisitTypeId(visitTypeId);
							consumptionDto.setPatientTypeId(patientTypeId);
							consumptionDto.setBillingBedCategoryId(billingBedCategoryId);
							consumptionDto.setPaymentEntitlementId(paymentEntitlementId);
							consumptionDto.setServiceId(ambulanceChargesDto.getServiceId());
							consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "yyyy-MM-dd"));
							consumptionDto.setEncounterId(ambulanceChargesDto.getEncounterId());
							consumptionDto.setAdmissionId(ambulanceChargesDto.getAdmissionId());
							consumptionDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
							consumptionDto.setSelfPayeeId(payeeId);
							consumptionDto.setPatientId(ambulanceChargesDto.getPatientId());
							consumptionDto.setContractId(contractId);
							consumptionDto.setPatientCategoryId(patientTypeId);
							consumptionDto.setPatientClassId(patientClassId);
							consumptionDto.setChangedPrice(serviceAmount.doubleValue());
							consumptionDto.setFinalPrice(serviceAmount.doubleValue());
						Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto = (ContractConsumptionDto) res.getObject();

					if (res.getObject() != null) 
					{
						
						Double onePeramount = consumptionDto.getNetAmount()/100;
						orderDetails.setNetAmount( new BigDecimal( consumptionDto.getNetAmount()));
						orderDetails.setOrdTotalAmt( new BigDecimal( consumptionDto.getNetAmount()));
						orderDetails.setConcessionPer(consumptionDto.getPatientClassPercentage() != null ? consumptionDto.getPatientClassPercentage() : 0);
						orderDetails.setPatientClassPercentage(consumptionDto.getPatientClassPercentage() != null ?  new BigDecimal(consumptionDto.getPatientClassPercentage())  : new BigDecimal(0));
						orderDetails.setSelfPayable(new BigDecimal(onePeramount * consumptionDto.getCoShare() ));
						orderDetails.setCreditPayable(new BigDecimal(onePeramount * ( 100 - consumptionDto.getCoShare()))); 
						orderDetails.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
						orderDetails.setOrdDiscount(new BigDecimal(0.00));
						orderDetails.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
						orderDetails.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage() != null ? consumptionDto.getTaxPercentage() : 0.00));
						orderDetails.setTaxId(consumptionDto.getTaxId());
						orderDetails.setPayeeId(consumptionDto.getPayeeId());
						orderDetails.setContractId(consumptionDto.getContractId());
						orderDetails.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
						orderDetails.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
						
						if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
							orderDetails.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						
						listOrderDetails.add(orderDetails);
					}
						
						
						orderMst.setListOrderDetailsMasterDto(listOrderDetails);
						Response order =  iOrderMasterService.saveOrderMasterWithDiscount(orderMst, null);
						List<Integer> orderDetailsIdsList = new LinkedList<>();
						if(order.getListObject() != null && order.getListObject().size()>0) {
							 orderDetailsIdsList.addAll(order.getListObject());
						}
						ambulanceChargesDto.setOrderDetailsId(orderDetailsIdsList.get(0));
						return iAmbulanceChargesDao.saveAmbulanceCharges(ambulanceChargesDto);

		} catch (Exception e) {
			logger.error("saveAmbulanceCharges failed", e);
			throw new ServiceException(e.getMessage());
		}
}
	
	
	@Override
	public Response getAmbulanceChargesList(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			return iAmbulanceChargesDao.getAmbulanceChargesList(ambulanceChargesDto);
		} catch (Exception e) {
			logger.error("getAmbulanceChargesList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getAmbulanceChargesActiveList(AmbulanceChargesDto ambulanceChargesDto)
			throws ApplicationException {
		try {
			return iAmbulanceChargesDao.getAmbulanceChargesActiveList(ambulanceChargesDto);
		} catch (Exception e) {
			logger.error("getAmbulanceChargesActiveList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getSingleAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			return iAmbulanceChargesDao.getSingleAmbulanceCharges(ambulanceChargesDto);
		} catch (Exception e) {
			logger.error("getSingleAmbulanceCharges failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			return iAmbulanceChargesDao.updateAmbulanceCharges(ambulanceChargesDto);
		} catch (Exception e) {
			logger.error("updateAmbulanceCharges failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response changeAmbulanceChargesStatus(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			return iAmbulanceChargesDao.changeAmbulanceChargesStatus(ambulanceChargesDto);
		} catch (Exception e) {
			logger.error("changeAmbulanceChargesStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response cancelOrder(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			return iAmbulanceChargesDao.cancelOrder(ambulanceChargesDto);
		} catch (Exception e) {
			logger.error("cancelOrder failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
