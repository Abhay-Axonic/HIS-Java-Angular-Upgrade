package com.param.global.billing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.billing.global.CostEstimateDto;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.BillingStatusMapperId;
import com.param.entity.model.master.Gender;
import com.param.entity.model.master.Prefix;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.dto.BillingStatusMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.global.dto.CancelOrderDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked"})
public class BillingServiceImpl implements IBillingService,ICommonConstants,IBillingCommonConstants {

	final static Logger logger = Logger.getLogger(BillingServiceImpl.class);

	@Autowired
	IBillingDao iBillingDao;
	
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	
	@Autowired
	private IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	
	@Autowired
	IOrderMasterDao iOrderMasterDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IWalkInSalesService iWalkInSalesService;
	
	@Autowired
	IWalkInSalesDao WalkInSalesDao;
	
	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;
	
	@Autowired
	private IPatientRegistrationDao iPatientRegistrationDao;
	
	@Override
	public Response postServiceToEncounterBill(OrderDetailsMaster orderDetailsMasterDto,AdmissionsForBatchProcessDto vistDetailsObj,List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList,List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtoList) {
		try{
			Set<Integer> orderDetailsIdList = new LinkedHashSet<>();
			Integer billStatusId = 0;
			orderDetailsIdList.add(orderDetailsMasterDto.getOrderDetailsId());
			
			BillingMaster billingMasterObj =iBillingDao.getBillMasterObject(vistDetailsObj.getBillId());
				billingMasterObj.setNetAmount(billingMasterObj.getNetAmount()+orderDetailsMasterDto.getNetAmount().doubleValue());
				billingMasterObj.setGst(billingMasterObj.getGst()+orderDetailsMasterDto.getTaxAmount().doubleValue());
				billingMasterObj.setTotalBillAmount(billingMasterObj.getTotalBillAmount()+orderDetailsMasterDto.getOrdTotalAmt().doubleValue());
				billingMasterObj.setOutstanding(billingMasterObj.getOutstanding()+orderDetailsMasterDto.getNetAmount().doubleValue());
				billingMasterObj.setSelfPayable(billingMasterObj.getSelfPayable()+orderDetailsMasterDto.getSelfPayable().doubleValue());
				billingMasterObj.setCreditPayable(billingMasterObj.getCreditPayable()+orderDetailsMasterDto.getCreditPayable().doubleValue());
				
				if(billingMasterObj.getOutstanding().intValue()==0)
					billingMasterObj.setBillStatusId(3);
	  			else if(((billingMasterObj.getSelfPayable() > 0.00 || (billingMasterObj.getCreditPayable()>0.00))))
	  				billingMasterObj.setBillStatusId(2);
	  			else
	  				billingMasterObj.setBillStatusId(1);
				billStatusId = billingMasterObj.getBillStatusId();
			iBillingDao.updateBillMaster(billingMasterObj);
			
				BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
					billingDetailsDto.setBillingMasterId(vistDetailsObj.getBillId());
					billingDetailsDto.setServiceId(orderDetailsMasterDto.getServiceId());
					billingDetailsDto.setRate(orderDetailsMasterDto.getServiceAmt()!=null ? Double.valueOf(orderDetailsMasterDto.getServiceAmt().doubleValue()): 0.00);
					billingDetailsDto.setQuantity(orderDetailsMasterDto.getOrderQty().doubleValue());
					billingDetailsDto.setConcession(0.00);
					billingDetailsDto.setGrossAmount(orderDetailsMasterDto.getOrdTotalAmt()!=null ? Double.valueOf(orderDetailsMasterDto.getOrdTotalAmt().doubleValue()):0.00);
					billingDetailsDto.setCoPayPercentage(orderDetailsMasterDto.getCoPayPer() !=null ? Double.valueOf(orderDetailsMasterDto.getCoPayPer().doubleValue()) : Double.valueOf(0.00));
					billingDetailsDto.setUnitId(orderDetailsMasterDto.getOrgUnitId());
					billingDetailsDto.setOrganizationId(orderDetailsMasterDto.getOrgId());
					billingDetailsDto.setStatus('A');
					billingDetailsDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
					billingDetailsDto.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
					billingDetailsDto.setCreatedDate(billingMasterObj.getBillDateTime());
					billingDetailsDto.setUpdatedDate(billingMasterObj.getBillDateTime());
					billingDetailsDto.setDiscount(orderDetailsMasterDto.getOrdDiscount()!=null ? Double.valueOf(orderDetailsMasterDto.getOrdDiscount().doubleValue()):0.00);
					billingDetailsDto.setTaxId(orderDetailsMasterDto.getTaxId());
					billingDetailsDto.setTaxPer(orderDetailsMasterDto.getTaxPer()!=null ? Double.valueOf(orderDetailsMasterDto.getTaxPer().doubleValue()):0.00);
					billingDetailsDto.setTaxAmount(orderDetailsMasterDto.getTaxAmount()!=null ? Double.valueOf(orderDetailsMasterDto.getTaxAmount().doubleValue()):0.00);
					billingDetailsDto.setNetAmt(orderDetailsMasterDto.getNetAmount()!=null ? Double.valueOf(orderDetailsMasterDto.getNetAmount().doubleValue()):0.00);
					billingDetailsDto.setOrdDocId(orderDetailsMasterDto.getOrdDocId());
					billingDetailsDto.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId());
					billingDetailsDto.setSelfPayable(orderDetailsMasterDto.getSelfPayable()!=null ? Double.valueOf(orderDetailsMasterDto.getSelfPayable().doubleValue()):0.00);
					billingDetailsDto.setCreditPayable(orderDetailsMasterDto.getCreditPayable()!=null ? Double.valueOf(orderDetailsMasterDto.getCreditPayable().doubleValue()):0.00);
					billingDetailsDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
					billingDetailsDto.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
					billingDetailsDto.setPackageId(orderDetailsMasterDto.getPackageId());
					billingDetailsDto.setPackageConsumptionAmt(orderDetailsMasterDto.getPackageConsumptionAmt()!=null ? Double.valueOf(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()): Double.valueOf(0.00));
					billingDetailsDto.setCoSharePer(orderDetailsMasterDto.getCoPayPer()!=null ? Double.valueOf(orderDetailsMasterDto.getCoPayPer().doubleValue()): Double.valueOf(0.00));
					billingDetailsDto.setIsDoctorSharePosted('N');
					billingDetailsDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
					iBillingDao.saveBillingDetails(billingDetailsDto);
			
			if(orderDetailsIdList != null && orderDetailsIdList.size() > 0)
				iOrderDetailsMasterService.updateIsBilledStatus(orderDetailsIdList,vistDetailsObj.getBillId(),null);
			
			//Response billPayeeDtlsRes =	iBillingDao.getBillPayeeDetailsByBillId(vistDetailsObj.getBillId());
			//List<BillPayeeDetailsResDto> listBillPayeeDetailsResDto = billPayeeDtlsRes.getListObject();
		
			Integer adtId = vistDetailsObj.getAdmissionId()!=null ? vistDetailsObj.getAdmissionId():0;
			Integer encId = vistDetailsObj.getEncounterId()!=null ? vistDetailsObj.getEncounterId(): 0;
			Response sponserRes =  iPatientSponsorDetailsDao.getSponsoredDetailsForUpdatePayee(adtId,encId);
			int i = 0;
			for(OrderDetailsPayeeMapperDto detailsPayeeMapperDto : orderDetailsPayeeMapperDtosList)
			{
				i = iBillingDao.updatePayeeDetailsByPayeeAndBillId(detailsPayeeMapperDto,vistDetailsObj.getBillId(),billingMasterObj.getUnitId());
				if(i==0)
				{
					BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();	
					billPayeeDetailsDto.setBillId(vistDetailsObj.getBillId());
						billPayeeDetailsDto.setStatus('A');
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
					iBillingDao.saveBillPayeeDetails(billPayeeDetailsDto);
				}
			}
			
		    //save bill status logs
            BillingStatusMapperDto mapperDto = new BillingStatusMapperDto();
            BillingStatusMapperId mapperId = new BillingStatusMapperId();
	            mapperId.setBillId(vistDetailsObj.getBillId());
	  			mapperId.setBillStatusId(billStatusId);
	            mapperDto.setBillingStatusMapperId(mapperId);
	            mapperDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
	            mapperDto.setOrgnisationId(orderDetailsMasterDto.getOrgId());
	            mapperDto.setUnitId(orderDetailsMasterDto.getOrgUnitId());
	            mapperDto.setStatus('A');
	        iBillingDao.saveBillStatus(mapperDto);
			
	        return new Response(SUCCESS, null, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			logger.error("postServiceToEncounterBill failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public Response postServicesIntoExistingBill(List<Integer> orderDetailsIdsList) {
		try{
			Double taxAmount = 0.00;
			Double netAmount = 0.00;
			Double totalAmount = 0.00;
			Double selfPayable = 0.00;
			Double creditPayable = 0.00;
			Integer billId = null;
			AdmissionsForBatchProcessDto vistDetailsObj = null;
			//Map<Integer,Double> outstandingMap = new HashMap<Integer, Double>();
			for(Integer orderDetailsId : orderDetailsIdsList)
			{
				OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailsId);
				
				Response res2 = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(orderDetailsId,null);
				List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = res2.getListObject();
				
				Response packageRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsId);
				List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtoList = packageRes.getListObject();
				
				Response res = iOrderDetailsMasterDao.getVisitDetailsForRenderingService(orderDetailsId);
				vistDetailsObj = (AdmissionsForBatchProcessDto) res.getObject();
				
				Set<Integer> orderDetailsIdList = new LinkedHashSet<>();
				orderDetailsIdList.add(orderDetailsMasterDto.getOrderDetailsId());
						billId=vistDetailsObj.getBillId();
					BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
						billingDetailsDto.setBillingMasterId(vistDetailsObj.getBillId());
						billingDetailsDto.setServiceId(orderDetailsMasterDto.getServiceId());
						billingDetailsDto.setRate(orderDetailsMasterDto.getServiceAmt()!=null ? Double.valueOf(orderDetailsMasterDto.getServiceAmt().doubleValue()): 0.00);
						billingDetailsDto.setQuantity(orderDetailsMasterDto.getOrderQty().doubleValue());
						billingDetailsDto.setConcession(0.00);
						billingDetailsDto.setGrossAmount(orderDetailsMasterDto.getOrdTotalAmt()!=null ? Double.valueOf(orderDetailsMasterDto.getOrdTotalAmt().doubleValue()):0.00);
						billingDetailsDto.setCoPayPercentage(orderDetailsMasterDto.getCoPayPer() !=null ? Double.valueOf(orderDetailsMasterDto.getCoPayPer().doubleValue()) : Double.valueOf(0.00));
						billingDetailsDto.setUnitId(orderDetailsMasterDto.getOrgUnitId());
						billingDetailsDto.setOrganizationId(orderDetailsMasterDto.getOrgId());
						billingDetailsDto.setStatus('A');
						billingDetailsDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
						billingDetailsDto.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
						billingDetailsDto.setCreatedDate(orderDetailsMasterDto.getCreatedDate());
						billingDetailsDto.setUpdatedDate(orderDetailsMasterDto.getUpdatedDate());
						billingDetailsDto.setDiscount(orderDetailsMasterDto.getOrdDiscount()!=null ? Double.valueOf(orderDetailsMasterDto.getOrdDiscount().doubleValue()):0.00);
						billingDetailsDto.setTaxId(orderDetailsMasterDto.getTaxId());
						billingDetailsDto.setTaxPer(orderDetailsMasterDto.getTaxPer()!=null ? Double.valueOf(orderDetailsMasterDto.getTaxPer().doubleValue()):0.00);
						billingDetailsDto.setTaxAmount(orderDetailsMasterDto.getTaxAmount()!=null ? Double.valueOf(orderDetailsMasterDto.getTaxAmount().doubleValue()):0.00);
						billingDetailsDto.setNetAmt(orderDetailsMasterDto.getNetAmount()!=null ? Double.valueOf(orderDetailsMasterDto.getNetAmount().doubleValue()):0.00);
						billingDetailsDto.setOrdDocId(orderDetailsMasterDto.getOrdDocId());
						billingDetailsDto.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId());
						billingDetailsDto.setSelfPayable(orderDetailsMasterDto.getSelfPayable()!=null ? Double.valueOf(orderDetailsMasterDto.getSelfPayable().doubleValue()):0.00);
						billingDetailsDto.setCreditPayable(orderDetailsMasterDto.getCreditPayable()!=null ? Double.valueOf(orderDetailsMasterDto.getCreditPayable().doubleValue()):0.00);
						billingDetailsDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
						billingDetailsDto.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
						billingDetailsDto.setPackageId(orderDetailsMasterDto.getPackageId());
						billingDetailsDto.setPackageConsumptionAmt(orderDetailsMasterDto.getPackageConsumptionAmt()!=null ? Double.valueOf(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()): Double.valueOf(0.00));
						billingDetailsDto.setCoSharePer(orderDetailsMasterDto.getCoPayPer()!=null ? Double.valueOf(orderDetailsMasterDto.getCoPayPer().doubleValue()): Double.valueOf(0.00));
						billingDetailsDto.setIsDoctorSharePosted('N');
						billingDetailsDto.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);
						billingDetailsDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageMapperDtoList);
						
						if(orderDetailsMasterDto.isPackage() && vistDetailsObj.getVisitTypeId()==OP)
						{
							vistDetailsObj.setPackageId(orderDetailsMasterDto.getPackageId());
							Response ehcServicesRes = iOrderDetailsMasterDao.getEhcOrderedServices(vistDetailsObj);
							List<OrderDetailsByEncounterIdDto> orderDetailsList = ehcServicesRes.getListObject();
							if(orderDetailsList!=null && orderDetailsList.size()>0)
							{
								for (OrderDetailsByEncounterIdDto dto : orderDetailsList) 
								{
									BillingDetailsDto billDtilsEch = new BillingDetailsDto();
									billDtilsEch.setBillingMasterId(vistDetailsObj.getBillId());
									billDtilsEch.setServiceId(dto.getServiceMasterId());
									billDtilsEch.setRate(dto.getBasePrice().doubleValue());
									billDtilsEch.setQuantity(dto.getQuantity().doubleValue());
									billDtilsEch.setConcession(0.00);
									billDtilsEch.setGrossAmount(dto.getTotalAmt()!=null ? Double.valueOf(dto.getTotalAmt().doubleValue()):0.00);
									billDtilsEch.setCoPayPercentage(dto.getCoPayPer() !=null ? Double.valueOf(dto.getCoPayPer().doubleValue()) : Double.valueOf(0.00));
									billDtilsEch.setUnitId(orderDetailsMasterDto.getOrgUnitId());
									billDtilsEch.setOrganizationId(orderDetailsMasterDto.getOrgId());
									billDtilsEch.setStatus('A');
									billDtilsEch.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
									billDtilsEch.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
									billDtilsEch.setCreatedDate(orderDetailsMasterDto.getCreatedDate());
									billDtilsEch.setUpdatedDate(orderDetailsMasterDto.getUpdatedDate());
									billDtilsEch.setDiscount(dto.getDiscount()!=null ? Double.valueOf(dto.getDiscount().doubleValue()):0.00);
									billDtilsEch.setTaxId(dto.getTaxId());
									billDtilsEch.setTaxPer(dto.getTaxPercentage()!=null ? Double.valueOf(dto.getTaxPercentage().doubleValue()):0.00);
									billDtilsEch.setTaxAmount(dto.getTaxAmt()!=null ? Double.valueOf(dto.getTaxAmt().doubleValue()):0.00);
									billDtilsEch.setNetAmt(dto.getNetPay()!=null ? Double.valueOf(dto.getNetPay().doubleValue()):0.00);
									billDtilsEch.setOrdDocId(dto.getDoctorSpecialityId());
									billDtilsEch.setOrdDocSplId(dto.getServiceSpecialityId());
									billDtilsEch.setSelfPayable(dto.getSelfPayable()!=null ? Double.valueOf(dto.getSelfPayable().doubleValue()):0.00);
									billDtilsEch.setCreditPayable(dto.getCreditPayable()!=null ? Double.valueOf(dto.getCreditPayable().doubleValue()):0.00);
									billDtilsEch.setPayeeId(dto.getPayeeId());
									billDtilsEch.setOrderDetailsId(dto.getOrderDetailsId());
									billDtilsEch.setPackageId(dto.getPackageId());
									billDtilsEch.setPackageConsumptionAmt(dto.getPackageConsumptionAmt()!=null ? Double.valueOf(dto.getPackageConsumptionAmt().doubleValue()): Double.valueOf(0.00));
									billDtilsEch.setCoSharePer(dto.getCoPayPer()!=null ? Double.valueOf(dto.getCoPayPer().doubleValue()): Double.valueOf(0.00));
									billDtilsEch.setIsDoctorSharePosted('N');
									
									Response ehcPayeeRes = iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(dto.getOrderDetailsId(),null);
									List<OrderDetailsPayeeMapperDto> ehcorderDetailsPayeeMapperDtosList = ehcPayeeRes.getListObject();
									
									Response ehcPackageRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(dto.getOrderDetailsId());
									List<OrderDetailsPackageMapperDto> ehcorderDetailsPackageMapperDtoList = ehcPackageRes.getListObject();
									
									billDtilsEch.setOrderDetailsPayeeMapperDtosList(ehcorderDetailsPayeeMapperDtosList);
									billDtilsEch.setOrderDetailsPackageMapperDtosList(ehcorderDetailsPackageMapperDtoList);
									iBillingDao.saveBillingDetails(billDtilsEch);
									orderDetailsIdList.add(dto.getOrderDetailsId());
								}
							}
						}
						
					    iBillingDao.saveBillingDetails(billingDetailsDto);
					    
//					    for(OrderDetailsPackageMapperDto detailsPackageMapperDto : orderDetailsPackageMapperDtoList)
//					    {
//					    	if(detailsPackageMapperDto.getIsPackageService()=='N' && detailsPackageMapperDto.getStatus()=='A')
//					    	{
//					    		totalAmount = totalAmount + new Double(detailsPackageMapperDto.getServiceAmount().doubleValue());
//					    	}
//					    }
					    
					    if(orderDetailsMasterDto.getPackageConsumptionAmt()!=null && orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()>0)
					    {
					    	OrderDetailsPackageMapperDto detailsPackageMapperDto = orderDetailsPackageMapperDtoList.stream()
									.filter(obj ->obj.getIsPackageService()=='N' && obj.getStatus()=='A')
									.findAny().orElse(null);
						    
						    if(detailsPackageMapperDto!=null)
						    {
						    	totalAmount = totalAmount + Double.valueOf(detailsPackageMapperDto.getServiceAmount().doubleValue() * orderDetailsMasterDto.getOrderQty().doubleValue());
						    }
					    }else{
					    	totalAmount = totalAmount + Double.valueOf(orderDetailsMasterDto.getOrdTotalAmt().doubleValue());
					    }
						taxAmount = taxAmount + Double.valueOf(orderDetailsMasterDto.getTaxAmount().doubleValue());
						netAmount = netAmount + Double.valueOf(orderDetailsMasterDto.getNetAmount().doubleValue());
						selfPayable = selfPayable + Double.valueOf(orderDetailsMasterDto.getSelfPayable().doubleValue());
						creditPayable = creditPayable + Double.valueOf(orderDetailsMasterDto.getCreditPayable().doubleValue());
						
				if(orderDetailsIdList != null && orderDetailsIdList.size() > 0)
					iOrderDetailsMasterService.updateIsBilledStatus(orderDetailsIdList,vistDetailsObj.getBillId(),null);
				
				Integer adtId = vistDetailsObj.getAdmissionId()!=null ? vistDetailsObj.getAdmissionId():0;
				Integer encId = vistDetailsObj.getEncounterId()!=null ? vistDetailsObj.getEncounterId(): 0;
				Response sponserRes =  iPatientSponsorDetailsDao.getSponsoredDetailsForUpdatePayee(adtId,encId);
				int i = 0;
				for(OrderDetailsPayeeMapperDto detailsPayeeMapperDto : orderDetailsPayeeMapperDtosList)
				{
					i = iBillingDao.updatePayeeDetailsByPayeeAndBillId(detailsPayeeMapperDto,vistDetailsObj.getBillId(),vistDetailsObj.getUnitId());
					if(i==0)
					{
						
						Response payeeRes = iBillingDao.getPayeeDetailsByPayeeAndBillId(vistDetailsObj.getBillId());
						
						if(payeeRes != null && payeeRes.getListObject()!= null && payeeRes.getListObject().size()>0) 
						{
							List<BillPayeeDetailsDto> reslist = payeeRes.getListObject();
							for (BillPayeeDetailsDto billPayeeDetails : reslist) 
							{
								if(sponserRes!=null && sponserRes.getListObject()!=null && sponserRes.getListObject().size()>0) 
								{
	                                List<PatientSponsorDetailsDto> sponserList = sponserRes.getListObject();
	                                PatientSponsorDetailsDto sponsorStramObj = null;
	                                if(billPayeeDetails.getAssociateCompanyId()!=null){
	                                    sponsorStramObj = sponserList.stream()
	                                            .filter(sponsor -> sponsor.getPayeeId().equals(billPayeeDetails.getPayeeId()) && (billPayeeDetails.getAssociateCompanyId()!=null && billPayeeDetails.getAssociateCompanyId().equals(sponsor.getInsuranceId())) )
	                                            .findAny().orElse(null);
	                                }else{
	                                    sponsorStramObj = sponserList.stream()
	                                            .filter(sponsor -> sponsor.getPayeeId().equals(billPayeeDetails.getPayeeId()))
	                                            .findAny().orElse(null);
	                                }
	                                
	                                if(sponsorStramObj!=null)
	                                {
	                                	billPayeeDetails.setPriorityId(sponsorStramObj.getPriorityId()!=null ? sponsorStramObj.getPriorityId():null);
	                                	billPayeeDetails.setPaymentEntitlementId(sponsorStramObj.getPaymentEntitlementId());
	                                }

								}
								iBillingDao.updatePayeePriority(billPayeeDetails);
							}
							
						}
						
						BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();	
							billPayeeDetailsDto.setBillId(vistDetailsObj.getBillId());
							billPayeeDetailsDto.setStatus('A');
							billPayeeDetailsDto.setPayeeId(detailsPayeeMapperDto.getPayeeId());
							billPayeeDetailsDto.setAssociateCompanyId(detailsPayeeMapperDto.getAssociateCompanyId());
							billPayeeDetailsDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
							billPayeeDetailsDto.setUpdatedBy(orderDetailsMasterDto.getCreatedBy());
							billPayeeDetailsDto.setCreatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
							billPayeeDetailsDto.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
							billPayeeDetailsDto.setBillAmount(detailsPayeeMapperDto.getPayeeAmount().doubleValue());
							billPayeeDetailsDto.setDueAmount(detailsPayeeMapperDto.getPayeeAmount().doubleValue());
							billPayeeDetailsDto.setOrgId(orderDetailsMasterDto.getOrgId());
							billPayeeDetailsDto.setUnitId(orderDetailsMasterDto.getOrgUnitId());
							
							if(sponserRes!=null && sponserRes.getListObject()!=null && sponserRes.getListObject().size()>0) 
							{
                                List<PatientSponsorDetailsDto> sponserList = sponserRes.getListObject();
                                PatientSponsorDetailsDto sponsorStramObj = null;
                                if(billPayeeDetailsDto.getAssociateCompanyId()!=null){
                                    sponsorStramObj = sponserList.stream()
                                            .filter(sponsor -> sponsor.getPayeeId().equals(billPayeeDetailsDto.getPayeeId()) && (billPayeeDetailsDto.getAssociateCompanyId()!=null && billPayeeDetailsDto.getAssociateCompanyId().equals(sponsor.getInsuranceId())) )
                                            .findAny().orElse(null);
                                }else{
                                    sponsorStramObj = sponserList.stream()
                                            .filter(sponsor -> sponsor.getPayeeId().equals(billPayeeDetailsDto.getPayeeId()))
                                            .findAny().orElse(null);
                                }
                                
                                if(sponsorStramObj!=null)
                                {
                                    billPayeeDetailsDto.setPriorityId(sponsorStramObj.getPriorityId()!=null ? sponsorStramObj.getPriorityId():null);
                                    billPayeeDetailsDto.setPaymentEntitlementId(sponsorStramObj.getPaymentEntitlementId());
                                }

							}
						iBillingDao.saveBillPayeeDetails(billPayeeDetailsDto);
						
						
					}
						
					//outstandingMap.put(detailsPayeeMapperDto.getPayeeId(), outstandingMap.get(detailsPayeeMapperDto.getPayeeId())+detailsPayeeMapperDto.getPayeeAmount().doubleValue());
				}
				//pharmacy bill consolidated
				List<Integer> orderDetailsIds = new ArrayList<>();
				orderDetailsIds.add(orderDetailsId);
				WalkInSalesDao.updateConsolidatedBillStatusBySaleDetails(orderDetailsIds,billId);
			}
			BillingMaster billingMasterObj =iBillingDao.getBillMasterObject(billId);
			billingMasterObj.setNetAmount(billingMasterObj.getNetAmount()+netAmount);
			billingMasterObj.setGst((billingMasterObj.getGst()!=null?billingMasterObj.getGst():0.00)+taxAmount);
			billingMasterObj.setTotalBillAmount(billingMasterObj.getTotalBillAmount()+totalAmount);
			billingMasterObj.setOutstanding(billingMasterObj.getOutstanding()+netAmount);
			billingMasterObj.setSelfPayable(billingMasterObj.getSelfPayable()+selfPayable);
			billingMasterObj.setCreditPayable(billingMasterObj.getCreditPayable()+creditPayable);
			
			if(billingMasterObj.getOutstanding().intValue()==0)
				billingMasterObj.setBillStatusId(3);
  			else if(billingMasterObj.getSelfPayable()>0 && billingMasterObj.getOutstanding().equals(billingMasterObj.getNetAmount()))
  				billingMasterObj.setBillStatusId(1);
  			else
  				billingMasterObj.setBillStatusId(2);
				
			iBillingDao.updateBillMaster(billingMasterObj);
				
	        return new Response(SUCCESS, null, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			logger.error("postServicesIntoExistingBill failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelPendingService(CancelOrderDto cancelOrderDto) {
		try 
		{
			OrderDetailsMaster orderDetailsMaster = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(cancelOrderDto.getOrderDetailsId());
			
			if((orderDetailsMaster.getIsPostedWhileDischarge()==null || orderDetailsMaster.getIsPostedWhileDischarge()=='N') && orderDetailsMaster.getServiceRendered()==1)
			{
				return new Response(ERROR, ALREADY_EXIST_CODE , "Service is rendered by another user...!!", null, null);
			}else if(orderDetailsMaster.getOrdCancelled()=='Y')
			{
				return new Response(ERROR, ALREADY_EXIST_CODE, "Service is already cancelled by another user...!!", null, null);
			}else {
			
				if(cancelOrderDto.getEncounterId()==null && cancelOrderDto.getAdmissionId()==null)
				{
					iOrderDetailsMasterDao.cancelUnRenderedServices(cancelOrderDto);
				}else 
				{
					iOrderDetailsMasterDao.cancelUnRenderedServices(cancelOrderDto);
					
					ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto.setEncounterId(cancelOrderDto.getEncounterId());
						consumptionDto.setAdmissionId(cancelOrderDto.getAdmissionId());
						consumptionDto.setSpecialityId(cancelOrderDto.getSpecialityId());
						consumptionDto.setServiceId(orderDetailsMaster.getServiceId());
					
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
					iContractConsumptionService.addCapAmount(consumptionDto);
				}
				
				if(cancelOrderDto.getSaleId()!=null && cancelOrderDto.getSaleId()>0)
				{
					cancelOrderDto.setScreenId(9);//static screen id
					iWalkInSalesService.revertAllocatedQty(cancelOrderDto.getSaleId().longValue(), cancelOrderDto.getScreenId(),orderDetailsMaster.getOrgUnitId(),null,null,null);
				}
				return new Response(SUCCESS, null, "Order Cancelled !!", null, null);
			}
			
		}catch (Exception e)
		{
			logger.error("cancelPendingService failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public AdmissionsForBatchProcessDto getBillByEncounterId(Integer encounterId) {
		try
		{
			return iBillingDao.getBillByEncounterId(encounterId);
		}catch (Exception e)
		{
			logger.error("getBillByEncounterId failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public AdmissionsForBatchProcessDto getBillDataByAdmissionId(Integer admissionId) {
		try
		{
			return iBillingDao.getBillDataByAdmissionId(admissionId);
		}catch (Exception e)
		{
			logger.error("getBillDataByAdmissionId failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveBillEstimate(CostEstimateDto costEstimationDto) {
		try 
		{
			PatientRegistrationDto dto = new PatientRegistrationDto();
			if (costEstimationDto.getPatientId() == null) {

				String uhidNo = iGlobalUnitMasterDao.getGeneratedGlobalNo(0, costEstimationDto.getOrgId(),
						costEstimationDto.getUnitId(), generateUhidConfig(costEstimationDto.getRegistrationTypeId()));
				if (uhidNo != null && !uhidNo.isEmpty())
					costEstimationDto.setUhidNumber(uhidNo);

				dto.setFirstName(costEstimationDto.getPatientName());
				dto.setMiddleName("");
				dto.setLastName("");
				dto.setEmail(costEstimationDto.getMail());
				if (costEstimationDto.getPatientGender() != null) {
					dto.setGenderId(costEstimationDto.getPatientGender());
				}
				if (costEstimationDto.getPrefixId() != null) {
					dto.setPrefixId(costEstimationDto.getPrefixId());
				}
				if (costEstimationDto.getPatientAddress() != null) {
					dto.setAddress(costEstimationDto.getPatientAddress());
				}

				if (costEstimationDto.getMobileNumber() != null) {
					dto.setMobileNumber(costEstimationDto.getMobileNumber());
				}

				dto.setOrganizationId(costEstimationDto.getOrgId());
				dto.setUnitId(costEstimationDto.getUnitId());
				dto.setRegistrationTypeId(4);// for walkin
				dto.setStatus('A');
				dto.setIsBlacklist('N');
				dto.setIsPrimary('Y');
				dto.setUhidNumber(costEstimationDto.getUhidNumber());
				Response res1 = iPatientRegistrationDao.savePatientRegistration(dto);
				if (res1.getStatus().equals(SUCCESS)) {
					PatientRegistrationDto pRdto = (PatientRegistrationDto) res1.getObject();
					dto.setPatientId(pRdto.getPatientId());
				    costEstimationDto.setPatientId(dto.getPatientId());
					Response patientResponse = iPatientRegistrationDao.savePatientDetails(dto);
				}

				TempPatientRegistration tempPatient = new TempPatientRegistration();
				tempPatient.setPatientName(costEstimationDto.getPatientName());
				tempPatient.setAddress(costEstimationDto.getPatientAddress());
				tempPatient.setMobileNumber(costEstimationDto.getMobileNumber());
				tempPatient.setTempUhid(costEstimationDto.getUhidNumber());
				tempPatient.setGender(
						costEstimationDto.getPatientGender() != null ? new Gender(costEstimationDto.getPatientGender())
								: null);
				tempPatient.setPrefix(
						costEstimationDto.getPrefixId() != null ? new Prefix(costEstimationDto.getPrefixId()) : null);
				tempPatient.setOrganizationId(costEstimationDto.getOrgId());
				tempPatient.setUnitId(costEstimationDto.getUnitId());
				tempPatient.setUpdatedByUser(costEstimationDto.getUpdatedBy());
				tempPatient.setAddedByUser(costEstimationDto.getCreatedBy());

				TempPatientRegistration tempResponse = iBillingDao.savePatient(tempPatient);
				dto.setTempPatientId(tempResponse.getId());

			}
			Response res = iBillingDao.saveEstimate(costEstimationDto);
			costEstimationDto.setCostEstimateId((Integer) res.getObject());
			if (null == costEstimationDto.getPatientId()) {
				costEstimationDto.setPatientId(dto.getPatientId());
			}
			iBillingDao.saveEstimateDetails(costEstimationDto);

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, (Integer) res.getObject());
			// return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,
			// costEstimationDto);
		}catch (Exception e){
			logger.error("saveBillEstimate failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response convertServicesFromOPToIP(ConvertServicesFromOptoIpDto convertServicesFromOptoIpDto) {
		try{
			List<Integer> orderMasterIds = new LinkedList<>();
			
			OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setVisitTypeId(convertServicesFromOptoIpDto.getVisitTypeId());// op==1
				orderMasterDto.setPatientId(convertServicesFromOptoIpDto.getPatientId());
				orderMasterDto.setAdmissionId(convertServicesFromOptoIpDto.getAdmissionId());
				orderMasterDto.setCreatedBy(convertServicesFromOptoIpDto.getUserId());
				orderMasterDto.setUpdatedBy(convertServicesFromOptoIpDto.getUserId());
				orderMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(convertServicesFromOptoIpDto.getUnitId()).getTime());
				orderMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(convertServicesFromOptoIpDto.getUnitId()).getTime());
				orderMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(convertServicesFromOptoIpDto.getUnitId()).getTime());
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setOrderNo(iGlobalUnitMasterDao.getGeneratedNo(0, convertServicesFromOptoIpDto.getUnitId(), IBillingCommonConstants.ORDER_NUMBER));
				orderMasterDto.setOrgId(convertServicesFromOptoIpDto.getOrganizationId());
				orderMasterDto.setOrgUnitId(convertServicesFromOptoIpDto.getUnitId());
				orderMasterDto.setOrderStatusId(1);// order status==1
			Response res = iOrderMasterDao.saveOrderMaster(orderMasterDto);
			Integer orderId = (Integer)res.getObject();
			
			AdmissionListIpdChargesResDto admissionDto = iBillingDao.getAdmissionsById(convertServicesFromOptoIpDto.getAdmissionId());
			
			for(Integer orderDetailId : convertServicesFromOptoIpDto.getOrderDetailsIds())
			{
					OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailId);
				
						orderDetailsMasterDto.setUpdatedBy(convertServicesFromOptoIpDto.getUserId());
						orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime());
						orderDetailsMasterDto.setOrderId(orderId);
					
					
						ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
							consumptionDto.setOrganizationId(orderDetailsMasterDto.getOrgId());
							consumptionDto.setUnitId(orderDetailsMasterDto.getOrgUnitId());
							consumptionDto.setVisitTypeId(convertServicesFromOptoIpDto.getVisitTypeId());
							consumptionDto.setPatientTypeId(admissionDto.getPatientCategoryId());
							consumptionDto.setBillingBedCategoryId(admissionDto.getBedBillingCategoryId());
							consumptionDto.setPaymentEntitlementId(SELF);
							consumptionDto.setServiceId(orderDetailsMasterDto.getServiceId());
							consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(orderDetailsMasterDto.getOrderDate(),"yyyy-MM-dd"));
							consumptionDto.setContractId(admissionDto.getContractId());
							consumptionDto.setAdmissionId(admissionDto.getAdmissionId());
							consumptionDto.setDefaultSelfTariffId(admissionDto.getDefaultSelfTariffId());
							consumptionDto.setPatientId(admissionDto.getPatientId());
							consumptionDto.setSelfPayeeId(admissionDto.getSelfPayeeId());
						    consumptionDto.setPatientClassId(admissionDto.getPatientClassId());	
						    
					    res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
							consumptionDto=(ContractConsumptionDto)res.getObject();
					
						if(res.getObject()!=null)
						{
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setTariffId(admissionDto.getDefaultSelfTariffId());
							orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							orderDetailsMasterDto.setServiceRendered(0);
							orderDetailsMasterDto.setServiceChargeable(1);
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
							orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
							orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
							orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
						}
					
						orderMasterIds.add(orderDetailsMasterDto.getOrderId());
				
					iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto,
							consumptionDto.getOrderDetailsPayeeMapperDtosList(),
							consumptionDto.getOrderDetailsPackageMapperDtosList());
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e){
			logger.error("convertServicesFromOPToIP failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateLabSampleStatus(Integer labSampleDetailsId) {
		try
		{

			return iBillingDao.updateLabSampleStatus(labSampleDetailsId);

		}catch (Exception e){
			logger.error("updateLabSampleStatus failed", e);
			throw new BillingException(e.getMessage());
		}
	}

	private int generateUhidConfig(int registrationTypeId) throws ApplicationException {
		Integer screenId = 0;
		try {
			
			if(registrationTypeId==5)
				screenId = PRE_REGESTRATION_UHID;
			
			if(registrationTypeId==2)
				screenId = OTC_PATIENT_UHID;
			
			if(registrationTypeId==4)
			screenId = EMERGENCY_PATIENT_UHID;


			} catch (Exception e) {
				logger.error("generateUhidConfig failed", e);
				throw new BillingException(e.getMessage());
			}

		return screenId;
	}

	@Override
	@Transactional
	public Response getPackageConsumeAmountItemwise(Long id, Integer orderDetailsId) {
		try {
			return iBillingDao.getPackageConsumeAmountItemwise(id,orderDetailsId);
		} catch (Exception e) {
			
			throw new BillingException(e.getMessage());
		}
	}

	
	
}